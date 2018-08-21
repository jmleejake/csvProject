package jp.prj.araku.file.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

/**
 * 楽天CSVファイル
 */
public class RakutenVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**受注番号*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**受注ステータス*/
	@CsvBindByPosition(position=1)
	private String order_status;
	/**カード決済ステータス*/
	@CsvBindByPosition(position=2)
	private String card_pay_status;
	/**入金日*/
	@CsvBindByPosition(position=3)
	private String deposit_date;
	/**配送日*/
	@CsvBindByPosition(position=4)
	private String delivery_date;
	/**お届け時間帯*/
	@CsvBindByPosition(position=5)
	private String delivery_time;
	/**お届け日指定*/
	@CsvBindByPosition(position=6)
	private String delivery_date_sel;
	/**担当者*/
	@CsvBindByPosition(position=7)
	private String person_in_charge;
	/**ひとことメモ*/
	@CsvBindByPosition(position=8)
	private String note;
	/**メール差込文(お客様へのメッセージ)*/
	@CsvBindByPosition(position=9)
	private String note_to_customer;
	/**初期購入合計金額*/
	@CsvBindByPosition(position=10)
	private String init_purchase_amt;
	/**利用端末*/
	@CsvBindByPosition(position=11)
	private String terminal;
	/**メールキャリアコード*/
	@CsvBindByPosition(position=12)
	private String mail_carrier_code;
	/**ギフトチェック（0:なし/1:あり）*/
	@CsvBindByPosition(position=13)
	private String gift_check;
	/**コメント*/
	@CsvBindByPosition(position=14)
	private String order_comment;
	/**注文日時*/
	@CsvBindByPosition(position=15)
	private String order_datetime;
	/**複数送付先フラグ*/
	@CsvBindByPosition(position=16)
	private String multi_destination_flag;
	/**警告表示フラグ*/
	@CsvBindByPosition(position=17)
	private String warning_flag;
	/**楽天会員フラグ*/
	@CsvBindByPosition(position=18)
	private String rmember_flag;
	/**合計*/
	@CsvBindByPosition(position=19)
	private String total;
	/**消費税(-99999=無効値)*/
	@CsvBindByPosition(position=20)
	private String consume_tax;
	/**送料(-99999=無効値)*/
	@CsvBindByPosition(position=21)
	private String shipping_cost;
	/**代引料(-99999=無効値)*/
	@CsvBindByPosition(position=22)
	private String substitute_cost;
	/**請求金額(-99999=無効値)*/
	@CsvBindByPosition(position=23)
	private String billing_amt;
	/**合計金額(-99999=無効値)*/
	@CsvBindByPosition(position=24)
	private String total_amt;
	/**同梱ID*/
	@CsvBindByPosition(position=25)
	private String enclosed_id;
	/**同梱ステータス*/
	@CsvBindByPosition(position=26)
	private String enclosed_status;
	/**同梱商品合計金額*/
	@CsvBindByPosition(position=27)
	private String enclosed_goods_total_amt;
	/**同梱送料合計*/
	@CsvBindByPosition(position=28)
	private String enclosed_total_shipping;
	/**同梱代引料合計*/
	@CsvBindByPosition(position=29)
	private String enclosed_total_substitue;
	/**同梱消費税合計*/
	@CsvBindByPosition(position=30)
	private String enclosed_total_consume;
	/**同梱請求金額*/
	@CsvBindByPosition(position=31)
	private String enclosed_billing_amt;
	/**同梱合計金額*/
	@CsvBindByPosition(position=32)
	private String enclosed_total_amt;
	/**同梱楽天バンク決済振替手数料*/
	@CsvBindByPosition(position=33)
	private String enclosed_rbank_transfer_fee;
	/**同梱ポイント利用合計*/
	@CsvBindByPosition(position=34)
	private String enclosed_total_point_usage;
	/**メールフラグ*/
	@CsvBindByPosition(position=35)
	private String mail_flag;
	/**注文日*/
	@CsvBindByPosition(position=36)
	private String order_date;
	/**注文時間*/
	@CsvBindByPosition(position=37)
	private String order_time;
	/**モバイルキャリア決済番号*/
	@CsvBindByPosition(position=38)
	private String mobile_carrier_payment_no;
	/**購入履歴修正可否タイプ*/
	@CsvBindByPosition(position=39)
	private String purchase_hist_mod_type;
	/**購入履歴修正アイコンフラグ*/
	@CsvBindByPosition(position=40)
	private String purchase_hist_mod_icon;
	/**購入履歴修正催促メールフラグ*/
	@CsvBindByPosition(position=41)
	private String purchase_hist_mod_prompt_mail;
	/**送付先一致フラグ*/
	@CsvBindByPosition(position=42)
	private String destination_match_flag;
	/**ポイント利用有無*/
	@CsvBindByPosition(position=43)
	private String point_usage;
	/**注文者郵便番号１*/
	@CsvBindByPosition(position=44)
	private String order_post_no1;
	/**注文者郵便番号２*/
	@CsvBindByPosition(position=45)
	private String order_post_no2;
	/**注文者住所：都道府県*/
	@CsvBindByPosition(position=46)
	private String order_address1;
	/**注文者住所：都市区*/
	@CsvBindByPosition(position=47)
	private String order_address2;
	/**注文者住所：町以降*/
	@CsvBindByPosition(position=48)
	private String order_address3;
	/**注文者名字*/
	@CsvBindByPosition(position=49)
	private String order_surname;
	/**注文者名前*/
	@CsvBindByPosition(position=50)
	private String order_name;
	/**注文者名字フリガナ*/
	@CsvBindByPosition(position=51)
	private String order_surname_kana;
	/**注文者名前フリガナ*/
	@CsvBindByPosition(position=52)
	private String order_name_kana;
	/**注文者電話番号１*/
	@CsvBindByPosition(position=53)
	private String order_tel1;
	/**注文者電話番号２*/
	@CsvBindByPosition(position=54)
	private String order_tel2;
	/**注文者電話番号３*/
	@CsvBindByPosition(position=55)
	private String order_tel3;
	/**メールアドレス*/
	@CsvBindByPosition(position=56)
	private String mail_address;
	/**注文者性別*/
	@CsvBindByPosition(position=57)
	private String order_sex;
	/**注文者誕生日*/
	@CsvBindByPosition(position=58)
	private String order_birth;
	/**決済方法*/
	@CsvBindByPosition(position=59)
	private String payment_method;
	/**クレジットカード種類*/
	@CsvBindByPosition(position=60)
	private String credit_type;
	/**クレジットカード番号*/
	@CsvBindByPosition(position=61)
	private String credit_no;
	/**クレジットカード名義人*/
	@CsvBindByPosition(position=62)
	private String credit_user;
	/**クレジットカード有効期限*/
	@CsvBindByPosition(position=63)
	private String credit_expire;
	/**クレジットカード分割選択*/
	@CsvBindByPosition(position=64)
	private String credit_installment;
	/**クレジットカード分割備考*/
	@CsvBindByPosition(position=65)
	private String credit_installment_note;
	/**配送方法*/
	@CsvBindByPosition(position=66)
	private String delivery_method;
	/**配送区分*/
	@CsvBindByPosition(position=67)
	private String delivery_type;
	/**ポイント利用額*/
	@CsvBindByPosition(position=68)
	private String point_usage_person;
	/**ポイント利用条件*/
	@CsvBindByPosition(position=69)
	private String point_usage_condition;
	/**ポイントステータス*/
	@CsvBindByPosition(position=70)
	private String point_status;
	/**楽天バンク決済ステータス*/
	@CsvBindByPosition(position=71)
	private String rbank_payment_status;
	/**楽天バンク振替手数料負担区分*/
	@CsvBindByPosition(position=72)
	private String rbank_payment_transfer_fee;
	/**楽天バンク決済手数料*/
	@CsvBindByPosition(position=73)
	private String rbank_payment_fee;
	/**ラッピングタイトル(包装紙)*/
	@CsvBindByPosition(position=74)
	private String wrap_paper_title;
	/**ラッピング名(包装紙)*/
	@CsvBindByPosition(position=75)
	private String wrap_paper_name;
	/**ラッピング料金(包装紙)*/
	@CsvBindByPosition(position=76)
	private String wrap_paper_fee;
	/**税込別(包装紙)*/
	@CsvBindByPosition(position=77)
	private String wrap_paper_tax_include;
	/**ラッピングタイトル(リボン)*/
	@CsvBindByPosition(position=78)
	private String wrap_ribbon_title;
	/**ラッピング名(リボン)*/
	@CsvBindByPosition(position=79)
	private String wrap_ribbon_name;
	/**ラッピング料金(リボン)*/
	@CsvBindByPosition(position=80)
	private String wrap_ribbon_fee;
	/**税込別(リボン)*/
	@CsvBindByPosition(position=81)
	private String wrap_ribbon_tax_include;
	/**送付先送料*/
	@CsvBindByPosition(position=82)
	private String delivery_cost;
	/**送付先代引料*/
	@CsvBindByPosition(position=83)
	private String delivery_substitute_cost;
	/**送付先消費税*/
	@CsvBindByPosition(position=84)
	private String delivery_consume;
	/**お荷物伝票番号*/
	@CsvBindByPosition(position=85)
	private String baggage_claim_no;
	/**送付先商品合計金額*/
	@CsvBindByPosition(position=86)
	private String delivery_goods_total_amt;
	/**のし*/
	@CsvBindByPosition(position=87)
	private String indicates;
	/**送付先郵便番号１*/
	@CsvBindByPosition(position=88)
	private String delivery_post_no1;
	/**送付先郵便番号２*/
	@CsvBindByPosition(position=89)
	private String delivery_post_no2;
	/**送付先住所：都道府県*/
	@CsvBindByPosition(position=90)
	private String delivery_address1;
	/**送付先住所：都市区*/
	@CsvBindByPosition(position=91)
	private String delivery_address2;
	/**送付先住所：町以降*/
	@CsvBindByPosition(position=92)
	private String delivery_address3;
	/**送付先名字*/
	@CsvBindByPosition(position=93)
	private String delivery_surname;
	/**送付先名前*/
	@CsvBindByPosition(position=94)
	private String delivery_name;
	/**送付先名字フリガナ*/
	@CsvBindByPosition(position=95)
	private String delivery_surname_kana;
	/**送付先名前フリガナ*/
	@CsvBindByPosition(position=96)
	private String delivery_name_kana;
	/**送付先電話番号１*/
	@CsvBindByPosition(position=97)
	private String delivery_tel1;
	/**送付先電話番号２*/
	@CsvBindByPosition(position=98)
	private String delivery_tel2;
	/**送付先電話番号３*/
	@CsvBindByPosition(position=99)
	private String delivery_tel3;
	/**商品ID*/
	@CsvBindByPosition(position=100)
	private String product_id;
	/**商品名*/
	@CsvBindByPosition(position=101)
	private String product_name;
	/**商品番号*/
	@CsvBindByPosition(position=102)
	private String product_no;
	/**商品URL*/
	@CsvBindByPosition(position=103)
	private String product_url;
	/**単価*/
	@CsvBindByPosition(position=104)
	private String unit_price;
	/**個数*/
	@CsvBindByPosition(position=105)
	private String unit_no;
	/**送料込別*/
	@CsvBindByPosition(position=106)
	private String delivery_cost_include;
	/**税込別*/
	@CsvBindByPosition(position=107)
	private String tax_exclude;
	/**代引手数料込別*/
	@CsvBindByPosition(position=108)
	private String substitute_fee_include;
	/**項目・選択肢*/
	@CsvBindByPosition(position=109)
	private String product_option;
	/**ポイント倍率*/
	@CsvBindByPosition(position=110)
	private String point_percent;
	/**ポイントタイプ*/
	@CsvBindByPosition(position=111)
	private String point_type;
	/**レコードナンバー*/
	@CsvBindByPosition(position=112)
	private String record_no;
	/**納期情報*/
	@CsvBindByPosition(position=113)
	private String delivery_info;
	/**在庫タイプ*/
	@CsvBindByPosition(position=114)
	private String inventory_type;
	/**ラッピング種類(包装紙)*/
	@CsvBindByPosition(position=115)
	private String wrap_type_paper;
	/**ラッピング種類(リボン)*/
	@CsvBindByPosition(position=116)
	private String wrap_type_ribbon;
	/**あす楽希望*/
	@CsvBindByPosition(position=117)
	private String tomorrow_hope;
	/**クーポン利用額*/
	@CsvBindByPosition(position=118)
	private String coupon_usage;
	/**店舗発行クーポン利用額*/
	@CsvBindByPosition(position=119)
	private String store_coupon_usage;
	/**楽天発行クーポン利用額*/
	@CsvBindByPosition(position=120)
	private String rcoupon_usage;
	/**同梱注文クーポン利用額*/
	@CsvBindByPosition(position=121)
	private String enclosed_coupon_usage;
	/**配送会社*/
	@CsvBindByPosition(position=122)
	private String delivery_company;
	/**薬事フラグ*/
	@CsvBindByPosition(position=123)
	private String pharm_flag;
	/**楽天スーパーDEAL*/
	@CsvBindByPosition(position=124)
	private String rakuten_super_deal;
	/**メンバーシッププログラム*/
	@CsvBindByPosition(position=125)
	private String membership_program;
	
	/**ゆプリあるダウンロード*/
	private ArrayList<String> seq_id_list;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getRegister_date() {
		return register_date;
	}
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
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
	public String getCard_pay_status() {
		return card_pay_status;
	}
	public void setCard_pay_status(String card_pay_status) {
		this.card_pay_status = card_pay_status;
	}
	public String getDeposit_date() {
		return deposit_date;
	}
	public void setDeposit_date(String deposit_date) {
		this.deposit_date = deposit_date;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
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
		// [CUSTOM] お届け日指定의 형식은 YYYY/MM/DD형식으로 저장.
		// csv파일내 형식의 예외가 있어 수정
		if (delivery_date_sel.contains("-")) {
			delivery_date_sel = delivery_date_sel.replace("-", "/");
		}
		
		if (delivery_date_sel.length() == 8) {
			String year = delivery_date_sel.substring(0, 4);
			String month = delivery_date_sel.substring(4, 6);
			String day = delivery_date_sel.substring(6, 8);
			delivery_date_sel = String.format("%s/%s/%s", year, month, day);
		}
		this.delivery_date_sel = delivery_date_sel;
	}
	public String getPerson_in_charge() {
		return person_in_charge;
	}
	public void setPerson_in_charge(String person_in_charge) {
		this.person_in_charge = person_in_charge;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote_to_customer() {
		return note_to_customer;
	}
	public void setNote_to_customer(String note_to_customer) {
		this.note_to_customer = note_to_customer;
	}
	public String getInit_purchase_amt() {
		return init_purchase_amt;
	}
	public void setInit_purchase_amt(String init_purchase_amt) {
		this.init_purchase_amt = init_purchase_amt;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getMail_carrier_code() {
		return mail_carrier_code;
	}
	public void setMail_carrier_code(String mail_carrier_code) {
		this.mail_carrier_code = mail_carrier_code;
	}
	public String getGift_check() {
		return gift_check;
	}
	public void setGift_check(String gift_check) {
		this.gift_check = gift_check;
	}
	public String getOrder_comment() {
		return order_comment;
	}
	public void setOrder_comment(String order_comment) {
		this.order_comment = order_comment;
	}
	public String getOrder_datetime() {
		return order_datetime;
	}
	public void setOrder_datetime(String order_datetime) {
		this.order_datetime = order_datetime;
	}
	public String getMulti_destination_flag() {
		return multi_destination_flag;
	}
	public void setMulti_destination_flag(String multi_destination_flag) {
		this.multi_destination_flag = multi_destination_flag;
	}
	public String getWarning_flag() {
		return warning_flag;
	}
	public void setWarning_flag(String warning_flag) {
		this.warning_flag = warning_flag;
	}
	public String getRmember_flag() {
		return rmember_flag;
	}
	public void setRmember_flag(String rmember_flag) {
		this.rmember_flag = rmember_flag;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getConsume_tax() {
		return consume_tax;
	}
	public void setConsume_tax(String consume_tax) {
		this.consume_tax = consume_tax;
	}
	public String getShipping_cost() {
		return shipping_cost;
	}
	public void setShipping_cost(String shipping_cost) {
		this.shipping_cost = shipping_cost;
	}
	public String getSubstitute_cost() {
		return substitute_cost;
	}
	public void setSubstitute_cost(String substitute_cost) {
		this.substitute_cost = substitute_cost;
	}
	public String getBilling_amt() {
		return billing_amt;
	}
	public void setBilling_amt(String billing_amt) {
		this.billing_amt = billing_amt;
	}
	public String getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}
	public String getEnclosed_id() {
		return enclosed_id;
	}
	public void setEnclosed_id(String enclosed_id) {
		this.enclosed_id = enclosed_id;
	}
	public String getEnclosed_status() {
		return enclosed_status;
	}
	public void setEnclosed_status(String enclosed_status) {
		this.enclosed_status = enclosed_status;
	}
	public String getEnclosed_goods_total_amt() {
		return enclosed_goods_total_amt;
	}
	public void setEnclosed_goods_total_amt(String enclosed_goods_total_amt) {
		this.enclosed_goods_total_amt = enclosed_goods_total_amt;
	}
	public String getEnclosed_total_shipping() {
		return enclosed_total_shipping;
	}
	public void setEnclosed_total_shipping(String enclosed_total_shipping) {
		this.enclosed_total_shipping = enclosed_total_shipping;
	}
	public String getEnclosed_total_substitue() {
		return enclosed_total_substitue;
	}
	public void setEnclosed_total_substitue(String enclosed_total_substitue) {
		this.enclosed_total_substitue = enclosed_total_substitue;
	}
	public String getEnclosed_total_consume() {
		return enclosed_total_consume;
	}
	public void setEnclosed_total_consume(String enclosed_total_consume) {
		this.enclosed_total_consume = enclosed_total_consume;
	}
	public String getEnclosed_billing_amt() {
		return enclosed_billing_amt;
	}
	public void setEnclosed_billing_amt(String enclosed_billing_amt) {
		this.enclosed_billing_amt = enclosed_billing_amt;
	}
	public String getEnclosed_total_amt() {
		return enclosed_total_amt;
	}
	public void setEnclosed_total_amt(String enclosed_total_amt) {
		this.enclosed_total_amt = enclosed_total_amt;
	}
	public String getEnclosed_rbank_transfer_fee() {
		return enclosed_rbank_transfer_fee;
	}
	public void setEnclosed_rbank_transfer_fee(String enclosed_rbank_transfer_fee) {
		this.enclosed_rbank_transfer_fee = enclosed_rbank_transfer_fee;
	}
	public String getEnclosed_total_point_usage() {
		return enclosed_total_point_usage;
	}
	public void setEnclosed_total_point_usage(String enclosed_total_point_usage) {
		this.enclosed_total_point_usage = enclosed_total_point_usage;
	}
	public String getMail_flag() {
		return mail_flag;
	}
	public void setMail_flag(String mail_flag) {
		this.mail_flag = mail_flag;
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
	public String getMobile_carrier_payment_no() {
		return mobile_carrier_payment_no;
	}
	public void setMobile_carrier_payment_no(String mobile_carrier_payment_no) {
		this.mobile_carrier_payment_no = mobile_carrier_payment_no;
	}
	public String getPurchase_hist_mod_type() {
		return purchase_hist_mod_type;
	}
	public void setPurchase_hist_mod_type(String purchase_hist_mod_type) {
		this.purchase_hist_mod_type = purchase_hist_mod_type;
	}
	public String getPurchase_hist_mod_icon() {
		return purchase_hist_mod_icon;
	}
	public void setPurchase_hist_mod_icon(String purchase_hist_mod_icon) {
		this.purchase_hist_mod_icon = purchase_hist_mod_icon;
	}
	public String getPurchase_hist_mod_prompt_mail() {
		return purchase_hist_mod_prompt_mail;
	}
	public void setPurchase_hist_mod_prompt_mail(String purchase_hist_mod_prompt_mail) {
		this.purchase_hist_mod_prompt_mail = purchase_hist_mod_prompt_mail;
	}
	public String getDestination_match_flag() {
		return destination_match_flag;
	}
	public void setDestination_match_flag(String destination_match_flag) {
		this.destination_match_flag = destination_match_flag;
	}
	public String getPoint_usage() {
		return point_usage;
	}
	public void setPoint_usage(String point_usage) {
		this.point_usage = point_usage;
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
	public String getOrder_address1() {
		return order_address1;
	}
	public void setOrder_address1(String order_address1) {
		this.order_address1 = order_address1;
	}
	public String getOrder_address2() {
		return order_address2;
	}
	public void setOrder_address2(String order_address2) {
		this.order_address2 = order_address2;
	}
	public String getOrder_address3() {
		return order_address3;
	}
	public void setOrder_address3(String order_address3) {
		this.order_address3 = order_address3;
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
	public String getMail_address() {
		return mail_address;
	}
	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}
	public String getOrder_sex() {
		return order_sex;
	}
	public void setOrder_sex(String order_sex) {
		this.order_sex = order_sex;
	}
	public String getOrder_birth() {
		return order_birth;
	}
	public void setOrder_birth(String order_birth) {
		this.order_birth = order_birth;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getCredit_type() {
		return credit_type;
	}
	public void setCredit_type(String credit_type) {
		this.credit_type = credit_type;
	}
	public String getCredit_no() {
		return credit_no;
	}
	public void setCredit_no(String credit_no) {
		this.credit_no = credit_no;
	}
	public String getCredit_user() {
		return credit_user;
	}
	public void setCredit_user(String credit_user) {
		this.credit_user = credit_user;
	}
	public String getCredit_expire() {
		return credit_expire;
	}
	public void setCredit_expire(String credit_expire) {
		this.credit_expire = credit_expire;
	}
	public String getCredit_installment() {
		return credit_installment;
	}
	public void setCredit_installment(String credit_installment) {
		this.credit_installment = credit_installment;
	}
	public String getCredit_installment_note() {
		return credit_installment_note;
	}
	public void setCredit_installment_note(String credit_installment_note) {
		this.credit_installment_note = credit_installment_note;
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
	public String getPoint_usage_person() {
		return point_usage_person;
	}
	public void setPoint_usage_person(String point_usage_person) {
		this.point_usage_person = point_usage_person;
	}
	public String getPoint_usage_condition() {
		return point_usage_condition;
	}
	public void setPoint_usage_condition(String point_usage_condition) {
		this.point_usage_condition = point_usage_condition;
	}
	public String getPoint_status() {
		return point_status;
	}
	public void setPoint_status(String point_status) {
		this.point_status = point_status;
	}
	public String getRbank_payment_status() {
		return rbank_payment_status;
	}
	public void setRbank_payment_status(String rbank_payment_status) {
		this.rbank_payment_status = rbank_payment_status;
	}
	public String getRbank_payment_transfer_fee() {
		return rbank_payment_transfer_fee;
	}
	public void setRbank_payment_transfer_fee(String rbank_payment_transfer_fee) {
		this.rbank_payment_transfer_fee = rbank_payment_transfer_fee;
	}
	public String getRbank_payment_fee() {
		return rbank_payment_fee;
	}
	public void setRbank_payment_fee(String rbank_payment_fee) {
		this.rbank_payment_fee = rbank_payment_fee;
	}
	public String getWrap_paper_title() {
		return wrap_paper_title;
	}
	public void setWrap_paper_title(String wrap_paper_title) {
		this.wrap_paper_title = wrap_paper_title;
	}
	public String getWrap_paper_name() {
		return wrap_paper_name;
	}
	public void setWrap_paper_name(String wrap_paper_name) {
		this.wrap_paper_name = wrap_paper_name;
	}
	public String getWrap_paper_fee() {
		return wrap_paper_fee;
	}
	public void setWrap_paper_fee(String wrap_paper_fee) {
		this.wrap_paper_fee = wrap_paper_fee;
	}
	public String getWrap_paper_tax_include() {
		return wrap_paper_tax_include;
	}
	public void setWrap_paper_tax_include(String wrap_paper_tax_include) {
		this.wrap_paper_tax_include = wrap_paper_tax_include;
	}
	public String getWrap_ribbon_title() {
		return wrap_ribbon_title;
	}
	public void setWrap_ribbon_title(String wrap_ribbon_title) {
		this.wrap_ribbon_title = wrap_ribbon_title;
	}
	public String getWrap_ribbon_name() {
		return wrap_ribbon_name;
	}
	public void setWrap_ribbon_name(String wrap_ribbon_name) {
		this.wrap_ribbon_name = wrap_ribbon_name;
	}
	public String getWrap_ribbon_fee() {
		return wrap_ribbon_fee;
	}
	public void setWrap_ribbon_fee(String wrap_ribbon_fee) {
		this.wrap_ribbon_fee = wrap_ribbon_fee;
	}
	public String getWrap_ribbon_tax_include() {
		return wrap_ribbon_tax_include;
	}
	public void setWrap_ribbon_tax_include(String wrap_ribbon_tax_include) {
		this.wrap_ribbon_tax_include = wrap_ribbon_tax_include;
	}
	public String getDelivery_cost() {
		return delivery_cost;
	}
	public void setDelivery_cost(String delivery_cost) {
		this.delivery_cost = delivery_cost;
	}
	public String getDelivery_substitute_cost() {
		return delivery_substitute_cost;
	}
	public void setDelivery_substitute_cost(String delivery_substitute_cost) {
		this.delivery_substitute_cost = delivery_substitute_cost;
	}
	public String getDelivery_consume() {
		return delivery_consume;
	}
	public void setDelivery_consume(String delivery_consume) {
		this.delivery_consume = delivery_consume;
	}
	public String getBaggage_claim_no() {
		return baggage_claim_no;
	}
	public void setBaggage_claim_no(String baggage_claim_no) {
		this.baggage_claim_no = baggage_claim_no;
	}
	public String getDelivery_goods_total_amt() {
		return delivery_goods_total_amt;
	}
	public void setDelivery_goods_total_amt(String delivery_goods_total_amt) {
		this.delivery_goods_total_amt = delivery_goods_total_amt;
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
	public String getDelivery_address1() {
		return delivery_address1;
	}
	public void setDelivery_address1(String delivery_address1) {
		this.delivery_address1 = delivery_address1;
	}
	public String getDelivery_address2() {
		return delivery_address2;
	}
	public void setDelivery_address2(String delivery_address2) {
		this.delivery_address2 = delivery_address2;
	}
	public String getDelivery_address3() {
		return delivery_address3;
	}
	public void setDelivery_address3(String delivery_address3) {
		this.delivery_address3 = delivery_address3;
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
		// [CUSTOM] 상품명 및 옵션에 대하여 trim추가
		this.product_name = product_name.trim();
	}
	public String getProduct_no() {
		return product_no;
	}
	public void setProduct_no(String product_no) {
		this.product_no = product_no;
	}
	public String getProduct_url() {
		return product_url;
	}
	public void setProduct_url(String product_url) {
		this.product_url = product_url;
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
	public String getProduct_option() {
		return product_option;
	}
	public void setProduct_option(String product_option) {
		// [CUSTOM] 상품명 및 옵션에 대하여 trim추가
		this.product_option = product_option.trim();
	}
	public String getPoint_percent() {
		return point_percent;
	}
	public void setPoint_percent(String point_percent) {
		this.point_percent = point_percent;
	}
	public String getPoint_type() {
		return point_type;
	}
	public void setPoint_type(String point_type) {
		this.point_type = point_type;
	}
	public String getRecord_no() {
		return record_no;
	}
	public void setRecord_no(String record_no) {
		this.record_no = record_no;
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
	public String getWrap_type_paper() {
		return wrap_type_paper;
	}
	public void setWrap_type_paper(String wrap_type_paper) {
		this.wrap_type_paper = wrap_type_paper;
	}
	public String getWrap_type_ribbon() {
		return wrap_type_ribbon;
	}
	public void setWrap_type_ribbon(String wrap_type_ribbon) {
		this.wrap_type_ribbon = wrap_type_ribbon;
	}
	public String getTomorrow_hope() {
		return tomorrow_hope;
	}
	public void setTomorrow_hope(String tomorrow_hope) {
		this.tomorrow_hope = tomorrow_hope;
	}
	public String getCoupon_usage() {
		return coupon_usage;
	}
	public void setCoupon_usage(String coupon_usage) {
		this.coupon_usage = coupon_usage;
	}
	public String getStore_coupon_usage() {
		return store_coupon_usage;
	}
	public void setStore_coupon_usage(String store_coupon_usage) {
		this.store_coupon_usage = store_coupon_usage;
	}
	public String getRcoupon_usage() {
		return rcoupon_usage;
	}
	public void setRcoupon_usage(String rcoupon_usage) {
		this.rcoupon_usage = rcoupon_usage;
	}
	public String getEnclosed_coupon_usage() {
		return enclosed_coupon_usage;
	}
	public void setEnclosed_coupon_usage(String enclosed_coupon_usage) {
		this.enclosed_coupon_usage = enclosed_coupon_usage;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public String getPharm_flag() {
		return pharm_flag;
	}
	public void setPharm_flag(String pharm_flag) {
		this.pharm_flag = pharm_flag;
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
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	
}
