package com.est.newstwin.repository;

import com.est.newstwin.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);
}
