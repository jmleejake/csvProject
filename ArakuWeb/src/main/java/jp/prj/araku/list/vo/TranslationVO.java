package jp.prj.araku.list.vo;

import java.util.ArrayList;

public class TranslationVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**商品名・項目・選択肢 置換前*/
	private String before_trans;
	/**商品名・項目・選択肢 置換後*/
	private String after_trans;
	/**データ修正日*/
	private String update_date;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String keyword;
	/**検索用*/
	private String search_type;
	
	private ArrayList<String> seq_id_list;
	
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
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	
	@Override
	public String toString() {
		return "TranslationVO [seq_id=" + seq_id + ", register_date=" + register_date + ", before_trans=" + before_trans
				+ ", after_trans=" + after_trans + ", update_date=" + update_date + ", start_date=" + start_date
				+ ", keyword=" + keyword + ", search_type=" + search_type + ", seq_id_list=" + seq_id_list + "]";
	}
	
	
	
}
