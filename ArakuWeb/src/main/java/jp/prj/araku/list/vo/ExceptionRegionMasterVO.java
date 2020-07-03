package jp.prj.araku.list.vo;

import java.util.ArrayList;

public class ExceptionRegionMasterVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ登録日*/
	private String update_date;
	/**例外地域データ*/
	private String exception_data;
	private String keyword;
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
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getException_data() {
		return exception_data;
	}
	public void setException_data(String exception_data) {
		this.exception_data = exception_data;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExceptionRegionMasterVO [");
		if (seq_id != null) {
			builder.append("seq_id=");
			builder.append(seq_id);
			builder.append(", ");
		}
		if (register_date != null) {
			builder.append("register_date=");
			builder.append(register_date);
			builder.append(", ");
		}
		if (update_date != null) {
			builder.append("update_date=");
			builder.append(update_date);
			builder.append(", ");
		}
		if (exception_data != null) {
			builder.append("exception_data=");
			builder.append(exception_data);
			builder.append(", ");
		}
		if (keyword != null) {
			builder.append("keyword=");
			builder.append(keyword);
		}
		if (seq_id_list != null) {
			builder.append("seq_id_list=");
			builder.append(seq_id_list);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
	
}
