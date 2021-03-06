package jp.prj.araku.q10.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class Q10YamatoVO extends ArakuVO {
	/**配送状態*/
	@CsvBindByPosition(position=0)
	private String delivery_sts;
	/**注文番号*/
	@CsvBindByPosition(position=1)
	private String order_no;
	/**カート番号*/
	@CsvBindByPosition(position=2)
	private String cart_no;
	/**配送会社*/
	@CsvBindByPosition(position=3)
	private String delivery_company_q10;
	/**送り状番号*/
	@CsvBindByPosition(position=4)
	private String invoice_no;
	/**発送日*/
	@CsvBindByPosition(position=5)
	private String ship_date;
	/**発送予定日*/
	@CsvBindByPosition(position=6)
	private String ship_eta;
	/**商品名*/
	@CsvBindByPosition(position=7)
	private String product_name;
	/**数量*/
	@CsvBindByPosition(position=8)
	private String qty;
	/**オプション情報*/
	@CsvBindByPosition(position=9)
	private String option_info;
	/**オプションコード*/
	@CsvBindByPosition(position=10)
	private String option_cd;
	/**受取人名*/
	@CsvBindByPosition(position=11)
	private String recpt_name;
	/**販売者商品コード*/
	@CsvBindByPosition(position=12)
	private String product_cd;
	/**外部広告*/
	@CsvBindByPosition(position=13)
	private String ads_no_site;
	/**決済サイト*/
	@CsvBindByPosition(position=14)
	private String pay_site;
	
	public String getDelivery_sts() {
		return delivery_sts;
	}
	public void setDelivery_sts(String delivery_sts) {
		this.delivery_sts = delivery_sts;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getCart_no() {
		return cart_no;
	}
	public void setCart_no(String cart_no) {
		this.cart_no = cart_no;
	}
	public String getDelivery_company_q10() {
		return delivery_company_q10;
	}
	public void setDelivery_company_q10(String delivery_company_q10) {
		this.delivery_company_q10 = delivery_company_q10;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public String getShip_date() {
		return ship_date;
	}
	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}
	public String getShip_eta() {
		return ship_eta;
	}
	public void setShip_eta(String ship_eta) {
		this.ship_eta = ship_eta;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getOption_info() {
		return option_info;
	}
	public void setOption_info(String option_info) {
		this.option_info = option_info;
	}
	public String getOption_cd() {
		return option_cd;
	}
	public void setOption_cd(String option_cd) {
		this.option_cd = option_cd;
	}
	public String getRecpt_name() {
		return recpt_name;
	}
	public void setRecpt_name(String recpt_name) {
		this.recpt_name = recpt_name;
	}
	public String getProduct_cd() {
		return product_cd;
	}
	public void setProduct_cd(String product_cd) {
		this.product_cd = product_cd;
	}
	public String getAds_no_site() {
		return ads_no_site;
	}
	public void setAds_no_site(String ads_no_site) {
		this.ads_no_site = ads_no_site;
	}
	public String getPay_site() {
		return pay_site;
	}
	public void setPay_site(String pay_site) {
		this.pay_site = pay_site;
	}
}
