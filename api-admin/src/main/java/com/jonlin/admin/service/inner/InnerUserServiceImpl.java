package com.jonlin.admin.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jonlin.admin.exception.BusinessException;
import com.jonlin.admin.service.UserService;
import com.jonlin.apicommon.model.entity.User;
import com.jonlin.apicommon.model.enums.ErrorCode;
import com.jonlin.apicommon.model.service.inner.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserService userService;

    @Override
    public User getInvokeUserByAccessKey(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccessKey, accessKey);
        User user = userService.getOne(userLambdaQueryWrapper);
        return user;
    }
}
