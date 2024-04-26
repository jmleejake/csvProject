package jp.prj.araku.user.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.user.mapper.IArakuUserDao;
import jp.prj.araku.user.vo.ArakuUserVO;
import jp.prj.araku.user.vo.ArakuAuthVO;

@Repository
public class ArakuUserDao {
	@Autowired
	SqlSession sqlSession;
	
	public int processLogin(HttpSession session, String user_id, String user_pass) {
		IArakuUserDao mapper = sqlSession.getMapper(IArakuUserDao.class);
		ArakuUserVO vo = new ArakuUserVO();
		vo.setUser_id(user_id);

		/*
		 * 1: no ID
		 * 2: id / password not match
		 * 3: ok
		 * */
		if(null != mapper.getArakuUser(vo)) {
			vo.setUser_pass(user_pass);
			if(null != mapper.getArakuUser(vo)) {
				session.setAttribute("arakuLogin", 3);
				session.setAttribute("arakuId", user_id);
				mapper.updateArakuUser(vo);
				return 3;
			}else {
				return 2;
			}
		}else {
			return 1;
		}
	}
	
	public List<ArakuAuthVO> getUserAuth(String user_id) {
		IArakuUserDao mapper = sqlSession.getMapper(IArakuUserDao.class);
		return mapper.selectArakuAuth(user_id);
	}
}
