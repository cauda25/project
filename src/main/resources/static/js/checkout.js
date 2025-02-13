(() => {
  "use strict";

  const form = document.querySelector("#shippingInfoForm");
  const validateBtn = document.querySelector(".checkout-btn");
  const nameInput = document.getElementById("name");
  const emailInput = document.getElementById("email");
  const phoneInput = document.getElementById("phoneNumber");

  // 유효성 검사 함수
  const validateField = (input, pattern) => {
    if (!pattern.test(input.value)) {
      input.classList.add("is-invalid");
      input.classList.remove("is-valid");
    } else {
      input.classList.remove("is-invalid");
      input.classList.add("is-valid");
    }
  };

  // 이벤트 리스너 등록
  nameInput.addEventListener("input", () => validateField(nameInput, /.+/));
  emailInput.addEventListener("input", () =>
    validateField(emailInput, /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)
  );
  phoneInput.addEventListener("input", () =>
    validateField(phoneInput, /^(01[0-9])-([0-9]{3,4})-([0-9]{4})$/)
  );

  validateField(nameInput, /.+/);
  validateField(emailInput, /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/);
  validateField(phoneInput, /^(01[0-9])-([0-9]{3,4})-([0-9]{4})$/);

  // 유효성 검사 버튼 클릭 시 실행
  validateBtn.addEventListener("click", (event) => {
    event.preventDefault(); // 폼 제출 방지

    validateField(nameInput, /.+/);
    validateField(emailInput, /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/);
    validateField(phoneInput, /^(01[0-9])-([0-9]{3,4})-([0-9]{4})$/);

    // 모든 필드가 유효하면 폼 제출 가능
    if (
      nameInput.classList.contains("is-valid") &&
      emailInput.classList.contains("is-valid") &&
      phoneInput.classList.contains("is-valid")
    ) {
      isValidated = true;
      console.log("Validated true");
    } else {
      isValidated = false;
      console.log("Validated false");
    }
  });
})();
