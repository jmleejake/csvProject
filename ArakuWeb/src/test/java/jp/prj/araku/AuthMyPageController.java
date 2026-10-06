package com.thekary.mbs.controller.auth.myPage;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thekary.mbs.dao.vo.CustomerVo;
import com.thekary.mbs.dto.CommonDto;
import com.thekary.mbs.dto.request.CouponSearchReqDto;
import com.thekary.mbs.dto.request.CustomerInfoModReqDto;
import com.thekary.mbs.dto.request.GiftReceiptAddReqDto;
import com.thekary.mbs.dto.request.GiftReceiptAddReqDto.GiftReceiptDetailAddReqDto;
import com.thekary.mbs.dto.request.GiftReceiptSearchReqDto;
import com.thekary.mbs.dto.request.GiftReceiptSendReqDto;
import com.thekary.mbs.dto.request.OrderSearchReqDto;
import com.thekary.mbs.dto.request.PointSearchReqDto;
import com.thekary.mbs.dto.response.CustomerInfoResDto;
import com.thekary.mbs.dto.response.CustomerResDto;
import com.thekary.mbs.dto.response.CustomerUnifiedInfoResDto;
import com.thekary.mbs.dto.response.GiftReceiptListResDto;
import com.thekary.mbs.dto.response.GiftReceiptResDto;
import com.thekary.mbs.dto.response.MyPageGradeDto;
import com.thekary.mbs.dto.response.MypageSummaryResDto;
import com.thekary.mbs.dto.response.OrderListResDto;
import com.thekary.mbs.dto.response.OrderSummaryResDto;
import com.thekary.mbs.dto.response.PointResDto;
import com.thekary.mbs.enums.StatusEnum;
import com.thekary.mbs.enums.TokenEnum;
import com.thekary.mbs.service.CommonService;
import com.thekary.mbs.service.CouponService;
import com.thekary.mbs.service.CustomerService;
import com.thekary.mbs.service.MyPageService;
import com.thekary.mbs.service.PointService;
import com.thekary.mbs.service.StampService;
import com.thekary.mbs.util.EncryptUtil;
import com.thekary.mbs.util.Pagination;
import com.thekary.mbs.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Common 처리
 *
 * @author Jack
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/myPage")
@Tag(name = "mypage", description = "마이페이지")
public class AuthMyPageController {

	/**
	 * CouponService
	 */
	private final CouponService couponService;
	private final PointService pointService;
	private final MyPageService myPageService;
	private final CustomerService customerService;
	private final StampService stampService;
	private final ModelMapper modelMapper;
	private final CommonService commonService;


	/**
	 * 인증 토큰 재발급
	 */
	@Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다. Authorize에 리프레쉬 토큰을 입력하면 재발급할 수 있습니다.")
	@PostMapping(value = "/app/token/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonDto<?>> appTokenCreate() {

		String jwtEncKey = commonService.getJwtEncKey();
		Algorithm algorithm = Algorithm.HMAC512(jwtEncKey);

		CustomerVo customer = customerService.getCustomerInfo(TokenUtil.getCustomerSeq());

		String accessToken = JWT.create()
			.withSubject(customer.getCustomerSeq())
			.withExpiresAt(new Date(System.currentTimeMillis() + 5L * 365 * 24 * 60 * 60 * 1000)) // 5년
			.withIssuer("/api/v1/auth/myPage/app/token/create")
			.withClaim(TokenEnum.roleName(),
				Arrays.asList(new String[]{"CUSTOMER"}))
			.withClaim(TokenEnum.basicRoleName(), TokenEnum.basicRoleCustomerCode())
			.withClaim("customerName", customer.getCustomerName())
			.withClaim("customerLevel", customer.getCustomerLevel())
			.withClaim("customerMobile", customer.getCustomerMobile())
			.withClaim("registType", customer.getRegistType())
			.sign(algorithm);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), accessToken),
			HttpStatus.OK);
	}

	@GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "쿠폰 목록 리스트", description = "쿠폰목록조회")
	public ResponseEntity<CommonDto<?>> myHome(

	) {
		//1. 이름 : 완료 (getPointSummary)
		//2. 현재포인트 : 완료 (getPointSummary)
		//3. 적립 에정포인트 : 완료 (getPointSummary)
		//4. 당월 소멸예정포인트 : 완료 (getPointDeleteSoon)
		//5. 최근 (30일) 사용한 포인트 : 완료 (getPointSummary)
		//6. 포인트 검색목록 (전체/적립/사용/소멸) : 완료
		//7. 사용가능 쿠폰 갯수 : getCouponCount
		//8. 사용가능 쿠폰 목록 : getCouponList
		//9. 사용한 쿠폰 목록 : getCouponList
		String customerSeq = TokenUtil.getCustomerSeq();

		// 쿠폰 사용가능 갯수
		Integer availableCouponCount = couponService.getCouponCount(CouponSearchReqDto.builder()
					.searchCustomerSeq(customerSeq)
					.searchCouponStatus("AVAILABLE")
					.build());

		MypageSummaryResDto summaryResDto = pointService.getMyPageHome(customerSeq);

		summaryResDto.setAvailableCouponCount(availableCouponCount);

		return new ResponseEntity<>(new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), summaryResDto), HttpStatus.OK);
	}

	@GetMapping(value = "/customer/grade", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "회원 등급", description = "회원 등급")
	public ResponseEntity<CommonDto<?>> getCustomerGrade(
	) {

		MyPageGradeDto gradeDto = myPageService.getCustomerGrade(TokenUtil.getCustomerSeq());

		return new ResponseEntity<>(new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), gradeDto), HttpStatus.OK);
	}

	@PostMapping(value = "/password/check")
	public ResponseEntity<CommonDto<?>> customerPasswordCheck(
		@RequestParam String pwd) {

		CustomerResDto resDto = customerService.getCustomerInfoByType("customerSeqAndPassword", TokenUtil.getCustomerSeq(), EncryptUtil.encodeSHA256(pwd), null);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@PostMapping(value = "/withdraw/summary")
	public ResponseEntity<CommonDto<?>> withdrawSummary() {
		String customerSeq = TokenUtil.getCustomerSeq();

		MypageSummaryResDto resDto = new MypageSummaryResDto();
		// 쿠폰 사용가능 갯수
		Integer availableCouponCount = couponService.getCouponCount(CouponSearchReqDto.builder()
			.searchCustomerSeq(customerSeq)
			.searchCouponStatus("AVAILABLE")
			.build());

		resDto.setAvailableCouponCount(availableCouponCount);

		PointResDto.PointSummaryResDto pointSummary = pointService.getPointSummary(
			PointSearchReqDto.builder()
				.searchCustomerSeq(customerSeq)
				.build());

		resDto.setSumAvailablePoints(pointSummary.getSumAvailablePoints());
		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@PostMapping(value = "/withdraw-exec")
	public ResponseEntity<CommonDto<?>> withdrawExec(@RequestParam String withdrawReason,
		@RequestParam(required = false) String withdrawDetailReason) throws Exception {

		if ("null".equals(withdrawDetailReason)) {
			withdrawDetailReason = null;
		}

		String result = customerService.modifyCustomerWithdraw(TokenUtil.getCustomerSeq(), withdrawReason,
			withdrawDetailReason);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), result),
			HttpStatus.OK);
	}

	@PostMapping(value = "/info")
	public ResponseEntity<CommonDto<?>> CustomerInfo() {

		CustomerVo vo = customerService.getCustomerInfo(TokenUtil.getCustomerSeq());
		CustomerInfoResDto resDto = modelMapper.map(vo, CustomerInfoResDto.class);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@PostMapping(value = "/info/modify-exec")
	public ResponseEntity<CommonDto<?>> CustomerInfo(
		@Valid @Parameter(description = "회원 등록 Exec Req") @RequestBody CustomerInfoModReqDto reqDto)
		throws Exception {
		String result = customerService.modifyCustomerInfo(reqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), result, null),
			HttpStatus.OK);
	}

	/**
	 * 현재 스탬프 조회
	 */
	@GetMapping(value ="/fnb/stamp")
	public ResponseEntity<CommonDto<?>> currentStamp()
	{
		String customerSeq = TokenUtil.getCustomerSeq();
		return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), myPageService.getCurrentStamp(customerSeq)),
				HttpStatus.OK);
	}

	/**
	 * 스탬프 이력조회
	 */
	@GetMapping(value ="/fnb/stamp/hisotry/{range}")
	public ResponseEntity<CommonDto<?>> stampHistory(@PathVariable String range
	) {
		String customerSeq = TokenUtil.getCustomerSeq();
		if (range == null || range.isEmpty()) {
			range = "1";
		}
		return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), myPageService.getStampHistory(customerSeq, range)),
				HttpStatus.OK);
	}

	/**
	 * 스탬프 지급
	 */
	@GetMapping(value ="/fnb/stamp/add-exec")
	public ResponseEntity<CommonDto<?>> stampAddExec(
			@Valid @RequestParam("orderNo") String orderNo
	) {
		String customerSeq = TokenUtil.getCustomerSeq();

		// 스탬프 지급
		String message = stampService.insertStampCustomerIssue(customerSeq, "orderNo");

		return new ResponseEntity<>(
				new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), message),
				HttpStatus.OK);
	}

	@GetMapping(value = "/customer/unified/info")
	public ResponseEntity<CommonDto<?>> CustomerUnifiedInfo() {

		CustomerUnifiedInfoResDto resDto = customerService.getCustomerUnifiedInfo();

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@GetMapping(value = "/offline/order/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "오프라인 주문 리스트", description = "오프라인 주문 리스트를 불러옵니다")
	public ResponseEntity<CommonDto<?>> authMyPageOfflineOrderList(
		@ParameterObject @ModelAttribute OrderSearchReqDto searchReqDto,
		@Parameter(description = "페이지 No") @RequestParam(defaultValue = "1") int pageNo,
		@Parameter(description = "페이지 Size") @RequestParam(defaultValue = "10") int pageSize
	) {

		Pagination pagination = new Pagination(customerService.getOfflineOrderCount(searchReqDto),
			pageNo, pageSize);

		OrderListResDto resDto = customerService.getOfflineOrderList(pagination, searchReqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@GetMapping(value = "/offline/order/summary", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "오프라인 주문 요약", description = "오프라인 주문 상태별 카운트를 조회합니다")
	public ResponseEntity<CommonDto<?>> authMyPageOfflineOrderSummary(
		@ParameterObject @ModelAttribute OrderSearchReqDto searchReqDto
	) {
		OrderSummaryResDto resDto = customerService.getOfflineOrderSummary(searchReqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@PostMapping(value = "/gift/receipt/add-exec", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "교환권 등록", description = "교환권을 등록합니다")
	public ResponseEntity<CommonDto<?>> authMyPageGiftReceiptAddExec(
		@RequestBody GiftReceiptAddReqDto reqDto) {

		String exchangeNo = customerService.addGiftReceipt(reqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), exchangeNo),
			HttpStatus.OK);
	}

	/**
	 * 출석 체크 이벤트 참여정보
	 */
	@GetMapping(value = "/gift/receipt/info", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "교환권 상세 조회", description = "교환권 상세 조회")
	public ResponseEntity<CommonDto<?>> authMyPageGiftReceiptInfo(
		@RequestParam Long giftReceiptSeq
	) {
		GiftReceiptResDto resDto = customerService.getGiftReceipt(giftReceiptSeq);
		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@GetMapping(value = "/gift/receipt/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "교환권 리스트", description = "교환권 리스트를 불러옵니다")
	public ResponseEntity<CommonDto<?>> authMyPageGiftReceiptList(
		@ParameterObject @ModelAttribute GiftReceiptSearchReqDto searchReqDto,
		@Parameter(description = "페이지 No") @RequestParam(defaultValue = "1") int pageNo,
		@Parameter(description = "페이지 Size") @RequestParam(defaultValue = "10") int pageSize
	) {

		Pagination pagination = new Pagination(customerService.getGiftReceiptCount(searchReqDto),
			pageNo, pageSize);

		GiftReceiptListResDto resDto = customerService.getGiftReceiptList(pagination, searchReqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), resDto),
			HttpStatus.OK);
	}

	@PostMapping(value = "/gift/receipt/send-exec", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "교환권 발송", description = "교환권을 발송합니다")
	public ResponseEntity<CommonDto<?>> authMyPageGiftReceiptSendExec(
		@RequestParam String orderNo,
		@RequestParam MultipartFile receiptImg,
		@RequestParam String selectedItemListJson) throws IOException {

		// ObjectMapper 직접 사용
		ObjectMapper objectMapper = new ObjectMapper();
		List<GiftReceiptDetailAddReqDto> selectedItemList = objectMapper.readValue(
			selectedItemListJson,
			objectMapper.getTypeFactory().constructCollectionType(
				List.class,
				GiftReceiptDetailAddReqDto.class
			)
		);

		GiftReceiptSendReqDto reqDto = GiftReceiptSendReqDto.builder()
			.orderNo(orderNo)
			.receiptImg(receiptImg)
			.selectedItemList(selectedItemList)
			.build();

		String url = customerService.sendGiftReceipt(reqDto);

		return new ResponseEntity<>(
			new CommonDto<>(StatusEnum.SUCCESS.code(), StatusEnum.SUCCESS.name(), url),
			HttpStatus.OK);
	}

}
