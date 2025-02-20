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
    // sessionStorage.setItem("name", name);
    // sessionStorage.setItem("email", email);
    // sessionStorage.setItem("phone", phoneNumber);
    // window.location.href = `/payment/success?orderId=${orderId}`;

    rsp = await payment(paymentInfo);
    if (rsp.success == true) {
      sessionStorage.setItem("name", name);
      sessionStorage.setItem("email", email);
      sessionStorage.setItem("phone", phoneNumber);
      window.location.href = `/payment/success?orderId=${orderId}`;
    } else {
      alert(rsp.error_msg);
      return;
    }
  } else {
    window.location.href = "/member/login"; // 로그인 페이지로 이동
  }
});

async function timeCheck() {
  try {
    const response = await fetch(`/rest/payment?orderId=${orderId}`, {
      method: "GET",
    });
    if (!response.ok) {
      throw new Error("error");
    }
    const data = await response.json();
    console.log(data);
    return data; // true 또는 false 반환
  } catch (error) {
    console.error(error);
    return false; // 실패 시 false 반환
  }
}
