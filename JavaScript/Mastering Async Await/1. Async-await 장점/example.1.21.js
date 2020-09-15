// example 1.21
// async/await를 사용하면 비동기 연산들을 순차적으로 처리하는 것들이 사소해 보인다.

import superagent from 'superagent';

async function getWithRetry(url, numRetries) {
  let lastError = null;
  for (let i = 0; i < numRetries; ++i) {
    try {
      const res = await superagent.get(url);
      return res.body;
    } catch (error) {
      lastError = error;
    }
  }
  throw lastError;
}

async function run() {
  const root = 'https://jsonplaceholder.typicode.com';
  const posts = await getWithRetry(`${root}/posts`, 3);
  for (const { id } of posts) {
    const comments = await getWithRetry(`${root}/comments?postId=${id}`, 3);
    console.log(comments);
  }
}

run();
