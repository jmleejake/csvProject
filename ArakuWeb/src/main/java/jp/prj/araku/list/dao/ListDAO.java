package jp.prj.araku.list.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.CommonUtil;

/**
 * @comment
 * [MOD-0819] 상품명에서 추출하는 개수는 계산하는데 필요없다
 * [MOD-0826] 콜론으로만 델리미터를 처리할 예정이므로 일단 주석처리
 */
@Repository
public class ListDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = LoggerFactory.getLogger(ListDAO.class);
	
	public ArrayList<RakutenSearchVO> getRList(RakutenSearchVO vo) {
		log.info("getRList");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRList(vo);
	}
	
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo) {
		log.info("getTransInfo");
		// 초기상태일때 2틀간의 데이터를 얻을수있게 처리 (*srch는 검색할때 넘기는 값)
		if (!CommonUtil.SEARCH_TYPE_SRCH.equals(vo.getSearch_type())) {
			vo.setStart_date(CommonUtil.getStartDate());
		}
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getTransInfo(vo);
	}
	
	@Transactional
	public ArrayList<TranslationVO> registerTransInfo(ArrayList<TranslationVO> transVO) {
		log.info("registerTransInfo");
		log.debug("{}", transVO);
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
		log.debug("del trans seq_id : {}", seq_id);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.delTransInfo(seq_id);
	}
	
	public void modRakutenInfo(ArrayList<RakutenSearchVO> list) {
		log.info("modRakutenInfo");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		for (RakutenSearchVO vo : list) {
			mapper.modRakutenInfo(vo);
		}
	}
	
	@Transactional
	public ArrayList<String> executeTranslate(ArrayList<RakutenSearchVO> targetList) {
		log.info("executeTranslate");
		log.debug("{}", targetList);
		
		ArrayList<String> ret = new ArrayList<>();
		
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		TranslationVO transVO = new TranslationVO();
		String transedName;
//		int productSetNo, unitNo;
		int unitNo;
		for (RakutenSearchVO vo : targetList) {
			
			transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			transVO.setKeyword(vo.getProduct_name());
			ArrayList<TranslationVO> searchRet = mapper.getTransInfo(transVO);
			
			// 치환후 상품명
			transedName = searchRet.get(0).getAfter_trans();
			// 상품세트수
			// [MOD-0819]
//			String[] arr = transedName.split(CommonUtil.SPLIT_BY_STAR);
//			productSetNo = Integer.parseInt(arr[1]);
			// 상품개수
			unitNo = Integer.parseInt(vo.getUnit_no());
			
			StringBuffer buf = null;
			String optionContent = vo.getProduct_option();
			if(optionContent != null && optionContent.length() > 1) {
				// 옵션에 대한 처리
				HashSet<String> cntCheck = new HashSet<>();
				HashMap<String, Integer> map = new HashMap<>();
				
				String[] strArr = vo.getProduct_option().split(CommonUtil.SPLIT_BY_NPER);
				ArrayList<String> list = new ArrayList<>();
				for (int i=0; i<strArr.length; i++) {
					log.debug(String.format("%d :: %s", i, strArr[i]));
					String[] data = strArr[i].split(CommonUtil.SPLIT_BY_COLON);
					String value = null;
					if (data.length > 1) {
						// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
						value = data[data.length-1];
						log.debug(String.format("option value1 :: %s", value));
						
						transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
						transVO.setKeyword(value);
						searchRet = mapper.getTransInfo(transVO);
						
						list.add(searchRet.get(0).getAfter_trans().trim());
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
				log.debug("option list {}", list);
				
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
				log.debug("option map : {}", map);
				
				Set<String> optionNames = map.keySet();
				buf = new StringBuffer(transedName);
				buf.append(" ");
				for (String optionName : optionNames) {
					// 옵션개수, 상품개수를 곱하여 치환결과에 반영
//					buf.append(optionName + "*" + (map.get(optionName)*productSetNo*unitNo));[MOD-0819]
					buf.append(optionName + "*" + (map.get(optionName)*unitNo));
					if (optionNames.size() > 1) {
						buf.append(";");
					}
				}
			} else {
				// 옵션이 없는 경우, 상품세트수와 상품개수를 곱하여 치환결과에 반영
//				buf = new StringBuffer(arr[0] + "*" + (productSetNo*unitNo));[MOD-0819]
				buf = new StringBuffer(transedName + "*" + unitNo);
			}
			
			String last = buf.toString();
			String finalStr = null;
			try {
				finalStr = last.substring(0, last.lastIndexOf(";"));
			} catch (StringIndexOutOfBoundsException e) {
				finalStr = last;
			}
			log.debug("final String : {}", finalStr);
			
			// 지역별 배송코드 세팅 (csv다운로드 기능)
			RakutenSearchVO rVO = new RakutenSearchVO();
			rVO.setSeq_id(vo.getSeq_id());
			ArrayList<RakutenSearchVO> regionAdd = mapper.getRList(rVO);
			rVO.setDelivery_address1(regionAdd.get(0).getDelivery_address1());
			
			RegionMasterVO rmVO = new RegionMasterVO();
			rmVO.setKeyword(regionAdd.get(0).getDelivery_address1());
			ArrayList<RegionMasterVO> regionM = mapper.getRegionMaster(rmVO);
			
			rVO.setDelivery_company(regionM.get(0).getDelivery_company());
			log.debug("Update Rakuten info : {}", rVO);
			mapper.modRakutenInfo(rVO);
			
			TranslationResultVO resultVO = new TranslationResultVO();
			resultVO.setTrans_target_id(vo.getSeq_id());
			resultVO.setTrans_target_type(CommonUtil.TRANS_TARGET_R);
			resultVO.setResult_text(finalStr);
			
			// 이미 치환된 결과가 있는 trans_target_id이면 update, 아니면 insert
			ArrayList<TranslationResultVO> transResult = mapper.getTransResult(resultVO);
			if (transResult.size() > 0) {
				mapper.modTransResult(resultVO);
				
				log.debug("seq_id? {}", transResult.get(0).getSeq_id());
				ret.add(transResult.get(0).getSeq_id());
			} else {
				mapper.addTransResult(resultVO);
				
				log.debug("seq_id? {}", resultVO.getSeq_id());
				ret.add(resultVO.getSeq_id());
			}
			
		}
		
		return ret;
	}
	
	public ArrayList<TranslationResultVO> getTransResult(String id_lst) {
		log.info("getTransResult");
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] strArr = id_lst.split(",");
		ArrayList<String> list = new ArrayList<>();
		for (String str : strArr) {
			list.add(str);
		}
		log.debug("query id list :: {}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		TranslationResultVO vo = new TranslationResultVO();
		vo.setSeq_id_list(list);
		
		return mapper.getTransResult(vo);
	}
	
	public int modTransResult(TranslationResultVO vo) {
		log.info("modTransResult");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.modTransResult(vo);
	}
	
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.info("showRegionMaster");
		log.debug("{}", vo);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		return mapper.getRegionMaster(vo);
	}
	
	public ArrayList<RegionMasterVO> modRegionMaster(ArrayList<RegionMasterVO> list) {
		log.info("modRegionMaster");
		log.debug("{}", list);
		IListMapper mapper = sqlSession.getMapper(IListMapper.class);
		
		ArrayList<String> idList = new ArrayList<>();
		for (RegionMasterVO rm : list) {
			log.debug("update target : {}", rm);
			mapper.modRegionMaster(rm);
			idList.add(rm.getSeq_id());
		}
		
		RegionMasterVO vo = new RegionMasterVO();
		vo.setSeq_id_list(idList);
		
		return mapper.getRegionMaster(vo);
	}
}
