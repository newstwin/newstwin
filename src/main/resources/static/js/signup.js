document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signupForm");

    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const confirmInput = document.getElementById("confirmPassword");
    const nicknameInput = document.getElementById("nickname");

    const emailFeedback = document.getElementById("emailFeedback");
    const passwordFeedback = document.getElementById("passwordFeedback");
    const confirmFeedback = document.getElementById("confirmFeedback");
    const checkEmailBtn = document.getElementById("checkEmailBtn");

    const togglePasswordBtn = document.getElementById("togglePassword");
    const toggleConfirmBtn = document.getElementById("toggleConfirm");

    let isEmailAvailable = false;

    const params = new URLSearchParams(window.location.search);

    const emailParam = params.get("email");
    if (emailParam) emailInput.value = emailParam;


    togglePasswordBtn.addEventListener("click", () => {
        const isHidden = passwordInput.type === "password";
        passwordInput.type = isHidden ? "text" : "password";
        const icon = togglePasswordBtn.querySelector("i");
        icon.classList.toggle("bi-eye", isHidden);
        icon.classList.toggle("bi-eye-slash", !isHidden);
    });

    toggleConfirmBtn.addEventListener("click", () => {
        const isHidden = confirmInput.type === "password";
        confirmInput.type = isHidden ? "text" : "password";
        const icon = toggleConfirmBtn.querySelector("i");
        icon.classList.toggle("bi-eye", isHidden);
        icon.classList.toggle("bi-eye-slash", !isHidden);
    });

    checkEmailBtn.addEventListener("click", async () => {
        const email = emailInput.value.trim();
        if (!validateEmail(email)) {
            emailFeedback.textContent = "올바른 이메일 형식을 입력해주세요.";
            emailFeedback.className = "invalid";
            return;
        }

        try {
            const res = await fetch(`/api/members/check-email?email=${encodeURIComponent(email)}`);
            const data = await res.json();

            if (res.ok && data.success) {
                emailFeedback.textContent = "사용 가능한 이메일입니다.";
                emailFeedback.className = "valid";
                isEmailAvailable = true;
            } else {
                emailFeedback.textContent = "이미 사용 중인 이메일입니다.";
                emailFeedback.className = "invalid";
                isEmailAvailable = false;
            }
        } catch (err) {
            emailFeedback.textContent = "이메일 확인 중 오류가 발생했습니다.";
            emailFeedback.className = "invalid";
            console.error(err);
        }
    });

    passwordInput.addEventListener("input", () => {
        const password = passwordInput.value.trim();
        if (validatePassword(password)) {
            passwordFeedback.textContent = "사용 가능한 비밀번호입니다.";
            passwordFeedback.className = "valid";
        } else {
            passwordFeedback.textContent = "8~20자, 영문과 숫자를 포함해야 합니다.";
            passwordFeedback.className = "invalid";
        }

        validateConfirmPassword();
    });

    confirmInput.addEventListener("input", validateConfirmPassword);

    function validateConfirmPassword() {
        const password = passwordInput.value.trim();
        const confirm = confirmInput.value.trim();
        if (confirm.length === 0) {
            confirmFeedback.textContent = "";
            return;
        }

        if (password === confirm) {
            confirmFeedback.textContent = "비밀번호가 일치합니다.";
            confirmFeedback.className = "valid";
        } else {
            confirmFeedback.textContent = "비밀번호가 일치하지 않습니다.";
            confirmFeedback.className = "invalid";
        }
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const submitBtn = form.querySelector("button[type='submit']");
        submitBtn.disabled = true;
        submitBtn.textContent = "처리 중...";

        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();
        const confirmPassword = confirmInput.value.trim();
        const memberName = nicknameInput.value.trim();

        if (!isEmailAvailable) {
            alert("이메일 중복 확인을 먼저 해주세요.");
            submitBtn.disabled = false;
            submitBtn.textContent = "회원가입";
            return;
        }

        if (!validatePassword(password)) {
            alert("비밀번호 형식이 올바르지 않습니다.");
            submitBtn.disabled = false;
            submitBtn.textContent = "회원가입";
            return;
        }

        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            submitBtn.disabled = false;
            submitBtn.textContent = "회원가입";
            return;
        }

        if (memberName.length < 2) {
            alert("닉네임은 2자 이상 입력해주세요.");
            submitBtn.disabled = false;
            submitBtn.textContent = "회원가입";
            return;
        }

        try {
            const response = await fetch("/api/members/signup", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password, memberName }),
            });

            const data = await response.json();

            if (response.ok && data.success) {
                alert("회원가입이 완료되었습니다. 이메일 인증 메일을 확인해주세요!");
                window.location.href = "/verify-info";
            } else {
                alert(data.message || "회원가입에 실패했습니다.");
                submitBtn.disabled = false;
                submitBtn.textContent = "회원가입";
            }
        } catch (err) {
            console.error("회원가입 요청 오류:", err);
            alert("서버와 통신 중 문제가 발생했습니다.");

            submitBtn.disabled = false;
            submitBtn.textContent = "회원가입";
        }
    });

    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function validatePassword(password) {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/;
        return passwordRegex.test(password);
    }
});
