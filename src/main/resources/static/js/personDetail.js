fetch(`/rest/personDetail/${personId}`)
  .then((response) => {
    if (!response.ok) throw new Error("에러");

    return response.json();
  })
  .then((data) => {
    console.log(data);

    let str = `<div class="section-title"><h5>필모그래피</h5></div>`;
    data.forEach((movie) => {
      str += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3 product">`;
      str += `<div class="product__item mb-2">`;
      str += `<a href="movieDetail?id=${movie.id}"><img src=${
        "https://image.tmdb.org/t/p/w500" + movie.posterPath
      } alt="" class="product__item__pic set-bg"></a></div>`;
      str += `<div class="product__item__text mx-4">`;
      str += `<h5><a href="movieDetail?id=${movie.id}">${movie.title}</a></h5>`;
      str += `<div class="text-white">${movie.releaseDate}</div>`;
      str += `</div></div></div>`;
    });

    document.querySelector(".trending__product-row").innerHTML = str;
})
