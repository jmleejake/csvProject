package jp.prj.araku.product.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.product.mapper.IEstimateMapper;
import jp.prj.araku.product.vo.EstimateVO;

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
}
