package jp.prj.araku.list.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.RakutenSearchVO;

@Controller
public class ListController {
	private static final Logger log = LoggerFactory.getLogger(ListController.class);
	
	@Autowired
	ListDAO dao;
	
	@ResponseBody
	@RequestMapping(value="/showRList")
	public ArrayList<RakutenSearchVO> showRakutenList(RakutenSearchVO searchVO) {
		log.info("showRakutenList :: POST");
		log.debug("search item :: {}", searchVO);
		return dao.getRList(searchVO);
	}
	
}
