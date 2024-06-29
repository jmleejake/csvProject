package jp.prj.araku.tanpin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jp.prj.araku.tanpin.vo.AllmartManageVo;

@RequestMapping(value="/araku/prdAnalysis")
@Controller
public class TanpinController {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
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
	
	@RequestMapping(value = "/orderToday", method = RequestMethod.POST)
	public void downloadTodayOrder(HttpServletResponse response) {
		try {
			dao.downloadTodayOrder(response, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	/**
	 * 20211120
	 * 発注書発行画面E
	 * */
	
	/**
	 * 20211120
	 * ALLMART価格管理画面E
	 * */
	@RequestMapping(value = "/allMng")
	public String showallManage(Model model) {
		return "tanpin/martManage";
	}
	
	@ResponseBody
	@RequestMapping(value="/getAllMng")
	public List<AllmartManageVo> getAllmartManage(HttpServletRequest req,AllmartManageVo vo) {
		return dao.getAllmartManage(vo);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/addAllMng", method=RequestMethod.POST)
//	public List<AllmartManageVo> addAllmartManage(AllmartManageVo vo) {
//		return dao.addAllmartManage(vo);
//	}
	
	@ResponseBody
	@RequestMapping(value = "/modAllMng", method=RequestMethod.POST)
	public List<AllmartManageVo> modifyAllmartManage(@RequestBody List<AllmartManageVo> list) {
		return dao.modifyAllmartManage(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delAllmart", method=RequestMethod.POST)
	public List<AllmartManageVo> removeAllmartManage(@RequestBody List<AllmartManageVo> list) {
		return dao.removeAllmartManage(list);
	}
	
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
	
	@RequestMapping(value="/prdAllFileUp", method=RequestMethod.POST)
//	public String manipulateAllmartInfo(
//			HttpServletRequest req, MultipartFile file) throws IOException {
	public String manipulateAllmartInfo(
			 MultipartFile upload) throws IOException {
        // ログを追加してアップロードファイルの情報を確認
        log.debug("Received file: " + upload.getOriginalFilename());
        
        // アップロードされたファイルを処理するコード
        try {
			dao.insertAllmartManageExe(upload, fileEncoding);
        // ファイル処理コード
	    } catch (Exception e) {
	        log.debug("Error processing file: " + e.getMessage(), e);
	        throw e;
	    }
        
		return "redirect:allMng";
	}
	
	@RequestMapping(value="/downAllmart", method=RequestMethod.POST)
	public void downloadAllmartInfo(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] product_id_list = id_lst.split(",");
		try {
			dao.downloadAllmart(response, product_id_list, fileEncoding);
		} catch (IOException e) {
		} catch (CsvDataTypeMismatchException e) {
		} catch (CsvRequiredFieldEmptyException e) {
		}
	}
}
