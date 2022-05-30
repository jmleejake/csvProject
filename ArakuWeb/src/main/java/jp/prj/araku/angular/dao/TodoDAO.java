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
		return getTodoList();
	}
	
	public ArrayList<TodoVO> deleteTodoList(String seq_id) {
		ITodoMapper mapper = sqlSession.getMapper(ITodoMapper.class);
		mapper.deleteTodo(seq_id);
		return getTodoList();
	}
}
