package jp.prj.araku.jaiko.partner.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.partner.vo.JaikoPartnerVO;
import jp.prj.araku.jaiko.partner.vo.JaikoWareTantou;

public interface IJaikoPartnerMapper {
	ArrayList<JaikoPartnerVO> getPartner(JaikoPartnerVO vo);
	int insertPartner(JaikoPartnerVO vo);
	int updatePartner(JaikoPartnerVO vo);
	int deletePartner(String seq_id);
	
	ArrayList<JaikoWareTantou> getWareTantou(JaikoWareTantou vo);
	int insertWareTantou(JaikoWareTantou vo);
	int updateWareTantou(JaikoWareTantou vo);
	int deleteWareTantou(String seq_id);
}
