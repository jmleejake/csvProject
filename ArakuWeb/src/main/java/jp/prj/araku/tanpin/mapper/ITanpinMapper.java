package jp.prj.araku.tanpin.mapper;

import java.util.ArrayList;

import jp.prj.araku.tanpin.vo.TanpinVO;

public interface ITanpinMapper {
	public int insertTanpinInfo(TanpinVO vo);
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo);
	public int updateTanpinInfo(TanpinVO vo);
	public int deleteTanpinInfo(String seq_id);
}
