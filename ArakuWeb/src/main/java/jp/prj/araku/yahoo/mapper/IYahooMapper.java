package jp.prj.araku.yahoo.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.yahoo.vo.YahooVO;

public interface IYahooMapper {
	public int insertYahooInfo(YahooVO vo);
	public int updateYahooInfo(YahooVO vo);
	public ArrayList<YahooVO> getYahooInfo(YahooVO vo);
	public ArrayList<YahooVO> getTransResult(TranslationResultVO vo);
	public int deleteYahooInfo(String seq_id);
}
