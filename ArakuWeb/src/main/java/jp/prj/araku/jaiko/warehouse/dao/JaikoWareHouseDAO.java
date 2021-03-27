package jp.prj.araku.jaiko.warehouse.dao;

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
import jp.prj.araku.util.CommonUtil;

@Repository
public class JaikoWareHouseDAO {
	@Autowired
	SqlSession sqlSession;
	
	public int insertJaikoWareHouse(JaikoWareHouseVO vo) {
		IJaikoWareHouseMapper mapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		return mapper.insertJaikoWareHouse(vo);
	}
	
	public int updateJaikoWareHouse(JaikoWareHouseVO vo) {
		IJaikoWareHouseMapper mapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		return mapper.updateJaikoWareHouse(vo);
	}
	
	public ArrayList<JaikoWareHouseVO> getJaikoWareHouse(JaikoWareHouseVO vo) {
		IJaikoWareHouseMapper mapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		if(null != vo.getDelivery_dt()) {
			String delivery_dt = vo.getDelivery_dt().replaceAll("/", "");
			
			vo.setFrom_dt(delivery_dt+"000000");
			vo.setTo_dt(delivery_dt+"235959");
		}
		return mapper.getJaikoWareHouse(vo);
	}
	
	public ArrayList<JaikoWareHouseVO> manipulateJaikoWareHouse(ArrayList<JaikoWareHouseVO> list) {
		IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		IJaikoWareHouseMapper wareHouseMapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		
		JaikoWareHouseVO vo = new JaikoWareHouseVO();
		for(JaikoWareHouseVO target : list) {
			vo.setJan_cd(target.getJan_cd());
			vo.setSearch_type(target.getSearch_type());
			if(null != target.getBrand_nm()) {
				vo.setBrand_nm(target.getBrand_nm());
			}
			if(null != target.getPrd_nm()) {
				vo.setPrd_nm(target.getPrd_nm());
			}
			if(null != target.getPrd_qty()) {
				vo.setPrd_qty(target.getPrd_qty());
			}
			if(null != target.getPrd_case()) {
				vo.setPrd_case(target.getPrd_case());
			}
			if(null != target.getPrd_bara()) {
				vo.setPrd_bara(target.getPrd_bara());
			}
			if(null != target.getExp_dt()) {
				vo.setExp_dt(target.getExp_dt());
			}
			if(null != target.getSell_prc()) {
				vo.setSell_prc(target.getSell_prc());
			}
			if(!"".equals(target.getPartner_id())) {
				vo.setPartner_id(target.getPartner_id());
			}
			if(!"".equals(target.getPartner_nm())) {
				vo.setPartner_nm(target.getPartner_nm());
			}
			
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setJan_cd(vo.getJan_cd());
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			ArrayList<JaikoPrdInventoryVO> invenRetList = invenMapper.getJaikoPrdInventory(invenVO);
				
			int iPrdQty = Integer.parseInt(null != vo.getPrd_qty() ? vo.getPrd_qty() : invenRetList.size() > 0 ? invenRetList.get(0).getPrd_qty() : "0");
			int iPrdCase = Integer.parseInt(null != vo.getPrd_case() ? vo.getPrd_case() : "0");
			int iPrdBara = Integer.parseInt(null != vo.getPrd_bara() ? vo.getPrd_bara() : "0");
			if(null != vo.getSearch_type() && "wareIn".equals(vo.getSearch_type())) {
				// 入庫
				JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
				prdVO.setSearch_type(vo.getSearch_type());
				prdVO.setJan_cd(vo.getJan_cd());
				prdVO.setBrand_nm(vo.getBrand_nm());
				prdVO.setPrd_nm(vo.getPrd_nm());
				prdVO.setPartner_id(vo.getPartner_id());
				prdVO.setPartner_nm(vo.getPartner_nm());
				prdMapper.updateJaikoPrdInfo(prdVO);
				
				JaikoWareHouseVO wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoWareHouseVO> wareRet = wareHouseMapper.getJaikoWareHouse(wareHouseVO);
				if(wareRet.size() > 0) {
					wareHouseVO.setSearch_type(vo.getSearch_type());
					wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
					if(invenRetList.size() > 0) {
						JaikoPrdInventoryVO invenRet = invenRetList.get(0);
						invenVO.setSearch_type(vo.getSearch_type());
						invenVO.setBrand_nm(vo.getBrand_nm());
						invenVO.setPrd_nm(vo.getPrd_nm());
						invenVO.setPrd_qty(vo.getPrd_qty());
						invenVO.setPrd_case(vo.getPrd_case());
						invenVO.setPrd_bara(vo.getPrd_bara());
						invenVO.setExp_dt(vo.getExp_dt());
						invenVO.setSell_prc(vo.getSell_prc());
						invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) + (iPrdQty*iPrdCase+iPrdBara)));
						invenMapper.updateJaikoPrdInventory(invenVO);
					}else {
						invenMapper.insertJaikoPrdInventoryForWareIn(invenVO);
					}
				}else {
					wareHouseMapper.insertJaikoWareHouse(wareHouseVO);
				}
			}else if(null != vo.getSearch_type() && "wareOut".equals(vo.getSearch_type())) {
				// 出庫
				JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
				prdVO.setSearch_type(vo.getSearch_type());
				prdVO.setJan_cd(vo.getJan_cd());
				prdVO.setBrand_nm(vo.getBrand_nm());
				prdVO.setPrd_nm(vo.getPrd_nm());
				prdVO.setPartner_id(vo.getPartner_id());
				prdVO.setPartner_nm(vo.getPartner_nm());
				prdMapper.updateJaikoPrdInfo(prdVO);
				
				JaikoWareHouseVO wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoWareHouseVO> wareRet = wareHouseMapper.getJaikoWareHouse(wareHouseVO);
				if(wareRet.size() > 0) {
					wareHouseVO.setSearch_type(vo.getSearch_type());
					wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
					JaikoPrdInventoryVO invenRet = invenRetList.get(0);
					invenVO.setSearch_type(vo.getSearch_type());
					invenVO.setBrand_nm(vo.getBrand_nm());
					invenVO.setPrd_nm(vo.getPrd_nm());
					invenVO.setPrd_qty(vo.getPrd_qty());
					invenVO.setPrd_case(vo.getPrd_case());
					invenVO.setPrd_bara(vo.getPrd_bara());
					invenVO.setExp_dt(vo.getExp_dt());
					invenVO.setSell_prc(vo.getSell_prc());
					invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) - (iPrdQty*iPrdCase+iPrdBara)));
					invenMapper.updateJaikoPrdInventory(invenVO);
				}else {
					wareHouseMapper.insertJaikoWareHouse(wareHouseVO);
				}
			}
		}
		JaikoWareHouseVO retVO = new JaikoWareHouseVO();
		retVO.setSearch_type("afterCommit");
		return getJaikoWareHouse(retVO);
	}

}
