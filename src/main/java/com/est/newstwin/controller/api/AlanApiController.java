package com.est.newstwin.controller.api;

import com.est.newstwin.service.AlanApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/alan")
@RequiredArgsConstructor
public class AlanApiController {

    private final AlanApiService alanApiService;

    /**
     * Alan AI 뉴스 호출
     */
    @GetMapping("/fetch")
    public String fetchNews(@RequestParam String category) {
        Set<String> exclude = new HashSet<>();

        return alanApiService.fetchNews(category, exclude);
    }

    /**
     * Alan AI 세션 초기화
     */
    @PostMapping("/reset")
    public String resetAlan() {
        return alanApiService.resetAlanState();
    }
}
