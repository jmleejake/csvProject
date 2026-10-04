package com.km.adm.service;

import static com.km.adm.util.AppUtil.convertToListOfMaps;
import static com.km.adm.util.AppUtil.convertToMap;

import com.km.adm.util.ExcelDownloadUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.agile.AgileEncryptionInfoBuilder;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

	public void excelDownload(HttpServletResponse response,
		Class<?> voClass,
		String saveFileName,
		List<? extends Object> listVo
	)
		throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, IOException {
		Object voOne = listVo.get(0);
		Map<String, Object> dataOne = convertToMap(voOne);
		List<Map<String, Object>> dataList = convertToListOfMaps(listVo);

		String[] dataKeys =dataOne.keySet().toArray(new String[ dataOne.size()]);

		// 헤더 생성
		List<String> headers = ExcelDownloadUtil.generateHeaders(voClass, dataKeys);

		SXSSFWorkbook wb = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// 메모리에 100개의 행을 유지합니다. 행의 수가 넘으면 디스크에 적습니다.
			wb = new SXSSFWorkbook(100);
			SXSSFSheet sheet = wb.createSheet("Result");
			CellStyle formatCs = wb.createCellStyle();
			Font formatFont = wb.createFont();

			// SXSSF에서 Column 자동 길이조정 사용
			// INFO 로그 도배로 미사용
			// sheet.trackAllColumnsForAutoSizing();


			// 헤더 쓰기
			Row headerRow = sheet.createRow(0);
			// 배경색
			formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// 가로 세로 가운데 정렬
			formatCs.setAlignment(HorizontalAlignment.CENTER);
			formatCs.setVerticalAlignment(VerticalAlignment.CENTER);


			// 폰트 굵게, 폰트색 변경
			formatFont.setColor(IndexedColors.WHITE.getIndex());
			formatFont.setBold(true);

			// 스타일 적용
			formatCs.setFont(formatFont);

			for (int i = 0; i < headers.size(); i++) {

				Cell cell = headerRow.createCell(i);

				// 헤더에 맞게 너비 조정
				sheet.setColumnWidth(i, (headers.get(i).length() + 2) * 512);

				cell.setCellStyle(formatCs);

				cell.setCellValue(headers.get(i));
			}

			// 셀 스타일 생성
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setWrapText(true); // 줄바꿈 설정
			boolean isCarriageReturn = false;

			// 데이터 쓰기
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> dataMap = dataList.get(i);
				Row dataRow = sheet.createRow(i + 1);

				int cellIndex = 0;
				for (String header : dataKeys) {
					Cell cell = dataRow.createCell(cellIndex);
					Object value = dataMap.get(header);
					if (value != null) {
						//System.out.println(value.getClass().getSimpleName());

						if (value instanceof Date) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String formattedDate = dateFormat.format((Date) value);
							cell.setCellValue(formattedDate);
						}else{

							if(value.toString().indexOf("^|^") > -1){
							//if(value.toString().indexOf("\\n") > -1){
								isCarriageReturn = true;
								cell.setCellStyle(cellStyle);
								//cell.setCellValue(value.toString());
								cell.setCellValue(value.toString().replaceAll("\\^[|]\\^", ""));
							} else {
								cell.setCellValue(value.toString());
							}
						}
					}
					cellIndex++;
				}
			}
			if(isCarriageReturn) {
				saveFileName = URLEncoder.encode(saveFileName, "UTF-8");
			} else {
				saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", " ");
			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName);

			// 파일 다운로드
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error("[excelDownload Error]", e);
			throw e;
		} finally {
			try {
				// 디스크 적었던 임시파일을 제거합니다.
				if (wb != null) {
					wb.dispose();
					wb.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	/**
	 * 상품 리스트 필터링 된 엑셀 다운로드
	 */
	public void filteredExcelDownload(HttpServletResponse response,
		Class<?> voClass,
		String saveFileName,
		List<? extends Object> listVo,
		String[] dataKeys
	)
		throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, IOException {

		List<Map<String, Object>> dataList = convertToListOfMaps(listVo);

		// 헤더 생성
		List<String> headers = ExcelDownloadUtil.generateHeaders(voClass, dataKeys);

		SXSSFWorkbook wb = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// 메모리에 100개의 행을 유지합니다. 행의 수가 넘으면 디스크에 적습니다.
			wb = new SXSSFWorkbook(100);
			SXSSFSheet sheet = wb.createSheet("Result");
			CellStyle formatCs = wb.createCellStyle();
			Font formatFont = wb.createFont();

			// SXSSF에서 Column 자동 길이조정 사용
			// INFO 로그 도배로 미사용
			// sheet.trackAllColumnsForAutoSizing();


			// 헤더 쓰기
			Row headerRow = sheet.createRow(0);
			// 배경색
			formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// 가로 세로 가운데 정렬
			formatCs.setAlignment(HorizontalAlignment.CENTER);
			formatCs.setVerticalAlignment(VerticalAlignment.CENTER);


			// 폰트 굵게, 폰트색 변경
			formatFont.setColor(IndexedColors.WHITE.getIndex());
			formatFont.setBold(true);

			// 스타일 적용
			formatCs.setFont(formatFont);

			for (int i = 0; i < headers.size(); i++) {

				Cell cell = headerRow.createCell(i);

				// 헤더에 맞게 너비 조정
				sheet.setColumnWidth(i, (headers.get(i).length() + 2) * 512);

				cell.setCellStyle(formatCs);

				cell.setCellValue(headers.get(i));
			}

			// 셀 스타일 생성
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setWrapText(true); // 줄바꿈 설정
			boolean isCarriageReturn = false;

			// 데이터 쓰기
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> dataMap = dataList.get(i);
				Row dataRow = sheet.createRow(i + 1);

				int cellIndex = 0;
				for (String header : dataKeys) {
					Cell cell = dataRow.createCell(cellIndex);
					Object value = dataMap.get(header);
					if (value != null) {
						//System.out.println(value.getClass().getSimpleName());

						if (value instanceof Date) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String formattedDate = dateFormat.format((Date) value);
							cell.setCellValue(formattedDate);
						}else{

							if(value.toString().indexOf("^|^") > -1){
								//if(value.toString().indexOf("\\n") > -1){
								isCarriageReturn = true;
								cell.setCellStyle(cellStyle);
								//cell.setCellValue(value.toString());
								cell.setCellValue(value.toString().replaceAll("\\^[|]\\^", ""));
							} else {
								cell.setCellValue(value.toString());
							}
						}
					}
					cellIndex++;
				}
			}
			if(isCarriageReturn) {
				saveFileName = URLEncoder.encode(saveFileName, "UTF-8");
			} else {
				saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", " ");
			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName);

			// 파일 다운로드
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error("[excelDownload Error]", e);
			throw e;
		} finally {
			try {
				// 디스크 적었던 임시파일을 제거합니다.
				if (wb != null) {
					wb.dispose();
					wb.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	/**
	 * 템플릿 없이 엑셀 다운로드
	 */
	public void excelDownloadWithoutTemplate(HttpServletResponse response,
		List<List<String>> valueList,
		String saveFileName, boolean headerYn, String colorCellNum) throws IOException {

		SXSSFWorkbook wb = null;
		String[] arrayColorCellNum = null;
		if (colorCellNum != "") {
			arrayColorCellNum = colorCellNum.split(",");
		}

		try {
			// 메모리에 100개의 행을 유지합니다. 행의 수가 넘으면 디스크에 적습니다.
			wb = new SXSSFWorkbook(100);
			SXSSFSheet sheet = wb.createSheet("Result");
			CellStyle formatCs = wb.createCellStyle();
			CellStyle formatCs2 = wb.createCellStyle();
			Font formatFont = wb.createFont();

			// SXSSF에서 Column 자동 길이조정 사용
			// INFO 로그 도배로 미사용
			// sheet.trackAllColumnsForAutoSizing();

			int rowCnt = 0;
			for (List<String> value : valueList) {
				int cellCnt = 0;

				SXSSFRow row = sheet.createRow(rowCnt++);
				for (String str : value) {

					SXSSFCell cell = row.createCell(cellCnt++);

					// 헤더
					if (headerYn && rowCnt == 1) {
						try {
							// 배경색
							//formatCs.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
							formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
							formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

							// 가로 세로 가운데 정렬
							formatCs.setAlignment(HorizontalAlignment.CENTER);
							formatCs.setVerticalAlignment(VerticalAlignment.CENTER);

							// 헤더에 맞게 너비 조정
							sheet.setColumnWidth(cellCnt - 1, (str.length() + 2) * 512);

							// 폰트 굵게, 폰트색 변경
							formatFont.setColor(IndexedColors.WHITE.getIndex());
							formatFont.setBold(true);

							// 스타일 적용
							formatCs.setFont(formatFont);
							cell.setCellStyle(formatCs);
						} catch (Exception e) {
						}
					}

					if (arrayColorCellNum != null) {
						for (String s : arrayColorCellNum) {
							if (cellCnt == Integer.parseInt(s) && rowCnt > 1) {
								formatCs2.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
								formatCs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
								cell.setCellStyle(formatCs2);
							}
						}
					}
					// 셀 적용
					if (str != null && !"".equals(str)) {
						cell.setCellValue(str);
					}
				}
			}

			saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", " ");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName);

			// 파일 다운로드
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error("[템플릿 없는 엑셀 다운로드 Error]", e);
			throw e;
		} finally {
			try {
				// 디스크 적었던 임시파일을 제거합니다.
				if (wb != null) {
					wb.dispose();
					wb.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public void excelDownloadWithoutTemplateForObject(HttpServletResponse response,
		List<List<Object>> valueList,
		String saveFileName, boolean headerYn, String colorCellNum) throws IOException {

		SXSSFWorkbook wb = null;
		String[] arrayColorCellNum = null;
		if (colorCellNum != "") {
			arrayColorCellNum = colorCellNum.split(",");
		}

		try {
			// 메모리에 100개의 행을 유지합니다. 행의 수가 넘으면 디스크에 적습니다.
			wb = new SXSSFWorkbook(100);
			SXSSFSheet sheet = wb.createSheet("Result");
			CellStyle formatCs = wb.createCellStyle();
			CellStyle formatCs2 = wb.createCellStyle();
			Font formatFont = wb.createFont();

			// SXSSF에서 Column 자동 길이조정 사용
			// INFO 로그 도배로 미사용
			// sheet.trackAllColumnsForAutoSizing();

			int rowCnt = 0;
			for (List<Object> value : valueList) {
				int cellCnt = 0;

				SXSSFRow row = sheet.createRow(rowCnt++);
				for (Object obj : value) {

					SXSSFCell cell = row.createCell(cellCnt++);

					// 헤더
					if (headerYn && rowCnt == 1) {
						try {
							// 배경색
							//formatCs.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
							formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
							formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

							// 가로 세로 가운데 정렬
							formatCs.setAlignment(HorizontalAlignment.CENTER);
							formatCs.setVerticalAlignment(VerticalAlignment.CENTER);

							// 헤더에 맞게 너비 조정
							sheet.setColumnWidth(cellCnt - 1, (obj.toString().length() + 2) * 512);

							// 폰트 굵게, 폰트색 변경
							formatFont.setColor(IndexedColors.WHITE.getIndex());
							formatFont.setBold(true);

							// 스타일 적용
							formatCs.setFont(formatFont);
							cell.setCellStyle(formatCs);
						} catch (Exception e) {
						}
					}

					if (arrayColorCellNum != null) {
						for (String s : arrayColorCellNum) {
							if (cellCnt == Integer.parseInt(s) && rowCnt > 1) {
								formatCs2.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
								formatCs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
								cell.setCellStyle(formatCs2);
							}
						}
					}
					// 셀 적용
					if (obj != null) {
						if (obj instanceof Number) {
							cell.setCellValue(((Number) obj).doubleValue());  // 숫자형 처리
						} else if (obj instanceof Date) {
							cell.setCellValue((Date) obj);  // 날짜 처리
							// 날짜 셀 스타일 적용이 필요할 경우 여기에 추가
						} else {
							cell.setCellValue(obj.toString());  // 문자열 처리
						}
					} else {
						cell.setCellValue("");  // null 안전 처리
					}
				}
			}

			saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", " ");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName);

			// 파일 다운로드
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error("[템플릿 없는 엑셀 다운로드 Error]", e);
			throw e;
		} finally {
			try {
				// 디스크 적었던 임시파일을 제거합니다.
				if (wb != null) {
					wb.dispose();
					wb.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	/**
	 * 엑셀 리스트 반환
	 */
	public List<List<String>> getExcelList(MultipartFile excelFile)
		throws IOException, InvalidFormatException {

		return getExcelList(excelFile, 0);
	}

	/**
	 * 엑셀 리스트 반환
	 *
	 * headerLocation : 엑셀 좌측 왼쪽 숫자 입력
	 */
	public List<List<String>> getExcelList(MultipartFile excelFile, int headerLocation)
		throws IOException, InvalidFormatException {

		Workbook workbook = null;
		OPCPackage opcPackage = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		try {
			// 업로드 파일
			try {
				// .xlsx
				opcPackage = OPCPackage.open(excelFile.getInputStream());
				workbook = new XSSFWorkbook(opcPackage);
			} catch (Exception e) {
				// .xlsx
				workbook = new HSSFWorkbook(excelFile.getInputStream());
			}
			sheet = workbook.getSheetAt(0);
			List<List<String>> valueList = new ArrayList<>();

			int maxRowLength = 0;
			try {
				if (maxRowLength < sheet.getRow(0).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(0).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(1).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(1).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(2).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(2).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(3).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(3).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(4).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(4).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(5).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(5).getPhysicalNumberOfCells();
				}
			} catch (Exception e) {
			}

			if (headerLocation != 0) {
				headerLocation = headerLocation - 1;
				maxRowLength = sheet.getRow(headerLocation).getPhysicalNumberOfCells();
			}
			// 상품 가공
			for (int i = headerLocation; i < sheet.getPhysicalNumberOfRows(); i++) {

				row = sheet.getRow(i);
				List<String> tmpList = new ArrayList<>();

				for (int j = 0; j < maxRowLength; j++) {

					try {
						cell = row.getCell(j);

						String str = cell.getStringCellValue()
							.replace(" ", "")
							.replace("\t", "")
							.replace("　", "")
							.replace("\n", "")
							.replace("\r", "");
						if (!"".equals(str)) {
							tmpList.add(cell.getStringCellValue());
						} else {
							tmpList.add(null);
						}
					} catch (Exception e1) {
						try {
							// 숫자면 소숫점 정리
							if (cell.getNumericCellValue() == (long) cell.getNumericCellValue()) {
								tmpList.add(String.format("%d", (long) cell.getNumericCellValue()));
							} else {
								tmpList.add(String.format("%s", cell.getNumericCellValue()));
							}
						} catch (Exception e2) {
							try {
								tmpList.add(String.valueOf(cell.getBooleanCellValue()));
							} catch (Exception e3) {
								tmpList.add(null);
							}
						}
					}
				}
				boolean status = false;
				// null 체크
				for (String str : tmpList) {
					if (str != null) {
						status = true;
						break;
					}
				}
				// null이 아니면 더한다.
				if (status) {
					valueList.add(tmpList);
				}
			}

			return valueList;

		} catch (Exception e) {
			log.error("[엑셀 리스트 변환 Error]", e);
			throw e;
		} finally {
			try {
				// OPCPackage 닫기
				if (opcPackage != null) {
					opcPackage.close();
				}
				// WorkBook 닫기
				if (workbook != null) {
					workbook.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public List<List<String>> getExcelListForBulkUploadBatch(InputStream is, int headerLocation)
		throws IOException, InvalidFormatException {

		Workbook workbook = null;
		OPCPackage opcPackage = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		try {
			// 업로드 파일
			try {
				// .xlsx
				opcPackage = OPCPackage.open(is);
				workbook = new XSSFWorkbook(opcPackage);
			} catch (Exception e) {
				// .xlsx
				workbook = new HSSFWorkbook(is);
			}
			sheet = workbook.getSheetAt(0);
			List<List<String>> valueList = new ArrayList<>();

			int maxRowLength = 0;
			try {
				if (maxRowLength < sheet.getRow(0).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(0).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(1).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(1).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(2).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(2).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(3).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(3).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(4).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(4).getPhysicalNumberOfCells();
				}
				if (maxRowLength < sheet.getRow(5).getPhysicalNumberOfCells()) {
					maxRowLength = sheet.getRow(5).getPhysicalNumberOfCells();
				}
			} catch (Exception e) {
			}

			if (headerLocation != 0) {
				headerLocation = headerLocation - 1;
				maxRowLength = sheet.getRow(headerLocation).getPhysicalNumberOfCells();
			}
			// 상품 가공
			for (int i = headerLocation; i < sheet.getPhysicalNumberOfRows(); i++) {

				row = sheet.getRow(i);
				List<String> tmpList = new ArrayList<>();

				for (int j = 0; j < maxRowLength; j++) {

					try {
						cell = row.getCell(j);

						String str = cell.getStringCellValue()
							.replace(" ", "")
							.replace("\t", "")
							.replace("　", "")
							.replace("\n", "")
							.replace("\r", "");
						if (!"".equals(str)) {
							tmpList.add(cell.getStringCellValue());
						} else {
							tmpList.add(null);
						}
					} catch (Exception e1) {
						try {
							// 숫자면 소숫점 정리
							if (cell.getNumericCellValue() == (long) cell.getNumericCellValue()) {
								tmpList.add(String.format("%d", (long) cell.getNumericCellValue()));
							} else {
								tmpList.add(String.format("%s", cell.getNumericCellValue()));
							}
						} catch (Exception e2) {
							try {
								tmpList.add(String.valueOf(cell.getBooleanCellValue()));
							} catch (Exception e3) {
								tmpList.add(null);
							}
						}
					}
				}
				boolean status = false;
				// null 체크
				for (String str : tmpList) {
					if (str != null) {
						status = true;
						break;
					}
				}
				// null이 아니면 더한다.
				if (status) {
					valueList.add(tmpList);
				}
			}

			return valueList;

		} catch (Exception e) {
			log.error("[엑셀 리스트 변환 Error]", e);
			throw e;
		} finally {
			try {
				// OPCPackage 닫기
				if (opcPackage != null) {
					opcPackage.close();
				}
				// WorkBook 닫기
				if (workbook != null) {
					workbook.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public List<String> getProductCodeWithColumnA(MultipartFile file) {
		List<String> productCodes = new ArrayList<>();
		Workbook workbook = null;

		try (InputStream is = file.getInputStream()) {
			if (file.getOriginalFilename().endsWith(".xls")) {
				workbook = new HSSFWorkbook(is);
			} else if (file.getOriginalFilename().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is);
			} else {
				throw new IllegalArgumentException("The file is not an Excel file.");
			}

			Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
			for (Row row : sheet) {
				Cell cell = row.getCell(0); // A열 (0 인덱스)
				if (cell != null) {
					// 셀의 데이터 타입에 따라 처리
					switch (cell.getCellType()) {
						case STRING:
							productCodes.add(cell.getStringCellValue());
							break;
						case NUMERIC:
							productCodes.add(String.valueOf(cell.getNumericCellValue()));
							break;
						// 기타 다른 셀 타입에 대한 처리 추가 가능
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return productCodes;
	}

	// 정산용 엑셀다운로드 (시트 분할)
	public void excelDownloadForSettlement(HttpServletResponse response,
		List<List<String>> valueList, List<List<String>> valueList2,
		String saveFileName, boolean headerYn, String colorCellNum) throws IOException {

		SXSSFWorkbook wb = null;
		String[] arrayColorCellNum = null;
		if (colorCellNum != "") {
			arrayColorCellNum = colorCellNum.split(",");
		}

		try {
			// 메모리에 100개의 행을 유지합니다. 행의 수가 넘으면 디스크에 적습니다.
			wb = new SXSSFWorkbook(100);
			SXSSFSheet sheet = wb.createSheet("조회결과요약");
			CellStyle formatCs = wb.createCellStyle();
			CellStyle formatCs2 = wb.createCellStyle();
			Font formatFont = wb.createFont();

			// SXSSF에서 Column 자동 길이조정 사용
			// INFO 로그 도배로 미사용
			// sheet.trackAllColumnsForAutoSizing();

			int rowCnt = 0;
			for (List<String> value : valueList) {
				int cellCnt = 0;

				SXSSFRow row = sheet.createRow(rowCnt++);
				for (String str : value) {

					SXSSFCell cell = row.createCell(cellCnt++);

					// 헤더
					if (headerYn && rowCnt == 1) {
						try {
							// 배경색
							//formatCs.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
							formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
							formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

							// 가로 세로 가운데 정렬
							formatCs.setAlignment(HorizontalAlignment.CENTER);
							formatCs.setVerticalAlignment(VerticalAlignment.CENTER);

							// 헤더에 맞게 너비 조정
							sheet.setColumnWidth(cellCnt - 1, (str.length() + 2) * 512);

							// 폰트 굵게, 폰트색 변경
							formatFont.setColor(IndexedColors.WHITE.getIndex());
							formatFont.setBold(true);

							// 스타일 적용
							formatCs.setFont(formatFont);
							cell.setCellStyle(formatCs);
						} catch (Exception e) {
						}
					}

					if (arrayColorCellNum != null) {
						for (String s : arrayColorCellNum) {
							if (cellCnt == Integer.parseInt(s) && rowCnt > 1) {
								formatCs2.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
								formatCs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
								cell.setCellStyle(formatCs2);
							}
						}
					}
					// 셀 적용
					if (str != null && !"".equals(str)) {
						cell.setCellValue(str);
					}
				}
			}

			rowCnt = 0;
			SXSSFSheet sheet2 = wb.createSheet("조회결과상세");
			for (List<String> value : valueList2) {
				int cellCnt = 0;

				SXSSFRow row = sheet2.createRow(rowCnt++);
				for (String str : value) {

					SXSSFCell cell = row.createCell(cellCnt++);

					// 헤더
					if (headerYn && rowCnt == 1) {
						try {
							// 배경색
							//formatCs.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
							formatCs.setFillForegroundColor(IndexedColors.BLACK.getIndex());
							formatCs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

							// 가로 세로 가운데 정렬
							formatCs.setAlignment(HorizontalAlignment.CENTER);
							formatCs.setVerticalAlignment(VerticalAlignment.CENTER);

							// 헤더에 맞게 너비 조정
							sheet2.setColumnWidth(cellCnt - 1, (str.length() + 2) * 512);

							// 폰트 굵게, 폰트색 변경
							formatFont.setColor(IndexedColors.WHITE.getIndex());
							formatFont.setBold(true);

							// 스타일 적용
							formatCs.setFont(formatFont);
							cell.setCellStyle(formatCs);
						} catch (Exception e) {
						}
					}

					if (arrayColorCellNum != null) {
						for (String s : arrayColorCellNum) {
							if (cellCnt == Integer.parseInt(s) && rowCnt > 1) {
								formatCs2.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
								formatCs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
								cell.setCellStyle(formatCs2);
							}
						}
					}
					// 셀 적용
					if (str != null && !"".equals(str)) {
						cell.setCellValue(str);
					}
				}
			}

			saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", " ");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName);

			// 파일 다운로드
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error("[템플릿 없는 엑셀 다운로드 Error]", e);
			throw e;
		} finally {
			try {
				// 디스크 적었던 임시파일을 제거합니다.
				if (wb != null) {
					wb.dispose();
					wb.close();
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	//엑셀 파일 반환
	public File createExcelFile(
	        Class<?> voClass,
	        String saveFileName,
	        List<? extends Object> listVo
	) throws Exception {

	    Object voOne = listVo.get(0);
	    Map<String, Object> dataOne = convertToMap(voOne);
	    List<Map<String, Object>> dataList = convertToListOfMaps(listVo);

	    String[] dataKeys = dataOne.keySet().toArray(new String[dataOne.size()]);
	    List<String> headers = ExcelDownloadUtil.generateHeaders(voClass, dataKeys);

	    SXSSFWorkbook wb = null;
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String fileName = saveFileName + "_" + timestamp + ".xlsx";

		File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
		FileOutputStream fos = new FileOutputStream(tempFile);

	    try {
	        wb = new SXSSFWorkbook(100);
	        SXSSFSheet sheet = wb.createSheet("Result");

	        CellStyle headerStyle = wb.createCellStyle();
	        Font headerFont = wb.createFont();
	        headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        headerFont.setColor(IndexedColors.WHITE.getIndex());
	        headerFont.setBold(true);
	        headerStyle.setFont(headerFont);

	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.size(); i++) {
	            Cell cell = headerRow.createCell(i);
	            sheet.setColumnWidth(i, (headers.get(i).length() + 2) * 512);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(headers.get(i));
	        }

	        CellStyle wrapStyle = wb.createCellStyle();
	        wrapStyle.setWrapText(true);
	        boolean isCarriageReturn = false;

	        for (int i = 0; i < dataList.size(); i++) {
	            Map<String, Object> dataMap = dataList.get(i);
	            Row dataRow = sheet.createRow(i + 1);
	            int cellIndex = 0;
	            for (String header : dataKeys) {
	                Cell cell = dataRow.createCell(cellIndex);
	                Object value = dataMap.get(header);
	                if (value != null) {
	                    if (value instanceof Date) {
	                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                        cell.setCellValue(dateFormat.format((Date) value));
	                    } else {
	                        String str = value.toString();
	                        if (str.contains("^|^")) {
	                            isCarriageReturn = true;
	                            cell.setCellStyle(wrapStyle);
	                            cell.setCellValue(str.replaceAll("\\^\\|\\^", ""));
	                        } else {
	                            cell.setCellValue(str);
	                        }
	                    }
	                }
	                cellIndex++;
	            }
	        }

	        // 파일 저장
	        fos = new FileOutputStream(tempFile);
	        wb.write(fos);
	        fos.flush();

	        return tempFile;

	    } finally {
	        if (fos != null) fos.close();
	        if (wb != null) {
	            wb.dispose();
	            wb.close();
	        }
	    }
	}

	public static File encryptExcel(File inputFile, String password) throws Exception {
		File encryptedFile = new File(inputFile.getParent(), inputFile.getName());

		try (
			POIFSFileSystem fs = new POIFSFileSystem();
			FileInputStream fis = new FileInputStream(inputFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis)
		) {
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
			Encryptor encryptor = info.getEncryptor();
			encryptor.confirmPassword(password);

			try (OutputStream os = encryptor.getDataStream(fs)) {
				workbook.write(os);
			}

			try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
				fs.writeFilesystem(fos);
			}
		}

		return encryptedFile;
	}

}
