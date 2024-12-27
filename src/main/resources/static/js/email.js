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
  button.addEventListener("click", function () {
    const id = this.getAttribute("data-id");
    if (confirm("정말 삭제하시겠습니까?")) {
      fetch(`/inquiries/${id}`, { method: "DELETE" }).then(() =>
        location.reload()
      );
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
