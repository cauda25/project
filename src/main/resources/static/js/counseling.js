document.addEventListener("DOMContentLoaded", () => {
  // 상담 내용 보기 버튼 클릭 이벤트
  const viewButtons = document.querySelectorAll(".view-btn");
  viewButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const content = button.getAttribute("data-content"); // 상담 내용
      const answer = button.getAttribute("data-answer"); // 상담 답변

      // 모달에 내용 채우기
      document.getElementById("inquiryContent").innerText = content;
      document.getElementById("inquiryAnswer").innerText =
        answer || "답변이 없습니다.";

      // 모달 표시
      const modal = document.getElementById("inquiryDetailModal");
      modal.style.display = "block";
    });
  });

  // 모달 닫기 이벤트
  const closeModal = document.querySelector(".close");
  closeModal.addEventListener("click", () => {
    const modal = document.getElementById("inquiryDetailModal");
    modal.style.display = "none";
  });

  // 모달 외부 클릭 시 닫기
  window.addEventListener("click", (event) => {
    const modal = document.getElementById("inquiryDetailModal");
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
  window.onload = function () {
    const selectAllCheckbox = document.getElementById("select-all");
    const deleteButton = document.getElementById("delete-selected");
    const deleteForm = document.getElementById("deleteForm");

    // 전체 선택 체크박스 기능
    selectAllCheckbox.addEventListener("change", function () {
      document.querySelectorAll(".select-checkbox").forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked;
      });
    });

    // 개별 체크박스 변경 시 전체 선택 체크 여부 업데이트
    document.addEventListener("change", function (e) {
      if (e.target.classList.contains("select-checkbox")) {
        const allCheckboxes = document.querySelectorAll(".select-checkbox");
        const checkedCheckboxes = document.querySelectorAll(
          ".select-checkbox:checked"
        );

        // 모든 체크박스가 선택되었으면 전체 선택 체크박스도 체크
        selectAllCheckbox.checked =
          allCheckboxes.length === checkedCheckboxes.length;
      }
    });

    // 선택삭제 버튼 클릭 이벤트
    deleteButton.addEventListener("click", function () {
      const selectedCheckboxes = document.querySelectorAll(
        ".select-checkbox:checked"
      );

      if (selectedCheckboxes.length === 0) {
        alert("삭제할 항목을 선택해주세요.");
        return;
      }

      if (confirm("선택한 항목을 삭제하시겠습니까?")) {
        deleteForm.submit();
      }
    });
  };

  // AJAX 요청으로 서버에 데이터 전달
  fetch("/center/inquiry/delete-selected", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ selectedIds: selectedIds }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("삭제에 실패했습니다.");
      }
      return response.json(); // 삭제 후 남은 데이터를 받음
    })
    .then((data) => {
      updateTable(data.inquiries);
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("삭제 처리 중 오류가 발생했습니다.");
    });
});

// 테이블 업데이트 함수
function updateTable(inquiries) {
  const tableBody = document.getElementById("inquiryTable");
  const totalCount = document.getElementById("totalCount");

  tableBody.innerHTML = ""; // 기존 테이블 초기화

  if (inquiries.length === 0) {
    tableBody.innerHTML = `<tr id="emptyRow"><td colspan="8">고객님의 상담 내역이 존재하지 않습니다.</td></tr>`;
  } else {
    inquiries.forEach((inquiry, index) => {
      const row = document.createElement("tr");
      row.innerHTML = `
          <td><input type="checkbox" name="selectedIds" value="${
            inquiry.id
          }" class="selectCheckbox" /></td>
          <td>${index + 1}</td>
          <td>${inquiry.member?.email || ""}</td>
          <td>${inquiry.content || ""}</td>
          <td>${inquiry.username || ""}</td>
          <td>${inquiry.type || ""}</td>
          <td>${inquiry.createdDate || ""}</td>
          <td>${inquiry.status || ""}</td>
        `;
      tableBody.appendChild(row);
    });
  }

  totalCount.textContent = inquiries.length; // 총 개수 갱신
  selectAllCheckbox.checked = false; // 전체 선택 초기화
}
// inquiry-link 클래스를 가진 링크 클릭 시 email.html로 이동
document.addEventListener("DOMContentLoaded", () => {
  const inquiryLink = document.querySelector(".inquiry-link");
  if (inquiryLink) {
    inquiryLink.addEventListener("click", (e) => {
      e.preventDefault();
      window.location.href = "/email";
    });
  }
});
