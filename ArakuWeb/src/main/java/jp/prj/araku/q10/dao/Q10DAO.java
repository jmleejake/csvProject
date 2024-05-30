package jp.prj.araku.q10.dao;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import jp.prj.araku.file.vo.ClickPostVO;
import jp.prj.araku.file.vo.NewYamatoVO;
import jp.prj.araku.file.vo.SagawaVO;
import jp.prj.araku.file.vo.YamatoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.PrdTransVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.SubTranslationVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.q10.mapper.IQ10Mapper;
import jp.prj.araku.q10.vo.Q10VO;
import jp.prj.araku.q10.vo.Q10YamatoVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class Q10DAO {
	@Autowired
	SqlSession sqlSession;
	
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Transactional
	public void insertQ10Info(MultipartFile qUpload, String fileEncoding, String type) throws IOException {
		log.debug("insertQ10Info");
		
		IQ10Mapper mapper =sqlSession.getMapper(IQ10Mapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + qUpload.getContentType());
		log.debug("name: " + qUpload.getName());
		log.debug("original name: " + qUpload.getOriginalFilename());
		log.debug("size: " + qUpload.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(qUpload.getInputStream(), fileEncoding));
			
			CsvToBean<Q10VO> csvToBean = new CsvToBeanBuilder<Q10VO>(reader)
                    .withType(Q10VO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<Q10VO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	Q10VO vo = iterator.next();
            	
            	if("SALES".equals(type)) {
            		PrdCdMasterVO prdVO = new PrdCdMasterVO();
            		prdVO.setPrd_cd(vo.getItem_no());
            		prdVO.setTarget_type(CommonUtil.TRANS_TARGET_Q);
            		ArrayList<PrdCdMasterVO> prdMaster = listMapper.getPrdCdMaster(prdVO);
            		if(prdMaster.size() < 1) {
            			continue;
            		}
            	}
            	
            	if(vo.getOrder_no() != null) {
            		
            		Q10VO srch = new Q10VO();
            		srch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            		srch.setOrder_no(vo.getOrder_no());
            		if(mapper.getQ10Info(srch).size() > 0) {
            			continue;
            		}
            		try {
            			mapper.insertQ10Info(vo);
            		} catch (Exception e) {
            			log.error(e.getMessage());
            			continue;
    				}
                    log.debug("inserted qoo10 seq_id :: " + vo.getSeq_id());
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
                    
                    String optionContent = vo.getOption_info();
                    if(optionContent != null && optionContent.length() > 1) {
    					String[] arr = optionContent.split(CommonUtil.SPLIT_BY_SLASH);
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
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public ArrayList<Q10VO> getQ10Info(Q10VO vo) {
		log.debug("getQ10Info");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", vo);
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		return mapper.getQ10Info(vo);
	}
	
	@Transactional
	public ArrayList<String> executeTranslate(ArrayList<Q10VO> targetList) {
		log.debug("executeTranslate");
		log.debug("{}", targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		
		TranslationVO transVO = new TranslationVO();
		String transedName = "";
		String trans_seq_id = ""; // 2021.06.11
		

		
		int unitNo = 1;
		StringBuffer buf = null;
		
		for (Q10VO vo : targetList) {
			// [MOD-1011] 
			Integer intsu = new Integer (unitNo); 
			String sintsu = intsu.toString(); 
			String su = CommonUtil.hankakuNumToZenkaku(sintsu); 
			unitNo = Integer.parseInt(vo.getQty());
			
			try {
				buf = new StringBuffer(transedName);
			}catch(NullPointerException e) {
				buf = new StringBuffer("o ");
			}; 
			buf.append(" ");
			
			// 이전에 에러처리 된 데이터가 있을경우 제거
			TranslationErrorVO errVO = new TranslationErrorVO();
			errVO.setTrans_target_id(vo.getSeq_id());
			errVO.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
			String err_seq_id = listMapper.getTranslationErr(errVO);
			if (err_seq_id != null && err_seq_id != "") {
				errVO.setSeq_id(err_seq_id);
				listMapper.deleteTranslationErr(errVO);
			}
			
			transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			transVO.setKeyword(vo.getProduct_name());
			ArrayList<TranslationVO> searchRet = listMapper.getTransInfo(transVO);
			
			if(searchRet.size() > 0) {
				trans_seq_id = searchRet.get(0).getSeq_id(); // 2021.06.11
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
			
			/**
			 * 2021.01.09 치환시 주문정보를 商品中間マスタ로 insert처리
			 * */
			PrdTransVO prdTransVO = new PrdTransVO();
			prdTransVO.setOrder_no(vo.getOrder_no());
			prdTransVO.setOrder_gbn("1");
			prdTransVO.setBefore_trans(vo.getProduct_name());
			prdTransVO.setAfter_trans(transedName);
			
			//buf = new StringBuffer(transedName + "×" + su);
			buf.append(transedName + "×" + su);
			
			transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			transVO.setKeyword(vo.getProduct_name());
			searchRet = listMapper.getTransInfo(transVO);
			prdTransVO.setBefore_trans(searchRet.get(0).getBefore_trans());
			prdTransVO.setJan_cd(searchRet.get(0).getJan_cd()); // 2023-08-11 kim
			
//			prdTransVO.setPrd_cnt(unitNo+"");
			if(searchRet.get(0).getPrd_cnt() != null) {  //  2023-08-11 kim NULLチェックが必要
			      int COUNT = unitNo* Integer.parseInt(searchRet.get(0).getPrd_cnt());
			      prdTransVO.setPrd_cnt(Integer.toString(COUNT)); //  2023-08-11 kim　COUNT 처리
			}else {
				prdTransVO.setPrd_cnt(unitNo+"");
			}
			
			prdTransVO.setPrd_master_hanei_gbn("0");
			prdTransVO.setSearch_type("translate");
			prdTransVO.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
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
						prdTransVO.setOrder_no(vo.getOrder_no());
						prdTransVO.setOrder_gbn("1");
						prdTransVO.setBefore_trans(subTrans.getBefore_trans());
						prdTransVO.setAfter_trans(subTrans.getAfter_trans());
						prdTransVO.setJan_cd(subTrans.getJan_cd()); // 2023-08-11 kim
						prdTransVO.setPrd_cnt(subTrans.getPrd_cnt());
						prdTransVO.setPrd_master_hanei_gbn("0");
						prdTransVO.setSearch_type("translate");
						prdTransVO.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
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
			// 県 府 都 道
			String state = vo.getAddress();
			if(null != state && !"".equals(state)) {
				if(state.indexOf("県") > -1) {
					state = state.substring(0, state.indexOf("県")+1);
				} else if(state.indexOf("都") > -1) {
					state = state.substring(0, state.indexOf("都")+1);
				} else if(state.indexOf("府") > -1) {
					state = state.substring(0, state.indexOf("府")+1);
				} else if(state.indexOf("道") > -1) {
					state = state.substring(0, state.indexOf("道")+1);
				}
				rmVO.setKeyword(state);
				ArrayList<RegionMasterVO> regionM = listMapper.getRegionMaster(rmVO);
				
				vo.setDelivery_company(regionM.get(0).getDelivery_company());
				log.debug("Update Q10 info : " + vo);
				mapper.updateQ10Info(vo);
			}
			
			//注文商品数
			unitNo = Integer.parseInt(vo.getQty());
			
//			Integer intsu = new Integer (unitNo); 
//			String sintsu = intsu.toString(); 
//			String su = CommonUtil.hankakuNumToZenkaku(sintsu);
//			
//			StringBuffer buf = null;
			String optionContent = vo.getOption_info();
			if(optionContent != null && optionContent.length() > 1) {
				// 옵션에 대한 처리
				HashSet<String> cntCheck = new HashSet<>();
				HashMap<String, Integer> map = new HashMap<>();
				
				String[] strArr = vo.getOption_info().split(CommonUtil.SPLIT_BY_SLASH);
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
						} catch (IndexOutOfBoundsException e) {
							errVO.setErr_text(CommonUtil.TRANS_ERR);
							listMapper.insertTranslationErr(errVO);
							continue;
						}
					} 
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
				try {
					buf = new StringBuffer(transedName);
				}catch(NullPointerException e) {
					buf = new StringBuffer("o ");
				}; 
				buf.append(" ");
				
				int i = 1;
				for (String optionName : optionNames) {
					
					Integer unitsu = map.get(optionName)*unitNo; 
					// [MOD-1011] 
					String unitsu1 = unitsu.toString(); 
					String su1 = CommonUtil.hankakuNumToZenkaku(unitsu1); 
					buf.append(optionName + "×" +su1); 

					if (optionNames.size() > 1) {
						buf.append(";");
					}
					
					/**
					 * 2021.01.09 치환시 주문정보를 商品中間マスタ로 insert처리
					 * */
					PrdTransVO prdTransVO2 = new PrdTransVO();
					prdTransVO2.setOrder_no(vo.getOrder_no());
					prdTransVO2.setOrder_gbn(i+"");
					prdTransVO2.setAfter_trans(optionName);
					
					transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					transVO.setKeyword(optionName.trim());
					searchRet = listMapper.getTransInfo(transVO);
					prdTransVO2.setBefore_trans(searchRet.get(0).getBefore_trans());
					prdTransVO2.setJan_cd(searchRet.get(0).getJan_cd()); // 2023-08-11 kim
					
//					prdTransVO2.setPrd_cnt(unitNo+"");
					if(searchRet.get(0).getPrd_cnt() != null) {  //  2023-08-11 kim NULLチェックが必要
					      int COUNT = unitNo* Integer.parseInt(searchRet.get(0).getPrd_cnt());
					      prdTransVO2.setPrd_cnt(Integer.toString(COUNT)); //  2023-08-11 kim　COUNT 처리
					}else {
						prdTransVO2.setPrd_cnt(unitNo+"");
					}
					
					prdTransVO2.setPrd_master_hanei_gbn("0");
					prdTransVO2.setSearch_type("translate");
					prdTransVO2.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
					ArrayList<PrdTransVO> prdTransRet2 = listMapper.getPrdTrans(prdTransVO2);
					if(prdTransRet2.size() > 0) {
						prdTransVO2.setSeq_id(prdTransRet2.get(0).getSeq_id());
						listMapper.updatePrdTrans(prdTransVO2);
					}else {
						listMapper.insertPrdTrans(prdTransVO2);
					}
					i++;
				}
			} else {
				// buf = new StringBuffer(transedName + "×" + su);
				buf = new StringBuffer(transedName + "×" + unitNo);
			}
			
			String last = buf.toString();
			
			// 商品名に「；」があると次の商品名の内容が切られるため修正する　2024/05/30 kim
//			String finalStr = null;
//			try {
//				finalStr = last.substring(0, last.lastIndexOf(";"));
//			} catch (StringIndexOutOfBoundsException e) {
//				finalStr = last;
//			}
//			log.debug("final String : " + finalStr);
			
			TranslationResultVO resultVO = new TranslationResultVO();
			resultVO.setTrans_target_id(vo.getSeq_id());
			resultVO.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
			resultVO.setResult_text(last);
			
			// 이미 치환된 결과가 있는 trans_target_id이면 update, 아니면 insert
			ArrayList<Q10VO> transResult = mapper.getTransResult(resultVO);
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
	
	public ArrayList<Q10VO> getTransResult(String id_lst) {
		log.debug("getTransResult");
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] strArr = id_lst.split(",");
		ArrayList<String> list = new ArrayList<>();
		for (String str : strArr) {
			list.add(str);
		}
		log.debug("query id list : " + list);
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(list);
		return mapper.getTransResult(vo);
	}
	
	public void deleteQ10Info(ArrayList<Q10VO> list) {
		log.debug("deleteQ10Info");
		log.debug("{}", list);
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		for(Q10VO vo : list) {
			mapper.deleteQ10Info(vo.getSeq_id());
		}
	}
	
	public void yamatoFormatDownload(
			HttpServletResponse response
			, String[] id_lst, String fileEncoding
			, String delivery_company, String storage) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("yamatoFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String frontMsg = !"".equals(storage)?storage+"SOUKO_YAMATO_":"YAMATO_";
			String csvFileName = frontMsg+CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			TranslationResultVO vo = new TranslationResultVO();
			vo.setSeq_id_list(seq_id_list);
			//vo.setDelivery_company(delivery_company);
			// 2021-07-03 lee S
			ArrayList<String> deli_com_list = new ArrayList<String>();
			deli_com_list.add(delivery_company);
			deli_com_list.add("1011");
			vo.setDeli_com_list(deli_com_list);
			// 2021-07-03 lee E
			
			ArrayList<Q10VO> list = mapper.getTransResult(vo);
			ArrayList<ArakuVO> yList = new ArrayList<>();
			
			// 예외테이블에 추가한 목록에 대하여 제2창고 목록으로 떨굴수있게 처리
			// 2021-07-23 야마토 제1창고, 2창고 구분 S
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			ArrayList<Q10VO> str1List = new ArrayList<Q10VO>();
			ArrayList<Q10VO> str2List = new ArrayList<Q10VO>();
			boolean exChk = false;
			for (Q10VO tmp : list) {
				for (ExceptionMasterVO exVO : exList) {
					if (tmp.getResult_text().contains(exVO.getException_data())) {
						exChk = true;
						if("2".equals(storage)) {
							str2List.add(tmp);
						}
					}
				}
				if(!exChk) {
					str1List.add(tmp);
				}
				//例外マスタの情報有無チェックフラグを初期化する。　21.7.24 kim
				exChk = false;
			}
			if("1".equals(storage)) {
				list = str1List;
			}else if("2".equals(storage)) {
				list = str2List;
			}
			// 2021-07-23 야마토 제1창고, 2창고 구분 E
			
			for (Q10VO tmp : list) {
				YamatoVO yVO = new YamatoVO();
				// 2020/06/20  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＳＴＡＲＴ
				// 2020/10/30  キム クリックポストが1件以上の場合、一般伝票する対応。			
				if (tmp.getResult_text().contains("全無") && tmp.getQty().equals("1") && !(tmp.getResult_text().equals("."))) {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_7);
				}	else {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				}
				if (tmp.getResult_text().contains("宅コン") && tmp.getQty().equals("1")) {
					yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_8);
				}	
				if (tmp.getResult_text().contains("冷凍")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_1);
				}
				if (tmp.getResult_text().contains("冷蔵")) {
					yVO.setCool_type(CommonUtil.COOL_TYPE_2);		
				}
				// 2020/06/20  キム 클리크포스트를 야마토 ネコポス로 설정함. 　⇒　ＥＮＤ
				yVO.setCustomer_no(tmp.getOrder_no().replace("\"", ""));
				//yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				yVO.setCollect_cash(tmp.getTotal_price().replace("\"", ""));
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048299821004-311");
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				yVO.setClient_name("有限会社ItempiaJapan (Q)");
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getPost_no().replace("\"", "").replace("-", "").replace("'", ""));
				// 2021/4/14  キム ヤマト 주소 컬럼에 대하여 16자 이상이면 次のカラムに設定する. 　⇒　ＳＴＡＲＴ
				//yVO.setDelivery_add(tmp.getAddress().replace("\"", ""));
				String addStr = tmp.getAddress().replace("\"", "");
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
				// 2021/4/14  キム ヤマト 주소 컬럼에 대하여 16자 이상이면 次のカラムに設定する. 　⇒　END
				yVO.setDelivery_name(tmp.getRecpt_name().replace("\"", ""));
				yVO.setDelivery_name_title(CommonUtil.TITLE_SAMA);
				String phone_no = tmp.getRecpt_mobile_no();
				if("-".equals(phone_no)) {
					phone_no = tmp.getRecpt_phone_no();
				}
				yVO.setDelivery_tel(phone_no);
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
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("sagawaFormatDownload");
		
		log.debug("encoding : " + fileEncoding);
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
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
			vo.setDelivery_company(delivery_company);
			
			ArrayList<Q10VO> list = mapper.getTransResult(vo);
			ArrayList<ExceptionMasterVO> exList = listMapper.getExceptionMaster(null);
			boolean chkRet = false;
			ArrayList<ArakuVO> sList = new ArrayList<>();
			
			for (Q10VO tmp : list) {
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
				
				// 県 府 都 道
				String add = tmp.getAddress();
				String tmpStr = "";
				String state = "";
				if(add.indexOf("県") > -1) {
					state = add.substring(0, add.indexOf("県")+1);
					tmpStr = add.substring(add.indexOf("県")+1, add.length());
				} else if(add.indexOf("都") > -1) {
					state = add.substring(0, add.indexOf("都")+1);
					tmpStr = add.substring(add.indexOf("都")+1, add.length());
				} else if(add.indexOf("府") > -1) {
					state = add.substring(0, add.indexOf("府")+1);
					tmpStr = add.substring(add.indexOf("府")+1, add.length());
				} else if(add.indexOf("道") > -1) {
					state = add.substring(0, add.indexOf("道")+1);
					tmpStr = add.substring(add.indexOf("道")+1, add.length());
				}
				
				// 市 区
				String city = "";
				if(tmpStr.indexOf("市") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("市")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("市")+1, tmpStr.length());
				} else if(tmpStr.indexOf("区") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("区")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("区")+1, tmpStr.length());
				}
				
				sVO.setDelivery_add1(state.replace("\"", ""));
				sVO.setDelivery_add2(city.replace("\"", ""));
				sVO.setDelivery_add3(tmpStr.replace("\"", ""));
				sVO.setDelivery_post_no(tmp.getPost_no().replace("\"", "").replace("'", ""));
				String phone_no = tmp.getRecpt_mobile_no();
				if("-".equals(phone_no)) {
					phone_no = tmp.getRecpt_phone_no();
				}
				sVO.setDelivery_tel(phone_no);
				sVO.setDelivery_name1(tmp.getRecpt_name().replace("\"", ""));
				
				sVO.setClient_add1("埼玉県川口市");
				sVO.setClient_add2("上青木西１丁目19-39エレガンス滝澤ビル1F");
				sVO.setClient_name1("有限会社");
				sVO.setClient_name2("ItempiaJapan (Q)");
				sVO.setClient_tel("048-242-3801");
				
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String createClickpostCsvFile(String fileEncoding, String cpDownPath, String[] id_lst) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		String ret = "CLICKPOST DOWNLOAD FAILED";
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(seq_id_list);
		
		ArrayList<Q10VO> list = mapper.getTransResult(vo);
		ArrayList<ArakuVO> cList = new ArrayList<>();
		
		for (Q10VO tmp : list) {
			if(tmp.getResult_text().contains("全無")) {
			/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
				ClickPostVO cVO = new ClickPostVO();
				cVO.setPost_no(tmp.getPost_no().replace("\"", "").replace("'", ""));
				cVO.setDelivery_name(tmp.getRecpt_name().replace("\"", ""));
				
				// 県 府 都 道
				String add = tmp.getAddress();
				String tmpStr = "";
				String state = "";
				if(add.indexOf("県") > -1) {
					state = add.substring(0, add.indexOf("県")+1);
					tmpStr = add.substring(add.indexOf("県")+1, add.length());
				} else if(add.indexOf("都") > -1) {
					state = add.substring(0, add.indexOf("都")+1);
					tmpStr = add.substring(add.indexOf("都")+1, add.length());
				} else if(add.indexOf("府") > -1) {
					state = add.substring(0, add.indexOf("府")+1);
					tmpStr = add.substring(add.indexOf("府")+1, add.length());
				} else if(add.indexOf("道") > -1) {
					state = add.substring(0, add.indexOf("道")+1);
					tmpStr = add.substring(add.indexOf("道")+1, add.length());
				}
				
				// 市 区
				String city = "";
				if(tmpStr.indexOf("市") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("市")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("市")+1, tmpStr.length());
				} else if(tmpStr.indexOf("区") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("区")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("区")+1, tmpStr.length());
				}
				cVO.setDelivery_add1(state.replace("\"", ""));
				cVO.setDelivery_add2(city.replace("\"", ""));
				// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
				String addStr = tmpStr.replace("\"", "");
				if(addStr.length() > 20) {
					cVO.setDelivery_add3(addStr.substring(0, 20));
					cVO.setDelivery_add4(addStr.substring(20, addStr.length()));
				}else {
					cVO.setDelivery_add3(tmpStr.replace("\"", ""));
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
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(seq_id_list);
		
		ArrayList<Q10VO> list = mapper.getTransResult(vo);
		ArrayList<ArakuVO> cList = new ArrayList<>();
		ArrayList<ClickPostVO> cList2 = new ArrayList<>();
		
		for (Q10VO tmp : list) {
			if(tmp.getResult_text().contains("全無")) {
			/*if(tmp.getProduct_name().indexOf("[全国送料無料]") != -1) {*/
				ClickPostVO cVO = new ClickPostVO();
				cVO.setPost_no(tmp.getPost_no().replace("\"", "").replace("'", ""));
				cVO.setDelivery_name(tmp.getRecpt_name().replace("\"", ""));
				
				// 県 府 都 道
				String add = tmp.getAddress();
				String tmpStr = "";
				String state = "";
				if(add.indexOf("県") > -1) {
					state = add.substring(0, add.indexOf("県")+1);
					tmpStr = add.substring(add.indexOf("県")+1, add.length());
				} else if(add.indexOf("都") > -1) {
					state = add.substring(0, add.indexOf("都")+1);
					tmpStr = add.substring(add.indexOf("都")+1, add.length());
				} else if(add.indexOf("府") > -1) {
					state = add.substring(0, add.indexOf("府")+1);
					tmpStr = add.substring(add.indexOf("府")+1, add.length());
				} else if(add.indexOf("道") > -1) {
					state = add.substring(0, add.indexOf("道")+1);
					tmpStr = add.substring(add.indexOf("道")+1, add.length());
				}
				
				// 市 区
				String city = "";
				if(tmpStr.indexOf("市") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("市")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("市")+1, tmpStr.length());
				} else if(tmpStr.indexOf("区") > -1) {
					city = tmpStr.substring(0, tmpStr.indexOf("区")+1);
					tmpStr = tmpStr.substring(tmpStr.indexOf("区")+1, tmpStr.length());
				}
				cVO.setDelivery_add1(state.replace("\"", ""));
				cVO.setDelivery_add2(city.replace("\"", ""));
				// 2019-09-28 크리쿠포스트 주소 컬럼에 대하여 전각 20자 이상이면 안되는 사항이 있어 수정.
				String addStr = tmpStr.replace("\"", "");
				if(addStr.length() > 20) {
					cVO.setDelivery_add3(addStr.substring(0, 20));
					cVO.setDelivery_add4(addStr.substring(20, addStr.length()));
				}else {
					cVO.setDelivery_add3(tmpStr.replace("\"", ""));
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
			String csvFileName = "[Q]URIAGE" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.q10Header();
			IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
			ArrayList<Q10VO> list = mapper.getQ10Info(null);
			
			StatefulBeanToCsv<Q10VO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
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
	
	public void q10YamatoUpdate(MultipartFile yamaUpload, String fileEncoding) throws IOException {
		log.debug("q10YamatoUpdate");
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + yamaUpload.getContentType());
		log.debug("name: " + yamaUpload.getName());
		log.debug("original name: " + yamaUpload.getOriginalFilename());
		log.debug("size: " + yamaUpload.getSize());
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		
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
            	Q10VO searchVO = new Q10VO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getCustomer_no());
            	
            	/*
            	 * お客様管理番号(customer_no) 값을 키로 해서
            	 * 伝票番号(slip_no) 값을 갱신
            	 * */
            	ArrayList<Q10VO> searchRetList = mapper.getQ10Info(searchVO);
            	
            	if (searchRetList.size() > 0) {
            		Q10VO searchRet = searchRetList.get(0);
            		
            		searchVO.setSeq_id(searchRet.getSeq_id());
                	searchVO.setInvoice_no(vo.getSlip_no());
                	searchVO.setDelivery_company("1001");
                	
                	// お荷物伝票番号값 update
                	mapper.updateQ10Info(searchVO);
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void downloadQ10File(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException  {
		
		IQ10Mapper mapper = sqlSession.getMapper(IQ10Mapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for(String seq_id : id_lst) {
			seq_id_list.add(seq_id);
		}
		Q10VO vo = new Q10VO();
		vo.setSeq_id_list(seq_id_list);
		ArrayList<Q10VO> list = mapper.getQ10Info(vo);
		ArrayList<ArakuVO> downList = new ArrayList<ArakuVO>();
		for(Q10VO vo2 : list) {
			if(null != vo2.getInvoice_no()) {
				Q10YamatoVO vo3 = new Q10YamatoVO();
				vo3.setDelivery_sts(vo2.getDelivery_sts());
				vo3.setOrder_no(vo2.getOrder_no());
				vo3.setCart_no(vo2.getCart_no());
				vo3.setDelivery_company_q10(vo2.getDelivery_company_q10());
				vo3.setInvoice_no(vo2.getInvoice_no());
				vo3.setShip_date(vo2.getShip_date());
				vo3.setShip_eta(vo2.getShip_eta());
				vo3.setProduct_name(vo2.getProduct_name());
				vo3.setQty(vo2.getQty());
				vo3.setOption_info(vo2.getOption_info());
				vo3.setOption_cd(vo2.getOption_cd());
				vo3.setRecpt_name(vo2.getRecpt_name());
				vo3.setProduct_cd(vo2.getProduct_cd());
				vo3.setAds_no_site(vo2.getAds_no_site());
				vo3.setPay_site(vo2.getPay_site());
				downList.add(vo3);
			}
		}
		
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		try {
			String csvFileName = "Q10YAMA" + CommonUtil.getDate("YYYYMMdd", 0)+".csv";

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
			
			CommonUtil.executeCSVDownload(csvWriter, writer, CommonUtil.q10YamatoHeader(), downList);
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