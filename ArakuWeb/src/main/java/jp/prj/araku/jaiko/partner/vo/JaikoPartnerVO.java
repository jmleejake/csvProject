package jp.prj.araku.jaiko.partner.vo;

public class JaikoPartnerVO {
	/**区分ID*/
	private String seq_id;
	/**取引先No.*/
	private String partner_id;
	/**取引先名*/
	private String partner_nm;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	private String keyword;
	
	/**郵便番号*/
	private String partner_post;
	/**住所*/
	private String partner_add;
	/**電話番号*/
	private String partner_tel;
	/**FAX番号*/
	private String partner_fax;
	/**担当者名*/
	private String tantou_nm;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
	public String getPartner_nm() {
		return partner_nm;
	}
	public void setPartner_nm(String partner_nm) {
		this.partner_nm = partner_nm;
	}
	public String getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getUpd_user_id() {
		return upd_user_id;
	}
	public void setUpd_user_id(String upd_user_id) {
		this.upd_user_id = upd_user_id;
	}
	public String getUpd_dt() {
		return upd_dt;
	}
	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPartner_post() {
		return partner_post;
	}
	public void setPartner_post(String partner_post) {
		this.partner_post = partner_post;
	}
	public String getPartner_add() {
		return partner_add;
	}
	public void setPartner_add(String partner_add) {
		this.partner_add = partner_add;
	}
	public String getPartner_tel() {
		return partner_tel;
	}
	public void setPartner_tel(String partner_tel) {
		this.partner_tel = partner_tel;
	}
	public String getPartner_fax() {
		return partner_fax;
	}
	public void setPartner_fax(String partner_fax) {
		this.partner_fax = partner_fax;
	}
	public String getTantou_nm() {
		return tantou_nm;
	}
	public void setTantou_nm(String tantou_nm) {
		this.tantou_nm = tantou_nm;
	}

}
