package jp.prj.araku.jaiko.warehouse.vo;

public class JaikoWareHouseVO {
	/**区分ID*/
	private String seq_id;
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
	/**入庫日*/
	private String warehouse_dt;
	/**出庫日*/
	private String delivery_dt;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品単価*/
	private String prd_unit_prc;
	/**商品入数*/
	private String prd_cnt;
	/**商品数量*/
	private String prd_quantity;
	/**商品合計*/
	private String prd_total;
	/**ＳＫＵ*/
	private String dsku;
	/**ＡＳＩＮ*/
	private String dasin;
	/**ライン数*/
	private String line_unit_no;
	/**注文数*/
	private String order_unit_no;
	/**商品税(抜、込)*/
	private String tax_incld;
	/**商品税率*/
	private String tax_rt;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	private String search_type;
	private String from_dt;
	private String to_dt;
	
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
	public String getWarehouse_dt() {
		return warehouse_dt;
	}
	public void setWarehouse_dt(String warehouse_dt) {
		this.warehouse_dt = warehouse_dt;
	}
	public String getDelivery_dt() {
		return delivery_dt;
	}
	public void setDelivery_dt(String delivery_dt) {
		this.delivery_dt = delivery_dt;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}
	public String getPrd_unit_prc() {
		return prd_unit_prc;
	}
	public void setPrd_unit_prc(String prd_unit_prc) {
		this.prd_unit_prc = prd_unit_prc;
	}
	public String getPrd_cnt() {
		return prd_cnt;
	}
	public void setPrd_cnt(String prd_cnt) {
		this.prd_cnt = prd_cnt;
	}
	public String getPrd_quantity() {
		return prd_quantity;
	}
	public void setPrd_quantity(String prd_quantity) {
		this.prd_quantity = prd_quantity;
	}
	public String getPrd_total() {
		return prd_total;
	}
	public void setPrd_total(String prd_total) {
		this.prd_total = prd_total;
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
	public String getLine_unit_no() {
		return line_unit_no;
	}
	public void setLine_unit_no(String line_unit_no) {
		this.line_unit_no = line_unit_no;
	}
	public String getOrder_unit_no() {
		return order_unit_no;
	}
	public void setOrder_unit_no(String order_unit_no) {
		this.order_unit_no = order_unit_no;
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
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	
	public String getFrom_dt() {
		return from_dt;
	}
	public void setFrom_dt(String from_dt) {
		this.from_dt = from_dt;
	}
	public String getTo_dt() {
		return to_dt;
	}
	public void setTo_dt(String to_dt) {
		this.to_dt = to_dt;
	}
	
	@Override
	public String toString() {
		return String.format(
				"JaikoWareHouseVO [seq_id=%s, partner_id=%s, partner_nm=%s, prd_cd=%s, brand_nm=%s, prd_nm=%s, warehouse_dt=%s, delivery_dt=%s, jan_cd=%s, prd_unit_prc=%s, prd_cnt=%s, prd_quantity=%s, prd_total=%s, dsku=%s, dasin=%s, line_unit_no=%s, order_unit_no=%s, tax_incld=%s, tax_rt=%s, reg_user_id=%s, reg_dt=%s, upd_user_id=%s, upd_dt=%s, search_type=%s, from_dt=%s, to_dt=%s]",
				seq_id, partner_id, partner_nm, prd_cd, brand_nm, prd_nm, warehouse_dt, delivery_dt, jan_cd,
				prd_unit_prc, prd_cnt, prd_quantity, prd_total, dsku, dasin, line_unit_no, order_unit_no, tax_incld,
				tax_rt, reg_user_id, reg_dt, upd_user_id, upd_dt, search_type, from_dt, to_dt);
	}

}
