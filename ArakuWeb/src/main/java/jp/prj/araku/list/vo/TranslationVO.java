package jp.prj.araku.list.vo;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;

public @Data class TranslationVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**その他*/
	private String etc_cntnt;
	
	/**ＪＡＮコード*/
	@CsvBindByPosition(position=0)
	private String jan_cd;
	/**商品名・項目・選択肢 置換前*/
	@CsvBindByPosition(position=1)
	private String before_trans;
	/**商品名・項目・選択肢 置換後*/
	@CsvBindByPosition(position=2)
	private String after_trans;
	/**商品数*/
	@CsvBindByPosition(position=3)
	private String prd_cnt;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String keyword;
	/**検索用*/
	private String search_type;
	
	private ArrayList<String> seq_id_list;
	
}
