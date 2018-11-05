package jp.prj.araku.list.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.RakutenSearchVO;

@Controller
public class ListController {
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
	@Autowired
	ListDAO dao;
	
	@RequestMapping(value="/modRakuten")
	public String modRakutenInfo(@RequestBody ArrayList<RakutenSearchVO> vo) {
		log.info("modRakutenInfo");
		dao.modRakutenInfo(vo);
		return "redirect:showRList";
	}
	
}
