const page = document.querySelector("[name='page']").value;
const movieList = document.querySelector("[name='movieList']").value;
const genre = document.querySelector("[name='genre']").value;
const type = document.querySelector("[name='type']").value;
const keyword = document.querySelector("[name='keyword']").value;

let url = "";
const short = keyword != "" && type == "a" ? true : false;

const options = {
  method: "GET",
  headers: {
    accept: "application/json",
    Authorization:
      "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhN2UwMzVjMzUyODU4ZDRmMTRiMDIxM2Y5NDE1ODI3YyIsIm5iZiI6MTczMzI5NzU5Ny4zMDU5OTk4LCJzdWIiOiI2NzUwMDViZDM1NWRiYzBiMTVkN2E1NWYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.ov8q6kG-1OXY0deIpXpF_2FqmMM9Z8YPEeqoIyZu8dg",
  },
};

// 검색 시 값 기본 변경
document.querySelector("#searchForm").addEventListener("submit", (e) => {
  e.preventDefault();
  document.querySelector("[name='page']").value = "1";
  document.querySelector("[name='movieList']").value = "popular";
  document.querySelector("[name='genre']").value = "";
  e.target.submit();
});

// 메뉴 탭 active 설정
if (movieList == "now_playing") {
  document.querySelector(".nowPlaying").className += " bg-danger active";
} else if (movieList == "upcoming") {
  document.querySelector(".upcoming").className += " bg-danger active";
} else {
  document.querySelector(".popular").className += " bg-danger active";
}

// 장르 메뉴
const genreUrl = "https://api.themoviedb.org/3/genre/movie/list?language=ko";
fetch(genreUrl, options)
  .then((res) => res.json())
  .then((json) => {
    console.log(json);
    str = `<li><a class="dropdown-item" href="list?movieList=popular&genre=&page=1">전체</a></li>`;
    const genres = json.genres;
    genres.forEach((g) => {
      str += `<li><a class="dropdown-item" href="list?movieList=popular&genre=${g.id}&page=1">${g.name}</a></li>`;
      if (genre == g.id) {
        document.querySelector(".header-title-text").innerHTML = g.name;
      }
    });
    document.querySelector(".dropdown-menu").innerHTML = str;
  })
  .catch((err) => console.error(err));

// 리스트 출력
function listfunction(url, str, selector) {
  fetch(url, options)
    .then((res) => res.json())
    .then((json) => {
      console.log(json);
      document.querySelector(selector).innerHTML = str(json);
      if (keyword == "" || type != "a") {
        paging(json.total_pages);
      }
    })
    .catch((err) => console.error(err));
}

// str 값
let movieStr = (json) => {
  // 영화 리스트
  results = json.results;
  if (short) {
    let str = `<div class="d-flex sub-head"><h3 class="text-white">영화 ${json.total_results}건</h3>`;
    str +=
      results.length > 4
        ? `<a class="btn btn-outline-danger more" href="list?movieList=movie&genre=${genre}&type=m&keyword=${keyword}&page=1">더보기</a></div>`
        : "</div>";
    console.log(results.length);
    let i = 0;
    while (i <= 3 && i < results.length) {
      console.log(i);
      str += `<div class="col-lg-3 col-md-4 col-sm-6 product">`;
      str += `<div class="product__item mb-2">`;
      str += `<a href="detail?id=${
        results[i].id
      }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
        "https://image.tmdb.org/t/p/w500" + results[i].poster_path
      } alt="" class="product__item__pic set-bg"></a></div>`;
      str += `<div class="product__item__text mx-4">`;
      str += `<h5><a href="detail?id=${results[i].id}&movieList=${movieList}&genre=${genre}&page=${page}">${results[i].title}</a></h5>`;
      str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${results[i].release_date}</ul>`;
      str += `</div></div>`;
      i++;
    }
    str += `<div class="mt-4"></div>`;
    return str;
  }
  let str = `<div class="d-flex sub-head"><h3 class="text-white">영화 ${json.total_results}건</h3></div>`;
  results.forEach((result) => {
    str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
    str += `<div class="product__item mb-2">`;
    str += `<a href="detail?id=${
      result.id
    }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
      "https://image.tmdb.org/t/p/w500" + result.poster_path
    } alt="" class="product__item__pic set-bg"></a></div>`;
    str += `<div class="product__item__text mx-4">`;
    str += `<h5><a href="detail?id=${result.id}&movieList=${movieList}&genre=${genre}&page=${page}">${result.title}</a></h5>`;
    str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${result.release_date}</ul>`;
    str += `</div></div>`;
  });
  return str;
};
let peopleStr = (json) => {
  // 인물 리스트
  results = json.results;
  if (short) {
    let str = `<div class="d-flex sub-head"><h3 class="text-white">인물 ${json.total_results}건</h3>`;
    str +=
      results.length > 4
        ? `<a class="btn btn-outline-danger more" href="list?movieList=person&genre=${genre}&type=p&keyword=${keyword}&page=1">더보기</a></div>`
        : "</div>";
    console.log(results.length);
    let i = 0;
    while (i <= 3 && i < results.length) {
      console.log(i);
      str += `<div class="col-lg-3 col-md-4 col-sm-6 product">`;
      str += `<div class="product__item mb-2">`;
      str += `<a href="detail?id=${
        results[i].id
      }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
        "https://image.tmdb.org/t/p/w500" + results[i].profile_path
      } alt="" class="product__item__pic set-bg"></a></div>`;
      str += `<div class="product__item__text mx-4">`;
      str += `<h5><a href="detail?id=${results[i].id}&movieList=${movieList}&genre=${genre}&page=${page}">${results[i].name}</a></h5>`;
      str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${results[i].release_date}</ul>`;
      str += `</div></div>`;
      i++;
    }
    return str;
  }

  let str = `<div class="d-flex sub-head"><h3 class="text-white">인물 ${json.total_results}건</h3></div>`;
  results.forEach((result) => {
    str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
    str += `<div class="product__item mb-2">`;
    str += `<a href="detail?id=${
      result.id
    }&movieList=${movieList}&genre=${genre}&page=${page}"><img src=${
      "https://image.tmdb.org/t/p/w500" + result.profile_path
    } alt="" class="product__item__pic set-bg"></a></div>`;
    str += `<div class="product__item__text mx-4">`;
    str += `<h5><a href="detail?id=${result.id}&movieList=${movieList}&genre=${genre}&page=${page}">${result.name}</a></h5>`;
    str += `<ul><li>예매율</li> 31.8%</ul><ul><li>개봉일</li> ${result.release_date}</ul>`;
    str += `</div></div>`;
  });
  return str;
};

// 페이지 출력
function paging(total) {
  // 페이지
  const totalPage = total;
  const size = 20;
  const tempEnd = Math.ceil(page / 10.0) * 10;
  const start = tempEnd - 9;
  const prev = start > 1;
  const end = totalPage > tempEnd ? tempEnd : totalPage;
  const next = totalPage > tempEnd;

  str = "";
  str += `<li class="page-item `;
  str += `${prev ? "" : "disabled"}`;
  str += `"><a class="page-link text-light bg-transparent" href="list?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=`;
  str += `${page - 10}`;
  str += `">Previous</a></li>`;
  for (let i = start; i < end + 1; i++) {
    str += `<li th:class="page-item" aria-current="page">`;
    str += `<a class="page-link text-light `;
    str += `${
      i == page ? "bg-danger border-light active" : "bg-transparent"
    }" `;
    str += `href="list?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=${i}">${i}</a></li>`;
  }
  str += `<li class="page-item `;
  str += `${next ? "" : "disabled"}`;
  str += `"><a class="page-link text-light bg-transparent" href="list?movieList=${movieList}&genre=${genre}&type=${type}&keyword=${keyword}&page=`;
  str += `${parseInt(page) + 10}`;
  str += `">Next</a></li>`;

  document.querySelector(".pagination").innerHTML = str;
}

// url 값
if (genre != "") {
  // 장르 값 있을 경우
  url =
    "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=ko-KR&sort_by=popularity.desc&with_genres=" +
    genre +
    "&language=ko-KR&page=" +
    page;
  listfunction(url, movieStr, ".trending__product-row");
} else if (keyword != "") {
  // 검색 값 있을 경우
  document.querySelector(".header-title-text").innerHTML = "검색: " + keyword;
  switch (type) {
    case "a":
      url =
        "https://api.themoviedb.org/3/search/movie?query=" +
        keyword +
        "&language=ko-KR&page=" +
        page;
      listfunction(url, movieStr, ".trending__product-row");
      url =
        "https://api.themoviedb.org/3/search/person?query=" +
        keyword +
        "&language=ko-KR&page=" +
        page;
      listfunction(url, peopleStr, ".trending__people-row");
      break;
    case "m":
      url =
        "https://api.themoviedb.org/3/search/movie?query=" +
        keyword +
        "&language=ko-KR&page=" +
        page;
      listfunction(url, movieStr, ".trending__product-row");
      break;
    case "p":
      url =
        "https://api.themoviedb.org/3/search/person?query=" +
        keyword +
        "&language=ko-KR&page=" +
        page;
      listfunction(url, peopleStr, ".trending__people-row");
      break;
    default:
      break;
  }
} else {
  // 장르, 검색 값 없을 경우
  switch (movieList) {
    case "now_playing":
      document.querySelector(".header-title-text").innerHTML = "현재 상영작";
      break;
    case "upcoming":
      document.querySelector(".header-title-text").innerHTML = "상영 예정작";
      break;
    case "popular":
      document.querySelector(".header-title-text").innerHTML = "전체 영화 목록";
      break;
    default:
      document.querySelector(".header-title-text").innerHTML = "현재 상영작";
      break;
  }
  url =
    "https://api.themoviedb.org/3/movie/" +
    movieList +
    "?language=ko-KR&region=KR&page=" +
    page;
  listfunction(url, movieStr, ".trending__product-row");
}
