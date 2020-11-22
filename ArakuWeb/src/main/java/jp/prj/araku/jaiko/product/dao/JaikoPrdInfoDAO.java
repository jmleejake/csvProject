package jp.prj.araku.jaiko.product.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;

@Repository
public class JaikoPrdInfoDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		return mapper.getJaikoPrdInfo(vo);
	}
	
	public ArrayList<JaikoPrdInfoVO> manipulateJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		for(JaikoPrdInfoVO vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updateJaikoPrdInfo(vo);
			}else {
				mapper.insertJaikoPrdInfo(vo);
			}
		}
		return mapper.getJaikoPrdInfo(new JaikoPrdInfoVO());
	}
	
	public ArrayList<JaikoPrdInfoVO> deleteJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		for(JaikoPrdInfoVO vo : list) {
			mapper.deleteJaikoPrdInfo(vo.getSeq_id());
		}
		return mapper.getJaikoPrdInfo(new JaikoPrdInfoVO());
	}

}
