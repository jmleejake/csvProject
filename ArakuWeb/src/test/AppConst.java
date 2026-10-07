package com.km.adm.config;

import java.util.HashMap;
import java.util.Map;

public class AppConst {

	/**
	 * [세션] 로그인 세션명
	 */
	public static final String LOGIN_SESSION_NAME = "loginUserInfo";

	/**
	 * 암복호화 키 
	 * 단순 전송용 데이터 암복호화
	 */
	public static final String ENCRYPT_KEY = "uyeziateebtnen1fe2xr4z1334267290";

	/**
	 * 온라인사이트코드
	 */
	public static final String ONLINE_SITE_CODE = "KM";

	/**
	 * 더캐리 코드(파트너코드)
	 */
	public static final String THEKARY_CODE = "THEKARY";


	private static final Map<String, String> addressNormalizationMap = new HashMap<>();

	static {
		addressNormalizationMap.put("서울특별시", "서울");
		addressNormalizationMap.put("부산광역시", "부산");
		addressNormalizationMap.put("대구광역시", "대구");
		addressNormalizationMap.put("울산광역시", "울산");
		addressNormalizationMap.put("광주광역시", "광주");
		addressNormalizationMap.put("대전광역시", "대전");
		addressNormalizationMap.put("인천광역시", "인천");


		addressNormalizationMap.put("전라북도", "전북");
		addressNormalizationMap.put("전북특별자치도", "전북");
		addressNormalizationMap.put("전라남도", "전남");
		addressNormalizationMap.put("충청북도", "충북");
		addressNormalizationMap.put("충청남도", "충남");
		addressNormalizationMap.put("경상북도", "경북");
		addressNormalizationMap.put("경상남도", "경남");

		addressNormalizationMap.put("강원도", "강원");
		addressNormalizationMap.put("강원특별자치도", "강원");
		addressNormalizationMap.put("세종특별자치시", "세종");
		addressNormalizationMap.put("경기도", "경기");
		addressNormalizationMap.put("제주특별자치도", "제주");
		// 추가적인 주소 정규화 규칙을 여기에 정의
	}

	public static String normalizeAddress(String address) {
		return addressNormalizationMap.getOrDefault(address, address);
	}

/*	public static List<Order> normalizeAddresses(List<Order> orders) {
		for (Order order : orders) {
			String normalizedAddress = normalizeAddress(order.getAddress());
			order.setAddress(normalizedAddress);
		}
		return orders;
	}*/

}
