package jp.prj.araku.jaiko.product.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;

public interface IJaikoPrdInfoMapper {
	ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo);
	int insertJaikoPrdInfo(JaikoPrdInfoVO vo);
	int updateJaikoPrdInfo(JaikoPrdInfoVO vo);
	int deleteJaikoPrdInfo(String seq_id);
	JaikoPrdInfoVO getOnePrdInfo(JaikoPrdInfoVO vo);
}
