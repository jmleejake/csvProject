package jp.prj.araku.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ArakuLoginInterceptor extends HandlerInterceptorAdapter {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("preHandle2");
		log.debug(request.getRequestURI());
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("arakuId");
		log.debug("-----------------------------");
		log.debug("arakuId : {}", userId);
		log.debug("-----------------------------");
		
		if(userId == null) {
			response.sendRedirect("/araku");
			return false;
		}
		return true;
	}
}
