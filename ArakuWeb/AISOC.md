# AISOC

## 켄싱턴락
락 008

## 백엔드 클로드 요청
* aisoc-no-okta에 수정요청사항이 있어
1 DashboardController.java > getActivityLogList의 메소드명을 getUserWeeklyAccessUrlList로 변경해줘

* 수정작업이 완료되면 
README파일에 반영할 내용이 있다면 업데이트 해줘

## 백엔드 JASYPT 마스터키
유플파트너

## 프론트엔드 클로드 요청
* aisoc-web에 수정요청사항이 있어
1 readme.md 최상단의 제목을 AISOC_WEB_FE 로 변경해줘
2 세션이 만료 혹은 소실되서 로그인화면으로 리다이렉트 되는 경우
"세션이 만료되어 로그인 화면으로 이동합니다." 라는 toast를 노출하고싶어

* 수정작업이 완료되면 
README파일에 반영할 내용이 있다면 업데이트 해줘

## GIT
- https://github.com/lgu-ciso/AISOC_WEB_BE.git
- https://github.com/lgu-ciso/AISOC_WEB_FE.git

jake

jmlee5@lgupluspartners.co.kr

## entraid 연동
* entraid 연동방식 구상

내가 생각한 방식은 로그인화면에서 로그인 시도했을때 table_0001에 있으면 그대로 태우고 
없다면 entraid를 연동하는 로직을 태워서 정보를 얻어서 table_000l에 insert하고 
추후에 시스템관리자가 해당 사용자의 시스템 접근을 confirm(table_0001)하는 방식이야 

- 클로드 답변

표준 방식: Microsoft가 공식 권장하는 유일한 방식은 OAuth2/OIDC Authorization Code Flow(리다이렉트) 입니다. 
ID/PW를 그대로 넘기는 ROPC는 Microsoft 문서에서도 "레거시용, 신규 앱 비권장"으로 명시돼 있고 MFA·조건부 접근이 걸린 테넌트에서는 애초에 막혀 있어서, 실무에서는 거의 안 씁니다.

사용자님 아이디어는 구현 가능합니다 — 트리거 지점만 리다이렉트 방식으로 바꾸면 됩니다. 일반적인 패턴은 이렇습니다.

1. 로그인 화면에서 ID 입력(또는 "회사 계정으로 로그인" 버튼) → 로컬 table_0001 조회
2. 없으면 Microsoft 로그인 페이지로 리다이렉트 (비밀번호는 우리 서버를 거치지 않고 Microsoft에서 직접 처리)
3. 콜백으로 authorization code → 토큰 교환 → ID 토큰에서 oid(entra_object_id), upn/preferred_username(entra_upn), 이름/이메일 클레임 추출
4. entra_object_id/entra_upn으로 table_0001 재조회
   - 있으면 기존 로직대로 세션(UserSession) 생성
   - 없으면 그 정보로 table_0001에 insert (confirm_yn=false), "승인 대기" 안내하고 로그인은 차단
5. 시스템관리자가 지금 있는 /api/user/confirm 플로우로 confirm_yn=true 처리 → 그 다음부터 정상 로그인

## querypie EC2 DB접속
```bash
psql -U postgres -d postgres -h dev-aisoc-postgres.cxegieo0kbv2.ap-northeast-2.rds.amazonaws.com
```

## querypie EC2 프론트엔드 기동하기
- 깃설치
```bash
sudo yum install -y git
```

- 노드설치
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
source ~/.bashrc
nvm install 20
node -v
```

- 깃 클론
```bash
git clone https://github.com/lgu-ciso/AISOC_WEB_FE.git aisoc-web-fe
```

- 소스폴더 접근
```bash
cd aisoc-web
```


- 서비스 기동
```bash
npm install
cp .env.example .env.local
```

```bash
nohup npm run dev > aisoc-fe.log 2>&1 &
```

- 로그 조회
```bash
tail -f aisoc-fe.log
```

- 서비스 중단
```bash
ps aux | grep "next dev"

kill <PID>
```

- 운영시 세팅 예시

지속적으로 운영할 거면 앞서 말씀드린 pm2가 더 낫습니다:
종료/재시작도 pm2 stop aisoc-web / pm2 restart aisoc-web으로 간단합니다. 지금은 테스트용으로 빨리 띄우는 거면 nohup, 운영 서버로 계속 띄워둘 거면 pm2 쓰시는 걸 추천해요.
```bash
npm install -g pm2
pm2 start npm --name aisoc-web -- run dev
pm2 logs aisoc-web      # 로그 확인
pm2 save
pm2 startup             # 재부팅 시 자동 실행
```

## querypie EC2 백엔드 기동하기

- ec2 터미널에서 폴더삭제 하는법
```bash
rm -rf <폴더명>
```

- 자바 설치
```bash
sudo dnf install -y java-17-amazon-corretto-devel git
```

- maven 설치
```bash
sudo yum install -y maven
```

- 초기화쿼리 실행
```bash
psql -h dev-aisoc-postgres.cxegieo0kbv2.ap-northeast-2.rds.amazonaws.com -U postgres -d postgres -f src/main/resources/schema.sql
```

- 깃 클론
```bash
git clone https://github.com/lgu-ciso/AISOC_WEB_BE.git aisoc-web-be
```


- 빌드
```bash
cd aisoc-web-be
mvn -B clean package -DskipTests
```

- 서비스 기동
```bash
export JASYPT_ENCRYPTOR_PASSWORD=dbvmfvkxmsj
export SPRING_PROFILES_ACTIVE=dev
nohup java -jar target/aisoc.jar > aisoc-be.log 2>&1 &
```

- 서비스 확인
```bash
ps -ef |grep java
```

```bash
ss -an |grep 8081
```

- 운영시 세팅 예시

 pm2는 Node.js 전용이 아니라 임의의 프로세스를 관리할 수 있어서 Java/Spring Boot jar도 돌릴 수 있습니다.

```bash
pm2 start java --name aisoc-no-okta --interpreter none -- -jar target/aisoc-*.jar
```

환경변수(JASYPT_ENCRYPTOR_PASSWORD, SPRING_PROFILES_ACTIVE)는 미리 export 해두거나

ecosystem config 파일의 env에 넣어줘야 함

또는 pm2 start ecosystem.config.js처럼 설정 파일로 관리하는 방법도 가능합니다.

- pm2 세팅방법
0. 사전 준비 (pm2 자체는 Node.js 기반이라 Node가 설치되어 있어야 함)
```bash
node -v    # 없으면 설치 필요
sudo npm install -g pm2
```

1. ecosystem.config.js 작성 (프로젝트 루트, 예: /home/ec2-user/aisoc-no-okta/ecosystem.config.js)
```bash
module.exports = {
  apps: [
    {
      name: 'aisoc-no-okta',
      script: 'java',
      args: '-jar target/aisoc-no-okta-0.0.1-SNAPSHOT.jar',
      cwd: '/home/ec2-user/aisoc-no-okta',
      interpreter: 'none',
      env: {
        JASYPT_ENCRYPTOR_PASSWORD: '실제비밀번호',
        SPRING_PROFILES_ACTIVE: 'dev',
      },
      autorestart: true,
      restart_delay: 5000,
      max_restarts: 10,
    },
  ],
};
```
- args의 jar 파일명은 mvn package 후 target/ 밑 실제 생성된 이름으로 맞춰야 함.
- interpreter: 'none'을 꼭 넣어야 pm2가 java를 node 스크립트로 해석하지 않고 그대로 실행함.
- 비밀번호를 파일에 평문으로 두기 싫으면 env를 빼고 쉘에서 export JASYPT_ENCRYPTOR_PASSWORD=... 먼저 해둔 뒤 pm2를 실행하는 방법도 가능.

2. 기동
```bash
cd /home/ec2-user/aisoc-no-okta
pm2 start ecosystem.config.js
```

3. 부팅 시 자동 시작 설정
```bash
pm2 save              # 현재 프로세스 목록 저장
pm2 startup           # OS 재부팅 시 pm2 자체를 띄우는 systemd 유닛 생성 (출력된 명령어를 그대로 한번 더 실행해야 함)
```

4. 관리 명령어
```bash
pm2 status                    # 상태 확인
pm2 logs aisoc-no-okta        # 로그 확인
pm2 restart aisoc-no-okta     # 재시작 (재배포 시)
pm2 stop aisoc-no-okta
pm2 delete aisoc-no-okta
```

- systemd 세팅방법
systemd로 등록하는 방법입니다.

1. 서비스 유닛 파일 생성
```bash
sudo vi /etc/systemd/system/aisoc-no-okta.service
```

```bash
[Unit]
Description=aisoc-no-okta Spring Boot App
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/home/ec2-user/aisoc-no-okta
Environment="JASYPT_ENCRYPTOR_PASSWORD=실제비밀번호"
Environment="SPRING_PROFILES_ACTIVE=dev"
ExecStart=/usr/bin/java -jar /home/ec2-user/aisoc-no-okta/target/aisoc-no-okta-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

- ExecStart의 jar 파일명은 실제 target/ 밑에 생성된 이름으로 맞춰야 함 (mvn package 후 ls target/*.jar로 확인).
- java 경로는 which java로 확인해서 넣기.
- 비밀번호를 유닛 파일에 평문으로 두기 싫으면 EnvironmentFile=/home/ec2-user/aisoc-no-okta/.env 로 분리해서 별도 파일(퍼미션 600)로 관리하는 것도 가능.

2. 등록 및 실행
```bash
sudo systemctl daemon-reload
sudo systemctl enable aisoc-no-okta     # 부팅 시 자동 시작
sudo systemctl start aisoc-no-okta
sudo systemctl status aisoc-no-okta
```

3. 로그 확인
```bash
sudo journalctl -u aisoc-no-okta -f
```

4. 재배포 시(새 jar 올린 뒤)
```bash
sudo systemctl restart aisoc-no-okta
```

pm2와 비교하면 OS에 통합돼서 부팅 자동시작·로그(journalctl) 연동이 기본 제공되는 게 장점이고, 대신 설정 파일이 pm2 ecosystem.config.js보다 약간 더 번거롭습니다.

## web과 agent연동에 대한 고찰
- 전제: 요구사항 P-02 / 전체현황표를 보면 Investigation / Hunting / MCP / Web이 애초에 4개 분리된 개발 트랙이야. 
aisoc-no-okta는 이 중 Web(BE) 트랙이고, Investigation Agent·Hunting Agent·MCP Gateway는 원칙적으로 별도 서비스로 개발되는 구조로 보여(SOAR→Investigation Agent, OpenCTI→Hunting Agent 직결 화살표). 
즉 aisoc-no-okta의 역할은 "Agent 로직 구현"이 아니라 그걸 관리/조회/트리거하는 관리자 백엔드+화면이라는 가정으로 답할게.

1. MCP Server 연동

- (추천) 관리 CRUD + Direct API 위임: Connector/Tool 등록·테스트·버전관리(FR-MCP-001~009)는 aisoc-no-okta DB에 저장하는 관리기능으로 두고
, "연결 테스트"/"실행" 같은 실제 액션만 별도 MCP Gateway 프로세스의 REST API를 호출해 위임. 
지금 UserController/LogController 패턴 그대로 ConnectorController/ToolController를 추가하면 됨.
- MCP 프로토콜(stdio/SSE) 자체를 aisoc-no-okta가 client로 흡수하는 방법도 있지만, 
Gateway가 "공용"이라고 명시돼 있으니 Gateway 쪽에서 프로토콜을 흡수하고 BE는 앞단 REST만 보는 (A)가 백엔드 부담이 적어.

백엔드 준비사항:
- 새 테이블: connector, tool, tool_version, connector_health (UserDto/UserVo+Mapper XML 패턴 그대로)
- Connector 인증정보 암호화 — 기존 EncryptUtil/Jasypt 재사용
- 외부 호출용 HTTP client 의존성이 지금 pom.xml에 전혀 없음 (순수 MVC+MyBatis). WebClient나 RestClient 추가 필요
- Tool 사용 권한을 "역할"뿐 아니라 "Agent 유형"까지 확장 — 지금 /permissions는 역할↔메뉴/API 매핑만 있어서 스키마 확장 필요

2. Hunting Agent 연동
(추천) Hunting Agent가 스케줄/실행을 자체 소유하고, 
 BE는 ① Hunt 주기/대상 설정을 관리 API로 노출해 Hunting Agent에 전달
 , ② 결과는 Hunting Agent가 콜백(webhook)으로 적재하거나 BE가 주기적으로 상태를 polling. 
 FR-HA-002 비고에 "주기값은 운영설정"이라고 돼 있어서 Web은 설정값만 바꾸고 실제 트리거는 Agent 책임으로 분리하는 게 깔끔함.

백엔드 준비사항:
- 새 테이블: hunt_run(트리거유형: scheduled/manual/natural-language,finding(Evidence/영향자산/IOC·TTP/요약/SOAR Ticket ID)
- 콜백 수신 엔드포인트든 polling 배치든 하나 선택 필요 — 지금 코드에 전혀 없으니(grep 결과 없음) polling 쪽이면 이것도 새로 추가해야 함
- /logs·/users의 검색+페이징+export 패턴을 Hunt Run/Finding 목록에도 동일하게 재사용 가능 (FR-WEB-005)

3. Investigation Agent 연동

- SOAR→Investigation Agent는 Agent가 직접 처리(FR-CASE-001)해서 BE가 끼지 않아. 
BE가 필요한 건 두 방향: 
① Case/Evidence/Timeline 조회(Investigation Agent의 REST를 호출, 또는 공용 "RAG·실행로그·조사전략 DB"를 같이 읽는 방식)
, ② Analyst Feedback/추가조사 요청 같은 쓰기를 Investigation Agent API로 전달.
- 조회를 Agent DB 직접 읽기로 할지 REST로만 할지는 서비스 경계 문제라 결정이 필요 — 경계 지키려면 REST 권장, 대시보드 집계(FR-WEB-001)처럼 레이턴시 부담되는 건 읽기전용 뷰/복제도 고려할 만함

백엔드 준비사항:
- 새 테이블/연동: case(또는 Investigation Agent 쪽 참조), feedback(원본 보존+정정 이력, FR-FB-002), case_revision(FR-FB-005)
- Feedback 작성 시 Investigation Agent 재분석 트리거 API로 아웃바운드 호출 — MCP 항목과 동일하게 HTTP client 의존성 필요
- Case 상세 화면(FR-WEB-002)에 필요한 조사이력/Tool 호출이력 필드 계약을 Investigation Agent 쪽과 먼저 맞춰야 프론트 lib/types.ts 계약 문서화가 가능

- 공통 갭
세 항목 모두에서 걸리는 공통 빈틈 하나: 지금 aisoc-no-okta는 외부 서비스를 호출하는 HTTP client 의존성이 아예 없음(순수 CRUD). 어느 걸 먼저 붙이든 이건 선행 작업임.

## 참고 URL
- https://www.oracle.com/java/technologies/downloads/#java17-windows
- https://splendidlolli.tistory.com/756
- https://manystory.tistory.com/entry/InteliJ-%ED%8F%90%EC%87%84%EB%A7%9D-%ED%94%8C%EB%9F%AC%EA%B7%B8%EC%9D%B8-%EC%B6%94%EA%B0%80
- https://start.spring.io/index.html
- https://postgresql.kr/blog/postgresql_for_windows.html

## 가상 데스크톱 주요 단축키
- 데스크톱 전환: Windows + Ctrl + ← / → (왼쪽 또는 오른쪽 데스크톱으로 이동)
- 데스크톱 보기 (작업 보기): Windows + Tab (모든 가상 데스크톱과 실행 중인 앱 확인)
- 새 데스크톱 만들기: Windows + Ctrl + D

## 현재 유저가 가지고 있는 테이블 조회
```sql
SELECT * 
FROM pg_catalog.pg_tables 
where schemaname = 'public';
```

## 테이블 데이터 json 조회
```sql
SELECT json_agg(row_to_json(t)) 
FROM (SELECT * FROM table_0003) t;
```

## 조회한 json데이터 밀어넣기
```sql
INSERT INTO tb_test (a, b, create_date)
SELECT a, b, create_date
FROM json_to_recordset($json$
[json데이터]
$json$::json) AS x(a varchar, b varchar, create_date timestamp);
```

## for문 돌려서 테스트데이터 삽입
```sql
DO $$
DECLARE
	i INT;
BEGIN
	FOR i IN 1..50 LOOP
			insert into table_0001 (user_id, user_name, group_code, group_name, created_by)
			values ('testuser'||i, '테스트유저'||i
				   , 'T261019'||i, 'TEST_어쩌구저쩌구블라블라협력사직원'||i, 'jake');
	END LOOP;
END $$;
```
