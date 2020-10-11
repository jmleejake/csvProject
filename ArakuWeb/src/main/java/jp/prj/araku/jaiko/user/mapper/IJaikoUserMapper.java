package jp.prj.araku.jaiko.user.mapper;

import jp.prj.araku.jaiko.user.vo.JaikoUserVO;

public interface IJaikoUserMapper {
	JaikoUserVO getJaikoUser(JaikoUserVO vo);
	int updateJaikoUser(JaikoUserVO vo);
}
