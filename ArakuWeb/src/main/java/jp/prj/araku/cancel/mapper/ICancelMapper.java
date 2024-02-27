package jp.prj.araku.cancel.mapper;

import java.util.ArrayList;
import java.util.List;

import jp.prj.araku.cancel.vo.CancelDataVO;
import jp.prj.araku.cancel.vo.CancelVO;


public interface ICancelMapper {
	public int insertCancelInfo(CancelVO vo);
	public ArrayList<CancelVO> getCancelInfo(CancelVO vo);
	public int updateCancelInfo(CancelDataVO vo);
	public List<CancelVO> listupdateCancelInfo(List<CancelVO> cancelVO);
	public int deleteCancelInfo(String seq_id);
	
	public ArrayList<CancelVO> getCancelManage(CancelVO vo);
	public ArrayList<CancelVO> showCancelManage(String baggage_claim_no);
	
	public ArrayList<CancelVO> multiCancelManage(String baggage_claim_no);

}
