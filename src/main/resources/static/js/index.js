document.addEventListener("DOMContentLoaded", () => {
    fetchPopularNews();
    fetchCategories();
    fetchCommunityTopics();
    setupNewsletterForm();
});

// 오늘의 인기 뉴스
async function fetchPopularNews() {
    const container = document.getElementById("popularNewsContainer");

    try {
        const res = await fetch("/api/home/popular-news");
        const newsList = await res.json();

        container.innerHTML = newsList.map(news => {
            const thumbnail = news.thumbnailUrl && news.thumbnailUrl.trim() !== ""
                ? news.thumbnailUrl
                : "/images/default-news2.jpg";

            const summary = news.content
                ? news.content.replace(/[#>*_`]/g, "").slice(0, 120) + "..."
                : "요약 준비 중입니다.";

            return `
                <div class="col-md-4">
                    <div class="card home-news-card" onclick="window.location.href='/post/${news.id}'">
                        <div class="thumb-wrap">
                            <img src="${thumbnail}" alt="${news.title}">
                        </div>

                        <div class="card-body">

                            <h6 class="news-title">${news.title}</h6>

                            <div class="news-meta">
                                ${news.createdAt} · 조회 ${news.count}
                            </div>

                            <p class="news-summary">${summary}</p>
                        </div>
                    </div>
                </div>
            `;
        }).join("");

    } catch (e) {
        console.error("인기 뉴스 로드 실패:", e);
    }
}


// AI 카테고리
async function fetchCategories() {
    const container = document.getElementById("categoryContainer");
    try {
        const res = await fetch("/api/home/categories");
        const categories = await res.json();

        container.innerHTML = categories.map(cat => `
            <button class="category-btn" onclick="goToCategory('${cat.categoryName}')">
                ${cat.categoryName}
            </button>
        `).join("");
    } catch (e) {
        console.error("카테고리 로드 실패:", e);
    }
}

function goToCategory(cat) {
    window.location.href = `/news?category=${encodeURIComponent(cat)}`;
}

// 커뮤니티 인기 토픽
async function fetchCommunityTopics() {
    const container = document.getElementById("communityList");

    try {
        const res = await fetch("/api/home/popular-community");
        const topics = await res.json();

        container.innerHTML = topics.map(topic => {

            const preview = topic.content
                ? topic.content.replace(/[#>*_`]/g, "").slice(0, 50) + "..."
                : "내용 없음";

            return `
                    <div class="community-card" onclick="window.location.href='/board/${topic.id}'">
            
                        <div class="community-title">${topic.title}</div>
            
                        <div class="community-summary">${preview}</div>
            
                        <div class="community-meta">
                            ${topic.author} · ${topic.createdAt} · 댓글 ${topic.count}
                        </div>
                    </div>
                `;
        }).join("");

    } catch (err) {
        console.error("커뮤니티 로드 실패:", err);
    }
}


// 뉴스레터 구독
function setupNewsletterForm() {
    const form = document.getElementById("newsletterForm");
    const emailInput = document.getElementById("newsletterEmail");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = emailInput.value.trim();
        if (!validateEmail(email)) {
            alert("올바른 이메일 주소를 입력해주세요.");
            return;
        }

        try {
            const res = await fetch(`/api/members/exists?email=${encodeURIComponent(email)}`);
            const data = await res.json();

            if (!res.ok) throw new Error("서버 오류");

            if (data.data === true) {
                if (confirm("이미 가입된 이메일입니다. 로그인 후 구독 상태를 관리하시겠습니까?")) {
                    sessionStorage.setItem("newsletterInfo", "로그인 후 구독을 관리할 수 있습니다.");
                    window.location.href = "/login";
                }
            } else {
                if (confirm("입력하신 이메일로 회원가입을 진행하시겠습니까?")) {
                    window.location.href = `/signup?email=${encodeURIComponent(email)}`;
                }
            }
        } catch (err) {
            console.error("구독 확인 오류:", err);
            alert("서버와 통신 중 문제가 발생했습니다.");
        }
    });
}

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}
