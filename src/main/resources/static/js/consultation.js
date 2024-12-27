document.addEventListener("DOMContentLoaded", function () {
  const createConsultationBtn = document.getElementById(
    "createConsultationBtn"
  );
  const formContainer = document.getElementById("form-container");
  const form = document.getElementById("consultationForm");
  const resetButton = document.querySelector("form button[type='reset']");

  // 글 작성 버튼 클릭 시 폼 표시
  createConsultationBtn.addEventListener("click", function () {
    form.reset(); // 기존 값 초기화
    formContainer.style.display = "block";
  });

  // 폼 리셋 버튼 클릭 시 폼 숨기기
  resetButton.addEventListener("click", function () {
    formContainer.style.display = "none";
  });

  // 수정 버튼 클릭 시 폼에 데이터 로드
  const editButtons = document.querySelectorAll(".edit-btn");
  editButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const id = button.getAttribute("data-id");
      const userId = button.getAttribute("data-user-id");
      const inquiryId = button.getAttribute("data-inquiry-id");
      const response = button.getAttribute("data-response");
      const status = button.getAttribute("data-status");
      const content = button.getAttribute("data-content");

      // 폼에 데이터 채우기
      document.getElementById("consultation-id").value = id;
      document.getElementById("userId").value = userId;
      document.getElementById("inquiryId").value = inquiryId;
      document.getElementById("response").value = response;
      document.getElementById("status").value = status;
      document.getElementById("content").value = content;

      formContainer.style.display = "block";
    });
  });

  // 삭제 버튼 클릭 이벤트 처리
  const deleteButtons = document.querySelectorAll(".delete-btn");
  deleteButtons.forEach((button) => {
    button.addEventListener("click", function (e) {
      e.preventDefault();
      const id = button.getAttribute("data-id");

      if (confirm("정말 삭제하시겠습니까?")) {
        fetch(`/center/consultation/delete?id=${id}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.querySelector("input[name='_csrf']").value,
          },
        })
          .then((response) => {
            if (response.ok) {
              alert("삭제되었습니다.");
              location.reload();
            } else {
              alert("삭제에 실패했습니다.");
            }
          })
          .catch((error) => console.error("Error:", error));
      }
    });
  });
});
