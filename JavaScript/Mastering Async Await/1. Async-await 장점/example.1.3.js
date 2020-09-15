// example 1.3
// async로 표시한 함수의 내부에서만 await를 사용할 수 있다.

function test() {   
  const p = new Promise(resolve => setTimeout(resolve, 1000));   
  
  // SyntaxError: Unexpected identifier
  await p; 
} 

test();