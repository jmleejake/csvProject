package jp.prj.araku.list.vo;

public class TranslationErrorVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**R:楽天・A:アマゾン*/
	private String trans_target_type;
	/**エラーテキスト*/
	private String err_text;
	
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
	public String getTrans_target_id() {
		return trans_target_id;
	}
	public void setTrans_target_id(String trans_target_id) {
		this.trans_target_id = trans_target_id;
	}
	public String getTrans_target_type() {
		return trans_target_type;
	}
	public void setTrans_target_type(String trans_target_type) {
		this.trans_target_type = trans_target_type;
	}
	public String getErr_text() {
		return err_text;
	}
	public void setErr_text(String err_text) {
		this.err_text = err_text;
	}
	
}
