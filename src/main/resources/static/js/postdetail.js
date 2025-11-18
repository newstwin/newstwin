(() => {
  const postId = document.getElementById("likeBtn").dataset.postId;

  const likeBtn = document.getElementById("likeBtn");
  const heartIcon = document.getElementById("heartIcon");
  const likeCountEl = document.getElementById("likeCount");

  const bookmarkBtn = document.getElementById("bookmarkBtn");
  const bookmarkIcon = document.getElementById("bookmarkIcon");

  const sideLike = document.getElementById("sideLike");
  const sideLikeCount = document.getElementById("sideLikeCount");
  const sideBookmark = document.getElementById("sideBookmark");
  const sideComment = document.getElementById("sideComment");
  const sideCommentCount = document.getElementById("sideCommentCount");

  const ensureAuthOrRedirect = (res) => {
    if (res.status === 401 || res.status === 403) {
      window.location.href = "/login";
      return false;
    }
    return true;
  };

  // 댓글 수 sync
  window.updateSideCommentCount = function (count) {
    if (sideCommentCount) sideCommentCount.textContent = count;
  };

  /* 좋아요 UI */
  const updateLikeUI = (liked, count) => {
    heartIcon.textContent = liked ? "❤️" : "🤍";
    likeBtn.dataset.liked = liked;
    likeCountEl.textContent = count;
    sideLike.querySelector(".emoji").textContent = liked ? "❤️" : "🤍";
    sideLikeCount.textContent = count;
  };

  /* 북마크 UI */
  const updateBookmarkUI = (bookmarked) => {
    bookmarkIcon.className = bookmarked ? "bi bi-bookmark-fill" : "bi bi-bookmark";
    bookmarkBtn.dataset.bookmarked = bookmarked;

    sideBookmark.querySelector("i").className =
        bookmarked ? "bi bi-bookmark-fill text-primary" : "bi bi-bookmark";
  };

  document.addEventListener("DOMContentLoaded", async () => {
    const resCommentCount = await fetch(`/api/posts/${postId}/comments?page=0&size=1`);
    if (resCommentCount.ok) {
      const data = await resCommentCount.json();
      const total = data.totalCount || 0;

      const ct = document.getElementById("commentTotal");
      if (ct) ct.textContent = total;

      window.updateSideCommentCount(total);
    }

    const [resCount, resLike, resBookmark] = await Promise.all([
      fetch(`/api/posts/${postId}/like/count`),
      fetch(`/api/posts/${postId}/like`),
      fetch(`/api/posts/${postId}/bookmark`)
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
  });

  /* 좋아요 */
  likeBtn.addEventListener("click", async () => {
    const res = await csrfFetch(`/api/posts/${postId}/like`, { method: "POST" });
    if (!ensureAuthOrRedirect(res)) return;

    const { liked, likeCount } = await res.json();
    updateLikeUI(liked, likeCount);
  });

  sideLike.addEventListener("click", () => likeBtn.click());

  /* 북마크 */
  bookmarkBtn.addEventListener("click", async () => {
    const res = await csrfFetch(`/api/posts/${postId}/bookmark`, { method: "POST" });
    if (!ensureAuthOrRedirect(res)) return;

    const { bookmarked } = await res.json();
    updateBookmarkUI(bookmarked);
  });

  sideBookmark.addEventListener("click", () => bookmarkBtn.click());

  /* 댓글 클릭 → 스크롤 */
  if (sideComment) {
    sideComment.addEventListener("click", () => {
      const section = document.querySelector(".comment-section");
      if (section) section.scrollIntoView({ behavior: "smooth", block: "start" });
    });
  }

  /* 공유 기능… (공유는 fetch 없음 → 변경 X) */
})();
