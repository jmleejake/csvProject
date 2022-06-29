package jp.prj.araku.jaiko.sales.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.jaiko.sales.dao.JaikoSalesDAO;
import jp.prj.araku.jaiko.sales.vo.JaikoSalesVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.DealerVO;

@RequestMapping(value = "/jaiko/sales")
@Controller
public class JaikoSalesController {
	
	@Autowired
	JaikoSalesDAO dao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@ResponseBody
	@RequestMapping(value = "/data/ins", method = RequestMethod.POST)
	public HashMap<String, String> createSalesData(@RequestBody ArrayList<JaikoOrderVO> list) {
		return dao.createSalesData(list);
	}
	
	/**
	 * 売上一覧
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String showSalesMainPage(Model model) {
		model.addAttribute("partners", tabletPrdDao.getDealerInfo(null));
		return "jaiko/salesMain";
	}
	
	/**
	 * 売上入力
	 * @param model
	 * @param dt
	 * @param partner_id
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public String showSalesDetailPage(Model model
			, @RequestParam String dt, @RequestParam String partner_id) {
		model.addAttribute("partner_id", partner_id);
		DealerVO srch = new DealerVO();
		srch.setDealer_id(partner_id);
		model.addAttribute("partner_nm", tabletPrdDao.getDealerInfo(srch).get(0).getDealer_nm());
		model.addAttribute("dt", dt);
		model.addAttribute("detail", dao.getSalesData(partner_id, dt, "TOT"));
		return "jaiko/salesDetail";
	}
	
	@ResponseBody
	@RequestMapping(value = "/main/srch")
	public ArrayList<JaikoSalesVO> getSalesData(JaikoSalesVO vo) {
		return dao.getSalesData(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/data/sel")
	public ArrayList<JaikoSalesVO> getSalesData(@RequestParam String dt, @RequestParam String partner_id) {
		return dao.getSalesData(partner_id, dt);
	}
	
	@ResponseBody
	@RequestMapping(value = "/data/upd", method = RequestMethod.POST)
	public ArrayList<JaikoSalesVO> updateSalesData(@RequestBody ArrayList<JaikoSalesVO> list) {
		return dao.updateData(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/data/del", method = RequestMethod.POST)
	public ArrayList<JaikoSalesVO> deleteSalesData(@RequestBody ArrayList<JaikoSalesVO> list) {
		return dao.deleteData(list);
	}

}
