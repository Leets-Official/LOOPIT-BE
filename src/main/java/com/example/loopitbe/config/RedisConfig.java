package com.example.loopitbe.config;

import com.example.loopitbe.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port
    ) {
        return new LettuceConnectionFactory(host, port);
    }

    // Cache 저장
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    // Pub/Sub 리스너 컨테이너
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapterChat,
            MessageListenerAdapter listenerAdapterRead
    ) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 채팅 메시지 구독
        container.addMessageListener(listenerAdapterChat,
                new ChannelTopic("chat:message"));

        // 읽음 처리 구독
        container.addMessageListener(listenerAdapterRead,
                new ChannelTopic("chat:read"));

        return container;
    }

    // 채팅 메시지용 어댑터
    @Bean
    public MessageListenerAdapter listenerAdapterChat(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    // 읽음 처리용 어댑터
    @Bean
    public MessageListenerAdapter listenerAdapterRead(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendReadMessage");
    }
}

