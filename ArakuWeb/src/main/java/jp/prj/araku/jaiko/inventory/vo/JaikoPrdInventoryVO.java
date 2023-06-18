package jp.prj.araku.jaiko.inventory.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(callSuper = false)
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

}
