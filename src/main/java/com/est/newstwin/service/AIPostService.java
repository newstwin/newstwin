package com.est.newstwin.service;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import com.est.newstwin.domain.Post;
import com.est.newstwin.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AIPostService {

    private final PostRepository postRepository;

    @Transactional
    public Post saveAiPost(
            Member aiMember,
            Category category,
            String markdown,
            String json,
            String title,
            String thumbnailUrl
    ) {

        Post post = Post.builder()
                .member(aiMember)
                .category(category)
                .type("news")
                .title(title)
                .content(markdown)
                .thumbnailUrl(thumbnailUrl)
                .analysisJson(json)
                .count(0)
                .isActive(true)
                .build();

        return postRepository.save(post);
    }
}
