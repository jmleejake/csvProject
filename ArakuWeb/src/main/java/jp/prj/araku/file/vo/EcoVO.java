package jp.prj.araku.file.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class EcoVO extends ArakuVO {
	/**
	* 届け先_電話番号
	*/
	@CsvBindByPosition(position=0)
	private String delivery_phn_no;
	/**
	* 届け先_郵便番号(必須)
	*/
	@CsvBindByPosition(position=1)
	private String delivery_zip_cd;
	/**
	* 届け先_住所(都道府県)(必須)
	*/
	@CsvBindByPosition(position=2)
	private String delivery_add1;
	/**
	* 届け先_住所(市区町村郡)
	*/
	@CsvBindByPosition(position=3)
	private String delivery_add2;
	/**
	* 届け先_住所(町域名+番地)
	*/
	@CsvBindByPosition(position=4)
	private String delivery_add3;
	/**
	* 届け先_住所(建物名)
	*/
	@CsvBindByPosition(position=5)
	private String delivery_add4;
	/**
	* 届け先_名称(必須)
	*/
	@CsvBindByPosition(position=6)
	private String delivery_nm1;
	/**
	* 届け先_名称２
	*/
	@CsvBindByPosition(position=7)
	private String delivery_nm2;
	/**
	* 届け先_部署名
	*/
	@CsvBindByPosition(position=8)
	private String delivery_dept_nm;
	/**
	* 届け先_担当者名
	*/
	@CsvBindByPosition(position=9)
	private String delivery_tantou_nm;
	/**
	* 届け先_備考1(品名など)
	*/
	@CsvBindByPosition(position=10)
	private String delivery_etc1;
	/**
	* 届け先_備考2
	*/
	@CsvBindByPosition(position=11)
	private String delivery_etc2;
	/**
	* 届け先_備考3
	*/
	@CsvBindByPosition(position=12)
	private String delivery_etc3;
	/**
	* 届け先_備考4
	*/
	@CsvBindByPosition(position=13)
	private String delivery_etc4;
	/**
	* 依頼主_電話番号
	*/
	@CsvBindByPosition(position=14)
	private String client_phn_no;
	/**
	* 依頼主_郵便番号(必須)
	*/
	@CsvBindByPosition(position=15)
	private String client_zip_cd;
	/**
	* 依頼主_住所(都道府県)(必須)
	*/
	@CsvBindByPosition(position=16)
	private String client_add1;
	/**
	* 依頼主_住所(市区町村郡
	*/
	@CsvBindByPosition(position=17)
	private String client_add2;
	/**
	* 依頼主_住所(町域名+番地
	*/
	@CsvBindByPosition(position=18)
	private String client_add3;
	/**
	* 依頼主_住所(建物名)
	*/
	@CsvBindByPosition(position=19)
	private String client_add4;
	/**
	* 依頼主_名称(必須)
	*/
	@CsvBindByPosition(position=20)
	private String client_nm1;
	/**
	* 依頼主_名称２
	*/
	@CsvBindByPosition(position=21)
	private String client_nm2;
	/**
	* 依頼主_部署名
	*/
	@CsvBindByPosition(position=22)
	private String client_dept_nm;
	/**
	* 依頼主_担当者名
	*/
	@CsvBindByPosition(position=23)
	private String client_tantou_nm;
	/**
	* 下段備考1
	*/
	@CsvBindByPosition(position=24)
	private String client_etc1;
	/**
	* 下段備考2
	*/
	@CsvBindByPosition(position=25)
	private String client_etc2;
	/**
	* 下段備考３
	*/
	@CsvBindByPosition(position=26)
	private String client_etc3;
	/**
	* 下段備考４
	*/
	@CsvBindByPosition(position=27)
	private String client_etc4;
	/**
	* 発送種別
	*/
	@CsvBindByPosition(position=28)
	private String ship_type;
	/**
	* 空き項目
	*/
	@CsvBindByPosition(position=29)
	private String empty_item;
	/**
	* お届け予定月
	*/
	@CsvBindByPosition(position=30)
	private String est_delivery_mon;
	/**
	* お届け予定日
	*/
	@CsvBindByPosition(position=31)
	private String est_delivery_dt;
	
	public String getDelivery_phn_no() {
		return delivery_phn_no;
	}
	public void setDelivery_phn_no(String delivery_phn_no) {
		this.delivery_phn_no = delivery_phn_no;
	}
	public String getDelivery_zip_cd() {
		return delivery_zip_cd;
	}
	public void setDelivery_zip_cd(String delivery_zip_cd) {
		this.delivery_zip_cd = delivery_zip_cd;
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
	public String getDelivery_add4() {
		return delivery_add4;
	}
	public void setDelivery_add4(String delivery_add4) {
		this.delivery_add4 = delivery_add4;
	}
	public String getDelivery_nm1() {
		return delivery_nm1;
	}
	public void setDelivery_nm1(String delivery_nm1) {
		this.delivery_nm1 = delivery_nm1;
	}
	public String getDelivery_nm2() {
		return delivery_nm2;
	}
	public void setDelivery_nm2(String delivery_nm2) {
		this.delivery_nm2 = delivery_nm2;
	}
	public String getDelivery_dept_nm() {
		return delivery_dept_nm;
	}
	public void setDelivery_dept_nm(String delivery_dept_nm) {
		this.delivery_dept_nm = delivery_dept_nm;
	}
	public String getDelivery_tantou_nm() {
		return delivery_tantou_nm;
	}
	public void setDelivery_tantou_nm(String delivery_tantou_nm) {
		this.delivery_tantou_nm = delivery_tantou_nm;
	}
	public String getDelivery_etc1() {
		return delivery_etc1;
	}
	public void setDelivery_etc1(String delivery_etc1) {
		this.delivery_etc1 = delivery_etc1;
	}
	public String getDelivery_etc2() {
		return delivery_etc2;
	}
	public void setDelivery_etc2(String delivery_etc2) {
		this.delivery_etc2 = delivery_etc2;
	}
	public String getDelivery_etc3() {
		return delivery_etc3;
	}
	public void setDelivery_etc3(String delivery_etc3) {
		this.delivery_etc3 = delivery_etc3;
	}
	public String getDelivery_etc4() {
		return delivery_etc4;
	}
	public void setDelivery_etc4(String delivery_etc4) {
		this.delivery_etc4 = delivery_etc4;
	}
	public String getClient_phn_no() {
		return client_phn_no;
	}
	public void setClient_phn_no(String client_phn_no) {
		this.client_phn_no = client_phn_no;
	}
	public String getClient_zip_cd() {
		return client_zip_cd;
	}
	public void setClient_zip_cd(String client_zip_cd) {
		this.client_zip_cd = client_zip_cd;
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
	public String getClient_add3() {
		return client_add3;
	}
	public void setClient_add3(String client_add3) {
		this.client_add3 = client_add3;
	}
	public String getClient_add4() {
		return client_add4;
	}
	public void setClient_add4(String client_add4) {
		this.client_add4 = client_add4;
	}
	public String getClient_nm1() {
		return client_nm1;
	}
	public void setClient_nm1(String client_nm1) {
		this.client_nm1 = client_nm1;
	}
	public String getClient_nm2() {
		return client_nm2;
	}
	public void setClient_nm2(String client_nm2) {
		this.client_nm2 = client_nm2;
	}
	public String getClient_dept_nm() {
		return client_dept_nm;
	}
	public void setClient_dept_nm(String client_dept_nm) {
		this.client_dept_nm = client_dept_nm;
	}
	public String getClient_tantou_nm() {
		return client_tantou_nm;
	}
	public void setClient_tantou_nm(String client_tantou_nm) {
		this.client_tantou_nm = client_tantou_nm;
	}
	public String getClient_etc1() {
		return client_etc1;
	}
	public void setClient_etc1(String client_etc1) {
		this.client_etc1 = client_etc1;
	}
	public String getClient_etc2() {
		return client_etc2;
	}
	public void setClient_etc2(String client_etc2) {
		this.client_etc2 = client_etc2;
	}
	public String getClient_etc3() {
		return client_etc3;
	}
	public void setClient_etc3(String client_etc3) {
		this.client_etc3 = client_etc3;
	}
	public String getClient_etc4() {
		return client_etc4;
	}
	public void setClient_etc4(String client_etc4) {
		this.client_etc4 = client_etc4;
	}
	public String getShip_type() {
		return ship_type;
	}
	public void setShip_type(String ship_type) {
		this.ship_type = ship_type;
	}
	public String getEmpty_item() {
		return empty_item;
	}
	public void setEmpty_item(String empty_item) {
		this.empty_item = empty_item;
	}
	public String getEst_delivery_mon() {
		return est_delivery_mon;
	}
	public void setEst_delivery_mon(String est_delivery_mon) {
		this.est_delivery_mon = est_delivery_mon;
	}
	public String getEst_delivery_dt() {
		return est_delivery_dt;
	}
	public void setEst_delivery_dt(String est_delivery_dt) {
		this.est_delivery_dt = est_delivery_dt;
	}
}
