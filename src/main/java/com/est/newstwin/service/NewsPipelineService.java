package com.est.newstwin.service;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsPipelineService {

    private final AlanApiService alanApiService;
    private final ChatGPTService chatGPTService;
    private final AIPostService aiPostService;
    private final PhotoService photoService;

    /**
     * 중복 URL 방지용 전역 Set
     */
    private final Set<String> usedUrls = new HashSet<>();

    /**
     * URL 추출 정규식
     */
    private static final Pattern URL_PATTERN =
            Pattern.compile("https?://[\\w\\-\\.]+(?:/[^\\s]*)?");

    /**
     * 카테고리 단위 파이프라인 처리
     */
    public void processCategory(Category category, Member aiMember) {

        String categoryName = category.getCategoryName();
        log.info("[Pipeline 시작] category={}", categoryName);

        try {
            // 1. Alan API 호출 + URL 검증 + 중복 방지
            log.info("[Alan 호출 시작] category={}", categoryName);
            String alanText = fetchAlanNewsWithRetry(category, 3);  // 최대 3회 재시도

            if (alanText == null || alanText.isBlank()) {
                log.warn("[Alan 응답 없음] category={}", categoryName);
                log.info("[Pipeline 종료] category={}", categoryName);
                return;
            }

            log.info("[Alan 응답 확보] 길이={} chars", alanText.length());
            log.debug("[Alan 응답 미리보기]\n{}", preview(alanText));

            // 2. ChatGPT Markdown 분석
            log.info("[ChatGPT 분석(Markdown) 요청]");
            String markdown = chatGPTService.analyzeMarkdown(alanText);
            log.info("[Markdown 분석 완료] length={}", markdown != null ? markdown.length() : 0);
            log.debug("[Markdown 미리보기]\n{}", preview(markdown));

            // 3. ChatGPT JSON 변환
            log.info("[ChatGPT JSON 변환 요청]");
            String json = chatGPTService.analyzeJson(markdown);
            log.info("[JSON 변환 완료] length={}", json != null ? json.length() : 0);
            log.debug("[JSON 미리보기]\n{}", preview(json));

            // 4. 제목 생성
            log.info("[ChatGPT 제목 생성 요청]");
            String title = chatGPTService.generateTitle(markdown);
            log.info("[제목 생성 완료] title={}", title);

            // 5. 대표 이미지 생성
            log.info("[대표 이미지 생성 요청]");
            String imageUrl = chatGPTService.generateRepresentativeImage(markdown);
            log.info("[대표 이미지 생성 완료] imageUrl={}", imageUrl);

            // 6. 이미지 S3 저장 (필요 시 PhotoService 이용)
            String finalImageUrl = imageUrl;
            log.info("[대표 이미지 S3 저장 완료] url={}", finalImageUrl);

            // 7. Markdown 맨 위에 이미지 삽입
            String markdownWithImage =
                    "![대표 이미지](" + finalImageUrl + ")\n\n" + markdown;

            // 8. 게시글 저장
            log.info("[AI 게시글 저장 시작] category={}, title={}", categoryName, title);
            aiPostService.saveAiPost(aiMember, category, markdownWithImage, json, title, finalImageUrl);
            log.info("[AI 게시글 저장 완료] category={}, title={}", categoryName, title);

        } catch (Exception e) {
            log.error("[Pipeline 실패] category={}, message={}", categoryName, e.getMessage(), e);
        }

        log.info("[Pipeline 종료] category={}", categoryName);
    }


    /**
     * Alan에서 뉴스 가져오기 + URL 존재 검증 + 재시도 로직
     */
    private String fetchAlanNewsWithRetry(Category category, int maxRetry) {
        String categoryName = category.getCategoryName();

        for (int attempt = 1; attempt <= maxRetry; attempt++) {
            log.info("[Alan 재시도] category={}, attempt={}/{}", categoryName, attempt, maxRetry);

            String alanText = alanApiService.fetchNews(categoryName, usedUrls);

            if (alanText == null || alanText.isBlank()) {
                log.warn("[Alan 빈 응답] attempt={}", attempt);
                continue;
            }

            Set<String> urls = extractUrls(alanText);

            if (urls.isEmpty()) {
                log.warn("[Alan 응답에 URL 없음] attempt={}", attempt);
                continue;
            }

            // 중복 URL 여부 검사
            boolean hasDuplicate = urls.stream().anyMatch(usedUrls::contains);
            if (hasDuplicate) {
                log.warn("[중복 URL 발견] attempt={}, urls={}", attempt, urls);
                continue;
            }

            // 새 URL 추가
            usedUrls.addAll(urls);
            log.info("[URL 확보 완료] urls={}", urls);

            return alanText;
        }

        log.error("[Alan 실패] category={} / URL 확보 실패", categoryName);
        return null;
    }


    /**
     * 문자열에서 URL 추출
     */
    private Set<String> extractUrls(String text) {
        Set<String> urls = new HashSet<>();
        Matcher m = URL_PATTERN.matcher(text);
        while (m.find()) {
            urls.add(m.group());
        }
        return urls;
    }

    /**
     * 실제 로그에선 텍스트가 너무 길지 않게 앞부분만 보여줌
     */
    private String preview(String text) {
        if (text == null) {
            return "(null)";
        }
        return text.length() > 500
                ? text.substring(0, 500) + "..."
                : text;
    }
}
