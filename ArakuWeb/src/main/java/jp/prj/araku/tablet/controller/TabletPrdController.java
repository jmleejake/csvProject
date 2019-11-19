package jp.prj.araku.tablet.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="tablet")
@Controller
public class TabletPrdController {
	@Autowired
	ListDAO listDao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		model.addAttribute("type", CommonUtil.TRANS_TARGET_TA);
		return "menu/regionMaster";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTrans")
	public ArrayList<TranslationVO> getTransInfo(TranslationVO transVO) {
		return listDao.getTransInfo(transVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/modTrans")
	public ArrayList<TranslationVO> modTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		return listDao.registerTransInfo(transVO);
	}
	
	@RequestMapping(value="/delTrans")
	public String delTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		for (TranslationVO vo : transVO) {
			listDao.delTransInfo(vo.getSeq_id());
		}
		return "redirect:getTrans";
	}
	
	@ResponseBody
	@RequestMapping(value="/showRegionMaster")
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		return listDao.showRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modRegionMaster")
	public ArrayList<RegionMasterVO> modRegionMaster(@RequestBody ArrayList<RegionMasterVO> list) {
		return listDao.modRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/showExceptionMaster")
	public ArrayList<ExceptionMasterVO> showExceptionMaster(ExceptionMasterVO vo) {
		return listDao.getExceptionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modExceptionMaster")
	public ArrayList<ExceptionMasterVO> processExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		return listDao.registerExceptionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delExceptionMaster")
	public ArrayList<ExceptionMasterVO> deleteExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		return listDao.deleteExceptionMaster(list);
	}
	
	@RequestMapping(value="/showPrdMaster", method=RequestMethod.GET)
	@ResponseBody
	public ArrayList<TabletPrdVO> getPrdInfo(TabletPrdVO vo) {
		return tabletPrdDao.getPrdInfo(vo);
	}
	
	@RequestMapping(value="/maniPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> manipulatePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		return tabletPrdDao.manipulatePrdInfo(list);
	}
	
	@RequestMapping(value="/delPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> deletePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		return tabletPrdDao.deletePrdInfo(list);
	}
	
	@RequestMapping(value="/prdMng")
	public String showPrdManage() {
		return "tablet/prdManage";
	}
	
	@RequestMapping(value="/stockMng")
	public String showStockManage() {
		return "tablet/stockManage";
	}
	
	@RequestMapping(value="/showPrdMng")
	@ResponseBody
	public ArrayList<TabletPrdVO> showPrdManage(
			@RequestParam(value="bgc_no", defaultValue="") String baggage_claim_no) {
		TabletPrdVO vo = new TabletPrdVO();
		vo.setBaggage_claim_no(baggage_claim_no);
		return tabletPrdDao.getPrdManage(vo);
	}
	
}
