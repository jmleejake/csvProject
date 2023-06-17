package jp.prj.araku.tanpin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tanpin.dao.TanpinDAO;
import jp.prj.araku.tanpin.vo.ExpireManageVo;
import jp.prj.araku.tanpin.vo.TanpinVO;

@RequestMapping(value="/araku/prdAnalysis")
@Controller
public class TanpinController {
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	private TanpinDAO dao;
	
	@Autowired
	private TabletPrdDAO dealerDAO;
	
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
	
	/**
	 * 20211120
	 * 発注書発行画面S
	 * */
	
	@RequestMapping(value = "/orderView")
	public String showOrderIssue(Model model) {
		model.addAttribute("dealers", dealerDAO.getDealerInfo(null));
		return "tanpin/orderView";
	}
	
	@RequestMapping(value = "/orderDown", method = RequestMethod.POST)
	public void downloadOrderForm(HttpServletResponse response
			,@RequestParam(value = "dealer_id") String id) {
		dao.downloadOrderForm(response, id, fileEncoding);
	}
	
	/**
	 * 20211120
	 * 発注書発行画面E
	 * */
	
	/**
	 * 20230617
	 * 賞味期限管理S
	 * */
	@RequestMapping(value = "/expMng")
	public String showExpireManage(Model model) {
		return "tanpin/expManage";
	}
	
	@ResponseBody
	@RequestMapping(value="/getExpMng")
	public List<ExpireManageVo> getExpireManage(ExpireManageVo vo) {
		return dao.getExpireManage(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addExpMng", method=RequestMethod.POST)
	public List<ExpireManageVo> addExpireManage(ExpireManageVo vo) {
		return dao.addExpireManage(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/modExpMng", method=RequestMethod.POST)
	public List<ExpireManageVo> modifyExpireManage(@RequestBody List<ExpireManageVo> list) {
		return dao.modifyExpireManage(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delExpMng", method=RequestMethod.POST)
	public List<ExpireManageVo> removeExpireManage(@RequestBody List<ExpireManageVo> list) {
		return dao.removeExpireManage(list);
	}
	/**
	 * 20230617
	 * 賞味期限管理E
	 * */
}
