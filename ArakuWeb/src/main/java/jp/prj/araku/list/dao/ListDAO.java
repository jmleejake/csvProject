package jp.prj.araku.list.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import jp.prj.araku.amazon.mapper.IAmazonMapper;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.SagawaUpdateVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.q10.mapper.IQ10Mapper;
import jp.prj.araku.q10.vo.Q10VO;
import jp.prj.araku.rakuten.mapper.IRakutenMapper;
import jp.prj.araku.rakuten.vo.RakutenVO;
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
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo) {
		log.info("getTransInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!(CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type()) && 
				CommonUtil.SEARCH_TYPE_SCREEN.equals(vo.getSearch_type()))) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getTransInfo(vo);
	}
	
	@Transactional
	public ArrayList<TranslationVO> registerTransInfo(ArrayList<TranslationVO> transVO) {
		log.info("registerTransInfo");
		log.debug(transVO);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> seq_id_list = new ArrayList<>();
		
		for (TranslationVO vo : transVO) {
			if (vo.getSeq_id() != null) {
				log.info("update process");
				mapper.modTransInfo(vo);
				seq_id_list.add(vo.getSeq_id());
			} else {
				log.info("create process");
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
		log.info("delTransInfo");
		log.debug("del trans seq_id : " + seq_id);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.delTransInfo(seq_id);
	}
	
	public int modTransResult(TranslationResultVO vo) {
		log.info("updateTransResult");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modTransResult(vo);
	}
	
	public void modRakutenInfo(ArrayList<RakutenSearchVO> list) {
		log.info("modRakutenInfo");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for (RakutenSearchVO vo : list) {
			mapper.modRakutenInfo(vo);
		}
	}
	
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.info("showRegionMaster");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRegionMaster(vo);
	}
	
	public ArrayList<RegionMasterVO> modRegionMaster(ArrayList<RegionMasterVO> list) {
		log.info("modRegionMaster");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> idList = new ArrayList<>();
		for (RegionMasterVO rm : list) {
			log.debug("update target : " + rm);
			mapper.modRegionMaster(rm);
			idList.add(rm.getSeq_id());
		}
		
		RegionMasterVO vo = new RegionMasterVO();
		vo.setSeq_id_list(idList);
		
		return mapper.getRegionMaster(vo);
	}
	
	public ArrayList<ExceptionMasterVO> getExceptionMaster(ExceptionMasterVO vo) {
		log.info("getExceptionMaster");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getExceptionMaster(vo);
	}
	
	public ArrayList<ExceptionMasterVO> registerExceptionMaster(ArrayList<ExceptionMasterVO> list) {
		log.info("registerExceptionMaster");
		log.debug(list);
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
		log.info("deleteExceptionMaster");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		for (ExceptionMasterVO vo : list) {
			mapper.deleteExceptionMaster(vo.getSeq_id());
		}
		
		return mapper.getExceptionMaster(null);
	}
	
	public void processSagawaUpdate2006(MultipartFile file, String fileEncoding, String type) throws IOException {
		log.info("processSagawaUpdate2006");
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
		log.info("getExceptionMaster");
		log.debug(vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getExceptionRegionMaster(vo);
	}
	
	public ArrayList<ExceptionRegionMasterVO> manipulateExceptionRegionMaster(ArrayList<ExceptionRegionMasterVO> list) {
		log.info("registerExceptionMaster");
		log.debug(list);
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
		log.info("deleteExceptionMaster");
		log.debug(list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		for (ExceptionRegionMasterVO vo : list) {
			mapper.deleteExceptionRegionMaster(vo.getSeq_id());
		}
		
		return getExceptionRegionMaster(null);
	}
	
	public ArrayList<String> deleteAllWeekAfterData() {
		log.info("deleteAllWeekAfterData");
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
		
		log.info("return: " + ret.toString());
		
		return ret;
	}
	
	public ArrayList<PrdCdMasterVO> getPrdCdMaster(PrdCdMasterVO vo) {
		log.info("getPrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getPrdCdMaster(vo);
	}
	
	public ArrayList<PrdCdMasterVO> manipulatePrdCdMaster(ArrayList<PrdCdMasterVO> list) {
		log.info("manipulatePrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		String gbn = "";
		for(PrdCdMasterVO vo : list) {
			if (vo.getSeq_id() != null) {
				mapper.updatePrdCdMaster(vo);
				log.info("updated!! "+vo.getSeq_id());
			}else {
				mapper.insertPrdCdMaster(vo);
				log.info("inserted!! "+vo.getSeq_id());
			}
			gbn = vo.getTarget_type();
		}
		PrdCdMasterVO srchVO = new PrdCdMasterVO();
		srchVO.setTarget_type(gbn);
		return mapper.getPrdCdMaster(srchVO);
	}
	
	public int deletePrdCdMaster(String seq_id) {
		log.info("deletePrdCdMaster");
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.deletePrdCdMaster(seq_id);
	}
}
