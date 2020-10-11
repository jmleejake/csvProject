package jp.prj.araku.jaiko.inventory.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;

@Repository
public class JaikoPrdInventoryDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoPrdInventoryVO> getJaikoPrdInventory(JaikoPrdInventoryVO vo) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		return mapper.getJaikoPrdInventory(vo);
	}
	
	public ArrayList<JaikoPrdInventoryVO> manipulatePrdInventory(ArrayList<JaikoPrdInventoryVO> list) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		for(JaikoPrdInventoryVO vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updateJaikoPrdInventory(vo);
			}else {
				mapper.insertJaikoPrdInventory(vo);
			}
		}
		return mapper.getJaikoPrdInventory(new JaikoPrdInventoryVO());
	}
	
	public ArrayList<JaikoPrdInventoryVO> deleteJaikoPrdInventory(ArrayList<JaikoPrdInventoryVO> list) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		for(JaikoPrdInventoryVO vo : list) {
			mapper.deleteJaikoPrdInventory(vo.getSeq_id());
		}
		return mapper.getJaikoPrdInventory(new JaikoPrdInventoryVO());
	}

}
