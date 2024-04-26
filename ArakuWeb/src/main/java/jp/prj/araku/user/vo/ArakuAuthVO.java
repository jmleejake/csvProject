package jp.prj.araku.user.vo;

import lombok.Data;

@Data
public class ArakuAuthVO {
	/**ユーザーID*/
	private String user_id;
	/**メニューID*/
	private String menu_id;
	/**メニュー名*/
	private String menu_name;
}
