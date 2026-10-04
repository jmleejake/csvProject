package com.km.adm.session;

import com.km.adm.dao.vo.UserVo;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

/**
 * 로그인 유저 세션 객체
 *
 * @author jack scott
 */
@Data
public class UserSession implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4357221827858948122L;
	/**
	 * UserVo
	 */
	private UserVo user;

	/**
	 * 로그인 유저 권한 링크
	 */
	private Set<String> authLink;
}
