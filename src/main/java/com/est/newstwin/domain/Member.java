package com.est.newstwin.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String memberName;
  private String password;
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  private Boolean receiveEmail = true;
  private String profileImage;

  @Column(name = "status", nullable = false)
  private Boolean isActive = true;   // 관리자 차단 여부

  private Boolean isVerified = false; // 이메일 인증 여부 추가

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<UserSubscription> subscriptions;

  public List<String> getCategories() {
    if (subscriptions == null) return List.of();
    return subscriptions.stream()
            .filter(UserSubscription::getIsActive)
            .map(s -> s.getCategory().getCategoryName())
            .collect(Collectors.toList());
  }

  public Member(String memberName, String password, String email, Role role, Boolean status) {
    this.memberName = memberName;
    this.password = password;
    this.email = email;
    this.role = role;
    this.receiveEmail = true;
    this.profileImage = "/images/basic-profile.png";
    this.isActive = status;
    this.isVerified = false; // 이메일 인증 전 false
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void update(String memberName, String password, Boolean status) {
    this.memberName = memberName;
    this.password = password;
    this.isActive = status;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateInfo(String newName, String newPassword, Boolean newReceiveEmail, String newProfileImage) {
    if (newName != null && !newName.isBlank()) this.memberName = newName;
    if (newPassword != null && !newPassword.isBlank()) this.password = newPassword;
    if (newReceiveEmail != null) this.receiveEmail = newReceiveEmail;
    if (newProfileImage != null && !newProfileImage.isBlank()) this.profileImage = newProfileImage;
    this.updatedAt = LocalDateTime.now();
  }

  public void verifyEmail() {
    this.isVerified = true;
    this.updatedAt = LocalDateTime.now();
  }

  public enum Role {
    ROLE_USER, ROLE_ADMIN
  }
}
