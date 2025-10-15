# Chapter 7 버튼과 링크 처리하기

## 이론 학습

### 링크 구현

- 지금까지의 레이아웃 트리에는 문단이나 헤딩 같은 요소의 위치나 크기는 계산해서 저장하지만, 링크와 같은 서식이 있는 텍스트에 대해서는 아무 처리를 하고 있지 않다.
- 링크 기능 구현일 위해 두 가지 새로운 유형의 레이아웃 객체를 도입한다.
  - LineLayout: BlockLayout은 각 텍스트 줄마다 LineLayout을 자식으로 가진다.
  - TextLayout: LineLayout은 해당 줄의 단어마다 TextLayout을 자식으로 가진다.

### 텍스트 렌더링

> 사실 텍스트 렌더링은 이보다 훨씬 더 복잡합니다(https://gankra.github.io/blah/text-hates-you/). 문자들(https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6morx.html)은 변형되거나 중첩될 수 있고, 사용자는 특정 글자나 글자의 일부에 다른 색을 지정하고 싶을 수 있습니다. 이 모든 것이 HTML에서 가능하며 실제 브라우저는 이를 지원합니다.

### 모바일 환경에서의 링크 클릭

> 모바일에서 '클릭'은 '탭'이기 때문에 한 지점이 아닌 여러 영역에 걸쳐 발생해 부정확합니다. 그래서 클릭은 '히트 테스트'를 위해 포인트가 아닌 영역을 사용해야 합니다. 일반적인 마우스의 클릭이라도 회전이나 스케일된 엘리먼트에도 비슷한 처리가 필요합니다(https://source.chromium.org/chromium/chromium/src/+/main:third_party/blink/renderer/core/layout/hit_test_location.h).

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
- LineLayout과 TextLayout의 layout 메서드 구현
  - LineLayout의 줄들은 수직으로 쌓이고 부모늬 전체 너비를 차지하므로 x와 y, width 계산은 블록의 처리와 거의 같다.
  - LineLayout의 높이를 계산하기 위해 자식 노드들의 최대 에선트와 최대 디센트 계산이 필요하다.
  - Textlayout의 크기와 x 위치는 폰트 매트릭을 사용해서 계산할 수 있다.
  - Textlayout의 y 위치는 같은 라인의 다른 단어에 따라 달라지므로 LineLayout의 layout에서 계산할 수 있다. 기존의 flush 메서드의 계산과 동일하게 계산할 수 있다. 이 계산을 처리하면서 TextLayout의 y 위치도 계산하여 저장한다.
- LineLayout과 TextLayout의 paint 메서드 구현
  - LineLayout은 페인팅할 것이 없으므로 빈 배열을 반환한다.
  - TextLayout은 DrawText를 반환한다.
  - TextLayout 객체가 paint 메서드를 제공하므로 BlockLayout의 paint 메서드에서 텍스트 페인팅 처리를 할 필요가 없다.
- 클릭 이벤트 핸들링
  - 링크 구현을 위해 링크 요소 클릭을 감지해야 한다.
  - 클릭 이벤트의 좌표는 화면 좌표계이므로 해당 정보에서 페이지 좌표를 변환하고, 페이지 좌표로 해당 좌표의 레이아웃 객체를 탐색한다.
    - 클릭한 페인트 커맨드를 먼저 찾고, 그 커맨드에서 레이아웃 개체를 찾을 수도 있지만, 실제 브라우저에서는 클릭될 수 없는 안보이는 객체를 포함해서 여러 가지 많은 이유로 그렇게 동작하지는 않는다.
    - 클릭 이벤트 좌표에는 하나 이상의 레이아웃 객체가 있을 수 있다. 클릭은 페인팅 처리의 반대이므로 마지막 엘리먼트에서 히트 테스트를 시작한다.
      - 실제 브라우저에서는 음수 마진을 포함해서 다양한 케이스로 여러 레이아웃 객체가 있을 수 있다. 또한 z-index 속성을 사용하여 어떤 형제 노드가 위에 있는지 정리한다. 그래서 스택 컨텍스트를 게산하여 사용자가 실제로 클릭한 내용을 확인해야 한다.
  - 클릭 히트 노드가 존재한다면, HTML 트리를 거슬러 올라가 링크 엘리먼트를 찾는다. 링크 엘리먼트가 존재한다면, URL을 추출하고 로드한다.
- 탭 브라우징 구현
  - 구현을 위해 기존의 Browser 클래스의 역할을 분리하여 Tab 클래스와 Browser 클래스로 나누는 리팩터링을 진행한다.
    - Browser 클래스는 탭 관리, 캔버스 초기화, 이벤트 처리를 담당한다.
    - Tab 클래스는 기존 Browser 클래스에서 초기화 클래스와 이벤트 처리를 제외한 부분을 담당한다.
