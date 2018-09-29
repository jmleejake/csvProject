package jp.prj.araku.amazon.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class AmazonVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**配送会社*/
	private String delivery_company;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**置換結果テキスト*/
	private String result_text;
	/**エラーテキスト*/
	private String err_text;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String search_type;
	
	@CsvBindByPosition(position=0)
	private String order_id;
	@CsvBindByPosition(position=1)
	private String order_item_id;
	@CsvBindByPosition(position=2)
	private String purchase_date;
	@CsvBindByPosition(position=3)
	private String payments_date;
	@CsvBindByPosition(position=4)
	private String reporting_date;
	@CsvBindByPosition(position=5)
	private String promise_date;
	@CsvBindByPosition(position=6)
	private String days_past_promise;
	@CsvBindByPosition(position=7)
	private String buyer_email;
	@CsvBindByPosition(position=8)
	private String buyer_name;
	@CsvBindByPosition(position=9)
	private String buyer_phone_number;
	@CsvBindByPosition(position=10)
	private String sku;
	@CsvBindByPosition(position=11)
	private String product_name;
	@CsvBindByPosition(position=12)
	private String quantity_purchased;
	@CsvBindByPosition(position=13)
	private String quantity_shipped;
	@CsvBindByPosition(position=14)
	private String quantity_to_ship;
	@CsvBindByPosition(position=15)
	private String ship_service_level;
	@CsvBindByPosition(position=16)
	private String recipient_name;
	@CsvBindByPosition(position=17)
	private String ship_address1;
	@CsvBindByPosition(position=18)
	private String ship_address2;
	@CsvBindByPosition(position=19)
	private String ship_address3;
	@CsvBindByPosition(position=20)
	private String ship_city;
	@CsvBindByPosition(position=21)
	private String ship_state;
	@CsvBindByPosition(position=22)
	private String ship_postal_code;
	@CsvBindByPosition(position=23)
	private String ship_country;
	@CsvBindByPosition(position=24)
	private String payment_method;
	@CsvBindByPosition(position=25)
	private String cod_collectible_amount;
	@CsvBindByPosition(position=26)
	private String already_paid;
	@CsvBindByPosition(position=27)
	private String payment_method_fee;
	@CsvBindByPosition(position=28)
	private String scheduled_delivery_start_date;
	@CsvBindByPosition(position=29)
	private String scheduled_delivery_end_date;
	@CsvBindByPosition(position=30)
	private String points_granted;
	@CsvBindByPosition(position=31)
	private String is_prime;
	
	private ArrayList<String> seq_id_list;
 	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getRegister_date() {
		return register_date;
	}
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
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
	public String getPurchase_date() {
		return purchase_date;
	}
	public void setPurchase_date(String purchase_date) {
		this.purchase_date = purchase_date;
	}
	public String getPayments_date() {
		return payments_date;
	}
	public void setPayments_date(String payments_date) {
		this.payments_date = payments_date;
	}
	public String getReporting_date() {
		return reporting_date;
	}
	public void setReporting_date(String reporting_date) {
		this.reporting_date = reporting_date;
	}
	public String getPromise_date() {
		return promise_date;
	}
	public void setPromise_date(String promise_date) {
		this.promise_date = promise_date;
	}
	public String getDays_past_promise() {
		return days_past_promise;
	}
	public void setDays_past_promise(String days_past_promise) {
		this.days_past_promise = days_past_promise;
	}
	public String getBuyer_email() {
		return buyer_email;
	}
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public String getBuyer_phone_number() {
		return buyer_phone_number;
	}
	public void setBuyer_phone_number(String buyer_phone_number) {
		this.buyer_phone_number = buyer_phone_number;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getQuantity_purchased() {
		return quantity_purchased;
	}
	public void setQuantity_purchased(String quantity_purchased) {
		this.quantity_purchased = quantity_purchased;
	}
	public String getQuantity_shipped() {
		return quantity_shipped;
	}
	public void setQuantity_shipped(String quantity_shipped) {
		this.quantity_shipped = quantity_shipped;
	}
	public String getQuantity_to_ship() {
		return quantity_to_ship;
	}
	public void setQuantity_to_ship(String quantity_to_ship) {
		this.quantity_to_ship = quantity_to_ship;
	}
	public String getShip_service_level() {
		return ship_service_level;
	}
	public void setShip_service_level(String ship_service_level) {
		this.ship_service_level = ship_service_level;
	}
	public String getRecipient_name() {
		return recipient_name;
	}
	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}
	public String getShip_address1() {
		return ship_address1;
	}
	public void setShip_address1(String ship_address1) {
		this.ship_address1 = ship_address1;
	}
	public String getShip_address2() {
		return ship_address2;
	}
	public void setShip_address2(String ship_address2) {
		this.ship_address2 = ship_address2;
	}
	public String getShip_address3() {
		return ship_address3;
	}
	public void setShip_address3(String ship_address3) {
		this.ship_address3 = ship_address3;
	}
	public String getShip_city() {
		return ship_city;
	}
	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}
	public String getShip_state() {
		return ship_state;
	}
	public void setShip_state(String ship_state) {
		this.ship_state = ship_state;
	}
	public String getShip_postal_code() {
		return ship_postal_code;
	}
	public void setShip_postal_code(String ship_postal_code) {
		this.ship_postal_code = ship_postal_code;
	}
	public String getShip_country() {
		return ship_country;
	}
	public void setShip_country(String ship_country) {
		this.ship_country = ship_country;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getCod_collectible_amount() {
		return cod_collectible_amount;
	}
	public void setCod_collectible_amount(String cod_collectible_amount) {
		this.cod_collectible_amount = cod_collectible_amount;
	}
	public String getAlready_paid() {
		return already_paid;
	}
	public void setAlready_paid(String already_paid) {
		this.already_paid = already_paid;
	}
	public String getPayment_method_fee() {
		return payment_method_fee;
	}
	public void setPayment_method_fee(String payment_method_fee) {
		this.payment_method_fee = payment_method_fee;
	}
	public String getScheduled_delivery_start_date() {
		return scheduled_delivery_start_date;
	}
	public void setScheduled_delivery_start_date(String scheduled_delivery_start_date) {
		this.scheduled_delivery_start_date = scheduled_delivery_start_date;
	}
	public String getScheduled_delivery_end_date() {
		return scheduled_delivery_end_date;
	}
	public void setScheduled_delivery_end_date(String scheduled_delivery_end_date) {
		this.scheduled_delivery_end_date = scheduled_delivery_end_date;
	}
	public String getPoints_granted() {
		return points_granted;
	}
	public void setPoints_granted(String points_granted) {
		this.points_granted = points_granted;
	}
	public String getIs_prime() {
		return is_prime;
	}
	public void setIs_prime(String is_prime) {
		this.is_prime = is_prime;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	public String getTrans_target_id() {
		return trans_target_id;
	}
	public void setTrans_target_id(String trans_target_id) {
		this.trans_target_id = trans_target_id;
	}
	public String getResult_text() {
		return result_text;
	}
	public void setResult_text(String result_text) {
		this.result_text = result_text;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public String getErr_text() {
		return err_text;
	}
	public void setErr_text(String err_text) {
		this.err_text = err_text;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AmazonVO [");
		if (seq_id != null) {
			builder.append("seq_id=");
			builder.append(seq_id);
			builder.append(", ");
		}
		if (register_date != null) {
			builder.append("register_date=");
			builder.append(register_date);
			builder.append(", ");
		}
		if (update_date != null) {
			builder.append("update_date=");
			builder.append(update_date);
			builder.append(", ");
		}
		if (delivery_company != null) {
			builder.append("delivery_company=");
			builder.append(delivery_company);
			builder.append(", ");
		}
		if (order_id != null) {
			builder.append("order_id=");
			builder.append(order_id);
			builder.append(", ");
		}
		if (order_item_id != null) {
			builder.append("order_item_id=");
			builder.append(order_item_id);
			builder.append(", ");
		}
		if (purchase_date != null) {
			builder.append("purchase_date=");
			builder.append(purchase_date);
			builder.append(", ");
		}
		if (payments_date != null) {
			builder.append("payments_date=");
			builder.append(payments_date);
			builder.append(", ");
		}
		if (reporting_date != null) {
			builder.append("reporting_date=");
			builder.append(reporting_date);
			builder.append(", ");
		}
		if (promise_date != null) {
			builder.append("promise_date=");
			builder.append(promise_date);
			builder.append(", ");
		}
		if (days_past_promise != null) {
			builder.append("days_past_promise=");
			builder.append(days_past_promise);
			builder.append(", ");
		}
		if (buyer_email != null) {
			builder.append("buyer_email=");
			builder.append(buyer_email);
			builder.append(", ");
		}
		if (buyer_name != null) {
			builder.append("buyer_name=");
			builder.append(buyer_name);
			builder.append(", ");
		}
		if (buyer_phone_number != null) {
			builder.append("buyer_phone_number=");
			builder.append(buyer_phone_number);
			builder.append(", ");
		}
		if (sku != null) {
			builder.append("sku=");
			builder.append(sku);
			builder.append(", ");
		}
		if (product_name != null) {
			builder.append("product_name=");
			builder.append(product_name);
			builder.append(", ");
		}
		if (quantity_purchased != null) {
			builder.append("quantity_purchased=");
			builder.append(quantity_purchased);
			builder.append(", ");
		}
		if (quantity_shipped != null) {
			builder.append("quantity_shipped=");
			builder.append(quantity_shipped);
			builder.append(", ");
		}
		if (quantity_to_ship != null) {
			builder.append("quantity_to_ship=");
			builder.append(quantity_to_ship);
			builder.append(", ");
		}
		if (ship_service_level != null) {
			builder.append("ship_service_level=");
			builder.append(ship_service_level);
			builder.append(", ");
		}
		if (recipient_name != null) {
			builder.append("recipient_name=");
			builder.append(recipient_name);
			builder.append(", ");
		}
		if (ship_address1 != null) {
			builder.append("ship_address1=");
			builder.append(ship_address1);
			builder.append(", ");
		}
		if (ship_address2 != null) {
			builder.append("ship_address2=");
			builder.append(ship_address2);
			builder.append(", ");
		}
		if (ship_address3 != null) {
			builder.append("ship_address3=");
			builder.append(ship_address3);
			builder.append(", ");
		}
		if (ship_city != null) {
			builder.append("ship_city=");
			builder.append(ship_city);
			builder.append(", ");
		}
		if (ship_state != null) {
			builder.append("ship_state=");
			builder.append(ship_state);
			builder.append(", ");
		}
		if (ship_postal_code != null) {
			builder.append("ship_postal_code=");
			builder.append(ship_postal_code);
			builder.append(", ");
		}
		if (ship_country != null) {
			builder.append("ship_country=");
			builder.append(ship_country);
			builder.append(", ");
		}
		if (payment_method != null) {
			builder.append("payment_method=");
			builder.append(payment_method);
			builder.append(", ");
		}
		if (cod_collectible_amount != null) {
			builder.append("cod_collectible_amount=");
			builder.append(cod_collectible_amount);
			builder.append(", ");
		}
		if (already_paid != null) {
			builder.append("already_paid=");
			builder.append(already_paid);
			builder.append(", ");
		}
		if (payment_method_fee != null) {
			builder.append("payment_method_fee=");
			builder.append(payment_method_fee);
			builder.append(", ");
		}
		if (scheduled_delivery_start_date != null) {
			builder.append("scheduled_delivery_start_date=");
			builder.append(scheduled_delivery_start_date);
			builder.append(", ");
		}
		if (scheduled_delivery_end_date != null) {
			builder.append("scheduled_delivery_end_date=");
			builder.append(scheduled_delivery_end_date);
			builder.append(", ");
		}
		if (points_granted != null) {
			builder.append("points_granted=");
			builder.append(points_granted);
			builder.append(", ");
		}
		if (is_prime != null) {
			builder.append("is_prime=");
			builder.append(is_prime);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
