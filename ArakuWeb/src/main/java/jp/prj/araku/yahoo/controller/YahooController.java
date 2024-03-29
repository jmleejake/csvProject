package jp.prj.araku.yahoo.controller;

import java.io.IOException;
import java.util.ArrayList;

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

import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.CommonUtil;
import jp.prj.araku.yahoo.dao.YahooDAO;
import jp.prj.araku.yahoo.vo.YahooVO;

@RequestMapping(value="/araku/yahoo")
@Controller
public class YahooController {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Value("${CLICKPOST_DOWN_PATH}")
	private String cpDownPath;
	
	@Autowired
	YahooDAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		log.debug("Welcome to yahoo file upload view");
		return "yahoo/fileView";
	}
	
	@RequestMapping(value = "/orderView")
	public String orderView() {
		log.debug("Welcome to yahoo orderView");
		return "yahoo/orderInfo";
	}
	
	@RequestMapping(value="/translationView")
	public String translationView(Model model) {
		log.debug("Welcome to yahoo translation view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_Y);
		return "menu/translation";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		log.debug("Welcome to yahoo region master view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_Y);
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.debug("Welcome to yahoo translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "yahoo/transResult";
	}
	
	@RequestMapping(value="/yhShouhin", method=RequestMethod.POST)
	public String processYahooShouhin(MultipartFile upload) throws IOException {
		dao.insertYahooInfoShouhin(upload, fileEncoding);
		return "redirect:orderView";
	}
	
	@RequestMapping(value="/yhChumon", method=RequestMethod.POST)
	public String processYahooChumon(MultipartFile upload) throws IOException {
		dao.insertYahooInfoChumon(upload, fileEncoding);
		return "redirect:orderView";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTrans")
	public ArrayList<TranslationVO> getTransInfo(TranslationVO transVO) {
		log.debug("getTransInfo");
		return listDao.getTransInfo(transVO);
	}
	
	@ResponseBody
	@RequestMapping(value="/modTrans")
	public ArrayList<TranslationVO> modTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.debug("modTransInfo");
		return listDao.registerTransInfo(transVO);
	}
	
	@RequestMapping(value="/delTrans")
	public String delTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.debug("delTransInfo");
		for (TranslationVO vo : transVO) {
			listDao.delTransInfo(vo.getSeq_id());
		}
		return "redirect:getTrans";
	}
	
	@ResponseBody
	@RequestMapping(value="/showRegionMaster")
	public ArrayList<RegionMasterVO> showRegionMaster(RegionMasterVO vo) {
		log.debug("showRegionMaster");
		return listDao.showRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modRegionMaster")
	public ArrayList<RegionMasterVO> modRegionMaster(@RequestBody ArrayList<RegionMasterVO> list) {
		log.debug("modRegionMaster");
		return listDao.modRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/showYList")
	public ArrayList<YahooVO> getYahooList(YahooVO searchVO) {
		log.debug("showYList");
		return dao.getYahooInfo(searchVO);
	}
	
	@RequestMapping(value="/delYahoo")
	public String deleteYahooInfo(@RequestBody ArrayList<YahooVO> vo) {
		log.debug("deleteYahooInfo");
		dao.deleteYahooInfo(vo);
		return "redirect:showYList";
	}
	
	@ResponseBody
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<YahooVO> targetList) {
		log.debug("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<YahooVO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.debug("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
	@RequestMapping(value="/modTransResult")
	public void modTransResult(TranslationResultVO vo) {
		log.debug("modTransResult");
		listDao.modTransResult(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/showExceptionMaster")
	public ArrayList<ExceptionMasterVO> showExceptionMaster(ExceptionMasterVO vo) {
		log.debug("showExceptionMaster");
		return listDao.getExceptionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modExceptionMaster")
	public ArrayList<ExceptionMasterVO> processExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		log.debug("processExceptionMaster");
		return listDao.registerExceptionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delExceptionMaster")
	public ArrayList<ExceptionMasterVO> deleteExceptionMaster(@RequestBody ArrayList<ExceptionMasterVO> list) {
		log.debug("deleteExceptionMaster");
		return listDao.deleteExceptionMaster(list);
	}
	
	@RequestMapping(value="/yaDown", method=RequestMethod.POST)
	public void processYamatoDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company) {
		log.debug("processYamatoDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		
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
	
	@RequestMapping(value="/saDown", method=RequestMethod.POST)
	public void processSagawaDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company) {
		log.debug("processSagawaDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.sagawaFormatDownload(response, seq_id_list, fileEncoding, delivery_company);
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
		log.debug("processClickPostDownload");
		log.debug("id list : " + id_lst);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			//ret = dao.createClickpostCsvFile(fileEncoding, cpDownPath, seq_id_list);
			dao.downloadClickpostCsvFile(response, fileEncoding, seq_id_list);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value="/showPrdMaster", method=RequestMethod.GET)
	@ResponseBody
	public ArrayList<TabletPrdVO> getPrdInfo(TabletPrdVO vo) {
		log.debug("getPrdInfo");
		return tabletPrdDao.getPrdInfo(vo);
	}
	
	@RequestMapping(value="/maniPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> manipulatePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		log.debug("manipulatePrdInfo");
		return tabletPrdDao.manipulatePrdInfo(list);
	}
	
	@RequestMapping(value="/delPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> deletePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		log.debug("manipulatePrdInfo");
		return tabletPrdDao.deletePrdInfo(list);
	}
	
	@RequestMapping(value="/showDealerMaster", method=RequestMethod.GET)
	@ResponseBody
	public ArrayList<DealerVO> getDealerInfo(DealerVO vo) {
		return tabletPrdDao.getDealerInfo(vo);
	}
	
	@RequestMapping(value="/maniDealerMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<DealerVO> manipulateDealerInfo(@RequestBody ArrayList<DealerVO> list) {
		return tabletPrdDao.manipulateDealerInfo(list);
	}
	
	@RequestMapping(value="/delDealerMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<DealerVO> deleteDealerInfo(@RequestBody ArrayList<DealerVO> list) {
		return tabletPrdDao.deleteDealerInfo(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/showExceptionRegionMaster")
	public ArrayList<ExceptionRegionMasterVO> showExceptionRegionMaster(ExceptionRegionMasterVO vo) {
		log.debug("showExceptionRegionMaster");
		return listDao.getExceptionRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modExceptionRegionMaster")
	public ArrayList<ExceptionRegionMasterVO> manipulateExceptionRegionMaster(@RequestBody ArrayList<ExceptionRegionMasterVO> list) {
		log.debug("manipulateExceptionRegionMaster");
		return listDao.manipulateExceptionRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delExceptionRegionMaster")
	public ArrayList<ExceptionRegionMasterVO> deleteExceptionRegionMaster(@RequestBody ArrayList<ExceptionRegionMasterVO> list) {
		log.debug("deleteExceptionRegionMaster");
		return listDao.deleteExceptionRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delWeekData")
	public ArrayList<String> deleteAllWeekAfterData() {
		return listDao.deleteAllWeekAfterData();
	}
	
	/**
	 * 置換データダウンロード
	 * */
	@RequestMapping(value = "transDown", method = RequestMethod.POST)
	public void translationCsvDownload(HttpServletResponse response) {
		try {
			listDao.translationCsvDownload(response, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	/**
	 * 置換データアップロード
	 * */
	@RequestMapping(value = "transUp", method =  RequestMethod.POST)
	public String translationCsvUpload(MultipartFile upload) {
		try {
			listDao.translationCsvUpload(upload, fileEncoding);
		}catch (IOException e) {
			log.error(e.toString());
		}
		return "redirect:translationView";
	}
	
}
