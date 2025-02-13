var count = 1;
var totalPrice = price;
let inCart = false;
// 상품 개수 증가
document.querySelector("#quantity-plus-btn").addEventListener("click", () => {
  console.log("플러스 버튼 클릭");
  if (count < 10) {
    count += 1;
    document.querySelector(".quantity-count").value = count;
    totalPrice = count * price;
    document.querySelector(".total-price").innerHTML = totalPrice + "원";
  }
});
// 상품 개수 감소
document.querySelector("#quantity-minus-btn").addEventListener("click", () => {
  console.log("마이너스 버튼 클릭");
  if (count > 1) {
    count -= 1;
    document.querySelector(".quantity-count").value = count;
    totalPrice = count * price;
    document.querySelector(".total-price").innerHTML = totalPrice + "원";
  }
});

console.log(productId);
console.log(count);
console.log(price);

const orderForm = document.querySelector("#orderForm");

document.querySelector(".btn-cart").addEventListener("click", async () => {
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log(isAuth);
  if (isAuth) {
    // 인증된 사용자일 경우 수행할 동작
    addToCart();
  } else {
    if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
      window.location.href = "/member/login"; // 로그인 페이지로 이동
    }
  }
});

function addToCart() {
  fetch(`/rest/cart/add/${productId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      quantity: count,
      price: price,
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 알림창 띄우기
      const goToCart = confirm(
        "상품이 장바구니에 추가되었습니다.\n장바구니로 이동하시겠습니까?"
      );
      if (goToCart) {
        window.location.href = "/cart/main?purchaseBtn=0"; // 장바구니 페이지로 이동
      }
    })
    .catch((error) => {
      console.error(error);
    });
}

document.querySelector(".btn-purchase").addEventListener("click", async () => {
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log(isAuth);
  // 인증된 사용자일 경우 수행할 동작
  if (isAuth) {
    if (inCart) {
      if (
        confirm(
          "이미 장바구니에 존재합니다. 장바구니 페이지로 이동하시겠습니까?"
        )
      ) {
        window.location.href = `/cart/main?purchaseBtn=${productId}`; // 장바구니 페이지로 이동
      }
    } else {
      fetch(`/rest/cart/add/${productId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          quantity: count,
          price: price,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);

          window.location.href = `/cart/main?purchaseBtn=${productId}`; // 장바구니 페이지로 이동
        })
        .catch((error) => {
          console.error(error);
        });
    }
  } else {
    if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
      window.location.href = "/member/login"; // 로그인 페이지로 이동
    }
  }
});

function isInCart() {
  fetch(`/rest/cart/${productId}`, { method: "GET" })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed");
      }
      return response.json();
    })
    .then((data) => {
      if (data) {
        inCart = true;
      }
      return data;
    })
    .catch((error) => {
      console.error(error);
    });
}
isInCart();
