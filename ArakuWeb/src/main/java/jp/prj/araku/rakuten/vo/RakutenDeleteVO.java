package jp.prj.araku.rakuten.vo;

import com.opencsv.bean.CsvBindByPosition;

public class RakutenDeleteVO {
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**送付先ID*/
	@CsvBindByPosition(position=1)
	private String ship_id;
	/**発送明細ID*/
	@CsvBindByPosition(position=2)
	private String ship_detail_id;
	/**お荷物伝票番号*/
	@CsvBindByPosition(position=3)
	private String baggage_claim_no;
	/**配送会社*/
	@CsvBindByPosition(position=4)
	private String delivery_company;
	/**発送日*/
	@CsvBindByPosition(position=5)
	private String ship_date;
	
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getShip_id() {
		return ship_id;
	}
	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
	}
	public String getShip_detail_id() {
		return ship_detail_id;
	}
	public void setShip_detail_id(String ship_detail_id) {
		this.ship_detail_id = ship_detail_id;
	}
	public String getBaggage_claim_no() {
		return baggage_claim_no;
	}
	public void setBaggage_claim_no(String baggage_claim_no) {
		this.baggage_claim_no = baggage_claim_no;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public String getShip_date() {
		return ship_date;
	}
	public void setShip_date(String ship_date) {
		this.ship_date = ship_date;
	}
}
