package jp.prj.araku.user.vo;

import lombok.Data;

@Data
public class ArakuAuthVO {
	/**ユーザーID*/
	private String user_id;
	/**ユーザー名*/
	private String user_name;
	/**メニューID*/
	private String menu_id;
	/**メニュー名*/
	private String menu_name;
	/**最近ローグイン日付*/
	private String last_login_dt;
	
	private String rak;
	private String ama;
	private String q10;
	private String yah;
	private String tab;
	private String can;
	private String tan;
	private String adm;
}
