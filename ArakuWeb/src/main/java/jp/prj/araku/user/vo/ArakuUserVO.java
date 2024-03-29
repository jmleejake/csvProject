package jp.prj.araku.user.vo;

import lombok.Data;

@Data
public class ArakuUserVO {
	/**区分ID*/
	private String seq_id;
	/**ユーザーID*/
	private String user_id;
	/**パスワード*/
	private String user_pass;
	/**登録者*/
	private String reg_user_id;
	/**登録日付*/
	private String reg_dt;
	/**更新者*/
	private String upd_user_id;
	/**更新日付*/
	private String upd_dt;
	/**最近ローグイン日付*/
	private String last_login_dt;
	
}
