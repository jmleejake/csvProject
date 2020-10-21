package jp.prj.araku.jaiko.product.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.product.dao.JaikoPrdInfoDAO;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;

@RequestMapping(value = "/jaiko/prdInfo")
@Controller
public class JaikoPrdInfoController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Autowired
	JaikoPrdInfoDAO dao;
	
	@RequestMapping(value = "")
	public String showJaikoPrdInfo() {
		return "jaiko/prdInfo";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrdInfo")
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		log.debug("getJaikoPrdInfo :: {}", vo);
		return dao.getJaikoPrdInfo(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInfoVO> manipulateJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInfoVO> list) {
		log.debug("manipulateJaikoPrdInfo :: {}", list);
		return dao.manipulateJaikoPrdInfo(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInfoVO> deleteJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInfoVO> list) {
		log.debug("deleteJaikoPrdInfo :: {}", list);
		return dao.deleteJaikoPrdInfo(list);
	}

}
