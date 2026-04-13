package com.ra.base_spring_boot.config.security;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @PostConstruct
    public void startRedis() {
        try {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
            System.out.println(">>> [Keep Up] Embedded Redis đã khởi chạy tại cổng: " + redisPort);
        } catch (Exception e) {
            System.err.println(">>> Lỗi khi khởi chạy Embedded Redis: " + e.getMessage());
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            System.out.println(">>> [Keep Up] Embedded Redis đã dừng.");
        }
    }
}
