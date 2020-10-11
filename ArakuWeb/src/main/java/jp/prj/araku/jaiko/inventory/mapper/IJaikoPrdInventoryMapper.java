package jp.prj.araku.jaiko.inventory.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;

public interface IJaikoPrdInventoryMapper {
	ArrayList<JaikoPrdInventoryVO> getJaikoPrdInventory(JaikoPrdInventoryVO vo);
	int insertJaikoPrdInventory(JaikoPrdInventoryVO vo);
	int updateJaikoPrdInventory(JaikoPrdInventoryVO vo);
	int deleteJaikoPrdInventory(String seq_id);
}
