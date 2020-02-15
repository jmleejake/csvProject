package jp.prj.araku.product.mapper;

import java.util.ArrayList;

import jp.prj.araku.product.vo.ProductAnalysisVO;

public interface IProductAnalysisMapper {
	int insertPrdAnalysis(ProductAnalysisVO vo);
	ArrayList<ProductAnalysisVO> getPrdAnalysis(ProductAnalysisVO vo);
	int updatePrdAnalysis(ProductAnalysisVO vo);
	int deletePrdAnalysis(String seq_id);
}
