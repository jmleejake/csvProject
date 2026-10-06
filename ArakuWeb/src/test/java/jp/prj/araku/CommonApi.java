package com.thekary.mbs.controller.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thekary.mbs.dao.vo.CustomerVo;
import com.thekary.mbs.dto.CommonDto;
import com.thekary.mbs.enums.StatusEnum;
import com.thekary.mbs.enums.TokenEnum;
import com.thekary.mbs.service.CommonService;
import com.thekary.mbs.service.CustomerService;
import com.thekary.mbs.util.AppUtil;
import com.thekary.mbs.util.EncryptUtil;
import com.thekary.mbs.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Scott
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Common", description = "공통 처리")
public class CommonApi {

	private final CustomerService customerService;
	private final CommonService commonService;

	/**
	 * Swagger UI 화면  리다이렉트
	 *
	 * @return
	 */
	@GetMapping("/docs")
	public void swaggerRedirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui");
	}

	/**
	 * 토큰 인증 확인
	 *
	 * @return
	 */
	@GetMapping(value = "/api/v1/auth/check", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "토큰 인증 확인", description = "토큰을 체크합니다.")
	public ResponseEntity<CommonDto<?>> authCheck() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "로그아웃(토큰 제거)", description = "토큰을 제거합니다.")
	@GetMapping(value = "/api/v1/token/delete", produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> someMethod(HttpServletResponse response) {

		response.addCookie(TokenUtil.deleteRefreshTokenCookie());

		return new ResponseEntity<>(new CommonDto<>(), HttpStatus.OK);
	}

	/**
	 * 인증 토큰 생성
	 */
	@Operation(summary = "토큰 발급", description = "토큰을 발급합니다.")
	@Parameters({
		@Parameter(in = ParameterIn.QUERY, name = "id-key", description = "id-key", schema = @Schema(type = "string", required = true)),
		@Parameter(in = ParameterIn.QUERY, name = "secret-key", description = "secret-key", schema = @Schema(type = "string", required = true))
	})
	@PostMapping(value = "/api/v1/token/create", produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<CommonDto<?>> tokenCreate() {
		// 컨트롤러 도착 전, Security가 뺐어감. Swagger에서 사용하기 위함.
		return new ResponseEntity<>(new CommonDto<>(), HttpStatus.OK);
	}

	/**
	 * 인증 토큰 재발급
	 */
	@Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다. Authorize에 리프레쉬 토큰을 입력하면 재발급할 수 있습니다.")
	@PostMapping(value = "/api/v1/token/refresh", produces = APPLICATION_JSON_UTF8_VALUE)
	public void tokenRefresh(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		Cookie[] myCookies = request.getCookies();
		String token = null;
		if (myCookies != null) {
			for (int i = 0; i < myCookies.length; i++) {
				if (TokenEnum.REFRESH.code().equals(myCookies[i].getName())) {
					token = myCookies[i].getValue();
					break;
				}
			}
		}
		String ip = AppUtil.getClientIp();
		String url = request.getRequestURI();
		String username = "";
		log.info("[토큰 재발급 시도][" + ip + "][" + url + "]");
		token = EncryptUtil.decryptAES256(token);
		if (token != null) {
			try {
				String jwtEncKey = commonService.getJwtEncKey();
				Algorithm algorithm = Algorithm.HMAC512(jwtEncKey);
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(token);
				String roles = decodedJWT.getClaim(TokenEnum.roleName()).asString();
				String basicRole = decodedJWT.getClaim(TokenEnum.basicRoleName()).asString();
				if (!TokenEnum.REFRESH.name().equals(roles) ||
					!TokenEnum.basicRoleCustomerCode().equals(basicRole)
				) {
					throw new Exception();
				}
				username = decodedJWT.getSubject();
				String accessToken = "";
				String refreshToken = "";
				try {
					if (TokenEnum.basicRoleCustomerCode().equals(basicRole)) {
						// 리프레쉬 토큰 확인 (사용한 refreshToken은 10초동안 사용 가능)
						commonService.addRefreshToken(token, username);

						CustomerVo customer = customerService.getCustomerInfo(username);
						accessToken = JWT.create()
							.withSubject(customer.getCustomerSeq())
							.withExpiresAt(TokenEnum.ACCESS.expiresAt())
							.withIssuer(request.getRequestURI().toString())
							.withClaim(TokenEnum.roleName(),
								Arrays.asList(new String[]{"CUSTOMER"}))
							.withClaim(TokenEnum.basicRoleName(), TokenEnum.basicRoleCustomerCode())
							.withClaim("customerName", customer.getCustomerName())
							.withClaim("customerLevel", customer.getCustomerLevel())
							.withClaim("customerMobile", customer.getCustomerMobile())
							.withClaim("registType", customer.getRegistType())
							.sign(algorithm);
						refreshToken = JWT.create()
							.withSubject(customer.getCustomerSeq())
							.withExpiresAt(TokenEnum.REFRESH.expiresAt())
							.withClaim(TokenEnum.roleName(), TokenEnum.REFRESH.name())
							.withClaim(TokenEnum.basicRoleName(), TokenEnum.basicRoleCustomerCode())
							.withIssuer(request.getRequestURI().toString())
							.sign(algorithm);
					}
					Map<String, String> tokens = new HashMap<>();
					tokens.put(TokenEnum.ACCESS.code(), accessToken);
					response.addHeader("Set-Cookie", TokenUtil.getRefreshTokenCookie(refreshToken));
					response.setContentType(APPLICATION_JSON_VALUE);
					response.setCharacterEncoding("UTF-8");
					new ObjectMapper().writeValue(response.getOutputStream(),
						new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(),
							tokens));
					log.info("[토큰 재발급 성공][" + ip + "][" + url + "][" + username + "]");
				} catch (Exception e) {
					log.info("[이미 사용된 재발급 토큰[" + ip + "][" + url + "] {}", token);
					response.setContentType(APPLICATION_JSON_VALUE);
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpStatus.FORBIDDEN.value());
					// response.addCookie(TokenUtil.deleteRefreshTokenCookie());
					List<String> messages = List.of("이미 사용된 재발급 토큰입니다.");
					new ObjectMapper().writeValue(response.getOutputStream(),
						new CommonDto<>(StatusEnum.FAILED.code(), messages));
				}
			} catch (TokenExpiredException e) {
				log.info("[토큰만료][" + ip + "][" + url + "] {}", token);
				response.setContentType(APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.FORBIDDEN.value());
				// response.addCookie(TokenUtil.deleteRefreshTokenCookie());
				String errorMessage = e.getMessage() != null ? e.getMessage() : "";
				List<String> messages = List.of("토큰이 만료되었습니다.", errorMessage);
				new ObjectMapper().writeValue(response.getOutputStream(),
					new CommonDto<>(StatusEnum.FAILED.code(), messages));
			} catch (Exception e) {
				log.error("[토큰 재발급 Error][" + ip + "][" + url + "][" + username + "]",
					e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.FORBIDDEN.value());
				// response.addCookie(TokenUtil.deleteRefreshTokenCookie());
				new ObjectMapper().writeValue(response.getOutputStream(),
					new CommonDto<>(StatusEnum.FAILED.code(), "재발급 토큰이 잘못되었습니다."));
			}
		} else {
			log.info("[토큰 재발급 실패][" + ip + "][" + url + "][" + username + "]");
			response.setContentType(APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			// response.addCookie(TokenUtil.deleteRefreshTokenCookie());
			new ObjectMapper().writeValue(response.getOutputStream(),
				new CommonDto<>(StatusEnum.FAILED.code(), "재발급 토큰을 확인해주세요."));
		}
	}
}
