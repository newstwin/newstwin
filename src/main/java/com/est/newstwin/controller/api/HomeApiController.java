package com.est.newstwin.controller.api;

import com.est.newstwin.domain.Category;
import com.est.newstwin.dto.post.PostSummaryDto;
import com.est.newstwin.repository.LikeRepository;
import com.est.newstwin.service.CategoryService;
import com.est.newstwin.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeApiController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final LikeRepository likeRepository;

    /**
     * 오늘의 인기 뉴스
     */
    @GetMapping("/popular-news")
    public List<PostSummaryDto> getPopularNews() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        // 오늘 발행된 뉴스 중 좋아요 많은 순 Top 3
        PageRequest pageable = PageRequest.of(0, 3);

        return postService.getTopPostsByTypeAndDate("news", start, end, pageable);
    }

    /**
     * AI가 분석하는 카테고리
     */
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 커뮤니티 인기 토픽
     */
    @GetMapping("/popular-community")
    public List<PostSummaryDto> getPopularCommunity() {

        PageRequest pageable = PageRequest.of(0, 3);

        return postService.getTopPostsByType("community", pageable);
    }
}
