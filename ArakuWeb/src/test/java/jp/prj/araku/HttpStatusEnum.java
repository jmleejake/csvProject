package com.thekary.mbs.enums;

public enum HttpStatusEnum {
	/**
	 * S:성공(200), F:실패(400-599), U:토큰만료(401)
	 */
	SUCCESS("S"), FAILED("F"), UNAUTHORIZED("U");

	private final String code;

	HttpStatusEnum(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}
}
