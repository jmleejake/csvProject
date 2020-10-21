package jp.prj.araku.rakuten.vo;

import com.opencsv.bean.CsvBindByPosition;

public class RakutenVOForUriage {
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**ステータス*/
	@CsvBindByPosition(position=1)
	private String order_status;
	/**サブステータスID*/
	@CsvBindByPosition(position=2)
	private String sub_status_id;
	/**サブステータス*/
	@CsvBindByPosition(position=3)
	private String sub_status;
	/**注文日時*/
	@CsvBindByPosition(position=4)
	private String order_datetime;
	/**注文日*/
	@CsvBindByPosition(position=5)
	private String order_date;
	/**注文時間*/
	@CsvBindByPosition(position=6)
	private String order_time;
	/**キャンセル期限日*/
	@CsvBindByPosition(position=7)
	private String cancel_due_date;
	/**注文確認日時*/
	@CsvBindByPosition(position=8)
	private String order_check_datetime;
	/**注文確定日時*/
	@CsvBindByPosition(position=9)
	private String order_confirm_datetime;
	/**発送指示日時*/
	@CsvBindByPosition(position=10)
	private String delivery_eta_datetime;
	/**発送完了報告日時*/
	@CsvBindByPosition(position=11)
	private String delivery_ata_datetime;
	/**支払方法名*/
	@CsvBindByPosition(position=12)
	private String pay_method_name;
	/**クレジットカード支払い方法*/
	@CsvBindByPosition(position=13)
	private String creadit_pay_method;
	/**クレジットカード支払い回数*/
	@CsvBindByPosition(position=14)
	private String credit_pay_times;
	/**配送方法*/
	@CsvBindByPosition(position=15)
	private String delivery_method;
	/**配送区分*/
	@CsvBindByPosition(position=16)
	private String delivery_type;
	/**注文種別*/
	@CsvBindByPosition(position=17)
	private String order_type;
	/**複数送付先フラグ*/
	@CsvBindByPosition(position=18)
	private String multi_destination_flag;
	/**送付先一致フラグ*/
	@CsvBindByPosition(position=19)
	private String destination_match_flag;
	/**離島フラグ*/
	@CsvBindByPosition(position=20)
	private String island_flag;
	/**楽天確認中フラグ*/
	@CsvBindByPosition(position=21)
	private String rverify_flag;
	/**警告表示タイプ*/
	@CsvBindByPosition(position=22)
	private String warning_type;
	/**楽天会員フラグ*/
	@CsvBindByPosition(position=23)
	private String rmember_flag;
	/**購入履歴修正有無フラグ*/
	@CsvBindByPosition(position=24)
	private String purchase_hist_mod_flag;
	/**商品合計金額*/
	@CsvBindByPosition(position=25)
	private String total_goods_amt;
	/**消費税合計*/
	@CsvBindByPosition(position=26)
	private String total_consume_tax;
	/**送料合計*/
	@CsvBindByPosition(position=27)
	private String total_shipping;
	/**代引料合計*/
	@CsvBindByPosition(position=28)
	private String gross_deduction;
	/**請求金額*/
	@CsvBindByPosition(position=29)
	private String invoice_amt;
	/**合計金額*/
	@CsvBindByPosition(position=30)
	private String total_amt;
	/**ポイント利用額*/
	@CsvBindByPosition(position=31)
	private String point_usage;
	/**クーポン利用総額*/
	@CsvBindByPosition(position=32)
	private String total_coupon_usage;
	/**店舗発行クーポン利用額*/
	@CsvBindByPosition(position=33)
	private String store_coupon_usage;
	/**楽天発行クーポン利用額*/
	@CsvBindByPosition(position=34)
	private String rakuten_coupon_usage;
	/**注文者郵便番号1*/
	@CsvBindByPosition(position=35)
	private String order_post_no1;
	/**注文者郵便番号2*/
	@CsvBindByPosition(position=36)
	private String order_post_no2;
	/**注文者住所都道府県*/
	@CsvBindByPosition(position=37)
	private String order_add1;
	/**注文者住所郡市区*/
	@CsvBindByPosition(position=38)
	private String order_add2;
	/**注文者住所それ以降の住所*/
	@CsvBindByPosition(position=39)
	private String order_add3;
	/**注文者姓*/
	@CsvBindByPosition(position=40)
	private String order_surname;
	/**注文者名*/
	@CsvBindByPosition(position=41)
	private String order_name;
	/**注文者姓カナ*/
	@CsvBindByPosition(position=42)
	private String order_surname_kana;
	/**注文者名カナ*/
	@CsvBindByPosition(position=43)
	private String order_name_kana;
	/**注文者電話番号1*/
	@CsvBindByPosition(position=44)
	private String order_tel1;
	/**注文者電話番号2*/
	@CsvBindByPosition(position=45)
	private String order_tel2;
	/**注文者電話番号3*/
	@CsvBindByPosition(position=46)
	private String order_tel3;
	/**注文者メールアドレス*/
	@CsvBindByPosition(position=47)
	private String order_email;
	/**注文者性別*/
	@CsvBindByPosition(position=48)
	private String order_sex;
	/**申込番号*/
	@CsvBindByPosition(position=49)
	private String request_no;
	/**申込お届け回数*/
	@CsvBindByPosition(position=50)
	private String request_received_no;
	/**送付先ID*/
	@CsvBindByPosition(position=51)
	private String ship_id;
	/**送付先送料*/
	@CsvBindByPosition(position=52)
	private String ship_charge;
	/**送付先代引料*/
	@CsvBindByPosition(position=53)
	private String ship_substitute_fee;
	/**送付先消費税合計*/
	@CsvBindByPosition(position=54)
	private String ship_total_consume_tax;
	/**送付先商品合計金額*/
	@CsvBindByPosition(position=55)
	private String ship_total_goods_amt;
	/**送付先合計金額*/
	@CsvBindByPosition(position=56)
	private String ship_total_amt;
	/**のし*/
	@CsvBindByPosition(position=57)
	private String indicates;
	/**送付先郵便番号1*/
	@CsvBindByPosition(position=58)
	private String delivery_post_no1;
	/**送付先郵便番号2*/
	@CsvBindByPosition(position=59)
	private String delivery_post_no2;
	/**送付先住所都道府県*/
	@CsvBindByPosition(position=60)
	private String delivery_add1;
	/**送付先住所郡市区*/
	@CsvBindByPosition(position=61)
	private String delivery_add2;
	/**送付先住所それ以降の住所*/
	@CsvBindByPosition(position=62)
	private String delivery_add3;
	/**送付先姓*/
	@CsvBindByPosition(position=63)
	private String delivery_surname;
	/**送付先名*/
	@CsvBindByPosition(position=64)
	private String delivery_name;
	/**送付先姓カナ*/
	@CsvBindByPosition(position=65)
	private String delivery_surname_kana;
	/**送付先名カナ*/
	@CsvBindByPosition(position=66)
	private String delivery_name_kana;
	/**送付先電話番号1*/
	@CsvBindByPosition(position=67)
	private String delivery_tel1;
	/**送付先電話番号2*/
	@CsvBindByPosition(position=68)
	private String delivery_tel2;
	/**送付先電話番号3*/
	@CsvBindByPosition(position=69)
	private String delivery_tel3;
	/**商品明細ID*/
	@CsvBindByPosition(position=70)
	private String product_detail_id;
	/**商品ID*/
	@CsvBindByPosition(position=71)
	private String product_id;
	/**商品名*/
	@CsvBindByPosition(position=72)
	private String product_name;
	/**商品番号*/
	@CsvBindByPosition(position=73)
	private String product_no;
	/**商品管理番号*/
	@CsvBindByPosition(position=74)
	private String product_manage_no;
	/**単価*/
	@CsvBindByPosition(position=75)
	private String unit_price;
	/**個数*/
	@CsvBindByPosition(position=76)
	private String unit_no;
	/**送料込別*/
	@CsvBindByPosition(position=77)
	private String delivery_cost_include;
	/**税込別*/
	@CsvBindByPosition(position=78)
	private String tax_exclude;
	/**代引手数料込別*/
	@CsvBindByPosition(position=79)
	private String substitute_fee_include;
	/**ポイント倍率*/
	@CsvBindByPosition(position=80)
	private String point_multiple;
	/**納期情報*/
	@CsvBindByPosition(position=81)
	private String delivery_info;
	/**在庫タイプ*/
	@CsvBindByPosition(position=82)
	private String inventory_type;
	/**ラッピングタイトル1*/
	@CsvBindByPosition(position=83)
	private String wrap_title1;
	/**ラッピング名1*/
	@CsvBindByPosition(position=84)
	private String wrap_name1;
	/**ラッピング料金1*/
	@CsvBindByPosition(position=85)
	private String wrap_amt1;
	/**ラッピング税込別1*/
	@CsvBindByPosition(position=86)
	private String wrap_tax_include1;
	/**ラッピング種類1*/
	@CsvBindByPosition(position=87)
	private String wrap_type1;
	/**ラッピングタイトル2*/
	@CsvBindByPosition(position=88)
	private String wrap_title2;
	/**ラッピング名2*/
	@CsvBindByPosition(position=89)
	private String wrap_name2;
	/**ラッピング料金2*/
	@CsvBindByPosition(position=90)
	private String wrap_amt2;
	/**ラッピング税込別2*/
	@CsvBindByPosition(position=91)
	private String wrap_tax_include2;
	/**ラッピング種類2*/
	@CsvBindByPosition(position=92)
	private String wrap_type2;
	/**お届け時間帯*/
	@CsvBindByPosition(position=93)
	private String delivery_time;
	/**お届け日指定*/
	@CsvBindByPosition(position=94)
	private String delivery_date_sel;
	/**担当者*/
	@CsvBindByPosition(position=95)
	private String manager;
	/**ひとことメモ*/
	@CsvBindByPosition(position=96)
	private String quick_note;
	/**メール差込文 (お客様へのメッセージ)*/
	@CsvBindByPosition(position=97)
	private String msg_to_customer;
	/**ギフト配送希望*/
	@CsvBindByPosition(position=98)
	private String gift_request;
	/**利用端末*/
	@CsvBindByPosition(position=99)
	private String util_terminal;
	/**メールキャリアコード*/
	@CsvBindByPosition(position=100)
	private String mail_carrier_code;
	/**あす楽希望フラグ*/
	@CsvBindByPosition(position=101)
	private String tomorrow_hope;
	/**医薬品受注フラグ*/
	@CsvBindByPosition(position=102)
	private String drug_order_flag;
	/**楽天スーパーDEAL商品受注フラグ*/
	@CsvBindByPosition(position=103)
	private String rakuten_super_deal;
	/**メンバーシッププログラム受注タイプ*/
	@CsvBindByPosition(position=104)
	private String membership_program;
	
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getSub_status_id() {
		return sub_status_id;
	}
	public void setSub_status_id(String sub_status_id) {
		this.sub_status_id = sub_status_id;
	}
	public String getSub_status() {
		return sub_status;
	}
	public void setSub_status(String sub_status) {
		this.sub_status = sub_status;
	}
	public String getOrder_datetime() {
		return order_datetime;
	}
	public void setOrder_datetime(String order_datetime) {
		this.order_datetime = order_datetime;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getCancel_due_date() {
		return cancel_due_date;
	}
	public void setCancel_due_date(String cancel_due_date) {
		this.cancel_due_date = cancel_due_date;
	}
	public String getOrder_check_datetime() {
		return order_check_datetime;
	}
	public void setOrder_check_datetime(String order_check_datetime) {
		this.order_check_datetime = order_check_datetime;
	}
	public String getOrder_confirm_datetime() {
		return order_confirm_datetime;
	}
	public void setOrder_confirm_datetime(String order_confirm_datetime) {
		this.order_confirm_datetime = order_confirm_datetime;
	}
	public String getDelivery_eta_datetime() {
		return delivery_eta_datetime;
	}
	public void setDelivery_eta_datetime(String delivery_eta_datetime) {
		this.delivery_eta_datetime = delivery_eta_datetime;
	}
	public String getDelivery_ata_datetime() {
		return delivery_ata_datetime;
	}
	public void setDelivery_ata_datetime(String delivery_ata_datetime) {
		this.delivery_ata_datetime = delivery_ata_datetime;
	}
	public String getPay_method_name() {
		return pay_method_name;
	}
	public void setPay_method_name(String pay_method_name) {
		this.pay_method_name = pay_method_name;
	}
	public String getCreadit_pay_method() {
		return creadit_pay_method;
	}
	public void setCreadit_pay_method(String creadit_pay_method) {
		this.creadit_pay_method = creadit_pay_method;
	}
	public String getCredit_pay_times() {
		return credit_pay_times;
	}
	public void setCredit_pay_times(String credit_pay_times) {
		this.credit_pay_times = credit_pay_times;
	}
	public String getDelivery_method() {
		return delivery_method;
	}
	public void setDelivery_method(String delivery_method) {
		this.delivery_method = delivery_method;
	}
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getMulti_destination_flag() {
		return multi_destination_flag;
	}
	public void setMulti_destination_flag(String multi_destination_flag) {
		this.multi_destination_flag = multi_destination_flag;
	}
	public String getDestination_match_flag() {
		return destination_match_flag;
	}
	public void setDestination_match_flag(String destination_match_flag) {
		this.destination_match_flag = destination_match_flag;
	}
	public String getIsland_flag() {
		return island_flag;
	}
	public void setIsland_flag(String island_flag) {
		this.island_flag = island_flag;
	}
	public String getRverify_flag() {
		return rverify_flag;
	}
	public void setRverify_flag(String rverify_flag) {
		this.rverify_flag = rverify_flag;
	}
	public String getWarning_type() {
		return warning_type;
	}
	public void setWarning_type(String warning_type) {
		this.warning_type = warning_type;
	}
	public String getRmember_flag() {
		return rmember_flag;
	}
	public void setRmember_flag(String rmember_flag) {
		this.rmember_flag = rmember_flag;
	}
	public String getPurchase_hist_mod_flag() {
		return purchase_hist_mod_flag;
	}
	public void setPurchase_hist_mod_flag(String purchase_hist_mod_flag) {
		this.purchase_hist_mod_flag = purchase_hist_mod_flag;
	}
	public String getTotal_goods_amt() {
		return total_goods_amt;
	}
	public void setTotal_goods_amt(String total_goods_amt) {
		this.total_goods_amt = total_goods_amt;
	}
	public String getTotal_consume_tax() {
		return total_consume_tax;
	}
	public void setTotal_consume_tax(String total_consume_tax) {
		this.total_consume_tax = total_consume_tax;
	}
	public String getTotal_shipping() {
		return total_shipping;
	}
	public void setTotal_shipping(String total_shipping) {
		this.total_shipping = total_shipping;
	}
	public String getGross_deduction() {
		return gross_deduction;
	}
	public void setGross_deduction(String gross_deduction) {
		this.gross_deduction = gross_deduction;
	}
	public String getInvoice_amt() {
		return invoice_amt;
	}
	public void setInvoice_amt(String invoice_amt) {
		this.invoice_amt = invoice_amt;
	}
	public String getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}
	public String getPoint_usage() {
		return point_usage;
	}
	public void setPoint_usage(String point_usage) {
		this.point_usage = point_usage;
	}
	public String getTotal_coupon_usage() {
		return total_coupon_usage;
	}
	public void setTotal_coupon_usage(String total_coupon_usage) {
		this.total_coupon_usage = total_coupon_usage;
	}
	public String getStore_coupon_usage() {
		return store_coupon_usage;
	}
	public void setStore_coupon_usage(String store_coupon_usage) {
		this.store_coupon_usage = store_coupon_usage;
	}
	public String getRakuten_coupon_usage() {
		return rakuten_coupon_usage;
	}
	public void setRakuten_coupon_usage(String rakuten_coupon_usage) {
		this.rakuten_coupon_usage = rakuten_coupon_usage;
	}
	public String getOrder_post_no1() {
		return order_post_no1;
	}
	public void setOrder_post_no1(String order_post_no1) {
		this.order_post_no1 = order_post_no1;
	}
	public String getOrder_post_no2() {
		return order_post_no2;
	}
	public void setOrder_post_no2(String order_post_no2) {
		this.order_post_no2 = order_post_no2;
	}
	public String getOrder_add1() {
		return order_add1;
	}
	public void setOrder_add1(String order_add1) {
		this.order_add1 = order_add1;
	}
	public String getOrder_add2() {
		return order_add2;
	}
	public void setOrder_add2(String order_add2) {
		this.order_add2 = order_add2;
	}
	public String getOrder_add3() {
		return order_add3;
	}
	public void setOrder_add3(String order_add3) {
		this.order_add3 = order_add3;
	}
	public String getOrder_surname() {
		return order_surname;
	}
	public void setOrder_surname(String order_surname) {
		this.order_surname = order_surname;
	}
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	public String getOrder_surname_kana() {
		return order_surname_kana;
	}
	public void setOrder_surname_kana(String order_surname_kana) {
		this.order_surname_kana = order_surname_kana;
	}
	public String getOrder_name_kana() {
		return order_name_kana;
	}
	public void setOrder_name_kana(String order_name_kana) {
		this.order_name_kana = order_name_kana;
	}
	public String getOrder_tel1() {
		return order_tel1;
	}
	public void setOrder_tel1(String order_tel1) {
		this.order_tel1 = order_tel1;
	}
	public String getOrder_tel2() {
		return order_tel2;
	}
	public void setOrder_tel2(String order_tel2) {
		this.order_tel2 = order_tel2;
	}
	public String getOrder_tel3() {
		return order_tel3;
	}
	public void setOrder_tel3(String order_tel3) {
		this.order_tel3 = order_tel3;
	}
	public String getOrder_email() {
		return order_email;
	}
	public void setOrder_email(String order_email) {
		this.order_email = order_email;
	}
	public String getOrder_sex() {
		return order_sex;
	}
	public void setOrder_sex(String order_sex) {
		this.order_sex = order_sex;
	}
	public String getRequest_no() {
		return request_no;
	}
	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}
	public String getRequest_received_no() {
		return request_received_no;
	}
	public void setRequest_received_no(String request_received_no) {
		this.request_received_no = request_received_no;
	}
	public String getShip_id() {
		return ship_id;
	}
	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
	}
	public String getShip_charge() {
		return ship_charge;
	}
	public void setShip_charge(String ship_charge) {
		this.ship_charge = ship_charge;
	}
	public String getShip_substitute_fee() {
		return ship_substitute_fee;
	}
	public void setShip_substitute_fee(String ship_substitute_fee) {
		this.ship_substitute_fee = ship_substitute_fee;
	}
	public String getShip_total_consume_tax() {
		return ship_total_consume_tax;
	}
	public void setShip_total_consume_tax(String ship_total_consume_tax) {
		this.ship_total_consume_tax = ship_total_consume_tax;
	}
	public String getShip_total_goods_amt() {
		return ship_total_goods_amt;
	}
	public void setShip_total_goods_amt(String ship_total_goods_amt) {
		this.ship_total_goods_amt = ship_total_goods_amt;
	}
	public String getShip_total_amt() {
		return ship_total_amt;
	}
	public void setShip_total_amt(String ship_total_amt) {
		this.ship_total_amt = ship_total_amt;
	}
	public String getIndicates() {
		return indicates;
	}
	public void setIndicates(String indicates) {
		this.indicates = indicates;
	}
	public String getDelivery_post_no1() {
		return delivery_post_no1;
	}
	public void setDelivery_post_no1(String delivery_post_no1) {
		this.delivery_post_no1 = delivery_post_no1;
	}
	public String getDelivery_post_no2() {
		return delivery_post_no2;
	}
	public void setDelivery_post_no2(String delivery_post_no2) {
		this.delivery_post_no2 = delivery_post_no2;
	}
	public String getDelivery_add1() {
		return delivery_add1;
	}
	public void setDelivery_add1(String delivery_add1) {
		this.delivery_add1 = delivery_add1;
	}
	public String getDelivery_add2() {
		return delivery_add2;
	}
	public void setDelivery_add2(String delivery_add2) {
		this.delivery_add2 = delivery_add2;
	}
	public String getDelivery_add3() {
		return delivery_add3;
	}
	public void setDelivery_add3(String delivery_add3) {
		this.delivery_add3 = delivery_add3;
	}
	public String getDelivery_surname() {
		return delivery_surname;
	}
	public void setDelivery_surname(String delivery_surname) {
		this.delivery_surname = delivery_surname;
	}
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	public String getDelivery_surname_kana() {
		return delivery_surname_kana;
	}
	public void setDelivery_surname_kana(String delivery_surname_kana) {
		this.delivery_surname_kana = delivery_surname_kana;
	}
	public String getDelivery_name_kana() {
		return delivery_name_kana;
	}
	public void setDelivery_name_kana(String delivery_name_kana) {
		this.delivery_name_kana = delivery_name_kana;
	}
	public String getDelivery_tel1() {
		return delivery_tel1;
	}
	public void setDelivery_tel1(String delivery_tel1) {
		this.delivery_tel1 = delivery_tel1;
	}
	public String getDelivery_tel2() {
		return delivery_tel2;
	}
	public void setDelivery_tel2(String delivery_tel2) {
		this.delivery_tel2 = delivery_tel2;
	}
	public String getDelivery_tel3() {
		return delivery_tel3;
	}
	public void setDelivery_tel3(String delivery_tel3) {
		this.delivery_tel3 = delivery_tel3;
	}
	public String getProduct_detail_id() {
		return product_detail_id;
	}
	public void setProduct_detail_id(String product_detail_id) {
		this.product_detail_id = product_detail_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_no() {
		return product_no;
	}
	public void setProduct_no(String product_no) {
		this.product_no = product_no;
	}
	public String getProduct_manage_no() {
		return product_manage_no;
	}
	public void setProduct_manage_no(String product_manage_no) {
		this.product_manage_no = product_manage_no;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getUnit_no() {
		return unit_no;
	}
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}
	public String getDelivery_cost_include() {
		return delivery_cost_include;
	}
	public void setDelivery_cost_include(String delivery_cost_include) {
		this.delivery_cost_include = delivery_cost_include;
	}
	public String getTax_exclude() {
		return tax_exclude;
	}
	public void setTax_exclude(String tax_exclude) {
		this.tax_exclude = tax_exclude;
	}
	public String getSubstitute_fee_include() {
		return substitute_fee_include;
	}
	public void setSubstitute_fee_include(String substitute_fee_include) {
		this.substitute_fee_include = substitute_fee_include;
	}
	public String getPoint_multiple() {
		return point_multiple;
	}
	public void setPoint_multiple(String point_multiple) {
		this.point_multiple = point_multiple;
	}
	public String getDelivery_info() {
		return delivery_info;
	}
	public void setDelivery_info(String delivery_info) {
		this.delivery_info = delivery_info;
	}
	public String getInventory_type() {
		return inventory_type;
	}
	public void setInventory_type(String inventory_type) {
		this.inventory_type = inventory_type;
	}
	public String getWrap_title1() {
		return wrap_title1;
	}
	public void setWrap_title1(String wrap_title1) {
		this.wrap_title1 = wrap_title1;
	}
	public String getWrap_name1() {
		return wrap_name1;
	}
	public void setWrap_name1(String wrap_name1) {
		this.wrap_name1 = wrap_name1;
	}
	public String getWrap_amt1() {
		return wrap_amt1;
	}
	public void setWrap_amt1(String wrap_amt1) {
		this.wrap_amt1 = wrap_amt1;
	}
	public String getWrap_tax_include1() {
		return wrap_tax_include1;
	}
	public void setWrap_tax_include1(String wrap_tax_include1) {
		this.wrap_tax_include1 = wrap_tax_include1;
	}
	public String getWrap_type1() {
		return wrap_type1;
	}
	public void setWrap_type1(String wrap_type1) {
		this.wrap_type1 = wrap_type1;
	}
	public String getWrap_title2() {
		return wrap_title2;
	}
	public void setWrap_title2(String wrap_title2) {
		this.wrap_title2 = wrap_title2;
	}
	public String getWrap_name2() {
		return wrap_name2;
	}
	public void setWrap_name2(String wrap_name2) {
		this.wrap_name2 = wrap_name2;
	}
	public String getWrap_amt2() {
		return wrap_amt2;
	}
	public void setWrap_amt2(String wrap_amt2) {
		this.wrap_amt2 = wrap_amt2;
	}
	public String getWrap_tax_include2() {
		return wrap_tax_include2;
	}
	public void setWrap_tax_include2(String wrap_tax_include2) {
		this.wrap_tax_include2 = wrap_tax_include2;
	}
	public String getWrap_type2() {
		return wrap_type2;
	}
	public void setWrap_type2(String wrap_type2) {
		this.wrap_type2 = wrap_type2;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getDelivery_date_sel() {
		return delivery_date_sel;
	}
	public void setDelivery_date_sel(String delivery_date_sel) {
		this.delivery_date_sel = delivery_date_sel;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getQuick_note() {
		return quick_note;
	}
	public void setQuick_note(String quick_note) {
		this.quick_note = quick_note;
	}
	public String getMsg_to_customer() {
		return msg_to_customer;
	}
	public void setMsg_to_customer(String msg_to_customer) {
		this.msg_to_customer = msg_to_customer;
	}
	public String getGift_request() {
		return gift_request;
	}
	public void setGift_request(String gift_request) {
		this.gift_request = gift_request;
	}
	public String getUtil_terminal() {
		return util_terminal;
	}
	public void setUtil_terminal(String util_terminal) {
		this.util_terminal = util_terminal;
	}
	public String getMail_carrier_code() {
		return mail_carrier_code;
	}
	public void setMail_carrier_code(String mail_carrier_code) {
		this.mail_carrier_code = mail_carrier_code;
	}
	public String getTomorrow_hope() {
		return tomorrow_hope;
	}
	public void setTomorrow_hope(String tomorrow_hope) {
		this.tomorrow_hope = tomorrow_hope;
	}
	public String getDrug_order_flag() {
		return drug_order_flag;
	}
	public void setDrug_order_flag(String drug_order_flag) {
		this.drug_order_flag = drug_order_flag;
	}
	public String getRakuten_super_deal() {
		return rakuten_super_deal;
	}
	public void setRakuten_super_deal(String rakuten_super_deal) {
		this.rakuten_super_deal = rakuten_super_deal;
	}
	public String getMembership_program() {
		return membership_program;
	}
	public void setMembership_program(String membership_program) {
		this.membership_program = membership_program;
	}


}
