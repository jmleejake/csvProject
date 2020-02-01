package jp.prj.araku.tanpin.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import jp.prj.araku.tanpin.mapper.ITanpinMapper;
import jp.prj.araku.tanpin.vo.TanpinVO;

@Repository
public class TanpinDAO {
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<TanpinVO> getTanpinInfo(String select_type) {
		TanpinVO vo = new TanpinVO();
		vo.setSelect_type(select_type);
		return getTanpinInfo(vo);
	}
	
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		return mapper.getTanpinInfo(vo);
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

}
