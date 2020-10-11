package jp.prj.araku.tanpin.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
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
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.tanpin.mapper.ITanpinMapper;
import jp.prj.araku.tanpin.vo.TanpinVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class TanpinDAO {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<TanpinVO> getTanpinInfo(String select_type) {
		TanpinVO vo = new TanpinVO();
		vo.setSelect_type(select_type);
		return getTanpinInfo(vo);
	}
	
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		ArrayList<TanpinVO> ret = mapper.getTanpinInfo(vo);
		if(!("dealer".equals(vo.getSelect_type()) || "maker".equals(vo.getSelect_type()))) {
			for(TanpinVO tmp : ret) {
				if(null != tmp.getInprice() && null != tmp.getPrice()) {
					tmp.setInprice("¥"+CommonUtil.toCommaFormat(Long.parseLong(tmp.getInprice())));
					tmp.setPrice("¥"+CommonUtil.toCommaFormat(Long.parseLong(tmp.getPrice())));
				}
			}
		}
		return ret;
	}
	
	public void manipulateTanpinInfo(
			HttpServletRequest req, 
			MultipartFile file, 
			String fileEncoding) throws IOException {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + file.getContentType());
		log.debug("name: " + file.getName());
		log.debug("original name: " + file.getOriginalFilename());
		log.debug("size: " + file.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), fileEncoding));
			
			CsvToBean<TanpinVO> csvToBean = new CsvToBeanBuilder<TanpinVO>(reader)
                    .withType(TanpinVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<TanpinVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	TanpinVO vo = iterator.next();
            	// JANコード 있으면 update, 없으면 insert
            	if(null != vo.getPrd_cd() && !vo.getPrd_cd().equals("")) {
            		ArrayList<TanpinVO> search = getTanpinInfo(vo);
            		if(search.size() == 1) {
            			vo.setSeq_id(search.get(0).getSeq_id());
            			mapper.updateTanpinInfo(vo);
            		}else {
            			mapper.insertTanpinInfo(vo);
            		}
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void deleteTanpinInfo(ArrayList<TanpinVO> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		
		for(TanpinVO vo : list) {
			mapper.deleteTanpinInfo(vo.getSeq_id());
		}
	}
	
	public void downloadTanpinInfo(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException  {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			TanpinVO tan = new TanpinVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for(String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			tan.setSeq_id_list(seq_id_list);
			
			String csvFileName = "TAN" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			
			String[] header = {
					"商品メーカー"
					, "商品メーカー名"
					, "商品コード(JAN)"
					, "商品名"
					, "容量"
					, "取引先code"
					, "取引先会社名"
					, "仕入金額"
					, "販売金額"
					, "商品販売基準金額"
					, "商品税(抜、込)"
					, "商品税率"
			};
			
			ArrayList<TanpinVO> searchList = mapper.getTanpinInfo(tan);
			ArrayList<ArakuVO> list = new ArrayList<>();
			for(TanpinVO vo : searchList) {
				list.add(vo);
			}
			CommonUtil.executeCSVDownload(csvWriter, writer, header, list);
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
