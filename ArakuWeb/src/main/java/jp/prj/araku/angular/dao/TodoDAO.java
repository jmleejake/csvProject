package jp.prj.araku.angular.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.angular.mapper.ITodoMapper;
import jp.prj.araku.angular.vo.TodoVO;

@Repository
public class TodoDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<TodoVO> getTodoList() {
		ITodoMapper mapper = sqlSession.getMapper(ITodoMapper.class);
		return mapper.selectTodo();
	}
	
	public ArrayList<TodoVO> manipulateTodoList(TodoVO vo) {
		ITodoMapper mapper = sqlSession.getMapper(ITodoMapper.class);
		if(null == vo.getSeq_id()) {
			mapper.insertTodo(vo);
			System.out.println(String.format("지금 insert한 seq_id? = %s", vo.getSeq_id()));
		}else {
			mapper.updateTodo(vo);
			System.out.println(String.format("지금 update한 seq_id? = %s", vo.getSeq_id()));
		}
		return getTodoList();
	}
	
	public ArrayList<TodoVO> deleteTodoList(String seq_id) {
		ITodoMapper mapper = sqlSession.getMapper(ITodoMapper.class);
		mapper.deleteTodo(seq_id);
		return getTodoList();
	}
}
