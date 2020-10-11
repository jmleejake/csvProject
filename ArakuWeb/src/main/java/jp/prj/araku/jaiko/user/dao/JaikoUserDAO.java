package jp.prj.araku.jaiko.user.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.user.mapper.IJaikoUserMapper;
import jp.prj.araku.jaiko.user.vo.JaikoUserVO;

@Repository
public class JaikoUserDAO {
	@Autowired
	SqlSession sqlSession;
	
	public int processLogin(String user_id, String user_pass) {
		IJaikoUserMapper mapper = sqlSession.getMapper(IJaikoUserMapper.class);
		JaikoUserVO vo = new JaikoUserVO();
		vo.setUser_id(user_id);
		/*
		 * 1: no ID
		 * 2: id / password not match
		 * 3: ok
		 * */
		if(null != mapper.getJaikoUser(vo)) {
			vo.setUser_pass(user_pass);
			if(null != mapper.getJaikoUser(vo)) {
				return 3;
			}else {
				return 2;
			}
		}else {
			return 1;
		}
	}
}
