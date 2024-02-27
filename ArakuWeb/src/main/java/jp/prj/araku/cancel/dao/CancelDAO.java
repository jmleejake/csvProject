package jp.prj.araku.cancel.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.OrderSumVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.TabletPrdVO;
import jp.prj.araku.tanpin.mapper.ITanpinMapper;
import jp.prj.araku.tanpin.vo.TanpinVO;
import jp.prj.araku.cancel.mapper.ICancelMapper;
import jp.prj.araku.cancel.vo.CancelDataVO;
import jp.prj.araku.cancel.vo.CancelVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class CancelDAO {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<CancelVO> getCancelInfo(CancelVO vo) {
		ICancelMapper mapper = sqlSession.getMapper(ICancelMapper.class);
		log.debug("Welcome to getCancelInfo");
		ArrayList<CancelVO> ret = mapper.getCancelInfo(vo);
		for(CancelVO tmp : ret) {
			if(null != tmp.getBaggage_claim_no()) {
				tmp.setBaggage_claim_no(tmp.getBaggage_claim_no());
			}
		}
		return ret;
	}
	
	public ArrayList<CancelVO> getCancelManage(CancelVO vo) {
		ICancelMapper mapper = sqlSession.getMapper(ICancelMapper.class);
		log.debug("Welcome to getCancelManage");
		return mapper.getCancelManage(vo);
	}
	
	public ArrayList<CancelVO> showCancelManage(String baggage_claim_no) {
		ICancelMapper mapper = sqlSession.getMapper(ICancelMapper.class);

		return mapper.showCancelManage(baggage_claim_no);
	}
	
	public ArrayList<CancelVO> multiCancelManage(String baggage_claim_no) {
		ICancelMapper mapper = sqlSession.getMapper(ICancelMapper.class);

		return mapper.multiCancelManage(baggage_claim_no);
	}
	
    // 데이터베이스 접근 및 처리 로직 구현
    public void saveAll(List<CancelVO> gridData) {
        // 여기서 gridData를 데이터베이스에 저장하거나 처리하는 로직 구현
    }
    
	public HashMap<String, Object> updateCancelInfo(ArrayList<CancelDataVO> canceldtVO) {
		ICancelMapper cancelmapper = sqlSession.getMapper(ICancelMapper.class);
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		
		HashMap<String, Object> ret = new HashMap<>();
		
		if(canceldtVO.size() < 1) {
			ret.put("retCd", "ERR");
			ret.put("retMsg", "キャンセル関連情報がありません。");
			return ret;
		}
		
		Iterator<CancelDataVO> iterator = canceldtVO.iterator();
	    log.debug("Welcome to updateCancelInfo in CancelDAO");
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

		for(CancelDataVO cancel : canceldtVO) {
			if (cancel.getJan_cd()!= null && !cancel.getJan_cd().isEmpty()) {
				JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
				invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				invenVO.setJan_cd(cancel.getJan_cd());
				ArrayList<JaikoPrdInventoryVO> invenList = invenMapper.getJaikoPrdInventory(invenVO);
				if(invenList.size() > 0) {
					for(JaikoPrdInventoryVO inven : invenList) {
						int nowCnt = Integer.parseInt(inven.getNow_prd_cnt());
						int plus = Integer.parseInt(cancel.getPrd_cnt());
						
						JaikoPrdInventoryVO invenUp = new JaikoPrdInventoryVO();
						invenUp.setJan_cd(cancel.getJan_cd());
						invenUp.setSearch_type("wareOut");
						invenUp.setNow_prd_cnt(String.valueOf(nowCnt+plus));
						invenMapper.updateJaikoPrdInventory(invenUp);
					}
					ret.put("retCd", "S");
				}else {
					ret.put("retCd", "ERR");
					ret.put("retMsg", "JANコード：" + cancel.getJan_cd() +"在庫管理を確認してください。");
					return ret;
				}
			}
		}
		return ret;
		
	}
}
