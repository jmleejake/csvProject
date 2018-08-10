package jp.prj.araku.file.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.file.mapper.IFileMapper;
import jp.prj.araku.file.vo.RakutenVO;

@Repository
public class FileDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = LoggerFactory.getLogger(FileDAO.class);
	
	public int insertRakutenInfo(RakutenVO vo) {
		log.info("insertRakutenInfo");
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		return mapper.insertRakutenInfo(vo);
	}
}
