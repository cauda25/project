// 글 작성 폼 열기
document
  .getElementById("emailInquiryBtn")
  .addEventListener("click", function () {
    document.getElementById("form-container").style.display = "block";
    document.getElementById("inquiryForm").reset();
    document.getElementById("inquiry-id").value = "";
  });

// 글 작성 폼 닫기
document.getElementById("closeFormBtn").addEventListener("click", function () {
  document.getElementById("form-container").style.display = "none";
});

// 수정 버튼 클릭 처리
document.querySelectorAll(".edit-btn").forEach((button) => {
  button.addEventListener("click", function () {
    const id = this.getAttribute("data-id");
    fetch(`/inquiries/${id}`)
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("form-container").style.display = "block";
        document.getElementById("inquiry-id").value = data.id;
        document.getElementById("name").value = data.name;
        document.getElementById("email").value = data.email;
        document.getElementById("message").value = data.message;
      });
  });
});

// 삭제 버튼 클릭 처리
document.querySelectorAll(".delete-btn").forEach((button) => {
  button.addEventListener("click", function (e) {
    e.preventDefault();
    const form = this.closest("form");
    if (confirm("정말 삭제하시겠습니까?")) {
      form.submit();
    }
  });
});

// 문의 폼 제출 처리
document.getElementById("inquiryForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const id = document.getElementById("inquiry-id").value;
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const message = document.getElementById("message").value;

  const requestData = {
    id,
    name,
    email,
    message,
  };

  const method = id ? "PUT" : "POST";
  const url = id ? `/inquiries/${id}` : "/inquiries";

  fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestData),
  }).then(() => {
    location.reload();
  });
});

function fetchInquiries(page) {
  fetch(`/inquiries?page=${page}`)
    .then((response) => response.json())
    .then((data) => {
      renderInquiries(data.content); // content: 문의 목록
      renderPagination(data.totalPages, page); // 페이지 정보
    })
    .catch((error) => console.error("Error:", error));
}

// 예시로 로그인된 사용자 정보 가져오기 (로그인된 사용자의 정보는 서버에서 받아옴)
const userInfo = {
  name: "홍길동", // 로그인한 사용자의 이름
  mobile: "010-1234-5678", // 로그인한 사용자의 전화번호
  email: "honggildong@example.com", // 로그인한 사용자의 이메일
};

// 해당 정보들 동적으로 채우기
document.getElementById("user_name").innerText = userInfo.name;
document.getElementById("user_mobile").innerText = userInfo.mobile;
document.getElementById("user_email").innerText = userInfo.email;
document.getElementById("HiddenUserName").value = userInfo.name;
document.getElementById("HiddenMobile1").value = userInfo.mobile;
document.getElementById("HiddenEmail1").value = userInfo.email;
