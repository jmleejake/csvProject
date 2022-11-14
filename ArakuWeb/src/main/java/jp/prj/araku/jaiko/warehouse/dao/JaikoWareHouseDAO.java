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
import jp.prj.araku.jaiko.warehouse.vo.JaikoWareTempVO;
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
		String dt = "";
		if(null != vo.getDelivery_dt()) {
			dt = vo.getDelivery_dt().replaceAll("/", "");
		}else if(null != vo.getWarehouse_dt()) {
			dt = vo.getWarehouse_dt().replaceAll("/", "");
		}
		if(!"".equals(dt)) {
			vo.setFrom_dt(dt+"000000");
			vo.setTo_dt(dt+"235959");
		}
		return mapper.getJaikoWareHouse(vo);
	}
	
	public ArrayList<JaikoWareHouseVO> manipulateJaikoWareHouse(ArrayList<JaikoWareHouseVO> list) {
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
			
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setJan_cd(janCd);
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			ArrayList<JaikoPrdInventoryVO> invenRetList = invenMapper.getJaikoPrdInventory(invenVO);
			if(invenRetList.size() < 1) {
				invenMapper.insertJaikoPrdInventoryForWareIn(invenVO);
			}
			
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
				JaikoPrdInventoryVO invenRet = new JaikoPrdInventoryVO();
				if(invenRetList.size() > 0) {
					invenRet = invenRetList.get(0);
				}else {
					invenRet.setNow_prd_cnt("0");
				}
				invenVO.setSearch_type(type);
				invenVO.setBrand_nm(janObj.getString("brandNm"));
				invenVO.setPrd_nm(janObj.getString("prdNm"));
				invenVO.setPrd_qty("0");
				invenVO.setPrd_case("0");
				invenVO.setPrd_bara("0");
				//invenVO.setExp_dt(janObj.getString("expDt"));
				//invenVO.setSell_prc(janObj.getString("sellPrc"));
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
				//invenVO.setExp_dt(janObj.getString("expDt"));
				//invenVO.setSell_prc(janObj.getString("sellPrc"));
				invenVO.setNow_prd_cnt(String.valueOf(Integer.parseInt(invenRet.getNow_prd_cnt()) - (iQty*iCase+iBara)));
				invenMapper.updateJaikoPrdInventory(invenVO);
			}
		}
		JaikoWareHouseVO retVO = new JaikoWareHouseVO();
		retVO.setSearch_type("afterCommit");
		return getJaikoWareHouse(retVO);
	}
	
	public int manipulateJaikoWareTemp(ArrayList<JaikoWareTempVO> list) {
		IJaikoWareHouseMapper mapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		int ret = 0;
		for(JaikoWareTempVO vo : list) {
			JaikoWareTempVO data = mapper.getWareTemp(vo.getJan_cd()).size() > 0  ? mapper.getWareTemp(vo.getJan_cd()).get(0) : null;
			if(null != data) {
				ret = mapper.updateWareTemp(vo);
			}else {
				ret = mapper.insertWareTemp(vo);
			}
		}
		return ret;
	}
	
	public int processJaikoWarehouse(String type) {
		IJaikoWareHouseMapper wareHouseMapper = sqlSession.getMapper(IJaikoWareHouseMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		
		int ret = 0;
		
		ArrayList<JaikoWareTempVO> tempList = wareHouseMapper.getWareTemp("");
		for(JaikoWareTempVO temp : tempList) {
			// 商品情報
			JaikoPrdInfoVO prdSrch = new JaikoPrdInfoVO();
			prdSrch.setSearch_type(type);
			prdSrch.setJan_cd(temp.getJan_cd());
			JaikoPrdInfoVO prdInfo = null;
			try {
				prdInfo = prdMapper.getJaikoPrdInfo(prdSrch).get(0);
			}catch (Exception e) {
				return 99;
			}
			
			// 在庫情報
			JaikoPrdInventoryVO invenSrch = new JaikoPrdInventoryVO();
			JaikoPrdInventoryVO inven = null;
			try {
				invenSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				invenSrch.setJan_cd(temp.getJan_cd());
				inven = invenMapper.getJaikoPrdInventory(invenSrch).get(0);
			} catch (Exception e) {
				return 98;
			}
			
			int quantity = Integer.parseInt(temp.getPrd_quantity());
			int unit = Integer.parseInt(temp.getPrd_unit());
			int nowCnt = Integer.parseInt(inven.getNow_prd_cnt());
			int unitCnt = Integer.parseInt(prdInfo.getPrd_cnt1());
			switch (unit) {
			case 2:
				// 中品
				unitCnt = Integer.parseInt(prdInfo.getPrd_cnt2());
				break;
				
			case 3:
				// 箱
				unitCnt = Integer.parseInt(prdInfo.getPrd_cnt3());
				break;
			}
			invenSrch.setSearch_type(type);
			if("wareIn".equals(type)) {
				invenSrch.setNow_prd_cnt(String.valueOf(nowCnt + quantity*unitCnt));
			}else if("wareOut".equals(type)) {
				invenSrch.setNow_prd_cnt(String.valueOf(nowCnt - quantity*unitCnt));
			}
			ret = invenMapper.updateJaikoPrdInventory(invenSrch);
			
			JaikoWareHouseVO houseSrch = new JaikoWareHouseVO();
			houseSrch.setSearch_type(type);
			ret = wareHouseMapper.insertWarehouseFromWareTemp(houseSrch);
		}
		ret = wareHouseMapper.deleteWareTemp();
		return ret;
	}

}
