// var id = 0;
// var CartId = 0;
// var productId = 0;
// var quantity = 0;
// var price = 0;

function getCartItems() {
    fetch("/rest/cart/list", { method: "GET" }) // 예시로 인증 상태 확인 API 호출
    .then((response) => {
      if (!response.ok) {
        throw new Error("Not authenticated");
      }
      return response.json();
    })
    .then((data) => {
    console.log(data);
    
    let str = `<li class="list-group-item list-menu">메뉴</li>`;
    data.forEach(cartItem => {
        str += `<li class="list-group-item">`;
        str += `<div class="row justify-content-center">`;
        str += `<div class="col-1">`;
        str += `<div class="form-check">`;
        str += `<input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">`;
        str += `</div></div>`;
        str += `<div class="col-6">`;
        str += `<div class="row">`;
        str += `<div class="col-4">`;
        str += `<img src="/img/product${cartItem.productDto.image}" class="img-thumbnail item-pic">`;
        str += `</div>`;
        str += `<div class="col-6">`;
        str += `<p>${cartItem.productDto.name}</p>`;
        str += `<p>${cartItem.productDto.itemDetails}</p>`;
        str += `<p>${cartItem.productDto.price}</p>`;
        str += `</div></div></div>`;
        str += `<div class="col-1">`;
        str += `<button type="button" class="btn btn-outline-dark delete-cart-item">X</button>`;
        str += `</div>`;
        str += `<div class="col-2">`;
        str += `<div class="input-group col-4 quantity">`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-minus-btn"><i class="fa-solid fa-minus"></i></button>`;
        str += `<input type="text" class="form-control border-danger bg-transparent text-center quantity-count" value="${cartItem.quantity}" readonly>`;
        str += `<button class="btn btn-outline-danger" type="button" id="quantity-plus-btn"><i class="fa-solid fa-plus"></i></button>`;
        str += `</div>`;
        str += `<button class="btn btn-outline-dark btn-modify-quantity" type="button">수정</button>`;
        str += `</div>`;
        str += `<div class="col-2">`;
        str += `<p class="totalPrice">${cartItem.price * cartItem.quantity}</p>`;
        str += `</div></div>`;
        str += `<input type="hidden" name="id" value="${cartItem.id}">`;
        str += `<input type="hidden" name="cartId" value="${cartItem.cartId}">`;
        str += `<input type="hidden" name="productId" value="${cartItem.productDto.id}">`;
        str += `<input type="hidden" name="price" value="${cartItem.price}">`;
        str += `</li>`;
    });
    document.querySelector(".cart-item-list").innerHTML = str;

    })
    .catch((error) => {
      console.error("Error adding to cart:", error);
    });
}
getCartItems();

document.querySelector(".cart-item-list").addEventListener("click", (e)=> {
    const li = e.target.closest(".list-group-item");
    let quantity = parseInt(li.querySelector(".quantity-count").value);
    const id = li.querySelector("[name='id']").value;
    const cartId = li.querySelector("[name='cartId']").value;
    const productId = li.querySelector("[name='productId']").value;
    let price = li.querySelector("[name='price']").value;
    let totalPrice = quantity * price;
    if (e.target.matches("#quantity-minus-btn")) {
        console.log("마이너스 클릭");
        console.log(typeof quantity);
        console.log(typeof totalPrice);
        if (quantity > 1) {
            quantity -= 1;
            li.querySelector(".quantity-count").value  = quantity;
            totalPrice = quantity * price;
            li.querySelector(".totalPrice").innerHTML  = totalPrice + "원";
        }
    }
    if (e.target.matches("#quantity-plus-btn")) {
        console.log("플러스 클릭");
        console.log(typeof quantity);
        console.log(typeof totalPrice);
        if (quantity < 10) {
            quantity += 1;
            li.querySelector(".quantity-count").value  = quantity;
            totalPrice = quantity * price;
            li.querySelector(".totalPrice").innerHTML  = totalPrice + "원";
        }
    }
    if (e.target.matches(".btn-modify-quantity")) {
      console.log("Button clicked!", cartId);
      fetch(`/rest/cart/modify/${productId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
            id: id,
            cartId: cartId,
            quantity: quantity,
            price: price
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          getCartItems();
  
          
        })
        .catch((error) => {
          console.error("Error adding to cart:", error);
        });
    }
    if (e.target.matches(".delete-cart-item")) {
        fetch(`/rest/cart/delete/${id}`, {
            method: "DELETE"
          })
            .then((response) => response.text())
            .then((data) => {
              console.log(data);
              getCartItems();
      
              
            })
            .catch((error) => {
              console.error("Error adding to cart:", error);
            });
        
    }
  });