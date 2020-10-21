package jp.prj.araku.jaiko;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/jaiko", method = RequestMethod.GET)
@Controller
public class JaikoMainController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String jaikoLogin(ModelMap model
			, @RequestParam(value = "type", defaultValue = "0") int type) {
		log.debug("jaiko login");
		model.addAttribute("type", type);
		return "jaiko/login";
	}
	
	@RequestMapping(value = "/prdWareIn", method = RequestMethod.GET)
	public String jaikoPrdWareHouseIn() {
		log.debug("jaikoPrdWareHouseIn");
		return "jaiko/prdWarehouseIn";
	}
	
	@RequestMapping(value = "/prdWareOut", method = RequestMethod.GET)
	public String jaikoPrdWareHouseOut() {
		log.debug("jaikoPrdWareHouseOut");
		return "jaiko/prdWarehouseOut";
	}

}
