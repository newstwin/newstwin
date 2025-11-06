package com.est.newstwin.service;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsPipelineService {

    private final AlanApiService alanApiService;
    private final ChatGPTService chatGPTService;
    private final AIPostService aiPostService;

    public void processCategory(Category category, Member aiMember) {
        log.info("🟢 [Pipeline 시작] 카테고리: {}", category.getCategoryName());

        try {
            // 1️⃣ Alan 호출
            log.info("⭐ Alan API 호출 시작...");
            String alanText = alanApiService.fetchNews(category.getCategoryName(), Collections.emptySet());
            log.info("✅ Alan 응답 수신 완료 (길이: {} chars)", alanText != null ? alanText.length() : 0);

            if (alanText == null || alanText.isBlank()) {
                log.warn("Alan 응답이 비어 있습니다. category={}", category.getCategoryName());
                return;
            }

            // (미리보기)
            log.info("⭐ Alan 응답 미리보기:\n{}", preview(alanText));

            // ChatGPT - Markdown
            log.info("⭐ ChatGPT 분석 (Markdown) 요청 중...");
            String markdown = chatGPTService.analyzeMarkdown(alanText);
            log.info("✅ Markdown 분석 완료 (길이: {} chars)", markdown != null ? markdown.length() : 0);
            log.info("⭐ Markdown 미리보기:\n{}", preview(markdown));

            // ChatGPT - JSON 변환
            log.info("⭐ ChatGPT JSON 변환 요청 중...");
            String json = chatGPTService.analyzeJson(markdown);
            log.info("✅ JSON 변환 완료 (길이: {} chars)", json != null ? json.length() : 0);
            log.info("⭐ JSON 미리보기:\n{}", preview(json));

            // ChatGPT - 제목 생성
            log.info("⭐ ChatGPT 제목 생성 중...");
            String title = chatGPTService.generateTitle(markdown);
            log.info("✅ 제목 생성 완료: {}", title);

            // 게시글 저장
            log.info("⭐ AI 게시글 저장 시작...");
            aiPostService.saveAiPost(aiMember, category, markdown, json, title);
            log.info("✅ 게시글 저장 성공: [카테고리: {}, 제목: {}]", category.getCategoryName(), title);

        } catch (Exception e) {
            log.error("❌ [Pipeline Error: {}] {}", category.getCategoryName(), e.getMessage(), e);
        }

        log.info("⭐ [Pipeline 종료] 카테고리: {}", category.getCategoryName());
    }

    /**
     * 응답 문자열 미리보기 (길면 앞부분 300자만)
     */
    private String preview(String text) {
        if (text == null) return "(null)";
        return text.length() > 300 ? text.substring(0, 500) + "..." : text;
    }
}
