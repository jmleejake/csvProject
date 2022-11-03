package jp.prj.araku.jaiko.partner.vo;

import lombok.Data;

public @Data class JaikoWareTantou {
	/**区分ID*/
	private String seq_id;
	/**登録日付*/
	private String reg_dt;
	/**更新日付*/
	private String upd_dt;
	/**担当者名*/
	private String tantou_nm;
	/**電話番号*/
	private String tantou_tel;
	/**担当者ID*/
	private String tantou_id;
	/**パスワード*/
	private String tantou_pass;
	/**権限*/
	private String tantou_auth;
}
