package com.example.loopitbe.service;

import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthService {
    @Value("${kakao.auth.client}")
    private String client;
    @Value("${kakao.auth.redirectLogin}")
    private String redirectLogin;

    private final ObjectMapper objectMapper;

    public KakaoOAuthService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 카카오 OAuth Access 토큰 가져오기
    public String getToken(String accessCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirectLogin);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        String accessToken = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            accessToken = jsonNode.get("access_token").asText();;
        }
        catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }
        return accessToken;
    }

    public String getKakaoId(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization","Bearer "+ token);

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity <>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class);

        String kakaoId = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 카카오 유저 id 추출
            kakaoId = jsonNode.get("id").asText();
        }
        catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }
        return kakaoId;
    }
}
