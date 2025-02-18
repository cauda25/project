function payment(paymentInfo) {
  return new Promise((resolve, reject) => {
    const IMP = window.IMP; // IMP 객체 초기화
    IMP.init("imp56626883"); // 결제 초기화

    // 결제 요청
    IMP.request_pay(paymentInfo, function (rsp) {
      if (rsp.success) {
        resolve(rsp); // 결제 성공 시 rsp 반환
      } else {
        reject(rsp.error_msg); // 결제 실패 시 error 메시지 반환
      }
    });
  });
}
