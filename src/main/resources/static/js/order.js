console.log(orderId);

window.addEventListener("beforeunload", function () {
  fetch(`/rest/exit?orderId=${orderId}`, { method: "POST" })
    .then((response) => response.json())
    .then((data) => {
      console.log("Order items removed from database.");
    })
    .catch((error) => {
      console.error("Error removing order items:", error);
    });
});

IMP.init("html5_inicis.INIpayTest");

document.querySelector(".checkout-btn").addEventListener("click", () => {
  IMP.request_pay(
    {
      pg: "html5_inicis.INIpayTest",
      pay_method: "card",
      merchant_uid: "order_no_0001", // 상점에서 관리하는 주문 번호를 전달
      name: "주문명:결제테스트",
      amount: 14000,
      buyer_email: "iamport@siot.do",
      buyer_name: "구매자이름",
      buyer_tel: "010-1234-5678",
      buyer_addr: "서울특별시 강남구 삼성동",
      buyer_postcode: "123-456",
      m_redirect_url: "{모바일에서 결제 완료 후 리디렉션 될 URL}", // 예: https://www.my-service.com/payments/complete/mobile
    },
    function (rsp) {
      // callback 로직
      //* ...중략 (README 파일에서 상세 샘플코드를 확인하세요)... *//
    }
  );
});
