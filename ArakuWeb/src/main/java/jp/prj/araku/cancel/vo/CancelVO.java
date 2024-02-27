package jp.prj.araku.cancel.vo;

import java.util.ArrayList;
import jp.prj.araku.util.ArakuVO;
import lombok.Data;

@Data
public class CancelVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;	
	/**注文番号*/
	private String order_no;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**注文番号区分*/
	private String order_gbn;
	/**商品名・項目・選択肢 置換前*/
	private String before_trans;
	/**商品名・項目・選択肢 置換後*/
	private String after_trans;
	/**商品数*/
	private String prd_cnt;
	/**お荷物伝票番号*/
	private String baggage_claim_no;
	
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String baggage;
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

	public String getPrd_cnt() {
		return prd_cnt;
	}

	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}

	public String getBaggage_claim_no() {
		return baggage_claim_no;
	}

	public void setBaggage_claim_no(String baggage_claim_no) {
		this.baggage_claim_no = baggage_claim_no;
	}
	
	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getBaggage() {
		return baggage;
	}

	public void setBaggage(String baggage) {
		this.baggage = baggage;
	}

	public void setTarget_type(String target_type) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
