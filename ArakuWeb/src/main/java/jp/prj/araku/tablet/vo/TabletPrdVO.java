package jp.prj.araku.tablet.vo;

public class TabletPrdVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**商品管理番号*/
	private String prd_cd;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品名*/
	private String prd_nm;
	/**商品詳細*/
	private String prd_dtl;
	/**商品数*/
	private String prd_cnt;
	/**順番*/
	private String order_no;
	
	/**注文者姓*/
	private String order_surname;
	/**注文者名*/
	private String order_name;
	/**送付先姓*/
	private String delivery_surname;
	/**送付先名*/
	private String delivery_name;
	/**お荷物伝票番号*/
	private String baggage_claim_no;
	/**個数*/
	private String unit_no;
	
	/**取引先ID*/
	private String dealer_id;
	/**取引先名*/
	private String dealer_nm;
	
	/**마스터에서의 업데이트인지 재고관리에서의 수량 업데이트인지의 구분값*/
	private String update_type;
	/**入庫日*/
	private String receive_date;
	
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
	public String getPrd_cd() {
		return prd_cd;
	}
	public void setPrd_cd(String prd_cd) {
		this.prd_cd = prd_cd;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getPrd_dtl() {
		return prd_dtl;
	}
	public void setPrd_dtl(String prd_dtl) {
		this.prd_dtl = prd_dtl;
	}
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
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
	public String getBaggage_claim_no() {
		return baggage_claim_no;
	}
	public void setBaggage_claim_no(String baggage_claim_no) {
		this.baggage_claim_no = baggage_claim_no;
	}
	public String getUnit_no() {
		return unit_no;
	}
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getDealer_nm() {
		return dealer_nm;
	}
	public void setDealer_nm(String dealer_nm) {
		this.dealer_nm = dealer_nm;
	}
	public String getUpdate_type() {
		return update_type;
	}
	public void setUpdate_type(String update_type) {
		this.update_type = update_type;
	}
	public String getReceive_date() {
		return receive_date;
	}
	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
	}
}
