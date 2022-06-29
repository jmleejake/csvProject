package jp.prj.araku.jaiko.order.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import jp.prj.araku.tablet.dao.TabletPrdDAO;

@RequestMapping(value = "/jaiko/order")
@Controller
public class JaikoOrderController {
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	JaikoOrderDAO dao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@RequestMapping(value = "")
	public String showJaikoOrder(Model model, JaikoOrderVO vo) {
		model.addAttribute("partners", tabletPrdDao.getDealerInfo(null));
		//model.addAttribute("calendar", dao.getCalendar(vo));
		if(null == vo.getReg_dt() || "".equals(vo.getReg_dt()) ) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			vo.setReg_dt(sdf.format(cal.getTime()));
		}
		String thisMonth = vo.getReg_dt();
		String[] yyyymm = thisMonth.split("-");
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Integer.parseInt(yyyymm[0]), Integer.parseInt(yyyymm[1])-1,1);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		String regDt = vo.getReg_dt().replaceAll("-", "");
		vo.setFrom_dt(regDt+"01000000");
		vo.setTo_dt(regDt+lastDay+"235959");
		model.addAttribute("orderData", dao.getData(vo));
		model.addAttribute("thisMonth", thisMonth);
		ArrayList<String> days = new ArrayList<String>();
		for(int i=1; i<=lastDay; i++) {
			days.add(thisMonth+"-"+String.format("%02d", i));
		}
		model.addAttribute("calendar", days);
		return "jaiko/order2";
	}
	/*
	public String showJaikoOrder(Model model) {
		model.addAttribute("partners", partnerDAO.getPartner(null));
		return "jaiko/order";
	}
	*/
	
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
