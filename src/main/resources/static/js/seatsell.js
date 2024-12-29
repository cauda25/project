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
      const totalColumns = 10; // 좌석 열 개수 (예시)
      const totalRows = Math.max(
        ...seatStatuses.map((seat) => seat.rowNum.charCodeAt(0) - 65 + 1)
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
                seatId: seatStatus.seatId,
                rowNum: seatStatus.rowNum,
                seatNum: seatStatus.seatNum,
                price: seatStatus.price,
              };

              if (seatButton.classList.contains("selected")) {
                selectedSeats.push(seatInfo);
              } else {
                const index = selectedSeats.findIndex(
                  (seat) => seat.seatId === seatInfo.seatId
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

document.getElementById("payment-button").addEventListener("click", (e) => {
  e.preventDefault(); // 기본 동작 방지

  const seatContainer = document.getElementById("seat-container");
  if (!seatContainer) {
    console.error("seat-container 요소를 찾을 수 없습니다.");
    return;
  }

  // 결제 폼 HTML
  const paymentFormHTML = `
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
        <label for="password">비밀번호 앞 두자리</label>
        <input type="password" id="password" name="password" maxlength="2" placeholder="**" required />
      </div>
      <button type="submit" id="submit-payment">결제하기</button>
    </form>
  </div>
`;

  // seat-container의 내용을 결제 폼으로 변경
  seatContainer.innerHTML = paymentFormHTML;

  // 결제 폼 제출 이벤트 추가
  const paymentForm = document.getElementById("payment-form");
  paymentForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const cardNumber = document.getElementById("card-number").value;
    const cvc = document.getElementById("cvc").value;
    const password = document.getElementById("password").value;

    // 간단한 유효성 검사 (추가 가능)
    if (!cardNumber || !cvc || !password) {
      alert("모든 필드를 채워주세요.");
      return;
    }

    // 결제 처리 로직
    alert("결제가 완료되었습니다.");
  });
});
