package jp.prj.araku.list.vo;

import lombok.Data;
import lombok.Setter;
import lombok.Getter;

public @Data class KeywordSearchInfo {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**キーワード１(メーカー名)*/
	private String kwrd1;
	/**キーワード2(キーワード名)*/
	private String kwrd2;
}
