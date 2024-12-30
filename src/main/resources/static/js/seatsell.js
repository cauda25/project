// seat_sell.html에서 URL의 tfoot 정보를 읽어오기
document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const theater = params.get("theater") || "극장을 선택해주세요.";
  const movie = params.get("movie") || "영화를 선택해주세요.";
  const date = params.get("date") || "날짜를 선택해주세요.";
  const auditorium = params.get("auditorium") || "상영관을 선택해주세요.";
  const time = params.get("time") || "시간을 선택해주세요.";

  if (theater && movie && date && auditorium && time) {
    document.getElementById("selected-theater").textContent = theater;
    document.getElementById("selected-movie").textContent = movie;
    document.getElementById("selected-date").textContent = date;
    document.getElementById("selected-auditorium").textContent = auditorium;
    document.getElementById("selected-time").textContent = time;
  } else {
    alert("전달된 정보가 부족합니다.");
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const seatContainer = document.getElementById("seat-container");
  const priceDisplay = document.getElementById("price-display");

  const screeningId = new URLSearchParams(window.location.search).get(
    "screeningId"
  );
  if (!screeningId) {
    console.error("Screening ID is missing.");
    return;
  }
  // 선택된 좌석 상태 관리
  const selectedSeats = [];
  const maxSeats = 4; // 선택 가능한 최대 좌석 수

  // 좌석 정보를 API로부터 가져오기
  fetch(`/reservation/seats/${screeningId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("좌석 정보를 가져오는데 실패했습니다.");
      }
      return response.json();
    })
    .then((seatStatuses) => {
      seatContainer.innerHTML = ""; // 기존 좌석 초기화
      // 컨테이너 중심 계산
      const containerWidth = seatContainer.offsetWidth;
      const containerHeight = seatContainer.offsetHeight;
      const totalColumns = Math.max(...seatStatuses.map((s) => s.seatNum));
      const totalRows = Math.max(
        ...seatStatuses.map((s) => s.rowNum.charCodeAt(0) - 65 + 1)
      );

      const seatWidth = 25; // 좌석 너비
      const seatHeight = 25; // 좌석 높이
      const totalSeatWidth = totalColumns * seatWidth;
      const totalSeatHeight = totalRows * seatHeight;

      const offsetX = (containerWidth - totalSeatWidth) / 2;
      const offsetY = (containerHeight - totalSeatHeight) / 2;

      seatStatuses.forEach((seatStatus) => {
        const seatButton = document.createElement("button");
        seatButton.type = "button";
        seatButton.setAttribute("data-seatstatus-id", seatStatus.seatStatusId);
        seatButton.setAttribute("data-seat-row", seatStatus.rowNum);
        seatButton.setAttribute("data-seat-num", seatStatus.seatNum);
        seatButton.setAttribute("data-seat-price", seatStatus.price);
        // 좌석 클래스 및 스타일 설정
        const status = seatStatus.status?.toUpperCase() || "UNKNOWN"; // Enum 값 처리
        // 기본 클래스
        let seatClass = "seat";

        // 상태에 따라 클래스 추가
        if (status === "RESERVED" || status === "SOLD") {
          seatClass += " reserved";
        } else if (status === "AVAILABLE") {
          seatClass += " available";
        } else {
          seatClass += " unknown"; // 알 수 없는 상태
        }

        // 클래스 적용
        seatButton.className = seatClass;
        seatButton.style.position = "absolute"; // 절대 위치 설정

        // 좌석 배치 계산
        const left = offsetX + (seatStatus.seatNum - 1) * seatWidth; // 열 좌표
        const top =
          offsetY + (seatStatus.rowNum.charCodeAt(0) - 65) * seatHeight; // 행 좌표

        seatButton.style.left = `${left}px`;
        seatButton.style.top = `${top}px`;

        // 좌석 크기 및 스타일 설정
        seatButton.style.width = `${seatWidth}px`;
        seatButton.style.height = `${seatHeight}px`;
        seatButton.style.lineHeight = `${seatHeight}px`; // 텍스트 가운데 정렬
        seatButton.title = `${seatStatus.rowNum}${seatStatus.seatNum} (${
          seatStatus.seatStatusEnum || "UNKNOWN"
        })`;

        // 좌석 버튼 내부 내용
        seatButton.textContent = seatStatus.seatNum;

        // 좌석 클릭 이벤트 추가
        seatButton.addEventListener("click", () => {
          if (status === "AVAILABLE") {
            if (
              seatButton.classList.contains("selected") ||
              selectedSeats.length < maxSeats
            ) {
              seatButton.classList.toggle("selected");

              const seatInfo = {
                seatId: seatStatus.seatStatusId,
                rowNum: seatStatus.rowNum,
                seatNum: seatStatus.seatNum,
                price: seatStatus.price,
              };

              if (seatButton.classList.contains("selected")) {
                selectedSeats.push(seatInfo);
              } else {
                const index = selectedSeats.findIndex(
                  (seat) => seat.seatStatusId === seatInfo.seatStatusId
                );
                selectedSeats.splice(index, 1);
              }

              const seatNumbers = selectedSeats
                .map((seat) => `${seat.rowNum}${seat.seatNum}`)
                .join(", ");
              const totalPrice = selectedSeats.reduce(
                (sum, seat) => sum + seat.price,
                0
              );

              priceDisplay.innerHTML =
                selectedSeats.length > 0
                  ? `좌석 번호: ${seatNumbers}<br>총 가격: ${totalPrice}원`
                  : "좌석이 선택되지 않았습니다.";
            } else {
              alert(`최대 ${maxSeats}개의 좌석만 선택할 수 있습니다.`);
            }
          } else if (status === "RESERVED") {
            alert("이미 예약된 좌석입니다.");
          } else {
            alert("선택할 수 없는 좌석입니다.");
          }
        });

        seatContainer.appendChild(seatButton);
      });
    })
    .catch((error) => {
      console.error("Error loading seat data:", error);
      alert("좌석 정보를 불러오는 중 문제가 발생했습니다.");
    });
});

// document.getElementById("payment-button").addEventListener("click", (e) => {
//   e.preventDefault();

//   const script = document.createElement("script");
//   script.src = "https://testpay.kcp.co.kr/plugin/payplus_web.jsp";
//   script.onload = () => {
//     if (typeof MakePayMessage === "function") {
//       if (MakePayMessage()) {
//         alert("결제가 성공적으로 완료되었습니다.");
//       } else {
//         alert("결제 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
//       }
//     } else {
//       alert("결제 함수를 로드하지 못했습니다.");
//     }
//   };
//   script.onerror = () => {
//     alert("KCP 결제 스크립트를 로드할 수 없습니다.");
//   };
//   document.head.appendChild(script);
// });

document
  .getElementById("payment-button")
  .addEventListener("click", async (e) => {
    e.preventDefault();

    const selectedSeats = [];
    document.querySelectorAll(".seat.selected").forEach((seat) => {
      const seatStatusId = seat.getAttribute("data-seatstatus-id");
      const rowNum = seat.getAttribute("data-seat-row");
      const seatNum = seat.getAttribute("data-seat-num");
      const price = parseInt(seat.getAttribute("data-seat-price"), 10);
      console.log("좌석 데이터:", { seatStatusId, rowNum, seatNum, price });
      if (seatStatusId && rowNum && seatNum && price) {
        selectedSeats.push({
          seatStatusId: parseInt(seatStatusId, 10),
          rowNum,
          seatNum,
          price,
        });
      }
    });
    console.log("선택된 좌석 정보:", selectedSeats); // 디버깅용
    if (selectedSeats.length === 0) {
      alert("선택된 좌석이 없습니다.");
      return;
    }

    try {
      // 좌석 상태를 RESERVED로 변경 요청
      const response = await fetch("/reservation/update-seat-status", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          seats: selectedSeats.map((seat) => ({
            seatStatusId: seat.seatStatusId,
          })),
          status: "RESERVED",
        }),
      });

      if (!response.ok) {
        throw new Error("좌석 예약 상태 업데이트 실패");
      }

      // 카드 결제 페이지로 전환
      document.getElementById("seat-container").innerHTML = `
      <div class="payment-form">
        <h3>결제 정보 입력</h3>
        <form id="payment-form">
          <div class="form-group">
            <label for="card-number">카드 번호</label>
            <input type="text" id="card-number" name="card-number" maxlength="16" placeholder="1234 5678 9012 3456" required />
          </div>
          <div class="form-group">
            <label for="cvc">CVC</label>
            <input type="text" id="cvc" name="cvc" maxlength="3" placeholder="123" required />
          </div>
          <div class="form-group">
            <label for="expiry-date">유효기간 (MM/YY)</label>
            <input type="text" id="expiry-date" name="expiry-date" maxlength="5" placeholder="MM/YY" required />
          </div>
          <div class="form-group">
            <label for="password">비밀번호 앞 두 자리</label>
            <input type="password" id="password" name="password" maxlength="2" placeholder="**" required />
          </div>
          <button type="submit" id="submit-payment">결제하기</button>
        </form>
      </div>
    `;

      // 결제 처리 이벤트 추가
      document
        .getElementById("payment-form")
        .addEventListener("submit", async (event) => {
          event.preventDefault();

          const cardNumber = document.getElementById("card-number").value;
          const cvc = document.getElementById("cvc").value;
          const expiryDate = document.getElementById("expiry-date").value;
          const password = document.getElementById("password").value;

          if (!cardNumber || !cvc || !expiryDate || !password) {
            alert("모든 필드를 채워주세요.");
            return;
          }

          try {
            // 결제 요청 및 좌석 SOLD 상태 변경
            const paymentResponse = await fetch(
              "/reservation/confirm-payment",
              {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                  seats: selectedSeats.map((seat) => ({
                    seatStatusId: seat.seatStatusId,
                  })),
                }),
              }
            );

            if (!paymentResponse.ok) {
              throw new Error("결제 처리 실패");
            }

            const result = await paymentResponse.json();
            if (result.success) {
              // 예매번호 생성
              const bookingNumber =
                new Date().toISOString().slice(0, 10).replace(/-/g, "") + // yyyyMMdd
                String(result.bookingOrder).padStart(4, "0"); // 순번

              // 결제 완료 UI 업데이트
              const tbody = document.querySelector("tbody");
              tbody.innerHTML = `
                <tr>
                  <td colspan="3">
                    <h3>예매가 완료되었습니다!</h3>
                    <p><strong>예매번호:</strong> ${bookingNumber}</p>
                    <p><strong>선택한 영화:</strong> ${
                      document.getElementById("selected-movie").textContent
                    }</p>
                    <p><strong>선택한 날짜:</strong> ${
                      document.getElementById("selected-date").textContent
                    }</p>
                    <p><strong>선택한 극장:</strong> ${
                      document.getElementById("selected-theater").textContent
                    }</p>
                    <p><strong>선택한 상영관:</strong> ${
                      document.getElementById("selected-auditorium").textContent
                    }</p>
                    <p><strong>선택한 시간:</strong> ${
                      document.getElementById("selected-time").textContent
                    }</p>
                    <p><strong>좌석 번호:</strong> ${selectedSeats
                      .map((seat) => `${seat.rowNum}${seat.seatNum}`)
                      .join(", ")}</p>
                    <p><strong>결제 금액:</strong> ${selectedSeats.reduce(
                      (sum, seat) => sum + seat.price,
                      0
                    )}원</p>
                  </td>
                </tr>
              `;

              // tfoot와 price-display 영역 초기화
              document.querySelector("tfoot").innerHTML = "";
              document.getElementById("price-display").innerHTML = "";
            } else {
              alert("결제 중 문제가 발생했습니다.");
            }
          } catch (error) {
            console.error("Error during payment:", error);
            alert("결제 요청 중 오류가 발생했습니다.");
          }
        });
    } catch (error) {
      console.error("Error updating seat status to RESERVED:", error);
      alert("좌석 예약 처리 중 오류가 발생했습니다.");
    }
  });
