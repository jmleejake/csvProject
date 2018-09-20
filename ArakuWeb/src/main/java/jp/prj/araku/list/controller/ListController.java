package jp.prj.araku.list.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;

@Controller
public class ListController {
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
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
	public String delTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.info("delTransInfo");
		for (TranslationVO vo : transVO) {
			dao.delTransInfo(vo.getSeq_id());
		}
		return "redirect:getTrans";
	}
	
	@RequestMapping(value="/modRakuten")
	public String modRakutenInfo(@RequestBody ArrayList<RakutenSearchVO> vo) {
		log.info("modRakutenInfo");
		dao.modRakutenInfo(vo);
		return "redirect:showRList";
	}
	
	@RequestMapping(value="/delRakuten")
	public String delRakutenInfo(@RequestBody ArrayList<RakutenSearchVO> vo) {
		log.info("delRakutenInfo");
		dao.delRakutenInfo(vo);
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
	
	@RequestMapping(value="/modTransResult")
	public void modTransResult(TranslationResultVO vo) {
		log.info("modTransResult");
		dao.modTransResult(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/showRegionMaster")
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.info("showRegionMaster");
		return dao.showRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modRegionMaster")
	public ArrayList<RegionMasterVO> modRegionMaster(@RequestBody ArrayList<RegionMasterVO> list) {
		log.info("modRegionMaster");
		return dao.modRegionMaster(list);
	}
	
}
