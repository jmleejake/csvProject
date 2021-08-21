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

DROP TABLE amazon_info;
CREATE TABLE amazon_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	order_id VARCHAR(20),
	order_item_id VARCHAR(14),
	purchase_date VARCHAR(30),
	payments_date VARCHAR(30),
	reporting_date VARCHAR(30),
	promise_date VARCHAR(30),
	days_past_promise VARCHAR(10),
	buyer_email VARCHAR(50),
	buyer_name VARCHAR(100),
	buyer_phone_number VARCHAR(13),
	sku VARCHAR(13),
	product_name VARCHAR(500),
	quantity_purchased VARCHAR(10),
	quantity_shipped VARCHAR(10),
	quantity_to_ship VARCHAR(10),
	ship_service_level VARCHAR(13),
	recipient_name VARCHAR(100),
	ship_address1 VARCHAR(100),
	ship_address2 VARCHAR(100),
	ship_address3 VARCHAR(100),
	ship_city VARCHAR(50),
	ship_state VARCHAR(50),
	ship_postal_code VARCHAR(10),
	ship_country VARCHAR(5),
	payment_method VARCHAR(20),
	cod_collectible_amount VARCHAR(20),
	already_paid VARCHAR(20),
	payment_method_fee VARCHAR(20),
	scheduled_delivery_start_date VARCHAR(10),
	scheduled_delivery_end_date VARCHAR(10),
	points_granted VARCHAR(20),
	is_prime VARCHAR(6),
	delivery_company VARCHAR(4) comment '配送会社',
	baggage_claim_no VARCHAR(12) comment 'お荷物伝票番号'
) default charset = utf8 comment 'AMAZON情報';

CREATE INDEX idx_amazon_upd_date ON amazon_info (update_date ASC);

CREATE INDEX idx_amazon_company_no ON amazon_info (delivery_company ASC);

CREATE INDEX idx_amazon_reg_date ON amazon_info (register_date ASC);

CREATE INDEX idx_amazon_order_id ON amazon_info (order_id ASC);


DROP TABLE dealer_info;
CREATE TABLE dealer_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	dealer_id VARCHAR(20),
	dealer_nm VARCHAR(100)
) default charset = utf8 comment '取引先情報';


DROP TABLE exception_master;
CREATE TABLE exception_master (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	exception_data VARCHAR(20)
) default charset = utf8 comment '例外マスタ';

alter table exception_master add column update_date DATETIME comment '修正日付';


DROP TABLE exception_region_master;
CREATE TABLE exception_region_master (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	exception_data VARCHAR(20) comment '例外地域'
) default charset = utf8 comment '例外地域マスタ';

drop table region_master;
create table region_master (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	p_id int comment '地域ID',
	region_name varchar(8) comment '名称',
	region_name_en varchar(15) comment '名称(英語)',
	delivery_company varchar(4) comment '配送会社'
) default charset = utf8 comment '地域区分コードマスタ';

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


DROP TABLE fare_info;
CREATE TABLE fare_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	ichiikiname VARCHAR(20),
	yamato VARCHAR(4),
	sagawa VARCHAR(4),
	yubinkyoku VARCHAR(4),
	boxsize VARCHAR(3),
	yasendvalue VARCHAR(4),
	sasendvalue VARCHAR(4),
	yusendvalue VARCHAR(4),
	nekoposu VARCHAR(3),
	clickpost VARCHAR(3),
	dmbin VARCHAR(3)
) default charset = utf8 comment '料金情報';


DROP TABLE items_info;
CREATE TABLE items_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	ctrl_col VARCHAR(10),
	item_url VARCHAR(30),
	item_num VARCHAR(30),
	directory_id VARCHAR(20),
	tag_id VARCHAR(10),
	pc_catch_cp VARCHAR(200),
	mobile_catch_cp VARCHAR(200),
	item_name VARCHAR(1500),
	sell_price VARCHAR(10),
	display_price VARCHAR(10),
	consume_tax VARCHAR(10),
	shipping_cost VARCHAR(10),
	individual_shipping_cost VARCHAR(10),
	shipping_category1 VARCHAR(10),
	shipping_category2 VARCHAR(10),
	commission_fee VARCHAR(10),
	select_warehouse VARCHAR(10),
	item_info_layout VARCHAR(10),
	order_btn VARCHAR(10),
	material_req_btn VARCHAR(10),
	item_inquiry_btn VARCHAR(10),
	notice_btn VARCHAR(10),
	nosi_resp VARCHAR(10),
	desc_pc_item MEDIUMTEXT,
	desc_phone_item MEDIUMTEXT,
	desc_pc_sales MEDIUMTEXT,
	item_image_url VARCHAR(200),
	item_image_name VARCHAR(10),
	video_name VARCHAR(10),
	select_sales_period VARCHAR(10),
	order_num VARCHAR(10),
	inven_type VARCHAR(10),
	inven_num VARCHAR(10),
	inven_num_view VARCHAR(10),
	item_horizon_name VARCHAR(10),
	item_vertical_name VARCHAR(10),
	item_inven_view VARCHAR(10),
	search_type VARCHAR(10),
	pw_black_market VARCHAR(10),
	catalog_id VARCHAR(200),
	back_stock_flag VARCHAR(10),
	out_stock_order VARCHAR(10),
	in_stock_delivery_num VARCHAR(10),
	out_stock_delivery_num VARCHAR(10),
	reserved_item_date VARCHAR(10),
	percent_point_var VARCHAR(10),
	period_point_apply VARCHAR(10),
	header_footer_leftnavi VARCHAR(10),
	order_display_item VARCHAR(10),
	desc_common_small VARCHAR(10),
	item_eye_catch VARCHAR(10),
	desc_common_big VARCHAR(10),
	display_review VARCHAR(10),
	manage_num_tomorrow_delivery VARCHAR(10),
	manage_num_oversea_delivery VARCHAR(10),
	link_size_tbl VARCHAR(10),
	manage_num_dbl_price VARCHAR(10),
	reason_no_catalog_id VARCHAR(10),
	manage_num_shipping_method VARCHAR(10),
	white_bg_image_url VARCHAR(200),
	display_maker_info VARCHAR(10)
) default charset = utf8 comment 'アイテム情報';


DROP TABLE prd_info;
CREATE TABLE prd_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	prd_cd VARCHAR(100),
	jan_cd VARCHAR(100),
	prd_nm VARCHAR(500),
	prd_dtl VARCHAR(500),
	prd_cnt VARCHAR(3),
	order_no VARCHAR(3)
) default charset = utf8 comment '商品情報';


DROP TABLE prdcd_master;
CREATE TABLE prdcd_master (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	prd_cd VARCHAR(100),
	target_type VARCHAR(3)
) default charset = utf8 comment '商品コード情報';


DROP TABLE product_analysis;
CREATE TABLE product_analysis (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	memo VARCHAR(100),
	prd_nm VARCHAR(1000),
	sku VARCHAR(13),
	url VARCHAR(500),
	prd_config VARCHAR(3),
	prd_price VARCHAR(30),
	prd_pkg VARCHAR(30),
	ship_cost VARCHAR(30),
	add_ship_cost VARCHAR(30),
	sales_comm_ratio VARCHAR(30),
	sales_price VARCHAR(30),
	etc VARCHAR(1000),
	ttl_price VARCHAR(30),
	sales_comm_price VARCHAR(30),
	benefit VARCHAR(30),
	benefit_ratio VARCHAR(30)
) default charset = utf8 comment '商品分析情報';


DROP TABLE q10_info;
CREATE TABLE q10_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	delivery_sts VARCHAR(10),
	order_no VARCHAR(15),
	cart_no VARCHAR(15),
	delivery_company_q10 VARCHAR(50),
	invoice_no VARCHAR(15),
	ship_date VARCHAR(10),
	order_date VARCHAR(20),
	pay_date VARCHAR(20),
	delivery_date VARCHAR(20),
	ship_eta VARCHAR(20),
	delivery_atd VARCHAR(20),
	ship_method VARCHAR(50),
	item_no VARCHAR(15),
	product_name VARCHAR(1500),
	qty VARCHAR(5),
	option_info VARCHAR(1500),
	option_cd VARCHAR(15),
	bonus VARCHAR(15),
	recpt_name VARCHAR(30),
	recpt_name_kana VARCHAR(30),
	recpt_phone_no VARCHAR(15),
	recpt_mobile_no VARCHAR(15),
	address VARCHAR(150),
	post_no VARCHAR(10),
	country_name VARCHAR(30),
	ship_pay VARCHAR(4),
	pay_site VARCHAR(4),
	currency_type VARCHAR(4),
	pay_amt VARCHAR(10),
	sel_price VARCHAR(10),
	disc_price VARCHAR(10),
	total_price VARCHAR(10),
	total_supply VARCHAR(10),
	pay_name VARCHAR(30),
	pay_name_kana VARCHAR(30),
	delivery_item VARCHAR(500),
	pay_phone_no VARCHAR(15),
	pay_mobile_no VARCHAR(15),
	product_cd VARCHAR(15),
	jan_cd VARCHAR(15),
	std_no VARCHAR(15),
	presenter VARCHAR(30),
	ads_no_site VARCHAR(30),
	material_type VARCHAR(50),
	delivery_company VARCHAR(4) comment '配送会社'
) default charset = utf8 comment 'Q10情報';

alter table q10_info modify column product_cd VARCHAR(50);


DROP TABLE rakuten_frozen_info;
CREATE TABLE rakuten_frozen_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	delivery_company VARCHAR(4) comment '配送会社',
	baggage_claim_no VARCHAR(12) comment 'お荷物伝票番号',
	order_no VARCHAR(25),
	order_status VARCHAR(10),
	sub_status_id VARCHAR(10),
	sub_status VARCHAR(10),
	order_datetime VARCHAR(20),
	order_date VARCHAR(10),
	order_time VARCHAR(20),
	cancel_due_date VARCHAR(10),
	order_check_datetime VARCHAR(20),
	order_confirm_datetime VARCHAR(20),
	delivery_eta_datetime VARCHAR(20),
	delivery_ata_datetime VARCHAR(20),
	pay_method_name VARCHAR(50),
	creadit_pay_method VARCHAR(50),
	credit_pay_times VARCHAR(2),
	delivery_method VARCHAR(10),
	delivery_type VARCHAR(10),
	order_type VARCHAR(20),
	multi_destination_flag VARCHAR(1),
	destination_match_flag VARCHAR(1),
	island_flag VARCHAR(1),
	rverify_flag VARCHAR(1),
	warning_type VARCHAR(10),
	rmember_flag VARCHAR(1),
	purchase_hist_mod_flag VARCHAR(1),
	total_goods_amt VARCHAR(10),
	total_consume_tax VARCHAR(10),
	total_shipping VARCHAR(10),
	gross_deduction VARCHAR(10),
	invoice_amt VARCHAR(10),
	total_amt VARCHAR(10),
	point_usage VARCHAR(10),
	total_coupon_usage VARCHAR(10),
	store_coupon_usage VARCHAR(10),
	rakuten_coupon_usage VARCHAR(10),
	order_post_no1 VARCHAR(4),
	order_post_no2 VARCHAR(4),
	order_add1 VARCHAR(20),
	order_add2 VARCHAR(20),
	order_add3 VARCHAR(50),
	order_surname VARCHAR(200),
	order_name VARCHAR(200),
	order_surname_kana VARCHAR(200),
	order_name_kana VARCHAR(200),
	order_tel1 VARCHAR(4),
	order_tel2 VARCHAR(4),
	order_tel3 VARCHAR(4),
	order_email VARCHAR(60),
	order_sex VARCHAR(3),
	request_no VARCHAR(10),
	request_received_no VARCHAR(10),
	ship_id VARCHAR(10),
	ship_charge VARCHAR(10),
	ship_substitute_fee VARCHAR(10),
	ship_total_consume_tax VARCHAR(10),
	ship_total_goods_amt VARCHAR(10),
	ship_total_amt VARCHAR(10),
	indicates VARCHAR(10),
	delivery_post_no1 VARCHAR(4),
	delivery_post_no2 VARCHAR(4),
	delivery_add1 VARCHAR(20),
	delivery_add2 VARCHAR(20),
	delivery_add3 VARCHAR(50),
	delivery_surname VARCHAR(200),
	delivery_name VARCHAR(200),
	delivery_surname_kana VARCHAR(200),
	delivery_name_kana VARCHAR(200),
	delivery_tel1 VARCHAR(4),
	delivery_tel2 VARCHAR(4),
	delivery_tel3 VARCHAR(4),
	product_detail_id VARCHAR(10),
	product_id VARCHAR(8),
	product_name VARCHAR(1500),
	product_no VARCHAR(30),
	product_manage_no VARCHAR(100),
	unit_price VARCHAR(10),
	unit_no VARCHAR(10),
	delivery_cost_include VARCHAR(10),
	tax_exclude VARCHAR(10),
	substitute_fee_include VARCHAR(10),
	product_option VARCHAR(1500),
	point_multiple VARCHAR(10),
	delivery_info VARCHAR(200),
	inventory_type VARCHAR(10),
	wrap_title1 VARCHAR(10),
	wrap_name1 VARCHAR(10),
	wrap_amt1 VARCHAR(10),
	wrap_tax_include1 VARCHAR(10),
	wrap_type1 VARCHAR(10),
	wrap_title2 VARCHAR(10),
	wrap_name2 VARCHAR(10),
	wrap_amt2 VARCHAR(10),
	wrap_tax_include2 VARCHAR(10),
	wrap_type2 VARCHAR(10),
	delivery_time VARCHAR(10),
	delivery_date_sel VARCHAR(10),
	manager VARCHAR(10),
	quick_note VARCHAR(100),
	msg_to_customer VARCHAR(200),
	gift_request VARCHAR(10),
	comment VARCHAR(300),
	util_terminal VARCHAR(10),
	mail_carrier_code VARCHAR(10),
	tomorrow_hope VARCHAR(1),
	drug_order_flag VARCHAR(1),
	rakuten_super_deal VARCHAR(1),
	membership_program VARCHAR(1)
) default charset = utf8 comment '楽天中腹データ情報';


DROP TABLE rakuten_info;
CREATE TABLE rakuten_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	delivery_company VARCHAR(4) comment '配送会社',
	baggage_claim_no VARCHAR(12) comment 'お荷物伝票番号',
	order_no VARCHAR(25),
	order_status VARCHAR(10),
	sub_status_id VARCHAR(10),
	sub_status VARCHAR(10),
	order_datetime VARCHAR(20),
	order_date VARCHAR(10),
	order_time VARCHAR(20),
	cancel_due_date VARCHAR(10),
	order_check_datetime VARCHAR(20),
	order_confirm_datetime VARCHAR(20),
	delivery_eta_datetime VARCHAR(20),
	delivery_ata_datetime VARCHAR(20),
	pay_method_name VARCHAR(50),
	creadit_pay_method VARCHAR(50),
	credit_pay_times VARCHAR(2),
	delivery_method VARCHAR(10),
	delivery_type VARCHAR(10),
	order_type VARCHAR(20),
	multi_destination_flag VARCHAR(1),
	destination_match_flag VARCHAR(1),
	island_flag VARCHAR(1),
	rverify_flag VARCHAR(1),
	warning_type VARCHAR(10),
	rmember_flag VARCHAR(1),
	purchase_hist_mod_flag VARCHAR(1),
	total_goods_amt VARCHAR(10),
	total_consume_tax VARCHAR(10),
	total_shipping VARCHAR(10),
	gross_deduction VARCHAR(10),
	invoice_amt VARCHAR(10),
	total_amt VARCHAR(10),
	point_usage VARCHAR(10),
	total_coupon_usage VARCHAR(10),
	store_coupon_usage VARCHAR(10),
	rakuten_coupon_usage VARCHAR(10),
	order_post_no1 VARCHAR(4),
	order_post_no2 VARCHAR(4),
	order_add1 VARCHAR(20),
	order_add2 VARCHAR(20),
	order_add3 VARCHAR(50),
	order_surname VARCHAR(200),
	order_name VARCHAR(200),
	order_surname_kana VARCHAR(200),
	order_name_kana VARCHAR(200),
	order_tel1 VARCHAR(4),
	order_tel2 VARCHAR(4),
	order_tel3 VARCHAR(4),
	order_email VARCHAR(60),
	order_sex VARCHAR(3),
	request_no VARCHAR(10),
	request_received_no VARCHAR(10),
	ship_id VARCHAR(10),
	ship_charge VARCHAR(10),
	ship_substitute_fee VARCHAR(10),
	ship_total_consume_tax VARCHAR(10),
	ship_total_goods_amt VARCHAR(10),
	ship_total_amt VARCHAR(10),
	indicates VARCHAR(10),
	delivery_post_no1 VARCHAR(4),
	delivery_post_no2 VARCHAR(4),
	delivery_add1 VARCHAR(20),
	delivery_add2 VARCHAR(20),
	delivery_add3 VARCHAR(50),
	delivery_surname VARCHAR(200),
	delivery_name VARCHAR(200),
	delivery_surname_kana VARCHAR(200),
	delivery_name_kana VARCHAR(200),
	delivery_tel1 VARCHAR(4),
	delivery_tel2 VARCHAR(4),
	delivery_tel3 VARCHAR(4),
	product_detail_id VARCHAR(10),
	product_id VARCHAR(8),
	product_name VARCHAR(1500),
	product_no VARCHAR(30),
	product_manage_no VARCHAR(100),
	unit_price VARCHAR(10),
	unit_no VARCHAR(10),
	delivery_cost_include VARCHAR(10),
	tax_exclude VARCHAR(10),
	substitute_fee_include VARCHAR(10),
	product_option VARCHAR(1500),
	point_multiple VARCHAR(10),
	delivery_info VARCHAR(200),
	inventory_type VARCHAR(10),
	wrap_title1 VARCHAR(10),
	wrap_name1 VARCHAR(10),
	wrap_amt1 VARCHAR(10),
	wrap_tax_include1 VARCHAR(10),
	wrap_type1 VARCHAR(10),
	wrap_title2 VARCHAR(10),
	wrap_name2 VARCHAR(10),
	wrap_amt2 VARCHAR(10),
	wrap_tax_include2 VARCHAR(10),
	wrap_type2 VARCHAR(10),
	delivery_time VARCHAR(10),
	delivery_date_sel VARCHAR(10),
	manager VARCHAR(10),
	quick_note VARCHAR(100),
	msg_to_customer VARCHAR(200),
	gift_request VARCHAR(10),
	comment VARCHAR(300),
	util_terminal VARCHAR(10),
	mail_carrier_code VARCHAR(10),
	tomorrow_hope VARCHAR(1),
	drug_order_flag VARCHAR(1),
	rakuten_super_deal VARCHAR(1),
	membership_program VARCHAR(1)
) default charset = utf8 comment '楽天情報';

CREATE INDEX idx_raku_order_no ON rakuten_info (order_no ASC);

CREATE INDEX idx_raku_baggage_no ON rakuten_info (baggage_claim_no ASC);

CREATE INDEX idx_raku_upd_date ON rakuten_info (update_date ASC);

CREATE INDEX idx_raku_surname ON rakuten_info (delivery_surname_kana ASC);

CREATE INDEX idx_raku_company_no ON rakuten_info (delivery_company ASC);

CREATE INDEX idx_raku_surname ON rakuten_info (delivery_surname ASC);

CREATE INDEX idx_raku_reg_date ON rakuten_info (register_date ASC);

CREATE INDEX idx_raku_name ON rakuten_info (delivery_name_kana ASC);

CREATE INDEX idx_raku_name ON rakuten_info (delivery_name ASC);

DROP TABLE stock_info;
CREATE TABLE stock_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	dealer_id VARCHAR(20),
	prd_nm VARCHAR(500),
	receive_date DATETIME,
	jan_cd VARCHAR(100),
	prd_cnt VARCHAR(3)
) default charset = utf8 comment '在庫情報';


DROP TABLE tanpin_info;
CREATE TABLE tanpin_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	maker_cd VARCHAR(20),
	maker_nm VARCHAR(100),
	prd_cd VARCHAR(20),
	prd_nm VARCHAR(100),
	capacity VARCHAR(10),
	inprice VARCHAR(6),
	price VARCHAR(6),
	std_price VARCHAR(6),
	tax_inc VARCHAR(10),
	tax_rt VARCHAR(2),
	dealer_id VARCHAR(20),
	dealer_nm VARCHAR(100)
) default charset = utf8 comment '単品情報';


DROP TABLE translation_err;
CREATE TABLE translation_err (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	trans_target_id BIGINT,
	trans_target_type VARCHAR(3),
	err_text VARCHAR(3)
) default charset = utf8 comment '置換エラー情報';


DROP TABLE translation_info;
CREATE TABLE translation_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	before_trans VARCHAR(3000),
	after_trans VARCHAR(50)
) default charset = utf8 comment '置換情報';


DROP TABLE translation_result;
CREATE TABLE translation_result (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	trans_target_id BIGINT,
	trans_target_type VARCHAR(3),
	result_text VARCHAR(200)
) default charset = utf8 comment '置換結果';


DROP TABLE yahoo_info;
CREATE TABLE yahoo_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID',
	register_date datetime default now() comment '登録日付',
	update_date DATETIME comment '修正日付',
	order_id VARCHAR(10),
	line_id VARCHAR(5),
	title VARCHAR(1500),
	qty VARCHAR(5),
	id VARCHAR(100),
	item_id VARCHAR(100),
	item_option_val VARCHAR(1500),
	lead_time_txt VARCHAR(500),
	item_tax_ratio VARCHAR(5),
	order_time VARCHAR(20),
	order_status VARCHAR(30),
	ship_nm VARCHAR(30),
	ship_fst_nm VARCHAR(30),
	ship_last_nm VARCHAR(30),
	ship_add1 VARCHAR(100),
	ship_add2 VARCHAR(100),
	ship_city VARCHAR(100),
	ship_pre VARCHAR(100),
	ship_post_no VARCHAR(10),
	ship_nm_kana VARCHAR(100),
	ship_fst_nm_kana VARCHAR(100),
	ship_last_nm_kana VARCHAR(100),
	ship_phone_no VARCHAR(11),
	ship_req_dt VARCHAR(10),
	ship_req_time VARCHAR(20),
	ship_company_cd VARCHAR(5),
	ship_ivc_no1 VARCHAR(10),
	ship_ivc_no2 VARCHAR(10)
) default charset = utf8 comment 'ヤフー情報';


DROP TABLE prd_trans_info;
CREATE TABLE prd_trans_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
	, order_no VARCHAR(25) comment '注文番号'
	, jan_cd varchar(15) comment 'ＪＡＮコード'
	, before_trans VARCHAR(3000) comment '置換前'
	, after_trans VARCHAR(50) comment '置換後'
	, order_gbn varchar(3) comment '区分'
	, prd_master_hanei_gbn varchar(2) comment '商品マスタ反映有無フラグ：0（未）、1（済）'
	, prd_cnt varchar(4) comment '商品数'
  	, trans_target_type varchar(3) comment 'メニュー区分'
) default charset = utf8 comment '商品中間マスタ';

alter table translation_info add column prd_cnt varchar(4) comment '商品数';

alter table translation_info add column etc_cntnt varchar(30) comment 'その他';

alter table translation_info add column jan_cd varchar(15) comment 'ＪＡＮコード';

DROP TABLE order_sum_info;
CREATE TABLE order_sum_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
	, jan_cd varchar(15) comment 'ＪＡＮコード'
	, after_trans VARCHAR(50) comment '置換後'
	, prd_sum varchar(10) comment '総商品数'
	, prd_master_hanei_gbn varchar(2) comment '商品マスタ反映有無フラグ：0（未）、1（済）'
	, target_type varchar(3) comment 'メニュー区分'
) default charset = utf8 comment '総商品数';



DROP TABLE etc_master_info;
CREATE TABLE etc_master_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
	, etc_key varchar(100) comment 'その他キー'
	, jan_cd varchar(15) comment 'ＪＡＮコード'
	, prd_cnt varchar(4) comment '商品数'
	, prd_nm VARCHAR(3000) comment '商品名'
	, target_type varchar(3) comment 'メニュー区分'
) default charset = utf8 comment 'その他マスタ';


DROP TABLE translation_sub_info;
CREATE TABLE translation_sub_info (
	seq_id bigint unsigned primary key auto_increment comment '区分ID'
	, parent_seq_id bigint unsigned comment '親区分ID'
	, before_trans VARCHAR(3000) comment '置換前値'
	, after_trans VARCHAR(50) comment '置換後値'
	, jan_cd varchar(15) comment 'ＪＡＮコード'
	, prd_cnt varchar(4) comment '商品数'
	, register_date datetime default now() comment '登録日付'
	, update_date DATETIME comment '修正日付'
) default charset = utf8 comment '置換サーブ情報';
