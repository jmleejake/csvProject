package jp.prj.araku.list.vo;

import java.util.ArrayList;

import lombok.Data;

public @Data class TranslationResultVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**R:楽天・A:アマゾン*/
	private String trans_target_type;
	/**置換結果テキスト*/
	private String result_text;
	/**エラーテキスト*/
	private String err_text;
	
	/**受注番号*/
	private String order_no;
	/**送付先名字*/
	private String delivery_surname;
	/**送付先名前*/
	private String delivery_name;
	/**商品名*/
	private String product_name;
	/**項目・選択肢*/
	private String product_option;
	/**個数*/
	private String unit_no;
	/**配送会社*/
	private String delivery_company;
	private ArrayList<String> deli_com_list;
	
	/**検索用*/
	private ArrayList<String> seq_id_list;
	
}
