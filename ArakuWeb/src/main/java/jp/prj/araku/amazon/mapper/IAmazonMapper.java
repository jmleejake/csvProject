package jp.prj.araku.amazon.mapper;

import java.util.ArrayList;

import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.list.vo.TranslationResultVO;

public interface IAmazonMapper {
	public int insertAmazonInfo(AmazonVO vo);
	public ArrayList<AmazonVO> getAmazonInfo(AmazonVO vo);
	public ArrayList<AmazonVO> getTransResult(TranslationResultVO vo);
	public int deleteAmazonInfo(String seq_id);
	public int updateAmazonInfo(AmazonVO vo);
}
