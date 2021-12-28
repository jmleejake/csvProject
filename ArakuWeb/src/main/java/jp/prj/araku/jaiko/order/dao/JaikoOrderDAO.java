package jp.prj.araku.jaiko.order.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.order.mapper.IJaikoOrderMapper;
import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;

@Repository
public class JaikoOrderDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoOrderVO> getMonthlyData(JaikoOrderVO vo) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		if(null == vo.getReg_dt() || "".equals(vo.getReg_dt()) ) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			vo.setReg_dt(sdf.format(cal.getTime()));
		}
		return mapper.getMonthlyData(vo);
	}
	
	public ArrayList<JaikoOrderVO> getCalendar(JaikoOrderVO vo) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		if(null == vo.getReg_dt() || "".equals(vo.getReg_dt()) ) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			vo.setReg_dt(sdf.format(cal.getTime()));
		}
		return mapper.getCalendar(vo);
	}
	
	public ArrayList<JaikoOrderVO> getData(JaikoOrderVO vo) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		if(null == vo.getReg_dt()) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			vo.setFrom_dt(sdf.format(cal.getTime())+"01000000");
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			vo.setTo_dt(sdf.format(cal.getTime())+lastDay+"235959");
		}else {
			String regDt = vo.getReg_dt().replaceAll("-", "");
			vo.setFrom_dt(regDt+"000000");
			vo.setTo_dt(regDt+"235959");
		}
		
		return mapper.getData(vo);
	}
	
	public ArrayList<JaikoOrderVO> manipulateJaikoOrder(ArrayList<JaikoOrderVO> list) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		String thisPartner=null;
		for(JaikoOrderVO vo : list) {
			thisPartner = vo.getPartner_id();
			if(vo.getSeq_id() != null) {
				mapper.updateData(vo);
				
				// TODO 상품정보테이블의 현재상품수에 입력한 수량만큼 마이너스
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
			
			// TODO 상품정보테이블의 현재상품수에 수량만큼 플러스
		}
		JaikoOrderVO srch = new JaikoOrderVO();
		srch.setPartner_id(thisPartner);
		return getData(srch);
	}

}
