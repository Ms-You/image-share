# image-share
Image share project for Spring Boot

### API 리스트업
**① GET**

|Index|Method|URI|Description|
|---|---|---|---|
|1|GET|/app/users|전체 유저 조회 API|
|2|GET|/app/users/userId/:userIdx|아이디로 유저 조회 API|
|3|GET|/app/users/userNickname/:userNickname|닉네임으로 유저 조회 API|
|4|GET|/app/users/userEmail/:userEmail|이메일로 유저 조회 API|
|5|GET|/app/users/userId/:userIdx/profile|아이디로 프로필 조회 API|
|6|GET|/app/feeds|전체 피드 조회API|
|7|GET|/app/feeds/feedId/:feedIdx|아이디로 피드 조회 API|
|8|GET|/app/feeds/feedName/:feedName|피드 명으로 피드 조회 API|
|9|GET|/app/feeds/tag/tagId/:tagIdx|태그 아이디로 전체 피드 조회 API|
|10|GET|/app/feeds/tag/tagName/:tagName|태그 명으로 전체 피드 조회 API|
|11|GET|/app/subs/status/:status|상태에 따른 구독 목록 조회 API|
|12|GET|/app/feeds/like|좋아요 누른 피드 조회 API|

<br/>

**② POST**

|Index|Method|URI|Description|
|---|---|---|---|
|1|POST|/app/users|회원 가입 API|
|2|POST|/app/users/findPassword|비밀번호 찾기 API|
|3|POST|/app/feeds|피드 업로드 API|
|4|POST|/app/good/feedId/:feedIdx|좋아요 클릭 API|
|5|POST|/app/sub/userId/:userIdx|구독 클릭 API|
|6|POST|/app/feeds/comments|댓글 작성 API|

<br/>

**③ PUT**

|Index|Method|URI|Description|
|---|---|---|---|
|1|PUT|/app/users/userId/:userIdx/password|비밀번호 변경 API|
|2|PUT|/app/users/userId/:userIdx/profile|프로필 수정 API|
|3|PUT|/app/feeds/feedId/:feedIdx|업로드 한 피드 수정 API|
|4|PUT|/app/feeds/feedId/:feedIdx/comments/commentIdx/:commentIdx|댓글 수정 API|

<br/>
<br/>

📢**기능**📝

① **사용자 관리**

- **회원가입**
    - 이메일, 비밀번호, 닉네임
    - 이메일, 닉네임 중복 검사
- **로그인**
    - 이메일, 비밀번호
- **비밀번호 찾기**
    - 임시 비밀번호를 메일로 전송
- **비밀번호 변경**
    - 기존 비밀번호 입력 후 새로운 비밀번호 입력 페이지 이동
- **프로필**
    - 프로필 사진, 닉네임, 소개말
    

**② 피드 관리**

- **피드를 보기 위해 원하는 태그 선택**
    - #음식 #영화 #게임 #만화 #애니메이션 등의 태그를 선택할 수 있게 설정
- **피드 보기**
    - 이미지, 제목, 설명, 태그, 구독, 댓글, 좋아요
- **피드 업로드**
    - 이미지, 제목, 설명, 태그
- **업로드 한 피드 관리**
    - 피드 수정, 삭제
- **좋아요**
    - 좋아요 수 확인
    - 좋아요 누른 이미지들을 마이 페이지에서 확인 가능
- **구독**
    - 구독 리스트를 보여줌
- **구독 관리**
    - 구독 취소 등
- **댓글**
    - 피드에 댓글 달기

**③ 기타**

- **검색 (태그 위주)**
    - 관심 분야 등을 검색할 수 있음
- **알림**
    - 업데이트, 공지 사항 등을 알림
- **메일함**
    - 받은 메일함, 메일 보내기
- **피드 신고**
    - 관리자가 신고된 피드를 확인 후 처리
- **피드에서 이미지 다운로드**
- **OAuth 로그인**
