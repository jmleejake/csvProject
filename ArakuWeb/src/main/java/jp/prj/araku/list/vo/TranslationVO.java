package jp.prj.araku.list.vo;

public class TranslationVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**商品名・項目・選択肢 置換前*/
	private String before_trans;
	/**商品名・項目・選択肢 置換後*/
	private String after_trans;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String keyword;
	/**検索用*/
	private String search_type;
	
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
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
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
	
	@Override
	public String toString() {
		return "TranslationVO [seq_id=" + seq_id + ", register_date=" + register_date + ", before_trans=" + before_trans
				+ ", after_trans=" + after_trans + ", start_date=" + start_date + ", keyword=" + keyword
				+ ", search_type=" + search_type + "]";
	}
	
}
