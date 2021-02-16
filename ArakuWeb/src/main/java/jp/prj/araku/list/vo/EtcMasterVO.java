package jp.prj.araku.list.vo;

public class EtcMasterVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品数*/
	private String prd_cnt;
	/**商品名*/
	private String prd_nm;
	/**メニュー区分*/
	private String target_type;
	/**その他キー*/
	private String etc_key;
	
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
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getTarget_type() {
		return target_type;
	}
	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
	public String getEtc_key() {
		return etc_key;
	}
	public void setEtc_key(String etc_key) {
		this.etc_key = etc_key;
	}

}
