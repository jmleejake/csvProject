package jp.prj.araku.list.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.amazon.mapper.IAmazonMapper;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.EtcMasterVO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.House3MasterVO;
import jp.prj.araku.list.vo.KeywordSearchInfo;
import jp.prj.araku.list.vo.OrderSumOriginalVO;
import jp.prj.araku.list.vo.OrderSumVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.PrdTransVO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.SagawaUpdateVO;
import jp.prj.araku.list.vo.SubTranslationVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.q10.mapper.IQ10Mapper;
import jp.prj.araku.q10.vo.Q10VO;
import jp.prj.araku.rakuten.mapper.IRakutenMapper;
import jp.prj.araku.rakuten.vo.RakutenVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;
import jp.prj.araku.yahoo.mapper.IYahooMapper;
import jp.prj.araku.yahoo.vo.YahooVO;

/**
 * @comment
 * [MOD-0819] 상품명에서 추출하는 개수는 계산하는데 필요없다
 * [MOD-0826] 콜론으로만 델리미터를 처리할 예정이므로 일단 주석처리
 */
@Repository
public class ListDAO {
	@Autowired
	SqlSession sqlSession;
	
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo) {
		log.debug("getTransInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!(CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type()) && 
				CommonUtil.SEARCH_TYPE_SCREEN.equals(vo.getSearch_type()))) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getTransInfo(vo);
	}
	
	@Transactional
	public ArrayList<TranslationVO> registerTransInfo(ArrayList<TranslationVO> transVO) {
		log.debug("registerTransInfo");
		log.debug("{}", transVO);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		
		for (TranslationVO vo : transVO) {
			if (vo.getSeq_id() != null) {
				log.debug("update process");
				mapper.modTransInfo(vo);
				seq_id_list.add(vo.getSeq_id());
			} else {
				log.debug("create process");
				mapper.addTransInfo(vo);
				seq_id_list.add(vo.getSeq_id());
			}
		}
		
		TranslationVO searchVO = new TranslationVO();
		searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
		searchVO.setSeq_id_list(seq_id_list);
		
		return mapper.getTransInfo(searchVO);
	}
	
	public int delTransInfo(String seq_id) {
		log.debug("delTransInfo");
		log.debug("del trans seq_id : " + seq_id);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.delTransInfo(seq_id);
	}
	
	public int modTransResult(TranslationResultVO vo) {
		log.debug("updateTransResult");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modTransResult(vo);
	}
	
	public void modRakutenInfo(ArrayList<RakutenSearchVO> list) {
		log.debug("modRakutenInfo");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for (RakutenSearchVO vo : list) {
			mapper.modRakutenInfo(vo);
		}
	}
	
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.debug("showRegionMaster");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRegionMaster(vo);
	}
	
	public ArrayList<RegionMasterVO> modRegionMaster(ArrayList<RegionMasterVO> list) {
		log.debug("modRegionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		for (RegionMasterVO rm : list) {
			log.debug("update target : " + rm);
			mapper.modRegionMaster(rm);
		}
		return mapper.getRegionMaster(new RegionMasterVO());
	}
	
	public ArrayList<ExceptionMasterVO> getExceptionMaster(ExceptionMasterVO vo) {
		log.debug("getExceptionMaster");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getExceptionMaster(vo);
	}
	
	public ArrayList<ExceptionMasterVO> registerExceptionMaster(ArrayList<ExceptionMasterVO> list) {
		log.debug("registerExceptionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (ExceptionMasterVO vo : list) {
			if (vo.getSeq_id() != null) {
				mapper.updateExceptionMaster(vo);
				seq_id_list.add(vo.getSeq_id());
				log.debug(String.format("updated seq_id: %s", vo.getSeq_id()));
			} else {
				mapper.insertExceptionMaster(vo);
				seq_id_list.add(vo.getSeq_id());
				log.debug(String.format("inserted seq_id: %s", vo.getSeq_id()));
			}
		}
		ExceptionMasterVO exVO = new  ExceptionMasterVO();
		exVO.setSeq_id_list(seq_id_list);
		
		return mapper.getExceptionMaster(exVO);
	}
	
	public ArrayList<ExceptionMasterVO> deleteExceptionMaster(ArrayList<ExceptionMasterVO> list) {
		log.debug("deleteExceptionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		for (ExceptionMasterVO vo : list) {
			mapper.deleteExceptionMaster(vo.getSeq_id());
		}
		
		return mapper.getExceptionMaster(null);
	}
	
	public void processSagawaUpdate2006(MultipartFile file, String fileEncoding, String type) throws IOException {
		log.debug("processSagawaUpdate2006");
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + file.getContentType());
		log.debug("name: " + file.getName());
		log.debug("original name: " + file.getOriginalFilename());
		log.debug("size: " + file.getSize());
		log.debug("type: " + type);
		
		IAmazonMapper amaMapper = sqlSession.getMapper(IAmazonMapper.class);
		IRakutenMapper rakMapper = sqlSession.getMapper(IRakutenMapper.class);
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), fileEncoding));
			
			CsvToBean<SagawaUpdateVO> csvToBean = new CsvToBeanBuilder<SagawaUpdateVO>(reader)
                    .withType(SagawaUpdateVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<SagawaUpdateVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	SagawaUpdateVO vo = iterator.next();
            	
            	if("ama".equals(type)) {
        			AmazonVO searchVO = new AmazonVO();
        			searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
                	searchVO.setOrder_id(vo.getRef_no());
                	ArrayList<AmazonVO> srchLst = amaMapper.getAmazonInfo(searchVO);
                	if(srchLst.size() > 0) {
                		if(null == srchLst.get(0).getBaggage_claim_no()) {
                			AmazonVO upVO = new AmazonVO();
                			upVO.setSeq_id(srchLst.get(0).getSeq_id());
                			upVO.setBaggage_claim_no(vo.getHawb_no());
                			upVO.setDelivery_company("1002");
                			amaMapper.updateAmazonInfo(upVO);
                		}
                	}
        		}else if("rak".equals(type)) {
        			RakutenVO searchVO = new RakutenVO();
                	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
                	searchVO.setOrder_no(vo.getRef_no());
        			ArrayList<RakutenVO> srchLst = rakMapper.getRakutenInfo(searchVO);
        			if(srchLst.size() > 0) {
                		if(null == srchLst.get(0).getBaggage_claim_no()) {
                			RakutenVO upVO = new RakutenVO();
                			upVO.setSeq_id(srchLst.get(0).getSeq_id());
                			upVO.setBaggage_claim_no(vo.getHawb_no());
                			upVO.setDelivery_company("1002");
                			rakMapper.updateRakutenInfo(upVO);
                		}
                	}
        		}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public ArrayList<ExceptionRegionMasterVO> getExceptionRegionMaster(ExceptionRegionMasterVO vo) {
		log.debug("getExceptionMaster");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getExceptionRegionMaster(vo);
	}
	
	public ArrayList<ExceptionRegionMasterVO> manipulateExceptionRegionMaster(ArrayList<ExceptionRegionMasterVO> list) {
		log.debug("registerExceptionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		for (ExceptionRegionMasterVO vo : list) {
			if (vo.getSeq_id() != null) {
				mapper.updateExceptionRegionMaster(vo);
				seq_id_list.add(vo.getSeq_id());
				log.debug(String.format("updated seq_id: %s", vo.getSeq_id()));
			} else {
				mapper.insertExceptionRegionMaster(vo);
				seq_id_list.add(vo.getSeq_id());
				log.debug(String.format("inserted seq_id: %s", vo.getSeq_id()));
			}
		}
		ExceptionRegionMasterVO exVO = new  ExceptionRegionMasterVO();
		exVO.setSeq_id_list(seq_id_list);
		
		return getExceptionRegionMaster(exVO);
	}
	
	public ArrayList<ExceptionRegionMasterVO> deleteExceptionRegionMaster(ArrayList<ExceptionRegionMasterVO> list) {
		log.debug("deleteExceptionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		for (ExceptionRegionMasterVO vo : list) {
			mapper.deleteExceptionRegionMaster(vo.getSeq_id());
		}
		
		return getExceptionRegionMaster(null);
	}
	
	public ArrayList<String> deleteAllWeekAfterData() {
		log.debug("deleteAllWeekAfterData");
		ArrayList<String> ret = new ArrayList<>();
		IRakutenMapper rMapper = sqlSession.getMapper(IRakutenMapper.class);
		IAmazonMapper aMapper = sqlSession.getMapper(IAmazonMapper.class);
		IQ10Mapper qMapper = sqlSession.getMapper(IQ10Mapper.class);
		IYahooMapper yMapper = sqlSession.getMapper(IYahooMapper.class);
		
		RakutenVO rVO = new RakutenVO();
		rVO.setSearch_type(CommonUtil.SEARCH_TYPE_WEEKDATA);
		ArrayList<RakutenVO> rList = rMapper.getRakutenInfo(rVO);
		int cnt = 0;
		for(RakutenVO vo : rList) {
			String seq_id = vo.getSeq_id();
			rMapper.deleteRakutenInfo(seq_id);
			cnt++;
		}
		ret.add("RAKUTEN: "+cnt+"件");
		cnt = 0;
		AmazonVO aVO = new AmazonVO();
		aVO.setSearch_type(CommonUtil.SEARCH_TYPE_WEEKDATA);
		ArrayList<AmazonVO> aList = aMapper.getAmazonInfo(aVO);
		for(AmazonVO vo : aList) {
			String seq_id = vo.getSeq_id();
			aMapper.deleteAmazonInfo(seq_id);
			cnt++;
		}
		ret.add("AMAZON: "+cnt+"件");
		cnt = 0;
		Q10VO qVO = new Q10VO();
		qVO.setSearch_type(CommonUtil.SEARCH_TYPE_WEEKDATA);
		ArrayList<Q10VO> qList = qMapper.getQ10Info(qVO);
		for(Q10VO vo : qList) {
			String seq_id = vo.getSeq_id();
			qMapper.deleteQ10Info(seq_id);
			cnt++;
					
		}
		ret.add("Q10: "+cnt+"件");
		cnt = 0;
		YahooVO yVO = new YahooVO();
		yVO.setSearch_type(CommonUtil.SEARCH_TYPE_WEEKDATA);
		ArrayList<YahooVO> yList = yMapper.getYahooInfo(yVO);
		for(YahooVO vo : yList) {
			String seq_id = vo.getSeq_id();
			yMapper.deleteYahooInfo(seq_id);
			cnt++;
		}
		ret.add("YAHOO: "+cnt+"件");
		
		log.debug("return: " + ret.toString());
		
		return ret;
	}
	
	public ArrayList<PrdCdMasterVO> getPrdCdMaster(PrdCdMasterVO vo) {
		log.debug("getPrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getPrdCdMaster(vo);
	}
	
	public ArrayList<PrdCdMasterVO> manipulatePrdCdMaster(ArrayList<PrdCdMasterVO> list) {
		log.debug("manipulatePrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String gbn = "";
		PrdCdMasterVO srchVO = new PrdCdMasterVO();
		for(PrdCdMasterVO vo : list) {
			gbn = vo.getTarget_type();
			srchVO.setPrd_cd(vo.getPrd_cd());
			ArrayList<PrdCdMasterVO> srchRet = mapper.prdCdMasterExistChk(srchVO);
			if(vo.getSeq_id() != null) {
				if(srchRet.size() > 0) {
					continue;
				}
				mapper.updatePrdCdMaster(vo);
				log.debug("updated!! "+vo.getSeq_id());
			}else {
				mapper.insertPrdCdMaster(vo);
				log.debug("inserted!! "+vo.getSeq_id());
			}
		}
		
		srchVO = new PrdCdMasterVO();
		srchVO.setTarget_type(gbn);
		return mapper.getPrdCdMaster(srchVO);
	}
	
	public ArrayList<PrdCdMasterVO> deletePrdCdMaster(ArrayList<PrdCdMasterVO> list) {
		log.debug("deletePrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String gbn = "";
		for(PrdCdMasterVO vo : list) {
			mapper.deletePrdCdMaster(vo.getSeq_id());
			gbn = vo.getTarget_type();
		}
		PrdCdMasterVO srchVO = new PrdCdMasterVO();
		srchVO.setTarget_type(gbn);
		return mapper.getPrdCdMaster(srchVO);
	}
	
	/**
	 * 商品中間マスタ
	 * */
	public ArrayList<PrdTransVO> getPrdTransInfo(PrdTransVO vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!(CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type()) && 
				CommonUtil.SEARCH_TYPE_SCREEN.equals(vo.getSearch_type()))) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		return mapper.getPrdTrans(vo);
	}
	
	public ArrayList<PrdTransVO> manipulatePrdTrans(ArrayList<PrdTransVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		String target = "";
		for(PrdTransVO vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updatePrdTrans(vo);
				target = vo.getTrans_target_type();
			}else {
				mapper.insertPrdTrans(vo);
				target = vo.getTrans_target_type();
			}
		}
		PrdTransVO vo = new PrdTransVO();
		vo.setTrans_target_type(target);
		return getPrdTransInfo(vo);
		
	}
	
	public ArrayList<PrdTransVO> deletePrdTrans(ArrayList<PrdTransVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String target = "";
		for(PrdTransVO vo : list) {
			target = vo.getTrans_target_type();
			mapper.deletePrdTrans(vo.getSeq_id());
		}
		PrdTransVO vo  = new PrdTransVO();
		vo.setTrans_target_type(target);
		return getPrdTransInfo(vo);
	}
	
	/**
	 * 総商品数
	 * */
	public ArrayList<OrderSumVO> getOrderSum(OrderSumVO vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		ArrayList<OrderSumVO> res = mapper.getOrderSum(vo);
		for(OrderSumVO sumVo : res) {
			// 商品情報
			JaikoPrdInfoVO prdSrch = new JaikoPrdInfoVO();
			prdSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			prdSrch.setJan_cd(sumVo.getJan_cd());
			JaikoPrdInfoVO prdInfo = null;
			try {
				prdInfo = prdMapper.getJaikoPrdInfo(prdSrch).get(0);
				if(null != prdInfo) {
					sumVo.setPrd_name(prdInfo.getPrd_nm());
					
					int prdCnt = Integer.parseInt(null != prdInfo.getPrd_cnt() ? prdInfo.getPrd_cnt() : "0");
					int total = sumVo.getPrd_sum();
					if(total > 0 && prdCnt > 0) {
						sumVo.setPkg_cnt(Math.floorDiv(total, prdCnt));
						sumVo.setBara_cnt(Math.floorMod(total, prdCnt));
					}
				}
			}catch (Exception e) {}
		}
		return res;
	}
	
	public ArrayList<OrderSumVO> deleteOrderSum(ArrayList<OrderSumVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String target = "";
		for(OrderSumVO vo : list) {
			target = vo.getTarget_type();
			mapper.deleteOrderSum(vo.getSeq_id());
		}
		OrderSumVO vo = new OrderSumVO();
		vo.setTarget_type(target);
		return getOrderSum(vo);
	}
	
	public ArrayList<OrderSumVO> executeOrderSum(String target_type, String sumType) {
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		IJaikoPrdInfoMapper jaikoPrdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		PrdTransVO vo1 = new PrdTransVO();
		vo1.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
		vo1.setTrans_target_type(target_type);
		ArrayList<PrdTransVO> list = listMapper.getPrdTrans(vo1);
		
		HashSet<String> afterTransCntnt = new HashSet<>();
		if(CommonUtil.SEARCH_TYPE_JAN_SUM.equals(sumType)) {
			for(PrdTransVO trans : list) {
				if(null != trans.getJan_cd() && !"".equals(trans.getJan_cd())) {
					afterTransCntnt.add(trans.getJan_cd());
				}
			}
			
			for(String jan_cd : afterTransCntnt) {
				vo1.setSearch_type(CommonUtil.SEARCH_TYPE_JAN_SUM);
				vo1.setJan_cd(jan_cd);
				list = listMapper.getPrdTrans(vo1);
				log.debug("sumType : " + target_type + "//"  + "Jan_cd : " + jan_cd);
				int sum = 0;
				for(PrdTransVO trans : list) {
					sum += Integer.parseInt(trans.getPrd_cnt());
				}
				JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
				prdVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				prdVO.setJan_cd(jan_cd);
				ArrayList<JaikoPrdInfoVO> prdInfoList = jaikoPrdMapper.getJaikoPrdInfo(prdVO);
				if(prdInfoList.size() > 0) {
					OrderSumVO sumVO = new OrderSumVO();
					sumVO.setPrd_sum(sum);
					sumVO.setAfter_trans(prdInfoList.get(0).getPrd_nm());
					sumVO.setTarget_type(target_type);
					sumVO.setJan_cd(jan_cd);
					listMapper.insertOrderSum(sumVO);
				}
			}
		}else {
			for(PrdTransVO trans : list) {
				// 277968-20230707-0616337403
				if(!"".equals(trans.getOrder_no()) && null == trans.getJan_cd()) {
					afterTransCntnt.add(trans.getAfter_trans());
				}
			}
			for(String str : afterTransCntnt) {
				vo1.setSearch_type(CommonUtil.SEARCH_TYPE_SUM);
				vo1.setAfter_trans(str);
				list = listMapper.getPrdTrans(vo1);
				int sum = 0;
				for(PrdTransVO trans : list) {
					sum += Integer.parseInt(trans.getPrd_cnt());
				}
				TranslationVO transVO = new TranslationVO();
				transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				transVO.setKeyword(str);
				ArrayList<TranslationVO> transRet = listMapper.getTransInfo(transVO);
				if(transRet.size() > 0) {
					OrderSumVO sumVO = new OrderSumVO();
					sumVO.setAfter_trans(str);
					sumVO.setPrd_sum(sum);
					sumVO.setTarget_type(target_type);
					listMapper.insertOrderSum(sumVO);
				}else {
					// 2021.06.11 order sum실행시 translation_info에 값이 없으면
					// translation_sub_info에서 search할수있게 처리
					SubTranslationVO subTrans = new SubTranslationVO();
					subTrans.setKeyword(str);
					ArrayList<SubTranslationVO> subTransRet = listMapper.getSubTransInfo(subTrans);
					if(subTransRet.size() > 0) {
						OrderSumVO sumVO = new OrderSumVO();
						sumVO.setAfter_trans(str);
						sumVO.setPrd_sum(sum);
						sumVO.setJan_cd(subTransRet.get(0).getJan_cd());
						sumVO.setTarget_type(target_type);
						listMapper.insertOrderSum(sumVO);
					}
				}
			}
			/*
			for(PrdTransVO trans : list) {
				afterTransCntnt.add(trans.getAfter_trans());
			}
			
			for(String str : afterTransCntnt) {
				vo1.setSearch_type(CommonUtil.SEARCH_TYPE_SUM);
				vo1.setAfter_trans(str);
				list = listMapper.getPrdTrans(vo1);
				int sum = 0;
				for(PrdTransVO trans : list) {
					sum += Integer.parseInt(trans.getPrd_cnt());
				}
				TranslationVO transVO = new TranslationVO();
				transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				transVO.setKeyword(str);
				ArrayList<TranslationVO> transRet = listMapper.getTransInfo(transVO);
				if(transRet.size() > 0) {
					OrderSumVO sumVO = new OrderSumVO();
					sumVO.setAfter_trans(str);
					sumVO.setPrd_sum(sum);
					sumVO.setTarget_type(target_type);
					listMapper.insertOrderSum(sumVO);
				}else {
					// 2021.06.11 order sum실행시 translation_info에 값이 없으면
					// translation_sub_info에서 search할수있게 처리
					SubTranslationVO subTrans = new SubTranslationVO();
					subTrans.setKeyword(str);
					ArrayList<SubTranslationVO> subTransRet = listMapper.getSubTransInfo(subTrans);
					if(subTransRet.size() > 0) {
						OrderSumVO sumVO = new OrderSumVO();
						sumVO.setAfter_trans(str);
						sumVO.setPrd_sum(sum);
						sumVO.setJan_cd(subTransRet.get(0).getJan_cd());
						sumVO.setTarget_type(target_type);
						listMapper.insertOrderSum(sumVO);
					}
				}
				
			}
			*/
		}
		OrderSumVO sumVO = new OrderSumVO();
		sumVO.setTarget_type(target_type);
		return listMapper.getOrderSum(sumVO);
	}
	
	public HashMap<String, Object> executeHanei(String target_type) {
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		
		HashMap<String, Object> ret = new HashMap<>();
		OrderSumVO sumVO = new OrderSumVO();
		sumVO.setTarget_type(target_type);
		ArrayList<OrderSumVO> orderSumList = listMapper.getOrderSum(sumVO);
		if(orderSumList.size() < 1) {
			ret.put("retCd", "ERR");
			ret.put("retMsg", "総商品数がなし。");
			return ret;
		}
		for(OrderSumVO sum : orderSumList) {
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			invenVO.setJan_cd(sum.getJan_cd());
			ArrayList<JaikoPrdInventoryVO> invenList = invenMapper.getJaikoPrdInventory(invenVO);
			if(invenList.size() > 0) {
				for(JaikoPrdInventoryVO inven : invenList) {
					int nowCnt = Integer.parseInt(inven.getNow_prd_cnt());
					int minus = sum.getPrd_sum();
					
					JaikoPrdInventoryVO invenUp = new JaikoPrdInventoryVO();
					invenUp.setJan_cd(sum.getJan_cd());
					invenUp.setSearch_type("wareOut");
					invenUp.setNow_prd_cnt(String.valueOf(nowCnt-minus));
					invenMapper.updateJaikoPrdInventory(invenUp);
				}
				ret.put("retCd", "S");
			}else {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "JANコード：" + sum.getJan_cd() +"在庫管理を確認してください。");
				return ret;
			}
		}
		return ret;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sumDownload(
			HttpServletResponse response
			, String fileEncoding
			, String targetType
			, String gbn) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.debug("sumDownload");
		log.debug("encoding : " + fileEncoding);
		
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "["+targetType+"]ORDERSUM" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
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
			
			String[] header = CommonUtil.orderSumHeader();
			IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
			OrderSumVO vo = new OrderSumVO();
			vo.setTarget_type(targetType);
			ArrayList<OrderSumVO> list = listMapper.getOrderSum(vo);
			if("sum".equals(gbn)) {
				ArrayList<OrderSumOriginalVO> oriList = new ArrayList<>();
				for(OrderSumVO sumVo : list) {
					OrderSumOriginalVO oriVo = new OrderSumOriginalVO();
					oriVo.setJan_cd(sumVo.getJan_cd());
					oriVo.setAfter_trans(sumVo.getAfter_trans());
					oriVo.setPrd_sum(sumVo.getPrd_sum());
					oriVo.setPrd_master_hanei_gbn(sumVo.getPrd_master_hanei_gbn());
					oriList.add(oriVo);
				}
				StatefulBeanToCsv<OrderSumOriginalVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
			            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
			            .build();
				
				csvWriter.writeNext(header);
				
				beanToCSV.write(oriList);
				
			}else if("jan".equals(gbn)) {
				header = CommonUtil.orderSumHeader2();
				
				IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
				IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
				
				for(OrderSumVO sumVo : list) {
					// 商品情報
					JaikoPrdInfoVO prdSrch = new JaikoPrdInfoVO();
					prdSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					prdSrch.setJan_cd(sumVo.getJan_cd());
					JaikoPrdInfoVO prdInfo = null;
					try {
						prdInfo = prdMapper.getJaikoPrdInfo(prdSrch).get(0);
						if(null != prdInfo) {
							sumVo.setPrd_name(prdInfo.getPrd_nm());
							
							int prdCnt = Integer.parseInt(null != prdInfo.getPrd_cnt() ? prdInfo.getPrd_cnt() : "0");
							int total = sumVo.getPrd_sum();
							if(total > 0 && prdCnt > 0) {
								sumVo.setPkg_cnt(Math.floorDiv(total, prdCnt));
								sumVo.setBara_cnt(Math.floorMod(total, prdCnt));
							}
						}
						
						JaikoPrdInventoryVO invenVo = new JaikoPrdInventoryVO();
						invenVo.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
						invenVo.setJan_cd(sumVo.getJan_cd());
						ArrayList<JaikoPrdInventoryVO> invenList = invenMapper.getJaikoPrdInventory(invenVo);
						if(invenList.size() > 0) {
							JaikoPrdInventoryVO invenSrchRes = invenList.get(0);
							sumVo.setPartner_nm(invenSrchRes.getDealer_nm());
						}
						
					}catch (Exception e) {}
				}
				StatefulBeanToCsv<OrderSumVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
			            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
			            .build();
				
				csvWriter.writeNext(header);
				
				beanToCSV.write(list);
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
	
	/**
	 * その他マスタ
	 * */
	public ArrayList<EtcMasterVO> getEtc(EtcMasterVO vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getEtc(vo);
	}
	
	public ArrayList<EtcMasterVO> manipulateEtc(ArrayList<EtcMasterVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		String target = "";
		for(EtcMasterVO vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updateEtc(vo);
				target = vo.getTarget_type();
			}else {
				mapper.insertEtc(vo);
				target = vo.getTarget_type();
			}
		}
		EtcMasterVO vo = new EtcMasterVO();
		vo.setTarget_type(target);
		return getEtc(vo);
		
	}
	
	public ArrayList<EtcMasterVO> deleteEtc(ArrayList<EtcMasterVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String target = "";
		for(EtcMasterVO vo : list) {
			target = vo.getTarget_type();
			mapper.deleteEtc(vo.getSeq_id());
		}
		EtcMasterVO vo = new EtcMasterVO();
		vo.setTarget_type(target);
		return getEtc(vo);
	}
	
	/**
	 * 置換サーブ情報
	 * */
	public ArrayList<SubTranslationVO> getSubTransInfo(SubTranslationVO vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getSubTransInfo(vo);
	}
	
	public ArrayList<SubTranslationVO> manipulateSubTransInfo(ArrayList<SubTranslationVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String parent_seq = "";
		for(SubTranslationVO vo : list) {
			parent_seq = vo.getParent_seq_id();
			if(null != vo.getSeq_id()) {
				mapper.updateSubTransInfo(vo);
			}else {
				mapper.insertSubTransInfo(vo);
			}
		}
		SubTranslationVO vo = new SubTranslationVO();
		vo.setParent_seq_id(parent_seq);
		return getSubTransInfo(vo);
	}
	
	public ArrayList<SubTranslationVO> deleteSubTransInfo(ArrayList<SubTranslationVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String parent_seq = "";
		for(SubTranslationVO vo : list) {
			parent_seq = vo.getParent_seq_id();
			mapper.deleteSubTransInfo(vo.getSeq_id());
		}
		SubTranslationVO vo = new SubTranslationVO();
		vo.setParent_seq_id(parent_seq);
		return getSubTransInfo(vo);
	}
	
	/**
	 * 第三倉庫マスタ
	 * */
	public ArrayList<House3MasterVO> getHouse3Master(House3MasterVO vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getHouse3Master(vo);
	}
	
	public ArrayList<House3MasterVO> manipulateHouse3Master(ArrayList<House3MasterVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for(House3MasterVO vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updateHouse3Master(vo);
			}else {
				mapper.insertHouse3Master(vo);
			}
		}
		House3MasterVO vo = new House3MasterVO();
		return getHouse3Master(vo);
	}
	
	public ArrayList<House3MasterVO> deleteHouse3Master(ArrayList<House3MasterVO> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for(House3MasterVO vo : list) {
			mapper.deleteHouse3Master(vo.getSeq_id());
		}
		House3MasterVO vo = new House3MasterVO();
		return getHouse3Master(vo);
	}
	
	/**
	 * 置換データダウンロード
	 * */
	public void translationCsvDownload(
			HttpServletResponse response, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "trans_"+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.transHeader();
			
			TranslationVO srch = new TranslationVO();
			srch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			ArrayList<ArakuVO> downList = new ArrayList<ArakuVO>();
			downList.addAll(listMapper.getTransInfo(srch));
			CommonUtil.executeCSVDownload(csvWriter, writer, header, downList);
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * 置換データアップロード
	 * */
	public void translationCsvUpload(MultipartFile upload, String fileEncoding) throws IOException {
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(upload.getInputStream(), fileEncoding));
			
			CsvToBean<TranslationVO> csvToBean = new CsvToBeanBuilder<TranslationVO>(reader)
                    .withType(TranslationVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<TranslationVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	TranslationVO vo = iterator.next();
            	
            	TranslationVO srch = new TranslationVO();
            	srch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	srch.setKeyword(vo.getBefore_trans());
            	ArrayList<TranslationVO> srchRet = listMapper.getTransInfo(srch);
            	if(srchRet.size() < 1) {
            		listMapper.addTransInfo(vo);
            	}
            }
		}finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	/**
	 * 第三倉庫マスタ
	 * */
	public ArrayList<KeywordSearchInfo> getKwrdInfo(KeywordSearchInfo vo) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getKwrdInfo(vo);
	}
	
	public ArrayList<KeywordSearchInfo> manipulateKwrdInfo(ArrayList<KeywordSearchInfo> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for(KeywordSearchInfo vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updateKwrdInfo(vo);
			}else {
				mapper.insertKwrdInfo(vo);
			}
		}
		KeywordSearchInfo vo = new KeywordSearchInfo();
		return getKwrdInfo(vo);
	}
	
	public ArrayList<KeywordSearchInfo> deleteKwrdInfo(ArrayList<KeywordSearchInfo> list) {
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for(KeywordSearchInfo vo : list) {
			mapper.deleteKwrdInfo(vo.getSeq_id());
		}
		KeywordSearchInfo vo = new KeywordSearchInfo();
		return getKwrdInfo(vo);
	}
}
