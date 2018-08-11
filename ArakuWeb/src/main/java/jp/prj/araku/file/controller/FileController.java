package jp.prj.araku.file.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
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
import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.TranslationVO;

@Controller
public class FileController {
	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Value("${FILE_ENCODING}")
	private String file_encoding;
	
	@Autowired
	FileDAO fileDAO;
	@Autowired
	ListDAO listDAO;
	
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
            	fileDAO.insertRakutenInfo(vo);
            	log.debug("registered id :: {}", vo.getSeq_id());
                log.debug(vo.toString());
                log.debug("==========================");
                
                // 項目・選択肢 (상품옵션) 처리
                String option_content = vo.getProduct_option();
                if(option_content != null && option_content.length() > 1) {
                        String[] arr = option_content.split("\n");

                       HashSet<String> set = new HashSet<>();
                        for (int i=0; i<arr.length; i++) {
                    		log.debug(String.format("%d :: %s", i, arr[i]));
                            String[] data = arr[i].split(":");
                            String value = null;
                            if (data.length > 1) {
                            	// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
                            	value = data[data.length-1];
                            	log.debug(String.format("option value1 :: %s", value));
                            	set.add(value.trim());
                            } else {
                            	// 콜론이 아닌 일본어자판 컴마로 나뉘어져있는 경우가 있어 처리
                            	data = arr[i].split("、");
                            	for (String value2 : data) {
                            		log.debug(String.format("option value2 :: %s", value2));
                            		set.add(value2.trim());
                            	}
                            }
                            
                            // 거의 없겠다만 콜론과 일본어자판 컴마가 같이 있는 경우도 있어 처리
                            if (data[0].contains("、")) {
                            	data = data[0].split("、");
                            	for (String value3 : data) {
                            		log.debug(String.format("option value3 :: %s", value3));
                            		set.add(value3.trim());
                            	}
                            }
                        }
                        log.debug("{}", set);
                        TranslationVO transVO = new TranslationVO();
                        for (String value : set) {
                        	transVO.setBefore_trans(vo.getProduct_name() + " " + value);
                        	listDAO.addTransInfo(transVO);
                        }
                        log.debug("==========================");
                }
            }
	            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return "redirect:orderView";
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
