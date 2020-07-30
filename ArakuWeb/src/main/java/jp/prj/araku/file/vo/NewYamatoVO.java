package jp.prj.araku.file.vo;

import com.opencsv.bean.CsvBindByPosition;

/**
 * 야마토는    お客様管理番号(customer_no) 값을 키로 해서
 * 伝票番号(slip_no) 값을 갱신
 * */
public class NewYamatoVO {
	/**お客様管理番号*/
	@CsvBindByPosition(position=0)
	private String customer_no;
	/**送り状種類*/
	@CsvBindByPosition(position=1)
	private String invoice_type;
	/**クール区分*/
	@CsvBindByPosition(position=2)
	private String cool_type;
	/**伝票番号*/
	@CsvBindByPosition(position=3)
	private String slip_no;
//	/**出荷予定日*/
//	@CsvBindByPosition(position=4)
//	private String estimate_ship_date;
//	/**お届け予定（指定）日*/
//	@CsvBindByPosition(position=5)
//	private String estimate_delivery_date;
//	/**配達時間帯*/
//	@CsvBindByPosition(position=6)
//	private String delivery_time;
//	/**お届け先コード*/
//	@CsvBindByPosition(position=7)
//	private String delivery_code;
//	/**お届け先電話番号*/
//	@CsvBindByPosition(position=8)
//	private String delivery_tel;
//	/**お届け先電話番号枝番*/
//	@CsvBindByPosition(position=9)
//	private String delivery_tel_branch;
//	/**お届け先郵便番号*/
//	@CsvBindByPosition(position=10)
//	private String delivery_post_no;
//	/**お届け先住所*/
//	@CsvBindByPosition(position=11)
//	private String delivery_add;
//	/**お届け先住所（アパートマンション名）*/
//	@CsvBindByPosition(position=12)
//	private String delivery_building;
//	/**お届け先会社・部門名１*/
//	@CsvBindByPosition(position=13)
//	private String delivery_company1;
//	/**お届け先会社・部門名２*/
//	@CsvBindByPosition(position=14)
//	private String delivery_company2;
//	/**お届け先名*/
//	@CsvBindByPosition(position=15)
//	private String delivery_name;
//	/**お届け先名略称カナ*/
//	@CsvBindByPosition(position=16)
//	private String delivery_name_kana;
//	/**敬称*/
//	@CsvBindByPosition(position=17)
//	private String delivery_name_title;
//	/**ご依頼主コード*/
//	@CsvBindByPosition(position=18)
//	private String client_code;
//	/**ご依頼主電話番号*/
//	@CsvBindByPosition(position=19)
//	private String client_tel;
//	/**ご依頼主電話番号枝番*/
//	@CsvBindByPosition(position=20)
//	private String client_tel_branch;
//	/**ご依頼主郵便番号*/
//	@CsvBindByPosition(position=21)
//	private String client_post_no;
//	/**ご依頼主住所*/
//	@CsvBindByPosition(position=22)
//	private String client_add;
//	/**ご依頼主住所（アパートマンション名）*/
//	@CsvBindByPosition(position=23)
//	private String client_building;
//	/**ご依頼主名*/
//	@CsvBindByPosition(position=24)
//	private String client_name;
//	/**ご依頼主略称カナ*/
//	@CsvBindByPosition(position=25)
//	private String client_name_kana;
//	/**品名コード１*/
//	@CsvBindByPosition(position=26)
//	private String product_code1;
//	/**品名１*/
//	@CsvBindByPosition(position=27)
//	private String product_name1;
//	/**品名コード２*/
//	@CsvBindByPosition(position=28)
//	private String product_code2;
//	/**品名２*/
//	@CsvBindByPosition(position=29)
//	private String product_name2;
//	/**荷扱い１*/
//	@CsvBindByPosition(position=30)
//	private String product_handle1;
//	/**荷扱い２*/
//	@CsvBindByPosition(position=31)
//	private String product_handle2;
//	/**記事*/
//	@CsvBindByPosition(position=32)
//	private String comment;
//	/**コレクト代金引換額（税込）*/
//	@CsvBindByPosition(position=33)
//	private String collect_cash;
//	/**コレクト内消費税額等*/
//	@CsvBindByPosition(position=34)
//	private String interval_consume_tax;
//	/**営業所止置き*/
//	@CsvBindByPosition(position=35)
//	private String stop_and_leave;
//	/**営業所コード*/
//	@CsvBindByPosition(position=36)
//	private String office_code;
//	/**発行枚数*/
//	@CsvBindByPosition(position=37)
//	private String issued_no;
//	/**個数口枠の印字*/
//	@CsvBindByPosition(position=38)
//	private String show_no_flag;
//	/**ご請求先顧客コード*/
//	@CsvBindByPosition(position=39)
//	private String bill_customer_code;
//	/**ご請求先分類コード*/
//	@CsvBindByPosition(position=40)
//	private String bill_class_code;
//	/**運賃管理番号*/
//	@CsvBindByPosition(position=41)
//	private String fare_manage_no;
//	/**クロネコwebコレクトデータ登録*/
//	@CsvBindByPosition(position=42)
//	private String kuroneko_collect_register;
//	/**クロネコwebコレクト加盟店番号*/
//	@CsvBindByPosition(position=43)
//	private String kuroneko_collect_member_no;
//	/**クロネコwebコレクト申込受付番号１*/
//	@CsvBindByPosition(position=44)
//	private String kuroneko_collect_apply_no1;
//	/**クロネコwebコレクト申込受付番号２*/
//	@CsvBindByPosition(position=45)
//	private String kuroneko_collect_apply_no2;
//	/**クロネコwebコレクト申込受付番号３*/
//	@CsvBindByPosition(position=46)
//	private String kuroneko_collect_apply_no3;
//	/**お届け予定ｅメール利用区分*/
//	@CsvBindByPosition(position=47)
//	private String email_status_usage;
//	/**お届け予定ｅメールe-mailアドレス*/
//	@CsvBindByPosition(position=48)
//	private String email_status_add;
//	/**入力機種*/
//	@CsvBindByPosition(position=49)
//	private String input_type;
//	/**お届け予定eメールメッセージ*/
//	@CsvBindByPosition(position=50)
//	private String email_status_msg;
//	/**お届け完了eメール利用区分*/
//	@CsvBindByPosition(position=51)
//	private String email_complete_usage;
//	/**お届け完了ｅメールe-mailアドレス*/
//	@CsvBindByPosition(position=52)
//	private String email_complete_add;
//	/**お届け完了ｅメールメッセージ*/
//	@CsvBindByPosition(position=53)
//	private String email_complete_msg;
//	/**クロネコ収納代行利用区分*/
//	@CsvBindByPosition(position=54)
//	private String kuroneko_substitute_usage;
//	/**収納代行決済ＱＲコード印刷*/
//	@CsvBindByPosition(position=55)
//	private String reserved_cal1;
//	/**収納代行請求金額(税込)*/
//	@CsvBindByPosition(position=56)
//	private String substitute_amount;
//	/**収納代行内消費税額等*/
//	@CsvBindByPosition(position=57)
//	private String substitute_tax;
//	/**収納代行請求先郵便番号*/
//	@CsvBindByPosition(position=58)
//	private String substitute_bill_post_no;
//	/**収納代行請求先住所*/
//	@CsvBindByPosition(position=59)
//	private String substitute_bill_add;
//	/**収納代行請求先住所（アパートマンション名）*/
//	@CsvBindByPosition(position=60)
//	private String substitute_bill_building;
//	/**収納代行請求先会社・部門名１*/
//	@CsvBindByPosition(position=61)
//	private String substitute_bill_company1;
//	/**収納代行請求先会社・部門名２*/
//	@CsvBindByPosition(position=62)
//	private String substitute_bill_company2;
//	/**収納代行請求先名(漢字)*/
//	@CsvBindByPosition(position=63)
//	private String substitute_bill_name;
//	/**収納代行請求先名(カナ)*/
//	@CsvBindByPosition(position=64)
//	private String substitute_bill_name_kana;
//	/**収納代行問合せ先名(漢字)*/
//	@CsvBindByPosition(position=65)
//	private String substitute_contact_name;
//	/**収納代行問合せ先郵便番号*/
//	@CsvBindByPosition(position=66)
//	private String substitute_contact_post_no;
//	/**収納代行問合せ先住所*/
//	@CsvBindByPosition(position=67)
//	private String substitute_contact_add;
//	/**収納代行問合せ先住所（アパートマンション名）*/
//	@CsvBindByPosition(position=68)
//	private String substitute_contact_building;
//	/**収納代行問合せ先電話番号*/
//	@CsvBindByPosition(position=69)
//	private String substitute_contact_tel;
//	/**収納代行管理番号*/
//	@CsvBindByPosition(position=70)
//	private String substitute_manage_no;
//	/**収納代行品名*/
//	@CsvBindByPosition(position=71)
//	private String substitute_product_name;
//	/**収納代行備考*/
//	@CsvBindByPosition(position=72)
//	private String substitute_note;
//	/**複数口くくりキー*/
//	@CsvBindByPosition(position=73)
//	private String multiple_key;
//	/**検索キータイトル１*/
//	@CsvBindByPosition(position=74)
//	private String search_key_title1;
//	/**検索キー１*/
//	@CsvBindByPosition(position=75)
//	private String search_key1;
//	/**検索キータイトル２*/
//	@CsvBindByPosition(position=76)
//	private String search_key_title2;
//	/**検索キー２*/
//	@CsvBindByPosition(position=77)
//	private String search_key2;
//	/**検索キータイトル３*/
//	@CsvBindByPosition(position=78)
//	private String search_key_title3;
//	/**検索キー３*/
//	@CsvBindByPosition(position=79)
//	private String search_key3;
//	/**検索キータイトル４*/
//	@CsvBindByPosition(position=80)
//	private String search_key_title4;
//	/**検索キー４*/
//	@CsvBindByPosition(position=81)
//	private String search_key4;
//	/**検索キータイトル５*/
//	@CsvBindByPosition(position=82)
//	private String search_key_title5;
//	/**検索キー５*/
//	@CsvBindByPosition(position=83)
//	private String search_key5;
//	/**予備*/
//	@CsvBindByPosition(position=84)
//	private String reserved_cal2;
//	/**予備*/
//	@CsvBindByPosition(position=85)
//	private String reserved_cal3;
//	/**投函予定メール利用区分*/
//	@CsvBindByPosition(position=86)
//	private String posting_status_mail_usage;
//	/**投函予定メールe-mailアドレス*/
//	@CsvBindByPosition(position=87)
//	private String posting_status_mail_add;
//	/**投函予定メールメッセージ*/
//	@CsvBindByPosition(position=88)
//	private String posting_status_mail_msg;
//	/**投函完了メール（お届け先宛）利用区分*/
//	@CsvBindByPosition(position=89)
//	private String posting_delivery_complete_mail_usage;
//	/**投函完了メール（お届け先宛）e-mailアドレス*/
//	@CsvBindByPosition(position=90)
//	private String posting_delivery_complete_mail_add;
//	/**投函完了メール（お届け先宛）メールメッセージ*/
//	@CsvBindByPosition(position=91)
//	private String posting_delivery_complete_mail_msg;
//	/**投函完了メール（ご依頼主宛）利用区分*/
//	@CsvBindByPosition(position=92)
//	private String posting_client_complete_mail_usage;
//	/**投函完了メール（ご依頼主宛）e-mailアドレス*/
//	@CsvBindByPosition(position=93)
//	private String posting_client_complete_mail_add;
//	/**投函完了メール（ご依頼主宛）メールメッセージ*/
//	@CsvBindByPosition(position=94)
//	private String posting_client_complete_mail_msg;
//	/**連携管理番号*/
//	@CsvBindByPosition(position=95)
//	private String link_manage_no;
//	/**通知メールアドレス*/
//	@CsvBindByPosition(position=96)
//	private String noti_mail;
	
	public String getCustomer_no() {
		return customer_no;
	}
	public void setCustomer_no(String customer_no) {
		this.customer_no = customer_no;
	}
	public String getInvoice_type() {
		return invoice_type;
	}
	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}
	public String getCool_type() {
		return cool_type;
	}
	public void setCool_type(String cool_type) {
		this.cool_type = cool_type;
	}
	public String getSlip_no() {
		return slip_no;
	}
	public void setSlip_no(String slip_no) {
		this.slip_no = slip_no;
	}
//	public String getEstimate_ship_date() {
//		return estimate_ship_date;
//	}
//	public void setEstimate_ship_date(String estimate_ship_date) {
//		this.estimate_ship_date = estimate_ship_date;
//	}
//	public String getEstimate_delivery_date() {
//		return estimate_delivery_date;
//	}
//	public void setEstimate_delivery_date(String estimate_delivery_date) {
//		this.estimate_delivery_date = estimate_delivery_date;
//	}
//	public String getDelivery_time() {
//		return delivery_time;
//	}
//	public void setDelivery_time(String delivery_time) {
//		this.delivery_time = delivery_time;
//	}
//	public String getDelivery_code() {
//		return delivery_code;
//	}
//	public void setDelivery_code(String delivery_code) {
//		this.delivery_code = delivery_code;
//	}
//	public String getDelivery_tel() {
//		return delivery_tel;
//	}
//	public void setDelivery_tel(String delivery_tel) {
//		this.delivery_tel = delivery_tel;
//	}
//	public String getDelivery_tel_branch() {
//		return delivery_tel_branch;
//	}
//	public void setDelivery_tel_branch(String delivery_tel_branch) {
//		this.delivery_tel_branch = delivery_tel_branch;
//	}
//	public String getDelivery_post_no() {
//		return delivery_post_no;
//	}
//	public void setDelivery_post_no(String delivery_post_no) {
//		this.delivery_post_no = delivery_post_no;
//	}
//	public String getDelivery_add() {
//		return delivery_add;
//	}
//	public void setDelivery_add(String delivery_add) {
//		this.delivery_add = delivery_add;
//	}
//	public String getDelivery_building() {
//		return delivery_building;
//	}
//	public void setDelivery_building(String delivery_building) {
//		this.delivery_building = delivery_building;
//	}
//	public String getDelivery_company1() {
//		return delivery_company1;
//	}
//	public void setDelivery_company1(String delivery_company1) {
//		this.delivery_company1 = delivery_company1;
//	}
//	public String getDelivery_company2() {
//		return delivery_company2;
//	}
//	public void setDelivery_company2(String delivery_company2) {
//		this.delivery_company2 = delivery_company2;
//	}
//	public String getDelivery_name() {
//		return delivery_name;
//	}
//	public void setDelivery_name(String delivery_name) {
//		this.delivery_name = delivery_name;
//	}
//	public String getDelivery_name_kana() {
//		return delivery_name_kana;
//	}
//	public void setDelivery_name_kana(String delivery_name_kana) {
//		this.delivery_name_kana = delivery_name_kana;
//	}
//	public String getDelivery_name_title() {
//		return delivery_name_title;
//	}
//	public void setDelivery_name_title(String delivery_name_title) {
//		this.delivery_name_title = delivery_name_title;
//	}
//	public String getClient_code() {
//		return client_code;
//	}
//	public void setClient_code(String client_code) {
//		this.client_code = client_code;
//	}
//	public String getClient_tel() {
//		return client_tel;
//	}
//	public void setClient_tel(String client_tel) {
//		this.client_tel = client_tel;
//	}
//	public String getClient_tel_branch() {
//		return client_tel_branch;
//	}
//	public void setClient_tel_branch(String client_tel_branch) {
//		this.client_tel_branch = client_tel_branch;
//	}
//	public String getClient_post_no() {
//		return client_post_no;
//	}
//	public void setClient_post_no(String client_post_no) {
//		this.client_post_no = client_post_no;
//	}
//	public String getClient_add() {
//		return client_add;
//	}
//	public void setClient_add(String client_add) {
//		this.client_add = client_add;
//	}
//	public String getClient_building() {
//		return client_building;
//	}
//	public void setClient_building(String client_building) {
//		this.client_building = client_building;
//	}
//	public String getClient_name() {
//		return client_name;
//	}
//	public void setClient_name(String client_name) {
//		this.client_name = client_name;
//	}
//	public String getClient_name_kana() {
//		return client_name_kana;
//	}
//	public void setClient_name_kana(String client_name_kana) {
//		this.client_name_kana = client_name_kana;
//	}
//	public String getProduct_code1() {
//		return product_code1;
//	}
//	public void setProduct_code1(String product_code1) {
//		this.product_code1 = product_code1;
//	}
//	public String getProduct_name1() {
//		return product_name1;
//	}
//	public void setProduct_name1(String product_name1) {
//		this.product_name1 = product_name1;
//	}
//	public String getProduct_code2() {
//		return product_code2;
//	}
//	public void setProduct_code2(String product_code2) {
//		this.product_code2 = product_code2;
//	}
//	public String getProduct_name2() {
//		return product_name2;
//	}
//	public void setProduct_name2(String product_name2) {
//		this.product_name2 = product_name2;
//	}
//	public String getProduct_handle1() {
//		return product_handle1;
//	}
//	public void setProduct_handle1(String product_handle1) {
//		this.product_handle1 = product_handle1;
//	}
//	public String getProduct_handle2() {
//		return product_handle2;
//	}
//	public void setProduct_handle2(String product_handle2) {
//		this.product_handle2 = product_handle2;
//	}
//	public String getComment() {
//		return comment;
//	}
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//	public String getCollect_cash() {
//		return collect_cash;
//	}
//	public void setCollect_cash(String collect_cash) {
//		this.collect_cash = collect_cash;
//	}
//	public String getInterval_consume_tax() {
//		return interval_consume_tax;
//	}
//	public void setInterval_consume_tax(String interval_consume_tax) {
//		this.interval_consume_tax = interval_consume_tax;
//	}
//	public String getStop_and_leave() {
//		return stop_and_leave;
//	}
//	public void setStop_and_leave(String stop_and_leave) {
//		this.stop_and_leave = stop_and_leave;
//	}
//	public String getOffice_code() {
//		return office_code;
//	}
//	public void setOffice_code(String office_code) {
//		this.office_code = office_code;
//	}
//	public String getIssued_no() {
//		return issued_no;
//	}
//	public void setIssued_no(String issued_no) {
//		this.issued_no = issued_no;
//	}
//	public String getShow_no_flag() {
//		return show_no_flag;
//	}
//	public void setShow_no_flag(String show_no_flag) {
//		this.show_no_flag = show_no_flag;
//	}
//	public String getBill_customer_code() {
//		return bill_customer_code;
//	}
//	public void setBill_customer_code(String bill_customer_code) {
//		this.bill_customer_code = bill_customer_code;
//	}
//	public String getBill_class_code() {
//		return bill_class_code;
//	}
//	public void setBill_class_code(String bill_class_code) {
//		this.bill_class_code = bill_class_code;
//	}
//	public String getFare_manage_no() {
//		return fare_manage_no;
//	}
//	public void setFare_manage_no(String fare_manage_no) {
//		this.fare_manage_no = fare_manage_no;
//	}
//	public String getKuroneko_collect_register() {
//		return kuroneko_collect_register;
//	}
//	public void setKuroneko_collect_register(String kuroneko_collect_register) {
//		this.kuroneko_collect_register = kuroneko_collect_register;
//	}
//	public String getKuroneko_collect_member_no() {
//		return kuroneko_collect_member_no;
//	}
//	public void setKuroneko_collect_member_no(String kuroneko_collect_member_no) {
//		this.kuroneko_collect_member_no = kuroneko_collect_member_no;
//	}
//	public String getKuroneko_collect_apply_no1() {
//		return kuroneko_collect_apply_no1;
//	}
//	public void setKuroneko_collect_apply_no1(String kuroneko_collect_apply_no1) {
//		this.kuroneko_collect_apply_no1 = kuroneko_collect_apply_no1;
//	}
//	public String getKuroneko_collect_apply_no2() {
//		return kuroneko_collect_apply_no2;
//	}
//	public void setKuroneko_collect_apply_no2(String kuroneko_collect_apply_no2) {
//		this.kuroneko_collect_apply_no2 = kuroneko_collect_apply_no2;
//	}
//	public String getKuroneko_collect_apply_no3() {
//		return kuroneko_collect_apply_no3;
//	}
//	public void setKuroneko_collect_apply_no3(String kuroneko_collect_apply_no3) {
//		this.kuroneko_collect_apply_no3 = kuroneko_collect_apply_no3;
//	}
//	public String getEmail_status_usage() {
//		return email_status_usage;
//	}
//	public void setEmail_status_usage(String email_status_usage) {
//		this.email_status_usage = email_status_usage;
//	}
//	public String getEmail_status_add() {
//		return email_status_add;
//	}
//	public void setEmail_status_add(String email_status_add) {
//		this.email_status_add = email_status_add;
//	}
//	public String getInput_type() {
//		return input_type;
//	}
//	public void setInput_type(String input_type) {
//		this.input_type = input_type;
//	}
//	public String getEmail_status_msg() {
//		return email_status_msg;
//	}
//	public void setEmail_status_msg(String email_status_msg) {
//		this.email_status_msg = email_status_msg;
//	}
//	public String getEmail_complete_usage() {
//		return email_complete_usage;
//	}
//	public void setEmail_complete_usage(String email_complete_usage) {
//		this.email_complete_usage = email_complete_usage;
//	}
//	public String getEmail_complete_add() {
//		return email_complete_add;
//	}
//	public void setEmail_complete_add(String email_complete_add) {
//		this.email_complete_add = email_complete_add;
//	}
//	public String getEmail_complete_msg() {
//		return email_complete_msg;
//	}
//	public void setEmail_complete_msg(String email_complete_msg) {
//		this.email_complete_msg = email_complete_msg;
//	}
//	public String getKuroneko_substitute_usage() {
//		return kuroneko_substitute_usage;
//	}
//	public void setKuroneko_substitute_usage(String kuroneko_substitute_usage) {
//		this.kuroneko_substitute_usage = kuroneko_substitute_usage;
//	}
//	public String getReserved_cal1() {
//		return reserved_cal1;
//	}
//	public void setReserved_cal1(String reserved_cal1) {
//		this.reserved_cal1 = reserved_cal1;
//	}
//	public String getSubstitute_amount() {
//		return substitute_amount;
//	}
//	public void setSubstitute_amount(String substitute_amount) {
//		this.substitute_amount = substitute_amount;
//	}
//	public String getSubstitute_tax() {
//		return substitute_tax;
//	}
//	public void setSubstitute_tax(String substitute_tax) {
//		this.substitute_tax = substitute_tax;
//	}
//	public String getSubstitute_bill_post_no() {
//		return substitute_bill_post_no;
//	}
//	public void setSubstitute_bill_post_no(String substitute_bill_post_no) {
//		this.substitute_bill_post_no = substitute_bill_post_no;
//	}
//	public String getSubstitute_bill_add() {
//		return substitute_bill_add;
//	}
//	public void setSubstitute_bill_add(String substitute_bill_add) {
//		this.substitute_bill_add = substitute_bill_add;
//	}
//	public String getSubstitute_bill_building() {
//		return substitute_bill_building;
//	}
//	public void setSubstitute_bill_building(String substitute_bill_building) {
//		this.substitute_bill_building = substitute_bill_building;
//	}
//	public String getSubstitute_bill_company1() {
//		return substitute_bill_company1;
//	}
//	public void setSubstitute_bill_company1(String substitute_bill_company1) {
//		this.substitute_bill_company1 = substitute_bill_company1;
//	}
//	public String getSubstitute_bill_company2() {
//		return substitute_bill_company2;
//	}
//	public void setSubstitute_bill_company2(String substitute_bill_company2) {
//		this.substitute_bill_company2 = substitute_bill_company2;
//	}
//	public String getSubstitute_bill_name() {
//		return substitute_bill_name;
//	}
//	public void setSubstitute_bill_name(String substitute_bill_name) {
//		this.substitute_bill_name = substitute_bill_name;
//	}
//	public String getSubstitute_bill_name_kana() {
//		return substitute_bill_name_kana;
//	}
//	public void setSubstitute_bill_name_kana(String substitute_bill_name_kana) {
//		this.substitute_bill_name_kana = substitute_bill_name_kana;
//	}
//	public String getSubstitute_contact_name() {
//		return substitute_contact_name;
//	}
//	public void setSubstitute_contact_name(String substitute_contact_name) {
//		this.substitute_contact_name = substitute_contact_name;
//	}
//	public String getSubstitute_contact_post_no() {
//		return substitute_contact_post_no;
//	}
//	public void setSubstitute_contact_post_no(String substitute_contact_post_no) {
//		this.substitute_contact_post_no = substitute_contact_post_no;
//	}
//	public String getSubstitute_contact_add() {
//		return substitute_contact_add;
//	}
//	public void setSubstitute_contact_add(String substitute_contact_add) {
//		this.substitute_contact_add = substitute_contact_add;
//	}
//	public String getSubstitute_contact_building() {
//		return substitute_contact_building;
//	}
//	public void setSubstitute_contact_building(String substitute_contact_building) {
//		this.substitute_contact_building = substitute_contact_building;
//	}
//	public String getSubstitute_contact_tel() {
//		return substitute_contact_tel;
//	}
//	public void setSubstitute_contact_tel(String substitute_contact_tel) {
//		this.substitute_contact_tel = substitute_contact_tel;
//	}
//	public String getSubstitute_manage_no() {
//		return substitute_manage_no;
//	}
//	public void setSubstitute_manage_no(String substitute_manage_no) {
//		this.substitute_manage_no = substitute_manage_no;
//	}
//	public String getSubstitute_product_name() {
//		return substitute_product_name;
//	}
//	public void setSubstitute_product_name(String substitute_product_name) {
//		this.substitute_product_name = substitute_product_name;
//	}
//	public String getSubstitute_note() {
//		return substitute_note;
//	}
//	public void setSubstitute_note(String substitute_note) {
//		this.substitute_note = substitute_note;
//	}
//	public String getMultiple_key() {
//		return multiple_key;
//	}
//	public void setMultiple_key(String multiple_key) {
//		this.multiple_key = multiple_key;
//	}
//	public String getSearch_key_title1() {
//		return search_key_title1;
//	}
//	public void setSearch_key_title1(String search_key_title1) {
//		this.search_key_title1 = search_key_title1;
//	}
//	public String getSearch_key1() {
//		return search_key1;
//	}
//	public void setSearch_key1(String search_key1) {
//		this.search_key1 = search_key1;
//	}
//	public String getSearch_key_title2() {
//		return search_key_title2;
//	}
//	public void setSearch_key_title2(String search_key_title2) {
//		this.search_key_title2 = search_key_title2;
//	}
//	public String getSearch_key2() {
//		return search_key2;
//	}
//	public void setSearch_key2(String search_key2) {
//		this.search_key2 = search_key2;
//	}
//	public String getSearch_key_title3() {
//		return search_key_title3;
//	}
//	public void setSearch_key_title3(String search_key_title3) {
//		this.search_key_title3 = search_key_title3;
//	}
//	public String getSearch_key3() {
//		return search_key3;
//	}
//	public void setSearch_key3(String search_key3) {
//		this.search_key3 = search_key3;
//	}
//	public String getSearch_key_title4() {
//		return search_key_title4;
//	}
//	public void setSearch_key_title4(String search_key_title4) {
//		this.search_key_title4 = search_key_title4;
//	}
//	public String getSearch_key4() {
//		return search_key4;
//	}
//	public void setSearch_key4(String search_key4) {
//		this.search_key4 = search_key4;
//	}
//	public String getSearch_key_title5() {
//		return search_key_title5;
//	}
//	public void setSearch_key_title5(String search_key_title5) {
//		this.search_key_title5 = search_key_title5;
//	}
//	public String getSearch_key5() {
//		return search_key5;
//	}
//	public void setSearch_key5(String search_key5) {
//		this.search_key5 = search_key5;
//	}
//	public String getReserved_cal2() {
//		return reserved_cal2;
//	}
//	public void setReserved_cal2(String reserved_cal2) {
//		this.reserved_cal2 = reserved_cal2;
//	}
//	public String getReserved_cal3() {
//		return reserved_cal3;
//	}
//	public void setReserved_cal3(String reserved_cal3) {
//		this.reserved_cal3 = reserved_cal3;
//	}
//	public String getPosting_status_mail_usage() {
//		return posting_status_mail_usage;
//	}
//	public void setPosting_status_mail_usage(String posting_status_mail_usage) {
//		this.posting_status_mail_usage = posting_status_mail_usage;
//	}
//	public String getPosting_status_mail_add() {
//		return posting_status_mail_add;
//	}
//	public void setPosting_status_mail_add(String posting_status_mail_add) {
//		this.posting_status_mail_add = posting_status_mail_add;
//	}
//	public String getPosting_status_mail_msg() {
//		return posting_status_mail_msg;
//	}
//	public void setPosting_status_mail_msg(String posting_status_mail_msg) {
//		this.posting_status_mail_msg = posting_status_mail_msg;
//	}
//	public String getPosting_delivery_complete_mail_usage() {
//		return posting_delivery_complete_mail_usage;
//	}
//	public void setPosting_delivery_complete_mail_usage(String posting_delivery_complete_mail_usage) {
//		this.posting_delivery_complete_mail_usage = posting_delivery_complete_mail_usage;
//	}
//	public String getPosting_delivery_complete_mail_add() {
//		return posting_delivery_complete_mail_add;
//	}
//	public void setPosting_delivery_complete_mail_add(String posting_delivery_complete_mail_add) {
//		this.posting_delivery_complete_mail_add = posting_delivery_complete_mail_add;
//	}
//	public String getPosting_delivery_complete_mail_msg() {
//		return posting_delivery_complete_mail_msg;
//	}
//	public void setPosting_delivery_complete_mail_msg(String posting_delivery_complete_mail_msg) {
//		this.posting_delivery_complete_mail_msg = posting_delivery_complete_mail_msg;
//	}
//	public String getPosting_client_complete_mail_usage() {
//		return posting_client_complete_mail_usage;
//	}
//	public void setPosting_client_complete_mail_usage(String posting_client_complete_mail_usage) {
//		this.posting_client_complete_mail_usage = posting_client_complete_mail_usage;
//	}
//	public String getPosting_client_complete_mail_add() {
//		return posting_client_complete_mail_add;
//	}
//	public void setPosting_client_complete_mail_add(String posting_client_complete_mail_add) {
//		this.posting_client_complete_mail_add = posting_client_complete_mail_add;
//	}
//	public String getPosting_client_complete_mail_msg() {
//		return posting_client_complete_mail_msg;
//	}
//	public void setPosting_client_complete_mail_msg(String posting_client_complete_mail_msg) {
//		this.posting_client_complete_mail_msg = posting_client_complete_mail_msg;
//	}
//	public String getLink_manage_no() {
//		return link_manage_no;
//	}
//	public void setLink_manage_no(String link_manage_no) {
//		this.link_manage_no = link_manage_no;
//	}
//	public String getNoti_mail() {
//		return noti_mail;
//	}
//	public void setNoti_mail(String noti_mail) {
//		this.noti_mail = noti_mail;
//	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewYamatoVO [");
		if (customer_no != null) {
			builder.append("customer_no=");
			builder.append(customer_no);
			builder.append(", ");
		}
		if (invoice_type != null) {
			builder.append("invoice_type=");
			builder.append(invoice_type);
			builder.append(", ");
		}
		if (cool_type != null) {
			builder.append("cool_type=");
			builder.append(cool_type);
			builder.append(", ");
		}
		if (slip_no != null) {
			builder.append("slip_no=");
			builder.append(slip_no);
			builder.append(", ");
		}
//		if (estimate_ship_date != null) {
//			builder.append("estimate_ship_date=");
//			builder.append(estimate_ship_date);
//			builder.append(", ");
//		}
//		if (estimate_delivery_date != null) {
//			builder.append("estimate_delivery_date=");
//			builder.append(estimate_delivery_date);
//			builder.append(", ");
//		}
//		if (delivery_time != null) {
//			builder.append("delivery_time=");
//			builder.append(delivery_time);
//			builder.append(", ");
//		}
//		if (delivery_code != null) {
//			builder.append("delivery_code=");
//			builder.append(delivery_code);
//			builder.append(", ");
//		}
//		if (delivery_tel != null) {
//			builder.append("delivery_tel=");
//			builder.append(delivery_tel);
//			builder.append(", ");
//		}
//		if (delivery_tel_branch != null) {
//			builder.append("delivery_tel_branch=");
//			builder.append(delivery_tel_branch);
//			builder.append(", ");
//		}
//		if (delivery_post_no != null) {
//			builder.append("delivery_post_no=");
//			builder.append(delivery_post_no);
//			builder.append(", ");
//		}
//		if (delivery_add != null) {
//			builder.append("delivery_add=");
//			builder.append(delivery_add);
//			builder.append(", ");
//		}
//		if (delivery_building != null) {
//			builder.append("delivery_building=");
//			builder.append(delivery_building);
//			builder.append(", ");
//		}
//		if (delivery_company1 != null) {
//			builder.append("delivery_company1=");
//			builder.append(delivery_company1);
//			builder.append(", ");
//		}
//		if (delivery_company2 != null) {
//			builder.append("delivery_company2=");
//			builder.append(delivery_company2);
//			builder.append(", ");
//		}
//		if (delivery_name != null) {
//			builder.append("delivery_name=");
//			builder.append(delivery_name);
//			builder.append(", ");
//		}
//		if (delivery_name_kana != null) {
//			builder.append("delivery_name_kana=");
//			builder.append(delivery_name_kana);
//			builder.append(", ");
//		}
//		if (delivery_name_title != null) {
//			builder.append("delivery_name_title=");
//			builder.append(delivery_name_title);
//			builder.append(", ");
//		}
//		if (client_code != null) {
//			builder.append("client_code=");
//			builder.append(client_code);
//			builder.append(", ");
//		}
//		if (client_tel != null) {
//			builder.append("client_tel=");
//			builder.append(client_tel);
//			builder.append(", ");
//		}
//		if (client_tel_branch != null) {
//			builder.append("client_tel_branch=");
//			builder.append(client_tel_branch);
//			builder.append(", ");
//		}
//		if (client_post_no != null) {
//			builder.append("client_post_no=");
//			builder.append(client_post_no);
//			builder.append(", ");
//		}
//		if (client_add != null) {
//			builder.append("client_add=");
//			builder.append(client_add);
//			builder.append(", ");
//		}
//		if (client_building != null) {
//			builder.append("client_building=");
//			builder.append(client_building);
//			builder.append(", ");
//		}
//		if (client_name != null) {
//			builder.append("client_name=");
//			builder.append(client_name);
//			builder.append(", ");
//		}
//		if (client_name_kana != null) {
//			builder.append("client_name_kana=");
//			builder.append(client_name_kana);
//			builder.append(", ");
//		}
//		if (product_code1 != null) {
//			builder.append("product_code1=");
//			builder.append(product_code1);
//			builder.append(", ");
//		}
//		if (product_name1 != null) {
//			builder.append("product_name1=");
//			builder.append(product_name1);
//			builder.append(", ");
//		}
//		if (product_code2 != null) {
//			builder.append("product_code2=");
//			builder.append(product_code2);
//			builder.append(", ");
//		}
//		if (product_name2 != null) {
//			builder.append("product_name2=");
//			builder.append(product_name2);
//			builder.append(", ");
//		}
//		if (product_handle1 != null) {
//			builder.append("product_handle1=");
//			builder.append(product_handle1);
//			builder.append(", ");
//		}
//		if (product_handle2 != null) {
//			builder.append("product_handle2=");
//			builder.append(product_handle2);
//			builder.append(", ");
//		}
//		if (comment != null) {
//			builder.append("comment=");
//			builder.append(comment);
//			builder.append(", ");
//		}
//		if (collect_cash != null) {
//			builder.append("collect_cash=");
//			builder.append(collect_cash);
//			builder.append(", ");
//		}
//		if (interval_consume_tax != null) {
//			builder.append("interval_consume_tax=");
//			builder.append(interval_consume_tax);
//			builder.append(", ");
//		}
//		if (stop_and_leave != null) {
//			builder.append("stop_and_leave=");
//			builder.append(stop_and_leave);
//			builder.append(", ");
//		}
//		if (office_code != null) {
//			builder.append("office_code=");
//			builder.append(office_code);
//			builder.append(", ");
//		}
//		if (issued_no != null) {
//			builder.append("issued_no=");
//			builder.append(issued_no);
//			builder.append(", ");
//		}
//		if (show_no_flag != null) {
//			builder.append("show_no_flag=");
//			builder.append(show_no_flag);
//			builder.append(", ");
//		}
//		if (bill_customer_code != null) {
//			builder.append("bill_customer_code=");
//			builder.append(bill_customer_code);
//			builder.append(", ");
//		}
//		if (bill_class_code != null) {
//			builder.append("bill_class_code=");
//			builder.append(bill_class_code);
//			builder.append(", ");
//		}
//		if (fare_manage_no != null) {
//			builder.append("fare_manage_no=");
//			builder.append(fare_manage_no);
//			builder.append(", ");
//		}
//		if (kuroneko_collect_register != null) {
//			builder.append("kuroneko_collect_register=");
//			builder.append(kuroneko_collect_register);
//			builder.append(", ");
//		}
//		if (kuroneko_collect_member_no != null) {
//			builder.append("kuroneko_collect_member_no=");
//			builder.append(kuroneko_collect_member_no);
//			builder.append(", ");
//		}
//		if (kuroneko_collect_apply_no1 != null) {
//			builder.append("kuroneko_collect_apply_no1=");
//			builder.append(kuroneko_collect_apply_no1);
//			builder.append(", ");
//		}
//		if (kuroneko_collect_apply_no2 != null) {
//			builder.append("kuroneko_collect_apply_no2=");
//			builder.append(kuroneko_collect_apply_no2);
//			builder.append(", ");
//		}
//		if (kuroneko_collect_apply_no3 != null) {
//			builder.append("kuroneko_collect_apply_no3=");
//			builder.append(kuroneko_collect_apply_no3);
//			builder.append(", ");
//		}
//		if (email_status_usage != null) {
//			builder.append("email_status_usage=");
//			builder.append(email_status_usage);
//			builder.append(", ");
//		}
//		if (email_status_add != null) {
//			builder.append("email_status_add=");
//			builder.append(email_status_add);
//			builder.append(", ");
//		}
//		if (input_type != null) {
//			builder.append("input_type=");
//			builder.append(input_type);
//			builder.append(", ");
//		}
//		if (email_status_msg != null) {
//			builder.append("email_status_msg=");
//			builder.append(email_status_msg);
//			builder.append(", ");
//		}
//		if (email_complete_usage != null) {
//			builder.append("email_complete_usage=");
//			builder.append(email_complete_usage);
//			builder.append(", ");
//		}
//		if (email_complete_add != null) {
//			builder.append("email_complete_add=");
//			builder.append(email_complete_add);
//			builder.append(", ");
//		}
//		if (email_complete_msg != null) {
//			builder.append("email_complete_msg=");
//			builder.append(email_complete_msg);
//			builder.append(", ");
//		}
//		if (kuroneko_substitute_usage != null) {
//			builder.append("kuroneko_substitute_usage=");
//			builder.append(kuroneko_substitute_usage);
//			builder.append(", ");
//		}
//		if (reserved_cal1 != null) {
//			builder.append("reserved_cal1=");
//			builder.append(reserved_cal1);
//			builder.append(", ");
//		}
//		if (substitute_amount != null) {
//			builder.append("substitute_amount=");
//			builder.append(substitute_amount);
//			builder.append(", ");
//		}
//		if (substitute_tax != null) {
//			builder.append("substitute_tax=");
//			builder.append(substitute_tax);
//			builder.append(", ");
//		}
//		if (substitute_bill_post_no != null) {
//			builder.append("substitute_bill_post_no=");
//			builder.append(substitute_bill_post_no);
//			builder.append(", ");
//		}
//		if (substitute_bill_add != null) {
//			builder.append("substitute_bill_add=");
//			builder.append(substitute_bill_add);
//			builder.append(", ");
//		}
//		if (substitute_bill_building != null) {
//			builder.append("substitute_bill_building=");
//			builder.append(substitute_bill_building);
//			builder.append(", ");
//		}
//		if (substitute_bill_company1 != null) {
//			builder.append("substitute_bill_company1=");
//			builder.append(substitute_bill_company1);
//			builder.append(", ");
//		}
//		if (substitute_bill_company2 != null) {
//			builder.append("substitute_bill_company2=");
//			builder.append(substitute_bill_company2);
//			builder.append(", ");
//		}
//		if (substitute_bill_name != null) {
//			builder.append("substitute_bill_name=");
//			builder.append(substitute_bill_name);
//			builder.append(", ");
//		}
//		if (substitute_bill_name_kana != null) {
//			builder.append("substitute_bill_name_kana=");
//			builder.append(substitute_bill_name_kana);
//			builder.append(", ");
//		}
//		if (substitute_contact_name != null) {
//			builder.append("substitute_contact_name=");
//			builder.append(substitute_contact_name);
//			builder.append(", ");
//		}
//		if (substitute_contact_post_no != null) {
//			builder.append("substitute_contact_post_no=");
//			builder.append(substitute_contact_post_no);
//			builder.append(", ");
//		}
//		if (substitute_contact_add != null) {
//			builder.append("substitute_contact_add=");
//			builder.append(substitute_contact_add);
//			builder.append(", ");
//		}
//		if (substitute_contact_building != null) {
//			builder.append("substitute_contact_building=");
//			builder.append(substitute_contact_building);
//			builder.append(", ");
//		}
//		if (substitute_contact_tel != null) {
//			builder.append("substitute_contact_tel=");
//			builder.append(substitute_contact_tel);
//			builder.append(", ");
//		}
//		if (substitute_manage_no != null) {
//			builder.append("substitute_manage_no=");
//			builder.append(substitute_manage_no);
//			builder.append(", ");
//		}
//		if (substitute_product_name != null) {
//			builder.append("substitute_product_name=");
//			builder.append(substitute_product_name);
//			builder.append(", ");
//		}
//		if (substitute_note != null) {
//			builder.append("substitute_note=");
//			builder.append(substitute_note);
//			builder.append(", ");
//		}
//		if (multiple_key != null) {
//			builder.append("multiple_key=");
//			builder.append(multiple_key);
//			builder.append(", ");
//		}
//		if (search_key_title1 != null) {
//			builder.append("search_key_title1=");
//			builder.append(search_key_title1);
//			builder.append(", ");
//		}
//		if (search_key1 != null) {
//			builder.append("search_key1=");
//			builder.append(search_key1);
//			builder.append(", ");
//		}
//		if (search_key_title2 != null) {
//			builder.append("search_key_title2=");
//			builder.append(search_key_title2);
//			builder.append(", ");
//		}
//		if (search_key2 != null) {
//			builder.append("search_key2=");
//			builder.append(search_key2);
//			builder.append(", ");
//		}
//		if (search_key_title3 != null) {
//			builder.append("search_key_title3=");
//			builder.append(search_key_title3);
//			builder.append(", ");
//		}
//		if (search_key3 != null) {
//			builder.append("search_key3=");
//			builder.append(search_key3);
//			builder.append(", ");
//		}
//		if (search_key_title4 != null) {
//			builder.append("search_key_title4=");
//			builder.append(search_key_title4);
//			builder.append(", ");
//		}
//		if (search_key4 != null) {
//			builder.append("search_key4=");
//			builder.append(search_key4);
//			builder.append(", ");
//		}
//		if (search_key_title5 != null) {
//			builder.append("search_key_title5=");
//			builder.append(search_key_title5);
//			builder.append(", ");
//		}
//		if (search_key5 != null) {
//			builder.append("search_key5=");
//			builder.append(search_key5);
//			builder.append(", ");
//		}
//		if (reserved_cal2 != null) {
//			builder.append("reserved_cal2=");
//			builder.append(reserved_cal2);
//			builder.append(", ");
//		}
//		if (reserved_cal3 != null) {
//			builder.append("reserved_cal3=");
//			builder.append(reserved_cal3);
//			builder.append(", ");
//		}
//		if (posting_status_mail_usage != null) {
//			builder.append("posting_status_mail_usage=");
//			builder.append(posting_status_mail_usage);
//			builder.append(", ");
//		}
//		if (posting_status_mail_add != null) {
//			builder.append("posting_status_mail_add=");
//			builder.append(posting_status_mail_add);
//			builder.append(", ");
//		}
//		if (posting_status_mail_msg != null) {
//			builder.append("posting_status_mail_msg=");
//			builder.append(posting_status_mail_msg);
//			builder.append(", ");
//		}
//		if (posting_delivery_complete_mail_usage != null) {
//			builder.append("posting_delivery_complete_mail_usage=");
//			builder.append(posting_delivery_complete_mail_usage);
//			builder.append(", ");
//		}
//		if (posting_delivery_complete_mail_add != null) {
//			builder.append("posting_delivery_complete_mail_add=");
//			builder.append(posting_delivery_complete_mail_add);
//			builder.append(", ");
//		}
//		if (posting_delivery_complete_mail_msg != null) {
//			builder.append("posting_delivery_complete_mail_msg=");
//			builder.append(posting_delivery_complete_mail_msg);
//			builder.append(", ");
//		}
//		if (posting_client_complete_mail_usage != null) {
//			builder.append("posting_client_complete_mail_usage=");
//			builder.append(posting_client_complete_mail_usage);
//			builder.append(", ");
//		}
//		if (posting_client_complete_mail_add != null) {
//			builder.append("posting_client_complete_mail_add=");
//			builder.append(posting_client_complete_mail_add);
//			builder.append(", ");
//		}
//		if (posting_client_complete_mail_msg != null) {
//			builder.append("posting_client_complete_mail_msg=");
//			builder.append(posting_client_complete_mail_msg);
//			builder.append(", ");
//		}
//		if (link_manage_no != null) {
//			builder.append("link_manage_no=");
//			builder.append(link_manage_no);
//			builder.append(", ");
//		}
//		if (noti_mail != null) {
//			builder.append("noti_mail=");
//			builder.append(noti_mail);
//		}
		builder.append("]");
		return builder.toString();
	}
}
