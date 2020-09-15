// example 1.20
// async/await를 사용하면 callback 기반 코드보다 더 쉬운 코드로 문제를 해결할 수 있다.

async function getWithRetry(url, numRetries) {
  let lastError = null;
  for (let i = 0; i < numRetries; ++i) {
    try {
      // await superagent.get(url).body이 동작하지 않는다고 생각합시다.
      const res = await superagent.get(url);

      // 정상 작동하면 바로 반환합니다.
      return res.body;
    } catch (error) {
      lastError = error;
    }
  }
  throw lastError;
}
