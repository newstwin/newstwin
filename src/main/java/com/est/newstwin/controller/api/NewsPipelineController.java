package com.est.newstwin.controller.api;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import com.est.newstwin.service.NewsPipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/pipeline")
@RequiredArgsConstructor
public class NewsPipelineController {

    private final NewsPipelineService newsPipelineService;

    /**
     * 파이프라인 실행
     * @param category 카테고리 이름 (예: "경제", "정치", "증시")
     * @return 처리 상태 메시지
     */
    @PostMapping("/run")
    public String runPipeline(@RequestParam String category) {
        try {
            // 임시 관리자 계정 (id=1L) 설정
            Member aiMember = Member.builder()
                    .id(1L)
                    .memberName("AI Writer")
                    .build();

            // 테스트용 Category (DB 연동 전이라면 임시 객체로)
            Category cat = Category.builder()
                    .id(1L)
                    .categoryName(category)
                    .build();

            newsPipelineService.processCategory(cat, aiMember);

            return "[PipeLine Log] Pipeline executed successfully for category: " + category;
        } catch (Exception e) {
            e.printStackTrace();
            return "[PipeLine Log] Pipeline execution failed: " + e.getMessage();
        }
    }
}
