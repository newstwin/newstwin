/**
 * 로그인 페이지 스크립트
 * - 로그인 폼 제출 시 /api/auth/login 요청
 * - JWT는 서버에서 HttpOnly 쿠키로 발급됨
 * - 이메일 인증 안 된 사용자는 /verify-info 로 리다이렉트
 * - 비밀번호 표시/숨김 토글 기능 포함
 */

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const togglePasswordBtn = document.getElementById("togglePassword");
    const toggleIcon = togglePasswordBtn.querySelector("i");

    // 🔹 비밀번호 표시/숨김 토글
    togglePasswordBtn.addEventListener("click", () => {
        const isHidden = passwordInput.type === "password";
        passwordInput.type = isHidden ? "text" : "password";
        toggleIcon.classList.toggle("bi-eye", isHidden);
        toggleIcon.classList.toggle("bi-eye-slash", !isHidden);
    });

    // 🔹 로그인 폼 제출 처리
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();

        if (!email || !password) {
            alert("이메일과 비밀번호를 입력해주세요.");
            return;
        }

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include", // HttpOnly 쿠키 자동 포함
                body: JSON.stringify({ email, password }),
            });

            const result = await response.json();

            // 로그인 성공
            if (response.ok && result.success) {
                alert("로그인 성공!");
                window.location.href = "/";
                return;
            }

            // 이메일 인증 미완료 시 → 안내 페이지로 이동
            if (
                result.message &&
                result.message.includes("이메일 인증이 완료되지 않았습니다")
            ) {
                alert("이메일 인증이 완료되지 않았습니다. 메일함을 확인해주세요.");
                window.location.href = "/verify-info";
                return;
            }

            // 계정 비활성화(관리자 차단)
            if (result.message && result.message.includes("비활성화된 계정")) {
                alert("관리자에 의해 비활성화된 계정입니다. 문의해주세요.");
                return;
            }

            // 비밀번호 오류 등 기타 실패
            alert(result.message || "로그인에 실패했습니다.");
        } catch (error) {
            console.error("로그인 요청 실패:", error);
            alert("서버와의 연결에 문제가 발생했습니다.");
        }
    });
});
