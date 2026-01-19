package com.example.loopitbe.controller;

import com.example.loopitbe.redis.RedisPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RedisTestController {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisPublisher redisPublisher;

    public RedisTestController(
            RedisTemplate<String, String> redisTemplate,
            RedisPublisher redisPublisher
    ) {
        this.redisTemplate = redisTemplate;
        this.redisPublisher = redisPublisher;
    }

    // 캐시 저장
    @GetMapping("/cache/set")
    public String setCache() {
        redisTemplate.opsForValue().set("test:key", "hello redis");
        return "cache saved";
    }

    // 캐시 조회
    @GetMapping("/cache/get")
    public String getCache() {
        return redisTemplate.opsForValue().get("test:key");
    }

    // Pub/Sub 발행
    @GetMapping("/publish")
    public String publish() {
        redisPublisher.publish("test:channel", "hello pubsub");
        return "published";
    }
}

