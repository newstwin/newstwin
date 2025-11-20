![Image](https://github.com/user-attachments/assets/f7679304-b932-4ee2-a20d-4bf915d15b96)

# 📰 NewsTwin  
AI가 경제 뉴스를 자동 분석해 개인 맞춤 뉴스레터로 전달하는 서비스

**🚀 Live Service**  
🔗 https://newstwin.kro.kr/

NewsTwin은 경제 뉴스를 자동 수집하고, AI가 분석한 결과를 기반으로  
사용자에게 **개인화된 경제 뉴스 브리핑**을 제공하는 자동화 플랫폼입니다.

Alan AI + ChatGPT 기반의 AI 분석과  
Spring Boot + PostgreSQL 기반의 안정적인 자동화 구조로 운영됩니다.

---

# 👥 Team NewsTwin

**NewsTwin 팀은 AI 자동화, 백엔드 개발, 웹 서비스 UI·UX 설계 등 다양한 역할을 협업하여 프로젝트를 완성했습니다.**

## 🎯 프로젝트 목표
- AI 기반 자동 경제 뉴스 분석 및 개인화 뉴스레터 서비스 개발  
- Alan AI + ChatGPT를 활용한 **뉴스 수집 → 분석 → 게시글 생성** 자동화  
- 회원/구독/게시글/메일 발송까지 연결되는 **엔드-투-엔드 서비스 흐름 구축**  
- 관리자(Admin) 페이지로 안정적인 운영 관리 환경 제공  

## 👨‍💻 팀원 소개

| 이름 | 담당 기능 |
|------|-----------|
| **최보윤** | 회원관리 · 인증 · 마이페이지 · AI 파이프라인 |
| **김재영** | 게시글/피드 · 캐싱 · 용어사전 · 필터링 |
| **최애정** | 관리자(Admin) · 댓글 · 이메일 발송/구독해지 |

---

# 📄 프로젝트 문서 (Notion)

👉 **전체 문서 보기:**  
https://www.notion.so/oreumi/5-NT-NewsTwin-299ebaa8982b80b6b9b6e7ce37a89583

---

# 🚀 Milestones (핵심 성과 요약)

| 단계 | 기간 | 달성 내용 |
|------|--------|-----------|
| **기획 & 설계** | 10/28 ~ 10/30 | ERD, 기능명세, 화면설계(Figma), 브랜치전략 설정 |
| **핵심 기능 구현** | 10/31 ~ 11/07 | 회원·구독·게시글 CRUD, Alan→ChatGPT 파이프라인, 이메일 자동발송 |
| **기능 확장 & UX 개선** | 11/08 ~ 11/14 | 좋아요·북마크·댓글·공유하기, 경제용어 툴팁 기능 |
| **테스트 & 배포** | 11/15 ~ 11/18 | EC2/RDS 배포, CI/CD, 보안환경, 실서버 스케줄러 테스트 |
| **발표 준비** | 11/19 ~ 11/20 | 발표자료 제작, 시연 영상, 데모 시나리오 구성 |
| **최종 발표** | 11/21 | 프로젝트 발표 및 회고 |

---

# ⚙️ 기능 요약 (Feature Summary)

### 회원 / 인증
- JWT 로그인, 회원가입, 소셜 로그인(OAuth2)
- 마이페이지: 정보 수정 · 관심카테고리 설정 · 구독 수신 여부

### 뉴스 자동 분석 파이프라인
- Alan AI: 카테고리별 최신 뉴스 5개 수집 (09시 Scheduler)
- ChatGPT: 긍정/부정 분석 + 요약 생성 + 키워드 추출
- 중복 뉴스 방지(사용 키워드 누적) & 직렬 구조 처리

### 이메일 뉴스레터
- 구독자별 개인화 뉴스 구성
- `@Async` 기반 비동기 이메일 발송
- UUID 기반 구독 해지 링크 (로그인 불필요)

### 게시글 / 피드
- 카테고리별 뉴스 리스트 & 상세 페이지
- 좋아요 · 북마크 · 댓글 · 대댓글
- Excel 캐싱 기반 경제 용어 자동 툴팁

### 관리자(Admin)
- 회원/게시글/댓글/메일 로그 관리
- 게시글 내용 수정 및 운영 도구 제공
- 관리자 전용 대시보드

---

## 🛠 Tech Stack

### Backend

<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/> <img src="https://img.shields.io/badge/SpringScheduler-4CAF50?style=for-the-badge&logo=spring&logoColor=white"/> <img src="https://img.shields.io/badge/SpringMail-EA4335?style=for-the-badge&logo=gmail&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>

### Database

<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/> <img src="https://img.shields.io/badge/H2-323330?style=for-the-badge&logo=h2&logoColor=white"/>

### AI / External API

<img src="https://img.shields.io/badge/OpenAI_ChatGPT-74AA9C?style=for-the-badge&logo=openai&logoColor=white"/> <img src="https://img.shields.io/badge/AlanAI-000000?style=for-the-badge&logoColor=white"/>

### Frontend

<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"/>

### DevOps / Deployment

<img src="https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"/> <img src="https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazonaws&logoColor=white"/> <img src="https://img.shields.io/badge/AWS_RDS-527FFF?style=for-the-badge&logo=amazonaws&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"/>

### Collaboration

<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white"/>

---

# 🏗 시스템 아키텍처 (Architecture)
<img width="3784" height="1739" alt="Image" src="https://github.com/user-attachments/assets/0c6261b1-c4f3-41da-837a-5314e5f89cbe" />

---
# 📊 ERD (Entity Relationship Diagram)
<img width="1322" height="750" alt="Image" src="https://github.com/user-attachments/assets/e06bea1f-34f9-4f6e-9aac-d4e4cdd851c7" />

| 구분 | 테이블명 | 역할 |
|------|-----------|--------|
| 사용자 | MEMBER | 사용자 기본 정보(닉네임, 이메일, 비밀번호, 프로필 이미지, 권한, 메일 수신 여부 등) 저장 |
| 사용자 구독 | USER_SUBSCRIPTION | 사용자별 구독 카테고리 및 활성/비활성 상태 관리 |
| 게시글 | POST | Alan + ChatGPT 분석 결과(제목, 본문, 요약 JSON, 조회수, 썸네일, 카테고리 등) 저장 |
| 카테고리 | CATEGORY | 서비스에서 사용하는 8개 뉴스 카테고리 정보 |
| 댓글 | COMMENT | 게시글 댓글 및 대댓글, 삭제 여부 포함 |
| 좋아요 | LIKE | 게시글 좋아요 정보(게시글 + 사용자 조합 UNIQUE) |
| 북마크 | BOOKMARK | 게시글 북마크 정보(게시글 + 사용자 조합 UNIQUE) |
| 메일 전송 | MAIL_LOG | 뉴스레터 발송 결과, 재시도 횟수, 에러 메시지, 마지막 시도 시간 기록 |
| 메일 인증 토큰 | EMAILVERIFICATIONTOKEN | 이메일 인증 토큰 및 만료 시간 저장 |
| 용어 | TERM | 용어, 정의, 생성일, 수정일 관리 |
| 사진 저장소 | PHOTO | 업로드된 이미지 파일(s3 key, url, 원본 파일명 등) 정보 저장 |

## 📌 Entity Relationship (NewsTwin)
<pre>
Member (1) ──< (N) Post : 한 사용자는 여러 게시글을 작성할 수 있음  
Category (1) ──< (N) Post : 하나의 카테고리에 여러 게시글이 속할 수 있음  
Post (1) ──< (N) Comment : 하나의 게시글에는 여러 댓글이 달릴 수 있음  
Member (1) ──< (N) Comment : 한 사용자는 여러 댓글을 작성할 수 있음  
Comment (1) ──< (N) Comment(parent-child) : 댓글은 대댓글 구조를 가질 수 있음  
Post (1) ──< (N) Like : 하나의 게시글을 여러 사용자가 좋아요할 수 있음  
Member (1) ──< (N) Like : 한 사용자는 여러 게시글에 좋아요할 수 있음(게시글당 1회)  
Post (1) ──< (N) Bookmark : 하나의 게시글을 여러 사용자가 북마크할 수 있음  
Member (1) ──< (N) Bookmark : 한 사용자는 여러 게시글을 북마크할 수 있음(게시글당 1회)  
Member (1) ──< (N) UserSubscription : 한 사용자는 여러 카테고리를 구독할 수 있음  
Category (1) ──< (N) UserSubscription : 하나의 카테고리는 여러 사용자가 구독할 수 있음  
Post (1) ──< (1) MailLog : 하나의 게시글은 단일 메일 발송 로그를 가짐(Unique: post_id)  
Member (1) ──< (N) MailLog : 한 사용자는 여러 메일 발송 이력을 가질 수 있음  
Member (1) ──< (1) EmailVerificationToken : 한 사용자는 하나의 이메일 인증 토큰을 가짐  
Term : 독립 엔티티로 다른 엔티티와 직접 관계 없음  
Photo : 독립 엔티티로 다른 엔티티와 직접 관계 없음
</pre>  
---

# 🗂️ 디렉토리 구조

<pre>
newstwin/
├── src/main/java/com/est/newstwin/
│   ├── config/               # 설정 클래스 (Security, JWT, Mail, Scheduler, OpenAPI 등)
│   ├── controller/           # REST API + 웹 컨트롤러 (Admin UI, Member, Post 등)
│   ├── domain/               # JPA 엔티티 (Member, Post, Category, Comment, MailLog 등)
│   ├── dto/                  # DTO (Request / Response)
│   ├── exception/            # 전역 예외 처리 및 커스텀 예외
│   ├── repository/           # JPA Repository 인터페이스
│   ├── scheduler/            # 스케줄러 실행 클래스 (AI 파이프라인, 메일 발송 등)
│   └── service/              # 비즈니스 로직 (Member, Post, Pipeline, Mail 등)
└── src/main/resources/
    ├── templates/            # Thymeleaf 템플릿 (HTML 페이지)
    └── static/               # 정적 리소스 (CSS, JS, Images)
</pre>

---

# 🧩 개발 컨벤션

## 🔀 Git 브랜치 전략
<pre>
develop
 ├─ feature/{기능명}
 ├─ fix/{버그명}
 └─ refactor/{리팩토링명}

stage       ← 테스트 배포
production  ← 실서버 배포
</pre>

## 📝 Git Commit Convention

### Commit Type
| 타입 | 설명 |
|------|-------------|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| refactor | 코드 리팩토링 |
| style | 포맷/주석 등 비기능 수정 |
| docs | 문서 수정 |
| chore | 빌드/환경설정 작업 |
| test | 테스트 코드 관련 변경 |

### Commit Rule
<타입>: <작업 내용 요약> (#이슈번호)

### Commit Example
feat: Alan AI 뉴스 수집 기능 추가 (#12)  
fix: 메일 발송 실패 로그 저장 오류 수정

## 💻 코드 컨벤션
- 클래스: PascalCase  
- 메서드/변수: lowerCamelCase  
- 상수: UPPER_SNAKE_CASE  
- 패키지: lowercase  
- DB 컬럼: snake_case
