# image-share
Image share project for Spring Boot

### API 리스트업
#### Case A - Administrator)


**① GET**

|Index|Method|URI|Description|
|---|---|---|---|
|1|GET|/admin|관리자 페이지 이동 API|
|2|GET|/admin/users|사용자 목록 조회 API|
|3|GET|/admin/user/{userId}|특정 사용자 조회 API|
|4|GET|/admin/user/search?keyword=nickName|사용자 닉네임으로 사용자 검색 API|
|5|GET|/admin/temporary/users|일시 정지된 사용자 목록 조회 API|
|6|GET|/admin/permanent/users|영구 정지된 사용자 목록 조회 API|
|7|GET|/admin/user/{userId}/feeds|특정 사용자가 생성한 피드 목록 조회 API|
|8|GET|/admin/tags|태그 목록 조회 API|
|9|GET|/admin/tag|태그 생성 페이지 이동 API|
|10|GET|/admin/modifying/tag/{tagId}|태그 수정 페이지 이동 API|
|11|GET|/admin/tag/{tagId}|태그에 따른 피드 목록 조회 API|
|12|GET|/admin/feed/{feedId}|특정 피드 조회 API|
|13|GET|/admin/feed/search?keyword=feedTitle|피드 명으로 특정 피드 검색 API|
|14|GET|/admin/report|신고 목록 조회 API|
|15|GET|/admin/report/{reportId}|특정 신고 조회 API|
|16|GET|/admin/infoList|공지 목록 조회 API|
|17|GET|/admin/info|공지 생성 페이지 이동 API|
|18|GET|/admin/info/{informationId}|특정 공지 조회 API|
|19|GET|/admin/modifying/info/{informationId}|공지 수정 페이지 이동 API|

<br/>

**② POST**

|Index|Method|URI|Description|
|---|---|---|---|
|1|POST|/admin/tag|태그 생성 API|
|2|POST|/admin/temporary/user/{userId}|사용자 계정 일시 정지 API|
|3|POST|/admin/permanent/user/{userId}|사용자 계정 영구 정지 API|
|4|POST|/admin/info|공지 작성 API|

<br/>

**③ PUT**

|Index|Method|URI|Description|
|---|---|---|---|
|1|PUT|/admin/tag/{tagId}|태그 수정 API|
|2|PUT|/admin/info/{informationId}|공지 수정 API|

<br/>

**④ DELETE**

|Index|Method|URI|Description|
|---|---|---|---|
|1|PUT|/admin/tag/{tagId}|태그 삭제 API|
|2|PUT|/admin/feed/{feedId}|피드 삭제 API|

<br/>
<br/>
<br/>

#### Case B - User)


**① GET**

|Index|Method|URI|Description|
|---|---|---|---|
|1|GET|/info/{informationId}|특정 공지 조회 API|
|2|GET|/auth/join|회원가입 페이지 이동 API|
|3|GET|/auth/login|로그인 페이지 이동 API|
|4|GET|/user/profile|프로필 페이지 이동 API|
|5|GET|/modifying/user/profile|프로필 수정 페이지 이동 API|
|6|GET|/user/{userId}|특정 사용자 조회 API|
|7|GET|/user/{userId}/feeds|특정 사용자가 생성한 피드 목록 조회 API|
|8|GET|/user/tag/{tagId}|태그에 따른 피드 목록 조회 API|
|9|GET|/user/feed|피드 생성 페이지 이동 API|
|10|GET|/user/feed/{feedId}|특정 피드 조회 API|
|11|GET|/user/subscription/feed/{feedId}|구독한 사용자들의 특정 피드 조회 API|
|12|GET|/user/modifying/feed/{feedId}|피드 수정 페이지 이동 API|
|13|GET|/user/feeds|사용자가 생성한 피드 목록 조회 API|
|14|GET|/user/toUser/{userId}/feeds|구독한 사용자의 피드 목록 조회 API|
|15|GET|/user/feed/search?keyword=feedTitle|피드 명으로 피드 검색 API|
|16|GET|/user/feed/searchType?searchType=최신순|검색 타입 별 피드 목록 조회 API|
|17|GET|/user/likes/feeds|좋아요 표시한 피드 목록 조회 API|
|18|GET|/download/feed/{feedId}|특정 피드의 이미지 다운로드 API|
|19|GET|/user/report/feed/{feedId}|특정 피드 신고 페이지 이동 API|
|20|GET|/user/subscribe|구독한 사용자 목록 조회 API|
|21|GET|/user/toUser/{userId}|구독한 특정 사용자 조회 API|

<br/>

**② POST**

|Index|Method|URI|Description|
|---|---|---|---|
|1|POST|/auth/join|회원가입 API|
|2|POST|/user/feed|피드 생성 API|
|3|POST|/user/feed/{feedId}/reply|특정 피드에 댓글 작성 API|
|4|POST|/user/likes/feed/{feedId}|특정 피드 좋아요 클릭 API|
|5|POST|/user/unLikes/feed/{feedId}|특정 피드 좋아요 취소 API|
|6|POST|/user/likes/feed/{feedId}/reply/{replyId}|특정 피드의 댓글 좋아요 클릭 API|
|7|POST|/user/unLikes/feed/{feedId}/reply/{replyId}|특정 피드의 댓글 좋아요 취소 API|
|8|POST|/user/report/feed/{feedId}|특정 피드 신고 API|
|9|POST|/user/subscribe/toUser/{toUserId}/feed/{feedId}| 특정 사용자의 피드에서 구독 클릭 API|
|10|POST|/user/unSubscribe/toUser/{toUserId}/feed/{feedId}|특정 사용자의 피드에서 구독 취소 API|

<br/>

**③ PUT**

|Index|Method|URI|Description|
|---|---|---|---|
|1|PUT|/user/profile|회원 프로필 수정 API|
|2|PUT|/user/feed/{feedId}|피드 수정 API|

<br/>

**④ DELETE**

|Index|Method|URI|Description|
|---|---|---|---|
|1|PUT|/user/feed/{feedId}|피드 삭제 API|
|2|PUT|/user/feed/reply/{replyId}|댓글 삭제 API|

<br/>
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
- **피드 좋아요**
    - 좋아요 수 확인
    - 좋아요 누른 이미지들을 마이 페이지에서 확인 가능
- **구독**
    - 구독 리스트를 보여줌
- **구독 관리**
    - 구독 취소 등
- **댓글**
    - 피드에 댓글 달기
- **댓글 좋아요**
    - 좋아요 수 확인
    
    

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
    - 카카오
    - 네이버
    - 구글
