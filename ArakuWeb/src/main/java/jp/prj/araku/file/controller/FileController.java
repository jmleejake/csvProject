package jp.prj.araku.file.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.file.dao.FileDAO;

@Controller
public class FileController {
	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	FileDAO dao;
	
	@RequestMapping(value="/csvUpload", method=RequestMethod.POST)
	public String processCsvUpload(MultipartFile rakUpload) throws IOException {
		log.info("processCsvUpload");
		dao.insertRakutenInfo(rakUpload, fileEncoding);
		return "redirect:orderView";
	}
	
	@RequestMapping(value="/txtUpload", method=RequestMethod.POST)
	public void processTxtUpload(MultipartFile amaUpload) throws IOException {
		log.info("processCsvUpload");
		dao.insertAmazonInfo(amaUpload, fileEncoding);
	}
	
	@RequestMapping(value="/yuDown", method=RequestMethod.POST)
	public void processYupuriDownload(
			HttpServletResponse response, @RequestParam(value="id_lst") String id_lst) {
		log.info("processYupuriDownload");
		log.debug("{}", id_lst);
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			 //TODO 다운로드시 상품명을 치환한 결과를 update하여 csv파일형태로 다운로드 한다.
			try {
				dao.yupuriDownload(response, seq_id_list, fileEncoding);
			} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
				log.error(e.toString());
			}
		} catch (IOException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/rFileDown", method=RequestMethod.POST)
	public void processRakutenCSVDownload(
			HttpServletResponse response, @RequestParam(value="id_lst") String id_lst) {
		log.info("processRakutenCSVDownload");
		try {
			dao.rCSVDownload(response, id_lst.split(","), fileEncoding);
		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
			log.error(e.toString());
		}
	}
}
