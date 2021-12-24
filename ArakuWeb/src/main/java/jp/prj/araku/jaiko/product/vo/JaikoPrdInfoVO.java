package jp.prj.araku.jaiko.product.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class JaikoPrdInfoVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**取引先*/
	@CsvBindByPosition(position=0)
	private String partner_id;
	/**取引先名*/
	@CsvBindByPosition(position=1)
	private String partner_nm;
	/**商品コード*/
	@CsvBindByPosition(position=2)
	private String prd_cd;
	/**ブランド*/
	@CsvBindByPosition(position=3)
	private String brand_nm;
	/**商品名*/
	@CsvBindByPosition(position=4)
	private String prd_nm;
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=5)
	private String jan_cd;
	/**商品入数*/
	@CsvBindByPosition(position=6)
	private String prd_cnt;
	/**商品単価*/
	@CsvBindByPosition(position=7)
	private String prd_unit_prc;
	/**商品税(抜、込)*/
	@CsvBindByPosition(position=8)
	private String tax_incld;
	/**商品税率*/
	@CsvBindByPosition(position=9)
	private String tax_rt;
	/**規格*/
	@CsvBindByPosition(position=10)
	private String std_info;
	/**ＳＫＵ*/
	@CsvBindByPosition(position=11)
	private String dsku;
	/**ＡＳＩＮ*/
	@CsvBindByPosition(position=12)
	private String dasin;
	
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	
	private String search_type;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
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
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getPrd_unit_prc() {
		return prd_unit_prc;
	}
	public void setPrd_unit_prc(String prd_unit_prc) {
		this.prd_unit_prc = prd_unit_prc;
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
	
	public String getStd_info() {
		return std_info;
	}
	public void setStd_info(String std_info) {
		this.std_info = std_info;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	
	@Override
	public String toString() {
		return String.format(
				"JaikoPrdInfoVO [seq_id=%s, partner_id=%s, partner_nm=%s, prd_cd=%s, brand_nm=%s, prd_nm=%s, jan_cd=%s, prd_cnt=%s, prd_unit_prc=%s, dsku=%s, dasin=%s, tax_incld=%s, tax_rt=%s, reg_user_id=%s, reg_dt=%s, upd_user_id=%s, upd_dt=%s, std_info=%s, search_type=%s]",
				seq_id, partner_id, partner_nm, prd_cd, brand_nm, prd_nm, jan_cd, prd_cnt, prd_unit_prc, dsku, dasin,
				tax_incld, tax_rt, reg_user_id, reg_dt, upd_user_id, upd_dt, std_info, search_type);
	}
	
}
