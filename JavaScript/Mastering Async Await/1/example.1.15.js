// example 1.15

async function computeValue() {
  // `err` is the "rejected value"
  const err = new Error('Oops!');
  throw err;
}

async function test() {
  try {
    const res = await computeValue(); // Never runs
    console.log(res);
  } catch (error) {
    console.log(error.message); // "Oops!"
  }
}

test();
