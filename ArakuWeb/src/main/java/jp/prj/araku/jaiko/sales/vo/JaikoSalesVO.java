package jp.prj.araku.jaiko.sales.vo;

public class JaikoSalesVO {
	/**区分ID*/
	private String seq_id;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	/**区分*/
	private String gbn;
	/**取引先コード*/
	private String partner_id;
	/**取引先会社名*/
	private String partner_nm;
	/**納品日*/
	private String dlv_dt;
	/**売上番号*/
	private String sales_no;
	/**商品名*/
	private String prd_nm;
	/**商品コード*/
	private String prd_cd;
	/**仕様・規格*/
	private String spec;
	/**入り数*/
	private String entry_no;
	/**数量*/
	private String qty;
	/**納品価格*/
	private String dlv_prc;
	/**税率*/
	private String tax_rt;
	/**合計*/
	private String tot;
	/**消費税*/
	private String cnsp_tax;
	/**小計*/
	private String sub_tot;
	/**中合計*/
	private String mid_tot;
	/**請求番号*/
	private String bill_no;
	/**決済方法*/
	private String pay_method;
	/**決済完了有無*/
	private String pay_comp_yn;
	/**担当*/
	private String manager;
	/**メール*/
	private String mail;
	/**メモ*/
	private String memo;
	/**請求日付*/
	private String bill_dt;
	/**JANコード*/
	private String jan_cd;
	
	private String from_dt;
	private String to_dt;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
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
	public String getGbn() {
		return gbn;
	}
	public void setGbn(String gbn) {
		this.gbn = gbn;
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
	public String getDlv_dt() {
		return dlv_dt;
	}
	public void setDlv_dt(String dlv_dt) {
		this.dlv_dt = dlv_dt;
	}
	public String getSales_no() {
		return sales_no;
	}
	public void setSales_no(String sales_no) {
		this.sales_no = sales_no;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getPrd_cd() {
		return prd_cd;
	}
	public void setPrd_cd(String prd_cd) {
		this.prd_cd = prd_cd;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getEntry_no() {
		return entry_no;
	}
	public void setEntry_no(String entry_no) {
		this.entry_no = entry_no;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getDlv_prc() {
		return dlv_prc;
	}
	public void setDlv_prc(String dlv_prc) {
		this.dlv_prc = dlv_prc;
	}
	public String getTax_rt() {
		return tax_rt;
	}
	public void setTax_rt(String tax_rt) {
		this.tax_rt = tax_rt;
	}
	public String getTot() {
		return tot;
	}
	public void setTot(String tot) {
		this.tot = tot;
	}
	public String getCnsp_tax() {
		return cnsp_tax;
	}
	public void setCnsp_tax(String cnsp_tax) {
		this.cnsp_tax = cnsp_tax;
	}
	public String getSub_tot() {
		return sub_tot;
	}
	public void setSub_tot(String sub_tot) {
		this.sub_tot = sub_tot;
	}
	public String getMid_tot() {
		return mid_tot;
	}
	public void setMid_tot(String mid_tot) {
		this.mid_tot = mid_tot;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	public String getPay_comp_yn() {
		return pay_comp_yn;
	}
	public void setPay_comp_yn(String pay_comp_yn) {
		this.pay_comp_yn = pay_comp_yn;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getBill_dt() {
		return bill_dt;
	}
	public void setBill_dt(String bill_dt) {
		this.bill_dt = bill_dt;
	}
	public String getJan_cd() {
		return jan_cd;
	}
	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}

}
