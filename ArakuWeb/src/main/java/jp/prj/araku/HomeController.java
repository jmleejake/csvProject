package jp.prj.araku;

import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@RequestMapping(value = "/araku")
@Controller
public class HomeController {
	
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "")
	public String home(Locale locale) {
		log.debug("Welcome araku mainpage! The client locale is {}.", locale);
		return "arakuMain";
	}
	
	/**
	 * Simply selects the home view to render by returning its name. kim 2023/11/10
	 */
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String home(Locale locale,Model mode) {
		log.debug("Welcome araku mainpage! The client kim locale is {}.", locale);
		return "arakuMain";
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
	
}
