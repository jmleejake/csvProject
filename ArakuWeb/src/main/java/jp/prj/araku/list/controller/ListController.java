package jp.prj.araku.list.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.CommonUtil;

@Controller
public class ListController {
	private static final Logger log = LoggerFactory.getLogger(ListController.class);
	
	@Autowired
	ListDAO dao;
	
	@ResponseBody
	@RequestMapping(value="/showRList")
	public ArrayList<RakutenSearchVO> getRakutenList(RakutenSearchVO searchVO) {
		log.info("getRakutenList");
		
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!"srch".equals(searchVO.getSearch_type())) {
			searchVO.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("search item :: {}", searchVO);
		return dao.getRList(searchVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTrans")
	public ArrayList<TranslationVO> getTransInfo(TranslationVO transVO) {
		log.info("getTransInfo");
		
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!"srch".equals(transVO.getSearch_type())) {
			transVO.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", transVO);
		return dao.getTransInfo(transVO);
	}
	
	@RequestMapping(value="/modTrans")
	public String modTransInfo(TranslationVO transVO) {
		log.info("modTransInfo");
		log.debug("{}", transVO);
		
		if (transVO.getSeq_id() != null) {
			log.info("update process");
			dao.modTransInfo(transVO);
		} else {
			log.info("create process");
			dao.addTransInfo(transVO);
		}
		return "redirect:getTrans";
	}
	
	@RequestMapping(value="/delTrans")
	public String delTransInfo(String seq_id) {
		log.info("modTransInfo");
		log.debug("del seq_id : {}", seq_id);
		dao.delTransInfo(seq_id);
		return "redirect:getTrans";
	}
	
	@RequestMapping(value="/modRakuten")
	public String modRakutenInfo(RakutenSearchVO vo) {
		log.info("modRakutenInfo");
		log.debug("{}", vo);
		dao.modRakutenInfo(vo);
		return "redirect:showRList";
	}
	
}
