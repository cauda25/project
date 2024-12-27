const id = document.querySelector("[name='id']").value;
const url = "https://api.themoviedb.org/3/movie/" + id + "?language=ko-KR";
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

    // 영화 상세
    let str = "";
    str += `<div class="col-lg-4"><img src=${
      "https://image.tmdb.org/t/p/original" + json.poster_path
    } alt="" class="product__item__pic set-bg"></div>`;
    str += `<div class="col-lg-8">`;
    str += `<h1 class="display-4">${json.title}</h1>`;
    str += `<p class="lead my-3">${json.overview}</p></div>`;

    document.querySelector(".bg-body-secondary").innerHTML = str;
  })
  .catch((err) => console.error(err));

function isExist() {
  if (isExist) {
    document.querySelector(".follow-btn").innerHTML = "찜 제거";
  } else {
    document.querySelector(".follow-btn").innerHTML = "찜 추가";
  }
}
isExist();
