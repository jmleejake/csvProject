package jp.prj.araku.product.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.product.dao.ProductAnalysisDAO;
import jp.prj.araku.product.vo.ProductAnalysisVO;

@RequestMapping(value="prdAnalysis")
@Controller
public class ProductAnalysisController {
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	private ProductAnalysisDAO dao;
	
	@RequestMapping(value="/listView")
	public String showPrdAnalysis() {
		return "productAnalysis/prdAnalysis";
	}
	
	@RequestMapping(value="/prdAnalFileUp", method=RequestMethod.POST)
	public String manipulateTanpinInfo(
			HttpServletRequest request, MultipartFile upload) throws IOException {
		dao.insertPrdAnalysis(request, upload, fileEncoding);
		return "redirect:listView";
	}
	
	@ResponseBody
	@RequestMapping(value="/getPrdAnal")
	public ArrayList<ProductAnalysisVO> getPrdAnalysis(ProductAnalysisVO vo) {
		return dao.getPrdAnalysis(vo);
	}
	
	@RequestMapping(value="/delPrdAnal", method=RequestMethod.POST)
	public String deletePrdAnalysis(@RequestBody ArrayList<ProductAnalysisVO> list) {
		dao.deletePrdAnalysis(list);
		return "redirect:getPrdAnal";
	}
	
	@RequestMapping(value="/updPrdAnal", method=RequestMethod.POST)
	public String updatePrdAnalysis(@RequestBody ArrayList<ProductAnalysisVO> list) {
		dao.updatePrdAnalysis(list);
		return "redirect:getPrdAnal";
	}
	
	@RequestMapping(value="/downPrdAnal", method=RequestMethod.POST)
	public void downloadTanpinInfo(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.downloadPrdAnalysis(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
		} catch (CsvDataTypeMismatchException e) {
		} catch (CsvRequiredFieldEmptyException e) {
		}
	}

}
