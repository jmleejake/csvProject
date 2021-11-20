package jp.prj.araku.tanpin.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.file.vo.ClickPostVO;
import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tanpin.mapper.ITanpinMapper;
import jp.prj.araku.tanpin.vo.TanpinVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class TanpinDAO {
	private Logger log = LoggerFactory.getLogger("arakuLog");
	
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<TanpinVO> getTanpinInfo(String select_type) {
		TanpinVO vo = new TanpinVO();
		vo.setSelect_type(select_type);
		return getTanpinInfo(vo);
	}
	
	public ArrayList<TanpinVO> getTanpinInfo(TanpinVO vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		ArrayList<TanpinVO> ret = mapper.getTanpinInfo(vo);
		if(!("dealer".equals(vo.getSelect_type()) || "maker".equals(vo.getSelect_type()))) {
			for(TanpinVO tmp : ret) {
				if(null != tmp.getInprice() && null != tmp.getPrice()) {
					tmp.setInprice(tmp.getInprice());
					// "¥"
					tmp.setPrice(tmp.getPrice());
				}
			}
		}
		return ret;
	}
	
	public void manipulateTanpinInfo(
			HttpServletRequest req, 
			MultipartFile file, 
			String fileEncoding) throws IOException {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + file.getContentType());
		log.debug("name: " + file.getName());
		log.debug("original name: " + file.getOriginalFilename());
		log.debug("size: " + file.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), fileEncoding));
			
			CsvToBean<TanpinVO> csvToBean = new CsvToBeanBuilder<TanpinVO>(reader)
                    .withType(TanpinVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<TanpinVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	TanpinVO vo = iterator.next();
            	// JANコード 있으면 update, 없으면 insert
            	if(null != vo.getPrd_cd() && !vo.getPrd_cd().equals("")) {
            		ArrayList<TanpinVO> search = getTanpinInfo(vo);
            		if(search.size() == 1) {
            			vo.setSeq_id(search.get(0).getSeq_id());
            			mapper.updateTanpinInfo(vo);
            		}else {
            			mapper.insertTanpinInfo(vo);
            		}
            	}
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void deleteTanpinInfo(ArrayList<TanpinVO> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		
		for(TanpinVO vo : list) {
			mapper.deleteTanpinInfo(vo.getSeq_id());
		}
	}
	
	public void downloadTanpinInfo(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException  {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			TanpinVO tan = new TanpinVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for(String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			tan.setSeq_id_list(seq_id_list);
			
			String csvFileName = "TAN" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			response.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(response.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			String[] header = {
					"商品メーカー"
					, "商品メーカー名"
					, "商品コード(JAN)"
					, "商品名"
					, "容量"
					, "取引先code"
					, "取引先会社名"
					, "仕入金額"
					, "販売金額"
					, "商品販売基準金額"
					, "商品税(抜、込)"
					, "商品税率"
			};
			
			ArrayList<TanpinVO> searchList = mapper.getTanpinInfo(tan);
			ArrayList<ArakuVO> list = new ArrayList<>();
			for(TanpinVO vo : searchList) {
				list.add(vo);
			}
			CommonUtil.executeCSVDownload(csvWriter, writer, header, list);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public ArrayList<TanpinVO> addTanpin(TanpinVO tanpin) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		mapper.insertTanpinInfo(tanpin);
		return getTanpinInfo("");
	}
	
	public ArrayList<TanpinVO> modTanpin(ArrayList<TanpinVO> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		for(TanpinVO tanpin : list) {
			mapper.updateTanpinInfo(tanpin);
		}
		return getTanpinInfo("");
	}
	
	public void downloadOrderForm(HttpServletResponse response
			, String id, String fileEncoding) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			XSSFRow row = null;
			XSSFCell cell = null;
			
			/*
			Map<Integer,String> headerList = new HashMap<Integer,String>();
			headerList.put(0, "お届け先郵便番号");
			headerList.put(1, "お届け先氏名");
			headerList.put(2, "お届け先敬称");
			headerList.put(3, "お届け先住所1行目");
			headerList.put(4, "お届け先住所2行目");
			headerList.put(5, "お届け先住所3行目");
			headerList.put(6, "お届け先住所4行目");
			headerList.put(7, "内容品");
			
			for(int i1=0; i1<subList.size(); i1++) {
				List<ClickPostVO> ll = subList.get(i1);
				sheet = workbook.createSheet("CLICKPOST-DATA"+(i1+1));
				// 데이터의 크기만큼 row생성
				for(int i2=0; i2<ll.size()+1; i2++) {
					row = sheet.createRow((short)i2);
					// headerList의 크기만큼
					for(int i3=0; i3<headerList.size(); i3++) {
						cell = row.createCell(i3);
						// 맨 윗줄은 헤더
						if(i2==0) {
							cell.setCellValue(headerList.get(i3));
						}else {
							// 헤더 아래부터는 데이터세팅
							Map<Integer,String> dataList = new HashMap<Integer,String>();
							ClickPostVO cData = ll.get(i2-1);
							dataList.put(0, cData.getPost_no());
							dataList.put(1, cData.getDelivery_name());
							dataList.put(2, cData.getDelivery_name_title());
							dataList.put(3, cData.getDelivery_add1());
							dataList.put(4, cData.getDelivery_add2());
							dataList.put(5, cData.getDelivery_add3());
							dataList.put(6, cData.getDelivery_add4());
							dataList.put(7, cData.getDelivery_contents());
							cell.setCellValue(dataList.get(i3));
						}
					}
				}
			}
			*/
			IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			invenVO.setDealer_id(id);
			ArrayList<JaikoPrdInventoryVO> list = invenMapper.getJaikoPrdInventory(invenVO);
			
			ITabletPrdMapper dealerMapper = sqlSession.getMapper(ITabletPrdMapper.class);
			DealerVO dealerVO = new DealerVO();
			dealerVO.setDealer_id(id);
			ArrayList<DealerVO> dealerInfo = dealerMapper.getDealerInfo(dealerVO);
			
			//셀 스타일 및 폰트 설정
			CellStyle title = workbook.createCellStyle();
			//정렬
			title.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
			title.setVerticalAlignment(VerticalAlignment.CENTER); //높이 가운데 정렬
			//테두리 선 (위,아래)
			title.setBorderBottom(BorderStyle.THICK);
			title.setBorderTop(BorderStyle.THICK);
			//폰트 설정
			Font titleFont = workbook.createFont();
			titleFont.setFontHeight((short)(20*20)); //사이즈
			titleFont.setBold(true); // 볼드체
			title.setFont(titleFont);
			
			if(list.size() > 10) {
				int i = list.size()/10;
				int ii = list.size()%10;
				int[] arr = new int[i];
				if(ii > 0) {
					arr = new int[i+1];
				}
				
				List<List<JaikoPrdInventoryVO>> subList = new ArrayList<>();
				
				for(int j=0; j<arr.length; j++) {
					arr[j] = (j+1)*10;
				}
				
				for(int k=0; k<arr.length; k++) {
					if(k==0) {
						subList.add(list.subList(0, arr[k]));
						continue;
					}
					
					if(arr[k] > list.size()) {
						subList.add(list.subList(k*arr[k-1], list.size()));
					}else {
						subList.add(list.subList(k*arr[k-1], arr[k]));
					}
				}
				
				for(int i1=0; i1<subList.size(); i1++) {
					sheet = workbook.createSheet("シート"+(i1+1));
					// 컬럼너비 세팅
					sheet.setDefaultColumnWidth(12);
					// 눈금선 없애기
					sheet.setDisplayGridlines(false);
					row = sheet.createRow(0);
					
					// 타이틀세팅
					row = sheet.createRow(1);
					sheet.addMergedRegion(new CellRangeAddress(1,2,0,8));
					cell = row.createCell(0);
					cell.setCellStyle(title);
					cell.setCellValue("発注書");
					for(int c=1; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(title);
					}
					row = sheet.createRow(2);
					for(int c=0; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(title);
					}
					row = sheet.createRow(3);
					
					CellStyle allLine = workbook.createCellStyle();
					allLine.setBorderBottom(BorderStyle.THICK);
					allLine.setBorderTop(BorderStyle.THICK);
					allLine.setBorderLeft(BorderStyle.THICK);
					allLine.setBorderRight(BorderStyle.THICK);
					
					CellStyle centerAllLine = workbook.createCellStyle();
					centerAllLine.setBorderBottom(BorderStyle.THICK);
					centerAllLine.setBorderTop(BorderStyle.THICK);
					centerAllLine.setBorderLeft(BorderStyle.THICK);
					centerAllLine.setBorderRight(BorderStyle.THICK);
					centerAllLine.setAlignment(HorizontalAlignment.CENTER);
					centerAllLine.setVerticalAlignment(VerticalAlignment.CENTER);
					
					row = sheet.createRow(4);
					sheet.addMergedRegion(new CellRangeAddress(4,4,7,8));
					cell = row.createCell(6);
					cell.setCellValue("発注日");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(7);
					cell.setCellValue(CommonUtil.getDate("YYYY/MM/dd", 0));
					cell.setCellStyle(allLine);
					cell = row.createCell(8);
					cell.setCellStyle(allLine);
					
					CellStyle underLine = workbook.createCellStyle();
					underLine.setBorderBottom(BorderStyle.THICK);
					row = sheet.createRow(5);
					sheet.addMergedRegion(new CellRangeAddress(5,5,0,1));
					sheet.addMergedRegion(new CellRangeAddress(5,5,7,8));
					cell = row.createCell(0);
					cell.setCellValue(dealerInfo.get(0).getDealer_nm()+" 御中");
					cell.setCellStyle(underLine);
					cell = row.createCell(1);
					cell.setCellStyle(underLine);
					cell = row.createCell(6);
					cell.setCellValue("発注者");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(7);
					cell.setCellValue("金 大元");
					cell.setCellStyle(allLine);
					cell = row.createCell(8);
					cell.setCellStyle(allLine);
					row = sheet.createRow(6);
					row = sheet.createRow(7);
					sheet.addMergedRegion(new CellRangeAddress(7,7,0,1));
					cell = row.createCell(0);
					cell.setCellValue("FAX："+dealerInfo.get(0).getDealer_fax());
					cell.setCellStyle(underLine);
					cell = row.createCell(1);
					cell.setCellStyle(underLine);
					row = sheet.createRow(8);
					sheet.addMergedRegion(new CellRangeAddress(8,8,5,8));
					cell = row.createCell(5);
					cell.setCellValue("有限会社アイテムピアジャパン");
					row = sheet.createRow(9);
					sheet.addMergedRegion(new CellRangeAddress(9,9,5,8));
					cell = row.createCell(5);
					cell.setCellValue("〒333－0845");
					row = sheet.createRow(10);
					sheet.addMergedRegion(new CellRangeAddress(10,10,0,3));
					sheet.addMergedRegion(new CellRangeAddress(10,10,5,8));
					cell = row.createCell(0);
					cell.setCellValue("いつも大変お世話になっております。");
					cell = row.createCell(5);
					cell.setCellValue("埼玉県川口市上青木西1-19-39");
					row = sheet.createRow(11);
					sheet.addMergedRegion(new CellRangeAddress(11,11,0,3));
					sheet.addMergedRegion(new CellRangeAddress(11,11,5,8));
					cell = row.createCell(0);
					cell.setCellValue("下記内容お手配いただけますようお願い申し上げます。");
					cell = row.createCell(5);
					cell.setCellValue("滝沢ビル１階");
					row = sheet.createRow(12);
					sheet.addMergedRegion(new CellRangeAddress(12,12,5,8));
					cell = row.createCell(5);
					cell.setCellValue("TEL：048-242-3801");
					row = sheet.createRow(13);
					sheet.addMergedRegion(new CellRangeAddress(13,13,5,8));
					cell = row.createCell(5);
					cell.setCellValue("FAX：048-242-3802");
					row = sheet.createRow(14);
					
					// 거래처 정보
					row = sheet.createRow(15);
					sheet.addMergedRegion(new CellRangeAddress(15,15,1,8));
					cell = row.createCell(0);
					cell.setCellValue("納品先");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue(dealerInfo.get(0).getDestination());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(16);
					sheet.addMergedRegion(new CellRangeAddress(16,16,1,8));
					sheet.addMergedRegion(new CellRangeAddress(16,17,0,0));
					sheet.addMergedRegion(new CellRangeAddress(17,17,1,8));
					cell = row.createCell(0);
					cell.setCellValue("住所");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue("〒 "+dealerInfo.get(0).getDealer_post());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(17);
					cell = row.createCell(0);
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue(dealerInfo.get(0).getDealer_add());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(18);
					sheet.addMergedRegion(new CellRangeAddress(18,18,1,8));
					cell = row.createCell(0);
					cell.setCellValue("TEL");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue(dealerInfo.get(0).getDealer_tel());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(19);
					sheet.addMergedRegion(new CellRangeAddress(19,19,1,8));
					cell = row.createCell(0);
					cell.setCellValue("納品希望日");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue(dealerInfo.get(0).getEst_delivery_dt());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(20);
					sheet.addMergedRegion(new CellRangeAddress(20,20,1,8));
					sheet.addMergedRegion(new CellRangeAddress(20,21,0,0));
					sheet.addMergedRegion(new CellRangeAddress(21,21,1,8));
					cell = row.createCell(0);
					cell.setCellValue("備考");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue("発注書No.??");
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(21);
					cell = row.createCell(0);
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellValue(dealerInfo.get(0).getRemark());
					cell.setCellStyle(allLine);
					for(int c=2; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(allLine);
					}
					row = sheet.createRow(22);
					
					row = sheet.createRow(23);
					sheet.addMergedRegion(new CellRangeAddress(23,23,0,2));
					sheet.addMergedRegion(new CellRangeAddress(23,23,4,5));
					cell = row.createCell(0);
					cell.setCellValue("商品名");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(1);
					cell.setCellStyle(allLine);
					cell = row.createCell(2);
					cell.setCellStyle(allLine);
					cell = row.createCell(3);
					cell.setCellValue("入数");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(4);
					cell.setCellValue("数量");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(5);
					cell.setCellStyle(allLine);
					cell = row.createCell(6);
					cell.setCellValue("単価");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(7);
					cell.setCellValue("合計金額");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(8);
					cell.setCellValue("備考");
					cell.setCellStyle(centerAllLine);
					List<JaikoPrdInventoryVO> innerList = subList.get(i1);
					int finalLine = 0;
					for(int i2=0; i2<innerList.size(); i2++) {
						row = sheet.createRow(i2+24);
						sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,0,2));
						sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,4,5));
						JaikoPrdInventoryVO innerVO = innerList.get(i2);
						cell = row.createCell(0);
						cell.setCellValue(innerVO.getPrd_nm());
						cell.setCellStyle(allLine);
						cell = row.createCell(1);
						cell.setCellStyle(allLine);
						cell = row.createCell(2);
						cell.setCellStyle(allLine);
						cell = row.createCell(3);
						cell.setCellValue("入数");
						cell.setCellStyle(allLine);
						cell = row.createCell(4);
						cell.setCellValue("数量");
						cell.setCellStyle(allLine);
						cell = row.createCell(5);
						cell.setCellStyle(allLine);
						cell = row.createCell(6);
						cell.setCellValue(innerVO.getPrd_unit_prc());
						cell.setCellStyle(allLine);
						cell = row.createCell(7);
						cell.setCellValue("合計金額");
						cell.setCellStyle(allLine);
						cell = row.createCell(8);
						cell.setCellValue("備考");
						cell.setCellStyle(allLine);
						finalLine = i2+24;
					}
					row = sheet.createRow(finalLine+1);
					row = sheet.createRow(finalLine+2);
					cell = row.createCell(0);
					cell.setCellValue("通信欄");
					cell.setCellStyle(centerAllLine);
					row = sheet.createRow(finalLine+3);
					cell = row.createCell(0);
					cell.setCellStyle(allLine);
					row = sheet.createRow(finalLine+4);
					cell = row.createCell(0);
					cell.setCellStyle(allLine);
					sheet.addMergedRegion(new CellRangeAddress(finalLine+2,finalLine+4,0,0));
				}
			}else {
				sheet = workbook.createSheet("シート1");
				// 컬럼너비 세팅
				sheet.setDefaultColumnWidth(12);
				// 눈금선 없애기
				sheet.setDisplayGridlines(false);
				row = sheet.createRow(0);
				
				// 타이틀세팅
				row = sheet.createRow(1);
				sheet.addMergedRegion(new CellRangeAddress(1,2,0,8));
				cell = row.createCell(0);
				cell.setCellStyle(title);
				cell.setCellValue("発注書");
				for(int c=1; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(title);
				}
				row = sheet.createRow(2);
				for(int c=0; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(title);
				}
				row = sheet.createRow(3);
				
				CellStyle allLine = workbook.createCellStyle();
				allLine.setBorderBottom(BorderStyle.THICK);
				allLine.setBorderTop(BorderStyle.THICK);
				allLine.setBorderLeft(BorderStyle.THICK);
				allLine.setBorderRight(BorderStyle.THICK);
				
				CellStyle centerAllLine = workbook.createCellStyle();
				centerAllLine.setBorderBottom(BorderStyle.THICK);
				centerAllLine.setBorderTop(BorderStyle.THICK);
				centerAllLine.setBorderLeft(BorderStyle.THICK);
				centerAllLine.setBorderRight(BorderStyle.THICK);
				centerAllLine.setAlignment(HorizontalAlignment.CENTER);
				centerAllLine.setVerticalAlignment(VerticalAlignment.CENTER);
				
				row = sheet.createRow(4);
				sheet.addMergedRegion(new CellRangeAddress(4,4,7,8));
				cell = row.createCell(6);
				cell.setCellValue("発注日");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(7);
				cell.setCellValue(CommonUtil.getDate("YYYY/MM/dd", 0));
				cell.setCellStyle(allLine);
				cell = row.createCell(8);
				cell.setCellStyle(allLine);
				
				CellStyle underLine = workbook.createCellStyle();
				underLine.setBorderBottom(BorderStyle.THICK);
				row = sheet.createRow(5);
				sheet.addMergedRegion(new CellRangeAddress(5,5,0,1));
				sheet.addMergedRegion(new CellRangeAddress(5,5,7,8));
				cell = row.createCell(0);
				cell.setCellValue(dealerInfo.get(0).getDealer_nm()+" 御中");
				cell.setCellStyle(underLine);
				cell = row.createCell(1);
				cell.setCellStyle(underLine);
				cell = row.createCell(6);
				cell.setCellValue("発注者");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(7);
				cell.setCellValue("金 大元");
				cell.setCellStyle(allLine);
				cell = row.createCell(8);
				cell.setCellStyle(allLine);
				row = sheet.createRow(6);
				row = sheet.createRow(7);
				sheet.addMergedRegion(new CellRangeAddress(7,7,0,1));
				cell = row.createCell(0);
				cell.setCellValue("FAX："+dealerInfo.get(0).getDealer_fax());
				cell.setCellStyle(underLine);
				cell = row.createCell(1);
				cell.setCellStyle(underLine);
				row = sheet.createRow(8);
				sheet.addMergedRegion(new CellRangeAddress(8,8,5,8));
				cell = row.createCell(5);
				cell.setCellValue("有限会社アイテムピアジャパン");
				row = sheet.createRow(9);
				sheet.addMergedRegion(new CellRangeAddress(9,9,5,8));
				cell = row.createCell(5);
				cell.setCellValue("〒333－0845");
				row = sheet.createRow(10);
				sheet.addMergedRegion(new CellRangeAddress(10,10,0,3));
				sheet.addMergedRegion(new CellRangeAddress(10,10,5,8));
				cell = row.createCell(0);
				cell.setCellValue("いつも大変お世話になっております。");
				cell = row.createCell(5);
				cell.setCellValue("埼玉県川口市上青木西1-19-39");
				row = sheet.createRow(11);
				sheet.addMergedRegion(new CellRangeAddress(11,11,0,3));
				sheet.addMergedRegion(new CellRangeAddress(11,11,5,8));
				cell = row.createCell(0);
				cell.setCellValue("下記内容お手配いただけますようお願い申し上げます。");
				cell = row.createCell(5);
				cell.setCellValue("滝沢ビル１階");
				row = sheet.createRow(12);
				sheet.addMergedRegion(new CellRangeAddress(12,12,5,8));
				cell = row.createCell(5);
				cell.setCellValue("TEL：048-242-3801");
				row = sheet.createRow(13);
				sheet.addMergedRegion(new CellRangeAddress(13,13,5,8));
				cell = row.createCell(5);
				cell.setCellValue("FAX：048-242-3802");
				row = sheet.createRow(14);
				
				// 거래처 정보
				row = sheet.createRow(15);
				sheet.addMergedRegion(new CellRangeAddress(15,15,1,8));
				cell = row.createCell(0);
				cell.setCellValue("納品先");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(dealerInfo.get(0).getDestination());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(16);
				sheet.addMergedRegion(new CellRangeAddress(16,16,1,8));
				sheet.addMergedRegion(new CellRangeAddress(16,17,0,0));
				sheet.addMergedRegion(new CellRangeAddress(17,17,1,8));
				cell = row.createCell(0);
				cell.setCellValue("住所");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue("〒 "+dealerInfo.get(0).getDealer_post());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(17);
				cell = row.createCell(0);
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(dealerInfo.get(0).getDealer_add());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(18);
				sheet.addMergedRegion(new CellRangeAddress(18,18,1,8));
				cell = row.createCell(0);
				cell.setCellValue("TEL");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(dealerInfo.get(0).getDealer_tel());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(19);
				sheet.addMergedRegion(new CellRangeAddress(19,19,1,8));
				cell = row.createCell(0);
				cell.setCellValue("納品希望日");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(dealerInfo.get(0).getEst_delivery_dt());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(20);
				sheet.addMergedRegion(new CellRangeAddress(20,20,1,8));
				sheet.addMergedRegion(new CellRangeAddress(20,21,0,0));
				sheet.addMergedRegion(new CellRangeAddress(21,21,1,8));
				cell = row.createCell(0);
				cell.setCellValue("備考");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue("発注書No.??");
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(21);
				cell = row.createCell(0);
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(dealerInfo.get(0).getRemark());
				cell.setCellStyle(allLine);
				for(int c=2; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(allLine);
				}
				row = sheet.createRow(22);
				
				row = sheet.createRow(23);
				sheet.addMergedRegion(new CellRangeAddress(23,23,0,2));
				sheet.addMergedRegion(new CellRangeAddress(23,23,4,5));
				cell = row.createCell(0);
				cell.setCellValue("商品名");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellStyle(allLine);
				cell = row.createCell(2);
				cell.setCellStyle(allLine);
				cell = row.createCell(3);
				cell.setCellValue("入数");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(4);
				cell.setCellValue("数量");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(5);
				cell.setCellStyle(allLine);
				cell = row.createCell(6);
				cell.setCellValue("単価");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(7);
				cell.setCellValue("合計金額");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(8);
				cell.setCellValue("備考");
				cell.setCellStyle(centerAllLine);
				int finalLine = 0;
				for(int i2=0; i2<list.size(); i2++) {
					row = sheet.createRow(i2+24);
					sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,0,2));
					sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,4,5));
					JaikoPrdInventoryVO innerVO = list.get(i2);
					cell = row.createCell(0);
					cell.setCellValue(innerVO.getPrd_nm());
					cell.setCellStyle(allLine);
					cell = row.createCell(1);
					cell.setCellStyle(allLine);
					cell = row.createCell(2);
					cell.setCellStyle(allLine);
					cell = row.createCell(3);
					cell.setCellValue("入数");
					cell.setCellStyle(allLine);
					cell = row.createCell(4);
					cell.setCellValue("数量");
					cell.setCellStyle(allLine);
					cell = row.createCell(5);
					cell.setCellStyle(allLine);
					cell = row.createCell(6);
					cell.setCellValue(innerVO.getPrd_unit_prc());
					cell.setCellStyle(allLine);
					cell = row.createCell(7);
					cell.setCellValue("合計金額");
					cell.setCellStyle(allLine);
					cell = row.createCell(8);
					cell.setCellValue("備考");
					cell.setCellStyle(allLine);
					finalLine = i2+24;
				}
				row = sheet.createRow(finalLine+1);
				row = sheet.createRow(finalLine+2);
				cell = row.createCell(0);
				cell.setCellValue("通信欄");
				cell.setCellStyle(centerAllLine);
				row = sheet.createRow(finalLine+3);
				cell = row.createCell(0);
				cell.setCellStyle(allLine);
				row = sheet.createRow(finalLine+4);
				cell = row.createCell(0);
				cell.setCellStyle(allLine);
				sheet.addMergedRegion(new CellRangeAddress(finalLine+2,finalLine+4,0,0));
			}
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment;filename=\"%s\"",
					"HaccyuSho" + CommonUtil.getDate("YYYYMMdd", 0)+".xlsx");
			response.setCharacterEncoding(fileEncoding);
			response.setHeader(headerKey, headerValue);
			workbook.write(response.getOutputStream());
			workbook.close();
		}catch (IOException e) {}
	}

}
