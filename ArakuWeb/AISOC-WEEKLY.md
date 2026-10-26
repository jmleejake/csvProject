# AISOC 주간보고
### 10/19(월) ~ 10/23(금)
- BE
```bash
월요일 (10/19) — 핵심 구현 콘텐츠 반영
- DashboardMapper.xml, HealthMapper.xml/LogMapper.xml/UserMapper.xml 작성
- pom.xml에 lombok/commons-codec/commons-csv/poi-ooxml/p6spy 의존성 추가
- schema.sql에 table_0001~`table_0006` 전체 스키마 반영
- 더 이상 쓰지 않는 레거시 9개 파일(ExternalTodo*, SampleItem*, ItemController 등) 삭제,
schema.sql에서 external_todo 테이블 제거
- 프로젝트 상위 폴더의 불필요한 aisoc/aisoc-fe/관련 zip 삭제 (나머지는 참고자료라 보존)
- README.md 신규 작성
- application.yml 기본 프로필 dev→local, mybatis.type-aliases-package 오류 수정
- application-local.yml 신규 추가, application-dev.yml에서 spring.sql.init.mode: always 제거
- README를 위 변경사항에 맞춰 동기화

화요일 (10/20)
- "빌드랑 컴파일이 정상적으로 되는지 확인해줘" → 샌드박스에 JDK17/Maven 포터블 설치해서 mvn compile 실행, BUILD SUCCESS 확인
(의심했던 lombok 1.18.46도 실제 존재해서 문제없음)
- Entra ID 연동 관련 질문: 연동 시 필요한 것, 표준 방식(OIDC 리다이렉트 vs ROPC),
로그인 폼에서 로컬 테이블 미존재 시 Entra ID 연동하는 아이디어 검토
→ 아직 가이드 문서가 없어서 구현은 보류하기로 결정 (스키마의 entra_object_id/entra_upn/confirm_yn은 이미 준비돼 있음)

수요일 (10/21) — EC2 배포 관련 Q&A
- GitHub에 올라간 소스를 EC2에서 기동하는 절차 안내 (Java/Maven 설치, clone, build, DB 스키마 적용,
프로필/Jasypt 환경변수, 실행, 보안그룹)
- 잘못 clone한 폴더 삭제 방법(rm -rf) 안내
- mvn: command not found 해결 (OS별 Maven 설치 명령 안내)

목요일 (10/22) — 코드/설정 수정
- DashboardController.getActivityLogList() → getUserWeeklyAccessUrlList() 메소드명 변경
- application.yml 변경사항 반영:
jasypt.encryptor.password 기본값 제거(환경변수 필수화),
server.port: 8080 블록 제거,
README 동기화

금요일 (10/23) — 오늘
- 이번 주 질문 전체 정리 요청 (지금 이 답변)
```
- FE
```bash
월요일 (10/19)
- /users 액션 컬럼에 비밀번호 초기화 기능 추가: 버튼 줄바꿈 노출, POST /api/user/pass/change(changeType: init) 호출,
초기화된 사용자가 로그인 시 tempPassYn=Y면 비밀번호 변경 모달 팝업 처리
- 대시보드 연결상태 체크 호출 간격을 30초 → 5분으로 변경
- /users 목록에 /logs와 동일한 페이징 적용
- 로그인 returnUrl 적용 (미인증 상태로 특정 경로 접근 시 /login?returnUrl=...로 리다이렉트 후 로그인 완료 시 원래 경로로 복귀)
- /users에 백엔드 검색 결과 노출, 생성일자/마지막로그인일자/승인일자 선택 + From/To 기간검색 추가,
초기화/검색 버튼 추가, CSV/JSON/XLSX 다운로드 버튼 추가(백엔드 /api/user/list/export 기준)

화요일 (10/20)
- /users 기간검색 라벨을 From/To와 같은 레벨에 추가 (JSX 가이드 제공)
- /users From/To 기본값을 "1주일 전 ~ 오늘"로 설정
- /users From 시간은 00:00, To 시간은 23:59로 기본값 변경
- handleResetPassword 성공 토스트 메시지를 "비밀번호를 1234로 초기화했습니다."로 변경
- VSCode에서 실행하는 방법 문의

수요일 (10/21)
- README 패키지 구조 섹션 추가 요청
- EC2에 GitHub에서 받아 배포/실행하는 방법 문의
- 그 실행 프로세스를 pm2 vs nohup으로 유지시키는 방법 후속 질문

목요일 (10/22)
- README 최상단 제목을 # AISOC_WEB_FE로 변경 + 세션 만료 시 로그인 화면에 토스트 메시지 노출 요청 (후속 README 반영까지)
- MCP 서버 / Hunting Agent / Investigation Agent 연동 아키텍처에 대한 자문 질문 (aisoc-web 작업 중 질문됨)

금요일 (10/23, 오늘)
- aisoc-web 관련 신규 요청은 없었고, 이후 aisoc-no-okta(백엔드)로 작업 초점이 넘어가
RestUtil.java 생성, README 업데이트, 제목 변경(# AISOC_WEB_BE) 작업이 진행됨

요청 대부분이 /users 페이지(검색/기간검색/페이징/비밀번호 초기화) 개선과,
완료 후 README 갱신 + aisoc-web.zip 반영이라는 동일한 패턴으로 이어졌습니다.
```

### 10/1(목) ~ 10/16(금)
- BE
```bash
 aisoc-no-okta.zip 파일 자체는 10월 1일 15:25에 디렉토리에 생성
```

- FE
```bash
10/1
- Next.js로 db-check-app 프로젝트 최초 생성(스캐폴딩) 및 zip 빌드
- 502 Bad Gateway 에러 디버깅
- route.ts에 /api/items 호출 추가 + page.tsx에서 목록 노출
- /api/regist로 데이터 등록 기능 + 등록 버튼 + 등록된 데이터 테이블 노출
- GET/POST route 파일을 하나로 통합(향후 다른 regist 추가 대비)

10/2
- 유저 테이블(web_user) DB 스키마 공유, zip 내용 분석 요청
- 로그인 연동 중 "userId를 서버에서 못받는" 문제 디버깅 질문 (이때는 미해결)

10/6
- (별도로 aisoc-no-okta.zip 분석 요청 — 백엔드 프로젝트 쪽 질문이라 aisoc-web 히스토리와는 무관)

10/7
- 스프링부트(localhost:8081) 세션 기반 인터셉터 권한 체크 구조로 바꿀 때 React 쪽 변경사항 문의 + 로그인 화면 연결 요청
- UserService/UserController/UserVo 코드 공유하며 로그인 세션 디버깅
(JSESSIONID는 생기는데 값이 null → 세션 setAttribute 확인 → 쿼리 로그 → 최종 로그인 응답 JSON 확인까지 단계별 디버깅)
- 결과를 db-check-app.zip에 반영

10/8, 10/12 (백엔드 디버깅 병행)
- ActivityLogInterceptor 생성자 오류,
권한-메뉴 매핑 조회 쿼리 등 백엔드(aisoc) 쪽 디버깅 질문들 (아이소크웹과 연동되는 백엔드 작업)

10/12
- 권한관리: 최초 접근 시 권한목록 첫 항목 자동 클릭 + 미매핑/매핑 목록 10개 초과 시 스크롤 처리
- 전체 페이지 Tailwind 적용 요청

10/13
- 사용자 화면 검색필터 추가(통합검색: 아이디/이름/이메일/entra id, 승인여부, 활성여부)
+ 권한관리 검색필터(메뉴명) + 메뉴/API 리스트에서 permissionCode 제거
- 권한관리 매핑/미매핑 다중선택 일괄 적용 기능
- 활동로그 상세모달: session id/userid/front url/request url/header/param/body/ip/status/생성일 노출 + JSON 필드 예쁘게 파싱
- 날짜필터 From/To + 시간 선택, 기본값 당일 0~23시, 30개씩 페이징 + 10/30/50/100 선택
- 페이징 최초 조회 시 0번 페이지로 떨어져 offset이 음수가 되는 버그 수정
- (병행) MyBatis 날짜 파라미터 처리, Postgres 페이징 쿼리 구현 등 백엔드 질문

10/14
- 활동로그조회: To 기본값 당일 23:59, 총 건수 bold+색상 강조, 페이징을 «‹ 1~10 ›» 형태로 변경
- 활동로그 CSV/JSON/XLSX 다운로드 기능 추가 + 백엔드에서 뭘 해야 하는지 문의
- 다운로드 파일명이 전부 download.json으로 저장되는 문제, Content-Disposition 처리 수정
- (병행) Spring Boot CSV/JSON export 구현, MyBatis Cursor already closed 에러 디버깅

10/15
- /api/items 목록 영역에도 CSV/JSON/XLSX export 기능 추가 요청 (동일 요청 재시도 포함)

10/16
- 메인화면 개편: "Backend Sample" 문구 삭제, API base URL 영역 삭제, 카드보드 형태로 전환
(① API/DB 연결상태 신호등 — /api/health, /api/db-check,
② 로그인 사용자 주간 URL 호출 막대그래프 — /api/dashboard/user/week, apexchart 사용), 메뉴명 "메인"→"대시보드"로 변경
- 로그아웃 버튼 왼쪽에 비밀번호 변경 기능 추가(/api/user/pass/change, currentPass/changePass 파라미터)
- /api/items CSV/JSON/XLSX export 기능 재요청 (중간에 "잠깐만" 중단 후 재진행)
```
