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

document.querySelector(".checkout-btn").addEventListener("click", () => {
  // 카드 정보 추출
  const cardNumber = document.getElementById("cc-number").value;
  const expiryDate = document.getElementById("cc-expiration").value;
  const cvv = document.getElementById("cc-cvv").value;
  const cardHolder = document.getElementById("cc-name").value;

  // 유효성 검사
  if (!cardNumber || !expiryDate || !cvv || !cardHolder) {
    alert("모든 정보를 입력해주세요.");
    return;
  }

  // 카드 번호 유효성 검사 (예시로 간단히 체크)
  const cardNumberPattern = /^[0-9]{16}$/;
  if (!cardNumberPattern.test(cardNumber)) {
    alert("카드 번호가 유효하지 않습니다.");
    return;
  }

  // 유효 기간 검사 (MM/YY 형식으로 입력되었는지)
  const expiryDatePattern = /^(0[1-9]|1[0-2])\/\d{2}$/;
  if (!expiryDatePattern.test(expiryDate)) {
    alert("유효 기간이 유효하지 않습니다. MM/YY 형식으로 입력해주세요.");
    return;
  }

  // CVV 번호 유효성 검사 (3자리 숫자)
  const cvvPattern = /^[0-9]{3}$/;
  if (!cvvPattern.test(cvv)) {
    alert("CVV 번호가 유효하지 않습니다.");
    return;
  }

  const IMP = window.IMP; // 생략 가능
  IMP.init("imp56626883"); // 예: imp00000000a

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
        // 결제 진행
        IMP.request_pay(
          {
            pg: "html5_inicis.INIpayTest",
            pay_method: "card",
            merchant_uid: "57008833-33004",
            name: productName,
            amount: totalPrice,
            buyer_email: "Iamport@chai.finance",
            buyer_name: "포트원 기술지원팀",
            buyer_tel: "010-1234-5678",
            buyer_addr: "서울특별시 강남구 삼성동",
            buyer_postcode: "123-456",
          },
          function (rsp) {
            // callback
            //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
            console.log(data);

            // window.location.href = `/payment-success?orderId=${orderId}`;
          }
        );
      } else {
        window.location.href = "/member/login"; // 로그인 페이지로 이동
      }
    })
    .catch((error) => {
      console.error("Error adding to cart:", error);
    });
});
