let count = 1;
let totalPrice = price;
let inCart = false;

async function main() {
  inCart = await isInCart();
  printInfo();
}
main();

// 상품 개수 증가
document.querySelector("#quantity-plus-btn").addEventListener("click", () => {
  console.log("플러스 버튼 클릭");
  if (count < 10) {
    count += 1;
    document.querySelector(".quantity-count").value = count;
    totalPrice = count * price;
    document.querySelector(".total-price").innerHTML = totalPrice + "원";
  }
  printInfo();
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
  printInfo();
});

// 장바구니 버튼 클릭
document.querySelector(".btn-cart").addEventListener("click", async () => {
  console.log("장바구니 버튼 클릭");
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log("로그인 여부: ", isAuth);
  inCart = await isInCart();
  if (isAuth) {
    // 인증된 사용자일 경우 수행할 동작
    if (inCart) {
      if (confirm("이미 장바구니에 존재합니다. 장바구니에 추가하시겠습니까?")) {
        addToCart();
      }
    } else{
      addToCart();
    }
  } else {
    if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
      window.location.href = "/member/login"; // 로그인 페이지로 이동
    }
  }
  printInfo();
});

// 장바구니 추가
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
  printInfo();
}

// 구매하기 버튼 클릭
document.querySelector(".btn-purchase").addEventListener("click", async () => {
  console.log("구매하기 버튼 클릭");
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log("로그인 여부: ", isAuth);
  inCart = await isInCart();
  // 인증된 사용자일 경우 수행할 동작
  if (isAuth) {
    if (inCart) {
      // 장바구니에 이미 존재하는 상품일 경우 장바구니에 상품 추가 X
      if (
        confirm(
          "이미 장바구니에 존재합니다. 장바구니 페이지로 이동하시겠습니까?"
        )
      ) {
        window.location.href = `/cart/main?purchaseBtn=${productId}`; // 장바구니 페이지로 이동
      }
    } else {
      // 장바구니에 없는 상품일 경우 장바구니에 상품 추가
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
  printInfo();
});

// 장바구니에 이미 있는 상품인지 확인
async function isInCart() {
  try {
    const response = await fetch(`/rest/cart/${productId}`, { method: "GET" });
    if (!response.ok) {
      throw new Error("Failed");
    }
    const data = await response.json();
    return data; // 데이터를 반환
  } catch (error) {
    console.error(error);
    return null; // 에러가 발생하면 null을 반환
  }
}

// 정보 출력
function printInfo() {
  console.log("상품 번호: ", productId);
  console.log("상품 가격: ", price);
  console.log("상품 개수: ", count);
  console.log("상품 총가격: ", totalPrice);
  console.log("장바구니 안 상품 여부: ", inCart);
}
