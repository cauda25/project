let prev = null; //지역 클릭시 배경색 변경
let selectedMovie = null;

document.querySelectorAll(".fw-bold").forEach((button) => {
  button.addEventListener("click", (e) => {
    e.preventDefault();
    if (prev) {
      prev.classList.remove("clicked");
    }
    e.currentTarget.classList.add("clicked");
    prev = e.currentTarget;
  });
});

//오늘부터 일주일 불러오기기
document.addEventListener("DOMContentLoaded", () => {
  const dateList = document.getElementById("dateList");
  if (!dateList) {
    console.error("dateList 요소를 찾을 수 없습니다!");
    return;
  }
  const today = new Date();
  let result = "";

  Array.from({ length: 7 }).forEach((_, i) => {
    const currentDate = new Date(today);
    currentDate.setDate(today.getDate() + i);

    const formattedDate = currentDate
      .toLocaleDateString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      })
      .replace(/\./g, "-")
      .replace(/-\s*$/, "")
      .replace(/\s/g, "");
    const day = currentDate.getDate();
    const dayOfWeek = currentDate.toLocaleDateString("ko-KR", {
      weekday: "short",
    });

    result += `
      <li>
        <a href="#" class="date-btn btn btn-light d-flex flex-column align-items-center justify-content-center" data-date="${formattedDate}">
          <label class="d-flex flex-column align-items-center">
            <input type="radio" name="radioDate">
            <strong>${day}</strong>
            <em>${dayOfWeek}</em>
          </label>
        </a>
      </li>`;
  });

  dateList.innerHTML = result;
});

// 예매 테이블 클릭시 변화
function addClickHandler(selector, clickedClass) {
  document.querySelectorAll(selector).forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      document
        .querySelectorAll(selector)
        .forEach((el) => el.classList.remove(clickedClass));
      e.currentTarget.classList.add(clickedClass);
    });
  });
}

function updateSelectionSummary(theater, movie, date, auditorium, time) {
  document.getElementById("selected-theater").textContent = theater || "-";
  document.getElementById("selected-movie").textContent = movie || "-";
  document.getElementById("selected-date").textContent = date || "-";
  document.getElementById("selected-auditorium").textContent =
    auditorium || "-";
  document.getElementById("selected-time").textContent = time || "-";

  const selectInfo = document.querySelector(".select-info");
  if (selectInfo) {
    selectInfo.setAttribute("data-theater", theater || "");
    selectInfo.setAttribute("data-movie", movie || "");
    selectInfo.setAttribute("data-date", date || "");
    selectInfo.setAttribute("data-auditorium", auditorium || "");
    selectInfo.setAttribute("data-time", time || "");
  }
}

function attachMovieClickHandler() {
  document.querySelectorAll(".movie-item").forEach((movieLink) => {
    movieLink.addEventListener("click", (e) => {
      e.preventDefault();

      document.querySelectorAll(".movie-item").forEach((item) => {
        item.classList.remove("clicked");
      });
      e.currentTarget.classList.add("clicked");

      selectedMovie = e.currentTarget.textContent?.trim();
      console.log("선택된 영화:", selectedMovie);

      document.querySelectorAll(".date-btn").forEach((btn) => {
        btn.classList.remove("clicked");
      });

      const screeningsDiv = document.getElementById("screenings");
      if (screeningsDiv) {
        screeningsDiv.innerHTML = "";
      }

      // 날짜 버튼 클릭 핸들러 등록
      attachDateClickHandler();
    });
  });
}

// 지역 선택 후 극장 목록 요청
document.querySelectorAll(".region-list").forEach((link) => {
  link.addEventListener("click", (e) => {
    e.preventDefault();

    const region = e.currentTarget.getAttribute("data-region");
    if (!region) {
      console.error("지역값이 없습니다");
      return;
    }
    fetch(`/reservation/theaters?region=${region}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        const theaterList = document.getElementById("theater-list");
        theaterList.innerHTML = "";

        let result = "";
        data.forEach((theater) => {
          result += `<a class="nav-link fw-semibold theater-title" 
                  href="#theater-${theater.theaterId}" 
                  data-theater-id="${theater.theaterId}">
                  ${theater.theaterName}</a>`;
        });
        theaterList.innerHTML = result;

        loadMoviesByTheater();
        addClickHandler(".fw-semibold", "clicked");
      })
      .catch((error) => console.error("극장 목록 오류", error));
  });
});

// 극장 클릭후 영화 목록 요청
function loadMoviesByTheater() {
  document.querySelectorAll(".theater-title").forEach((theaterLink) => {
    theaterLink.addEventListener("click", (e) => {
      e.preventDefault();

      const theaterId = e.currentTarget.getAttribute("data-theater-id");
      if (!theaterId) {
        console.error("Theater ID is missing");
        return;
      }

      const movieList = document.getElementById("movie-list");
      movieList.innerHTML = "";

      fetch(`/reservation/movies?theaterId=${theaterId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
        })
        .then((movies) => {
          if (!movies.length) {
            movieList.innerHTML =
              "<p>해당 극장에서 상영 중인 영화가 없습니다.</p>";
            return;
          }

          let result = "";
          movies.forEach((movieTitle) => {
            result += `<a class="movie-item" href="#">
                          ${movieTitle}
                       </a>`;
          });
          movieList.innerHTML = result;

          attachMovieClickHandler(); // 영화 클릭 핸들러 등록
        })
        .catch((error) => {
          console.error("Error fetching movies:", error);
          movieList.innerHTML = "<p>영화 목록을 불러오지 못했습니다.</p>";
        });
    });
  });
}

function attachDateClickHandler() {
  document.querySelectorAll(".date-btn").forEach((dateBtn) => {
    dateBtn.addEventListener("click", (e) => {
      e.preventDefault();

      document.querySelectorAll(".date-btn").forEach((btn) => {
        btn.classList.remove("clicked");
      });

      e.currentTarget.classList.add("clicked");

      const selectedDate = e.currentTarget.getAttribute("data-date");
      console.log("Selected date in JavaScript:", selectedDate);
      if (!selectedDate || !selectedMovie) {
        console.error("날짜 또는 선택된 영화가 없습니다.");
        return;
      }
      fetchScreenings(selectedMovie, selectedDate);
    });
  });
}

function fetchScreenings(selectedMovie, selectedDate) {
  const screeningsDiv = document.getElementById("screenings");
  if (!screeningsDiv) {
    console.error("screeningsDiv 요소를 찾을 수 없습니다.");
    return;
  }

  screeningsDiv.innerHTML = "";

  fetch(
    `/reservation/screenings?movieTitle=${encodeURIComponent(
      selectedMovie
    )}&date=${selectedDate}`
  )
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then((screenings) => {
      if (!screenings.length) {
        screeningsDiv.innerHTML = "<p>해당 날짜에 상영시간표가 없습니다.</p>";
        return;
      }

      const now = new Date();
      // 선택된 날짜가 오늘인 경우 현재 시간 이후만 필터링
      const filteredScreenings =
        selectedDate === now.toISOString().slice(0, 10) // YYYY-MM-DD 형태 비교
          ? screenings.filter((screening) => {
              const [hours, minutes] = screening.startTime.split(":");
              const screeningTime = new Date(
                now.getFullYear(),
                now.getMonth(),
                now.getDate(),
                parseInt(hours, 10),
                parseInt(minutes, 10)
              );
              return screeningTime > now; // 현재 시간 이후의 상영만 포함
            })
          : screenings;

      if (!filteredScreenings.length) {
        screeningsDiv.innerHTML = "<p>해당 날짜에 상영시간표가 없습니다.</p>";
        return;
      }

      const groupedScreenings = filteredScreenings.reduce((acc, screening) => {
        const { auditoriumName } = screening;
        if (!acc[auditoriumName]) {
          acc[auditoriumName] = [];
        }
        acc[auditoriumName].push(screening);
        return acc;
      }, {});

      let result = `<div class="select_tit">${selectedMovie}</div>`;
      Object.keys(groupedScreenings).forEach((auditoriumName) => {
        result += `<div class="timeSelect">`;
        result += `<span class="theaterCate">${auditoriumName}</span>`;
        result += `<ul class="timelist-container" style="padding: 0; margin: 0;">`;

        groupedScreenings[auditoriumName].forEach((screening) => {
          result += `<li class="timelist" style="list-style: none;">`;
          result += `<a role="button" href="#" class="button" 
                          data-movie="${selectedMovie}" 
                          data-auditorium="${auditoriumName}" 
                          data-time="${screening.startTime}" 
                          data-auditorium-no="${screening.auditoriumNo}"
                          data-screening-id="${screening.screeningId}">
                          <div>
                            <span class="time"><strong>${screening.startTime}</strong></span>
                            <span class="seat-hall">
                              <span class="seatrest">${screening.availableSeats}/${screening.totalSeats}</span>
                              <span class="hall">${screening.auditoriumNo}관</span>
                            </span>
                          </div>
                        </a>`;
          result += `</li>`;
        });

        result += `</ul></div>`;
      });

      screeningsDiv.innerHTML = result;
      attachTimeClickHandler();
    })
    .catch((error) => {
      console.error("상영시간표 요청 오류:", error);
      screeningsDiv.innerHTML = "<p>상영시간표를 불러오지 못했습니다.</p>";
    });
}
function attachTimeClickHandler() {
  document.querySelectorAll(".timelist .button").forEach((timeButton) => {
    timeButton.addEventListener("click", (e) => {
      e.preventDefault();

      // 선택된 시간 버튼 스타일 변경
      document.querySelectorAll(".timelist .button").forEach((btn) => {
        btn.classList.remove("clicked");
      });
      e.currentTarget.classList.add("clicked");

      // tfoot 정보 업데이트
      const selectedMovie = e.currentTarget.getAttribute("data-movie");
      const auditoriumName = e.currentTarget.getAttribute("data-auditorium");
      const startTime = e.currentTarget.getAttribute("data-time");
      const auditoriumNo = e.currentTarget.getAttribute("data-auditorium-no");
      const screeningId = e.currentTarget.getAttribute("data-screening-id");
      const selectedDate = document
        .querySelector(".date-btn.clicked")
        ?.getAttribute("data-date");
      const selectedTheater = document
        .querySelector(".theater-title.clicked")
        ?.textContent?.trim();

      updateSelectionSummary(
        selectedTheater, // 극장 정보는 상영시간표에는 없음
        selectedMovie,
        selectedDate, // 날짜는 이미 선택된 상태
        `${auditoriumName} (${auditoriumNo}관)`,
        startTime,
        screeningId
      );
    });
  });
}

function updateSelectionSummary(
  theater,
  movie,
  date,
  auditorium,
  time,
  screeningId
) {
  document.getElementById("selected-theater").textContent = theater || "-";
  document.getElementById("selected-movie").textContent = movie || "-";
  document.getElementById("selected-date").textContent = date || "-";
  document.getElementById("selected-auditorium").textContent =
    auditorium || "-";
  document.getElementById("selected-time").textContent = time || "-";

  const selectInfo = document.querySelector(".select-info");
  if (selectInfo) {
    selectInfo.setAttribute("data-theater", theater || "");
    selectInfo.setAttribute("data-movie", movie || "");
    selectInfo.setAttribute("data-date", date || "");
    selectInfo.setAttribute("data-auditorium", auditorium || "");
    selectInfo.setAttribute("data-time", time || "");
    selectInfo.setAttribute("data-screening-id", screeningId || "");
  }
}

document.querySelectorAll(".timeSelect a").forEach((timeLink) => {
  timeLink.addEventListener("click", (e) => {
    e.preventDefault();

    const selectedTheater = document
      .querySelector(".theater-title.clicked")
      ?.textContent?.trim();
    const selectedMovie = document
      .querySelector(".movie-item.clicked")
      ?.textContent?.trim();
    const selectedDate = document
      .querySelector(".date-btn.clicked")
      ?.getAttribute("data-date");
    const selectedAuditorium = e.currentTarget
      .closest(".timeSelect")
      .querySelector(".theaterCate")?.textContent;
    const selectedTime =
      e.currentTarget.querySelector(".time strong")?.textContent;

    if (
      selectedTheater &&
      selectedMovie &&
      selectedDate &&
      selectedAuditorium &&
      selectedTime
    ) {
      updateSelectionInfo(
        selectedTheater,
        selectedMovie,
        selectedDate,
        selectedAuditorium,
        selectedTime
      );
    } else {
      console.error("선택된 정보가 부족합니다.");
    }
  });
});

document.querySelector(".select-info").addEventListener("click", async (e) => {
  e.preventDefault();

  const selectInfo = e.currentTarget;
  const theater = selectInfo.getAttribute("data-theater");
  const movie = selectInfo.getAttribute("data-movie");
  const date = selectInfo.getAttribute("data-date");
  const auditorium = selectInfo.getAttribute("data-auditorium");
  const time = selectInfo.getAttribute("data-time");
  const screeningId = selectInfo.getAttribute("data-screening-id");

  if (!theater || !movie || !date || !auditorium || !time || !screeningId) {
    Swal.fire({
      icon: "warning",
      text: "모든 정보를 선택해주세요.",
      confirmButtonColor: "#0022ff",
      confirmButtonText: "다시선택",
    });
    return;
  }

  try {
    const response = await fetch("/reservation/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        theater,
        movie,
        date,
        auditorium,
        time,
        screeningId,
      }),
    });

    if (!response.ok) {
      throw new Error("서버에 정보를 저장하는데 실패했습니다.");
    }

    window.location.href = "/reservation/seat_sell";
  } catch (error) {
    console.error("예약 정보 저장 실패:", error);
    Swal.fire("예약 정보를 저장하는 중 오류가 발생했습니다.");
  }
});
