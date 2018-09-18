package jp.prj.araku.file.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
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
	public String processCsvUpload(MultipartFile rakUpload, HttpServletRequest req) throws IOException {
		log.info("processCsvUpload");
		dao.insertRakutenInfo(rakUpload, fileEncoding, req);
		return "redirect:orderView";
	}
	
	@RequestMapping(value="/txtUpload", method=RequestMethod.POST)
	public void processTxtUpload(MultipartFile amaUpload) throws IOException {
		log.info("processTxtUpload");
		dao.insertAmazonInfo(amaUpload, fileEncoding);
	}
	
	@RequestMapping(value="/yuUpload", method=RequestMethod.POST)
	public String processYuUpload(MultipartFile yuUpload) throws IOException {
		log.info("processYuUpload");
		dao.updateRakutenInfo(yuUpload, fileEncoding);
		return "redirect:orderView";
	}
	
	@RequestMapping(value="/yuDown", method=RequestMethod.POST)
	public void processYupuriDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company) {
		log.info("processYupuriDownload");
		log.debug("id list {}", id_lst);
		log.debug("delivery company {}", delivery_company);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.rakutenFormatCSVDownload(response, seq_id_list, fileEncoding, "YU", delivery_company);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
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
	
	@RequestMapping(value="/errListDown", method=RequestMethod.POST)
	public String processErrListDownload(
			HttpServletResponse response, HttpServletRequest request) {
		log.info("processErrListDownload");
		
		String[] seq_id_list = {};
		
		try {
				dao.rakutenFormatCSVDownload(request, response, seq_id_list, fileEncoding, "ERR", null);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
		
		return "redirect:orderView";
	}
	
	@RequestMapping(value="/yaDown", method=RequestMethod.POST)
	public void processYamatoDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company) {
		log.info("processYamatoDownload");
		log.debug("id list {}", id_lst);
		log.debug("delivery company {}", delivery_company);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.yamatoFormatDownload(response, seq_id_list, fileEncoding, delivery_company);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
}
