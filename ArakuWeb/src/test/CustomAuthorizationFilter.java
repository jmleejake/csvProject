package com.thekary.erp.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thekary.erp.dao.PartnerDao;
import com.thekary.erp.dao.UserDao;
import com.thekary.erp.dao.vo.LogBpsResultVo;
import com.thekary.erp.dao.vo.PartnerUserVo;
import com.thekary.erp.dao.vo.UserVo;
import com.thekary.erp.dto.CommonDto;
import com.thekary.erp.enums.StatusEnum;
import com.thekary.erp.enums.TokenEnum;
import com.thekary.erp.service.CommonService;
import com.thekary.erp.util.AppUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private final UserDao userDao;
	private final CommonService commonService;
	private final PartnerDao partnerDao;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader(AUTHORIZATION);
		Cookie[] myCookies = request.getCookies();
		String ssoToken = null;
		if (myCookies != null) {
			for (int i = 0; i < myCookies.length; i++) {
				if (TokenEnum.SSO.code().equals(myCookies[i].getName())) {
					ssoToken = myCookies[i].getValue();
					break;
				}
			}
		}
		String uriPath = request.getRequestURI();
		if (uriPath.equals("/api/v1/token/create")
			|| uriPath.equals("/api/v1/token/create/partner")
			|| uriPath.equals("/api/v1/token/refresh")
			|| uriPath.equals("/api/v1/token/delete")
			|| uriPath.equals("/api/v1/token/iframe")
			|| uriPath.startsWith("/api/v1/employee/auth")
			|| uriPath.startsWith("/tk-ws")
			|| uriPath.equals("/api/v1/kicc/easyCard/ecm/order/new/result-exec")
			|| uriPath.equals("/api/v1/sls/fcm/token/modify-exec")
			|| uriPath.equals("/api/v1/sls/fcm/push/click-exec")
			|| (authorizationHeader == null && ssoToken == null)) {

			filterChain.doFilter(request, response);
		}
		/*else if (request.getServletPath().equals("/api/v1/repair/add-exec") ||
			request.getServletPath().equals("/api/v1/logisticsReceivePlan/add-exec")) {
			String token = authorizationHeader.substring(TokenEnum.prefix().length());
			if ("SYSTEMDAKDJHAKDJQOWJDI)OJIDOQWJDOKWI".equals(token)) {
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(TokenEnum.basicRoleAdminCode()));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					"SYSTEM", null, authorities);
				SecurityContextHolder.getContext()
					.setAuthentication(authenticationToken);
			}
			filterChain.doFilter(request, response);
		}*/
		else {
			try {
				DecodedJWT decodedJWT;
				String username;
				String basicRole;
				if (authorizationHeader.startsWith(TokenEnum.prefix()) || ssoToken != null) {
					if (uriPath.startsWith("/api/v1")) {
						try {
							String ssoJwtEncKey = commonService.getSSOJwtEncKey();
							Algorithm ssoAlgorithm = Algorithm.HMAC512(ssoJwtEncKey);
							JWTVerifier verifier = JWT.require(ssoAlgorithm).build();
							decodedJWT = verifier.verify(ssoToken);
							basicRole = TokenEnum.basicRoleAdminCode();
						} catch (Exception e) {
							// SSO 토큰이 없거나 검증 실패 시, 일반 토큰 검증
							String jwtEncKey = commonService.getJwtEncKey();
							String token = authorizationHeader.substring(
								TokenEnum.prefix().length());
							Algorithm algorithm = Algorithm.HMAC512(jwtEncKey);
							JWTVerifier verifier = JWT.require(algorithm).build();
							decodedJWT = verifier.verify(token);
							basicRole = decodedJWT.getClaim(TokenEnum.basicRoleName()).asString();
						}

						username = decodedJWT.getSubject();

						String permissionType;
						if ("/api/v1/notification/message/list/count".equals(uriPath)
							|| "/api/v1/auth/check".equals(uriPath)
						) {
							permissionType = "ALLOW";
						} else if (TokenEnum.basicRolePartnerCode().equals(basicRole)) {
							permissionType = userDao.selectRoleAuthPermissionCheck(uriPath,
								"PARTNER_" + username);
						} else {
							permissionType = userDao.selectRoleAuthPermissionCheck(uriPath,
								username);
						}
						if ("ALLOW".equals(permissionType)) {
							Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
							authorities.add(new SimpleGrantedAuthority(basicRole));
							UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								username, null, authorities);
							SecurityContextHolder.getContext()
								.setAuthentication(authenticationToken);
						} else {
							response.setContentType(APPLICATION_JSON_VALUE);
							response.setCharacterEncoding("UTF-8");
							response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value()); // 권한 없음.
							return;
						}

						// BPS 로깅 (백) 시작
						List<String> bpsExcludePathList = List.of(
							"/"
							, ""
							, "/api/v1/brand/list-all"
							, "/api/v1/store/list-all"
							, "/api/v1/common/van/result/add-exec"
							, "/api/v1/common/trs/result/add-exec"
							, "/api/v1/common/van/ecm/result/add-exec"
							, "/api/v1/common/van/eca/result/add-exec"
							, "/api/v1/common/bps/result/add-exec"
							, "/api/v1/user/table/list"
							, "/api/v1/common/profile/info"
							, "/api/v1/common/product/image/info"
							, "/api/v1/common/code/list"
							, "/api/v1/user/list"
							, "/api/v1/user/auth/json"
							, "/api/v1/auth/check"
							, "/api/v1/user/auth/check"
							, "/api/v1/notification/message/list/count"
							, "/api/v1/notification/message/list"
							, "/api/v1/notification/direct/message/list"
							, "/api/v1/common/bps/list");
						if (!bpsExcludePathList.contains(uriPath)) {
							try {
								ObjectMapper objectMapper = new ObjectMapper();

								// 헤더
								String headerJson = null;
								try {
									// request 안에 있는 header를 json으로 변환
									Enumeration<String> headerNames = request.getHeaderNames();
									Map<String, String> headerMap = new HashMap<>();
									while (headerNames.hasMoreElements()) {
										String key = headerNames.nextElement();
										headerMap.put(key, request.getHeader(key));
									}
									if (!headerMap.isEmpty()) {
										headerJson = objectMapper.writeValueAsString(headerMap);
										// 4000자 넘으면 자르기
										headerJson = AppUtil.truncateUtf8(headerJson, 3999);
									}
								} catch (Exception e) {
									log.warn("[BpsLogResult] requestHeader json 변환 실패", e);
								}

								// 파라미터
								String requestParam = null;
								Map<String, String[]> paramMap = request.getParameterMap();
								if (paramMap != null && !paramMap.isEmpty()) {
									requestParam = objectMapper.writeValueAsString(paramMap);
								}

								// 직원코드
								String employeeCode = null;
								if (request.getHeader("POS_SELLER_EMPLOYEE_CODE") != null) {
									employeeCode = request.getHeader("POS_SELLER_EMPLOYEE_CODE");
								}

								LogBpsResultVo bpsResult = LogBpsResultVo.builder()
									.systemCode("ERP")
									.bpsTypeCode("BACK")
									.frontUrl(request.getHeader("FRONT-URL")) // config.headers["FRONT-URL"] = window?.location?.pathname || "";
									.requestUrl(uriPath)
									.requestHeader(headerJson)
									.requestParam(requestParam)
									.requestBody(null) // 성능을 위해 쌓지 않음.
									.ip(AppUtil.getClientIp())
									.userId(username)
									.employeeCode(employeeCode)
									.build();

								commonService.addLogBpsResult(bpsResult);
							} catch (Exception e) {
								log.error("BPS 로깅 에러", e);
							}
						}
						// BPS 로깅 (백) 끝
					}
				}
				filterChain.doFilter(request, response);
			} catch (TokenExpiredException e) {
				String ip = AppUtil.getClientIp();
				log.info("[토큰만료][" + ip + "][" + uriPath + "] {}", authorizationHeader);
				response.setContentType(APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				// 토큰 삭제하면 안됨.
				// response.addCookie(TokenUtil.deleteRefreshTokenCookie());

				String errorMessage = e.getMessage() != null ? e.getMessage() : "";
				List<String> messages = List.of("토큰이 만료되었습니다.", errorMessage);
				new ObjectMapper().writeValue(response.getOutputStream(),
					new CommonDto<>(StatusEnum.UNAUTHORIZED.code(), messages));
			} catch (Exception e) {
				String ip = AppUtil.getClientIp();
				log.error("[접근 필터 Error][" + ip + "][" + uriPath + "]", e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.FORBIDDEN.value());
				// 토큰 삭제하면 안됨.
				// response.addCookie(TokenUtil.deleteRefreshTokenCookie());
				new ObjectMapper().writeValue(response.getOutputStream(),
					new CommonDto<>(StatusEnum.FAILED.code(), "인증 토큰이 잘못되었습니다."));
			}
		}
	}
}
