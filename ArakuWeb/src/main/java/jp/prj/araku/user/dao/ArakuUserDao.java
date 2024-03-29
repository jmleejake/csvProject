package jp.prj.araku.user.dao;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.user.mapper.IJaikoUserMapper;
import jp.prj.araku.jaiko.user.vo.JaikoUserVO;
import jp.prj.araku.user.mapper.IArakuUserDao;
import jp.prj.araku.user.vo.ArakuUserVO;

@Repository
public class ArakuUserDao {
	@Autowired
	SqlSession sqlSession;
	
	public int processLogin(HttpSession session, String user_id, String user_pass) {
		IArakuUserDao mapper = sqlSession.getMapper(IArakuUserDao.class);
		ArakuUserVO vo = new ArakuUserVO();
		vo.setUser_id(user_id);
		if ( session.getAttribute("login") != null ){
            // 기존에 login이란 세션 값이 존재한다면
            session.removeAttribute("login"); // 기존값을 제거해 준다.
        }
		/*
		 * 1: no ID
		 * 2: id / password not match
		 * 3: ok
		 * */
		if(null != mapper.getArakuUser(vo)) {
			vo.setUser_pass(user_pass);
			if(null != mapper.getArakuUser(vo)) {
				session.setAttribute("login", 3);
				return 3;
			}else {
				return 2;
			}
		}else {
			return 1;
		}
	}
}
