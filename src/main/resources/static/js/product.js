var count = 1;
var totalPrice = price;
// 상품 개수 증가
document.querySelector("#quantity-plus-btn").addEventListener("click",()=>{
    console.log("플러스 버튼 클릭");
    if (count < 10) {
        count += 1;
        document.querySelector(".quantity-count").value  = count;
        totalPrice = count * price;
        document.querySelector(".total-price").innerHTML  = totalPrice + "원";

    }
})
// 상품 개수 감소
document.querySelector("#quantity-minus-btn").addEventListener("click",()=>{
    console.log("마이너스 버튼 클릭");
    if (count > 1) {
        count -= 1;
        document.querySelector(".quantity-count").value  = count;
        totalPrice = count * price;
        document.querySelector(".total-price").innerHTML  = totalPrice + "원";
    } 
})

console.log(productId);
console.log(count);
console.log(price);

const orderForm = document.querySelector("#orderForm");

document.querySelector(".btn-cart").addEventListener("click",()=>{
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
        addToCart();
      }else{
          if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
            window.location.href = "/member/login"; // 로그인 페이지로 이동
          }
      }
    })
    .catch((error) => {
      console.error("Error adding to cart:", error);
    });
})



function addToCart() {
  fetch(`/rest/cart/add/${productId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
          quantity: count,
          price: price
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
            window.location.href = "/order/cart"; // 장바구니 페이지로 이동
          }

        
      })
      .catch((error) => {
        console.error("Error adding to cart:", error);
      });
}

