package jp.prj.araku.jaiko.order.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.order.mapper.IJaikoOrderMapper;
import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;

@Repository
public class JaikoOrderDAO {
	@Autowired
	SqlSession sqlSession;
	
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	public ArrayList<JaikoOrderVO> getMonthlyData(JaikoOrderVO vo) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		if(null == vo.getReg_dt() || "".equals(vo.getReg_dt()) ) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			vo.setReg_dt(sdf.format(cal.getTime()));
		}
		return mapper.getMonthlyData(vo);
	}
	
	public ArrayList<JaikoOrderVO> getData(JaikoOrderVO vo) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, 1);
		vo.setTo_dt(sdf.format(cal.getTime()));
		cal.add(Calendar.DATE, -3);
		vo.setFrom_dt(sdf.format(cal.getTime()));
		return mapper.getData(vo);
	}
	
	public ArrayList<JaikoOrderVO> manipulateJaikoOrder(ArrayList<JaikoOrderVO> list) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		String thisPartner=null;
		for(JaikoOrderVO vo : list) {
			thisPartner = vo.getPartner_id();
			if(vo.getSeq_id() != null) {
				mapper.updateData(vo);
			}else {
				mapper.insertData(vo);
			}
		}
		JaikoOrderVO srch = new JaikoOrderVO();
		srch.setPartner_id(thisPartner);
		return getData(srch);
	}
	
	public ArrayList<JaikoOrderVO> deleteJaikoOrder(ArrayList<JaikoOrderVO> list) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		String thisPartner=null;
		for(JaikoOrderVO vo : list) {
			thisPartner = vo.getPartner_id();
			mapper.deleteData(vo.getSeq_id());
		}
		JaikoOrderVO srch = new JaikoOrderVO();
		srch.setPartner_id(thisPartner);
		return getData(srch);
	}

}
