package jp.prj.araku.rakuten.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

import jp.prj.araku.batch.dao.BatchDAO;
import jp.prj.araku.batch.vo.ItemOutputVO;
import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.rakuten.dao.RakutenDAO;
import jp.prj.araku.rakuten.vo.RakutenVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="rakuten")
@Controller
public class RakutenController {
	private static final Logger log = Logger.getLogger("jp.prj.araku.rakuten");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Autowired
	RakutenDAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@Autowired
	BatchDAO batchDao;
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		log.info("Welcome to rakuten file upload view");
		return "rakuten/fileView";
	}
	
	@RequestMapping(value = "/orderView")
	public String orderView() {
		log.info("Welcome to rakuten orderView");
		return "rakuten/orderInfo";
	}
	
	@RequestMapping(value="/translationView")
	public String translationView(Model model) {
		log.info("Welcome to rakuten translation view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_R);
		return "menu/translation";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		log.info("Welcome to rakuten region master view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_R);
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.info("Welcome to rakuten translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "rakuten/transResult";
	}
	
	@RequestMapping(value = "/itemsView")
	public String itemsView() {
		log.info("Welcome to rakuten items view");
		return "rakuten/itemsInfo";
	}
	
	@RequestMapping(value = "/rFileDownView")
	public String rFileDownView() {
		log.info("Welcome to rakuten file down view");
		return "rakuten/rakutenFileDown";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTrans")
	public ArrayList<TranslationVO> getTransInfo(TranslationVO transVO) {
		log.info("getTransInfo");
		return listDao.getTransInfo(transVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/modTrans")
	public ArrayList<TranslationVO> modTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.info("modTransInfo");
		return listDao.registerTransInfo(transVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/showRegionMaster")
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.info("showRegionMaster");
		return listDao.showRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modRegionMaster")
	public ArrayList<RegionMasterVO> modRegionMaster(@RequestBody ArrayList<RegionMasterVO> list) {
		log.info("modRegionMaster");
		return listDao.modRegionMaster(list);
	}
	
	@RequestMapping(value="/csvUpload", method=RequestMethod.POST)
	public String processCsvUpload(MultipartFile rakUpload, HttpServletRequest req) throws IOException {
		log.info("processCsvUpload");
		dao.insertRakutenInfo(rakUpload, fileEncoding, req);
		return "redirect:orderView";
	}
	
	@ResponseBody
	@RequestMapping(value="/showRList")
	public ArrayList<RakutenVO> getRakutenList(RakutenVO searchVO) {
		log.info("getRakutenList");
		return dao.getRakutenInfo(searchVO);
	}
	
	@RequestMapping(value="/delRakuten")
	public String deleteRakutenInfo(@RequestBody ArrayList<RakutenVO> vo) {
		log.info("deleteRakutenInfo");
		dao.deleteRakutenInfo(vo);
		return "redirect:showRList";
	}
	
	@ResponseBody
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<RakutenVO> targetList) {
		log.info("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<RakutenVO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.info("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
	
	@RequestMapping(value="/yaDown", method=RequestMethod.POST)
	public void processYamatoDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company
			, @RequestParam(value="isChecked") String chk_ex) {
		log.info("processYamatoDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		log.debug("isChecked : " + chk_ex);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.yamatoFormatDownload(response, seq_id_list, fileEncoding, delivery_company, chk_ex);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/saDown", method=RequestMethod.POST)
	public void processSagawaDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company
			, @RequestParam(value="isChecked") String chk_ex) {
		log.info("processSagawaDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		log.debug("isChecked : " + chk_ex);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.sagawaFormatDownload(response, seq_id_list, fileEncoding, delivery_company, chk_ex);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/modTransResult")
	public void modTransResult(TranslationResultVO vo) {
		log.info("modTransResult");
		listDao.modTransResult(vo);
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
	
	@RequestMapping(value="/delFileUpload", method=RequestMethod.POST)
	public String processErrFileUpload(MultipartFile delUpload, HttpServletRequest req) throws IOException {
		log.info("processCsvUpload");
		dao.checkRakutenInfo(delUpload, fileEncoding, req);
		return "redirect:orderView";
	}
	
	@ResponseBody
	@RequestMapping(value="/showExceptionMaster")
	public ArrayList<ExceptionMasterVO> showExceptionMaster(ExceptionMasterVO vo) {
		log.info("showExceptionMaster");
		return listDao.getExceptionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modExceptionMaster")
	public ArrayList<ExceptionMasterVO> processExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		log.info("processExceptionMaster");
		return listDao.registerExceptionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delExceptionMaster")
	public ArrayList<ExceptionMasterVO> deleteExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		log.info("deleteExceptionMaster");
		return listDao.deleteExceptionMaster(list);
	}
	
	@RequestMapping(value="/itemsUpload", method=RequestMethod.POST)
	public String processItemsUpload(MultipartFile itemsUpload, HttpServletRequest req) throws IOException {
		log.info("processItemsUpload");
		batchDao.insertItemsInfo(itemsUpload, fileEncoding, req);
		return "redirect:itemsView";
	}
	
	@ResponseBody
	@RequestMapping(value="/getItems")
	public ArrayList<ItemOutputVO> getItemsInfo(ItemOutputVO vo) {
		log.info("getItemsInfo");
		return batchDao.getItemsInfo(vo);
	}
	
	@RequestMapping(value="/itemDown")
	public void processItemDownload(
			HttpServletResponse resp
			, ItemOutputVO vo) {
		log.info("processItemDownload");
		
		try {
			batchDao.itemsCsvDownload(resp, fileEncoding, "item", vo);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/catDown")
	public void processItemCatDownload(
			HttpServletResponse resp
			, ItemOutputVO vo) {
		log.info("processItemCatDownload");
		
		try {
			batchDao.itemsCsvDownload(resp, fileEncoding, "itemCat", vo);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/selDown")
	public void processSelectDownload(
			HttpServletResponse resp
			, ItemOutputVO vo) {
		log.info("processSelectDownload");
		
		try {
			batchDao.itemsCsvDownload(resp, fileEncoding, "selectOut", vo);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/updateItem")
	public ArrayList<ItemOutputVO> updateItemsInfo(@RequestBody ArrayList<ItemOutputVO> vo) {
		log.info("updateItemsInfo");
		return batchDao.updateItems(vo);
	}
	
	@RequestMapping(value="/sagaUpload", method=RequestMethod.POST)
	public String processSagawaUpload(MultipartFile sagaUpload) throws IOException {
		log.info("processSagawaUpload");
		dao.rakutenSagawaUpdate(sagaUpload, fileEncoding);
		return "redirect:rFileDownView";
	}
	
	@RequestMapping(value="/yamaUpload", method=RequestMethod.POST)
	public String processYamatoUpload(MultipartFile yamaUpload) throws IOException {
		log.info("processYamatoUpload");
		dao.rakutenYamatoUpdate(yamaUpload, fileEncoding);
		return "redirect:rFileDownView";
	}
	
	@RequestMapping(value="/rFileDown", method=RequestMethod.POST)
	public void processRakutenFileDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst) {
		log.info("processRakutenFileDownload");
		
		log.debug("id list : " + id_lst);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.rCSVDownload(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/cpDown", method=RequestMethod.POST)
	public void processClickPostDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst) {
		log.info("processClickPostDownload");
		
		log.debug("id list : " + id_lst);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.clickPostFormatDownload(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
}
