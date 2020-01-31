package jp.prj.araku.amazon.vo;

import com.opencsv.bean.CsvBindByPosition;

public class AmazonFileVO {
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_id;
	/**注文商品番号*/
	@CsvBindByPosition(position=1)
	private String order_item_id;
	/**出荷数*/
	@CsvBindByPosition(position=2)
	private String quantity;
	/**出荷日*/
	@CsvBindByPosition(position=3)
	private String ship_date;
	/**配送業者コード*/
	@CsvBindByPosition(position=4)
	private String carrier_code;
	/**配送業者名*/
	@CsvBindByPosition(position=5)
	private String carrier_name;
	/**お問い合わせ伝票番号*/
	@CsvBindByPosition(position=6)
	private String tracking_number;
	/**配送方法*/
	@CsvBindByPosition(position=7)
	private String ship_method;
	/**代金引換*/
	@CsvBindByPosition(position=8)
	private String cod_collection_method;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_item_id() {
		return order_item_id;
	}
	public void setOrder_item_id(String order_item_id) {
		this.order_item_id = order_item_id;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getShip_date() {
		return ship_date;
	}
	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}
	public String getCarrier_code() {
		return carrier_code;
	}
	public void setCarrier_code(String carrier_code) {
		this.carrier_code = carrier_code;
	}
	public String getCarrier_name() {
		return carrier_name;
	}
	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}
	public String getTracking_number() {
		return tracking_number;
	}
	public void setTracking_number(String tracking_number) {
		this.tracking_number = tracking_number;
	}
	public String getShip_method() {
		return ship_method;
	}
	public void setShip_method(String ship_method) {
		this.ship_method = ship_method;
	}
	public String getCod_collection_method() {
		return cod_collection_method;
	}
	public void setCod_collection_method(String cod_collection_method) {
		this.cod_collection_method = cod_collection_method;
	}

}
