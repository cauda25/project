document.addEventListener("DOMContentLoaded", function () {
  // 글 작성 버튼 클릭 시 폼 표시
  const createConsultationBtn = document.getElementById(
    "createConsultationBtn"
  );
  const formContainer = document.getElementById("form-container");

  createConsultationBtn.addEventListener("click", function () {
    formContainer.style.display = "block";
  });

  // 폼 리셋 버튼 클릭 시 폼 숨기기
  const resetButton = document.querySelector("form button[type='reset']");
  resetButton.addEventListener("click", function () {
    formContainer.style.display = "none";
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
