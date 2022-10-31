package jp.prj.araku.jaiko.partner.vo;

import lombok.Data;

public @Data class JaikoPartnerVO {
	/**区分ID*/
	private String seq_id;
	/**取引先No.*/
	private String partner_id;
	/**取引先名*/
	private String partner_nm;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	private String keyword;
	
	/**郵便番号*/
	private String partner_post;
	/**住所*/
	private String partner_add;
	/**電話番号*/
	private String partner_tel;
	/**FAX番号*/
	private String partner_fax;
	/**担当者名*/
	private String tantou_nm;
	
}
