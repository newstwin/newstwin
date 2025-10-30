package com.est.newstwin.repository;

import com.est.newstwin.domain.Comment;
import com.est.newstwin.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
  List<Comment> findAllByParent(Comment parent);
}
