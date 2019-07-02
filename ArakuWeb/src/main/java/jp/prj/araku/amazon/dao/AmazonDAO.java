package jp.prj.araku.amazon.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.amazon.mapper.IAmazonMapper;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.file.vo.ClickPostVO;
import jp.prj.araku.file.vo.SagawaVO;
import jp.prj.araku.file.vo.YamatoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

/**
 * [MOD-1011] 半角→全角へ変換する。　　kim
 * */
@Repository
public class AmazonDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.amazon");
	
	public void insertAmazonInfo(MultipartFile amaUpload, String fileEncoding) throws IOException {
		log.info("insertAmazonInfo");
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		BufferedReader reader = null;
        try {
			reader = new BufferedReader(
			                new InputStreamReader(amaUpload.getInputStream(), fileEncoding));
			String line = "";
			String splitBy = "	";
			int cnt = 0;
			
			while ((line = reader.readLine()) != null) {
		        String[] arr = line.split(splitBy);
		        log.debug(arr[0]);
		        if(cnt == 0) {
		        	cnt++;
		        	// 컬럼 헤더명이 나올 경우 다음 레코드로 진행
		        	continue;
		        } else {
		        	// 헤더 이후의 데이터 처리
		        	AmazonVO vo = new AmazonVO();
		        	vo.setOrder_id(arr[0]);
		        	vo.setOrder_item_id(arr[1]);
		        	vo.setPurchase_date(arr[2]);
		        	vo.setPayments_date(arr[3]);
		        	vo.setReporting_date(arr[4]);
		        	vo.setPromise_date(arr[5]);
		        	vo.setDays_past_promise(arr[6]);
		        	vo.setBuyer_email(arr[7]);
		        	vo.setBuyer_name(arr[8]);
		        	vo.setBuyer_phone_number(arr[9]);
		        	vo.setSku(arr[10]);
		        	vo.setProduct_name(arr[11]);
		        	vo.setQuantity_purchased(arr[12]);
		        	vo.setQuantity_shipped(arr[13]);
		        	vo.setQuantity_to_ship(arr[14]);
		        	vo.setShip_service_level(arr[15]);
		        	vo.setRecipient_name(arr[16]);
		        	vo.setShip_address1(arr[17]);
		        	vo.setShip_address2(arr[18]);
		        	vo.setShip_address3(arr[19]);
		        	vo.setShip_city(arr[20]);
		        	vo.setShip_state(arr[21]);
		        	vo.setShip_postal_code(arr[22]);
		        	vo.setShip_country(arr[23]);
		        	vo.setPayment_method(arr[24]);
		        	vo.setCod_collectible_amount(arr[25]);
		        	vo.setAlready_paid(arr[26]);
		        	vo.setPayment_method_fee(arr[27]);
		        	vo.setScheduled_delivery_start_date(arr[28]);
		        	vo.setScheduled_delivery_end_date(arr[29]);
		        	vo.setPoints_granted(arr[30]);
		        	vo.setIs_prime(arr[31]);
		        	
		        	ArrayList<AmazonVO> dupCheck = mapper.getAmazonInfo(vo);
		        	if (dupCheck.size() > 0) {
		        		log.debug("already have data");
		        	} else {
		        		int result = mapper.insertAmazonInfo(vo);
			        	log.debug(String.format("inserted amazon info seq_id : [%d]", result));
		        	}
		        	
		        	// 項目・選択肢 (상품옵션) 처리
	                TranslationVO transVO = new TranslationVO();
	                transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
	                transVO.setKeyword(vo.getProduct_name());
	                transVO.setBefore_trans(vo.getProduct_name());
	                
	                ArrayList<TranslationVO> transList = listMapper.getTransInfo(transVO);
	                if (transList.size() == 0) {
	                	listMapper.addTransInfo(transVO);
	                }
		        	
		        	cnt++;
		        }
			}
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
	}
	
	public ArrayList<AmazonVO> getAmazonInfo(AmazonVO vo) {
		log.info("getAmazonInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug(vo);
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		return mapper.getAmazonInfo(vo);
	}
	
	@Transactional
	public ArrayList<String> executeTranslate(ArrayList<AmazonVO> targetList) {
		log.info("executeTranslate");
		log.debug(targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		IAmazonMapper amazonMapper = sqlSession.getMapper(IAmazonMapper.class);
		
		TranslationVO transVO = new TranslationVO();
		String transedName;
		
		for (AmazonVO vo : targetList) {
			// 이전에 에러처리 된 데이터가 있을경우 제거
			TranslationErrorVO errVO = new TranslationErrorVO();
			errVO.setTrans_target_id(vo.getSeq_id());
			errVO.setTrans_target_type(CommonUtil.TRANS_TARGET_A);
			String err_seq_id = listMapper.getTranslationErr(errVO);
			if (err_seq_id != null && err_seq_id != "") {
				errVO.setSeq_id(err_seq_id);
				listMapper.deleteTranslationErr(errVO);
			}
			
			transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			transVO.setKeyword(vo.getProduct_name());
			ArrayList<TranslationVO> searchRet = listMapper.getTransInfo(transVO);
			
			if(searchRet.size() > 0) {
				// 치환후 상품명
				transedName = searchRet.get(0).getAfter_trans();
				// 치환한 결과가 없을 경우 에러처리
				if (transedName == null) {
					errVO.setErr_text(CommonUtil.TRANS_ERR);
					listMapper.insertTranslationErr(errVO);
				}
			} else {
				errVO.setErr_text(CommonUtil.TRANS_ERR);
				listMapper.insertTranslationErr(errVO);
				transedName = "";
			}
			
			// 지역별 배송코드 세팅 (csv다운로드 기능)
			RegionMasterVO rmVO = new RegionMasterVO();
			String state = vo.getShip_state();
			if (state.contains("-")) {
				state = state.split("-")[0];
			}
			rmVO.setKeyword(state);
			ArrayList<RegionMasterVO> regionM = listMapper.getRegionMaster(rmVO);
			
			vo.setDelivery_company(regionM.get(0).getDelivery_company());
			log.debug("Update Amazon info : " + vo);
			amazonMapper.updateAmazonInfo(vo);
			
			TranslationResultVO resultVO = new TranslationResultVO();
			resultVO.setTrans_target_id(vo.getSeq_id());
			resultVO.setTrans_target_type(CommonUtil.TRANS_TARGET_A);
//			resultVO.setResult_text(transedName + "*" + vo.getQuantity_to_ship()); // [MOD-1011] 
			
			// [MOD-1011] 
			Integer intsu = new Integer (vo.getQuantity_to_ship()); 
			String sintsu = intsu.toString(); 
			String su = CommonUtil.hankakuNumToZenkaku(sintsu); 
			resultVO.setResult_text(transedName + "×" + su); 
			
			// 이미 치환된 결과가 있는 trans_target_id이면 update, 아니면 insert
			ArrayList<AmazonVO> transResult = amazonMapper.getTransResult(resultVO);
			if (transResult.size() > 0) {
				listMapper.modTransResult(resultVO);
				
				log.debug("seq_id : " + transResult.get(0).getSeq_id());
				ret.add(transResult.get(0).getSeq_id());
			} else {
				listMapper.addTransResult(resultVO);
				
				log.debug("seq_id : " + resultVO.getSeq_id());
				ret.add(resultVO.getSeq_id());
			}
		}
		
		return ret;
	}
	
	public ArrayList<AmazonVO> getTransResult(String id_lst) {
		log.info("getTransResult");
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] strArr = id_lst.split(",");
		ArrayList<String> list = new ArrayList<>();
		for (String str : strArr) {
			list.add(str);
		}
		log.debug("query id list : " + list);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(list);
		return mapper.getTransResult(vo);
	}
	
	public void deleteAmazonInfo(ArrayList<AmazonVO> list) {
		log.info("deleteAmazonInfo");
		log.debug(list);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		for (AmazonVO vo : list) {
			mapper.deleteAmazonInfo(vo.getSeq_id());
		}
	}
	
	public void yamatoFormatDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String delivery_company
			, String isChecked) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("yamatoFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "YAM" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(response.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			String[] header = 
			{
				"お客様管理番号"
				, "送り状種類"
				, "クール区分"
				, "伝票番号"
				, "出荷予定日"
				, "お届け予定日"
				, "配達時間帯"
				, "お届け先コード"
				, "お届け先電話番号"
				, "お届け先電話番号枝番"
				, "お届け先郵便番号"
				, "お届け先住所"
				, "お届け先アパートマンション名"
				, "お届け先会社・部門１"
				, "お届け先会社・部門２"
				, "お届け先名"
				, "お届け先名(ｶﾅ)"
				, "敬称"
				, "ご依頼主コード"
				, "ご依頼主電話番号"
				, "ご依頼主郵便番号"
				, "ご依頼主住所"
				, "ご依頼主アパートマンション"
				, "ご依頼主名"
				, "ご依頼主名(ｶﾅ)"
				, "品名コード１"
				, "品名１"
				, "品名コード２"
				, "品名２"
				, "荷扱い１"
				, "荷扱い２"
				, "記事"
				, "ｺﾚｸﾄ代金引換額（税込)"
				, "内消費税額等"
				, "止置き"
				, "営業所コード"
				, "発行枚数"
				, "個数口表示フラグ"
				, "請求先顧客コード"
				, "請求先分類コード"
				, "運賃管理番号"
				, "クロネコwebコレクトデータ登録"
				, "クロネコwebコレクト加盟店番号"
				, "クロネコwebコレクト申込受付番号１"
				, "クロネコwebコレクト申込受付番号２"
				, "クロネコwebコレクト申込受付番号３"
				, "お届け予定ｅメール利用区分"
				, "お届け予定ｅメールe-mailアドレス"
				, "入力機種"
				, "お届け予定ｅメールメッセージ"
				, "お届け完了ｅメール利用区分"
				, "お届け完了ｅメールe-mailアドレス"
				, "お届け完了ｅメールメッセージ"
				, "クロネコ収納代行利用区分"
				, "予備"
				, "収納代行請求金額(税込)"
				, "収納代行内消費税額等"
				, "収納代行請求先郵便番号"
				, "収納代行請求先住所"
				, "収納代行請求先住所（アパートマンション名）"
				, "収納代行請求先会社・部門名１"
				, "収納代行請求先会社・部門名２"
				, "収納代行請求先名(漢字)"
				, "収納代行請求先名(カナ)"
				, "収納代行問合せ先名(漢字)"
				, "収納代行問合せ先郵便番号"
				, "収納代行問合せ先住所"
				, "収納代行問合せ先住所（アパートマンション名）"
				, "収納代行問合せ先電話番号"
				, "収納代行管理番号"
				, "収納代行品名"
				, "収納代行備考"
				, "複数口くくりキー"
				, "検索キータイトル1"
				, "検索キー1"
				, "検索キータイトル2"
				, "検索キー2"
				, "検索キータイトル3"
				, "検索キー3"
				, "検索キータイトル4"
				, "検索キー4"
				, "検索キータイトル5"
				, "検索キー5"
				, "予備"
				, "予備"
				, "投函予定メール利用区分"
				, "投函予定メールe-mailアドレス"
				, "投函予定メールメッセージ"
				, "投函完了メール（お届け先宛）利用区分"
				, "投函完了メール（お届け先宛）e-mailアドレス"
				, "投函完了メール（お届け先宛）メールメッセージ"
				, "投函完了メール（ご依頼主宛）利用区分"
				, "投函完了メール（ご依頼主宛）e-mailアドレス"
				, "投函完了メール（ご依頼主宛）メールメッセージ"
			};
			
			// 야마토 포맷으로 바꾸기 전 치환된 결과와 함께 아마존 정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			vo.setDelivery_company(delivery_company);
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> yList = new ArrayList<>();
			
			for (AmazonVO tmp : list) {
				// 빠른배송을 옵션으로 둔 항목에 대하여 체크가 되어있으면 제외
				if ("1".equals(isChecked)) {
					if ("NextDay".equals(tmp.getShip_service_level())) {
						log.debug("tomorrow hope checked and excepted!");
						continue;
					}
				}
				
				// 예외테이블에 추가한 목록에 대하여 야마토에서 제외
				ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
				boolean chkRet = false;
				for (ExceptionMasterVO exVO : exList) {
					if (tmp.getResult_text().contains(exVO.getException_data())) {
						log.debug(String.format("exception_data: %s - result_txt: %s", exVO.getException_data(), tmp.getResult_text()));
						chkRet = true;
					}
				}
				if (chkRet) {
					continue;
				}
				YamatoVO yVO = new YamatoVO();
				yVO.setCustomer_no(tmp.getOrder_id().replace("\"", ""));
				yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				if ("COD".equals(tmp.getPayment_method())) {
					yVO.setCollect_cash(tmp.getCod_collectible_amount().replace("\"", ""));
				}
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048299821004-311");
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				yVO.setClient_name("有限会社ItempiaJapan (A)");
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getShip_postal_code().replace("\"", "").replace("-", ""));
				yVO.setDelivery_add(tmp.getShip_state().replace("\"", "") + "" + tmp.getShip_address1().replace("\"", "") + "" + tmp.getShip_address2().replace("\"", "") + "" + tmp.getShip_address3().replace("\"", ""));
				yVO.setDelivery_name(tmp.getRecipient_name().replace("\"", ""));
				yVO.setDelivery_name_title(CommonUtil.TITLE_SAMA);
				String phone_no = tmp.getBuyer_phone_number();
				if (phone_no.contains("-")) {
					yVO.setDelivery_tel(phone_no);
				} else {
					if (phone_no.length() == 10) {
						yVO.setDelivery_tel(phone_no.substring(0, 2) + "-" + phone_no.subSequence(2, 6) + "-" + phone_no.subSequence(6, 10));
					} else if (phone_no.length() == 11) {
						yVO.setDelivery_tel(phone_no.substring(0, 3) + "-" + phone_no.subSequence(3, 7) + "-" + phone_no.subSequence(7, 11));
					}
				}
//				yVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
				
				// 配送サービスレベル가 NextDay인 경우
        		if ("NextDay".equals(tmp.getShip_service_level())) {
        			yVO.setDelivery_time(CommonUtil.YA_TOMORROW_MORNING_CODE);
        		}
				
				yVO.setProduct_name1(tmp.getResult_text().replace("\"", ""));
				
				// csv작성을 위한 리스트작성
				yList.add(yVO);
			}
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, yList);
			
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void sagawaFormatDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String delivery_company
			, String isChecked) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("sagawaFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "SAGA" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(response.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			String[] header = 
			{
					"住所録コード"
					, "お届け先電話番号"
					, "お届け先郵便番号"
					, "お届け先住所１（必須）"
					, "お届け先住所２"
					, "お届け先住所３"
					, "お届け先名称１（必須）"
					, "お届け先名称２"
					, "お客様管理ナンバー"
					, "お客様コード"
					, "部署・担当者"
					, "荷送人電話番号"
					, "ご依頼主電話番号"
					, "ご依頼主郵便番号"
					, "ご依頼主住所１"
					, "ご依頼主住所２"
					, "ご依頼主名称１"
					, "ご依頼主名称２"
					, "荷姿コード"
					, "品名１"
					, "品名２"
					, "品名３"
					, "品名４"
					, "品名５"
					, "出荷個数"
					, "便種（スピードで選択）"
					, "便種（商品）"
					, "配達日"
					, "配達指定時間帯"
					, "配達指定時間（時分）"
					, "代引金額"
					, "消費税"
					, "決済種別"
					, "保険金額"
					, "保険金額印字"
					, "指定シール①"
					, "指定シール②"
					, "指定シール③"
					, "営業店止め"
					, "ＳＲＣ区分"
					, "営業店コード"
					, "元着区分"
			};
			
			// 사가와 포맷으로 바꾸기 전 치환된 결과와 함께 아마존 정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			vo.setDelivery_company(delivery_company);
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			
			// 예외테이블에 있는 목록은 사가와로
			vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			ArrayList<AmazonVO> list2 = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			boolean chkRet = false;
			
			for (AmazonVO tmp : list2) {
				chkRet = false;
				for (ExceptionMasterVO exVO : exList) {
					if (tmp.getResult_text().contains(exVO.getException_data())) {
						chkRet = true;
						// 예외테이블에 있는 목록중 배송코드가 같지 않은것만 리스트에 추가
						if (!tmp.getDelivery_company().equals(delivery_company)) {
							list.add(tmp);
						}
					}
					
					if ("1".equals(isChecked)) {
						if ("NextDay".equals(tmp.getShip_service_level())) {
							chkRet = true;
							// 예외테이블에 있는 목록에 없고, 배송코드가 같지 않은 빠른배송 목록을 리스트에 추가
							if ((!tmp.getResult_text().contains(exVO.getException_data()))
									&& (!tmp.getDelivery_company().equals(delivery_company))) {
								list.add(tmp);
							}
						}
					}
					if (chkRet) {
						break;
					}
				}
			}
			
			ArrayList<ArakuVO> sList = new ArrayList<>();
			
			for (AmazonVO tmp : list) {
				SagawaVO sVO = new SagawaVO();
				sVO.setDelivery_add1(tmp.getShip_state().replace("\"", ""));
				sVO.setDelivery_add2(tmp.getShip_address1().replace("\"", ""));
				sVO.setDelivery_add3(tmp.getShip_address2().replace("\"", "") + "" + tmp.getShip_address3().replace("\"", ""));
				sVO.setDelivery_post_no(tmp.getShip_postal_code().replace("\"", ""));
				String phone_no = tmp.getBuyer_phone_number();
				if (phone_no.contains("-")) {
					sVO.setDelivery_tel(phone_no);
				} else {
					if (phone_no.length() == 10) {
						sVO.setDelivery_tel(phone_no.substring(0, 2) + "-" + phone_no.subSequence(2, 6) + "-" + phone_no.subSequence(6, 10));
					} else if (phone_no.length() == 11) {
						sVO.setDelivery_tel(phone_no.substring(0, 3) + "-" + phone_no.subSequence(3, 7) + "-" + phone_no.subSequence(7, 11));
					}
				}
//				sVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
				sVO.setDelivery_name1(tmp.getRecipient_name().replace("\"", ""));
				
				sVO.setClient_add1("埼玉県川口市");
				sVO.setClient_add2("上青木西１丁目19-39エレガンス滝澤ビル1F");
				sVO.setClient_name1("有限会社");
				sVO.setClient_name2("ItempiaJapan (A)");
				sVO.setClient_tel("048-242-3801");
				
				// 配送サービスレベル가 NextDay인 경우
        		if ("NextDay".equals(tmp.getShip_service_level())) {
        			sVO.setDelivery_time(CommonUtil.SA_TOMORROW_MORNING_CODE);
        			sVO.setDelivery_date(CommonUtil.getDate("YYYY/MM/dd", 1));
        		}
				
        		String product_name = tmp.getResult_text().replace("\"", "");
        		// 반각문자를 전각문자로 치환 (https://kurochan-note.hatenablog.jp/entry/2014/02/04/213737)
        		product_name = Normalizer.normalize(product_name, Normalizer.Form.NFKC);
        		// 사가와 정책에 따라 品名1~5 각각 32바이트가 넘어가면 다음으로 세팅하는 방식으로 수정
        		if (product_name.length() > 30) {
        			sVO.setProduct_name1(product_name.substring(0, 30));
        			sVO.setProduct_name2(product_name.substring(30, product_name.length()));
        			if (sVO.getProduct_name2().length() > 30) {
        				String product_name2 = sVO.getProduct_name2();
        				sVO.setProduct_name2(product_name2.substring(0, 30));
        				sVO.setProduct_name3(product_name2.substring(30, product_name2.length()));
        			}
        		} else {
        			sVO.setProduct_name1(product_name);
        		}
				
				// csv작성을 위한 리스트작성
				sList.add(sVO);
			}
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, sList);
			
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void clickPostFormatDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("clickPostFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(response.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			String[] header = 
				{
					"お届け先郵便番号"
					,"お届け先氏名"
					,"お届け先敬称"
					,"お届け先住所1行目"
					,"お届け先住所2行目"
					,"お届け先住所3行目"
					,"お届け先住所4行目"
					,"内容品"
				};
			
			// 사가와 포맷으로 바꾸기 전 치환된 결과와 함께 아마존 정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> cList = new ArrayList<>();
			
			for (AmazonVO tmp : list) {
				if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {
					ClickPostVO cVO = new ClickPostVO();
					cVO.setPost_no(tmp.getShip_postal_code().replace("\"", ""));
					cVO.setDelivery_name(tmp.getRecipient_name().replace("\"", ""));
					cVO.setDelivery_add1(tmp.getShip_state().replace("\"", ""));
					cVO.setDelivery_add2(tmp.getShip_address1().replace("\"", ""));
					cVO.setDelivery_add3(tmp.getShip_address2().replace("\"", ""));
					cVO.setDelivery_add4(tmp.getShip_address3().replace("\"", ""));
					String product_name = tmp.getResult_text().replace("\"", "");
	        		// 반각문자를 전각문자로 치환 (https://kurochan-note.hatenablog.jp/entry/2014/02/04/213737)
	        		product_name = Normalizer.normalize(product_name, Normalizer.Form.NFKC);
	        		// clickpost 정책에 따라  内容品의 문자가 전각15바이트가 넘어가면 잘라내고 집어 넣을수있게 처리
	        		if (product_name.length() > 13) {
	        			product_name = product_name.substring(0, 13);
	        		}
					cVO.setDelivery_contents(product_name);
					
					// csv작성을 위한 리스트작성
	        		cList.add(cVO);
				}
			}
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, cList);
			
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
}
