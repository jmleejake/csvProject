package jp.prj.araku.jaiko.partner.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.partner.mapper.IJaikoPartnerMapper;
import jp.prj.araku.jaiko.partner.vo.JaikoPartnerVO;
import jp.prj.araku.jaiko.partner.vo.JaikoWareTantou;

@Repository
public class JaikoPartnerDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoPartnerVO> getPartner(JaikoPartnerVO vo) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		return mapper.getPartner(vo);
	}
	
	public ArrayList<JaikoPartnerVO> manipulatePartner(ArrayList<JaikoPartnerVO> list) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		for(JaikoPartnerVO vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updatePartner(vo);
			}else {
				mapper.insertPartner(vo);
			}
		}
		return mapper.getPartner(new JaikoPartnerVO());
	}
	
	public ArrayList<JaikoPartnerVO> deletePartner(ArrayList<JaikoPartnerVO> list) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		for(JaikoPartnerVO vo : list) {
			mapper.deletePartner(vo.getSeq_id());
		}
		return mapper.getPartner(new JaikoPartnerVO());
	}
	
	public ArrayList<JaikoWareTantou> getWareTantou(JaikoWareTantou vo) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		return mapper.getWareTantou(vo);
	}
	
	public ArrayList<JaikoWareTantou> manipulateWareTantou(ArrayList<JaikoWareTantou> list) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		for(JaikoWareTantou vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updateWareTantou(vo);
			}else {
				mapper.insertWareTantou(vo);
			}
		}
		return mapper.getWareTantou(new JaikoWareTantou());
	}
	
	public ArrayList<JaikoWareTantou> deleteWareTantou(ArrayList<JaikoWareTantou> list) {
		IJaikoPartnerMapper mapper = sqlSession.getMapper(IJaikoPartnerMapper.class);
		for(JaikoWareTantou vo : list) {
			mapper.deleteWareTantou(vo.getSeq_id());
		}
		return mapper.getWareTantou(new JaikoWareTantou());
	}

}
