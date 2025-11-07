package com.est.newstwin.controller.api;

import com.est.newstwin.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    /**
     * Alan 응답 → Markdown 분석
     */
    @PostMapping("/markdown")
    public String toMarkdown(@RequestBody String alanText) {
        return chatGPTService.analyzeMarkdown(alanText);
    }

    /**
     * Markdown → JSON 변환
     */
    @PostMapping("/json")
    public String toJson(@RequestBody String markdownText) {
        return chatGPTService.analyzeJson(markdownText);
    }
}
