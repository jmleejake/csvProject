package jp.prj.araku.jaiko.inventory.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.inventory.dao.JaikoPrdInventoryDAO;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;

@RequestMapping(value = "/jaiko/prdInven")
@Controller
public class JaikoPrdInventoryController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Autowired
	JaikoPrdInventoryDAO dao;
	
	@RequestMapping(value = "")
	public String showJaikoPrdInventory() {
		return "jaiko/prdInventory";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrdInven")
	public ArrayList<JaikoPrdInventoryVO> getJaikoPrdInfo(JaikoPrdInventoryVO vo) {
		log.debug("getJaikoPrdInfo :: {}", vo);
		return dao.getJaikoPrdInventory(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInventoryVO> manipulateJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInventoryVO> list) {
		log.debug("manipulateJaikoPrdInfo :: {}", list);
		return dao.manipulatePrdInventory(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInventoryVO> deleteJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInventoryVO> list) {
		log.debug("deleteJaikoPrdInfo :: {}", list);
		return dao.deleteJaikoPrdInventory(list);
	}

}
