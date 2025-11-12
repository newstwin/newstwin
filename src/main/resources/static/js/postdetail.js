(() => {
  const postId = document.getElementById("likeBtn").dataset.postId;

  const likeBtn = document.getElementById("likeBtn");
  const heartIcon = document.getElementById("heartIcon");
  const likeCountEl = document.getElementById("likeCount");

  const bookmarkBtn = document.getElementById("bookmarkBtn");
  const bookmarkIcon = document.getElementById("bookmarkIcon");

  // 사이드 액션바 요소
  const sideLike = document.getElementById("sideLike");
  const sideLikeCount = document.getElementById("sideLikeCount");
  const sideBookmark = document.getElementById("sideBookmark");
  const sideCommentCount = document.getElementById("sideCommentCount");

  // 로그인 필요 시 리다이렉트
  const ensureAuthOrRedirect = (res) => {
    if (res.status === 401 || res.status === 403) {
      window.location.href = "/login";
      return false;
    }
    return true;
  };

  // 좋아요 / 북마크 UI 업데이트
  const updateLikeUI = (liked, count) => {
    heartIcon.textContent = liked ? "❤️" : "🤍";
    likeBtn.dataset.liked = liked;
    likeCountEl.textContent = count;
    sideLike.querySelector(".emoji").textContent = liked ? "❤️" : "🤍";
    sideLikeCount.textContent = count;
  };

  const updateBookmarkUI = (bookmarked) => {
    bookmarkIcon.className = bookmarked ? "bi bi-bookmark-fill" : "bi bi-bookmark";
    bookmarkBtn.dataset.bookmarked = bookmarked;
    sideBookmark.querySelector("i").className =
        bookmarked ? "bi bi-bookmark-fill text-primary" : "bi bi-bookmark";
  };
  
  document.addEventListener("DOMContentLoaded", () => {
    const deleteBtn = document.getElementById("deleteBtn");
    if (!deleteBtn) return;

    deleteBtn.addEventListener("click", async () => {
      const postId = deleteBtn.dataset.postId;
      const confirmed = confirm("정말 이 글을 삭제하시겠습니까?");
      if (!confirmed) return;

      try {
        const res = await fetch(`/board/delete/${postId}`, { method: 'DELETE' });
        if (res.ok) {
          alert("게시글이 삭제되었습니다.");
          window.location.href = "/board"; // 목록 페이지로 이동
        } else if (res.status === 401) {
          alert("로그인이 필요합니다.");
          window.location.href = "/login";
        } else {
          alert("삭제 중 오류가 발생했습니다.");
        }
      } catch (e) {
        console.error(e);
        alert("삭제 요청 실패");
      }
    });
  });


  // 초기 데이터 로드
  document.addEventListener("DOMContentLoaded", async () => {
    try {
      // tooltip 활성화
      document.querySelectorAll("[title]").forEach((el) => new bootstrap.Tooltip(el));

      const [resCount, resLike, resBookmark] = await Promise.all([
        fetch(`/api/posts/${postId}/like/count`),
        fetch(`/api/posts/${postId}/like`),
        fetch(`/api/posts/${postId}/bookmark`),
      ]);

      if (resCount.ok) {
        const { likeCount } = await resCount.json();
        likeCountEl.textContent = likeCount;
        sideLikeCount.textContent = likeCount;
      }

      if (resLike.ok) {
        const { liked, likeCount } = await resLike.json();
        updateLikeUI(liked, likeCount);
      }

      if (resBookmark.ok) {
        const { bookmarked } = await resBookmark.json();
        updateBookmarkUI(bookmarked);
      }

      // 댓글 개수는 comments.js에서 totalCount 설정 시 자동 반영
      window.updateSideCommentCount = function (count) {
        sideCommentCount.textContent = count;
      };
    } catch (e) {
      console.error("초기 로드 실패:", e);
    }
  });

  // 좋아요 / 북마크 이벤트
  likeBtn.addEventListener("click", async () => {
    likeBtn.disabled = true;
    try {
      const res = await fetch(`/api/posts/${postId}/like`, { method: "POST" });
      if (!ensureAuthOrRedirect(res)) return;
      const { liked, likeCount } = await res.json();
      updateLikeUI(liked, likeCount);
    } finally {
      likeBtn.disabled = false;
    }
  });

  sideLike.addEventListener("click", () => likeBtn.click());

  bookmarkBtn.addEventListener("click", async () => {
    bookmarkBtn.disabled = true;
    try {
      const res = await fetch(`/api/posts/${postId}/bookmark`, { method: "POST" });
      if (!ensureAuthOrRedirect(res)) return;
      const { bookmarked } = await res.json();
      updateBookmarkUI(bookmarked);
    } finally {
      bookmarkBtn.disabled = false;
    }
  });

  sideBookmark.addEventListener("click", () => bookmarkBtn.click());

  // 댓글 클릭 → 스크롤 이동
  const sideComment = document.getElementById("sideComment");
  sideComment.addEventListener("click", () => {
    document.querySelector(".comment-section").scrollIntoView({ behavior: "smooth" });
  });

  // 공유 기능 (▼ 제거, 클릭 즉시 열림)
  const sideShare = document.getElementById("sideShare");
  const dropdownItems = document.querySelectorAll(".dropdown-item");

  dropdownItems.forEach((item) => {
    item.addEventListener("click", async (e) => {
      e.preventDefault();

      if (item.classList.contains("share-copy")) {
        try {
          await navigator.clipboard.writeText(window.location.href);
          alert("주소가 복사되었습니다.");
        } catch {
          alert("복사 실패");
        }
      }

      if (item.classList.contains("share-sns")) {
        const url = encodeURIComponent(window.location.href);
        const title = encodeURIComponent(document.title);
        if (navigator.share) {
          try {
            await navigator.share({ title, url });
          } catch (e) {}
        } else {
          window.open(
              `https://www.facebook.com/sharer/sharer.php?u=${url}&quote=${title}`,
              "_blank",
              "width=600,height=500"
          );
        }
      }

      const dropdown = bootstrap.Dropdown.getInstance(sideShare);
      if (dropdown) dropdown.hide();
    });
  });
})();
