const page = document.querySelector("[name='page']").value;
const movieList = document.querySelector("[name='movieList']").value;
const genre = document.querySelector("[name='genre']").value;
const type = document.querySelector("[name='type']").value;
const keyword = document.querySelector("[name='keyword']").value;
let url = "";

// 무비 목록 제목
switch (movieList) {
  case "now_playing":
    document.querySelector(".movieListTitle").innerHTML = "현재 상영작";
    break;
  case "upcoming":
    document.querySelector(".movieListTitle").innerHTML = "상영 예정작";
    break;
  case "popular":
    document.querySelector(".movieListTitle").innerHTML = "전체 영화 목록";
    break;
  default:
    document.querySelector(".movieListTitle").innerHTML = "현재 상영작";
    break;
}

// 데이터 가져오기
if (keyword != "") {
  let t = "";
  switch (type) {
    case "a":
      t = "movie";
      break;
    case "m":
      t = "movie";
      break;
    case "p":
      t = "person";
      break;
    default:
      t = "movie";
      break;
  }

  url =
    "https://api.themoviedb.org/3/search/" +
    t +
    "?query=" +
    keyword +
    "&language=ko-KR&page=" +
    page;

  document.querySelector(".movieListTitle").innerHTML = "검색: " + keyword;
} else if (genre == "") {
  // 장르 값 없을 경우
  url =
    "https://api.themoviedb.org/3/movie/" +
    movieList +
    "?language=ko-KR&region=KR&page=" +
    page;
} else {
  // 장르 값 있을 경우
  url =
    "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=ko-KR&sort_by=popularity.desc&with_genres=" +
    genre +
    "&language=ko-KR&page=" +
    page;
}

document.querySelector("#searchForm").addEventListener("submit", (e) => {
  e.preventDefault();
  document.querySelector("[name='page']").value = "1";
  document.querySelector("[name='movieList']").value = "popular";
  document.querySelector("[name='genre']").value;
  e.target.submit();
});

const options = {
  method: "GET",
  headers: {
    accept: "application/json",
    Authorization:
      "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhN2UwMzVjMzUyODU4ZDRmMTRiMDIxM2Y5NDE1ODI3YyIsIm5iZiI6MTczMzI5NzU5Ny4zMDU5OTk4LCJzdWIiOiI2NzUwMDViZDM1NWRiYzBiMTVkN2E1NWYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.ov8q6kG-1OXY0deIpXpF_2FqmMM9Z8YPEeqoIyZu8dg",
  },
};

fetch(url, options)
  .then((res) => res.json())
  .then((json) => {
    console.log(json);

    // 영화 리스트
    results = json.results;

    let str = "";
    results.forEach((result) => {
      str += `<div class="col-lg-3 col-md-4 col-sm-6 product">`;
      str += `<div class="product__item mb-2">`;
      str += `<a href="movieDetail?id=${
        result.id
      }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
        "https://image.tmdb.org/t/p/w500" + result.poster_path
      } alt="" class="product__item__pic set-bg"></a></div>`;
      str += `<div class="product__item__text mx-4">`;
      str += `<h5><a href="movieDetail?id=${result.id}&movieList=${movieList}&genre=${genre}&page=${page}">${result.title}</a></h5>`;
      str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${result.release_date}</ul>`;
      str += `</div></div>`;
    });
    document.querySelector(".trending__product-row").innerHTML = str;

    // 페이지
    const totalPage = json.total_pages;
    const size = 20;
    const tempEnd = Math.ceil(page / 10.0) * 10;
    const start = tempEnd - 9;
    const prev = start > 1;
    const end = totalPage > tempEnd ? tempEnd : totalPage;
    const next = totalPage > tempEnd;

    str = "";
    str += `<li class="page-item `;
    str += `${prev ? "" : "disabled"}`;
    str += `"><a class="page-link text-light bg-transparent" href="movieList?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=`;
    str += `${page - 10}`;
    str += `">Previous</a></li>`;
    for (let i = start; i < end + 1; i++) {
      str += `<li th:class="page-item" aria-current="page">`;
      str += `<a class="page-link text-light `;
      str += `${
        i == page ? "bg-danger border-light active" : "bg-transparent"
      }" `;
      str += `href="movieList?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=${i}">${i}</a></li>`;
    }
    str += `<li class="page-item `;
    str += `${next ? "" : "disabled"}`;
    str += `"><a class="page-link text-light bg-transparent" href="movieList?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=`;
    str += `${parseInt(page) + 10}`;
    str += `">Next</a></li>`;

    document.querySelector(".pagination").innerHTML = str;
  })
  .catch((err) => console.error(err));

// 메뉴 탭 active 설정
if (movieList == "now_playing") {
  document.querySelector(".nowPlaying").className += " bg-danger active";
} else if (movieList == "upcoming") {
  document.querySelector(".upcoming").className += " bg-danger active";
} else {
  document.querySelector(".popular").className += " bg-danger active";
}

if (type == "a") {
  url =
    "https://api.themoviedb.org/3/search/person?query=" +
    keyword +
    "&language=ko-KR&page=" +
    page;
  fetch(url, options)
    .then((res) => res.json())
    .then((json) => {
      console.log(json);
      results = json.results;

      // let str = "";
      // results.forEach((result) => {
      //   str += `<div class="col-lg-3 col-md-4 col-sm-6 product">`;
      //   str += `<div class="product__item mb-2">`;
      //   str += `<a href="movieDetail?id=${
      //     result.id
      //   }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
      //     "https://image.tmdb.org/t/p/w500" + result.poster_path
      //   } alt="" class="product__item__pic set-bg"></a></div>`;
      //   str += `<div class="product__item__text mx-4">`;
      //   str += `<h5><a href="movieDetail?id=${result.id}&movieList=${movieList}&genre=${genre}&page=${page}">${result.title}</a></h5>`;
      //   str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${result.release_date}</ul>`;
      //   str += `</div></div>`;
      // });
      // document.querySelector(".trending__product-row").innerHTML = str;
    })
    .catch((err) => console.error(err));
}

// 장르 메뉴
const genreUrl = "https://api.themoviedb.org/3/genre/movie/list?language=ko";

fetch(genreUrl, options)
  .then((res) => res.json())
  .then((json) => {
    str = `<li><a class="dropdown-item" href="movieList?movieList=popular&genre=&page=1">전체</a></li>`;
    const genres = json.genres;
    genres.forEach((g) => {
      str += `<li><a class="dropdown-item" href="movieList?movieList=popular&genre=${g.id}&page=1">${g.name}</a></li>`;
      if (genre == g.id) {
        document.querySelector("h3").innerHTML = g.name;
      }
    });
    document.querySelector(".dropdown-menu").innerHTML = str;
  })
  .catch((err) => console.error(err));
