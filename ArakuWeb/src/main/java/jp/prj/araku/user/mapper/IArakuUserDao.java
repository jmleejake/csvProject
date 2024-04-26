package jp.prj.araku.user.mapper;

import jp.prj.araku.user.vo.ArakuUserVO;

import java.util.List;

import jp.prj.araku.user.vo.ArakuAuthVO;

public interface IArakuUserDao {
	ArakuUserVO getArakuUser(ArakuUserVO vo);
	int updateArakuUser(ArakuUserVO vo);
	List<ArakuAuthVO> selectArakuAuth(String user_id);
}
