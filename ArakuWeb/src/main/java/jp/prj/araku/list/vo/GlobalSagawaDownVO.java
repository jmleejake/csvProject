package jp.prj.araku.list.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class GlobalSagawaDownVO extends ArakuVO {
	/**Seller CODE*/
	@CsvBindByPosition(position=0)
	private String seller_cd;
	/**PICKUP_DATE*/
	@CsvBindByPosition(position=1)
	private String pick_dt;
	/**ORDER_NO*/
	@CsvBindByPosition(position=2)
	private String order_no;
	/**CONSIGNEE_NAME*/
	@CsvBindByPosition(position=3)
	private String consign_nm;
	/**YOMIGANA*/
	@CsvBindByPosition(position=4)
	private String consign_nm_kana;
	/**CONSIGNEE_ADDRESS1*/
	@CsvBindByPosition(position=5)
	private String consign_add1;
	/**CONSIGNEE_ADDRESS2*/
	@CsvBindByPosition(position=6)
	private String consign_add2;
	/**CONSIGNEE_POSTALCODE*/
	@CsvBindByPosition(position=7)
	private String consign_post_no;
	/**CONSIGNEE_PHONENO*/
	@CsvBindByPosition(position=8)
	private String consign_tel;
	/**CONSIGNEE_MAILADDRESS*/
	@CsvBindByPosition(position=9)
	private String consign_email;
	/**DELIVERY_DATE*/
	@CsvBindByPosition(position=10)
	private String delivery_dt;
	/**DELIVERY_TIME*/
	@CsvBindByPosition(position=11)
	private String delivery_tm;
	/**PKG*/
	@CsvBindByPosition(position=12)
	private String pkg;
	/**WEIGHT*/
	@CsvBindByPosition(position=13)
	private String weight;
	/**ITEM_CODE*/
	@CsvBindByPosition(position=14)
	private String item_cd;
	/**ITEM_NAME*/
	@CsvBindByPosition(position=15)
	private String item_nm;
	/**ITEM_PCS*/
	@CsvBindByPosition(position=16)
	private String item_pcs;
	/**UNIT_PRICE*/
	@CsvBindByPosition(position=17)
	private String unit_price;
	/**ITEM_ORIGIN*/
	@CsvBindByPosition(position=18)
	private String item_origin;
	public String getSeller_cd() {
		return seller_cd;
	}
	public void setSeller_cd(String seller_cd) {
		this.seller_cd = seller_cd;
	}
	public String getPick_dt() {
		return pick_dt;
	}
	public void setPick_dt(String pick_dt) {
		this.pick_dt = pick_dt;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getConsign_nm() {
		return consign_nm;
	}
	public void setConsign_nm(String consign_nm) {
		this.consign_nm = consign_nm;
	}
	public String getConsign_nm_kana() {
		return consign_nm_kana;
	}
	public void setConsign_nm_kana(String consign_nm_kana) {
		this.consign_nm_kana = consign_nm_kana;
	}
	public String getConsign_add1() {
		return consign_add1;
	}
	public void setConsign_add1(String consign_add1) {
		this.consign_add1 = consign_add1;
	}
	public String getConsign_add2() {
		return consign_add2;
	}
	public void setConsign_add2(String consign_add2) {
		this.consign_add2 = consign_add2;
	}
	public String getConsign_post_no() {
		return consign_post_no;
	}
	public void setConsign_post_no(String consign_post_no) {
		this.consign_post_no = consign_post_no;
	}
	public String getConsign_tel() {
		return consign_tel;
	}
	public void setConsign_tel(String consign_tel) {
		this.consign_tel = consign_tel;
	}
	public String getConsign_email() {
		return consign_email;
	}
	public void setConsign_email(String consign_email) {
		this.consign_email = consign_email;
	}
	public String getDelivery_dt() {
		return delivery_dt;
	}
	public void setDelivery_dt(String delivery_dt) {
		this.delivery_dt = delivery_dt;
	}
	public String getDelivery_tm() {
		return delivery_tm;
	}
	public void setDelivery_tm(String delivery_tm) {
		this.delivery_tm = delivery_tm;
	}
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getItem_cd() {
		return item_cd;
	}
	public void setItem_cd(String item_cd) {
		this.item_cd = item_cd;
	}
	public String getItem_nm() {
		return item_nm;
	}
	public void setItem_nm(String item_nm) {
		this.item_nm = item_nm;
	}
	public String getItem_pcs() {
		return item_pcs;
	}
	public void setItem_pcs(String item_pcs) {
		this.item_pcs = item_pcs;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getItem_origin() {
		return item_origin;
	}
	public void setItem_origin(String item_origin) {
		this.item_origin = item_origin;
	}

}
