package jp.prj.araku.product.dao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.product.mapper.IEstimateMapper;
import jp.prj.araku.product.vo.EstimateVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class EstimateDAO {
	@Autowired
	SqlSession sqlSession;
	
	public ArrayList<EstimateVO> selectStatus(EstimateVO vo) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		return mapper.selectStatus(vo);
	}
	
	public ArrayList<EstimateVO> manupulateStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		String partId="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			if(null != vo.getSeq_id() && !"".equals(vo.getSeq_id())) {
				mapper.updateStatus(vo);
			}else {
				mapper.insertStatus(vo);
			}
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
	
	public ArrayList<EstimateVO> deleteStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		String partId="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			mapper.deleteStatus(vo.getSeq_id());
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
	
	public ArrayList<EstimateVO> createStatus(ArrayList<EstimateVO> list) {
		IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
		IJaikoPrdInfoMapper jaikoPrdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		String partId="";
		String partNm="";
		for(EstimateVO vo : list) {
			partId = vo.getPartner_id();
			partNm = vo.getPartner_nm();
			double per = vo.getPercent();
			
			if(per == (double)0) {
				ArrayList<EstimateVO> srchStatus = mapper.selectStatus(vo);
				
				if(srchStatus.size() > 0) {
					vo.setSeq_id(srchStatus.get(0).getSeq_id());
					mapper.updateStatus(vo);
				}else {
					mapper.insertStatus(vo);
				}
			}else {
				JaikoPrdInfoVO prdVO = new JaikoPrdInfoVO();
				prdVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				ArrayList<JaikoPrdInfoVO> prdList = jaikoPrdMapper.getJaikoPrdInfo(prdVO);
				
				double prd_prc = 0;
				for(JaikoPrdInfoVO prd : prdList) {
					vo = new EstimateVO();
					int prc = Integer.parseInt(prd.getPrd_unit_prc());
					prd_prc = prc*(per/(double)100);
					prd_prc = prc + prd_prc;
					
					vo.setPartner_id(partId);
					vo.setPartner_nm(partNm);
					vo.setJan_cd(prd.getJan_cd());
					
					ArrayList<EstimateVO> srchStatus = mapper.selectStatus(vo);
					
					if(srchStatus.size() > 0) {
						vo.setSeq_id(srchStatus.get(0).getSeq_id());
						vo.setPrd_nm(prd.getPrd_nm());
						vo.setPrd_prc(String.valueOf(Math.round(prd_prc)));
						mapper.updateStatus(vo);
					}else {
						vo.setPrd_nm(prd.getPrd_nm());
						vo.setPrd_prc(String.valueOf(Math.round(prd_prc)));
						mapper.insertStatus(vo);
					}
				}
			}
		}
		EstimateVO srch = new EstimateVO();
		srch.setPartner_id(partId);
		return mapper.selectStatus(srch);
	}
	
	public void downloadEstimateForm(HttpServletResponse response, String id, String fileEncoding) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			XSSFRow row = null;
			XSSFCell cell = null;
			
			IEstimateMapper mapper = sqlSession.getMapper(IEstimateMapper.class);
			EstimateVO srch = new EstimateVO();
			srch.setPartner_id(id);
			ArrayList<EstimateVO> srchStatus = mapper.selectStatus(srch);
			double total = 0;
			for(EstimateVO statusVO : srchStatus) {
				total += Double.parseDouble(statusVO.getPrd_prc());
			}
			
			//셀 스타일 및 폰트 설정
			CellStyle title = workbook.createCellStyle();
			//정렬
			title.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
			title.setVerticalAlignment(VerticalAlignment.CENTER); //높이 가운데 정렬
			//테두리 선 (위,아래)
			title.setBorderBottom(BorderStyle.THIN);
			title.setBorderTop(BorderStyle.THIN);
			//폰트 설정
			Font titleFont = workbook.createFont();
			titleFont.setFontHeight((short)(20*20)); //사이즈
			titleFont.setBold(true); // 볼드체
			title.setFont(titleFont);
			
			CellStyle allLine = workbook.createCellStyle();
			allLine.setBorderBottom(BorderStyle.THIN);
			allLine.setBorderTop(BorderStyle.THIN);
			allLine.setBorderLeft(BorderStyle.THIN);
			allLine.setBorderRight(BorderStyle.THIN);
			allLine.setVerticalAlignment(VerticalAlignment.CENTER);
			
			CellStyle centerAllLine = workbook.createCellStyle();
			centerAllLine.setBorderBottom(BorderStyle.THIN);
			centerAllLine.setBorderTop(BorderStyle.THIN);
			centerAllLine.setBorderLeft(BorderStyle.THIN);
			centerAllLine.setBorderRight(BorderStyle.THIN);
			centerAllLine.setAlignment(HorizontalAlignment.CENTER);
			centerAllLine.setVerticalAlignment(VerticalAlignment.CENTER);
			
			CellStyle topLine = workbook.createCellStyle();
			topLine.setBorderTop(BorderStyle.THIN);
			
			CellStyle rightLine1 = workbook.createCellStyle();
			rightLine1.setBorderTop(BorderStyle.THIN);
			rightLine1.setBorderRight(BorderStyle.THIN);
			
			CellStyle rightLine2 = workbook.createCellStyle();
			rightLine2.setBorderBottom(BorderStyle.THIN);
			rightLine2.setBorderRight(BorderStyle.THIN);
			
			CellStyle rightLine = workbook.createCellStyle();
			rightLine.setBorderRight(BorderStyle.THIN);
			
			CellStyle underLine = workbook.createCellStyle();
			underLine.setBorderBottom(BorderStyle.THIN);
			
			sheet = workbook.createSheet("シート1");
			// 컬럼너비 세팅
			sheet.setDefaultColumnWidth(12);
			// 컬럼높이 세팅
			sheet.setDefaultRowHeight((short)(25*20));
			// 눈금선 없애기
			sheet.setDisplayGridlines(false);
			row = sheet.createRow(0);
			
			// 타이틀세팅
			row = sheet.createRow(1);
			sheet.addMergedRegion(new CellRangeAddress(1,2,0,7));
			cell = row.createCell(0);
			cell.setCellStyle(title);
			cell.setCellValue("御見積書");
			for(int c=1; c<=7; c++) {
				cell = row.createCell(c);
				cell.setCellStyle(title);
			}
			row = sheet.createRow(2);
			for(int c=0; c<=7; c++) {
				cell = row.createCell(c);
				cell.setCellStyle(title);
			}
			row = sheet.createRow(3); // 타이틀 아래 한줄 비우고...
			
			row = sheet.createRow(4);
			sheet.addMergedRegion(new CellRangeAddress(4,4,0,1));
			cell = row.createCell(0);
			cell.setCellValue(String.format("株式会社 %s 御中", srchStatus.get(0).getPartner_nm()));
			cell.setCellStyle(underLine);
			cell = row.createCell(1);
			cell.setCellStyle(underLine);
			
			cell = row.createCell(6);
			cell.setCellValue("発行日");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(7);
			cell.setCellValue(CommonUtil.getDate("YYYY/MM/dd", 0));
			cell.setCellStyle(allLine);
			
			row = sheet.createRow(5);
			cell = row.createCell(6);
			cell.setCellValue("担当者");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(7);
			cell.setCellValue("金 大元");
			cell.setCellStyle(allLine);
			
			row = sheet.createRow(6);
			sheet.addMergedRegion(new CellRangeAddress(6,6,0,1));
			cell = row.createCell(0);
			cell.setCellValue("FAX：048-764-8952");
			cell.setCellStyle(underLine);
			cell = row.createCell(1);
			cell.setCellStyle(underLine);
			
			row = sheet.createRow(7);
			cell = row.createCell(0);
			cell.setCellValue("下記の通り、御見積り申し上げます。");
			
			row = sheet.createRow(8);
			sheet.addMergedRegion(new CellRangeAddress(8,8,5,8));
			cell = row.createCell(5);
			cell.setCellValue("有限会社アイテムピアジャパン");
			row = sheet.createRow(9);
			sheet.addMergedRegion(new CellRangeAddress(9,9,0,1));
			sheet.addMergedRegion(new CellRangeAddress(9,9,5,8));
			cell = row.createCell(0);
			Calendar cal = Calendar.getInstance( ); 
			cal.add(Calendar.YEAR, 1);
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月dd日"); 
			cell.setCellValue(String.format("有効期限：%s", sdf.format(cal.getTime())));
			cell = row.createCell(5);
			cell.setCellValue("〒333－0845");
			row = sheet.createRow(10);
			sheet.addMergedRegion(new CellRangeAddress(10,10,5,8));
			cell = row.createCell(5);
			cell.setCellValue("埼玉県川口市上青木西1-19-39");
			row = sheet.createRow(11);
			sheet.addMergedRegion(new CellRangeAddress(11,11,5,8));
			cell = row.createCell(5);
			cell.setCellValue("滝沢ビル１階");
			row = sheet.createRow(12);
			sheet.addMergedRegion(new CellRangeAddress(12,12,0,1));
			sheet.addMergedRegion(new CellRangeAddress(12,12,5,8));
			cell = row.createCell(0);
			cell.setCellValue(String.format("合計金額：￥%s（税抜）", String.valueOf(total)));
			cell.setCellStyle(underLine);
			cell = row.createCell(1);
			cell.setCellStyle(underLine);
			cell = row.createCell(5);
			cell.setCellValue("TEL：048-242-3801");
			row = sheet.createRow(13);
			sheet.addMergedRegion(new CellRangeAddress(13,13,5,8));
			cell = row.createCell(5);
			cell.setCellValue("FAX：048-242-3802");
			
			row = sheet.createRow(14); // 어쩌구 정보를 보여준 뒤 한줄 비우고...
			
			// 데이터 노출
			row = sheet.createRow(15);
			sheet.addMergedRegion(new CellRangeAddress(15,15,1,3));
			cell = row.createCell(0);
			cell.setCellValue("番号");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(1);
			cell.setCellValue("商品名");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(2);
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(3);
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(4);
			cell.setCellValue("数量");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(5);
			cell.setCellValue("単価");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(6);
			cell.setCellValue("金額");
			cell.setCellStyle(centerAllLine);
			cell = row.createCell(7);
			cell.setCellValue("備考");
			cell.setCellStyle(centerAllLine);
			
			for(int i=0; i<srchStatus.size(); i++) {
				row = sheet.createRow(16+i);
				sheet.addMergedRegion(new CellRangeAddress(16+i,16+i,1,3));
				cell = row.createCell(0);
				cell.setCellValue(String.valueOf(i+1));
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(1);
				cell.setCellValue(srchStatus.get(i).getPrd_nm());
				cell.setCellStyle(allLine);
				cell = row.createCell(2);
				cell.setCellStyle(allLine);
				cell = row.createCell(3);
				cell.setCellStyle(allLine);
				cell = row.createCell(4);
				cell.setCellValue("1");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(5);
				cell.setCellValue(srchStatus.get(i).getPrd_unit_prc());
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(6);
				cell.setCellValue(srchStatus.get(i).getPrd_prc());
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(7);
				cell.setCellValue(srchStatus.get(i).getJan_cd());
				cell.setCellStyle(allLine);
			}
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment;filename=\"%s\"",
					"MitumoriSho" + CommonUtil.getDate("YYYYMMdd", 0)+".xlsx");
			response.setCharacterEncoding(fileEncoding);
			response.setHeader(headerKey, headerValue);
			workbook.write(response.getOutputStream());
			workbook.close();
		}catch (IOException e) {}
	}
}
