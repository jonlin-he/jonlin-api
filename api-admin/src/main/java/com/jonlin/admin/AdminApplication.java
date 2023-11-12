package com.jonlin.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

// todo 如需开启 Redis，须移除 exclude 中的内容
// todo mapper 扫描地址要确认正确
//@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.jonlin.admin.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
public class AdminApplication {


    public static void main(String[] args) {
        System.setProperty("dubbo.cache.enabled", "false");
        System.setProperty("dubbo.cache.file", "false");
        System.setProperty("dubbo.mapping.cache.file", "false");
        SpringApplication.run(AdminApplication.class, args);
    }

}
