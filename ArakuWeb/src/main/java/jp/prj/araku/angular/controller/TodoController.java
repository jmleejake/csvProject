package jp.prj.araku.angular.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.angular.dao.TodoDAO;
import jp.prj.araku.angular.vo.TodoVO;

@RequestMapping(value = "/araku/ang")
@Controller
public class TodoController {
	@Autowired
	TodoDAO dao;
	
	@RequestMapping(value = "")
	public String mainPage() {
		return "test/angular";
	}
	
	@ResponseBody
	@RequestMapping(value = "getTodo")
	public ArrayList<TodoVO> getTodoList() {
		return dao.getTodoList();
	}
	
	@ResponseBody
	@RequestMapping(value = "maniTodo", method = RequestMethod.POST)
	public ArrayList<TodoVO> manipulateTodo(@RequestBody TodoVO vo) {
		return dao.manipulateTodoList(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "delTodo", method = RequestMethod.POST)
	public ArrayList<TodoVO> deleteTodo(@RequestBody TodoVO vo) {
		return dao.deleteTodoList(vo.getSeq_id());
	}
}
