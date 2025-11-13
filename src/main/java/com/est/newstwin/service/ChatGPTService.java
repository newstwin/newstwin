package com.est.newstwin.service;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final OpenAIClient openAIClient;

    /**
     * Alan → ChatGPT: Markdown 버전 (핵심 분석 리포트)
     * - 뉴스별 긍정/부정 측면을 함께 작성
     */
    public String analyzeMarkdown(String alanText) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

        String prompt = """
                당신은 한국 경제 뉴스의 '양면적 경제 해석'을 제공하는 전문가입니다.
                아래는 Alan AI가 수집한 오늘의 경제 뉴스 요약입니다.

                각 기사별로 다음 항목을 반드시 포함하여 **Markdown 형식으로 리포트**를 작성하세요.

                ---
                ## NewsTwin 경제 브리핑 - %s
                발행: %s

                ### 1) 기사 제목
                - 링크: [뉴스 링크](URL)
                - 요약: 한두 문장으로 핵심 내용
                - 한국 경제에 미치는 **긍정적 측면**:
                    - 2가지 이상, 3~5문장, 해당 기사에서 다루는 주제가 한국 경제에 어떤 점에서 긍정적인지
                - 한국 경제에 미치는 **부정적 측면**:
                    - 2가지 이상, 3~5문장, 해당 기사에서 다루는 주제가 한국 경제에 어떤 점에서 부정적인지
                - 해석 요약: 전체 맥락을 한 문장으로 정리

                ---
                [Alan 제공 뉴스 요약]
                %s
                """.formatted(today, today, alanText);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .input(prompt)
                .maxOutputTokens(4096L)
                .temperature(0.4)
                .build();

        Response response = openAIClient.responses().create(params);

        return response.output().stream()
                .flatMap(o -> o.message().stream())
                .flatMap(m -> m.content().stream())
                .flatMap(c -> c.outputText().stream())
                .map(t -> t.text())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("(응답 없음)");
    }

    /**
     * ChatGPT: 동일 내용을 JSON 버전으로 재구성
     * - markdown 결과를 그대로 JSON으로 구조화
     */
    public String analyzeJson(String markdownText) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String prompt = """
                당신은 이전에 작성한 Markdown 뉴스 리포트를 JSON 구조로 변환하는 데이터 분석가입니다.

                아래 Markdown 내용에는 각 기사의 제목, URL, 요약, 긍정적 측면, 부정적 측면이 포함되어 있습니다.
                이를 아래 스키마에 맞게 **JSON 배열**로 변환하세요.

                출력 형식 (JSON만, 코드블록 없이):
                [
                  {
                    "title": "기사 제목",
                    "url": "기사 URL",
                    "summary": "기사 요약",
                    "positive": ["한국 경제에 긍정적인 이유 2~4개"],
                    "negative": ["한국 경제에 부정적인 이유 2~4개"]
                  }
                ]

                규칙:
                - Markdown 외의 텍스트는 절대 포함하지 마세요.
                - positive/negative 항목은 Markdown에 있던 내용을 그대로 반영하되, 핵심 문장만 추출
                - 분석 기준일: %s

                [Markdown 리포트 원문]
                ----
                %s
                ----
                """.formatted(today, markdownText);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .input(prompt)
                .maxOutputTokens(4096L)
                .temperature(0.3)
                .build();

        Response response = openAIClient.responses().create(params);
        String raw = response.output().stream()
                .flatMap(o -> o.message().stream())
                .flatMap(m -> m.content().stream())
                .flatMap(c -> c.outputText().stream())
                .map(t -> t.text())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("[]");

        String cleaned = raw.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```(json)?", "")
                    .replaceAll("```$", "")
                    .trim();
        }
        return cleaned;
    }


    // 제목 자동 생성 (요약된 대표 타이틀)
    public String generateTitle(String markdownText) {
        String prompt = """
        당신은 경제 전문 에디터입니다.
        아래의 Markdown 뉴스 리포트를 한 문장으로 요약하여,
        **오늘의 핵심 주제**를 나타내는 제목을 생성하세요.

        조건:
        - 한국어 50자 이내
        - 뉴스 전체를 대표하는 요약형 문장.
        - 날짜는 포함하지 마세요.

        [뉴스 리포트]
        ----
        %s
        ----
        """.formatted(markdownText);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .input(prompt)
                .maxOutputTokens(500L)
                .temperature(0.3)
                .build();

        Response response = openAIClient.responses().create(params);

        return response.output().stream()
                .flatMap(o -> o.message().stream())
                .flatMap(m -> m.content().stream())
                .flatMap(c -> c.outputText().stream())
                .map(t -> t.text().trim().replaceAll("[\\n\\r]+", ""))
                .findFirst()
                .orElse("오늘의 경제 브리핑");
    }

    public NewsletterResult generateNewsletterSummary(String sourceText) {
      String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

      String prompt = """
          당신은 한국 경제 뉴스의 종합 분석 전문가입니다.
          아래 뉴스 요약을 기반으로 다음 세 가지를 한 번에 작성하세요.
      
          1️⃣ Markdown 뉴스 리포트
          2️⃣ JSON 분석 배열  
          3️⃣ 핵심 제목 (50자 이내, 날짜 제외)
      
          출력 형식은 다음과 같습니다:
          ---MARKDOWN---
          (여기에 Markdown)
          ---JSON---
          (여기에 JSON)
          ---TITLE---
          (여기에 제목)
      
          [Markdown 작성 규칙]
          - 각 뉴스별로 반드시 제목과 요약을 포함합니다.
          - **각 기사 제목 아래에 ‘(뉴스 링크: URL)’ 형태로 링크 placeholder를 남기세요.**
          - 긍정적 / 부정적 측면은 불필요합니다. 간단한 요약 위주로.
          - Markdown 문법을 준수하세요.
      
          [뉴스 요약 원문]
          ----
          %s
          ----
          """.formatted(sourceText);

      ResponseCreateParams params = ResponseCreateParams.builder()
          .model(ChatModel.GPT_4O_MINI)
          .input(prompt)
          .maxOutputTokens(4096L)
          .temperature(0.4)
          .build();

      Response response = openAIClient.responses().create(params);

      String gptResponse = response.output().stream()
          .flatMap(o -> o.message().stream())
          .flatMap(m -> m.content().stream())
          .flatMap(c -> c.outputText().stream())
          .map(t -> t.text())
          .reduce((a, b) -> a + "\n" + b)
          .orElse("");

      String markdown = extractBetween(gptResponse, "---MARKDOWN---", "---JSON---");
      String json = extractBetween(gptResponse, "---JSON---", "---TITLE---");
      String title = extractBetween(gptResponse, "---TITLE---", null);

      return new NewsletterResult(
          markdown != null ? markdown.trim() : "(내용 없음)",
          json != null ? json.trim() : "[]",
          title != null ? title.trim() : "오늘의 경제 브리핑"
      );
    }

    private String extractBetween(String text, String start, String end) {
      int s = text.indexOf(start);
      if (s == -1) return null;
      s += start.length();
      int e = (end != null) ? text.indexOf(end, s) : text.length();
      if (e == -1) e = text.length();
      return text.substring(s, e).trim();
    }

    public record NewsletterResult(String markdown, String json, String title) {}
}
