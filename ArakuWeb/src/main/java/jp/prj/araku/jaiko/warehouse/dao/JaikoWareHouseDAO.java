package jp.prj.araku.jaiko.warehouse.dao;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
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
		
		JSONObject janCdObj = new JSONObject();
 		HashSet<String> janCdList = new HashSet<>();
		for(JaikoWareHouseVO target : list) {
			JSONObject obj = new JSONObject();
			obj.put("qty", 0);
			obj.put("case", 0);
			obj.put("bara", 0);
			if(janCdObj.has(target.getJan_cd())) {
				JSONObject janObj = janCdObj.getJSONObject(target.getJan_cd());
				if(janObj.has("qty")) {
					obj.put("qty", janObj.getInt("qty"));
				}
				if(janObj.has("case")) {
					obj.put("case", janObj.getInt("case"));
				}
				if(janObj.has("bara")) {
					obj.put("bara", janObj.getInt("bara"));
				}
			}
			if(null != target.getPrd_qty()) {
				obj.put("qty", Integer.parseInt(target.getPrd_qty()));
			}
			if(null != target.getPrd_case()) {
				obj.put("case", Integer.parseInt(target.getPrd_case()));
			}
			if(null != target.getPrd_bara()) {
				obj.put("bara", Integer.parseInt(target.getPrd_bara()));
			}
			obj.put("prdNm", target.getPrd_nm());
			obj.put("brandNm", target.getBrand_nm());
			obj.put("partId", target.getPartner_id());
			obj.put("partNm", target.getPartner_nm());
			obj.put("type", target.getSearch_type());
			obj.put("expDt", target.getExp_dt());
			obj.put("sellPrc", target.getSell_prc());
			janCdObj.put(target.getJan_cd(), obj);
			janCdList.add(target.getJan_cd());
		}
		
		for(String janCd : janCdList) {
			JSONObject janObj = janCdObj.getJSONObject(janCd);
			int iQty = janObj.getInt("qty");
			int iCase = janObj.getInt("case");
			int iBara = janObj.getInt("bara");
			String type = janObj.getString("type");
			
			JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
			prdVO.setSearch_type(type);
			prdVO.setJan_cd(janCd);
			prdVO.setBrand_nm(janObj.getString("brandNm"));
			prdVO.setPrd_nm(janObj.getString("prdNm"));
			prdVO.setPartner_id(janObj.getString("partId"));
			prdVO.setPartner_nm(janObj.getString("partNm"));
			prdMapper.updateJaikoPrdInfo(prdVO);
			
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setJan_cd(janCd);
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			ArrayList<JaikoPrdInventoryVO> invenRetList = invenMapper.getJaikoPrdInventory(invenVO);
			
			JaikoWareHouseVO wareHouseVO = new JaikoWareHouseVO();
			wareHouseVO.setJan_cd(janCd);
			wareHouseVO.setSearch_type("insertSrch");
			ArrayList<JaikoWareHouseVO> wareRet = wareHouseMapper.getJaikoWareHouse(wareHouseVO);
			if(wareRet.size() < 1) {
				wareHouseVO.setSearch_type(type);
				wareHouseMapper.insertJaikoWareHouse(wareHouseVO);
			}
			
			if(null != type && "wareIn".equals(type)) {
				// 入庫
				wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(janCd);
				wareHouseVO.setSearch_type(type);
				wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
				JaikoPrdInventoryVO invenRet = invenRetList.get(0);
				invenVO.setSearch_type(type);
				invenVO.setBrand_nm(janObj.getString("brandNm"));
				invenVO.setPrd_nm(janObj.getString("prdNm"));
				invenVO.setPrd_qty("0");
				invenVO.setPrd_case("0");
				invenVO.setPrd_bara("0");
				invenVO.setExp_dt(janObj.getString("expDt"));
				invenVO.setSell_prc(janObj.getString("sellPrc"));
				invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) + (iQty*iCase+iBara)));
				invenMapper.updateJaikoPrdInventory(invenVO);
			}else if(null != type && "wareOut".equals(type)) {
				// 出庫
				wareHouseVO = new JaikoWareHouseVO();
				wareHouseVO.setJan_cd(janCd);
				wareHouseVO.setSearch_type(type);
				wareHouseMapper.updateJaikoWareHouse(wareHouseVO);
				JaikoPrdInventoryVO invenRet = invenRetList.get(0);
				invenVO.setSearch_type(type);
				invenVO.setBrand_nm(janObj.getString("brandNm"));
				invenVO.setPrd_nm(janObj.getString("prdNm"));
				invenVO.setPrd_qty("0");
				invenVO.setPrd_case("0");
				invenVO.setPrd_bara("0");
				invenVO.setExp_dt(janObj.getString("expDt"));
				invenVO.setSell_prc(janObj.getString("sellPrc"));
				invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) - (iQty*iCase+iBara)));
				invenMapper.updateJaikoPrdInventory(invenVO);
			}
		}
		JaikoWareHouseVO retVO = new JaikoWareHouseVO();
		retVO.setSearch_type("afterCommit");
		return getJaikoWareHouse(retVO);
	}

}
