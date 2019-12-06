package jp.prj.araku.tablet.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class StockVO extends ArakuVO{
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**取引先ID*/
	private String dealer_id;
	/**取引先名*/
	@CsvBindByPosition(position=0)
	private String dealer_nm;
	/**商品名*/
	@CsvBindByPosition(position=1)
	private String prd_nm;
	/**商品数量*/
	@CsvBindByPosition(position=2)
	private String prd_cnt;
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=3)
	private String jan_cd;
	/**入庫日*/
	@CsvBindByPosition(position=4)
	private String receive_date;
	
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
	public String getDealer_nm() {
		return dealer_nm;
	}
	public void setDealer_nm(String dealer_nm) {
		this.dealer_nm = dealer_nm;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getReceive_date() {
		return receive_date;
	}
	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
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
}
