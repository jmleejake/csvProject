package jp.prj.araku.tablet.mapper;

import java.util.ArrayList;

import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.StockVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;

public interface ITabletPrdMapper {
	public int insertPrdInfo(TabletPrdVO vo);
	public ArrayList<TabletPrdVO> getPrdInfo(TabletPrdVO vo);
	public int updatePrdInfo(TabletPrdVO vo);
	public int deletePrdInfo(String seq_id);
	
	public ArrayList<TabletPrdVO> getPrdManage(TabletPrdVO vo);
	
	public int insertDealerInfo(DealerVO vo);
	public ArrayList<DealerVO> getDealerInfo(DealerVO vo);
	public int updateDealerInfo(DealerVO vo);
	public int deleteDealerInfo(String seq_id);
	
	public ArrayList<StockVO> getStockManage(StockVO vo);
	public int insertStockManage(StockVO vo);
	public TabletPrdVO getPrdInfoForStock(String jan_cd);
	public int updateStockManage(StockVO vo);
}
