package com.est.newstwin.repository;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import com.est.newstwin.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findAllByMember(Member member);
  List<Post> findAllByCategory(Category category);
  List<Post> findAllByType(String type);
}
