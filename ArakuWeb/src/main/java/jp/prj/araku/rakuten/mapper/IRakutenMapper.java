package jp.prj.araku.rakuten.mapper;

import java.util.ArrayList;

import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.rakuten.vo.RakutenVO;

public interface IRakutenMapper {
	public int insertRakutenInfo(RakutenVO vo);
	public int insertRakutenFrozenInfo(RakutenVO vo);
	public ArrayList<RakutenVO> getRakutenInfo(RakutenVO vo);
	public ArrayList<RakutenVO> getRakutenFrozenInfo(RakutenVO vo);
	public int deleteRakutenInfo(String seq_id);
	public int updateRakutenInfo(RakutenVO vo);
	public int updateRakutenFrozenInfo(RakutenVO vo);
	public int updateRakutenFrozenInfoForTranslate(RakutenVO vo);
	public ArrayList<RakutenVO> getTransResult(TranslationResultVO vo);
	public ArrayList<RCSVDownVO> getRCSVDownList(RCSVDownVO vo);
	public int updateRakutenRegistDate(String seq_id);
	public int deleteRakutenFrozenInfo();
}
