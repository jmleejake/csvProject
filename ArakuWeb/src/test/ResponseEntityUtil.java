package com.km.adm.util;

import com.km.adm.common.CommonResponse;
import com.km.adm.enums.HttpStatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResponseEntityUtil {

	/**
	 * 컨트롤러 공통 반환
	 */
	public static <T> ResponseEntity<CommonResponse> success(T body) {
		return new ResponseEntity<>(
			new CommonResponse(HttpStatusEnum.SUCCESS.code(), HttpStatusEnum.SUCCESS.name(), body), HttpStatus.OK);
	}
	public static ResponseEntity<CommonResponse> success() {
		return new ResponseEntity<>(
			new CommonResponse(HttpStatusEnum.SUCCESS.code(), HttpStatusEnum.SUCCESS.name()), HttpStatus.OK);
	}

	public static <T> ResponseEntity<CommonResponse> failed(T body) {
		return new ResponseEntity<>(
			new CommonResponse(HttpStatusEnum.FAILED.code(), HttpStatusEnum.FAILED.name(), body), HttpStatus.BAD_REQUEST);
	}
	public static ResponseEntity<CommonResponse> failed() {
		return new ResponseEntity<>(
			new CommonResponse(HttpStatusEnum.FAILED.code(), HttpStatusEnum.FAILED.name()), HttpStatus.BAD_REQUEST);
	}

	public static <T> ResponseEntity<CommonResponse> failed(String errorCode, String errorMsg) {
		return new ResponseEntity<>(
			new CommonResponse(errorCode, errorMsg), HttpStatus.BAD_REQUEST);
	}
}
