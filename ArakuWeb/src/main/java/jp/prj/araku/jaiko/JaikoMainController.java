package jp.prj.araku.jaiko;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public String jaikoLogin(HttpServletRequest request, ModelMap model
			, @RequestParam(value = "type", defaultValue = "0") int type) {
		log.debug("jaiko login");
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("login");
		log.debug("after login object: {}", obj);
		if(null != obj) {
			return "jaiko/fileView";
		}
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
	
	@RequestMapping(value = "/fileView", method = RequestMethod.GET)
	public String jaikoFileView() {
		log.debug("jaikoFileView");
		return "jaiko/fileView";
	}

}
