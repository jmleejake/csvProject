package jp.prj.araku.product.vo;

public class EstimateVO {
	/**区分ID*/
	private String seq_id;
	/**見積書番号*/
	private String estimate_id;
	/**取引先*/
	private String partner_id;
	/**取引先名*/
	private String partner_nm;
	/**商品コード*/
	private String prd_cd;
	/**ブランド*/
	private String brand_nm;
	/**商品名*/
	private String prd_nm;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**金額*/
	private String prd_prc;
	/**商品税(抜、込)*/
	private String tax_incld;
	/**商品税率*/
	private String tax_rt;
	/**規格*/
	private String std_info;
	/**ＳＫＵ*/
	private String dsku;
	/**ＡＳＩＮ*/
	private String dasin;
	
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	private double percent;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getEstimate_id() {
		return estimate_id;
	}
	public void setEstimate_id(String estimate_id) {
		this.estimate_id = estimate_id;
	}
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
	public String getPartner_nm() {
		return partner_nm;
	}
	public void setPartner_nm(String partner_nm) {
		this.partner_nm = partner_nm;
	}
	public String getPrd_cd() {
		return prd_cd;
	}
	public void setPrd_cd(String prd_cd) {
		this.prd_cd = prd_cd;
	}
	public String getBrand_nm() {
		return brand_nm;
	}
	public void setBrand_nm(String brand_nm) {
		this.brand_nm = brand_nm;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getTax_incld() {
		return tax_incld;
	}
	public void setTax_incld(String tax_incld) {
		this.tax_incld = tax_incld;
	}
	public String getTax_rt() {
		return tax_rt;
	}
	public void setTax_rt(String tax_rt) {
		this.tax_rt = tax_rt;
	}
	public String getStd_info() {
		return std_info;
	}
	public void setStd_info(String std_info) {
		this.std_info = std_info;
	}
	public String getDsku() {
		return dsku;
	}
	public void setDsku(String dsku) {
		this.dsku = dsku;
	}
	public String getDasin() {
		return dasin;
	}
	public void setDasin(String dasin) {
		this.dasin = dasin;
	}
	public String getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getUpd_user_id() {
		return upd_user_id;
	}
	public void setUpd_user_id(String upd_user_id) {
		this.upd_user_id = upd_user_id;
	}
	public String getUpd_dt() {
		return upd_dt;
	}
	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}
	public String getPrd_prc() {
		return prd_prc;
	}
	public void setPrd_prc(String prd_prc) {
		this.prd_prc = prd_prc;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}

}
