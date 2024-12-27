// 찜한 목록에 있는지 여부
function isExistFavorite() {
  if (isExist === "true") {
    document.querySelector(".follow-btn").innerHTML = "찜 제거";
  } else {
    document.querySelector(".follow-btn").innerHTML = "찜 추가";
  }
}
isExistFavorite();

// DOM 조작 및 섹션 초기화를 위한 함수
function clearSections() {
  const sections = [
    ".overview",
    ".director_row",
    ".actor_row",
    ".review_row",
    ".movie_similar_row",
    ".movie_director_row",
  ];
  sections.forEach((section) => {
    document.querySelector(section).innerHTML = "";
  });
}

// fetch 에러 핸들링 및 응답 처리 공통 함수
function handleFetchResponse(response) {
  if (!response.ok) {
    return response.text().then((errorMessage) => {
      throw new Error(errorMessage);
    });
  }
  return response.json();
}

// 주요 정보 버튼 클릭 핸들러
function handleOverviewClick(data) {
  clearSections();
  const htmlContent = `
    <div class="section-title"><h5>개요</h5></div>
    <div class="anime__review__item overview">${data.overview}</div>
  `;
  document.querySelector(".overview").innerHTML = htmlContent;
}

// 감독/출연 버튼 클릭 핸들러
function handleDirectorActorClick(data) {
  clearSections();

  const rolesMap = new Map();

  // 역할별 분류
  data.personDtos.forEach((personDto) => {
    personDto.moviePersonDtos.forEach((moviePersonDto) => {
      if (!rolesMap.has(moviePersonDto.role)) {
        rolesMap.set(moviePersonDto.role, []);
      }
      rolesMap.get(moviePersonDto.role).push(personDto);
    });
  });

  const directors = rolesMap.get("Director") || [];
  const actors = rolesMap.get("Actor") || [];

  // 감독 섹션 생성
  let directorContent = `<div class="section-title"><h5>감독</h5></div>`;
  directors.forEach((director) => {
    directorContent += `
      <div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">
        <div class="product__item mb-2">
          <a href="personDetail?id=${director.id}">
            <img src="https://image.tmdb.org/t/p/w500${director.profilePath}" 
                 alt="" class="product__item__pic set-bg">
          </a>
        </div>
        <div class="product__item__text mx-4">
          <h5><a href="personDetail?id=${director.id}">${director.name}</a></h5>
          <div class="text-white">Director</div>
        </div>
      </div>
    `;
  });
  document.querySelector(".director_row").innerHTML = directorContent;

  // 출연 섹션 생성
  let actorContent = `<div class="section-title"><h5>출연</h5></div>`;
  actors.forEach((actor) => {
    actorContent += `
      <div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">
        <div class="product__item mb-2">
          <a href="personDetail?id=${actor.id}">
            <img src="https://image.tmdb.org/t/p/w500${actor.profilePath}" 
                 alt="" class="product__item__pic set-bg">
          </a>
        </div>
        <div class="product__item__text mx-4">
          <h5><a href="personDetail?id=${actor.id}">${actor.name}</a></h5>
          <div class="text-white">
            ${actor.moviePersonDtos
              .map((mpDto) => `${mpDto.character} 역`)
              .join(", ")}
          </div>
        </div>
      </div>
    `;
  });
  document.querySelector(".actor_row").innerHTML = actorContent;
}

function handleReviewClick() {
  clearSections();

  document.getElementById("reviewreview").removeAttribute("hidden");
  // let str = "";
  // str += `<div class="col-lg-8 col-md-8">`;
  // str += `<div class="anime__details__review">`;
  // str += `<div class="section-title"><h5>관람평</h5></div>`;
  // // array.forEach((element) => {
  // //   str += `<div class="anime__review__item">`;
  // //   str += `<div class="anime__review__item__pic">`;
  // //   str += `<img src="img/anime/review-1.jpg" alt="" /></div>`;
  // //   str += `<div class="anime__review__item__text">`;
  // //   str += `<h6>작성자</h6><p>댓글 내용</p>`;
  // //   str += `</div></div>`;
  // // });
  // str += `<div class="anime__details__form">`;
  // str += `<div class="section-title">`;
  // str += `<h5>관람평 작성하기</h5></div>`;
  // str += `<form action="" method="post" id="reviewForm" >`;
  // str += `<textarea class="m-0" placeholder="Your Comment" id="comment"></textarea>`;
  // // 평점 선택 섹션
  // str += `<div class="star-rating">`;
  // str += `<input type="radio" id="star5" name="rating" value="5" /><label for="star5" title="5 stars" class="fa-solid fa-star"></label>`;
  // str += `<input type="radio" id="star4" name="rating" value="4" /><label for="star4" title="4 stars" class="fa-solid fa-star"></label>`;
  // str += `<input type="radio" id="star3" name="rating" value="3" /><label for="star3" title="3 stars" class="fa-solid fa-star"></label>`;
  // str += `<input type="radio" id="star2" name="rating" value="2" /><label for="star2" title="2 stars" class="fa-solid fa-star"></label>`;
  // str += `<input type="radio" id="star1" name="rating" value="1" /><label for="star1" title="1 star" class="fa-solid fa-star"></label>`;
  // str += `</div>`;
  // str += `<button type="submit">`;
  // str += `<i class="fa fa-location-arrow review-btn"></i> 관람평 등록</button>`;
  // str += `</form></div></div></div>`;

  // document.querySelector(".review_row").innerHTML = str;
}

function handleRecommendClick() {
  document.querySelector("#btnradio4").addEventListener("click", (e) => {
    fetch(`/rest/movieDetail/genres/${movieId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to add to favorites");
        }
        return response.json(); // 서버에서 전송된 텍스트 응답
      })
      .then((data) => {
        console.log(data);
        document.querySelector(".overview").innerHTML = "";
        document.querySelector(".director_row").innerHTML = "";
        document.querySelector(".actor_row").innerHTML = "";
        document.querySelector(".review_row").innerHTML = "";

        str = "";
        document.querySelector(".movie_similar_row").innerHTML = str;
        str = `<div class="row trending__product-row actor_row">`;
        str = `<div class="section-title"><h5>비슷한 장르의 영화</h5></div>`;
        data.slice(0, 8).forEach((movie) => {
          str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
          str += `<div class="product__item mb-2">`;
          str += `<a href="personDetail?id=${movie.id}"><img src=${
            "https://image.tmdb.org/t/p/w500" + movie.posterPath
          } alt="" class="product__item__pic set-bg"></a></div>`;
          str += `<div class="product__item__text mx-4 pt-0">`;
          str += `<h5><a href="personDetail?id=${movie.id}">${movie.title}</a></h5>`;
          str += `<div class="text-white">${movie.releaseDate}`;
          str += `</div>`;
          str += `</div></div>`;
        });
        document.querySelector(".movie_similar_row").innerHTML = str;
      })
      .catch((error) => {
        alert("Failed to add to favorites: " + error.message); // 오류 메시지
      });

    fetch(`/rest/movieDetail/director/${directorId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to add to favorites");
        }
        return response.json(); // 서버에서 전송된 텍스트 응답
      })
      .then((data) => {
        console.log(data);
        document.querySelector(".overview").innerHTML = "";
        document.querySelector(".director_row").innerHTML = "";
        document.querySelector(".actor_row").innerHTML = "";
        document.querySelector(".review_row").innerHTML = "";

        str = "";
        document.querySelector(".movie_director_row").innerHTML = str;
        str = `<div class="row trending__product-row actor_row">`;
        str = `<div class="section-title"><h5>${directorName} 감독의 다른 영화</h5></div>`;
        data.forEach((movie) => {
          str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
          str += `<div class="product__item mb-2">`;
          str += `<a href="personDetail?id=${movie.id}"><img src=${
            "https://image.tmdb.org/t/p/w500" + movie.posterPath
          } alt="" class="product__item__pic set-bg"></a></div>`;
          str += `<div class="product__item__text mx-4 pt-0">`;
          str += `<h5><a href="personDetail?id=${movie.id}">${movie.title}</a></h5>`;
          str += `<div class="text-white">${movie.releaseDate}`;
          str += `</div>`;
          str += `</div></div>`;
        });
        document.querySelector(".movie_director_row").innerHTML = str;
      })
      .catch((error) => {
        alert("Failed to add to favorites: " + error.message); // 오류 메시지
      });
  });
}

// 이벤트 핸들러 구조화
const buttonHandlers = {
  btnradio1: (data) => handleOverviewClick(data),
  btnradio2: (data) => handleDirectorActorClick(data),
  btnradio3: () => handleReviewClick(),
  btnradio4: () => handleRecommendClick(),
};

// 영화 상세 정보 fetch 및 이벤트 연결
fetch(`/rest/movieDetail/${movieId}`)
  .then(handleFetchResponse)
  .then((data) => {
    console.log(data);
    // 기본 개요 표시
    handleOverviewClick(data);

    // 각 버튼에 이벤트 핸들러 연결
    Object.entries(buttonHandlers).forEach(([buttonId, handler]) => {
      document
        .querySelector(`#${buttonId}`)
        .addEventListener("click", () => handler(data));
    });
  })
  .catch((error) => {
    console.error("Error fetching movie details:", error);
    alert("영화 정보를 가져오는 중 문제가 발생했습니다.");
  });

// 찜 버튼 클릭 핸들러
document.querySelector(".follow-btn").addEventListener("click", () => {
  console.log("찜 버튼 클릭");
  if (isLogin !== "false") {
    fetch(`/rest/movieDetail/${movieId}`, { method: "POST" })
      .then(handleFetchResponse)
      .then((message) => {
        alert(message);
        isExist = isExist === "true" ? "false" : "true";
        isExistFavorite();
      })
      .catch((error) => {
        console.error("Error adding to favorites:", error);
        alert(`찜 등록에 실패했습니다: ${error.message}`);
      });
  }
});

// 영화 상세 정보
fetch(`/rest/movieDetail/${movieId}`)
  .then((response) => {
    if (!response.ok) throw new Error("에러");

    return response.json();
  })
  .then((data) => {
    console.log(data);

    let str = `<div class="section-title"><h5>개요</h5></div>`;
    str += `<div class="anime__review__item overview">${data.overview}</div>`;
    document.querySelector(".overview").innerHTML = str;

    // 주요 정보 버튼 클릭 시
    document.querySelector("#btnradio1").addEventListener("click", () => {
      console.log("버튼1 클릭");
      document.querySelector(".director_row").innerHTML = "";
      document.querySelector(".actor_row").innerHTML = "";
      document.querySelector(".review_row").innerHTML = "";
      document.querySelector(".movie_similar_row").innerHTML = "";
      document.querySelector(".movie_director_row").innerHTML = "";
      // document.getElementById("reviewreview").setAttribute("hidden", "true");

      let str = `<div class="section-title"><h5>개요</h5></div>`;
      str += `<div class="anime__review__item overview">${data.overview}</div>`;

      document.querySelector(".overview").innerHTML = str;
    });

    // 감독/출연 버튼 클릭 시
    document.querySelector("#btnradio2").addEventListener("click", () => {
      console.log("버튼2 클릭");
      document.querySelector(".overview").innerHTML = "";
      document.querySelector(".review_row").innerHTML = "";
      document.querySelector(".movie_similar_row").innerHTML = "";
      document.querySelector(".movie_director_row").innerHTML = "";
      // document.getElementById("reviewreview").setAttribute("hidden", "true");

      const directorList = new Set();
      const actorList = new Set();

      data.personDtos.forEach((personDto) => {
        personDto.moviePersonDtos.forEach((moviePersonDto) => {
          // Director 역할이 있는 사람은 directorList에 추가
          if (moviePersonDto.role && moviePersonDto.role === "Director") {
            directorList.add(personDto);
          }
          // role이 null인 사람은 actorList에 추가
          if (moviePersonDto.role && moviePersonDto.role === "Actor") {
            actorList.add(personDto);
          }
        });
      });

      // Set을 다시 Array로 변환
      const directorListArray = Array.from(directorList);
      const actorListArray = Array.from(actorList);

      console.log(directorListArray);
      console.log(actorListArray);

      let str = `<div class="section-title"><h5>감독</h5></div>`;
      directorListArray.forEach((director) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="personDetail?id=${director.id}"><img src=${
          "https://image.tmdb.org/t/p/w500" + director.profilePath
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4">`;
        str += `<h5><a href="personDetail?id=${director.id}">${director.name}</a></h5>`;
        str += `<div class="text-white">Director</div>`;
        str += `</div></div></div>`;
      });

      document.querySelector(".director_row").innerHTML = str;
      str = `<div class="row trending__product-row actor_row">`;
      str = `<div class="section-title"><h5>출연</h5></div>`;
      actorListArray.forEach((actor) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="personDetail?id=${actor.id}"><img src=${
          "https://image.tmdb.org/t/p/w500" + actor.profilePath
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4">`;
        str += `<h5><a href="personDetail?id=${actor.id}">${actor.name}</a></h5>`;
        str += `<div class="text-white">`;
        actor.moviePersonDtos.forEach((moviePersonDto) => {
          str += `${moviePersonDto.character} 역`;
        });
        str += `</div>`;
        str += `</div></div>`;
      });
      document.querySelector(".actor_row").innerHTML = str;
    });
  });

// // 평점/리뷰 버튼 클릭 시
// document.querySelector("#btnradio3").addEventListener("click", () => {
//   console.log("버튼3 클릭");
//   document.querySelector(".overview").innerHTML = "";
//   document.querySelector(".director_row").innerHTML = "";
//   document.querySelector(".actor_row").innerHTML = "";
//   document.querySelector(".movie_similar_row").innerHTML = "";
//   document.querySelector(".movie_director_row").innerHTML = "";

//   document.getElementById("reviewreview").removeAttribute("hidden");

//   // str = "";
//   // str += `<div class="col-lg-8 col-md-8">`;
//   // str += `<div class="anime__details__review">`;
//   // str += `<div class="section-title"><h5>관람평</h5></div>`;
//   // // array.forEach((element) => {
//   // //   str += `<div class="anime__review__item">`;
//   // //   str += `<div class="anime__review__item__pic">`;
//   // //   str += `<img src="img/anime/review-1.jpg" alt="" /></div>`;
//   // //   str += `<div class="anime__review__item__text">`;
//   // //   str += `<h6>작성자</h6><p>댓글 내용</p>`;
//   // //   str += `</div></div>`;
//   // // });
//   // str += `<div class="anime__details__form">`;
//   // str += `<div class="section-title">`;
//   // str += `<h5>관람평 작성하기</h5></div>`;
//   // str += `<form action="" method="post" id="reviewForm" >`;
//   // str += `<textarea class="m-0" placeholder="Your Comment" id="comment"></textarea>`;
//   // // 평점 선택 섹션
//   // str += `<div class="star-rating">`;
//   // str += `<input type="radio" id="star5" name="rating" value="5" /><label for="star5" title="5 stars" class="fa-solid fa-star"></label>`;
//   // str += `<input type="radio" id="star4" name="rating" value="4" /><label for="star4" title="4 stars" class="fa-solid fa-star"></label>`;
//   // str += `<input type="radio" id="star3" name="rating" value="3" /><label for="star3" title="3 stars" class="fa-solid fa-star"></label>`;
//   // str += `<input type="radio" id="star2" name="rating" value="2" /><label for="star2" title="2 stars" class="fa-solid fa-star"></label>`;
//   // str += `<input type="radio" id="star1" name="rating" value="1" /><label for="star1" title="1 star" class="fa-solid fa-star"></label>`;
//   // str += `</div>`;
//   // str += `<button type="submit">`;
//   // str += `<i class="fa fa-location-arrow review-btn"></i> 관람평 등록</button>`;
//   // str += `</form></div></div></div>`;

//   // document.querySelector(".review_row").innerHTML = str;
// });

document.querySelector("#btnradio4").addEventListener("click", (e) => {
  fetch(`/rest/movieDetail/genres/${movieId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add to favorites");
      }
      return response.json(); // 서버에서 전송된 텍스트 응답
    })
    .then((data) => {
      console.log(data);
      document.querySelector(".overview").innerHTML = "";
      document.querySelector(".director_row").innerHTML = "";
      document.querySelector(".actor_row").innerHTML = "";
      document.querySelector(".review_row").innerHTML = "";

      str = "";
      document.querySelector(".movie_similar_row").innerHTML = str;
      str = `<div class="row trending__product-row actor_row">`;
      str = `<div class="section-title"><h5>비슷한 장르의 영화</h5></div>`;
      data.slice(0, 8).forEach((movie) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="personDetail?id=${movie.id}"><img src=${
          "https://image.tmdb.org/t/p/w500" + movie.posterPath
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4 pt-0">`;
        str += `<h5><a href="personDetail?id=${movie.id}">${movie.title}</a></h5>`;
        str += `<div class="text-white">${movie.releaseDate}`;
        str += `</div>`;
        str += `</div></div>`;
      });
      document.querySelector(".movie_similar_row").innerHTML = str;
    })
    .catch((error) => {
      alert("Failed to add to favorites: " + error.message); // 오류 메시지
    });

  fetch(`/rest/movieDetail/director/${directorId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add to favorites");
      }
      return response.json(); // 서버에서 전송된 텍스트 응답
    })
    .then((data) => {
      console.log(data);
      document.querySelector(".overview").innerHTML = "";
      document.querySelector(".director_row").innerHTML = "";
      document.querySelector(".actor_row").innerHTML = "";
      document.querySelector(".review_row").innerHTML = "";

      str = "";
      document.querySelector(".movie_director_row").innerHTML = str;
      str = `<div class="row trending__product-row actor_row">`;
      str = `<div class="section-title"><h5>${directorName} 감독의 다른 영화</h5></div>`;
      data.forEach((movie) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="personDetail?id=${movie.id}"><img src=${
          "https://image.tmdb.org/t/p/w500" + movie.posterPath
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4 pt-0">`;
        str += `<h5><a href="personDetail?id=${movie.id}">${movie.title}</a></h5>`;
        str += `<div class="text-white">${movie.releaseDate}`;
        str += `</div>`;
        str += `</div></div>`;
      });
      document.querySelector(".movie_director_row").innerHTML = str;
    })
    .catch((error) => {
      alert("Failed to add to favorites: " + error.message); // 오류 메시지
    });
});

document.querySelector(".follow-btn").addEventListener("click", (e) => {
  console.log("찜 버튼 클릭");

  if (isLogin != "false") {
    fetch(`/rest/movieDetail/${movieId}`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to add to favorites");
        }
        return response.text(); // 서버에서 전송된 텍스트 응답
      })
      .then((data) => {
        alert(data); // 성공 메시지 표시
        // button.textContent = "Added!"; // 버튼 텍스트 변경
        // button.disabled = true; // 버튼 비활성화
      })
      .catch((error) => {
        alert("Failed to add to favorites: " + error.message); // 오류 메시지
      });
    if (isExist === "true") {
      isExist = "false";
    } else {
      isExist = "true";
    }
    isExistFavorite();
  }
});

document.querySelectorAll(".star-rating input").forEach((star) => {
  star.addEventListener("change", (event) => {
    console.log(`Selected rating: ${event.target.value}`);
    // 추가 작업: 서버에 전송하거나 UI 업데이트
  });
});

// 리뷰
const reviewForm = document.querySelector("#reviewForm");

reviewForm.addEventListener("submit", function (e) {
  e.preventDefault(); // 폼의 기본 제출을 막음

  // 입력된 데이터 가져오기
  const content = document.getElementById("comment").value;

  console.log(content);
  console.log(movieId);

  // AJAX 요청을 사용하여 서버에 데이터를 보냄
  fetch("/review/submit", {
    method: "POST", // POST 요청
    headers: {
      "Content-Type": "application/json", // 데이터를 JSON 형식으로 보냄
    },
    body: JSON.stringify({ content: content, movieId: movieId }), // 전송할 데이터
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      alert("관람평이 등록되었습니다!");
      console.log(data);
    })
    .catch((error) => {
      console.error("서버 요청 중 오류 발생:", error);
    });
});
