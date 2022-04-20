package jp.prj.araku.jaiko.sales.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.jaiko.sales.dao.JaikoSalesDAO;

@RequestMapping(value = "/jaiko/sales")
@Controller
public class JaikoSalesController {
	
	@Autowired
	JaikoSalesDAO dao;
	
	@ResponseBody
	@RequestMapping(value = "/data/ins", method = RequestMethod.POST)
	public HashMap<String, String> createSalesData(@RequestBody ArrayList<JaikoOrderVO> list) {
		return dao.createSalesData(list);
	}

}
