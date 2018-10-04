package jp.prj.araku.list.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.CommonUtil;

/**
 * @comment
 * [MOD-0819] 상품명에서 추출하는 개수는 계산하는데 필요없다
 * [MOD-0826] 콜론으로만 델리미터를 처리할 예정이므로 일단 주석처리
 */
@Repository
public class ListDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo) {
		log.info("getTransInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getTransInfo(vo);
	}
	
	@Transactional
	public ArrayList<TranslationVO> registerTransInfo(ArrayList<TranslationVO> transVO) {
		log.info("registerTransInfo");
		log.debug(transVO);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		
		for (TranslationVO vo : transVO) {
			if (vo.getSeq_id() != null) {
				log.info("update process");
				mapper.modTransInfo(vo);
				seq_id_list.add(vo.getSeq_id());
			} else {
				log.info("create process");
				mapper.addTransInfo(vo);
				seq_id_list.add(vo.getSeq_id());
			}
		}
		
		TranslationVO searchVO = new TranslationVO();
		searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
		searchVO.setSeq_id_list(seq_id_list);
		
		return mapper.getTransInfo(searchVO);
	}
	
	public int delTransInfo(String seq_id) {
		log.info("delTransInfo");
		log.debug("del trans seq_id : " + seq_id);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.delTransInfo(seq_id);
	}
	
	public int modTransResult(TranslationResultVO vo) {
		log.info("updateTransResult");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modTransResult(vo);
	}
	
	public void modRakutenInfo(ArrayList<RakutenSearchVO> list) {
		log.info("modRakutenInfo");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for (RakutenSearchVO vo : list) {
			mapper.modRakutenInfo(vo);
		}
	}
	
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.info("showRegionMaster");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRegionMaster(vo);
	}
	
	public ArrayList<RegionMasterVO> modRegionMaster(ArrayList<RegionMasterVO> list) {
		log.info("modRegionMaster");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> idList = new ArrayList<>();
		for (RegionMasterVO rm : list) {
			log.debug("update target : " + rm);
			mapper.modRegionMaster(rm);
			idList.add(rm.getSeq_id());
		}
		
		RegionMasterVO vo = new RegionMasterVO();
		vo.setSeq_id_list(idList);
		
		return mapper.getRegionMaster(vo);
	}
}
