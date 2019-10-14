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
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.rakuten.mapper.IRakutenMapper;
import jp.prj.araku.rakuten.vo.RakutenDeleteVO;
import jp.prj.araku.rakuten.vo.RakutenDuplicateVO;
import jp.prj.araku.rakuten.vo.RakutenVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

/**
 * [MOD-1011] 半角→全角へ変換する。　　kim
 * */
@Repository
public class RakutenDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.rakuten");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public String insertRakutenInfo(MultipartFile rakUpload, String fileEncoding, HttpServletRequest req, String duplDownPath) throws IOException {
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
		ArrayList<RakutenVO> errList = new ArrayList<>();
		// 2019-10-09: 別紙처리
    	HashSet<String> exceptionList = new HashSet<>();
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
            	
            	// 2019-10-05: 注1】이라는 항목에 대해서 빈 값으로 처리후 insert처리
            	if(null != vo.getProduct_option() && "注1".contains(vo.getProduct_option())) {
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
            	
            	// 데이터 중복체크
            	RakutenVO searchVO = new RakutenVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getOrder_no());
            	
            	ArrayList<RakutenVO> dupCheckList = mapper.getRakutenInfo(searchVO);
        		
            	// 이미 존재하는 受注番号가 있으면
        		if (dupCheckList.size() == 1) {
        			// 2019-10-08: 업로드시 존재하는 주문번호가 있다면 등록일을 현재일로 갱신
        			mapper.updateRakutenRegistDate(dupCheckList.get(0).getSeq_id());
        			// 商品ID가 같으면
        			if (vo.getProduct_id().equals(dupCheckList.get(0).getProduct_id())) {
        				// 다음 레코드로 진행
                		continue;
        			} else {
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
        			}
        		}
        		
        		// あす楽希望이 1인 경우
        		if (null != vo.getTomorrow_hope() && vo.getTomorrow_hope().equals("1")) {
        			// お届け日指定 컬럼에 데이터 등록일 +1
        			vo.setDelivery_date_sel(CommonUtil.getDate("yyyy/MM/dd", 1));
        			// お届け時間帯 컬럼에 午前中
        			vo.setDelivery_time(CommonUtil.TOMORROW_MORNING);
        		}
        		
        		try {
        			mapper.insertRakutenInfo(vo);
        		} catch (Exception e) {
        			// 에러 발생시 에러리스트에 넣은후 다음 레코드로 진행
        			log.debug("[ERR]: " + vo.getOrder_no());
        			errList.add(vo);
        			continue;
				}
                log.debug("inserted rakuten seq_id :: " + vo.getSeq_id());
                log.debug("==========================");
                
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
				finalStr = last.substring(0, last.lastIndexOf(";"));
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
			resultVO.setResult_text(finalStr);
			
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
		
		return mapper.getTransResult(vo);
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
			
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> yList = new ArrayList<>();
			
			for (RakutenVO tmp : list) {
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
						log.debug(String.format("exception_data: %s - result_txt: %s", exVO.getException_data(), tmp.getResult_text()));
						chkRet = true;
					}
				}
				if (chkRet) {
					continue;
				}
				YamatoVO yVO = new YamatoVO();
				yVO.setCustomer_no(tmp.getOrder_no().replace("\"", ""));
				yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				yVO.setComment(tmp.getComment().replace("\n", "").replace("\"", "").replace("[配送日時指定:]", "").replace("[備考:]", ""));
				yVO.setCollect_cash(tmp.getTotal_amt().replace("\"", ""));
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048299821004-311");
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				yVO.setClient_name("有限会社ItempiaJapan (R)");
//				yVO.setClient_name_kana(tmp.getOrder_surname_kana().replace("\"", "") + " " + tmp.getOrder_name_kana().replace("\"", ""));
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getDelivery_post_no1().replace("\"", "") + tmp.getDelivery_post_no2().replace("\"", ""));
				yVO.setDelivery_add(tmp.getDelivery_add1().replace("\"", "") + "" + tmp.getDelivery_add2().replace("\"", "") + "" + tmp.getDelivery_add3().replace("\"", ""));
				yVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
//				yVO.setDelivery_name_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana());
				yVO.setDelivery_name_title(CommonUtil.TITLE_SAMA);
				yVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
				
				// あす楽希望이 1인 경우
        		if (tmp.getTomorrow_hope().equals("1")) {
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
			vo.setDelivery_company(delivery_company);
			
			ArrayList<RakutenVO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			boolean chkRet = false;
			ArrayList<ArakuVO> sList = new ArrayList<>();
			
			for (RakutenVO tmp : list) {
				for (ExceptionMasterVO exVO : exList) {
					chkRet = false;
					if (tmp.getResult_text().contains(exVO.getException_data())) {
						chkRet = true;
					}
				}
				if (chkRet) {
					continue;
				}
				SagawaVO sVO = new SagawaVO();
				sVO.setDelivery_add1(tmp.getDelivery_add1().replace("\"", ""));
				sVO.setDelivery_add2(tmp.getDelivery_add2().replace("\"", ""));
				sVO.setDelivery_add3(tmp.getDelivery_add3().replace("\"", ""));
				sVO.setDelivery_post_no(tmp.getDelivery_post_no1().replace("\"", "") + "-" +  tmp.getDelivery_post_no2().replace("\"", ""));
				sVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
				sVO.setDelivery_name1(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
//				sVO.setDelivery_name2(tmp.getDelivery_name().replace("\"", "") + " " + CommonUtil.TITLE_SAMA); //様는 제거하고 두개로 나누지 않고 1에만 이름을 세팅
				
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
                	searchVO.setDelivery_tel1(strArr2[0]);
                	searchVO.setDelivery_tel2(strArr2[1]);
                	searchVO.setDelivery_tel3(strArr2[2]);
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
                	searchVO.setBaggage_claim_no(vo.getSlip_no());
                	
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
	
	public void rCSVDownload(HttpServletResponse response, String[] id_lst, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rCSVDownload");
		log.debug("seq_id_list : " + id_lst.toString());
		log.debug("encoding : " + fileEncoding);
		
		IRakutenMapper mapper = sqlSession.getMapper(IRakutenMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "R" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			ArrayList<ArakuVO> list = mapper.getRCSVDownList(vo);
			
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
}
