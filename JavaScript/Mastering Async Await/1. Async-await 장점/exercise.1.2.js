import fetch from 'node-fetch';

// This makes every 2nd `fetch()` fail
let calls = 0;
const _fetch = function (url) {
  const err = new Error('Hard-coded fetch() error');
  return ++calls % 2 === 0 ? Promise.reject(err) : fetch(url);
};

async function getWithRetry(url, numRetries) {
  let lastError = null;
  for (let i = 0; i < numRetries; i++) {
    try {
      const res = await _fetch(url);
      const json = await res.json();
      return json;
    } catch (error) {
      lastError = error;
    }
  }
  throw lastError;
}

async function run() {
  const root =
    'https://' + 'us-central1-mastering-async-await.cloudfunctions.net';
  const posts = await getWithRetry(`${root}/posts`, 3);
  for (const p of posts) {
    console.log(`Fetch post ${p.id}`);
    const content = await getWithRetry(`${root}/post?id=${p.id}`, 3);
    if (content.content.includes('async/await hell')) {
      console.log(`Correct answer: ${p.id}`);
      break;
    }
  }
}

run().catch((error) => console.error(error.stack));
