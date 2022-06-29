package jp.prj.araku.angular.vo;

public class TodoVO {
	private String seq_id;
	private String cntnt;
	private String todo_end_yn;
	private String reg_dt;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getCntnt() {
		return cntnt;
	}
	public void setCntnt(String cntnt) {
		this.cntnt = cntnt;
	}
	public String getTodo_end_yn() {
		return todo_end_yn;
	}
	public void setTodo_end_yn(String todo_end_yn) {
		this.todo_end_yn = todo_end_yn;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
}
