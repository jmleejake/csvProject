package jp.prj.araku.tablet.vo;

public class DealerVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**取引先ID*/
	private String dealer_id;
	/**取引先名*/
	private String dealer_nm;
	/**電話番号*/
	private String dealer_tel;
	/**FAX*/
	private String dealer_fax;
	/**携帯*/
	private String dealer_mobile;
	/**郵便番号*/
	private String dealer_post;
	/**住所*/
	private String dealer_add;
	/**納品希望日*/
	private String est_delivery_dt;
	/**納品先*/
	private String destination;
	/**備考*/
	private String remark;
	
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
	public String getDealer_tel() {
		return dealer_tel;
	}
	public void setDealer_tel(String dealer_tel) {
		this.dealer_tel = dealer_tel;
	}
	public String getDealer_fax() {
		return dealer_fax;
	}
	public void setDealer_fax(String dealer_fax) {
		this.dealer_fax = dealer_fax;
	}
	public String getDealer_mobile() {
		return dealer_mobile;
	}
	public void setDealer_mobile(String dealer_mobile) {
		this.dealer_mobile = dealer_mobile;
	}
	public String getDealer_post() {
		return dealer_post;
	}
	public void setDealer_post(String dealer_post) {
		this.dealer_post = dealer_post;
	}
	public String getDealer_add() {
		return dealer_add;
	}
	public void setDealer_add(String dealer_add) {
		this.dealer_add = dealer_add;
	}
	public String getEst_delivery_dt() {
		return est_delivery_dt;
	}
	public void setEst_delivery_dt(String est_delivery_dt) {
		this.est_delivery_dt = est_delivery_dt;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
