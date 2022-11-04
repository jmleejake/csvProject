package jp.prj.araku.jaiko.warehouse.vo;

import lombok.Data;

public @Data class JaikoWareTempVO {
	/**区分ID*/
	private String seq_id;
	/**取引先*/
	private String partner_id;
	/**取引先名*/
	private String partner_nm;
	/**担当者ID*/
	private String tantou_id;
	/**担当者名*/
	private String tantou_nm;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品コード*/
	private String prd_cd;
	/**商品名*/
	private String prd_nm;
	/**商品数量*/
	private String prd_quantity;
	/**単位*/
	private String prd_unit;
	/**場所*/
	private String ware_loc;
}
