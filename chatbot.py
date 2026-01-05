import google.generativeai as genai

# 1. API 키 설정
genai.configure(api_key="GEMINI_API_KEY")

# 2. 시스템 프롬프트 설정 (브랜드별 대응 로직 포함)
SYSTEM_INSTRUCTION = """
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
"""

# 3. 모델 생성 (Google Search Tool 활성화)
model = genai.GenerativeModel(
    model_name="gemini-1.5-flash",  # 혹은 성능이 더 높은 gemini-1.5-pro
    tools=[{"google_search_retrieval": {}}], # 실시간 검색 기능 활성화
    system_instruction=SYSTEM_INSTRUCTION
)

# 4. 챗봇 함수
def get_repair_cost(user_query):
    # 채팅 세션 시작
    chat = model.start_chat()
    
    # 메시지 전송 및 답변 생성
    response = chat.send_message(user_query)
    
    # 답변 및 출처(Grounding Metadata) 출력
    print(f"\n[S-Helper 수리비 가이드]\n")
    print(response.text)
    
    # (선택) 출처 링크 확인이 필요한 경우
    if response.candidates[0].grounding_metadata.search_entry_point:
        print("\n[관련 정보 확인]")
        print("구글 검색 결과 기반으로 작성되었습니다.")

# 5. 실행 예시
if __name__ == "__main__":
    print("수리비 질문을 입력하세요 (예: 아이폰 15 액정 수리비, 플립6 배터리 교체비)")
    user_input = input("질문: ")
    get_repair_cost(user_input)