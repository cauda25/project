console.log(orderId);

window.addEventListener("beforeunload", function () {
  fetch(`/rest/exit?orderId=${orderId}`, { method: "POST" })
    .then((response) => response.json())
    .then((data) => {
      console.log("Order items removed from database.");
    })
    .catch((error) => {
      console.error("Error removing order items:", error);
    });
});
