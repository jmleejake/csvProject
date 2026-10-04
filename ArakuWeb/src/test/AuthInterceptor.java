package com.km.adm.interceptor;

import com.km.adm.config.AppConst;
import com.km.adm.dao.LogDao;
import com.km.adm.dao.vo.ApiAuthVo;
import com.km.adm.dao.vo.LogVo;
import com.km.adm.service.CommonService;
import com.km.adm.session.UserSession;
import com.km.adm.util.AppConstants;
import com.km.adm.util.AppUtil;
import com.km.adm.util.MessageUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 인터셉터 CLASS
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private final LogDao logDao;

	private final MessageUtil messageUtil;

	private final CommonService commonService;

	/**
	 * Controller Method 실행전
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param handler  Object
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		log.info(request.getRequestURI());
		// 로그인 세션 취득
		UserSession loginSession =
			(UserSession) request.getSession().getAttribute(AppConstants.LOGIN_SESSION_NAME);
		if (request.getRequestURI().startsWith("/rest/order/fnb")
			|| request.getRequestURI().startsWith("/rest/o2o/pos/return-exec")
			|| request.getRequestURI().startsWith("/rest/order/cancel/add-exec")
		) {
			String idKey = request.getHeader("id-key");
			String secretKey = request.getHeader("secret-key");
			ApiAuthVo apiAuth = commonService.getApiAuthByApiTypeCode("ERP-TO-KM");
			if (apiAuth != null
				&& apiAuth.getIdKey().equals(idKey) && apiAuth.getSecretKey().equals(secretKey)
			) {
				return true;
			}
		}

		if (loginSession != null) {
			// 로그인 세션 안에 해당 링크 권한이 없는 경우
			if (!loginSession.getAuthLink().contains(request.getRequestURI())) {
				// Ajax sendError 906
				sendAuthError(request, response);

				return false;
			}

			// 에러를 제외하고 로그를 남김
			if (!"/error".equals(request.getRequestURI())) {
				LogVo logVo = new LogVo();
				logVo.setAccessSystem(AppConst.ONLINE_SITE_CODE);
				logVo.setUserId(loginSession.getUser().getUserId());
				logVo.setPageUrl(request.getRequestURI());
				logVo.setLogTypeCode("AC01");
				logVo.setLogRemark("");
				logVo.setClientIp(AppUtil.getClientIp());
				logDao.insertAccessLog(logVo);
			}

			return true;
		} else {
			if (isAjaxRequest(request)) {
				response.sendError(907);
				return false;
			}
			log.info("LOGIN SESSION IS NULL");
			// 세션 없을경우 로그인 페이지로 이동
			response.sendRedirect("/login");
			return false;
		}
	}

	private boolean isAjaxRequest(HttpServletRequest req) {
		String header = req.getHeader("AJAX");
		if ("true".equals(header)) {
			return true;
		} else {
			return false;
		}
	}

	private void sendAuthError(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		String message = messageUtil.getMessage("common.auth.fail");
		response.setContentType("text/html;charset=UTF-8");

		if (isAjaxRequest(request)) {
			response.setStatus(906);
			response.getWriter().write(message);
		} else {
			response.getWriter().println(
				"<script>\n"
					+ "function auth() {\n"
					+ "  if (window.opener) {\n"
					+ "    window.opener.toastr.error('" + message + "');\n"
					+ "    window.close();\n"
					+ "  } else {\n"
					+ "    window.top.toastr.error('" + message + "');\n"
					+ "    window.parent.closeLoading()\n"
					+ "  }\n"
					+ "}\n"
					+ "auth()\n"
					+ "</script>"
			);
		}
	}
}
