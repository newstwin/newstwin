async function csrfFetch(url, options = {}) {
    // 기본값 세팅
    const opts = {
        credentials: "include", // 쿠키 포함
        ...options,
        headers: {
            "X-XSRF-TOKEN": getCsrfToken(),
            ...(options.headers || {})
        }
    };
    return fetch(url, opts);
}

function getCsrfToken() {
    const m = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return m ? decodeURIComponent(m[1]) : "";
}
