package com.jonlin.apiclient.sdk;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.jonlin.apiclient.model.User;
import com.jonlin.apiclient.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;



/**
 * 调用第三方接口的客户端
 * 一般来说一个接口对应一个客户端（SDK）
 *
 * 我觉得这里的sdk 应该提供更多功能 比如校验接口是否可用等等
 * 每个接口的参数和返回值并不一定相同，因此校验的方法也不同，这也是为什么最好一个接口对应一个sdk
 */
public class GetUserNameApiClient {

    private String accessKey;

    private String secretKey;

    // 通过网关发送请求 让网关发挥功能
    private static final String GATEWAY_HOST = "http://127.0.0.1:8090";


    public GetUserNameApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    /**
     * 封装请求头参数
     * @param body
     * @return
     */
    private Map<String, String> setHeader(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 不能直接发送 secretKey
        // hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(body, secretKey));
        return hashMap;
    }

    /**
     * 利用SDK发送请求
     * @param user
     * @return
     */
    public String getUserNameByPost(User user) {
        String userJson = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(setHeader(userJson))
                .body(userJson)
                .execute();
        String result = httpResponse.body();
        return result;
    }

}
