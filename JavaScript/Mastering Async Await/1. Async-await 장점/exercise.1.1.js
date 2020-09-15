import fetch from 'node-fetch';

const root =
  'https://' + 'us-central1-mastering-async-await.cloudfunctions.net';

async function run() {
  const res = await fetch(`${root}/posts`);
  const posts = await res.json();
  for (const { id } of posts) {
    const res = await fetch(`${root}/post?id=${id}`);
    const { content } = await res.json();
    if (content.includes('async/await hell')) {
      console.log(id);
    }
  }
}

run().catch((error) => console.error(error.stack));
