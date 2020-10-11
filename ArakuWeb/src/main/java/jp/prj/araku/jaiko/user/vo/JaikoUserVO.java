package jp.prj.araku.jaiko.user.vo;

public class JaikoUserVO {
	/**区分ID*/
	private String seq_id;
	/**ユーザーID*/
	private String user_id;
	/**パスワード*/
	private String user_pass;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	/**最近ローグイン日付*/
	private String last_login_dt;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
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
	public String getLast_login_dt() {
		return last_login_dt;
	}
	public void setLast_login_dt(String last_login_dt) {
		this.last_login_dt = last_login_dt;
	}
	@Override
	public String toString() {
		return String.format(
				"JaikoUserVO [seq_id=%s, user_id=%s, user_pass=%s, reg_user_id=%s, reg_dt=%s, upd_user_id=%s, upd_dt=%s, last_login_dt=%s]",
				seq_id, user_id, user_pass, reg_user_id, reg_dt, upd_user_id, upd_dt, last_login_dt);
	}
	
}
