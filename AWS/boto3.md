# Boto3
Python용 AWS SDK다.

## 참조
- [공식 문서](https://boto3.readthedocs.io/)

## 설치
```zsh
$ pip install Boto3
```

## 설정
boto3를 통해 AWS 서비스에 접근하기 위해 인증 설정을 해야한다.  
Access key와 Secret access key를 준비하고 다음 방법 중 하나로 설정할 수 있다.  

1. AWS CLI  
[AWS CLI](https://aws.amazon.com/ko/cli/)가 설치되있다면 다음 명령어를 통해 키를 등록할 수 있다.
```zsh
$ aws configure
```

2. 직접 파일 생성
```shell
# ~/.aws/credentials
aws_access_key_id = YOUR_ACCESS_KEY
aws_secret_access_key = YOUR_SECRET_KEY
```
```shell
# ~/.aws/config
region=ap-northeast-2
```
