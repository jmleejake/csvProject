package jp.prj.araku.jaiko.inventory.vo;

import com.opencsv.bean.CsvBindByPosition;

public class InventoryDownVO {
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=0)
	private String jan_cd;
	/**商品名*/
	@CsvBindByPosition(position=1)
	private String prd_nm;
	/**規格*/
	@CsvBindByPosition(position=2)
	private String std_info;
	/**入数*/
	@CsvBindByPosition(position=3)
	private String prd_qty;
	/**ケース*/
	@CsvBindByPosition(position=4)
	private String prd_case;
	/**バラ*/
	@CsvBindByPosition(position=5)
	private String prd_bara;
	/**賞味期限*/
	@CsvBindByPosition(position=6)
	private String exp_dt;
	/**原価*/
	@CsvBindByPosition(position=7)
	private String prd_unit_prc;
	/**売価(本体)*/
	@CsvBindByPosition(position=8)
	private String sell_prc;
	/**備考*/
	@CsvBindByPosition(position=9)
	private String etc_note;
	
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getStd_info() {
		return std_info;
	}
	public void setStd_info(String std_info) {
		this.std_info = std_info;
	}
	public String getPrd_qty() {
		return prd_qty;
	}
	public void setPrd_qty(String prd_qty) {
		this.prd_qty = prd_qty;
	}
	public String getPrd_case() {
		return prd_case;
	}
	public void setPrd_case(String prd_case) {
		this.prd_case = prd_case;
	}
	public String getPrd_bara() {
		return prd_bara;
	}
	public void setPrd_bara(String prd_bara) {
		this.prd_bara = prd_bara;
	}
	public String getExp_dt() {
		return exp_dt;
	}
	public void setExp_dt(String exp_dt) {
		this.exp_dt = exp_dt;
	}
	public String getPrd_unit_prc() {
		return prd_unit_prc;
	}
	public void setPrd_unit_prc(String prd_unit_prc) {
		this.prd_unit_prc = prd_unit_prc;
	}
	public String getSell_prc() {
		return sell_prc;
	}
	public void setSell_prc(String sell_prc) {
		this.sell_prc = sell_prc;
	}
	public String getEtc_note() {
		return etc_note;
	}
	public void setEtc_note(String etc_note) {
		this.etc_note = etc_note;
	}
}
