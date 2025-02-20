let cartTotal = 0;
let checkedItems = [];

// 카트 목록 출력
function getCartItems() {
  fetch("/rest/cart/list", { method: "GET" })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed");
      }
      return response.json();
    })
    .then((data) => {
      console.log("장바구니 데이터: ", data);

      cartTotal = 0;
      let str = `<li class="list-group-item list-menu bg-transparent rounded-0">`;
      str += `<div class="row"><div class="col-1 align-center">`;
      str += `</div>`;
      str += `<div class="col-7 text-center fw-bold text-white">상품정보</div>`;
      str += `<div class="col-2 text-center fw-bold text-white">수량</div>`;
      str += `<div class="col-2 text-center fw-bold text-white">주문금액</div>`;
      str += `</div>`;
      str += `</li>`;
      if (data.length <= 0) {
        str += `<li class="list-group-item list-item bg-transparent mt-5 mb-5">`;
        str += `<h class="d-flex justify-content-center text-light fw-bold fs-5 m-0">장바구니에 상품이 없습니다.</h></li>`;
      }
      data.forEach((cartItem) => {
        str += `<li class="list-group-item list-item bg-transparent">`;
        str += `<div class="row justify-content-center">`;
        str += `<div class="col-1 d-flex align-items-center justify-content-center">`;
        str += `<div class="form-check">`;
        str += `<input class="form-check-input" type="checkbox" id="productSelect" ${
          pId == 0 ? "checked" : cartItem.productDto.id == pId ? "checked" : ""
        }>`;
        str += `</div></div>`;
        str += `<div class="col-6">`;
        str += `<div class="row">`;
        str += `<div class="col-4"><a href="/store/detail?category=${cartItem.productDto.category}&id=${cartItem.productDto.id}">`;
        str += `<img src="/img/product${cartItem.productDto.image}" class="img-thumbnail item-pic"></a>`;
        str += `</div>`;
        str += `<div class="col-6">`;
        str += `<div class="row"><a href="/store/detail?category=${cartItem.productDto.category}&id=${cartItem.productDto.id}" class=""><span class="fs-2 fw-bold text-light">${cartItem.productDto.name}</span></a></div>`;
        str += `<div class="row"><span class="fw-semibold text-white-50">${cartItem.productDto.itemDetails}</span></div>`;
        str += `<div class="row"><span class="text-white-50">${cartItem.productDto.price}원</span></div>`;
        str += `</div></div></div>`;
        str += `<div class="col-1 d-flex justify-content-end">`;
        str += `<div><button type="button" class="btn btn-outline-light fw-bold delete-cart-item">X</button></div>`;
        str += `</div>`;
        str += `<div class="col-2 text-center d-flex align-items-center justify-content-center"><div class="mt-4">`;
        str += `<div class="input-group col-4 quantity">`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-minus-btn"><i class="fa-solid fa-minus"></i></button>`;
        str += `<input type="text" class="form-control border-danger bg-transparent text-center quantity-count fw-bold text-light" value="${cartItem.quantity}" readonly>`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-plus-btn"><i class="fa-solid fa-plus"></i></button>`;
        str += `</div>`;
        str += `<p class="text-center text-white-50">최대 10개</p>`;
        str += `</div></div>`;
        str += `<div class="col-2 text-center d-flex align-items-center justify-content-center">`;
        str += `<p class="itemTotal fs-4 fw-medium text-light" value=${
          cartItem.price * cartItem.quantity
        }>${cartItem.price * cartItem.quantity}원</p>`;
        str += `</div></div>`;
        str += `<input type="hidden" name="id" value="${cartItem.id}">`;
        str += `<input type="hidden" name="cartId" value="${cartItem.cartId}">`;
        str += `<input type="hidden" name="productId" value="${cartItem.productDto.id}">`;
        str += `<input type="hidden" name="price" value="${cartItem.price}">`;
        str += `</li>`;
      });
      str += `<li class="list-group-item list-total bg-transparent rounded-0">`;
      str += `<div class="row">`;
      str += `<div class="cartTotal d-flex justify-content-end fs-4 fw-semibold text-light">총 결제금액 ${cartTotal}원</div>`;
      str += `</div></li>`;
      document.querySelector(".cart-item-list").innerHTML = str;
      updateInfo();
    })
    .catch((error) => {
      console.error(error);
    });
}
getCartItems();

// 클릭 시
document.querySelector(".cart-item-list").addEventListener("click", (e) => {
  const li = e.target.closest(".list-group-item");
  let quantity = parseInt(li.querySelector(".quantity-count").value);
  const id = li.querySelector("[name='id']").value;
  const cartId = li.querySelector("[name='cartId']").value;
  const productId = li.querySelector("[name='productId']").value;
  let price = parseInt(li.querySelector("[name='price']").value);
  console.log("클릭 상품 ID: ", productId);
  // 수량 마이너스 버튼 클릭 시
  if (e.target.closest("#quantity-minus-btn")) {
    console.log("마이너스 클릭");
    if (quantity > 1) {
      quantity -= 1;
      li.querySelector(".quantity-count").value = quantity;
      li.querySelector(".itemTotal").value = price * quantity;
      quantityChange();
    }
  }
  // 수량 플러스 버튼 클릭 시
  if (e.target.closest("#quantity-plus-btn")) {
    console.log("플러스 클릭");
    if (quantity < 10) {
      quantity += 1;
      li.querySelector(".quantity-count").value = quantity;
      li.querySelector(".itemTotal").value = price * quantity;
      quantityChange();
    }
  }

  // 데이터베이스 수량 변경
  function quantityChange() {
    fetch(`/rest/cart/modify/${productId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id: id,
        cartId: cartId,
        quantity: quantity,
        price: price,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("변경 후 상품 데이터: ", data);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  // 삭제 버튼 클릭 시 데이터 베이스 변경
  if (e.target.closest(".delete-cart-item")) {
    fetch(`/rest/cart/delete/${id}`, {
      method: "DELETE",
    })
      .then((response) => response.text())
      .then((data) => {
        console.log(data);
        getCartItems();
      })
      .catch((error) => {
        console.error(error);
      });
  }
  updateInfo();
});

// 구매하기 버튼 클릭 시
document.querySelector(".btn-checkout").addEventListener("click", async () => {
  console.log("선택된 상품 목록: ", checkedItems);
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log("로그인 여부: ", isAuth);

  if (isAuth && checkedItems.length > 0) {
    // 인증된 사용자일 경우 수행할 동작
    fetch(`/rest/payment`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(checkedItems),
    })
      .then((response) => response.json())
      .then((data) => {
        if (checkedItems.length > 0) {
          console.log("결제할 상품 목록: ", data);
          orderId = data[0].orderId;
          window.location.href = `/payment/payment?orderId=${orderId}`;
        } else {
          alert("오류가 발생했습니다. 다시 시도해주세요.");
          window.location.href = `/payment/cart?purchaseBtn=${pId}`;
        }
      })
      .catch((error) => {
        console.error(error);
      });
  } else {
    if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
      window.location.href = "/member/login"; // 로그인 페이지로 이동
    }
  }
});

// 정보 변경 시
function updateInfo() {
  cartItems = document.querySelectorAll(".list-group-item.list-item");
  checkedItems = [];
  cartTotal = 0;
  cartItems.forEach((item) => {
    const productId = item.querySelector("[name='productId']").value;
    const quantity = parseInt(item.querySelector(".quantity-count").value);
    const price = parseInt(item.querySelector("[name='price']").value);

    item.querySelector(".itemTotal").innerHTML = price * quantity + "원";

    // 상품이 선택된 상태일 경우
    if (item.querySelector("#productSelect").checked) {
      // 총 결제금액에 추가
      cartTotal += price * quantity;
      // 선택된 상품 리스트에 추가
      checkedItems.push(productId);
    }
  });

  // 선택된 상품이 존재할 경우
  if (checkedItems.length > 0) {
    // 구매하기 버튼 활성화
    document.querySelector(".btn-checkout").removeAttribute("disabled");
  } else {
    // 존재하지 않을 경우 구매하기 버튼 비활성화
    document.querySelector(".btn-checkout").setAttribute("disabled", "");
  }

  document.querySelector(".cartTotal").innerHTML =
    "총 결제금액 " + cartTotal + "원";

  console.log("선택된 상품 목록: ", checkedItems);
}
