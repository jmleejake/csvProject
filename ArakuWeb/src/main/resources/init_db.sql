/*
 * 
mysql> create database araku_db;
Query OK, 1 row affected (0.00 sec)

mysql> create user 'araku' identified by 'araku';
Query OK, 0 rows affected (0.01 sec)

mysql> use mysql
Database changed
mysql> grant all privileges on araku_db to 'araku';
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.02 sec)

-------- 한번에 되면 재미 없는것같다 이것들은...

mysql> flush privileges;
Query OK, 0 rows affected (0.13 sec)

mysql> grant all privileges on araku_db.* to 'araku'@'localhost' identified by 'araku';
Query OK, 0 rows affected, 1 warning (0.05 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

C:\ProgramData\MySQL\MySQL Server 5.7\my.ini
에서
[client]
default-character-set=utf8

[mysql]
no-beep

default-character-set=utf8

[mysqld]
character-set-server=utf8
collation-server=utf8_general_ci
init_connect=set collation_connection=utf8_general_ci
init_connect=set names utf8

를 차분한 마음으로 세팅해 일본어가 깨지는 참사를 안볼수있게 한다.

 */

/*
drop table test;
create table test (
	seq_id bigint unsigned primary key  auto_increment
	, name varchar(5)
	, email varchar(30)
	, phoneNo varchar(15)
	, country varchar(10)
	, register_date datetime default now()
);

insert into test (name, email, phoneNo, country)
values ('name1', 'test@test.com', '011-1234-5678', 'korea');

insert into test (name, email, phoneNo, country)
values ('name2', 'test1@test1.com', '090-1234-5678', 'japan');

insert into test (name, email, phoneNo, country)
values ('name3', 'test2@test3.com', '080-1234-5678', 'korea');

-- 특정 형식의 string을 date형태로
select str_to_date(register_date, '%Y/%m/%d') create_date from test

-- date를 string형태로
select date_format(register_date, '%Y/%m/%d') create_date from test

select
	seq_id
	, name
	, email
	, phoneNo
	, country
	, date_format(register_date, '%Y/%m/%d') register_date
from
	test
*/

/*楽天CSVファイル*/
drop table rakuten_info;

create table rakuten_info (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, update_date datetime /*データ修正日*/
	, order_no varchar(25) /*受注番号*/
	, order_status varchar(10) /*受注ステータス*/
	, card_pay_status varchar(10) /*カード決済ステータス*/
	, deposit_date varchar(10) /*入金日*/
	, delivery_date varchar(10) /*配送日*/
	, delivery_time varchar(10) /*お届け時間帯*/
	, delivery_date_sel varchar(10) /*お届け日指定*/
	, person_in_charge varchar(10) /*担当者*/
	, note varchar(50) /*ひとことメモ*/
	, note_to_customer varchar(50) /*メール差込文(お客様へのメッセージ)*/
	, init_purchase_amt varchar(10) /*初期購入合計金額*/
	, terminal varchar(3) /*利用端末*/
	, mail_carrier_code varchar(3) /*メールキャリアコード*/
	, gift_check varchar(1) /*ギフトチェック（0:なし/1:あり）*/
	, order_comment varchar(300) /*コメント*/
	, order_datetime varchar(20) /*注文日時*/
	, multi_destination_flag varchar(1) /*複数送付先フラグ*/
	, warning_flag varchar(1) /*警告表示フラグ*/
	, rmember_flag varchar(1) /*楽天会員フラグ*/
	, total varchar(10) /*合計*/
	, consume_tax varchar(10) /*消費税(-99999=無効値)*/
	, shipping_cost varchar(10) /*送料(-99999=無効値)*/
	, substitute_cost varchar(10) /*代引料(-99999=無効値)*/
	, billing_amt varchar(10) /*請求金額(-99999=無効値)*/
	, total_amt varchar(10) /*合計金額(-99999=無効値)*/
	, enclosed_id varchar(10) /*同梱ID*/
	, enclosed_status varchar(1) /*同梱ステータス*/
	, enclosed_goods_total_amt varchar(10) /*同梱商品合計金額*/
	, enclosed_total_shipping varchar(10) /*同梱送料合計*/
	, enclosed_total_substitue varchar(10) /*同梱代引料合計*/
	, enclosed_total_consume varchar(10) /*同梱消費税合計*/
	, enclosed_billing_amt varchar(10) /*同梱請求金額*/
	, enclosed_total_amt varchar(10) /*同梱合計金額*/
	, enclosed_rbank_transfer_fee varchar(10) /*同梱楽天バンク決済振替手数料*/
	, enclosed_total_point_usage varchar(10) /*同梱ポイント利用合計*/
	, mail_flag varchar(3) /*メールフラグ*/
	, order_date varchar(10) /*注文日*/
	, order_time varchar(20) /*注文時間*/
	, mobile_carrier_payment_no varchar(10) /*モバイルキャリア決済番号*/
	, purchase_hist_mod_type varchar(20) /*購入履歴修正可否タイプ*/
	, purchase_hist_mod_icon varchar(1) /*購入履歴修正アイコンフラグ*/
	, purchase_hist_mod_prompt_mail varchar(1) /*購入履歴修正催促メールフラグ*/
	, destination_match_flag varchar(1) /*送付先一致フラグ*/
	, point_usage varchar(1) /*ポイント利用有無*/
	, order_post_no1 varchar(4) /*注文者郵便番号１*/
	, order_post_no2 varchar(4) /*注文者郵便番号２*/
	, order_address1 varchar(20) /*注文者住所：都道府県*/
	, order_address2 varchar(20) /*注文者住所：都市区*/
	, order_address3 varchar(50) /*注文者住所：町以降*/
	, order_surname varchar(10) /*注文者名字*/
	, order_name varchar(10) /*注文者名前*/
	, order_surname_kana varchar(10) /*注文者名字フリガナ*/
	, order_name_kana varchar(10) /*注文者名前フリガナ*/
	, order_tel1 varchar(4) /*注文者電話番号１*/
	, order_tel2 varchar(4) /*注文者電話番号２*/
	, order_tel3 varchar(4) /*注文者電話番号３*/
	, mail_address varchar(60) /*メールアドレス*/
	, order_sex varchar(3) /*注文者性別*/
	, order_birth varchar(15) /*注文者誕生日*/
	, payment_method varchar(10) /*決済方法*/
	, credit_type varchar(10) /*クレジットカード種類*/
	, credit_no varchar(10) /*クレジットカード番号*/
	, credit_user varchar(10) /*クレジットカード名義人*/
	, credit_expire varchar(10) /*クレジットカード有効期限*/
	, credit_installment varchar(1) /*クレジットカード分割選択*/
	, credit_installment_note varchar(10) /*クレジットカード分割備考*/
	, delivery_method varchar(10) /*配送方法*/
	, delivery_type varchar(10) /*配送区分*/
	, point_usage_person varchar(10) /*ポイント利用額*/
	, point_usage_condition varchar(10) /*ポイント利用条件*/
	, point_status varchar(1) /*ポイントステータス*/
	, rbank_payment_status varchar(1) /*楽天バンク決済ステータス*/
	, rbank_payment_transfer_fee varchar(10) /*楽天バンク振替手数料負担区分*/
	, rbank_payment_fee varchar(10) /*楽天バンク決済手数料*/
	, wrap_paper_title varchar(10) /*ラッピングタイトル(包装紙)*/
	, wrap_paper_name varchar(10) /*ラッピング名(包装紙)*/
	, wrap_paper_fee varchar(10) /*ラッピング料金(包装紙)*/
	, wrap_paper_tax_include varchar(10) /*税込別(包装紙)*/
	, wrap_ribbon_title varchar(10) /*ラッピングタイトル(リボン)*/
	, wrap_ribbon_name varchar(10) /*ラッピング名(リボン)*/
	, wrap_ribbon_fee varchar(10) /*ラッピング料金(リボン)*/
	, wrap_ribbon_tax_include varchar(10) /*税込別(リボン)*/
	, delivery_cost varchar(10) /*送付先送料*/
	, delivery_substitute_cost varchar(10) /*送付先代引料*/
	, delivery_consume varchar(10) /*送付先消費税*/
	, baggage_claim_no varchar(12) /*お荷物伝票番号*/
	, delivery_goods_total_amt varchar(10) /*送付先商品合計金額*/
	, indicates varchar(10) /*のし*/
	, delivery_post_no1 varchar(4) /*送付先郵便番号１*/
	, delivery_post_no2 varchar(4) /*送付先郵便番号２*/
	, delivery_address1 varchar(20) /*送付先住所：都道府県*/
	, delivery_address2 varchar(20) /*送付先住所：都市区*/
	, delivery_address3 varchar(50) /*送付先住所：町以降*/
	, delivery_surname varchar(10) /*送付先名字*/
	, delivery_name varchar(10) /*送付先名前*/
	, delivery_surname_kana varchar(10) /*送付先名字フリガナ*/
	, delivery_name_kana varchar(10) /*送付先名前フリガナ*/
	, delivery_tel1 varchar(4) /*送付先電話番号１*/
	, delivery_tel2 varchar(4) /*送付先電話番号２*/
	, delivery_tel3 varchar(4) /*送付先電話番号３*/
	, product_id varchar(8) /*商品ID*/
	, product_name varchar(1500) /*商品名*/
	, product_no varchar(30) /*商品番号*/
	, product_url varchar(50) /*商品URL*/
	, unit_price varchar(10) /*単価*/
	, unit_no varchar(10) /*個数*/
	, delivery_cost_include varchar(10) /*送料込別*/
	, tax_exclude varchar(10) /*税込別*/
	, substitute_fee_include varchar(10) /*代引手数料込別*/
	, product_option varchar(1500) /*項目・選択肢*/
	, point_percent varchar(2) /*ポイント倍率*/
	, point_type varchar(1) /*ポイントタイプ*/
	, record_no varchar(10) /*レコードナンバー*/
	, delivery_info varchar(100) /*納期情報*/
	, inventory_type varchar(1) /*在庫タイプ*/
	, wrap_type_paper varchar(10) /*ラッピング種類(包装紙)*/
	, wrap_type_ribbon varchar(10) /*ラッピング種類(リボン)*/
	, tomorrow_hope varchar(1) /*あす楽希望*/
	, coupon_usage varchar(10) /*クーポン利用額*/
	, store_coupon_usage varchar(10) /*店舗発行クーポン利用額*/
	, rcoupon_usage varchar(10) /*楽天発行クーポン利用額*/
	, enclosed_coupon_usage varchar(10) /*同梱注文クーポン利用額*/
	, delivery_company varchar(4) /*配送会社*/
	, pharm_flag varchar(1) /*薬事フラグ*/
	, rakuten_super_deal varchar(1) /*楽天スーパーDEAL*/
	, membership_program varchar(1) /*メンバーシッププログラム*/
) default charset = utf8;


/*置換情報*/
drop table translation_info;

create table translation_info (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, update_date datetime /*データ修正日*/
	, before_trans varchar(3000) /*商品名・項目・選択肢 置換前*/
	, after_trans varchar(50) /*商品名・項目・選択肢 置換後*/
) default charset = utf8;


/*置換結果*/
drop table translation_result;

create table translation_result (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, trans_target_id bigint /*楽天・アマゾン区分ID*/
	, trans_target_type varchar(1) /*R:楽天・A:アマゾン*/
	, result_text varchar(200) /*置換結果テキスト*/
) default charset = utf8;


/*AMAZON 情報*/
drop table amazon_info;

create table amazon_info (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, update_date datetime /*データ修正日*/
	, order_id varchar(20)
	, order_item_id varchar(14)
	, purchase_date varchar(30)
	, payments_date varchar(30)
	, reporting_date varchar(30)
	, promise_date varchar(30)
	, days_past_promise varchar(2)
	, buyer_email varchar(50)
	, buyer_name varchar(20)
	, buyer_phone_number varchar(13)
	, sku varchar(13)
	, product_name varchar(500)
	, quantity_purchased varchar(2)
	, quantity_shipped varchar(2)
	, quantity_to_ship varchar(2)
	, ship_service_level varchar(13)
	, recipient_name varchar(20)
	, ship_address1 varchar(100)
	, ship_address2 varchar(100)
	, ship_address3 varchar(100)
	, ship_city varchar(20)
	, ship_state varchar(10)
	, ship_postal_code varchar(20)
	, ship_country varchar(5)
	, payment_method varchar(20)
	, cod_collectible_amount varchar(20)
	, already_paid varchar(20)
	, payment_method_fee varchar(20)
	, scheduled_delivery_start_date varchar(2)
	, scheduled_delivery_end_date varchar(10)
	, points_granted varchar(20)
	, is_prime varchar(6)
) default charset = utf8;



/*地域区分コードマスタ*/
drop table region_master;
create table region_master (
seq_id int primary key  auto_increment /*区分ID*/
, register_date datetime default now() /*データ登録日*/
, p_id int /*地域ID*/
, region_name varchar(8) /*名称*/
, region_name_en varchar(15) /*名称(英語)*/
, delivery_company varchar(4) /*配送会社*/
) default charset = utf8;

insert into region_master (p_id, region_name, delivery_company) values
(0, '北海道', 1003)
, (0, '北東北', 1003)
, (0, '南東北', 1003)
, (0, '関東', 1003)
, (0, '信越', 1003)
, (0, '北陸', 1003)
, (0, '中部', 1003)
, (0, '関西', 1003)
, (0, '中国', 1003)
, (0, '四国', 1003)
, (0, '九州', 1003)
, (0, '沖縄', 1003);

insert into region_master (p_id, region_name, region_name_en, delivery_company) values
(1, '北海道', 'Hokkaido', 1003)
, (2, '青森県', 'Aomori', 1003)
, (2, '秋田県', 'Akita', 1003)
, (2, '岩手県', 'Iwate', 1003)
, (3, '宮城県', 'Miyagi', 1003)
, (3, '山形県', 'Yamagata', 1003)
, (3, '福島県', 'Fukushima', 1003)
, (4, '茨城県', 'Ibaraki ', 1003)
, (4, '栃木県', 'Tochigi', 1003)
, (4, '群馬県', 'Gunma', 1003)
, (4, '埼玉県', 'Saitama', 1003)
, (4, '千葉県', 'Chiba', 1003)
, (4, '神奈川県', 'Kanagawa', 1003)
, (4, '東京都', 'Tokyo', 1003)
, (4, '山梨県', 'Yamanashi', 1003)
, (5, '新潟県', 'Niigata', 1003)
, (5, '長野県', 'Nagano', 1003)
, (6, '富山県', 'Toyama', 1003)
, (6, '石川県', 'Ishikawa', 1003)
, (6, '福井県', 'Fukui', 1003)
, (7, '静岡県', 'Shizuoka', 1003)
, (7, '愛知県', 'Aichi', 1003)
, (7, '三重県', 'Mie', 1003)
, (7, '岐阜県', 'Gifu', 1003)
, (8, '大阪府', 'Osaka', 1003)
, (8, '京都府', 'Kyoto', 1003)
, (8, '滋賀県', 'Shiga', 1003)
, (8, '奈良県', 'Nara', 1003)
, (8, '和歌山県', 'Wakayama', 1003)
, (8, '兵庫県', 'Hyogo', 1003)
, (9, '岡山県', 'Okayama', 1003)
, (9, '広島県', 'Hiroshima', 1003)
, (9, '山口県', 'Yamaguchi', 1003)
, (9, '鳥取県', 'Tottori', 1003)
, (9, '島根県', 'Shimane', 1003)
, (10, '香川県', 'Kagawa', 1003)
, (10, '徳島県', 'Tokushima', 1003)
, (10, '愛媛県', 'Ehime', 1003)
, (10, '高知県', 'Kochi', 1003)
, (11, '福岡県', 'Fukuoka', 1003)
, (11, '佐賀県', 'Saga', 1003)
, (11, '長崎県', 'Nagasaki', 1003)
, (11, '熊本県', 'Kumamoto', 1003)
, (11, '大分県', 'Oita', 1003)
, (11, '宮崎県', 'Miyazaki', 1003)
, (11, '鹿児島県', 'Kagoshima', 1003)
, (12, '沖縄県', 'Okinawa', 1003);





select b.seq_id, a.region_name parent_region_name, b.region_name, b.region_name_en
from region_master a
inner join region_master b on a.seq_id = b.p_id












update rakuten_info
/*
set register_date = adddate(now(), -3)
*/
set register_date = now()

update translation_info
set register_date = now()


select after_trans
/*
 ミチョ　
 パイン
 カラ
 マス
select 
	seq_id
	, before_trans
	, after_trans
	, date_format(register_date, '%Y/%m/%d') register_date
*/
from
	translation_info


select 
	seq_id
	, order_no
	, order_status
	, delivery_date_sel
	, total_amt
	, baggage_claim_no
	, delivery_surname
	, delivery_name
	, delivery_surname_kana
	, delivery_name_kana
	, delivery_tel1
	, delivery_tel2
	, delivery_tel3
	, product_name
	, product_option
	, unit_no
	, tomorrow_hope
	, date_format(register_date, '%Y/%m/%d') register_date
from
	rakuten_info
where
	register_date between str_to_date('2018/08/09 00:00:00', '%Y/%m/%d %H:%i:%s') and now()
	
	
select
	tr.seq_id
	, date_format(tr.register_date, '%Y/%m/%d') register_date
	, result_text
	, product_name
	, product_option
	, delivery_surname
	, delivery_name
	, unit_no
from
	translation_result tr
inner join rakuten_info ri on ri.seq_id = tr.trans_target_id
