package jp.prj.araku.list.vo;

import java.util.ArrayList;

public class ExceptionMasterVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**例外データ*/
	private String exception_data;
	
	private ArrayList<String> seq_id_list;
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
	public String getException_data() {
		return exception_data;
	}
	public void setException_data(String exception_data) {
		this.exception_data = exception_data;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExceptionMasterVO [");
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
		if (exception_data != null) {
			builder.append("exception_data=");
			builder.append(exception_data);
			builder.append(", ");
		}
		if (seq_id_list != null) {
			builder.append("seq_id_list=");
			builder.append(seq_id_list);
			builder.append(", ");
		}
		if (keyword != null) {
			builder.append("keyword=");
			builder.append(keyword);
		}
		builder.append("]");
		return builder.toString();
	}
}
