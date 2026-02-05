package com.example.loopitbe.config.handler;

import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.jwt.JwtProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;

    public StompHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            // 1. 헤더에서 Authorization 추출
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new CustomException(ErrorCode.EMPTY_JWT);
            }

            String token = authorizationHeader.substring(7);

            try {
                // 2. 토큰 검증
                jwtProvider.validateToken(token);

                // 3. 유저 ID 추출 및 세션 저장
                Long userId = jwtProvider.getUserId(token);

                if (userId != null) {
                    // Principal 설정을 위한 인증 객체 생성
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId, // principal.getName()
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    // accessor에 유저 정보 등록
                    accessor.setUser(authentication);

                    Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
                    // 만약 attributes가 null이면 새로 만들어서 넣어줌
                    if (sessionAttributes != null) {
                        sessionAttributes.put("userId", userId);
                    }
                }
            } catch (Exception e) {
                System.out.println("검증 과정 중 예외 발생: " + e.getMessage());
                throw e;
            }
        }

        return message;
    }
}
