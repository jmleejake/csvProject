package jp.prj.araku.file.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class RCSVDownVO {
	/**受注番号*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**受注ステータス*/
	@CsvBindByPosition(position=1)
	private String order_status;
	/**処理番号*/
	@CsvBindByPosition(position=2)
	private String process_no;
	/**お荷物伝票番号*/
	@CsvBindByPosition(position=3)
	private String baggage_claim_no;
	/**配送会社*/
	@CsvBindByPosition(position=4)
	private String delivery_company;
	/**フリー項目01*/
	@CsvBindByPosition(position=5)
	private String free_space;
	
	/**検索用*/
	private ArrayList<String> seq_id_list;
	
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getProcess_no() {
		return process_no;
	}
	public void setProcess_no(String process_no) {
		this.process_no = process_no;
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
	public String getFree_space() {
		return free_space;
	}
	public void setFree_space(String free_space) {
		this.free_space = free_space;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	
}
