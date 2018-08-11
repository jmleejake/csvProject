package jp.prj.araku.list.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.TranslationVO;

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
	
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo) {
		log.info("getTransInfo");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getTransInfo(vo);
	}
	
	public int addTransInfo(TranslationVO vo) {
		log.info("addTransInfo");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.addTransInfo(vo);
	}
	
	public int modTransInfo(TranslationVO vo) {
		log.info("modTransInfo");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modTransInfo(vo);
	}
	
	public int delTransInfo(String seq_id) {
		log.info("delTransInfo");
		log.debug("del trans seq_id : {}", seq_id);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.delTransInfo(seq_id);
	}
	
	public int modRakutenInfo(RakutenSearchVO vo) {
		log.info("modRakuten");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modRakutenInfo(vo);
	}
}
