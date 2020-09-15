// example 1.19
// callback 기반의 실패한 요청 재시도 처리

function getWithRetry(url, numRetries, callback, count) {
  count = count || 0;
  superagent.get(url).end(function (error, res) {
    if (error) {
      if (count >= numRetries) {
        return callback(error);
      }
      return getWithRetry(url, numRetries, callback, count + 1);
    }
    return callback(null, res.body);
  });
}
