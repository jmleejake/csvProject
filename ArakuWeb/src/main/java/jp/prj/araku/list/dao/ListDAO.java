package jp.prj.araku.list.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;

@Repository
public class ListDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = LoggerFactory.getLogger(ListDAO.class);
	
	public ArrayList<RakutenSearchVO> getRList(RakutenSearchVO vo) {
		log.info("getRList");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRList(vo);
	}
}
