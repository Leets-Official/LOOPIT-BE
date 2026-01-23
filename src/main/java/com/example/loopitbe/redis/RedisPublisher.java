package com.example.loopitbe.redis;

import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // JSON 변환기

    public RedisPublisher(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(ChannelTopic topic, Object data) {
        try {
            // 자바 객체(Object) -> JSON 문자열(String) 변환
            String jsonMessage = objectMapper.writeValueAsString(data);

            // Redis로 전송
            redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.REDIS_PARSE_ERROR);
        }
    }
}
