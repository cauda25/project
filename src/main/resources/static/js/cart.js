let cartTotal = 0;
let checkedItems = [];

function getCartItems() {
  fetch("/rest/cart/list", { method: "GET" })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed");
      }
      return response.json();
    })
    .then((data) => {
      console.log(data);

      cartTotal = 0;
      let str = `<li class="list-group-item list-menu">`;
      str += `<div class="row"><div class="col-1 align-center">`;
      // str += `<div class="form-check">`;
      // str += `<input class="form-check-input" type="checkbox" value="" id="productSelectAll">`;
      // str += `</div>`;
      str += `</div>`;
      str += `<div class="col-7 text-center fw-bold">상품정보</div>`;
      str += `<div class="col-2 text-center fw-bold">수량</div>`;
      str += `<div class="col-2 text-center fw-bold">주문금액</div>`;
      str += `</div>`;
      // str += `<input type="hidden" class="quantity-count" name="" value="0">`;
      // str += `<input type="hidden" name="id" value="0">`;
      // str += `<input type="hidden" name="cartId" value="0">`;
      // str += `<input type="hidden" name="productId" value="0">`;
      // str += `<input type="hidden" name="price" value="0">`;
      // str += `<input type="hidden" name="price" value="0">`;
      str += `</li>`;
      data.forEach((cartItem) => {
        str += `<li class="list-group-item">`;
        str += `<div class="row justify-content-center">`;
        str += `<div class="col-1 d-flex align-items-center justify-content-center">`;
        str += `<div class="form-check">`;
        str += `<input class="form-check-input" type="checkbox" value="" id="productSelect" ${
          pId == 0 ? "checked" : cartItem.productDto.id == pId ? "checked" : ""
        }>`;
        str += `</div></div>`;
        str += `<div class="col-6">`;
        str += `<div class="row">`;
        str += `<div class="col-4">`;
        str += `<img src="/img/product${cartItem.productDto.image}" class="img-thumbnail item-pic">`;
        str += `</div>`;
        str += `<div class="col-6">`;
        str += `<div class="row"><span class="fs-2 fw-bold">${cartItem.productDto.name}</span></div>`;
        str += `<div class="row"><span class="fw-semibold text-secondary">${cartItem.productDto.itemDetails}</span></div>`;
        str += `<div class="row"><span class="text-secondary">${cartItem.productDto.price}</span></div>`;
        str += `</div></div></div>`;
        str += `<div class="col-1 d-flex justify-content-end">`;
        str += `<div><button type="button" class="btn btn-outline-dark delete-cart-item">X</button></div>`;
        str += `</div>`;
        str += `<div class="col-2 text-center d-flex align-items-center justify-content-center"><div>`;
        str += `<div class="input-group col-4 quantity">`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-minus-btn"><i class="fa-solid fa-minus"></i></button>`;
        str += `<input type="text" class="form-control border-danger bg-transparent text-center quantity-count" value="${cartItem.quantity}" readonly>`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-plus-btn"><i class="fa-solid fa-plus"></i></button>`;
        str += `</div>`;
        str += `<p class="text-center text-secondary">최대 10개</p>`;
        str += `</div></div>`;
        str += `<div class="col-2 text-center d-flex align-items-center justify-content-center">`;
        str += `<p class="itemTotal" value=${
          cartItem.price * cartItem.quantity
        }>${cartItem.price * cartItem.quantity}원</p>`;
        str += `</div></div>`;
        str += `<input type="hidden" name="id" value="${cartItem.id}">`;
        str += `<input type="hidden" name="cartId" value="${cartItem.cartId}">`;
        str += `<input type="hidden" name="productId" value="${cartItem.productDto.id}">`;
        str += `<input type="hidden" name="price" value="${cartItem.price}">`;
        str += `</li>`;
        if (pId == 0) {
          cartTotal += cartItem.price * cartItem.quantity;
          checkedItems.push(cartItem.productDto.id);
        } else {
          if (pId == cartItem.productDto.id) {
            cartTotal += cartItem.price * cartItem.quantity;
            checkedItems.push(cartItem.productDto.id);
          }
        }
      });
      str += `<li class="list-group-item cartTotal">`;
      str += `<div class="row">`;
      str += `<div class="cartTotal">총 결제금액 ${cartTotal}원</div>`;
      str += `</div></li>`;
      document.querySelector(".cart-item-list").innerHTML = str;
      ischeckedItems();
    })
    .catch((error) => {
      console.error(error);
    });
}
getCartItems();

document.querySelector(".cart-item-list").addEventListener("click", (e) => {
  console.log(".cart-item-list 클릭");
  const li = e.target.closest(".list-group-item");
  let quantity = parseInt(li.querySelector(".quantity-count").value);
  const id = li.querySelector("[name='id']").value;
  const cartId = li.querySelector("[name='cartId']").value;
  const productId = li.querySelector("[name='productId']").value;
  let price = parseInt(li.querySelector("[name='price']").value);
  let itemTotal = quantity * price;
  if (e.target.closest("#quantity-minus-btn")) {
    console.log("마이너스 클릭");
    if (quantity > 1) {
      quantity -= 1;
      li.querySelector(".quantity-count").value = quantity;
      itemTotal = quantity * price;
      cartTotal -= price;
      document.querySelector(".cartTotal").innerHTML =
        "총 결제금액 " + cartTotal + "원";
      li.querySelector(".itemTotal").innerHTML = itemTotal + "원";
      quantityChange();
    }
  }
  if (e.target.closest("#quantity-plus-btn")) {
    console.log("플러스 클릭");
    if (quantity < 10) {
      quantity += 1;
      li.querySelector(".quantity-count").value = quantity;
      itemTotal = quantity * price;
      cartTotal += price;
      document.querySelector(".cartTotal").innerHTML =
        "총 결제금액 " + cartTotal + "원";
      li.querySelector(".itemTotal").innerHTML = itemTotal + "원";
      quantityChange();
    }
  }
  // if (e.target.matches(".btn-modify-quantity")) {
  //   console.log("Button clicked!", cartId);

  // }
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
        console.log(data);
      })
      .catch((error) => {
        console.error(error);
      });
  }
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
  if (e.target.matches("#productSelect")) {
    if (e.target.checked) {
      console.log(productId, "선택 체크박스 체크 활성화");
      checkedItems.push(productId);
      cartTotal += itemTotal;
      document.querySelector(".cartTotal").innerHTML =
        "총 결제금액 " + cartTotal + "원";
    } else {
      console.log(productId, "선택 체크박스 체크 비활성화");
      checkedItems.pop(productId);
      cartTotal -= itemTotal;
      document.querySelector(".cartTotal").innerHTML =
        "총 결제금액 " + cartTotal + "원";
    }
    ischeckedItems();
  }
});

// document.querySelector("#productSelectAll").addEventListener("change", (e)=>{
//   console.log("전체 선택 버튼 클릭");
//           // productSelectAll 체크박스 상태에 맞게 다른 체크박스들 활성화/비활성화
//           document.querySelectorAll("#productSelect").forEach(function(checkbox) {
//             checkbox.checked = e.target.checked; // 다른 체크박스들 상태 변경
//             checkbox.disabled = !e.target.checked; // 전체 선택이 비활성화되면, 다른 체크박스들 비활성화
//         });

// });

function ischeckedItems() {
  if (checkedItems.length > 0) {
    document.querySelector(".btn-checkout").removeAttribute("disabled");
  } else {
    document.querySelector(".btn-checkout").setAttribute("disabled", "");
  }
}
ischeckedItems();

document.querySelector(".btn-checkout").addEventListener("click", async () => {
  console.log(checkedItems);
  const isAuth = await checkAuth(); // checkAuth()가 완료될 때까지 기다림
  console.log("로그인 여부: ", isAuth);

  if (isAuth) {
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
        console.log(data);
        orderId = data[0].orderId;
        window.location.href = `/payment/payment?orderId=${orderId}`;
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
