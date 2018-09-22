package jp.prj.araku.file.vo;

import com.opencsv.bean.CsvBindByPosition;

public class SagawaVO {
	
	/**
	*住所録コード
	*
	*/
	@CsvBindByPosition(position=0)
	private String add_book_code;
	/**
	*お届け先電話番号
	*“-”ハイフン
	*/
	@CsvBindByPosition(position=1)
	private String delivery_tel;
	/**
	*お届け先郵便番号
	*“-”ハイフン
	*/
	@CsvBindByPosition(position=2)
	private String delivery_post_no;
	/**
	*お届け先住所１（必須）
	*　※半角文字は全角文字に変換
	*/
	@CsvBindByPosition(position=3)
	private String delivery_add1;
	/**
	*お届け先住所２
	*
	*/
	@CsvBindByPosition(position=4)
	private String delivery_add2;
	/**
	*お届け先住所３
	*
	*/
	@CsvBindByPosition(position=5)
	private String delivery_add3;
	/**
	*お届け先名称１（必須）
	*　※半角文字は全角文字に変換
	*/
	@CsvBindByPosition(position=6)
	private String delivery_name1;
	/**
	*お届け先名称２
	*
	*/
	@CsvBindByPosition(position=7)
	private String delivery_name2;
	/**
	*お客様管理ナンバー
	*
	*/
	@CsvBindByPosition(position=8)
	private String client_manage_no;
	/**
	*お客様コード
	*
	*/
	@CsvBindByPosition(position=9)
	private String client_code;
	/**
	*部署・担当者
	*　※荷送人の部署・担当者
	*/
	@CsvBindByPosition(position=10)
	private String transport_dept_charge;
	/**
	*荷送人電話番号
	*“-”ハイフン
	*/
	@CsvBindByPosition(position=11)
	private String transport_tel;
	/**
	*ご依頼主電話番号
	*“-”ハイフン
	*/
	@CsvBindByPosition(position=12)
	private String client_tel;
	/**
	*ご依頼主郵便番号
	*“-”ハイフン
	*/
	@CsvBindByPosition(position=13)
	private String client_post_no;
	/**
	*ご依頼主住所１
	*　※半角文字は全角文字に変換
	*/
	@CsvBindByPosition(position=14)
	private String client_add1;
	/**
	*ご依頼主住所２
	*
	*/
	@CsvBindByPosition(position=15)
	private String client_add2;
	/**
	*ご依頼主名称１
	*　※半角文字は全角文字に変換
	*/
	@CsvBindByPosition(position=16)
	private String client_name1;
	/**
	*ご依頼主名称２
	*
	*/
	@CsvBindByPosition(position=17)
	private String client_name2;
	/**
	*荷姿コード
	*001 ：箱類　　　　　 002 ：バッグ類　　　 003 ：スーツケース
	004 ：封筒類　　　　 005 ：ゴルフバッグ　 006 ：スキー
	007 ：スノーボード　 008 ：その他
	*/
	@CsvBindByPosition(position=18)
	private String transport_code;
	/**
	*品名１
	*　※「全角英数カナ」 の場合、16桁で取込。
	        「半角英数カナ」 の場合、32桁で取込。
	*/
	@CsvBindByPosition(position=19)
	private String product_name1;
	/**
	*品名２
	*
	*/
	@CsvBindByPosition(position=20)
	private String product_name2;
	/**
	*品名３
	*
	*/
	@CsvBindByPosition(position=21)
	private String product_name3;
	/**
	*品名４
	*
	*/
	@CsvBindByPosition(position=22)
	private String product_name4;
	/**
	*品名５
	*
	*/
	@CsvBindByPosition(position=23)
	private String product_name5;
	/**
	*出荷個数
	*
	*/
	@CsvBindByPosition(position=24)
	private String delivery_cnt;
	/**
	*便種（スピードで選択）
	*000 ：飛脚宅配便　　　　　　　001 ：飛脚スーパー便
	002 ：飛脚即日配達便　　　　　003 ：飛脚航空便（翌日中配達）
	004 ：飛脚航空便（翌日午前中配達）
	005 ：飛脚ジャストタイム便
	*/
	@CsvBindByPosition(position=25)
	private String speed_type;
	/**
	*便種（商品）
	*001 ：指定なし　　　　　　　　　002 ：飛脚クール便（冷蔵）
	003 ：飛脚クール便（冷凍）
	*/
	@CsvBindByPosition(position=26)
	private String product_type;
	/**
	*配達日
	*例）20110101（2011年1月1日を指定する場合）
	*/
	@CsvBindByPosition(position=27)
	private String delivery_date;
	/**
	*配達指定時間帯
	*｢５時間帯｣の場合

	01 ： 午前中                                      12 ： 12:00～14:00
	14 ： 14:00～16:00              16 ： 16:00～18:00
	04 ： 18:00～21:00

	｢６時間帯（オプション）｣の場合

	01 ： 午前中                                      12 ： 12:00～14:00
	14 ： 14:00～16:00              16 ： 16:00～18:00
	18 ： 18:00～20:00              19 ： 19:00～21:00
	*/
	@CsvBindByPosition(position=28)
	private String delivery_time;
	/**
	*配達指定時間（時分）
	*例）1530　（15時30分を指定する場合）
	※ 飛脚ジャストタイム便利用時のみ指定可能
	※ “分”の値は「00」または「30」の30分単位で指定可能
	*/
	@CsvBindByPosition(position=29)
	private String delivery_time_detail;
	/**
	*代引金額
	*
	*/
	@CsvBindByPosition(position=30)
	private String total_amt;
	/**
	*消費税
	*
	*/
	@CsvBindByPosition(position=31)
	private String consume_tax;
	/**
	*決済種別
	*0 ：指定なし　1 ：全て可　　2 ：現金のみ　　5 ：ﾃﾞﾋﾞｯﾄ･ｸﾚｼﾞｯﾄ
	*/
	@CsvBindByPosition(position=32)
	private String payment_method;
	/**
	*保険金額
	*0～49999999の範囲で指定可能
	*/
	@CsvBindByPosition(position=33)
	private String insurance_amt;
	/**
	*保険金額印字
	*0 ：印字しない　　　    1 ：印字する
	*/
	@CsvBindByPosition(position=34)
	private String insurance_status;
	/**
	*指定シール①
	*001 ：飛脚クール便（冷蔵）               002 ：飛脚クール便（冷凍）
	004 ：営業所受取サービス                   005 ：指定日配達サービス
	007 ：時間帯指定サービス(※1)   008 ：eコレクト（現金決済）
	009 ：eコレクト（デビット／クレジット決済）
	010 ：eコレクト（なんでも決済）
	011 ：取扱注意                                       012 ：貴重品
	013 ：天地無用                                       014 ：飛脚即日配達便
	016 ：時間帯指定サービス（午前       017 ：飛脚航空便
	中）(※1)                                 019 ：時間帯指定サービス(※2)
	018 ：飛脚ジャストタイム便

	（※1）「５時間帯」用のシールコードとなります。
	（※2）「６時間帯（オプション）」用のシールコードとなります。
	*/
	@CsvBindByPosition(position=35)
	private String select_seal1;
	/**
	*指定シール②
	*
	*/
	@CsvBindByPosition(position=36)
	private String select_seal2;
	/**
	*指定シール③
	*
	*/
	@CsvBindByPosition(position=37)
	private String select_seal3;
	/**
	*営業店止め
	*0 ：通常出荷　　　    　1 ：営業所受取
	*/
	@CsvBindByPosition(position=38)
	private String store_stop;
	/**
	*ＳＲＣ区分
	*0 ：指定なし　　　    　1 ：ＳＲＣ
	*/
	@CsvBindByPosition(position=39)
	private String src_type;
	/**
	*営業店コード
	*
	*/
	@CsvBindByPosition(position=40)
	private String store_code;
	/**
	*元着区分
	*1 ：元払　　　　　　　2 ：着払
	*/
	@CsvBindByPosition(position=41)
	private String origin_type;
	
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
	public String getTransport_dept_charge() {
		return transport_dept_charge;
	}
	public void setTransport_dept_charge(String transport_dept_charge) {
		this.transport_dept_charge = transport_dept_charge;
	}
	public String getTransport_tel() {
		return transport_tel;
	}
	public void setTransport_tel(String transport_tel) {
		this.transport_tel = transport_tel;
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
	
}
