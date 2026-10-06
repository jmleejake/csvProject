package com.thekary.mbs.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.thekary.mbs.config.AppConst;
import com.thekary.mbs.dao.CustomerDao;
import com.thekary.mbs.dao.PointDao;
import com.thekary.mbs.dao.vo.CustomerVo;
import com.thekary.mbs.dto.CommonDto;
import com.thekary.mbs.dto.request.CustomerEmailCheckReqDto;
import com.thekary.mbs.dto.request.CustomerHistoryModReqDto;
import com.thekary.mbs.dto.request.CustomerInfoModReqDto;
import com.thekary.mbs.dto.request.CustomerModReqDto;
import com.thekary.mbs.dto.request.CustomerPasswordModReqDto;
import com.thekary.mbs.dto.request.CustomerSearchReqDto;
import com.thekary.mbs.dto.response.CustomerHistoryResDto;
import com.thekary.mbs.dto.response.CustomerResDto;
import com.thekary.mbs.enums.StatusEnum;
import com.thekary.mbs.enums.TokenEnum;
import com.thekary.mbs.service.CustomerService;
import com.thekary.mbs.util.EncryptUtil;
import com.thekary.mbs.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 회원 컨트롤러
 *
 * @author Patrick
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

	private final CustomerService customerService;
	private final PointDao pointDao;
	private final CustomerDao customerDao;

	/**
	 * 간편회원 CSRF 방어용 세션 등록
	 */
	@GetMapping("/setSession")
	public ResponseEntity<CommonDto<?>> setSession(HttpServletResponse response) {
		String snsToken = StringUtil.getUniqueId();
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Set-Cookie", ResponseCookie.from(AppConst.SNS_STATE_SESSION_NAME, snsToken)
				.path("/")
				.sameSite("Strict")
				.httpOnly(true)
				.secure(true)
				.maxAge(TokenEnum.REFRESH.expiresAtInt())
				.build().toString());

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), snsToken),
			HttpStatus.OK);
	}

	/**
	 * 회원정보 취득
	 */
	@PostMapping(value = "/info/check")
	public ResponseEntity<CommonDto<?>> customerInfoCheck(
		@RequestParam String type,
		@RequestParam String value,
		@RequestParam(required = false) String value2,
		@RequestParam(required = false) String appJwtToken,
		@RequestParam(required = false) String appYn,
		@RequestParam(required = false) String passwordChangeDelayYn,
		HttpServletRequest request) {

		if ("customerSeqAndPassword".equals(type)) {
			value = "MBS_" + value;
			value2 = EncryptUtil.encodeSHA256(value2);
		}

		CustomerResDto resDto;

		// 기본적으로 bio 로그인 시의 token을 사용
		String tempAccessToken = appJwtToken;
		Cookie[] cookies = request.getCookies();

		if (tempAccessToken == null || tempAccessToken.isEmpty()) {
			// 비밀번호 변경 필요 시 임시 토큰을 쿠키에 저장하므로 해당 토큰 활용
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("tp_tk".equals(cookie.getName())) {
						tempAccessToken = cookie.getValue();
					}
				}
			}
		}

		if (tempAccessToken != null && !tempAccessToken.isEmpty()) {
			resDto = customerService.getCustomerInfoByBioLogin(tempAccessToken);
		} else {
			resDto = customerService.getCustomerInfoByType(type, value, value2, null);
		}

		// 프론트에서 비밀번호 30일 뒤 변경 누르면 실행
		if (resDto != null && "Y".equals(passwordChangeDelayYn)) {
			customerService.modifyCustomerPasswordChangeReserveDate(resDto.getCustomerSeq());
		}

		/** 2025.09.01 삭제 (APP 다운로드 포인트 적립) */
		// 1. APP 다운로드 포인트 적립
		if ("Y".equals(appYn)) {
			if("N".equals(resDto.getAppDownloadYn())) {
				CustomerInfoModReqDto reqDto = CustomerInfoModReqDto.builder()
					.customerSeq(resDto.getCustomerSeq())
					.appDownloadYn("Y")
					.build();

				// 회원정보 수정
				customerDao.updateCustomerInfo(reqDto);
				Long checkAppDownload = Long.valueOf(pointDao.selectCheckAppPoint(72L));
				if (checkAppDownload > 0) {
					// 포인트 지급 대상 확인 (5/1 ~ 8/31 가입자)
					Long checkJoin = Long.valueOf(pointDao.selectCheckCustomerJoinValid(resDto.getCustomerSeq(),"2024-09-03", "2099-12-31"));
					if (checkJoin != null && checkJoin > 0) {
						pointDao.insertUniquePoint(resDto.getCustomerSeq(), 72L);
					}
				}
			}
		}
		return new ResponseEntity<>(new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto), HttpStatus.OK);
	}

	/**
	 * 회원등록
	 */
	@PostMapping(value = "/add-exec")
	public ResponseEntity<CommonDto<?>> customerAddExec(
		@Valid @Parameter(description = "회원 등록 Exec Req") @RequestBody CustomerModReqDto reqDto,
		HttpServletResponse response) {

		CustomerVo vo = customerService.addCustomer(reqDto);

		if (!(reqDto.getRegistType().endsWith("GE"))) {
			customerService.destroySocialInfoCookie(response);
		}

		if (vo != null) {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), vo),
				HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.FAILED.code(), "회원가입에 실패했습니다. \n 잠시 후 다시 시도해주세요."),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 회원 추가 전환
	 */
	@PostMapping(value = "/history/modify-exec")
	public ResponseEntity<CommonDto<?>> customerHistoryModifyExec(
		@Valid @Parameter(description = "회원 수정 Exec Req") @RequestBody CustomerHistoryModReqDto reqDto)
		throws Exception {

		if (customerService.modifyCustomerHistory(reqDto)) {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name()),
				HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CommonDto<>(StatusEnum.FAILED.code(), "회원 수정에 실패했습니다."),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 회원 내역
	 */
	@PostMapping(value = "/history")
	public ResponseEntity<CommonDto<?>> customerHistory(
		@ParameterObject @ModelAttribute CustomerSearchReqDto reqDto) {

		CustomerHistoryResDto resDto = customerService.getCustomerHistory(reqDto);

		if (resDto != null) {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
				HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.FAILED.code(), "회원 내역이 없습니다."),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 아이디 찾기 (이름, 이메일 기준)
	 */
	@PostMapping(value = "/find/id")
	public ResponseEntity<CommonDto<?>> findId(
		@RequestParam String customerName,
		@RequestParam String customerEmail) {
		String type = "customerNameAndCustomerEmail";
		CustomerResDto resDto = customerService.getCustomerInfoByType(type, customerName, customerEmail, null);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	/**
	 * 회원정보 취득
	 */
	@PostMapping(value = "/find/password")
	public ResponseEntity<CommonDto<?>> findPassword(
		@RequestParam String customerName,
		@RequestParam(required = false) String customerEmail,
		@RequestParam(required = false) String customerMobile,
		@RequestParam String findType) {
		String type;
		CustomerResDto resDto = null;
		if ("EMAIL".equals(findType)) {
			type = "customerIdAndCustomerNameAndCustomerEmail";
			resDto = customerService.getCustomerInfoByType(type, null,
				customerName, customerEmail);
		} else {
			type = "customerIdAndCustomerNameAndCustomerMobile";
			resDto = customerService.getCustomerInfoByType(type, null,
				customerName, customerMobile);
		}

		if (resDto == null) {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), null),
				HttpStatus.OK);
		}

//		customerService.sendTempPassword(resDto.getCustomerSeq(), customerName,
//			StringUtil.maskId(customerId), customerEmail, customerMobile, findType);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@GetMapping(value = "/main-info")
	@Operation(summary = "회원 메인 정보", description = "회원 메인 정보를 불러옵니다")
	public ResponseEntity<CommonDto<?>> getCustomerMainInfo(
			@RequestParam("customerSeq") String customerSeq) {

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(),
				customerService.getCustomerMainInfo(customerSeq)), HttpStatus.OK);
	}

	@PostMapping(value = "/password/modify-exec")
	public ResponseEntity<CommonDto<?>> customerPasswordModifyExec(
		HttpServletRequest request,
		@Valid @Parameter(description = "회원 비밀번호 변경 Exec Req") @RequestBody CustomerPasswordModReqDto reqDto)
		throws Exception {
		String result = customerService.modifyCustomerPassword(request, reqDto);

		return new ResponseEntity<>(new CommonDto<>(StatusEnum.SUCCESS.code(), result, null),
			HttpStatus.OK);
	}

	@PostMapping(value = "/email/send-exec", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "이메일 본인인증 번호 발송", description = "이메일 본인인증 번호를 발송합니다.")
	public ResponseEntity<CommonDto<?>> sendPosSelfAuthNumber(
		@Parameter(description = "이메일") @RequestParam String email) {

		customerService.sendEmailSelfAuthNumber(email);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name()),
			HttpStatus.OK);
	}
	/**
	 * SMS 본인인증 번호 확인
	 */
	@PostMapping(value = "/email/check-exec", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "이메일 본인인증 번호 확인", description = "포스 본인인증 번호를 확인합니다.")
	public ResponseEntity<CommonDto<?>> sendPosSelfAuthNumber(
		@Valid @Parameter(description = "본인인증 체크 ReqDto") @RequestBody CustomerEmailCheckReqDto reqDto
	) {

		String token = customerService.checkEmailSelfAuthNumber(reqDto);

		if (token != null) {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), token),
				HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.FAILED.code(), ""), HttpStatus.BAD_REQUEST);
		}
	}
}
