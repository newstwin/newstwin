document.addEventListener("DOMContentLoaded", async () => {
    const logoutLink = document.querySelector("a[href='/logout']");
    const profileImgEl = document.getElementById("headerProfileImg");

    // 로그아웃 처리
    if (logoutLink) {
        logoutLink.addEventListener("click", async (e) => {
            e.preventDefault();
            try {
                await csrfFetch("/api/auth/logout", { method: "POST" });
                window.location.href = "/";
            } catch (error) {
                console.error("로그아웃 실패:", error);
                alert("로그아웃 중 오류가 발생했습니다.");
            }
        });
    }

    // 로그인 상태면 헤더 이미지 갱신
    if (profileImgEl) {
        try {
            const res = await fetch("/api/mypage/me");

            if (res.ok) {
                const result = await res.json();
                if (result.success && result.data) {
                    const imgUrl = result.data.profileImage;

                    profileImgEl.src = imgUrl && imgUrl.trim() !== ""
                        ? imgUrl
                        : "/images/basic-profile.png";
                }
            } else {
                profileImgEl.src = "/images/basic-profile.png";
            }
        } catch (err) {
            console.error("프로필 이미지 로드 실패:", err);
            profileImgEl.src = "/images/basic-profile.png";
        }
    }
});
