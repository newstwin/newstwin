(() => {
  /*** 좋아요 기능 ***/
  const likeBtn = document.getElementById("likeBtn");
  const heartIcon = document.getElementById("heartIcon");
  const likeCountEl = document.getElementById("likeCount");

  if (likeBtn && heartIcon && likeCountEl) {
    likeBtn.addEventListener("click", async () => {
      const postId = likeBtn.dataset.postId;

      const res = await fetch(`/api/posts/${postId}/like`, {
        method: "POST"
      });

      if (res.status === 401) {
        alert("로그인이 필요합니다.");
        return location.href = "/login";
      }

      if (!res.ok) {
        return alert("좋아요 처리 중 오류가 발생했습니다.");
      }

      const { liked, likeCount } = await res.json();
      heartIcon.textContent = liked ? "❤️" : "🤍";
      likeCountEl.textContent = likeCount;
    });
  }
  /*** 삭제 기능 ***/
  const deleteBtn = document.getElementById("deleteBtn");

  if (deleteBtn) {
    deleteBtn.addEventListener("click", async () => {
      if (!confirm("정말 이 글을 삭제하시겠습니까?")) return;

      const postId = deleteBtn.dataset.postId;

      try {
        const res = await fetch(`/board/delete/${postId}`, {
          method: "DELETE"
        });

        if (res.ok) {
          alert("게시글이 삭제되었습니다.");
          location.href = "/board";
        } else if (res.status === 401) {
          alert("로그인이 필요합니다.");
          location.href = "/login";
        } else {
          alert("삭제 중 오류가 발생했습니다.");
        }
      } catch (err) {
        console.error(err);
        alert("삭제 요청 실패");
      }
    });
  }
})();