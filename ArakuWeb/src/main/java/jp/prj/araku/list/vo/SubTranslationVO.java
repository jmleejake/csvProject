package jp.prj.araku.list.vo;

public class SubTranslationVO {
	/**区分ID*/
	private String seq_id;
	/**親区分ID*/
	private String parent_seq_id;
	/**親置換後*/
	private String parent_after_trans;
	/**置換前*/
	private String before_trans;
	/**置換後*/
	private String after_trans;
	/**商品数*/
	private String prd_cnt;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	private String keyword;
	private String search_type;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getParent_seq_id() {
		return parent_seq_id;
	}
	public void setParent_seq_id(String parent_seq_id) {
		this.parent_seq_id = parent_seq_id;
	}
	public String getParent_after_trans() {
		return parent_after_trans;
	}
	public void setParent_after_trans(String parent_after_trans) {
		this.parent_after_trans = parent_after_trans;
	}
	public String getBefore_trans() {
		return before_trans;
	}
	public void setBefore_trans(String before_trans) {
		this.before_trans = before_trans;
	}
	public String getAfter_trans() {
		return after_trans;
	}
	public void setAfter_trans(String after_trans) {
		this.after_trans = after_trans;
	}
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
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
}
