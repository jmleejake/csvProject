package jp.prj.araku.tanpin.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import jp.prj.araku.tanpin.vo.ExpireManageVo;
import jp.prj.araku.tanpin.vo.AllmartManageVo;
import jp.prj.araku.tanpin.vo.TanpinVO;

public interface ITanpinMapper {
	public int insertTanpinInfo(TanpinVO vo);
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo);
	public ArrayList<TanpinVO> getTanpinSubInfo(TanpinVO vo);
	public int updateTanpinInfo(TanpinVO vo);
	public int deleteTanpinInfo(String seq_id);
	
	/**
	 * 20240627
	 * Allmart価格管理S
	 * */
	public int insertAllmartManage(AllmartManageVo vo);
	/**
	public ArrayList<AllmartManageVo> getAllmartManage(String[] product_id_list);
		 * */
	public List<AllmartManageVo> selectAllmartManage(AllmartManageVo vo);
	public ArrayList<AllmartManageVo> getAllmartManageDL(AllmartManageVo vo);
	public ArrayList<AllmartManageVo> getAllmartManageT(AllmartManageVo vo);
	
	public int updateAllmartManage(AllmartManageVo vo);
	public void deleteAllmartManage(Map<String, Object> params);
	/**
	List<AllmartManageVo> getAllmartManage(@Param("product_id_list") List<String> product_id_list);
	 * */
	/**
	 * 20230617
	 * 賞味期限管理S
	 * */
	public int insertExpireManage(ExpireManageVo vo);
	public ArrayList<ExpireManageVo> getExpireManage(ExpireManageVo vo);
	public List<ExpireManageVo> selectExpireManage(ExpireManageVo vo);
	public int updateExpireManage(ExpireManageVo vo);
	public int deleteExpireManage(String seq_id);
	/**
	 * 20230617
	 * 賞味期限管理E
	 * */
}
