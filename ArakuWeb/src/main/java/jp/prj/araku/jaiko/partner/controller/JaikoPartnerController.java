package jp.prj.araku.jaiko.partner.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.prj.araku.jaiko.partner.dao.JaikoPartnerDAO;
import jp.prj.araku.jaiko.partner.vo.JaikoPartnerVO;
import jp.prj.araku.jaiko.partner.vo.JaikoWareTantou;

@RequestMapping(value = "/jaiko/partner")
@Controller
public class JaikoPartnerController {
	@Autowired
	JaikoPartnerDAO dao;
	
	@ResponseBody
	@RequestMapping(value = "/getInfo")
	public ArrayList<JaikoPartnerVO> getPartner(JaikoPartnerVO vo) {
		return dao.getPartner(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoPartnerVO> manipulatePartner(@RequestBody ArrayList<JaikoPartnerVO> list) {
		return dao.manipulatePartner(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ArrayList<JaikoPartnerVO> deleteJaikoPartner(@RequestBody ArrayList<JaikoPartnerVO> list) {
		return dao.deletePartner(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/tantou/getInfo")
	public ArrayList<JaikoWareTantou> getWareTantou(JaikoWareTantou vo) {
		return dao.getWareTantou(vo);
	}
	
	@ResponseBody
	@RequestMapping(value = "/tantou/manipulate", method = RequestMethod.POST)
	public ArrayList<JaikoWareTantou> manipulateWareTantou(@RequestBody ArrayList<JaikoWareTantou> list) {
		return dao.manipulateWareTantou(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "/tantou/delete", method = RequestMethod.POST)
	public ArrayList<JaikoWareTantou> deleteWareTantou(@RequestBody ArrayList<JaikoWareTantou> list) {
		return dao.deleteWareTantou(list);
	}

}
