package jp.prj.araku.list.mapper;

import java.util.ArrayList;

import jp.prj.araku.list.vo.EtcMasterVO;
import jp.prj.araku.list.vo.ExceptionMasterVO;
import jp.prj.araku.list.vo.ExceptionRegionMasterVO;
import jp.prj.araku.list.vo.OrderSumVO;
import jp.prj.araku.list.vo.PrdCdMasterVO;
import jp.prj.araku.list.vo.PrdTransVO;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.RegionMasterVO;
import jp.prj.araku.list.vo.TranslationErrorVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;

public interface IListMapper {
	public ArrayList<TranslationVO> getTransInfo(TranslationVO vo);
	public int addTransInfo(TranslationVO vo);
	public int modTransInfo(TranslationVO vo);
	public int delTransInfo(String seq_id);
	public int modRakutenInfo(RakutenSearchVO vo);
	public int addTransResult(TranslationResultVO vo);
	public ArrayList<TranslationResultVO> getTransResult(TranslationResultVO vo);
	public int modTransResult(TranslationResultVO vo);
	public ArrayList<RegionMasterVO> getRegionMaster(RegionMasterVO vo);
	public int modRegionMaster(RegionMasterVO vo);
	public int insertTranslationErr(TranslationErrorVO vo);
	public String getTranslationErr(TranslationErrorVO vo);
	public int deleteTranslationErr(TranslationErrorVO vo);
	
	/**
	 * 例外地域マスタ
	 * */
	public ArrayList<ExceptionMasterVO> getExceptionMaster(ExceptionMasterVO vo);
	public int insertExceptionMaster(ExceptionMasterVO vo);
	public int updateExceptionMaster(ExceptionMasterVO vo);
	public int deleteExceptionMaster(String seq_id);
	
	/**
	 * 例外地域マスタ
	 * */
	public ArrayList<ExceptionRegionMasterVO> getExceptionRegionMaster(ExceptionRegionMasterVO vo);
	public int insertExceptionRegionMaster(ExceptionRegionMasterVO vo);
	public int updateExceptionRegionMaster(ExceptionRegionMasterVO vo);
	public int deleteExceptionRegionMaster(String seq_id);
	
	/**
	 * 商品管理番号マスタ
	 * */
	public ArrayList<PrdCdMasterVO> getPrdCdMaster(PrdCdMasterVO vo);
	public ArrayList<PrdCdMasterVO> prdCdMasterExistChk(PrdCdMasterVO vo);
	public int insertPrdCdMaster(PrdCdMasterVO vo);
	public int updatePrdCdMaster(PrdCdMasterVO vo);
	public int deletePrdCdMaster(String seq_id);
	
	/**
	 * 商品中間マスタ
	 * */
	public ArrayList<PrdTransVO> getPrdTrans(PrdTransVO vo);
	public int insertPrdTrans(PrdTransVO vo);
	public int updatePrdTrans(PrdTransVO vo);
	public int deletePrdTrans(String seq_id);
	
	/**
	 * 総商品数
	 * */
	public ArrayList<OrderSumVO> getOrderSum(OrderSumVO vo);
	public int insertOrderSum(OrderSumVO vo);
	public int updateOrderSum(OrderSumVO vo);
	public int deleteOrderSum(String seq_id);
	
	/**
	 * その他マスタ
	 * */
	public ArrayList<EtcMasterVO> getEtc(EtcMasterVO vo);
	public int insertEtc(EtcMasterVO vo);
	public int updateEtc(EtcMasterVO vo);
	public int deleteEtc(String seq_id);
}