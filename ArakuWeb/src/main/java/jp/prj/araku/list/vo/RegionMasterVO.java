package jp.prj.araku.list.vo;

import java.util.ArrayList;

import lombok.Data;

public @Data class RegionMasterVO {
	/**区分ID*/
	private String seq_id;
	/**地域ID*/
	private String p_id;
	/**データ登録日*/
	private String register_date;
	/**地域名称*/
	private String parent_region_name;
	/**名称*/
	private String region_name;
	/**名称(英語)*/
	private String region_name_en;
	/**配送会社*/
	private String delivery_company;
	/**倉庫区分*/
	private String house_type;
	
	private ArrayList<String> seq_id_list;
	private String keyword;
	
}
