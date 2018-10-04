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
@Controller
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(Locale locale) {
		log.info("Welcome araku mainpage! The client locale is {}.", locale);
		return "arakuMain";
	}
	
	@RequestMapping(value = "/translationView")
	public String translationView() {
		log.info("Welcome to translationView");
		return "menu/translation";
	}
	
	@RequestMapping(value = "/claimNoView")
	public String claimNoView() {
		log.info("Welcome to claimNo updateView");
		return "menu/claimNoUpdate";
	}
	
	@RequestMapping(value = "/rFileDownView")
	public String rFileDownView() {
		log.info("Welcome to Rakuten File download view");
		return "menu/rakutenFileDown";
	}
	
	@RequestMapping(value = "/yuView")
	public String yuView() {
		log.info("Welcome to yupuri file upload view");
		return "menu/yuUpload";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView() {
		log.info("Welcome to region master view");
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.info("Welcome to translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "menu/transResult";
	}
	
}
