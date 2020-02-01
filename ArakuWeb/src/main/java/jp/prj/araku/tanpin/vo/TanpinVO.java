package jp.prj.araku.tanpin.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

public class TanpinVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**商品メーカー*/
	@CsvBindByPosition(position=0)
	private String maker_cd;
	/**商品メーカー名*/
	@CsvBindByPosition(position=1)
	private String maker_nm;
	/**商品コード*/
	@CsvBindByPosition(position=2)
	private String prd_cd;
	/**商品名*/
	@CsvBindByPosition(position=3)
	private String prd_nm;
	/**容量*/
	@CsvBindByPosition(position=4)
	private String capacity;
	/**取引先コード*/
	@CsvBindByPosition(position=5)
	private String dealer_id;
	/**取引先会社名*/
	@CsvBindByPosition(position=6)
	private String dealer_nm;
	/**仕入金額*/
	@CsvBindByPosition(position=7)
	private String inprice;
	/**販売金額*/
	@CsvBindByPosition(position=8)
	private String price;
	/**商品販売基準金額*/
	@CsvBindByPosition(position=9)
	private String std_price;
	/**商品税(抜、込)*/
	@CsvBindByPosition(position=10)
	private String tax_inc;
	/**商品税率*/
	@CsvBindByPosition(position=11)
	private String tax_rt;
	
	private String select_type;
	private String search_type;
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
	public String getMaker_cd() {
		return maker_cd;
	}
	public void setMaker_cd(String maker_cd) {
		this.maker_cd = maker_cd;
	}
	public String getMaker_nm() {
		return maker_nm;
	}
	public void setMaker_nm(String maker_nm) {
		this.maker_nm = maker_nm;
	}
	public String getPrd_cd() {
		return prd_cd;
	}
	public void setPrd_cd(String prd_cd) {
		this.prd_cd = prd_cd;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getInprice() {
		return inprice;
	}
	public void setInprice(String inprice) {
		this.inprice = inprice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStd_price() {
		return std_price;
	}
	public void setStd_price(String std_price) {
		this.std_price = std_price;
	}
	public String getTax_inc() {
		return tax_inc;
	}
	public void setTax_inc(String tax_inc) {
		this.tax_inc = tax_inc;
	}
	public String getTax_rt() {
		return tax_rt;
	}
	public void setTax_rt(String tax_rt) {
		this.tax_rt = tax_rt;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getDealer_nm() {
		return dealer_nm;
	}
	public void setDealer_nm(String dealer_nm) {
		this.dealer_nm = dealer_nm;
	}
	public String getSelect_type() {
		return select_type;
	}
	public void setSelect_type(String select_type) {
		this.select_type = select_type;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}
	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}

}
