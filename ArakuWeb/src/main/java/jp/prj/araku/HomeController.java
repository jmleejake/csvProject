package jp.prj.araku;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.user.dao.ArakuUserDao;
import jp.prj.araku.user.vo.ArakuAuthVO;

/**
 * Handles requests for the application home page.
 */
@RequestMapping(value = "/araku")
@Controller
public class HomeController {
	
	@Autowired
	ArakuUserDao dao;
	
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "")
	public String home(HttpServletRequest request, Locale locale, Model model) {
		log.debug("Welcome araku mainpage! The client locale is {}.", locale);
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("arakuId");
		log.debug("arakuId : {}", userId);
		if(userId == null) {
			return "arakuLogin";
		}else {
			model.addAttribute("authList", dao.getUserAuth(userId));
			return "arakuMain";
		}
	}
	
	@RequestMapping(value = "/translationView")
	public String translationView() {
		log.debug("Welcome to translationView");
		return "menu/translation";
	}
	
	@RequestMapping(value = "/claimNoView")
	public String claimNoView() {
		log.debug("Welcome to claimNo updateView");
		return "menu/claimNoUpdate";
	}
	
	@RequestMapping(value = "/rFileDownView")
	public String rFileDownView() {
		log.debug("Welcome to Rakuten File download view");
		return "menu/rakutenFileDown";
	}
	
	@RequestMapping(value = "/yuView")
	public String yuView() {
		log.debug("Welcome to yupuri file upload view");
		return "menu/yuUpload";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView() {
		log.debug("Welcome to region master view");
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.debug("Welcome to translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "menu/transResult";
	}
	
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
			ret = "redirect:/araku";
		}else if(procRet == 3) {
			model.addAttribute("authList", dao.getUserAuth(user_id));
			ret = "arakuMain";
		}
		return ret;
	}
	
	@RequestMapping(value = "/user/list")
	public String showArkauUserList(Model model) {
		model.addAttribute("authList", dao.getArakuAuth());
		return "user/userAuthSetting";
	}
	
	@RequestMapping(value = "/logout")
	public String loginCheck(HttpSession session) {
		session.invalidate();
		return "redirect:/araku";
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/modify-exec", method = RequestMethod.POST)
	public String modifyUserAuth(ArakuAuthVO auth) {
		return dao.modifyUserAuth(auth);
	}
	
}
