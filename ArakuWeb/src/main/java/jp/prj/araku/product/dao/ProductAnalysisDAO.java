package jp.prj.araku.product.dao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.product.mapper.IProductAnalysisMapper;
import jp.prj.araku.product.vo.PrdAnalysisVOForDownload;
import jp.prj.araku.product.vo.ProductAnalysisVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class ProductAnalysisDAO {
	private static final Logger log = Logger.getLogger("jp.prj.araku.list");
	
	@Autowired
	SqlSession sqlSession;
	
	public void insertPrdAnalysis(HttpServletRequest req, 
			MultipartFile file, 
			String fileEncoding) throws IOException {
		IProductAnalysisMapper mapper = sqlSession.getMapper(IProductAnalysisMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + file.getContentType());
		log.debug("name: " + file.getName());
		log.debug("original name: " + file.getOriginalFilename());
		log.debug("size: " + file.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), fileEncoding));
			
			CsvToBean<ProductAnalysisVO> csvToBean = new CsvToBeanBuilder<ProductAnalysisVO>(reader)
                    .withType(ProductAnalysisVO.class)
                    .withSkipLines(2)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<ProductAnalysisVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	ProductAnalysisVO vo = iterator.next();
            	ProductAnalysisVO setVO = setData(vo, false);
            	mapper.insertPrdAnalysis(setVO);
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public ArrayList<ProductAnalysisVO> getPrdAnalysis(ProductAnalysisVO vo) {
		IProductAnalysisMapper mapper = sqlSession.getMapper(IProductAnalysisMapper.class);
		return mapper.getPrdAnalysis(vo);
	}
	
	public void deletePrdAnalysis(ArrayList<ProductAnalysisVO> list) {
		IProductAnalysisMapper mapper = sqlSession.getMapper(IProductAnalysisMapper.class);
		
		for(ProductAnalysisVO vo : list) {
			mapper.deletePrdAnalysis(vo.getSeq_id());
		}
	}
	
	public void updatePrdAnalysis(ArrayList<ProductAnalysisVO> list) {
		IProductAnalysisMapper mapper = sqlSession.getMapper(IProductAnalysisMapper.class);
		
		for(ProductAnalysisVO vo : list) {
			ProductAnalysisVO setVO = setData(vo, false);
			mapper.updatePrdAnalysis(setVO);
		}
	}
	
	/**
	 * 계산식에 의해 VO를 세팅 (insert, update시 사용)
	 * @param vo
	 * @param ｉｆ (2020-07-28)
	 * @return
	 */
	public ProductAnalysisVO setData(ProductAnalysisVO vo, boolean ｉｆ) {
		int iPrd_config = Integer.parseInt(vo.getPrd_config()); // 商品構成
    	int iPrd_price = Integer.parseInt(vo.getPrd_price()); // 1個当たり仕入金額(税別)
    	int iPrd_pkg = Integer.parseInt(vo.getPrd_pkg()); // 包装(箱+印刷+他)
    	int iShip_cost = Integer.parseInt(vo.getShip_cost()); // 送料
    	String  iAdd_ship_cost = vo.getAdd_ship_cost(); // 追加送料
    	
        if(StringUtils.isEmpty(iAdd_ship_cost)) {
        		iAdd_ship_cost = "0";
          }
 	    
    	// 合計仕入価格 = 【（商品構成×1個当たり仕入金額(税別)）×1.08＋　包装(箱+印刷+他)　＋送料+追加送料　】  ★
    	//double dTtl_price = ((iPrd_config*iPrd_price)+iPrd_pkg+iShip_cost+iAdd_ship_cost)*1.08;
    	double dTtl_price = ((iPrd_config*iPrd_price)*1.08+iPrd_pkg+iShip_cost+ Integer.parseInt(iAdd_ship_cost));
    	// 販売価格★
    	int iSales_price = Integer.parseInt(vo.getSales_price()); 
    	// 販売手数料（10%）
    	//int iSales_comm_ratio =   Integer.parseInt(vo.getSales_comm_ratio());
    	int  iSales_comm_ratio = 10;
    	
    	// 販売手数料（10%）
    	double dSales_comm_ratio = iSales_price / iSales_comm_ratio;
    	// 販売手数料金額 = 販売価格 * 販売手数料
    	double dSales_comm_price = dSales_comm_ratio;
    	// 利益 = 販売価格 - 合計仕入価格 - 販売手数料金額  ★
    	double dBenefit = iSales_price - dTtl_price - dSales_comm_price;
    	// 利益率 = 利益 / 販売価格
    	double dBenefit_ratio = dBenefit / iSales_price;
    	
    	long lTtl_price = Math.round(dTtl_price);   // 合計仕入価格★
    	long lSales_comm_ratio = Math.round(iSales_comm_ratio);  // 販売手数料（10%）
    	long lSales_comm_price = Math.round(dSales_comm_price);  // 販売手数料金額
    	long lBenefit = Math.round(dBenefit);  // 利益★
    	int  lBenefit_ratio = (int)(dBenefit_ratio*100);  // 利益率
    	
    	vo.setTtl_price("￥"+lTtl_price+""); // 合計仕入価格★
    	vo.setAdd_ship_cost("￥"+iAdd_ship_cost+""); // 追加送料
    	
    	vo.setSales_comm_ratio(lSales_comm_ratio+"%"); // 販売手数料（10%）
    	vo.setSales_comm_price("￥"+lSales_comm_price+""); // 販売手数料金額
    	vo.setBenefit("￥"+lBenefit+"");  // 利益 ★
    	vo.setBenefit_ratio(lBenefit_ratio+"%");   // 利益率
    	
//    	vo.setTtl_price(lTtl_price+""); // 合計仕入価格
//    	vo.setSales_comm_ratio(lSales_comm_ratio+""); // 販売手数料（10%）
//    	vo.setSales_comm_price(lSales_comm_price+""); // 販売手数料金額
//    	vo.setBenefit(lBenefit+"");  // 利益
//    	vo.setBenefit_ratio(lBenefit_ratio+"");   // 利益率
    	
    	return vo;
	}
	
	public void downloadPrdAnalysis(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding) 
					throws IOException
					, CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException  {
		IProductAnalysisMapper mapper = sqlSession.getMapper(IProductAnalysisMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			ProductAnalysisVO prd = new ProductAnalysisVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for(String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			prd.setSeq_id_list(seq_id_list);
			
			String csvFileName = "PRDANALYSIS" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
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
				"MEMO"
				, "商品名"
				, "SKU"
				, "URL"
				, "商品構成"
				, "1個当たり仕入金額(税別)"
				, "包装(箱+印刷+他)"
				, "送料"
				, "追加送料"
				, "合計仕入価格"
				, "販売手数料"
				, "販売手数料金額"
				, "販売価格"
				, "利益"
				, "利益率"
				, "備考"
			};
			
			ArrayList<ProductAnalysisVO> searchList = mapper.getPrdAnalysis(prd);
			ArrayList<ArakuVO> list = new ArrayList<>();
			for(ProductAnalysisVO vo : searchList) {
				PrdAnalysisVOForDownload downVO = new PrdAnalysisVOForDownload();
				downVO.setMemo(vo.getMemo());
				downVO.setAdd_ship_cost(vo.getAdd_ship_cost());
				downVO.setBenefit(vo.getBenefit());
				downVO.setBenefit_ratio(vo.getBenefit_ratio());
				downVO.setEtc(vo.getEtc());
				downVO.setPrd_config(vo.getPrd_config());
				downVO.setPrd_nm(vo.getPrd_nm());
				downVO.setPrd_pkg(vo.getPrd_pkg());
				downVO.setPrd_price(vo.getPrd_price());
				downVO.setSales_comm_ratio(vo.getSales_comm_ratio());
				downVO.setSales_comm_price(vo.getSales_comm_price());
				downVO.setSales_price(vo.getSales_price());
				downVO.setTtl_price(vo.getTtl_price());
				downVO.setShip_cost(vo.getShip_cost());
				downVO.setSku(vo.getSku());
				downVO.setUrl(vo.getUrl());
				
				list.add(downVO);
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
}
