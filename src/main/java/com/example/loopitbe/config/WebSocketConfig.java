package com.example.loopitbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP annotation
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. 소켓 연결 엔드포인트 설정
        registry.addEndpoint("/ws-chat") // 연결 URL: ws://도메인:8080/ws-chat
                .setAllowedOriginPatterns("*") // 모든 도메인에서 접속 허용 (CORS)
                .withSockJS(); // 브라우저가 websocket을 지원하지 않을 경우 폴백 옵션 활성화
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. 메시지 구독 요청
        // 클라이언트가 메시지를 받을 때 사용 (서버 -> 클라이언트)
        registry.enableSimpleBroker("/sub");

        // 3. 메시지 발행 요청
        // 클라이언트가 메시지를 보낼 때 사용 (클라이언트 -> 서버)
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
