# 08장 블로그 웹 앱 : 콜백 함수, 웹뷰, 네이티브 설정

## 권한 및 네이티브 설정

웹뷰 사용을 위해 몇 가지 네이티브 설정이 필요하다.

- Android 환경을 위한 권한 설정은 `android/app/src/main/AndroidManifest.xml` 파일에서 설정한다.
  - 플러그인 사용시 해당 플러그인의 문서를 참고하여 compileSdkVersion, minSdkVersion 설정을 관리해야 한다.
- iOS 환경을 위한 권한 설정은 `ios/Runner/Info.plist` 파일에서 설정한다.

## 기록

- [pub.dev](https://pub.dev/): The official package repository for Dart and Flutter apps.
- [pub command-line](https://dart.dev/tools/pub/cmd)
- [The pubspec file](https://dart.dev/tools/pub/pubspec)
