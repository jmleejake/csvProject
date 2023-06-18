package jp.prj.araku.jaiko.inventory.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(callSuper = false)
public class TodayOrderVo extends ArakuVO {
	/**取引先会社名*/
	@CsvBindByPosition(position=0)
	private String dealer_nm;
	/**商品名*/
	@CsvBindByPosition(position=1)
	private String prd_nm;
	/**現在商品数*/
	@CsvBindByPosition(position=2)
	private String now_prd_cnt;
	/**ロート数*/
	@CsvBindByPosition(position=3)
	private String prd_lot;
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=4)
	private String jan_cd;
}
