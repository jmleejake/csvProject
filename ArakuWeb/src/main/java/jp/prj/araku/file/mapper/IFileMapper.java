package jp.prj.araku.file.mapper;

import jp.prj.araku.file.vo.RakutenVO;

public interface IFileMapper {
	/**
	 * @param vo 楽天CSVファイルVO
	 * @return 登録状態
	 */
	public int insertRakutenInfo(RakutenVO vo);
}
