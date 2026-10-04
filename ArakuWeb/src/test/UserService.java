package com.km.adm.service;

import com.km.adm.config.AppConst;
import com.km.adm.config.AppProperties;
import com.km.adm.dao.LogDao;
import com.km.adm.dao.UserDao;
import com.km.adm.dao.vo.AuthVo;
import com.km.adm.dao.vo.LogVo;
import com.km.adm.dao.vo.UserVo;
import com.km.adm.session.UserSession;
import com.km.adm.util.AppUtil;
import com.km.adm.util.EncryptUtil;
import com.km.adm.dao.vo.UserAuthVo;
import com.km.adm.dao.vo.UserGroupVo;
import com.km.adm.model.UserModel;
import com.km.adm.model.UserSessionModel;
import com.km.adm.util.Pagination;
import com.km.adm.util.SessionUtil;
import com.km.adm.util.TemplateUtil;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserDao userDao;
	private final LogDao logDao;
	private final MailService mailService;
	private final AppProperties appProperties;
	private final MsgTemplateService msgTemplateService;

	/**로그인 세션*/
	public boolean setLoginSession(HttpServletRequest request, String userId, String password, String userTypeCode) {

		UserVo user = this.getUserInfo(userId, password, userTypeCode);
		LogVo logVo = new LogVo();
		logVo.setAccessSystem(AppConst.ONLINE_SITE_CODE);
		logVo.setUserId(userId);
		logVo.setClientIp(AppUtil.getClientIp());

		if (user == null) {
			if(!"HO".equals(userTypeCode)) {
				// 임점사는 AD 로그인을 하지 않음
				return false;
			} else {
				log.info("[로그인] 일치하는 유저정보 없음 [ID]{}", userId);
				log.info("[로그인] AD 정보 확인 [ID]{}", userId);

				// AD 정보 확인, AD 정보가 맞으면 비밀번호 변경
				boolean status = adAuth(userId, password);
				if (status) {
					log.info("[로그인] AD 정보로 패스워드 변경 완료, 로그인 시도 [ID]{}", userId);
					user = this.getUserInfo(userId, password, userTypeCode);

					if (user == null) {
						log.error("[로그인] 알 수 없는 에러 발생 [ID]{}", userId);
						logVo.setLogTypeCode("AL03");    //로그인 에러
						logDao.insertLoginLog(logVo);
						return false;
					}
				} else {
					logVo.setLogTypeCode("AL06");    // AD계정없음
					logDao.insertLoginLog(logVo);
					return false;
				}
			}
		}

		HttpSession session = request.getSession();
		UserSessionModel userSession = new UserSessionModel();
		userSession.setUserId(user.getUserId());
		userSession.setUserName(user.getUserName());
		userSession.setPassword(user.getPassword());
		userSession.setUserGroupCode(user.getUserGroupCode());
		userSession.setUserTypeCode(user.getUserTypeCode());
		userSession.setDivisionCode(user.getDivisionCode());
		userSession.setPartnerCode(user.getPartnerCode());
		userSession.setEmail(user.getEmail());
		userSession.setMobileNo(user.getMobileNo());
		userSession.setPartnerCompName(user.getPartnerCompName());
		userSession.setPartnerUserYn(user.getPartnerUserYn());
		userSession.setAlarmYn(user.getAlarmYn());
		userSession.setTempPassYn(user.getTempPassYn());
		session.setAttribute(AppConst.LOGIN_SESSION_NAME, userSession);

		// 로그인세션
		UserSession loginUser = new UserSession();
		// User
		loginUser.setUser(user);
		// 권한 링크
		loginUser.setAuthLink(userDao.selectAuthLink(user.getUserGroupCode()));
		// 로그인 세션 SET
		SessionUtil.createLoginSession(loginUser);

		try {
			userDao.updateUserLoginDate(user.getUserId());

			// 클라이언트 아이피
			String clientIp = AppUtil.getClientIp();
			log.info("[로그인] IP : {} ", clientIp);

			// 로그인 로그처리
			logVo.setLogTypeCode("AL01");    // 로그인
			logDao.insertLoginLog(logVo);

		} catch (Exception e) {
			log.error("[로그인시간변경 오류] userId : {}", user.getUserId(), e);
		}

		return true;
	}

	/**
	 * AD 정보 확인
	 */
	private boolean adAuth(String userId, String password) {
		//찾을 사용자
		String userMail = userId + "@thekary.com";
		String encPassword = EncryptUtil.encodeSHA256(password);

		if ("cs".equals(userId) || "salesmd".equals(userId)) {
			log.info("[AD] 해당 계정은 로그인이 불가능합니다.");
			return false;
		}

		//환경설정
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put("com.sun.jndi.ldap.connect.timeout", "1000");
		//사용자 계정 - 메일주소 포함
		env.put(Context.SECURITY_PRINCIPAL, userMail);
		//사용자 비밀번호
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			//ou - 조직 , dc - 도메인
			String baseRdn = "dc=thekary,dc=com";
			//위에 환경설정으로 셋팅
			DirContext ctx;
			try {
				env.put(Context.PROVIDER_URL, "LDAP://10.10.30.31");
				ctx = new InitialDirContext(env);
			} catch (CommunicationException e) {
				log.info("[AD] Error : " + e.getMessage());
				env.put(Context.PROVIDER_URL, "LDAP://10.10.30.231");
				ctx = new InitialDirContext(env);
			}
			//결과값 필터를 미리 세팅
			SearchControls searchCtrl = new SearchControls();
			//결과값 중 sn, givenName, samAccountName, mail, mobile 항목을 보겠다는 의미
			String returnedAttrs[] = {"sn", "givenName", "samAccountName", "mail", "mobile"};
			searchCtrl.setReturningAttributes(returnedAttrs);
			//찾는 범위
			searchCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
			//mail 항목이 찾을 유저와 동일한 내용만 가져옴.
			String searchFilter = String.format("(&(objectClass=user)(mail=%s))", userMail);

			// 검색 결과
			NamingEnumeration<SearchResult> answer = ctx.search(baseRdn, searchFilter, searchCtrl);
			while (answer.hasMoreElements()) {
				SearchResult sr = answer.next();
				log.info("[AD] 유저정보 : " + sr.getName());
				String userGroupCode = userDao.selectUserGroupCode(sr.getName());
				if (userGroupCode == null) {
					log.info("[AD] 조회된 그룹 없음 : " + userId);
//					return false;
					/*
					  AD 유저는 임시로 E-BIZDept 할당
					 */
					userGroupCode = "E-BIZDept";
				}
				Attributes attrs = sr.getAttributes();
				UserVo user = new UserVo();
				user.setUserId(userId);
				user.setPassword(encPassword);
				user.setUserName(attrs.get("sn").get().toString() + attrs.get("givenName").get().toString());
				user.setUserGroupCode(userGroupCode);
				user.setUserTypeCode("HO");
				user.setDivisionCode(userGroupCode);
				user.setEmail(attrs.get("mail").get().toString());
				user.setMobileNo(attrs.get("mobile").get().toString());
				user.setPartnerCode(AppConst.THEKARY_CODE);
				userDao.mergeUser(user);

				return true;
			}

		} catch (NamingException e) {
			String msg = e.getMessage();
			log.info("[AD] Error : " + msg);
			if (msg.indexOf("data 525") > 0) {
				log.info("[AD] 사용자를 찾을 수 없습니다.");
			} else if (msg.indexOf("data 773") > 0) {
				log.info("[AD] 사용자 암호를 재설정해야합니다.");
			} else if (msg.indexOf("data 52e") > 0) {
				log.info("[AD] 아이디 또는 비밀번호가 잘못 입력 되었습니다.");
			} else if (msg.indexOf("data 533") > 0) {
				// 비활성화된 아이디는 추후 사용안함으로 변경 필요.
				log.info("[AD] 입력한 아이디는 비활성화 상태 입니다.");
			} else if (msg.indexOf("data 532") > 0) {
				log.info("[AD] 비밀번호가 만료되었습니다.");
			} else if (msg.indexOf("data 701") > 0) {
				log.info("[AD] 계정이 만료되었습니다.");
			} else {
				log.error("[AD] AD 서버가 작동하지 않거나 알 수 없는 에러가 발생했습니다.");
			}
		}
		return false;
	}

	/**유저정보 취득*/
	public UserVo getUserInfo(String userId, String password, String userTypeCode) {
		return userDao.selectUserInfo(userId, EncryptUtil.encodeSHA256(password), userTypeCode);
	}
	public UserVo getUserInfo(String userId) {
		return userDao.selectUserInfo(userId, "", "");
	}

	public UserVo getUserInfo(String userId, String password) {
		return userDao.selectUserInfo(userId, EncryptUtil.encodeSHA256(password), "");
	}

	/**로그인 유저의 메뉴목록 취득*/
	public List<UserVo> getUserMenuList(String userId) {
		return userDao.selectUserMenuList(userId);
	}

	/**사용자 목록 카운트*/
	public int getUserCount(UserModel model) {
		return userDao.selectUserCount(model);
	}

	/**사용자 목록*/
	public List<UserVo> getUserList(Pagination pagination, UserModel model) {
		List<UserVo> res = userDao.selectUserList(pagination, model);
		for(UserVo user : res) {
			if(null != user.getGroupName() && !"".equals(user.getGroupName())) {
				user.setUserName(user.getUserName() + "(" + user.getGroupName() + ")");
			}
		}
		return res;
	}

	/**사용자 등록*/
	public void addUser(UserVo vo) {
		vo.setPassword(EncryptUtil.encodeSHA256(vo.getPassword()));
		userDao.insertUser(vo);
	}

	/**사용자 수정*/
	public void modifyUser(UserVo vo) {
		if(null != vo.getUpdateFrom() && "mypage".equals(vo.getUpdateFrom())) {
			if(null != vo.getPassword() && !"".equals(vo.getPassword())) {
				vo.setPassword(EncryptUtil.encodeSHA256(vo.getPassword()));
				userDao.updateUserPassword(vo);
			}
		}
		userDao.updateUser(vo);
	}

	/**비밀번호 변경*/
	public void modifyUserPassword(UserVo vo) {
		if("Y".equals(vo.getTempPassYn())) { // 입점사 대표ID 담당자 임시비밀번호 발급
			String tempPass = RandomStringUtils.randomAlphanumeric(8);
			UserVo user = getUserInfo(vo.getUserId());
			vo.setPassword(EncryptUtil.encodeSHA256(tempPass));

			if("KAKAO".equals(vo.getAlarmType())) {
				msgTemplateService.sendKkoTemplateMsg("TKKM29", vo.getMobileNo(), null, null
				, user.getPartnerCompName(), user.getUserId(), tempPass, "캐리마켓");
			}else if("SMS".equals(vo.getAlarmType())) {
				msgTemplateService.sendSmsMsg("SMS003", vo.getMobileNo()
				, user.getPartnerCompName(), user.getUserId(), tempPass, "캐리마켓");
			}else if("EMAIL".equals(vo.getAlarmType())) {
				String content = TemplateUtil.getInstance().makeTempPasswordForPartnerEmailTemplate(
					user.getUserId()
					, user.getUserName()
					, appProperties.getFront().getUrl()
					, tempPass);
				mailService.sendMail("[캐리마켓] 임시 비밀번호 안내", vo.getEmail(), content);
			}
		}else {
			vo.setPassword(EncryptUtil.encodeSHA256(vo.getPassword()));
		}
		userDao.updateUserPassword(vo);
	}

	/**문의알림수신여부 변경*/
	public void modifyUserAlarmYn(UserVo vo) {
		userDao.updateAlarmYn(vo);
	}

	/**사용자 삭제*/
	public void removeUser(String userId) {
		userDao.deleteUser(userId);
	}

	/**사용자 그룹 목록 카운트*/
	public int getUserGroupCount(UserModel model) {
		return userDao.selectUserGroupCount(model);
	}

	/**사용자 그룹 목록*/
	public List<UserGroupVo> getUserGroupList(Pagination pagination, UserModel model) {
		return userDao.selectUserGroupList(pagination, model);
	}

	/**
	 * 사용자 그룹 리스트 전체
	 *
	 * userGroupAttribute 입력안하면 전체 가져옴
	 */
	public List<UserGroupVo> getUserGroupListAll(String userGroupAttribute) {
		return userDao.selectUserGroupListAll(userGroupAttribute);
	}

	/**사용자 그룹 등록*/
	public void addUserGroup(UserGroupVo vo) {
		userDao.insertUserGroup(vo);
	}

	/**사용자 그룹 수정*/
	public void modifyUserGroup(UserGroupVo vo) {
		userDao.updateUserGroup(vo);
	}

	/**사용자 그룹 정보*/
	public UserGroupVo getUserGroup(String userGroupCode) {
		return userDao.selectUserGroup(userGroupCode);
	}

	/**권한 목록 카운트*/
	public int getAuthCount(UserModel model) {
		return userDao.selectAuthCount(model);
	}

	/**권한 목록*/
	public List<UserAuthVo> getAuthList(Pagination pagination, UserModel model) {
		return userDao.selectAuthList(pagination, model);
	}

	/**권한 등록*/
	public void addAuth(UserAuthVo vo) {
		userDao.insertAuth(vo);
	}

	/**권한 수정*/
	public void modifyAuth(UserAuthVo vo) {
		userDao.updateAuth(vo);
	}

	/**권한 정보*/
	public UserAuthVo getAuth(String authCode) {
		return userDao.selectAuth(authCode);
	}

	/**
	 * 유저가 접근 가능한 메뉴 리스트
	 */
	public List<AuthVo> getAuthMenuList(UserVo userVo, Locale locale) {
		return userDao.selectAuthMenuList(userVo.getUserGroupCode(), locale.toString());
	}

	/**시스템문의 > 메뉴선택 */
	public List<AuthVo> getAdmAuthForSystemInquiry(UserVo userVo) {
		return userDao.selectAdmAuthForSystemInquiry(
			userVo.getParentCode(), userVo.getUserGroupCode());
	}

}
