package com.est.newstwin.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityPostSummaryDto {

    private Long id;
    private String title;
    private String author;       // 작성자 추가
    private LocalDateTime createdAt;
    private int count;
    private String content;      // 요약용
}
