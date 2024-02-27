package jp.prj.araku.cancel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.tablet.dao.TabletPrdDAO;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.tanpin.vo.TanpinVO;
import jp.prj.araku.util.CommonUtil;
import jp.prj.araku.amazon.vo.AmazonVO;
import jp.prj.araku.cancel.dao.CancelDAO;
import jp.prj.araku.cancel.vo.CancelDataVO;
import jp.prj.araku.cancel.vo.CancelVO;
import org.springframework.web.bind.annotation.*;
import java.util.List;




@RequestMapping(value="/araku/cancel")
@Controller
public class CancelController {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Autowired
	private CancelDAO dao;
	
	@RequestMapping(value="/cancelMng")
	public String showCancelManage() {
		return "cancel/cancelMng";
	}
	
	@RequestMapping(value="/showCancelManage")
	@ResponseBody
	public ArrayList<CancelVO> showCancelManage(
			@RequestParam(value="bgc_no", defaultValue="") String baggage_claim_no) {
		CancelVO vo = new CancelVO();
		vo.setBaggage_claim_no(baggage_claim_no);
		log.debug("baggage_claim_no:" ,baggage_claim_no);
		return dao.getCancelInfo(vo);
	}

	@RequestMapping(value="/data")
	@ResponseBody
    public ResponseEntity<List<CancelVO>> getData(@RequestParam(value="bgc_no", defaultValue="") String baggage_claim_no) {
		List<CancelVO> dataItems = fetchDataFromDatabase(baggage_claim_no); 
        return ResponseEntity.ok(dataItems);
    }

//    // 데이터베이스에서 데이터를 가져오는 메서드를 가정
//    private List<CancelVO> fetchDataFromDatabase(@RequestParam(value="bgc_no", defaultValue="") String baggage_claim_no) {
//		return dao.multiCancelManage(baggage_claim_no);
//    }
    
//	@ResponseBody
//	@RequestMapping(value = "/jaikoHanei", method = RequestMethod.POST)
//	public HashMap<String, Object> jaikoHanei(@RequestBody List<CancelDataVO> targetList) {
//		//log.debug("cancelController  jaikoHanei");
//		return dao.updateCancelInfo(targetList);
//	}

	private List<CancelVO> fetchDataFromDatabase(String baggage_claim_no) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@RequestMapping(value = "/jaikoHanei", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> jaikoHanei(@RequestBody ArrayList<CancelDataVO> cancelList) {
	    HashMap<String, String> response = new HashMap<>();
	    try {
	        // 여기서 cancelList를 처리
			Iterator<CancelDataVO> iterator = cancelList.iterator();
		    log.debug("Welcome to jaikoHanei in CancelController");
			while (iterator.hasNext()) {
			    CancelDataVO item = iterator.next();
			    System.out.println(item.getOrder_no());
			    System.out.println(item.getOrder_gbn());
			    System.out.println(item.getJan_cd());
			    System.out.println(item.getBefore_trans());
			    
			    System.out.println(item.getAfter_trans());
			    System.out.println(item.getPrd_cnt());
			    // 필요한 경우 iterator.remove();를 호출하여 현재 항목을 제거할 수 있습니다.
			}
			
	        dao.updateCancelInfo(cancelList);
	        
	        // 성공적인 처리 후 응답 반환
	        response.put("retCd", "S");
	        response.put("retMsg", "反映完了しました。");
	        return ResponseEntity.ok(response);
	        
	    } catch (Exception e) {
	        // 예외 발생 시 처리 로직
	        e.printStackTrace(); // 서버 로그에 예외 스택 트레이스 출력 (선택적)
	        
	        // 클라이언트에 반환할 오류 메시지와 상태 코드 설정
	        response.put("retCd", "E");
	        response.put("retMsg", "データ反映中にエラーが発生しました。"); // "데이터 반영 중 에러가 발생했습니다."
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
//    @PostMapping("/sendGridData")
//    public ResponseEntity<?> receiveGridData(@RequestBody List<CancelVO> gridData) {
//        // gridData를 DAO 객체를 통해 데이터베이스에 저장하거나 필요한 처리 수행
//        // 예: gridDataDao.saveAll(gridData);
//    	//dao.saveAll(gridData);
//        dao.updateCancelInfo(gridData);
//        return ResponseEntity.ok().body("Grid データ処理完了");
//    }
}
