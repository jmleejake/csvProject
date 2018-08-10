package jp.prj.araku.file.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import jp.prj.araku.file.dao.FileDAO;
import jp.prj.araku.file.vo.RakutenVO;

@Controller
public class FileController {
	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Value("${FILE_ENCODING}")
	private String file_encoding;
	
	@Autowired
	FileDAO dao;
	
	@RequestMapping(value="/csvUpload", method=RequestMethod.POST)
	public String processCsvUpload(MultipartFile rakUpload) throws IOException {
		log.info("processCsvUpload");
		log.debug("encoding : {}", file_encoding);
		log.debug("contentType: {}", rakUpload.getContentType());
		log.debug("name: {}", rakUpload.getName());
		log.debug("original name: {}", rakUpload.getOriginalFilename());
		log.debug("size: {}", rakUpload.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(rakUpload.getInputStream(), file_encoding));
			
			CsvToBean<RakutenVO> csvToBean = new CsvToBeanBuilder<RakutenVO>(reader)
                    .withType(RakutenVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<RakutenVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	RakutenVO vo = iterator.next();
            	dao.insertRakutenInfo(vo);
            	log.debug("registered id :: {}", vo.getSeq_id());
                log.debug(vo.toString());
                log.debug("==========================");
            }
	            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/txtUpload", method=RequestMethod.POST)
	public void processTxtUpload(MultipartFile upload) throws IOException {
		BufferedReader reader = null;
        try {
                reader = new BufferedReader(
                                new InputStreamReader(upload.getInputStream(), file_encoding));
                String line = "";
                String splitBy = "      ";

                while ((line = reader.readLine()) != null) {
                	log.debug(String.format("\none line\n%s", line));
                        String[] whatArr = line.split(splitBy);
                        for (String what : whatArr) {
                        	log.debug(String.format("%s", what));
                        }
                }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
	}
}
