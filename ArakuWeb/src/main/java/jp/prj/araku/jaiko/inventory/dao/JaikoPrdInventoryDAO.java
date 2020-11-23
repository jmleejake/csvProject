package jp.prj.araku.jaiko.inventory.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.InventoryDownVO;
import jp.prj.araku.jaiko.inventory.vo.InventoryUpVO;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class JaikoPrdInventoryDAO {
	@Autowired
	SqlSession sqlSession;
	
	private Logger log = LoggerFactory.getLogger("jaikoLog");
	
	public ArrayList<JaikoPrdInventoryVO> getJaikoPrdInventory(JaikoPrdInventoryVO vo) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		return mapper.getJaikoPrdInventory(vo);
	}
	
	public ArrayList<JaikoPrdInventoryVO> manipulatePrdInventory(ArrayList<JaikoPrdInventoryVO> list) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		for(JaikoPrdInventoryVO vo : list) {
			if(vo.getSeq_id() != null) {
				mapper.updateJaikoPrdInventory(vo);
			}else {
				mapper.insertJaikoPrdInventory(vo);
			}
		}
		return mapper.getJaikoPrdInventory(new JaikoPrdInventoryVO());
	}
	
	public ArrayList<JaikoPrdInventoryVO> deleteJaikoPrdInventory(ArrayList<JaikoPrdInventoryVO> list) {
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		for(JaikoPrdInventoryVO vo : list) {
			mapper.deleteJaikoPrdInventory(vo.getSeq_id());
		}
		return mapper.getJaikoPrdInventory(new JaikoPrdInventoryVO());
	}
	
	public void processProductInventory(MultipartFile upload, String fileEncoding) throws IOException {
		log.debug("processProductInventory");
		
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		IJaikoPrdInfoMapper prdInfoMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + upload.getContentType());
		log.debug("name: " + upload.getName());
		log.debug("original name: " + upload.getOriginalFilename());
		log.debug("size: " + upload.getSize());
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(upload.getInputStream(), fileEncoding));
			
			CsvToBean<InventoryUpVO> csvToBean = new CsvToBeanBuilder<InventoryUpVO>(reader)
                    .withType(InventoryUpVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
			Iterator<InventoryUpVO> iterator = csvToBean.iterator();
			while(iterator.hasNext()) {
				InventoryUpVO vo = iterator.next();
				
				JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
				invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				invenVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoPrdInventoryVO> invenSrchRet = mapper.getJaikoPrdInventory(invenVO);
				invenVO.setPrd_nm(vo.getPrd_nm());
				invenVO.setPrd_qty(vo.getPrd_qty());
				invenVO.setPrd_case(vo.getPrd_case());
				invenVO.setPrd_bara(vo.getPrd_bara());
				invenVO.setNow_prd_cnt(vo.getNow_prd_cnt());
				invenVO.setExp_dt(vo.getExp_dt());
				invenVO.setSell_prc(vo.getSell_prc());
				if(invenSrchRet.size() > 0) {
					invenVO.setSeq_id(invenSrchRet.get(0).getSeq_id());
					mapper.updateJaikoPrdInventory(invenVO);
				}else {
					mapper.insertJaikoPrdInventory(invenVO);
				}
				
				JaikoPrdInfoVO pInfoVO = new JaikoPrdInfoVO();
				pInfoVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				pInfoVO.setJan_cd(vo.getJan_cd());
				ArrayList<JaikoPrdInfoVO> prdSrchRet = prdInfoMapper.getJaikoPrdInfo(pInfoVO);
				pInfoVO.setPrd_nm(vo.getPrd_nm());
				pInfoVO.setStd_info(vo.getStd_info());
				pInfoVO.setPrd_cnt("1");
				pInfoVO.setPrd_unit_prc(vo.getPrd_unit_prc());
				pInfoVO.setTax_incld(vo.getTax_incl());
				pInfoVO.setTax_rt(vo.getTax_rt());
				if(prdSrchRet.size() > 0) {
					pInfoVO.setSeq_id(prdSrchRet.get(0).getSeq_id());
					prdInfoMapper.updateJaikoPrdInfo(pInfoVO);
				}else {
					prdInfoMapper.insertJaikoPrdInfo(pInfoVO);
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void prdInventoryDownload(HttpServletResponse response, String fileEncoding)
	throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		log.debug("prdInventoryDownload");
		log.debug("encoding : " + fileEncoding);
		IJaikoPrdInventoryMapper mapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
			
		ArrayList<JaikoPrdInventoryVO> targetList = mapper.getJaikoInventoryDownList();
		ArrayList<InventoryDownVO> list = new ArrayList<InventoryDownVO>();
		for(JaikoPrdInventoryVO vo : targetList) {
			InventoryDownVO invenDown = new InventoryDownVO();
			invenDown.setJan_cd(vo.getJan_cd());
			invenDown.setPrd_nm(vo.getPrd_nm());
			invenDown.setPrd_bara(vo.getPrd_bara());
			invenDown.setPrd_case(vo.getPrd_case());
			invenDown.setPrd_qty(vo.getPrd_qty());
			invenDown.setPrd_unit_prc(vo.getPrd_unit_prc());
			invenDown.setSell_prc(vo.getSell_prc());
			invenDown.setStd_info(vo.getStd_info());
			invenDown.setExp_dt(vo.getExp_dt());
			list.add(invenDown);
		}
		
		List<List<InventoryDownVO>> subList = new ArrayList<>();
		if(list.size() > 30) {
			int i = list.size() / 30;
			int[] arr = new int[i+1];
			
			for(int j=0; j<arr.length; j++) {
				arr[j] = (j+1)*30;
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
		}else {
			subList.add(list);
		}
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			XSSFRow row = null;
			XSSFCell cell = null;
			Map<Integer,String> headerList = new HashMap<Integer,String>();
			headerList.put(0, "SNO.");
			headerList.put(1, "ＪＡＮコード");
			headerList.put(2, "商品名");
			headerList.put(3, "規格");
			headerList.put(4, "入数");
			headerList.put(5, "ケース");
			headerList.put(6, "バラ");
			headerList.put(7, "賞味期限");
			headerList.put(8, "原価");
			headerList.put(9, "売価(本体)");
			headerList.put(10, "備考");
			
			for(int i1=0; i1<subList.size(); i1++) {
				sheet = workbook.createSheet("JAIKOINVENTORY-DATA"+(i1+1));
				row = sheet.createRow(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 9));
				row.createCell(0).setCellValue("");
				row.createCell(1).setCellValue("");
				CellStyle ss = workbook.createCellStyle();
				Font ff = workbook.createFont();
				ff.setFontHeightInPoints((short)20);
				ss.setFont(ff);
				ss.setAlignment(HorizontalAlignment.CENTER);
				ss.setVerticalAlignment(VerticalAlignment.CENTER);
				Cell cl = row.createCell(2);
				cl.setCellValue("棚　　卸　　表");
				cl.setCellStyle(ss);
				
				CellStyle ss3 = workbook.createCellStyle();
				ss3.setAlignment(HorizontalAlignment.RIGHT);
				ss3.setVerticalAlignment(VerticalAlignment.CENTER);
				Cell cl3 = row.createCell(10);
				cl3.setCellValue("株式会社XXX");
				cl3.setCellStyle(ss3);
				row.setHeight((short)1000);
				
				row = sheet.createRow(1);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 10));
				row.createCell(0).setCellValue("棚卸担当者");
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("調査場所");
				row.createCell(4).setCellValue("");
				row.createCell(6).setCellValue("実施日");
				row.createCell(7).setCellValue("");
				row.setHeight((short)600);
				
				List<InventoryDownVO> ll = subList.get(i1);
				// 데이터의 크기만큼 row생성
				for(int i2=2; i2<33; i2++) {
					row = sheet.createRow((short)i2);
					// headerList의 크기만큼
					for(int i3=0; i3<headerList.size(); i3++) {
						CellStyle ss5 = workbook.createCellStyle();
						ss5.setBorderBottom(BorderStyle.THICK);
						cell = row.createCell(i3);
						// 맨 윗줄은 헤더
						if(i2==2) {
							cell.setCellValue(headerList.get(i3));
							cell.setCellStyle(ss5);
						}else {
							// 헤더 아래부터는 데이터세팅
							Map<Integer,String> dataList = new HashMap<Integer,String>();
							dataList.put(0, String.valueOf(i2-2));
							if(ll.size() >= (i2-2)) {
								InventoryDownVO data = ll.get(i2-3);
								dataList.put(1, data.getJan_cd());
								dataList.put(2, data.getPrd_nm());
								dataList.put(3, data.getStd_info());
								dataList.put(4, data.getPrd_qty());
								dataList.put(5, data.getPrd_case());
								dataList.put(6, data.getPrd_bara());
								dataList.put(7, data.getExp_dt());
								dataList.put(8, data.getPrd_unit_prc());
								dataList.put(9, data.getSell_prc());
								dataList.put(10, data.getEtc_note());
							}else {
								dataList.put(1, "");
								dataList.put(2, "");
								dataList.put(3, "");
								dataList.put(4, "");
								dataList.put(5, "");
								dataList.put(6, "");
								dataList.put(7, "");
								dataList.put(8, "");
								dataList.put(9, "");
								dataList.put(10, "");
							}
							cell.setCellValue(dataList.get(i3));
						}
						sheet.autoSizeColumn(0);
						sheet.autoSizeColumn(1);
						sheet.autoSizeColumn(2);
						sheet.setColumnWidth(7, 12*256);
						sheet.setColumnWidth(10, 15*256);
					}
					row.setHeight((short)400);
				}
				row = sheet.createRow(33);
				CellRangeAddress merged = new CellRangeAddress(33, 35, 0, 10);
				sheet.addMergedRegion(merged);
				RegionUtil.setBorderTop(BorderStyle.THICK, merged, sheet);
				CellStyle ss2 = workbook.createCellStyle();
				ss2.setVerticalAlignment(VerticalAlignment.TOP);
				ss2.setBorderTop(BorderStyle.THICK);
				Cell cl2 = row.createCell(0);
				cl2.setCellValue("メモ");
				cl2.setCellStyle(ss2);
				row.setHeight((short)800);
				row = sheet.createRow(36);
				row.createCell(0).setCellValue("");
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("");
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue("");
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue("");
				row.createCell(9).setCellValue("");
				CellStyle ss4 = workbook.createCellStyle();
				ss4.setAlignment(HorizontalAlignment.RIGHT);
				ss4.setVerticalAlignment(VerticalAlignment.BOTTOM);
				Cell cl4 = row.createCell(10);
				cl4.setCellValue("P. "+(i1+1)+"／"+subList.size());
				cl4.setCellStyle(ss4);
				row.setHeight((short)400);
			}
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment;filename=\"%s\"",
					"JAIKOINVENTORY_" + CommonUtil.getDate("YYYYMMdd", 0)+".xlsx");
			response.setCharacterEncoding(fileEncoding);
			response.setHeader(headerKey, headerValue);
			workbook.write(response.getOutputStream());
			workbook.close();
		}finally {
			
		}
			
	}
}
