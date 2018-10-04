package jp.prj.araku.file.mapper;

import java.util.ArrayList;

import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.rakuten.vo.RakutenVO;

public interface IFileMapper {
	/**
	 * @param vo 楽天CSVファイルVO
	 * @return 登録状態
	 */
	public ArrayList<RCSVDownVO> getRCSVDownList(RCSVDownVO vo);
	public ArrayList<RakutenVO> getYUCSVDownList(RakutenVO vo);
}
