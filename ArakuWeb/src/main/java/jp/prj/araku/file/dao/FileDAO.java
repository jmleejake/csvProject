package jp.prj.araku.file.dao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.file.mapper.IFileMapper;
import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.util.CommonUtil;

/**
 * @comment
 * [MOD-0826] 콜론으로만 델리미터를 처리할 예정이므로 일단 주석처리
 */
@Repository
public class FileDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.file");
	
	
	
	public void rCSVDownload(HttpServletResponse response, String[] id_lst, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rCSVDownload");
		log.debug("seq_id_list : " + id_lst.toString());
		log.debug("encoding : " + fileEncoding);
		
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "R" + CommonUtil.getDate("YYYY-MM-dd HH:mm:ss", 0) + ".csv";

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
			
			String[] header = {"受注番号", "受注ステータス", "処理番号", "お荷物伝票番号", "配送会社", "フリー項目01"};
			
			RCSVDownVO vo = new RCSVDownVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			vo.setSeq_id_list(seq_id_list);
			ArrayList<RCSVDownVO> list = mapper.getRCSVDownList(vo);
			
			executeRakutenDownload(csvWriter, writer, header, list);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeRakutenDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<RCSVDownVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<RCSVDownVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
	
	/*
	public void updateRakutenInfo(MultipartFile yuUpload, String fileEncoding) throws IOException {
		log.info("updateRakutenInfo");
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + yuUpload.getContentType());
		log.debug("name: " + yuUpload.getName());
		log.debug("original name: " + yuUpload.getOriginalFilename());
		log.debug("size: " + yuUpload.getSize());
		
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(yuUpload.getInputStream(), fileEncoding));
			
			CsvToBean<RCSVDownVO> csvToBean = new CsvToBeanBuilder<RCSVDownVO>(reader)
	                .withType(RCSVDownVO.class)
	                .withSkipLines(1)
	                .withIgnoreLeadingWhiteSpace(true)
	                .build();

	        Iterator<RCSVDownVO> iterator = csvToBean.iterator();
	        
	        while (iterator.hasNext()) {
	        	RCSVDownVO vo = iterator.next();
            	
            	// 受注番号 검색하여 お荷物伝票番号를 update
            	RakutenSearchVO searchVO = new RakutenSearchVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getOrder_no());
            	
            	ArrayList<RakutenSearchVO> searchResult = listMapper.getRList(searchVO);
            	String seq_id = searchResult.get(0).getSeq_id();
            	
            	searchVO.setSeq_id(seq_id);
            	searchVO.setBaggage_claim_no(vo.getBaggage_claim_no());
            	listMapper.modRakutenInfo(searchVO);
	        }
		} finally {
			if (reader != null) {
                reader.close();
            }
		}
	}
	*/
}
