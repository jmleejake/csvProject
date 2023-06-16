package jp.prj.araku.tanpin.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;

@Data
public class TanpinVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**商品メーカー*/
	@CsvBindByPosition(position=0)
	private String maker_cd;
	/**商品メーカー名*/
	@CsvBindByPosition(position=1)
	private String maker_nm;
	/**商品コード*/
	@CsvBindByPosition(position=2)
	private String prd_cd;
	/**商品名*/
	@CsvBindByPosition(position=3)
	private String prd_nm;
	/**容量*/
	@CsvBindByPosition(position=4)
	private String capacity;
	/**取引先コード*/
	@CsvBindByPosition(position=5)
	private String dealer_id;
	/**取引先会社名*/
	@CsvBindByPosition(position=6)
	private String dealer_nm;
	/**仕入金額*/
	@CsvBindByPosition(position=7)
	private String inprice;
	/**販売金額*/
	@CsvBindByPosition(position=8)
	private String price;
	/**商品販売基準金額*/
	@CsvBindByPosition(position=9)
	private String std_price;
	/**商品税(抜、込)*/
	@CsvBindByPosition(position=10)
	private String tax_inc;
	/**商品税率*/
	@CsvBindByPosition(position=11)
	private String tax_rt;
	/**メモ*/
	@CsvBindByPosition(position=12)
	private String memo;
	/** 入庫数*/
	private int prd_qty;
	/** 賞味期限*/
	private String exp_dt;
	
	private String start_date;
	private String end_date;
	
	private String select_type;
	private String search_type;
	private ArrayList<String> seq_id_list;
	

}
