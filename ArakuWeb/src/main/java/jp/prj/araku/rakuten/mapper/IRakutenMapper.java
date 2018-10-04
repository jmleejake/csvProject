package jp.prj.araku.rakuten.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.rakuten.vo.RakutenVO;

public interface IRakutenMapper {
	public int insertRakutenInfo(RakutenVO vo);
	public ArrayList<RakutenVO> getRakutenInfo(RakutenVO vo);
	public int deleteRakutenInfo(String seq_id);
	public int updateRakutenInfo(RakutenVO vo);
	public ArrayList<RakutenVO> getTransResult(TranslationResultVO vo);
}
