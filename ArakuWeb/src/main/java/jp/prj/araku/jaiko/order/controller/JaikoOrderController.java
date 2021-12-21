package jp.prj.araku.jaiko.order.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.order.dao.JaikoOrderDAO;
import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.jaiko.partner.dao.JaikoPartnerDAO;

@RequestMapping(value = "/jaiko/order")
@Controller
public class JaikoOrderController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	JaikoOrderDAO dao;
	
	@Autowired
	JaikoPartnerDAO partnerDAO;
	
	@RequestMapping(value = "")
	public String showJaikoOrder(Model model) {
		model.addAttribute("partners", partnerDAO.getPartner(null));
		return "jaiko/order";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMonthlyData")
	public ArrayList<JaikoOrderVO> getMonthlyData(JaikoOrderVO vo) {
		return dao.getMonthlyData(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getData")
	public ArrayList<JaikoOrderVO> getData(JaikoOrderVO vo) {
		return dao.getData(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/mani", method = RequestMethod.POST)
	public ArrayList<JaikoOrderVO> manipulateJaikoOrder(@RequestBody ArrayList<JaikoOrderVO> list) {
		return dao.manipulateJaikoOrder(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public ArrayList<JaikoOrderVO> deleteJaikoOrder(@RequestBody ArrayList<JaikoOrderVO> list) {
		return dao.deleteJaikoOrder(list);
	}
}
