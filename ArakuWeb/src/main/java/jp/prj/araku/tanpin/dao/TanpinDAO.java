package jp.prj.araku.tanpin.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.math.BigDecimal;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.jaiko.inventory.mapper.IJaikoPrdInventoryMapper;
import jp.prj.araku.jaiko.inventory.vo.JaikoPrdInventoryVO;
import jp.prj.araku.jaiko.product.mapper.IJaikoPrdInfoMapper;
import jp.prj.araku.jaiko.product.vo.JaikoPrdInfoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.tablet.mapper.ITabletPrdMapper;
import jp.prj.araku.tablet.vo.DealerVO;
import jp.prj.araku.tanpin.mapper.ITanpinMapper;
import jp.prj.araku.tanpin.vo.ExpireManageVo;
import jp.prj.araku.tanpin.vo.TanpinVO;
import jp.prj.araku.tanpin.vo.AllmartManageVo;
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
		if(null != vo.getEnd_date() && !"".equals(vo.getEnd_date())) {
			vo.setEnd_date(vo.getEnd_date()+" 23:59:59");
		}
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
	
	@Transactional(rollbackFor = Exception.class)
	public ArrayList<TanpinVO> modTanpin(ArrayList<TanpinVO> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		IJaikoPrdInventoryMapper prdInvenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		IJaikoPrdInfoMapper prdInfoMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
		
		for(TanpinVO tanpin : list) {
			mapper.updateTanpinInfo(tanpin);
			
			if(tanpin.getPrd_qty() > 0) {
				// 入庫数 0、NULL이외의 숫자가 입력되면 재고관리테이블에서 현재재고와 합한값을 갱신한다.
				JaikoPrdInventoryVO prdInvenSrch = new JaikoPrdInventoryVO();
				prdInvenSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				prdInvenSrch.setJan_cd(tanpin.getPrd_cd());
				ArrayList<JaikoPrdInventoryVO> invenList = prdInvenMapper.getJaikoPrdInventory(prdInvenSrch);
				
				
				//単品商品管理Table
				TanpinVO tanpinSrch = new TanpinVO();
				tanpinSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				tanpinSrch.setPrd_cd(tanpin.getPrd_cd());
				ArrayList<TanpinVO> tanpinList = mapper.getTanpinInfo(tanpinSrch);			
				
				//ALLmart価格管理Table
				AllmartManageVo allmartmngSrch = new AllmartManageVo();
				allmartmngSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				allmartmngSrch.setProduct_code(tanpin.getPrd_cd());
				List<AllmartManageVo> allmartList = mapper.selectAllmartManage(allmartmngSrch);	
				
				
				//商品管理Table
				JaikoPrdInfoVO prdInfoSrch = new JaikoPrdInfoVO();
				prdInfoSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				prdInfoSrch.setJan_cd(tanpin.getPrd_cd());
				ArrayList<JaikoPrdInfoVO> prdInfoList = prdInfoMapper.getJaikoPrdInfo(prdInfoSrch);
				
				//賞味期限管理Table
				ExpireManageVo expireSrch = new ExpireManageVo();
				expireSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				expireSrch.setJan_cd(tanpin.getPrd_cd());
				expireSrch.setExp_dt(tanpin.getExp_dt());
				ArrayList<ExpireManageVo> expireList = mapper.getExpireManage(expireSrch);
				
				//商品管理Table　更新
				if(prdInfoList.size() > 0) {
					prdInfoSrch = prdInfoList.get(0);
					if(tanpinList.size() > 0) {
						tanpinSrch = tanpinList.get(0);
						String prdInfoSeqId = prdInfoSrch.getSeq_id();
						String jancodeId = tanpinSrch.getPrd_cd();
						String prdInfoUpdate = tanpinSrch.getUpdate_date();
						String prdUnitPrc = tanpinSrch.getInprice();
						
						prdInfoSrch = new JaikoPrdInfoVO();
						prdInfoSrch.setSeq_id(prdInfoSeqId);
						prdInfoSrch.setUpd_dt(prdInfoUpdate);	  // 更新日を更新する。		
						prdInfoSrch.setJan_cd(jancodeId);
						prdInfoSrch.setPrd_unit_prc(prdUnitPrc);  // 商品価格を更新する。
						//prdInfoSrch.setProduct_image_url("'/resources/img/shouhin/" + jancodeId + ".png'");
						
						prdInfoMapper.updateJaikoPrdInfo(prdInfoSrch);
					}
					// 商品情報TにJANCODEがない場合、レコードを追加する。
					JaikoPrdInfoVO prdInfodataSrch = new JaikoPrdInfoVO();
					prdInfodataSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					prdInfodataSrch.setJan_cd(tanpin.getPrd_cd());
					//prdInfodataSrch.setUpd_user_id(user_Id);
					ArrayList<JaikoPrdInfoVO> infodataList = prdInfoMapper.getJaikoPrdInfo(prdInfodataSrch);
					if(infodataList.size() <= 0) {
						/*
						商品情報T
						商品名 prd_nm
						JANCODE1 jan_cd
						商品数1　　Prd_cnt1
						商品税(抜、込)　Tax_incld
						商品税率　　Tax_rt
						単価 prd_unit_prc
						*/
						JaikoPrdInfoVO jaikoPrdVo = new JaikoPrdInfoVO();
						jaikoPrdVo.setPrd_nm(tanpin.getPrd_nm());
						jaikoPrdVo.setJan_cd1(tanpin.getPrd_cd());
						jaikoPrdVo.setJan_cd(tanpin.getPrd_cd());
						jaikoPrdVo.setPrd_cnt1("1");
						jaikoPrdVo.setTax_incld("0");
						jaikoPrdVo.setTax_rt("8");
						jaikoPrdVo.setPrd_unit_prc(tanpin.getInprice());
						
						prdInfoMapper.insertJaikoPrdInfo(jaikoPrdVo);
					}
				}
				
				//Allmart価格管理Table更新
				if(allmartList.size() > 0) {
					
					allmartmngSrch = allmartList.get(0);
					String prdProductCode = tanpin.getPrd_cd();
					String prdUnitPrice = tanpin.getInprice();
				
					// BigDecimalに変換
					BigDecimal unitPrice = new BigDecimal(prdUnitPrice);
					BigDecimal multiplier = new BigDecimal("1.3"); // 1.3をBigDecimalとして定義

					// BigDecimal同士の掛け算を行う
					BigDecimal newUnitPrice = unitPrice.multiply(multiplier);

					// BigDecimalをdoubleに変換
					double doubleUnitPrice = newUnitPrice.doubleValue();
					double roundedUp = Math.ceil(doubleUnitPrice); // 切り上げ

					// doubleをStringに変換
					String unitPriceString = Double.toString(roundedUp);
					
					AllmartManageVo allmartmng = new AllmartManageVo();
					allmartmng.setProduct_id(allmartmngSrch.getProduct_id());
					allmartmng.setDepartment_id(allmartmngSrch.getDepartment_id());
					allmartmng.setGroup_code(allmartmngSrch.getGroup_code());
					allmartmng.setProduct_code(prdProductCode);
					allmartmng.setProduct_name(allmartmngSrch.getProduct_name());					
					allmartmng.setUnit_price(unitPriceString);
					allmartmng.setTax_category(allmartmngSrch.getTax_category());
					
					// 現在の日付を取得し、フォーマットを設定する
					LocalDate currentDate = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					String formattedDate = currentDate.format(formatter);

					// update_dateに現在の日付を設定する
					allmartmng.setUpdate_date(formattedDate);
					
					// Login情報を設定する。
					allmartmng.setLogin_id("admin");

				    // まず、更新を試みます。
				    int updateCount = mapper.updateAllmartManage(allmartmng);
				    
				    // 更新された行がなければ、挿入を行います。
				    if (updateCount == 0) {
				        mapper.insertAllmartManage(allmartmng);
				    }
				}
				
				//賞味期間管理Table　更新
				ExpireManageVo expireData = new ExpireManageVo();
				if(expireList.size() <= 0) {
					expireData.setSeq_id(tanpin.getSeq_id());
					expireData.setRegister_date(tanpin.getRegister_date());
					expireData.setJan_cd(tanpin.getPrd_cd());
					expireData.setPrd_nm(tanpin.getPrd_nm());
					expireData.setPrd_qty(tanpin.getPrd_qty());
					expireData.setExp_dt(tanpin.getExp_dt());

					mapper.insertExpireManage(expireData);
				}
				
				//在庫管理Table　更新
				if(invenList.size() > 0) {
					prdInvenSrch = invenList.get(0);
					
					int nowPrdCnt = 0;
					String prdInvenSeqId = prdInvenSrch.getSeq_id();
					String prdInvenUpdate = prdInvenSrch.getUpd_dt();
					
					if(null != prdInvenSrch.getNow_prd_cnt() && !"".equals(prdInvenSrch.getNow_prd_cnt())) {
						nowPrdCnt = Integer.parseInt(prdInvenSrch.getNow_prd_cnt());
					}
					prdInvenSrch = new JaikoPrdInventoryVO();
					prdInvenSrch.setSeq_id(prdInvenSeqId);
					prdInvenSrch.setNow_prd_cnt(String.valueOf(tanpin.getPrd_qty() + nowPrdCnt));
					prdInvenSrch.setDealer_id(tanpin.getDealer_id());
					prdInvenSrch.setDealer_nm(tanpin.getDealer_nm());				
					prdInvenSrch.setUpd_dt(prdInvenUpdate);
					
					prdInvenMapper.updateJaikoPrdInventory(prdInvenSrch);
				}else {
					// 재고관리 테이블에 관련 JANCODE 가 없을 경우
					/*
					単一商品管理T
					JANCODE
					入庫数
					取引先コード
					取引先会社名
					ブランド ???
					商品名
					賞味期限
					登録日
					 * */
					/*
					在庫管理T
					JANCODE
					現在商品数
					取引先コード
					取引先会社名
					ブランド
					商品名
					賞味期限
					登録日
			 		*/
					//2024-01-30 start kim daewon
					//jancode
					prdInvenSrch.setJan_cd(String.valueOf(tanpin.getPrd_cd()));
					//入庫数
					prdInvenSrch.setNow_prd_cnt(String.valueOf(tanpin.getPrd_qty()));
					//取引先コード
					prdInvenSrch.setDealer_id(tanpin.getDealer_id());
					//取引先会社名
					prdInvenSrch.setDealer_nm(tanpin.getDealer_nm());
					//ブランド名
					prdInvenSrch.setBrand_nm(tanpin.getMaker_nm());
					//商品名
					prdInvenSrch.setPrd_nm(tanpin.getPrd_nm());
					//ケース数
					prdInvenSrch.setPrd_case("1");
					//バラ数
					prdInvenSrch.setPrd_bara("0");
					//ロット数
					prdInvenSrch.setPrd_lot("0");
					//賞味期限
					prdInvenSrch.setExp_dt(tanpin.getExp_dt());
					//2024-01-30 end kim daewon
					
					prdInvenMapper.insertJaikoPrdInventory(prdInvenSrch);
					
					/*
					商品情報T
					商品名 prd_nm
					JANCODE1 jan_cd
					商品数1　　Prd_cnt1
					商品税(抜、込)　Tax_incld
					商品税率　　Tax_rt
					単価 prd_unit_prc
					*/
					JaikoPrdInfoVO jaikoPrdVo = new JaikoPrdInfoVO();
					jaikoPrdVo.setPrd_nm(tanpin.getPrd_nm());
					jaikoPrdVo.setJan_cd1(tanpin.getPrd_cd());
					//jaikoPrdVo.setJan_cd(tanpin.getPrd_cd());
					jaikoPrdVo.setPrd_cnt1("1");
					jaikoPrdVo.setTax_incld("0");
					jaikoPrdVo.setTax_rt("8");
					jaikoPrdVo.setPrd_unit_prc(tanpin.getInprice());
					prdInfoMapper.insertJaikoPrdInfo(jaikoPrdVo);
					/*
					賞味管理T
					JANCODE
					商品名
					取引先コード
					取引先会社名
					入庫数
					賞味期限
					登録日
					 */
					ExpireManageVo expire = new ExpireManageVo();
					expire.setJan_cd(tanpin.getPrd_cd());
					expire.setPrd_nm(tanpin.getPrd_nm());
//					expire.setPartner_id(tanpin.getDealer_id());
//					expire.setPartner_nm(tanpin.getDealer_nm());
					expire.setPrd_qty(tanpin.getPrd_qty());
					expire.setExp_dt(tanpin.getExp_dt());
					mapper.insertExpireManage(expire);
				}
				//2024-01-30 kim daewon start
				// 商品情報TにJANCODEがない場合、レコードを追加する。
				JaikoPrdInfoVO prdInfodataSrch = new JaikoPrdInfoVO();
				prdInfodataSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
				prdInfodataSrch.setJan_cd(tanpin.getPrd_cd());
				ArrayList<JaikoPrdInfoVO> infodataList = prdInfoMapper.getJaikoPrdInfo(prdInfodataSrch);
				if(infodataList.size() <= 0) {
					/*
					商品情報T
					商品名 prd_nm
					JANCODE1 jan_cd
					商品数1　　Prd_cnt1
					商品税(抜、込)　Tax_incld
					商品税率　　Tax_rt
					単価 prd_unit_prc
					*/
					JaikoPrdInfoVO jaikoPrdVo = new JaikoPrdInfoVO();
					jaikoPrdVo.setPrd_nm(tanpin.getPrd_nm());
					jaikoPrdVo.setJan_cd1(tanpin.getPrd_cd());
					jaikoPrdVo.setJan_cd(tanpin.getPrd_cd());
					jaikoPrdVo.setPrd_cnt1("1");
					jaikoPrdVo.setTax_incld("0");
					jaikoPrdVo.setTax_rt("8");
					jaikoPrdVo.setPrd_unit_prc(tanpin.getInprice());
					prdInfoMapper.insertJaikoPrdInfo(jaikoPrdVo);
				}
				//2024-01-30 kim daewon end
			}
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
			
			// 상품재고 테이블에서 해당 거래처코드에 해당하는 리스트 얻기
			IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
			JaikoPrdInventoryVO invenVO = new JaikoPrdInventoryVO();
			invenVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
			invenVO.setDealer_id(id);
			ArrayList<JaikoPrdInventoryVO> invenList = invenMapper.getJaikoPrdInventory(invenVO);
			ArrayList<JaikoPrdInventoryVO> list = new ArrayList<>();
			for(JaikoPrdInventoryVO vo : invenList) {
				// 現在在庫数＜ロット数 の場合、商品名と入数と数量をEXCELに書き込みする。
				if(Integer.parseInt(vo.getPrd_lot()) < Integer.parseInt(vo.getNow_prd_cnt())) {
					continue;
				}

				// 現在在庫数が設定したロット数半分よりおおきい場合、EXCELに書き込みしない。
				int halfsu = Integer.parseInt(vo.getPrd_lot())/2;
				// 現在在庫数 > ロット数の半分より大きい場合、-- 2024/01/04 修正者 kim daewon START
				if (Integer.parseInt(vo.getNow_prd_cnt()) > halfsu) {
					continue;
				}
				//-- 2024/01/04 修正者 kim daewon END
			   
				list.add(vo);
			}
			
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
			
			String ranNum = String.format("発注書No.%s", CommonUtil.getRandomNumber(7));
			
			if(list.size() > 24) {
				int i = list.size()/24;
				int ii = list.size()%24;
				int[] arr = new int[i];
				if(ii > 0) {
					arr = new int[i+1];
				}
				List<List<JaikoPrdInventoryVO>> subList = new ArrayList<>();
				
				for(int j=0; j<arr.length; j++) {
					arr[j] = (j+1)*24;
				}
				
				for(int k=0; k<arr.length; k++) {
					if(k==0) {
						subList.add(list.subList(0, arr[k]));
						continue;
					}
					
					if(arr[k] > list.size()) {
						subList.add(list.subList(arr[k-1], list.size()));
					}else {
						subList.add(list.subList(arr[k-1], arr[k]));
					}
				}
				
				for(int i1=0; i1<subList.size(); i1++) {
					sheet = workbook.createSheet("シート"+(i1+1));
					// 컬럼너비 세팅
					sheet.setDefaultColumnWidth(8);
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
					underLine.setBorderBottom(BorderStyle.THIN);
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
					cell.setCellValue("048-242-3801");
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
					cell.setCellValue(ranNum);
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
					sheet.addMergedRegion(new CellRangeAddress(23,23,7,8));
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
					cell.setCellValue("単位");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(6);
					cell.setCellValue("備考");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(7);
					cell.setCellValue("");
					cell.setCellStyle(centerAllLine);
					cell = row.createCell(8);
					cell.setCellStyle(allLine);
					List<JaikoPrdInventoryVO> innerList = subList.get(i1);
					int finalLine = 0;
					for(int i2=0; i2<innerList.size(); i2++) {
						row = sheet.createRow(i2+24);
						sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,0,2));
						sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,7,8));
						JaikoPrdInventoryVO innerVO = innerList.get(i2);
						/*
						IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
						JaikoPrdInfoVO jaikoPrdVO = new JaikoPrdInfoVO();
						jaikoPrdVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
						jaikoPrdVO.setJan_cd(innerVO.getJan_cd());
						ArrayList<JaikoPrdInfoVO> prdInfo = prdMapper.getJaikoPrdInfo(jaikoPrdVO);
						*/
						cell = row.createCell(0);
						cell.setCellValue(innerVO.getPrd_nm());
						cell.setCellStyle(allLine);
						cell = row.createCell(1);
						cell.setCellStyle(allLine);
						cell = row.createCell(2);
						cell.setCellStyle(allLine);
						cell = row.createCell(3);
						cell.setCellValue(innerVO.getPrd_qty());
						cell.setCellStyle(centerAllLine);
						cell = row.createCell(4);
						cell.setCellValue("1");
						cell.setCellStyle(centerAllLine);
						/*
						cell = row.createCell(5);
						cell.setCellValue(String.valueOf(prdInfo.get(0).getPrd_unit_prc()));
						cell.setCellStyle(allLine);
						cell = row.createCell(6);
						// 入数 * 数量 * 単価 = 合計金額"
						int sum = Integer.parseInt(innerVO.getPrd_qty()) * 1 * Integer.parseInt(prdInfo.get(0).getPrd_unit_prc());
						cell.setCellValue(String.valueOf(sum));
						cell.setCellStyle(allLine);
						cell = row.createCell(7);
						cell.setCellValue(innerVO.getJan_cd());
						cell.setCellStyle(allLine);
						cell = row.createCell(8);
						cell.setCellStyle(allLine);
						finalLine = i2+24;
						*/
						cell = row.createCell(5);
						cell.setCellValue("ケース");
						cell.setCellStyle(centerAllLine);
						cell = row.createCell(6);
						cell.setCellValue("");
						cell.setCellStyle(allLine);
						cell = row.createCell(7);
						cell.setCellValue("");
						cell.setCellStyle(allLine);
						cell = row.createCell(8);
						cell.setCellStyle(allLine);
						finalLine = i2+24;
					}
					row = sheet.createRow(finalLine+1);
					row = sheet.createRow(finalLine+2);
					cell = row.createCell(0);
					cell.setCellValue("通信欄");
					cell.setCellStyle(centerAllLine);
					for(int c=1; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(topLine);
					}
					cell = row.createCell(8);
					cell.setCellStyle(rightLine1);
					row = sheet.createRow(finalLine+3);
					cell = row.createCell(0);
					cell.setCellStyle(allLine);
					cell = row.createCell(8);
					cell.setCellStyle(rightLine);
					row = sheet.createRow(finalLine+4);
					cell = row.createCell(0);
					cell.setCellStyle(allLine);
					for(int c=1; c<=8; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(underLine);
					}
					cell = row.createCell(8);
					cell.setCellStyle(rightLine2);
					sheet.addMergedRegion(new CellRangeAddress(finalLine+2,finalLine+4,0,0));
				}
			}else {
				sheet = workbook.createSheet("シート1");
				// 컬럼너비 세팅
				sheet.setDefaultColumnWidth(8);
				// 컬럼높이 세팅
				sheet.setDefaultRowHeight((short)(25*20));
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
				underLine.setBorderBottom(BorderStyle.THIN);
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
				cell.setCellValue("048-242-3801");
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
				cell.setCellValue(ranNum);
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
				sheet.addMergedRegion(new CellRangeAddress(23,23,7,8));
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
				cell.setCellValue("単価");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(6);
				cell.setCellValue("合計金額");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(7);
				cell.setCellValue("備考");
				cell.setCellStyle(centerAllLine);
				cell = row.createCell(8);
				cell.setCellStyle(allLine);
				int finalLine = 0;
				for(int i2=0; i2<list.size(); i2++) {
					row = sheet.createRow(i2+24);
					sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,0,2));
					sheet.addMergedRegion(new CellRangeAddress(i2+24,i2+24,7,8));
					JaikoPrdInventoryVO innerVO = list.get(i2);
					/*
					IJaikoPrdInfoMapper prdMapper = sqlSession.getMapper(IJaikoPrdInfoMapper.class);
					JaikoPrdInfoVO jaikoPrdVO = new JaikoPrdInfoVO();
					jaikoPrdVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					jaikoPrdVO.setJan_cd(innerVO.getJan_cd());
					ArrayList<JaikoPrdInfoVO> prdInfo = prdMapper.getJaikoPrdInfo(jaikoPrdVO);
					*/
					cell = row.createCell(0);
					cell.setCellValue(innerVO.getPrd_nm());
					cell.setCellStyle(allLine);
					cell = row.createCell(1);
					cell.setCellStyle(allLine);
					cell = row.createCell(2);
					cell.setCellStyle(allLine);
					cell = row.createCell(3);
					cell.setCellValue(innerVO.getPrd_qty());
					cell.setCellStyle(allLine);
					cell = row.createCell(4);
					//cell.setCellValue("1");
					// （現在在庫数＜0　&& 更新日＝本日）	の場合、		
					// （現在在庫数+ロット数）／箱数 = 数量を設定する。　-- 2024/01/04 修正者 kim daewon START
					Date datehonzitu = new Date();
					SimpleDateFormat datakata = new SimpleDateFormat("yyyy/MM/dd");
					String hozitu = datakata.format(datehonzitu);

					// 現在在庫数が設定したロット数より1/2以下になる場合、
					int halfsu = Integer.parseInt(innerVO.getPrd_lot())/2;
					
					if (Integer.parseInt(innerVO.getNow_prd_cnt()) < 0 && hozitu.equals(innerVO.getUpd_dt()) ) {
						//現在在庫数を－から＋に変更する。
						int abstotla = Math.abs(Integer.parseInt(innerVO.getNow_prd_cnt()));

						//現在在庫数+ロット数					
						int suLot = abstotla +Integer.parseInt(innerVO.getPrd_lot());
						
						//（現在在庫数+ロット数）／箱数 
						double warisan = (double) suLot / Integer.parseInt(innerVO.getPrd_qty());
						int warisansu = (int) Math.round(warisan);
						// 在庫数が1より小さい場合、”1”を設定する。
						if (warisansu < 1) {
							warisansu = 1;
							cell.setCellValue(warisansu);
						} else{
							cell.setCellValue(Math.round(warisan));
						}
						
						// 現在在庫数 < ロット数の半分より少ない場合、
					} else if (Integer.parseInt(innerVO.getNow_prd_cnt()) < halfsu) {
						int sudata = Integer.parseInt(innerVO.getPrd_lot()) /  Integer.parseInt(innerVO.getPrd_qty());
						cell.setCellValue(Math.round(sudata));
						
					} else {
						
						cell.setCellValue("1");
					}
					// -- 2024/01/04 修正者 kim daewon END
					
					cell.setCellStyle(allLine);
					cell = row.createCell(5);
					//cell.setCellValue(String.valueOf(prdInfo.get(0).getPrd_unit_prc()));
					cell.setCellStyle(allLine);
					cell = row.createCell(6);
					// 入数 * 数量 * 単価 = 合計金額"
					//int sum = Integer.parseInt(innerVO.getPrd_qty()) * 1 * Integer.parseInt(prdInfo.get(0).getPrd_unit_prc());
					//cell.setCellValue(String.valueOf(sum));
					cell.setCellStyle(allLine);
					cell = row.createCell(7);
					//cell.setCellValue(innerVO.getJan_cd());
					cell.setCellStyle(allLine);
					cell = row.createCell(8);
					cell.setCellStyle(allLine);
					finalLine = i2+24;
				}
				row = sheet.createRow(finalLine+1);
				row = sheet.createRow(finalLine+2);
				cell = row.createCell(0);
				cell.setCellValue("通信欄");
				cell.setCellStyle(centerAllLine);
				for(int c=1; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(topLine);
				}
				cell = row.createCell(8);
				cell.setCellStyle(rightLine1);
				row = sheet.createRow(finalLine+3);
				cell = row.createCell(0);
				cell.setCellStyle(allLine);
				cell = row.createCell(8);
				cell.setCellStyle(rightLine);
				row = sheet.createRow(finalLine+4);
				cell = row.createCell(0);
				cell.setCellStyle(allLine);
				for(int c=1; c<=8; c++) {
					cell = row.createCell(c);
					cell.setCellStyle(underLine);
				}
				cell = row.createCell(8);
				cell.setCellStyle(rightLine2);
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
	
	/**
	 * 20240627
	 * Allmart価格管理S
	 * */
//	public List<AllmartManageVo> getAllmartManage(AllmartManageVo vo) {
//		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
//		return mapper.selectAllmartManage(vo);
//	}

	public ArrayList<AllmartManageVo> getAllmartManage(String select_type) {
		AllmartManageVo vo = new AllmartManageVo();
		vo.setSelect_type(select_type);
		
		return getAllmartManage(vo);
	}
	public ArrayList<AllmartManageVo> getAllmartManage(AllmartManageVo vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		
		if(null != vo.getEnd_date() && !"".equals(vo.getEnd_date())) {
			vo.setEnd_date(vo.getEnd_date());
		}
		ArrayList<AllmartManageVo> ret = mapper.getAllmartManageT(vo);
		for(AllmartManageVo tmp : ret) {
			if(null != tmp.getUnit_price()) {
				tmp.setUpdate_date(tmp.getUnit_price());
			}
		}
		return ret;
	}
//	public List<ExpireManageVo> addAllmartManage(AllmartManageVo vo) {
//		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
//		mapper.insertAllmartManage(vo);
//		return mapper.selectAllmartManage(null);
//	}
	
	public List<AllmartManageVo> modifyAllmartManage(List<AllmartManageVo> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
	    
		for(AllmartManageVo allprice : list) {
			
			// 現在の日付を取得し、フォーマットを設定する
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			String formattedDate = currentDate.format(formatter);

			// update_dateに現在の日付を設定する
			allprice.setUpdate_date(formattedDate);
			
			// Login情報を設定する。
			allprice.setLogin_id("admin");
			mapper.updateAllmartManage(allprice);
		}
		return mapper.selectAllmartManage(null);
	}
	
	public List<AllmartManageVo> removeAllmartManage(List<AllmartManageVo> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		Map<String, Object> params = new HashMap<>();
	    
		for(AllmartManageVo allprice : list) {
			params.put("product_code", allprice.getProduct_code());
		    params.put("user_id", "admin");
			//mapper.deleteAllmartManage(allprice.getProduct_code());
			mapper.deleteAllmartManage(params);
		}
		return mapper.selectAllmartManage(null);
	}
	/**
	 * 20240627
	 * Allmart価格管理E
	 * */
	
	/**
	 * 20230617
	 * 賞味期限管理S
	 * */
	public List<ExpireManageVo> getExpireManage(ExpireManageVo vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		return mapper.selectExpireManage(vo);
	}
	
	public List<ExpireManageVo> addExpireManage(ExpireManageVo vo) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		mapper.insertExpireManage(vo);
		return mapper.selectExpireManage(null);
	}
	
	public List<ExpireManageVo> modifyExpireManage(List<ExpireManageVo> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		for(ExpireManageVo expire : list) {
			mapper.updateExpireManage(expire);
		}
		return mapper.selectExpireManage(null);
	}
	
	public List<ExpireManageVo> removeExpireManage(List<ExpireManageVo> list) {
		ITanpinMapper mapper = sqlSession.getMapper(ITanpinMapper.class);
		for(ExpireManageVo expire : list) {
			mapper.deleteExpireManage(expire.getSeq_id());
		}
		return mapper.selectExpireManage(null);
	}
	/**
	 * 20230617
	 * 賞味期限管理E
	 * */
	
	public void downloadAllmart(
			HttpServletResponse response, String[] id_list, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		ITanpinMapper infoAllmartMapper = sqlSession.getMapper(ITanpinMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		log.debug("product_id_list : " + id_list);
		
		
		try {
			
			AllmartManageVo Allm = new AllmartManageVo();
			ArrayList<String> product_id_list = new ArrayList<>();
			for(String productId : id_list) {
				product_id_list.add(productId);
			}
			Allm.setProduct_id_list(product_id_list);
			
			String csvFileName = "Allmart_"+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			"商品ID", "部門ID", "グループコード", "商品コード", "商品名", "商品単価", "税区分"
			};
			
			ArrayList<AllmartManageVo> searchList = infoAllmartMapper.getAllmartManageDL(Allm);
			ArrayList<AllmartManageVo> list = new ArrayList<>();
			for(AllmartManageVo dlvo : searchList) {
				list.add(dlvo);
			}
			CommonUtil.executeAllmartCSVDownload(csvWriter, writer, header, list);
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}

	public void insertAllmartManageExe( 
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

	        String line = "";
	        int cnt = 0;
	        
	        while ((line = reader.readLine()) != null) {
	            // コンマ区切りで行を分割
	            String[] arr = line.split(",");
	            
	            // ヘッダー行をスキップ
	            if(cnt == 0) {
	                cnt++;
	                continue;
	            }
	            
	            // データをAllmartManageVoにマッピング
	            AllmartManageVo vo = new AllmartManageVo();
	            vo.setProduct_id(arr[0].replaceAll("\"", "")); // ダブルクォートを削除
	            vo.setDepartment_id(arr[1].replaceAll("\"", ""));
	            vo.setGroup_code(arr[2].replaceAll("\"", ""));
	            vo.setProduct_code(arr[3].replaceAll("\"", ""));
	            vo.setProduct_name(arr[4].replaceAll("\"", ""));
	            vo.setUnit_price(arr[5].replaceAll("\"", ""));
	            vo.setTax_category(arr[6].replaceAll("\"", ""));
	            
				// 現在の日付を取得し、フォーマットを設定する
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				String formattedDate = currentDate.format(formatter);

				// 登録日に現在の日付を設定する
				vo.setInsert_date(formattedDate);
				
				// Login情報を設定する。
				vo.setLogin_id("admin");
				
	            // データベースに挿入
	            try {
	            	
					//ALLmart価格管理Table
					AllmartManageVo allmartmngSrch = new AllmartManageVo();
					allmartmngSrch.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
					allmartmngSrch.setProduct_code(vo.getProduct_code());
					allmartmngSrch.setLogin_id("admin");
					List<AllmartManageVo> allmartList = mapper.selectAllmartManage(allmartmngSrch);	
					
					if(allmartList.size() <= 0) {
						mapper.insertAllmartManage(vo);
					}
//	            	String[] product_id_list = { vo.getProduct_id() }; // getProduct_id()を配列に変換する
//	            	log.debug("product_id: " + product_id_list[0]);
//	            	String adc = vo.getProduct_id();
//	            	
//	            	List<String> productIds = Arrays.asList(adc);
//	            	List<AllmartManageVo> results = mapper.getAllmartManageT(productIds);
//	            	    
//	                //List<AllmartManageVo> existingRecords = mapper.getAllmartManageT(vo.getProduct_id());
//	                
//	                if (results.isEmpty()) {
//	                    mapper.insertAllmartManage(vo);
//	                } else {
//	                    // すでにデータが存在する場合の処理
//	                    // 何か特別な処理が必要であればここに記述する
//	                    // 例えば、ログを出力するなど
//	                    log.debug("Record already exists: " + vo);
//	                }
	            } catch (DataIntegrityViolationException e) {
	                // データの整合性違反などの例外処理
	                //System.err.println("Error inserting record: " + vo);
	                //e.printStackTrace();
	            	continue;
	            }
	            
	            cnt++;
	        }
	        
	    } finally {
	        if (reader != null) {
	            reader.close();
	        }
	    }
	}

	
	/**
	 * 계산식에 의해 VO를 세팅 (insert, update시 사용)
	 * @param vo
	 * @param ｉｆ (2020-07-28)
	 * @return
	 */
	public AllmartManageVo setData(AllmartManageVo vo, boolean ｉｆ) {
//		int iPrd_config = Integer.parseInt(vo.getPrd_config()); // 商品構成
//    	int iPrd_price = Integer.parseInt(vo.getPrd_price()); // 1個当たり仕入金額(税別)
//    	int iPrd_pkg = Integer.parseInt(vo.getPrd_pkg()); // 包装(箱+印刷+他)
//    	int iShip_cost = Integer.parseInt(vo.getShip_cost()); // 送料
//    	String  iAdd_ship_cost = vo.getAdd_ship_cost(); // 追加送料
//    	
//        if(StringUtils.isEmpty(iAdd_ship_cost)) {
//        		iAdd_ship_cost = "0";
//          }
// 	    
//    	// 合計仕入価格 = 【（商品構成×1個当たり仕入金額(税別)）×1.08＋　包装(箱+印刷+他)　＋送料+追加送料　】  ★
//    	//double dTtl_price = ((iPrd_config*iPrd_price)+iPrd_pkg+iShip_cost+iAdd_ship_cost)*1.08;
//    	double dTtl_price = ((iPrd_config*iPrd_price)*1.08+iPrd_pkg+iShip_cost+ Integer.parseInt(iAdd_ship_cost));
//    	// 販売価格★
//    	int iSales_price = Integer.parseInt(vo.getSales_price()); 
//    	// 販売手数料（10%）
//    	//int iSales_comm_ratio =   Integer.parseInt(vo.getSales_comm_ratio());
//    	int  iSales_comm_ratio = 10;
//    	
//    	// 販売手数料（10%）
//    	double dSales_comm_ratio = iSales_price / iSales_comm_ratio;
//    	// 販売手数料金額 = 販売価格 * 販売手数料
//    	double dSales_comm_price = dSales_comm_ratio;
//    	// 利益 = 販売価格 - 合計仕入価格 - 販売手数料金額  ★
//    	double dBenefit = iSales_price - dTtl_price - dSales_comm_price;
//    	// 利益率 = 利益 / 販売価格
//    	double dBenefit_ratio = dBenefit / iSales_price;
//    	
//    	long lTtl_price = Math.round(dTtl_price);   // 合計仕入価格★
//    	long lSales_comm_ratio = Math.round(iSales_comm_ratio);  // 販売手数料（10%）
//    	long lSales_comm_price = Math.round(dSales_comm_price);  // 販売手数料金額
//    	long lBenefit = Math.round(dBenefit);  // 利益★
//    	int  lBenefit_ratio = (int)(dBenefit_ratio*100);  // 利益率
//    	
//    	vo.setTtl_price("￥"+lTtl_price+""); // 合計仕入価格★
//    	vo.setAdd_ship_cost("￥"+iAdd_ship_cost+""); // 追加送料
//    	
//    	vo.setSales_comm_ratio(lSales_comm_ratio+"%"); // 販売手数料（10%）
//    	vo.setSales_comm_price("￥"+lSales_comm_price+""); // 販売手数料金額
//    	vo.setBenefit("￥"+lBenefit+"");  // 利益 ★
//    	vo.setBenefit_ratio(lBenefit_ratio+"%");   // 利益率
//    	
//    	vo.setTtl_price(lTtl_price+""); // 合計仕入価格
//    	vo.setSales_comm_ratio(lSales_comm_ratio+""); // 販売手数料（10%）
//    	vo.setSales_comm_price(lSales_comm_price+""); // 販売手数料金額
//    	vo.setBenefit(lBenefit+"");  // 利益
//    	vo.setBenefit_ratio(lBenefit_ratio+"");   // 利益率
    	
    	return vo;
	}
	
	public void downloadTodayOrder(
			HttpServletResponse response, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		IJaikoPrdInventoryMapper invenMapper = sqlSession.getMapper(IJaikoPrdInventoryMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "order_"+ CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
			"取引先会社名", "商品名", "現在商品数", "ロート数", "ＪＡＮコード"
			};
			ArrayList<ArakuVO> downList = new ArrayList<ArakuVO>();
			downList.addAll(invenMapper.getTodayJaikoPrdInventory());
			CommonUtil.executeCSVDownload(csvWriter, writer, header, downList);
		}finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}

}
