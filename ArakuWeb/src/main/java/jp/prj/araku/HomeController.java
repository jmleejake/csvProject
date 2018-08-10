package jp.prj.araku;

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
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(Locale locale) {
		logger.info("Welcome home! The client locale is {}.", locale);
		return "redirect:fileView";
	}
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		return "menu/fileView";
	}
	
	@RequestMapping(value = "/orderView")
	public String orderView() {
		return "menu/orderInfo";
	}
	
	@RequestMapping(value = "/translationView")
	public String translationView() {
		return "menu/translation";
	}
	
	@RequestMapping(value = "/claimNoView")
	public String claimNoView() {
		return "menu/claimNoUpdate";
	}
	
	@RequestMapping(value = "/rFileDownView")
	public String rFileDownView() {
		return "menu/rakutenFileDown";
	}
	
	public String yuView() {
		return "menu/";
	}
	
}
