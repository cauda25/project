// 찜한 영화에 있을 시 버튼 텍스트 변경
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

// 해당 영화 상세 정보 가져오기
fetch(`/rest/movieDetail/${movieId}`)
  .then((response) => {
    if (!response.ok) throw new Error("에러");

    return response.json();
  })
  .then((data) => {
    console.log(data);
    // 주요 정보 실행
    clearSection();

    // 개요 화면 출력
    let str = `<div class="section-title"><h5>개요</h5></div>`;
    if (data.overview != null) {
      str += `<div class="anime__review__item overview">${data.overview}</div>`;
    }
    document.querySelector(".overview").innerHTML = str;

    // 감독, 출연자 정보 불러오기
    const directorList = new Array();
    const actorList = new Array();
    data.personDtos.forEach((personDto) => {
      personDto.moviePersonDtos.forEach((moviePersonDto) => {
        // Director 역할이 있는 사람은 directorList에 추가
        if (moviePersonDto.role && moviePersonDto.role === "Director") {
          directorList.push(personDto);
        }
        // Actor 역할이 있는 사람은 actorList에 추가
        if (moviePersonDto.role && moviePersonDto.role === "Actor") {
          actorList.push(personDto);
        }
      });
    });
    console.log(directorList);
    console.log(actorList);

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

      let str = `<div class="section-title"><h5>감독</h5></div>`;
      directorList.forEach((director) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="person/detail?id=${director.id}"><img src=${
          director.profilePath != null
            ? "https://image.tmdb.org/t/p/w500" + director.profilePath
            : "https://placehold.co/217x325?text=Director"
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4">`;
        str += `<h5><a href="person/detail?id=${director.id}">${director.name}</a></h5>`;
        str += `<div class="text-white">Director</div>`;
        str += `</div></div></div>`;
      });

      document.querySelector(".director_row").innerHTML = str;
      str = `<div class="row trending__product-row actor_row">`;
      str = `<div class="section-title"><h5>출연</h5></div>`;
      actorList.forEach((actor) => {
        str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
        str += `<div class="product__item mb-2">`;
        str += `<a href="person/detail?id=${actor.id}"><img src=${
          actor.profilePath != null
            ? "https://image.tmdb.org/t/p/w500" + actor.profilePath
            : "https://placehold.co/217x325?text=Actor"
        } alt="" class="product__item__pic set-bg"></a></div>`;
        str += `<div class="product__item__text mx-4">`;
        str += `<h5><a href="person/detail?id=${actor.id}">${actor.name}</a></h5>`;
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

    document.querySelector("#btnradio4").addEventListener("click", (e) => {
      console.log("클릭");

      fetch(`/rest/movieDetail/genres/${movieId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed");
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
            str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
            str += `<div class="product__item mb-2">`;
            str += `<a href="detail?id=${movie.id}"><img src=${
              movie.posterPath != null
                ? "https://image.tmdb.org/t/p/w500" + movie.posterPath
                : "https://placehold.co/217x325?text=Movie"
            } alt="" class="product__item__pic set-bg"></a></div>`;
            str += `<div class="product__item__text mx-4 pt-0">`;
            str += `<h5><a href="detail?id=${movie.id}">${movie.title}</a></h5>`;
            str += `<div class="text-white">${movie.releaseDate}`;
            str += `</div>`;
            str += `</div></div>`;
          });
          document.querySelector(".movie_similar_row").innerHTML = str;
        })
        .catch((error) => {
          console.log(error);
        });

      if (directorList.length > 0) {
        fetch(`/rest/movieDetail/director/${directorList[0].id}`)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Failed to add to favorites");
            }
            return response.json(); // 서버에서 전송된 텍스트 응답
          })
          .then((data) => {
            console.log("감독 영화", data);

            str = "";
            document.querySelector(".movie_director_row").innerHTML = str;
            str = `<div class="row trending__product-row actor_row">`;
            str = `<div class="section-title"><h5>${directorName} 감독의 다른 영화</h5></div>`;
            data.forEach((movie) => {
              str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 pt-0 product">`;
              str += `<div class="product__item mb-2">`;
              str += `<a href="detail?id=${movie.id}"><img src=${
                movie.posterPath != null
                  ? "https://image.tmdb.org/t/p/w500" + movie.posterPath
                  : "https://placehold.co/217x325?text=Movie"
              } alt="" class="product__item__pic set-bg"></a></div>`;
              str += `<div class="product__item__text mx-4 pt-0">`;
              str += `<h5><a href="detail?id=${movie.id}">${movie.title}</a></h5>`;
              str += `<div class="text-white">${movie.releaseDate}`;
              str += `</div>`;
              str += `</div></div>`;
            });
            document.querySelector(".movie_director_row").innerHTML = str;
          })
          .catch((error) => {
            alert("Failed to add to favorites: " + error.message); // 오류 메시지
          });
      }
    });
  });

// 찜한 영화에 추가
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
      console.log(error);
    });
}

// 찜하기 버튼 누를 시
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
      console.log(error);
    });
});

// 평점 클릭 시
document.querySelectorAll(".star-rating input").forEach((star) => {
  star.addEventListener("change", (event) => {
    console.log(`Selected rating: ${event.target.value}`);
    // 추가 작업: 서버에 전송하거나 UI 업데이트
  });
});
