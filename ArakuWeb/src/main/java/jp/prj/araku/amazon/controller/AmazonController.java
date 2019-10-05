package jp.prj.araku.amazon.controller;

import java.io.IOException;
import java.util.ArrayList;

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

import jp.prj.araku.amazon.dao.AmazonDAO;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.list.dao.ListDAO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.CommonUtil;

@RequestMapping(value="amazon")
@Controller
public class AmazonController {
	private static final Logger log = Logger.getLogger("jp.prj.araku.amazon");
	
	@Value("${FILE_ENCODING}")
	private String fileEncoding;
	
	@Value("${CLICKPOST_DOWN_PATH}")
	private String cpDownPath;
	
	@Autowired
	AmazonDAO dao;
	
	@Autowired
	ListDAO listDao;
	
	@RequestMapping(value = "/fileView")
	public String fileView() {
		log.info("Welcome to amazon file upload view");
		return "amazon/fileView";
	}
	
	@RequestMapping(value="/orderView")
	public String orderView() {
		log.info("Welcome to amazon order view");
		return "amazon/orderInfo";
	}
	
	@RequestMapping(value="/translationView")
	public String translationView(Model model) {
		log.info("Welcome to amazon translation view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_A);
		return "menu/translation";
	}
	
	@RequestMapping(value = "/regionView")
	public String regionView(Model model) {
		log.info("Welcome to amazon region master view");
		model.addAttribute("type", CommonUtil.TRANS_TARGET_A);
		return "menu/regionMaster";
	}
	
	@RequestMapping(value = "/resultView", method=RequestMethod.POST)
	public String resultView(String[] list, Model model ) {
		log.info("Welcome to amazon translation result view");
		ArrayList<String> idList = new ArrayList<>();
		for (String str : list) {
			log.debug(str);
			idList.add(str);
		}
		model.addAttribute("idList", idList);
		return "amazon/transResult";
	}
	
	@RequestMapping(value="/amaUpload", method=RequestMethod.POST)
	public String processTxtUpload(MultipartFile upload) throws IOException {
		log.info("processTxtUpload");
		dao.insertAmazonInfo(upload, fileEncoding);
		return "redirect:orderView";
	}
	
	@ResponseBody
	@RequestMapping(value="/showAList")
	public ArrayList<AmazonVO> getAmazonInfo(AmazonVO vo) {
		log.info("getAmazonInfo");
		return dao.getAmazonInfo(vo);
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
	@RequestMapping(value="/executeTrans")
	public ArrayList<String> executeTranslate(@RequestBody ArrayList<AmazonVO> targetList) {
		log.info("executeTranslate");
		return dao.executeTranslate(targetList);
	}
	
	@ResponseBody
	@RequestMapping(value="/getTransResult")
	public ArrayList<AmazonVO> getTransResult(@RequestParam(value="id_lst") String id_lst) {
		log.info("getTransResult");
		return dao.getTransResult(id_lst);
	}
	
	@RequestMapping(value="/delAmazon")
	public String deleteAmazonInfo(@RequestBody ArrayList<AmazonVO> list) {
		log.info("deleteAmazonInfo");
		dao.deleteAmazonInfo(list);
		return "redirect:showAList";
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
	
	@RequestMapping(value="/cpDown", method=RequestMethod.POST)
	@ResponseBody
	public String processClickPostDownload(
			@RequestParam(value="id_lst") String id_lst) {
		log.info("processClickPostDownload");
		log.debug("id list : " + id_lst);
		String ret = "";
		
		id_lst = id_lst.replace("[", "");
		id_lst = id_lst.replace("]", "");
		String[] seq_id_list = id_lst.split(",");
		try {
			// dao.clickPostFormatDownload(response, seq_id_list, fileEncoding);
			// 2019-10-03: 크리쿠포스트 csv다운로드시 목록에 40개 제한이 있어 잘라서 다운로드처리
			ret = dao.createClickpostCsvFile(cpDownPath, seq_id_list);
		} catch (IOException e) {
			log.error(e.toString());
		} catch (CsvDataTypeMismatchException e) {
			log.error(e.toString());
		} catch (CsvRequiredFieldEmptyException e) {
			log.error(e.toString());
		}
		return ret;
	}
}
