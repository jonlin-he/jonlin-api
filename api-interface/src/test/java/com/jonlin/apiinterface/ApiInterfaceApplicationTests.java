package com.jonlin.apiinterface;

import com.jonlin.apiclient.model.User;
import com.jonlin.apiclient.sdk.GetUserNameApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    GetUserNameApiClient apiClient;

    /**
     * 使用 api-client 的 sdk 调用
     */
    @Test
    void contextLoads() {

    }

}
