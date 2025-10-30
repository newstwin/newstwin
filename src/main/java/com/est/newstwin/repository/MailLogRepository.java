package com.est.newstwin.repository;

import com.est.newstwin.domain.MailLog;
import com.est.newstwin.domain.Member;
import com.est.newstwin.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailLogRepository extends JpaRepository<MailLog, Long> {
  List<MailLog> findAllByMember(Member member);
  List<MailLog> findAllByPost(Post post);
  List<MailLog> findAllByStatus(String status);
}
