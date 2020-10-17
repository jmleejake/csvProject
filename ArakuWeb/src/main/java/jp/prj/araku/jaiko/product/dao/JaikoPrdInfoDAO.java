package jp.prj.araku.jaiko.product.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.jaiko.warehouse.mapper.IJaikoWareHouseMapper;
import jp.prj.araku.jaiko.warehouse.vo.JaikoWareHouseVO;

@Repository
public class JaikoPrdInfoDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		return mapper.getJaikoPrdInfo(vo);
	}
	
	public ArrayList<JaikoPrdInfoVO> manipulateJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		IJaikoWareHouseMapper wareHouseMapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		String search_type = "";
		String seq_id = "";
		for(JaikoPrdInfoVO vo : list) {
			int iPrdCnt = Integer.parseInt(null != vo.getPrd_cnt() ? vo.getPrd_cnt() : "0");
			//int iPrdUnitPrc = Integer.parseInt(null != vo.getPrd_unit_prc() ? vo.getPrd_unit_prc() : "0");
			//int iTaxIncld = Integer.parseInt(null != vo.getTax_incld() ? vo.getTax_incld() : "0");
			//int iTaxRt = Integer.parseInt(null != vo.getTax_rt() ? vo.getTax_rt() : "0");
			
			if(null != vo.getSearch_type() && "wareIn".equals(vo.getSearch_type())) {
				// 入庫
				search_type = vo.getSearch_type();
				seq_id = vo.getSeq_id();
				JaikoPrdInfoVO forSearch = new JaikoPrdInfoVO();
				forSearch.setSeq_id(vo.getSeq_id());
				forSearch.setSearch_type("srch");
				JaikoPrdInfoVO srchRet = mapper.getJaikoPrdInfo(forSearch).get(0);
				//iPrdUnitPrc = iPrdUnitPrc + Integer.parseInt(srchRet.getPrd_unit_prc());
				//iTaxIncld = iTaxIncld + Integer.parseInt(srchRet.getTax_incld());
				//iTaxRt = iTaxRt + Integer.parseInt(srchRet.getTax_rt());
				vo.setPrd_cnt(String.valueOf(Integer.parseInt(srchRet.getPrd_cnt()) + iPrdCnt));
				//vo.setPrd_unit_prc(String.valueOf(iPrdUnitPrc));
				//vo.setTax_incld(String.valueOf(iTaxIncld));
				//vo.setTax_rt(String.valueOf(iTaxRt));
				
				JaikoWareHouseVO wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoWareHouseVO> wareRet = wareHouseMapper.getJaikoWareHouse(wareHouseVO);
				if(wareRet.size() > 0) {
					wareHouseVO.setSearch_type(vo.getSearch_type());
					wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
					JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
					invenVO.setJan_cd(vo.getJan_cd());
					invenVO.setSearch_type("srch");
					JaikoPrdInventoryVO invenRet = invenMapper.getJaikoPrdInventory(invenVO).get(0);
					invenVO.setSearch_type(vo.getSearch_type());
					invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) + iPrdCnt));
					invenMapper.updateJaikoPrdInventory(invenVO);
				}else {
					wareHouseMapper.insertJaikoWareHouse(wareHouseVO);
				}
			}else if(null != vo.getSearch_type() && "wareOut".equals(vo.getSearch_type())) {
				// 出庫
				search_type = vo.getSearch_type();
				seq_id = vo.getSeq_id();
				JaikoPrdInfoVO forSearch = new JaikoPrdInfoVO();
				forSearch.setSeq_id(vo.getSeq_id());
				forSearch.setSearch_type("srch");
				JaikoPrdInfoVO srchRet = mapper.getJaikoPrdInfo(forSearch).get(0);
				//iPrdUnitPrc = iPrdUnitPrc - Integer.parseInt(srchRet.getPrd_unit_prc());
				//iTaxIncld = iTaxIncld - Integer.parseInt(srchRet.getTax_incld());
				//iTaxRt = iTaxRt - Integer.parseInt(srchRet.getTax_rt());
				vo.setPrd_cnt(String.valueOf(Integer.parseInt(srchRet.getPrd_cnt()) - iPrdCnt));
				//vo.setPrd_unit_prc(String.valueOf(iPrdUnitPrc));
				//vo.setTax_incld(String.valueOf(iTaxIncld));
				//vo.setTax_rt(String.valueOf(iTaxRt));
				
				JaikoWareHouseVO wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoWareHouseVO> wareRet = wareHouseMapper.getJaikoWareHouse(wareHouseVO);
				if(wareRet.size() > 0) {
					wareHouseVO.setSearch_type(vo.getSearch_type());
					wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
					JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
					invenVO.setJan_cd(vo.getJan_cd());
					invenVO.setSearch_type("srch");
					JaikoPrdInventoryVO invenRet = invenMapper.getJaikoPrdInventory(invenVO).get(0);
					invenVO.setSearch_type(vo.getSearch_type());
					invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) - iPrdCnt));
					invenMapper.updateJaikoPrdInventory(invenVO);
				}else {
					wareHouseMapper.insertJaikoWareHouse(wareHouseVO);
				}
			}
			if(vo.getSeq_id() != null) {
				mapper.updateJaikoPrdInfo(vo);
			}else {
				mapper.insertJaikoPrdInfo(vo);
			}
		}
		JaikoPrdInfoVO forSearch = new JaikoPrdInfoVO();
		if("wareIn".equals(search_type) || "wareOut".equals(search_type)) {
			forSearch.setSeq_id(seq_id);
			forSearch.setSearch_type("srch");
		}
		return mapper.getJaikoPrdInfo(forSearch);
	}
	
	public ArrayList<JaikoPrdInfoVO> deleteJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		for(JaikoPrdInfoVO vo : list) {
			mapper.deleteJaikoPrdInfo(vo.getSeq_id());
		}
		return mapper.getJaikoPrdInfo(new JaikoPrdInfoVO());
	}

}
