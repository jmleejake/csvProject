package jp.prj.araku.jaiko.user.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.prj.araku.jaiko.user.dao.JaikoUserDAO;

@RequestMapping(value = "/jaiko/user", method = RequestMethod.GET)
@Controller
public class JaikoUserController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Autowired
	JaikoUserDAO dao;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginCheck(HttpSession session
			, ModelMap model
			, @RequestParam(value = "user_id") String user_id
			, @RequestParam(value = "user_pass") String user_pass) {
		log.debug("id: {} pass: {}",user_id,user_pass);
		String ret = "";
		int procRet = dao.processLogin(session, user_id, user_pass);
		log.debug("jaiko login {}",procRet);
		if(procRet == 1 || procRet == 2) {
			model.addAttribute("type", procRet);
			ret = "redirect:/jaiko";
		}else if(procRet == 3) {
			ret = "jaiko/fileView";
		}
		return ret;
	}
}
