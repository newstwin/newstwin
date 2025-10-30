package com.est.newstwin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  @Column(name = "category_name", nullable = false, length = 50, unique = true)
  private String categoryName;
}