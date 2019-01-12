package jp.prj.araku.batch.dao;

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

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.prj.araku.batch.mapper.IBatchItemsMapper;
import jp.prj.araku.batch.vo.ItemCatOutputVO;
import jp.prj.araku.batch.vo.ItemOutputVO;
import jp.prj.araku.batch.vo.SelectOutputVO;
import jp.prj.araku.util.ArakuVO;
import jp.prj.araku.util.CommonUtil;

@Repository
public class BatchDAO {
	@Autowired
	SqlSession sqlSession;
	
	private static final Logger log = Logger.getLogger("jp.prj.araku.batch");
	
	public void insertItemsInfo(MultipartFile itemsUpload, String fileEncoding, HttpServletRequest req) throws IOException {
		log.info("insertItemsInfo");
		
		IBatchItemsMapper mapper = sqlSession.getMapper(IBatchItemsMapper.class);
		
		log.debug("encoding : " + fileEncoding);
		log.debug("contentType: " + itemsUpload.getContentType());
		log.debug("name: " + itemsUpload.getName());
		log.debug("original name: " + itemsUpload.getOriginalFilename());
		log.debug("size: " + itemsUpload.getSize());
		
		BufferedReader reader = null;
		try  {
			reader = new BufferedReader(
					new InputStreamReader(itemsUpload.getInputStream(), fileEncoding));
			
			CsvToBean<ItemOutputVO> csvToBean = new CsvToBeanBuilder<ItemOutputVO>(reader)
                    .withType(ItemOutputVO.class)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<ItemOutputVO> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
            	ItemOutputVO vo = iterator.next();
            	
        		try {
        			mapper.insertItemsInfo(vo);
        		} catch (Exception e) {
        			continue;
				}
                log.debug("inserted item seq_id :: " + vo.getSeq_id());
                log.debug("==========================");
                
            }
            
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public ArrayList<ItemOutputVO> getItemsInfo(ItemOutputVO vo) {
		log.info("getItemsInfo");
		log.debug(vo.toString());
		IBatchItemsMapper mapper = sqlSession.getMapper(IBatchItemsMapper.class);
		return mapper.getItemsInfo(vo);
	}
	
	public void itemsCsvDownload(
			HttpServletResponse resp
			, String fileEncoding
			, String type
			, ItemOutputVO vo) 
			throws IOException
			, CsvDataTypeMismatchException
			, CsvRequiredFieldEmptyException {
		log.info("itemsCsvDownload");
		
		log.debug(vo.toString());
		log.debug(String.format("file encoding : %s", fileEncoding));
		log.debug(String.format("type: %s", type));
		
		IBatchItemsMapper mapper = sqlSession.getMapper(IBatchItemsMapper.class);
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		
		try {
			String csvFileName = "";
			String[] header = {};
			
			ArrayList<ItemOutputVO> list = mapper.getItemsInfo(vo);
			ArrayList<ArakuVO> listForCsv = new ArrayList<>();
			
			switch (type) {
			case "item":
				csvFileName = "item" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
				header = new String[] {
						"コントロールカラム"
						, "商品管理番号（商品URL）"
						, "商品番号"
						, "全商品ディレクトリID"
						, "タグID"
						, "PC用キャッチコピー"
						, "モバイル用キャッチコピー"
						, "商品名"
						, "販売価格"
						, "表示価格"
						, "消費税"
						, "送料"
						, "個別送料"
						, "送料区分1"
						, "送料区分2"
						, "代引料"
						, "倉庫指定"
						, "商品情報レイアウト"
						, "注文ボタン"
						, "資料請求ボタン"
						, "商品問い合わせボタン"
						, "再入荷お知らせボタン"
						, "のし対応"
						, "PC用商品説明文"
						, "スマートフォン用商品説明文"
						, "PC用販売説明文"
						, "商品画像URL"
						, "商品画像名（ALT）"
						, "動画"
						, "販売期間指定"
						, "注文受付数"
						, "在庫タイプ"
						, "在庫数"
						, "在庫数表示"
						, "項目選択肢別在庫用横軸項目名"
						, "項目選択肢別在庫用縦軸項目名"
						, "項目選択肢別在庫用残り表示閾値"
						, "サーチ非表示"
						, "闇市パスワード"
						, "カタログID"
						, "在庫戻しフラグ"
						, "在庫切れ時の注文受付"
						, "在庫あり時納期管理番号"
						, "在庫切れ時納期管理番号"
						, "予約商品発売日"
						, "ポイント変倍率"
						, "ポイント変倍率適用期間"
						, "ヘッダー・フッター・レフトナビ"
						, "表示項目の並び順"
						, "共通説明文（小）"
						, "目玉商品"
						, "共通説明文（大）"
						, "レビュー本文表示"
						, "あす楽配送管理番号"
						, "海外配送管理番号"
						, "サイズ表リンク"
						, "二重価格文言管理番号"
						, "カタログIDなしの理由"
						, "配送方法セット管理番号"
						, "白背景画像URL"
						, "メーカー提供情報表示"
				};
			
				for (ItemOutputVO outVO : list) {
					listForCsv.add(outVO);
				}
				
				break;
				
			case "itemCat":
				csvFileName = "item-cat" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
				header = new String[] {
						"コントロールカラム"
						, "商品管理番号（商品URL）"
						, "商品名"
						, "優先度"
						, "1ページ複数形式"
						, "表示先カテゴリ"
				};
				
				for (ItemOutputVO outVO : list) {
					ItemCatOutputVO catVO = new ItemCatOutputVO();
					catVO.setCtrl_col(outVO.getCtrl_col());
					catVO.setItem_url(outVO.getItem_url());
					catVO.setItem_name(outVO.getItem_name());
					catVO.setPriority("");
					catVO.setPage1_multi_format("");
					catVO.setDestination_category("");
					
					listForCsv.add(catVO);
				}
				
				break;
				
			case "selectOut":
				csvFileName = "select" + CommonUtil.getDate("YYYYMMdd", 0) + ".csv";
				header = new String[] {
						"項目選択肢用コントロールカラム"
						, "商品管理番号（商品URL）"
						, "選択肢タイプ"
						, "Select/Checkbox用項目名"
						, "Select/Checkbox用選択肢"
						, "項目選択肢別在庫用横軸選択肢"
						, "項目選択肢別在庫用横軸選択肢子番号"
						, "項目選択肢別在庫用縦軸選択肢"
						, "項目選択肢別在庫用縦軸選択肢子番号"
						, "項目選択肢別在庫用取り寄せ可能表示"
						, "項目選択肢別在庫用在庫数"
						, "在庫戻しフラグ"
						, "在庫切れ時の注文受付"
						, "在庫あり時納期管理番号"
						, "在庫切れ時納期管理番号"
						, "タグID"
						, "画像URL"
				};
				
				for (ItemOutputVO outVO : list) {
					SelectOutputVO selVO = new SelectOutputVO();
					selVO.setItem_ctrl_col(outVO.getCtrl_col());
					selVO.setItem_url(outVO.getItem_url());
					selVO.setSelection_type("");
					selVO.setSel_chk_item_name("");
					selVO.setSel_chk_options("");
					selVO.setItem_horizon_axis("");
					selVO.setItem_horizon_choice_num("");
					selVO.setItem_vertical_axis("");
					selVO.setItem_vertical_choice_num("");
					selVO.setItem_orderable_display("");
					selVO.setItem_inventory("");
					selVO.setStock_flag("");
					selVO.setOut_stock_order("");
					selVO.setIn_stock_delivery_num("");
					selVO.setOut_stock_delivery_num("");
					selVO.setTag_id(outVO.getTag_id());
					selVO.setImage_url(outVO.getItem_image_url());
					
					listForCsv.add(selVO);
				}
				
				break;

			}

			resp.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			resp.setHeader(headerKey, headerValue);
			resp.setCharacterEncoding(fileEncoding);
			
			writer = new BufferedWriter(resp.getWriter());
			
			csvWriter = new CSVWriter(writer
					, CSVWriter.DEFAULT_SEPARATOR
					, CSVWriter.NO_QUOTE_CHARACTER
					, CSVWriter.DEFAULT_ESCAPE_CHARACTER
					, CSVWriter.DEFAULT_LINE_END);
			
			
			CommonUtil.executeCSVDownload(csvWriter, writer, header, listForCsv);
			
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public ArrayList<ItemOutputVO> updateItems(ArrayList<ItemOutputVO> list) {
		log.info("updateItems");
		
		log.debug(list.toString());
		
		IBatchItemsMapper mapper = sqlSession.getMapper(IBatchItemsMapper.class);
		ItemOutputVO searchVO = new ItemOutputVO();
		
		for (ItemOutputVO vo : list) {
			searchVO.setStart_date(vo.getStart_date());
			searchVO.setEnd_date(vo.getEnd_date());
			
			mapper.updateItemsInfo(vo);
		}
		
		return mapper.getItemsInfo(searchVO);
	}
}
