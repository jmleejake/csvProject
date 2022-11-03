package jp.prj.araku.jaiko.product.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;

public @Data class JaikoPrdInfoVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**取引先*/
	@CsvBindByPosition(position=0)
	private String partner_id;
	/**取引先名*/
	@CsvBindByPosition(position=1)
	private String partner_nm;
	/**商品コード*/
	@CsvBindByPosition(position=2)
	private String prd_cd;
	/**ブランド*/
	@CsvBindByPosition(position=3)
	private String brand_nm;
	/**商品名*/
	@CsvBindByPosition(position=4)
	private String prd_nm;
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=5)
	private String jan_cd;
	/**商品入数*/
	@CsvBindByPosition(position=6)
	private String prd_cnt;
	/**商品単価*/
	@CsvBindByPosition(position=7)
	private String prd_unit_prc;
	/**商品税(抜、込)*/
	@CsvBindByPosition(position=8)
	private String tax_incld;
	/**商品税率*/
	@CsvBindByPosition(position=9)
	private String tax_rt;
	/**規格*/
	@CsvBindByPosition(position=10)
	private String std_info;
	/**ＳＫＵ*/
	@CsvBindByPosition(position=11)
	private String dsku;
	/**ＡＳＩＮ*/
	@CsvBindByPosition(position=12)
	private String dasin;
	@CsvBindByPosition(position=13)
	private String jan_cd1;
	@CsvBindByPosition(position=14)
	private String prd_cnt1;
	@CsvBindByPosition(position=15)
	private String jan_cd2;
	@CsvBindByPosition(position=16)
	private String prd_cnt2;
	@CsvBindByPosition(position=17)
	private String jan_cd3;
	@CsvBindByPosition(position=18)
	private String prd_cnt3;
	
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	
	private String search_type;
	
}
