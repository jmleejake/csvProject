package jp.prj.araku.list.vo;

import com.opencsv.bean.CsvBindByPosition;

/**
200625
이번주 급하게 아락시스템에 추가 해야 할게 있습니다. 
내용은 사가와 송장번호를 아락시스템에 주문번호에 있는 전표번호 란에 갱신치는 일입니다.

Amazon更新ファイル画面のお問い合わせ伝票番号
や楽天更新ファイル画面のお荷物伝票番号に更新すること。
 * */
public class SagawaUpdateVO {
	/**出荷予定日*/
	@CsvBindByPosition(position=0)
	private String ship_dt;
	/**集荷日*/
	@CsvBindByPosition(position=1)
	private String pickup_dt;
	/**顧客*/
	@CsvBindByPosition(position=2)
	private String cust_nm;
	/**HAWB#*/
	@CsvBindByPosition(position=3)
	private String hawb_no;
	/**貨物種類*/
	@CsvBindByPosition(position=4)
	private String cargo_typ;
	/**リファレンス＃*/
	@CsvBindByPosition(position=5)
	private String ref_no;
	/**荷主コード*/
	@CsvBindByPosition(position=6)
	private String ship_cd;
	/**荷主名称1*/
	@CsvBindByPosition(position=7)
	private String ship_nm1;
	/**荷主名称2*/
	@CsvBindByPosition(position=8)
	private String ship_nm2;
	/**荷主郵便番号*/
	@CsvBindByPosition(position=9)
	private String ship_post_no;
	/**荷主住所1*/
	@CsvBindByPosition(position=10)
	private String ship_add1;
	/**荷主住所2*/
	@CsvBindByPosition(position=11)
	private String ship_add2;
	/**荷主住所3*/
	@CsvBindByPosition(position=12)
	private String ship_add3;
	/**荷主電話番号*/
	@CsvBindByPosition(position=13)
	private String ship_tel;
	/**荷受人コード*/
	@CsvBindByPosition(position=14)
	private String consign_cd;
	/**荷受人名称1*/
	@CsvBindByPosition(position=15)
	private String consign_nm1;
	/**荷受人名称2*/
	@CsvBindByPosition(position=16)
	private String consign_nm2;
	/**荷受人郵便番号*/
	@CsvBindByPosition(position=17)
	private String consign_post_no;
	/**荷受人住所1*/
	@CsvBindByPosition(position=18)
	private String consign_add1;
	/**荷受人住所2*/
	@CsvBindByPosition(position=19)
	private String consign_add2;
	/**荷受人住所3*/
	@CsvBindByPosition(position=20)
	private String consign_add3;
	/**荷受人住所4*/
	@CsvBindByPosition(position=21)
	private String consign_add4;
	/**荷受人電話番号*/
	@CsvBindByPosition(position=22)
	private String consign_tel;
	/**荷受人カナ1*/
	@CsvBindByPosition(position=23)
	private String consign_nm_kana1;
	/**荷受人カナ2*/
	@CsvBindByPosition(position=24)
	private String consign_nm_kana2;
	/**メールアドレス*/
	@CsvBindByPosition(position=25)
	private String email_add;
	/**着払SGX番号*/
	@CsvBindByPosition(position=26)
	private String sgx_no;
	/**インボイス作成*/
	@CsvBindByPosition(position=27)
	private String inv_cre;
	/**佐川問合せ番号*/
	@CsvBindByPosition(position=28)
	private String sagawa_no;
	/**商品合計*/
	@CsvBindByPosition(position=29)
	private String total_cost;
	/**送料*/
	@CsvBindByPosition(position=30)
	private String ship_cost;
	/**代引手数料*/
	@CsvBindByPosition(position=31)
	private String cod_amt;
	/**合計金額*/
	@CsvBindByPosition(position=32)
	private String total_amt;
	/**税金支払*/
	@CsvBindByPosition(position=33)
	private String tax_amt;
	/**運賃支払*/
	@CsvBindByPosition(position=34)
	private String frt_pay;
	/**個口数*/
	@CsvBindByPosition(position=35)
	private String unit_no1;
	/**重量G/W*/
	@CsvBindByPosition(position=36)
	private String wgt_gw;
	/**重量V/W*/
	@CsvBindByPosition(position=37)
	private String wgt_vw;
	/**通貨*/
	@CsvBindByPosition(position=38)
	private String currency1;
	/**発地*/
	@CsvBindByPosition(position=39)
	private String depart_add;
	/**着地*/
	@CsvBindByPosition(position=40)
	private String land_add;
	/**保険*/
	@CsvBindByPosition(position=41)
	private String insu;
	/**保険金額*/
	@CsvBindByPosition(position=42)
	private String insu_amt;
	/**C.O.D*/
	@CsvBindByPosition(position=43)
	private String cod;
	/**運送会社*/
	@CsvBindByPosition(position=44)
	private String delivery_company;
	/**問い合せ番号*/
	@CsvBindByPosition(position=45)
	private String inq_no;
	/**配達指定日*/
	@CsvBindByPosition(position=46)
	private String delivery_dt;
	/**時間帯指定*/
	@CsvBindByPosition(position=47)
	private String time_sel;
	/**便種*/
	@CsvBindByPosition(position=48)
	private String type;
	/**国コード*/
	@CsvBindByPosition(position=49)
	private String country_cd;
	/**着地都市名*/
	@CsvBindByPosition(position=50)
	private String land_city_nm;
	/**着地州名*/
	@CsvBindByPosition(position=51)
	private String land_state;
	/**備考*/
	@CsvBindByPosition(position=52)
	private String etc;
	/**Bag No.*/
	@CsvBindByPosition(position=53)
	private String bag_no;
	/**出荷指示番号*/
	@CsvBindByPosition(position=54)
	private String ship_order_no;
	/**登録管理番号*/
	@CsvBindByPosition(position=55)
	private String regist_mng_no;
	/**行No*/
	@CsvBindByPosition(position=56)
	private String row_no;
	/**商品コード*/
	@CsvBindByPosition(position=57)
	private String prd_cd;
	/**商品名称*/
	@CsvBindByPosition(position=58)
	private String prd_nm;
	/**HSCODE*/
	@CsvBindByPosition(position=59)
	private String hscd;
	/**原産国*/
	@CsvBindByPosition(position=60)
	private String ori_country;
	/**単価*/
	@CsvBindByPosition(position=61)
	private String unit_price;
	/**個数*/
	@CsvBindByPosition(position=62)
	private String unit_no2;
	/**金額*/
	@CsvBindByPosition(position=63)
	private String money_amt;
	/**重量（KG）*/
	@CsvBindByPosition(position=64)
	private String weight;
	/**通貨*/
	@CsvBindByPosition(position=65)
	private String currency2;
	/**登録日時*/
	@CsvBindByPosition(position=66)
	private String reg_dt;
	
	public String getShip_dt() {
		return ship_dt;
	}
	public void setShip_dt(String ship_dt) {
		this.ship_dt = ship_dt;
	}
	public String getPickup_dt() {
		return pickup_dt;
	}
	public void setPickup_dt(String pickup_dt) {
		this.pickup_dt = pickup_dt;
	}
	public String getCust_nm() {
		return cust_nm;
	}
	public void setCust_nm(String cust_nm) {
		this.cust_nm = cust_nm;
	}
	public String getHawb_no() {
		return hawb_no;
	}
	public void setHawb_no(String hawb_no) {
		this.hawb_no = hawb_no;
	}
	public String getCargo_typ() {
		return cargo_typ;
	}
	public void setCargo_typ(String cargo_typ) {
		this.cargo_typ = cargo_typ;
	}
	public String getRef_no() {
		return ref_no;
	}
	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}
	public String getShip_cd() {
		return ship_cd;
	}
	public void setShip_cd(String ship_cd) {
		this.ship_cd = ship_cd;
	}
	public String getShip_nm1() {
		return ship_nm1;
	}
	public void setShip_nm1(String ship_nm1) {
		this.ship_nm1 = ship_nm1;
	}
	public String getShip_nm2() {
		return ship_nm2;
	}
	public void setShip_nm2(String ship_nm2) {
		this.ship_nm2 = ship_nm2;
	}
	public String getShip_post_no() {
		return ship_post_no;
	}
	public void setShip_post_no(String ship_post_no) {
		this.ship_post_no = ship_post_no;
	}
	public String getShip_add1() {
		return ship_add1;
	}
	public void setShip_add1(String ship_add1) {
		this.ship_add1 = ship_add1;
	}
	public String getShip_add2() {
		return ship_add2;
	}
	public void setShip_add2(String ship_add2) {
		this.ship_add2 = ship_add2;
	}
	public String getShip_add3() {
		return ship_add3;
	}
	public void setShip_add3(String ship_add3) {
		this.ship_add3 = ship_add3;
	}
	public String getShip_tel() {
		return ship_tel;
	}
	public void setShip_tel(String ship_tel) {
		this.ship_tel = ship_tel;
	}
	public String getConsign_cd() {
		return consign_cd;
	}
	public void setConsign_cd(String consign_cd) {
		this.consign_cd = consign_cd;
	}
	public String getConsign_nm1() {
		return consign_nm1;
	}
	public void setConsign_nm1(String consign_nm1) {
		this.consign_nm1 = consign_nm1;
	}
	public String getConsign_nm2() {
		return consign_nm2;
	}
	public void setConsign_nm2(String consign_nm2) {
		this.consign_nm2 = consign_nm2;
	}
	public String getConsign_post_no() {
		return consign_post_no;
	}
	public void setConsign_post_no(String consign_post_no) {
		this.consign_post_no = consign_post_no;
	}
	public String getConsign_add1() {
		return consign_add1;
	}
	public void setConsign_add1(String consign_add1) {
		this.consign_add1 = consign_add1;
	}
	public String getConsign_add2() {
		return consign_add2;
	}
	public void setConsign_add2(String consign_add2) {
		this.consign_add2 = consign_add2;
	}
	public String getConsign_add3() {
		return consign_add3;
	}
	public void setConsign_add3(String consign_add3) {
		this.consign_add3 = consign_add3;
	}
	public String getConsign_add4() {
		return consign_add4;
	}
	public void setConsign_add4(String consign_add4) {
		this.consign_add4 = consign_add4;
	}
	public String getConsign_tel() {
		return consign_tel;
	}
	public void setConsign_tel(String consign_tel) {
		this.consign_tel = consign_tel;
	}
	public String getConsign_nm_kana1() {
		return consign_nm_kana1;
	}
	public void setConsign_nm_kana1(String consign_nm_kana1) {
		this.consign_nm_kana1 = consign_nm_kana1;
	}
	public String getConsign_nm_kana2() {
		return consign_nm_kana2;
	}
	public void setConsign_nm_kana2(String consign_nm_kana2) {
		this.consign_nm_kana2 = consign_nm_kana2;
	}
	public String getEmail_add() {
		return email_add;
	}
	public void setEmail_add(String email_add) {
		this.email_add = email_add;
	}
	public String getSgx_no() {
		return sgx_no;
	}
	public void setSgx_no(String sgx_no) {
		this.sgx_no = sgx_no;
	}
	public String getInv_cre() {
		return inv_cre;
	}
	public void setInv_cre(String inv_cre) {
		this.inv_cre = inv_cre;
	}
	public String getSagawa_no() {
		return sagawa_no;
	}
	public void setSagawa_no(String sagawa_no) {
		this.sagawa_no = sagawa_no;
	}
	public String getTotal_cost() {
		return total_cost;
	}
	public void setTotal_cost(String total_cost) {
		this.total_cost = total_cost;
	}
	public String getShip_cost() {
		return ship_cost;
	}
	public void setShip_cost(String ship_cost) {
		this.ship_cost = ship_cost;
	}
	public String getCod_amt() {
		return cod_amt;
	}
	public void setCod_amt(String cod_amt) {
		this.cod_amt = cod_amt;
	}
	public String getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}
	public String getTax_amt() {
		return tax_amt;
	}
	public void setTax_amt(String tax_amt) {
		this.tax_amt = tax_amt;
	}
	public String getFrt_pay() {
		return frt_pay;
	}
	public void setFrt_pay(String frt_pay) {
		this.frt_pay = frt_pay;
	}
	public String getUnit_no1() {
		return unit_no1;
	}
	public void setUnit_no1(String unit_no1) {
		this.unit_no1 = unit_no1;
	}
	public String getWgt_gw() {
		return wgt_gw;
	}
	public void setWgt_gw(String wgt_gw) {
		this.wgt_gw = wgt_gw;
	}
	public String getWgt_vw() {
		return wgt_vw;
	}
	public void setWgt_vw(String wgt_vw) {
		this.wgt_vw = wgt_vw;
	}
	public String getCurrency1() {
		return currency1;
	}
	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}
	public String getDepart_add() {
		return depart_add;
	}
	public void setDepart_add(String depart_add) {
		this.depart_add = depart_add;
	}
	public String getLand_add() {
		return land_add;
	}
	public void setLand_add(String land_add) {
		this.land_add = land_add;
	}
	public String getInsu() {
		return insu;
	}
	public void setInsu(String insu) {
		this.insu = insu;
	}
	public String getInsu_amt() {
		return insu_amt;
	}
	public void setInsu_amt(String insu_amt) {
		this.insu_amt = insu_amt;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
	public String getInq_no() {
		return inq_no;
	}
	public void setInq_no(String inq_no) {
		this.inq_no = inq_no;
	}
	public String getDelivery_dt() {
		return delivery_dt;
	}
	public void setDelivery_dt(String delivery_dt) {
		this.delivery_dt = delivery_dt;
	}
	public String getTime_sel() {
		return time_sel;
	}
	public void setTime_sel(String time_sel) {
		this.time_sel = time_sel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCountry_cd() {
		return country_cd;
	}
	public void setCountry_cd(String country_cd) {
		this.country_cd = country_cd;
	}
	public String getLand_city_nm() {
		return land_city_nm;
	}
	public void setLand_city_nm(String land_city_nm) {
		this.land_city_nm = land_city_nm;
	}
	public String getLand_state() {
		return land_state;
	}
	public void setLand_state(String land_state) {
		this.land_state = land_state;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getBag_no() {
		return bag_no;
	}
	public void setBag_no(String bag_no) {
		this.bag_no = bag_no;
	}
	public String getShip_order_no() {
		return ship_order_no;
	}
	public void setShip_order_no(String ship_order_no) {
		this.ship_order_no = ship_order_no;
	}
	public String getRegist_mng_no() {
		return regist_mng_no;
	}
	public void setRegist_mng_no(String regist_mng_no) {
		this.regist_mng_no = regist_mng_no;
	}
	public String getRow_no() {
		return row_no;
	}
	public void setRow_no(String row_no) {
		this.row_no = row_no;
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
	public String getHscd() {
		return hscd;
	}
	public void setHscd(String hscd) {
		this.hscd = hscd;
	}
	public String getOri_country() {
		return ori_country;
	}
	public void setOri_country(String ori_country) {
		this.ori_country = ori_country;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getUnit_no2() {
		return unit_no2;
	}
	public void setUnit_no2(String unit_no2) {
		this.unit_no2 = unit_no2;
	}
	public String getMoney_amt() {
		return money_amt;
	}
	public void setMoney_amt(String money_amt) {
		this.money_amt = money_amt;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCurrency2() {
		return currency2;
	}
	public void setCurrency2(String currency2) {
		this.currency2 = currency2;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

}
