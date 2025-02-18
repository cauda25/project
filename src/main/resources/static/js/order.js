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

// checkout 버튼 클릭
document.querySelector(".checkout-btn").addEventListener("click", async () => {
  // 배송 정보
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const phoneNumber = document.getElementById("phoneNumber").value;
  // const address = document.getElementById("address").value;
  // const address2 = document.getElementById("address2").value;

  if (!isValidated) {
    return;
  }

  const paymentInfo = {
    pg: "html5_inicis.INIpayTest",
    pay_method: "card",
    merchant_uid: orderId,
    name: productName,
    amount: totalPrice,
    buyer_email: email,
    buyer_name: name,
    buyer_tel: phoneNumber,
    // buyer_addr: address + " " + address2,
    // buyer_postcode: "123-456",
  };

  preventExit = true; // 페이지 이동을 막기 위해 `beforeunload` 이벤트가 작동하지 않도록 flag 설정
  // 서버나 클라이언트 측에서 인증 여부를 확인
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log("로그인 여부: ", isAuth);
  if (isAuth) {
    // 인증된 사용자일 경우 수행할 동작
    // rsp = await payment(paymentInfo);
    // if (rsp.success == true) {
      sessionStorage.setItem("name", name);
      sessionStorage.setItem("email", email);
      sessionStorage.setItem("phone", phoneNumber);
      window.location.href = `/payment/success?orderId=${orderId}`;
    // } else {
      // alert(rsp.error_msg);
    // }
    // window.location.href = `/payment/success?orderId=${orderId}`;
    // fetch(`/rest/payment?orderId=${orderId}`, { method: "GET" })
    //   .then((response) => {
    //     if (!response.ok) {
    //       throw new Error("error");
    //     }
    //     return response.json();
    //   })
    //   .then((data) => {
    //     console.log(data);
    //     if (data == true) {
    //       window.location.href = `/payment/success?orderId=${orderId}`;
    //     } else if (data == false) {
    //       alert("결제 오류가 발생하였습니다. 다시 시도해주세요.");
    //       window.location.href = `/movie/main`;
    //     }
    //   });
  } else {
    window.location.href = "/member/login"; // 로그인 페이지로 이동
  }
});

// // 카드 정보 추출
// const cardNumber = document.getElementById("cc-number").value;
// const expiryDate = document.getElementById("cc-expiration").value;
// const cvv = document.getElementById("cc-cvv").value;
// const cardHolder = document.getElementById("cc-name").value;

// // 유효성 검사
// if (!cardNumber || !expiryDate || !cvv || !cardHolder) {
//   alert("모든 정보를 입력해주세요.");
//   return;
// }

// // 카드 번호 유효성 검사
// const cardNumberPattern = /^[0-9]{16}$/;
// if (!cardNumberPattern.test(cardNumber)) {
//   alert("카드 번호가 유효하지 않습니다.");
//   return;
// }

// // 유효 기간 검사 (MM/YY 형식으로 입력되었는지)
// const expiryDatePattern = /^(0[1-9]|1[0-2])\/\d{2}$/;
// if (!expiryDatePattern.test(expiryDate)) {
//   alert("유효 기간이 유효하지 않습니다. MM/YY 형식으로 입력해주세요.");
//   return;
// }

// // CVV 번호 유효성 검사 (3자리 숫자)
// const cvvPattern = /^[0-9]{3}$/;
// if (!cvvPattern.test(cvv)) {
//   alert("CVV 번호가 유효하지 않습니다.");
//   return;
// }
