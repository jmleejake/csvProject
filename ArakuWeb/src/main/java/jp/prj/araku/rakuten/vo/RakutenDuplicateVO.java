package jp.prj.araku.rakuten.vo;

import com.opencsv.bean.CsvBindByPosition;

public class RakutenDuplicateVO {
	/**別紙番号*/
	@CsvBindByPosition(position=0)
	private String attach_no;
	/**注文番号*/
	@CsvBindByPosition(position=1)
	private String order_no;
	/**商品ID*/
	@CsvBindByPosition(position=2)
	private String product_id;
	/**商品名*/
	@CsvBindByPosition(position=3)
	private String product_name;
	/**項目・選択肢*/
	@CsvBindByPosition(position=4)
	private String product_option;
	/**個数*/
	@CsvBindByPosition(position=5)
	private String unit_no;
	/**送付先姓*/
	@CsvBindByPosition(position=6)
	private String delivery_surname;
	/**送付先名*/
	@CsvBindByPosition(position=7)
	private String delivery_name;
	/**送付先姓カナ*/
	@CsvBindByPosition(position=8)
	private String delivery_surname_kana;
	/**送付先名カナ*/
	@CsvBindByPosition(position=9)
	private String delivery_name_kana;
	
	public String getAttach_no() {
		return attach_no;
	}
	public void setAttach_no(String attach_no) {
		this.attach_no = attach_no;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_option() {
		return product_option;
	}
	public void setProduct_option(String product_option) {
		this.product_option = product_option;
	}
	public String getUnit_no() {
		return unit_no;
	}
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}
	public String getDelivery_surname() {
		return delivery_surname;
	}
	public void setDelivery_surname(String delivery_surname) {
		this.delivery_surname = delivery_surname;
	}
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	public String getDelivery_surname_kana() {
		return delivery_surname_kana;
	}
	public void setDelivery_surname_kana(String delivery_surname_kana) {
		this.delivery_surname_kana = delivery_surname_kana;
	}
	public String getDelivery_name_kana() {
		return delivery_name_kana;
	}
	public void setDelivery_name_kana(String delivery_name_kana) {
		this.delivery_name_kana = delivery_name_kana;
	}

}
