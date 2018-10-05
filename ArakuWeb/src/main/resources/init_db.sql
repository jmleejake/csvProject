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
/*
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
*/

/*NEW 楽天CSVファイル*/

drop table rakuten_info;
create table rakuten_info (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, update_date datetime /*データ修正日*/
	, delivery_company varchar(4) /*配送会社*/
	, baggage_claim_no varchar(12) /*お荷物伝票番号*/
	, order_no varchar(25) /*注文番号*/
	, order_status varchar(10) /*ステータス*/
	, sub_status_id varchar(10) /*サブステータスID*/
	, sub_status varchar(10) /*サブステータス*/
	, order_datetime varchar(20) /*注文日時*/
	, order_date varchar(10) /*注文日*/
	, order_time varchar(20) /*注文時間*/
	, cancel_due_date varchar(10) /*キャンセル期限日*/
	, order_check_datetime varchar(20) /*注文確認日時*/
	, order_confirm_datetime varchar(20) /*注文確定日時*/
	, delivery_eta_datetime varchar(20) /*発送指示日時*/
	, delivery_ata_datetime varchar(20) /*発送完了報告日時*/
	, pay_method_name varchar(50) /*支払方法名*/
	, creadit_pay_method varchar(50) /*クレジットカード支払い方法*/
	, credit_pay_times varchar(2) /*クレジットカード支払い回数*/
	, delivery_method varchar(10) /*配送方法*/
	, delivery_type varchar(10) /*配送区分*/
	, order_type varchar(20) /*注文種別*/
	, multi_destination_flag varchar(1) /*複数送付先フラグ*/
	, destination_match_flag varchar(1) /*送付先一致フラグ*/
	, island_flag varchar(1) /*離島フラグ*/
	, rverify_flag varchar(1) /*楽天確認中フラグ*/
	, warning_type varchar(10) /*警告表示タイプ*/
	, rmember_flag varchar(1) /*楽天会員フラグ*/
	, purchase_hist_mod_flag varchar(1) /*購入履歴修正有無フラグ*/
	, total_goods_amt varchar(10) /*商品合計金額*/
	, total_consume_tax varchar(10) /*消費税合計*/
	, total_shipping varchar(10) /*送料合計*/
	, gross_deduction varchar(10) /*代引料合計*/
	, invoice_amt varchar(10) /*請求金額*/
	, total_amt varchar(10) /*合計金額*/
	, point_usage varchar(10) /*ポイント利用額*/
	, total_coupon_usage varchar(10) /*クーポン利用総額*/
	, store_coupon_usage varchar(10) /*店舗発行クーポン利用額*/
	, rakuten_coupon_usage varchar(10) /*楽天発行クーポン利用額*/
	, order_post_no1 varchar(4) /*注文者郵便番号1*/
	, order_post_no2 varchar(4) /*注文者郵便番号2*/
	, order_add1 varchar(20) /*注文者住所都道府県*/
	, order_add2 varchar(20) /*注文者住所郡市区*/
	, order_add3 varchar(50) /*注文者住所それ以降の住所*/
	, order_surname varchar(10) /*注文者姓*/
	, order_name varchar(10) /*注文者名*/
	, order_surname_kana varchar(10) /*注文者姓カナ*/
	, order_name_kana varchar(10) /*注文者名カナ*/
	, order_tel1 varchar(4) /*注文者電話番号1*/
	, order_tel2 varchar(4) /*注文者電話番号2*/
	, order_tel3 varchar(4) /*注文者電話番号3*/
	, order_email varchar(60) /*注文者メールアドレス*/
	, order_sex varchar(3) /*注文者性別*/
	, request_no varchar(10) /*申込番号*/
	, request_received_no varchar(10) /*申込お届け回数*/
	, ship_id varchar(10) /*送付先ID*/
	, ship_charge varchar(10) /*送付先送料*/
	, ship_substitute_fee varchar(10) /*送付先代引料*/
	, ship_total_consume_tax varchar(10) /*送付先消費税合計*/
	, ship_total_goods_amt varchar(10) /*送付先商品合計金額*/
	, ship_total_amt varchar(10) /*送付先合計金額*/
	, indicates varchar(10) /*のし*/
	, delivery_post_no1 varchar(4) /*送付先郵便番号1*/
	, delivery_post_no2 varchar(4) /*送付先郵便番号2*/
	, delivery_add1 varchar(20) /*送付先住所都道府県*/
	, delivery_add2 varchar(20) /*送付先住所郡市区*/
	, delivery_add3 varchar(50) /*送付先住所それ以降の住所*/
	, delivery_surname varchar(10) /*送付先姓*/
	, delivery_name varchar(10) /*送付先名*/
	, delivery_surname_kana varchar(10) /*送付先姓カナ*/
	, delivery_name_kana varchar(10) /*送付先名カナ*/
	, delivery_tel1 varchar(4) /*送付先電話番号1*/
	, delivery_tel2 varchar(4) /*送付先電話番号2*/
	, delivery_tel3 varchar(4) /*送付先電話番号3*/
	, product_detail_id varchar(10) /*商品明細ID*/
	, product_id varchar(8) /*商品ID*/
	, product_name varchar(1500) /*商品名*/
	, product_no varchar(30) /*商品番号*/
	, product_manage_no varchar(100) /*商品管理番号*/
	, unit_price varchar(10) /*単価*/
	, unit_no varchar(10) /*個数*/
	, delivery_cost_include varchar(10) /*送料込別*/
	, tax_exclude varchar(10) /*税込別*/
	, substitute_fee_include varchar(10) /*代引手数料込別*/
	, product_option varchar(1500) /*項目・選択肢*/
	, point_multiple varchar(10) /*ポイント倍率*/
	, delivery_info varchar(200) /*納期情報*/
	, inventory_type varchar(10) /*在庫タイプ*/
	, wrap_title1 varchar(10) /*ラッピングタイトル1*/
	, wrap_name1 varchar(10) /*ラッピング名1*/
	, wrap_amt1 varchar(10) /*ラッピング料金1*/
	, wrap_tax_include1 varchar(10) /*ラッピング税込別1*/
	, wrap_type1 varchar(10) /*ラッピング種類1*/
	, wrap_title2 varchar(10) /*ラッピングタイトル2*/
	, wrap_name2 varchar(10) /*ラッピング名2*/
	, wrap_amt2 varchar(10) /*ラッピング料金2*/
	, wrap_tax_include2 varchar(10) /*ラッピング税込別2*/
	, wrap_type2 varchar(10) /*ラッピング種類2*/
	, delivery_time varchar(10) /*お届け時間帯*/
	, delivery_date_sel varchar(10) /*お届け日指定*/
	, manager varchar(10) /*担当者*/
	, quick_note varchar(100) /*ひとことメモ*/
	, msg_to_customer varchar(200) /*メール差込文 (お客様へのメッセージ)*/
	, gift_request varchar(10) /*ギフト配送希望*/
	, comment varchar(300) /*コメント*/
	, util_terminal varchar(10) /*利用端末*/
	, mail_carrier_code varchar(10) /*メールキャリアコード*/
	, tomorrow_hope varchar(1) /*あす楽希望フラグ*/
	, drug_order_flag varchar(1) /*医薬品受注フラグ*/
	, rakuten_super_deal varchar(1) /*楽天スーパーDEAL商品受注フラグ*/
	, membership_program varchar(1) /*メンバーシッププログラム受注タイプ*/
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
	, buyer_name varchar(100)
	, buyer_phone_number varchar(13)
	, sku varchar(13)
	, product_name varchar(500)
	, quantity_purchased varchar(2)
	, quantity_shipped varchar(2)
	, quantity_to_ship varchar(2)
	, ship_service_level varchar(13)
	, recipient_name varchar(100)
	, ship_address1 varchar(100)
	, ship_address2 varchar(100)
	, ship_address3 varchar(100)
	, ship_city varchar(50)
	, ship_state varchar(50)
	, ship_postal_code varchar(10)
	, ship_country varchar(5)
	, payment_method varchar(20)
	, cod_collectible_amount varchar(20)
	, already_paid varchar(20)
	, payment_method_fee varchar(20)
	, scheduled_delivery_start_date varchar(2)
	, scheduled_delivery_end_date varchar(10)
	, points_granted varchar(20)
	, is_prime varchar(6)
	, delivery_company varchar(4) /*配送会社*/
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
, (4, '埼玉県', 'Saitama', 1001)
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
, (11, '佐賀県', 'Saga', 1002)
, (11, '長崎県', 'Nagasaki', 1003)
, (11, '熊本県', 'Kumamoto', 1003)
, (11, '大分県', 'Oita', 1003)
, (11, '宮崎県', 'Miyazaki', 1003)
, (11, '鹿児島県', 'Kagoshima', 1003)
, (12, '沖縄県', 'Okinawa', 1003);



/*置換する時、エラー発生の場合*/
drop table translation_err;
create table translation_err (
	seq_id bigint unsigned primary key  auto_increment /*区分ID*/
	, register_date datetime default now() /*データ登録日*/
	, trans_target_id bigint /*楽天・アマゾン区分ID*/
	, trans_target_type varchar(1) /*R:楽天・A:アマゾン*/
	, err_text varchar(3) /*エラーテキスト*/
) default charset = utf8;



/*YAMATO情報*/
drop table yamato_info;
create table yamato_info (
seq_id bigint unsigned primary key  auto_increment /*区分ID*/
, register_date datetime default now() /*データ登録日*/
, customer_no varchar(50) /*お客様管理番号
半角英数字50文字*/
, invoice_type varchar(1) /*送り状種類
半角数字1文字
 0 : 発払い
 2 : コレクト
 3 : ＤＭ便
 4 : タイム ※新規追加
 5 : 着払い ※新規追加
 7 : ネコポス
 8 : 宅急便コンパクト
 9 : 宅急便コンパクトコレクト

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)
*/
, cool_type varchar(1) /*クール区分
半角数字1文字
0または空白 : 通常
 1 : クール冷凍
 2 : クール冷蔵*/
, slip_no varchar(12) /*伝票番号
半角数字12文字

※B2クラウドにて付与*/
, estimate_ship_date varchar(10) /*出荷予定日
半角10文字
｢YYYY/MM/DD｣で入力してください。

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)


出荷予定日は本日～30日後までの範囲で指定して下さい。*/
, estimate_delivery_date varchar(10) /*お届け予定日
半角10文字
｢YYYY/MM/DD｣で入力してください。

※入力なしの場合、印字されません。
※「最短日」と入力可*/
, delivery_time varchar(4) /*配達時間帯
半角4文字
タイム、ＤＭ便以外
 空白 : 指定なし
 0812 : 午前中
 1416 : 14～16時
 1618 : 16～18時
 1820 : 18～20時
 1921 : 19～21時

タイム
 0010 : 午前10時まで
 0017 : 午後5時まで*/
, delivery_code varchar(20) /*お届け先コード
半角英数字20文字*/
, delivery_tel varchar(15) /*お届け先電話番号
半角数字15文字ハイフン含む

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)*/
, delivery_tel_branch varchar(2) /*お届け先電話番号枝番
半角数字2文字*/
, delivery_post_no varchar(8) /*お届け先郵便番号
半角数字8文字
ハイフンなし7文字も可

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)*/
, delivery_add varchar(64) /*お届け先住所
全角/半角
都道府県（４文字）
市区郡町村（１２文字）
町・番地（１６文字）

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)*/
, delivery_building varchar(32) /*お届け先アパートマンション名
全角/半角 
16文字/32文字 */
, delivery_company1 varchar(50) /*お届け先会社・部門１
全角/半角
25文字/50文字 */
, delivery_company2 varchar(50) /*お届け先会社・部門２
全角/半角 
25文字/50文字 */
, delivery_name varchar(32) /*お届け先名
全角/半角
16文字/32文字 

(※宅急便_必須項目)
(※ＤＭ便_必須項目)
(※ネコポス_必須項目)*/
, delivery_name_kana varchar(50) /*お届け先名(ｶﾅ)
半角カタカナ 50文字 */
, delivery_name_title varchar(4) /*敬称
全角/半角 2文字/4文字
ＤＭ便の場合に指定可能
【入力例】
様・御中・殿・行・係・宛・先生・なし*/
, client_code varchar(20) /*ご依頼主コード
半角英数字 20文字 */
, client_tel varchar(15) /*ご依頼主電話番号
半角数字15文字ハイフン含む

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, client_tel_branch varchar(2) /*ご依頼主電話番号枝番
半角数字 2文字 */
, client_post_no varchar(8) /*ご依頼主郵便番号
半角数字8文字
ハイフンなし半角7文字も可 

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, client_add varchar(64) /*ご依頼主住所
全角/半角32文字/64文字
都道府県（４文字）
市区郡町村（１２文字）
町・番地（１６文字）

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, client_building varchar(32) /*ご依頼主アパートマンション
全角/半角 16文字/32文字 */
, client_name varchar(32) /*ご依頼主名
全角/半角 16文字/32文字 

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, client_name_kana varchar(50) /*ご依頼主名(ｶﾅ)
半角カタカナ 50文字*/
, product_code1 varchar(30) /*品名コード１
半角英数字 30文字 */
, product_name1 varchar(50) /*品名１
全角/半角 25文字/50文字 

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, product_code2 varchar(30) /*品名コード２
半角英数字 30文字*/
, product_name2 varchar(50) /*品名２
全角/半角 25文字/50文字 */
, product_handle1 varchar(20) /*荷扱い１
全角/半角 10文字/20文字 



精密機器
ワレ物注意
下積厳禁
天地無用
ナマモノ*/
, product_handle2 varchar(20) /*荷扱い２
全角/半角 10文字/20文字 



精密機器
ワレ物注意
下積厳禁
天地無用
ナマモノ*/
, comment varchar(44) /*記事
全角/半角 22文字/44文字 */
, collect_cash varchar(7) /*ｺﾚｸﾄ代金引換額（税込)
半角数字 7文字

※コレクトの場合は必須
300,000円以下　1円以上
※但し、宅急便コンパクトコレクトの場合は
30,000円以下　　1円以上*/
, interval_consume_tax varchar(7) /*内消費税額等
半角数字 7文字

※コレクトの場合は必須 
※コレクト代金引換額（税込)以下*/
, stop_and_leave varchar(1) /*止置き
半角数字 1文字
0 : 利用しない
1 : 利用する */
, office_code varchar(6) /*営業所コード
半角数字 6文字

※止置きを利用する場合は必須 */
, issued_no varchar(2) /*発行枚数
半角数字 2文字

※発払いのみ指定可能*/
, show_no_flag varchar(1) /*個数口表示フラグ
半角数字 1文字
1 : 印字する
2 : 印字しない 
3 : 枠と口数を印字する

※宅急便コンパクト、宅急便コンパクトコレクトは対象外*/
, bill_customer_code varchar(12) /*請求先顧客コード
半角数字12文字

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, bill_class_code varchar(3) /*請求先分類コード
空白または半角数字3文字
*/
, fare_manage_no varchar(2) /*運賃管理番号
半角数字2文字

(※宅急便_必須項目)
(※ネコポス_必須項目)*/
, kuroneko_collect_register varchar(1) /*クロネコwebコレクトデータ登録
半角数字 1文字
0 : 無し
1 : 有り */
, kuroneko_collect_member_no varchar(9) /*クロネコwebコレクト加盟店番号
半角英数字 9文字 

※クロネコwebコレクトデータ有りの場合は必須 */
, kuroneko_collect_apply_no1 varchar(23) /*クロネコwebコレクト申込受付番号１
半角英数字 23文字

※クロネコwebコレクトデータ有りの場合は必須 */
, kuroneko_collect_apply_no2 varchar(23) /*クロネコwebコレクト申込受付番号２
半角英数字 23文字*/
, kuroneko_collect_apply_no3 varchar(23) /*クロネコwebコレクト申込受付番号３
半角英数字 23文字*/
, email_status_usage varchar(1) /*お届け予定ｅメール利用区分
半角数字 1文字
0 : 利用しない
1 : 利用する */
, email_status_add varchar(60) /*お届け予定ｅメールe-mailアドレス
半角英数字＆記号 60文字

※お届け予定eメールを利用する場合は必須 */
, input_type varchar(1) /*入力機種
半角数字 1文字
1 : ＰＣ
2 : 携帯電話

※お届け予定eメールを利用する場合は必須*/
, email_status_msg varchar(74) /*お届け予定ｅメールメッセージ
全角 74文字


※お届け予定eメールを利用する場合は必須*/
, email_complete_usage varchar(1) /*お届け完了ｅメール利用区分
半角数字 1文字
0 : 利用しない
1 : 利用する */
, email_complete_add varchar(60) /*お届け完了ｅメールe-mailアドレス
半角英数字 60文字

※お届け完了eメールを利用する場合は必須 */
, email_complete_msg varchar(159) /*お届け完了ｅメールメッセージ
全角 159文字 

※お届け完了eメールを利用する場合は必須 */
, kuroneko_substitute_usage varchar(1) /*クロネコ収納代行利用区分
半角数字１文字*/
, reserved_cal1 varchar(1) /*予備
半角数字１文字*/
, substitute_amount varchar(7) /*収納代行請求金額(税込)
半角数字７文字*/
, substitute_tax varchar(7) /*収納代行内消費税額等
半角数字７文字*/
, substitute_bill_post_no varchar(8) /*収納代行請求先郵便番号
半角数字＆ハイフン8文字*/
, substitute_bill_add varchar(64) /*収納代行請求先住所
全角/半角　32文字/64文字
都道府県（４文字）
市区郡町村（１２文字）
町・番地（１６文字）*/
, substitute_bill_building varchar(32) /*収納代行請求先住所（アパートマンション名）
全角/半角　16文字/32文字*/
, substitute_bill_company1 varchar(50) /*収納代行請求先会社・部門名１
全角/半角　25文字/50文字*/
, substitute_bill_company2 varchar(50) /*収納代行請求先会社・部門名２
全角/半角　25文字/50文字*/
, substitute_bill_name varchar(32) /*収納代行請求先名(漢字)
全角/半角　16文字/32文字*/
, substitute_bill_name_kana varchar(50) /*収納代行請求先名(カナ)
半角カタカナ50文字*/
, substitute_contact_name varchar(32) /*収納代行問合せ先名(漢字)
全角/半角　16文字/32文字*/
, substitute_contact_post_no varchar(8) /*収納代行問合せ先郵便番号
半角数字＆ハイフン8文字*/
, substitute_contact_add varchar(64) /*収納代行問合せ先住所
全角/半角　32文字/64文字
都道府県（４文字）
市区郡町村（１２文字）
町・番地（１６文字）*/
, substitute_contact_building varchar(32) /*収納代行問合せ先住所（アパートマンション名）
全角/半角　16文字/32文字*/
, substitute_contact_tel varchar(15) /*収納代行問合せ先電話番号
半角数字＆ハイフン15文字*/
, substitute_manage_no varchar(20) /*収納代行管理番号
半角英数字20文字*/
, substitute_product_name varchar(50) /*収納代行品名
全角/半角　25文字/50文字*/
, substitute_note varchar(28) /*収納代行備考
全角/半角　14文字/28文字*/
, multiple_key varchar(20) /*複数口くくりキー
半角英数字20文字

※「出荷予定個数」が2以上で「個数口枠の印字」で 「3 : 枠と口数を印字する」を選択し、且つ「複数口くくりキー」が空白の場合は、送り状発行時に「B2」という文言を自動補完する。

出荷予定個数:1*/
, search_key_title1 varchar(20) /*検索キータイトル1
全角/半角 
10文字/20文字 */
, search_key1 varchar(20) /*検索キー1
半角英数字
20文字*/
, search_key_title2 varchar(20) /*検索キータイトル2
全角/半角 
10文字/20文字 */
, search_key2 varchar(20) /*検索キー2
半角英数字
20文字*/
, search_key_title3 varchar(20) /*検索キータイトル3
全角/半角 
10文字/20文字 */
, search_key3 varchar(20) /*検索キー3
半角英数字
20文字*/
, search_key_title4 varchar(20) /*検索キータイトル4
全角/半角 
10文字/20文字 */
, search_key4 varchar(20) /*検索キー4
半角英数字
20文字*/
, search_key_title5 varchar(20) /*検索キータイトル5

※入力時は不要。出力時に自動反映。
※「ユーザーID」という文言を送り状発行時に固定で自動補完する。*/
, search_key5 varchar(20) /*検索キー5

※入力時は不要。出力時に自動反映。
※送り状発行時のユーザーIDを固定で自動補完する。*/
, reserved_cal2 varchar(50) /*予備*/
, reserved_cal3 varchar(50) /*予備*/
, posting_status_mail_usage varchar(1) /*投函予定メール利用区分
半角数字
1文字
0 : 利用しない
1 : 利用する PC宛て
2 : 利用する モバイル宛て*/
, posting_status_mail_add varchar(60) /*投函予定メールe-mailアドレス
半角英数字＆記号
60文字*/
, posting_status_mail_msg varchar(148) /*投函予定メールメッセージ
全角/半角
74文字/148文字

※半角カタカナ及び半角スペースは使えません。*/
, posting_delivery_complete_mail_usage varchar(1) /*投函完了メール（お届け先宛）利用区分
半角数字
1文字
0 : 利用しない
1 : 利用する PC宛て
2 : 利用する モバイル宛て*/
, posting_delivery_complete_mail_add varchar(60) /*投函完了メール（お届け先宛）e-mailアドレス
半角英数字＆記号
60文字*/
, posting_delivery_complete_mail_msg varchar(318) /*投函完了メール（お届け先宛）メールメッセージ
全角/半角
159文字/318文字

※半角カタカナ及び半角スペースは使えません。*/
, posting_client_complete_mail_usage varchar(1) /*投函完了メール（ご依頼主宛）利用区分
半角数字
1文字
0 : 利用しない
1 : 利用する PC宛て
2 : 利用する モバイル宛て*/
, posting_client_complete_mail_add varchar(60) /*投函完了メール（ご依頼主宛）e-mailアドレス
半角英数字＆記号
60文字*/
, posting_client_complete_mail_msg varchar(318) /*投函完了メール（ご依頼主宛）メールメッセージ
全角/半角
159文字/318文字

※半角カタカナ及び半角スペースは使えません。*/
) default charset = utf8;




/*SAGAWA情報*/
drop table sagawa_info;
create table sagawa_info (
seq_id bigint unsigned primary key  auto_increment /*区分ID*/
, register_date datetime default now() /*データ登録日*/
, add_book_code varchar(12) /*住所録コード*/
, delivery_tel varchar(14) /*お届け先電話番号*/
, delivery_post_no varchar(8) /*お届け先郵便番号*/
, delivery_add1 varchar(16) /*お届け先住所１（必須）*/
, delivery_add2 varchar(16) /*お届け先住所２*/
, delivery_add3 varchar(16) /*お届け先住所３*/
, delivery_name1 varchar(16) /*お届け先名称１（必須）*/
, delivery_name2 varchar(16) /*お届け先名称２*/
, client_manage_no varchar(16) /*お客様管理ナンバー*/
, client_code varchar(12) /*お客様コード*/
, transport_dept_charge varchar(16) /*部署・担当者*/
, transport_tel varchar(14) /*荷送人電話番号*/
, client_tel varchar(14) /*ご依頼主電話番号*/
, client_post_no varchar(8) /*ご依頼主郵便番号*/
, client_add1 varchar(16) /*ご依頼主住所１*/
, client_add2 varchar(16) /*ご依頼主住所２*/
, client_name1 varchar(16) /*ご依頼主名称１*/
, client_name2 varchar(16) /*ご依頼主名称２*/
, transport_code varchar(3) /*荷姿コード*/
, product_name1 varchar(32) /*品名１*/
, product_name2 varchar(32) /*品名２*/
, product_name3 varchar(32) /*品名３*/
, product_name4 varchar(32) /*品名４*/
, product_name5 varchar(32) /*品名５*/
, delivery_cnt varchar(3) /*出荷個数*/
, speed_type varchar(3) /*便種（スピードで選択）*/
, product_type varchar(3) /*便種（商品）*/
, delivery_date varchar(8) /*配達日*/
, delivery_time varchar(2) /*配達指定時間帯*/
, delivery_time_detail varchar(4) /*配達指定時間（時分）*/
, total_amt varchar(7) /*代引金額*/
, consume_tax varchar(6) /*消費税*/
, payment_method varchar(1) /*決済種別*/
, insurance_amt varchar(8) /*保険金額*/
, insurance_status varchar(1) /*保険金額印字*/
, select_seal1 varchar(3) /*指定シール①*/
, select_seal2 varchar(3) /*指定シール②*/
, select_seal3 varchar(3) /*指定シール③*/
, store_stop varchar(1) /*営業店止め*/
, src_type varchar(1) /*ＳＲＣ区分*/
, store_code varchar(4) /*営業店コード*/
, origin_type varchar(1) /*元着区分*/
) default charset = utf8;
















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



/*라쿠텐 데이터 등록일 최신화*/
update rakuten_info
set register_date = now()




/*아마존 데이터 등록일 최신화*/
update amazon_info
set register_date = now()





select tr.seq_id , date_format(tr.register_date, '%Y/%m/%d') register_date 
, result_text , order_no , product_name , product_option 
, unit_no , delivery_surname , delivery_name , delivery_company 
, ri.delivery_address1, ri.delivery_address2, ri.delivery_address3

from translation_result tr 
inner join rakuten_info ri on ri.seq_id = tr.trans_target_id 
WHERE tr.seq_id in ( 1,2,3,4,5 ) 




select 
	ai.seq_id
	, date_format(ai.register_date, '%Y/%m/%d') register_date
	, date_format(ai.update_date, '%Y/%m/%d') update_date
	, order_id
	, order_item_id
	, purchase_date
	, payments_date
	, reporting_date
	, promise_date
	, days_past_promise
	, buyer_email
	, buyer_name
	, buyer_phone_number
	, sku
	, product_name
	, quantity_purchased
	, quantity_shipped
	, quantity_to_ship
	, ship_service_level
	, recipient_name
	, ship_address1
	, ship_address2
	, ship_address3
	, ship_city
	, ship_state
	, ship_postal_code
	, ship_country
	, payment_method
	, cod_collectible_amount
	, already_paid
	, payment_method_fee
	, scheduled_delivery_start_date
	, scheduled_delivery_end_date
	, points_granted
	, is_prime
	, err_text
from
	amazon_info ai
left outer join translation_err terr on terr.trans_target_id = ai.seq_id and trans_target_type = 'A'
order by ai.seq_id asc


select ri.seq_id , err_text , order_no 
, order_status , delivery_date_sel , total_amt , baggage_claim_no 
, delivery_surname , delivery_name , delivery_surname_kana 
, delivery_name_kana , delivery_tel1 , delivery_tel2 , delivery_tel3 
, delivery_address1 , product_id , product_name , product_option , unit_no 
, tomorrow_hope , date_format(ri.register_date, '%Y/%m/%d') register_date 
, date_format(ri.update_date, '%Y/%m/%d') update_date 
from rakuten_info ri 
left outer join translation_err terr on terr.trans_target_id = ri.seq_id and terr.trans_target_type = 'R' 
WHERE ri.register_date between str_to_date('2018/09/28 00:00:00', '%Y/%m/%d %H:%i:%s') and now() 
order by ri.seq_id asc



select tr.seq_id , date_format(tr.register_date, '%Y/%m/%d') register_date , err_text 
, result_text , order_no , product_name , product_option , unit_no 
, delivery_surname , delivery_name , delivery_company 
from translation_result tr 
inner join rakuten_info ri on ri.seq_id = tr.trans_target_id and tr.trans_target_type = 'R' 
left outer join translation_err terr on terr.trans_target_id = ri.seq_id and terr.trans_target_type = 'R' 
WHERE tr.seq_id in ( 1 , 2 , 9 , 10 ) 

/*
 * 새로운 라쿠텐 테이블을 생성하였으므로
 * 기존 데이터를 삭제하는 쿼리를 작성
 * */
/*select * */
delete
from translation_result
where trans_target_type = 'R'

/*select * */
delete
from translation_err
where trans_target_type = 'R'
