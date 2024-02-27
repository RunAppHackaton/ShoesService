package com.runapp.shoesservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.RedisTemplate;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
public class ShoesServiceApplication {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public static void main(String[] args) {
        SpringApplication.run(ShoesServiceApplication.class, args);
    }
}
