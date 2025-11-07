package com.est.newstwin.controller.api;

import com.est.newstwin.scheduler.AiScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 수동 실행용 컨트롤러
 * - 실제 스케줄러 로직을 수동으로 트리거함
 * - AI Writer 계정으로 전체 카테고리 파이프라인 강제 실행
 */
@Slf4j
@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class AiSchedulerController {

    private final AiScheduler aiScheduler;

    /**
     * 스케줄러 강제 실행 (관리자 전용)
     */
    @PostMapping("/run")
    @PreAuthorize("hasRole('ADMIN')")
    public String runSchedulerManually() {
        try {
            log.info("⚙️ [Scheduler-Manual] 관리자에 의해 스케줄러 수동 실행 요청");

            aiScheduler.runPipelineEvery15Min();

            return "✅ 스케줄러 수동 실행 완료 (AI 뉴스 파이프라인이 백그라운드에서 실행됩니다)";
        } catch (Exception e) {

            log.error("❌ [Scheduler-Manual] 수동 실행 실패: {}", e.getMessage(), e);

            return "❌ 스케줄러 수동 실행 실패: " + e.getMessage();
        }
    }
}
