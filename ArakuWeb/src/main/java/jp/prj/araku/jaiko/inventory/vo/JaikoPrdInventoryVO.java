package jp.prj.araku.jaiko.inventory.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class JaikoPrdInventoryVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	
	/**商品コード*/
	@CsvBindByPosition(position=0)
	private String prd_cd;
	/**ブランド*/
	@CsvBindByPosition(position=1)
	private String brand_nm;
	/**商品名*/
	@CsvBindByPosition(position=2)
	private String prd_nm;
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=3)
	private String jan_cd;
	/**現在商品数*/
	@CsvBindByPosition(position=4)
	private String now_prd_cnt;
	/**入数*/
	@CsvBindByPosition(position=5)
	private String prd_qty;
	/**ケース数*/
	@CsvBindByPosition(position=6)
	private String prd_case;
	/**バラ数*/
	@CsvBindByPosition(position=7)
	private String prd_bara;
	/**本体売価*/
	@CsvBindByPosition(position=8)
	private String sell_prc;
	/**ロート数*/
	@CsvBindByPosition(position=9)
	private String prd_lot;
	/**取引先コード*/
	@CsvBindByPosition(position=10)
	private String dealer_id;
	/**取引先会社名*/
	@CsvBindByPosition(position=11)
	private String dealer_nm;
	
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	/**賞味期限*/
	private String exp_dt;
	
	private String search_type;
	
	/**検索用*/
	private ArrayList<String> seq_id_list;
	
	// for download
	/**規格*/
	private String std_info;
	/**商品単価*/
	private String prd_unit_prc;

	public String getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}

	public String getPrd_cd() {
		return prd_cd;
	}

	public void setPrd_cd(String prd_cd) {
		this.prd_cd = prd_cd;
	}

	public String getBrand_nm() {
		return brand_nm;
	}

	public void setBrand_nm(String brand_nm) {
		this.brand_nm = brand_nm;
	}

	public String getPrd_nm() {
		return prd_nm;
	}

	public void setPrd_nm(String prd_nm) {
		this.prd_nm = prd_nm;
	}

	public String getJan_cd() {
		return jan_cd;
	}

	public void setJan_cd(String jan_cd) {
		this.jan_cd = jan_cd;
	}

	public String getNow_prd_cnt() {
		return now_prd_cnt;
	}

	public void setNow_prd_cnt(String now_prd_cnt) {
		this.now_prd_cnt = now_prd_cnt;
	}

	public String getReg_user_id() {
		return reg_user_id;
	}

	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getUpd_user_id() {
		return upd_user_id;
	}

	public void setUpd_user_id(String upd_user_id) {
		this.upd_user_id = upd_user_id;
	}

	public String getUpd_dt() {
		return upd_dt;
	}

	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}

	public String getSearch_type() {
		return search_type;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public String getPrd_qty() {
		return prd_qty;
	}

	public void setPrd_qty(String prd_qty) {
		this.prd_qty = prd_qty;
	}

	public String getPrd_case() {
		return prd_case;
	}

	public void setPrd_case(String prd_case) {
		this.prd_case = prd_case;
	}

	public String getPrd_bara() {
		return prd_bara;
	}

	public void setPrd_bara(String prd_bara) {
		this.prd_bara = prd_bara;
	}

	public String getExp_dt() {
		return exp_dt;
	}

	public void setExp_dt(String exp_dt) {
		this.exp_dt = exp_dt;
	}

	public String getSell_prc() {
		return sell_prc;
	}

	public void setSell_prc(String sell_prc) {
		this.sell_prc = sell_prc;
	}

	public String getStd_info() {
		return std_info;
	}

	public void setStd_info(String std_info) {
		this.std_info = std_info;
	}

	public String getPrd_unit_prc() {
		return prd_unit_prc;
	}

	public void setPrd_unit_prc(String prd_unit_prc) {
		this.prd_unit_prc = prd_unit_prc;
	}

	public String getPrd_lot() {
		return prd_lot;
	}

	public void setPrd_lot(String prd_lot) {
		this.prd_lot = prd_lot;
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

	public ArrayList<String> getSeq_id_list() {
		return seq_id_list;
	}

	public void setSeq_id_list(ArrayList<String> seq_id_list) {
		this.seq_id_list = seq_id_list;
	}

}
