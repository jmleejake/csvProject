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
	
	@Override
	public String toString() {
		return String.format(
				"JaikoPartnerVO [seq_id=%s, partner_id=%s, partner_nm=%s, reg_user_id=%s, reg_dt=%s, upd_user_id=%s, upd_dt=%s, keyword=%s]",
				seq_id, partner_id, partner_nm, reg_user_id, reg_dt, upd_user_id, upd_dt, keyword);
	}

}
