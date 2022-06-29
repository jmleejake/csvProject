package jp.prj.araku.jaiko.product.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class JaikoPrdInfoDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		return mapper.getJaikoPrdInfo(vo);
	}
	
	public ArrayList<JaikoPrdInfoVO> manipulateJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		for(JaikoPrdInfoVO vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updateJaikoPrdInfo(vo);
			}else {
				mapper.insertJaikoPrdInfo(vo);
			}
		}
		return mapper.getJaikoPrdInfo(new JaikoPrdInfoVO());
	}
	
	public ArrayList<JaikoPrdInfoVO> deleteJaikoPrdInfo(ArrayList<JaikoPrdInfoVO> list) {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		for(JaikoPrdInfoVO vo : list) {
			mapper.deleteJaikoPrdInfo(vo.getSeq_id());
		}
		return mapper.getJaikoPrdInfo(new JaikoPrdInfoVO());
	}
	
	/**
	 * CSVダウンロード
	 * 
	 * @param response
	 * @param fileEncoding
	 * @throws IOException
	 * @throws CsvDataTypeMismatchException
	 * @throws CsvRequiredFieldEmptyException
	 */
	public void jaikoPrdCsvDownload(HttpServletResponse response, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "jaiko_prd_"+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			JaikoPrdInfoVO srch = new JaikoPrdInfoVO();
			srch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			ArrayList<ArakuVO> downList = new ArrayList<ArakuVO>();
			downList.addAll(mapper.getJaikoPrdInfo(srch));
			CommonUtil.executeCSVDownload(csvWriter, writer, CommonUtil.jaikoPrdHeader(), downList);
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * アップロード
	 * 
	 * @param upFile
	 * @param fileEncoding
	 * @throws IOException
	 */
	public void jaikoPrdCsvUpload(MultipartFile upFile, String fileEncoding) throws IOException {
		IJaikoPrdInfoMapper mapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(upFile.getInputStream(), fileEncoding));
			
			CsvToBean<JaikoPrdInfoVO> csvToBean = new CsvToBeanBuilder<JaikoPrdInfoVO>(reader)
                    .withType(JaikoPrdInfoVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<JaikoPrdInfoVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	JaikoPrdInfoVO vo = iterator.next();
            	
            	JaikoPrdInfoVO srch = new JaikoPrdInfoVO();
            	srch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	srch.setJan_cd(vo.getJan_cd());
            	ArrayList<JaikoPrdInfoVO> srchRet = mapper.getJaikoPrdInfo(srch);
            	if(srchRet.size() < 1) {
            		mapper.insertJaikoPrdInfo(vo);
            	}
            }
		}finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
