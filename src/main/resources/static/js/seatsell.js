document.addEventListener("DOMContentLoaded", async () => {
  try {
    const response = await fetch("/reservation/info", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("서버에서 정보를 가져오는 데 실패했습니다.");
    }

    const data = await response.json();

    if (data.error || !data.screeningId) {
      Swal.fire("전달된 정보가 부족합니다.");
      return;
    }

    // 예매 정보 UI 업데이트
    document.getElementById("selected-theater").textContent =
      data.theater || "극장을 선택해주세요.";
    document.getElementById("selected-movie").textContent =
      data.movie || "영화를 선택해주세요.";
    document.getElementById("selected-date").textContent =
      data.date || "날짜를 선택해주세요.";
    document.getElementById("selected-auditorium").textContent =
      data.auditorium || "상영관을 선택해주세요.";
    document.getElementById("selected-time").textContent =
      data.time || "시간을 선택해주세요.";

    const screeningId = data.screeningId;
    loadSeatData(screeningId);
  } catch (error) {
    console.error("Error fetching reservation info:", error);
    Swal.fire("정보를 불러오는 중 문제가 발생했습니다.");
  }
});

async function loadSeatData(screeningId) {
  const seatContainer = document.getElementById("seat-container");
  const priceDisplay = document.getElementById("price-display");

  if (!screeningId) {
    console.error("Screening ID is missing.");
    return;
  }
  // 선택된 좌석 상태 관리
  const selectedSeats = [];
  const maxSeats = 4; // 선택 가능한 최대 좌석 수

  try {
    const response = await fetch(`/reservation/seats`, { method: "GET" });

    if (!response.ok) {
      throw new Error("좌석 정보를 가져오는데 실패했습니다.");
    }

    const seatStatuses = await response.json();
    seatContainer.innerHTML = "";

    // 컨테이너 크기 계산
    const containerWidth = seatContainer.offsetWidth;
    const containerHeight = seatContainer.offsetHeight;
    const totalColumns = Math.max(...seatStatuses.map((s) => s.seatNum));
    const totalRows = Math.max(
      ...seatStatuses.map((s) => s.rowNum.charCodeAt(0) - 65 + 1)
    );

    const seatWidth = 25;
    const seatHeight = 25;
    const totalSeatWidth = totalColumns * seatWidth;
    const totalSeatHeight = totalRows * seatHeight;

    const offsetX = (containerWidth - totalSeatWidth) / 2;
    const offsetY = (containerHeight - totalSeatHeight) / 2;

    const addedRowLabels = new Set();

    seatStatuses.forEach((seatStatus) => {
      const rowNum = seatStatus.rowNum;
      if (!addedRowLabels.has(rowNum)) {
        const rowLabel = document.createElement("span");
        rowLabel.textContent = rowNum;
        rowLabel.classList.add("row-label");
        rowLabel.style.position = "absolute";
        rowLabel.style.left = `${offsetX - 20}px`;
        rowLabel.style.top = `${
          offsetY + (rowNum.charCodeAt(0) - 65) * seatHeight
        }px`;
        rowLabel.style.fontWeight = "bold";
        rowLabel.style.fontSize = "14px";
        rowLabel.style.width = "15px";
        rowLabel.style.textAlign = "center";
        seatContainer.appendChild(rowLabel);

        addedRowLabels.add(rowNum);
      }

      const seatButton = document.createElement("button");
      seatButton.type = "button";
      seatButton.setAttribute("data-seatstatus-id", seatStatus.seatStatusId);
      seatButton.setAttribute("data-seat-row", seatStatus.rowNum);
      seatButton.setAttribute("data-seat-num", seatStatus.seatNum);
      seatButton.setAttribute("data-seat-price", seatStatus.price);

      let seatClass = "seat";
      const status = seatStatus.status?.toUpperCase() || "UNKNOWN";

      if (status === "RESERVED" || status === "SOLD") {
        seatClass += " reserved";
      } else if (status === "AVAILABLE") {
        seatClass += " available";
      } else {
        seatClass += " unknown";
      }

      seatButton.className = seatClass;
      seatButton.style.position = "absolute";
      seatButton.style.left = `${
        offsetX + (seatStatus.seatNum - 1) * seatWidth
      }px`;
      seatButton.style.top = `${
        offsetY + (seatStatus.rowNum.charCodeAt(0) - 65) * seatHeight
      }px`;
      seatButton.style.width = `${seatWidth}px`;
      seatButton.style.height = `${seatHeight}px`;

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

            priceDisplay.setAttribute("data-total-price", totalPrice);
          } else {
            Swal.fire({
              icon: "warning",
              text: `최대 ${maxSeats}개의 좌석만 선택할 수 있습니다.`,
              confirmButtonColor: "#0022ff",
              confirmButtonText: "다시선택",
            });
          }
        } else if (status === "RESERVED") {
          Swal.fire({
            text: "이미 예약된 좌석입니다.",
            confirmButtonColor: "#0022ff",
          });
        } else {
          Swal.fire({
            text: "선택할 수 없는 좌석입니다.",
            confirmButtonColor: "#0022ff",
          });
        }
      });

      seatContainer.appendChild(seatButton);
    });
  } catch (error) {
    console.error("Error loading seat data:", error);
    Swal.fire("좌석 정보를 불러오는 중 문제가 발생했습니다.");
  }
}

document
  .getElementById("payment-button")
  .addEventListener("click", async (e) => {
    e.preventDefault();

    const selectedSeats = [];
    document.querySelectorAll(".seat.selected").forEach((seat) => {
      selectedSeats.push({
        seatStatusId: seat.getAttribute("data-seatstatus-id"),
      });
    });

    if (selectedSeats.length === 0) {
      Swal.fire("선택된 좌석이 없습니다.");
      return;
    }

    document.getElementById("seatpage").innerHTML = "결제 진행 중";
    const priceDisplayElement = document.getElementById("price-display");
    const totalAmount = parseInt(
      priceDisplayElement.getAttribute("data-total-price"),
      10
    );

    try {
      // 좌석 상태를 `RESERVED`로 변경
      const reserveResponse = await fetch("/reservation/update-seat-status", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          seats: selectedSeats.map((seat) => ({
            seatStatusId: seat.seatStatusId,
          })),
          status: "RESERVED",
        }),
      });

      if (!reserveResponse.ok) {
        throw new Error("좌석 예약 상태 업데이트 실패");
      }

      function randomId() {
        return [...crypto.getRandomValues(new Uint32Array(2))]
          .map((word) => word.toString(16).padStart(8, "0"))
          .join("");
      }

      const paymentId = randomId();
      const payment = await PortOne.requestPayment({
        storeId: "store-a98eb9c7-f730-4a20-a83f-2da38864d8fd",
        channelKey: "channel-key-d5be91bf-4f91-4476-a523-569240fa415d",
        paymentId,
        orderName: "영화 예매",
        totalAmount: totalAmount,
        currency: "KRW",
        payMethod: "CARD",
      });

      if (payment.code !== undefined) {
        console.error("결제 실패:", payment.message);
        Swal.fire("결제 실패: " + payment.message).then(() => {
          window.location.href = "/reservation";
        });
        return;
      }

      const completeResponse = await fetch("/reservation/confirm-payment", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          seatStatuses: selectedSeats.map((seat) => seat.seatStatusId),
          movieTitle: document.getElementById("selected-movie").textContent,
          screeningDate: document.getElementById("selected-date").textContent,
          auditoriumName: document.getElementById("selected-auditorium")
            .textContent,
          screeningTime: document.getElementById("selected-time").textContent,
          theaterName: document.getElementById("selected-theater").textContent,
        }),
      });

      console.log(" [confirm-payment 요청] 전송 데이터:", {
        seatStatuses: selectedSeats.map((seat) => seat.seatStatusId),
        movieTitle: document.getElementById("selected-movie").textContent,
        screeningTime: document.getElementById("selected-time").textContent,
        screeningDate: document.getElementById("selected-date").textContent,
        theaterName: document.getElementById("selected-theater").textContent,
        auditoriumName: document.getElementById("selected-auditorium")
          .textContent,
      });

      if (!completeResponse.ok) {
        const errorData = await completeResponse.json();
        console.error(" [confirm-payment 오류]", errorData);
        Swal.fire("결제 처리 중 오류가 발생했습니다: " + errorData.error).then(
          () => {
            window.location.href = "/reservation";
          }
        );
        return;
      }

      const paymentComplete = await completeResponse.json();
      if (paymentComplete.success) {
        await fetch("/reservation/update-seat-status", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            seats: selectedSeats,
            status: "SOLD",
          }),
        });

        Swal.fire({
          icon: "success",
          title: "예매가 완료되었습니다!",
          text: `총 결제 금액: ${totalAmount.toLocaleString()}원`,
          confirmButtonText: "확인",
        }).then(() => {
          window.location.href = "/movie/main";
        });
      }
    } catch (error) {
      console.error("결제 오류:", error);
      Swal.fire("결제 처리 중 오류가 발생했습니다.").then(() => {
        window.location.href = "/reservation";
      });
    }
  });
