package jp.prj.araku.amazon.controller;

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

import jp.prj.araku.amazon.dao.AmazonDAO;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="/araku/amazon")
@Controller
public class AmazonController {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Value("${CLICKPOST_DOWN_PATH}")
	private String cpDownPath;
	
	@Autowired
	AmazonDAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		log.debug("Welcome to amazon file upload view");
		return "amazon/fileView";
	}
	
	@RequestMapping(value="/orderView")
	public String orderView() {
		log.debug("Welcome to amazon order view");
		return "amazon/orderInfo";
	}
	
	@RequestMapping(value="/translationView")
	public String translationView(Model model) {
		log.debug("Welcome to amazon translation view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_A);
		return "menu/translation";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		log.debug("Welcome to amazon region master view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_A);
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.debug("Welcome to amazon translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "amazon/transResult";
	}
	
	@RequestMapping(value="/amaUpload", method=RequestMethod.POST)
	public String processTxtUpload(
			MultipartFile upload
			, @RequestParam(value="type", defaultValue="NORMAL") String type) throws IOException {
		log.debug("processTxtUpload");
		String ret = "redirect:orderView";
		dao.insertAmazonInfo(upload, fileEncoding, type);
		if("SALES".equals(type)) {
			ret = "redirect:salesView";
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value="/showAList")
	public ArrayList<AmazonVO> getAmazonInfo(AmazonVO vo) {
		log.debug("getAmazonInfo");
		return dao.getAmazonInfo(vo);
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
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<AmazonVO> targetList) {
		log.debug("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<AmazonVO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.debug("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
	@RequestMapping(value="/delAmazon")
	public String deleteAmazonInfo(@RequestBody ArrayList<AmazonVO> list) {
		log.debug("deleteAmazonInfo");
		dao.deleteAmazonInfo(list);
		return "redirect:showAList";
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
	
	@RequestMapping(value="/yaDown", method=RequestMethod.POST)
	public void processYamatoDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="company") String delivery_company
			, @RequestParam(value="isChecked") String chk_ex) {
		log.debug("processYamatoDownload");
		
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
		log.debug("processSagawaDownload");
		
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
	
	@RequestMapping(value="/cpDown", method=RequestMethod.POST)
	@ResponseBody
	public String processClickPostDownload(
			@RequestParam(value="id_lst") String id_lst) {
		log.debug("processClickPostDownload");
		log.debug("id list : " + id_lst);
		String ret = "";
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			// dao.clickPostFormatDownload(response, seq_id_list, fileEncoding);
			// 2019-10-03: 크리쿠포스트 csv다운로드시 목록에 40개 제한이 있어 잘라서 다운로드처리
			ret = dao.createClickpostCsvFile(fileEncoding, cpDownPath, seq_id_list);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
		return ret;
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
	
	@RequestMapping(value="/yamaUpload", method=RequestMethod.POST)
	public String processYamatoUpload(MultipartFile yamaUpload) throws IOException {
		log.debug("processYamatoUpload");
		dao.amazonYamatoUpdate(yamaUpload, fileEncoding);
		return "redirect:aFileDownView";
	}
	
	@RequestMapping(value = "/aFileDownView")
	public String amazonFileDownView() {
		log.debug("Welcome to amazon file download view");
		return "amazon/amazonFileDown";
	}
	
	@RequestMapping(value="/aFileDown", method=RequestMethod.POST)
	public void downloadAmazonFile(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.downloadAmazonFile(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@RequestMapping(value = "/tabChumonView")
	public String tabChumonView(Model model) {
		log.debug("Welcome to amazon tablet chumon view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_A);
		return "menu/tabletOrder";
	}
	
	@RequestMapping(value="/sagaUpload2006", method=RequestMethod.POST)
	public String processSagawaUpdate2006(MultipartFile sagaUpload2006) throws IOException {
		log.debug("amazon > processSagawaUpdate2006");
		listDao.processSagawaUpdate2006(sagaUpload2006, fileEncoding, "ama");
		return "redirect:aFileDownView";
	}
	
	@RequestMapping(value="/globalSaDown", method=RequestMethod.POST)
	public void downloadGlobalSagawaFile(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.downloadGlobalSagawa(response, seq_id_list, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
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
	
	@RequestMapping(value = "/salesView")
	public String salesView() {
		log.debug("Welcome to amazon sales view");
		return "amazon/salesView";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrdCdMaster")
	public ArrayList<PrdCdMasterVO> getPrdCdMaster(PrdCdMasterVO vo) {
		return listDao.getPrdCdMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "maniPrdCdMaster", method = RequestMethod.POST)
	public ArrayList<PrdCdMasterVO> manipulatePrdCdMaster(@RequestBody ArrayList<PrdCdMasterVO> list) {
		return listDao.manipulatePrdCdMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "delPrdCdMaster", method = RequestMethod.POST)
	public ArrayList<PrdCdMasterVO> deletePrdCdMaster(@RequestBody ArrayList<PrdCdMasterVO> list) {
		return listDao.deletePrdCdMaster(list);
	}
	
	@RequestMapping(value="uriageDown", method = RequestMethod.POST)
	public void uriageDownload(HttpServletResponse response) {
		try {
			dao.uriageDownload(response, fileEncoding);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
}
