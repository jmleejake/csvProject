package jp.prj.araku.jaiko.product.controller;

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

import jp.prj.araku.jaiko.product.dao.JaikoPrdInfoDAO;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;

@RequestMapping(value = "/jaiko/prdInfo")
@Controller
public class JaikoPrdInfoController {
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	JaikoPrdInfoDAO dao;
	
	@RequestMapping(value = "")
	public String showJaikoPrdInfo() {
		return "jaiko/prdInfo";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrdInfo")
	public ArrayList<JaikoPrdInfoVO> getJaikoPrdInfo(JaikoPrdInfoVO vo) {
		log.debug("getJaikoPrdInfo :: {}", vo);
		return dao.getJaikoPrdInfo(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInfoVO> manipulateJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInfoVO> list) {
		log.debug("manipulateJaikoPrdInfo :: {}", list);
		return dao.manipulateJaikoPrdInfo(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ArrayList<JaikoPrdInfoVO> deleteJaikoPrdInfo(@RequestBody ArrayList<JaikoPrdInfoVO> list) {
		log.debug("deleteJaikoPrdInfo :: {}", list);
		return dao.deleteJaikoPrdInfo(list);
	}
	
	@RequestMapping(value = "/down", method = RequestMethod.POST)
	public void csvDownload(HttpServletResponse response) {
		try {
			dao.jaikoPrdCsvDownload(response, fileEncoding);
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
			dao.jaikoPrdCsvUpload(upFile, fileEncoding);
		}catch (IOException e) {
			log.error(e.toString());
		}
		return "jaiko/prdInfo";
	}

}
