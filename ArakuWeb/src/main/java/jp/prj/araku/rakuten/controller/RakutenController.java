package jp.prj.araku.rakuten.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import jp.prj.araku.batch.dao.BatchDAO;
import jp.prj.araku.batch.vo.ItemOutputVO;
import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.EtcMasterVO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.House3MasterVO;
import jp.prj.araku.list.vo.OrderSumVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.PrdTransVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.SubTranslationVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.rakuten.dao.RakutenDAO;
import jp.prj.araku.rakuten.vo.RakutenVO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="/araku/rakuten")
@Controller
public class RakutenController {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Value("${CLICKPOST_DOWN_PATH}")
	private String cpDownPath;
	
	@Value("${DUPLICATE_DOWN_PATH}")
	private String duplDownPath;
	
	@Autowired
	RakutenDAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@Autowired
	BatchDAO batchDao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
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
	
	@RequestMapping(value="/delTrans")
	public String delTransInfo(@RequestBody ArrayList<TranslationVO> transVO) {
		log.info("delTransInfo");
		for (TranslationVO vo : transVO) {
			listDao.delTransInfo(vo.getSeq_id());
		}
		return "redirect:getTrans";
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
	public String processCsvUpload(
			MultipartFile rakUpload
			, HttpServletRequest req
			, @RequestParam(value="type", defaultValue="NORMAL") String type) throws IOException {
		log.info("processCsvUpload");
		dao.insertRakutenInfo(rakUpload, fileEncoding, req, duplDownPath, type);
		String ret = "redirect:orderView";
		if("SALES".equals(type)) {
			ret = "redirect:salesView";
		}
		return ret;
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
			, @RequestParam(value="storage") String storage
			, @RequestParam(value="company") String delivery_company
			, @RequestParam(value="downType") String downType
			, @RequestParam(value="isChecked") String chk_ex) {
		log.info("processYamatoDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		log.debug("isChecked : " + chk_ex);
		log.debug("downType : " + downType);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.yamatoFormatDownload(response, seq_id_list, fileEncoding, delivery_company, chk_ex, downType, storage);
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
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="delivery_company") String delivery_company) {
		log.info("processRakutenFileDownload");
		
		log.debug("id list : " + id_lst);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.rCSVDownload(response, seq_id_list, fileEncoding, delivery_company);
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
			// 2019-10-03: 크리쿠포스트 csv다운로드시 목록에 40개 제한이 있어 잘라서 다운로드처리
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
		log.info("getPrdInfo");
		return tabletPrdDao.getPrdInfo(vo);
	}
	
	@RequestMapping(value="/maniPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> manipulatePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		log.info("manipulatePrdInfo");
		return tabletPrdDao.manipulatePrdInfo(list);
	}
	
	@RequestMapping(value="/delPrdMaster", method=RequestMethod.POST)
	@ResponseBody
	public ArrayList<TabletPrdVO> deletePrdInfo(@RequestBody ArrayList<TabletPrdVO> list) {
		log.info("manipulatePrdInfo");
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
	
	@RequestMapping(value = "/tabChumonView")
	public String tabChumonView(Model model) {
		log.info("Welcome to rakuten tablet chumon view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_R);
		return "menu/tabletOrder";
	}
	
	@RequestMapping(value="/sagaUpload2006", method=RequestMethod.POST)
	public String processSagawaUpdate2006(MultipartFile sagaUpload2006) throws IOException {
		log.info("rakuten > processSagawaUpdate2006");
		listDao.processSagawaUpdate2006(sagaUpload2006, fileEncoding, "rak");
		return "redirect:rFileDownView";
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
		log.info("showExceptionRegionMaster");
		return listDao.getExceptionRegionMaster(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/modExceptionRegionMaster")
	public ArrayList<ExceptionRegionMasterVO> manipulateExceptionRegionMaster(@RequestBody ArrayList<ExceptionRegionMasterVO> list) {
		log.info("manipulateExceptionRegionMaster");
		return listDao.manipulateExceptionRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delExceptionRegionMaster")
	public ArrayList<ExceptionRegionMasterVO> deleteExceptionRegionMaster(@RequestBody ArrayList<ExceptionRegionMasterVO> list) {
		log.info("deleteExceptionRegionMaster");
		return listDao.deleteExceptionRegionMaster(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/delWeekData")
	public ArrayList<String> deleteAllWeekAfterData() {
		return listDao.deleteAllWeekAfterData();
	}
	
	@ResponseBody
	@RequestMapping(value="/delRakutenFrozen", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public String deleteRakutenFrozenInfo() {
		String ret = "";
		log.info("deleteRakutenFrozenInfo");
		ret = dao.deleteRakutenFrozenInfo(null) + "件 削除完了しました。";
		return ret;
	}
	
	@RequestMapping(value = "/salesView")
	public String salesView() {
		log.info("Welcome to rakuten sales view");
		return "rakuten/salesView";
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
	
	@ResponseBody
	@RequestMapping(value = "showAllRList")
	public ArrayList<RakutenVO> getAllRakutenInfo() {
		return dao.getAllData();
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
	
	/**
	 * 商品中間マスタ
	 * */
	@RequestMapping(value = "/prdTransView")
	public String showPrdTransView(Model model) {
		model.addAttribute("type", CommonUtil.TRANS_TARGET_R);
		return "menu/prdTrans";
	}
	
	@ResponseBody
	@RequestMapping(value="/getPrdTrans")
	public ArrayList<PrdTransVO> getPrdTransInfo(PrdTransVO vo) {
		vo.setTrans_target_type(CommonUtil.TRANS_TARGET_R);
		return listDao.getPrdTransInfo(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/modPrdTrans")
	public ArrayList<PrdTransVO> manipulatePrdTransInfo(@RequestBody ArrayList<PrdTransVO> list) {
		return listDao.manipulatePrdTrans(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delPrdTrans")
	public ArrayList<PrdTransVO> deletePrdTransInfo(@RequestBody ArrayList<PrdTransVO> list) {
		return listDao.deletePrdTrans(list);
	}
	
	/**
	 * 総商品数
	 * */
	@ResponseBody
	@RequestMapping(value = "/executeOrderSum", method = RequestMethod.POST)
	public ArrayList<OrderSumVO> executeOrderSum(@RequestParam(value = "sumVal", defaultValue = "sum") String sumType) {
		return listDao.executeOrderSum(CommonUtil.TRANS_TARGET_R, sumType);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delOrderSum", method = RequestMethod.POST)
	public ArrayList<OrderSumVO> deleteOrderSum(@RequestBody ArrayList<OrderSumVO> list) {
		return listDao.deleteOrderSum(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getOrderSum")
	public ArrayList<OrderSumVO> getOrderSum() {
		OrderSumVO vo = new OrderSumVO();
		vo.setTarget_type(CommonUtil.TRANS_TARGET_R);
		return listDao.getOrderSum(vo);
	}
	
	@RequestMapping(value="sumDown", method = RequestMethod.POST)
	public void orderSumDownload(HttpServletResponse response, @RequestParam String gbn) {
		try {
			listDao.sumDownload(response, fileEncoding, CommonUtil.TRANS_TARGET_R, gbn);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/executeHanei", method = RequestMethod.POST)
	public HashMap<String, Object> executeHanei() {
		return listDao.executeHanei(CommonUtil.TRANS_TARGET_R);
	}
	
	/**
	 * その他マスタ
	 * */
	@ResponseBody
	@RequestMapping(value = "/getEtc")
	public ArrayList<EtcMasterVO> getEtc(EtcMasterVO vo) {
		vo.setTarget_type(CommonUtil.TRANS_TARGET_R);
		return listDao.getEtc(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/modEtc")
	public ArrayList<EtcMasterVO> manipulateEtc(@RequestBody ArrayList<EtcMasterVO> list) {
		return listDao.manipulateEtc(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delEtc")
	public ArrayList<EtcMasterVO> deleteEtc(@RequestBody ArrayList<EtcMasterVO> list) {
		return listDao.deleteEtc(list);
	}
	
	/**
	 * 
	 * */
	@RequestMapping(value="/ecoDown", method=RequestMethod.POST)
	public void downloadEcoFile(
			HttpServletResponse response
			,@RequestParam(value="company") String delivery_company
			,@RequestParam(value="id_lst") String id_lst) {
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.ecoFormatDownload(response, seq_id_list, fileEncoding, delivery_company);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
	}
	
	/**
	 * 置換サーブ情報
	 * */
	@ResponseBody
	@RequestMapping(value = "getSubTrans")
	public ArrayList<SubTranslationVO> getSubTransInfo(SubTranslationVO vo) {
		return listDao.getSubTransInfo(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "maniSubTrans", method = RequestMethod.POST)
	public ArrayList<SubTranslationVO> manipulateSubTransInfo(@RequestBody ArrayList<SubTranslationVO> list) {
		return listDao.manipulateSubTransInfo(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "delSubTrans", method = RequestMethod.POST)
	public ArrayList<SubTranslationVO> deleteSubTransInfo(@RequestBody ArrayList<SubTranslationVO> list) {
		return listDao.deleteSubTransInfo(list);
	}
	
	/**
	 * 第三倉庫マスタ
	 * */
	@ResponseBody
	@RequestMapping(value = "getHouse3")
	public ArrayList<House3MasterVO> getHouse3Master(House3MasterVO vo) {
		return listDao.getHouse3Master(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "modHouse3", method = RequestMethod.POST)
	public ArrayList<House3MasterVO> manipulateHouse3Master(@RequestBody ArrayList<House3MasterVO> list) {
		return listDao.manipulateHouse3Master(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "delHouse3", method = RequestMethod.POST)
	public ArrayList<House3MasterVO> deleteHouse3Master(@RequestBody ArrayList<House3MasterVO> list) {
		return listDao.deleteHouse3Master(list);
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
