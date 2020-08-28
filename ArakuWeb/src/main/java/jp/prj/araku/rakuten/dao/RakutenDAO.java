package jp.prj.araku.rakuten.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
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

import jp.prj.araku.file.vo.ClickPostVO;
import jp.prj.araku.file.vo.NewSagawaVO;
import jp.prj.araku.file.vo.NewYamatoVO;
import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.file.vo.SagawaVO;
import jp.prj.araku.file.vo.YamatoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.GlobalSagawaDownVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.rakuten.mapper.IRakutenMapper;
import jp.prj.araku.rakuten.vo.RakutenDeleteVO;
import jp.prj.araku.rakuten.vo.RakutenVO;
import jp.prj.araku.rakuten.vo.RakutenVOForUriage;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

/**
 * [MOD-1011] 半角→全角へ変換する。　　
 * */
@Repository
public class RakutenDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.rakuten");
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public String insertRakutenInfo(MultipartFile rakUpload, String fileEncoding, HttpServletRequest req, String duplDownPath, String type) throws IOException {
		log.info("insertRakutenInfo");
		String ret = "アップロードを失敗しました。";
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + rakUpload.getContentType());
		log.debug("name: " + rakUpload.getName());
		log.debug("original name: " + rakUpload.getOriginalFilename());
		log.debug("size: " + rakUpload.getSize());
		
		BufferedReader reader = null;
//		ArrayList<RakutenVO> errList = new ArrayList<>();
		// 2019-10-09: 別紙처리
//    	HashSet<String> exceptionList = new HashSet<>();
		try  {
			reader = new BufferedReader(
					new InputStreamReader(rakUpload.getInputStream(), fileEncoding));
			
			CsvToBean<RakutenVO> csvToBean = new CsvToBeanBuilder<RakutenVO>(reader)
                    .withType(RakutenVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<RakutenVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	RakutenVO vo = iterator.next();
            	
            	if("SALES".equals(type)) {
            		PrdCdMasterVO prdVO = new PrdCdMasterVO();
            		prdVO.setPrd_cd(vo.getProduct_id());
            		prdVO.setTarget_type(CommonUtil.TRANS_TARGET_R);
            		ArrayList<PrdCdMasterVO> prdMaster = listMapper.getPrdCdMaster(prdVO);
            		if(prdMaster.size() < 1) {
            			continue;
            		}
            	}
            	
            	// 2019-10-05: 注1】이라는 항목에 대해서 빈 값으로 처리후 insert처리
            	if(null != vo.getProduct_option() && "注".contains(vo.getProduct_option())) {
            		vo.setProduct_option("");
            	}
            	
            	// 2019-10-08: 배송지정일이 일주일 내에 있는 데이터에 대해서만 insert
            	if(null != vo.getDelivery_date_sel()) {
            		Calendar c = Calendar.getInstance();
            		c.add(Calendar.DAY_OF_MONTH, 7);
            		Date d = c.getTime();
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            		long today = Long.parseLong(sdf.format(d));
            		long selectedDate = Long.parseLong(vo.getDelivery_date_sel().replaceAll("/", "").replaceAll("-", ""));
            		if(selectedDate >= today) {
            			continue;
            		}
            	}
            	
            	// あす楽希望이 1인 경우
        		if (null != vo.getTomorrow_hope() && vo.getTomorrow_hope().equals("1")) {
        			// お届け日指定 컬럼에 데이터 등록일 +1
        			vo.setDelivery_date_sel(CommonUtil.getDate("yyyy/MM/dd", 1));
        			// お届け時間帯 컬럼에 午前中
        			vo.setDelivery_time(CommonUtil.TOMORROW_MORNING);
        		}
            	
            	// 데이터 중복체크
            	RakutenVO searchVO = new RakutenVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getOrder_no().trim());
            	
            	ArrayList<RakutenVO> dupCheckList = mapper.getRakutenInfo(searchVO);
        		
            	boolean isInserted = false;
            	// 이미 존재하는 受注番号가 있으면
        		if (dupCheckList.size() == 1) {
        			// 2019-10-08: 업로드시 존재하는 주문번호가 있다면 등록일을 현재일로 갱신
        			mapper.updateRakutenRegistDate(dupCheckList.get(0).getSeq_id());
        			// 商品ID가 같으면
        			if (vo.getProduct_id().equals(dupCheckList.get(0).getProduct_id())) {
        				mapper.insertRakutenFrozenInfo(vo);
        				log.debug("insertRakutenFrozenInfo seq_id :: " + vo.getSeq_id());
        				isInserted = true;
                        log.debug("==========================");
//                        continue;
        			} else {
        				/*
        				 * 2020-07-23
        				 *   1. 라쿠텐파일 업로드시 한 주문번호에 여러건의 상품번호가 있을경우 
        				 *   냉동냉장구분마스터(rakuten_frozen_info) 테이블에 해당 데이터들을 넣어준다.
        				 *   2. 업로드후 주문정보화면에서 치환작업 진행시 
        				 *   냉동냉장구분마스터(rakuten_frozen_info) 테이블에 데이터가 있을 경우
        				 *   해당 데이터들에 대해서도 함께 진행한다.
        				 * */
        				mapper.insertRakutenFrozenInfo(vo);
        				log.debug("insertRakutenFrozenInfo seq_id :: " + vo.getSeq_id());
        				isInserted = true;
                        log.debug("==========================");
        				
        				/* 2020-07-10
        				// 商品ID가 다르면 한사람이 여러 상품을 주문한 것으로 간주, 에러리스트에 넣은후 다음 레코드로 진행
        				log.debug("[ERR]: " + vo.getOrder_no());
        				if(exceptionList.add(vo.getOrder_no().trim())) {
        					RakutenVO forException = new RakutenVO();
        					forException.setSeq_id(dupCheckList.get(0).getSeq_id());
        					forException.setProduct_name("[別紙"+(exceptionList.size())+"]");
        					mapper.updateRakutenInfo(forException);
        					// 2019-10-09: 別紙처리
        					dupCheckList.get(0).setAttach_no("[別紙"+(exceptionList.size())+"] ");
        					errList.add(dupCheckList.get(0));
        				}
        				vo.setAttach_no("[別紙"+(exceptionList.size())+"] ");
        				errList.add(vo);
            			continue;
            			*/
        			}
        		}
        		
        		if(!isInserted) {
        			try {
            			mapper.insertRakutenInfo(vo);
            		} catch (Exception e) {
            			/* 2020-07-10
            			// 에러 발생시 에러리스트에 넣은후 다음 레코드로 진행
            			log.debug("[ERR]: " + vo.getOrder_no());
            			errList.add(vo);
            			continue;
            			*/
    				}
                    log.debug("insertRakutenInfo seq_id :: " + vo.getSeq_id());
                    log.debug("==========================");
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
                
                String optionContent = vo.getProduct_option();
                if(optionContent != null && optionContent.length() > 1) {
					String[] arr = optionContent.split(CommonUtil.SPLIT_BY_NPER);
					HashSet<String> set = new HashSet<>();
					for (int i=0; i<arr.length; i++) {
						log.debug(String.format("[%d] :: %s", i, arr[i]));
					String[] dataArr = arr[i].split(CommonUtil.SPLIT_BY_COLON);
					String data = null;
					if (dataArr.length > 1) {
						// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
						data = dataArr[dataArr.length-1];
						log.debug(String.format("option value1 :: %s", data));
						set.add(data.trim());
					} 
					/*
					// [MOD-0826]
					else {
						// 콜론이 아닌 일본어자판 컴마로 나뉘어져있는 경우가 있어 처리
						data = arr[i].split(CommonUtil.JPCOMMA);
						for (String value2 : data) {
							log.debug(String.format("option value2 :: %s", value2));
							set.add(value2.trim());
						}
					}
					
					// 거의 없겠다만 콜론과 일본어자판 컴마가 같이 있는 경우도 있어 처리
					if (data[0].contains(CommonUtil.JPCOMMA)) {
						data = data[0].split(CommonUtil.JPCOMMA);
						for (String value3 : data) {
							log.debug(String.format("option value3 :: %s", value3));
					    		set.add(value3.trim());
					    	}
					    }
					    */
					}
					
					log.debug("final option set : " + set);
					
					for (String value : set) {
						transVO.setKeyword(value);
						transVO.setBefore_trans(value);
						
						transList = listMapper.getTransInfo(transVO);
						if (transList.size() == 0) {
								listMapper.addTransInfo(transVO);
					    }
					}
					log.debug("==========================");
                }
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
			// 2019-10-03: 세션에 에러리스트 및 개수 노출기능 제거
//			HttpSession session = req.getSession();
//			session.setAttribute("errSize", errList.size());
//			session.setAttribute("errList", errList);
			/* 2020-07-10
			if(errList.size() > 0) {
				ArrayList<RakutenDuplicateVO> duplList = new ArrayList<>();
				for(RakutenVO rvo : errList) {
					// 2019-10-08: 다건주문자에 대하여 파일다운로드시 널체크추가
					if(null != rvo.getProduct_option()) {
						rvo.setProduct_option(rvo.getProduct_option().replaceAll("\n", ""));
					}
					
					// 2019-10-09: 別紙처리
					RakutenDuplicateVO rdVO = new RakutenDuplicateVO();
					rdVO.setAttach_no(rvo.getAttach_no());
					rdVO.setProduct_id(rvo.getProduct_id());
					rdVO.setProduct_name(rvo.getProduct_name());
					rdVO.setProduct_option(rvo.getProduct_option());
					rdVO.setOrder_no(rvo.getOrder_no());
					rdVO.setDelivery_name(rvo.getDelivery_name());
					rdVO.setDelivery_surname(rvo.getDelivery_surname());
					rdVO.setDelivery_name_kana(rvo.getDelivery_name_kana());
					rdVO.setDelivery_surname_kana(rvo.getDelivery_surname_kana());
					rdVO.setUnit_no(rvo.getUnit_no());
					duplList.add(rdVO);
				}
				String[] header = {
						"別紙番号"
						, "注文番号"
						, "商品ID"
						, "商品名"
						, "項目・選択肢"
						, "個数"
						, "送付先姓"
						, "送付先名"
						, "送付先姓カナ"
						, "送付先名カナ"
				};
				String[] blank= {"","","","","","","","",""};
				try
				(
					FileOutputStream fos = new FileOutputStream(duplDownPath+"[DUPL] "+CommonUtil.getDate("YYYYMMdd", 0) + ".csv");
					Writer writer = new OutputStreamWriter(fos, fileEncoding);
					CSVWriter	csvWriter = new CSVWriter(writer
							, CSVWriter.DEFAULT_SEPARATOR
							, CSVWriter.NO_QUOTE_CHARACTER
							, CSVWriter.DEFAULT_ESCAPE_CHARACTER
							, CSVWriter.DEFAULT_LINE_END);
				) 
				{
					// 2019-10-09: 정해준 포맷으로 출력될수있게 처리 	
					StatefulBeanToCsv<RakutenDuplicateVO> beanToCsv = new StatefulBeanToCsvBuilder(writer)
			                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
			                    .build();
						
						csvWriter.writeNext(header);

			            try {
							//beanToCsv.write(duplList);
			            	for(String str : exceptionList) {
			            		for(RakutenDuplicateVO vo : duplList) {
			            			if(str.equals(vo.getOrder_no())) {
			            				beanToCsv.write(vo);
			            			}
			            		}
			            		csvWriter.writeNext(blank);
			            	}
						} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
							e.printStackTrace();
						}
				}
			}
			*/
			
			ret = "アップロードを完了しました。";
		}
		return ret;
	}
	
	public ArrayList<RakutenVO> getRakutenInfo(RakutenVO vo) {
		log.info("getRakutenInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug(vo);
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		return mapper.getRakutenInfo(vo);
	}
	
	public void deleteRakutenInfo(ArrayList<RakutenVO> list) {
		log.info("deleteRakutenInfo");
		log.debug(list);
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		for (RakutenVO vo : list) {
			mapper.deleteRakutenInfo(vo.getSeq_id());
		}
	}
	
	@Transactional
	public ArrayList<String> executeTranslate(ArrayList<RakutenVO> targetList) {
		log.info("executeTranslate");
		log.debug(targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IRakutenMapper rMapper = sqlSession.getMapper(IRakutenMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		/*
		 * 2020-07-23
		 * 
		 *   1. 라쿠텐파일 업로드시 한 주문번호에 여러건의 상품번호가 있을경우 
		 *   냉동냉장구분마스터(rakuten_frozen_info) 테이블에 해당 데이터들을 넣어준다.
		 *   2. 업로드후 주문정보화면에서 치환작업 진행시 
		 *   냉동냉장구분마스터(rakuten_frozen_info) 테이블에 데이터가 있을 경우
		 *   해당 데이터들에 대해서도 함께 진행한다.
		 * */
		ArrayList<RakutenVO> frozenList = rMapper.getRakutenFrozenInfo(new RakutenVO());
		ret.addAll(executeFrozenTranslate(frozenList));
		
		TranslationVO transVO = new TranslationVO();
		String transedName;
//		int productSetNo, unitNo;
		int unitNo;
		for (RakutenVO vo : targetList) {
			// 이전에 에러처리 된 데이터가 있을경우 제거
			TranslationErrorVO errVO = new TranslationErrorVO();
			errVO.setTrans_target_id(vo.getSeq_id());
			errVO.setTrans_target_type(CommonUtil.TRANS_TARGET_R);
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
					transedName = "";
				}
			} else {
				errVO.setErr_text(CommonUtil.TRANS_ERR);
				listMapper.insertTranslationErr(errVO);
				transedName = "";
			}
			// 상품세트수
			// [MOD-0819]
//			String[] arr = transedName.split(CommonUtil.SPLIT_BY_STAR);
//			productSetNo = Integer.parseInt(arr[1]);
			// 상품개수
			unitNo = Integer.parseInt(vo.getUnit_no());
			
			// [MOD-1011] 
			Integer intsu = new Integer (unitNo); 
			String sintsu = intsu.toString(); 
			String su = CommonUtil.hankakuNumToZenkaku(sintsu); 
			
			StringBuffer buf = null;
			String optionContent = vo.getProduct_option();
			if(optionContent != null && optionContent.length() > 1) {
				// 옵션에 대한 처리
				HashSet<String> cntCheck = new HashSet<>();
				HashMap<String, Integer> map = new HashMap<>();
				
				String[] strArr = vo.getProduct_option().split(CommonUtil.SPLIT_BY_NPER);
				ArrayList<String> list = new ArrayList<>();
				for (int i=0; i<strArr.length; i++) {
					// 이전에 에러처리 된 데이터가 있을경우 제거 (옵션이 여러개인 경우 중복제거)
					err_seq_id = listMapper.getTranslationErr(errVO);
					if (err_seq_id != null && err_seq_id != "") {
						errVO.setSeq_id(err_seq_id);
						listMapper.deleteTranslationErr(errVO);
					}
					
					log.debug(String.format("%d :: %s", i, strArr[i]));
					
					//2020.1.16 kim  옵션처리전에 注 표시가 있으면 처리하지 않고 다음 옵션처리함.
	            	if(null != strArr[i] && !strArr[i].contains("注")) {
						String[] data = strArr[i].split(CommonUtil.SPLIT_BY_COLON);
						String value = null;
						if (data.length > 1) {
							// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
							value = data[data.length-1];
							log.debug(String.format("option value1 :: %s", value));
							
							transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
							transVO.setKeyword(value.trim());
							searchRet = listMapper.getTransInfo(transVO);
							
							try {
								list.add(searchRet.get(0).getAfter_trans().trim());
							} catch (NullPointerException e) {
								errVO.setErr_text(CommonUtil.TRANS_ERR);
								listMapper.insertTranslationErr(errVO);
								continue;
							}
						} 
	            	}
					/*
					// [MOD-0826]
					else {
						// 콜론이 아닌 일본어자판 컴마로 나뉘어져있는 경우가 있어 처리
						data = strArr[i].split(CommonUtil.JPCOMMA);
						for (String value2 : data) {
							log.debug(String.format("option value2 :: %s", value2));
							list.add(value2.trim());
						}
					}
					
					// 거의 없겠다만 콜론과 일본어자판 컴마가 같이 있는 경우도 있어 처리
					if (data[0].contains(CommonUtil.JPCOMMA)) {
						data = data[0].split(CommonUtil.JPCOMMA);
						for (String value3 : data) {
							log.debug(String.format("option value3 :: %s", value3));
							list.add(value3.trim());
						}
					}
					*/
				}
				log.debug("option list : " + list);
				
				for (String str : list) {
					String trimmed = str.trim();
					if (cntCheck.add(trimmed)) {
						map.put(trimmed, 1);
					} else {
						// 이미 존재하는 옵션명의 경우 +1후 map에 저장
						int recnt = map.get(trimmed);
						map.put(trimmed, recnt+1);
					}
				}
				log.debug("option map : " + map);
				
				Set<String> optionNames = map.keySet();
				buf = new StringBuffer(transedName);
				buf.append(" ");
				
				// 2020/08/19 注文件数が１以上の場合、通常出荷にする。
				if (unitNo >1) {
				// 2020/01/27 ネコポス対応の為、注文件数が１以上の場合、通常出荷にする。
				//if (transedName.contains("全無") && unitNo >1) {
					buf = new StringBuffer(transedName + "×" + su);
				}
				
				for (String optionName : optionNames) {
					// 옵션개수, 상품개수를 곱하여 치환결과에 반영
//					buf.append(optionName + "*" + (map.get(optionName)*productSetNo*unitNo)); // [MOD-0819]
//					buf.append(optionName + "*" + (map.get(optionName)*unitNo)); // [MOD-1011] 
					
					Integer unitsu = map.get(optionName)*unitNo; 
					// [MOD-1011] 
					String unitsu1 = unitsu.toString(); 
					String su1 = CommonUtil.hankakuNumToZenkaku(unitsu1); 
					buf.append(optionName + "×" +su1); 

					if (optionNames.size() > 1) {
						buf.append(";");
					}
				}
			} else {
				// 옵션이 없는 경우, 상품세트수와 상품개수를 곱하여 치환결과에 반영
//				buf = new StringBuffer(arr[0] + "*" + (productSetNo*unitNo)); // [MOD-0819]
//				buf = new StringBuffer(transedName + "*" + unitNo); // [MOD-1011] 
				buf = new StringBuffer(transedName + "×" + su);
			}
			
			String last = buf.toString();
			String finalStr = null;
			try {
				finalStr = last.substring(0, last.lastIndexOf(","));
			} catch (StringIndexOutOfBoundsException e) {
				finalStr = last;
			}
			log.debug("final String : " + finalStr);
			
			// 지역별 배송코드 세팅 (csv다운로드 기능)
			RegionMasterVO rmVO = new RegionMasterVO();
			rmVO.setKeyword(vo.getDelivery_add1());
			ArrayList<RegionMasterVO> regionM = listMapper.getRegionMaster(rmVO);
			
			vo.setDelivery_company(regionM.get(0).getDelivery_company());
			log.debug("Update Rakuten info : " + vo);
			rMapper.updateRakutenInfo(vo);
			
			TranslationResultVO resultVO = new TranslationResultVO();
			resultVO.setTrans_target_id(vo.getSeq_id());
			resultVO.setTrans_target_type(CommonUtil.TRANS_TARGET_R);
			
			//20200116 kim　変換後が空白の場合、”.”を設定する。
			if (vo.getProduct_name().contains("別紙")) {
				resultVO.setResult_text(".");
			}else {
				resultVO.setResult_text(finalStr);
			}
			
			// 이미 치환된 결과가 있는 trans_target_id이면 update, 아니면 insert
			ArrayList<RakutenVO> transResult = rMapper.getTransResult(resultVO);
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
	
	@Transactional
	public ArrayList<String> executeFrozenTranslate(ArrayList<RakutenVO> targetList) {
		log.info("executeFrozenTranslate");
		log.debug(targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IRakutenMapper rMapper = sqlSession.getMapper(IRakutenMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		TranslationVO transVO = new TranslationVO();
		String transedName;
//		int productSetNo, unitNo;
		int unitNo;
		for (RakutenVO vo : targetList) {
			// 이전에 에러처리 된 데이터가 있을경우 제거
			TranslationErrorVO errVO = new TranslationErrorVO();
			errVO.setTrans_target_id(vo.getSeq_id());
			errVO.setTrans_target_type(CommonUtil.TRANS_TARGET_RF);
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
					transedName = "";
				}
			} else {
				errVO.setErr_text(CommonUtil.TRANS_ERR);
				listMapper.insertTranslationErr(errVO);
				transedName = "";
			}
			// 상품세트수
			// [MOD-0819]
//			String[] arr = transedName.split(CommonUtil.SPLIT_BY_STAR);
//			productSetNo = Integer.parseInt(arr[1]);
			// 상품개수
			unitNo = Integer.parseInt(vo.getUnit_no());
			
			// [MOD-1011] 
			Integer intsu = new Integer (unitNo); 
			String sintsu = intsu.toString(); 
			String su = CommonUtil.hankakuNumToZenkaku(sintsu); 
			
			StringBuffer buf = null;
			String optionContent = vo.getProduct_option();
			if(optionContent != null && optionContent.length() > 1) {
				// 옵션에 대한 처리
				HashSet<String> cntCheck = new HashSet<>();
				HashMap<String, Integer> map = new HashMap<>();
				
				String[] strArr = vo.getProduct_option().split(CommonUtil.SPLIT_BY_NPER);
				ArrayList<String> list = new ArrayList<>();
				for (int i=0; i<strArr.length; i++) {
					// 이전에 에러처리 된 데이터가 있을경우 제거 (옵션이 여러개인 경우 중복제거)
					err_seq_id = listMapper.getTranslationErr(errVO);
					if (err_seq_id != null && err_seq_id != "") {
						errVO.setSeq_id(err_seq_id);
						listMapper.deleteTranslationErr(errVO);
					}
					
					log.debug(String.format("%d :: %s", i, strArr[i]));
					
					//2020.1.16 kim  옵션처리전에 注 표시가 있으면 처리하지 않고 다음 옵션처리함.
	            	if(null != strArr[i] && !strArr[i].contains("注")) {
						String[] data = strArr[i].split(CommonUtil.SPLIT_BY_COLON);
						String value = null;
						if (data.length > 1) {
							// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
							value = data[data.length-1];
							log.debug(String.format("option value1 :: %s", value));
							
							transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
							transVO.setKeyword(value.trim());
							searchRet = listMapper.getTransInfo(transVO);
							
							try {
								list.add(searchRet.get(0).getAfter_trans().trim());
							} catch (NullPointerException e) {
								errVO.setErr_text(CommonUtil.TRANS_ERR);
								listMapper.insertTranslationErr(errVO);
								continue;
							}
						} 
	            	}
					/*
					// [MOD-0826]
					else {
						// 콜론이 아닌 일본어자판 컴마로 나뉘어져있는 경우가 있어 처리
						data = strArr[i].split(CommonUtil.JPCOMMA);
						for (String value2 : data) {
							log.debug(String.format("option value2 :: %s", value2));
							list.add(value2.trim());
						}
					}
					
					// 거의 없겠다만 콜론과 일본어자판 컴마가 같이 있는 경우도 있어 처리
					if (data[0].contains(CommonUtil.JPCOMMA)) {
						data = data[0].split(CommonUtil.JPCOMMA);
						for (String value3 : data) {
							log.debug(String.format("option value3 :: %s", value3));
							list.add(value3.trim());
						}
					}
					*/
				}
				log.debug("option list : " + list);
				
				for (String str : list) {
					String trimmed = str.trim();
					if (cntCheck.add(trimmed)) {
						map.put(trimmed, 1);
					} else {
						// 이미 존재하는 옵션명의 경우 +1후 map에 저장
						int recnt = map.get(trimmed);
						map.put(trimmed, recnt+1);
					}
				}
				log.debug("option map : " + map);
				
				Set<String> optionNames = map.keySet();
				buf = new StringBuffer(transedName);
				buf.append(" ");
				
				// 2020/01/27 ネコポス対応の為、注文件数が１以上の場合、通常出荷にする。
				if (transedName.contains("全無") && unitNo >1) {
					buf = new StringBuffer(transedName + "×" + su);
				}
				
				for (String optionName : optionNames) {
					// 옵션개수, 상품개수를 곱하여 치환결과에 반영
//					buf.append(optionName + "*" + (map.get(optionName)*productSetNo*unitNo)); // [MOD-0819]
//					buf.append(optionName + "*" + (map.get(optionName)*unitNo)); // [MOD-1011] 
					
					Integer unitsu = map.get(optionName)*unitNo; 
					// [MOD-1011] 
					String unitsu1 = unitsu.toString(); 
					String su1 = CommonUtil.hankakuNumToZenkaku(unitsu1); 
					buf.append(optionName + "×" +su1); 

					if (optionNames.size() > 1) {
						buf.append(";");
					}
				}
			} else {
				// 옵션이 없는 경우, 상품세트수와 상품개수를 곱하여 치환결과에 반영
//				buf = new StringBuffer(arr[0] + "*" + (productSetNo*unitNo)); // [MOD-0819]
//				buf = new StringBuffer(transedName + "*" + unitNo); // [MOD-1011] 
				buf = new StringBuffer(transedName + "×" + su);
			}
			
			String last = buf.toString();
			String finalStr = null;
			try {
				finalStr = last.substring(0, last.lastIndexOf(","));
			} catch (StringIndexOutOfBoundsException e) {
				finalStr = last;
			}
			log.debug("final String : " + finalStr);
			
			// 지역별 배송코드 세팅 (csv다운로드 기능)
			RegionMasterVO rmVO = new RegionMasterVO();
			rmVO.setKeyword(vo.getDelivery_add1());
			ArrayList<RegionMasterVO> regionM = listMapper.getRegionMaster(rmVO);
			
			vo.setDelivery_company(regionM.get(0).getDelivery_company());
			log.debug("Update Rakuten Frozen info : " + vo);
			rMapper.updateRakutenFrozenInfo(vo);
			
			TranslationResultVO resultVO = new TranslationResultVO();
			resultVO.setTrans_target_id(vo.getSeq_id());
			resultVO.setTrans_target_type(CommonUtil.TRANS_TARGET_RF);
			
			//20200116 kim　変換後が空白の場合、”.”を設定する。
			if (vo.getProduct_name().contains("別紙")) {
				resultVO.setResult_text(".");
			}else {
				resultVO.setResult_text(finalStr);
			}
			
			// 이미 치환된 결과가 있는 trans_target_id이면 update, 아니면 insert
			ArrayList<RakutenVO> transResult = rMapper.getTransResult(resultVO);
			if (transResult.size() > 0) {
				listMapper.modTransResult(resultVO);
				
				log.debug("seq_id : " + transResult.get(0).getSeq_id());
				ret.add(transResult.get(0).getSeq_id());
			} else {
				listMapper.addTransResult(resultVO);
				
				log.debug("seq_id : " + resultVO.getSeq_id());
				ret.add(resultVO.getSeq_id());
				log.debug("商品名 : " + resultVO.getProduct_name());
			}
			
		}
		
		return ret;
	}
	
	public ArrayList<RakutenVO> getTransResult(String id_lst) {
		log.info("getTransResult");
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] strArr = id_lst.split(",");
		ArrayList<String> list = new ArrayList<>();
		for (String str : strArr) {
			list.add(str);
		}
		log.debug("query id list : " + list);
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(list);
		
		ArrayList<RakutenVO> realRet = new ArrayList<>();
		ArrayList<RakutenVO> ret = mapper.getTransResult(vo);
		
		String orderNo;
		HashMap<String, ArrayList<Integer>> zenkoku = new HashMap<>();
		HashMap<String, ArrayList<Integer>> frozen = new HashMap<>();
		HashMap<String, ArrayList<Integer>> fridge = new HashMap<>();
		
		ArrayList<Integer> zenkokuCnt = new ArrayList<>();
		ArrayList<Integer> frozenCnt = new ArrayList<>();
		ArrayList<Integer> fridgeCnt = new ArrayList<>();
		
		HashSet<String> orderNoLst = new HashSet<>();
		
		for(int i=0; i<ret.size(); i++) {
			RakutenVO rVO = ret.get(i);
			orderNo = rVO.getOrder_no();
			
			if(!orderNoLst.add(orderNo)) {
				if (rVO.getProduct_name().contains("全国送料無料")) {
					zenkokuCnt.add(i);
					zenkoku.put(orderNo, zenkokuCnt);
				}else if (rVO.getProduct_name().contains("冷凍")) {
					frozenCnt.add(i);
					frozen.put(orderNo, frozenCnt);
				}else if (rVO.getProduct_name().contains("冷蔵")) {
					fridgeCnt.add(i);
					fridge.put(orderNo, fridgeCnt);
				}else {
					zenkokuCnt.add(i);
					zenkoku.put(orderNo, zenkokuCnt);
				}
			}else {
				zenkokuCnt = new ArrayList<>();
				frozenCnt = new ArrayList<>();
				fridgeCnt = new ArrayList<>();
				
				if (rVO.getProduct_name().contains("全国送料無料")) {
					zenkokuCnt.add(i);
					zenkoku.put(orderNo, zenkokuCnt);
				}else if (rVO.getProduct_name().contains("冷凍")) {
					frozenCnt.add(i);
					frozen.put(orderNo, frozenCnt);
				}else if (rVO.getProduct_name().contains("冷蔵")) {
					fridgeCnt.add(i);
					fridge.put(orderNo, fridgeCnt);
				}else {
					zenkokuCnt.add(i);
					zenkoku.put(orderNo, zenkokuCnt);
				}
			}
			
			
		}
		
		for(String key : orderNoLst) {
			ArrayList<Integer> iZen = zenkoku.get(key);
			ArrayList<Integer> iFro = frozen.get(key);
			ArrayList<Integer> iFri = fridge.get(key);
			
			if(null != iZen && iZen.size() > 1) {
				for(int j=0; j<iZen.size(); j++) {
					int val = iZen.get(j);
					if(j==0) {
						ret.get(val).setResult_text(".");
						realRet.add(ret.get(val));
					}
				}
			}else if(null != iFro && iFro.size() > 1) {
				for(int j=0; j<iFro.size(); j++) {
					int val = iFro.get(j);
					if(j==0) {
						ret.get(val).setResult_text(".");
						realRet.add(ret.get(val));
					}
				}
			}else if(null != iFri && iFri.size() > 1) {
				for(int j=0; j<iFri.size(); j++) {
					int val = iFri.get(j);
					if(j==0) {
						ret.get(val).setResult_text(".");
						realRet.add(ret.get(val));
					}
				}
			}else {
				if(null != iZen) {
					int val = iZen.get(0);
					realRet.add(ret.get(val));
				}
				if(null != iFro) {
					int val = iFro.get(0);
					realRet.add(ret.get(val));
				}
				if(null != iFri) {
					int val = iFri.get(0);
					realRet.add(ret.get(val));
				}
			}
		}
		
		RakutenVO.sortListVO(realRet, "getOrder_datetime", "DESC");
		
		return realRet;
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
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
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
			
			String[] header = CommonUtil.deliveryCompanyHeader("YAMA");
			
			// 야마토 포맷으로 바꾸기 전 치환된 결과와 함께 라쿠텐정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			vo.setDelivery_company(delivery_company);
			
			ArrayList<RakutenVO> realRet = new ArrayList<>();
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> yList = new ArrayList<>();
			
			String orderNo;
			HashMap<String, ArrayList<Integer>> zenkoku = new HashMap<>();
			HashMap<String, ArrayList<Integer>> frozen = new HashMap<>();
			HashMap<String, ArrayList<Integer>> fridge = new HashMap<>();
			
			ArrayList<Integer> zenkokuCnt = new ArrayList<>();
			ArrayList<Integer> frozenCnt = new ArrayList<>();
			ArrayList<Integer> fridgeCnt = new ArrayList<>();
			
			HashSet<String> orderNoLst = new HashSet<>();
			
			for(int i=0; i<list.size(); i++) {
				RakutenVO rVO = list.get(i);
				
				orderNo = rVO.getOrder_no();
				
				if(!orderNoLst.add(orderNo)) {
					if (rVO.getProduct_name().contains("全国送料無料")) {
						zenkokuCnt.add(i);
						zenkoku.put(orderNo, zenkokuCnt);
					}else if (rVO.getProduct_name().contains("冷凍")) {
						frozenCnt.add(i);
						frozen.put(orderNo, frozenCnt);
					}else if (rVO.getProduct_name().contains("冷蔵")) {
						fridgeCnt.add(i);
						fridge.put(orderNo, fridgeCnt);
					}else {
						zenkokuCnt.add(i);
						zenkoku.put(orderNo, zenkokuCnt);
					}
				}else {
					zenkokuCnt = new ArrayList<>();
					frozenCnt = new ArrayList<>();
					fridgeCnt = new ArrayList<>();
					
					if (rVO.getProduct_name().contains("全国送料無料")) {
						zenkokuCnt.add(i);
						zenkoku.put(orderNo, zenkokuCnt);
					}else if (rVO.getProduct_name().contains("冷凍")) {
						frozenCnt.add(i);
						frozen.put(orderNo, frozenCnt);
					}else if (rVO.getProduct_name().contains("冷蔵")) {
						fridgeCnt.add(i);
						fridge.put(orderNo, fridgeCnt);
					}else {
						zenkokuCnt.add(i);
						zenkoku.put(orderNo, zenkokuCnt);
					}
				}
			}
			
			for(String key : orderNoLst) {
				ArrayList<Integer> iZen = zenkoku.get(key);
				ArrayList<Integer> iFro = frozen.get(key);
				ArrayList<Integer> iFri = fridge.get(key);
				
				if(null != iZen && iZen.size() > 1) {
					for(int j=0; j<iZen.size(); j++) {
						int val = iZen.get(j);
						if(j==0) {
							list.get(val).setResult_text(".");
							realRet.add(list.get(val));
						}
					}
				}else if(null != iFro && iFro.size() > 1) {
					for(int j=0; j<iFro.size(); j++) {
						int val = iFro.get(j);
						if(j==0) {
							list.get(val).setResult_text(".");
							realRet.add(list.get(val));
						}
					}
				}else if(null != iFri && iFri.size() > 1) {
					for(int j=0; j<iFri.size(); j++) {
						int val = iFri.get(j);
						if(j==0) {
							list.get(val).setResult_text(".");
							realRet.add(list.get(val));
						}
					}
				}else {
					if(null != iZen) {
						int val = iZen.get(0);
						realRet.add(list.get(val));
					}
					if(null != iFro) {
						int val = iFro.get(0);
						realRet.add(list.get(val));
					}
					if(null != iFri) {
						int val = iFri.get(0);
						realRet.add(list.get(val));
					}
				}
			}
			
			RakutenVO.sortListVO(realRet, "getOrder_datetime", "DESC");
			
			for (RakutenVO tmp : realRet) {
				// 빠른배송을 옵션으로 둔 항목에 대하여 체크가 되어있으면 제외
				if ("1".equals(isChecked)) {
					if ("1".equals(tmp.getTomorrow_hope())) {
						log.debug("tomorrow hope checked and excepted!");
						continue;
					}
				}
				
				// 예외테이블에 추가한 목록에 대하여 야마토에서 제외
				ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
				boolean chkRet = false;
				for (ExceptionMasterVO exVO : exList) {
					if (tmp.getResult_text().contains(exVO.getException_data())) {
						//注文者とお届け先のお客様が同一の場合、佐川にて発送する。  2020/7/23  金
						String deliveryname = tmp.getDelivery_surname().trim() + tmp.getDelivery_name().trim();   //お届け先お客様
						String ordername =tmp.getOrder_surname().trim()+  tmp.getOrder_name().trim();		//注文者
						
						if (deliveryname.equals(ordername)) {					
							log.debug(String.format("exception_data: %s - result_txt: %s", exVO.getException_data(), tmp.getResult_text()));
							chkRet = true;
						}else{
							chkRet = false;
						}
					}
				}
//				if (chkRet) {
//					continue;
//				}
				
				/**
				 * 2020-07-28
				 * 例外地域マスタに登録されている地域情報はヤマトによって発送する。
				 * やまとにてDLする処理する。
				 * */
				boolean isExY = false;
				for(
						
						ExceptionRegionMasterVO region : exRegionList) {
					if(tmp.getDelivery_add1().contains(region.getException_data())) {
						isExY = true;
					}
				}
				
				if (chkRet && !isExY) {
					continue;
				}
				
				YamatoVO yVO = new YamatoVO();
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＳＴＡＲＴ
				if (tmp.getProduct_name().contains("全国送料無料") && tmp.getUnit_no() .equals("1") && !(tmp.getResult_text().equals("."))) {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_7);
				}else {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				}
				if (tmp.getProduct_name().contains("冷凍") || tmp.getResult_text().contains("冷凍")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_1);
				}
				if (tmp.getProduct_name().contains("冷蔵") || tmp.getResult_text().contains("冷蔵")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_2);		
				}
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
				
				yVO.setCustomer_no(tmp.getOrder_no().replace("\"", ""));
//				yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				
				// 2019/12/24  キム 야마토 배송날짜를 설정함. 　⇒　ＳＴＡＲＴ
				String strComment = tmp.getComment().replace("\n", "").replace("\"", "").replace("[配送日時指定:]", "").replace("[備考:]", "");
				// 備考欄の内容を分析して日付を設定する。
				if (strComment.contains(CommonUtil.TOMORROW_MORNING)) {
					yVO.setEstimate_delivery_date(CommonUtil.getDate("YYYY/MM/dd", 1));
					yVO.setDelivery_time(CommonUtil.YA_TOMORROW_MORNING_CODE);
				}
				if (strComment.contains(CommonUtil.TIMEMAP1)) {
					yVO.setDelivery_time(CommonUtil.YA_TOMORROW_TIMEMAP1);
				}	
				if (strComment.contains(CommonUtil.TIMEMAP2)) {
					yVO.setDelivery_time(CommonUtil.YA_TOMORROW_TIMEMAP2);
				}
				if (strComment.contains(CommonUtil.TIMEMAP3)) {
					yVO.setDelivery_time(CommonUtil.YA_TOMORROW_TIMEMAP3);
				}
				if (strComment.contains(CommonUtil.TIMEMAP4)) {
					yVO.setDelivery_time(CommonUtil.YA_TOMORROW_TIMEMAP4);
				}
				
				// 20200116 kim 時間帯指定：午前中で、お届け予定日が空白の場合、次の日付を設定する。
				if ("1".equals(tmp.getTomorrow_hope())) {
						yVO.setEstimate_delivery_date(CommonUtil.getDate("YYYY/MM/dd", 1));
				}
				
				String strXsplit ;
				if(strComment.contains("-")) {
					strXsplit = strComment.substring(0,4) + "/" + strComment.substring(5,7) +  "/" +strComment.substring(8,10);
					yVO.setEstimate_delivery_date(strXsplit);
				} 
				
				// 20200116 kim 記事(メモ)欄に領収書や連絡などの文字以外はすべて削除する。
				//if  (strComment.contains("領収書") || strComment.contains("連絡")) {
				//	yVO.setComment(strComment);
				//} else {
				//	yVO.setComment("");
					//}
				yVO.setComment(tmp.getComment().replace("\n", "").replace("\"", "").replace("[配送日時指定:]", "").replace("[備考:]", ""));
				// 2019/12/24  キム 야마토 배송날짜를 설정함. 　⇒　END
				
				yVO.setCollect_cash(tmp.getTotal_amt().replace("\"", ""));
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048242380101");
//				yVO.setBill_customer_code("048299821004-311");
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				//2020/01/13  キム お届け先名とご依頼主名がおなじではない場合、ご依頼主名を登録する。
				if(tmp.getDelivery_surname().equals(tmp.getOrder_surname()) && tmp.getDelivery_name().equals(tmp.getOrder_name() )) {
					yVO.setClient_name("有限会社ItempiaJapan (R)");
				}else {
					yVO.setClient_name(tmp.getOrder_surname() +  " " +  tmp.getOrder_name() + " (R)");
				}
//				yVO.setClient_name_kana(tmp.getOrder_surname_kana().replace("\"", "") + " " + tmp.getOrder_name_kana().replace("\"", ""));
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getDelivery_post_no1().replace("\"", "") + tmp.getDelivery_post_no2().replace("\"", ""));

				// 2019/12/24  キム ヤマト 주소 컬럼에 대하여 전각 16자 이상이면 안되는 사항이 있어 수정. 　⇒　ＳＴＡＲＴ
				String addStr = tmp.getDelivery_add1().replace("\"", "") + "" + tmp.getDelivery_add2().replace("\"", "") + "" + tmp.getDelivery_add3().replace("\"", "");
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
				//yVO.setDelivery_add(tmp.getDelivery_add1().replace("\"", "") + "" + tmp.getDelivery_add2().replace("\"", "") + "" + tmp.getDelivery_add3().replace("\"", ""));
				try {
					String strdeliveryname = tmp.getDelivery_name();
					if (!strdeliveryname.equals(null)) {
						yVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
					}
				}
				catch (NullPointerException e){
					yVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", ""));				
				}
				// 2019/12/24  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
//				yVO.setDelivery_name_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana());
				yVO.setDelivery_name_title(CommonUtil.TITLE_SAMA);
				yVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));

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
				
				// あす楽希望이 1인 경우
        		if (tmp.getTomorrow_hope().equals("1")) {
        			yVO.setDelivery_time(CommonUtil.YA_TOMORROW_MORNING_CODE);
        		}
				
				//yVO.setProduct_name1(tmp.getResult_text().replace("\"", ""));
				
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
	
	public void rakutenFormatCSVDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String type
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		rakutenFormatCSVDownload(null, response, id_lst, fileEncoding, type, delivery_company);
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
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
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
			
			// 사가와 포맷으로 바꾸기 전 치환된 결과와 함께 라쿠텐정보 얻기
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
//			例外マスタの注文情報をＤＬするため、運送会社マスタのチェックを外す。　2020.06.01
//			vo.setDelivery_company(delivery_company);
			
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
//			boolean chkRet = false;
			ArrayList<ArakuVO> sList = new ArrayList<>();
			ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
			
			for (RakutenVO tmp : list) {
				/**
				 * 사가와 대상 목록중 예외지역마스터(例外地域マスタ)에 있는 값인 경우
				 * 해당 데이터의 배송회사를 야마토로 update치고
				 * 야마토로 다운로드 될 수 있게 처리
				 * */
				boolean isEx = false;
				for(ExceptionRegionMasterVO region : exRegionList) {
					if(tmp.getDelivery_add1().contains(region.getException_data())) {
						isEx = true;
						RakutenVO rv = new RakutenVO();
						rv.setSeq_id(tmp.getReal_seq_id());
						rv.setDelivery_company("1001");
						mapper.updateRakutenInfo(rv);
					}
				}
				
				if(isEx) continue;
				
				for (ExceptionMasterVO exVO : exList) {
//						chkRet = false;
					if (tmp.getResult_text().contains(exVO.getException_data())) {
//							chkRet = true;
						SagawaVO sVO = new SagawaVO();
						sVO.setDelivery_add1(tmp.getDelivery_add1().replace("\"", ""));
						sVO.setDelivery_add2(tmp.getDelivery_add2().replace("\"", ""));
						sVO.setDelivery_add3(tmp.getDelivery_add3().replace("\"", ""));
						sVO.setDelivery_post_no(tmp.getDelivery_post_no1().replace("\"", "") + "-" +  tmp.getDelivery_post_no2().replace("\"", ""));
						sVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
						sVO.setDelivery_name1(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
//							sVO.setDelivery_name2(tmp.getDelivery_name().replace("\"", "") + " " + CommonUtil.TITLE_SAMA); //様는 제거하고 두개로 나누지 않고 1에만 이름을 세팅
						
						sVO.setClient_add1("埼玉県川口市");
						sVO.setClient_add2("上青木西１丁目19-39エレガンス滝澤ビル1F");
						sVO.setClient_name1("有限会社");
						sVO.setClient_name2("ItempiaJapan (R)");
						sVO.setClient_tel("048-242-3801");
						
						// あす楽希望이 1인 경우
		        		if (tmp.getTomorrow_hope().equals("1")) {
		        			sVO.setDelivery_time(CommonUtil.SA_TOMORROW_MORNING_CODE);
		        		}
		        		
		        		// 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
		        		if  (tmp.getDelivery_date_sel() != null) {
		        			sVO.setDelivery_date(tmp.getDelivery_date_sel().replaceAll("/", "").replaceAll("-", ""));
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
//					if (chkRet) {
//						continue;
//					}
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
	
	@SuppressWarnings("unchecked")
	public void rakutenFormatCSVDownload(
			HttpServletRequest request
			, HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String type
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rakutenFormatCSVDownload");
		
		log.debug("encoding : " + fileEncoding);
		log.debug("type : " + type);
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = type + CommonUtil.getDate("YYYY-MM-dd HH:mm:ss", 0) + ".csv";

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
			
			String[] header = CommonUtil.rakutenHeader();
			ArrayList<RakutenVO> list = null;
			if ("YU".equals(type)) {
				log.debug("seq_id_list : " + id_lst.toString());
				RakutenVO vo = new RakutenVO();
				ArrayList<String> seq_id_list = new ArrayList<>();
				for (String seq_id : id_lst) {
					seq_id_list.add(seq_id);
				}
				vo.setSeq_id_list(seq_id_list);
				vo.setDelivery_company(delivery_company);
				
				list = mapper.getRakutenInfo(vo);
				
				for (RakutenVO tmp : list) {
					tmp.setDelivery_name(tmp.getDelivery_name() + " " + CommonUtil.TITLE_SAMA);
				}
				
			} else if ("ERR".equals(type)) {
				HttpSession session = request.getSession();
				list = (ArrayList<RakutenVO>) session.getAttribute("errList");
				session.setAttribute("errSize", "");
				session.setAttribute("errList", "");
			}
			
			for (RakutenVO tmp : list) {
				String comment = tmp.getComment();
				comment = comment.replace("\n", "");
				tmp.setComment(comment);
				
				String option = tmp.getProduct_option();
				if (option != null) {
					option = option.replace("\n", "");
					tmp.setProduct_option(option);
				}
			}
			
			executeYUDownload(csvWriter, writer, header, list);
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
	public void executeYUDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<RakutenVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<RakutenVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
	
	@Transactional
	public void checkRakutenInfo(MultipartFile file, String fileEncoding, HttpServletRequest req) throws IOException {
		log.info("checkRakutenInfo");
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + file.getContentType());
		log.debug("name: " + file.getName());
		log.debug("original name: " + file.getOriginalFilename());
		log.debug("size: " + file.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), fileEncoding));
			
			// 2019-10-09: 발송처리된 목록을 업로드했을때 니모쯔칸리방고가 존재하는 데이터에 대해 해당 주문번호의 데이터를 DB상에서 삭제처리 진행
			CsvToBean<RakutenDeleteVO> csvToBean = new CsvToBeanBuilder<RakutenDeleteVO>(reader)
                    .withType(RakutenDeleteVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<RakutenDeleteVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	RakutenDeleteVO vo = iterator.next();
            	
            	// 니모쯔칸리방고가 있는 데이터의 경우 주문번호로 데이터를 찾아 삭제처리
            	if(null != vo.getBaggage_claim_no()) {
            		RakutenVO searchVO = new RakutenVO();
                	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
                	searchVO.setOrder_no(vo.getOrder_no());
                	
                	ArrayList<RakutenVO> dupCheckList = mapper.getRakutenInfo(searchVO);
            		
            		// 이미 존재하는 受注番号가 있으면
            		if (dupCheckList.size() == 1) {
        				// seq_id로 데이터 삭제 
        				mapper.deleteRakutenInfo(dupCheckList.get(0).getSeq_id());
            		}
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void rakutenSagawaUpdate(MultipartFile sagaUpload, String fileEncoding) throws IOException {
		log.info("rakutenSagawaUpdate");
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + sagaUpload.getContentType());
		log.debug("name: " + sagaUpload.getName());
		log.debug("original name: " + sagaUpload.getOriginalFilename());
		log.debug("size: " + sagaUpload.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(sagaUpload.getInputStream(), fileEncoding));
			
			CsvToBean<NewSagawaVO> csvToBean = new CsvToBeanBuilder<NewSagawaVO>(reader)
                    .withType(NewSagawaVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<NewSagawaVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	NewSagawaVO vo = iterator.next();
            	
            	// 데이터가 있는지 체크
            	RakutenVO searchVO = new RakutenVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SAGAWA);
            	String[] strArr = vo.getDelivery_name1().split("　");
            	if(strArr.length > 1) {
            		searchVO.setDelivery_name(strArr[1]);
            	} else {
            		searchVO.setDelivery_name(vo.getDelivery_name1());
            	}
            	if(vo.getDelivery_tel() != null) {
        			String[] strArr2 = vo.getDelivery_tel().split("-");
        			if(strArr2.length > 2) {
        				searchVO.setDelivery_tel1(strArr2[0]);
        				searchVO.setDelivery_tel2(strArr2[1]);
        				searchVO.setDelivery_tel3(strArr2[2]);
        			}
        		}
            	
            	/* 2019-10-14
            	 * 送付先名, 送付先電話番号1-3
            	 * 두가지를 키값으로하여
            	 * お問合せ送り状No(contact_no) 값을 갱신
            	 * */
            	ArrayList<RakutenVO> searchRetList = mapper.getRakutenInfo(searchVO);
            	
            	RakutenVO searchRet = new RakutenVO();
            	if (searchRetList.size() == 1) {
            		// 결과값이 1건이 아닌경우는 키가된 값에대하여 결과값이 제대로 나오지 못한 경우이므로 continue
            		searchRet = searchRetList.get(0);
            	} else {
            		continue;
            	}
            	
            	searchVO.setSeq_id(searchRet.getSeq_id());
            	searchVO.setBaggage_claim_no(vo.getContact_no());
            	searchVO.setDelivery_company("1002");
            	
            	// お荷物伝票番号값 update
            	mapper.updateRakutenInfo(searchVO);
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void rakutenYamatoUpdate(MultipartFile yamaUpload, String fileEncoding) throws IOException {
		log.info("rakutenYamatoUpdate");
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + yamaUpload.getContentType());
		log.debug("name: " + yamaUpload.getName());
		log.debug("original name: " + yamaUpload.getOriginalFilename());
		log.debug("size: " + yamaUpload.getSize());
		
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
            	RakutenVO searchVO = new RakutenVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getCustomer_no());
            	
            	/*
            	 * お客様管理番号(customer_no) 값을 키로 해서
            	 * 伝票番号(slip_no) 값을 갱신
            	 * */
            	ArrayList<RakutenVO> searchRetList = mapper.getRakutenInfo(searchVO);
            	
            	if (searchRetList.size() > 0) {
            		RakutenVO searchRet = searchRetList.get(0);
           		
            		searchVO.setSeq_id(searchRet.getSeq_id());
                	searchVO.setBaggage_claim_no(vo.getSlip_no().trim());
                	searchVO.setDelivery_company("1001");
                	
                	// お荷物伝票番号값 update
                	mapper.updateRakutenInfo(searchVO);
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void rCSVDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rCSVDownload");
		log.debug("seq_id_list : " + id_lst.toString());
		log.debug("delivery_company : " + delivery_company);
		log.debug("encoding : " + fileEncoding);
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String fileNm = "1001".equals(delivery_company) ? "YAMA" : "SAGA";
			String csvFileName = "R_"+fileNm+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = {"注文番号", "送付先ID", "発送明細ID", "お荷物伝票番号", "配送会社", "発送日"};
			
			RCSVDownVO vo = new RCSVDownVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			vo.setSeq_id_list(seq_id_list);
			vo.setDelivery_company(delivery_company);
			ArrayList<ArakuVO> list = new ArrayList<>();
			ArrayList<RCSVDownVO> csvList = mapper.getRCSVDownList(vo);
			
			/*楽天更新ファイル画面にてヤマトボタン押下時に
			 * 配送会社：1001とお荷物伝票番号が存在するもののみDLしたいです。
			 */
			for(RCSVDownVO csv : csvList) {
				if(null != csv.getBaggage_claim_no() &&  !"".equals(csv.getBaggage_claim_no())) {
					list.add(csv);
				}
			}
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, list);
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
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
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
			
			String[] header = CommonUtil.deliveryCompanyHeader("CLICK");
			
			log.debug("seq_id_list : " + id_lst.toString());
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> cList = new ArrayList<>();

			for (RakutenVO tmp : list) {
				if(tmp.getProduct_name().contains("【全国送料無料】")) {
				/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
					ClickPostVO cVO = new ClickPostVO();
					cVO.setPost_no(tmp.getDelivery_post_no1().replace("\"", "") + "-" +  tmp.getDelivery_post_no2().replace("\"", ""));
					cVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
					cVO.setDelivery_add1(tmp.getDelivery_add1().replace("\"", ""));
					cVO.setDelivery_add2(tmp.getDelivery_add2().replace("\"", ""));
					// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
					String addStr = tmp.getDelivery_add3().replace("\"", "");
					if(addStr.length() > 20) {
						cVO.setDelivery_add3(addStr.substring(0, 20));
						cVO.setDelivery_add4(addStr.substring(20, addStr.length()));
					}else {
						cVO.setDelivery_add3(tmp.getDelivery_add3().replace("\"", ""));
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String createClickpostCsvFile(String fileEncoding, String cpDownPath, String[] id_lst) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		String ret = "CLICKPOST DOWNLOAD FAILED";
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(seq_id_list);
		
		ArrayList<RakutenVO> list = mapper.getTransResult(vo);
		ArrayList<ArakuVO> cList = new ArrayList<>();
		
		for (RakutenVO tmp : list) {
			if(tmp.getProduct_name().contains("【全国送料無料】")) {
			/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
				ClickPostVO cVO = new ClickPostVO();
				cVO.setPost_no(tmp.getDelivery_post_no1().replace("\"", "") + "-" +  tmp.getDelivery_post_no2().replace("\"", ""));
				cVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
				cVO.setDelivery_add1(tmp.getDelivery_add1().replace("\"", ""));
				cVO.setDelivery_add2(tmp.getDelivery_add2().replace("\"", ""));
				// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
				String addStr = tmp.getDelivery_add3().replace("\"", "");
				if(addStr.length() > 20) {
					cVO.setDelivery_add3(addStr.substring(0, 20));
					cVO.setDelivery_add4(addStr.substring(20, addStr.length()));
				}else {
					cVO.setDelivery_add3(tmp.getDelivery_add3().replace("\"", ""));
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
	
	public void downloadGlobalSagawa(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("downloadGlobalSagawa");
		
		log.debug("encoding : " + fileEncoding);
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
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
			
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			ArrayList<ExceptionRegionMasterVO> exRegionList = listMapper.getExceptionRegionMaster(null);
			ArrayList<ArakuVO> gsaList = new ArrayList<>();

			for (RakutenVO tmp : list) {
//				/**
//				 * 사가와 대상 목록중 예외지역마스터(例外地域マスタ)에 있는 값인 경우
//				 * 해당 데이터의 배송회사를 야마토로 update치고
//				 * 야마토로 다운로드 될 수 있게 처리
//				 * */
//				boolean isEx = false;
//				for(ExceptionRegionMasterVO region : exRegionList) {
//					if(tmp.getDelivery_add1().contains(region.getException_data())) {
//						isEx = true;
//						RakutenVO rv = new RakutenVO();
//						rv.setSeq_id(tmp.getReal_seq_id());
//						rv.setDelivery_company("1001");
//						mapper.updateRakutenInfo(rv);
//					}
//				}
//				
//				if(isEx) continue;
				
				/**
				 * 2020-07-30
				 * 주문번호로 frozen_info에 검색시 
				 * 결과가 한건이라도 존재한다면 글로벌사가와에 대한 대상이 아니므로 continue처리
				 * */
				boolean isEx = false;
				RakutenVO forFrozenSrch = new RakutenVO();
				forFrozenSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				forFrozenSrch.setOrder_no(tmp.getOrder_no());
				ArrayList<RakutenVO> frozenList = mapper.getRakutenFrozenInfo(forFrozenSrch);
				if(frozenList.size() > 0) {
					isEx = true;
				}
				
				// ヤマト宅配便対応　
				if(isEx) continue;
				
				/**
				 * 2020-07-28
				 * 例外地域マスタに登録されている地域情報はヤマトによって発送する。
				 * やまとにてDLする処理する。
				 * */
				for(ExceptionRegionMasterVO region : exRegionList) {
					log.debug(String.format("exception_data: %s - result_txt: %s", region.getException_data(), tmp.getResult_text()));
					if(tmp.getDelivery_add1().contains(region.getException_data())) {
						isEx = true;
					}
				}
				
				// ヤマト宅配便対応　
				if(isEx) continue;
				
				// getDelivery_name() = null の場合、
				try {
					//注文者とお届け先のお客様が同一ではない場合、やまとにて発送する。  2020/7/23  金
					String deliveryname = tmp.getDelivery_surname().trim() + tmp.getDelivery_name().trim();   //お届け先お客様
					String ordername =tmp.getOrder_surname().trim()+  tmp.getOrder_name().trim();		//注文者
					if (!deliveryname.equals(null) || !ordername.equals(null)) {
						if (!deliveryname.equals(ordername)) {					
							isEx = true;
						}else{
							isEx = false;
						}
					}
				}
				catch (NullPointerException e){
					isEx = false;
				}
				
				// ヤマト宅配便対応
				if(isEx) continue;
				
				for (ExceptionMasterVO exVO : exList) {
					String str= tmp.getResult_text();
					// 例外テーブルに含んている場合、ファイル作成するように変更する。　2020/06/01
					if(str.contains(exVO.getException_data()) && isEx != Boolean.TRUE) {
						
						
						if(str.length() > 10) {
							for (int i = 0; i < str.length(); i += 10) {
								GlobalSagawaDownVO gsaVO = new GlobalSagawaDownVO();
								gsaVO.setSeller_cd("Fastbox2");
								gsaVO.setPick_dt(CommonUtil.getDate("YYYYMMdd", 0));
								gsaVO.setOrder_no(tmp.getOrder_no());
								
								// getDelivery_name() = null の場合、
								try {
									String strdeliveryname = tmp.getDelivery_name();
									if (!strdeliveryname.equals(null)) {
										gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
										//gsaVO.setConsign_nm_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana().replace("\"", ""));
									}
								}
									catch (NullPointerException e){
										gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") );	
								}
								
								// getDelivery_surname_kana() = null の場合、
								try {
									String strdeliverysurnamekana = tmp.getDelivery_surname_kana();
									if (!strdeliverysurnamekana.equals(null)) {
										gsaVO.setConsign_nm_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana().replace("\"", ""));
									}
								}
								catch (NullPointerException ex){
									gsaVO.setConsign_nm_kana("");
								}
								//gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
								//gsaVO.setConsign_nm_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana().replace("\"", ""));
								gsaVO.setConsign_add1(tmp.getDelivery_add1());
								gsaVO.setConsign_add2(tmp.getDelivery_add2() + " " + tmp.getDelivery_add3());
								gsaVO.setConsign_post_no(tmp.getDelivery_post_no1()+"-"+tmp.getDelivery_post_no2());
								gsaVO.setConsign_tel(tmp.getDelivery_tel1()+"-"+tmp.getDelivery_tel2()+"-"+tmp.getDelivery_tel3());
								// あす楽希望이 1인 경우
				        		if (tmp.getTomorrow_hope().equals("1")) {
				        			gsaVO.setDelivery_tm(CommonUtil.SA_TOMORROW_MORNING_CODE);
				        		}
				        		
				        		// 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
				        		if  (tmp.getDelivery_date_sel() != null) {
				        			gsaVO.setDelivery_dt(tmp.getDelivery_date_sel().replaceAll("/", "").replaceAll("-", ""));
				        		}
								gsaVO.setPkg(tmp.getUnit_no());
								gsaVO.setItem_nm(str.substring(i, Math.min(i + 10, str.length())).trim());
								gsaVO.setItem_origin("JP");
								gsaList.add(gsaVO);
							}
						}else {
							GlobalSagawaDownVO gsaVO = new GlobalSagawaDownVO();
							gsaVO.setSeller_cd("Fastbox2");
							gsaVO.setPick_dt(CommonUtil.getDate("YYYYMMdd", 0));
							gsaVO.setOrder_no(tmp.getOrder_no());
							
							// getDelivery_name() = null の場合、
							try {
								String strdeliveryname = tmp.getDelivery_name();
								if (!strdeliveryname.equals(null)) {
									gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
								}
							}
							catch (NullPointerException e){
									gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") );
							}		
							
							// getDelivery_surname_kana() = null の場合、
							try {
								String strdeliverysurnamekana = tmp.getDelivery_surname_kana();
								if (!strdeliverysurnamekana.equals(null)) {
									gsaVO.setConsign_nm_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana().replace("\"", ""));
								}
							}
							catch (NullPointerException ex){
									gsaVO.setConsign_nm_kana("");
							}	

							//gsaVO.setConsign_nm(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
							//gsaVO.setConsign_nm_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana().replace("\"", ""));
							gsaVO.setConsign_add1(tmp.getDelivery_add1());
							gsaVO.setConsign_add2(tmp.getDelivery_add2() + " " + tmp.getDelivery_add3());
							gsaVO.setConsign_post_no(tmp.getDelivery_post_no1()+"-"+tmp.getDelivery_post_no2());
							gsaVO.setConsign_tel(tmp.getDelivery_tel1()+"-"+tmp.getDelivery_tel2()+"-"+tmp.getDelivery_tel3());
							// あす楽希望이 1인 경우
			        		if (tmp.getTomorrow_hope().equals("1")) {
			        			gsaVO.setDelivery_tm(CommonUtil.SA_TOMORROW_MORNING_CODE);
			        		}
			        		
			        		// 2019-09-21: 배송일 컬럼에 대하여 YYYYMMDD의 형태로 처리
			        		if  (tmp.getDelivery_date_sel() != null) {
			        			gsaVO.setDelivery_dt(tmp.getDelivery_date_sel().replaceAll("/", "").replaceAll("-", ""));
			        		}
							gsaVO.setPkg(tmp.getUnit_no());
							gsaVO.setItem_nm(str);
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
	
	public int deleteRakutenFrozenInfo() {
		log.info("deleteRakutenFrozenInfo");
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		return mapper.deleteRakutenFrozenInfo();
	}
	
	public ArrayList<RakutenVO> getAllData() {
		ArrayList<RakutenVO> ret = new ArrayList<>();
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		ret.addAll(mapper.getRakutenInfo(null));
		ret.addAll(mapper.getRakutenFrozenInfo(null));
		return ret;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void uriageDownload(
			HttpServletResponse response
			, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("uriageDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "[R]URIAGE" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.rakutenHeader();
			ArrayList<RakutenVO> targetList = getAllData();
			ArrayList<RakutenVOForUriage> list = new ArrayList<>();
			for(RakutenVO vo : targetList) {
				RakutenVOForUriage uriage = new RakutenVOForUriage();
				uriage.setOrder_no(vo.getOrder_no());
				uriage.setOrder_status(vo.getOrder_status());
				uriage.setSub_status_id(vo.getSub_status_id());
				uriage.setSub_status(vo.getSub_status());
				uriage.setOrder_datetime(vo.getOrder_datetime());
				uriage.setOrder_date(vo.getOrder_date());
				uriage.setOrder_time(vo.getOrder_time());
				uriage.setCancel_due_date(vo.getCancel_due_date());
				uriage.setOrder_check_datetime(vo.getOrder_check_datetime());
				uriage.setOrder_confirm_datetime(vo.getOrder_confirm_datetime());
				uriage.setDelivery_eta_datetime(vo.getDelivery_eta_datetime());
				uriage.setDelivery_ata_datetime(vo.getDelivery_ata_datetime());
				uriage.setPay_method_name(vo.getPay_method_name());
				uriage.setCreadit_pay_method(vo.getCreadit_pay_method());
				uriage.setCredit_pay_times(vo.getCredit_pay_times());
				uriage.setDelivery_method(vo.getDelivery_method());
				uriage.setDelivery_type(vo.getDelivery_type());
				uriage.setOrder_type(vo.getOrder_type());
				uriage.setMulti_destination_flag(vo.getMulti_destination_flag());
				uriage.setDestination_match_flag(vo.getDestination_match_flag());
				uriage.setIsland_flag(vo.getIsland_flag());
				uriage.setRverify_flag(vo.getRverify_flag());
				uriage.setWarning_type(vo.getWarning_type());
				uriage.setRmember_flag(vo.getRmember_flag());
				uriage.setPurchase_hist_mod_flag(vo.getPurchase_hist_mod_flag());
				uriage.setTotal_goods_amt(vo.getTotal_goods_amt());
				uriage.setTotal_consume_tax(vo.getTotal_consume_tax());
				uriage.setTotal_shipping(vo.getTotal_shipping());
				uriage.setGross_deduction(vo.getGross_deduction());
				uriage.setInvoice_amt(vo.getInvoice_amt());
				uriage.setTotal_amt(vo.getTotal_amt());
				uriage.setPoint_usage(vo.getPoint_usage());
				uriage.setTotal_coupon_usage(vo.getTotal_coupon_usage());
				uriage.setStore_coupon_usage(vo.getStore_coupon_usage());
				uriage.setRakuten_coupon_usage(vo.getRakuten_coupon_usage());
				uriage.setOrder_post_no1(vo.getOrder_post_no1());
				uriage.setOrder_post_no2(vo.getOrder_post_no2());
				uriage.setOrder_add1(vo.getOrder_add1());
				uriage.setOrder_add2(vo.getOrder_add2());
				uriage.setOrder_add3(vo.getOrder_add3());
				uriage.setOrder_surname(vo.getOrder_surname());
				uriage.setOrder_name(vo.getOrder_name());
				uriage.setOrder_surname_kana(vo.getOrder_surname_kana());
				uriage.setOrder_name_kana(vo.getOrder_name_kana());
				uriage.setOrder_tel1(vo.getOrder_tel1());
				uriage.setOrder_tel2(vo.getOrder_tel2());
				uriage.setOrder_tel3(vo.getOrder_tel3());
				uriage.setOrder_email(vo.getOrder_email());
				uriage.setOrder_sex(vo.getOrder_sex());
				uriage.setRequest_no(vo.getRequest_no());
				uriage.setRequest_received_no(vo.getRequest_received_no());
				uriage.setShip_id(vo.getShip_id());
				uriage.setShip_charge(vo.getShip_charge());
				uriage.setShip_substitute_fee(vo.getShip_substitute_fee());
				uriage.setShip_total_consume_tax(vo.getShip_total_consume_tax());
				uriage.setShip_total_goods_amt(vo.getShip_total_goods_amt());
				uriage.setShip_total_amt(vo.getShip_total_amt());
				uriage.setIndicates(vo.getIndicates());
				uriage.setDelivery_post_no1(vo.getDelivery_post_no1());
				uriage.setDelivery_post_no2(vo.getDelivery_post_no2());
				uriage.setDelivery_add1(vo.getDelivery_add1());
				uriage.setDelivery_add2(vo.getDelivery_add2());
				uriage.setDelivery_add3(vo.getDelivery_add3());
				uriage.setDelivery_surname(vo.getDelivery_surname());
				uriage.setDelivery_name(vo.getDelivery_name());
				uriage.setDelivery_surname_kana(vo.getDelivery_surname_kana());
				uriage.setDelivery_name_kana(vo.getDelivery_name_kana());
				uriage.setDelivery_tel1(vo.getDelivery_tel1());
				uriage.setDelivery_tel2(vo.getDelivery_tel2());
				uriage.setDelivery_tel3(vo.getDelivery_tel3());
				uriage.setProduct_detail_id(vo.getProduct_detail_id());
				uriage.setProduct_id(vo.getProduct_id());
				uriage.setProduct_name(vo.getProduct_name());
				uriage.setProduct_no(vo.getProduct_no());
				uriage.setProduct_manage_no(vo.getProduct_manage_no());
				uriage.setUnit_price(vo.getUnit_price());
				uriage.setUnit_no(vo.getUnit_no());
				uriage.setDelivery_cost_include(vo.getDelivery_cost_include());
				uriage.setTax_exclude(vo.getTax_exclude());
				uriage.setSubstitute_fee_include(vo.getSubstitute_fee_include());
				uriage.setPoint_multiple(vo.getPoint_multiple());
				uriage.setDelivery_info(vo.getDelivery_info());
				uriage.setInventory_type(vo.getInventory_type());
				uriage.setWrap_title1(vo.getWrap_title1());
				uriage.setWrap_name1(vo.getWrap_name1());
				uriage.setWrap_amt1(vo.getWrap_amt1());
				uriage.setWrap_tax_include1(vo.getWrap_tax_include1());
				uriage.setWrap_type1(vo.getWrap_type1());
				uriage.setWrap_title2(vo.getWrap_title2());
				uriage.setWrap_name2(vo.getWrap_name2());
				uriage.setWrap_amt2(vo.getWrap_amt2());
				uriage.setWrap_tax_include2(vo.getWrap_tax_include2());
				uriage.setWrap_type2(vo.getWrap_type2());
				uriage.setDelivery_time(vo.getDelivery_time());
				uriage.setDelivery_date_sel(vo.getDelivery_date_sel());
				uriage.setManager(vo.getManager());
				uriage.setQuick_note(vo.getQuick_note());
				uriage.setMsg_to_customer(vo.getMsg_to_customer());
				uriage.setGift_request(vo.getGift_request());
				uriage.setUtil_terminal(vo.getUtil_terminal());
				uriage.setMail_carrier_code(vo.getMail_carrier_code());
				uriage.setTomorrow_hope(vo.getTomorrow_hope());
				uriage.setDrug_order_flag(vo.getDrug_order_flag());
				uriage.setRakuten_super_deal(vo.getRakuten_super_deal());
				uriage.setMembership_program(vo.getMembership_program());
				list.add(uriage);
			}
			
			StatefulBeanToCsv<RakutenVOForUriage> beanToCSV = new StatefulBeanToCsvBuilder(writer)
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
