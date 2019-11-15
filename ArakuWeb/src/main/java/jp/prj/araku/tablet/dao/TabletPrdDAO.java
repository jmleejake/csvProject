package jp.prj.araku.tablet.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.TabletPrdVO;

@Repository
public class TabletPrdDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<TabletPrdVO> getPrdInfo(TabletPrdVO vo) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		return mapper.getPrdInfo(vo);
	}
	
	public ArrayList<TabletPrdVO> manipulatePrdInfo(ArrayList<TabletPrdVO> list) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		
		for(TabletPrdVO vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updatePrdInfo(vo);
			}else {
				mapper.insertPrdInfo(vo);
			}
		}
		
		return mapper.getPrdInfo(null);
	}
	
	public ArrayList<TabletPrdVO> deletePrdInfo(ArrayList<TabletPrdVO> list) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		
		for(TabletPrdVO vo : list) {
			mapper.deletePrdInfo(vo.getSeq_id());
		}
		
		return mapper.getPrdInfo(null);
	}
}
