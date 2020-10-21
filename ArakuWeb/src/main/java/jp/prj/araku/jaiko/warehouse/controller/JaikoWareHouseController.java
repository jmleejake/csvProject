package jp.prj.araku.jaiko.warehouse.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.warehouse.dao.JaikoWareHouseDAO;
import jp.prj.araku.jaiko.warehouse.vo.JaikoWareHouseVO;

@RequestMapping(value = "/jaiko/warehouse")
@Controller
public class JaikoWareHouseController {
	@Autowired
	JaikoWareHouseDAO dao;
	
	@RequestMapping(value = "/getList")
	@ResponseBody
	public ArrayList<JaikoWareHouseVO> getJaikoWareHouse(JaikoWareHouseVO vo) {
		return dao.getJaikoWareHouse(vo);
	}

}
