const fetch = require('node-fetch');

const root =
  'https://' + 'us-central1-mastering-async-await.cloudfunctions.net';

async function run() {
  // Example of using `fetch()` API
  const res = await fetch(`${root}/posts`);
  console.log(await res.json());
}

run().catch((error) => console.error(error.stack));
