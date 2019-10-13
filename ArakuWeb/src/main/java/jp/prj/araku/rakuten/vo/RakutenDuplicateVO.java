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
	/**注文者姓*/
	@CsvBindByPosition(position=5)
	private String order_surname;
	/**注文者名*/
	@CsvBindByPosition(position=6)
	private String order_name;
	/**注文者姓カナ*/
	@CsvBindByPosition(position=7)
	private String order_surname_kana;
	/**注文者名カナ*/
	@CsvBindByPosition(position=8)
	private String order_name_kana;
	
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
	public String getOrder_surname() {
		return order_surname;
	}
	public void setOrder_surname(String order_surname) {
		this.order_surname = order_surname;
	}
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	public String getOrder_surname_kana() {
		return order_surname_kana;
	}
	public void setOrder_surname_kana(String order_surname_kana) {
		this.order_surname_kana = order_surname_kana;
	}
	public String getOrder_name_kana() {
		return order_name_kana;
	}
	public void setOrder_name_kana(String order_name_kana) {
		this.order_name_kana = order_name_kana;
	}

}
