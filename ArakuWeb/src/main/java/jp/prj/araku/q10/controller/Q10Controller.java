package jp.prj.araku.q10.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import jp.prj.araku.q10.dao.Q10DAO;
import jp.prj.araku.q10.vo.Q10VO;
import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="/araku/q10")
@Controller
public class Q10Controller {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Value("${Q10_FILE_ENCODING}")
	private String upFileEncoding;
	
	@Value("${FILE_ENCODING}")
	private String downFileEncoding;
	
	@Value("${CLICKPOST_DOWN_PATH}")
	private String cpDownPath;
	
	@Autowired
	Q10DAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@Autowired
	TabletPrdDAO tabletPrdDao;
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		log.debug("Welcome to q10 file upload view");
		return "q10/fileView";
	}
	
	@RequestMapping(value="/translationView")
	public String translationView(Model model) {
		log.debug("Welcome to q10 translation view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_Q);
		return "menu/translation";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		log.debug("Welcome to q10 region master view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_Q);
		return "menu/regionMaster";
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
	
	@RequestMapping(value="/qUpload", method=RequestMethod.POST)
	public String processCsvUpload(
			MultipartFile upload
			, @RequestParam(value="type", defaultValue="NORMAL") String type) throws IOException {
		log.debug("processCsvUpload");
		String ret = "redirect:orderView";
		dao.insertQ10Info(upload, upFileEncoding, type);
		if("SALES".equals(type)) {
			ret = "redirect:salesView";
		}
		return ret;
	}
	
	@RequestMapping(value="/orderView")
	public String orderView() {
		log.debug("Welcome to q10 order view");
		return "q10/orderInfo";
	}
	
	@ResponseBody
	@RequestMapping(value="/showQList")
	public ArrayList<Q10VO> getQ10Info(Q10VO vo) {
		log.debug("getQ10Info");
		return dao.getQ10Info(vo);
	}
	
	@ResponseBody
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<Q10VO> targetList) {
		log.debug("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.debug("Welcome to q10 translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "q10/transResult";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<Q10VO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.debug("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
	@RequestMapping(value="/modTransResult")
	public void modTransResult(TranslationResultVO vo) {
		log.debug("modTransResult");
		listDao.modTransResult(vo);
	}
	
	@RequestMapping(value="/delQ10")
	public String deleteQ10Info(@RequestBody ArrayList<Q10VO> list) {
		log.debug("deleteQ10Info");
		dao.deleteQ10Info(list);
		return "redirect:showQList";
	}
	
	@RequestMapping(value="/yaDown", method=RequestMethod.POST)
	public void processYamatoDownload(
			HttpServletResponse response
			, @RequestParam(value="id_lst") String id_lst
			, @RequestParam(value="storage") String storage
			, @RequestParam(value="company") String delivery_company) {
		log.debug("processYamatoDownload");
		
		log.debug("id list : " + id_lst);
		log.debug("delivery company : " + delivery_company);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
				dao.yamatoFormatDownload(response, seq_id_list, downFileEncoding, delivery_company, storage);
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
				dao.sagawaFormatDownload(response, seq_id_list, downFileEncoding, delivery_company);
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
			,@RequestParam(value="id_lst") String id_lst) {
		log.debug("processClickPostDownload");
		log.debug("id list : " + id_lst);
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		
		try {
			// 2019-10-03: 크리쿠포스트 csv다운로드시 목록에 40개 제한이 있어 잘라서 다운로드처리
			//ret = dao.createClickpostCsvFile(downFileEncoding, cpDownPath,seq_id_list);
			dao.downloadClickpostCsvFile(response, downFileEncoding, seq_id_list);
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
	
	@RequestMapping(value = "/salesView")
	public String salesView() {
		log.debug("Welcome to q10 sales view");
		return "q10/salesView";
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
			dao.uriageDownload(response, downFileEncoding);
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
		model.addAttribute("type", CommonUtil.TRANS_TARGET_Q);
		return "menu/prdTrans";
	}
	
	@ResponseBody
	@RequestMapping(value="/getPrdTrans")
	public ArrayList<PrdTransVO> getPrdTransInfo(PrdTransVO vo) {
		vo.setTrans_target_type(CommonUtil.TRANS_TARGET_Q);
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
		return listDao.executeOrderSum(CommonUtil.TRANS_TARGET_Q, sumType);
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
		vo.setTarget_type(CommonUtil.TRANS_TARGET_Q);
		return listDao.getOrderSum(vo);
	}
	
	@RequestMapping(value="sumDown", method = RequestMethod.POST)
	public void orderSumDownload(HttpServletResponse response) {
		try {
			listDao.sumDownload(response, downFileEncoding, CommonUtil.TRANS_TARGET_Q);
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
		return listDao.executeHanei(CommonUtil.TRANS_TARGET_Q);
	}
	
	/**
	 * その他マスタ
	 * */
	@ResponseBody
	@RequestMapping(value = "/getEtc")
	public ArrayList<EtcMasterVO> getEtc(EtcMasterVO vo) {
		vo.setTarget_type(CommonUtil.TRANS_TARGET_Q);
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
	 * Q10更新ファイル
	 * */
	@RequestMapping(value="/yamaUpload", method=RequestMethod.POST)
	public String processYamatoUpload(MultipartFile yamaUpload) throws IOException {
		dao.q10YamatoUpdate(yamaUpload, upFileEncoding);
		return "redirect:qFileDownView";
	}
	
	@RequestMapping(value = "/qFileDownView")
	public String showQFileDownView() {
		return "/q10/q10FileDown";
	}
	
	
	@RequestMapping(value = "/qFileDown")
	public void downloadQ10File(
			HttpServletResponse response,
			@RequestParam(value="id_lst") String id_lst) {
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			dao.downloadQ10File(response, seq_id_list, downFileEncoding);
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
	
}
