package jp.prj.araku.jaiko.order.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;

public interface IJaikoOrderMapper {
	ArrayList<JaikoOrderVO> getMonthlyData(JaikoOrderVO vo);
	ArrayList<JaikoOrderVO> getData(JaikoOrderVO vo);
	int insertData(JaikoOrderVO vo);
	int updateData(JaikoOrderVO vo);
	int deleteData(String seq_id);
}
