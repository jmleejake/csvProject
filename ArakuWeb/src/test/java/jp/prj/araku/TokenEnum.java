package com.thekary.mbs.enums;

import java.util.Date;

public enum TokenEnum {
	/**
	 * ACCESS: 엑세스토큰, REFRESH: 리프레쉬토큰
	 */
	ACCESS("access_token"), REFRESH("tk_rt");

	private final String code;

	TokenEnum(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	/**
	 * 토큰 만료시간
	 * <p>
	 * ex) 10 * 60 * 1000 = 10분
	 */
	public Date expiresAt() {
		if ("ACCESS".equals(this.name())) {
			return new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
		} else if ("REFRESH".equals(this.name())) {
			return new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000);
		} else {
			return null;
		}
	}

	public int expiresAtInt() {
		if ("ACCESS".equals(this.name())) {
			return 16 * 60 * 60;
		} else if ("REFRESH".equals(this.name())) {
			return 24 * 60 * 60;
		} else {
			return 0;
		}
	}

	/**
	 * 토큰 권한 코드 이름
	 */
	public static String roleName() {
		return "roles";
	}

	/**
	 * 토큰 기본 권한 이름
	 */
	public static String basicRoleName() {
		return "role_basic";
	}

	/**
	 * 토큰 기본 관리자 권한 코드
	 */
	public static String basicRoleCustomerCode() {
		return "ROLE_BASIC_CUSTOMER_V1";
	}

	/**
	 * 토큰 prefix
	 */
	public static String prefix() {
		return "Bearer ";
	}
}
