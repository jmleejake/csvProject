package jp.prj.araku.list.vo;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

public @Data class OrderSumVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	
	/**取引先名*/
	@CsvBindByPosition(position=0)
	private String partner_nm;
	
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=1)
	private String jan_cd;
	
	/**商品名*/
	@CsvBindByPosition(position=2)
	private String prd_name;
	
	/**箱数*/
	@CsvBindByPosition(position=3)
	private int pkg_cnt;
	
	/**バラ数*/
	@CsvBindByPosition(position=4)
	private int bara_cnt;
	
	/**総商品数*/
	@CsvBindByPosition(position=5)
	private int prd_sum;
	
	/**商品名・項目・選択肢 置換後*/
	private String after_trans;
	
	/**商品マスタ反映有無フラグ：0（未）、1（済）*/
	private String prd_master_hanei_gbn;
	/**メニュー区分*/
	private String target_type;
	
}
