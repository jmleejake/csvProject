package com.km.adm.controller.rest;

import com.km.adm.common.CommonResponse;
import com.km.adm.config.ExcelHeader;
import com.km.adm.dao.vo.UserAuthVo;
import com.km.adm.dao.vo.UserGroupVo;
import com.km.adm.dao.vo.UserVo;
import com.km.adm.model.UserModel;
import com.km.adm.service.ExcelService;
import com.km.adm.service.UserService;
import com.km.adm.util.ResponseEntityUtil;
import com.km.adm.util.SessionUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	private final UserService userService;
	private final ExcelService excelService;

	/**로그인*/
	@PostMapping(value ="/login-exec")
	public ResponseEntity<CommonResponse> login(HttpServletRequest request
		, @RequestParam String userId
		, @RequestParam String password
		, @RequestParam String userTypeCode) {

		boolean status = userService.setLoginSession(request, userId, password, userTypeCode);
		if (status) {
			return ResponseEntityUtil.success("SUCCESS");
		} else {
			return ResponseEntityUtil.success("NOTFOUND");
		}
	}

	/**사용자 그룹 등록*/
	@PostMapping(value ="/rest/user/group/add")
	public ResponseEntity<CommonResponse> addUserGroup(UserGroupVo vo) {
		userService.addUserGroup(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**사용자 그룹 수정*/
	@PostMapping(value ="/rest/user/group/modify")
	public ResponseEntity<CommonResponse> modifyUserGroup(UserGroupVo vo) {
		userService.modifyUserGroup(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**사용자 그룹 정보*/
	@GetMapping(value ="/rest/user/group/get")
	public ResponseEntity<CommonResponse> getUserGroup(UserGroupVo vo) {
		return ResponseEntityUtil.success(userService.getUserGroup(vo.getUserGroupCode()));
	}

	/**시스템관리 > 계정관리 > 사용자 > 엑셀다운로드*/
	@PostMapping(value ="/rest/user/group/excel")
	public ResponseEntity<CommonResponse> getUserGroupListExcel(
		UserGroupVo vo, HttpServletResponse response)
		throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		UserModel userModel = new UserModel();
		userModel.setGroupVo(vo);
		List<UserGroupVo> list = userService.getUserGroupList(null, userModel);
		excelService.excelDownload(response, UserGroupVo.class, "userGroupManage_list.xlsx", list);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**권한 등록*/
	@PostMapping(value ="/rest/user/auth/add")
	public ResponseEntity<CommonResponse> addAuth(UserAuthVo vo) {
		userService.addAuth(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**권한 수정*/
	@PostMapping(value ="/rest/user/auth/modify")
	public ResponseEntity<CommonResponse> modifyAuth(UserAuthVo vo) {
		userService.modifyAuth(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**권한 정보*/
	@GetMapping(value ="/rest/user/auth/get")
	public ResponseEntity<CommonResponse> getAuth(UserAuthVo vo) {
		return ResponseEntityUtil.success(userService.getAuth(vo.getAuthCode()));
	}

	/**시스템관리 > 계정관리 > 권한 > 엑셀다운로드*/
	@PostMapping(value ="/rest/user/auth/excel")
	public ResponseEntity<CommonResponse> getUserGroupListExcel(
		UserAuthVo vo, HttpServletResponse response)
		throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		UserModel userModel = new UserModel();
		userModel.setAuthVo(vo);
		List<UserAuthVo> list = userService.getAuthList(null, userModel);
		excelService.excelDownload(response, UserAuthVo.class, "userAuthManage_list.xlsx", list);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**사용자 등록*/
	@PostMapping(value ="/rest/user/add")
	public ResponseEntity<CommonResponse> addUser(UserVo vo) {
		userService.addUser(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**사용자 수정*/
	@PostMapping(value ="/rest/user/modify")
	public ResponseEntity<CommonResponse> modifyUser(UserVo vo) {
		userService.modifyUser(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**사용자 정보*/
	@GetMapping(value ="/rest/user/get")
	public ResponseEntity<CommonResponse> getUser(UserVo vo) {
		return ResponseEntityUtil.success(userService.getUserInfo(vo.getUserId()));
	}

	/**시스템관리 > 계정관리 > 사용자 > 엑셀다운로드*/
	@PostMapping(value ="/rest/user/excel")
	public ResponseEntity<CommonResponse> getUserListExcel(
		UserVo vo, HttpServletResponse response)
		throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		UserModel userModel = new UserModel();
		userModel.setUserVo(vo);
		List<UserVo> list = userService.getUserList(null, userModel);
		excelService.excelDownload(response, UserVo.class, "userManage_list.xlsx", list);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**기존 비밀번호 확인*/
	@GetMapping(value ="/rest/user/pass/check")
	public ResponseEntity<CommonResponse> checkPassword(UserVo vo) {
		UserVo user = userService.getUserInfo(vo.getUserId(), vo.getPassword());
		if(user != null) {
			return ResponseEntityUtil.success("SUCCESS");
		}else {
			return ResponseEntityUtil.success("NOTFOUND");
		}
	}

	/**비밀번호 변경*/
	@PostMapping(value ="/rest/user/pass/modify")
	public ResponseEntity<CommonResponse> modifyPassword(UserVo vo) {
		userService.modifyUserPassword(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**문의알림수신여부 변경*/
	@PostMapping(value ="/rest/user/alarm/modify")
	public ResponseEntity<CommonResponse> modifyAlarm(UserVo vo) {
		userService.modifyUserAlarmYn(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	/**임시비밀번호 발급*/
	@PostMapping(value ="/rest/user/tmppass")
	public ResponseEntity<CommonResponse> issueTempPassword(UserVo vo) {
		userService.modifyUserPassword(vo);
		return ResponseEntityUtil.success("SUCCESS");
	}

	@GetMapping("/rest/auth/menu")
	public ResponseEntity<CommonResponse> getAdmAuthForSystemInquiry(UserVo vo) {
		UserVo user = SessionUtil.getLoginSession();
		user.setParentCode(vo.getParentCode());
		return ResponseEntityUtil.success(userService.getAdmAuthForSystemInquiry(user));
	}

	/**
	 * MD 코드 엑셀 다운로드
	 */
	@PostMapping("/rest/user/mdInfo/excel-download")
	public ResponseEntity<CommonResponse> mdInfo(
		@ModelAttribute UserModel userModel, HttpServletResponse response
	) throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException
	{
		userModel.setSearchUserGroupCode("MD");
		List<UserVo> mdList = userService.getUserList(null,userModel);
		List<Object> dataList = new ArrayList<>();
		for(UserVo userVo : mdList){
			dataList.add(new Object(){
				@ExcelHeader("담당자")
				private String userName = userVo.getUserName();
				@ExcelHeader("MD 코드")
				private long searchGroupSeq = userVo.getSearchGroupSeq();
				@ExcelHeader("분류")
				private String groupName = userVo.getGroupName();
			});

		}
		String fileName = "mdInfo_code_list.xlsx";

		excelService.excelDownload(response, new Object(){
			@ExcelHeader("담당자")
			private String userName;
			@ExcelHeader("MD 코드")
			private long searchGroupSeq;
			@ExcelHeader("분류")
			private String groupName;
		}.getClass(), fileName, dataList);
		return ResponseEntityUtil.success("SUCCESS");
	}
}
