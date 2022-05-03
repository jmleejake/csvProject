package jp.prj.araku.jaiko.sales.dao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.order.vo.JaikoOrderVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.jaiko.sales.mapper.IJaikoSalesMapper;
import jp.prj.araku.jaiko.sales.vo.JaikoSalesVO;
import jp.prj.araku.product.mapper.IEstimateMapper;
import jp.prj.araku.product.vo.EstimateVO;
import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.DealerVO;

@Repository
public class JaikoSalesDAO {
	@Autowired
	SqlSession sqlSession;
	
	public HashMap<String, String> createSalesData(ArrayList<JaikoOrderVO> list) {
		HashMap<String, String> ret = new HashMap<>();
		IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		IEstimateMapper estimateMapper = sqlSession.getMapper(IEstimateMapper.class);
		ITabletPrdMapper dealerMapper = sqlSession.getMapper(ITabletPrdMapper.class);
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		
		String partner_id = "";
		String partner_nm = "";
		String dlv_dt = "";
		for(JaikoOrderVO orderVO : list) {
			JaikoSalesVO salesVO = new JaikoSalesVO();
			String jan_cd = orderVO.getJan_cd();
			partner_id = orderVO.getPartner_id();
			dlv_dt = orderVO.getReg_dt();
			salesVO.setDlv_dt(dlv_dt);
			salesVO.setJan_cd(jan_cd);
			
			dlv_dt = dlv_dt.replaceAll("-", "");
			salesVO.setFrom_dt(dlv_dt+"000000");
			salesVO.setTo_dt(dlv_dt+"235959");
			salesVO.setPartner_id(partner_id);
			
			ArrayList<JaikoSalesVO> srchRet = salesMapper.selectData(salesVO);
			if(srchRet.size() > 0) {
				// データがあったら処理を終わり。
				continue;
			}
			salesVO.setFrom_dt("");
			salesVO.setTo_dt("");
			salesVO.setPartner_id("");
			
			JaikoPrdInfoVO srchPrd = new JaikoPrdInfoVO();
			srchPrd.setJan_cd(jan_cd);
			// srchPrd.setPartner_id(partner_id);
			JaikoPrdInfoVO prdInfo = prdMapper.getOnePrdInfo(srchPrd);
			if(null == prdInfo) {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "["+jan_cd+"] 商品情報がありません。");
				return ret;
			}
			salesVO.setTax_rt(prdInfo.getTax_rt()); // 세율
			salesVO.setPrd_cd(prdInfo.getPrd_cd());
			salesVO.setPrd_nm(orderVO.getPrd_nm());
			
			EstimateVO srchEstimate = new EstimateVO();
			srchEstimate.setJan_cd(jan_cd);
			srchEstimate.setPartner_id(partner_id);
			ArrayList<EstimateVO> estiList = estimateMapper.selectStatus(srchEstimate);
			if(estiList.size() > 0) {
				EstimateVO estiInfo = estiList.get(0);
				salesVO.setDlv_prc(estiInfo.getPrd_prc()); // 단가
			}else {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "["+jan_cd+"] 見積情報がありません。");
				return ret;
			}
			
			double prc = Double.parseDouble(salesVO.getDlv_prc());
			int tax = Integer.parseInt(salesVO.getTax_rt());
			double dSalesRatio = (double) tax/100;
			double dCnspTax = prc * dSalesRatio;
			salesVO.setCnsp_tax(String.valueOf(dCnspTax)); // 消費税
			
			DealerVO srchDealer = new DealerVO();
			srchDealer.setDealer_id(partner_id);
			ArrayList<DealerVO> dealerList = dealerMapper.getDealerInfo(srchDealer);
			if(dealerList.size() > 0) {
				DealerVO retDealer = dealerList.get(0);
				partner_nm = retDealer.getDealer_nm();
				salesVO.setPartner_id(partner_id);
				salesVO.setPartner_nm(partner_nm);
				if(null == retDealer.getGbn()) {
					ret.put("retCd", "ERR");
					ret.put("retMsg", "["+partner_nm+"] の締切区分情報がありません。");
					return ret;
				}else {
					salesVO.setGbn(retDealer.getGbn()); // 締切区分
				}
				
			}else {
				ret.put("retCd", "ERR");
				ret.put("retMsg", "["+partner_id+"] 取引先情報がありません。");
				return ret;
			}
			
			salesMapper.insertData(salesVO);
		}
		
		JaikoSalesVO total = new JaikoSalesVO();
		total.setGbn("TOT");
		total.setPartner_id(partner_id);
		total.setDlv_dt(dlv_dt);
		dlv_dt = dlv_dt.replaceAll("-", "");
		total.setFrom_dt(dlv_dt+"000000");
		total.setTo_dt(dlv_dt+"235959");
		ArrayList<JaikoSalesVO> srchRet = salesMapper.selectData(total);
		if(srchRet.size() < 1) {
			total.setMid_tot("0");
			total.setMemo("");
			total.setPartner_nm(partner_nm);
			DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate paramDate = LocalDate.parse(dlv_dt, frm);
		    YearMonth yearMonth = YearMonth.from(paramDate);
		    LocalDate lastCurrentDate = yearMonth.atEndOfMonth();
		    total.setBill_dt(lastCurrentDate.format(frm)); // 해당월의 마지막날이 請求日付
			
			int retIns = salesMapper.insertData(total);
			if(retIns > 0) {
				ret.put("retCd", "S");
				ret.put("retMsg", "追加成功！");
			}
		}else {
			ret.put("retCd", "S");
			ret.put("retMsg", "データが存在する。");
		}
		
		return ret;
	}
	
	public ArrayList<JaikoSalesVO> getSalesData(JaikoSalesVO srch) {
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		return salesMapper.selectData(srch);
	}
	
	public ArrayList<JaikoSalesVO> getSalesData(String partner_id, String dt) {
		return getSalesData2(partner_id, dt, "");
	}
	
	public ArrayList<JaikoSalesVO> getSalesData2(String partner_id, String dt, String gbn) {
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		JaikoSalesVO srch = new JaikoSalesVO();
		srch.setPartner_id(partner_id);
		if(!"".equals(dt)) {
			srch.setFrom_dt(dt.replaceAll("-", "")+"000000");
			srch.setTo_dt(dt.replaceAll("-", "")+"235959");
		}
		srch.setGbn(gbn);
		return salesMapper.selectData(srch);
	}
	
	public HashMap<String, Object> getSalesData(String partner_id, String dt, String gbn) {
		HashMap<String, Object> ret = new HashMap<>();
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		JaikoSalesVO srch = new JaikoSalesVO();
		srch.setPartner_id(partner_id);
		if(!"".equals(dt)) {
			srch.setFrom_dt(dt.replaceAll("-", "")+"000000");
			srch.setTo_dt(dt.replaceAll("-", "")+"235959");
		}
		srch.setGbn(gbn);
		ArrayList<JaikoSalesVO> srchRet = salesMapper.selectData(srch);
		if(srchRet.size() > 0) {
			JaikoSalesVO vo = srchRet.get(0);
			ret.put("memo", vo.getMemo());
			ret.put("dlvNo", String.format("%08d", Integer.parseInt(vo.getSeq_id())));
			ret.put("billDt", vo.getBill_dt());
		}
		return ret;
	}
	
	public ArrayList<JaikoSalesVO> updateData(ArrayList<JaikoSalesVO> list) {
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		ITabletPrdMapper dealerMapper = sqlSession.getMapper(ITabletPrdMapper.class);
		IEstimateMapper estimateMapper = sqlSession.getMapper(IEstimateMapper.class);
		
		for(JaikoSalesVO vo : list) {
			if(null != vo.getSeq_id()) {
				if("SEL".equals(vo.getGbn())) {
					vo.setGbn("");
					EstimateVO srchEstimate = new EstimateVO();
					srchEstimate.setJan_cd(vo.getJan_cd());
					// srchEstimate.setPartner_id(vo.getPartner_id());
					ArrayList<EstimateVO> estiList = estimateMapper.selectStatus(srchEstimate);
					if(estiList.size() > 0) {
						EstimateVO estiInfo = estiList.get(0);
						vo.setDlv_prc(estiInfo.getPrd_prc()); // 단가
					}else {
						vo.setDlv_prc("0"); // 미쯔모리에 데이터가 없는경우 단가를 0으로 세팅해버림
					}
					double prc = Double.parseDouble(vo.getDlv_prc());
					int tax = Integer.parseInt(vo.getTax_rt());
					double dSalesRatio = (double) tax/100;
					double dCnspTax = prc * dSalesRatio;
					vo.setCnsp_tax(String.valueOf(dCnspTax)); // 消費税
				}
				salesMapper.updateData(vo);
			}else if("TOT".equals(vo.getGbn())) {
				String dlvDt = vo.getDlv_dt().replaceAll("-", "");
				vo.setFrom_dt(dlvDt+"000000");
				vo.setTo_dt(dlvDt+"235959");
				DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			    LocalDate paramDate = LocalDate.parse(vo.getDlv_dt(), frm);
			    YearMonth yearMonth = YearMonth.from(paramDate);
			    LocalDate lastCurrentDate = yearMonth.atEndOfMonth();
			    vo.setBill_dt(lastCurrentDate.format(frm)); // 해당월의 마지막날이 請求日付
			    DealerVO srchDealer = new DealerVO();
				srchDealer.setDealer_id(vo.getPartner_id());
				ArrayList<DealerVO> dealerList = dealerMapper.getDealerInfo(srchDealer);
				if(dealerList.size() > 0) {
					DealerVO retDealer = dealerList.get(0);
					vo.setPartner_nm(retDealer.getDealer_nm());
				}
				ArrayList<JaikoSalesVO> srchRet = salesMapper.selectData(vo);
				if(srchRet.size() > 0) {
					vo.setSeq_id(srchRet.get(0).getSeq_id());
					salesMapper.updateData(vo);
				}else {
					salesMapper.insertData(vo);
				}
			}else {
				DealerVO srchDealer = new DealerVO();
				srchDealer.setDealer_id(vo.getPartner_id());
				ArrayList<DealerVO> dealerList = dealerMapper.getDealerInfo(srchDealer);
				if(dealerList.size() > 0) {
					DealerVO retDealer = dealerList.get(0);
					vo.setPartner_nm(retDealer.getDealer_nm());
					vo.setGbn(retDealer.getGbn()); // 締切区分
				}
				vo.setPrd_cd("コード");
				vo.setPrd_nm("商品名");
				salesMapper.insertData(vo);
			}
		}
		if("TOT".equals(list.get(0).getGbn())) {
			return getSalesData2(list.get(0).getPartner_id(), list.get(0).getDlv_dt(), list.get(0).getGbn());
		}else {
			return getSalesData(list.get(0).getPartner_id(), list.get(0).getDlv_dt());
		}
	}
	
	public ArrayList<JaikoSalesVO> deleteData(ArrayList<JaikoSalesVO> list) {
		IJaikoSalesMapper salesMapper = sqlSession.getMapper(IJaikoSalesMapper.class);
		
		for(JaikoSalesVO vo : list) {
			salesMapper.deleteData(vo.getSeq_id());
		}
		return getSalesData2(list.get(0).getPartner_id(), list.get(0).getDlv_dt(), list.get(0).getGbn());
	}
}
