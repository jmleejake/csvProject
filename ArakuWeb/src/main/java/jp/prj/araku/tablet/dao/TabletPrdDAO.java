package jp.prj.araku.tablet.dao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.rakuten.vo.RakutenDuplicateVO;
import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.StockVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

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
	
	public ArrayList<TabletPrdVO> getPrdManage(TabletPrdVO vo) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		return mapper.getPrdManage(vo);
	}
	
	public ArrayList<DealerVO> getDealerInfo(DealerVO vo) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		return mapper.getDealerInfo(vo);
	}
	
	public ArrayList<DealerVO> manipulateDealerInfo(ArrayList<DealerVO> list) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		
		for(DealerVO vo : list) {
			if(null != vo.getSeq_id()) {
				mapper.updateDealerInfo(vo);
			}else {
				mapper.insertDealerInfo(vo);
			}
		}
		
		return mapper.getDealerInfo(null);
	}
	
	public ArrayList<DealerVO> deleteDealerInfo(ArrayList<DealerVO> list) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		
		for(DealerVO vo : list) {
			mapper.deleteDealerInfo(vo.getSeq_id());
		}
		
		return mapper.getDealerInfo(null);
	}
	
	public ArrayList<StockVO> getStockManage(StockVO vo) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		return mapper.getStockManage(vo);
	}
	
	public ArrayList<StockVO> manipulateStockManage(ArrayList<StockVO> list) {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		
		StockVO searchVO1 = new StockVO();
		for(StockVO vo : list) {
			ArrayList<StockVO> stockList = mapper.getStockManage(vo);
			if(stockList.size() > 0) {
				mapper.updateStockManage(vo);
				int stock_cnt = Integer.parseInt(vo.getPrd_cnt());
				TabletPrdVO prdVO = mapper.getPrdInfoForStock(vo.getJan_cd());
				if(null != prdVO) {
					int prd_cnt = 0;
					if(null != prdVO.getPrd_cnt()) {
						prd_cnt = Integer.parseInt(prdVO.getPrd_cnt());
					}
					prdVO.setPrd_cd("");
					prdVO.setPrd_nm("");
					prdVO.setPrd_dtl("");
					prdVO.setOrder_no("");
					prdVO.setJan_cd(vo.getJan_cd());
					prdVO.setPrd_cnt((prd_cnt + stock_cnt)+"");
					prdVO.setUpdate_type("cnt");
					mapper.updatePrdInfo(prdVO);
				}
			}else {
				TabletPrdVO prdVO = mapper.getPrdInfoForStock(vo.getJan_cd());
				if(null != prdVO) {
					vo.setPrd_cnt(prdVO.getPrd_cnt());
					vo.setPrd_nm(prdVO.getPrd_nm() + " " + prdVO.getPrd_dtl());
					mapper.insertStockManage(vo);
				}
			}
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
		searchVO1.setReceive_date(sdf.format(cal.getTime()));
		searchVO1.setDealer_id(list.get(0).getDealer_id());
		return mapper.getStockManage(searchVO1);
	}
	
	public void processStockDownload(
			HttpServletResponse response
			, String[] seqIds
			, String fileEncoding) throws IOException
											, CsvDataTypeMismatchException
											, CsvRequiredFieldEmptyException {
		ITabletPrdMapper mapper = sqlSession.getMapper(ITabletPrdMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "StockInfo" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(response.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			String[] header = {"取引先名", "商品名", "商品数量", "ＪＡＮコード", "入庫日"};
			ArrayList<ArakuVO> sList = new ArrayList<>();
			for(String id : seqIds) {
				StockVO vo = new StockVO();
				vo.setSeq_id(id);
				sList.add(mapper.getStockManage(vo).get(0));
			}
			CommonUtil.executeCSVDownload(csvWriter, writer, header, sList);
			
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
}
