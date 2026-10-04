package com.km.adm.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.km.adm.config.AppConst;
import com.km.adm.config.AppProperties;
import com.km.adm.config.KiccProperties;
import com.km.adm.dao.BatchDao;
import com.km.adm.dao.OrderDao;
import com.km.adm.dao.ProductDao;
import com.km.adm.dao.vo.ApiAuthVo;
import com.km.adm.dao.vo.BatchLogVo;
import com.km.adm.dao.vo.CommonCodeVo;
import com.km.adm.dao.vo.CouponVo;
import com.km.adm.dao.vo.DashboardVo.DashboardOrderExchangeVo;
import com.km.adm.dao.vo.DashboardVo.DashboardOrderSalesReturnRankingVo;
import com.km.adm.dao.vo.DashboardVo.DashboardOrderSalesVo;
import com.km.adm.dao.vo.IssuedPointVo.PointUseVo;
import com.km.adm.dao.vo.KiccApprovalVo;
import com.km.adm.dao.vo.OrdGfShippingLogVo;
import com.km.adm.dao.vo.OrdGfShippingVo;
import com.km.adm.dao.vo.OrdSalesDiscountVo;
import com.km.adm.dao.vo.OrdSalesHistoryMemoVo;
import com.km.adm.dao.vo.OrdSalesVo;
import com.km.adm.dao.vo.OrdShippingVo;
import com.km.adm.dao.vo.OrderChangeVo;
import com.km.adm.dao.vo.OrderGiftMsgVo;
import com.km.adm.dao.vo.OrderKiccApprovalVo;
import com.km.adm.dao.vo.OrderSalesDetailVo;
import com.km.adm.dao.vo.OrderSalesDiscountDetailVo;
import com.km.adm.dao.vo.OrderSalesDiscountVo;
import com.km.adm.dao.vo.OrderSalesVo;
import com.km.adm.dao.vo.OrderVo;
import com.km.adm.dao.vo.PartnerShippingVo;
import com.km.adm.dao.vo.PointRewardResultVo;
import com.km.adm.dao.vo.PointVo.PointRewardVo;
import com.km.adm.dao.vo.ProductContentsVo;
import com.km.adm.dao.vo.ProductItemVo;
import com.km.adm.dao.vo.RefundAccountVo;
import com.km.adm.dao.vo.ReturnExchangeVo;
import com.km.adm.dao.vo.SettlementVo;
import com.km.adm.dao.vo.ShinhanTransactionVo;
import com.km.adm.dao.vo.TimesaleVo;
import com.km.adm.dao.vo.UserVo;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto.KmToErpNewOrderDetailReq;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto.KmToErpOrderDiscountDetailReq;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto.KmToErpOrderDiscountReq;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto.KmToErpOrderPaymentReq;
import com.km.adm.dto.erp.request.KmToErpNewOrderModReqDto.KmToErpOrderShippingReq;
import com.km.adm.dto.erp.request.OrderShippingAddressModifyReqDto;
import com.km.adm.model.KiccApprovalResModel;
import com.km.adm.model.KiccAuthenticationReqModel;
import com.km.adm.model.KiccReviseResModel;
import com.km.adm.model.OrdCancelModel;
import com.km.adm.model.OrderModel;
import com.km.adm.model.OrderTrackingNoExcelUploadResultModel;
import com.km.adm.model.ShinhanTransactionModel;
import com.km.adm.model.StatisticsModel;
import com.km.adm.util.AppConstants;
import com.km.adm.util.AppUtil;
import com.km.adm.util.DateUtil;
import com.km.adm.util.KiccUtil;
import com.km.adm.util.Pagination;
import com.km.adm.util.RestUtil;
import com.km.adm.util.SessionUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 주문 서비스
 *
 * @author smlee
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    /**
     * orderDao
     */
    private final OrderDao orderDao;

    private final KakaoService kakaoService;

    private final CustomerService customerService;

    private final CommonService commonService;

    private final ExcelService excelService;

    private final AppProperties appProperties;
    private final KiccProperties kiccProperties;

    private final BatchDao batchDao;

    private final ProductDao productDao;

    final static String historyTypeAdmin = "ADMIN_MEMO";

    /**
     * 통합주문 카운트
     */
    public int getOrderTotalCount(OrderModel orderModel) {
        return orderDao.selectOrderTotalCount(orderModel);
    }

    /**
     * 통합주문 리스트
     */
    public List<OrderVo> getOrderTotalList(Pagination pagination, OrderModel orderModel) {
        List<OrderVo> res = orderDao.selectOrderTotalList(pagination, orderModel);
        for (OrderVo vo : res) {
            vo.setPaymentYn("Y".equals(vo.getPaymentYn()) ? "결제완료" : "미결제");
        }
        return res;
    }
    /**
     * 통합 주문 상세 카운트
     */
    public int getOrderDetailTotalCount(OrderModel orderModel) {
        return orderDao.selectOrderDetailTotalCount(orderModel);
    }

    /**
     * 통합 주문 상세 리스트
     */
    public List<OrderVo> getOrderDetailTotalList(Pagination pagination, OrderModel orderModel) {
        List<OrderVo> res = orderDao.selectOrderDetailTotalList(pagination, orderModel);
        for (OrderVo vo : res) {
            vo.setPaymentYn("Y".equals(vo.getPaymentYn()) ? "결제완료" : "미결제");
        }
        return res;
    }

    /**
     * 상품준비주문 카운트
     */
    public int getProductPrepareCount(OrderModel orderModel) {
        return orderDao.selectProductPrepareCount(orderModel);
    }

    /**
     * 상품준비주문 리스트
     */
    public List<OrderVo> getProductPrepareList(Pagination pagination, OrderModel orderModel) {
        return orderDao.selectProductPrepareList(pagination, orderModel);
    }

    /**
     * 주문상세 카운트
     */
    public int getOrderDetailCount(OrderModel orderModel) {
        return orderDao.selectOrderDetailCount(orderModel);
    }

    /**
     * 주문상세 리스트
     */
    public List<OrderVo> getOrderDetailList(Pagination pagination, OrderModel orderModel) {
        orderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
        return getOrderDetailList(pagination, orderModel, 0);
    }
    public List<OrderVo> getOrderDetailList(Pagination pagination, OrderModel orderModel, int lateDay) {

        List<OrderVo> res = null;
        // 스마트 오더는 이걸로 가져오기
        if("PU".equals(orderModel.getOrderType())){
            res = orderDao.selectOrderFnbDetailList(pagination, orderModel);
        } else {
            res = orderDao.selectOrderDetailList(pagination, orderModel);
        }
        for (OrderVo vo : res) {
            vo.setPaymentYn("Y".equals(vo.getPaymentYn()) ? "결제완료" : "미결제");

            // 배송시작일 세팅
            /*OrdGfShippingLogVo statusDateVo = orderDao.selectGfStatusDateTime("DLV_START", vo.getOrderNo(), vo.getOrderDetailNo());
            if(null != statusDateVo) {
                vo.setDlvStartDate(statusDateVo.getStatusDateTime());
            }else {
                vo.setDlvStartDate("배송시작 전");
            }*/

            // 주문번호로 굿스플로 로그 조회
            // MO04 배송중이고
            // status_date_time + lateDay가 현재시간(SYSDATE)보다 작거나 같고
            // status_date_time가 DLV_START : 배송출발의 status_date_time과  status_date_time + lateDay 사이의 목록
            /*List<OrdGfShippingLogVo> gfLogList = orderDao.selectLateDeliveryProcess(vo.getOrderNo(), lateDay);
            int completeCnt = 0;
            for(OrdGfShippingLogVo logVo : gfLogList) {
                if("COMPLETED".equals(logVo.getGfOrdStatus())) {
                    completeCnt++;
                }
            }
            if(gfLogList.size() > 0) {
                if(completeCnt > 0) {
                    vo.setIsDeliveryLate("N");
                }else {
                    // COMPLETED가 없으면 배송지연으로 판단
                    vo.setIsDeliveryLate("Y");
                }
            }else {
                // 목록에 없으면 정상배송
                vo.setIsDeliveryLate("N");
            }*/
        }
        return res;
    }

    /**
     * 주문상태 변경
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderStatus(OrderModel orderModel) throws Exception {
        List<OrderVo> ordDetailList = this.getOrderDetailList(null, orderModel);
        for(OrderVo order : ordDetailList) {
            if("MC02".equals(order.getOrderStatus())) {
                throw new Exception("이미 취소처리된 주문이 있습니다.");
            }
        }

        /**KICC취소금액과 주문상세의 취소금액에 차이가 있는지 조회*/
        String[] arrOrderNo = orderModel.getArrOrderNo();
        for(String orderNo : arrOrderNo) {
            int cancelDiffrence = orderDao.selectCancelOrderDifference(orderNo);
            if(cancelDiffrence > 0) {
                throw new Exception("KICC취소금액과 주문상세의 취소금액이 일치하지 않습니다.");
            }
        }

        String changeStatusCode = "";
        String orderStatus = orderModel.getCurrentStatusCode();
        int orderStatusNum = Integer.parseInt(orderStatus.substring(3, 4));

        UserVo user = SessionUtil.getLoginSession();
        if(null == orderModel.getUserId() || "".equals(orderModel.getUserId())) {
            orderModel.setUserId(user.getUserId());
        }

        try {
            if ("N".equals(orderModel.getChangeStatusType())) {
                // 다음 상태로 변경
                if (orderStatusNum == 5) {
                    // 배송완료의 다음 상태는 없다.
                    throw new Exception("현 주문은 배송완료 상태입니다.");
                } else {
                    changeStatusCode =
                        orderStatus.substring(0, 3) + (orderStatusNum + 1);
                }
            } else if ("B".equals(orderModel.getChangeStatusType())) {
                // 이전 상태로 변경
                if (orderStatusNum == 2) {
                    // 주문완료-결제완료의 이전 상태는 없다.
                    throw new Exception("현재 주문은 상품준비중 상태입니다.");
                } else {
                    changeStatusCode =
                        orderStatus.substring(0, 3) + Integer.toString((orderStatusNum - 1));
                }
            } else if ("HE".equals(orderModel.getChangeStatusType())) { // 배송보류로 변경 (배송보류는 상품준비중에만 가능)
                // 이전 상태로 변경
                if (orderStatusNum != 2) {
                    // 주문완료-결제완료의 이전 상태는 없다.
                    throw new Exception("배송보류는 상품중비중 상태에서만 가능합니다.");
                } else {
                    changeStatusCode = "MO10";
                }
            } else if ("HC".equals(orderModel.getChangeStatusType())) { // 배송보류 해제로 변경 (배송보류 해제는 배송보류 상태에서만 가능)
                // 이전 상태로 변경
                if (!"MO10".equals(orderStatus)) {
                    // 배송보류 상태에서만 가능
                    throw new Exception("배송보류 해제는 배송보류 상태에서만 가능합니다.");
                } else {
                    changeStatusCode = "MO02";
                }
            } else if ("HN".equals(orderModel.getChangeStatusType())) { // 배송보류 해제로 변경 (배송보류 해제는 배송보류 상태에서만 가능)
                // 이전 상태로 변경
                if (!"MO10".equals(orderStatus)) {
                    // 배송보류 상태에서만 가능
                    throw new Exception("배송보류 해제 및 배송준비중 상태변경은 배송보류 상태에서만 가능합니다.");
                } else {
                    changeStatusCode = "MO03";
                }
            }

            if("B".equals(orderModel.getChangeStatusType()) && "VENDOR".equals(user.getUserGroupCode()) && !"MO02".equals(changeStatusCode)) {
                // 입점사계정이면 배송상태를 돌릴수없게
                throw new Exception("권한이 없습니다.");
            }

            if("MO05".equals(changeStatusCode) && "VENDOR".equals(user.getUserGroupCode())) {
                // 입점사계정이면 배송완료상태로 만들지 못하게
                throw new Exception("권한이 없습니다.");
            }

            /*
            // 임시 주석처리
            if("THEKARY".equals(user.getPartnerCode())) {
                if ("MO03".equals(changeStatusCode)) {
                    throw new Exception("더캐리 상품은 ERP에서 배송준비중으로 변경 가능합니다.");
                }
            }

             */

            if(!"THEKARY".equals(user.getPartnerCode())) {
                for(String partnerCode : orderModel.getArrPartnerCode()) {
                    if (partnerCode != null && !user.getPartnerCode().equals(partnerCode)) {
                        throw new Exception("상태를 변경할 수 없는 상품이 있습니다.");
                    }
                }
            }

            orderModel.setChangeOrderStatus(changeStatusCode);
            orderDao.updateOrderStatus(orderModel);

            List<CommonCodeVo> commonList = commonService.getCommonCodeList("ORD_STATUS");
            String changeStatusName = "";
            String currentStatusName = "";

            for (CommonCodeVo commonCodeVo : commonList) {
                if (changeStatusCode.equals(commonCodeVo.getCode())) {
                    changeStatusName = commonCodeVo.getValue();
                }
                if (orderStatus.equals(commonCodeVo.getCode())) {
                    currentStatusName = commonCodeVo.getValue();
                }
            }

            // 변경 이력저장
            String historyTypeCode = "STATUS_CHANGE";

            OrdSalesHistoryMemoVo ordSalesHistoryMemoVo =
                OrdSalesHistoryMemoVo.builder()
                    .orderNo(orderModel.getOrderNo())
                    .orderDetailNo(orderModel.getOrderDetailNo())
                    .historyTypeCode(historyTypeCode)
                    .oldOrderStatus(orderStatus)
                    .newOrderStatus(changeStatusCode)
                    .remark("주문상태 변경 : " + currentStatusName + "(" + orderStatus + ") -> " + changeStatusName + "(" + changeStatusCode + ")")
                    .arrOrderDetailNo(orderModel.getArrOrderDetailNo())
                    .build();

            if("BATCH".equals(orderModel.getUserId())) {
                ordSalesHistoryMemoVo =
                    OrdSalesHistoryMemoVo.builder()
                        .orderNo(orderModel.getOrderNo())
                        .orderDetailNo(orderModel.getOrderDetailNo())
                        .historyTypeCode(historyTypeCode)
                        .oldOrderStatus(orderStatus)
                        .newOrderStatus(changeStatusCode)
                        .remark("주문상태 변경 : " + currentStatusName + "(" + orderStatus + ") -> " + changeStatusName + "(" + changeStatusCode + ")")
                        .arrOrderDetailNo(orderModel.getArrOrderDetailNo())
                        .regUserId(orderModel.getRegUserId())
                        .build();
            }

            this.addOrdSalesHistoryMemoList(ordSalesHistoryMemoVo);

            if (changeStatusCode.equals("MO03")) {
                orderDao.updateMainOrderStatus(orderModel);

                if ("B".equals(orderModel.getChangeStatusType())) {
                    // 운송장번호 초기화
                    orderDao.deleteOrdShipping(orderModel);
                }
            }
            if (changeStatusCode.equals("MO02")) {
                orderDao.updateMainOrderStatusReset(orderModel);
            }

            if (changeStatusCode.equals("MO05")) {
                // 주문완료면 complete_date 업데이트
                orderDao.updateOrderShipping(orderModel);

                // 주문완료면 정산일 업데이트
                orderDao.updateSettlementDate(orderModel);
            }

            if(null != orderModel.getArrOrderNo() && orderModel.getArrOrderNo().length > 0) {
                for(String orderNo : orderModel.getArrOrderNo()) {
                    // 처리해야할 주문건수
                    int orderCnt = orderDao.selectNotCompleteOrderCount(orderNo);

                    // 주문완료이면서 해당 주문에 더이상 처리해야할 주문이 없는 경우 주문 마스터 완료처리
                    if (changeStatusCode.equals("MO05") && orderCnt == 0) {
                        OrderSalesVo orderSalesVo
                            = OrderSalesVo.builder()
                            .orderNo(orderNo)
                            .orderMainStatus("MO05")
                            .regUserId(orderModel.getUserId())
                            .build();
                        this.modifyOrdSalesStatus(orderSalesVo);

                        // 대기포인트 지급
                        orderDao.updatePointWait(orderNo);
                        List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
                        commonService.kmToErpSend("PATCH"
                            ,"/api/v1/km-to-erp/point/list/modify-exec"
                            ,AppUtil.convertObjectToJson(pointRewardList));
                    }

                    // 배송프로세스 진행중이면서 처리해야할 주문이 있는 경우 주문 마스터 진행처리
                    if (!changeStatusCode.equals("MO02") && !changeStatusCode.equals("MO05") && orderCnt > 0) {
                        OrderSalesVo orderSalesVo
                            = OrderSalesVo.builder()
                            .orderNo(orderNo)
                            .orderMainStatus("MO03")
                            .regUserId(orderModel.getUserId())
                            .build();
                        this.modifyOrdSalesStatus(orderSalesVo);
                    }
                }
            }

            // 배송완료후 리뷰작성 독려 알림톡
            if (changeStatusCode.equals("MO05")) {
                List<OrderVo> list = orderDao.selectOrderDetailList(null, orderModel);
                for (OrderVo order : list) {
                    if("SO".equals(order.getOrderType()) || "SR".equals(order.getOrderType())) {
                        customerService.reviewWriteKkoMsg(
                            order.getOrdererName()
                            , order.getOrdererPhone()
                            , order.getProductCode()
                            , order.getOrderDetailNo()
                            , order.getItemCode());
                    }
                }
            }


            List<KmToErpNewOrderModReqDto> dto = this.getKmToErpNewOrderModReqDtoByOrderDetailNoList(
                orderModel.getArrOrderDetailNo());

            // 더캐리 상품만 ERP에 전송
            if (dto != null && !dto.isEmpty()) {
                for (KmToErpNewOrderModReqDto tmp : dto) {
                    tmp.setOrderMainStatus(changeStatusCode);
                }
                // 결제승인 후 ERP 전송
                String apiUrl = appProperties.getKmToErp().getApiUrl();
                ApiAuthVo apiAuth = commonService.getApiAuthByApiTypeCode("KM-TO-ERP");

                //API 호출 후 등록
                String json = AppUtil.convertObjectToJson(dto);

                // 헤더 등록
                Map<String, String> header = new HashMap<>();
                header.put("id-key", apiAuth.getIdKey());
                header.put("secret-key", apiAuth.getSecretKey());

                // 헤더와 함께 POST API 호출
                String response = RestUtil.postApi(MediaType.APPLICATION_JSON
                    , apiUrl + "/api/v1/km-to-erp/order/list/modify-exec",
                    json, header);

                if (AppConstants.MODE_ERROR.equals(response)) {
                    throw new Exception("주문상태 변경 실패, ERP에서 변경 가능");
                }
                log.info("[KM -> ERP 주문 상태 변경 결과] : {}", response);
            }
        } catch (Exception e) {
            log.error("[주문상태 변경 서비스 Error]", e);

            throw e;
        }
    }


    /**
     * 운송장 저장
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addOrderShipping(OrderModel orderModel) throws Exception {
        UserVo user = SessionUtil.getLoginSession();
        if (user != null) {
            orderModel.setUserId(user.getUserId());
        }

        try {
            String itemName = "캐리마켓 상품";
            if (orderModel.getOrdShippingVoList() != null
                && !orderModel.getOrdShippingVoList().isEmpty()) {
                List<Long> seqList = commonService.getSeqList(
                    "ORD_SHIPPING", orderModel.getOrdShippingVoList().size());
                int seqCnt = 0;
                for (OrdShippingVo vo : orderModel.getOrdShippingVoList()) {
                    vo.setShippingSeq(seqList.get(seqCnt++));
                }
                orderDao.insertOrderShipping(orderModel);
            }
            OrderVo orderInfo = null;
            String orderNo = null;
            String shippingNo = null;
            String deliveryCompCode = null;
            // 배송정보(good flow) 가져오기
            for (OrdShippingVo shipping : orderModel.getOrdShippingVoList()) {
                orderInfo = orderDao.selectOrderDetailInfoForKakao(shipping.getOrderDetailNo());
                orderNo = orderInfo.getOrderNo();
                if(null != orderInfo.getOriginOrderNo()) {
                    orderNo = orderInfo.getOriginOrderNo();
                }
                shippingNo = shipping.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("   ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n","");
                deliveryCompCode = shipping.getDeliveryCompCode();
                if(!"DC17".equals(shipping.getDeliveryCompCode())) { // 업체직송이 아닌경우 굿스플로 로직 진행
                    OrdGfShippingVo ordGfShippingVo = orderDao.selectOrdGfShipping(null, shippingNo);

                    // 배송정보(good flow)가 없을때 good flow 프로세스 진행
                    if (ordGfShippingVo == null) {
                        Map<String, String> queryStringMap = new HashMap<>();
                        CommonCodeVo srchCodeVo = new CommonCodeVo();
                        srchCodeVo.setGroupCode("DELIVERY_COMP");
                        srchCodeVo.setCode(shipping.getDeliveryCompCode());
                        List<CommonCodeVo> codeList = commonService.getCommonCodeList2(srchCodeVo);
                        if (codeList.size() > 0) {
                            CommonCodeVo code = codeList.get(0);
                            List<String> retExcCodeList = new ArrayList<>();
                            retExcCodeList.add("RN09");
                            retExcCodeList.add("EH09");
                            retExcCodeList.add("RN99");
                            retExcCodeList.add("EH99");
                            OrderSalesDetailVo detailVo = orderDao.selectSalesDetailByPk(null, shipping.getOrderDetailNo(), retExcCodeList);
                            Map<String, String> header = new HashMap<>();
                            header.put("accept", "application/json");
                            header.put("Authorization", appProperties.getGoodsFlow().getApiKey());

                            JsonObject req = new JsonObject();
                            req.addProperty("requestId", detailVo.getOrderNo());
                            JsonObject req2 = new JsonObject();
                            JsonArray reqArr = new JsonArray();
                            //req2.addProperty("uniqueId", detailVo.getOrderDetailNo());
                            req2.addProperty("transporter", code.getAttribute());
                            req2.addProperty("invoiceNo", shippingNo);
                            req2.addProperty("itemName", itemName);
                            reqArr.add(req2);
                            req.add("items", reqArr);

                            // 굿스플로 API 호출 (운송장번호 등록)
                            String response = RestUtil.postApi(MediaType.APPLICATION_JSON
                                , appProperties.getGoodsFlow().getApiUrl() + "/api/deliveries/tracking"
                                , req.toString(), header);

                            JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();
                            boolean isSuccess = jObject.get("success").getAsBoolean();

                            // api 통신여부
                            String serviceId = "";
                            if (!isSuccess) {
                                throw new Exception("배송정보 등록 실패");
                            } else {
                                for(int i = 0; i < jObject.get("data").getAsJsonObject().get("items").getAsJsonArray().size(); i++) {
                                    boolean dataIsSuccess = jObject.get("data").getAsJsonObject()
                                        .get("items").getAsJsonArray()
                                        .get(i).getAsJsonObject()
                                        .get("success").getAsBoolean();
                                    if (!dataIsSuccess) {
                                        String message = jObject.get("data").getAsJsonObject().get("items").getAsJsonArray()
                                            .get(i).getAsJsonObject()
                                            .get("error").getAsJsonObject()
                                            .get("message").getAsString();
                                        if (message != null && message.contains("기존 등록된 운송장번호")) {
                                            continue;
                                        } else {
                                            throw new Exception(message);
                                        }
                                    }
                                    serviceId = jObject.get("data").getAsJsonObject()
                                        .get("items").getAsJsonArray()
                                        .get(i).getAsJsonObject()
                                        .get("data").getAsJsonObject()
                                        .get("serviceId").getAsString();
                                }
                            }

                            // 굿스플로 배송정보 저장
                            OrdGfShippingVo gfVo = new OrdGfShippingVo();
                            gfVo.setOrderDetailNo(shipping.getOrderDetailNo());
                            gfVo.setGfDeliveryComp(code.getAttribute());
                            gfVo.setDeliveryTrackingNo(shippingNo);
                            gfVo.setGfServiceId(serviceId);
                            gfVo.setGfOrdStatus("IN_TRANSIT");
                            gfVo.setRegUserId(user.getUserId());
                            if (!serviceId.isEmpty()) {
                                orderDao.insertOrdGfShipping(gfVo);
                            }

                            // 현재시간
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date time = new Date();

                            // 굿스플로 로그 저장
                            OrdGfShippingLogVo gfLogVo
                                = OrdGfShippingLogVo.builder()
                                .orderDetailNo(gfVo.getOrderDetailNo())
                                .gfDeliveryComp(gfVo.getGfDeliveryComp())
                                .deliveryTrackingNo(gfVo.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("    ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""))
                                .gfServiceId(gfVo.getGfServiceId())
                                .statusDateTime(format.format(time))
                                .gfOrdStatus("DLV_FAILED")
                                .build();
                            if (!serviceId.isEmpty()) {
                                orderDao.insertOrdGfShippingLog(gfLogVo);
                            }
                        }
                    } else {
                        // 굿스플로 배송정보 저장
                        OrdGfShippingVo gfVo = new OrdGfShippingVo();
                        gfVo.setOrderDetailNo(shipping.getOrderDetailNo());
                        gfVo.setGfDeliveryComp(ordGfShippingVo.getGfDeliveryComp());
                        gfVo.setDeliveryTrackingNo(shippingNo);
                        gfVo.setGfServiceId(ordGfShippingVo.getGfServiceId());
                        gfVo.setGfOrdStatus(ordGfShippingVo.getGfOrdStatus());
                        gfVo.setRegUserId(user.getUserId());
                        orderDao.insertOrdGfShipping(gfVo);
                    }

                    // 배송중 알림톡 전송
                    CommonCodeVo srchCodeVo = new CommonCodeVo();
                    srchCodeVo.setGroupCode("DELIVERY_COMP");
                    srchCodeVo.setCode(shipping.getDeliveryCompCode());
                    List<CommonCodeVo> codeList = commonService.getCommonCodeList2(srchCodeVo);

                    kakaoService.startDeliveryKkoMsg(
                        orderInfo.getRecipientName()
                        , orderInfo.getRecipientTelNo()
                        , orderInfo.getOrderNo()
                        , orderInfo.getProductName()
                        , codeList.get(0).getValue()
                        , shipping.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""));

                    // 에스크로 주문건은 상태변경처리
                    String reviseTypeCode = null;
                    String reviseSubTypeCode = null;
                    OrderKiccApprovalVo orderKiccApprovalVo = this.getKiccApprovalInfo(orderNo);
                    if(null != orderKiccApprovalVo) {
                        if("ES01".equals(orderKiccApprovalVo.getStatusCode())) {
                            // 에스크로 계좌이체 주문건 취소시
                            reviseTypeCode = "61";
                            reviseSubTypeCode = "ES07"; // 배송중 상태변경

                        }

                        if("ES03".equals(orderKiccApprovalVo.getStatusCode())) {
                            // 에스크로 무통장입금 주문건 취소시
                            reviseTypeCode = "61";
                            reviseSubTypeCode = "ES07"; // 배송중 상태변경
                        }

                        if("61".equals(reviseTypeCode)) {
                            KiccAuthenticationReqModel kiccAuthenticationReqModel = new KiccAuthenticationReqModel();
                            kiccAuthenticationReqModel.setPgCno(orderKiccApprovalVo.getPgCno());
                            kiccAuthenticationReqModel.setClientIp(AppUtil.getClientIp());
                            kiccAuthenticationReqModel.setReviseTypeCode(reviseTypeCode);
                            kiccAuthenticationReqModel.setReviseSubTypeCode(reviseSubTypeCode);
                            kiccAuthenticationReqModel.setShopOrderNo(orderKiccApprovalVo.getShopOrderNo());

                            if("DC04".equals(deliveryCompCode)) {
                                deliveryCompCode = "DC05";
                            }else if("DC01".equals(deliveryCompCode)) {
                                deliveryCompCode = "DC01";
                            }else if("DC02".equals(deliveryCompCode)) {
                                deliveryCompCode = "DC07";
                            }else if("DC03".equals(deliveryCompCode)) {
                                deliveryCompCode = "DC08";
                            }else {
                                deliveryCompCode = "DC13";
                            }
                            kiccAuthenticationReqModel.setDeliveryCompCode(deliveryCompCode);
                            kiccAuthenticationReqModel.setShippingNo(shippingNo);
                            this.kiccReviseForEscrow(kiccAuthenticationReqModel);
                        }
                    } // orderKiccApprovalVo
                }
            }
            if (user != null && "THEKARY".equals(user.getPartnerCode())) {
                String[] arrOrderDetailNo = new String[orderModel.getOrdShippingVoList().size()];
                int i = 0;
                for(OrdShippingVo shipping : orderModel.getOrdShippingVoList()) {
                    arrOrderDetailNo[i++] = shipping.getOrderDetailNo();
                }
                List<KmToErpNewOrderModReqDto> dto = this.getKmToErpNewOrderModReqDtoByOrderDetailNoList(
                    arrOrderDetailNo);


                // 더캐리 상품만 ERP에 전송
                if (dto != null && !dto.isEmpty()) {
                    // 운송장 등록 후 ERP 전송
                    String apiUrl = appProperties.getKmToErp().getApiUrl();
                    ApiAuthVo apiAuth = commonService.getApiAuthByApiTypeCode("KM-TO-ERP");

                    //API 호출 후 등록
                    String json = AppUtil.convertObjectToJson(dto);

                    // 헤더 등록
                    Map<String, String> header = new HashMap<>();
                    header.put("id-key", apiAuth.getIdKey());
                    header.put("secret-key", apiAuth.getSecretKey());

                    // 헤더와 함께 POST API 호출
                    String response = RestUtil.postApi(MediaType.APPLICATION_JSON
                        , apiUrl + "/api/v1/km-to-erp/order/list/modify-exec",
                        json, header);

                    if (AppConstants.MODE_ERROR.equals(response)) {
                        throw new Exception("운송장 등록 실패");
                    }
                    log.info("[KM -> ERP 운송장 등록 결과] : {}", response);
                }
            }
        } catch (Exception e) {
            log.error("[운송장 저장 서비스 Error]", e);
            throw e;
        }
    }

    /**
     * 적립 포인트 현황
     */
    public List<PointRewardResultVo> getPointRewardResultList(String orderNo) {
        return orderDao.selectPointRewardResultList(orderNo);
    }

    /**
     * 주문 할인리스트
     */
    public List<OrdSalesDiscountVo> getOrdSalesDiscountList(String orderNo) {
        return orderDao.selectOrdSalesDiscountList(orderNo);
    }

    /**
     * 포인트 사용현황
     */
    public PointUseVo getPointUse(String orderNo) {
        return orderDao.selectPointUse(orderNo);
    }

    /**
     * 주문마스터 정보
     */
    public OrdSalesVo getOrdSales(String orderNo) {
        return orderDao.selectOrdSales(orderNo);
    }

    /**
     * 대시보드 주문/배송 정보
     */
    public DashboardOrderSalesVo getOrderSalesTotalCount(StatisticsModel model) {
        UserVo user = SessionUtil.getLoginSession();
        return orderDao.selectOrderSalesTotalCount(user,model);
    }

    /**
     * 대시보드 취소/교환/반품 정보 (취소건수 / 반품건수 / 교환건수)
     */
    public DashboardOrderExchangeVo getOrderReturnExchangeTotalCount(StatisticsModel model) {
        UserVo user = SessionUtil.getLoginSession();

        Integer cancelCount = orderDao.selectOrderCancelTotalCount(user,model);

        DashboardOrderExchangeVo dashboardOrderExchangeVo = orderDao.selectOrderReturnExchangeTotalCount(user,model);

        if (dashboardOrderExchangeVo != null) {
            dashboardOrderExchangeVo.setMonthlyOrderCancelCount(cancelCount);
        }

        return dashboardOrderExchangeVo;
    }

    /**
     * 대시보드 취소/교환/반품 정보 (취소완료 / 반품요청 / 교환요청)
     */
    public DashboardOrderExchangeVo getReturnExchangeTotalCount(StatisticsModel model) {
        UserVo user = SessionUtil.getLoginSession();
        return orderDao.selectReturnExchangeTotalCount(user,model);
    }

    /**
     * 상품준비주문 리스트
     */
    public List<KiccApprovalVo> getKiccApprovalList(String orderNo) {
        return orderDao.selectKiccApprovalList(orderNo);
    }

    /**
     * 주문배송리스트
     */
    public List<OrdShippingVo> getOrdShippingList(String orderNo, String orderDetailNo) {
        List<OrdShippingVo> res = orderDao.selectOrdShippingList(orderNo, orderDetailNo);
        for (OrdShippingVo vo : res) {
            vo.setGfTrackingUrl(appProperties.getGoodsFlow().getTrackingUrl() + "/" + vo.getGfServiceId());
        }
        return res;
    }

    /**
     * 임시주문 삭제
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void removeReadyOrder(String orderNo) {
        try {
            orderDao.deleteReadyOrder(orderNo);
        } catch (Exception e) {
            log.error("[임시 주문 삭제 오류] orderNo : {}", orderNo, e);
        }
    }

    /**
     * KICC 주문 승인
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addKiccApprovalOrderLog(KiccApprovalResModel kiccApprovalResModel, String clientIp) {
        try {
            orderDao.insertKiccApprovalOrderLog(kiccApprovalResModel, clientIp);
        } catch (Exception e) {
            log.error("[KICC 주문 승인로그 오류] kiccApprovalResModel : {}", kiccApprovalResModel, e);
        }
    }

    /**
     * KICC 승인이후 주문업데이트
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyKiccApprovalOrder(KiccApprovalResModel kiccApprovalResModel) {
        try {
            orderDao.updateKiccApprovalOrder(kiccApprovalResModel);
        } catch (Exception e) {
            log.error("[KICC 승인이후 주문업데이트 오류] kiccApprovalResModel : {}", kiccApprovalResModel, e);
            throw e;
        }
    }

    /**
     * 주문포인트 수정
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderPoint(String shopOrderNo, String customerSeq) {
        Long discountPrice = 0L;
        // 주문 할인정보
        List<OrderSalesDiscountVo> orderDiscountList = orderDao.selectOrderDiscountList(shopOrderNo);
        for (OrderSalesDiscountVo discountVo : orderDiscountList) {
            if (discountVo.getDiscountTypeCode().equals("POINT")) {
                discountPrice = discountVo.getDiscountPrice();
            }
        }

        if (discountPrice > 0) {
            // 사용가능 적립금
            List<PointRewardVo> pointRewardList = orderDao.selectPointRewardList(customerSeq);
            List<Long> seqList = commonService.getSeqList("APP_POINT_USE", pointRewardList.size());
            List<PointRewardVo> sendList = new ArrayList<PointRewardVo>();
            int seqCnt = 0;
            for (PointRewardVo rewardVo : pointRewardList) {
                Long remainPoint = rewardVo.getRemainPoint();
                Long calPoint = discountPrice - remainPoint;
                rewardVo.setUsePointSeq(seqList.get(seqCnt++));
                if (calPoint >= 0) {
                    rewardVo.setUsePoint(remainPoint);
                    rewardVo.setOrderNo(shopOrderNo);
                    orderDao.updateUsePoint(rewardVo);
                    PointRewardVo rvo = orderDao.selectPointRewardBySeq(rewardVo.getPointRewardSeq());
                    PointRewardVo pointUseVo = orderDao.selectUsePoint(seqList.get(seqCnt-1));
                    rvo.setUsePoint(pointUseVo.getUsePoint());
                    rvo.setPointUseSeq(pointUseVo.getPointUseSeq());
                    rvo.setCancelYn("N");
                    rvo.setUpdateDate(pointUseVo.getCreateDate());
                    rvo.setPointUseOrderNo(pointUseVo.getOrderNo());
                    sendList.add(rvo);
                } else {
                    rewardVo.setUsePoint(discountPrice);
                    rewardVo.setOrderNo(shopOrderNo);
                    orderDao.updateUsePoint(rewardVo);
                    PointRewardVo rvo = orderDao.selectPointRewardBySeq(rewardVo.getPointRewardSeq());
                    PointRewardVo pointUseVo = orderDao.selectUsePoint(seqList.get(seqCnt-1));
                    rvo.setUsePoint(pointUseVo.getUsePoint());
                    rvo.setPointUseSeq(pointUseVo.getPointUseSeq());
                    rvo.setCancelYn("N");
                    rvo.setUpdateDate(pointUseVo.getCreateDate());
                    rvo.setPointUseOrderNo(pointUseVo.getOrderNo());
                    sendList.add(rvo);
                    break;
                }
                discountPrice = discountPrice - rewardVo.getRemainPoint();
                if (discountPrice <= 0) {
                    break;
                }
                commonService.kmToErpSend("POST"
                    ,"/api/v1/km-to-erp/point/use/list/add-exec"
                    ,AppUtil.convertObjectToJson(sendList));

            }
        }
    }

    /**
     * 쿠폰사용 수정
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderCoupon(String shopOrderNo, String customerSeq) {
        String customerId = customerSeq.replace("KM_", "");
        String couponIssueSeq = "";

        // 주문 할인정보
        List<OrderSalesDiscountVo> orderDiscountList = orderDao.selectOrderDiscountList(shopOrderNo);
        for (OrderSalesDiscountVo discountVo : orderDiscountList) {
            if (discountVo.getDiscountTypeCode().equals("COUPON")) {
                couponIssueSeq = discountVo.getCouponIssueSeq();
            }
        }

        if (couponIssueSeq != null && !couponIssueSeq.equals("")) {
            orderDao.updateUseCoupon(couponIssueSeq, customerSeq);
        }
    }

    /**
     * 포인트 적립
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void addSaveOrderPoint(String shopOrderNo, String customerSeq) {
        try {
            List<Long> seqList = commonService.getSeqList("APP_POINT_REWARD", 1);
            orderDao.insertSaveOrderPoint(shopOrderNo, customerSeq, seqList.get(0));

        } catch (Exception e) {
            log.error("[KICC 승인이후 포인트적립 오류] shopOrderNo : {}", shopOrderNo, e);
            throw e;
        }
    }

    /**
     * 재고차감
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderStock(String shopOrderNo) {
        try {
            orderDao.updateOrderStock(shopOrderNo);
        } catch (Exception e) {
            log.error("[KICC 승인이후 재고차감 오류] shopOrderNo : {}", shopOrderNo, e);
            throw e;
        }
    }

    /**
     * 주문후 주문상품 장바구니제거
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void removeCart(String shopOrderNo, String customerSeq) {
        try {
            orderDao.deleteCart(shopOrderNo, customerSeq);
        } catch (Exception e) {
            log.error("[KICC 승인이후 장바구니 삭제 오류] shopOrderNo : {}", shopOrderNo, e);
            throw e;
        }
    }

    /**
     * 주문완료 카카오 알림
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void sendOrderComplete(String orderNo) {
        OrderSalesVo orderSalesVo = orderDao.selectOrderCompleteInfo(orderNo);
        String customerName = orderSalesVo.getOrdererName();
        String customerPhone = orderSalesVo.getOrdererPhone();
        String orderDate = orderSalesVo.getOrderDate().toString();
        String productName = orderSalesVo.getProductName();
        String totalPaymentPrice = AppUtil.formatNumberWithCommas(orderSalesVo.getTotalPaymentPrice());

        kakaoService.newOrderKkoMsg(customerName, customerPhone, orderDate, orderNo, productName, totalPaymentPrice);
    }

    /**
     * 주문입금 카카오 알림
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void sendOrderDeposit(String orderNo, String bankName, String accountNo, String accountName) {
        OrderSalesVo orderSalesVo = orderDao.selectOrderCompleteInfo(orderNo);
        String customerName = orderSalesVo.getOrdererName();
        String customerPhone = orderSalesVo.getOrdererPhone();
        String totalPaymentPrice = AppUtil.formatNumberWithCommas(orderSalesVo.getTotalPaymentPrice());

        kakaoService.depositInfoKkoMsg(customerName, customerPhone, bankName, accountNo, accountName, totalPaymentPrice);
    }

    /**
     * 주문취소 카카오 알림
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void sendOrderCancel(String orderNo, String reviseTypeCode, Long refundPrice) {
        OrderSalesVo orderSalesVo = orderDao.selectOrderCompleteInfo(orderNo);
        String customerName = orderSalesVo.getOrdererName();
        String customerPhone = orderSalesVo.getOrdererPhone();
        String productName = orderSalesVo.getProductName();

        if ("40".equals(reviseTypeCode)) {
            String refundDate = AppUtil.getCurrentDate();
            kakaoService.refundKkoMsg(customerName, customerPhone, refundDate, orderNo, productName);
        }

        if ("32".equals(reviseTypeCode)) {
            String strRefundPrice = AppUtil.formatNumberWithCommas(refundPrice.intValue());
            kakaoService.cardPaymentPartialCancelKkoMsg(customerName, customerPhone, strRefundPrice, orderNo);
        }
    }

    /**
     * KICC 주문 취소
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean modifyKiccReviseOrder(KiccReviseResModel kiccReviseResModel) {
        try {
            orderDao.updateKiccReviseOrder(kiccReviseResModel);
            return true;
        } catch (Exception e) {
            log.error("[KICC 주문 취소 오류] kiccReviseResModel : {}", kiccReviseResModel, e);
            return false;
        }
    }

    /**
     * 주문상태 변경
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int modifyOrdSalesStatus(OrderSalesVo orderSalesVo) {
        return orderDao.updateOrdSalesStatus(orderSalesVo);
    }

    /**
     * 포인트 롤백
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyPointRollback(String orderNo) {
        orderDao.updatePointRollback(orderNo);
        List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
        if(pointRewardList.size() == 0) {
            PointRewardVo pointRewardVo = new PointRewardVo();
            pointRewardVo.setOrderNo(orderNo);
            pointRewardList.add(pointRewardVo);
        }
        commonService.kmToErpSend("PATCH","/api/v1/km-to-erp/point/rollback", AppUtil.convertObjectToJson(pointRewardList));
    }

    /**
     * 쿠폰사용 롤백
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyCouponRollback(String orderNo, String orderDetailNo) {
        orderDao.updateCouponRollback(orderNo, orderDetailNo);
    }

    /**
     * 포인트 적립 롤백
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void removePointReward(String orderNo, String isDel) {
        PointRewardVo pointRewardVo = new PointRewardVo();
        if("Y".equals(isDel)) {
            // 삭제처리
            pointRewardVo.setOrderNo(orderNo);
            orderDao.deletePointReward(orderNo);
        } else {
            // 포인트 삭제가 아닌 마이너스 처리
            List<Long> seqList = commonService.getSeqList("APP_POINT_REWARD", 1);
            orderDao.insertPointRewardRefund(orderNo, seqList.get(0));
            pointRewardVo = orderDao.selectPointRewardBySeq(seqList.get(0).toString());
        }
        pointRewardVo.setIsDel(isDel);
        commonService.kmToErpSend("DELETE","/api/v1/km-to-erp/point/delete-exec",AppUtil.convertObjectToJson(pointRewardVo));
    }

    /**
     * 재고 차감 롤백
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyStockRollback(String orderNo, String orderDetailNo, String customerSeq) {
        orderDao.updateStockRollback(orderNo, orderDetailNo, customerSeq);
    }

    /**
     * 상품할인 취소
     */
    public void modifyDiscountListCancel(String orderNo, String orderDetailNo) {
        orderDao.updateDiscountListCancel(orderNo, orderDetailNo);
    }

    /**
     * 상품할인(상세) 취소
     */
    public void modifyDiscountDetailListCancel(String orderNo, String orderDetailNo) {
        orderDao.updateDiscountDetailListCancel(orderNo, orderDetailNo);
    }

    public void modifyDiscountListForShipping(String orderNo, String orderDetailNo, String newOrderDetailNo) {
        orderDao.updateDiscountListForShipping(orderNo, orderDetailNo, newOrderDetailNo);
    }

    public void modifyDiscountDetailListForShipping(String orderNo, String orderDetailNo, String newOrderDetailNo) {
        orderDao.updateDiscountDetailListForShipping(orderNo, orderDetailNo, newOrderDetailNo);
    }

    /**
     * 주문상세 상태변경
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrdSalesDetailStatus(OrderSalesDetailVo orderSalesDetailVo) {
        orderDao.updateOrdSalesDetailStatus(orderSalesDetailVo);
    }

    /**
     * kicc승인정보
     */
    public OrderKiccApprovalVo getKiccApprovalInfo(String orderNo) {
        return orderDao.selectKiccApprovalInfo(orderNo);
    }


    /**
     * 주문할인리스트
     */
    public List<OrderSalesDiscountVo> getOrderDiscountList(String orderNo) {
        return orderDao.selectOrderDiscountList(orderNo);
    }

    /**
     * 주문옵션 리스트
     */
    public List<ProductItemVo> getOrderOptionList(OrderModel orderModel) {
        return orderDao.selectOrderOptionList(orderModel);
    }

    /**
     * 상품준비주문 리스트
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderOption(OrderModel orderModel) {
        UserVo user = SessionUtil.getLoginSession();
        orderModel.setRegUserId(user.getUserId());

        String[] arrOldItemCode = orderModel.getArrOldItemCode();
        String[] arrNewItemCode = orderModel.getArrNewItemCode();
        String[] arrNewItemOptions = orderModel.getArrNewItemOptions();

        int i = 0;
        for (String orderDetailNo : orderModel.getArrOrderDetailNo()) {
            OrderChangeVo orderChangeVo = new OrderChangeVo();
            orderChangeVo.setOrderDetailNo(orderDetailNo);

            orderChangeVo.setOldItemCode(arrOldItemCode[i]);
            orderChangeVo.setNewItemCode(arrNewItemCode[i]);
            orderChangeVo.setNewItemOptions(arrNewItemOptions[i]);
            orderChangeVo.setRegUserId(user.getUserId());
            orderChangeVo.setOrderNo(orderModel.getOrderNo());

            try {
                // 예외가 발생할 수 있는 코드
                orderChangeVo.setStockChange("P");
                orderDao.updateStockChange(orderChangeVo);
            } catch (Exception e) {
                e.printStackTrace(); // 스택 트레이스 출력
            }

            // 기존재고 플러스처리
            orderDao.updateOrderItem(orderChangeVo);

            // 재고차감 처리
            orderChangeVo.setStockChange("M");
            orderDao.updateStockChange(orderChangeVo);


            // 변경 이력저장
            String historyTypeCode = "OPTION_CHANGE";

            OrdSalesHistoryMemoVo ordSalesHistoryMemoVo =
                OrdSalesHistoryMemoVo.builder()
                    .orderNo(orderModel.getOrderNo())
                    .orderDetailNo(orderDetailNo)
                    .historyTypeCode(historyTypeCode)
                    .oldItemCode(orderChangeVo.getOldItemCode())
                    .newItemCode(orderChangeVo.getNewItemCode())
                    .remark("주문옵션 변경 : " + orderChangeVo.getOldItemCode() + " -> " + orderChangeVo.getNewItemCode())
                    .build();
            this.addOrdSalesHistoryMemo(ordSalesHistoryMemoVo);

            i++;
        }
    }

    /**
     * 회원관리 > 상세모달 > 주문내역
     */
    public List<OrderVo> getOrderListForCustomer(OrderModel model, int pageNo, int pageSize) {
        List<String> ordStatusList = new ArrayList<>();
        if ("ORDER".equals(model.getSearchCondition())) {
            ordStatusList.add("MO01");
            ordStatusList.add("MO02");
            ordStatusList.add("MO03");
            ordStatusList.add("MO04");
            ordStatusList.add("MO05");
            ordStatusList.add("MC02");
            ordStatusList.add("MO10");
            ordStatusList.add("GO00");
        } else if ("RETURN".equals(model.getSearchCondition())) {
            ordStatusList.add("MR01");
            ordStatusList.add("MR02");
            ordStatusList.add("MR03");
            ordStatusList.add("MR04");
            ordStatusList.add("ME01");
            ordStatusList.add("ME02");
            ordStatusList.add("ME03");
            ordStatusList.add("ME04");
        }
        model.setSearchOrdStatusList(ordStatusList);

        String customerId = "";
        if(model.getSearchCustomerSeq().startsWith("KM_")) {
            customerId = model.getSearchCustomerSeq().split("KM_")[1];
        }else if(model.getSearchCustomerSeq().startsWith("MBS_")) {
            customerId = model.getSearchCustomerSeq().split("MBS_")[1];
        }
        model.setSearchCustomerSeq(customerId);

        List<OrderVo> res = orderDao.selectOrderListForCustomer(model, pageNo, pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        for (OrderVo vo : res) {
            vo.setCreateDateStr(sdf.format(vo.getCreateDate()));
            vo.setSubTotalProductPrcStr(decimalFormat.format(null != vo.getSubTotalProductPrice() && vo.getSubTotalProductPrice() > 0 ? vo.getSubTotalProductPrice() : 0));
            vo.setSubTotalPaymentPrcStr(decimalFormat.format(null != vo.getSubTotalProductPrice() && vo.getSubTotalPaymentPrice() > 0 ? vo.getSubTotalPaymentPrice() : 0));
            vo.setPaymentYn("Y".equals(vo.getPaymentYn()) ? "결제완료" : "미결제");
        }
        return res;
    }


    /**
     * 고객 환불계좌
     */
    public RefundAccountVo getRefundAccountInfo(String customerSeq) {
        return orderDao.selectRefundAccountInfo(customerSeq);
    }

    /**
     * 주문변경 이력저장(메모)
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addOrdSalesHistoryMemo(OrdSalesHistoryMemoVo ordSalesHistoryMemoVo) {
        UserVo user = SessionUtil.getLoginSession();
        if (user != null) {
            ordSalesHistoryMemoVo.setRegUserId(user.getUserId());
        }

        try {
            orderDao.insertOrdSalesHistoryMemo(ordSalesHistoryMemoVo);
        } catch (Exception e) {
            log.error("[주문변경 이력저장(메모)저장 서비스 Error]", e);

            throw e;
        }
    }

    /**
     * 주문변경 이력저장(메모)
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addOrdSalesHistoryMemoList(OrdSalesHistoryMemoVo ordSalesHistoryMemoVo) {
        UserVo user = SessionUtil.getLoginSession();
        if (user != null) {
            ordSalesHistoryMemoVo.setRegUserId(user.getUserId());
        }

        try {
            orderDao.insertOrdSalesHistoryMemoList(ordSalesHistoryMemoVo);
        } catch (Exception e) {
            log.error("[주문변경 이력저장(메모)저장 서비스 Error]", e);

            throw e;
        }
    }

    /**
     * 배송비 조건
     */
    public PartnerShippingVo getOrderShippingCondition(String orderDetailNo) {
        return orderDao.selectOrderShippingCondition(orderDetailNo);
    }

    /**
     * 주문상세 배송비 수정
     */
    public void modifyOrdDetailDeliveryFee(OrderSalesDetailVo orderSalesDetailVo) {
        orderDao.updateOrdDetailDeliveryFee(orderSalesDetailVo);
    }

    /**
     * 주문 배송비 수정
     */
    public void modifyOrdDeliveryFee(OrderSalesVo orderSalesVo) {
        orderDao.updateOrdDeliveryFee(orderSalesVo);
    }

    /**
     * 적립포인트 수정
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyPointReward(String orderNo, Long point) {
        orderDao.updatePointReward(orderNo, point);

		// 오늘날짜가 2026.4.1 00:00:00 ~ 2026.4.30 23:59:59 이면 로직 시작
		// 3월 31일 이후이면서 5월 1일 이전이면 -> 즉, 4월 한 달 전체
		LocalDate today = LocalDate.now();
		boolean bpIsActive = today.isAfter(LocalDate.of(2026, 3, 31))
			&& today.isBefore(LocalDate.of(2026, 5, 1));

		if(bpIsActive) {
			orderDao.updatePointRewardForEvent(orderNo, point);
		}

        List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
        commonService.kmToErpSend("PATCH"
            ,"/api/v1/km-to-erp/point/list/modify-exec"
            ,AppUtil.convertObjectToJson(pointRewardList));
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyPointRewardForReturnExchange(String orderNo, Long point) {
        /*orderDao.updatePointRewardForReturnExchange(orderNo, point);
        List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
        commonService.kmToErpSend("PATCH"
            ,"/api/v1/km-to-erp/point/list/modify-exec"
            ,AppUtil.convertObjectToJson(pointRewardList));*/

        PointRewardVo pointRewardVo = new PointRewardVo();
        // 포인트 삭제가 아닌 마이너스 처리
        List<Long> seqList = commonService.getSeqList("APP_POINT_REWARD", 1);
        orderDao.insertPointRewardRefundForReturnExchange(orderNo, seqList.get(0), point);
        pointRewardVo = orderDao.selectPointRewardBySeq(seqList.get(0).toString());
        if(null != pointRewardVo) {
            pointRewardVo.setIsDel("N");
            commonService.kmToErpSend("DELETE","/api/v1/km-to-erp/point/delete-exec",AppUtil.convertObjectToJson(pointRewardVo));
        }

		// 오늘날짜가 2026.4.1 00:00:00 ~ 2026.4.30 23:59:59 이면 로직 시작
		// 3월 31일 이후이면서 5월 1일 이전이면 -> 즉, 4월 한 달 전체
		LocalDate today = LocalDate.now();
		boolean bpIsActive = today.isAfter(LocalDate.of(2026, 3, 31))
			&& today.isBefore(LocalDate.of(2026, 5, 1));

		if(bpIsActive) {
			// 포인트 삭제가 아닌 마이너스 처리
			seqList = commonService.getSeqList("APP_POINT_REWARD", 1);
			orderDao.insertEventPointRewardRefundForReturnExchange(orderNo, seqList.get(0), point);
			pointRewardVo = orderDao.selectPointRewardBySeq(seqList.get(0).toString());
			if(null != pointRewardVo) {
				pointRewardVo.setIsDel("N");
				commonService.kmToErpSend("DELETE","/api/v1/km-to-erp/point/delete-exec",AppUtil.convertObjectToJson(pointRewardVo));
			}
		}
    }

    /**
     * 주문 관리자(메모) 리스트
     */
    public List<OrdSalesHistoryMemoVo> getOrdSalesHistoryMemoList(OrdSalesHistoryMemoVo ordSalesHistoryMemoVo) {
        return orderDao.selectOrdSalesHistoryMemoList(ordSalesHistoryMemoVo);
    }

    /**
     * 주문 관리자(메모) 저장/삭제
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrdMemo(OrderModel orderModel) throws Exception {
        UserVo user = SessionUtil.getLoginSession();

        try {

            OrdSalesHistoryMemoVo orderSalesMemo
                = OrdSalesHistoryMemoVo.builder()
                .seq(orderModel.getSeq())
                .orderNo(orderModel.getOrderNo())
                .orderDetailNo(orderModel.getOrderDetailNo())
                .historyTypeCode(historyTypeAdmin)
                .remark(orderModel.getRemark())
                .regUserId(user.getUserId())
                .build();

            // 다른 관리자가 등록한 메모는 수정/삭제 불가
            if (!StringUtils.isEmpty(orderModel.getSeq()) && !user.getUserId().equals(orderModel.getRegUserId())) {
                throw new Exception("메모 수정/삭제는 작성자만 가능합니다.");
            }

            // 등록 (seq가 없을 경우)
            if (StringUtils.isEmpty(orderModel.getSeq())) {
                orderDao.insertOrdSalesHistoryMemo(orderSalesMemo);
            } else {    // 수정/삭제
                if ("Y".equals(orderModel.getIsDelMemo())) {
                    orderDao.deleteOrdSalesHistoryMemo(orderSalesMemo);
                } else {
                    orderDao.updateOrdSalesHistoryMemo(orderSalesMemo);
                }
            }

        } catch (Exception e) {
            log.error("[주문 관리자(메모) 저장/삭제 서비스 Error]", e);

            throw e;
        }
    }

    /**
     * 주문 취소금액 수정 (ORD_SALES)
     */
    public void modifyOrdCancelPrice(OrderSalesVo orderSalesVo) {
        orderDao.updateOrdCancelPrice(orderSalesVo);
    }


    /**
     * 굿스플로 배송추적
     */
    public void addOrderGoodsFlowShippingLog() {
        Map<String, String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("Authorization", appProperties.getGoodsFlow().getApiKey());

        // 굿스플로 배송추적 정보 조회
        String response = RestUtil.getApiWithHeaders(MediaType.APPLICATION_JSON
            , appProperties.getGoodsFlow().getApiUrl() + "/api/deliveries/webhooks"
            , header);


        long seq = 0;
        try {

            JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();

            BatchLogVo batchLog = new BatchLogVo();
            batchLog.setSystem("KM");
            batchLog.setTask("GoodsFlowShippingLogJob");
            batchLog.setBatchStatus("PROGRESS");
            batchLog.setContents(jObject.toString());
            batchDao.insertBatchLog(batchLog);

            seq = batchLog.getSeq();
            batchLog = new BatchLogVo();
            StringBuffer sb = new StringBuffer();

            // 굿스플로 배송정보 로그 저장
            for (int i = 0; i < jObject.get("data").getAsJsonArray().size(); i++) {
                JsonObject dataObj = jObject.get("data").getAsJsonArray().get(i).getAsJsonObject();

                String serviceId = dataObj.get("serviceId").getAsString().trim();
                String deliveryStatus = dataObj.get("deliveryStatus").getAsString().trim();
                String invoiceNo = dataObj.get("invoiceNo").getAsString().trim();

                List<OrdGfShippingVo> gfList = orderDao.selectGfShippingWithGfServiceId(serviceId, invoiceNo);
                for(OrdGfShippingVo gf : gfList) {
                    OrdGfShippingLogVo logVo = new OrdGfShippingLogVo();
                    logVo.setGfServiceId(serviceId);
                    logVo.setOrderDetailNo(gf.getOrderDetailNo());
                    logVo.setGfDeliveryComp(dataObj.get("transporter").getAsString());
                    logVo.setDeliveryTrackingNo(invoiceNo);
                    logVo.setStatusDateTime(dataObj.get("statusDateTime").getAsString());
                    logVo.setGfOrdStatus(deliveryStatus);
                    logVo.setLocation(dataObj.get("location") != null ? dataObj.get("location") + "" : null);
                    logVo.setLocationPhone(dataObj.get("locationPhoneNo") != null ? dataObj.get("locationPhoneNo") + "" : null);
                    logVo.setDriverName(dataObj.get("driverName") != null ? dataObj.get("driverName") + "" : null);
                    logVo.setDriverPhone(dataObj.get("driverPhoneNo") != null ? dataObj.get("driverPhoneNo") + "" : null);
                    logVo.setErrorName(dataObj.get("errorName") != null ? dataObj.get("errorName") + "" : null);
                    logVo.setExceptionName(dataObj.get("exceptionName") != null ? dataObj.get("exceptionName") + "" : null);
                    int insertGfLogRes = orderDao.insertOrdGfShippingLog(logVo);
                    sb.append("\ninsertGfLog: " + insertGfLogRes);
                }

                int updateGfShippingRes = orderDao.updateOrdGfShipping(deliveryStatus, serviceId);
                sb.append("\nupdateGfShipping: " + updateGfShippingRes);

                if (deliveryStatus.equals("COMPLETED")) {
                    // 굿스플로 서비스아이디로 order_no 찾기
                    List<OrderVo> orderList = orderDao.selectOrderNoByGfServiceId(serviceId);
                    for(OrderVo order : orderList) {
                        String orderNo = order.getOrderNo();
                        if("ES".equals(order.getOrderType())) {
                            // 완료처리됐을때 교환완료상태로 변경
                            OrderSalesVo orderSalesVo
                                = OrderSalesVo.builder()
                                .orderNo(orderNo)
                                .orderMainStatus("ME02")
                                .regUserId("BATCH")
                                .build();
                            int modifyOrdStatusRes = this.modifyOrdSalesStatus(orderSalesVo);
                            sb.append("\nmodifyOrdStatus: " + modifyOrdStatusRes);

                            // 상세주문 교환완료상태로 변경
                            OrderSalesDetailVo detailVo = new OrderSalesDetailVo();
                            detailVo.setOrderStatus("ME02");
                            detailVo.setTotalCancelPrice(0l);
                            detailVo.setRegUserId("BATCH");
                            detailVo.setOrderNo(orderNo);
                            int modifyOrdDetailStatusRes = orderDao.updateOrdSalesDetailStatus(detailVo);
                            sb.append("\nupdateOrdSalesDetailStatus: " + modifyOrdDetailStatusRes);

                            // 정산테이블 INSERT
                            SettlementVo stm = new SettlementVo();
                            List<Long> seqList = commonService.getSeqList("ORD_SETTLEMENT", 1);
                            stm.setSettlementSeq(seqList.get(0));
                            stm.setOrderDetailNo(order.getOrderDetailNo());
                            stm.setOrdSettlementTypeCode("EXCHANGE");
                            int insertOrdSettlement = orderDao.insertOrdSettlementForExchange(stm);
                            sb.append("\ninsertOrdSettlement: " + insertOrdSettlement);

                        }else if("SO".equals(order.getOrderType()) || "SR".equals(order.getOrderType())) {
                            if(!"MO05".equals(order.getOrderMainStatus())) {
                                // 완료처리
                                int updateOrdDetailShippingRes = orderDao.updateOrdSalesDetailShipping(
                                    serviceId
                                    , "BATCH"
                                    , "MO05"
                                );
                                sb.append("\nupdateOrdDetailShipping: " + updateOrdDetailShippingRes);

                                // 처리해야할 주문건수
                                int orderCnt = orderDao.selectNotCompleteOrderCount(orderNo);

                                // 주문완료이면서 해당 주문에 더이상 처리해야할 주문이 없는 경우 주문 마스터 완료처리
                                if (orderCnt == 0) {
                                    OrderSalesVo orderSalesVo
                                        = OrderSalesVo.builder()
                                        .orderNo(orderNo)
                                        .orderMainStatus("MO05")
                                        .regUserId("BATCH")
                                        .build();
                                    int modifyOrdStatusRes = this.modifyOrdSalesStatus(orderSalesVo);
                                    sb.append("\nmodifyOrdStatus: " + modifyOrdStatusRes);

                                    // 대기포인트 지급
                                    int updatePointRes = orderDao.updatePointWait(orderNo);
                                    sb.append("\nupdatePoint: " + updatePointRes);
                                    List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
                                    commonService.kmToErpSend("PATCH"
                                        ,"/api/v1/km-to-erp/point/list/modify-exec"
                                        ,AppUtil.convertObjectToJson(pointRewardList));
                                }

                                // 배송완료후 리뷰작성 독려 알림톡
                                OrderModel orderModel = new OrderModel();
                                orderModel.setSearchOrderDetailNo(order.getOrderDetailNo());
                                List<OrderVo> list = orderDao.selectOrderDetailList(null, orderModel);
                                for (OrderVo orderVo : list) {
                                    if("SO".equals(orderVo.getOrderType())) {
                                        customerService.reviewWriteKkoMsg(
                                            orderVo.getOrdererName()
                                            , orderVo.getOrdererPhone()
                                            , orderVo.getProductCode()
                                            , orderVo.getOrderDetailNo()
                                            , orderVo.getItemCode());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            batchLog.setBatchStatus("END");
            batchLog.setErrorContents(sb.toString());
            batchLog.setSeq(seq);
            batchDao.updateBatchLog(batchLog);

        }catch (Exception e) {
            BatchLogVo batchLog = new BatchLogVo();
            batchLog.setBatchStatus("ERROR");
            batchLog.setErrorContents(e.getMessage());
            batchLog.setSeq(seq);
            batchDao.updateBatchLog(batchLog);
        }

    }

    /**
     * 선물하기정보 조회
     */
    public OrderGiftMsgVo getOrdGiftMsg(String ordNo) {
        return orderDao.selectOrdGiftMsg(ordNo);
    }

    /**
     * 운송장 삭제
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void delOrderShipping(OrderModel orderModel)
        throws Exception {
        UserVo user = SessionUtil.getLoginSession();
        // 기존 운송장에 필요한 정보를 가져오기위해 조회
        OrdGfShippingVo ordGfShippingVo = orderDao.selectOrdGfShipping(orderModel.getArrOrderDetailNo()[0], null);

        // 기존 굿스플로 정보가 있으면
        if (ordGfShippingVo != null) {
            // 동일 운송장 Count를 위해 조회
            OrdGfShippingVo ordGfShippingVos = orderDao.selectOrdGfShipping(null, ordGfShippingVo.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("   ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""));

            // 삭제하려는 운송장이 유일한 경우 굿스플로 삭제 처리
            if (ordGfShippingVos.getTrackingCnt() == 1){
                // 굿스플로 배송정보 삭제
                Map<String, String> header = new HashMap<>();
                header.put("accept", "application/json");
                header.put("Authorization", appProperties.getGoodsFlow().getApiKey());

                JsonObject req = new JsonObject();
                JsonArray reqArr = new JsonArray();
                JsonObject req2 = new JsonObject();
                req2.addProperty("workerId", user.getUserId());
                req2.addProperty("id", ordGfShippingVo.getGfServiceId());
                req2.addProperty("reasonType", "PICKUP_FAIL");

                reqArr.add(req2);
                req.add("items", reqArr);

                // 굿스플로 API 호출 (운송장번호 등록)
                String response = RestUtil.deleteApi(MediaType.APPLICATION_JSON
                    , appProperties.getGoodsFlow().getApiUrl() + "/api/deliveries/cancel"
                    , req.toString(), header);

                JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();
                boolean isSuccess = jObject.get("success").getAsBoolean();
                // api 통신여부
                if (!isSuccess) {
                    throw new Exception("배송정보 삭제 실패");
                } else {
                    for(int i = 0; i < jObject.get("data").getAsJsonArray().size(); i++) {
                        boolean dataIsSuccess = jObject.get("data").getAsJsonArray()
                            .get(i).getAsJsonObject()
                            .get("success").getAsBoolean();
                        if (!dataIsSuccess) {
                            String message = jObject.get("data").getAsJsonArray()
                                .get(i).getAsJsonObject()
                                .get("error").getAsJsonObject()
                                .get("message").getAsString();
                            String[] arr = StringUtils.split(message, ":");
                            if(arr.length > 1) {
                                String messageCode = arr[0];
                                message = arr[1];
                                if(!"E20002".equals(messageCode) && !"E20000".equals(messageCode)) {
                                    throw new Exception(message);
                                }
                            }
                        }
                    }
                }
                log.info("response : {}", response);
            }
        }
        orderDao.deleteOrdShipping(orderModel);
        this.addOrderShipping(orderModel);
    }

    /**
     * 대시보드 판매건수 기준 상품 순위
     */
    public List<DashboardOrderSalesReturnRankingVo> getDashboardSalesRankingList(
        String brandCode,
        String searchStartDate,
        String searchEndDate) {
        UserVo user = SessionUtil.getLoginSession();
        return orderDao.selectDashboardSalesRankingList(brandCode, searchStartDate,searchEndDate, user);
    }

    /**
     * 대시보드 반품건수 기준 상품 순위
     */
    public List<DashboardOrderSalesReturnRankingVo> getDashboardReturnRankingList(
        String brandCode,
        String searchStartDate,
        String searchEndDate) {
        UserVo user = SessionUtil.getLoginSession();
        return orderDao.selectDashboardReturnRankingList(brandCode, searchStartDate,searchEndDate, user);
    }

    /**일괄 송장번호 입력 실행*/
    // @Transactional(rollbackFor = {java.lang.Exception.class, RuntimeException.class})
    public Map<String, Object> bulkTrackingNoAddExec(MultipartFile excelUploadFile)
        throws Exception {
        OrderModel orderModel = new OrderModel();
        UserVo user = SessionUtil.getLoginSession();
        if (user != null) {
            orderModel.setUserId(user.getUserId());
        }

        Map<String, Object> returnMap = new HashMap<>();
        List<OrderTrackingNoExcelUploadResultModel> resultModel = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try {
            List<List<String>> excelList = excelService.getExcelList(excelUploadFile);

            // 헤더 제거
            excelList.remove(0);

            List<OrdShippingVo> ordShippingVoList = new ArrayList<>();
            List<String> messages = new ArrayList<>();
            int rowLocation = 2; // 헤더 빼면 시작위치는 2

            // 1. 데이터 적합성 체크
            CommonCodeVo commonCodeVo = new CommonCodeVo();
            commonCodeVo.setGroupCode("DELIVERY_COMP");

            for (List<String> strList : excelList) {
                String message = "";
                String orderDate = strList.get(0);
                String orderNo = strList.get(3);
                String orderDetailNo = strList.get(4);
                String customerId = strList.get(8);
                String ordererName = strList.get(9);
                String compName = strList.get(11);
                String productName = strList.get(15);
                String optionValue = strList.get(16);
                String deliveryCompCode = strList.get(17);
                String trackingNo = strList.get(18);
                String quantity = strList.get(20);
                String deliveryRequest = strList.get(36);
                String totalProductPrice = strList.get(37);
                String totalPaymentPrice = strList.get(38);
                String payMethodTypeName = strList.get(39);


                if(null == deliveryCompCode || "".equals(deliveryCompCode)) {
                    message += "\r\n 배송사코드가 없습니다.";
                }

                if(null == trackingNo || "".equals(trackingNo)) {
                    message += "\r\n 운송장번호가 없습니다.";
                }

                commonCodeVo.setCode(deliveryCompCode);
                List<CommonCodeVo> comcode = commonService.getCommonCodeList2(commonCodeVo);
                if(comcode.size() < 1) {
                    message += "\r\n 적합하지 않은 배송사코드 입니다.";
                }

                // 배송준비중
                OrderModel searchOrderModel = new OrderModel();
                searchOrderModel.setOrdStatus("MO03");
                searchOrderModel.setSearchOnlineSiteCode(AppConst.ONLINE_SITE_CODE);
                // searchOrderModel.setPartnerCode(user.getPartnerCode());
                searchOrderModel.setSearchOrderDetailNo(orderDetailNo);
                List<OrderVo> list = getOrderDetailList(null, searchOrderModel);
                if(list.size() < 1) {
                    message += "\r\n 적합하지 않은 주문상세번호 입니다.";
                }else {
                    OrderVo orderVo = list.get(0);
                    if(!orderNo.equals(orderVo.getOrderNo())) {
                        message += "\r\n 적합하지 않은 주문번호 입니다.";
                    }
                }

                if(!"".equals(message)) {
                    messages.add("[" + rowLocation + "행]" + message);
                }

                OrderTrackingNoExcelUploadResultModel trackingNoExcelModel = new OrderTrackingNoExcelUploadResultModel();
                trackingNoExcelModel.setMessage(message);
                trackingNoExcelModel.setCreateDate(orderDate);
                trackingNoExcelModel.setOrderNo(orderNo);
                trackingNoExcelModel.setOrderDetailNo(orderDetailNo);
                trackingNoExcelModel.setOrdererName(ordererName);
                trackingNoExcelModel.setCustomerId(customerId);
                trackingNoExcelModel.setCompName(compName);
                trackingNoExcelModel.setDeliveryCompCode(deliveryCompCode);
                trackingNoExcelModel.setDeliveryTrackingNo(trackingNo);
                trackingNoExcelModel.setProductName(productName);
                trackingNoExcelModel.setOptionValue(optionValue);
                trackingNoExcelModel.setQuantity(quantity);
                trackingNoExcelModel.setSubTotalProductPrice(totalProductPrice);
                trackingNoExcelModel.setSubTotalPaymentPrice(totalPaymentPrice);
                trackingNoExcelModel.setPayMethodTypeName(payMethodTypeName);
                trackingNoExcelModel.setDeliveryRequest(deliveryRequest);
                resultModel.add(trackingNoExcelModel);

                rowLocation++;
            }

            orderModel.setOrdShippingVoList(ordShippingVoList);

            // 만약 하나라도 에러가 있으면 반환한다.
            if (messages.size() > 0) {
                returnMap.put("messages", messages.size());
                returnMap.put("result", resultModel);
                return returnMap;
            }

            // 2. 저장 프로세스
            String itemName = "캐리마켓 상품";

            OrderVo orderInfo = null;
            String orderNo = null;
            String shippingNo = null;
            String deliveryCompCode = null;

            for(OrderTrackingNoExcelUploadResultModel model : resultModel) {
                ordShippingVoList = new ArrayList<>();
                orderInfo = orderDao.selectOrderDetailInfoForKakao(model.getOrderDetailNo());
                orderNo = orderInfo.getOrderNo();
                shippingNo = model.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n","");
                deliveryCompCode = model.getDeliveryCompCode();
                if(!"DC17".equals(model.getDeliveryCompCode())) { // 업체직송이 아닌경우 굿스플로 로직 진행
                    // 배송정보(good flow) 가져오기
                    OrdGfShippingVo ordGfShippingVo = orderDao.selectOrdGfShipping(null, shippingNo);
                    if(null != ordGfShippingVo) {
                        // message += "\r\n 이미 등록된 운송장번호 입니다.";
                        // 굿스플로 배송정보 저장
                        OrdGfShippingVo gfVo = new OrdGfShippingVo();
                        gfVo.setOrderDetailNo(model.getOrderDetailNo());
                        gfVo.setGfDeliveryComp(ordGfShippingVo.getGfDeliveryComp());
                        gfVo.setDeliveryTrackingNo(shippingNo);
                        gfVo.setGfServiceId(ordGfShippingVo.getGfServiceId());
                        gfVo.setGfOrdStatus(ordGfShippingVo.getGfOrdStatus());
                        gfVo.setRegUserId(user.getUserId());
                        orderDao.insertOrdGfShipping(gfVo);

                        // 최종 ORD_SHIPPING에 저장될  detailNo 저장
                        OrdShippingVo shippingVo = new OrdShippingVo();
                        shippingVo.setOrderDetailNo(model.getOrderDetailNo());
                        shippingVo.setDeliveryCompCode(model.getDeliveryCompCode());
                        shippingVo.setDeliveryTrackingNo(model.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""));
                        ordShippingVoList.add(shippingVo);

                        // 현재시간
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date time = new Date();

                        // 굿스플로 로그 저장
                        OrdGfShippingLogVo gfLogVo
                            = OrdGfShippingLogVo.builder()
                            .orderDetailNo(gfVo.getOrderDetailNo())
                            .gfDeliveryComp(gfVo.getGfDeliveryComp())
                            .deliveryTrackingNo(gfVo.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""))
                            .gfServiceId(gfVo.getGfServiceId())
                            .statusDateTime(format.format(time))
                            .gfOrdStatus("DLV_FAILED")
                            .build();
                        orderDao.insertOrdGfShippingLog(gfLogVo);

                        successCount++;

                        // 최종 ORD_SHIPPING 저장
                        if(ordShippingVoList.size() > 0) {
                            orderModel.setOrdShippingVoList(ordShippingVoList);

                            List<Long> seqList = commonService.getSeqList(
                                "ORD_SHIPPING", ordShippingVoList.size());
                            int seqCnt = 0;
                            for (OrdShippingVo vo : ordShippingVoList) {
                                vo.setShippingSeq(seqList.get(seqCnt++));
                            }
                            orderDao.insertOrderShipping(orderModel);
                        }
                    }else {
                        // 배송정보(good flow)가 없을때 good flow 프로세스 진행
                        Map<String, String> queryStringMap = new HashMap<>();
                        CommonCodeVo srchCodeVo = new CommonCodeVo();
                        srchCodeVo.setGroupCode("DELIVERY_COMP");
                        srchCodeVo.setCode(model.getDeliveryCompCode());
                        List<CommonCodeVo> codeList = commonService.getCommonCodeList2(srchCodeVo);
                        if (codeList.size() > 0) {
                            CommonCodeVo code = codeList.get(0);
                            List<String> retExcCodeList = new ArrayList<>();
                            retExcCodeList.add("RN09");
                            retExcCodeList.add("EH09");
                            retExcCodeList.add("RN99");
                            retExcCodeList.add("EH99");
                            OrderSalesDetailVo detailVo = orderDao.selectSalesDetailByPk(null, model.getOrderDetailNo(), retExcCodeList);
                            Map<String, String> header = new HashMap<>();
                            header.put("accept", "application/json");
                            header.put("Authorization", appProperties.getGoodsFlow().getApiKey());

                            JsonObject req = new JsonObject();
                            req.addProperty("requestId", detailVo.getOrderNo());
                            JsonObject req2 = new JsonObject();
                            JsonArray reqArr = new JsonArray();
                            //req2.addProperty("uniqueId", detailVo.getOrderDetailNo());
                            req2.addProperty("transporter", code.getAttribute());
                            req2.addProperty("invoiceNo", shippingNo);
                            req2.addProperty("itemName", itemName);
                            reqArr.add(req2);
                            req.add("items", reqArr);

                            // 굿스플로 API 호출 (운송장번호 등록)
                            String response = RestUtil.postApi(MediaType.APPLICATION_JSON
                                , appProperties.getGoodsFlow().getApiUrl() + "/api/deliveries/tracking"
                                , req.toString(), header);

                            JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();
                            boolean isSuccess = jObject.get("success").getAsBoolean();

                            // api 통신여부
                            String serviceId = "";
                            if (!isSuccess) {
                                // throw new Exception("배송정보 등록 실패.(운송장번호 : " + shippingNo + ")");
                                failCount++;
                                model.setMessage("배송정보 등록 실패.(운송장번호 : " + shippingNo + ")");
                            } else {
                                for(int i = 0; i < jObject.get("data").getAsJsonObject().get("items").getAsJsonArray().size(); i++) {
                                    boolean dataIsSuccess = jObject.get("data").getAsJsonObject()
                                        .get("items").getAsJsonArray()
                                        .get(i).getAsJsonObject()
                                        .get("success").getAsBoolean();
                                    if (!dataIsSuccess) {
                                        String message = jObject.get("data").getAsJsonObject().get("items").getAsJsonArray()
                                            .get(i).getAsJsonObject()
                                            .get("error").getAsJsonObject()
                                            .get("message").getAsString();


                                        // throw new Exception("운송장번호 : " + shippingNo + " ("+message+")");
                                        failCount++;
                                        model.setMessage("운송장번호 : " + shippingNo + " ("+message+")");
                                    }else {
                                        // 최종 ORD_SHIPPING에 저장될  detailNo 저장
                                        OrdShippingVo shippingVo = new OrdShippingVo();
                                        shippingVo.setOrderDetailNo(model.getOrderDetailNo());
                                        shippingVo.setDeliveryCompCode(model.getDeliveryCompCode());
                                        shippingVo.setDeliveryTrackingNo(model.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""));
                                        ordShippingVoList.add(shippingVo);

                                        serviceId = jObject.get("data").getAsJsonObject()
                                            .get("items").getAsJsonArray()
                                            .get(i).getAsJsonObject()
                                            .get("data").getAsJsonObject()
                                            .get("serviceId").getAsString();

                                        // 굿스플로 배송정보 저장
                                        OrdGfShippingVo gfVo = new OrdGfShippingVo();
                                        gfVo.setOrderDetailNo(model.getOrderDetailNo());
                                        gfVo.setGfDeliveryComp(code.getAttribute());
                                        gfVo.setDeliveryTrackingNo(shippingNo);
                                        gfVo.setGfServiceId(serviceId);
                                        gfVo.setGfOrdStatus("IN_TRANSIT");
                                        gfVo.setRegUserId(user.getUserId());
                                        orderDao.insertOrdGfShipping(gfVo);

                                        // 현재시간
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date time = new Date();

                                        // 굿스플로 로그 저장
                                        OrdGfShippingLogVo gfLogVo
                                            = OrdGfShippingLogVo.builder()
                                            .orderDetailNo(gfVo.getOrderDetailNo())
                                            .gfDeliveryComp(gfVo.getGfDeliveryComp())
                                            .deliveryTrackingNo(gfVo.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""))
                                            .gfServiceId(gfVo.getGfServiceId())
                                            .statusDateTime(format.format(time))
                                            .gfOrdStatus("DLV_FAILED")
                                            .build();
                                        orderDao.insertOrdGfShippingLog(gfLogVo);

                                        // 배송중 알림톡 전송
                                        kakaoService.startDeliveryKkoMsg(
                                            orderInfo.getRecipientName()
                                            , orderInfo.getRecipientTelNo()
                                            , orderInfo.getOrderNo()
                                            , orderInfo.getProductName()
                                            , code.getValue()
                                            , shippingNo);

                                        successCount++;
                                    }
                                    // 최종 ORD_SHIPPING 저장
                                    if(ordShippingVoList.size() > 0) {
                                        orderModel.setOrdShippingVoList(ordShippingVoList);

                                        List<Long> seqList = commonService.getSeqList(
                                            "ORD_SHIPPING", ordShippingVoList.size());
                                        int seqCnt = 0;
                                        for (OrdShippingVo vo : ordShippingVoList) {
                                            vo.setShippingSeq(seqList.get(seqCnt++));
                                        }
                                        orderDao.insertOrderShipping(orderModel);
                                    }
                                }
                            }
                        }
                    }
                }else {
                    // 최종 ORD_SHIPPING에 저장될  detailNo 저장
                    OrdShippingVo shippingVo = new OrdShippingVo();
                    shippingVo.setOrderDetailNo(model.getOrderDetailNo());
                    shippingVo.setDeliveryCompCode(model.getDeliveryCompCode());
                    shippingVo.setDeliveryTrackingNo(model.getDeliveryTrackingNo().trim().replaceAll("-", "").replaceAll("  ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n",""));
                    ordShippingVoList.add(shippingVo);

                    // 업체직송이면 성공카운트+1
                    successCount++;

                    // 최종 ORD_SHIPPING 저장
                    if(ordShippingVoList.size() > 0) {
                        orderModel.setOrdShippingVoList(ordShippingVoList);

                        List<Long> seqList = commonService.getSeqList(
                            "ORD_SHIPPING", ordShippingVoList.size());
                        int seqCnt = 0;
                        for (OrdShippingVo vo : ordShippingVoList) {
                            vo.setShippingSeq(seqList.get(seqCnt++));
                        }
                        orderDao.insertOrderShipping(orderModel);
                    }
                }

                // 에스크로 주문건은 상태변경처리
                String reviseTypeCode = null;
                String reviseSubTypeCode = null;
                OrderKiccApprovalVo orderKiccApprovalVo = this.getKiccApprovalInfo(orderNo);
                if("ES01".equals(orderKiccApprovalVo.getStatusCode())) {
                    // 에스크로 계좌이체 주문건 취소시
                    reviseTypeCode = "61";
                    reviseSubTypeCode = "ES07"; // 배송중 상태변경

                }

                if("ES03".equals(orderKiccApprovalVo.getStatusCode())) {
                    // 에스크로 무통장입금 주문건 취소시
                    reviseTypeCode = "61";
                    reviseSubTypeCode = "ES07"; // 배송중 상태변경
                }

                if("61".equals(reviseTypeCode)) {
                    KiccAuthenticationReqModel kiccAuthenticationReqModel = new KiccAuthenticationReqModel();
                    kiccAuthenticationReqModel.setPgCno(orderKiccApprovalVo.getPgCno());
                    kiccAuthenticationReqModel.setClientIp(AppUtil.getClientIp());
                    kiccAuthenticationReqModel.setReviseTypeCode(reviseTypeCode);
                    kiccAuthenticationReqModel.setReviseSubTypeCode(reviseSubTypeCode);
                    kiccAuthenticationReqModel.setShopOrderNo(orderKiccApprovalVo.getShopOrderNo());

                    if("DC04".equals(deliveryCompCode)) {
                        deliveryCompCode = "DC05";
                    }else if("DC01".equals(deliveryCompCode)) {
                        deliveryCompCode = "DC01";
                    }else if("DC02".equals(deliveryCompCode)) {
                        deliveryCompCode = "DC07";
                    }else if("DC03".equals(deliveryCompCode)) {
                        deliveryCompCode = "DC08";
                    }else {
                        deliveryCompCode = "DC13";
                    }
                    kiccAuthenticationReqModel.setDeliveryCompCode(deliveryCompCode);
                    kiccAuthenticationReqModel.setShippingNo(shippingNo);
                    this.kiccReviseForEscrow(kiccAuthenticationReqModel);
                }
            }// for문

        }catch (Exception e) {
            log.error("[일괄 운송장 저장 서비스 Error]", e);
            throw e;
        }

        returnMap.put("result", resultModel);
        returnMap.put("successCount", successCount);
        returnMap.put("failCount", failCount);
        return returnMap;
    }

    /**7일지난 배송준비중상태 업체배송 목록 배송완료처리*/
    public void setVendorDeliveryComplete() {
        List<OrderVo> targetList = orderDao.selectShippingBeginVendorDeliveryList();

        BatchLogVo batchLog = new BatchLogVo();
        batchLog.setSystem("KM");
        batchLog.setTask("VendorDeliveryCompleteJob");
        batchLog.setBatchStatus("PROGRESS");
        batchLog.setContents("targetList: "+targetList.size());
        batchDao.insertBatchLog(batchLog);

        long seq = batchLog.getSeq();

        if(targetList.size() > 0) {
            String[] arr = new String[targetList.size()];
            for(int i = 0; i < targetList.size(); i++) {
                arr[i] = targetList.get(i).getOrderDetailNo();
            }
            OrderModel model = new OrderModel();
            model.setArrOrderDetailNo(arr);
            model.setUserId("BATCH");
            model.setRegUserId("BATCH");
            model.setChangeStatusType("N"); // 배송완료
            model.setCurrentStatusCode("MO04"); // 배송중

            try {
                this.modifyOrderStatus(model);

            }catch (Exception e) {
                batchLog = new BatchLogVo();
                batchLog.setBatchStatus("ERROR");
                batchLog.setErrorContents(e.getMessage());
                batchLog.setSeq(seq);
                batchDao.updateBatchLog(batchLog);
            }

        }

        batchLog.setBatchStatus("END");
        batchLog.setErrorContents("NO ERROR");
        batchLog.setSeq(seq);
        batchDao.updateBatchLog(batchLog);

    }

    /**
     * KM -> ERP 주문 정보 가져오기
     */
    public KmToErpNewOrderModReqDto getKmToErpNewOrderModReqDto(String orderNo) {
        // 지금 주문 조회
        KmToErpNewOrderModReqDto dto = orderDao.selectKmToErpNewOrderInfo(orderNo);
        if(dto != null) {
            // 상세 주문 조회
            List<KmToErpNewOrderDetailReq> orderDetailList = orderDao.selectKmToErpNewOrderDetailList(
                orderNo);
            List<KmToErpOrderPaymentReq> orderPaymentList = orderDao.selectKmToErpNewOrderPaymentList(
                orderNo);
            List<KmToErpOrderDiscountReq> orderDiscountList = orderDao.selectKmToErpNewOrderDiscountList(
                orderNo);
            List<KmToErpOrderDiscountDetailReq> orderDiscountDetailList = orderDao.selectKmToErpNewOrderDiscountDetailList(
                orderNo);
            List<KmToErpOrderShippingReq> orderShippingList = orderDao.selectKmToErpNewOrderShippingList(
                orderNo);

            dto.setOrderDetailList(orderDetailList);
            dto.setOrderPaymentList(orderPaymentList);
            dto.setOrderDiscountList(orderDiscountList);
            dto.setOrderDiscountDetailList(orderDiscountDetailList);
            dto.setOrderShippingList(orderShippingList);
        }

        return dto;
    }

    public KmToErpNewOrderModReqDto getKmToErpNewOrderModReqDtoAll(String orderNo) {
        // 지금 주문 조회
        KmToErpNewOrderModReqDto dto = orderDao.selectKmToErpNewOrderInfoAll(orderNo);
        if(dto != null) {
            // 상세 주문 조회
            List<KmToErpNewOrderDetailReq> orderDetailList = orderDao.selectKmToErpNewOrderDetailListAll(
                orderNo);
            List<KmToErpOrderPaymentReq> orderPaymentList = orderDao.selectKmToErpNewOrderPaymentListAll(
                orderNo);
            List<KmToErpOrderDiscountReq> orderDiscountList = orderDao.selectKmToErpNewOrderDiscountListAll(
                orderNo);
            List<KmToErpOrderDiscountDetailReq> orderDiscountDetailList = orderDao.selectKmToErpNewOrderDiscountDetailListAll(
                orderNo);
            List<KmToErpOrderShippingReq> orderShippingList = orderDao.selectKmToErpNewOrderShippingListAll(
                orderNo);

            dto.setOrderDetailList(orderDetailList);
            dto.setOrderPaymentList(orderPaymentList);
            dto.setOrderDiscountList(orderDiscountList);
            dto.setOrderDiscountDetailList(orderDiscountDetailList);
            dto.setOrderShippingList(orderShippingList);
        }

        return dto;
    }



    public List<KmToErpNewOrderModReqDto> getKmToErpNewOrderModReqDtoByOrderDetailNoList(String[] arrOrderDetailNo) {
        // 지금 주문 조회
        List<KmToErpNewOrderModReqDto> dto = orderDao.selectKmToErpNewOrderInfoByOrderDetailNoList(arrOrderDetailNo);
        if (dto != null && !dto.isEmpty()) {
            // 상세 주문 조회
            List<KmToErpNewOrderDetailReq> orderDetailList = orderDao.selectKmToErpNewOrderDetailListByOrderDetailNoList(
                arrOrderDetailNo);
            List<KmToErpOrderPaymentReq> orderPaymentList = orderDao.selectKmToErpNewOrderPaymentListByOrderDetailNoList(
                arrOrderDetailNo);
            List<KmToErpOrderDiscountReq> orderDiscountList = orderDao.selectKmToErpNewOrderDiscountListByOrderDetailNoList(
                arrOrderDetailNo);
            List<KmToErpOrderDiscountDetailReq> orderDiscountDetailList = orderDao.selectKmToErpNewOrderDiscountDetailListByOrderDetailNoList(
                arrOrderDetailNo);
            List<KmToErpOrderShippingReq> orderShippingList = orderDao.selectKmToErpNewOrderShippingListByOrderDetailNoList(
                arrOrderDetailNo);

            dto.get(0).setOrderDetailList(orderDetailList);
            dto.get(0).setOrderPaymentList(orderPaymentList);
            dto.get(0).setOrderDiscountList(orderDiscountList);
            dto.get(0).setOrderDiscountDetailList(orderDiscountDetailList);
            dto.get(0).setOrderShippingList(orderShippingList);
        }

        return dto;
    }

    public List<KmToErpNewOrderModReqDto> getKmToErpNewOrderModReqDtoByOrderDetailNoListAll(String[] arrOrderDetailNo) {
        // 지금 주문 조회
        List<KmToErpNewOrderModReqDto> dto = orderDao.selectKmToErpNewOrderInfoByOrderDetailNoListAll(arrOrderDetailNo);
        if (dto != null && !dto.isEmpty()) {
            // 상세 주문 조회
            List<KmToErpNewOrderDetailReq> orderDetailList = orderDao.selectKmToErpNewOrderDetailListByOrderDetailNoListAll(
                arrOrderDetailNo);
            List<KmToErpOrderPaymentReq> orderPaymentList = orderDao.selectKmToErpNewOrderPaymentListByOrderDetailNoListAll(
                arrOrderDetailNo);
            List<KmToErpOrderDiscountReq> orderDiscountList = orderDao.selectKmToErpNewOrderDiscountListByOrderDetailNoListAll(
                arrOrderDetailNo);
            List<KmToErpOrderDiscountDetailReq> orderDiscountDetailList = orderDao.selectKmToErpNewOrderDiscountDetailListByOrderDetailNoListAll(
                arrOrderDetailNo);
            List<KmToErpOrderShippingReq> orderShippingList = orderDao.selectKmToErpNewOrderShippingListByOrderDetailNoListAll(
                arrOrderDetailNo);

            dto.get(0).setOrderDetailList(orderDetailList);
            dto.get(0).setOrderPaymentList(orderPaymentList);
            dto.get(0).setOrderDiscountList(orderDiscountList);
            dto.get(0).setOrderDiscountDetailList(orderDiscountDetailList);
            dto.get(0).setOrderShippingList(orderShippingList);
        }

        return dto;
    }

    /**엑셀다운로드용 쿼리*/
    public List<OrderVo> getOrderInfoForExcelDown(OrderModel model) {
        return orderDao.selectOrderInfoForExcelDown(model);
    }

    /**배송추적 (굿스플로 재조회)*/
    public void goodsFlowShippingTracking(String orderDetailNo, String trackingNo) {
        trackingNo = trackingNo.replaceAll("-", "").replaceAll("    ", "").replaceAll(" ", "").replaceAll("\t","").replaceAll("\r\n","").replaceAll("\n","");
        OrdGfShippingVo gfVo = orderDao.selectOrdGfShipping(orderDetailNo, trackingNo);

        if(gfVo != null) {
            Map<String, String> header = new HashMap<>();
            header.put("accept", "application/json");
            header.put("Authorization", appProperties.getGoodsFlow().getApiKey());

            // 굿스플로 배송추적 정보 조회
            String response = RestUtil.getApiWithHeaders(MediaType.APPLICATION_JSON
                , appProperties.getGoodsFlow().getApiUrl() + "/api/deliveries/webhooks/"+gfVo.getGfServiceId()
                , header);

            long seq = 0;
            try {
                JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();

                BatchLogVo batchLog = new BatchLogVo();
                batchLog.setSystem("KM");
                batchLog.setTask("GoodsFlowShippingTracking");
                batchLog.setBatchStatus("PROGRESS");
                batchLog.setContents(jObject.toString());
                batchDao.insertBatchLog(batchLog);

                seq = batchLog.getSeq();
                batchLog = new BatchLogVo();
                StringBuffer sb = new StringBuffer();

                // 굿스플로 배송정보 로그 저장
                for (int i = 0; i < jObject.get("data").getAsJsonArray().size(); i++) {
                    JsonObject dataObj = jObject.get("data").getAsJsonArray().get(i).getAsJsonObject();

                    String serviceId = dataObj.get("serviceId").getAsString().trim();
                    String deliveryStatus = dataObj.get("deliveryStatus").getAsString().trim();
                    String invoiceNo = dataObj.get("invoiceNo").getAsString().trim();

                    List<OrdGfShippingVo> gfList = orderDao.selectGfShippingWithGfServiceId(serviceId, invoiceNo);
                    for(OrdGfShippingVo gf : gfList) {
                        OrdGfShippingLogVo logVo = new OrdGfShippingLogVo();
                        logVo.setGfServiceId(serviceId);
                        logVo.setOrderDetailNo(gf.getOrderDetailNo());
                        logVo.setGfDeliveryComp(dataObj.get("transporter").getAsString());
                        logVo.setDeliveryTrackingNo(invoiceNo);
                        logVo.setStatusDateTime(dataObj.get("statusDateTime").getAsString());
                        logVo.setGfOrdStatus(deliveryStatus);
                        logVo.setLocation(dataObj.get("location") != null ? dataObj.get("location") + "" : null);
                        logVo.setLocationPhone(dataObj.get("locationPhoneNo") != null ? dataObj.get("locationPhoneNo") + "" : null);
                        logVo.setDriverName(dataObj.get("driverName") != null ? dataObj.get("driverName") + "" : null);
                        logVo.setDriverPhone(dataObj.get("driverPhoneNo") != null ? dataObj.get("driverPhoneNo") + "" : null);
                        logVo.setErrorName(dataObj.get("errorName") != null ? dataObj.get("errorName") + "" : null);
                        logVo.setExceptionName(dataObj.get("exceptionName") != null ? dataObj.get("exceptionName") + "" : null);
                        int insertGfLogRes = orderDao.insertOrdGfShippingLog(logVo);
                        sb.append("\ninsertGfLog: " + insertGfLogRes);
                    }

                    int updateGfShippingRes = orderDao.updateOrdGfShipping(deliveryStatus, serviceId);
                    sb.append("\nupdateGfShipping: " + updateGfShippingRes);

                    if (deliveryStatus.equals("COMPLETED")) {
                        // 굿스플로 서비스아이디로 order_no 찾기
                        List<OrderVo> orderList = orderDao.selectOrderNoByGfServiceId(serviceId);
                        for(OrderVo order : orderList) {
                            String orderNo = order.getOrderNo();
                            if("ES".equals(order.getOrderType())) {
                                // 완료처리됐을때 교환완료상태로 변경
                                OrderSalesVo orderSalesVo
                                    = OrderSalesVo.builder()
                                    .orderNo(orderNo)
                                    .orderMainStatus("ME02")
                                    .regUserId("BATCH")
                                    .build();
                                int modifyOrdStatusRes = this.modifyOrdSalesStatus(orderSalesVo);
                                sb.append("\nmodifyOrdStatus: " + modifyOrdStatusRes);

                                // 상세주문 교환완료상태로 변경
                                OrderSalesDetailVo detailVo = new OrderSalesDetailVo();
                                detailVo.setOrderStatus("ME02");
                                detailVo.setTotalCancelPrice(0l);
                                detailVo.setRegUserId("BATCH");
                                detailVo.setOrderNo(orderNo);
                                int modifyOrdDetailStatusRes = orderDao.updateOrdSalesDetailStatus(detailVo);
                                sb.append("\nupdateOrdSalesDetailStatus: " + modifyOrdDetailStatusRes);

                                // 정산테이블 INSERT
                                SettlementVo stm = new SettlementVo();
                                List<Long> seqList = commonService.getSeqList("ORD_SETTLEMENT", 1);
                                stm.setSettlementSeq(seqList.get(0));
                                stm.setOrderDetailNo(order.getOrderDetailNo());
                                stm.setOrdSettlementTypeCode("EXCHANGE");
                                int insertOrdSettlement = orderDao.insertOrdSettlementForExchange(stm);
                                sb.append("\ninsertOrdSettlement: " + insertOrdSettlement);

                            }else if("SO".equals(order.getOrderType()) || "SR".equals(order.getOrderType())) {
                                if(!"MO05".equals(order.getOrderMainStatus())) {
                                    // 완료처리
                                    int updateOrdDetailShippingRes = orderDao.updateOrdSalesDetailShipping(
                                        serviceId
                                        , "BATCH"
                                        , "MO05"
                                    );
                                    sb.append("\nupdateOrdDetailShipping: " + updateOrdDetailShippingRes);

                                    // 처리해야할 주문건수
                                    int orderCnt = orderDao.selectNotCompleteOrderCount(orderNo);

                                    // 주문완료이면서 해당 주문에 더이상 처리해야할 주문이 없는 경우 주문 마스터 완료처리
                                    if (orderCnt == 0) {
                                        OrderSalesVo orderSalesVo
                                            = OrderSalesVo.builder()
                                            .orderNo(orderNo)
                                            .orderMainStatus("MO05")
                                            .regUserId("BATCH")
                                            .build();
                                        int modifyOrdStatusRes = this.modifyOrdSalesStatus(orderSalesVo);
                                        sb.append("\nmodifyOrdStatus: " + modifyOrdStatusRes);

                                        // 대기포인트 지급
                                        int updatePointRes = orderDao.updatePointWait(orderNo);
                                        sb.append("\nupdatePoint: " + updatePointRes);

                                        // ERP로 적립금 전송
                                        List<PointRewardVo> pointRewardList = orderDao.selectPointRewardListByOrderNo(orderNo);
                                        commonService.kmToErpSend("PATCH"
                                            ,"/api/v1/km-to-erp/point/list/modify-exec"
                                            ,AppUtil.convertObjectToJson(pointRewardList));
                                    }

                                    if(!"COMPLETED".equals(gfVo.getGfOrdStatus())) {
                                        // 배송완료후 리뷰작성 독려 알림톡
                                        OrderModel orderModel = new OrderModel();
                                        orderModel.setSearchOrderDetailNo(order.getOrderDetailNo());
                                        List<OrderVo> list = orderDao.selectOrderDetailList(null, orderModel);
                                        for (OrderVo orderVo : list) {
                                            if("SO".equals(orderVo.getOrderType())) {
                                                customerService.reviewWriteKkoMsg(
                                                    orderVo.getOrdererName()
                                                    , orderVo.getOrdererPhone()
                                                    , orderVo.getProductCode()
                                                    , orderVo.getOrderDetailNo()
                                                    , orderVo.getItemCode());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                batchLog.setBatchStatus("END");
                batchLog.setErrorContents(sb.toString());
                batchLog.setSeq(seq);
                batchDao.updateBatchLog(batchLog);

            }catch (Exception e) {
                BatchLogVo batchLog = new BatchLogVo();
                batchLog.setBatchStatus("ERROR");
                batchLog.setErrorContents(e.getMessage());
                batchLog.setSeq(seq);
                batchDao.updateBatchLog(batchLog);
            }
        }
    }

    /**
     * 정산취소
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void cancelSettlement(String orderNo, String orderDetailNo, String customerSeq) {
        try {
            orderDao.updateOrdSettlement(orderNo, orderDetailNo, customerSeq);
        }catch(Exception e) {
            log.error("[정산취소 오류] orderNo : {}", orderNo, e);
            throw e;
        }
    }

    /**orderNo의 상세주문개수 조회*/
    public int getCountOrderDetail(String orderNo) {
        return orderDao.selectCountOrderDetail(orderNo);
    }

    /**배송비쿠폰 정보 조회*/
    public CouponVo getShippingCouponInfo(String orderDetailNo) {
        return orderDao.selectShippingCouponInfo(orderDetailNo);
    }

    /**취소로직에서 배송비 발생시 정산데이터 수정*/
    public void modifyOrdSettlementShippingCost(String regUserId, int shippingCost, String orderNo, String orderDetailNo) {
        orderDao.updateOrdSettlementShippingCost(regUserId, shippingCost, orderNo, orderDetailNo);
    }

    /**할인 상세정보 조회*/
    public List<OrderSalesDiscountDetailVo> getDiscountDetail(String orderNo) {
        return orderDao.selectDiscountDetail(orderNo);
    }

    /**주문상세 할인정보 수정*/
    public Integer modifyOrderDetailDiscountPrice(OrderVo vo) {
        return orderDao.updateOrderDetailDiscountPrice(vo);
    }

    /**주문상세 / 할인 정보 수정*/
    public Integer modifyOrderDetailInfo(OrderSalesDiscountDetailVo detail) {
        return orderDao.updateOrderDetailInfo(detail);
    }

    /**반품완료시 주문상세 개수 조회*/
    public List<OrderVo > getOrderDetailForReturnExchange(String orderNo) {
        return orderDao.selectOrderDetailForReturnExchange(orderNo);
    }

    /**장바구니쿠폰 메인 취소*/
    public Integer modifyDiscountListCancelForCartCoupon(Long seq) {
        return orderDao.updateDiscountListCancelForCartCoupon(seq);
    }

    /**
     * 주문 전체 조회 카운트
     */
    public int getOrderTotalDetailCount(OrderModel orderModel) {
        return orderDao.selectOrderTotalDetailCount(orderModel);
    }

    /**
     * 주문 전체 조회 리스트
     */
    public Object getOrderTotalDetailList(Pagination pagination, OrderModel orderModel) {
        List<OrderVo> res = orderDao.selectOrderTotalDetailList(pagination, orderModel);
        for (OrderVo vo : res) {
            List<ProductContentsVo> contentsList = productDao.selectProductContentsList(vo.getProductCode());
            if(contentsList.size() > 0) {
                vo.setContentsPath(contentsList.get(0).getContentsPath());
            }

            vo.setPaymentYn("Y".equals(vo.getPaymentYn()) ? "결제완료" : "미결제");

            // 배송시작일 세팅
            OrdGfShippingLogVo statusDateVo = orderDao.selectGfStatusDateTime("DLV_START", vo.getOrderNo(), vo.getOrderDetailNo());
            if(null != statusDateVo) {
                vo.setDlvStartDate(statusDateVo.getStatusDateTime());
            }else {
                vo.setDlvStartDate("배송시작 전");
            }

            // 주문번호로 굿스플로 로그 조회
            // MO04 배송중이고
            // status_date_time + lateDay가 현재시간(SYSDATE)보다 작거나 같고
            // status_date_time가 DLV_START : 배송출발의 status_date_time과  status_date_time + lateDay 사이의 목록
            List<OrdGfShippingLogVo> gfLogList = orderDao.selectLateDeliveryProcess(vo.getOrderNo(), 0);
            int completeCnt = 0;
            for(OrdGfShippingLogVo logVo : gfLogList) {
                if("COMPLETED".equals(logVo.getGfOrdStatus())) {
                    completeCnt++;
                }
            }
            if(gfLogList.size() > 0) {
                if(completeCnt > 0) {
                    vo.setIsDeliveryLate("N");
                }else {
                    // COMPLETED가 없으면 배송지연으로 판단
                    vo.setIsDeliveryLate("Y");
                }
            }else {
                // 목록에 없으면 정상배송
                vo.setIsDeliveryLate("N");
            }
        }
        return res;
    }

    /**신한은행 토큰 발급*/
    public String getShinhanToken(BatchLogVo batchLog) {
        long seq = 0;
        if(null != batchLog) {
            seq = batchLog.getSeq();
        }
        String ret = "";
        // access token 발급 위해 필요한 기본 정보
        final String API_KEY = appProperties.getShinhanApi().getApiKey();
        final String SECRET_KEY = appProperties.getShinhanApi().getScretKey();
        String scope = "oob";
        String grant_type = "client_credentials";

        OutputStream os = null;
        BufferedReader reader = null;

        try {
            // client hash 생성
            // "unix time stamp+'|'+client id" 를 client secret 으로 Hmac SHA256 후 Base64 encoding & URL encoding

            // HMAC SHA256 알고리즘을 사용하도록 설정
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            // 현재 시간을 Instant 객체로 얻음
            Instant now = Instant.now();

            // Instant 객체를 Epoch Second로 변환하여 Unix 타임스탬프를 얻음
            long unixTimestamp = now.getEpochSecond();

            String msg = unixTimestamp + "|" + API_KEY;

            // 메시지에 대해 HMAC을 계산
            byte[] hash = sha256_HMAC.doFinal(msg.getBytes());

            // 결과를 Base64로 인코딩
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // http body 세팅
            Map<String, Object> httpBody = new HashMap<>();
            httpBody.put("client_id", API_KEY);
            httpBody.put("scope", scope);
            httpBody.put("grant_type", grant_type);
            httpBody.put("client_hash", hashBase64);
            httpBody.put("timestamp", unixTimestamp);

            String response = RestUtil.postApiWithHeadersAndQueryParams(
                appProperties.getShinhanApi().getTokenUrl()
                , header, httpBody);

            System.out.println("RESPONSE DATA ===================");
            JsonObject jObject = JsonParser.parseString(response).getAsJsonObject();
            JsonObject head = jObject.getAsJsonObject("dataHeader");
            JsonObject body = jObject.getAsJsonObject("dataBody");
            if(head.size() > 0) {
                ret = "ERROR";
                log.error(head.toString());
                if(null != batchLog) {
                    batchLog.setBatchStatus("ERROR");
                    batchLog.setErrorContents(head.toString());
                    batchLog.setSeq(seq);
                    batchDao.updateBatchLog(batchLog);
                }

            }
            if(body.size() > 0) {
                ret = body.get("access_token").getAsString();
                log.info(body.toString());
                if(null != batchLog) {
                    batchLog.setErrorContents(body.toString());
                }
            }
            System.out.println("RESPONSE DATA ===================");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**신한은행 거래내역 조회*/
    public void getShinhanTransaction(BatchLogVo batchLog, ShinhanTransactionModel model) {
        long seq = 0;
        if(null != batchLog) {
            seq = batchLog.getSeq();
        }
        final String API_KEY = appProperties.getShinhanApi().getApiKey();
        final String SECRET_KEY = appProperties.getShinhanApi().getScretKey();
        final String TOKEN_TYPE = "Bearer";
        final String TEST_URL = appProperties.getShinhanApi().getTransactionUrl();
        String accessToken = this.getShinhanToken(batchLog);
        String hashKey = "";

        if(!"ERROR".equals(accessToken)) {
            OutputStream os = null;
            BufferedReader reader = null;

            // http body json 생성
            JSONObject dataHeader = new JSONObject();
            dataHeader.put("subChannel", appProperties.getShinhanApi().getSubChannel());
            dataHeader.put("성별", "F");
            dataHeader.put("연령대", "30");

            JSONObject dataBody = new JSONObject();
            dataBody.put("계좌번호", appProperties.getShinhanApi().getAccountNo());
            dataBody.put("조회시작일", model.getStartDate());
            dataBody.put("조회종료일", model.getEndDate());

            JSONObject httpBody = new JSONObject();
            httpBody.put("dataHeader", dataHeader);
            httpBody.put("dataBody", dataBody);

            String strHttpBody = httpBody.toString();

            System.out.println("REQUEST DATA ===================");
            log.info(strHttpBody);

            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

                // SSL 연결
                URL url = new URL(TEST_URL);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                // client hash 생성
                // http body 를 client secret 로 Hmac SHA256 후 Base64 encoding
                // HMAC SHA256 알고리즘을 사용하도록 설정
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
                sha256_HMAC.init(secret_key);

                // 메시지에 대해 HMAC을 계산
                byte[] hash = sha256_HMAC.doFinal(strHttpBody.getBytes());

                // 결과를 Base64로 인코딩
                hashKey = Base64.getEncoder().encodeToString(hash);

                // http header 세팅
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
                conn.setRequestProperty("Authorization", TOKEN_TYPE+" "+accessToken);
                conn.setRequestProperty("charset", "UTF-8");
                conn.setRequestProperty("apikey", API_KEY);
                conn.setRequestProperty("hsKey", hashKey);

                // 요청 송신
                byte[] buf = strHttpBody.getBytes("UTF-8");
                os = conn.getOutputStream();
                os.write(buf, 0, buf.length);
                os.flush();

                // 응답 수신
                System.out.println("RESPONSE DATA ===================");
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String buff = "";

                while ((buff = reader.readLine()) != null) {
                    log.info(buff);
                    if(null != batchLog) {
                        batchLog.setErrorContents(batchLog.getErrorContents() + " TRANSACTIONLIST: " + buff);
                    }

                    if(null != buff && !"".equals(buff)) {
                        List<ShinhanTransactionVo> list = new ArrayList<>();
                        JsonObject jObject = JsonParser.parseString(buff).getAsJsonObject();
                        JsonObject head = jObject.getAsJsonObject("dataHeader");
                        JsonObject body = jObject.getAsJsonObject("dataBody");
                        if(head.size() > 0) {
                            String succCd = head.get("successCode").getAsString();
                            if ("0".equals(succCd)) {
                                if (body.size() > 0) {
                                    JsonArray arr = body.getAsJsonArray("거래내역");
                                    log.info("{}", arr);

                                    for (int i = 0; i < arr.size(); i++) {
                                        JsonObject obj = arr.get(i).getAsJsonObject();
                                        log.info("{}", obj);

                                        String transactionDay = obj.get("거래일자").getAsString();
                                        String transactionTime = obj.get("거래시간").getAsString();
                                        String transactionName = obj.get("거래점명").getAsString();
                                        String memo = obj.get("거래메모").getAsString();

                                        model.setTransactionDay(transactionDay);
                                        model.setTransactionTime(transactionTime);
                                        model.setTransactionName(transactionName);
                                        model.setMemo(memo);

                                        List<ShinhanTransactionVo> dataList = orderDao.selectShinhanTransactionList(null, model);

                                        if (dataList.size() == 0) {
                                            ShinhanTransactionVo vo = new ShinhanTransactionVo();
                                            vo.setAccountNo(body.get("계좌번호").getAsString());
                                            vo.setAccountName(body.get("고객명").getAsString());
                                            vo.setAccountRemainPrice(body.get("계좌잔액").getAsLong());
                                            vo.setTransactionDay(transactionDay);
                                            vo.setTransactionTime(transactionTime);
                                            vo.setWithdrawPrice(obj.get("출금금액").getAsLong());
                                            vo.setDepositPrice(obj.get("입금금액").getAsLong());
                                            vo.setMemo(memo);
                                            vo.setRemainPrice(obj.get("잔액").getAsLong());
                                            vo.setTransactionName(transactionName);
                                            vo.setLocationType(obj.get("입지구분").getAsString());
                                            vo.setCmsNo(obj.get("CMS번호").getAsString());
                                            // UserVo user = SessionUtil.getLoginSession();
                                            vo.setRegUserId("BATCH");
                                            list.add(vo);
                                        }
                                    }
                                    if(list.size() > 0) {
                                        int insertRet = orderDao.saveShinhanTransactionList(list);
                                        if (null != batchLog) {
                                            batchLog.setErrorContents(batchLog.getErrorContents() + " INSERT: " + insertRet);
                                        }
                                    }

                                }

                            } else if ("1".equals(succCd)) {
                                log.error(head.toString());
                                if (null != batchLog) {
                                    batchLog.setBatchStatus("ERROR");
                                    batchLog.setErrorContents(head.toString());
                                    batchLog.setSeq(seq);
                                    batchDao.updateBatchLog(batchLog);
                                }
                            }
                        }
                    }
                    System.out.println("RESPONSE DATA ===================");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                os = null;
                reader = null;
            }
        }
        if (null != batchLog) {
            if(!"ERROR".equals(batchLog.getBatchStatus())) {
                batchLog.setBatchStatus("END");
                batchLog.setSeq(seq);
                batchDao.updateBatchLog(batchLog);
            }
        }
    }

    /**신한은행 거래내역 카운트*/
    public int getShinhanTransactionCount(ShinhanTransactionModel model) {
        return orderDao.selectShinhanTransactionCount(model);
    }

    /**신한은행 거래내역 조회*/
    public List<ShinhanTransactionVo> getShinhanTransactionList(Pagination page, ShinhanTransactionModel model) {
        return orderDao.selectShinhanTransactionList(page,model);
    }

    /**신한은행 거래내역 상세*/
    public ShinhanTransactionVo getShinhanTransactionDetail(long seq) {
        return orderDao.selectShinhanTransactionDetail(seq);
    }

    /**신한은행 거래내역 관리자메모 저장*/
    public int modifyShinhanTransactionAdminMemo(ShinhanTransactionVo vo) {
        return orderDao.updateShinhanTransaction(vo);
    }

    /**에스크로 상태변경 (배송등록)*/
    public void kiccReviseForEscrow(KiccAuthenticationReqModel kiccAuthenticationReqModel) {
        //가맹점 트랜잭션 ID
        kiccAuthenticationReqModel.setShopTransactionId(KiccUtil.getKiccTID());
        //승인요청일자(취소)
        kiccAuthenticationReqModel.setApprovalReqDate(DateUtil.getDate(DateUtil.FORMAT_YYYYMMDD, 0));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Charset", "utf-8");

        String message = kiccAuthenticationReqModel.getPgCno() + "|" + kiccAuthenticationReqModel.getShopTransactionId();
        StringBuilder msgAuthValue = new StringBuilder();

        try {
            Mac sha256_HMAC = Mac.getInstance( "HmacSHA256" );
            SecretKeySpec secret_key = new SecretKeySpec( kiccProperties.getSecretKey().getBytes(), "HmacSHA256" );
            sha256_HMAC.init( secret_key );
            byte[] hash = sha256_HMAC.doFinal( message.getBytes() ); // hash 값을 HexString 으로 변환하세요
            for(final byte b : hash) {
                msgAuthValue.append(String.format("%02x", b&0xff));
            }
        } catch(Exception e) {
            log.error("[KICC 결제취소 - msgAuthValue 생성 실패]", e);
        }

        Gson gson = new Gson();
        JsonObject params = new JsonObject();
        params.addProperty("mallId", kiccProperties.getMallId());
        params.addProperty("shopTransactionId", kiccAuthenticationReqModel.getShopTransactionId());
        params.addProperty("pgCno", kiccAuthenticationReqModel.getPgCno());
        params.addProperty("reviseTypeCode", kiccAuthenticationReqModel.getReviseTypeCode());
        if (kiccAuthenticationReqModel.getReviseSubTypeCode() != null) {
            params.addProperty("reviseSubTypeCode", kiccAuthenticationReqModel.getReviseSubTypeCode());
        }
        params.addProperty("clientIp", kiccAuthenticationReqModel.getClientIp());
        params.addProperty("clientId", "THEKARY");
        params.addProperty("msgAuthValue", msgAuthValue.toString());
        params.addProperty("cancelReqDate", kiccAuthenticationReqModel.getApprovalReqDate());

        JsonObject escrowInfo = new JsonObject();
        escrowInfo.addProperty("deliveryCode", "DE02");
        escrowInfo.addProperty("deliveryCorpCode", kiccAuthenticationReqModel.getDeliveryCompCode());
        escrowInfo.addProperty("deliveryInvoice", kiccAuthenticationReqModel.getShippingNo());
        params.add("escrowInfo", escrowInfo);

        HttpEntity<?> entity = new HttpEntity<>(gson.toJson(params), headers);

        URI uri = UriComponentsBuilder.fromUriString(kiccProperties.getTradeReviseUrl())
            .build()
            .encode()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KiccReviseResModel> responseEntity =
            restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<KiccReviseResModel>() {});

        if( responseEntity.getStatusCode() == HttpStatus.OK ) {
            // 통신성공
            KiccReviseResModel kiccReviseResModel = responseEntity.getBody();
            kiccReviseResModel.setClientIp(kiccAuthenticationReqModel.getClientIp());
            kiccReviseResModel.setShopOrderNo(kiccAuthenticationReqModel.getShopOrderNo());
            kiccReviseResModel.setShopTransactionId(kiccAuthenticationReqModel.getShopTransactionId());


            // 로그 쌓기
            boolean isSuccess  = this.modifyKiccReviseOrder(kiccReviseResModel);
            if( !isSuccess ) {
                // 로그 쌓기 실패
                log.error("[KICC 결제취소 - 로그 쌓기 실패]");
            }
            // 로그 쌓기 성공
            if ("0000".equals(
                responseEntity.getBody().getResCd())){
                log.info("[KICC 결제취소 - 성공] 상태 코드: {}", responseEntity.getBody().getResCd());
            } else {
                kiccAuthenticationReqModel.setErrorMsg(responseEntity.getBody().getResMsg());
                log.info("[KICC 결제취소 - 실패] 상태 코드: {}", responseEntity.getBody().getResCd());
            }
        } else {
            // 통신 실패
            log.error("[KICC 결제취소 - 통신 실패] 상태 코드: {}", responseEntity.getStatusCodeValue());
        }
    }

    /**상품불량이나 품절로 주문취소시 재고를 0으로 저장*/
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyStockZeroForOrderCancel(String orderNo, String orderDetailNo, String customerSeq, String cancelReasonCode) {
        orderDao.updateStockZeroForOrderCancel(orderNo, orderDetailNo, customerSeq, cancelReasonCode);
    }

    /**일괄 굿스플로 배송추적*/
    public void bulkGoodsFlowShippingTracking(OrderModel orderModel) {
        String[] arrOrderDetailNo = orderModel.getArrOrderDetailNo();
        String[] arrTrackingNo = orderModel.getArrTrackingNo();

        for(int i=0; i<arrOrderDetailNo.length; i++) {
            this.goodsFlowShippingTracking(arrOrderDetailNo[i], arrTrackingNo[i]);
        }
    }

    /**
     * 굿스플로 배송상태 변경을 위한 호출
     */
    public List<OrdShippingVo> getShippingListForFinishShipping() {
        return orderDao.selectShippingListForFinishShipping();
    }

    /**
     * 배송 준비중 주문에 대해 배송지를 변경한다.
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrderShippingAddress (OrderShippingAddressModifyReqDto reqDto) throws Exception {

        // 주문번호 디테일을 조회한다.
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderNo(reqDto.getOrderNo());

        OrdSalesVo orderSalesVo = OrdSalesVo.builder()
                .orderNo(reqDto.getOrderNo())
                .postCode(reqDto.getPostCode())
                .address(reqDto.getAddress())
                .addressDetail(reqDto.getAddressDetail())
                .recipientName(reqDto.getInputExcName())
                .recipientMobileNo(reqDto.getInputExcMobile())
                .recipientTelNo(reqDto.getInputExcTel())
                .build();

        List<OrderVo> orderDetailVoList = orderDao.selectOrderDetailList(null, orderModel);
        OrderSalesVo orderMasterVo = orderDao.selectSalesByPk(orderModel.getOrderNo());

        // 주문디테일의 주문 상태가 모두 배송 준비 중 인지 체크 한다.
        long orderStatusInvalidCount = orderDetailVoList.stream().filter(order -> !"MO03".equals(order.getOrderStatus())).count();

        // 배송준비중 상태가 아니라면 리턴한다.
        if(orderStatusInvalidCount > 0) {
            throw new Exception("배송 준비중 상태가 아닌 주문이 포함되어 있습니다.");
        }

        // 배송정보가 변경된게 맞는지 체크한다. (배송지 변경이 없다면 리턴한다.)
        if (reqDto.getAddress().equals(orderMasterVo.getAddress()) && reqDto.getAddressDetail().equals(orderMasterVo.getAddressDetail())) {
            throw new Exception("주문 배송지 정보가 변경되지 않았습니다.");
        }

        // 파트너를 통해 주문 배송지 변경정보를 전달한다.
        boolean apiResult = this.kmToErpModifyShippingAddress(reqDto);

        if (apiResult) {
            // 주문 배송지 정보를 수정 한다.
            orderDao.updateShippingAddress(orderSalesVo);
        } else {
            throw new Exception("KM TO ERP 배송지 변경 API 호출 실패");
        }
    }

    /**
     * API : KM TO ERP 배송지 변경
     */
    public boolean kmToErpModifyShippingAddress(OrderShippingAddressModifyReqDto reqDto) {
        String apiUrl = appProperties.getKmToErp().getApiUrl();
//        String apiUrl = "http://localhost:8082";
        ApiAuthVo apiAuth = commonService.getApiAuthByApiTypeCode("KM-TO-ERP");

        //API 호출 후 등록
        String json = AppUtil.convertObjectToJson(reqDto);

        // 토큰 등록
        Map<String, String> header = new HashMap<>();
        header.put("id-key", apiAuth.getIdKey());
        header.put("secret-key", apiAuth.getSecretKey());

        // 헤더와 함께 POST API 호출
        try {
            String apiResult = RestUtil.patchApi(MediaType.APPLICATION_JSON
                    , apiUrl + "/api/v1/km-to-erp/order/shipping/address/modify-exec",
                    json, header);

            if ("S".equals(apiResult) ) {
                return false;
            } else {
                return true;
            }
        }catch( Exception e){
            log.error("KM TO ERP 배송지 변경 API 호출 실패", e);
            return false;
        }

    }

    /**주문취소사유 조회*/
    public OrdCancelModel getOrdCancelInfo(String orderNo) {
        return orderDao.selectOrdCancelInfo(orderNo);
    }

    /**주문 타임세일 할인정보목록 조회*/
    public List<TimesaleVo> getOrderTimesaleList(String orderNo) {
        return orderDao.selectOrderTimesaleList(orderNo);
    }

}
