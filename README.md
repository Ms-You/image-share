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