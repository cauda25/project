var count = 1;
var totalPrice = price;
document.querySelector("#quantity-plus-btn").addEventListener("click",()=>{
    console.log("플러스 버튼 클릭");
    if (count < 10) {
        count += 1;
        document.querySelector(".quantity-count").value  = count;
        totalPrice = count * price;
        document.querySelector(".total-price").innerHTML  = totalPrice + "원";

    }
})
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

// document.querySelector(".btn-cart")
orderForm.addEventListener("submit",(e)=>{
  e.preventDefault();
    fetch("/rest/cart/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
            productId: productId,
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

        // fetch("/rest/cart/add", {
        //   method: "POST", // POST 요청
        //   headers: {
        //     "Content-Type": "application/json", // 데이터를 JSON 형식으로 보냄
        //   },
        //   body: JSON.stringify({ 
        //     productId: productId,
        //     quantity: count,
        //     price: price 
        //    }), // 전송할 데이터
        // })
        //   .then((response) => {
        //     if (!response.ok) {
        //       throw new Error(`HTTP error! status: ${response.status}`);
        //     }
        //     return response.json();
        //   })
        //   .then((data) => {
        //     alert("관람평이 등록되었습니다!");
        //     console.log(data);
        //   })
        //   .catch((error) => {
        //     console.error("서버 요청 중 오류 발생:", error);
        //   });
})