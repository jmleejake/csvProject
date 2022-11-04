drop table jaiko_user_info;
create table jaiko_user_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, user_id varchar(20) comment 'ユーザーID'
, user_pass varchar(20) comment 'パスワード'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
, last_login_dt datetime default now() comment '最近ローグイン日付'
) default charset = utf8 comment 'ユーザー情報';

drop table jaiko_warehouse_info;
create table jaiko_warehouse_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, partner_id varchar(20) comment '取引先'
, partner_nm varchar(100) comment '取引先名'
, prd_cd varchar(20) comment '商品コード'
, brand_nm varchar(20) comment 'ブランド'
, prd_nm varchar(1000) comment '商品名'
, warehouse_dt datetime comment '入庫日(入庫時に設定する。)'
, delivery_dt datetime comment '出庫日(出庫時に設定する。)'
, jan_cd varchar(15) comment 'ＪＡＮコード'
, prd_unit_prc varchar(20) comment '商品単価'
, prd_cnt varchar(3) comment '商品入数'
, prd_quantity varchar(5) comment '商品数量'
, prd_total varchar(10) comment '商品合計'
, dsku varchar(7) comment 'ＳＫＵ'
, dasin varchar(7) comment 'ＡＳＩＮ'
, line_unit_no varchar(2) comment 'ライン数'
, order_unit_no varchar(3) comment '注文数'
, tax_incld varchar(12) comment '商品税(抜、込)'
, tax_rt varchar(2) comment '商品税率'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '入出庫在庫';


alter table jaiko_warehouse_info add column tantou_id varchar(20) comment '担当者ID';
alter table jaiko_warehouse_info add column tantou_nm varchar(100) comment '担当者名';
alter table jaiko_warehouse_info add column prd_unit char(1) default '1' comment '単位';
alter table jaiko_warehouse_info add column ware_loc char(1) default '1' comment '場所';


drop table jaiko_prd_info;
create table jaiko_prd_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, partner_id varchar(20) comment '取引先'
, partner_nm varchar(100) comment '取引先名'
, prd_cd varchar(20) comment '商品コード'
, brand_nm varchar(20) comment 'ブランド'
, prd_nm varchar(1000) comment '商品名'
, jan_cd varchar(15) comment 'ＪＡＮコード'
, prd_cnt varchar(3) comment '商品入数'
, prd_unit_prc varchar(20) comment '商品単価'
, dsku varchar(7) comment 'ＳＫＵ'
, dasin varchar(7) comment 'ＡＳＩＮ'
, tax_incld varchar(12) comment '商品税(抜、込)'
, tax_rt varchar(2) comment '商品税率'
, std_info varchar(30) comment '規格'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '商品情報';


alter table jaiko_prd_info add column jan_cd1 varchar(15) comment 'JANコード1(単品)';
alter table jaiko_prd_info add column prd_cnt1 varchar(15) comment '商品数1';
alter table jaiko_prd_info add column jan_cd2 varchar(15) comment 'JANコード2(中数)';
alter table jaiko_prd_info add column prd_cnt2 varchar(15) comment '商品数2';
alter table jaiko_prd_info add column jan_cd3 varchar(15) comment 'JANコード3(箱)';
alter table jaiko_prd_info add column prd_cnt3 varchar(15) comment '商品数3';


drop table jaiko_invoice_info;
create table jaiko_invoice_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, denpyou_no varchar(20) comment '伝票No.'
, partner_id varchar(20) comment '取引先No.'
, partner_nm varchar(100) comment '取引先名'
, tantou_nm varchar(20) comment '担当者'
, company_nm varchar(20) comment '会社名(販売者情報)'
, company_add varchar(200) comment '会社住所(販売者情報)'
, company_tel varchar(20) comment '会社電話(販売者情報)'
, company_fax varchar(20) comment '会社FAX(販売者情報)'
, bank_transfer_info varchar(500) comment '振込先(築地銀行／本店営業部／普通)'
, brand_nm varchar(20) comment 'ブランド'
, prd_nm varchar(1000) comment '商品名'
, prd_spec varchar(20) comment '規格'
, prd_cd varchar(20) comment '品コード'
, prd_cnt varchar(3) comment '入数'
, prd_quantity varchar(5) comment '数量'
, prd_indiv_cnt varchar(3) comment '個数'
, prd_unit_prc varchar(20) comment '単価'
, prd_total_prc varchar(20) comment '金額'
, tax_rt varchar(2) comment '税率'
, tax_rt_symb varchar(2) comment '税率記号'
, ref_prc varchar(20) comment '参考売価'
, ref_spec varchar(15) comment '摘要(JANコード記載)'
, buy_amt varchar(20) comment 'お買上額計'
, comsump_tax varchar(20) comment '消費税額'
, tax_incld_amt varchar(20) comment '税込合計額'
, prev_remain_amt varchar(20) comment '前回残金額'
, total_amt varchar(20) comment 'お会計金額'
, carry_balance_amt varchar(20) comment '繰越残高'
, etc1 varchar(100) comment 'その他１(よい商品を心をこめて！)'
, etc2 varchar(100) comment 'その他２(★★★仕入れの窓口★★★)'
, etc3 varchar(100) comment 'その他３(毎度ありがとうございます。)'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '納品書＆請求書情報';

drop table jaiko_partner_info;
create table jaiko_partner_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, partner_id varchar(20) comment '取引先No.'
, partner_nm varchar(100) comment '取引先名'
, partner_post varchar(10) comment '郵便番号'
, partner_add varchar(260) comment '住所'
, partner_tel varchar(20) comment '電話番号'
, partner_fax varchar(20) comment 'FAX番号'
, tantou_nm varchar(100) comment '担当者名'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '取引先情報';

drop table jaiko_inventory_info;
create table jaiko_inventory_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, prd_cd varchar(20) comment '商品コード'
, brand_nm varchar(20) comment 'ブランド'
, prd_nm varchar(1000) comment '商品名'
, jan_cd varchar(15) comment 'ＪＡＮコード'
, now_prd_cnt varchar(15) comment '現在商品数'
, prd_qty varchar(10) comment '入数'
, prd_case varchar(10) comment 'ケース数'
, prd_bara varchar(10) comment 'バラ数'
, exp_dt datetime comment '賞味期限'
, sell_prc varchar(20) comment '本体売価'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '商品在庫管理';


alter table jaiko_inventory_info add column dealer_id VARCHAR(20) comment '取引先コード';
alter table jaiko_inventory_info add column dealer_nm VARCHAR(100) comment '取引先会社名';
alter table jaiko_inventory_info add column prd_lot varchar(10) comment 'ロート数';


drop table jaiko_receipt_info;
create table jaiko_receipt_info (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, denpyou_no varchar(20) comment '伝票No.'
, partner_id varchar(20) comment '取引先No.'
, partner_nm varchar(100) comment '取引先名'
, tantou_nm varchar(20) comment '担当者'
, company_nm varchar(20) comment '会社名'
, company_add varchar(200) comment '会社住所'
, company_tel varchar(20) comment '会社電話'
, company_fax varchar(20) comment '会社FAX'
, bank_transfer_info varchar(500) comment '振込先'
, brand_nm varchar(20) comment 'ブランド'
, prd_nm varchar(1000) comment '商品名'
, prd_spec varchar(20) comment '規格'
, prd_cd varchar(20) comment '品コード'
, prd_cnt varchar(3) comment '入数'
, prd_quantity varchar(5) comment '数量'
, prd_indiv_cnt varchar(3) comment '個数'
, prd_unit_prc varchar(20) comment '単価'
, prd_total_prc varchar(20) comment '金額'
, tax_rt varchar(2) comment '税率'
, tax_rt_symb varchar(2) comment '税率記号'
, ref_prc varchar(20) comment '参考売価'
, ref_spec varchar(15) comment '摘要'
, buy_amt varchar(20) comment 'お買上額計'
, comsump_tax varchar(20) comment '消費税額'
, tax_incld_amt varchar(20) comment '税込合計額'
, prev_remain_amt varchar(20) comment '前回残金額'
, total_amt varchar(20) comment 'お会計金額'
, carry_balance_amt varchar(20) comment '繰越残高'
, etc1 varchar(100) comment 'その他１'
, etc2 varchar(100) comment 'その他２'
, etc3 varchar(100) comment 'その他３'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '領収書情報';


drop table jaiko_order_status;
create table jaiko_order_status (
seq_id bigint unsigned primary key auto_increment comment '区分ID'
, partner_id varchar(20) comment '納品先コード'
, prd_nm VARCHAR(1500) comment '商品名'
, prd_cnt_box VARCHAR(10) comment '数量（BOX） ―箱―'
, prd_cnt VARCHAR(10) comment '数量 －個数―'
, jan_cd varchar(15) comment 'JANコード'
, gtin_cd varchar(15) comment 'GTINコード'
, reg_user_id varchar(20) comment '登録者'
, reg_dt datetime default now() comment '登録日付'
, upd_user_id varchar(20) default null comment '更新者'
, upd_dt datetime comment '更新日付'
) default charset = utf8 comment '注文状況';


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
, jan_cd varchar(15) comment 'JANコード'
, prd_cd varchar(20) comment '商品コード'
, prd_nm varchar(1000) comment '商品名'
, prd_quantity varchar(5) comment '商品数量'
, prd_unit char(1) comment '単位'
, ware_loc char(1) comment '場所'
, reg_dt datetime default now() comment '登録日付'
, upd_dt datetime default now() comment '更新日付'
) default charset = utf8 comment '入出庫TEMP';
