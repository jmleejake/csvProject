package jp.prj.araku.file.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.file.mapper.IFileMapper;
import jp.prj.araku.file.vo.RCSVDownVO;
import jp.prj.araku.file.vo.RakutenVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;
import jp.prj.araku.list.vo.TranslationResultVO;
import jp.prj.araku.list.vo.TranslationVO;
import jp.prj.araku.util.CommonUtil;

/**
 * @comment
 * [MOD-0826] 콜론으로만 델리미터를 처리할 예정이므로 일단 주석처리
 */
@Repository
public class FileDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = LoggerFactory.getLogger(FileDAO.class);
	
	@Transactional
	public void insertRakutenInfo(MultipartFile rakUpload, String fileEncoding) throws IOException {
		log.info("insertRakutenInfo");
		IFileMapper fileMapper = sqlSession.getMapper(IFileMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		log.debug("encoding : {}", fileEncoding);
		log.debug("contentType: {}", rakUpload.getContentType());
		log.debug("name: {}", rakUpload.getName());
		log.debug("original name: {}", rakUpload.getOriginalFilename());
		log.debug("size: {}", rakUpload.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(rakUpload.getInputStream(), fileEncoding));
			
			CsvToBean<RakutenVO> csvToBean = new CsvToBeanBuilder<RakutenVO>(reader)
                    .withType(RakutenVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<RakutenVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	RakutenVO vo = iterator.next();
            	
            	// 데이터 중복체크
            	RakutenSearchVO searchVO = new RakutenSearchVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getOrder_no());
            	
            	ArrayList<RakutenSearchVO> dupCheckList = listMapper.getRList(searchVO);
        		if (dupCheckList.size() > 1) {
        			for (RakutenSearchVO dupVO : dupCheckList) {
        				if (vo.getProduct_name().equals(dupVO.getProduct_name())) {
        					// 이미 존재하는 受注番号가 2개 이상 있으면 상품명을 확인한 뒤 다음 레코드로 진행
                    		continue;
        				}
        			}
        		}
        		
        		if (dupCheckList.size() == 1) {
            		// 이미 존재하는 受注番号가 있으면 다음 레코드로 진행
            		continue;
        		}
        		
        		// あす楽希望이 1인 경우
        		if (vo.getTomorrow_hope().equals("1")) {
        			// お届け日指定 컬럼에 데이터 등록일 +1
        			vo.setDelivery_date_sel(CommonUtil.getDate("yyyy/MM/dd", 1));
        			// お届け時間帯 컬럼에 午前中
        			vo.setDelivery_time(CommonUtil.TOMORROW_MORNING);
        		}
            	
        		fileMapper.insertRakutenInfo(vo);
                log.debug("inserted rakuten seq_id :: {}", vo.getSeq_id());
                log.debug("==========================");
                
                // 項目・選択肢 (상품옵션) 처리
                TranslationVO transVO = new TranslationVO();
                transVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
                transVO.setKeyword(vo.getProduct_name());
                transVO.setBefore_trans(vo.getProduct_name());
                
                ArrayList<TranslationVO> transList = listMapper.getTransInfo(transVO);
                if (transList.size() == 0) {
                	listMapper.addTransInfo(transVO);
                }
                
                String optionContent = vo.getProduct_option();
                if(optionContent != null && optionContent.length() > 1) {
					String[] arr = optionContent.split(CommonUtil.SPLIT_BY_NPER);
					HashSet<String> set = new HashSet<>();
					for (int i=0; i<arr.length; i++) {
						log.debug(String.format("[%d] :: %s", i, arr[i]));
					String[] dataArr = arr[i].split(CommonUtil.SPLIT_BY_COLON);
					String data = null;
					if (dataArr.length > 1) {
						// 예외적인 경우로 콜론 바로 뒤에 데이터가 있는것이 아니라 콜론 두개 뒤에 있는 경우가 있어 스플릿 결과의 맨 마지막 값을 가져올 수 있도록 처리
						data = dataArr[dataArr.length-1];
						log.debug(String.format("option value1 :: %s", data));
						set.add(data.trim());
					} 
					/*
					// [MOD-0826]
					else {
						// 콜론이 아닌 일본어자판 컴마로 나뉘어져있는 경우가 있어 처리
						data = arr[i].split(CommonUtil.JPCOMMA);
						for (String value2 : data) {
							log.debug(String.format("option value2 :: %s", value2));
							set.add(value2.trim());
						}
					}
					
					// 거의 없겠다만 콜론과 일본어자판 컴마가 같이 있는 경우도 있어 처리
					if (data[0].contains(CommonUtil.JPCOMMA)) {
						data = data[0].split(CommonUtil.JPCOMMA);
						for (String value3 : data) {
							log.debug(String.format("option value3 :: %s", value3));
					    		set.add(value3.trim());
					    	}
					    }
					    */
					}
					
					log.debug("final option set :: {}", set);
					
					for (String value : set) {
						transVO.setKeyword(value);
						transVO.setBefore_trans(value);
						
						transList = listMapper.getTransInfo(transVO);
						if (transList.size() == 0) {
							listMapper.addTransInfo(transVO);
					    }
					}
					log.debug("==========================");
                }
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void yupuriDownload(HttpServletResponse response, String[] id_lst, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("yupuriDownload");
		log.debug("seq_id_list : {}", id_lst.toString());
		log.debug("encoding : {}", fileEncoding);
		
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "YU" + CommonUtil.getDate("YYYY-MM-dd HH:mm:ss", 0) + ".csv";

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
			
			String[] header = 
			{
				"受注番号"
				,"受注ステータス"
				,"カード決済ステータス"
				,"入金日"
				,"配送日"
				,"お届け時間帯"
				,"お届け日指定"
				,"担当者"
				,"ひとことメモ"
				,"メール差込文(お客様へのメッセージ)"
				,"初期購入合計金額"
				,"利用端末"
				,"メールキャリアコード"
				,"ギフトチェック（0:なし/1:あり）"
				,"コメント"
				,"注文日時"
				,"複数送付先フラグ"
				,"警告表示フラグ"
				,"楽天会員フラグ"
				,"合計"
				,"消費税(-99999=無効値)"
				,"送料(-99999=無効値)"
				,"代引料(-99999=無効値)"
				,"請求金額(-99999=無効値)"
				,"合計金額(-99999=無効値)"
				,"同梱ID"
				,"同梱ステータス"
				,"同梱商品合計金額"
				,"同梱送料合計"
				,"同梱代引料合計"
				,"同梱消費税合計"
				,"同梱請求金額"
				,"同梱合計金額"
				,"同梱楽天バンク決済振替手数料"
				,"同梱ポイント利用合計"
				,"メールフラグ"
				,"注文日"
				,"注文時間"
				,"モバイルキャリア決済番号"
				,"購入履歴修正可否タイプ"
				,"購入履歴修正アイコンフラグ"
				,"購入履歴修正催促メールフラグ"
				,"送付先一致フラグ"
				,"ポイント利用有無"
				,"注文者郵便番号１"
				,"注文者郵便番号２"
				,"注文者住所：都道府県"
				,"注文者住所：都市区"
				,"注文者住所：町以降"
				,"注文者名字"
				,"注文者名前"
				,"注文者名字フリガナ"
				,"注文者名前フリガナ"
				,"注文者電話番号１"
				,"注文者電話番号２"
				,"注文者電話番号３"
				,"メールアドレス"
				,"注文者性別"
				,"注文者誕生日"
				,"決済方法"
				,"クレジットカード種類"
				,"クレジットカード番号"
				,"クレジットカード名義人"
				,"クレジットカード有効期限"
				,"クレジットカード分割選択"
				,"クレジットカード分割備考"
				,"配送方法"
				,"配送区分"
				,"ポイント利用額"
				,"ポイント利用条件"
				,"ポイントステータス"
				,"楽天バンク決済ステータス"
				,"楽天バンク振替手数料負担区分"
				,"楽天バンク決済手数料"
				,"ラッピングタイトル(包装紙)"
				,"ラッピング名(包装紙)"
				,"ラッピング料金(包装紙)"
				,"税込別(包装紙)"
				,"ラッピングタイトル(リボン)"
				,"ラッピング名(リボン)"
				,"ラッピング料金(リボン)"
				,"税込別(リボン)"
				,"送付先送料"
				,"送付先代引料"
				,"送付先消費税"
				,"お荷物伝票番号"
				,"送付先商品合計金額"
				,"のし"
				,"送付先郵便番号１"
				,"送付先郵便番号２"
				,"送付先住所：都道府県"
				,"送付先住所：都市区"
				,"送付先住所：町以降"
				,"送付先名字"
				,"送付先名前"
				,"送付先名字フリガナ"
				,"送付先名前フリガナ"
				,"送付先電話番号１"
				,"送付先電話番号２"
				,"送付先電話番号３"
				,"商品ID"
				,"商品名"
				,"商品番号"
				,"商品URL"
				,"単価"
				,"個数"
				,"送料込別"
				,"税込別"
				,"代引手数料込別"
				,"項目・選択肢"
				,"ポイント倍率"
				,"ポイントタイプ"
				,"レコードナンバー"
				,"納期情報"
				,"在庫タイプ"
				,"ラッピング種類(包装紙)"
				,"ラッピング種類(リボン)"
				,"あす楽希望"
				,"クーポン利用額"
				,"店舗発行クーポン利用額"
				,"楽天発行クーポン利用額"
				,"同梱注文クーポン利用額"
				,"配送会社"
				,"薬事フラグ"
				,"楽天スーパーDEAL"
				,"メンバーシッププログラム"
			};
			
			RakutenVO vo = new RakutenVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			vo.setSeq_id_list(seq_id_list);
			ArrayList<RakutenVO> list = mapper.getYUCSVDownList(vo);
			
			for (RakutenVO tmp : list) {
				String comment = tmp.getOrder_comment();
				comment = comment.replace("\n", "");
				tmp.setOrder_comment(comment);
				
				String option = tmp.getProduct_option();
				if (option != null) {
					option = option.replace("\n", "");
					tmp.setProduct_option(option);
				}
				tmp.setDelivery_name(tmp.getDelivery_name() + "様");
			}
			
			executeYUDownload(csvWriter, writer, header, list);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void rCSVDownload(HttpServletResponse response, String[] id_lst, String fileEncoding) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rCSVDownload");
		log.debug("seq_id_list : {}", id_lst.toString());
		log.debug("encoding : {}", fileEncoding);
		
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "R" + CommonUtil.getDate("YYYY-MM-dd HH:mm:ss", 0) + ".csv";

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
			
			String[] header = {"受注番号", "受注ステータス", "処理番号", "お荷物伝票番号", "配送会社", "フリー項目01"};
			
			RCSVDownVO vo = new RCSVDownVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			vo.setSeq_id_list(seq_id_list);
			ArrayList<RCSVDownVO> list = mapper.getRCSVDownList(vo);
			
			executeRakutenDownload(csvWriter, writer, header, list);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeRakutenDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<RCSVDownVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<RCSVDownVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeYUDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<RakutenVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<RakutenVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
	
	public void insertAmazonInfo(MultipartFile amaUpload, String fileEncoding) throws IOException {
		BufferedReader reader = null;
        try {
			reader = new BufferedReader(
			                new InputStreamReader(amaUpload.getInputStream(), fileEncoding));
			String line = "";
			String splitBy = "";
			
			while ((line = reader.readLine()) != null) {
				log.debug(String.format("\none line\n%s", line));
			        String[] whatArr = line.split(splitBy);
			        for (String what : whatArr) {
			        	log.debug(String.format("%s", what));
			        }
			}
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
	}
	
	public void updateRakutenInfo(MultipartFile amaUpload, String fileEncoding) {
		
	}
}
