package jp.prj.araku.jaiko.order.vo;

public class JaikoOrderVO {
	private String partner_id;
	private String partner_nm;
	private String prd_nm;
	private String prd_cnt;
	private String prd_cnt_box;
	private String reg_dt;
	private String seq_id;
	
	private String from_dt;
	private String to_dt;
	
	public String getPrd_nm() {
		return prd_nm;
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
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getPrd_cnt_box() {
		return prd_cnt_box;
	}
	public void setPrd_cnt_box(String prd_cnt_box) {
		this.prd_cnt_box = prd_cnt_box;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getFrom_dt() {
		return from_dt;
	}
	public void setFrom_dt(String from_dt) {
		this.from_dt = from_dt;
	}
	public String getTo_dt() {
		return to_dt;
	}
	public void setTo_dt(String to_dt) {
		this.to_dt = to_dt;
	}
}
