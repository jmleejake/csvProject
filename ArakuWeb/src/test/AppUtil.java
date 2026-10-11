package com.thekary.erp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonElement;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.thekary.erp.dto.erp.request.ExcelSearchReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.PrintBookmark;
import net.sf.jasperreports.engine.PrintPart;
import net.sf.jasperreports.engine.PrintParts;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AppUtil {

	private static final AtomicInteger CAMPAIGN_SEQ = new AtomicInteger(0);

	/**
	 * Unique ID 생성
	 *
	 * @return
	 */
	public static String getUniqueId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Unique ID 생성(날짜포함)
	 *
	 * @return
	 */
	public static String getUniqueIdWithDate() {
		return AppUtil.getUniqueId() + "-" + new Date().getTime();
	}

	/**
	 * 공백 및 엔터 제거
	 */
	public static String removeBlank(String str) {
		if (str == null) {
			return null;
		}
		return str
			.replaceAll("\n", "")
			.replaceAll("\r", "")
			.replaceAll("\t", "")
			.replaceAll("　", "")
			.replaceAll(" ", "");
	}

	/**
	 * 클라이언트 아이피 취득
	 *
	 * @return
	 */
	public static String getClientIp() {
		String ip = "";
		try {
			// HttpServletRequest 취득
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
			ip = request.getHeader("X-Forwarded-For");
			if (ip != null) {
				log.info("X-FORWARDED-FOR : " + ip);
			}
			if (ip == null) {
				ip = request.getHeader("Proxy-Client-IP");
				if (ip != null) {
					log.info("Proxy-Client-IP : " + ip);
				}
			}
			if (ip == null) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (ip != null) {
					log.info("HTTP_CLIENT_IP : " + ip);
				}
			}
			if (ip == null) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (ip != null) {
					log.info("HTTP_X_FORWARDED_FOR : " + ip);
				}
			}
			if (ip == null) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (ip != null) {
					log.info("WL-Proxy-Client-IP : " + ip);
				}
			}
			if (ip == null) {
				ip = request.getRemoteAddr();
				if (ip != null) {
					log.info("getRemoteAddr : " + ip);
				}
			}

			log.info("Result : IP Address : " + ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 자릿수 맞추기
	 */
	public static String setLPad(String strContext, int iLen, String strChar) {
		String strResult = "";
		StringBuilder sbAddChar = new StringBuilder();

		for (int i = strContext.length(); i < iLen; i++) { // iLen길이 만큼 strChar문자로 채운다.
			sbAddChar.append(strChar);
		}
		strResult = sbAddChar + strContext; // LPAD이므로, 채울문자열 + 원래문자열로 Concate한다.

		return strResult;
	}

	/**
	 * 매장배분 번호 생성
	 */
	public static String getAssignNo() {
		return "A" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Instant.now().toEpochMilli());
	}

	/**
	 * 창고이동지시 번호 생성
	 */
	public static String getStockMoveNo() {
		return "M" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Instant.now().toEpochMilli());
	}

	/**
	 * 메인작업지시 번호 생성
	 */
	public static String getManufactureOrderNo() {
		return "MO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(
			Instant.now().toEpochMilli());
	}

	/**
	 * 생산지시 코드 생성
	 */
	public static String getManufactureOrderCode() {
		return "MO" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(
			Instant.now().toEpochMilli());
	}


	/**
	 * 일련번호 생성
	 */
	public static String getSerialNo(String str) {
		return str + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Instant.now().toEpochMilli());
	}

	public static String getSerialNo() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Instant.now().toEpochMilli());
	}
	public static Long getSerialNoForNumber() {
		return Long.parseLong(new SimpleDateFormat("yyMMddHHmmssSSS").format(Instant.now().toEpochMilli()));
	}

	/**
	 * 랜덤 쿠폰 시리얼 번호 생성
	 */
	public static List<String> getRandomSerialNos(String prefix, int count) {
		List<String> serialList = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			String serial = generateRandomSerial();
			serialList.add(prefix + serial);
		}
		return serialList;
	}

	public static String generateRandomSerial() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(20);

		// 현재 날짜 및 시간 정보를 포함한 숫자 부분 생성
		String stamp1 = new SimpleDateFormat("yy").format(Instant.now().toEpochMilli());
		String stamp2 = new SimpleDateFormat("MMdd").format(Instant.now().toEpochMilli());
		String stamp3 = new SimpleDateFormat("HHmmss").format(Instant.now().toEpochMilli());

		sb.append(stamp1);
		for (int i = 0; i < 4; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			sb.append(randomChar);
		}
		sb.append(stamp2);
		for (int i = 0; i < 2; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			sb.append(randomChar);
		}
		sb.append(stamp3);
		for (int i = 0; i < 3; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			sb.append(randomChar);
		}
		return sb.toString();
	}

	/**
	 * 캠페인 코드 생성
	 */
	public static String getCampaignCode(String prefix) {
		try {
			String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(Instant.now().toEpochMilli());
			int seq = CAMPAIGN_SEQ.incrementAndGet() % 1000;
			return prefix + "-" + yyyyMMddHHmmss + String.format("%03d", seq);
		} catch (Exception e) {
			log.error("캠페인 코드 생성 에러");
			return null;
		}
	}

	/**
	 * 주문번호 취득
	 *
	 * @return yyyyMMddHHmmssSSS
	 */
	public static String getOrderNo(String orderCode) {
		try {
			return orderCode + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(
				Instant.now().toEpochMilli());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("주문번호 취득 에러");
			return null;
		}
	}
	/**
	 * 수선번호 취득
	 *
	 * @return yyyyMMddHHmmssSSS
	 */
	public static String getRepairNo(String repairCode) {
		try {
			return repairCode + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(
				Instant.now().toEpochMilli());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("수선번호 취득 에러");
			return null;
		}
	}


	/**
	 * 롯데택배 운송장 다음 번호
	 */
	public static String getNextLotTrackingNo(String lastUsedNo) {
		String trackingNo = lastUsedNo.substring(0, 11);
		long nextNo = Long.parseLong(trackingNo) + 1;
		long parity = nextNo % 7;

		return String.format("%011d", nextNo) + parity;
	}

	/**
	 * Object to Json String
	 */
	public static String convertObjectToJson(Object obj) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(obj);

			return jsonStr;
		} catch (Exception e) {
			log.error("객체 -> Json 문자열 변환 에러", e);
			return null;
		}
	}

	/**
	 * Json String to Object
	 */
	public static Object convertJsonToObject(String jsonStr, Class<?> clazz) {
		try {
			// json 안에 항목 객체에 넣을건데, 없는 항목이 있으면 무시하고 객체에 담기
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object obj = mapper.readValue(jsonStr, clazz);

			return obj;
		} catch (Exception e) {
			log.error("Json -> 객체 변환 에러", e);
			return null;
		}
	}

	// 제네릭 타입용
	public static <T> T convertJsonToObject(String json, TypeReference<T> typeReference) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        return objectMapper.readValue(json, typeReference);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	public static String getNullAsEmptyString(JsonElement jsonElement) {
		try {
			if (jsonElement == null) {
				return "";
			}
			return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
		} catch (Exception e) {
			return jsonElement.toString();
		}
	}

	/**
	 * Json String to Object List
	 */
	public static List<?> convertJsonToObjectList(String jsonStr, Class<?> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
			List<?> obj = mapper.readValue(jsonStr, javaType);

			return obj;
		} catch (Exception e) {
			log.error("Json -> 객체 리스트 변환 에러", e);
			return null;
		}
	}

	public static <T> List<T> convertJsonToObjectList2(String jsonStr, Class<T> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return mapper.readValue(jsonStr, javaType);
		} catch (Exception e) {
			log.error("Json -> 객체 리스트 변환 에러", e);
			return Collections.emptyList();  // null 대신 빈 리스트 반환
		}
	}


	/**
	 * List<List<String>> 오름차순 및 내림차순
	 * <p>
	 * ascending: true -> 오름차순
	 * <p>
	 * ascending: false -> 내림차순
	 * <p>
	 * sort -> 첫번째 : 0부터 시작 (미사용 -1)
	 */
	public static List<List<String>> sortArrayList(List<List<String>> paramList, boolean ascending,
		int firstSort, int secondSort, int thirdSort, int firthSort) {

		List<String>[] list = new List[paramList.size()];

		int size = 0;
		for (List<String> strList : paramList) {
			list[size++] = strList;
		}

		Comparator<List<String>> ascendingSort = new Comparator<>() {
			int result = 0;

			@Override
			public int compare(List<String> o1, List<String> o2) {

				result = o1.get(firstSort).compareTo(o2.get(firstSort));
				if (secondSort != -1 && result == 0) {
					result = o1.get(secondSort).compareTo(o2.get(secondSort));
					if (thirdSort != -1 && result == 0) {
						result = o1.get(thirdSort).compareTo(o2.get(thirdSort));
						if (firthSort != -1 && result == 0) {
							result = o1.get(firthSort).compareTo(o2.get(firthSort));
						}
					}
				}

				return result;
			}
		};

		Comparator<List<String>> descendingSort = new Comparator<>() {
			int result = 0;

			@Override
			public int compare(List<String> o1, List<String> o2) {

				result = o1.get(firstSort).compareTo(o2.get(firstSort)) * -1;
				if (secondSort != -1 && result == 0) {
					result = o1.get(secondSort).compareTo(o2.get(secondSort)) * -1;
					if (thirdSort != -1 && result == 0) {
						result = o1.get(thirdSort).compareTo(o2.get(thirdSort)) * -1;
						if (firthSort != -1 && result == 0) {
							result = o1.get(firthSort).compareTo(o2.get(firthSort)) * -1;
						}
					}
				}

				return result;
			}
		};

		if (ascending) {
			Arrays.sort(list, ascendingSort);
		} else {
			Arrays.sort(list, descendingSort);
		}

		return new ArrayList<>(Arrays.asList(list));
	}

	public static byte[] getBarCodeImage(String text, int width, int height) {
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();

			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			Writer writer = new Code128Writer();

			BitMatrix byteMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height,
				hintMap);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			MatrixToImageWriter.writeToStream(byteMatrix, "png", byteArrayOutputStream);

			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert Map
	 */
	public static Map<String, Object> convertToMap(Object obj)
		throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (obj == null) {
			return Collections.emptyMap();
		}
		Map<String, Object> convertMap = new HashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			convertMap.put(field.getName(), field.get(obj));
		}
		return convertMap;
	}

	/**
	 * Convert ValueObject
	 */
	public static <T> T convertToValueObject(Map<String, Object> map, Class<T> type)
		throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (type == null) {
			throw new NullPointerException("Class cannot be null");
		}

		T instance = type.getConstructor().newInstance();

		if (map == null || map.isEmpty()) {
			return instance;
		}
		for (Map.Entry<String, Object> entrySet : map.entrySet()) {
			Field[] fields = type.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);

				String fieldName = field.getName();

				boolean isSameType = entrySet.getValue().getClass().equals(field.getType());
				boolean isSameName = entrySet.getKey().equals(fieldName);

				if (isSameType && isSameName) {
					field.set(instance, map.get(fieldName));
				}
			}
		}
		return instance;
	}

	/**
	 * Convert List Map
	 */
	public static List<Map<String, Object>> convertToMaps(List<?> list)
		throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> convertList = new ArrayList<>();
		for (Object obj : list) {
			convertList.add(AppUtil.convertToMap(obj));
		}
		return convertList;
	}

	/**
	 * Convert List ValueObject
	 */
	public static <T> List<T> convertToValueObjects(List<Map<String, Object>> list, Class<T> type)
		throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> convertList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			convertList.add(AppUtil.convertToValueObject(map, type));
		}
		return convertList;
	}

	/**
	 * JasperReport 생성
	 */
	public static void createJasperReport(HttpServletResponse response,
		InputStream inputStream, Collection<?> printList) throws JRException, IOException {

		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

		// Swap 파일 크기 확장 (OOM 방지 핵심)
		JRSwapFile swapFile = new JRSwapFile(System.getProperty("java.io.tmpdir"), 8192, 200);
		JRSwapFileVirtualizer virtualizer = new JRSwapFileVirtualizer(300, swapFile, true);

		try {
			// 데이터 소스 (캐싱 비활성)
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(printList,
				false);

			Map<String, Object> params = new HashMap<>();
			params.put("CreatedBy", "scott");
			params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
				dataSource);

			// Response 초기화 (누락 방지)
			response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=report.pdf");
			response.setBufferSize(1024 * 8);

			try (OutputStream out = response.getOutputStream()) {

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(
					new SimpleOutputStreamExporterOutput(out)
				);

				SimplePdfExporterConfiguration config =
					new SimplePdfExporterConfiguration();
				config.setCompressed(true);                 // 메모리 사용 감소
				config.setCreatingBatchModeBookmarks(false);

				exporter.setConfiguration(config);

				exporter.exportReport();
				out.flush(); // 중요
			}

		} finally {
			// 반드시 호출 (swap 파일 정리)
			virtualizer.cleanup();
		}
	}

	public static void createJasperReport(HttpServletResponse response,
		InputStream inputStream, Collection<?> printList,
		List<HashMap<String, String>> positionList) throws JRException, IOException {

		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JRBeanCollectionDataSource dataSrc = new JRBeanCollectionDataSource(printList, false);
		Map<String, Object> params = new HashMap<>();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSrc);

		List<JRPrintPage> pages = jasperPrint.getPages();
		for (JRPrintPage page : pages) {
			List<JRPrintElement> pageElements = page.getElements();
			for (int i = 0; i < pageElements.size(); i++) {
				JRPrintElement element = pageElements.get(i);
				HashMap<String, String> position = positionList.get(i);
				element.setX(Integer.parseInt(position.get("x")));
				element.setY(Integer.parseInt(position.get("y")));
			}
		}

		response.setContentType("application/pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

		inputStream.close();
	}

	public static JasperPrint createJasperReport(String name, JasperPrint pattern) {
		JasperPrint newPrint = new JasperPrint();
		newPrint.setBottomMargin(pattern.getBottomMargin());
		newPrint.setLeftMargin(pattern.getLeftMargin());
		newPrint.setTopMargin(pattern.getTopMargin());
		newPrint.setRightMargin(pattern.getRightMargin());
		newPrint.setLocaleCode(pattern.getLocaleCode());
		newPrint.setName(name);
		newPrint.setOrientation(pattern.getOrientationValue());
		newPrint.setPageHeight(pattern.getPageHeight());
		newPrint.setPageWidth(pattern.getPageWidth());
		newPrint.setTimeZoneId(pattern.getTimeZoneId());

		return newPrint;
	}

	public static void addJasperPrint(JasperPrint base, JasperPrint add) {
		for (JRStyle style : add.getStyles()) {
			String styleName = style.getName();
			if (!base.getStylesMap().containsKey(styleName)) {
				try {
					base.addStyle(style);
				} catch (JRException e) {
					log.warn("Couldn't add a style", e);
				}
			}
		}

		for (JRPrintPage page : add.getPages()) {
			base.addPage(page);
		}

		if (add.hasProperties()) {
			JRPropertiesMap propMap = add.getPropertiesMap();
			for (String propName : propMap.getPropertyNames()) {
				String propValue = propMap.getProperty(propName);
				base.setProperty(propName, propValue);
			}
		}

		if (add.hasParts()) {
			PrintParts parts = add.getParts();
			Iterator<Entry<Integer, PrintPart>> partsIterator = parts.partsIterator();
			while (partsIterator.hasNext()) {
				Entry<Integer, PrintPart> partsEntry = partsIterator.next();
				base.addPart(partsEntry.getKey(), partsEntry.getValue());
			}
		}

		List<PrintBookmark> bookmarks = add.getBookmarks();
		if (bookmarks != null) {
			for (PrintBookmark bookmark : bookmarks) {
				base.addBookmark(bookmark);
			}
		}
	}

	public static Map<String, Object> getJasperParams(String offlineCode,
		String storeName, String ceoName, String storeAddress, String bizNo, String bizSection,
		String bizIndustry, Long totalQty, Long totalSupplyPrice, String shippingMasterSetSeq,
		String shippingOrderDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("pvStoreName", "(주)더캐리");
		params.put("pvCeoName", "이은정, 윤중용");
		params.put("pvStoreAddress", "서울특별시 용산구 한남동 738-36 2F-5F");
		params.put("pvBizNo", "128-87-03548");
		params.put("pvBizSection", "도소매");
		params.put("pvBizIndustry", "의류/제조");
		params.put("offlineCode", offlineCode);
		params.put("storeName", storeName);
		params.put("ceoName", ceoName);
		params.put("storeAddress", storeAddress);
		params.put("bizNo", bizNo);
		params.put("bizSection", bizSection);
		params.put("bizIndustry", bizIndustry);
		params.put("totalQty", totalQty);
		params.put("totalSupplyPrice", totalSupplyPrice);
		params.put("shippingMasterSetSeq", shippingMasterSetSeq);
		// params.put("shippingOrderDate", shippingOrderDate);
		// 요구사항 - 지금날짜로
		params.put("shippingOrderDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return params;
	}

	/**
	 * 천의 자리 콤마
	 */
	public static String thousandsSeparator(Long value) {
		return String.format("%,d", value);
	}

	/**
	 * 전화 번호 분리
	 */
	public static String[] splitPhoneNumber(String phoneNumber) {
		if (StringUtils.isBlank(phoneNumber)) {
			return new String[] {"-", "-", "-"};
		}

		// 하이픈이 두 개 포함되고 동시에 숫자가 포함된 문자열인지 확인
		Pattern pattern = Pattern.compile("^(?=.*\\d)([^-]*-[^-]*-[^-]*)$");
		Matcher matcher = pattern.matcher(phoneNumber);

		if (matcher.matches()) {
			return phoneNumber.split("-");
		} else {
			phoneNumber = phoneNumber.replace("-", "").replace(" ", "");
			if (StringUtils.isBlank(phoneNumber)) {
				return new String[] {"-", "-", "-"};
			}

			String part1, part2, part3;

			if (phoneNumber.length() >= 9) {
				// 길이가 9이상인 경우
				if (phoneNumber.startsWith("02")) {
					// 서울 지역번호 (02)일 경우
					part1 = phoneNumber.substring(0, 2);
					if (phoneNumber.length() == 9) {
						// 길이가 9 <-> 02-XXX-XXXX
						part2 = phoneNumber.substring(2, 5);
						part3 = phoneNumber.substring(5);
					} else {
						// 길이가 10 이상인 경우 <-> 02-XXXX-XXXX
						part2 = phoneNumber.substring(2, 6);
						part3 = phoneNumber.substring(6);
					}
				} else {
					// 다른 지역번호 또는 휴대폰 번호일 경우
					part1 = phoneNumber.substring(0, 3);
					if (phoneNumber.length() == 10) {
						// 길이가 10 <-> 0XX-XXX-XXXX
						part2 = phoneNumber.substring(3, 6);
						part3 = phoneNumber.substring(6);
					} else {
						// 길이가 11 이상인 경우 <-> 0XX-XXXX-XXXX
						part2 = phoneNumber.substring(3, 7);
						part3 = phoneNumber.substring(7);
					}
				}
			} else {
				// 길이가 8이하인 경우
				int length = phoneNumber.length();

				if (length == 1) {
					part1 = "-";
					part2 = "-";
					part3 = phoneNumber;
				} else if (length == 2) {
					part1 = "-";
					part2 = phoneNumber.substring(0, 1);
					part3 = phoneNumber.substring(1, 2);
				} else {
					// 세 개의 부분으로 나누기 위해 각 부분의 길이를 계산 후 나누기
					int part1Length = length / 3 + (length % 3 > 0 ? 1 : 0);
					int part2Length = (length - part1Length) / 2 + ((length - part1Length) % 2 > 0 ? 1 : 0);

					part1 = phoneNumber.substring(0, part1Length);
					part2 = phoneNumber.substring(part1Length, part1Length + part2Length);
					part3 = phoneNumber.substring(part1Length + part2Length, length);
				}
			}

			return new String[] { part1, part2, part3 };
		}
	}

	/**
	 * 이미지 변환
	 */
	public static InputStream convertImageToJpg(InputStream inputStream) throws IOException {
		// 원본 이미지 읽기
		BufferedImage originalImage = ImageIO.read(inputStream);

		// 이미지가 null인지 확인 (지원되지 않는 형식의 경우 null 반환)
		if (originalImage == null) {
			throw new IllegalArgumentException("Unsupported image format or corrupted image file.");
		}

		// 새로운 BufferedImage 생성 (JPG는 투명도를 지원하지 않기 때문에 투명도 제거)
		BufferedImage newImage = new BufferedImage(
			originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

		// 원본 이미지 그리기
		Graphics2D g2d = newImage.createGraphics();
		g2d.drawImage(originalImage, 0, 0, Color.WHITE, null);
		g2d.dispose();

		// JPG로 변환된 이미지 버퍼
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		// JPG 포맷으로 이미지 쓰기
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		if (!writers.hasNext()) {
			throw new IllegalStateException("No writers found for jpg");
		}
		ImageWriter writer = writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);

		// 이미지 압축 품질 설정
		ImageWriteParam param = writer.getDefaultWriteParam();
		if (param.canWriteCompressed()) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.85f); // 압축 품질 설정 (0.0f ~ 1.0f)
		}

		writer.write(null, new javax.imageio.IIOImage(newImage, null, null), param);
		ios.close();
		writer.dispose();

		// InputStream으로 변환된 이미지 반환
		return new ByteArrayInputStream(os.toByteArray());
	}

	/**
	 * 이미지 리사이즈
	 */
	public static InputStream resizeImage(InputStream imgStream, long maxImageByte) throws IOException {
		// BufferedImage로 변환
		BufferedImage image = ImageIO.read(imgStream);

		// 초기 품질 설정
		float quality = 1.0f;

		// 결과 이미지 용량을 측정하기 위한 ByteArrayOutputStream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// JPEG writer와 ImageWriteParam 설정
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

		// 이미지 크기를 줄이기 위한 반복
		while (true) {
			baos.reset();
			param.setCompressionQuality(quality);

			try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
				writer.setOutput(ios);
				writer.write(null, new IIOImage(image, null, null), param);
			}

			// 압축된 이미지의 크기를 측정
			byte[] imageData = baos.toByteArray();

			// 이미지 크기가 조건에 맞으면 종료
			if (imageData.length <= maxImageByte) {
				break;
			}

			// 품질을 줄여서 다시 시도
			quality -= 0.05f;

			// 품질이 너무 낮아지면 예외 발생
			if (quality <= 0) {
				throw new IOException("Unable to compress image to desired size");
			}
		}

		// 최종적으로 압축된 이미지를 InputStream으로 변환하여 반환
		return new ByteArrayInputStream(baos.toByteArray());
	}


	public static String getArrayValue(String[] array, int index) {
		// 배열의 길이 내에 있는지 검사
		if (index >= 0 && index < array.length) {
			return array[index];
		} else {
			return null; // 범위를 벗어난 경우 null 반환
		}
	}

	public static Date getDateDefaultFormat(String dateString) {
		return getDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDate(String dateString, String format) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("날짜 파싱 에러", dateString);
			return null;
		}
	}

	// 현재 시간
	public static Date getCurrentDateTime() {
		// 1. 현재 시간 (타임존 없는 LocalDateTime)
		LocalDateTime localDateTime = LocalDateTime.now();

		// 2. 대한민국(Asia/Seoul) 타임존 적용
		ZonedDateTime seoulZonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));

		// 3. ZonedDateTime → Date
		Date date = Date.from(seoulZonedDateTime.toInstant());

		return date;
	}

	// 현재 시간
	public static String getCurrentDateString() {
		// 1. 현재 시간 (타임존 없는 LocalDateTime)
		LocalDateTime localDateTime = LocalDateTime.now();

		// 2. 대한민국(Asia/Seoul) 타임존 적용
		ZonedDateTime seoulZonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));

		// 3. ZonedDateTime → Date
		Date date = Date.from(seoulZonedDateTime.toInstant());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(date);
	}

	public static ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		return objectMapper;
	}

	public static <T> T readValueAsObject(String json, Class<T> clazz) {
		ObjectMapper objectMapper = AppUtil.getObjectMapper();
		if (json == null) {
			return null;
		}

		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			log.error("json 변환 실패", e);
		}
		return null;
	}

	public static <T> T[] readValueAsArray(String json, Class<T[]> clazz) {
		ObjectMapper objectMapper = AppUtil.getObjectMapper();
		if (json == null) {
			return null;
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			log.error("JSON 배열 변환 실패", e);
		}
		return null;
	}

	public static ExcelSearchReqDto getSchemaKeyValue(Object dto, boolean skipNull, Class<?> clazz) {
		List<ExcelSearchReqDto.Search> list = new ArrayList();
		Class<?> dtoClass = dto.getClass();
		Field[] fields = dtoClass.getDeclaredFields();

		System.out.println("DTO 필드 정보:");

		for (Field field : fields) {
			field.setAccessible(true); // private 필드 접근 허용

			String description = "설명 없음";
			if (field.isAnnotationPresent(Schema.class)) {
				Schema schemaAnnotation = field.getAnnotation(Schema.class);
				description = schemaAnnotation.description();
			}

			try {
				Object value = field.get(dto);

				String valueStr = (value instanceof String[]) ? Arrays.toString((String[]) value) : String.valueOf(value);

				if ( (Objects.isNull(value) || "".equals(valueStr) || "[]".equals(valueStr)) && skipNull) {
					continue;
				}
				list.add(ExcelSearchReqDto.Search.builder()
						.description(description)
						.fieldName(field.getName())
						.value(valueStr)
						.build());

			} catch (IllegalAccessException e) {
				list.add(ExcelSearchReqDto.Search.builder()
						.description(description)
						.fieldName(field.getName())
						.value("알수없음")
						.build());

			}
		}
		return ExcelSearchReqDto.builder().searchList(list).build();
	}

	public static String convertExtension(String mimeType) {
		return convertExtension(mimeType, null);
	}

	public static String convertExtension(String mimeType, String originalFilename) {
		if (mimeType == null) {
			return "unknown";
		}

		switch (mimeType) {
			case "image/svg+xml":
				return "svg";
			case "image/gif":
				return "gif";
			case "image/tiff":
				return "tiff";
			case "image/jpeg":
			case "image/jpg":
				return "jpg";
			case "image/png":
				return "png";
			case "image/bmp":
			case "image/x-ms-bmp":
			case "image/x-bmp":
			case "image/x-wbmp":
				return "bmp";
			default:
				if (originalFilename != null && originalFilename.contains(".")) {
					return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
				} else {
					return "bin";
				}
		}
	}

	public static boolean getBoolean(Map<Object, Object> map, String key, boolean defaultValue) {
		Object val = map.get(key);
		if (val instanceof Boolean) {
			return (Boolean) val;
		} else if (val instanceof String) {
			return Boolean.parseBoolean((String) val);
		}
		return defaultValue;
	}

	public static String truncateUtf8(String input, int maxByte) {
		if (input == null) return null;

		byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);

		if (utf8Bytes.length <= maxByte) {
			return input;
		}

		int endIndex = maxByte;

		// 멀티바이트 문자 깨짐 방지: UTF-8 continuation byte 처리
		while (endIndex > 0 && (utf8Bytes[endIndex] & 0xC0) == 0x80) {
			endIndex--;
		}

		return new String(utf8Bytes, 0, endIndex, StandardCharsets.UTF_8);
	}

	// DTO → Map
	public static Map<String, Object> convertDtoToMap(Object dto) {
		return dto == null ? new HashMap<>() : new ObjectMapper().convertValue(dto, Map.class);
	}

	public static <T> List<T> readValue(String jsonString, Class<T[]> classType) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		try {
			return Arrays.asList(objectMapper.readValue(jsonString, classType));
		} catch (NullPointerException npe) {
			log.error("JSON parsing error", npe);
			return Collections.emptyList();
		} catch(IllegalArgumentException iae) {
			log.error("JSON parsing error", iae);
			return Collections.emptyList();
		} catch (UnrecognizedPropertyException upe) {
			log.error("JSON parsing error", upe);
			return Collections.emptyList();
		} catch (JsonProcessingException e) {
			log.error("JSON parsing error", e);
			return Collections.emptyList();
		}
	}

	/**
	 * 변화율 계산
	 */
	public static Double calcChangeRate(Long prev, Long current) {
	    if (prev == null || prev == 0L) {
	        return null;
	    }
	    long currentVal = (current != null) ? current : 0L;
	    double rate = ((double) (currentVal - prev) / prev) * 100;
	    return Math.round(rate * 10.0) / 10.0;
	}

	public static String formatDateString(Date date, String format){
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 널값을 "-"로 변환
	 * @param val 변환할 문자열
	 * @return val이 null이면 "-", 그렇지 않으면 val 반환
	 */
	public static String nvl(String val) {
		return val == null ? "-" : val;
	}
}
