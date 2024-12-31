document.addEventListener("DOMContentLoaded", () => {
  // 상담 내용 보기 버튼 클릭 이벤트
  const viewButtons = document.querySelectorAll(".view-btn");
  viewButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      const content = button.getAttribute("data-content"); // 상담 내용
      const answer = button.getAttribute("data-answer"); // 상담 답변

      // 모달에 내용 채우기
      document.getElementById("inquiryContent").innerText = content;
      document.getElementById("inquiryAnswer").innerText = answer
        ? answer
        : "답변이 없습니다.";

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
});

// 상담 목록 페이지 이동 버튼 (예시)
const inquiryBtn = document.getElementById("counselingInquiryBtn");
if (inquiryBtn) {
  inquiryBtn.addEventListener("click", (e) => {
    e.preventDefault(); // 기본 동작 방지 (폼 제출 등)
    window.location.href = "/center/counseling"; // 페이지 이동
  });
}

// 취소 버튼 클릭 시 폼 숨기기
const resetButton = document.querySelector('button[type="reset"]');
resetButton.addEventListener("click", function () {
  document.getElementById("form-container").style.display = "none"; // 폼 숨기기
});

// 수정 버튼 클릭 시 폼을 업데이트 하기 위한 설정
const editButtons = document.querySelectorAll(".edit-btn");
editButtons.forEach((button) => {
  button.addEventListener("click", (e) => {
    e.preventDefault();
    const counselingId = button.getAttribute("data-id");
    document.getElementById("counseling-id").value = counselingId;
    document.getElementById("form-container").style.display = "block"; // 폼 표시
  });
});

// 삭제 버튼 클릭 시 삭제 요청
const deleteButtons = document.querySelectorAll(".delete-btn");
deleteButtons.forEach((button) => {
  button.addEventListener("click", (e) => {
    const counselingId = button.getAttribute("data-id");
    if (confirm("정말로 삭제하시겠습니까?")) {
      // 서버로 삭제 요청 (예시)
      window.location.href = `/center/counseling/delete/${counselingId}`;
    }
  });
});

// 답변 미답변
function fetchInquiryDetails(id) {
  fetch(`/inquiries/${id}`)
    .then((response) => response.json())
    .then((data) => {
      // 상담 상세 정보 표시
      document.getElementById("detail-content").innerText =
        data.content || "내용 없음";
      document.getElementById("detail-answer").innerText =
        data.answer || "답변 없음";

      // 상태 업데이트
      if (data.status === "답변") {
        document.getElementById(`status-${id}`).innerText = "답변";
      }

      // 상담 상세 정보 패널 표시
      document.getElementById("inquiry-details").style.display = "block";
    })
    .catch((error) => {
      console.error("Error fetching inquiry details:", error);
      alert("상담 내용을 가져오는 중 오류가 발생했습니다.");
    });
}

function closeDetails() {
  // 상담 상세 정보 패널 숨기기
  document.getElementById("inquiry-details").style.display = "none";
}
