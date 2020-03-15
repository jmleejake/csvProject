package jp.prj.araku.product.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

/**
 * 商品分析
 * */
public class ProductAnalysisVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**MEMO*/
	@CsvBindByPosition(position=0)
	private String memo;
	/**商品名*/
	@CsvBindByPosition(position=1)
	private String prd_nm;
	/**ＳＫＵ*/
	@CsvBindByPosition(position=2)
	private String sku;
	/**URL*/
	@CsvBindByPosition(position=3)
	private String url;
	/**商品構成*/
	@CsvBindByPosition(position=4)
	private String prd_config;
	/**1個当たり仕入金額(税別)*/
	@CsvBindByPosition(position=5)
	private String prd_price;
	/**包装(箱+印刷+他)*/
	@CsvBindByPosition(position=6)
	private String prd_pkg;
	/**送料*/
	@CsvBindByPosition(position=7)
	private String ship_cost;
	/**販売手数料*/
	@CsvBindByPosition(position=8)
	private String sales_comm_ratio;
	/**販売価格*/
	@CsvBindByPosition(position=9)
	private String sales_price;
	/**備考*/
	@CsvBindByPosition(position=10)
	private String etc;
	/**合計仕入価格*/
	private String ttl_price;
	/**販売手数料金額*/
	private String sales_comm_price;
	/**利益*/
	private String benefit;
	/**利益率*/
	private String benefit_ratio;
	/**追加送料*/
	private String add_ship_cost;
	
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPrd_nm() {
		return prd_nm;
	}
	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPrd_config() {
		return prd_config;
	}
	public void setPrd_config(String prd_config) {
		this.prd_config = prd_config;
	}
	public String getPrd_price() {
		return prd_price;
	}
	public void setPrd_price(String prd_price) {
		this.prd_price = prd_price;
	}
	public String getPrd_pkg() {
		return prd_pkg;
	}
	public void setPrd_pkg(String prd_pkg) {
		this.prd_pkg = prd_pkg;
	}
	public String getShip_cost() {
		return ship_cost;
	}
	public void setShip_cost(String ship_cost) {
		this.ship_cost = ship_cost;
	}
	public String getSales_comm_ratio() {
		return sales_comm_ratio;
	}
	public void setSales_comm_ratio(String sales_comm_ratio) {
		this.sales_comm_ratio = sales_comm_ratio;
	}
	public String getSales_price() {
		return sales_price;
	}
	public void setSales_price(String sales_price) {
		this.sales_price = sales_price;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getTtl_price() {
		return ttl_price;
	}
	public void setTtl_price(String ttl_price) {
		this.ttl_price = ttl_price;
	}
	public String getSales_comm_price() {
		return sales_comm_price;
	}
	public void setSales_comm_price(String sales_comm_price) {
		this.sales_comm_price = sales_comm_price;
	}
	public String getBenefit() {
		return benefit;
	}
	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}
	public String getBenefit_ratio() {
		return benefit_ratio;
	}
	public void setBenefit_ratio(String benefit_ratio) {
		this.benefit_ratio = benefit_ratio;
	}
	public String getAdd_ship_cost() {
		return add_ship_cost;
	}
	public void setAdd_ship_cost(String add_ship_cost) {
		this.add_ship_cost = add_ship_cost;
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
