package jp.prj.araku.product.mapper;

import java.util.ArrayList;

import jp.prj.araku.product.vo.EstimateVO;

public interface IEstimateMapper {
	ArrayList<EstimateVO> selectStatus(EstimateVO vo);
	int insertStatus(EstimateVO vo);
	int updateStatus(EstimateVO vo);
	int deleteStatus(String id);
}
