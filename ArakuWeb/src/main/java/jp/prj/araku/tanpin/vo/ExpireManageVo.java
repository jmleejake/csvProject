package jp.prj.araku.tanpin.vo;

import java.util.ArrayList;

import jp.prj.araku.util.ArakuVO;
import lombok.Data;

@Data
public class ExpireManageVo extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**ＪＡＮコード*/
	private String jan_cd;
	/**商品名*/
	private String prd_nm;
	/**取引先コード*/
	private String partner_id;
	/**取引先会社名*/
	private String partner_nm;
	/** 入庫数*/
	private int prd_qty;
	/** 賞味期限*/
	private String exp_dt;
	
	private String search_type;
	private ArrayList<String> seq_id_list;
}
