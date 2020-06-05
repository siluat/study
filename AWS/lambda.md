# AWS Lambda

## Lambda Invoke

> Lambda 코드상에서 다른 Lambda 호출하기

invoke API를 사용하면 Lambda function에서 다른 lambda function을 직접 호출할 수 있다.
API는 각 언어별 SDK에 포함되있다.

### Request parameters

- FunctionName : 함수 이름
- InvocationType : 요청 타입. 3가지 사용 가능
  - 'RequestResponse' : 기본 설정. 대상 Lambda function을 동기적으로 실행
  - 'Event' : 대상 Lambda function을 비동기적으로 실행
  - 'DryRun' : 호출 권한과 입력에 대한 검증만 하고 실제 실행하지는 않는다.
- LogType
  - 'None' : 미설정
  - 'Tail' : 'RequestResponse'로 실행할 때 사용할 수 있다. 실행 완료 후 응답에 로그의 뒷 부분을 일부 포함해서 반환한다.
- ClientContext : 클라이언트 정보를 전달한다.
- Payload : JSON Format의 문자열로 데이터를 전달한다. 실행되는 Lambda function의 event로 전달된다.
- Qualifier : 실행하려는 Lambda function의 version이나 alias를 지정한다.

### Reference

- [API Guide의 소개](http://docs.aws.amazon.com/ko_kr/lambda/latest/dg/API_Invoke.html)
- [boto3의 invoke API](http://docs.aws.amazon.com/ko_kr/lambda/latest/dg/API_Invoke.html)
