package jp.prj.araku.list.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;

public interface IListMapper {
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo);
	public int addTransInfo(TranslationVO vo);
	public int modTransInfo(TranslationVO vo);
	public int delTransInfo(String seq_id);
	public int modRakutenInfo(RakutenSearchVO vo);
	public int addTransResult(TranslationResultVO vo);
	public ArrayList<TranslationResultVO> getTransResult(TranslationResultVO vo);
	public int modTransResult(TranslationResultVO vo);
	public ArrayList<RegionMasterVO> getRegionMaster(RegionMasterVO vo);
	public int modRegionMaster(RegionMasterVO vo);
	public int insertTranslationErr(TranslationErrorVO vo);
	public String getTranslationErr(TranslationErrorVO vo);
	public int deleteTranslationErr(TranslationErrorVO vo);
}
