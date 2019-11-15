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
}
