package jp.prj.araku.jaiko.inventory.vo;

import com.opencsv.bean.CsvBindByPosition;

public class InventoryUpVO {
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
	/**ケース数*/
	@CsvBindByPosition(position=4)
	private String prd_case;
	/**バラ数*/
	@CsvBindByPosition(position=5)
	private String prd_bara;
	/**総数*/
	@CsvBindByPosition(position=6)
	private String now_prd_cnt;
	/**原価*/
	@CsvBindByPosition(position=7)
	private String prd_unit_prc;
	/**本体売価*/
	@CsvBindByPosition(position=8)
	private String sell_prc;
	/**値入*/
	@CsvBindByPosition(position=9)
	private String entry_prc;
	/**税込売価*/
	@CsvBindByPosition(position=10)
	private String tax_incl;
	/**在庫原価*/
	@CsvBindByPosition(position=11)
	private String inventory_cost;
	/**在庫金額*/
	@CsvBindByPosition(position=12)
	private String inventory_prc;
	/**税率*/
	@CsvBindByPosition(position=13)
	private String tax_rt;
	/**備考*/
	@CsvBindByPosition(position=14)
	private String exp_dt;
	
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
	public String getNow_prd_cnt() {
		return now_prd_cnt;
	}
	public void setNow_prd_cnt(String now_prd_cnt) {
		this.now_prd_cnt = now_prd_cnt;
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
	public String getEntry_prc() {
		return entry_prc;
	}
	public void setEntry_prc(String entry_prc) {
		this.entry_prc = entry_prc;
	}
	public String getTax_incl() {
		return tax_incl;
	}
	public void setTax_incl(String tax_incl) {
		this.tax_incl = tax_incl;
	}
	public String getInventory_cost() {
		return inventory_cost;
	}
	public void setInventory_cost(String inventory_cost) {
		this.inventory_cost = inventory_cost;
	}
	public String getInventory_prc() {
		return inventory_prc;
	}
	public void setInventory_prc(String inventory_prc) {
		this.inventory_prc = inventory_prc;
	}
	public String getTax_rt() {
		return tax_rt;
	}
	public void setTax_rt(String tax_rt) {
		this.tax_rt = tax_rt;
	}
	public String getExp_dt() {
		return exp_dt;
	}
	public void setExp_dt(String exp_dt) {
		this.exp_dt = exp_dt;
	}
}
