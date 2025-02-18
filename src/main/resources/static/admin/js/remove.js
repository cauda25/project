const movieForm = document.querySelector("#movieForm");

// 이벤트 위임으로 삭제 처리
movieForm.addEventListener("click", (e) => {
  const btn = e.target;

  // 삭제 버튼이 아닌 경우 무시
  if (!btn.classList.contains("fa-xmark")) return;

  const row = btn.closest("tr"); // 클릭된 버튼의 행
  const theaterId = row.getAttribute("data-theaterId"); // 행의 data-tno 속성 값 가져오기

  if (!theaterId) {
    alert("삭제할 데이터를 찾을 수 없습니다.");
    return;
  }

  if (!confirm("정보를 지우시겠습니까?")) return;

  // 서버에 DELETE 요청
  fetch(`/admin/page/movie/${theaterId}`, {
    method: "DELETE",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("삭제 요청 실패");
      }
      return response.text();
    })
    .then((data) => {
      alert(" 정상 처리되었습니다.");
      row.remove(); // 성공적으로 삭제된 행을 화면에서 제거
    })
    .catch((error) => {
      console.error("삭제 중 오류 발생:", error);
      alert("삭제에 실패했습니다.");
    });
});
