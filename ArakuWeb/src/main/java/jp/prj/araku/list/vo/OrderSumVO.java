package jp.prj.araku.list.vo;

public class OrderSumVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**商品名・項目・選択肢 置換後*/
	private String after_trans;
	/**データ修正日*/
	private String update_date;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品マスタ反映有無フラグ：0（未）、1（済）*/
	private String prd_master_hanei_gbn;
	/**総商品数*/
	private String prd_sum;
	/**メニュー区分*/
	private String target_type;
	
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
	public String getAfter_trans() {
		return after_trans;
	}
	public void setAfter_trans(String after_trans) {
		this.after_trans = after_trans;
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
	public String getPrd_master_hanei_gbn() {
		return prd_master_hanei_gbn;
	}
	public void setPrd_master_hanei_gbn(String prd_master_hanei_gbn) {
		this.prd_master_hanei_gbn = prd_master_hanei_gbn;
	}
	public String getPrd_sum() {
		return prd_sum;
	}
	public void setPrd_sum(String prd_sum) {
		this.prd_sum = prd_sum;
	}
	public String getTarget_type() {
		return target_type;
	}
	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
}
