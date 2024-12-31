console.log(orderId);
let preventExit = false; // 페이지를 떠나는 것을 막을 조건을 설정하는 flag


window.addEventListener("beforeunload", function () {
  if (preventExit) {
    console.log("Exit prevented, not sending the request.");
    preventExit = false; // 요청을 보냈으므로 다시 초기화
    return; // 요청을 보내지 않도록 함
  }

  fetch(`/rest/exit?orderId=${orderId}`, { method: "POST" })
    .then((response) => response.json())
    .then((data) => {
      console.log("Order items removed from database.");
    })
    .catch((error) => {
      console.error("Error removing order items:", error);
    });
});

document.querySelector(".checkout-btn").addEventListener("click",()=>{
  preventExit = true; // 페이지 이동을 막기 위해 `beforeunload` 이벤트가 작동하지 않도록 flag 설정
  // 서버나 클라이언트 측에서 인증 여부를 확인
  fetch("/rest/check-auth", { method: "GET" }) // 예시로 인증 상태 확인 API 호출
    .then((response) => {
      if (!response.ok) {
        throw new Error("Not authenticated");
      }
      return response.json();
    })
    .then((data) => {
      if (data == true) {
        // 인증된 사용자일 경우 수행할 동작
        window.location.href = `/payment-success?orderId=${orderId}`;
      }else{
            window.location.href = "/member/login"; // 로그인 페이지로 이동

      }
    })
    .catch((error) => {
      console.error("Error adding to cart:", error);
    });
})
