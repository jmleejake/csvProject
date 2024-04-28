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
		ArakuAuthVO searchVo = new ArakuAuthVO();
		searchVo.setUser_id(user_id);
		return mapper.selectArakuUserAuth(searchVo);
	}
	
	public List<ArakuAuthVO> getArakuAuth() {
		IArakuUserDao mapper = sqlSession.getMapper(IArakuUserDao.class);
		List<ArakuAuthVO> list = mapper.selectArakuAuth();
		
		for(ArakuAuthVO auth : list) {
			String[] arr = auth.getMenu_id().split(",");
			auth.setRak("N");
			auth.setAma("N");
			auth.setQ10("N");
			auth.setYah("N");
			auth.setTab("N");
			auth.setCan("N");
			auth.setTan("N");
			auth.setAdm("N");
			if(arr.length > 0) {
				for(String id : arr) {
					switch (id) {
					case "rak":
						auth.setRak("Y");
						break;
					case "ama":
						auth.setAma("Y");
						break;
					case "q10":
						auth.setQ10("Y");
						break;
					case "yah":
						auth.setYah("Y");
						break;
					case "tab":
						auth.setTab("Y");
						break;
					case "can":
						auth.setCan("Y");
						break;
					case "tan":
						auth.setTan("Y");
						break;
					case "adm":
						auth.setAdm("Y");
						break;
					}
				}
			}
		}
		return list;
	}
	
	public String modifyUserAuth(ArakuAuthVO auth) {
		IArakuUserDao mapper = sqlSession.getMapper(IArakuUserDao.class);
		List<ArakuAuthVO> list = mapper.selectArakuUserAuth(auth);
		if(list.size() > 0) {
			mapper.deleteUserAuth(auth);
		}else {
			mapper.insertUserAuth(auth);
		}
		return "SUCC";
	}
}
