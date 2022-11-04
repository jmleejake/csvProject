package jp.prj.araku.jaiko.warehouse.vo;

import lombok.Data;

public @Data class JaikoWareHouseVO {
	/**区分ID*/
	private String seq_id;
	/**取引先*/
	private String partner_id;
	/**取引先名*/
	private String partner_nm;
	/**商品コード*/
	private String prd_cd;
	/**ブランド*/
	private String brand_nm;
	/**商品名*/
	private String prd_nm;
	/**入庫日*/
	private String warehouse_dt;
	/**出庫日*/
	private String delivery_dt;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品単価*/
	private String prd_unit_prc;
	/**商品入数*/
	private String prd_cnt;
	/**商品数量*/
	private String prd_quantity;
	/**商品合計*/
	private String prd_total;
	/**ＳＫＵ*/
	private String dsku;
	/**ＡＳＩＮ*/
	private String dasin;
	/**ライン数*/
	private String line_unit_no;
	/**注文数*/
	private String order_unit_no;
	/**商品税(抜、込)*/
	private String tax_incld;
	/**商品税率*/
	private String tax_rt;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	/**入数*/
	private String prd_qty;
	/**ケース数*/
	private String prd_case;
	/**バラ数*/
	private String prd_bara;
	/**賞味期限*/
	private String exp_dt;
	/**本体売価*/
	private String sell_prc;
	/**現在商品数*/
	private String now_prd_cnt;
	
	private String search_type;
	private String from_dt;
	private String to_dt;
	
	/**担当者ID*/
	private String tantou_id;
	/**担当者名*/
	private String tantou_nm;
	/**単位*/
	private String prd_unit;
	/**場所*/
	private String ware_loc;
	
}
