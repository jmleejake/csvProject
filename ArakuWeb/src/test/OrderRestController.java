package com.km.adm.controller.rest;

import com.km.adm.common.CommonResponse;
import com.km.adm.config.AppConst;
import com.km.adm.dao.vo.OrdSalesHistoryMemoVo;
import com.km.adm.dao.vo.OrderVo;
import com.km.adm.dao.vo.ShinhanTransactionVo;
import com.km.adm.dao.vo.UserVo;
import com.km.adm.dto.erp.request.OrderShippingAddressModifyReqDto;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.km.adm.model.OrdCancelModel;
import com.km.adm.model.OrderModel;
import com.km.adm.model.ReturnExchangeModel;
import com.km.adm.model.ShinhanTransactionModel;
import com.km.adm.service.CommonService;
import com.km.adm.service.ExcelService;
import com.km.adm.service.KiccService;
import com.km.adm.service.OrderCancelService;
import com.km.adm.service.OrderService;
import com.km.adm.service.ReturnExchangeService;
import com.km.adm.util.AppUtil;
import com.km.adm.util.DateUtil;
import com.km.adm.util.Pagination;
import com.km.adm.util.ResponseEntityUtil;
import com.km.adm.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 주문 처리
 *
 * @author smlee
 */

@RestController
@RequiredArgsConstructor
public class OrderRestController {

	private final OrderService orderService;
	private final OrderCancelService orderCancelService;
	private final ExcelService excelService;

	final static String historyTypeAdmin = "ADMIN_MEMO";
	private final KiccService kiccService;
	private final CommonService commonService;
	private final ReturnExchangeService returnExchangeService;

	/**
	 * 주문상태 변경
	 */
	@PostMapping(value="/rest/order/status/change")
	public ResponseEntity<String> OrderStatusModifyExec(
		@RequestBody OrderModel orderModel) {
		try {
			orderService.modifyOrderStatus(orderModel);
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 운송장 저장
	 */
	@PostMapping(value="/rest/order/shipping/add-exec")
	public ResponseEntity<CommonResponse> OrderShippingAddExec(
		@RequestBody OrderModel orderModel
	) throws Exception {
		orderService.addOrderShipping(orderModel);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**
	 * 운송장 변경
	 */
	@PostMapping(value="/rest/order/shipping/modify-exec")
	public ResponseEntity<CommonResponse> OrderShippingModifyExec(
		@RequestBody OrderModel orderModel
	) throws Exception {
		orderService.delOrderShipping(orderModel);
		return ResponseEntityUtil.success("SUCCESS");
	}


	/**통합주문조회 엑셀다운로드*/
	@PostMapping(value ="/rest/order/total/excel")
	public ResponseEntity<CommonResponse> getOrderTotalListExcel(
		@ModelAttribute("orderModel") OrderModel orderModel
		, HttpServletResponse response)
		throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		String fileName = "통합주문조회"+DateUtil.getDate(DateUtil.FORMAT_YYYYMMDDHHMMSSMI, 0)+".xlsx";
		UserVo user = SessionUtil.getLoginSession();
		orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
		if(!AppConst.THEKARY_CODE.equals(user.getPartnerCode())) {
			orderModel.setPartnerCode(user.getPartnerCode());
		}

		if("GIFT".equals(orderModel.getMenuType())) {
			orderModel.setSearchGiftYn("Y");
			fileName = "giftOrder_list.xlsx";
		}
		commonService.setBatchModule("TOTAL_ORDER_EXCEL_DOWN");
		List<OrderVo> list = orderService.getOrderInfoForExcelDown(orderModel);
		List<List<Object>> valueList = new ArrayList<>();
		List<Object> headerList = new ArrayList<>();

		headerList.add("주문일");
		headerList.add("원주문번호");
		headerList.add("원주문 상세번호");
		headerList.add("주문번호");
		headerList.add("품목별 주문번호");
		headerList.add("상태코드");
		headerList.add("상태명");
		headerList.add("회원등급");
		headerList.add("구매자ID");
		headerList.add("구매자명");
		headerList.add("수령자명");

		headerList.add("카테고리1");
		headerList.add("카테고리2");
		headerList.add("거래형태");

		headerList.add("공급사");
		headerList.add("브랜드명");
		headerList.add("상품자체코드");
		headerList.add("상품코드");
		headerList.add("상품명");
		headerList.add("옵션");
		headerList.add("");
		headerList.add("수량");
		headerList.add("옵션+판매가");
		headerList.add("할인금액");
		headerList.add("쿠폰명");

		headerList.add("쿠폰총할인금액");

		headerList.add("쿠폰사용금액(입점사 부담)");
		headerList.add("쿠폰사용금액(캐리마켓 부담)");
		headerList.add("기본수수료율");
		headerList.add("적용수수료율");
		headerList.add("수수료금액");
		headerList.add("캐리마켓 수수료매출");

		headerList.add("상품혜택명");

		headerList.add("타임세일명");
		headerList.add("타임세일 할인금액");

		headerList.add("배송비");
		headerList.add("수령인 우편번호");
		headerList.add("수령인 주소");
		headerList.add("수령인 휴대전화");
		headerList.add("배송메시지");
		headerList.add("총상품구매금액");
		headerList.add("총실결제금액");
		headerList.add("결제수단");
		headerList.add("카페24 데이터 여부");
		headerList.add("브랜드 구분");
		valueList.add(headerList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderVo vo : list) {
			List<Object> tmpList = new ArrayList<>();
			tmpList.add(sdf.format(vo.getOrderDate())); //주문일
			tmpList.add(vo.getOriginOrderNo()); //원주문번호
			tmpList.add(vo.getOriginOrderDetailNo()); //원주문 상세번호
			tmpList.add(vo.getOrderNo()); //주문번호
			tmpList.add(vo.getOrderDetailNo()); //품목별주문번호
			tmpList.add(vo.getOrderStatus()); //상태코드
			tmpList.add(vo.getOrderStatusName()); //상태명
			tmpList.add(vo.getCustomerLevel()); //회원등급
			tmpList.add(vo.getCustomerId()); //구매자ID
			tmpList.add(vo.getOrdererName()); //구매자명
			tmpList.add(vo.getRecipientName()); //수령자명

			String categoryCode = vo.getCategoryCode();
			String[] categoryNameArr = null != vo.getCategoryName() ? vo.getCategoryName().split(">") : null;
			String category1 = "";
			String category2 = "";
			if(null != categoryNameArr) {
				category1 = categoryNameArr.length > 0 ? categoryNameArr[0].trim() : "";
				category2 = categoryNameArr.length > 1 ? categoryNameArr[1].trim() : "";
			}
			tmpList.add(category1); //카테고리1
			tmpList.add(category2); //카테고리2
			tmpList.add(vo.getTransactionType()); // 거래형태

			tmpList.add(vo.getCompName()); //공급사
			tmpList.add(vo.getBrandName()); //브랜드명
			tmpList.add(vo.getPartnerProductCode()); //상품자체코드
			tmpList.add(vo.getProductCode());
			tmpList.add(vo.getProductName()); //상품명
			tmpList.add(vo.getOptionValue()); //옵션
			tmpList.add("");
			String returnExchangeStatusCode = null != vo.getReturnExchangeStatusCode() ? vo.getReturnExchangeStatusCode() : "";
			long qty = null != vo.getQuantity() ? vo.getQuantity() : 0l;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				qty = qty < 0 ? qty : -1 * qty; // 취소/반품완료 상태의 경우 수량을 음수로 표시
			}
			tmpList.add(qty);
			long calculatedPrice = null != vo.getCalculatedPrice() ? vo.getCalculatedPrice() : 0l;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				calculatedPrice = calculatedPrice < 0 ? calculatedPrice : -1 * calculatedPrice; // 취소/반품완료 상태의 경우 옵션+판매가 값을 음수로 표시
			}
			tmpList.add(calculatedPrice);
			long discountPrice = null != vo.getDiscountPrice() ? vo.getDiscountPrice().longValue() : 0l;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				discountPrice = discountPrice < 0 ? discountPrice : -1 * discountPrice; // 취소/반품완료 상태의 경우 쿠폰총할인금액 값을 음수로 표시
			}
			tmpList.add(null != vo.getDiscountPrice() ? vo.getDiscountPrice().longValue() : "0"); // 할인금액
			tmpList.add(vo.getCouponName());
			tmpList.add(discountPrice); // 쿠폰 총 할인금액

			// 쿠폰사용금액(입점사 부담) , 쿠폰사용금액(캐리마켓 부담), 기본수수료율, 적용수수료율, 수수료 금액, 캐리마켓 수수료 매출 추가
			tmpList.add(vo.getDiscountProductCost() != null ? vo.getDiscountProductCost() : "0"); // 쿠폰사용금액(입점사 부담)

			int additionalProductCost = vo.getAdditionalProductCost() != null ? vo.getAdditionalProductCost() : 0;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				// 취소/반품완료 상태의 경우 쿠폰사용금액(캐리마켓 부담) 값을 음수로 표시
				additionalProductCost = additionalProductCost < 0 ? additionalProductCost : -1 * additionalProductCost;
			}
			tmpList.add(additionalProductCost); // 쿠폰사용금액(캐리마켓 부담)
			tmpList.add(vo.getDefaultCommissionRate() != null ? vo.getDefaultCommissionRate() : "0"); // 기본수수료율
			tmpList.add(vo.getApplyCommissionRate() != null ? vo.getApplyCommissionRate() : "0"); // 적용수수료율

			int feePrice = vo.getFeePrice() != null ? vo.getFeePrice() : 0;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				// 취소/반품완료 상태의 경우 수수료금액 값을 음수로 표시
				feePrice = feePrice < 0 ? feePrice : -1 * feePrice;
			}
			tmpList.add(feePrice); // 수수료금액

			int karyFeeCommission = feePrice - additionalProductCost;
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				// 취소/반품완료 상태의 경우 캐리마켓 수수료매출 값을 음수로 표시
				karyFeeCommission = karyFeeCommission < 0 ? karyFeeCommission : -1 * karyFeeCommission;
			}
			tmpList.add(karyFeeCommission); // 캐리마켓 수수료매출

			tmpList.add(vo.getBenefitName());

			tmpList.add(vo.getTimesaleName());
			tmpList.add(vo.getCalculatedTimesalePrice());

			tmpList.add(vo.getShippingPrice());
			tmpList.add(vo.getRecipientPostCode());
			tmpList.add(vo.getRecipientAddress());
			tmpList.add(vo.getRecipientTelNo());
			tmpList.add(vo.getDeliveryRequest());

			long subTotalProductPrice = vo.getSubTotalProductPrice();
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				// 취소/반품완료 상태의 경우 총상품구매금액 값을 음수로 표시
				subTotalProductPrice = subTotalProductPrice < 0 ? subTotalProductPrice : -1 * subTotalProductPrice;
			}
			tmpList.add(subTotalProductPrice); // 총상품구매금액

			long subTotalPaymentPrice = vo.getSubTotalPaymentPrice();
			if(vo.getOrderStatus().equals("MC02") || vo.getOrderStatus().equals("MR02")) {
				// 취소/반품완료 상태의 경우 총실결제금액 값을 음수로 표시
				subTotalPaymentPrice = subTotalPaymentPrice < 0 ? subTotalPaymentPrice : -1 * subTotalPaymentPrice;
			}
			tmpList.add(subTotalPaymentPrice); // 총실결제금액

			tmpList.add(vo.getPayMethodTypeName());
			tmpList.add(vo.getMigDataYn());
			tmpList.add(vo.getBrandTypeName());
			valueList.add(tmpList);
		}
		excelService.excelDownloadWithoutTemplateForObject(response, valueList, fileName,true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**상품준비중 관리 엑셀다운로드*/
	@PostMapping(value ="/rest/order/product/prepare/excel")
	public ResponseEntity<CommonResponse> getOrderProductPrepareListExcel(
		@ModelAttribute("orderModel") OrderModel orderModel
		, HttpServletResponse response)
		throws IOException {
		UserVo user = SessionUtil.getLoginSession();
		orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
		if(!AppConst.THEKARY_CODE.equals(user.getPartnerCode())) {
			orderModel.setPartnerCode(user.getPartnerCode());
		}
		List<OrderVo> list = orderService.getOrderInfoForExcelDown(orderModel);
		List<List<String>> valueList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();
		String fileName = "";

		headerList.add("주문일");
		headerList.add("원주문번호");
		headerList.add("원주문 상세번호");
		headerList.add("주문번호");
		headerList.add("품목별 주문번호");
		headerList.add("상태코드");
		headerList.add("상태명");
		headerList.add("회원등급");
		headerList.add("구매자ID");
		headerList.add("구매자명");
		headerList.add("수령자명");
		headerList.add("공급사");
		headerList.add("브랜드명");
		headerList.add("상품자체코드");
		headerList.add("상품코드");
		headerList.add("상품명");
		headerList.add("옵션");
		headerList.add("");
		headerList.add("수량");
		headerList.add("옵션+판매가");
		headerList.add("할인금액");
		headerList.add("쿠폰명");
		headerList.add("쿠폰총할인금액");

		headerList.add("쿠폰사용금액(입점사 부담)");
		headerList.add("쿠폰사용금액(캐리마켓 부담)");
		headerList.add("기본수수료율");
		headerList.add("적용수수료율");
		headerList.add("수수료금액");
		headerList.add("캐리마켓 수수료매출");

		headerList.add("상품혜택명");
		headerList.add("배송비");
		headerList.add("수령인 우편번호");
		headerList.add("수령인 주소");
		headerList.add("수령인 휴대전화");
		headerList.add("배송메시지");
		headerList.add("총상품구매금액");
		headerList.add("총실결제금액");
		headerList.add("결제수단");
		headerList.add("카페24 데이터 여부");
		valueList.add(headerList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderVo vo : list) {
			List<String> tmpList = new ArrayList<>();
			tmpList.add(sdf.format(vo.getOrderDate()));
			tmpList.add(vo.getOriginOrderNo()); //원주문번호
			tmpList.add(vo.getOriginOrderDetailNo()); //원주문 상세번호
			tmpList.add(vo.getOrderNo());
			tmpList.add(vo.getOrderDetailNo());
			tmpList.add(vo.getOrderStatus());
			tmpList.add(vo.getOrderStatusName());
			tmpList.add(vo.getCustomerLevel());
			tmpList.add(vo.getCustomerId());
			tmpList.add(vo.getOrdererName());
			tmpList.add(vo.getRecipientName());
			tmpList.add(vo.getCompName());
			tmpList.add(vo.getBrandName());
			tmpList.add(vo.getPartnerProductCode());
			tmpList.add(vo.getProductCode());
			tmpList.add(vo.getProductName());
			tmpList.add(vo.getOptionValue());
			tmpList.add("");
			tmpList.add(String.valueOf(vo.getQuantity()));
			tmpList.add(String.valueOf(vo.getCalculatedPrice()));
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			tmpList.add(vo.getCouponName());
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			// 쿠폰사용금액(입점사 부담) , 쿠폰사용금액(캐리마켓 부담), 기본수수료율, 적용수수료율, 수수료 금액, 캐리마켓 수수료 매출 추가
			tmpList.add(vo.getDiscountProductCost() != null ? String.valueOf(vo.getDiscountProductCost()) : "0"); // 쿠폰사용금액(입점사 부담)
			tmpList.add(vo.getAdditionalProductCost() != null ? String.valueOf(vo.getAdditionalProductCost()) : "0"); // 쿠폰사용금액(캐리마켓 부담)
			tmpList.add(vo.getDefaultCommissionRate() != null ? String.valueOf(vo.getDefaultCommissionRate()) : "0"); // 기본수수료율
			tmpList.add(vo.getApplyCommissionRate() != null ? String.valueOf(vo.getApplyCommissionRate()) : "0"); // 적용수수료율
			tmpList.add(vo.getFeePrice() != null ? String.valueOf(vo.getFeePrice()) : "0"); // 수수료금액
			tmpList.add((vo.getFeePrice() != null && vo.getDiscountProductCost() != null) ? String.valueOf(vo.getFeePrice() - vo.getAdditionalProductCost()) : "0"); // 캐리마켓 수수료매출

			tmpList.add(vo.getBenefitName());
			tmpList.add(String.valueOf(vo.getShippingPrice()));
			tmpList.add(vo.getRecipientPostCode());
			tmpList.add(vo.getRecipientAddress());
			tmpList.add(vo.getRecipientTelNo());
			tmpList.add(vo.getDeliveryRequest());
			tmpList.add(String.valueOf(vo.getSubTotalProductPrice()));
			tmpList.add(String.valueOf(vo.getSubTotalPaymentPrice()));
			tmpList.add(vo.getPayMethodTypeName());
			tmpList.add(vo.getMigDataYn());
			valueList.add(tmpList);
		}

		if("MO01".equals(orderModel.getOrdStatus())) {
			fileName = "orderProductBeforeDeposit_list.xlsx";
		}else if("MO10".equals(orderModel.getOrdStatus())) {
			fileName = "orderProductHolding_list.xlsx";
		}else if("MO02".equals(orderModel.getOrdStatus())) {
			fileName = "orderProductPrepare_list.xlsx";
		}

		excelService.excelDownloadWithoutTemplate(response, valueList, fileName,true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**배송준비중 관리 엑셀다운로드*/
	@PostMapping(value ="/rest/order/shipping/prepare/excel")
	public ResponseEntity<CommonResponse> getOrderShippingPrepareListExcel(
		@ModelAttribute("orderModel") OrderModel orderModel
		, HttpServletResponse response)
		throws IOException {
		UserVo user = SessionUtil.getLoginSession();
		orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
		if(!AppConst.THEKARY_CODE.equals(user.getPartnerCode())) {
			orderModel.setPartnerCode(user.getPartnerCode());
		}
		List<OrderVo> list = orderService.getOrderInfoForExcelDown(orderModel);
		List<List<String>> valueList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();

		headerList.add("주문일");
		headerList.add("원주문번호");
		headerList.add("원주문 상세번호");
		headerList.add("주문번호");
		headerList.add("품목별 주문번호");
		headerList.add("상태코드");
		headerList.add("상태명");
		headerList.add("회원등급");
		headerList.add("구매자ID");
		headerList.add("구매자명");
		headerList.add("수령자명");
		headerList.add("공급사");
		headerList.add("브랜드명");
		headerList.add("상품자체코드");
		headerList.add("상품코드");
		headerList.add("상품명");
		headerList.add("옵션");
		headerList.add("택배사");
		headerList.add("운송장번호");
		headerList.add("");
		headerList.add("수량");
		headerList.add("옵션+판매가");
		headerList.add("할인금액");
		headerList.add("쿠폰명");
		headerList.add("쿠폰총할인금액");

		headerList.add("쿠폰사용금액(입점사 부담)");
		headerList.add("쿠폰사용금액(캐리마켓 부담)");
		headerList.add("기본수수료율");
		headerList.add("적용수수료율");
		headerList.add("수수료금액");
		headerList.add("캐리마켓 수수료매출");

		headerList.add("상품혜택명");
		headerList.add("배송비");
		headerList.add("수령인 우편번호");
		headerList.add("수령인 주소");
		headerList.add("수령인 휴대전화");
		headerList.add("배송메시지");
		headerList.add("총상품구매금액");
		headerList.add("총실결제금액");
		headerList.add("결제수단");
		headerList.add("카페24 데이터 여부");
		valueList.add(headerList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderVo vo : list) {
			List<String> tmpList = new ArrayList<>();
			tmpList.add(sdf.format(vo.getOrderDate()));
			tmpList.add(vo.getOriginOrderNo()); //원주문번호
			tmpList.add(vo.getOriginOrderDetailNo()); //원주문 상세번호
			tmpList.add(vo.getOrderNo());
			tmpList.add(vo.getOrderDetailNo());
			tmpList.add(vo.getOrderStatus());
			tmpList.add(vo.getOrderStatusName());
			tmpList.add(vo.getCustomerLevel());
			tmpList.add(vo.getCustomerId());
			tmpList.add(vo.getOrdererName());
			tmpList.add(vo.getRecipientName());
			tmpList.add(vo.getCompName());
			tmpList.add(vo.getBrandName());
			tmpList.add(vo.getPartnerProductCode());
			tmpList.add(vo.getProductCode());
			tmpList.add(vo.getProductName());
			tmpList.add(vo.getOptionValue());
			tmpList.add("");
			tmpList.add("");
			tmpList.add("");
			tmpList.add(String.valueOf(vo.getQuantity()));
			tmpList.add(String.valueOf(vo.getCalculatedPrice()));
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			tmpList.add(vo.getCouponName());
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			// 쿠폰사용금액(입점사 부담) , 쿠폰사용금액(캐리마켓 부담), 기본수수료율, 적용수수료율, 수수료 금액, 캐리마켓 수수료 매출 추가
			tmpList.add(vo.getDiscountProductCost() != null ? String.valueOf(vo.getDiscountProductCost()) : "0"); // 쿠폰사용금액(입점사 부담)
			tmpList.add(vo.getAdditionalProductCost() != null ? String.valueOf(vo.getAdditionalProductCost()) : "0"); // 쿠폰사용금액(캐리마켓 부담)
			tmpList.add(vo.getDefaultCommissionRate() != null ? String.valueOf(vo.getDefaultCommissionRate()) : "0"); // 기본수수료율
			tmpList.add(vo.getApplyCommissionRate() != null ? String.valueOf(vo.getApplyCommissionRate()) : "0"); // 적용수수료율
			tmpList.add(vo.getFeePrice() != null ? String.valueOf(vo.getFeePrice()) : "0"); // 수수료금액
			tmpList.add((vo.getFeePrice() != null && vo.getDiscountProductCost() != null) ? String.valueOf(vo.getFeePrice() - vo.getAdditionalProductCost()) : "0"); // 캐리마켓 수수료매출
			tmpList.add(vo.getBenefitName());
			tmpList.add(String.valueOf(vo.getShippingPrice()));
			tmpList.add(vo.getRecipientPostCode());
			tmpList.add(vo.getRecipientAddress());
			tmpList.add(vo.getRecipientTelNo());
			tmpList.add(vo.getDeliveryRequest());
			tmpList.add(String.valueOf(vo.getSubTotalProductPrice()));
			tmpList.add(String.valueOf(vo.getSubTotalPaymentPrice()));
			tmpList.add(vo.getPayMethodTypeName());
			tmpList.add(vo.getMigDataYn());
			valueList.add(tmpList);
		}
		excelService.excelDownloadWithoutTemplate(response, valueList, "shippingPrepare_list.xlsx",true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**배송중 관리 엑셀다운로드*/
	@PostMapping(value ="/rest/order/shipping/begin/excel")
	public ResponseEntity<CommonResponse> getOrderShippingBeginListExcel(
		@ModelAttribute("orderModel") OrderModel orderModel
		, HttpServletResponse response)
		throws IOException {
		// 배송중
		String defaultOrdStatus = "MO04";
		orderModel.setOrdStatus(defaultOrdStatus);
		UserVo user = SessionUtil.getLoginSession();
		orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
		if(!AppConst.THEKARY_CODE.equals(user.getPartnerCode())) {
			orderModel.setPartnerCode(user.getPartnerCode());
		}
		List<OrderVo> list = orderService.getOrderInfoForExcelDown(orderModel);
		List<List<String>> valueList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();

		headerList.add("주문일");
		headerList.add("원주문번호");
		headerList.add("원주문 상세번호");
		headerList.add("주문번호");
		headerList.add("품목별 주문번호");
		headerList.add("상태코드");
		headerList.add("상태명");
		headerList.add("회원등급");
		headerList.add("구매자ID");
		headerList.add("구매자명");
		headerList.add("수령자명");
		headerList.add("공급사");
		headerList.add("브랜드명");
		headerList.add("상품자체코드");
		headerList.add("상품코드");
		headerList.add("상품명");
		headerList.add("옵션");
		headerList.add("택배사");
		headerList.add("운송장번호");
		headerList.add("운송장입력일");
		headerList.add("");
		headerList.add("수량");
		headerList.add("옵션+판매가");
		headerList.add("할인금액");
		headerList.add("쿠폰명");
		headerList.add("쿠폰총할인금액");

		headerList.add("쿠폰사용금액(입점사 부담)");
		headerList.add("쿠폰사용금액(캐리마켓 부담)");
		headerList.add("기본수수료율");
		headerList.add("적용수수료율");
		headerList.add("수수료금액");
		headerList.add("캐리마켓 수수료매출");

		headerList.add("상품혜택명");
		headerList.add("배송비");
		headerList.add("수령인 우편번호");
		headerList.add("수령인 주소");
		headerList.add("수령인 휴대전화");
		headerList.add("배송메시지");
		headerList.add("총상품구매금액");
		headerList.add("총실결제금액");
		headerList.add("결제수단");
		headerList.add("카페24 데이터 여부");
		valueList.add(headerList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderVo vo : list) {
			List<String> tmpList = new ArrayList<>();
			tmpList.add(sdf.format(vo.getOrderDate()));
			tmpList.add(vo.getOriginOrderNo()); //원주문번호
			tmpList.add(vo.getOriginOrderDetailNo()); //원주문 상세번호
			tmpList.add(vo.getOrderNo());
			tmpList.add(vo.getOrderDetailNo());
			tmpList.add(vo.getOrderStatus());
			tmpList.add(vo.getOrderStatusName());
			tmpList.add(vo.getCustomerLevel());
			tmpList.add(vo.getCustomerId());
			tmpList.add(vo.getOrdererName());
			tmpList.add(vo.getRecipientName());
			tmpList.add(vo.getCompName());
			tmpList.add(vo.getBrandName());
			tmpList.add(vo.getPartnerProductCode());
			tmpList.add(vo.getProductCode());
			tmpList.add(vo.getProductName());
			tmpList.add(vo.getOptionValue());
			tmpList.add(vo.getDeliveryCompName());
			tmpList.add(vo.getDeliveryTrackingNo());
			tmpList.add(vo.getDeliveryCreateDate() != null ? sdf.format(vo.getDeliveryCreateDate()) : "");
			tmpList.add("");
			tmpList.add(String.valueOf(vo.getQuantity()));
			tmpList.add(String.valueOf(vo.getCalculatedPrice()));
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			tmpList.add(vo.getCouponName());
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");

			// 쿠폰사용금액(입점사 부담) , 쿠폰사용금액(캐리마켓 부담), 기본수수료율, 적용수수료율, 수수료 금액, 캐리마켓 수수료 매출 추가
			tmpList.add(vo.getDiscountProductCost() != null ? String.valueOf(vo.getDiscountProductCost()) : "0"); // 쿠폰사용금액(입점사 부담)
			tmpList.add(vo.getAdditionalProductCost() != null ? String.valueOf(vo.getAdditionalProductCost()) : "0"); // 쿠폰사용금액(캐리마켓 부담)
			tmpList.add(vo.getDefaultCommissionRate() != null ? String.valueOf(vo.getDefaultCommissionRate()) : "0"); // 기본수수료율
			tmpList.add(vo.getApplyCommissionRate() != null ? String.valueOf(vo.getApplyCommissionRate()) : "0"); // 적용수수료율
			tmpList.add(vo.getFeePrice() != null ? String.valueOf(vo.getFeePrice()) : "0"); // 수수료금액
			tmpList.add((vo.getFeePrice() != null && vo.getDiscountProductCost() != null) ? String.valueOf(vo.getFeePrice() - vo.getAdditionalProductCost()) : "0"); // 캐리마켓 수수료매출

			tmpList.add(vo.getBenefitName());
			tmpList.add(String.valueOf(vo.getShippingPrice()));
			tmpList.add(vo.getRecipientPostCode());
			tmpList.add(vo.getRecipientAddress());
			tmpList.add(vo.getRecipientTelNo());
			tmpList.add(vo.getDeliveryRequest());
			tmpList.add(String.valueOf(vo.getSubTotalProductPrice()));
			tmpList.add(String.valueOf(vo.getSubTotalPaymentPrice()));
			tmpList.add(vo.getPayMethodTypeName());
			tmpList.add(vo.getMigDataYn());
			valueList.add(tmpList);
		}
		excelService.excelDownloadWithoutTemplate(response, valueList, "shippingBegin_list.xlsx",true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**배송완료조회 엑셀다운로드*/
	@PostMapping(value ="/rest/order/shipping/complete/excel")
	public ResponseEntity<CommonResponse> getOrderShippingCompleteListExcel(
		@ModelAttribute("orderModel") OrderModel orderModel
		, HttpServletResponse response)
		throws IOException {
		// 배송완료
		String defaultOrdStatus = "MO05";
		commonService.setBatchModule("ORDER_COMPLETE_EXCEL_DOWN");
		orderModel.setOrdStatus(defaultOrdStatus);
		UserVo user = SessionUtil.getLoginSession();
		orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
		if(!AppConst.THEKARY_CODE.equals(user.getPartnerCode())) {
			orderModel.setPartnerCode(user.getPartnerCode());
		}
		List<OrderVo> list = orderService.getOrderInfoForExcelDown(orderModel);
		List<List<String>> valueList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();

		headerList.add("주문일");
		headerList.add("배송완료일");
		headerList.add("원주문번호");
		headerList.add("원주문 상세번호");
		headerList.add("주문번호");
		headerList.add("품목별 주문번호");
		headerList.add("상태코드");
		headerList.add("상태명");
		headerList.add("회원등급");
		headerList.add("구매자ID");
		headerList.add("구매자명");
		headerList.add("수령자명");
		headerList.add("공급사");
		headerList.add("브랜드명");
		headerList.add("상품자체코드");
		headerList.add("상품코드");
		headerList.add("상품명");
		headerList.add("옵션");
		headerList.add("");
		headerList.add("수량");
		headerList.add("옵션+판매가");
		headerList.add("할인금액");
		headerList.add("쿠폰명");
		headerList.add("쿠폰총할인금액");

		headerList.add("쿠폰사용금액(입점사 부담)");
		headerList.add("쿠폰사용금액(캐리마켓 부담)");
		headerList.add("기본수수료율");
		headerList.add("적용수수료율");
		headerList.add("수수료금액");
		headerList.add("캐리마켓 수수료매출");

		headerList.add("상품혜택명");
		headerList.add("배송비");
		headerList.add("수령인 우편번호");
		headerList.add("수령인 주소");
		headerList.add("수령인 휴대전화");
		headerList.add("배송메시지");
		headerList.add("총상품구매금액");
		headerList.add("총실결제금액");
		headerList.add("결제수단");
		headerList.add("카페24 데이터 여부");
		valueList.add(headerList);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderVo vo : list) {
			List<String> tmpList = new ArrayList<>();
			tmpList.add(sdf.format(vo.getOrderDate()));
			tmpList.add(vo.getDeliveryCompleteDate());
			tmpList.add(vo.getOriginOrderNo()); //원주문번호
			tmpList.add(vo.getOriginOrderDetailNo()); //원주문 상세번호
			tmpList.add(vo.getOrderNo());
			tmpList.add(vo.getOrderDetailNo());
			tmpList.add(vo.getOrderStatus());
			tmpList.add(vo.getOrderStatusName());
			tmpList.add(vo.getCustomerLevel());
			tmpList.add(vo.getCustomerId());
			tmpList.add(vo.getOrdererName());
			tmpList.add(vo.getRecipientName());
			tmpList.add(vo.getCompName());
			tmpList.add(vo.getBrandName());
			tmpList.add(vo.getPartnerProductCode());
			tmpList.add(vo.getProductCode());
			tmpList.add(vo.getProductName());
			tmpList.add(vo.getOptionValue());
			tmpList.add("");
			tmpList.add(String.valueOf(vo.getQuantity()));
			tmpList.add(String.valueOf(vo.getCalculatedPrice()));
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");
			tmpList.add(vo.getCouponName());
			tmpList.add(null != vo.getDiscountPrice() ? String.valueOf(vo.getDiscountPrice().longValue()) : "0");

			// 쿠폰사용금액(입점사 부담) , 쿠폰사용금액(캐리마켓 부담), 기본수수료율, 적용수수료율, 수수료 금액, 캐리마켓 수수료 매출 추가
			tmpList.add(vo.getDiscountProductCost() != null ? String.valueOf(vo.getDiscountProductCost()) : "0"); // 쿠폰사용금액(입점사 부담)
			tmpList.add(vo.getAdditionalProductCost() != null ? String.valueOf(vo.getAdditionalProductCost()) : "0"); // 쿠폰사용금액(캐리마켓 부담)
			tmpList.add(vo.getDefaultCommissionRate() != null ? String.valueOf(vo.getDefaultCommissionRate()) : "0"); // 기본수수료율
			tmpList.add(vo.getApplyCommissionRate() != null ? String.valueOf(vo.getApplyCommissionRate()) : "0"); // 적용수수료율
			tmpList.add(vo.getFeePrice() != null ? String.valueOf(vo.getFeePrice()) : "0"); // 수수료금액
			tmpList.add((vo.getFeePrice() != null && vo.getDiscountProductCost() != null) ? String.valueOf(vo.getFeePrice() - vo.getAdditionalProductCost()) : "0"); // 캐리마켓 수수료매출

			tmpList.add(vo.getBenefitName());
			tmpList.add(String.valueOf(vo.getShippingPrice()));
			tmpList.add(vo.getRecipientPostCode());
			tmpList.add(vo.getRecipientAddress());
			tmpList.add(vo.getRecipientTelNo());
			tmpList.add(vo.getDeliveryRequest());
			tmpList.add(String.valueOf(vo.getSubTotalProductPrice()));
			tmpList.add(String.valueOf(vo.getSubTotalPaymentPrice()));
			tmpList.add(vo.getPayMethodTypeName());
			tmpList.add(vo.getMigDataYn());
			valueList.add(tmpList);
		}
		excelService.excelDownloadWithoutTemplate(response, valueList, "shippingComplete_list.xlsx",true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}


	/**
	 * 주문취소 신청
	 */
	@PostMapping(value ="/rest/order/cancel/add-exec")
	public ResponseEntity<String> cancelAddExec( @RequestBody OrdCancelModel ordCancelModel) {
		ordCancelModel.setClientIp(AppUtil.getClientIp());
		try {
			orderCancelService.addOrderCancel(ordCancelModel);
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**주문취소 전 체크*/
	@PostMapping(value ="/rest/order/cancel/check")
	public ResponseEntity<CommonResponse> beforeCancelCheck( @RequestBody OrdCancelModel ordCancelModel) {
		try {
			return ResponseEntityUtil.success(orderCancelService.beforeCancelCheck(ordCancelModel));
		}catch (Exception e) {
			return ResponseEntityUtil.failed(e.getMessage());
		}
	}

	/**
	 * 주문옵션변경
	 */
	@PostMapping(value ="/rest/order/optionchange/modify-exec")
	public ResponseEntity<CommonResponse> optionChangeModifyExec( @RequestBody OrderModel orderModel) {
		orderService.modifyOrderOption(orderModel);
		return ResponseEntityUtil.success("SUCCESS");
	}
	/**회원관리 > 상세모달 > 주문내역 (주문/주문취소)*/
	@GetMapping(value ="/rest/order/cust/list")
	public ResponseEntity<CommonResponse> getOrderListForCustomer(
		OrderModel orderModel, @RequestParam(value = "page", defaultValue = "1") int pageNo) {
		int pageSize = 5;
		Map<String, Object> res = new HashMap<>();
		orderModel.setSearchCondition("ORDER"); // 주문 / 주문취소 내역
		List<OrderVo> orderList = orderService.getOrderListForCustomer(orderModel, pageNo, pageSize);
		if(orderList.size() > 0) {
			Pagination orderPaging = new Pagination(orderList.get(0).getTotalCount(), pageNo, pageSize);
			res.put("orderPaging", orderPaging);
		}
		res.put("orderList", orderList);
		return ResponseEntityUtil.success(res);
	}

	/**회원관리 > 상세모달 > 주문내역 (교환 / 반품 내역)*/
	@GetMapping(value ="/rest/order/return/cust/list")
	public ResponseEntity<CommonResponse> getOrderReturnListForCustomer(
		OrderModel orderModel, @RequestParam(value = "page", defaultValue = "1") int pageNo) {
		int pageSize = 5;
		Map<String, Object> res = new HashMap<>();
		orderModel.setSearchCondition("RETURN"); // 교환 / 반품 내역
		List<OrderVo> returnList = orderService.getOrderListForCustomer(orderModel, pageNo, pageSize);
		if(returnList.size() > 0) {
			Pagination returnPaging = new Pagination(returnList.get(0).getTotalCount(), pageNo, pageSize);
			res.put("returnPaging", returnPaging);
		}
		res.put("returnList", returnList);
		return ResponseEntityUtil.success(res);
	}

	/**
	 * 주문메모 등록
	 */
	@PostMapping(value ="/rest/order/memo/modify-exec")
	public ResponseEntity<CommonResponse> memoModifyExec( @RequestBody OrderModel orderModel)
		throws Exception {
		orderService.modifyOrdMemo(orderModel);

		OrdSalesHistoryMemoVo ordSalesHistoryMemoVo
			= OrdSalesHistoryMemoVo.builder()
			.orderNo(orderModel.getOrderNo())
			.historyTypeCode(historyTypeAdmin)
			.build();

		List<OrdSalesHistoryMemoVo> memoList = orderService.getOrdSalesHistoryMemoList(ordSalesHistoryMemoVo);

		return ResponseEntityUtil.success(memoList);
	}

	/**일괄 송장번호 입력 실행*/
	@PostMapping(value = "/rest/order/bulk/tracking/no/add-exec", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonResponse> bulkTrackingNoAddExec(
		@RequestPart(value="excelFile") MultipartFile excelFile)
		throws Exception {

		return ResponseEntityUtil.success(orderService.bulkTrackingNoAddExec(excelFile));
	}

	/**배송추적 (굿스플로 재조회)*/
	@PostMapping(value="/rest/order/shipping/tracking")
	public ResponseEntity<CommonResponse> shippingTracking(@RequestParam String orderDetailNo, @RequestParam String trackingNo) {
		orderService.goodsFlowShippingTracking(orderDetailNo, trackingNo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**신한은행 거래내역 엑셀다운로드*/
	@PostMapping(value ="/rest/order/shinhan/transaction/excel")
	public ResponseEntity<CommonResponse> getShinhanTransactionExcel(
			ShinhanTransactionModel model
			, HttpServletResponse response)
			throws IOException {
		List<ShinhanTransactionVo> list = orderService.getShinhanTransactionList(null, model);
		List<List<String>> valueList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();

		headerList.add("거래일자");
		headerList.add("거래점/은행명");
		headerList.add("거래메모(입금자명)");
		headerList.add("입금금액");
		headerList.add("출금금액");
		headerList.add("잔액");
		headerList.add("관리자메모");
		valueList.add(headerList);

		for(ShinhanTransactionVo vo : list) {
			List<String> tmpList = new ArrayList<>();
			tmpList.add(vo.getTransactionDate());
			tmpList.add(vo.getTransactionName());
			tmpList.add(vo.getMemo());
			tmpList.add(String.valueOf(vo.getDepositPrice()));
			tmpList.add(String.valueOf(vo.getWithdrawPrice()));
			tmpList.add(String.valueOf(vo.getRemainPrice()));
			tmpList.add(vo.getAdminMemo());
			valueList.add(tmpList);
		}
		excelService.excelDownloadWithoutTemplate(response, valueList, "신한거래내역"+ DateUtil.getDate(DateUtil.FORMAT_YYYYMMDDHHMMSSMI, 0) +".xlsx",true, "");
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**신한은행 거래내역 상세*/
	@GetMapping(value ="/rest/order/shinhan/transaction/info")
	public ResponseEntity<CommonResponse> getShinhanTransactionDetail(@RequestParam long seq) {
		return ResponseEntityUtil.success(orderService.getShinhanTransactionDetail(seq));
	}

	/**신한은행 거래내역 관리자메모 저장*/
	@PostMapping(value ="/rest/order/shinhan/transaction/adminmemo")
	public ResponseEntity<CommonResponse> modifyShinhanTransactionAdminMemo(ShinhanTransactionVo vo) {
		orderService.modifyShinhanTransactionAdminMemo(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	@PatchMapping(value="/rest/order/fnb/cancel")
	public ResponseEntity<CommonResponse> fnbOrderCancel(@RequestBody OrderModel orderModel, HttpServletRequest request
	) throws Exception {

		String orderNo = orderModel.getOrderNo();
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		orderModel.setSearchOrderNo(orderModel.getSearchOrderNo());
		String status = orderModel.getOrdStatus();
		orderModel.setOrdStatus(null);
		orderModel.setSearchOrderNo(orderModel.getOrderNo());
		List<OrderVo> detailList = orderService.getOrderDetailList(null, orderModel);
		String[] orderDetailNo = detailList.stream().map(OrderVo::getOrderDetailNo).toArray(String[]::new);

		return ResponseEntityUtil.success(kiccService.cancelOrderFnb(orderNo,ip,status,orderModel.getRegUserId(),detailList,orderDetailNo));
	}

	@PatchMapping(value="/rest/order/fnb/return")
	public ResponseEntity<CommonResponse> fnbOrderReturn(@RequestBody OrderModel orderModel, HttpServletRequest request
	) throws Exception {

		String orderNo = orderModel.getOrderNo();
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		ReturnExchangeModel returnExchangeModel = new ReturnExchangeModel();
			returnExchangeModel.setRequestType("RETURN");
			returnExchangeModel.setOrderNo(orderNo);

		returnExchangeService.fnbReturn(orderNo);

		return ResponseEntityUtil.success();
	}

	/**
	 * 일괄 굿스플로 배송추적
	 */
	@PostMapping(value="/rest/order/bulk/goodsflow/tracking")
	public ResponseEntity<String> bulkShippingTracking(
		@RequestBody OrderModel orderModel) {
		try {
			orderService.bulkGoodsFlowShippingTracking(orderModel);
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 주문 배송지 수정
	 */
	@PatchMapping(value="/rest/order/shipping/address/modify-exec", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponse> modifyOrderShippingAddress(
			@RequestBody OrderShippingAddressModifyReqDto reqDto) {
		try {
			orderService.modifyOrderShippingAddress(reqDto);
		} catch (Exception e) {
			return ResponseEntityUtil.failed(e.getMessage());
		}
		return ResponseEntityUtil.success("SUCCESS");
	}

}
