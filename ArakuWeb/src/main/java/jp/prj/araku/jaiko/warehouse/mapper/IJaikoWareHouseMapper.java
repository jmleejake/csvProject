package jp.prj.araku.jaiko.warehouse.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.warehouse.vo.JaikoWareHouseVO;
import jp.prj.araku.jaiko.warehouse.vo.JaikoWareTempVO;

public interface IJaikoWareHouseMapper {
	int updateJaikoWareHouse(JaikoWareHouseVO vo);
	int insertJaikoWareHouse(JaikoWareHouseVO vo);
	ArrayList<JaikoWareHouseVO> getJaikoWareHouse(JaikoWareHouseVO vo);
	
	ArrayList<JaikoWareTempVO> getWareTemp(String jan_cd);
	int insertWareTemp(JaikoWareTempVO vo);
	int updateWareTemp(JaikoWareTempVO vo);
	int deleteWareTemp();
	int insertWarehouseFromWareTemp(JaikoWareHouseVO vo);
}
