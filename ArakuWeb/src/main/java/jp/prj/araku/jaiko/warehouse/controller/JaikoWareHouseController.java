package jp.prj.araku.jaiko.warehouse.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.product.dao.JaikoPrdInfoDAO;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.jaiko.warehouse.dao.JaikoWareHouseDAO;
import jp.prj.araku.jaiko.warehouse.vo.JaikoWareHouseVO;

@RequestMapping(value = "/jaiko/warehouse")
@Controller
public class JaikoWareHouseController {
	@Autowired
	JaikoWareHouseDAO dao;
	@Autowired
	JaikoPrdInfoDAO prdDao;
	
	@RequestMapping(value = "/getPrd")		
	@ResponseBody
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		return prdDao.getJaikoPrdInfo(vo);
	}
	
	@RequestMapping(value = "/getList")
	@ResponseBody
	public ArrayList<JaikoWareHouseVO> getJaikoWareHouse(JaikoWareHouseVO vo) {
		return dao.getJaikoWareHouse(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoWareHouseVO> manipulateJaikoPrdInfo(@RequestBody ArrayList<JaikoWareHouseVO> list) {
		return dao.manipulateJaikoWareHouse(list);
	}

}
