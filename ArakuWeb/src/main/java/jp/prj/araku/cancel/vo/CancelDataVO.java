package jp.prj.araku.cancel.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import jp.prj.araku.util.ArakuVO;
import lombok.Data;

@Data
public class CancelDataVO extends ArakuVO {
    private String seq_id;
    private String register_date;
    private String update_date;
    private String order_no;
    private String jan_cd;
    private String order_gbn;
    private String before_trans;
    private String after_trans;
    private String prd_cnt;
    private String baggage_claim_no;

    // 기타 필드 ...

    @JsonCreator
    public CancelDataVO(
        @JsonProperty("seq_id") String seq_id, 
        @JsonProperty("register_date") String register_date, 
        @JsonProperty("update_date") String update_date, 
        @JsonProperty("order_no") String order_no, 
        @JsonProperty("order_gbn") String order_gbn, 
        @JsonProperty("jan_cd") String jan_cd, 
        @JsonProperty("before_trans") String before_trans, 
        @JsonProperty("after_trans") String after_trans, 
        @JsonProperty("prd_cnt") String prd_cnt, 
        @JsonProperty("baggage_claim_no") String baggage_claim_no
    ) {
        this.seq_id = seq_id;
        this.register_date = register_date;
        this.update_date = update_date;
        this.order_no = order_no;
        this.order_gbn = order_gbn;
        this.jan_cd = jan_cd;
        this.before_trans = before_trans;
        this.after_trans = after_trans;
        this.prd_cnt = prd_cnt;
        this.baggage_claim_no = baggage_claim_no;
        // 기타 필드 초기화...
    }

    // 기본 생성자
    public CancelDataVO() {}

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

	public String getOrder_gbn() {
		return order_gbn;
	}

	public void setOrder_gbn(String order_gbn) {
		this.order_gbn = order_gbn;
	}


	public String getJan_cd() {
		return jan_cd;
	}

	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
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
}
