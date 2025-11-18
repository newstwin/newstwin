document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const togglePasswordBtn = document.getElementById("togglePassword");
    const toggleIcon = togglePasswordBtn.querySelector("i");

    togglePasswordBtn.addEventListener("click", () => {
        const isHidden = passwordInput.type === "password";
        passwordInput.type = isHidden ? "text" : "password";
        toggleIcon.classList.toggle("bi-eye", isHidden);
        toggleIcon.classList.toggle("bi-eye-slash", !isHidden);
    });

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
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ email, password }),
            });

            const result = await response.json();

            if (response.ok && result.success) {
                alert("로그인 성공!");
                window.location.href = "/";
                return;
            }

            if (result.message && result.message.includes("이메일 인증이 완료되지 않았습니다")) {
                alert("이메일 인증이 완료되지 않았습니다. 메일함을 확인해주세요.");
                window.location.href = "/verify-info";
                return;
            }

            if (result.message && result.message.includes("비활성화된 계정")) {
                alert("관리자에 의해 비활성화된 계정입니다.");
                return;
            }

            alert(result.message || "로그인에 실패했습니다.");
        } catch (error) {
            console.error("로그인 요청 실패:", error);
            alert("서버와의 연결에 문제가 발생했습니다.");
        }
    });
});
