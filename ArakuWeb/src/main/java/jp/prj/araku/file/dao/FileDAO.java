package jp.prj.araku.file.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import jp.prj.araku.file.vo.YamatoVO;
import jp.prj.araku.list.mapper.IListMapper;
import jp.prj.araku.list.vo.RakutenSearchVO;
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
	public void insertRakutenInfo(MultipartFile rakUpload, String fileEncoding, HttpServletRequest req) throws IOException {
		log.info("insertRakutenInfo");
		IFileMapper fileMapper = sqlSession.getMapper(IFileMapper.class);
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		log.debug("encoding : {}", fileEncoding);
		log.debug("contentType: {}", rakUpload.getContentType());
		log.debug("name: {}", rakUpload.getName());
		log.debug("original name: {}", rakUpload.getOriginalFilename());
		log.debug("size: {}", rakUpload.getSize());
		
		BufferedReader reader = null;
		ArrayList<RakutenVO> errList = new ArrayList<>();
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
        		
        		// 이미 존재하는 受注番号가 있으면
        		if (dupCheckList.size() == 1) {
        			// 商品ID가 같으면
        			if (vo.getProduct_id().equals(dupCheckList.get(0).getProduct_id())) {
        				// 다음 레코드로 진행
                		continue;
        			} else {
        				// 商品ID가 다르면 한사람이 여러 상품을 주문한 것으로 간주, 에러리스트에 넣은후 다음 레코드로 진행
        				log.debug("[ERR]: {}", vo.getOrder_no());
        				errList.add(vo);
            			continue;
        			}
        		}
        		
        		// あす楽希望이 1인 경우
        		if (vo.getTomorrow_hope().equals("1")) {
        			// お届け日指定 컬럼에 데이터 등록일 +1
        			vo.setDelivery_date_sel(CommonUtil.getDate("yyyy/MM/dd", 1));
        			// お届け時間帯 컬럼에 午前中
        			vo.setDelivery_time(CommonUtil.TOMORROW_MORNING);
        		}
        		
        		try {
        			fileMapper.insertRakutenInfo(vo);
        		} catch (Exception e) {
        			// 에러 발생시 에러리스트에 넣은후 다음 레코드로 진행
        			log.debug("[ERR]: {}", vo.getOrder_no());
        			errList.add(vo);
        			continue;
				}
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
			
			HttpSession session = req.getSession();
			session.setAttribute("errSize", errList.size());
			session.setAttribute("errList", errList);
		}
	}
	
	public void rakutenFormatCSVDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String type
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		rakutenFormatCSVDownload(null, response, id_lst, fileEncoding, type, delivery_company);
	}
	
	@SuppressWarnings("unchecked")
	public void rakutenFormatCSVDownload(
			HttpServletRequest request
			, HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String type
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("rakutenFormatCSVDownload");
		
		log.debug("encoding : {}", fileEncoding);
		log.debug("type : {}", type);
		
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = type + CommonUtil.getDate("YYYY-MM-dd HH:mm:ss", 0) + ".csv";

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
			
			ArrayList<RakutenVO> list = null;
			if ("YU".equals(type)) {
				log.debug("seq_id_list : {}", id_lst.toString());
				RakutenVO vo = new RakutenVO();
				ArrayList<String> seq_id_list = new ArrayList<>();
				for (String seq_id : id_lst) {
					seq_id_list.add(seq_id);
				}
				vo.setSeq_id_list(seq_id_list);
				vo.setDelivery_company(delivery_company);
				
				list = mapper.getYUCSVDownList(vo);
				
				for (RakutenVO tmp : list) {
					tmp.setDelivery_name(tmp.getDelivery_name() + " " + CommonUtil.TITLE_SAMA);
				}
				
			} else if ("ERR".equals(type)) {
				HttpSession session = request.getSession();
				list = (ArrayList<RakutenVO>) session.getAttribute("errList");
				session.setAttribute("errSize", "");
				session.setAttribute("errList", "");
			}
			
			for (RakutenVO tmp : list) {
				String comment = tmp.getOrder_comment();
				comment = comment.replace("\n", "");
				tmp.setOrder_comment(comment);
				
				String option = tmp.getProduct_option();
				if (option != null) {
					option = option.replace("\n", "");
					tmp.setProduct_option(option);
				}
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
	
	public void updateRakutenInfo(MultipartFile yuUpload, String fileEncoding) throws IOException {
		log.info("updateRakutenInfo");
		
		log.debug("encoding : {}", fileEncoding);
		log.debug("contentType: {}", yuUpload.getContentType());
		log.debug("name: {}", yuUpload.getName());
		log.debug("original name: {}", yuUpload.getOriginalFilename());
		log.debug("size: {}", yuUpload.getSize());
		
		IListMapper listMapper = sqlSession.getMapper(IListMapper.class);
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(yuUpload.getInputStream(), fileEncoding));
			
			CsvToBean<RCSVDownVO> csvToBean = new CsvToBeanBuilder<RCSVDownVO>(reader)
	                .withType(RCSVDownVO.class)
	                .withSkipLines(1)
	                .withIgnoreLeadingWhiteSpace(true)
	                .build();

	        Iterator<RCSVDownVO> iterator = csvToBean.iterator();
	        
	        while (iterator.hasNext()) {
	        	RCSVDownVO vo = iterator.next();
            	
            	// 受注番号 검색하여 お荷物伝票番号를 update
            	RakutenSearchVO searchVO = new RakutenSearchVO();
            	searchVO.setSearch_type(CommonUtil.SEARCH_TYPE_SRCH);
            	searchVO.setOrder_no(vo.getOrder_no());
            	
            	ArrayList<RakutenSearchVO> searchResult = listMapper.getRList(searchVO);
            	String seq_id = searchResult.get(0).getSeq_id();
            	
            	searchVO.setSeq_id(seq_id);
            	searchVO.setBaggage_claim_no(vo.getBaggage_claim_no());
            	listMapper.modRakutenInfo(searchVO);
	        }
		} finally {
			if (reader != null) {
                reader.close();
            }
		}
	}
	
	public void yamatoFormatDownload(
			HttpServletResponse response
			, String[] id_lst
			, String fileEncoding
			, String delivery_company) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("yamatoFormatDownload");
		
		log.debug("encoding : {}", fileEncoding);
		
		IFileMapper mapper = sqlSession.getMapper(IFileMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "YAM" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";

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
				"お客様管理番号"
				, "送り状種類"
				, "クール区分"
				, "伝票番号"
				, "出荷予定日"
				, "お届け予定日"
				, "配達時間帯"
				, "お届け先コード"
				, "お届け先電話番号"
				, "お届け先電話番号枝番"
				, "お届け先郵便番号"
				, "お届け先住所"
				, "お届け先アパートマンション名"
				, "お届け先会社・部門１"
				, "お届け先会社・部門２"
				, "お届け先名"
				, "お届け先名(ｶﾅ)"
				, "敬称"
				, "ご依頼主コード"
				, "ご依頼主電話番号"
				, "ご依頼主郵便番号"
				, "ご依頼主住所"
				, "ご依頼主アパートマンション"
				, "ご依頼主名"
				, "ご依頼主名(ｶﾅ)"
				, "品名コード１"
				, "品名１"
				, "品名コード２"
				, "品名２"
				, "荷扱い１"
				, "荷扱い２"
				, "記事"
				, "ｺﾚｸﾄ代金引換額（税込)"
				, "内消費税額等"
				, "止置き"
				, "営業所コード"
				, "発行枚数"
				, "個数口表示フラグ"
				, "請求先顧客コード"
				, "請求先分類コード"
				, "運賃管理番号"
				, "クロネコwebコレクトデータ登録"
				, "クロネコwebコレクト加盟店番号"
				, "クロネコwebコレクト申込受付番号１"
				, "クロネコwebコレクト申込受付番号２"
				, "クロネコwebコレクト申込受付番号３"
				, "お届け予定ｅメール利用区分"
				, "お届け予定ｅメールe-mailアドレス"
				, "入力機種"
				, "お届け予定ｅメールメッセージ"
				, "お届け完了ｅメール利用区分"
				, "お届け完了ｅメールe-mailアドレス"
				, "お届け完了ｅメールメッセージ"
				, "クロネコ収納代行利用区分"
				, "予備"
				, "収納代行請求金額(税込)"
				, "収納代行内消費税額等"
				, "収納代行請求先郵便番号"
				, "収納代行請求先住所"
				, "収納代行請求先住所（アパートマンション名）"
				, "収納代行請求先会社・部門名１"
				, "収納代行請求先会社・部門名２"
				, "収納代行請求先名(漢字)"
				, "収納代行請求先名(カナ)"
				, "収納代行問合せ先名(漢字)"
				, "収納代行問合せ先郵便番号"
				, "収納代行問合せ先住所"
				, "収納代行問合せ先住所（アパートマンション名）"
				, "収納代行問合せ先電話番号"
				, "収納代行管理番号"
				, "収納代行品名"
				, "収納代行備考"
				, "複数口くくりキー"
				, "検索キータイトル1"
				, "検索キー1"
				, "検索キータイトル2"
				, "検索キー2"
				, "検索キータイトル3"
				, "検索キー3"
				, "検索キータイトル4"
				, "検索キー4"
				, "検索キータイトル5"
				, "検索キー5"
				, "予備"
				, "予備"
				, "投函予定メール利用区分"
				, "投函予定メールe-mailアドレス"
				, "投函予定メールメッセージ"
				, "投函完了メール（お届け先宛）利用区分"
				, "投函完了メール（お届け先宛）e-mailアドレス"
				, "投函完了メール（お届け先宛）メールメッセージ"
				, "投函完了メール（ご依頼主宛）利用区分"
				, "投函完了メール（ご依頼主宛）e-mailアドレス"
				, "投函完了メール（ご依頼主宛）メールメッセージ"
			};
			
			// 야마토 포맷으로 바꾸기 전 치환된 결과와 함께 라쿠텐정보 얻기
			log.debug("seq_id_list : {}", id_lst.toString());
			RakutenVO vo = new RakutenVO();
			ArrayList<String> seq_id_list = new ArrayList<>();
			for (String seq_id : id_lst) {
				seq_id_list.add(seq_id);
			}
			vo.setSeq_id_list(seq_id_list);
			vo.setDelivery_company(delivery_company);
			
			ArrayList<RakutenVO> list = mapper.getYUCSVDownList(vo);
			ArrayList<YamatoVO> yList = new ArrayList<>();
			
			for (RakutenVO tmp : list) {
				YamatoVO yVO = new YamatoVO();
				yVO.setCustomer_no(tmp.getOrder_no().replace("\"", ""));
				yVO.setInvoice_type(CommonUtil.INVOICE_TYPE_0);
				yVO.setComment(tmp.getOrder_comment().replace("\n", "").replace("\"", "").replace("[配送日時指定:]", "").replace("[備考:]", ""));
				yVO.setCollect_cash(tmp.getTotal_amt().replace("\"", ""));
				yVO.setEstimate_ship_date(CommonUtil.getDate("YYYY/MM/dd", 0));
				yVO.setBill_customer_code("048299821004-311");
				yVO.setMultiple_key("1");
				
				yVO.setClient_post_no("3330845");
				yVO.setClient_add("埼玉県川口市上青木西１丁目19-39");
				yVO.setClient_building("エレガンス滝澤ビル1F");
				yVO.setClient_name("有限会社ItempiaJapan");
//				yVO.setClient_name_kana(tmp.getOrder_surname_kana().replace("\"", "") + " " + tmp.getOrder_name_kana().replace("\"", ""));
				yVO.setClient_tel("048-242-3801");
				
				yVO.setDelivery_post_no(tmp.getDelivery_post_no1().replace("\"", "") + tmp.getDelivery_post_no2().replace("\"", ""));
				yVO.setDelivery_add(tmp.getDelivery_address1().replace("\"", "") + "" + tmp.getDelivery_address2().replace("\"", "") + "" + tmp.getDelivery_address3().replace("\"", ""));
				yVO.setDelivery_name(tmp.getDelivery_surname().replace("\"", "") + " " + tmp.getDelivery_name().replace("\"", ""));
//				yVO.setDelivery_name_kana(tmp.getDelivery_surname_kana().replace("\"", "") + " " + tmp.getDelivery_name_kana());
				yVO.setDelivery_name_title(CommonUtil.TITLE_SAMA);
				yVO.setDelivery_tel(tmp.getDelivery_tel1().replace("\"", "") + "-" + tmp.getDelivery_tel2().replace("\"", "") + "-" + tmp.getDelivery_tel3().replace("\"", ""));
				
				// あす楽希望이 1인 경우
        		if (tmp.getTomorrow_hope().equals("1")) {
        			yVO.setDelivery_time(CommonUtil.TOMORROW_MORNING_CODE);
        		}
				
				yVO.setProduct_name1(tmp.getProduct_name().replace("\"", ""));
				
				// csv작성을 위한 리스트작성
				yList.add(yVO);
			}
			
			executeYamatoDownload(csvWriter, writer, header, yList);
			
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
	public void executeYamatoDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<YamatoVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<YamatoVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
}
