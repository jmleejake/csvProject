package jp.prj.araku.list.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.RakutenSearchVO;

public interface IListMapper {
	public ArrayList<RakutenSearchVO> getRList(RakutenSearchVO vo);
}
