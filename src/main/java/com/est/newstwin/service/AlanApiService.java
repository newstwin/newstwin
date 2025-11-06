package com.est.newstwin.service;

import com.est.newstwin.config.AlanAIConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AlanApiService {

    private final RestTemplate restTemplate;
    private final AlanAIConfig alanAIConfig;

    /**
     *  Alan AI에서 뉴스 텍스트 가져오기
     */
    public String fetchNews(String category, Set<String> excludeKeywords) {
        try {
            String prompt = buildPrompt(category, excludeKeywords);
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);

            String url = alanAIConfig.getBaseUrl()
                    + "/question?client_id=" + alanAIConfig.getClientId()
                    + "&content=" + encodedPrompt;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Alan API 호출 실패: " + e.getMessage(), e);
        }
    }

    /**
     *  Alan 세션 상태 초기화
     */
    public String resetAlanState() {
        try {
            String url = alanAIConfig.getBaseUrl() + "/reset-state";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{\"client_id\":\"" + alanAIConfig.getClientId() + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, entity, String.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Alan 상태 초기화 실패: " + e.getMessage(), e);
        }
    }

    /**
     *  Alan에게 전달할 프롬프트 생성
     */
    private String buildPrompt(String category, Set<String> excludeKeywords) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

        StringBuilder sb = new StringBuilder();

        sb.append("당신은 한국 경제 뉴스를 요약하는 전문 분석 AI입니다.\n\n")
                .append("다음 조건에 따라 뉴스를 검색하고 요약하세요:\n")
                .append("1. 뉴스 발행일: **").append(today).append("에 실제로 발행된 기사만 포함** (이전 날짜 기사 제외)\n")
                .append("2. 카테고리: '").append(category).append("'\n")
                .append("3. 제외 조건: 제목 또는 URL에 아래 키워드가 포함된 뉴스는 제외합니다.\n");

        if (excludeKeywords == null || excludeKeywords.isEmpty()) {
            sb.append("   - 없음\n");
        } else {
            sb.append("   - ").append(String.join(", ", excludeKeywords)).append("\n");
        }

        sb.append("\n4. **모든 뉴스 항목은 반드시 아래 형식을 따르세요 (URL은 반드시 포함되어야 함)**:\n")
                .append("   - 번호. **[제목]** (출처: [뉴스 링크](https://...))\n")
                .append("   - 내용: 약 150~200 tokens 분량으로 작성\n")
                .append("   - 내용에는 **원인, 배경, 경제적 영향, 시장 반응** 등을 포함\n")
                .append("   - 각 뉴스는 반드시 **고유한 URL**을 가져야 하며, URL이 없는 뉴스는 제외합니다.\n")
                .append("   - URL이 누락된 경우, 그 뉴스 항목은 잘못된 형식으로 간주하고 새 뉴스를 대신 포함시키세요.\n")
                .append("   - 중복 기사, 광고성 기사, 요약 중복 뉴스는 제외합니다.\n\n")

                .append("5. 출력 예시:\n")
                .append("1. **[삼성전자, AI 반도체 수출 호조]** (출처: [뉴스 링크](https://www.hankyung.com/article/...))\n")
                .append("   - 내용: 반도체 수출 호조로 인해 수출액이 증가했고, 이는 한국 경기 회복세에 긍정적인 영향을 주었습니다.\n\n")

                .append("이제 '").append(category)
                .append("' 카테고리의 **").append(today)
                .append("에 발행된 주요 경제 뉴스 5개를**, 위 형식에 맞춰 정리하세요.")
                .append("\n\n 반드시 각 뉴스마다 [뉴스 링크](https://...) 형태의 실제 URL을 포함해야 합니다.");

        return sb.toString();
    }

}
