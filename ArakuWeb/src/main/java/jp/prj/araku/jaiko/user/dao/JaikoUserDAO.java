package jp.prj.araku.jaiko.user.dao;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.user.mapper.IJaikoUserMapper;
import jp.prj.araku.jaiko.user.vo.JaikoUserVO;

@Repository
public class JaikoUserDAO {
	@Autowired
	SqlSession sqlSession;
	
	public int processLogin(HttpSession session, String user_id, String user_pass) {
		IJaikoUserMapper mapper = sqlSession.getMapper(IJaikoUserMapper.class);
		JaikoUserVO vo = new JaikoUserVO();
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
		if(null != mapper.getJaikoUser(vo)) {
			vo.setUser_pass(user_pass);
			if(null != mapper.getJaikoUser(vo)) {
				session.setAttribute("login", 3);
				mapper.updateJaikoUser(vo);
				return 3;
			}else {
				return 2;
			}
		}else {
			return 1;
		}
	}
}
