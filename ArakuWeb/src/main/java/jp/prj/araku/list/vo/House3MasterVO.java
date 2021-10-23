package jp.prj.araku.list.vo;

public class House3MasterVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**第三倉庫データ*/
	private String house3_data;
	/**データ修正日*/
	private String update_date;
	
	private String keyword;
	
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
	public String getHouse3_data() {
		return house3_data;
	}
	public void setHouse3_data(String house3_data) {
		this.house3_data = house3_data;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
