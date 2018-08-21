package jp.prj.araku.list.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;

@Controller
public class ListController {
	private static final Logger log = LoggerFactory.getLogger(ListController.class);
	
	@Autowired
	ListDAO dao;
	
	@ResponseBody
	@RequestMapping(value="/showRList")
	public ArrayList<RakutenSearchVO> getRakutenList(RakutenSearchVO searchVO) {
		log.info("getRakutenList");
		return dao.getRList(searchVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTrans")
	public ArrayList<TranslationVO> getTransInfo(TranslationVO transVO) {
		log.info("getTransInfo");
		return dao.getTransInfo(transVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/modTrans")
	public ArrayList<TranslationVO> modTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.info("modTransInfo");
		return dao.registerTransInfo(transVO);
	}
	
	@RequestMapping(value="/delTrans")
	public String delTransInfo(String seq_id) {
		log.info("delTransInfo");
		dao.delTransInfo(seq_id);
		return "redirect:getTrans";
	}
	
	@RequestMapping(value="/modRakuten")
	public String modRakutenInfo(RakutenSearchVO vo) {
		log.info("modRakutenInfo");
		dao.modRakutenInfo(vo);
		return "redirect:showRList";
	}
	
	@ResponseBody
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<RakutenSearchVO> targetList) {
		log.info("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<TranslationResultVO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.info("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
}
