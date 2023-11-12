package com.jonlin.apiclient.config;

import com.jonlin.apiclient.sdk.GetUserNameApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 从配置文件中读取，在那个项目运行这个类 就读取那个项目的配置文件
@ConfigurationProperties("sdk.client")
@Configuration
@Data
@ComponentScan
public class ApiClientConfig {

    private String accessKey;

    private String secretKey;

    /**
     * 自动创建 ApiClient
     * @return
     */
    @Bean
    public GetUserNameApiClient GetUserNameApiClient() {
        return new GetUserNameApiClient(accessKey, secretKey);
    }

}