package jp.prj.araku.jaiko.sales.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.product.mapper.IEstimateMapper;
import jp.prj.araku.product.vo.EstimateVO;

@Repository
public class JaikoSalesDAO {
	@Autowired
	SqlSession sqlSession;
	
	public HashMap<String, String> createSalesData(ArrayList<JaikoOrderVO> list) {
		HashMap<String, String> ret = new HashMap<>();
		IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		IEstimateMapper estimateMapper = sqlSession.getMapper(IEstimateMapper.class);
		
		for(JaikoOrderVO orderVO : list) {
			String jan_cd = orderVO.getJan_cd();
			String partner_id = orderVO.getPartner_id();
			
			JaikoPrdInfoVO srchPrd = new JaikoPrdInfoVO();
			srchPrd.setJan_cd(jan_cd);
			srchPrd.setPartner_id(partner_id);
			JaikoPrdInfoVO prdInfo = prdMapper.getOnePrdInfo(srchPrd);
			if(null == prdInfo) {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "["+jan_cd+"] 商品情報がありません。");
				return ret;
			}
			prdInfo.getTax_rt(); // 세율
			
			EstimateVO srchEstimate = new EstimateVO();
			srchEstimate.setJan_cd(jan_cd);
			srchEstimate.setPartner_id(partner_id);
			ArrayList<EstimateVO> estiList = estimateMapper.selectStatus(srchEstimate);
			if(estiList.size() > 0) {
				EstimateVO estiInfo = estiList.get(0);
				estiInfo.getPrd_prc(); // 단가
			}else {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "["+jan_cd+"] 見積情報がありません。");
				return ret;
			}
		}
		
		return ret;
	}
}
