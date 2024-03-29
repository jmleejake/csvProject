package jp.prj.araku.user.mapper;

import jp.prj.araku.user.vo.ArakuUserVO;

public interface IArakuUserDao {
	ArakuUserVO getArakuUser(ArakuUserVO vo);
	int updateArakuUser(ArakuUserVO vo);
}
