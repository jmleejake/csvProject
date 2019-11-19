package jp.prj.araku.tablet.mapper;

import java.util.ArrayList;

import jp.prj.araku.tablet.vo.TabletPrdVO;

public interface ITabletPrdMapper {
	public int insertPrdInfo(TabletPrdVO vo);
	public ArrayList<TabletPrdVO> getPrdInfo(TabletPrdVO vo);
	public int updatePrdInfo(TabletPrdVO vo);
	public int deletePrdInfo(String seq_id);
	public ArrayList<TabletPrdVO> getPrdManage(TabletPrdVO vo);
}
