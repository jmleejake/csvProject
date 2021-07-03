package jp.prj.araku.list.vo;

import java.util.ArrayList;

public class TranslationResultVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**R:楽天・A:アマゾン*/
	private String trans_target_type;
	/**置換結果テキスト*/
	private String result_text;
	/**エラーテキスト*/
	private String err_text;
	
	/**受注番号*/
	private String order_no;
	/**送付先名字*/
	private String delivery_surname;
	/**送付先名前*/
	private String delivery_name;
	/**商品名*/
	private String product_name;
	/**項目・選択肢*/
	private String product_option;
	/**個数*/
	private String unit_no;
	/**配送会社*/
	private String delivery_company;
	private ArrayList<String> deli_com_list;
	
	/**検索用*/
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
	public String getTrans_target_id() {
		return trans_target_id;
	}
	public void setTrans_target_id(String trans_target_id) {
		this.trans_target_id = trans_target_id;
	}
	public String getTrans_target_type() {
		return trans_target_type;
	}
	public void setTrans_target_type(String trans_target_type) {
		this.trans_target_type = trans_target_type;
	}
	public String getResult_text() {
		return result_text;
	}
	public void setResult_text(String result_text) {
		this.result_text = result_text;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
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
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public ArrayList<String> getDeli_com_list() {
		return deli_com_list;
	}
	public void setDeli_com_list(ArrayList<String> deli_com_list) {
		this.deli_com_list = deli_com_list;
	}
	public String getErr_text() {
		return err_text;
	}
	public void setErr_text(String err_text) {
		this.err_text = err_text;
	}
	
}
