package jp.prj.araku.tanpin.mapper;

import java.util.ArrayList;
import java.util.List;

import jp.prj.araku.tanpin.vo.ExpireManageVo;
import jp.prj.araku.tanpin.vo.TanpinVO;

public interface ITanpinMapper {
	public int insertTanpinInfo(TanpinVO vo);
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo);
	public int updateTanpinInfo(TanpinVO vo);
	public int deleteTanpinInfo(String seq_id);
	
	/**
	 * 20230617
	 * 賞味期限管理S
	 * */
	public int insertExpireManage(ExpireManageVo vo);
	public List<ExpireManageVo> selectExpireManage(ExpireManageVo vo);
	public int updateExpireManage(ExpireManageVo vo);
	public int deleteExpireManage(String seq_id);
	/**
	 * 20230617
	 * 賞味期限管理E
	 * */
}
