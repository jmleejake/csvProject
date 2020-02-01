package jp.prj.araku.tanpin.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jp.prj.araku.tanpin.dao.TanpinDAO;
import jp.prj.araku.tanpin.vo.TanpinVO;

@RequestMapping(value="tanpin")
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
	
	@RequestMapping(value="delTanpin", method=RequestMethod.POST)
	public String deleteTanpinInfo(@RequestBody ArrayList<TanpinVO> list) {
		dao.deleteTanpinInfo(list);
		return "redirect:getTanpin";
	}
}
