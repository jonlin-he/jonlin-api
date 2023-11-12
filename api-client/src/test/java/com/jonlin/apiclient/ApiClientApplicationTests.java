package com.jonlin.apiclient;

import com.jonlin.apiclient.model.User;
import com.jonlin.apiclient.sdk.GetUserNameApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiClientApplicationTests {

    @Test
    void contextLoads() {
        GetUserNameApiClient apiClient = new GetUserNameApiClient("jonlin", "abcdefgh");
        User user = new User();
        user.setUsername("jonlin");
        apiClient.getUserNameByPost(user);
    }

}
