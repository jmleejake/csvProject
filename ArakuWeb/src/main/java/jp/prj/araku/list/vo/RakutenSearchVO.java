package jp.prj.araku.list.vo;

/**
 * 楽天CSVファイル(検索用)
 * */
public class RakutenSearchVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**受注番号*/
	private String order_no;
	/**受注ステータス*/
	private String order_status;
	/**お届け日指定*/
	private String delivery_date_sel;
	/**合計金額(-99999=無効値)*/
	private String total_amt;
	/**お荷物伝票番号*/
	private String baggage_claim_no;
	/**送付先名字*/
	private String delivery_surname;
	/**送付先名前*/
	private String delivery_name;
	/**送付先名字フリガナ*/
	private String delivery_surname_kana;
	/**送付先名前フリガナ*/
	private String delivery_name_kana;
	/**送付先電話番号１*/
	private String delivery_tel1;
	/**送付先電話番号２*/
	private String delivery_tel2;
	/**送付先電話番号３*/
	private String delivery_tel3;
	/**商品名*/
	private String product_name;
	/**項目・選択肢*/
	private String product_option;
	/**個数*/
	private String unit_no;
	/**あす楽希望*/
	private String tomorrow_hope;
	/**データ修正日*/
	private String update_date;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String search_type;
	
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
	public String getDelivery_date_sel() {
		return delivery_date_sel;
	}
	public void setDelivery_date_sel(String delivery_date_sel) {
		this.delivery_date_sel = delivery_date_sel;
	}
	public String getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}
	public String getBaggage_claim_no() {
		return baggage_claim_no;
	}
	public void setBaggage_claim_no(String baggage_claim_no) {
		this.baggage_claim_no = baggage_claim_no;
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
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_option() {
		return product_option;
	}
	public void setProduct_option(String product_option) {
		this.product_option = product_option;
	}
	public String getUnit_no() {
		return unit_no;
	}
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}
	public String getTomorrow_hope() {
		return tomorrow_hope;
	}
	public void setTomorrow_hope(String tomorrow_hope) {
		this.tomorrow_hope = tomorrow_hope;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
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
	
	@Override
	public String toString() {
		return "RakutenSearchVO [seq_id=" + seq_id + ", register_date=" + register_date + ", order_no=" + order_no
				+ ", order_status=" + order_status + ", delivery_date_sel=" + delivery_date_sel + ", total_amt="
				+ total_amt + ", baggage_claim_no=" + baggage_claim_no + ", delivery_surname=" + delivery_surname
				+ ", delivery_name=" + delivery_name + ", delivery_surname_kana=" + delivery_surname_kana
				+ ", delivery_name_kana=" + delivery_name_kana + ", delivery_tel1=" + delivery_tel1 + ", delivery_tel2="
				+ delivery_tel2 + ", delivery_tel3=" + delivery_tel3 + ", product_name=" + product_name
				+ ", product_option=" + product_option + ", unit_no=" + unit_no + ", tomorrow_hope=" + tomorrow_hope
				+ ", update_date=" + update_date + ", start_date=" + start_date + ", search_type=" + search_type + "]";
	}
	
}
