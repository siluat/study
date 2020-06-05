# Travis CI로 AWS Lambda 코드 배포 자동화

> AWS Lambda 코드 배포는 같은 AWS 서비스를 이용해서 할 수도 있지만, 해당 기능은 유료다. 배포 자동화를 비용없이 하기위해 AWS Lambda 배포에 Travis CI를 사용해보았다.

내 손으로 직접 코드를 일일이 올릴 순 없다. 최초의 목적을 잊은 채 자동 배포 환경부터 준비했다.
(자동화에 대한 집착)

## 구조

Github에 코드를 커밋하면 Travis CI가 AWS Lambda에 배포하도록 구성한다.  
커밋만 직접하고 나머지 점선으로 된 액션들은 모두 자동이다.

![basic concept](10.png)

## AWS Lambda Function 준비

먼저 Travis CI로 배포할 AWS Lambda function을 준비했다. 코드는 Travis CI로 배포할 것이므로 그대로 두었다.

![create lambda function](20.png)

## AWS IAM User 준비

다음으로 Travis CI에서 AWS Lambda에 접근할 때 사용할 User를 준비해야 한다.
AWS IAM User 관리 화면으로 가서 새 User를 추가했다.

![create iam user](30.png)

User name을 정하고, Travis CI 연동에 access key와 secret access key가 필요하므로
AWS access type을 'Programmatic access'으로 선택했다.

![set user details](40.png)

다음으로 권한 설정에서 Policy를 설정한다.
[Travis CI 공식 문서](https://docs.travis-ci.com/user/deployment/lambda/#AWS-permissions)에 나온대로 설정했다.
문서에 있는 예시에서 &lt;region&gt;, &lt;account-id&gt;, &lt;name-of-function&gt;, &lt;name-of-role&gt; 등은 자신에게 해당하는 값으로 바꿔줘야 한다.

새 User를 생성하면 완료화면에서 바로 Access key와 Secret access key를 보여준다.
Travis CI 연동 때 써야 하니 두 값을 **잠시** 메모해두었다.

![set user permissions](50.png)

## Github repository와 Travis CI 연결

코드를 커밋할 코드 저장소를 Github에 생성하고 해당 저장소를 Travis CI에서 접근할 수 있도록 활성화했다.
![flick the repository switch on](70.png)

## 배포할 코드 준비

자동 배포가 잘 되는지 테스트하는 것이 목적이라 필수적인 것만 준비했다.
배포할 Python 파일 하나와 Travis 설정 파일이다. Travis CI를 통해 배포될 때 프로젝트 전체가 ZIP 파일로 압축돼서 배포된다.
간단한 예시를 위해 파일 하나만 준비했을 뿐, 코드를 모두 한 파일에 작성해야 한다는 제약은 없다.
또한, 외부 라이브러리도 사용할 수 있다. 외부 라이브러리 사용시 해당 라이브러리를 프로젝트 내에 설치해서 라이브러리 파일을 통째로 커밋해야 한다.
자세한 내용은 [AWS 공식 문서](https://docs.aws.amazon.com/lambda/latest/dg/deployment-package-v2.html)에 잘 나와 있다.

![project structure](80.png)

lambda.py 파일은 별거 없다. 그냥 문자열을 반환하는 코드를 준비했다.

```python
# lambda.py
def lambda_handler(event, context):
    return '이 코드는 Travis CI로 배포된 코드입니다.'
```

중요한 Travis 설정 파일이다. [Travis 공식 문서](https://docs.travis-ci.com/user/deployment/lambda)를 참고해서 다음과 같이 작성했다.

```yaml
# .travis.yml
notifications:
  email: false
language: python
python:
  - "3.6"
script:
  - python lambda.py
deploy:
  provider: "lambda"
  function_name: "lambdaWithTravis"
  region: "ap-northeast-2"
  role: "arn:aws:iam::876863305772:role/lambda_basic_execution"
  runtime: "python3.6"
  module_name: "lambda"
  handler_name: "lambda_handler"
  access_key_id: "AKIAIVMVHQSPMGH3KU7Q"
  secret_access_key: "AWS SECRET ACCESS KEY"
```

### module_name과 handler_name

module_name과 handler_name 설정은 중요하다.
AWS Lambda function 실행 시 module_name에 있는 파일의 lambda_handler를 실행하기 때문이다.
위와 같이 설정하면 lambda.py 파일의 lambda_handler 함수를 최초로 실행한다.

### access_key_id와 secret_access_key

Travis CI용으로 만들었던 AWS IAM User의 Access key id를 여기에 사용한다.
secret_access_key는 Github을 통해 외부로 노출되기 때문에 그대로 쓰지 말고 반드시 암호화해야 한다.
암호화는 Travis CI Command line tool을 이용해서 할 수 있다. 자세한 내용은 역시 [Travis 공식 문서](https://docs.travis-ci.com/user/deployment/lambda)에 잘 나와 있다.

```bash
$ travis encrypt "AWS SECRET ACCESS KEY" --add deploy.secret_access_key
```

## 커밋

모든 준비를 마치고 코드를 커밋하면 잠시 후 Travis CI 사이트에서 빌드 및 배포 상황을 볼 수 있다.

![travis build](90.png)

배포까지 성공한 것을 확인하고 AWS Lambda function으로 가보면 커밋한 코드가 적용돼있다.

![lambda function deployed](100.png)

드디어 원래 하려던 일을 할 준비가 되었다.

## 후일담?

**사실 이건 별로 유용하지 않았다.**  
결국, 하려던 일을 하기 위해 여러 개의 Lambda function을 만들게 되었는데, 위의 방법으론 여러 개의 Lambda function을 관리하는 데 적합하지 않다.
이런저런 삽질 끝에 Lambda function 배포 관리를 하는 [Python 툴](https://pypi.python.org/pypi/awslambda/1.0.6)을 찾긴 했는데 2.x 버전이다.
직접 써보지는 않고 문서만 봤는데 상당히 편리하게 만든 것 같다.

일단 지금 사용하는 Lambda function들은 한 번 작성하면 수정할 일이 거의 없어서 배포 시스템에 과한 투자를 할 필요는 없는 것 같아 그냥 aws-cli를 통해 배포하는 선에서 정리했다.
