# AISOC 주간보고

### 11/9(월) ~ 11/13(금)
```bash
11/9(월)

- aisoc-web 주석 한글화: server.mjs, app/api/[...path]/route.ts, proxy.ts, lib/api.ts, lib/types.ts, app/login/page.tsx 등
영문 주석을 전부 한글로 번역 (로직 변경 없음).
- 미해결 이슈 확인: "회사계정으로 로그인" 클릭 시 ERR_CONNECTION_TIMED_OUT 발생 건 — 원인 미확정 상태로 사용자 회신 대기 중이었음.

11/10(화)

- UserMapper.xml의 updateUser 쿼리 수정 (confirm/passChange/init/tempPassChange 타입별 version 증가 로직 추가 등).
- UserService.createEntraLoginSession: Entra 최초 로그인 시 신규 유저 등록과 동시에 VIEWER 권한 매핑도 함께 insert하도록 보강.
- Entra(SSO) 계정의 일반 ID/PW 로그인 차단: SSO로 생성된 계정은 password_hash가 없어 ID/PW 폼으로는 로그인 불가
— 이를 버그가 아닌 설계상 동작으로 확인하고, 전용 안내 메시지("회사 계정으로 로그인 버튼 이용 요청")를 던지도록 수정.
프론트 수정 없이 기존 에러 메시지 렌더링 로직으로 처리됨.

11/11(수)

- SSO 사용자에게 "비밀번호 변경" 버튼 숨김: UserSession에 sso 필드 추가
→ Entra 로그인 시 true로 세팅
→ 프론트 AuthInfo.sso로 전달
→ (authed)/layout.tsx 헤더에서 조건부 렌더링.
- .env.production.local의 API Base URL을 운영 퍼블릭 IP(13.209.123.164:3000)로 수정.
- Hunting/MCP 헬스체크 실연동: 기존 더미 응답(/api/ht/health, /api/mcp/health)을
실제 백엔드 상태 체크로 교체(TestService 메서드 추가, YAML 헬스 URL 오타 2건 수정 포함).

11/12(목)

- EC2(systemd) 환경에서 Entra ID SSO 테스트 중 DEPTH_ZERO_SELF_SIGNED_CERT 에러 최초 보고.
이 시점엔 트레이스 로그 없이 일반적 원인 후보만 제시하고 로그 공유 요청.

11/13(금) — 가장 작업량 많은 날

- DEPTH_ZERO_SELF_SIGNED_CERT 근본 원인 해결: Next.js rewrite 프록시(httpxy)가
secure 옵션 미지원으로 rejectUnauthorized를 강제 true 처리하는 게 원인
→ 효과 없던 NODE_TLS_REJECT_UNAUTHORIZED=0 제거, 대신 NODE_EXTRA_CA_CERTS로 백엔드 self-signed cert를 신뢰 CA로 등록.
- 2차: SAN 누락 에러 → cert에 subjectAltName 없어서 발생, IP SAN 추가하여 재발급 안내.
- 3차: ECONNREFUSED → keystore alias 불일치
→ key-alias: aisoc 설정값과 keystore export 시 -name 값 불일치가 원인, alias 맞춰서 재생성.
- Entra Redirect URI 확인: 실제 등록해야 할 콜백 경로는 /oauth2/authorization/entra가 아니라 /login/oauth2/code/entra임을 확인.
- aisoc-web README.md 개발 내용 반영: TLS 설명 최신화,
커스텀 대시보드/MonitoringItemPicker 등 미문서화 기능 추가,
start:https 포트 설명 오류 수정.
- aisoc-no-okta README.md 개발 내용 반영 + schema.sql 파일 분리: README를 실제 코드와 전면 대조(설정 YAML 누락 항목 추가 등),
이후 사용자 요청으로 schema.sql을 schema-web.sql(table_0001~0008) / schema-agent.sql(table_0010 이상)
두 물리 파일로 분리, README·DB_DESC.md도 함께 갱신.
- 로그인 포트 증상 진단(수정 보류): ID/PW 로그인이 백엔드로 안 가는 것처럼 보이는 증상 — 프록시 구조상 정상 동작이며,
.env.local의 자기참조 포트(8081)가 실제 원인 후보로 추정됨. 사용자가 직접 원인을 찾아 추가 수정은 하지 않음.

미해결/후속 확인 필요 (11/13 기준)

- Entra 앱 등록에 올바른 Redirect URI 반영 후 실제 로그인 테스트 결과 미확인.
- .env.production.local의 퍼블릭 IP와 트레이스 로그상 프라이빗 IP 차이는 완전히 규명되지 않음(현재 설정과는 일치 확인됨).
- aisoc-web 로그인 포트 증상 — .env.local 값/백엔드 실행 프로필 조정 필요 시 추가 확인 필요.
```

### 11/2(월) ~ 11/6(금)
```bach
11/2(월) — Entra ID OIDC 로그인 통합
- Entra ID(Azure AD) OIDC 로그인 통합 작업 계속 진행 (계획 승인 후 구현)
- "오늘 작업후에 달라진 package구조나 내용들 aisoc-web / aisoc-no-okta의 readme.md에 반영해줘"

11/3(화) — 테스트 중 서버 운영 질문 + 새 기능 요청
- Entra ID 로그인 테스트 중: nohup으로 띄운 서비스의 로그 보는 법, PID로 프로세스 종료하는 법 질문
- "모니터링 대시보드 - 표시 항목 선택" 기능 요청 (메인 대시보드 표시 항목 체크박스 선택) → 계획 승인, 구현

11/4(수) — 커스텀 대시보드 데이터소스 확장
- "커스텀 대시보드 > 위젯추가 > 데이터소스 및 그룹기준에
지난주에 신규 추가된 investigation이랑 hunting에 대해서도 추가해주면 좋을것같아"
→ 계획 승인,
- 백엔드(CdashSourceRegistry/CdashDataMapper/CdashDataService)
- 프론트(catalog.ts/types.ts) 양쪽 구현

11/5(목) — 배포 전환, UI 수정, 방화벽/아키텍처 Q&A
- "백엔드랑 프론트엔드를 https로 리눅스 환경에서 백그라운드 실행하려면 어떻게 해야돼?
nohup 했더니 잘 안되는것 같아서 그거말고 다른 방법 찾아줘"
→ systemd 서비스 전환 (env 파일, 실제 경로/keystore 경로, npm 경로 확인 등 후속 질문 포함, 최종 정상 기동 확인)
- "HuntRequestForm.tsx 조회기간이 최대 30일 제한인데 캘린더에서 30일 → min/max 적용 완료
- "리눅스에서 공인 IP 확인하는법"
- 방화벽 신청 관련: 사설 IP 192.168.1.10, 백엔드 3000포트 기준으로
 Entra ID용 방화벽(아웃바운드)을 어떻게 신청하면 되는지
"302 리다이렉트 응답받는게 백엔드 springboot인데 3000포트가 인바운"
/ "사용자가 백엔드를 브라우저를 통해 바라본다는게 이해가 안된다"
- OIDC 리다이렉트 흐름(브라우저 직접 접근 이유) 설명
- Entra ID + 로컬 테이블(table_0001) SSO 병합 설계 제안 후
"이미 login.microsoft 페이지에 접근하면 어떻게 처리해야 하는지" 질문 → 기존
  핸들러(createEntraLoginSession)로 이미 자연스럽게 처리됨을 설명

11/6(금, 오늘) — 실제 접속 오류 "회사계정으로 로그인 클릭 시
 .../oauth2/authorization/entra로 이동ss-origin, 응답 시간 초과 에러
- cross domain 처리가 필요한지"
→ CORS 무관, 실제로는 타임아웃(포트 불일치 또는 인바운드 방화벽 누락 가능

# 백엔드가 실제 어느 포트에서 listen 중인지
sudo ss -tlnp | grep java
# 현재 NEXT_PUBLIC_API_BASE_URL 값 (프론트 .env 또는 systemd 환경변수 확인)
grep -r NEXT_PUBLIC_API_BASE_URL /home/ec2-user/aisoc-web-fe/.env* 2>/dev/null

- (지금) 11/2~11/6 질문 정리 요청
```

### 10/26(월) ~ 10/30(금)
```bash
10/26 (월) - 에이전트 관련 내용 web적용 준비
DDL 전달받아 내용체크

10/27 (화) — 커스텀 대시보드 + 모니터링 대시보드 신설
agent-web.html 전달받아 해당 내용을 aisoc-web으로 이식하는것으로 작업 진행!

- 프런트: app/(authed)/custom-dashboard/ 신규 — 대시보드 목록/상세 페이지
, 위젯 빌더 모달(WidgetBuilderModal), 위젯 카드/피커
, 위젯 타입별 렌더러(KPI/Note/List/Metric/Links) 및 편집기(editors/*)
, useCdashQuery 훅.
- 백엔드: CdashController/CdashDataController
, CdashMapper/CdashDataMapper(+XML), Cdash*Service
, CdashScreenRegistry/CdashSourceRegistry/CdashSpecValidator/CdashException 등 커스텀 대시보드 전용 패키지 신설.
- 모니터링 대시보드 DTO 다수 신설(DashboardMonitoringSummaryResDto, DashboardMonitoringRecentResDto
, DashboardHuntingStatusDto, DashboardInvestigationStatusDto, DashboardLlmUsageDto 등)
- 현황/추이/상세 화면 기반 작업.
- schema.sql 변경(백업 파일 schema.sql.bak.20261027162058 생성
- table_0007/table_0008 커스텀 대시보드 테이블 추가 추정).

10/28 (수) — 검토 대기(Review) 화면 + 조사(Investigation) 목록 백엔드

- 프런트: review/page.tsx + FindingDetailModal(검토대기 Finding 상세/승인·반려)
, HuntingDetailModal(모니터링 대시보드용 Hunting 상세)
, Sidebar.tsx 메뉴 갱신.
- 백엔드: DashboardController(검토대기 API)
, InvestigationController/InvestigationMapper(조사 목록)
, FindingReviewReqDto/DashboardReviewFinding*Dto/DashboardReviewInvestigationDto
/DashboardHypothesisDto/DashboardCaseEvidenceDto 등 DTO 다수
, MenuResDto, WebConfig/AppConst, UserService/UserMapper 수정.
- application-{local,dev,prod}.yml 프로필 설정 변경.

10/29 (목) — MCP Tools / 조사전략 / Hunting 요청 등록

- 프런트: tools/page.tsx + McpToolDetailModal/ToggleSwitch/mcpToolActive.ts(MCP Tool 관리)
, strategies/(조사전략 관리 메뉴 신설) StrategyForm/BasicToolsEditor/FollowupToolsEditor/strategyActive.ts
, hunting/HuntRequestForm.tsx(Hunting 요청 등록), InvestigationDetailModal
, 공용 confirmToast.tsx/lib/types.ts 갱신.
- 백엔드: McpToolController/McpToolService/McpToolMapper
, InvestigationStrategyController/Service/Mapper(신규), HuntingController/Service/Mapper
, 관련 DTO 전부(Strategy*/McpTool*/Hunting*) 신설. 거의 모든 MyBatis Mapper XML 동시 수정.
- schema.sql 추가 변경(MCP Tool table_5001, 조사전략 table_1004, M008/M009 메뉴·권한 seed 등).

10/30 (금, 오늘) — 마무리 및 문서화

- strategies/StrategyDetailModal.tsx 추가
, strategies/page.tsx/investigation/page.tsx/logs/page.tsx/app/(authed)/page.tsx 소소한 수정.
- aisoc-web/README.md, aisoc-no-okta/README.md 문서 정리 — 패키지구조/주요 API/데이터베이스 섹션 최신화
, schema.sql → schema-web.sql(table_0001~0008) / schema-agent.sql(table_0010+) 분리 반영
, M001(커스텀 대시보드)·M000A005~008(모니터링 대시보드 API) 권한 코드 문서화.

한 줄 요약: 화~목 3일간 커스텀 대시보드 → 검토대기/조사 목록 → MCP Tools/조사전략/Hunting 요청까지
신규 화면 5~6개를 백엔드·프런트 풀스택으로 순차 구축했고, 금요일은 마무리 수정과 README 문서화로 마감했습니다.
```

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
- "빌드랑 컴파일이 정상적으로 되는지 확인해줘"
 → 샌드박스에 JDK17/Maven 포터블 설치해서 mvn compile 실행, BUILD SUCCESS 확인
(의심했던 lombok 1.18.46도 실제 존재해서 문제없음)
- Entra ID 연동 관련 질문: 연동 시 필요한 것, 표준 방식(OIDC 리다이렉트 vs ROPC),
로그인 폼에서 로컬 테이블 미존재 시 Entra ID 연동하는 아이디어 검토
→ 아직 가이드 문서가 없어서 구현은 보류하기로 결정
 (스키마의 entra_object_id/entra_upn/confirm_yn은 이미 준비돼 있음)

수요일 (10/21) — EC2 배포 관련 Q&A
- GitHub에 올라간 소스를 EC2에서 기동하는 절차 안내 (Java/Maven 설치, clone, build, DB 스키마 적용,
프로필/Jasypt 환경변수, 실행, 보안그룹)
- 잘못 clone한 폴더 삭제 방법(rm -rf) 안내
- mvn: command not found 해결 (OS별 Maven 설치 명령 안내)

목요일 (10/22) — 코드/설정 수정
- DashboardController.getActivityLogList()
 → getUserWeeklyAccessUrlList() 메소드명 변경
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
- 로그인 returnUrl 적용 (미인증 상태로 특정 경로 접근 시
 /login?returnUrl=...로 리다이렉트 후 로그인 완료 시 원래 경로로 복귀)
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
- (별도로 aisoc-no-okta.zip 분석 요청 - 백엔드 프로젝트 쪽 질문이라 aisoc-web 히스토리와는 무관)

10/7
- 스프링부트(localhost:8081) 세션 기반 인터셉터 권한 체크 구조로 바꿀 때 React 쪽 변경사항 문의 + 로그인 화면 연결 요청
- UserService/UserController/UserVo 코드 공유하며 로그인 세션 디버깅
(JSESSIONID는 생기는데 값이 null
 → 세션 setAttribute 확인
 → 쿼리 로그 → 최종 로그인 응답 JSON 확인까지 단계별 디버깅)
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
- 활동로그 상세모달: session id/userid/front url/request url/header/param/body/ip/status/생성일 노출
 + JSON 필드 예쁘게 파싱
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
② 로그인 사용자 주간 URL 호출 막대그래프
- /api/dashboard/user/week, apexchart 사용), 메뉴명 "메인"→"대시보드"로 변경
- 로그아웃 버튼 왼쪽에 비밀번호 변경 기능 추가(/api/user/pass/change, currentPass/changePass 파라미터)
- /api/items CSV/JSON/XLSX export 기능 재요청 (중간에 "잠깐만" 중단 후 재진행)
```
