function isExistFavorite() {
  if (isExist === "true") {
    document.querySelector(".follow-btn").innerHTML = "찜 제거";
  } else {
    document.querySelector(".follow-btn").innerHTML = "찜 추가";
  }
}
isExistFavorite();

function clearSection() {
  document.querySelector(".overview").innerHTML = "";
  document.querySelector(".director_row").innerHTML = "";
  document.querySelector(".actor_row").innerHTML = "";
  document.querySelector(".review_row").innerHTML = "";
  document.querySelector(".movie_similar_row").innerHTML = "";
  document.querySelector(".movie_director_row").innerHTML = "";
  document.getElementById("reviewreview").setAttribute("hidden", "");
}

fetch(`/rest/movieDetail/${movieId}`)
  .then((response) => {
    if (!response.ok) throw new Error("에러");

    return response.json();
  })
  .then((data) => {
    console.log(data);
    // 주요 정보 실행
    clearSection();

    let str = `<div class="section-title"><h5>개요</h5></div>`;
    if (data.overview != null) {
      str += `<div class="anime__review__item overview">${data.overview}</div>`;
    }

    document.querySelector(".overview").innerHTML = str;

    // 주요 정보 버튼 클릭 시
    document.querySelector("#btnradio1").addEventListener("click", () => {
      console.log("버튼1 클릭");
      clearSection();

      let str = `<div class="section-title"><h5>개요</h5></div>`;
      if (data.overview != null) {
        str += `<div class="anime__review__item overview">${data.overview}</div>`;
      }

      document.querySelector(".overview").innerHTML = str;
    });

    // 감독/출연 버튼 클릭 시
    document.querySelector("#btnradio2").addEventListener("click", () => {
      console.log("버튼2 클릭");
      clearSection();

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
          director.profilePath != null
            ? "https://image.tmdb.org/t/p/w500" + director.profilePath
            : "https://placehold.co/217x325?text=Director"
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
          actor.profilePath != null
            ? "https://image.tmdb.org/t/p/w500" + actor.profilePath
            : "https://placehold.co/217x325?text=Actor"
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

    // 평점/리뷰 버튼 클릭 시
    document.querySelector("#btnradio3").addEventListener("click", () => {
      console.log("버튼3 클릭");
      clearSection();
      document.getElementById("reviewreview").removeAttribute("hidden");
      // str = "";
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
      // str += `<form action="#">`;
      // str += `<textarea class="m-0" placeholder="Your Comment"></textarea>`;
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
    });
  });

function addToFavoriteMovie() {
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
}

document.querySelector(".follow-btn").addEventListener("click", (e) => {
  console.log("찜하기 버튼 클릭");
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
        addToFavoriteMovie();
        if (isExist === "true") {
          isExist = "false";
        } else {
          isExist = "true";
        }
        isExistFavorite();
      } else {
        if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
          window.location.href = "/member/login"; // 로그인 페이지로 이동
        }
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
});

document.querySelector("#btnradio4").addEventListener("click", (e) => {
  console.log("클릭");

  fetch(`/rest/movieDetail/genres/${movieId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add to favorites");
      }
      return response.json(); // 서버에서 전송된 텍스트 응답
    })
    .then((data) => {
      console.log("장르 영화", data);
      document.querySelector(".overview").innerHTML = "";
      document.querySelector(".director_row").innerHTML = "";
      document.querySelector(".actor_row").innerHTML = "";
      document.querySelector(".review_row").innerHTML = "";
      document.getElementById("reviewreview").setAttribute("hidden", "");

      str = "";
      document.querySelector(".movie_similar_row").innerHTML = str;
      str = `<div class="row trending__product-row actor_row">`;
      str = `<div class="section-title"><h5>비슷한 장르의 영화</h5></div>`;
      data.slice(0, 8).forEach((movie) => {
        if (movie.id != movieId) {
          str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
          str += `<div class="product__item mb-2">`;
          str += `<a href="movieDetail?id=${movie.id}"><img src=${
            movie.posterPath != null
              ? "https://image.tmdb.org/t/p/w500" + movie.posterPath
              : "https://placehold.co/217x325?text=Movie"
          } alt="" class="product__item__pic set-bg"></a></div>`;
          str += `<div class="product__item__text mx-4 pt-0">`;
          str += `<h5><a href="movieDetail?id=${movie.id}">${movie.title}</a></h5>`;
          str += `<div class="text-white">${movie.releaseDate}`;
          str += `</div>`;
          str += `</div></div>`;
        }
      });
      document.querySelector(".movie_similar_row").innerHTML = str;
    })
    .catch((error) => {
      alert("Failed to add to favorites: " + error.message); // 오류 메시지
    });

  if (directors > 0) {
    fetch(`/rest/movieDetail/director/${directors}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to add to favorites");
        }
        return response.json(); // 서버에서 전송된 텍스트 응답
      })
      .then((data) => {
        console.log("감독 영화", data);
        document.querySelector(".overview").innerHTML = "";
        document.querySelector(".director_row").innerHTML = "";
        document.querySelector(".actor_row").innerHTML = "";
        document.querySelector(".review_row").innerHTML = "";
        document.getElementById("reviewreview").setAttribute("hidden", "");

        str = "";
        document.querySelector(".movie_director_row").innerHTML = str;
        str += `<div class="row trending__product-row actor_row">`;
        str += `<div class="section-title"><h5>${directorName} 감독의 다른 영화</h5></div>`;
        data.slice(0, 8).forEach((movie) => {
          if (movie.id != movieId) {
            str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
            str += `<div class="product__item mb-2">`;
            str += `<a href="movieDetail?id=${movie.id}"><img src=${
              movie.posterPath != null
                ? "https://image.tmdb.org/t/p/w500" + movie.posterPath
                : "https://placehold.co/217x325?text=Movie"
            } alt="" class="product__item__pic set-bg"></a></div>`;
            str += `<div class="product__item__text mx-4 pt-0">`;
            str += `<h5><a href="movieDetail?id=${movie.id}">${movie.title}</a></h5>`;
            str += `<div class="text-white">${movie.releaseDate}`;
            str += `</div>`;
            str += `</div></div>`;
          }
        });
        document.querySelector(".movie_director_row").innerHTML = str;
      })
      .catch((error) => {
        alert("Failed to add to favorites: " + error.message); // 오류 메시지
      });
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const reviewList = document.getElementById("reviewList");
  const reviewForm = document.getElementById("reviewForm");
  const reviewSection = document.getElementById("reviewreview"); // 리뷰 섹션
  const btnRadio3 = document.getElementById("btnradio3"); // 평점/리뷰 버튼

  if (!reviewList || !reviewForm || !reviewSection || !btnRadio3) {
    console.warn("Review elements not found. Skipping review functionality.");
    return;
  }

  // 초기 설정: 리뷰 작성 섹션 숨기기
  reviewSection.style.display = "none";

  // 버튼 그룹 관련 섹션 DOM 요소
  const overviewSection = document.querySelector(".overview");
  const directorSection = document.querySelector(".director_row");
  const actorSection = document.querySelector(".actor_row");
  const reviewRow = document.querySelector(".review_row");
  const similarMoviesSection = document.querySelector(".movie_similar_row");
  const directorMoviesSection = document.querySelector(".movie_director_row");

  // 모든 섹션 숨기기 함수
  function clearSections() {
    reviewSection.style.display = "none";
    overviewSection.style.display = "none";
    directorSection.style.display = "none";
    actorSection.style.display = "none";
    reviewRow.style.display = "none";
    similarMoviesSection.style.display = "none";
    directorMoviesSection.style.display = "none";
  }

  // "평점/리뷰" 버튼 클릭 시 리뷰 섹션 표시
  btnRadio3.addEventListener("click", () => {
    clearSections();
    reviewSection.style.display = "block"; // 리뷰 섹션 보이기
  });

  // 다른 버튼 클릭 처리 (예시: 주요 정보, 감독/출연, 추천 버튼)
  document.getElementById("btnradio1").addEventListener("click", () => {
    clearSections();
    overviewSection.style.display = "block"; // 주요 정보 섹션 보이기
  });

  document.getElementById("btnradio2").addEventListener("click", () => {
    clearSections();
    directorSection.style.display = "block"; // 감독/출연 섹션 보이기
    actorSection.style.display = "block"; // 출연 섹션도 함께 보이기
  });

  document.getElementById("btnradio4").addEventListener("click", () => {
    clearSections();
    similarMoviesSection.style.display = "block"; // 추천 섹션 보이기
  });

  // 초기 데이터
  const reviews = [];
  let loggedInUserId = null; // 로그인 사용자 ID를 저장할 변수

  // 사용자 정보 가져오기
  function fetchUserInfo() {
    return fetch("/member/me")
      .then((response) => {
        if (!response.ok) throw new Error("사용자 정보를 가져올 수 없습니다.");
        return response.json();
      })
      .then((userInfo) => {
        loggedInUserId = userInfo.memberId; // 사용자 memberId 저장
        console.log("로그인 사용자 ID:", loggedInUserId);
      })
      .catch((error) => {
        console.error("사용자 정보 불러오기 실패:", error);
        loggedInUserId = null; // 로그인 실패 시 null로 설정
      });
  }

  // 리뷰 폼 제출 이벤트
  reviewForm.addEventListener("submit", (event) => {
    event.preventDefault();

    if (!loggedInUserId) {
      alert("로그인 후 댓글을 작성할 수 있습니다.");
      return;
    }

    const comment = document.getElementById("comment").value.trim();
    const rating = document.querySelector(
      'input[name="rating"]:checked'
    )?.value;

    if (!comment || !rating) {
      alert("댓글과 평점을 모두 입력해주세요.");
      return;
    }

    // 새 리뷰 추가
    reviews.push({ author: loggedInUserId, content: comment, rating });

    // 입력 필드 초기화
    document.getElementById("comment").value = "";
    renderReviews();
  });

  // 리뷰 렌더링 함수
  function renderReviews() {
    reviewList.innerHTML = ""; // 기존 리뷰 초기화
    reviews.forEach((review, index) => {
      const reviewItem = document.createElement("div");
      reviewItem.className = "anime__review__item";

      reviewItem.innerHTML = `
        <div class="anime__review__item__pic">
          <img src="img/anime/review-${index + 1}.jpg" alt="" />
        </div>
        <div class="anime__review__item__text">
          <h6>${review.author}</h6>
          <p>${review.content}</p>
          <div  style="color: #ffffff;">평점: ${"★".repeat(review.rating)}</div>
        </div>
      `;

      reviewList.appendChild(reviewItem);
    });
  }

  // 초기화: 사용자 정보 가져오기
  fetchUserInfo();
});
