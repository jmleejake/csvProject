package jp.prj.araku.q10.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.q10.vo.Q10VO;

public interface IQ10Mapper {
	public int insertQ10Info(Q10VO vo);
	public ArrayList<Q10VO> getQ10Info(Q10VO vo);
	public ArrayList<Q10VO> getTransResult(TranslationResultVO vo);
	public int updateQ10Info(Q10VO vo);
	public int deleteQ10Info(String seq_id);
}
