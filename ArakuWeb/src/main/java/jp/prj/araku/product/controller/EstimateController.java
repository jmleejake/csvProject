package jp.prj.araku.product.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.product.dao.EstimateDAO;
import jp.prj.araku.product.vo.EstimateVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;

@RequestMapping(value="/araku/prdAnalysis")
@Controller
public class EstimateController {
	@Autowired
	private TabletPrdDAO dealerDAO;
	
	@Autowired
	private EstimateDAO dao;
	
	@RequestMapping(value = "/estimate")
	public String estimateStatus(Model model) {
		model.addAttribute("dealers", dealerDAO.getDealerInfo(null));
		return "estimate/status";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStatus")
	public ArrayList<EstimateVO> getStatus(EstimateVO vo) {
		return dao.selectStatus(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/mani", method = RequestMethod.POST)
	public ArrayList<EstimateVO> manipulateStatus(@RequestBody ArrayList<EstimateVO> list) {
		return dao.manupulateStatus(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public ArrayList<EstimateVO> deleteStatus(@RequestBody ArrayList<EstimateVO> list) {
		return dao.deleteStatus(list);
	}

}
