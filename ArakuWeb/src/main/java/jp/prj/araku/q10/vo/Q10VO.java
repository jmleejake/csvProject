package jp.prj.araku.q10.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class Q10VO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**配送状態*/
	@CsvBindByPosition(position=0)
	private String delivery_sts;
	/**注文番号*/
	@CsvBindByPosition(position=1)
	private String order_no;
	/**カート番号*/
	@CsvBindByPosition(position=2)
	private String cart_no;
	/**配送会社*/
	@CsvBindByPosition(position=3)
	private String delivery_company_q10;
	/**送り状番号*/
	@CsvBindByPosition(position=4)
	private String invoice_no;
	/**発送日*/
	@CsvBindByPosition(position=5)
	private String ship_date;
	/**注文日*/
	@CsvBindByPosition(position=6)
	private String order_date;
	/**入金日*/
	@CsvBindByPosition(position=7)
	private String pay_date;
	/**お届け希望日*/
	@CsvBindByPosition(position=8)
	private String delivery_date;
	/**発送予定日*/
	@CsvBindByPosition(position=9)
	private String ship_eta;
	/**配送完了日*/
	@CsvBindByPosition(position=10)
	private String delivery_atd;
	/**配送方法*/
	@CsvBindByPosition(position=11)
	private String ship_method;
	/**商品番号*/
	@CsvBindByPosition(position=12)
	private String item_no;
	/**商品名*/
	@CsvBindByPosition(position=13)
	private String product_name;
	/**数量*/
	@CsvBindByPosition(position=14)
	private String qty;
	/**オプション情報*/
	@CsvBindByPosition(position=15)
	private String option_info;
	/**オプションコード*/
	@CsvBindByPosition(position=16)
	private String option_cd;
	/**おまけ*/
	@CsvBindByPosition(position=17)
	private String bonus;
	/**受取人名*/
	@CsvBindByPosition(position=18)
	private String recpt_name;
	/**受取人名(フリガナ)*/
	@CsvBindByPosition(position=19)
	private String recpt_name_kana;
	/**受取人電話番号*/
	@CsvBindByPosition(position=20)
	private String recpt_phone_no;
	/**受取人携帯電話番号*/
	@CsvBindByPosition(position=21)
	private String recpt_mobile_no;
	/**住所*/
	@CsvBindByPosition(position=22)
	private String address;
	/**郵便番号*/
	@CsvBindByPosition(position=23)
	private String post_no;
	/**国家*/
	@CsvBindByPosition(position=24)
	private String country_name;
	/**送料の決済*/
	@CsvBindByPosition(position=25)
	private String ship_pay;
	/**決済サイト*/
	@CsvBindByPosition(position=26)
	private String pay_site;
	/**通貨*/
	@CsvBindByPosition(position=27)
	private String currency_type;
	/**購入者決済金額*/
	@CsvBindByPosition(position=28)
	private String pay_amt;
	/**販売価格*/
	@CsvBindByPosition(position=29)
	private String sel_price;
	/**割引額*/
	@CsvBindByPosition(position=30)
	private String disc_price;
	/**注文金額の合計*/
	@CsvBindByPosition(position=31)
	private String total_price;
	/**供給原価の合計*/
	@CsvBindByPosition(position=32)
	private String total_supply;
	/**購入者名*/
	@CsvBindByPosition(position=33)
	private String pay_name;
	/**購入者名(フリガナ)*/
	@CsvBindByPosition(position=34)
	private String pay_name_kana;
	/**配送要請事項*/
	@CsvBindByPosition(position=35)
	private String delivery_item;
	/**購入者電話番号*/
	@CsvBindByPosition(position=36)
	private String pay_phone_no;
	/**購入者携帯電話番号*/
	@CsvBindByPosition(position=37)
	private String pay_mobile_no;
	/**販売者商品コード*/
	@CsvBindByPosition(position=38)
	private String product_cd;
	/**JANコード*/
	@CsvBindByPosition(position=39)
	private String jan_cd;
	/**規格番号*/
	@CsvBindByPosition(position=40)
	private String std_no;
	/**プレゼント贈り主*/
	@CsvBindByPosition(position=41)
	private String presenter;
	/**外部広告*/
	@CsvBindByPosition(position=42)
	private String ads_no_site;
	/**素材*/
	@CsvBindByPosition(position=43)
	private String material_type;
	
	/**配送会社*/
	private String delivery_company;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**置換結果テキスト*/
	private String result_text;
	/**エラーテキスト*/
	private String err_text;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String search_type;
	/**検索用*/
	private ArrayList<String> seq_id_list;
	
	public String getDelivery_sts() {
		return delivery_sts;
	}
	public void setDelivery_sts(String delivery_sts) {
		this.delivery_sts = delivery_sts;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getCart_no() {
		return cart_no;
	}
	public void setCart_no(String cart_no) {
		this.cart_no = cart_no;
	}
	public String getDelivery_company_q10() {
		return delivery_company_q10;
	}
	public void setDelivery_company_q10(String delivery_company_q10) {
		this.delivery_company_q10 = delivery_company_q10;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public String getShip_date() {
		return ship_date;
	}
	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getPay_date() {
		return pay_date;
	}
	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getShip_eta() {
		return ship_eta;
	}
	public void setShip_eta(String ship_eta) {
		this.ship_eta = ship_eta;
	}
	public String getDelivery_atd() {
		return delivery_atd;
	}
	public void setDelivery_atd(String delivery_atd) {
		this.delivery_atd = delivery_atd;
	}
	public String getShip_method() {
		return ship_method;
	}
	public void setShip_method(String ship_method) {
		this.ship_method = ship_method;
	}
	public String getItem_no() {
		return item_no;
	}
	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getOption_info() {
		return option_info;
	}
	public void setOption_info(String option_info) {
		this.option_info = option_info;
	}
	public String getOption_cd() {
		return option_cd;
	}
	public void setOption_cd(String option_cd) {
		this.option_cd = option_cd;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getRecpt_name() {
		return recpt_name;
	}
	public void setRecpt_name(String recpt_name) {
		this.recpt_name = recpt_name;
	}
	public String getRecpt_name_kana() {
		return recpt_name_kana;
	}
	public void setRecpt_name_kana(String recpt_name_kana) {
		this.recpt_name_kana = recpt_name_kana;
	}
	public String getRecpt_phone_no() {
		return recpt_phone_no;
	}
	public void setRecpt_phone_no(String recpt_phone_no) {
		this.recpt_phone_no = recpt_phone_no;
	}
	public String getRecpt_mobile_no() {
		return recpt_mobile_no;
	}
	public void setRecpt_mobile_no(String recpt_mobile_no) {
		this.recpt_mobile_no = recpt_mobile_no;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPost_no() {
		return post_no;
	}
	public void setPost_no(String post_no) {
		this.post_no = post_no;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getShip_pay() {
		return ship_pay;
	}
	public void setShip_pay(String ship_pay) {
		this.ship_pay = ship_pay;
	}
	public String getPay_site() {
		return pay_site;
	}
	public void setPay_site(String pay_site) {
		this.pay_site = pay_site;
	}
	public String getCurrency_type() {
		return currency_type;
	}
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	public String getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(String pay_amt) {
		this.pay_amt = pay_amt;
	}
	public String getSel_price() {
		return sel_price;
	}
	public void setSel_price(String sel_price) {
		this.sel_price = sel_price;
	}
	public String getDisc_price() {
		return disc_price;
	}
	public void setDisc_price(String disc_price) {
		this.disc_price = disc_price;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getTotal_supply() {
		return total_supply;
	}
	public void setTotal_supply(String total_supply) {
		this.total_supply = total_supply;
	}
	public String getPay_name() {
		return pay_name;
	}
	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}
	public String getPay_name_kana() {
		return pay_name_kana;
	}
	public void setPay_name_kana(String pay_name_kana) {
		this.pay_name_kana = pay_name_kana;
	}
	public String getDelivery_item() {
		return delivery_item;
	}
	public void setDelivery_item(String delivery_item) {
		this.delivery_item = delivery_item;
	}
	public String getPay_phone_no() {
		return pay_phone_no;
	}
	public void setPay_phone_no(String pay_phone_no) {
		this.pay_phone_no = pay_phone_no;
	}
	public String getPay_mobile_no() {
		return pay_mobile_no;
	}
	public void setPay_mobile_no(String pay_mobile_no) {
		this.pay_mobile_no = pay_mobile_no;
	}
	public String getProduct_cd() {
		return product_cd;
	}
	public void setProduct_cd(String product_cd) {
		this.product_cd = product_cd;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getStd_no() {
		return std_no;
	}
	public void setStd_no(String std_no) {
		this.std_no = std_no;
	}
	public String getPresenter() {
		return presenter;
	}
	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}
	public String getAds_no_site() {
		return ads_no_site;
	}
	public void setAds_no_site(String ads_no_site) {
		this.ads_no_site = ads_no_site;
	}
	public String getMaterial_type() {
		return material_type;
	}
	public void setMaterial_type(String material_type) {
		this.material_type = material_type;
	}
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
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public String getTrans_target_id() {
		return trans_target_id;
	}
	public void setTrans_target_id(String trans_target_id) {
		this.trans_target_id = trans_target_id;
	}
	public String getResult_text() {
		return result_text;
	}
	public void setResult_text(String result_text) {
		this.result_text = result_text;
	}
	public String getErr_text() {
		return err_text;
	}
	public void setErr_text(String err_text) {
		this.err_text = err_text;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
}
