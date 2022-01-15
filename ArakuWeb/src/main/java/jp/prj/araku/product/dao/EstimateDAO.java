package jp.prj.araku.product.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.product.mapper.IEstimateMapper;
import jp.prj.araku.product.vo.EstimateVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class EstimateDAO {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<EstimateVO> selectStatus(EstimateVO vo) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		return mapper.selectStatus(vo);
	}
	
	public ArrayList<EstimateVO> manupulateStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		String partId="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			if(null != vo.getSeq_id() && !"".equals(vo.getSeq_id())) {
				mapper.updateStatus(vo);
			}else {
				mapper.insertStatus(vo);
			}
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
	
	public ArrayList<EstimateVO> deleteStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		String partId="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			mapper.deleteStatus(vo.getSeq_id());
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
	
	public ArrayList<EstimateVO> createStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		IJaikoPrdInfoMapper jaikoPrdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		String partId="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			double per = vo.getPercent();
			
			if(per == (double)0) {
				ArrayList<EstimateVO> srchStatus = mapper.selectStatus(vo);
				
				if(srchStatus.size() > 0) {
					vo.setSeq_id(srchStatus.get(0).getSeq_id());
					mapper.updateStatus(vo);
				}else {
					mapper.insertStatus(vo);
				}
			}else {
				JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
				prdVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				prdVO.setPartner_id(vo.getPartner_id());
				ArrayList<JaikoPrdInfoVO> prdList = jaikoPrdMapper.getJaikoPrdInfo(prdVO);
				
				double prd_prc = 0;
				for(JaikoPrdInfoVO prd : prdList) {
					vo = new EstimateVO();
					int prc = Integer.parseInt(prd.getPrd_unit_prc());
					prd_prc = prc*(per/(double)100);
					prd_prc = prc + prd_prc;
					
					vo.setPartner_id(prd.getPartner_id());
					vo.setPartner_nm(prd.getPartner_nm());
					vo.setJan_cd(prd.getJan_cd());
					
					ArrayList<EstimateVO> srchStatus = mapper.selectStatus(vo);
					
					if(srchStatus.size() > 0) {
						vo.setSeq_id(srchStatus.get(0).getSeq_id());
						vo.setPrd_nm(prd.getPrd_nm());
						vo.setPrd_prc(String.valueOf(prd_prc));
						mapper.updateStatus(vo);
					}else {
						vo.setPrd_nm(prd.getPrd_nm());
						vo.setPrd_prc(String.valueOf(prd_prc));
						mapper.insertStatus(vo);
					}
				}
			}
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
}
