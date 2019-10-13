package jp.prj.araku.yahoo.vo;

import java.util.ArrayList;

public class YahooVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**注文番号*/
	private String order_id;
	/**注文内容*/
	private String line_id;
	/**商品名*/
	private String title;
	/**個数*/
	private String qty;
	/**ストアアカウント付き注文番号*/
	private String id;
	/**商品コード*/
	private String item_id;
	/**商品オプション*/
	private String item_option_val;
	/**あす楽*/
	private String lead_time_txt;
	/**税金率*/
	private String item_tax_ratio;
	/**注文時間*/
	private String order_time;
	/**処理ステータス*/
	private String order_status;
	/**お届け先氏名*/
	private String ship_nm;
	/**お届け先名前*/
	private String ship_fst_nm;
	/**お届け先名氏*/
	private String ship_last_nm;
	/**お届け住所１行目*/
	private String ship_add1;
	/**お届け住所２行目*/
	private String ship_add2;
	/**お届け市区町村*/
	private String ship_city;
	/**お届け先都道府県*/
	private String ship_pre;
	/**お届け先郵便番号*/
	private String ship_post_no;
	/**お届け先氏名カナ*/
	private String ship_nm_kana;
	/**お届け先名前カナ*/
	private String ship_fst_nm_kana;
	/**お届け先名字カナ*/
	private String ship_last_nm_kana;
	/**お届け先電話番号*/
	private String ship_phone_no;
	/**お届け希望日*/
	private String ship_req_dt;
	/**お届け希望時間*/
	private String ship_req_time;
	/**配送会社*/
	private String ship_company_cd;
	/**お問い合わせ伝票番号*/
	private String ship_ivc_no1;
	/**お問い合わせ伝票番号２*/
	private String ship_ivc_no2;
	
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
	private String update_type;
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
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getLine_id() {
		return line_id;
	}
	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_option_val() {
		return item_option_val;
	}
	public void setItem_option_val(String item_option_val) {
		this.item_option_val = item_option_val;
	}
	public String getLead_time_txt() {
		return lead_time_txt;
	}
	public void setLead_time_txt(String lead_time_txt) {
		this.lead_time_txt = lead_time_txt;
	}
	public String getItem_tax_ratio() {
		return item_tax_ratio;
	}
	public void setItem_tax_ratio(String item_tax_ratio) {
		this.item_tax_ratio = item_tax_ratio;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getShip_nm() {
		return ship_nm;
	}
	public void setShip_nm(String ship_nm) {
		this.ship_nm = ship_nm;
	}
	public String getShip_fst_nm() {
		return ship_fst_nm;
	}
	public void setShip_fst_nm(String ship_fst_nm) {
		this.ship_fst_nm = ship_fst_nm;
	}
	public String getShip_last_nm() {
		return ship_last_nm;
	}
	public void setShip_last_nm(String ship_last_nm) {
		this.ship_last_nm = ship_last_nm;
	}
	public String getShip_add1() {
		return ship_add1;
	}
	public void setShip_add1(String ship_add1) {
		this.ship_add1 = ship_add1;
	}
	public String getShip_add2() {
		return ship_add2;
	}
	public void setShip_add2(String ship_add2) {
		this.ship_add2 = ship_add2;
	}
	public String getShip_city() {
		return ship_city;
	}
	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}
	public String getShip_pre() {
		return ship_pre;
	}
	public void setShip_pre(String ship_pre) {
		this.ship_pre = ship_pre;
	}
	public String getShip_post_no() {
		return ship_post_no;
	}
	public void setShip_post_no(String ship_post_no) {
		this.ship_post_no = ship_post_no;
	}
	public String getShip_nm_kana() {
		return ship_nm_kana;
	}
	public void setShip_nm_kana(String ship_nm_kana) {
		this.ship_nm_kana = ship_nm_kana;
	}
	public String getShip_fst_nm_kana() {
		return ship_fst_nm_kana;
	}
	public void setShip_fst_nm_kana(String ship_fst_nm_kana) {
		this.ship_fst_nm_kana = ship_fst_nm_kana;
	}
	public String getShip_last_nm_kana() {
		return ship_last_nm_kana;
	}
	public void setShip_last_nm_kana(String ship_last_nm_kana) {
		this.ship_last_nm_kana = ship_last_nm_kana;
	}
	public String getShip_phone_no() {
		return ship_phone_no;
	}
	public void setShip_phone_no(String ship_phone_no) {
		this.ship_phone_no = ship_phone_no;
	}
	public String getShip_req_dt() {
		return ship_req_dt;
	}
	public void setShip_req_dt(String ship_req_dt) {
		this.ship_req_dt = ship_req_dt;
	}
	public String getShip_req_time() {
		return ship_req_time;
	}
	public void setShip_req_time(String ship_req_time) {
		this.ship_req_time = ship_req_time;
	}
	public String getShip_company_cd() {
		return ship_company_cd;
	}
	public void setShip_company_cd(String ship_company_cd) {
		this.ship_company_cd = ship_company_cd;
	}
	public String getShip_ivc_no1() {
		return ship_ivc_no1;
	}
	public void setShip_ivc_no1(String ship_ivc_no1) {
		this.ship_ivc_no1 = ship_ivc_no1;
	}
	public String getShip_ivc_no2() {
		return ship_ivc_no2;
	}
	public void setShip_ivc_no2(String ship_ivc_no2) {
		this.ship_ivc_no2 = ship_ivc_no2;
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
	public String getUpdate_type() {
		return update_type;
	}
	public void setUpdate_type(String update_type) {
		this.update_type = update_type;
	}
}
