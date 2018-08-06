package jp.prj.araku.file.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import jp.prj.araku.file.vo.CSVUser;

@Controller
public class FileController {
	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Value("${FILE_ENCODING}")
	private String file_encoding;
	
	@RequestMapping(value="/csvUpload", method=RequestMethod.POST)
	public void processCsvUpload(MultipartFile upload) {
		log.info("processUpload :: encoding : {}", file_encoding);
		log.info("contentType: {}", upload.getContentType());
		log.info("name: {}", upload.getName());
		log.info("original name: {}", upload.getOriginalFilename());
		log.info("size: {}", upload.getSize());
		
		try  {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(upload.getInputStream(), file_encoding));
			
			CsvToBean<CSVUser> csvToBean = new CsvToBeanBuilder(bufferedReader)
                    .withType(CSVUser.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<CSVUser> csvUserIterator = csvToBean.iterator();

            while (csvUserIterator.hasNext()) {
                CSVUser csvUser = csvUserIterator.next();
                log.info(csvUser.toString());
                log.info("==========================");
            }
	            
		} catch(IOException e) {
        }
	}
	
	@RequestMapping(value="/txtUpload", method=RequestMethod.POST)
	public void processTxtUpload(MultipartFile upload) {
		BufferedReader reader = null;
        try {
                reader = new BufferedReader(
                                new InputStreamReader(upload.getInputStream(), file_encoding));
                String line = "";
                String splitBy = "      ";

                while ((line = reader.readLine()) != null) {
                        System.out.println(String.format("\none line\n%s", line));
                        String[] whatArr = line.split(splitBy);
                        for (String what : whatArr) {
                                System.out.println(String.format("%s", what));
                        }
                }
        } catch (UnsupportedEncodingException ue) {
        } catch (FileNotFoundException fe) {
        } catch (IOException e) {
        } finally {
                if (reader != null) {
                        try {
                                reader.close();
                        } catch (IOException e) {
                        }
                }
        }
	}
}
