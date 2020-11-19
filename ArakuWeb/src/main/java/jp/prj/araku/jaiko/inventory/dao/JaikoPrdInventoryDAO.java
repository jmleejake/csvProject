package jp.prj.araku.jaiko.inventory.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.InventoryDownVO;
import jp.prj.araku.jaiko.inventory.vo.InventoryUpVO;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class JaikoPrdInventoryDAO {
	@Autowired
	SqlSession sqlSession;
	
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
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
	
	public void processProductInventory(MultipartFile upload, String fileEncoding) throws IOException {
		log.debug("processProductInventory");
		
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		IJaikoPrdInfoMapper prdInfoMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + upload.getContentType());
		log.debug("name: " + upload.getName());
		log.debug("original name: " + upload.getOriginalFilename());
		log.debug("size: " + upload.getSize());
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(upload.getInputStream(), fileEncoding));
			
			CsvToBean<InventoryUpVO> csvToBean = new CsvToBeanBuilder<InventoryUpVO>(reader)
                    .withType(InventoryUpVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
			Iterator<InventoryUpVO> iterator = csvToBean.iterator();
			while(iterator.hasNext()) {
				InventoryUpVO vo = iterator.next();
				
				JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
				invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				invenVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoPrdInventoryVO> invenSrchRet = mapper.getJaikoPrdInventory(invenVO);
				invenVO.setPrd_nm(vo.getPrd_nm());
				invenVO.setPrd_qty(vo.getPrd_qty());
				invenVO.setPrd_case(vo.getPrd_case());
				invenVO.setPrd_bara(vo.getPrd_bara());
				invenVO.setNow_prd_cnt(vo.getNow_prd_cnt());
				invenVO.setExp_dt(vo.getExp_dt());
				invenVO.setSell_prc(vo.getSell_prc());
				if(invenSrchRet.size() > 0) {
					invenVO.setSeq_id(invenSrchRet.get(0).getSeq_id());
					mapper.updateJaikoPrdInventory(invenVO);
				}else {
					mapper.insertJaikoPrdInventory(invenVO);
				}
				
				JaikoPrdInfoVO pInfoVO = new JaikoPrdInfoVO();
				pInfoVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				pInfoVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoPrdInfoVO> prdSrchRet = prdInfoMapper.getJaikoPrdInfo(pInfoVO);
				pInfoVO.setPrd_nm(vo.getPrd_nm());
				pInfoVO.setStd_info(vo.getStd_info());
				pInfoVO.setPrd_cnt("1");
				pInfoVO.setPrd_unit_prc(vo.getPrd_unit_prc());
				pInfoVO.setTax_incld(vo.getTax_incl());
				pInfoVO.setTax_rt(vo.getTax_rt());
				if(prdSrchRet.size() > 0) {
					pInfoVO.setSeq_id(prdSrchRet.get(0).getSeq_id());
					prdInfoMapper.updateJaikoPrdInfo(pInfoVO);
				}else {
					prdInfoMapper.insertJaikoPrdInfo(pInfoVO);
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void prdInventoryDownload(HttpServletResponse response, String fileEncoding)
	throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		log.debug("prdInventoryDownload");
		log.debug("encoding : " + fileEncoding);
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		try {
			String csvFileName = "JAIKODOWN_" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = CommonUtil.prdInventoryHeader();
			
			ArrayList<JaikoPrdInventoryVO> targetList = mapper.getJaikoInventoryDownList();
			ArrayList<InventoryDownVO> list = new ArrayList<InventoryDownVO>();
			for(JaikoPrdInventoryVO vo : targetList) {
				InventoryDownVO invenDown = new InventoryDownVO();
				invenDown.setJan_cd(vo.getJan_cd());
				invenDown.setPrd_nm(vo.getPrd_nm());
				invenDown.setPrd_bara(vo.getPrd_bara());
				invenDown.setPrd_case(vo.getPrd_case());
				invenDown.setPrd_qty(vo.getPrd_qty());
				invenDown.setPrd_unit_prc(vo.getPrd_unit_prc());
				invenDown.setSell_prc(vo.getSell_prc());
				invenDown.setStd_info(vo.getStd_info());
				invenDown.setExp_dt(vo.getExp_dt());
				list.add(invenDown);
			}
			StatefulBeanToCsv<InventoryDownVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
		            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
		            .build();
			csvWriter.writeNext(header);
			beanToCSV.write(list);
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
}
