package jp.prj.araku.batch.mapper;

import java.util.ArrayList;

import jp.prj.araku.batch.vo.ItemOutputVO;

public interface IBatchItemsMapper {
	public int insertItemsInfo(ItemOutputVO vo);
	public ArrayList<ItemOutputVO> getItemsInfo(ItemOutputVO vo);
	public int updateItemsInfo(ItemOutputVO vo);
}
