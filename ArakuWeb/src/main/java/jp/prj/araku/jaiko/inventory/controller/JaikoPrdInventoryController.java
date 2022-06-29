package jp.prj.araku.jaiko.inventory.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.inventory.dao.JaikoPrdInventoryDAO;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;

@RequestMapping(value = "/jaiko/prdInven")
@Controller
public class JaikoPrdInventoryController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	JaikoPrdInventoryDAO dao;
	
	@RequestMapping(value = "")
	public String showJaikoPrdInventory() {
		return "jaiko/prdInventory";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrdInven")
	public ArrayList<JaikoPrdInventoryVO> getJaikoPrdInfo(JaikoPrdInventoryVO vo) {
		log.debug("getJaikoPrdInfo :: {}", vo);
		return dao.getJaikoPrdInventory(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInventoryVO> manipulateJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInventoryVO> list) {
		log.debug("manipulateJaikoPrdInfo :: {}", list);
		return dao.manipulatePrdInventory(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInventoryVO> deleteJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInventoryVO> list) {
		log.debug("deleteJaikoPrdInfo :: {}", list);
		return dao.deleteJaikoPrdInventory(list);
	}
	
	/**
	 * 棚卸入力表
	 * 
	 * @param upload
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/csvUpload", method = RequestMethod.POST)
	public String processProductInventory(MultipartFile upload) throws IOException {
		dao.processProductInventory(upload, fileEncoding);
		return "redirect:/jaiko/prdInven";
	}
	
	/**
	 * 棚卸表DL
	 * 
	 * @param response
	 */
	@RequestMapping(value="/csvDown", method = RequestMethod.POST)
	public void prdInventoryDownload(HttpServletResponse response) {
		try {
			dao.prdInventoryDownload(response, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value = "/down", method = RequestMethod.POST)
	public void csvDownload(HttpServletResponse response, String[] list) {
		try {
			dao.jaikoInvenCsvDownload(response, fileEncoding, list);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value = "/up", method = RequestMethod.POST)
	public String csvUpload(MultipartFile upFile) {
		try {
			dao.jaikoInvenCsvUpload(upFile, fileEncoding);
		}catch (IOException e) {
			log.error(e.toString());
		}
		return "jaiko/prdInventory";
	}

}
