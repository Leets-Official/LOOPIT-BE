package com.example.loopitbe.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber {

    public void onMessage(String message, String channel) {
        System.out.println("=== Redis Pub/Sub 수신 ===");
        System.out.println("채널: " + channel);
        System.out.println("메시지: " + message);
    }
}