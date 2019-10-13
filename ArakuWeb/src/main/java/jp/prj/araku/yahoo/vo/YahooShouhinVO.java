package jp.prj.araku.yahoo.vo;

import com.opencsv.bean.CsvBindByPosition;

public class YahooShouhinVO {
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_id;
	/**注文内容*/
	@CsvBindByPosition(position=1)
	private String line_id;
	/**商品名*/
	@CsvBindByPosition(position=2)
	private String title;
	/**個数*/
	@CsvBindByPosition(position=3)
	private String qty;
	/**ストアアカウント付き注文番号*/
	@CsvBindByPosition(position=4)
	private String id;
	/**商品コード*/
	@CsvBindByPosition(position=5)
	private String item_id;
	/**商品オプション*/
	@CsvBindByPosition(position=6)
	private String item_option_val;
	/**あす楽*/
	@CsvBindByPosition(position=7)
	private String lead_time_txt;
	/**税金率*/
	@CsvBindByPosition(position=8)
	private String item_tax_ratio;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getLine_id() {
		return line_id;
	}
	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_option_val() {
		return item_option_val;
	}
	public void setItem_option_val(String item_option_val) {
		this.item_option_val = item_option_val;
	}
	public String getLead_time_txt() {
		return lead_time_txt;
	}
	public void setLead_time_txt(String lead_time_txt) {
		this.lead_time_txt = lead_time_txt;
	}
	public String getItem_tax_ratio() {
		return item_tax_ratio;
	}
	public void setItem_tax_ratio(String item_tax_ratio) {
		this.item_tax_ratio = item_tax_ratio;
	}

}
