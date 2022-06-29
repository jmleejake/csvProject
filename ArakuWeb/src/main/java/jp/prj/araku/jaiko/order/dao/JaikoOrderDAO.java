package jp.prj.araku.jaiko.order.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.order.mapper.IJaikoOrderMapper;
import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.util.CommonUtil;

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
		if("afterMani".equals(vo.getSearch_type()) || null != vo.getPartner_id()) {
			String regDt = vo.getReg_dt().replaceAll("-", "");
			vo.setFrom_dt(regDt+"000000");
			vo.setTo_dt(regDt+"235959");
		}
		return mapper.getData(vo);
	}
	
	public ArrayList<JaikoOrderVO> manipulateJaikoOrder(ArrayList<JaikoOrderVO> list) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		String thisPartner=null;
		String regDt = null;
		for(JaikoOrderVO vo : list) {
			thisPartner = vo.getPartner_id();
			regDt = vo.getReg_dt();
			if(vo.getSeq_id() != null) {
				mapper.updateData(vo);
				
				if(null != vo.getPrd_cnt()) {
					JaikoPrdInventoryVO invenSrch = new JaikoPrdInventoryVO();
					invenSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					invenSrch.setJan_cd(vo.getJan_cd());
					JaikoPrdInventoryVO invenVO = invenMapper.getJaikoPrdInventory(invenSrch).get(0);
					int nowCnt = Integer.parseInt(invenVO.getNow_prd_cnt());
					int minus = Integer.parseInt(vo.getPrd_cnt());
					
					JaikoPrdInventoryVO invenUp = new JaikoPrdInventoryVO();
					invenUp.setSeq_id(invenVO.getSeq_id());
					invenUp.setNow_prd_cnt(String.valueOf((nowCnt-minus)));
					invenMapper.updateJaikoPrdInventory(invenUp);
				}
				
			}else {
				mapper.insertData(vo);
			}
		}
		JaikoOrderVO srch = new JaikoOrderVO();
		srch.setPartner_id(thisPartner);
		srch.setReg_dt(regDt);
		srch.setSearch_type("afterMani");
		return getData(srch);
	}
	
	public ArrayList<JaikoOrderVO> deleteJaikoOrder(ArrayList<JaikoOrderVO> list) {
		IJaikoOrderMapper mapper = sqlSession.getMapper(IJaikoOrderMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		String thisPartner=null;
		String regDt = null;
		for(JaikoOrderVO vo : list) {
			thisPartner = vo.getPartner_id();
			regDt = vo.getReg_dt();
			mapper.deleteData(vo.getSeq_id());
			
			if(null != vo.getPrd_cnt()) {
				JaikoPrdInventoryVO invenSrch = new JaikoPrdInventoryVO();
				invenSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				invenSrch.setJan_cd(vo.getJan_cd());
				JaikoPrdInventoryVO invenVO = invenMapper.getJaikoPrdInventory(invenSrch).get(0);
				int nowCnt = Integer.parseInt(invenVO.getNow_prd_cnt());
				int plus = Integer.parseInt(vo.getPrd_cnt());
				
				JaikoPrdInventoryVO invenUp = new JaikoPrdInventoryVO();
				invenUp.setSeq_id(invenVO.getSeq_id());
				invenUp.setNow_prd_cnt(String.valueOf((nowCnt+plus)));
				invenMapper.updateJaikoPrdInventory(invenUp);
			}
		}
		JaikoOrderVO srch = new JaikoOrderVO();
		srch.setPartner_id(thisPartner);
		srch.setReg_dt(regDt);
		srch.setSearch_type("afterMani");
		return getData(srch);
	}

}
