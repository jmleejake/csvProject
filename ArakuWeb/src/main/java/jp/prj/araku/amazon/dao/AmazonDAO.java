package jp.prj.araku.amazon.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.Transliterator;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.amazon.mapper.IAmazonMapper;
import jp.prj.araku.amazon.vo.AmazonFileVO;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.file.vo.ClickPostVO;
import jp.prj.araku.file.vo.NewYamatoVO;
import jp.prj.araku.file.vo.SagawaVO;
import jp.prj.araku.file.vo.YamatoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.GlobalSagawaDownVO;
import jp.prj.araku.list.vo.House3MasterVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.PrdTransVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.SubTranslationVO;
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
	
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	public void insertAmazonInfo(MultipartFile amaUpload, String fileEncoding, String type) throws IOException {
		log.debug("insertAmazonInfo");
		
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
		        	
		        	if("SALES".equals(type)) {
	            		PrdCdMasterVO prdVO = new PrdCdMasterVO();
	            		prdVO.setPrd_cd(vo.getOrder_item_id());
	            		prdVO.setTarget_type(CommonUtil.TRANS_TARGET_A);
	            		ArrayList<PrdCdMasterVO> prdMaster = listMapper.getPrdCdMaster(prdVO);
	            		if(prdMaster.size() < 1) {
	            			continue;
	            		}
	            	}
		        	
		        	ArrayList<AmazonVO> dupCheck = mapper.getAmazonInfo(vo);
		        	if (dupCheck.size() > 0) {
		        		log.debug("already have data");
		        	} else {
		        		// 2020/01/27 Tom'sの”　'　”がエラーを起こす。
		        		//if(vo.getProduct_name().contains("'")) {
		        		//	vo.getProduct_name().replace("'", "");
		        		//	log.debug(String.format("inserted amazon info seq_id : [%s]", vo.getProduct_name()));
		        		//}
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
		log.debug("getAmazonInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", vo);
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		return mapper.getAmazonInfo(vo);
	}
	
	@Transactional
	public ArrayList<String> executeTranslate(ArrayList<AmazonVO> targetList) {
		log.debug("executeTranslate");
		log.debug("{}", targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		IAmazonMapper amazonMapper = sqlSession.getMapper(IAmazonMapper.class);
		
		TranslationVO transVO = new TranslationVO();
		String transedName; 
		String trans_seq_id = ""; // 2020.06.11
		
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
				trans_seq_id = searchRet.get(0).getSeq_id(); // 2020.06.11
				// 치환후 상품명
				transedName = searchRet.get(0).getAfter_trans();
				log.debug("치환후 상품명" + transedName);
				// 치환한 결과가 없을 경우 에러처리
				if (transedName == null) {
					errVO.setErr_text(CommonUtil.TRANS_ERR);
					listMapper.insertTranslationErr(errVO);
				}
			} else {
				errVO.setErr_text(CommonUtil.TRANS_ERR);
				listMapper.insertTranslationErr(errVO);
				transedName = "";
				log.debug("치환후 상품명" + transedName);
			}
			
			/**
			 * 2021.01.09 치환시 주문정보를 商品中間マスタ로 insert처리
			 * */
			PrdTransVO prdTransVO = new PrdTransVO();
			prdTransVO.setOrder_no(vo.getOrder_id());
			prdTransVO.setOrder_gbn("1");
			prdTransVO.setBefore_trans(vo.getProduct_name());
			transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			transVO.setKeyword(vo.getProduct_name());
			searchRet = listMapper.getTransInfo(transVO);
			prdTransVO.setBefore_trans(searchRet.get(0).getBefore_trans());
			prdTransVO.setJan_cd(searchRet.get(0).getJan_cd()); // 2021-09-26 kim jan_cd 처리
			prdTransVO.setAfter_trans(transedName);
			if((null != searchRet.get(0).getJan_cd()) && (null != searchRet.get(0).getPrd_cnt()) && (null != vo.getQuantity_to_ship())) {
				if(("" != searchRet.get(0).getJan_cd()) || ("" != searchRet.get(0).getPrd_cnt())) {
					int totalcnt = Integer.parseInt(searchRet.get(0).getPrd_cnt()) * Integer.parseInt(vo.getQuantity_to_ship());
					prdTransVO.setPrd_cnt(Integer.toString(totalcnt));	
				}else {
					prdTransVO.setPrd_cnt(vo.getQuantity_to_ship());
				}	
			}else {
				prdTransVO.setPrd_cnt(vo.getQuantity_to_ship());
			}
			

			prdTransVO.setPrd_master_hanei_gbn("0");
			prdTransVO.setSearch_type("translate");
			prdTransVO.setTrans_target_type(CommonUtil.TRANS_TARGET_A);
			ArrayList<PrdTransVO> prdTransRet = listMapper.getPrdTrans(prdTransVO);
			if(prdTransRet.size() > 0) {
				prdTransVO.setSeq_id(prdTransRet.get(0).getSeq_id());
				listMapper.updatePrdTrans(prdTransVO);
			}else {
				listMapper.insertPrdTrans(prdTransVO);
			}
			
			/**
			 * 2021.06.11 その他마스터테이블 (translation_sub_info)를 
			 * translation_info의 seq_id로 select하여 결과가 있을경우 해당 정보들을 商品中間マスタ로 insert처리
			 * */
			if(!"".equals(trans_seq_id)) {
				SubTranslationVO subTransVO = new SubTranslationVO();
				subTransVO.setParent_seq_id(trans_seq_id);
				ArrayList<SubTranslationVO> subTransList = listMapper.getSubTransInfo(subTransVO);
				if(subTransList.size() > 0) {
					for(SubTranslationVO subTrans : subTransList) {
						prdTransVO = new PrdTransVO();
						prdTransVO.setOrder_no(vo.getOrder_id());
						prdTransVO.setOrder_gbn("1");
						prdTransVO.setBefore_trans(subTrans.getBefore_trans());
						prdTransVO.setJan_cd(subTrans.getJan_cd()); // 2021-09-26 kim
						prdTransVO.setAfter_trans(subTrans.getAfter_trans());
						
						int totalsubcnt = Integer.parseInt(vo.getQuantity_to_ship()) * Integer.parseInt(subTrans.getPrd_cnt());
						prdTransVO.setPrd_cnt(Integer.toString(totalsubcnt));	
						
						prdTransVO.setPrd_master_hanei_gbn("0");
						prdTransVO.setSearch_type("translate");
						prdTransVO.setTrans_target_type(CommonUtil.TRANS_TARGET_A);
						prdTransRet = listMapper.getPrdTrans(prdTransVO);
						if(prdTransRet.size() > 0) {
							prdTransVO.setSeq_id(prdTransRet.get(0).getSeq_id());
							listMapper.updatePrdTrans(prdTransVO);
						}else {
							listMapper.insertPrdTrans(prdTransVO);
						}
					}
				}
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
			// 20220224 kim S
			//if("1011".equals(regionM.get(0).getDelivery_company())) {
			//	vo.setDelivery_company("1001");
			//}
			// 20220224 kim E
			
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
		log.debug("getTransResult");
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
		log.debug("deleteAmazonInfo");
		log.debug("{}", list);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		for (AmazonVO vo : list) {
			mapper.deleteAmazonInfo(vo.getSeq_id());
		}
	}
	
	public void yamatoFormatDownload(
			HttpServletResponse response
			, String[] id_lst, String fileEncoding
			, String delivery_company
			, String isChecked, String storage) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("yamatoFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		// ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String frontMsg = !"".equals(storage)?storage+"SOUKO_YAMATO_":"YAMATO_";
			String csvFileName = frontMsg+delivery_company+"_"+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.deliveryCompanyHeader("YAMA");
			
			// 야마토 포맷으로 바꾸기 전 치환된 결과와 함께 아마존 정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			//地域区分マスタにてヤマトと佐川の区分をチェックする。
			//vo.setDelivery_company(delivery_company);
			// 2022-02-24 kim S
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			
			ArrayList<String> deli_com_list = new ArrayList<String>();
			deli_com_list.add(delivery_company);
			deli_com_list.add("1011");
			vo.setDeli_com_list(deli_com_list);
			// 2021-07-03 lee E
			
			
			//TranslationResultVO vo = new TranslationResultVO();
			//vo.setSeq_id_list(seq_id_list);
			//vo.setDelivery_company(delivery_company);
			// 2022-02-24 kim E
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> yList = new ArrayList<>();
			
			// 예외테이블에 추가한 목록에 대하여 제2창고 목록으로 떨굴수있게 처리
			// 2021-07-23 야마토 제1창고, 2창고 구분 S
			// 2022-03-05 야마토 제1창고,　地方に区分 S			
//			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			ArrayList<House3MasterVO> house3Lst = listMapper.getHouse3Master(null);
			ArrayList<AmazonVO> str1List = new ArrayList<AmazonVO>();
			ArrayList<AmazonVO> str2List = new ArrayList<AmazonVO>();
			
			// 2021-10-24 야마토 지방 (제3창고)처리
			ArrayList<AmazonVO> str3List = new ArrayList<AmazonVO>();
			boolean exChk = false;
			for(AmazonVO tmp : list) {
				// ship_state 지역마스터
				RegionMasterVO regionVO = new RegionMasterVO();
				// err 対応：”‐”が含まれるとエラーになるため　2021.11.21 kim 対応
				// ハイフンで行を分割
				String splitData = tmp.getShip_state();
			    String[] data = splitData.split("-");
				log.debug("Ship_state : " +data[0]);
				regionVO.setKeyword(data[0]);
				ArrayList<RegionMasterVO> region = listMapper.getRegionMaster(regionVO);
				String house_type = region.get(0).getHouse_type();
//				
//				if("1".equals(house_type)) {
//					str1List.add(tmp);
//				}else if("2".equals(house_type) && "2".equals(storage)) {
//					for(ExceptionMasterVO exVO : exList) {
//						if (tmp.getResult_text().contains(exVO.getException_data())) {
//							exChk = true;
//							str2List.add(tmp);
//						}
//					}
//				}else if("3".equals(house_type)) {
//					if("1011".equals(tmp.getDelivery_company())) {
//						for(House3MasterVO house3 : house3Lst) {
//							if(tmp.getResult_text().contains(house3.getHouse3_data())) {
//								exChk = true;
//								str3List.add(tmp);
//							}
//						}
//					}
////					for(House3MasterVO house3 : house3Lst) {
////						if(tmp.getResult_text().contains(house3.getHouse3_data())) {
////							exChk = true;
////							str3List.add(tmp);
////						}
////					}
//				}
//				
//				if(!exChk && ("2".equals(house_type) || "3".equals(house_type))) {
//					// 2021-10-24 제2창고, 지방에 대한 조건이 성립되지 않으면 탬프테이블에 넣어두고 1창고 리스트를 만들때 추가
//					mapper.insertAmazonInfoTmp(tmp);
//				}
				
				if("1011".equals(tmp.getDelivery_company())) {
					for (House3MasterVO house3 : house3Lst) {
						if (tmp.getResult_text().contains(house3.getHouse3_data())) {
							exChk = true;
							if("3".equals(house_type)) {
								str3List.add(tmp);
							}
							
							//if("2".equals(storage) && "1011".equals(vo.getDelivery_company())) {
								//exChk = true;
								//str2List.add(tmp);
							//}
						}
					}
				}
				
				if(!exChk) {
					str1List.add(tmp);
				}
				
				//例外マスタの情報有無チェックフラグを初期化する。
				exChk = false;
			}
			if("1".equals(storage)) {
				ArrayList<AmazonVO> tmpList = mapper.getAmazonInfoTmp();
				str1List.addAll(tmpList);
				mapper.deleteAmazonInfoTmp();
				list = str1List;
			}else if("2".equals(storage)) {
				list = str2List;
			}else {
				list = str3List;
			}
			// 2021-07-23 야마토 제1창고, 2창고 구분 E
			// 2022-03-05 야마토 제1창고,　地方に区分 E
			
 			for (AmazonVO tmp : list) {
				/*
 				// 2022-02-20: あす楽のみ　対応
 				// 빠른배송을 옵션으로 둔 항목에 대하여 체크가 되어있으면 제외
				if ("1".equals(isChecked)) {
					if ("NextDay".equals(tmp.getShip_service_level())) {
						log.debug("tomorrow hope checked and excepted!");
						continue;
					}
				}
				*/
				
//				例外地域マスタ処理により、倉庫１/２対応に影響あり、取り下げする。21.7.24 kim
//				/**
//				 * 2020-07-28
//				 * 例外地域マスタに登録されている地域情報はヤマトによって発送する。
//				 * やまとにてDLする処理する。
//				 * */
//				boolean isExY = false;
//				for(ExceptionRegionMasterVO region : exRegionList) {
//					if(tmp.getShip_state().contains(region.getException_data())) {
//						isExY = true;
//					}
//
//					if(tmp.getShip_address1().contains(region.getException_data())) {
//						isExY = true;
//					}
//				}
//				
//				if (!isExY) {
//					continue;
//				}
	
				YamatoVO yVO = new YamatoVO();
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＳＴＡＲＴ
				// 2020/10/30  キム クリックポストが1件以上の場合、一般にする対応。　
				if (tmp.getResult_text().contains("全無")&& tmp.getQuantity_purchased().equals("1") && !(tmp.getResult_text().equals("."))) {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_7);
				}	else {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				}
				if (tmp.getResult_text().contains("宅コン")&& tmp.getQuantity_purchased().equals("1") ) {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_8);
				}	
				if (tmp.getResult_text().contains("冷凍")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_1);
				}
				if (tmp.getResult_text().contains("冷蔵")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_2);		
				}
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
				
				yVO.setCustomer_no(tmp.getOrder_id().replace("\"", ""));
				//yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				if ("COD".equals(tmp.getPayment_method())) {
					yVO.setCollect_cash(tmp.getCod_collectible_amount().replace("\"", ""));
				}
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048242380101");
//				yVO.setBill_customer_code("048299821004-311");
				
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				//2020/01/13  キム お届け先名とご依頼主名がおなじではない場合、ご依頼主名を登録する。
				if(tmp.getRecipient_name().equals(tmp.getBuyer_name())) {
					yVO.setClient_name("有限会社ItempiaJapan (A)");
				}else {
					yVO.setClient_name(tmp.getBuyer_name()+ " (A)");
				}
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getShip_postal_code().replace("\"", "").replace("-", ""));
				// 2019/12/24  キム ヤマト 주소 컬럼에 대하여 전각 16자 이상이면 안되는 사항이 있어 수정. 　⇒　ＳＴＡＲＴ
				String addStr = tmp.getShip_state().replace("\"", "") + "" + tmp.getShip_address1().replace("\"", "") + "" + tmp.getShip_address2().replace("\"", "") + "" + tmp.getShip_address3().replace("\"", "");
				if(addStr.length() > 16) {
					yVO.setDelivery_add(addStr.substring(0, 16));

					if(addStr.length() > 32) {
						yVO.setDelivery_building(addStr.substring(16, 32));
						yVO.setDelivery_company1(addStr.substring(32, addStr.length()));
					}else {
						yVO.setDelivery_building(addStr.substring(16, addStr.length()));
					}
				}else {
					yVO.setDelivery_add(addStr);
				}
				//yVO.setDelivery_add(tmp.getShip_state().replace("\"", "") + "" + tmp.getShip_address1().replace("\"", "") + "" + tmp.getShip_address2().replace("\"", "") + "" + tmp.getShip_address3().replace("\"", ""));
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
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
					yVO.setEstimate_delivery_date(CommonUtil.getDate("YYYY/MM/dd", 1));
        			yVO.setDelivery_time(CommonUtil.YA_TOMORROW_MORNING_CODE);
        		}
				
				// 2019/12/24  キム ヤマト 품명컬럼에 대하여 전각 25자 이상이면 안되는 사항이 있어 수정. 　⇒　ＳＴＡＲＴ
				String productStr = tmp.getResult_text();
				if (!productStr.equals(null)) {
					if(productStr.length() > 25) {
						yVO.setProduct_name1(productStr.substring(0, 25));
						yVO.setProduct_name2(productStr.substring(25, productStr.length()));
					}else {
						yVO.setProduct_name1(productStr);
					}
				}
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
				
				//yVO.setProduct_name1(tmp.getResult_text().replace("\"", ""));
				
				// csv작성을 위한 리스트작성
				// 2022-02-20: あす楽のみ　対応
				if ("1".equals(isChecked)) {
					if ("NextDay".equals(tmp.getShip_service_level())) {
						yList.add(yVO);
					}
				}else {
					yList.add(yVO);
				}
				
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
		log.debug("sagawaFormatDownload");
		
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
			
			String[] header = CommonUtil.deliveryCompanyHeader("SAGA");
			
			// 사가와 포맷으로 바꾸기 전 치환된 결과와 함께 아마존 정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
//			配送会社のマスタ情報をチェックするため。コメントアウトする。 2020.06.01  kim
//			vo.setDelivery_company(delivery_company);
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
//			boolean chkRet = false;
			ArrayList<ArakuVO> sList = new ArrayList<>();
			
			for (AmazonVO tmp : list) {
				boolean isEx = false;
				for(ExceptionRegionMasterVO region : exRegionList) {
					
					if(tmp.getShip_address1().contains(region.getException_data())) {
						isEx = true;
						AmazonVO av = new AmazonVO();
						av.setSeq_id(tmp.getReal_seq_id());
						av.setDelivery_company("1001");
						mapper.updateAmazonInfo(av);
					}
				}
				
				if(isEx) continue;
				
//				例外テーブルに含んている場合、ファイル作成するように変更する。　2020/06/01
				for (ExceptionMasterVO exVO : exList) {
//					chkRet = false;
					if (tmp.getResult_text().contains(exVO.getException_data())) {
//						chkRet = true;
						
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
					        			sVO.setDelivery_date(CommonUtil.getDate("YYYYMMdd", 1)); // 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
					        		}
									
					        		String product_name = tmp.getResult_text().replace("\"", "");
					        		
					        		// 2019-09-21: 전각처리 / 전각처리된 상품명1-5에 대하여 각 14자리가 들어갈수있게 처리 
					        		Transliterator transliterator = Transliterator.getInstance("Halfwidth-Fullwidth");
					        		product_name = transliterator.transliterate(product_name);
					        		if(product_name.length() > 14) {
					        			sVO.setProduct_name1(product_name.substring(0,14));
					        			sVO.setProduct_name2(product_name.substring(14,product_name.length()));
					        			
					        			if(sVO.getProduct_name2().length() > 14) {
					        				String str1 = sVO.getProduct_name2();
					            			sVO.setProduct_name2(str1.substring(0,14));
					            			sVO.setProduct_name3(str1.substring(14,str1.length()));
					            			
					            			if(sVO.getProduct_name3().length() > 14) {
					            				String str2 = sVO.getProduct_name3();
					            				sVO.setProduct_name3(str2.substring(0,14));
					            				sVO.setProduct_name4(str2.substring(14,str2.length()));
					            				
					            				if(sVO.getProduct_name4().length() > 14) {
					            					String str3 = sVO.getProduct_name4();
					            					sVO.setProduct_name4(str3.substring(0,14));
					            					sVO.setProduct_name5(str3.substring(14,str3.length()));
					            					
					            					if(sVO.getProduct_name5().length() > 14) {
					            						sVO.setProduct_name5(sVO.getProduct_name5().substring(0,14));
					            					}
					            				}
					            			}
					            		}
					        		}else {
					        			sVO.setProduct_name1(product_name);
					        		}
					        		
									// csv작성을 위한 리스트작성
									sList.add(sVO);
					}
				}
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String createClickpostCsvFile(String fileEncoding, String cpDownPath, String[] id_lst) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		String ret = "CLICKPOST DOWNLOAD FAILED";
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(seq_id_list);
		
		ArrayList<AmazonVO> list = mapper.getTransResult(vo);
		ArrayList<ArakuVO> cList = new ArrayList<>();
		
		for (AmazonVO tmp : list) {
			if(tmp.getResult_text().contains("全無")) {
			/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
				ClickPostVO cVO = new ClickPostVO();
				cVO.setPost_no(tmp.getShip_postal_code().replace("\"", ""));
				cVO.setDelivery_name(tmp.getRecipient_name().replace("\"", ""));
				cVO.setDelivery_add1(tmp.getShip_state().replace("\"", ""));
				cVO.setDelivery_add2(tmp.getShip_address1().replace("\"", ""));
				// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
				String addStr = tmp.getShip_address2().replace("\"", "");
				if(addStr.length() > 20) {
					cVO.setDelivery_add3(addStr.substring(0, 20));
					cVO.setDelivery_add4(addStr.substring(20, addStr.length())+" "+tmp.getShip_address3().replace("\"", ""));
				}else {
					cVO.setDelivery_add3(tmp.getShip_address2().replace("\"", ""));
					cVO.setDelivery_add4(tmp.getShip_address3().replace("\"", ""));
				}
				
				String product_name = tmp.getResult_text().replace("\"", "");
        		// 반각문자를 전각문자로 치환 (https://kurochan-note.hatenablog.jp/entry/2014/02/04/213737)
        		product_name = Normalizer.normalize(product_name, Normalizer.Form.NFKC);
        		// clickpost 정책에 따라  内容品의 문자가 전각15바이트가 넘어가면 잘라내고 집어 넣을수있게 처리
        		if (product_name.length() > 15) {
        			product_name = product_name.substring(0, 15);
        		}
				cVO.setDelivery_contents(product_name);
				
				// csv작성을 위한 리스트작성
        		cList.add(cVO);
			}
		}
		
		if(cList.size() > 40) {
			int i = cList.size()/40;
			int[] arr = new int[i+1];
			
			List<List<ArakuVO>> subList = new ArrayList<>();
			
			for(int j=0; j<arr.length; j++) {
				arr[j] = (j+1)*40;
			}
			
			for(int k=0; k<arr.length; k++) {
				if(k==0) {
					subList.add(cList.subList(0, arr[k]));
					continue;
				}
				
				if(arr[k] > cList.size()) {
					subList.add(cList.subList(k*arr[k-1], cList.size()));
				}else {
					subList.add(cList.subList(k*arr[k-1], arr[k]));
				}
			}
			
			ArrayList<String> fileList = new ArrayList<>();
			for(int i1=0; i1<subList.size(); i1++) {
				try
				(
						FileOutputStream fos = new FileOutputStream(cpDownPath+"CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0) +"-"+i1+".csv");
						Writer writer = new OutputStreamWriter(fos, fileEncoding);
						CSVWriter	csvWriter = new CSVWriter(writer
							, CSVWriter.DEFAULT_SEPARATOR
							, CSVWriter.NO_QUOTE_CHARACTER
							, CSVWriter.DEFAULT_ESCAPE_CHARACTER
							, CSVWriter.DEFAULT_LINE_END);
				) 
				{
					StatefulBeanToCsv<ArakuVO> beanToCsv = new StatefulBeanToCsvBuilder(writer)
		                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
		                    .build();
					csvWriter.writeNext(CommonUtil.deliveryCompanyHeader("CLICK"));
		            beanToCsv.write(subList.get(i1));
		            fileList.add(cpDownPath+"CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0) +"-"+i1+".csv");
				}
			}
			ret = "CLICKPOST DOWNLOAD SUCCESS<br>[FILE PATH]: "+fileList;
		}else {
			try
			(
				FileOutputStream fos = new FileOutputStream(cpDownPath+"CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0) +".csv");
				Writer writer = new OutputStreamWriter(fos, fileEncoding);
				CSVWriter	csvWriter = new CSVWriter(writer
						, CSVWriter.DEFAULT_SEPARATOR
						, CSVWriter.NO_QUOTE_CHARACTER
						, CSVWriter.DEFAULT_ESCAPE_CHARACTER
						, CSVWriter.DEFAULT_LINE_END);
			) 
			{
				StatefulBeanToCsv<ArakuVO> beanToCsv = new StatefulBeanToCsvBuilder(writer)
	                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	                    .build();
				
				csvWriter.writeNext(CommonUtil.deliveryCompanyHeader("CLICK"));

	            beanToCsv.write(cList);
			}
			ret = "CLICKPOST DOWNLOAD SUCCESS<br>[FILE PATH]: "+cpDownPath+"CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
		}
		return ret;
	}
	
	public void downloadClickpostCsvFile(
			HttpServletResponse response
			, String fileEncoding, String[] id_lst) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(seq_id_list);
		
		ArrayList<AmazonVO> list = mapper.getTransResult(vo);
		ArrayList<ArakuVO> cList = new ArrayList<>();
		ArrayList<ClickPostVO> cList2 = new ArrayList<>();
		
		for (AmazonVO tmp : list) {
			if(tmp.getResult_text().contains("全無")) {
			/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
				ClickPostVO cVO = new ClickPostVO();
				cVO.setPost_no(tmp.getShip_postal_code().replace("\"", ""));
				cVO.setDelivery_name(tmp.getRecipient_name().replace("\"", ""));
				cVO.setDelivery_add1(tmp.getShip_state().replace("\"", ""));
				cVO.setDelivery_add2(tmp.getShip_address1().replace("\"", ""));
				// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
				String addStr = tmp.getShip_address2().replace("\"", "");
				if(addStr.length() > 20) {
					cVO.setDelivery_add3(addStr.substring(0, 20));
					cVO.setDelivery_add4(addStr.substring(20, addStr.length())+" "+tmp.getShip_address3().replace("\"", ""));
				}else {
					cVO.setDelivery_add3(tmp.getShip_address2().replace("\"", ""));
					cVO.setDelivery_add4(tmp.getShip_address3().replace("\"", ""));
				}
				
				String product_name = tmp.getResult_text().replace("\"", "");
        		// 반각문자를 전각문자로 치환 (https://kurochan-note.hatenablog.jp/entry/2014/02/04/213737)
        		product_name = Normalizer.normalize(product_name, Normalizer.Form.NFKC);
        		// clickpost 정책에 따라  内容品의 문자가 전각15바이트가 넘어가면 잘라내고 집어 넣을수있게 처리
        		if (product_name.length() > 15) {
        			product_name = product_name.substring(0, 15);
        		}
				cVO.setDelivery_contents(product_name);
				
				// csv작성을 위한 리스트작성
        		cList.add(cVO);
        		cList2.add(cVO);
			}
		}
		
		if(cList.size() > 40) {
			int i = cList2.size()/40;
			int[] arr = new int[i+1];
			
			List<List<ClickPostVO>> subList = new ArrayList<>();
			
			for(int j=0; j<arr.length; j++) {
				arr[j] = (j+1)*40;
			}
			
			for(int k=0; k<arr.length; k++) {
				if(k==0) {
					subList.add(cList2.subList(0, arr[k]));
					continue;
				}
				
				if(arr[k] > cList2.size()) {
					subList.add(cList2.subList(k*arr[k-1], cList2.size()));
				}else {
					subList.add(cList2.subList(k*arr[k-1], arr[k]));
				}
			}
			
			try {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = null;
				XSSFRow row = null;
				XSSFCell cell = null;
				Map<Integer,String> headerList = new HashMap<Integer,String>();
				headerList.put(0, "お届け先郵便番号");
				headerList.put(1, "お届け先氏名");
				headerList.put(2, "お届け先敬称");
				headerList.put(3, "お届け先住所1行目");
				headerList.put(4, "お届け先住所2行目");
				headerList.put(5, "お届け先住所3行目");
				headerList.put(6, "お届け先住所4行目");
				headerList.put(7, "内容品");
				
				for(int i1=0; i1<subList.size(); i1++) {
					List<ClickPostVO> ll = subList.get(i1);
					sheet = workbook.createSheet("CLICKPOST-DATA"+(i1+1));
					// 데이터의 크기만큼 row생성
					for(int i2=0; i2<ll.size()+1; i2++) {
						row = sheet.createRow((short)i2);
						// headerList의 크기만큼
						for(int i3=0; i3<headerList.size(); i3++) {
							cell = row.createCell(i3);
							// 맨 윗줄은 헤더
							if(i2==0) {
								cell.setCellValue(headerList.get(i3));
							}else {
								// 헤더 아래부터는 데이터세팅
								Map<Integer,String> dataList = new HashMap<Integer,String>();
								ClickPostVO cData = ll.get(i2-1);
								dataList.put(0, cData.getPost_no());
								dataList.put(1, cData.getDelivery_name());
								dataList.put(2, cData.getDelivery_name_title());
								dataList.put(3, cData.getDelivery_add1());
								dataList.put(4, cData.getDelivery_add2());
								dataList.put(5, cData.getDelivery_add3());
								dataList.put(6, cData.getDelivery_add4());
								dataList.put(7, cData.getDelivery_contents());
								cell.setCellValue(dataList.get(i3));
							}
						}
					}
				}
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment;filename=\"%s\"",
						"CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0)+".xlsx");
				response.setCharacterEncoding(fileEncoding);
				response.setHeader(headerKey, headerValue);
				workbook.write(response.getOutputStream());
				workbook.close();
			}finally {
				
			}
		}else {
			BufferedWriter writer = null;
			CSVWriter csvWriter = null;
			try {
				String csvFileName = "CLICKPOST" + CommonUtil.getDate("YYYYMMdd", 0)+".csv";

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
				
				String[] header = CommonUtil.deliveryCompanyHeader("CLICK");
				
				CommonUtil.executeCSVDownload(csvWriter, writer, header, cList);
			}finally {
				if (csvWriter != null) {
					csvWriter.close();
				}
				
				if (writer != null) {
					writer.close();
				}
			}
		}
	}
	
	public void amazonYamatoUpdate(MultipartFile yamaUpload, String fileEncoding) throws IOException {
		log.debug("amazonYamatoUpdate");
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + yamaUpload.getContentType());
		log.debug("name: " + yamaUpload.getName());
		log.debug("original name: " + yamaUpload.getOriginalFilename());
		log.debug("size: " + yamaUpload.getSize());
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(yamaUpload.getInputStream(), fileEncoding));
			
			CsvToBean<NewYamatoVO> csvToBean = new CsvToBeanBuilder<NewYamatoVO>(reader)
                    .withType(NewYamatoVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<NewYamatoVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	NewYamatoVO vo = iterator.next();
            	
            	// 데이터가 있는지 체크
            	AmazonVO searchVO = new AmazonVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_id(vo.getCustomer_no());
            	
            	/*
            	 * お客様管理番号(customer_no) 값을 키로 해서
            	 * 伝票番号(slip_no) 값을 갱신
            	 * */
            	ArrayList<AmazonVO> searchRetList = mapper.getAmazonInfo(searchVO);
            	
            	if (searchRetList.size() > 0) {
            		AmazonVO searchRet = searchRetList.get(0);
            		
            		searchVO.setSeq_id(searchRet.getSeq_id());
                	searchVO.setBaggage_claim_no(vo.getSlip_no());
                	searchVO.setDelivery_company("1001");
                	
                	// お荷物伝票番号값 update
                	mapper.updateAmazonInfo(searchVO);
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void downloadAmazonFile(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException  {
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		ArrayList<AmazonFileVO> ret = new ArrayList<>();
		
		AmazonVO ama = new AmazonVO();
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		ama.setSeq_id_list(seq_id_list);
		
		ArrayList<AmazonVO> list = mapper.getAmazonInfo(ama);
		
		for(AmazonVO vo : list) {
			/*楽天更新ファイル画面にてヤマトDLするときお荷物伝票番号対応したように
			 * アマゾンもアマゾン更新ファイル画面にで
			 * お問い合わせ伝票番号にデーターがあるもののみDLするように
			 * 対応していただけますでしょうか
			 * */
			if(null != vo.getBaggage_claim_no() && !"".equals(vo.getBaggage_claim_no())) {
				AmazonFileVO fileVO = new AmazonFileVO();
				fileVO.setOrder_id(vo.getOrder_id());
				fileVO.setShip_date(CommonUtil.getDate("yyyy-MM-dd", 0));
				fileVO.setCarrier_code("OTHER");
				fileVO.setCarrier_name("ヤマト運輸");
				fileVO.setTracking_number(vo.getBaggage_claim_no());
				fileVO.setShip_method("宅配便");
				ret.add(fileVO);
			}
		}
		
		String[] contents1= {"TemplateType=OrderFulfillment","Version=2011.1102","この行はAmazonが使用しますので変更や削除しないでください。","","","","","",""};
		String[] contents2= {"注文番号","注文商品番号","出荷数","出荷日","配送業者コード","配送業者名","お問い合わせ伝票番号","配送方法","代金引換"};
		String[] contents3= {"order-id","order-item-id","quantity","ship-date","carrier-code","carrier-name","tracking-number","ship-method","cod-collection-method"};
		
		BufferedWriter writer =  null;
		CSVWriter	csvWriter = null;
		try {
			String fileName = "Yama-AFile"+CommonUtil.getDate("YYYYMMdd", 0) + ".txt";
			response.setContentType("text/plain");
			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					fileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
				
			writer = new BufferedWriter(response.getWriter());
			csvWriter = new CSVWriter(writer
					, '\t'
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
		
			csvWriter.writeNext(contents1);
			csvWriter.writeNext(contents2);
			csvWriter.writeNext(contents3);
			
			for(AmazonFileVO vo : ret) {
				String[] strArr = new String[9];
				strArr[0] = vo.getOrder_id();
				strArr[1] = vo.getOrder_item_id();
				strArr[2] = vo.getQuantity();
				strArr[3] = vo.getShip_date();
				strArr[4] = vo.getCarrier_code();
				strArr[5] = vo.getCarrier_name();
				strArr[6] = vo.getTracking_number();
				strArr[7] = vo.getShip_method();
				strArr[8] = vo.getCod_collection_method();
				csvWriter.writeNext(strArr);
			}
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void downloadGlobalSagawa(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("downloadGlobalSagawa");
		
		log.debug("encoding : " + fileEncoding);
		
		IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "GSAGA" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.deliveryCompanyHeader("GSAGA");
			
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			
			ArrayList<AmazonVO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
			ArrayList<ArakuVO> gsaList = new ArrayList<>();
			
			for (AmazonVO tmp : list) {
//				/**
//				 * 사가와 대상 목록중 예외지역마스터(例外地域マスタ)에 있는 값인 경우
//				 * 해당 데이터의 배송회사를 야마토로 update치고
//				 * 야마토로 다운로드 될 수 있게 처리
//				 * */
//				boolean isEx = false;
//				for(ExceptionRegionMasterVO region : exRegionList) {
//					if(tmp.getShip_address1().contains(region.getException_data())) {
//						isEx = true;
//						AmazonVO av = new AmazonVO();
//						av.setSeq_id(tmp.getReal_seq_id());
//						av.setDelivery_company("1001");
//						mapper.updateAmazonInfo(av);
//					}
//				}
//				
//				if(isEx) continue;
				
				/**
				 * 2020-07-28
				 * 例外地域マスタに登録されている地域情報はヤマトによって発送する。
				 * やまとにてDLする処理する。
				 * */
				boolean isEx = false;
				for(ExceptionRegionMasterVO region : exRegionList) {
					if(tmp.getShip_state().contains(region.getException_data())) {
						isEx = true;
					}
				}
				
				for (ExceptionMasterVO exVO : exList) {
					String str= tmp.getResult_text();
					if(str.contains(exVO.getException_data())  && isEx != Boolean.TRUE) {
						if(str.length() > 10) {
							for (int i = 0; i < str.length(); i += 10) {
								GlobalSagawaDownVO gsaVO = new GlobalSagawaDownVO();
								gsaVO.setSeller_cd("Fastbox2");
								gsaVO.setPick_dt(CommonUtil.getDate("YYYYMMdd", 0));
								gsaVO.setOrder_no(tmp.getOrder_id());
								gsaVO.setConsign_nm(tmp.getRecipient_name());
								//gsaVO.setConsign_nm_kana();
								gsaVO.setConsign_add1(tmp.getShip_state()+tmp.getShip_address1() + tmp.getShip_address2() + " " + tmp.getShip_address3());
								//gsaVO.setConsign_add2(tmp.getShip_address2() + " " + tmp.getShip_address3());
								gsaVO.setConsign_post_no(tmp.getShip_postal_code());
								gsaVO.setConsign_tel(tmp.getBuyer_phone_number());
								// 配送サービスレベル가 NextDay인 경우
				        		if ("NextDay".equals(tmp.getShip_service_level())) {
				        			gsaVO.setDelivery_tm(CommonUtil.SA_TOMORROW_MORNING_CODE);
				        			gsaVO.setDelivery_dt(CommonUtil.getDate("YYYYMMdd", 1)); // 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
				        		}
								//gsaVO.setPkg();
								gsaVO.setItem_nm(str.substring(i, Math.min(i + 10, str.length())).trim());
								gsaVO.setDelivery_tm("10");
								gsaVO.setUnit_price("500");	
								gsaVO.setItem_pcs("9");		
								gsaVO.setItem_origin("JP");
								gsaList.add(gsaVO);
								
							}
						}else {
							GlobalSagawaDownVO gsaVO = new GlobalSagawaDownVO();
							gsaVO.setSeller_cd("Fastbox2");
							gsaVO.setPick_dt(CommonUtil.getDate("YYYYMMdd", 0));
							gsaVO.setOrder_no(tmp.getOrder_id());
							gsaVO.setConsign_nm(tmp.getRecipient_name());
							//gsaVO.setConsign_nm_kana();
							gsaVO.setConsign_add1(tmp.getShip_state()+tmp.getShip_address1()+tmp.getShip_address2() + " " + tmp.getShip_address3());
							//gsaVO.setConsign_add2(tmp.getShip_address2() + " " + tmp.getShip_address3());
							gsaVO.setConsign_post_no(tmp.getShip_postal_code());
							gsaVO.setConsign_tel(tmp.getBuyer_phone_number());
							// 配送サービスレベル가 NextDay인 경우
			        		if ("NextDay".equals(tmp.getShip_service_level())) {
			        			gsaVO.setDelivery_tm(CommonUtil.SA_TOMORROW_MORNING_CODE);
			        			gsaVO.setDelivery_dt(CommonUtil.getDate("YYYYMMdd", 1)); // 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
			        		}
							//gsaVO.setPkg();
							gsaVO.setItem_nm(str);
							gsaVO.setDelivery_tm("10");
							gsaVO.setUnit_price("500");	
							gsaVO.setItem_pcs("9");		
							gsaVO.setItem_origin("JP");
							gsaList.add(gsaVO);
						}
					}
				}
			}
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, gsaList);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void uriageDownload(
			HttpServletResponse response
			, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("uriageDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "[A]URIAGE" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.amazonHeader();
			IAmazonMapper mapper = sqlSession.getMapper(IAmazonMapper.class);
			ArrayList<AmazonVO> list = mapper.getAmazonInfo(null);
			
			StatefulBeanToCsv<AmazonVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
		            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
		            .build();
			
			csvWriter.writeNext(header);
			
			beanToCSV.write(list);
			
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
