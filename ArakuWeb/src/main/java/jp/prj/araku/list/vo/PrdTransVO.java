package jp.prj.araku.list.vo;

import java.util.ArrayList;

public class PrdTransVO {
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
	/**注文番号*/
	private String order_no;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**区分*/
	private String order_gbn;
	/**商品マスタ反映有無フラグ：0（未）、1（済）*/
	private String prd_master_hanei_gbn;
	/**商品数*/
	private String prd_cnt;
	/**メニュー区分*/
	private String trans_target_type;
	
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

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getJan_cd() {
		return jan_cd;
	}

	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}

	public String getOrder_gbn() {
		return order_gbn;
	}

	public void setOrder_gbn(String order_gbn) {
		this.order_gbn = order_gbn;
	}

	public String getPrd_master_hanei_gbn() {
		return prd_master_hanei_gbn;
	}

	public void setPrd_master_hanei_gbn(String prd_master_hanei_gbn) {
		this.prd_master_hanei_gbn = prd_master_hanei_gbn;
	}

	public String getPrd_cnt() {
		return prd_cnt;
	}

	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
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

	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}

	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}

	public String getTrans_target_type() {
		return trans_target_type;
	}

	public void setTrans_target_type(String trans_target_type) {
		this.trans_target_type = trans_target_type;
	}

}
