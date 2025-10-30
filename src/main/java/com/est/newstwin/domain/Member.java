package com.est.newstwin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(name = "member_name", nullable = false, length = 50)
  private String memberName;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP(0)")
  private LocalDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP(0)")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now().withNano(0);
    this.updatedAt = LocalDateTime.now().withNano(0);
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now().withNano(0);
  }

  public enum Role {
    USER, ADMIN
  }
}