package jp.prj.araku.tanpin.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.tanpin.dao.TanpinDAO;
import jp.prj.araku.tanpin.vo.TanpinVO;

@RequestMapping(value="/araku/prdAnalysis")
@Controller
public class TanpinController {
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	private TanpinDAO dao;
	
	public String tanpinFileUpload() {
		return "tanpin/prdManage";
	}
	
	@RequestMapping(value="/prdMng")
	public String showPrdManage(Model model) {
		model.addAttribute("dealers", dao.getTanpinInfo("dealer"));
		model.addAttribute("makers", dao.getTanpinInfo("maker"));
		return "tanpin/prdManage";
	}
	
	@RequestMapping(value="/fileUpload", method=RequestMethod.POST)
	public String manipulateTanpinInfo(
			HttpServletRequest request, MultipartFile upload) throws IOException {
		dao.manipulateTanpinInfo(request, upload, fileEncoding);
		return "redirect:prdMng";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTanpin")
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo) {
		return dao.getTanpinInfo(vo);
	}
	
	@RequestMapping(value="/delTanpin", method=RequestMethod.POST)
	public String deleteTanpinInfo(@RequestBody ArrayList<TanpinVO> list) {
		dao.deleteTanpinInfo(list);
		return "redirect:getTanpin";
	}
	
	@RequestMapping(value="/down", method=RequestMethod.POST)
	public void downloadTanpinInfo(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.downloadTanpinInfo(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
		} catch (CsvDataTypeMismatchException e) {
		} catch (CsvRequiredFieldEmptyException e) {
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/addTanpin", method=RequestMethod.POST)
	public ArrayList<TanpinVO> addTanpin(TanpinVO vo) {
		return dao.addTanpin(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/modTanpin", method=RequestMethod.POST)
	public ArrayList<TanpinVO> modTanpin(@RequestBody ArrayList<TanpinVO> list) {
		return dao.modTanpin(list);
	}
}
