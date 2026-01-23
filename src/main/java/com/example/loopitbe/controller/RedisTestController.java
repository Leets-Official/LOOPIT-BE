package com.example.loopitbe.controller;

import com.example.loopitbe.redis.RedisPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
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
        System.out.println(">>> /test/cache/set 호출됨");
        redisTemplate.opsForValue().set("test:key", "hello redis");
        return "cache saved";
    }

    // 캐시 조회
    @GetMapping("/cache/get")
    public String getCache() {
        System.out.println(">>> /test/cache/get 호출됨");
        return redisTemplate.opsForValue().get("test:key");
    }

    // Pub/Sub 발행
    @GetMapping("/publish")
    public String publish() {
        System.out.println(">>> /test/publish 호출됨");

        redisPublisher.publish(new ChannelTopic("test:channel"), "hello pubsub");

        return "published";
    }
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}