// 서버나 클라이언트 측에서 인증 여부를 확인
async function checkAuth() {
  try {
    const response = await fetch("/rest/check-auth", { method: "GET" });
    if (!response.ok) {
      throw new Error("Not authenticated");
    }
    const data = await response.json();
    console.log(data);
    return data; // true 또는 false 반환
  } catch (error) {
    console.error(error);
    return false; // 인증 실패 시 false 반환
  }
}
