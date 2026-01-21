package com.example.loopitbe.service;

import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.exception.RateLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ChatBotService {
    private final WebClient webClient;
    private final StringRedisTemplate redisTemplate;
    private static final String HISTORY_KEY = "user:history:";
    private final String API_KEY = "AIzaSyCOE0ulson7G36-cbgl_RLWnT2qCVPXdIo";

    public ChatBotService(WebClient.Builder webClientBuilder, StringRedisTemplate redisTemplate) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.redisTemplate = redisTemplate;
    }

    // 질문 개수 초과 확인
    public void checkAvailability(String userId) {
        String key = HISTORY_KEY + userId;

        Long currentSize = redisTemplate.opsForList().size(key);

        if (currentSize != null && currentSize >= 10) {
            Long expire = redisTemplate.getExpire(HISTORY_KEY + userId, TimeUnit.HOURS);
            throw new RateLimitException(expire);
        }
    }

    public String getRepairEstimate(String userId, String userMessage) {
        // Redis에서 이전 대화 내역 가져오기
        List<String> history = getChatHistory(userId);

        // 프롬프트 적용
        StringBuilder sb = new StringBuilder();
        String questionPrompt = """
                너는 삼성전자와 애플 제품의 수리비를 실시간으로 검색해서 안내하는 '모바일 수리 가이드'야.
                반드시 제공된 'Google Search' 결과를 바탕으로 답변해.
                
                [답변 가이드]
                1. 브랜드 식별: 사용자의 기기가 삼성인지 애플인지 먼저 파악해.
                2. 최신성 유지: 검색 결과 중 가장 최신 날짜(2025-2026년)의 정보를 우선해.
                3. 비교 안내:
                   - 삼성: 액정 반납가와 삼성 케어 플러스(삼케플) 가격을 구분.
                   - 애플: 일반 수리비와 AppleCare+(애케플) 가격을 구분.
                4. 출처 표시: 정보의 출처(공식 홈페이지 등)를 간단히 언급해.
                5. 면책 조항: "정확한 실제 견적은 업체의 판단에 따라 달라질 수 있습니다"라는 문구를 마지막에 포함해.
                """ + "수리비와 무관한 질문은 '제가 답변드릴 수 없는 질문입니다. 저는 수리비를 예측해주는 챗봇입니다.'라고 답변해.";
        sb.append(questionPrompt).append("\n\n");

        // 이전 질문 있으면 대화 플로우에 적용
        if (history != null && !history.isEmpty()) {
            sb.append("[이전 대화 내용]\n");
            for (String msg : history) {
                sb.append(msg).append("\n");
            }
        }
        // 이전 질문 없으면 첫 질문에 대해 TTL 24시간 설정(첫 질문 기준으로 24시간 안에 5개 질문만 가능)
        else{
            saveChatMessage(userId, "User",  userMessage);
        }
        sb.append("사용자 신규 질문: ").append(userMessage);

        String geminiReply = callGeminiApi(sb.toString());

        // 4. 대화 결과가 정상적일 때만 Redis에 대화 쌍 저장
        if (!geminiReply.startsWith("API 호출 중") && !geminiReply.equals("응답 해석 오류")) {
            saveChatMessage(userId, "User", userMessage);
            saveChatMessage(userId, "Bot", geminiReply);
        }

        return geminiReply;
    }

    // 제미나이 API 호출
    private String callGeminiApi(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.queryParam("key", API_KEY).build())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::extractTextFromResponse)
                    .block();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GEMINI_REQUEST_ERROR); // 커스텀 예외 처리
        }
    }

    // 대화 내역 저장
    public void saveChatMessage(String userId, String role, String message) {
        String key = HISTORY_KEY + userId;
        String data = role + ": " + message;

        // 현재 키의 남은 TTL 확인
        long currentTTL = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        // key값에 data저장
        redisTemplate.opsForList().rightPush(key, data);
        // 최근 10개의 메시지만 유지
        redisTemplate.opsForList().trim(key, -10, -1);


        if (currentTTL < 0) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        } else {
            redisTemplate.expire(key, currentTTL, TimeUnit.SECONDS);
        }
    }

    // 대화 내역 조회
    public List<String> getChatHistory(String userId) {
        return redisTemplate.opsForList().range(HISTORY_KEY + userId, 0, -1);
    }

    private String extractTextFromResponse(Map response) {
        try {
            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            return "응답 해석 오류";
        }
    }
}
