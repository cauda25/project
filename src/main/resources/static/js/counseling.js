document.addEventListener("DOMContentLoaded", () => {
  // 상담 내용 보기 버튼 클릭 이벤트
  const viewButtons = document.querySelectorAll(".view-btn");
  viewButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const content = button.getAttribute("data-content"); // 상담 내용
      const answer = button.getAttribute("data-answer"); // 상담 답변

      const contentElement = document.getElementById("inquiryContent");
      const answerElement = document.getElementById("inquiryAnswer");
      const modal = document.getElementById("inquiryDetailModal");

      if (contentElement && answerElement && modal) {
        contentElement.innerText = content;
        answerElement.innerText = answer || "답변이 없습니다.";
        modal.style.display = "block";
      }
    });
  });

  // 모달 닫기 이벤트
  const closeModal = document.querySelector(".close");
  if (closeModal) {
    closeModal.addEventListener("click", () => {
      const modal = document.getElementById("inquiryDetailModal");
      if (modal) modal.style.display = "none";
    });
  }

  // 모달 외부 클릭 시 닫기
  window.addEventListener("click", (event) => {
    const modal = document.getElementById("inquiryDetailModal");
    if (modal && event.target === modal) {
      modal.style.display = "none";
    }
  });

  // 체크박스 및 삭제 버튼 기능
  const selectAllCheckbox = document.getElementById("select-all");
  const deleteButton = document.getElementById("delete-selected");
  const deleteForm = document.getElementById("deleteForm");
  const inquiryIdsField = document.getElementById("inquiryIds");

  if (selectAllCheckbox) {
    selectAllCheckbox.addEventListener("change", function () {
      document.querySelectorAll(".select-checkbox").forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked;
      });
    });
  }

  // 선택된 항목 체크박스 상태 변경 시 전체 선택 체크박스 상태 갱신
  document.addEventListener("change", function (e) {
    if (e.target.classList.contains("select-checkbox")) {
      const allCheckboxes = document.querySelectorAll(".select-checkbox");
      const checkedCheckboxes = document.querySelectorAll(
        ".select-checkbox:checked"
      );

      if (selectAllCheckbox) {
        selectAllCheckbox.checked =
          allCheckboxes.length === checkedCheckboxes.length;
      }
    }
  });

  // 선택삭제 버튼 클릭 시
  if (deleteButton) {
    deleteButton.addEventListener("click", function () {
      const selectedCheckboxes = document.querySelectorAll(
        ".select-checkbox:checked"
      );
      if (selectedCheckboxes.length === 0) {
        alert("삭제할 항목을 선택해주세요.");
        return;
      }

      if (confirm("선택한 항목을 삭제하시겠습니까?")) {
        // 선택된 항목의 ID를 모아서 폼의 hidden 필드에 담기
        const selectedIds = Array.from(selectedCheckboxes)
          .map((checkbox) => checkbox.value)
          .join(",");

        inquiryIdsField.value = selectedIds; // inquiryIds 필드에 ID 값 설정

        // 폼 제출
        deleteForm.submit();
      }
    });
  }
  document
    .getElementById("searchButton")
    .addEventListener("click", function () {
      var searchTerm = document
        .getElementById("searchInput")
        .value.toLowerCase();
      var rows = document.querySelectorAll("#inquiryTable tr");

      rows.forEach(function (row) {
        var title = row
          .querySelector("td:nth-child(3)")
          .textContent.toLowerCase();
        var content = row
          .querySelector("td:nth-child(4)")
          .textContent.toLowerCase();
        var email = row
          .querySelector("td:nth-child(2)")
          .textContent.toLowerCase();

        if (
          title.includes(searchTerm) ||
          content.includes(searchTerm) ||
          email.includes(searchTerm)
        ) {
          row.style.display = "";
        } else {
          row.style.display = "none";
        }
      });
    });
});
