package jp.prj.araku.file.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class RCSVDownVO extends ArakuVO {
	/**
	注文番号
	アップロードを行なう際、必須
	店舗ID-yyyymmdd-******
	*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**
	送付先ID
	送付先単位に振られる番号
	複数送付先がある場合に、どの送付先を更新するかの指定が必要
	半角数字10桁
	*/
	@CsvBindByPosition(position=1)
	private String ship_id;
	/**
	発送明細ID
	お荷物伝票番号、発送日、配送会社の報告単位に振られる番号
	登録済みの発送明細を更新する場合には必要
	半角数字12桁
	*/
	@CsvBindByPosition(position=2)
	private String ship_detail_id;
	/**
	お荷物伝票番号
	*/
	@CsvBindByPosition(position=3)
	private String baggage_claim_no;
	/**
	配送会社
	*/
	@CsvBindByPosition(position=4)
	private String delivery_company;
	/**
	発送日
	yyyy-mm-dd（yyyymmddも可）
	*/
	@CsvBindByPosition(position=5)
	private String ship_date;

	
	/**検索用*/
	private ArrayList<String> seq_id_list;


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


	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}


	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}
	
	
	
}
