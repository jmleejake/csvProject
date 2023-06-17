## Araku system Contents update list <csvProject>
### 2023.06
<ul>
<li>
単品管理 수정 요청사항 대응	
<div>
	
```sql
alter table tanpin_info add column prd_qty int4 COMMENT '入庫数';
alter table tanpin_info add column exp_dt datetime COMMENT '賞味期限';

drop table expire_manage;
create table expire_manage(
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
	, jan_cd varchar(15) comment 'ＪＡＮコード'	
	, prd_nm varchar(1000) comment '商品名'
	, partner_id varchar(20) comment '取引先'
	, partner_nm varchar(100) comment '取引先名'
	, prd_qty int4 comment '入庫数'
	, exp_dt datetime COMMENT '賞味期限'
) default charset = utf8 comment '賞味管理';
```	
</div>
<div>src/main/webapp/WEB-INF/views/tanpin/prdManage.jsp</div>
<div>src/main/webapp/resources/tanpin/prdManage.js</div>
<div>src/main/java/jp/prj/araku/tanpin/vo/TanpinVO.java</div>
<div>src/main/java/jp/prj/araku/tanpin/vo/ExpireManageVo.java</div>
<div>
<img src='https://github.com/jmleejake/csvProject/assets/18359068/b6d54e5d-cb81-4e05-bcae-52836914a716' width='300' />	
</div>
</li>
<li>
タブレット注文画面 수정 요청사항 대응
<div>src/main/webapp/WEB-INF/views/menu/tabletOrder.jsp</div>
<div>
<img src='https://github.com/jmleejake/csvProject/assets/18359068/ee94a878-0499-4b81-9d04-522e0f6457fa' width='300' />
</div>
</li>
</ul>

### 2023.04 Rakuten download/upload 탬플릿항목 추가 예정
<img src='https://user-images.githubusercontent.com/18359068/197534164-9f25bbfe-969c-4c52-9350-c4c264c66b17.png' width='300' />

### 2023.01
<ul>
<li>
발주서 상품리스트 노출영역 24줄로 수정
<div>src/main/java/jp/prj/araku/tanpin/dao/TanpinDAO.java > downloadOrderForm</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/212219842-09ca1d17-3554-4a8c-829c-1d49d0fd9e42.png' width='300' />
</div>
</li>
<li>
재고관리화면 > 거래처별 다운로드 구현
<div>src/main/java/jp/prj/araku/jaiko/inventory/controller/JaikoPrdInventoryController.java > showJaikoPrdInventory</div>
<div>src/main/webapp/WEB-INF/views/jaiko/prdInventory.jsp</div>
<div>src/main/webapp/resources/jaiko/prdInventory.js</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/212800933-52c88f17-075a-49a5-9093-3fa97d7289dc.png' width='300' />
</div>
</li>
<li>
에러 대응
<div>해당 부분 디버깅시 정상작동하는것으로 판단되어 관련 alert메시지 수정</div>
<div>src/main/webapp/resources/jaiko/newWareIn.js</div>
<div>src/main/webapp/resources/jaiko/newWareOut.js</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/212801376-8542a040-d51f-45c3-87a0-6395f94c070e.png' width='300' />
</div>
</li>
<li>
치환마스터 화면에 사이즈컬럼 추가
<div>

```sql
alter table translation_info add column prd_size varchar(5) comment 'サイズ';
```
</div>
<div>src/main/java/jp/prj/araku/list/vo/TranslationVO.java</div>
<div>src/main/java/jp/prj/araku/list/mapper/ListMapper.xml</div>
<div>src/main/webapp/resources/menu/translation.js</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/215120179-182c357d-3712-434f-b1a4-9ecb8f3ffa29.png' width='300' />
</div>
</li>
</ul>

### 2022.12
<ul>
<li>
src/main/java/jp/prj/araku/tanpin/dao/TanpinDAO.java > downloadOrderForm
<div>
<img src='https://user-images.githubusercontent.com/18359068/208329093-90f75b6c-c2c7-4488-be65-dabea2567d81.png' width='300' />
</div>
</li>
<li>
src/main/webapp/resources/jaiko/salesMain.js > 페이지 접근시 일주일 default설정
<div>
<img src='https://user-images.githubusercontent.com/18359068/208329258-5c5ee011-2824-47df-9552-8d89bcf386cf.png' width='300' />
</div>
</li>
<li>
src/main/java/jp/prj/araku/jaiko/product/mapper/JaikoPrdInfoMapper.xml > getJaikoPrdInfo
<div>
<img src='https://user-images.githubusercontent.com/18359068/208329304-ee3716e1-45ac-43cd-967b-9ac8d530c922.png' width='300' />
</div>
</li>
<li>
src/main/webapp/WEB-INF/views/menu/translation.jsp > modal-dialog > style width설정
<div>
<img src='https://user-images.githubusercontent.com/18359068/208329331-158cc084-86d1-495e-be4e-d9110b283522.png' width='300' />
</div>
</li>
<li>
src/main/java/jp/prj/araku/jaiko/product/mapper/JaikoPrdInfoMapper.xml > getJaikoPrdInfo
<div>
<img src='https://user-images.githubusercontent.com/18359068/208593862-08203099-e3e9-4b39-910f-f14cd13584e9.png' width='300' />
</div>
</li>
</ul>

### 2022.11.14
<ul>
<li>
아마존 > 在庫管理(Keyword別) > 아마존재고/아마존/라쿠텐 검색기능 / 메모입력 추가
<div>

```sql
alter table kwrd_srch_info add column memo varchar(2000) comment 'メモ';
```
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/201694241-fcb74374-8ed1-4c26-91b0-1af98f97a668.png' width='300' />
</div>
</li>
<li>
재고관리 > 입고/출고 > jan_cd검색시 temp테이블 jan_cd에 상품정보 jan_cd1 입력
<div>
<img src='https://user-images.githubusercontent.com/18359068/201694374-0a85b136-b4c8-4cd7-b18a-fd74df0e8969.png' width='300' />
</div>
</li>
</ul>

### 2022.11.02 - 11.06 재고관리시스템 > 입고/출고 화면 개편

```sql
alter table jaiko_warehouse_info add column tantou_id varchar(20) comment '担当者ID';
alter table jaiko_warehouse_info add column tantou_nm varchar(100) comment '担当者名';
alter table jaiko_warehouse_info add column prd_unit char(1) default '1' comment '単位';
alter table jaiko_warehouse_info add column ware_loc char(1) default '1' comment '場所';

alter table jaiko_prd_info add column jan_cd1 varchar(15) comment 'JAＮコード1(単品)';
alter table jaiko_prd_info add column prd_cnt1 varchar(15) comment '商品数1';
alter table jaiko_prd_info add column jan_cd2 varchar(15) comment 'JANコード2(中数)';
alter table jaiko_prd_info add column prd_cnt2 varchar(15) comment '商品数2';
alter table jaiko_prd_info add column jan_cd3 varchar(15) comment 'JANコード3(箱)';
alter table jaiko_prd_info add column prd_cnt3 varchar(15) comment '商品数3';

drop table jaiko_warehouse_tantou_info;
create table jaiko_warehouse_tantou_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, reg_dt datetime default now() comment '登録日付'
, upd_dt datetime comment '更新日付'
, tantou_id varchar(20) comment '担当者ID'
, tantou_pass varchar(20) comment 'パスワード'
, tantou_nm varchar(100) comment '担当者名'
, tantou_tel varchar(20) comment '電話番号'
, tantou_auth varchar(3) comment '権限'
) default charset = utf8 comment '担当者情報';

drop table jaiko_warehouse_temp;
create table jaiko_warehouse_temp (
seq_id bigint unsigned comment '区分ID'
, partner_id varchar(20) comment '取引先'
, partner_nm varchar(100) comment '取引先名'
, tantou_id varchar(20) comment '本社担当者ID'
, tantou_nm varchar(100) comment '本社担当者名'
, jan_cd varchar(15) comment 'JAＮコード'
, prd_cd varchar(20) comment '商品コード'
, prd_nm varchar(1000) comment '商品名'
, prd_quantity varchar(5) comment '商品数量'
, prd_unit char(1) comment '単位'
, ware_loc char(1) comment '場所'
, reg_dt datetime default now() comment '登録日付'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '入出庫TEMP';
```

### 2022.10.23 아마존 > 在庫管理(Keyword別) 메뉴추가

```sql
 drop table kwrd_srch_info;
 create table kwrd_srch_info(
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
	, kwrd1 VARCHAR(50) comment 'キーワード１(メーカー名)'
	, kwrd2 VARCHAR(50) comment 'キーワード2(キーワード名)'
 ) default charset = utf8 comment 'キーワード検索管理';
 ```
<ul>
 <li>
 추가메뉴 캡쳐
 <div>
 <img src='https://user-images.githubusercontent.com/18359068/197352982-db43433c-f1bc-4925-9757-49d8c82e1458.png' width='300' />
 </div>
 </li>
 <li>
 LOMBOK 인스톨
 <div>
 <img src='https://user-images.githubusercontent.com/18359068/197353308-bb69e680-6f5b-4a58-a318-bb45bf3928de.png' width='300' />
 </div>
 </li>
</ul>

### 2022.06.27
<ol>
<li>
発注書 포맷변경
<div>
<img src='https://user-images.githubusercontent.com/18359068/175954313-cdd4dc55-195c-4eee-815b-ede76e140185.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/175954066-0ae3eea3-e41b-4d80-9870-2659044bf71e.png' width='300' />
</div>
</li>
<li>
재고관리 > 상품재고 > 다운로드 클릭시 선택된 데이터만 다운로드 가능하도록 수정
<div>
<img src='https://user-images.githubusercontent.com/18359068/175954489-ef9f329a-4f91-4cc4-b481-05f65aa8b89f.png' width='300' />
</div>
</li>
</ol>

### 2022.05.20 단품상품관리 メーカー名 자동완성기능
<img src='https://user-images.githubusercontent.com/18359068/169544246-97078489-4862-4fb2-9eb6-c50fc65d47a1.png' width='300' />

### 2022.04.19 納品書

```sql
alter table dealer_info add column gbn varchar(20) comment '締切区分';

drop table jaiko_sales_info;
create table jaiko_sales_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, gbn varchar(20) comment '区分'
, partner_id varchar(20) comment '取引先コード'
, partner_nm varchar(100) comment '取引先会社名'
, dlv_dt datetime comment '納品日'
, sales_no varchar(20) comment '売上番号'
, prd_nm varchar(1500) comment '商品名'
, prd_cd varchar(20) comment '商品コード'
, spec varchar(20) comment '仕様・規格'
, entry_no varchar(20) comment '入り数'
, qty varchar(20) comment '数量'
, dlv_prc varchar(20) comment '納品価格'
, tax_rt varchar(2) comment '税率'
, tot varchar(20) comment '合計'
, cnsp_tax varchar(20) comment '消費税'
, sub_tot varchar(20) comment '小計'
, mid_tot varchar(20) comment '中合計'
, bill_no varchar(20) comment '請求番号'
, pay_method varchar(20) comment '決済方法'
, pay_comp_yn char(1) comment '決済完了有無'
, manager varchar(100) comment '担当'
, mail varchar(100) comment 'メール'
, memo varchar(4000) comment 'メモ'
, bill_dt datetime comment '請求日付'
, jan_cd varchar(15) comment 'JANコード'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime comment '更新日付'
) default charset = utf8 comment '売上情報';
```

### 2022.04.11 JAIKO管理システムにて仕様変更
<ol>
 <li>注文状況画面→新規登録　ボタン押下時、何も検索しないように変更する。</li>
 <li>新規登録ボタン押下時、SUB画面の幅と高さをもっと大きくする。</li>
 <li>
  SUB画面の詳細のカラムを以下の4項目に変更する。
  <ol>
   <li>商品名検索</li>
   <li>商品名</li>
   <li>数量(箱)</li>
   <li>数量(個)</li>
  </ol>
  </li>
</ol>

### 2022.02.03 라쿠텐 order_no 길이변경 대응
```sql
alter table rakuten_frozen_info modify column order_no VARCHAR(30); // 라쿠텐 냉동
alter table rakuten_info modify column order_no VARCHAR(30); // 라쿠텐
alter table prd_trans_info modify column order_no VARCHAR(30); // 중간마스터
```
### 見積書
- init.sql > esitmate_info 테이블 추가

```sql
drop table estimate_info;
create table estimate_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, estimate_id bigint comment '見積書番号'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime comment '更新日付'
, partner_id varchar(20) comment '取引先No.'
, partner_nm varchar(100) comment '取引先名'
, brand_nm varchar(20) comment 'ブランド'
, jan_cd varchar(15) comment 'ＪＡＮコード'
, prd_cd varchar(20) comment '商品コード'
, prd_nm varchar(1000) comment '商品名'
, prd_prc varchar(20) comment '金額'
, dsku varchar(7) comment 'ＳＫＵ'
, dasin varchar(7) comment 'ＡＳＩＮ'
, tax_incld varchar(12) comment '商品税(抜、込)'
, tax_rt varchar(2) comment '商品税率'
, std_info varchar(30) comment '規格'
) default charset = utf8 comment '見積明細';
```
<ul>
<li>
화면 확인
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670441-5b813687-9484-4804-81a3-fcb95ed31ca1.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670454-13388c79-18a6-419e-9a5f-8f1ec4b40418.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670467-22bf395a-9603-45f2-a905-ae8609483804.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670486-fdad38c7-b917-4bf9-9041-54d876332dc8.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670503-7cc569f1-b4e4-4431-a14f-d325c60d8ead.png' width='300' />
</div>
</li>
<li>
작성 > 일괄 변경시
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670517-ae3e0b90-8a50-4e4b-a38e-9cbd5f551bfb.png' width='300' />
</div>
</li>
<li>
개별 변경시
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670550-caefdeba-8aba-4cae-9e27-6d69382f1f25.png' width='300' />
</div>
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670560-5f57636c-10b8-4f2a-978f-65fc1ebcf044.png' width='300' />
</div>
</li>
<li>
다운로드
<div>
<img src='https://user-images.githubusercontent.com/18359068/150670580-7300c431-5ac1-4d6c-97cc-4ba9f12063ce.png' width='300' />
</div>
</li>
</ul>
