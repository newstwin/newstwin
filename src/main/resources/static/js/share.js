(() => {
  const shareBtn = document.getElementById('shareBtn');
  const shareMenu = document.getElementById('shareMenu');

  if (!shareBtn || !shareMenu) return;

  shareBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    const rect = shareBtn.getBoundingClientRect();
    shareMenu.style.position = 'absolute';
    shareMenu.style.top = `${shareBtn.offsetTop + shareBtn.offsetHeight + 5}px`;
    shareMenu.style.left = `${shareBtn.offsetLeft}px`;
    shareMenu.classList.toggle('d-none');
  });

  // 외부 클릭 시 닫기
  document.addEventListener('click', (e) => {
    if (!shareMenu.classList.contains('d-none') &&
        !shareMenu.contains(e.target) &&
        !shareBtn.contains(e.target)) {
      shareMenu.classList.add('d-none');
    }
  });

  // URL 복사
  document.querySelector('.share-copy').addEventListener('click', async () => {
    const url = window.location.href;
    try {
      await navigator.clipboard.writeText(url);
      alert("주소가 복사되었습니다.");
      shareMenu.classList.add('d-none');
    } catch (e) {
      alert("복사 실패");
    }
  });

  // SNS 공유
  document.querySelector('.share-sns').addEventListener('click', async () => {
    const url = window.location.href;
    const title = document.title;

    if (navigator.share) {
      try {
        await navigator.share({ title, url });
      } catch (e) {}
    } else {
      window.open(
          `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}&quote=${encodeURIComponent(title)}`,
          "_blank",
          "width=600,height=500"
      );
    }
    shareMenu.classList.add('d-none');
  });
})();
