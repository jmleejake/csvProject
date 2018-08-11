package jp.prj.araku.list.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.TranslationVO;

public interface IListMapper {
	public ArrayList<RakutenSearchVO> getRList(RakutenSearchVO vo);
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo);
	public int addTransInfo(TranslationVO vo);
	public int modTransInfo(TranslationVO vo);
	public int delTransInfo(String seq_id);
	public int modRakutenInfo(RakutenSearchVO vo);
}
