package com.est.newstwin.repository;

import com.est.newstwin.domain.Category;
import com.est.newstwin.domain.Member;
import com.est.newstwin.domain.UserSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
  List<UserSubscription> findAllByMember(Member member); // 단방향 조회용
  List<UserSubscription> findAllByCategory(Category category);
  Optional<UserSubscription> findByMemberAndCategory(Member member, Category category);
}