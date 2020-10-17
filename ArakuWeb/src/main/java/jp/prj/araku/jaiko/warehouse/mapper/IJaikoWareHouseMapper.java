package jp.prj.araku.jaiko.warehouse.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.warehouse.vo.JaikoWareHouseVO;

public interface IJaikoWareHouseMapper {
	int updateJaikoWareHouse(JaikoWareHouseVO vo);
	int insertJaikoWareHouse(JaikoWareHouseVO vo);
	ArrayList<JaikoWareHouseVO> getJaikoWareHouse(JaikoWareHouseVO vo);
}
