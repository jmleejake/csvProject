package jp.prj.araku.file.vo;

import com.opencsv.bean.CsvBindByPosition;

/**
 * 사가와는   お届け先名称２ (delivery_name2)을 키로 해서 
 * お問合せ送り状No(contact_no) 값을 갱신
 * */
public class NewSagawaVO {
	/**お問合せ送り状No*/
	@CsvBindByPosition(position=0)
	private String contact_no;
	/**出荷日時*/
	@CsvBindByPosition(position=1)
	private String delivery_datetime;
	/**住所録コード*/
	@CsvBindByPosition(position=2)
	private String add_book_code;
	/**お届け先電話番号*/
	@CsvBindByPosition(position=3)
	private String delivery_tel;
	/**お届け先郵便番号*/
	@CsvBindByPosition(position=4)
	private String delivery_post_no;
	/**お届け先住所１*/
	@CsvBindByPosition(position=5)
	private String delivery_add1;
	/**お届け先住所２*/
	@CsvBindByPosition(position=6)
	private String delivery_add2;
	/**お届け先住所３*/
	@CsvBindByPosition(position=7)
	private String delivery_add3;
	/**お届け先ＪＩＳコード*/
	@CsvBindByPosition(position=8)
	private String delivery_jis_code;
	/**お届け先名称１*/
	@CsvBindByPosition(position=9)
	private String delivery_name1;
	/**お届け先名称２*/
	@CsvBindByPosition(position=10)
	private String delivery_name2;
	/**グループ名*/
	@CsvBindByPosition(position=11)
	private String group_name;
	/**お客様管理ナンバー*/
	@CsvBindByPosition(position=12)
	private String client_manage_no;
	/**お客様コード*/
	@CsvBindByPosition(position=13)
	private String client_code;
	/**荷送人電話番号*/
	@CsvBindByPosition(position=14)
	private String client_tel;
	/**荷送人郵便番号*/
	@CsvBindByPosition(position=15)
	private String client_post_no;
	/**荷送人住所１*/
	@CsvBindByPosition(position=16)
	private String client_add1;
	/**荷送人住所２*/
	@CsvBindByPosition(position=17)
	private String client_add2;
	/**荷送人名称１*/
	@CsvBindByPosition(position=18)
	private String client_name1;
	/**荷送人名称２*/
	@CsvBindByPosition(position=19)
	private String client_name2;
	/**部署・担当者*/
	@CsvBindByPosition(position=20)
	private String in_charge;
	/**ご依頼主コード*/
	@CsvBindByPosition(position=21)
	private String request_code;
	/**ご依頼主電話番号*/
	@CsvBindByPosition(position=22)
	private String request_tel;
	/**ご依頼主郵便番号*/
	@CsvBindByPosition(position=23)
	private String request_post_no;
	/**ご依頼主住所１*/
	@CsvBindByPosition(position=24)
	private String request_add1;
	/**ご依頼主住所２*/
	@CsvBindByPosition(position=25)
	private String request_add2;
	/**ご依頼主名称１*/
	@CsvBindByPosition(position=26)
	private String request_name1;
	/**ご依頼主名称２*/
	@CsvBindByPosition(position=27)
	private String request_name2;
	/**荷姿コード*/
	@CsvBindByPosition(position=28)
	private String transport_code;
	/**品名１*/
	@CsvBindByPosition(position=29)
	private String product_name1;
	/**品名２*/
	@CsvBindByPosition(position=30)
	private String product_name2;
	/**品名３*/
	@CsvBindByPosition(position=31)
	private String product_name3;
	/**品名４*/
	@CsvBindByPosition(position=32)
	private String product_name4;
	/**品名５*/
	@CsvBindByPosition(position=33)
	private String product_name5;
	/**出荷個数*/
	@CsvBindByPosition(position=34)
	private String delivery_cnt;
	/**便種（スピードで選択）*/
	@CsvBindByPosition(position=35)
	private String speed_type;
	/**便種（商品）*/
	@CsvBindByPosition(position=36)
	private String product_type;
	/**配達日*/
	@CsvBindByPosition(position=37)
	private String delivery_date;
	/**配達指定時間帯*/
	@CsvBindByPosition(position=38)
	private String delivery_time;
	/**配達指定時間（時分）*/
	@CsvBindByPosition(position=39)
	private String delivery_time_detail;
	/**代引金額*/
	@CsvBindByPosition(position=40)
	private String total_amt;
	/**消費税*/
	@CsvBindByPosition(position=41)
	private String consume_tax;
	/**決済種別*/
	@CsvBindByPosition(position=42)
	private String payment_method;
	/**保険金額*/
	@CsvBindByPosition(position=43)
	private String insurance_amt;
	/**保険金額印字*/
	@CsvBindByPosition(position=44)
	private String insurance_status;
	/**指定シール1*/
	@CsvBindByPosition(position=45)
	private String select_seal1;
	/**指定シール2*/
	@CsvBindByPosition(position=46)
	private String select_seal2;
	/**指定シール3*/
	@CsvBindByPosition(position=47)
	private String select_seal3;
	/**営業店止め*/
	@CsvBindByPosition(position=48)
	private String store_stop;
	/**SRC区分*/
	@CsvBindByPosition(position=49)
	private String src_type;
	/**営業店コード*/
	@CsvBindByPosition(position=50)
	private String store_code;
	/**元着区分*/
	@CsvBindByPosition(position=51)
	private String origin_type;
	/**削除区分*/
	@CsvBindByPosition(position=52)
	private String delete_type;
	
	public String getContact_no() {
		return contact_no;
	}
	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}
	public String getDelivery_datetime() {
		return delivery_datetime;
	}
	public void setDelivery_datetime(String delivery_datetime) {
		this.delivery_datetime = delivery_datetime;
	}
	public String getAdd_book_code() {
		return add_book_code;
	}
	public void setAdd_book_code(String add_book_code) {
		this.add_book_code = add_book_code;
	}
	public String getDelivery_tel() {
		return delivery_tel;
	}
	public void setDelivery_tel(String delivery_tel) {
		this.delivery_tel = delivery_tel;
	}
	public String getDelivery_post_no() {
		return delivery_post_no;
	}
	public void setDelivery_post_no(String delivery_post_no) {
		this.delivery_post_no = delivery_post_no;
	}
	public String getDelivery_add1() {
		return delivery_add1;
	}
	public void setDelivery_add1(String delivery_add1) {
		this.delivery_add1 = delivery_add1;
	}
	public String getDelivery_add2() {
		return delivery_add2;
	}
	public void setDelivery_add2(String delivery_add2) {
		this.delivery_add2 = delivery_add2;
	}
	public String getDelivery_add3() {
		return delivery_add3;
	}
	public void setDelivery_add3(String delivery_add3) {
		this.delivery_add3 = delivery_add3;
	}
	public String getDelivery_jis_code() {
		return delivery_jis_code;
	}
	public void setDelivery_jis_code(String delivery_jis_code) {
		this.delivery_jis_code = delivery_jis_code;
	}
	public String getDelivery_name1() {
		return delivery_name1;
	}
	public void setDelivery_name1(String delivery_name1) {
		this.delivery_name1 = delivery_name1;
	}
	public String getDelivery_name2() {
		return delivery_name2;
	}
	public void setDelivery_name2(String delivery_name2) {
		this.delivery_name2 = delivery_name2;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getClient_manage_no() {
		return client_manage_no;
	}
	public void setClient_manage_no(String client_manage_no) {
		this.client_manage_no = client_manage_no;
	}
	public String getClient_code() {
		return client_code;
	}
	public void setClient_code(String client_code) {
		this.client_code = client_code;
	}
	public String getClient_tel() {
		return client_tel;
	}
	public void setClient_tel(String client_tel) {
		this.client_tel = client_tel;
	}
	public String getClient_post_no() {
		return client_post_no;
	}
	public void setClient_post_no(String client_post_no) {
		this.client_post_no = client_post_no;
	}
	public String getClient_add1() {
		return client_add1;
	}
	public void setClient_add1(String client_add1) {
		this.client_add1 = client_add1;
	}
	public String getClient_add2() {
		return client_add2;
	}
	public void setClient_add2(String client_add2) {
		this.client_add2 = client_add2;
	}
	public String getClient_name1() {
		return client_name1;
	}
	public void setClient_name1(String client_name1) {
		this.client_name1 = client_name1;
	}
	public String getClient_name2() {
		return client_name2;
	}
	public void setClient_name2(String client_name2) {
		this.client_name2 = client_name2;
	}
	public String getIn_charge() {
		return in_charge;
	}
	public void setIn_charge(String in_charge) {
		this.in_charge = in_charge;
	}
	public String getRequest_code() {
		return request_code;
	}
	public void setRequest_code(String request_code) {
		this.request_code = request_code;
	}
	public String getRequest_tel() {
		return request_tel;
	}
	public void setRequest_tel(String request_tel) {
		this.request_tel = request_tel;
	}
	public String getRequest_post_no() {
		return request_post_no;
	}
	public void setRequest_post_no(String request_post_no) {
		this.request_post_no = request_post_no;
	}
	public String getRequest_add1() {
		return request_add1;
	}
	public void setRequest_add1(String request_add1) {
		this.request_add1 = request_add1;
	}
	public String getRequest_add2() {
		return request_add2;
	}
	public void setRequest_add2(String request_add2) {
		this.request_add2 = request_add2;
	}
	public String getRequest_name1() {
		return request_name1;
	}
	public void setRequest_name1(String request_name1) {
		this.request_name1 = request_name1;
	}
	public String getRequest_name2() {
		return request_name2;
	}
	public void setRequest_name2(String request_name2) {
		this.request_name2 = request_name2;
	}
	public String getTransport_code() {
		return transport_code;
	}
	public void setTransport_code(String transport_code) {
		this.transport_code = transport_code;
	}
	public String getProduct_name1() {
		return product_name1;
	}
	public void setProduct_name1(String product_name1) {
		this.product_name1 = product_name1;
	}
	public String getProduct_name2() {
		return product_name2;
	}
	public void setProduct_name2(String product_name2) {
		this.product_name2 = product_name2;
	}
	public String getProduct_name3() {
		return product_name3;
	}
	public void setProduct_name3(String product_name3) {
		this.product_name3 = product_name3;
	}
	public String getProduct_name4() {
		return product_name4;
	}
	public void setProduct_name4(String product_name4) {
		this.product_name4 = product_name4;
	}
	public String getProduct_name5() {
		return product_name5;
	}
	public void setProduct_name5(String product_name5) {
		this.product_name5 = product_name5;
	}
	public String getDelivery_cnt() {
		return delivery_cnt;
	}
	public void setDelivery_cnt(String delivery_cnt) {
		this.delivery_cnt = delivery_cnt;
	}
	public String getSpeed_type() {
		return speed_type;
	}
	public void setSpeed_type(String speed_type) {
		this.speed_type = speed_type;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getDelivery_time_detail() {
		return delivery_time_detail;
	}
	public void setDelivery_time_detail(String delivery_time_detail) {
		this.delivery_time_detail = delivery_time_detail;
	}
	public String getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}
	public String getConsume_tax() {
		return consume_tax;
	}
	public void setConsume_tax(String consume_tax) {
		this.consume_tax = consume_tax;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getInsurance_amt() {
		return insurance_amt;
	}
	public void setInsurance_amt(String insurance_amt) {
		this.insurance_amt = insurance_amt;
	}
	public String getInsurance_status() {
		return insurance_status;
	}
	public void setInsurance_status(String insurance_status) {
		this.insurance_status = insurance_status;
	}
	public String getSelect_seal1() {
		return select_seal1;
	}
	public void setSelect_seal1(String select_seal1) {
		this.select_seal1 = select_seal1;
	}
	public String getSelect_seal2() {
		return select_seal2;
	}
	public void setSelect_seal2(String select_seal2) {
		this.select_seal2 = select_seal2;
	}
	public String getSelect_seal3() {
		return select_seal3;
	}
	public void setSelect_seal3(String select_seal3) {
		this.select_seal3 = select_seal3;
	}
	public String getStore_stop() {
		return store_stop;
	}
	public void setStore_stop(String store_stop) {
		this.store_stop = store_stop;
	}
	public String getSrc_type() {
		return src_type;
	}
	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}
	public String getStore_code() {
		return store_code;
	}
	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}
	public String getOrigin_type() {
		return origin_type;
	}
	public void setOrigin_type(String origin_type) {
		this.origin_type = origin_type;
	}
	public String getDelete_type() {
		return delete_type;
	}
	public void setDelete_type(String delete_type) {
		this.delete_type = delete_type;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewSagawaVO [");
		if (contact_no != null) {
			builder.append("contact_no=");
			builder.append(contact_no);
			builder.append(", ");
		}
		if (delivery_datetime != null) {
			builder.append("delivery_datetime=");
			builder.append(delivery_datetime);
			builder.append(", ");
		}
		if (add_book_code != null) {
			builder.append("add_book_code=");
			builder.append(add_book_code);
			builder.append(", ");
		}
		if (delivery_tel != null) {
			builder.append("delivery_tel=");
			builder.append(delivery_tel);
			builder.append(", ");
		}
		if (delivery_post_no != null) {
			builder.append("delivery_post_no=");
			builder.append(delivery_post_no);
			builder.append(", ");
		}
		if (delivery_add1 != null) {
			builder.append("delivery_add1=");
			builder.append(delivery_add1);
			builder.append(", ");
		}
		if (delivery_add2 != null) {
			builder.append("delivery_add2=");
			builder.append(delivery_add2);
			builder.append(", ");
		}
		if (delivery_add3 != null) {
			builder.append("delivery_add3=");
			builder.append(delivery_add3);
			builder.append(", ");
		}
		if (delivery_jis_code != null) {
			builder.append("delivery_jis_code=");
			builder.append(delivery_jis_code);
			builder.append(", ");
		}
		if (delivery_name1 != null) {
			builder.append("delivery_name1=");
			builder.append(delivery_name1);
			builder.append(", ");
		}
		if (delivery_name2 != null) {
			builder.append("delivery_name2=");
			builder.append(delivery_name2);
			builder.append(", ");
		}
		if (group_name != null) {
			builder.append("group_name=");
			builder.append(group_name);
			builder.append(", ");
		}
		if (client_manage_no != null) {
			builder.append("client_manage_no=");
			builder.append(client_manage_no);
			builder.append(", ");
		}
		if (client_code != null) {
			builder.append("client_code=");
			builder.append(client_code);
			builder.append(", ");
		}
		if (client_tel != null) {
			builder.append("client_tel=");
			builder.append(client_tel);
			builder.append(", ");
		}
		if (client_post_no != null) {
			builder.append("client_post_no=");
			builder.append(client_post_no);
			builder.append(", ");
		}
		if (client_add1 != null) {
			builder.append("client_add1=");
			builder.append(client_add1);
			builder.append(", ");
		}
		if (client_add2 != null) {
			builder.append("client_add2=");
			builder.append(client_add2);
			builder.append(", ");
		}
		if (client_name1 != null) {
			builder.append("client_name1=");
			builder.append(client_name1);
			builder.append(", ");
		}
		if (client_name2 != null) {
			builder.append("client_name2=");
			builder.append(client_name2);
			builder.append(", ");
		}
		if (in_charge != null) {
			builder.append("in_charge=");
			builder.append(in_charge);
			builder.append(", ");
		}
		if (request_code != null) {
			builder.append("request_code=");
			builder.append(request_code);
			builder.append(", ");
		}
		if (request_tel != null) {
			builder.append("request_tel=");
			builder.append(request_tel);
			builder.append(", ");
		}
		if (request_post_no != null) {
			builder.append("request_post_no=");
			builder.append(request_post_no);
			builder.append(", ");
		}
		if (request_add1 != null) {
			builder.append("request_add1=");
			builder.append(request_add1);
			builder.append(", ");
		}
		if (request_add2 != null) {
			builder.append("request_add2=");
			builder.append(request_add2);
			builder.append(", ");
		}
		if (request_name1 != null) {
			builder.append("request_name1=");
			builder.append(request_name1);
			builder.append(", ");
		}
		if (request_name2 != null) {
			builder.append("request_name2=");
			builder.append(request_name2);
			builder.append(", ");
		}
		if (transport_code != null) {
			builder.append("transport_code=");
			builder.append(transport_code);
			builder.append(", ");
		}
		if (product_name1 != null) {
			builder.append("product_name1=");
			builder.append(product_name1);
			builder.append(", ");
		}
		if (product_name2 != null) {
			builder.append("product_name2=");
			builder.append(product_name2);
			builder.append(", ");
		}
		if (product_name3 != null) {
			builder.append("product_name3=");
			builder.append(product_name3);
			builder.append(", ");
		}
		if (product_name4 != null) {
			builder.append("product_name4=");
			builder.append(product_name4);
			builder.append(", ");
		}
		if (product_name5 != null) {
			builder.append("product_name5=");
			builder.append(product_name5);
			builder.append(", ");
		}
		if (delivery_cnt != null) {
			builder.append("delivery_cnt=");
			builder.append(delivery_cnt);
			builder.append(", ");
		}
		if (speed_type != null) {
			builder.append("speed_type=");
			builder.append(speed_type);
			builder.append(", ");
		}
		if (product_type != null) {
			builder.append("product_type=");
			builder.append(product_type);
			builder.append(", ");
		}
		if (delivery_date != null) {
			builder.append("delivery_date=");
			builder.append(delivery_date);
			builder.append(", ");
		}
		if (delivery_time != null) {
			builder.append("delivery_time=");
			builder.append(delivery_time);
			builder.append(", ");
		}
		if (delivery_time_detail != null) {
			builder.append("delivery_time_detail=");
			builder.append(delivery_time_detail);
			builder.append(", ");
		}
		if (total_amt != null) {
			builder.append("total_amt=");
			builder.append(total_amt);
			builder.append(", ");
		}
		if (consume_tax != null) {
			builder.append("consume_tax=");
			builder.append(consume_tax);
			builder.append(", ");
		}
		if (payment_method != null) {
			builder.append("payment_method=");
			builder.append(payment_method);
			builder.append(", ");
		}
		if (insurance_amt != null) {
			builder.append("insurance_amt=");
			builder.append(insurance_amt);
			builder.append(", ");
		}
		if (insurance_status != null) {
			builder.append("insurance_status=");
			builder.append(insurance_status);
			builder.append(", ");
		}
		if (select_seal1 != null) {
			builder.append("select_seal1=");
			builder.append(select_seal1);
			builder.append(", ");
		}
		if (select_seal2 != null) {
			builder.append("select_seal2=");
			builder.append(select_seal2);
			builder.append(", ");
		}
		if (select_seal3 != null) {
			builder.append("select_seal3=");
			builder.append(select_seal3);
			builder.append(", ");
		}
		if (store_stop != null) {
			builder.append("store_stop=");
			builder.append(store_stop);
			builder.append(", ");
		}
		if (src_type != null) {
			builder.append("src_type=");
			builder.append(src_type);
			builder.append(", ");
		}
		if (store_code != null) {
			builder.append("store_code=");
			builder.append(store_code);
			builder.append(", ");
		}
		if (origin_type != null) {
			builder.append("origin_type=");
			builder.append(origin_type);
			builder.append(", ");
		}
		if (delete_type != null) {
			builder.append("delete_type=");
			builder.append(delete_type);
		}
		builder.append("]");
		return builder.toString();
	}
}
