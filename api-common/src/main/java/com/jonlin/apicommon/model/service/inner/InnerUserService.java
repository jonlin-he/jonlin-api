package com.jonlin.apicommon.model.service.inner;


import com.jonlin.apicommon.model.entity.User;

public interface InnerUserService {

    /**
     * 通过访问密钥获取invoke用户
     * 按凭证获取invoke用户
     *
     * @param accessKey 访问密钥
     * @return {@link User}
     */
    User getInvokeUserByAccessKey(String accessKey);
}
