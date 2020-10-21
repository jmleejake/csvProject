package jp.prj.araku.jaiko.partner.mapper;

import java.util.ArrayList;

import jp.prj.araku.jaiko.partner.vo.JaikoPartnerVO;

public interface IJaikoPartnerMapper {
	ArrayList<JaikoPartnerVO> getPartner(JaikoPartnerVO vo);
	int insertPartner(JaikoPartnerVO vo);
	int updatePartner(JaikoPartnerVO vo);
	int deletePartner(String seq_id);
}
