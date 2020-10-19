package jp.prj.araku.jaiko.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class JaikoLoginInterceptor extends HandlerInterceptorAdapter {
	private Logger log = LoggerFactory.getLogger("JaikoLoginInterceptor");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("login");
		log.debug("-----------------------------");
		log.debug("session attribute: {}", session.getAttribute("login"));
		log.debug("object: {}", obj);
		log.debug("-----------------------------");
		if(null == obj) {
			response.sendRedirect("/jaiko");
			return false;
		}
		return true;
	}

}
