const memberName = sessionStorage.getItem("name");
const email = sessionStorage.getItem("email");
const phoneNumber = sessionStorage.getItem("phone");

fetch(`/rest/success/${orderId}`, {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    name: memberName,
    email: email,
    phone: phoneNumber,
  }),
})
  .then((response) => response.json())
  .then((data) => {
    console.log(data);
  })
  .catch((error) => {
    console.error(error);
  });
