package com.jonlin.apigateway.filter;

import com.jonlin.apiclient.utils.SignUtils;
import com.jonlin.apicommon.model.entity.InterfaceInfo;
import com.jonlin.apicommon.model.entity.User;
import com.jonlin.apicommon.model.enums.ErrorCode;
import com.jonlin.apicommon.model.enums.InterfaceStatusEnum;
import com.jonlin.apicommon.model.service.inner.InnerInterfaceInfoService;
import com.jonlin.apicommon.model.service.inner.InnerUserInterfaceInvokeService;
import com.jonlin.apicommon.model.service.inner.InnerUserService;
import com.jonlin.apigateway.exception.BusinessException;
import com.jonlin.apigateway.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.jonlin.apicommon.model.enums.UserAccountStatusEnum.BAN;


@Component
@Slf4j
public class GatewayGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 请求白名单
     */
    private final static List<String> WHITE_HOST_LIST = Arrays.asList("127.0.0.1");


    /**
     * 五分钟过期时间
     */
    private static final long FIVE_MINUTES = 5L * 60;
    @Resource
    private RedissonLockUtil redissonLockUtil;
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInvokeService interfaceInvokeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一id：" + request.getId());
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求方法：" + request.getMethod());
        log.info("请求路径：" + request.getPath());
        log.info("网关本地地址：" + request.getLocalAddress());
        log.info("请求远程地址：" + request.getRemoteAddress());
        log.info("url:" + request.getURI());
        return verifyParameters(exchange, chain);
    }

    /**
     * 验证参数
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
    private Mono<Void> verifyParameters(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求白名单
        if (!WHITE_HOST_LIST.contains(Objects.requireNonNull(request.getRemoteAddress()).getHostString())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }

        HttpHeaders headers = request.getHeaders();
        String body = headers.getFirst("body");
        String accessKey = headers.getFirst("accessKey");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        // 请求头中参数必须完整
        if (StringUtils.isAnyBlank(body, sign, accessKey, timestamp)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 防重发XHR
        long currentTime = System.currentTimeMillis() / 1000;
        assert timestamp != null;
        if (currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "会话已过期,请重试！");
        }
        try {
            User user = innerUserService.getInvokeUserByAccessKey(accessKey);
            if (user == null) {
                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "账号不存在");
            }
            if (user.getStatus().equals(BAN.getValue())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该账号已封禁");
            }
            // 校验accessKey
            if (!user.getAccessKey().equals(accessKey)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请先获取请求密钥");
            }
            // 校验签名
            if (!SignUtils.genSign(body, user.getSecretKey()).equals(sign)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "非法请求");
            }
            if (user.getBalance() <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "余额不足，请先充值。");
            }
            // 校验url和请求方法
            String method = Objects.requireNonNull(request.getMethod()).toString();
            String uri = request.getURI().toString().trim();
            if (StringUtils.isAnyBlank(uri, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            // 通过uri和请求方法判断查询接口是否存在
            InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(uri, method);
            if (interfaceInfo == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
            }
            if (interfaceInfo.getStatus() == InterfaceStatusEnum.AUDITING.getValue()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口审核中");
            }
            if (interfaceInfo.getStatus() == InterfaceStatusEnum.OFFLINE.getValue()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未开启");
            }
            // 请求转发，调用模拟接口 + 响应日志
            return handleResponse(exchange, chain, user, interfaceInfo);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
        }
    }

    /**
     * 处理响应
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, User user, InterfaceInfo interfaceInfo) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        // 缓存数据的工厂
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        // 拿到响应码
        HttpStatus statusCode = originalResponse.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            // 装饰，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 对response增强，等调用完转发的接口，结果返回之前才会执行
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 往返回值里写数据
                        return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    // 扣除积分
                                    redissonLockUtil.redissonDistributedLocks(("gateway_" + user.getUserAccount()).intern(), () -> {
                                        boolean invoke = interfaceInvokeService.invokeHandler(interfaceInfo.getId(), user.getId(), interfaceInfo.getNeedScore());
                                        if (!invoke) {
                                            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
                                        }
                                    }, "接口调用失败");
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    // 释放掉内存
                                    DataBufferUtils.release(dataBuffer);
                                    String data = new String(content, StandardCharsets.UTF_8);
                                    // 打印日志
                                    log.info("响应结果：" + data);
                                    return bufferFactory.wrap(content);
                                }));
                    } else {
                        // 8. 调用失败，返回一个规范的错误码
                        log.error("<--- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 设置 response 对象为装饰过的
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        // 降级处理返回数据
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}