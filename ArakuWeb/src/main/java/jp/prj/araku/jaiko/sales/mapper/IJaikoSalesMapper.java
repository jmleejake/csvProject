package jp.prj.araku.jaiko.sales.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.sales.vo.JaikoSalesVO;

public interface IJaikoSalesMapper {
	int insertData(JaikoSalesVO vo);
	ArrayList<JaikoSalesVO> selectData(JaikoSalesVO vo);
	int updateData(JaikoSalesVO vo);
	int deleteData(String seq_id);
}
