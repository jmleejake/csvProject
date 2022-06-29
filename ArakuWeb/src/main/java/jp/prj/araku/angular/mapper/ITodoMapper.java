package jp.prj.araku.angular.mapper;

import java.util.ArrayList;

import jp.prj.araku.angular.vo.TodoVO;

public interface ITodoMapper {
	int insertTodo(TodoVO vo);
	ArrayList<TodoVO> selectTodo();
	int updateTodo(TodoVO vo);
	int deleteTodo(String seq_id);
}
