# Chapter 7 버튼과 링크 처리하기

## 이론 학습

### 링크 구현

- 지금까지의 레이아웃 트리에는 문단이나 헤딩 같은 요소의 위치나 크기는 계산해서 저장하지만, 링크와 같은 서식이 있는 텍스트에 대해서는 아무 처리를 하고 있지 않다.
- 링크 기능 구현일 위해 두 가지 새로운 유형의 레이아웃 객체를 도입한다.
  - LineLayout: BlockLayout은 각 텍스트 줄마다 LineLayout을 자식으로 가진다.
  - TextLayout: LineLayout은 해당 줄의 단어마다 TextLayout을 자식으로 가진다.

## 실습

- LineLayout과 TextLayout 클래스 정의
  - LineLayout은 노드, 부모, 이전 형제, 자식 노드를 저장한다.
  - TextLayout은 노드, 단어, 부모, 이전 형제, 자식 노드를 저장한다.
- 기존 구현을 LineLayout과 TextLayout을 사용하도록 변경
  - LineLayout과 TextLayout의 생성 시점은 줄바꿈이 일어나는 시점이다.
  - LineLayout과 TextLayout은 자신만의 layout과 paint 메서드 제공하도록 구현할 것이므로 BlockLayout의 word 메서드는 flush 메서드를 사용해서 그리기에 필요한 위치 계산이나 그리기에 필요한 정보를 처리할 필요가 없다. word 메서드는 TextLayout 생성
  하고 줄바꿈 상황에 new_line 메서드를 사용해서 LineLayout 생성한다.
  - 라인의 높이를 참조할 수 있게 되었으므로 layout 메서드에서 레이아웃 모드에 상관없이 높이를 계산하는 코드로 변경할 수 있다.
  - 필요없어진 display_list, cursor_y, line 필드를 제거한다.

