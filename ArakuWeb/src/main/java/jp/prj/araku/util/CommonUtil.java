package jp.prj.araku.util;

import java.io.BufferedWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CommonUtil {
	public static final String SEARCH_TYPE_SRCH = "srch";
	public static final String SEARCH_TYPE_SAGAWA = "saga";
	public static final String SEARCH_TYPE_WEEKDATA = "weekData";
	public static final String SEARCH_TYPE_SCREEN = "screen";
	
	public static final String UPDATE_TYPE_TWOMORE="twomore";
	
	public static final String SPLIT_BY_TAB = "	";
	public static final String SPLIT_BY_STAR = "\\*";
	public static final String SPLIT_BY_NPER = "\n";
	public static final String SPLIT_BY_COLON = ":";
	public static final String SPLIT_BY_SEMICOLON = ";";
	public static final String SPLIT_BY_SLASH = "/";
	public static final String JPCOMMA = "、";
	
	public static final String TRANS_TARGET_R = "R"; /*R:楽天*/
	public static final String TRANS_TARGET_RF = "RF"; /*RF:楽天の冷凍冷蔵区分*/
	public static final String TRANS_TARGET_A = "A"; /*A:アマゾン*/
	public static final String TRANS_TARGET_Q = "Q"; /*Q:qoo10*/
	public static final String TRANS_TARGET_Y = "Y"; /*Y:yahoo*/
	public static final String TRANS_TARGET_TA = "TA"; /*TA:tablet*/
	public static final String TRANS_ERR = "ERR"; /*エラーテキスト*/
	
	public static final String ORDER_STATUS_COMPLETE = "出荷準備済";
	public static final String TOMORROW_MORNING = "午前中";
	public static final String TIMEMAP1 = "14：00～16：00";
	public static final String TIMEMAP2 = "16：00～18：00";
	public static final String TIMEMAP3 = "18：00～20：00";
	public static final String TIMEMAP4 = "19：00～21：00";
	public static final String YA_TOMORROW_MORNING_CODE = "0812";
	public static final String YA_TOMORROW_TIMEMAP1 = "1416";
	public static final String YA_TOMORROW_TIMEMAP2 = "1618";
	public static final String YA_TOMORROW_TIMEMAP3 = "1820";
	public static final String YA_TOMORROW_TIMEMAP4 = "1921";
	public static final String SA_TOMORROW_MORNING_CODE = "01";
	
	public static final String SA_TOMORROW_TIMEMAP1 = "14";
	public static final String SA_TOMORROW_TIMEMAP2 = "16";
	public static final String SA_TOMORROW_TIMEMAP3 = "18";
	public static final String SA_TOMORROW_TIMEMAP4 = "19";
	
	public static final String TITLE_SAMA = "様";
	public static final String INVOICE_TYPE_0 = "0";/*発払い*/
	public static final String INVOICE_TYPE_2 = "2";/*コレクト*/
	public static final String INVOICE_TYPE_3 = "3";/*ＤＭ便*/
	public static final String INVOICE_TYPE_7 = "7"; /*ネコポス*/
	public static final String COOL_TYPE_1 = "1"; /*冷凍*/
	public static final String COOL_TYPE_2 = "2"; /*冷蔵*/
	
	public static String getStartDate() {
		Calendar cal = Calendar.getInstance( );
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date today = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(today) + " 00:00:00";
	}
	
	public static String getDate(String format, int day) { 
		Calendar cal = Calendar.getInstance( ); 
		cal.add(Calendar.DAY_OF_MONTH, day);
		Date today = cal.getTime(); 
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		return sdf.format(today); 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void executeCSVDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<ArakuVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<ArakuVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
	
	public static String hankakuNumToZenkaku(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i=0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (c >= '0' && c <= '9') {
				sb.setCharAt(i, (char) (c - '0' + '０'));
			}
		}
		
		return sb.toString();
	}
	
	public static String[] deliveryCompanyHeader(String type) {
		String[] ret = {};
		if("YAMA".equals(type)) {
			String[] arr1 = {
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
			ret = arr1;
		} else if("SAGA".equals(type)) {
			String[] arr2 = {
					"住所録コード"
					, "お届け先電話番号"
					, "お届け先郵便番号"
					, "お届け先住所１（必須）"
					, "お届け先住所２"
					, "お届け先住所３"
					, "お届け先名称１（必須）"
					, "お届け先名称２"
					, "お客様管理ナンバー"
					, "お客様コード"
					, "部署・担当者"
					, "荷送人電話番号"
					, "ご依頼主電話番号"
					, "ご依頼主郵便番号"
					, "ご依頼主住所１"
					, "ご依頼主住所２"
					, "ご依頼主名称１"
					, "ご依頼主名称２"
					, "荷姿コード"
					, "品名１"
					, "品名２"
					, "品名３"
					, "品名４"
					, "品名５"
					, "出荷個数"
					, "便種（スピードで選択）"
					, "便種（商品）"
					, "配達日"
					, "配達指定時間帯"
					, "配達指定時間（時分）"
					, "代引金額"
					, "消費税"
					, "決済種別"
					, "保険金額"
					, "保険金額印字"
					, "指定シール①"
					, "指定シール②"
					, "指定シール③"
					, "営業店止め"
					, "ＳＲＣ区分"
					, "営業店コード"
					, "元着区分"
			};
			ret = arr2;
		} else if("CLICK".equals(type)) {
			String[] arr3 = {
					"お届け先郵便番号"
					,"お届け先氏名"
					,"お届け先敬称"
					,"お届け先住所1行目"
					,"お届け先住所2行目"
					,"お届け先住所3行目"
					,"お届け先住所4行目"
					,"内容品"
				};
			ret = arr3;
		}else if("GSAGA".equals(type)) {
			String[] arr4= {
					"Seller CODE"
					,"PICKUP_DATE"
					,"ORDER_NO"
					,"CONSIGNEE_NAME"
					,"YOMIGANA"
					,"CONSIGNEE_ADDRESS1"
					,"CONSIGNEE_ADDRESS2"
					,"CONSIGNEE_POSTALCODE"
					,"CONSIGNEE_PHONENO"
					,"CONSIGNEE_MAILADDRESS"
					,"DELIVERY_DATE"
					,"DELIVERY_TIME"
					,"PKG"
					,"WEIGHT"
					,"ITEM_CODE"
					,"ITEM_NAME"
					,"ITEM_PCS"
					,"UNIT_PRICE"
					,"ITEM_ORIGIN"

			};
			ret = arr4;
		}
		return ret;
	}
	
	public static String[] rakutenHeader() {
		String[] header = 
			{
				"注文番号"
				, "ステータス"
				, "サブステータスID"
				, "サブステータス"
				, "注文日時"
				, "注文日"
				, "注文時間"
				, "キャンセル期限日"
				, "注文確認日時"
				, "注文確定日時"
				, "発送指示日時"
				, "発送完了報告日時"
				, "支払方法名"
				, "クレジットカード支払い方法"
				, "クレジットカード支払い回数"
				, "配送方法"
				, "配送区分"
				, "注文種別"
				, "複数送付先フラグ"
				, "送付先一致フラグ"
				, "離島フラグ"
				, "楽天確認中フラグ"
				, "警告表示タイプ"
				, "楽天会員フラグ"
				, "購入履歴修正有無フラグ"
				, "商品合計金額"
				, "消費税合計"
				, "送料合計"
				, "代引料合計"
				, "請求金額"
				, "合計金額"
				, "ポイント利用額"
				, "クーポン利用総額"
				, "店舗発行クーポン利用額"
				, "楽天発行クーポン利用額"
				, "注文者郵便番号1"
				, "注文者郵便番号2"
				, "注文者住所都道府県"
				, "注文者住所郡市区"
				, "注文者住所それ以降の住所"
				, "注文者姓"
				, "注文者名"
				, "注文者姓カナ"
				, "注文者名カナ"
				, "注文者電話番号1"
				, "注文者電話番号2"
				, "注文者電話番号3"
				, "注文者メールアドレス"
				, "注文者性別"
				, "申込番号"
				, "申込お届け回数"
				, "送付先ID"
				, "送付先送料"
				, "送付先代引料"
				, "送付先消費税合計"
				, "送付先商品合計金額"
				, "送付先合計金額"
				, "のし"
				, "送付先郵便番号1"
				, "送付先郵便番号2"
				, "送付先住所都道府県"
				, "送付先住所郡市区"
				, "送付先住所それ以降の住所"
				, "送付先姓"
				, "送付先名"
				, "送付先姓カナ"
				, "送付先名カナ"
				, "送付先電話番号1"
				, "送付先電話番号2"
				, "送付先電話番号3"
				, "商品明細ID"
				, "商品ID"
				, "商品名"
				, "商品番号"
				, "商品管理番号"
				, "単価"
				, "個数"
				, "送料込別"
				, "税込別"
				, "代引手数料込別"
				, "ポイント倍率"
				, "納期情報"
				, "在庫タイプ"
				, "ラッピングタイトル1"
				, "ラッピング名1"
				, "ラッピング料金1"
				, "ラッピング税込別1"
				, "ラッピング種類1"
				, "ラッピングタイトル2"
				, "ラッピング名2"
				, "ラッピング料金2"
				, "ラッピング税込別2"
				, "ラッピング種類2"
				, "お届け時間帯"
				, "お届け日指定"
				, "担当者"
				, "ひとことメモ"
				, "メール差込文 (お客様へのメッセージ)"
				, "ギフト配送希望"
				, "利用端末"
				, "メールキャリアコード"
				, "あす楽希望フラグ"
				, "医薬品受注フラグ"
				, "楽天スーパーDEAL商品受注フラグ"
				, "メンバーシッププログラム受注タイプ"
			};
		return header;
	}
	
	public static String[] amazonHeader() {
		String[] header = 
			{
				"order-id"
				, "order-item-id"
				, "purchase-date"
				, "payments-date"
				, "reporting-date"
				, "promise-date"
				, "days-past-promise"
				, "buyer-email"
				, "buyer-name"
				, "buyer-phone-number"
				, "sku"
				, "product-name"
				, "quantity-purchased"
				, "quantity-shipped"
				, "quantity-to-ship"
				, "ship-service-level"
				, "recipient-name"
				, "ship-address-1"
				, "ship-address-2"
				, "ship-address-3"
				, "ship-city"
				, "ship-state"
				, "ship-postal-code"
				, "ship-country"
				, "payment-method"
				, "cod-collectible-amount"
				, "already-paid"
				, "payment-method-fee"
				, "scheduled-delivery-start-date"
				, "scheduled-delivery-end-date"
				, "points-granted"
				, "is-prime"
			};
		return header;
	}
	
	public static String[] q10Header() {
		String[] header = 
			{
				"配送状態"
				, "注文番号"
				, "カート番号"
				, "配送会社"
				, "送り状番号"
				, "発送日"
				, "注文日"
				, "入金日"
				, "お届け希望日"
				, "発送予定日"
				, "配送完了日"
				, "配送方法"
				, "商品番号"
				, "商品名"
				, "数量"
				, "オプション情報"
				, "オプションコード"
				, "おまけ"
				, "受取人名"
				, "受取人名(フリガナ)"
				, "受取人電話番号"
				, "受取人携帯電話番号"
				, "住所"
				, "郵便番号"
				, "国家"
				, "送料の決済"
				, "決済サイト"
				, "通貨"
				, "購入者決済金額"
				, "販売価格"
				, "割引額"
				, "注文金額の合計"
				, "供給原価の合計"
				, "購入者名"
				, "購入者名(フリガナ)"
				, "配送要請事項"
				, "購入者電話番号"
				, "購入者携帯電話番号"
				, "販売者商品コード"
				, "JANコード"
				, "規格番号"
				, "プレゼント贈り主"
				, "外部広告"
				, "素材"
			};
		return header;
	}
	
	public static String toCommaFormat(long pdMoney) {
		DecimalFormat form = new DecimalFormat("#,###");
	    form.setDecimalSeparatorAlwaysShown(false);
        return form.format(pdMoney);
	}
	
}
