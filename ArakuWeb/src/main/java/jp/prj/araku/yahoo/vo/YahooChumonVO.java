package jp.prj.araku.yahoo.vo;

import com.opencsv.bean.CsvBindByPosition;

public class YahooChumonVO {
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_id;
	/**注文時間*/
	@CsvBindByPosition(position=1)
	private String order_time;
	/**処理ステータス*/
	@CsvBindByPosition(position=2)
	private String order_status;
	/**お届け先氏名*/
	@CsvBindByPosition(position=3)
	private String ship_nm;
	/**お届け先名前*/
	@CsvBindByPosition(position=4)
	private String ship_fst_nm;
	/**お届け先名氏*/
	@CsvBindByPosition(position=5)
	private String ship_last_nm;
	/**お届け住所１行目*/
	@CsvBindByPosition(position=6)
	private String ship_add1;
	/**お届け住所２行目*/
	@CsvBindByPosition(position=7)
	private String ship_add2;
	/**お届け市区町村*/
	@CsvBindByPosition(position=8)
	private String ship_city;
	/**お届け先都道府県*/
	@CsvBindByPosition(position=9)
	private String ship_pre;
	/**お届け先郵便番号*/
	@CsvBindByPosition(position=10)
	private String ship_post_no;
	/**お届け先氏名カナ*/
	@CsvBindByPosition(position=11)
	private String ship_nm_kana;
	/**お届け先名前カナ*/
	@CsvBindByPosition(position=12)
	private String ship_fst_nm_kana;
	/**お届け先名字カナ*/
	@CsvBindByPosition(position=13)
	private String ship_last_nm_kana;
	/**お届け先電話番号*/
	@CsvBindByPosition(position=14)
	private String ship_phone_no;
	/**お届け希望日*/
	@CsvBindByPosition(position=15)
	private String ship_req_dt;
	/**お届け希望時間*/
	@CsvBindByPosition(position=16)
	private String ship_req_time;
	/**配送会社*/
	@CsvBindByPosition(position=17)
	private String ship_company_cd;
	/**お問い合わせ伝票番号*/
	@CsvBindByPosition(position=18)
	private String ship_ivc_no1;
	/**お問い合わせ伝票番号２*/
	@CsvBindByPosition(position=19)
	private String ship_ivc_no2;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
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

}
