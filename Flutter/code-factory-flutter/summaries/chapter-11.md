# 10장 디지털 주사위 : 가속도계, 자이로스코프, Sensor_Plus

## 가속도계

가속도계는 3개의 축(x, y, z)으로 가속도를 측정할 수 있다.

핸드폰을 정면으로 봤을 때 기준,

- x축: 좌우
- y축: 위아래
- z축: 앞뒤

## 자이로스코프

x, y, z축의 회전을 측정

## sensor_plus 패키지

sensor_plus 패키지를 사용해서 가속도계와 자이로스코프 센서의 이벤트를 감지할 수 있다. 이 예제에서는 더 간단하게 핸드폰 흔들기를 감지하기 위해 shake라는 패키지를 사용한다.

## TickerProviderMixin과 vsync

TabContoller에서 vsync를 사용하기 위해 TickerProviderMixin를 사용한다.

TickerProviderMixin는 애니메이션 효율을 담당한다. 한 틱마다 애니메이션을 실행해줌으로써 애니메이션 사용시 비효율적인 렌더링이 실행되는 것을 막아준다.
