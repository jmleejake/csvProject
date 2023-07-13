package jp.prj.araku.rakuten.vo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

public @Data class RakutenVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**データ修正日*/
	private String update_date;
	/**注文番号*/
	@CsvBindByPosition(position=0)
	private String order_no;
	/**ステータス*/
	@CsvBindByPosition(position=1)
	private String order_status;
	/**サブステータスID*/
	@CsvBindByPosition(position=2)
	private String sub_status_id;
	/**サブステータス*/
	@CsvBindByPosition(position=3)
	private String sub_status;
	/**注文日時*/
	@CsvBindByPosition(position=4)
	private String order_datetime;
	/**注文日*/
	@CsvBindByPosition(position=5)
	private String order_date;
	/**注文時間*/
	@CsvBindByPosition(position=6)
	private String order_time;
	/**キャンセル期限日*/
	@CsvBindByPosition(position=7)
	private String cancel_due_date;
	/**注文確認日時*/
	@CsvBindByPosition(position=8)
	private String order_check_datetime;
	/**注文確定日時*/
	@CsvBindByPosition(position=9)
	private String order_confirm_datetime;
	/**発送指示日時*/
	@CsvBindByPosition(position=10)
	private String delivery_eta_datetime;
	/**発送完了報告日時*/
	@CsvBindByPosition(position=11)
	private String delivery_ata_datetime;
	/**支払方法名*/
	@CsvBindByPosition(position=12)
	private String pay_method_name;
	/**クレジットカード支払い方法*/
	@CsvBindByPosition(position=13)
	private String creadit_pay_method;
	/**クレジットカード支払い回数*/
	@CsvBindByPosition(position=14)
	private String credit_pay_times;
	/**配送方法*/
	@CsvBindByPosition(position=15)
	private String delivery_method;
	/**配送区分*/
	@CsvBindByPosition(position=16)
	private String delivery_type;
	/**注文種別*/
	@CsvBindByPosition(position=17)
	private String order_type;
	/**複数送付先フラグ*/
	@CsvBindByPosition(position=18)
	private String multi_destination_flag;
	/**送付先一致フラグ*/
	@CsvBindByPosition(position=19)
	private String destination_match_flag;
	/**離島フラグ*/
	@CsvBindByPosition(position=20)
	private String island_flag;
	/**楽天確認中フラグ*/
	@CsvBindByPosition(position=21)
	private String rverify_flag;
	/**警告表示タイプ*/
	@CsvBindByPosition(position=22)
	private String warning_type;
	/**楽天会員フラグ*/
	@CsvBindByPosition(position=23)
	private String rmember_flag;
	/**購入履歴修正有無フラグ*/
	@CsvBindByPosition(position=24)
	private String purchase_hist_mod_flag;
	/**商品合計金額*/
	@CsvBindByPosition(position=25)
	private String total_goods_amt;
	/**消費税合計*/
	@CsvBindByPosition(position=26)
	private String total_consume_tax;
	/**送料合計*/
	@CsvBindByPosition(position=27)
	private String total_shipping;
	/**代引料合計*/
	@CsvBindByPosition(position=28)
	private String gross_deduction;
	/**請求金額*/
	@CsvBindByPosition(position=29)
	private String invoice_amt;
	/**合計金額*/
	@CsvBindByPosition(position=30)
	private String total_amt;
	/**ポイント利用額*/
	@CsvBindByPosition(position=31)
	private String point_usage;
	/**クーポン利用総額*/
	@CsvBindByPosition(position=32)
	private String total_coupon_usage;
	/**店舗発行クーポン利用額*/
	@CsvBindByPosition(position=33)
	private String store_coupon_usage;
	/**楽天発行クーポン利用額*/
	@CsvBindByPosition(position=34)
	private String rakuten_coupon_usage;
	/**注文者郵便番号1*/
	@CsvBindByPosition(position=35)
	private String order_post_no1;
	/**注文者郵便番号2*/
	@CsvBindByPosition(position=36)
	private String order_post_no2;
	/**注文者住所都道府県*/
	@CsvBindByPosition(position=37)
	private String order_add1;
	/**注文者住所郡市区*/
	@CsvBindByPosition(position=38)
	private String order_add2;
	/**注文者住所それ以降の住所*/
	@CsvBindByPosition(position=39)
	private String order_add3;
	/**注文者姓*/
	@CsvBindByPosition(position=40)
	private String order_surname;
	/**注文者名*/
	@CsvBindByPosition(position=41)
	private String order_name;
	/**注文者姓カナ*/
	@CsvBindByPosition(position=42)
	private String order_surname_kana;
	/**注文者名カナ*/
	@CsvBindByPosition(position=43)
	private String order_name_kana;
	/**注文者電話番号1*/
	@CsvBindByPosition(position=44)
	private String order_tel1;
	/**注文者電話番号2*/
	@CsvBindByPosition(position=45)
	private String order_tel2;
	/**注文者電話番号3*/
	@CsvBindByPosition(position=46)
	private String order_tel3;
	/**注文者メールアドレス*/
	@CsvBindByPosition(position=47)
	private String order_email;
	/**注文者性別*/
	@CsvBindByPosition(position=48)
	private String order_sex;
	/**申込番号*/
	@CsvBindByPosition(position=49)
	private String request_no;
	/**申込お届け回数*/
	@CsvBindByPosition(position=50)
	private String request_received_no;
	/**送付先ID*/
	@CsvBindByPosition(position=51)
	private String ship_id;
	/**送付先送料*/
	@CsvBindByPosition(position=52)
	private String ship_charge;
	/**送付先代引料*/
	@CsvBindByPosition(position=53)
	private String ship_substitute_fee;
	/**送付先消費税合計*/
	@CsvBindByPosition(position=54)
	private String ship_total_consume_tax;
	/**送付先商品合計金額*/
	@CsvBindByPosition(position=55)
	private String ship_total_goods_amt;
	/**送付先合計金額*/
	@CsvBindByPosition(position=56)
	private String ship_total_amt;
	/**のし*/
	@CsvBindByPosition(position=57)
	private String indicates;
	/**送付先郵便番号1*/
	@CsvBindByPosition(position=58)
	private String delivery_post_no1;
	/**送付先郵便番号2*/
	@CsvBindByPosition(position=59)
	private String delivery_post_no2;
	/**送付先住所都道府県*/
	@CsvBindByPosition(position=60)
	private String delivery_add1;
	/**送付先住所郡市区*/
	@CsvBindByPosition(position=61)
	private String delivery_add2;
	/**送付先住所それ以降の住所*/
	@CsvBindByPosition(position=62)
	private String delivery_add3;
	/**送付先姓*/
	@CsvBindByPosition(position=63)
	private String delivery_surname;
	/**送付先名*/
	@CsvBindByPosition(position=64)
	private String delivery_name;
	/**送付先姓カナ*/
	@CsvBindByPosition(position=65)
	private String delivery_surname_kana;
	/**送付先名カナ*/
	@CsvBindByPosition(position=66)
	private String delivery_name_kana;
	/**送付先電話番号1*/
	@CsvBindByPosition(position=67)
	private String delivery_tel1;
	/**送付先電話番号2*/
	@CsvBindByPosition(position=68)
	private String delivery_tel2;
	/**送付先電話番号3*/
	@CsvBindByPosition(position=69)
	private String delivery_tel3;
	/**商品明細ID*/
	@CsvBindByPosition(position=70)
	private String product_detail_id;
	/**商品ID*/
	@CsvBindByPosition(position=71)
	private String product_id;
	/**商品名*/
	@CsvBindByPosition(position=72)
	private String product_name;
	/**商品番号*/
	@CsvBindByPosition(position=73)
	private String product_no;
	/**商品管理番号*/
	@CsvBindByPosition(position=74)
	private String product_manage_no;
	/**単価*/
	@CsvBindByPosition(position=75)
	private String unit_price;
	/**個数*/
	@CsvBindByPosition(position=76)
	private String unit_no;
	/**送料込別*/
	@CsvBindByPosition(position=77)
	private String delivery_cost_include;
	/**税込別*/
	@CsvBindByPosition(position=78)
	private String tax_exclude;
	/**代引手数料込別*/
	@CsvBindByPosition(position=79)
	private String substitute_fee_include;
	/**項目・選択肢*/
	@CsvBindByPosition(position=80)
	private String product_option;
	/**ポイント倍率*/
	@CsvBindByPosition(position=81)
	private String point_multiple;
	/**納期情報*/
	@CsvBindByPosition(position=82)
	private String delivery_info;
	/**在庫タイプ*/
	@CsvBindByPosition(position=83)
	private String inventory_type;
	/**ラッピングタイトル1*/
	@CsvBindByPosition(position=84)
	private String wrap_title1;
	/**ラッピング名1*/
	@CsvBindByPosition(position=85)
	private String wrap_name1;
	/**ラッピング料金1*/
	@CsvBindByPosition(position=86)
	private String wrap_amt1;
	/**ラッピング税込別1*/
	@CsvBindByPosition(position=87)
	private String wrap_tax_include1;
	/**ラッピング種類1*/
	@CsvBindByPosition(position=88)
	private String wrap_type1;
	/**ラッピングタイトル2*/
	@CsvBindByPosition(position=89)
	private String wrap_title2;
	/**ラッピング名2*/
	@CsvBindByPosition(position=90)
	private String wrap_name2;
	/**ラッピング料金2*/
	@CsvBindByPosition(position=91)
	private String wrap_amt2;
	/**ラッピング税込別2*/
	@CsvBindByPosition(position=92)
	private String wrap_tax_include2;
	/**ラッピング種類2*/
	@CsvBindByPosition(position=93)
	private String wrap_type2;
	/**お届け時間帯*/
	@CsvBindByPosition(position=94)
	private String delivery_time;
	/**お届け日指定*/
	@CsvBindByPosition(position=95)
	private String delivery_date_sel;
	/**担当者*/
	@CsvBindByPosition(position=96)
	private String manager;
	/**ひとことメモ*/
	@CsvBindByPosition(position=97)
	private String quick_note;
	/**メール差込文 (お客様へのメッセージ)*/
	@CsvBindByPosition(position=98)
	private String msg_to_customer;
	/**ギフト配送希望*/
	@CsvBindByPosition(position=99)
	private String gift_request;
	/**コメント*/
	@CsvBindByPosition(position=100)
	private String comment;
	/**利用端末*/
	@CsvBindByPosition(position=101)
	private String util_terminal;
	/**メールキャリアコード*/
	@CsvBindByPosition(position=102)
	private String mail_carrier_code;
	/**あす楽希望フラグ*/
	@CsvBindByPosition(position=103)
	private String tomorrow_hope;
	/**医薬品受注フラグ*/
	@CsvBindByPosition(position=104)
	private String drug_order_flag;
	/**楽天スーパーDEAL商品受注フラグ*/
	@CsvBindByPosition(position=105)
	private String rakuten_super_deal;
	/**メンバーシッププログラム受注タイプ*/
	@CsvBindByPosition(position=106)
	private String membership_program;
	
	/**決済手数料*/
	@CsvBindByPosition(position=107)
	private String settlment_fee;
	
	/**注文者負担金合計*/
	@CsvBindByPosition(position=108)
	private String total_orderer_contribution;
	
	/**店舗負担金合計*/
	@CsvBindByPosition(position=109)
	private String total_store_contribution;
	
	/**外税合計*/
	@CsvBindByPosition(position=110)
	private String total_foreign_tax;
	
	/**決済手数料税率*/
	@CsvBindByPosition(position=111)
	private String settlment_fee_tax_rate;
	
	/**ラッピング税率1*/
	@CsvBindByPosition(position=112)
	private String wrapping_tax_rate1;
	
	/**ラッピング税額1*/
	@CsvBindByPosition(position=113)
	private String wrapping_tax_amt1;
	
	/**ラッピング税率2*/
	@CsvBindByPosition(position=114)
	private String wrapping_tax_rate2;
	
	/**ラッピング税額2*/
	@CsvBindByPosition(position=115)
	private String wrapping_tax_amt2;
	
	/**送付先外税合計*/
	@CsvBindByPosition(position=116)
	private String total_non_destination_tax;
	
	/**送付先送料税率*/
	@CsvBindByPosition(position=117)
	private String shipping_tax_rate;
	
	/**送付先代引料税率*/
	@CsvBindByPosition(position=118)
	private String destination_cash_on_delivery_tax_rate;
	
	/**商品税率*/
	@CsvBindByPosition(position=119)
	private String commodity_tax_rate;
	
	/**商品毎税込価格*/
	@CsvBindByPosition(position=120)
	private String price_incld_tax_per_prd;
	
	/**10%税率*/
	@CsvBindByPosition(position=121)
	private String tax_rate10;
	
	/**10%請求金額*/
	@CsvBindByPosition(position=122)
	private String invoice_amt10;
	
	/**10%請求額に対する税額*/
	@CsvBindByPosition(position=123)
	private String tax_amt10;
	
	/**10%合計金額*/
	@CsvBindByPosition(position=124)
	private String total_amt10;
	
	/**10%決済手数料*/
	@CsvBindByPosition(position=125)
	private String settlement_fee10;
	
	/**10%クーポン割引額*/
	@CsvBindByPosition(position=126)
	private String cpn_discount10;
	
	/**10%利用ポイント数*/
	@CsvBindByPosition(position=127)
	private String point_use10;
	
	/**8%税率*/
	@CsvBindByPosition(position=128)
	private String tax_rate8;
	
	/**8%請求金額*/
	@CsvBindByPosition(position=129)
	private String invoice_amt8;
	
	/**8%請求額に対する税額*/
	@CsvBindByPosition(position=130)
	private String tax_amt8;
	
	/**8%合計金額*/
	@CsvBindByPosition(position=131)
	private String total_amt8;
	
	/**8%決済手数料*/
	@CsvBindByPosition(position=132)
	private String settlement_fee8;
	
	/**8%クーポン割引額*/
	@CsvBindByPosition(position=133)
	private String cpn_discount8;
	
	/**8%利用ポイント数*/
	@CsvBindByPosition(position=134)
	private String point_use8;
	
	/**0%税率*/
	@CsvBindByPosition(position=135)
	private String tax_rate0;
	
	/**0%請求金額*/
	@CsvBindByPosition(position=136)
	private String invoice_amt0;
	
	/**0%請求額に対する税額*/
	@CsvBindByPosition(position=137)
	private String tax_amt0;
	
	/**0%合計金額*/
	@CsvBindByPosition(position=138)
	private String total_amt0;
	
	/**0%決済手数料*/
	@CsvBindByPosition(position=139)
	private String settlement_fee0;
	
	/**0%クーポン割引額*/
	@CsvBindByPosition(position=140)
	private String cpn_discount0;
	
	/**0%利用ポイント数*/
	@CsvBindByPosition(position=141)
	private String point_use0;
	
	/**単品配送フラグ*/
	@CsvBindByPosition(position=142)
	private String single_item_delivery_flag;
	
	/**購入時配送会社*/
	@CsvBindByPosition(position=143)
	private String delivery_comp_purchase;
	
	/**領収書発行回数*/
	@CsvBindByPosition(position=144)
	private String receipts_no;
	
	/**領収書初回発行日時*/
	@CsvBindByPosition(position=145)
	private String receipt_first_issuance_date;
	
	/**領収書最終発行日時*/
	@CsvBindByPosition(position=146)
	private String receipt_last_issuance_date;
	
	/**支払い期限日*/
	@CsvBindByPosition(position=147)
	private String due_date;
	
	/**支払い方法変更期限日*/
	@CsvBindByPosition(position=148)
	private String payment_method_due_date;
	
	/**返金手続き期限日*/
	@CsvBindByPosition(position=149)
	private String refund_due_date;
	
	/**店舗発行クーポンコード*/
	@CsvBindByPosition(position=150)
	private String store_issuance_cpn_cd;
	
	/**店舗発行クーポン名*/
	@CsvBindByPosition(position=151)
	private String store_issuance_cpn_nm;
	
	/**楽天発行クーポンコード*/
	@CsvBindByPosition(position=152)
	private String rakuten_issuance_cpn_cd;
	
	/**楽天発行クーポン名*/
	@CsvBindByPosition(position=153)
	private String rakuten_issuance_cpn_nm;
	
	/**警告表示タイプ詳細*/
	@CsvBindByPosition(position=154)
	private String alert_display_type_dt;
	
	/**SKU管理番号*/
	@CsvBindByPosition(position=155)
	private String sku_management_no;
	
	/**システム連携用SKU番号*/
	@CsvBindByPosition(position=156)
	private String sku_sys_integration_no;
	
	/**SKU情報*/
	@CsvBindByPosition(position=157)
	private String sku_info;

	

	
	
	/**配送会社*/
	private String delivery_company;
	/**お荷物伝票番号*/
	private String baggage_claim_no;
	/**楽天・アマゾン区分ID*/
	private String trans_target_id;
	/**置換結果テキスト*/
	private String result_text;
	/**エラーテキスト*/
	private String err_text;
	
	/**検索用*/
	private String start_date;
	/**検索用*/
	private String search_type;
	private ArrayList<String> seq_id_list;
	
	// 2019-10-09: 別紙처리
	/**別紙番号*/
	private String attach_no;
	
	private String real_seq_id;
	
	// 2021.06.29 라쿠텐 시간검색 추가
	private String fromDt;
	private String toDt;
	
	public static void sortListVO(List<?> list, String getterMethodText, String sortInfo) {
		 
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object firstObject, Object secondObject) {
                int rtn = 0;
                int compareIndex = 0; // 비교 인덱스 (작은 문자열 수)
                String firstData = "";
                String secondData = "";
                int firstValue = 0;
                int secondValue = 0;
 
                // 비교할 대상
                try {
                    Method firstDeclaredMethod = firstObject.getClass().getDeclaredMethod(getterMethodText);
                    firstData = (String) firstDeclaredMethod.invoke(firstObject, new Object[] {});
                    Method secondDeclaredMethod = secondObject.getClass().getDeclaredMethod(getterMethodText);
                    secondData = (String) secondDeclaredMethod.invoke(secondObject, new Object[] {});
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
 
                if (firstData == null || firstData == "" || secondData == null || secondData == "") { return rtn; }
 
                // 기본 정렬값 설정
                if ("DESC".equals(sortInfo)) {
                    if (firstData.length() > secondData.length()) {
                        rtn = -1;
                        compareIndex = secondData.length();
                    } else if (firstData.length() < secondData.length()) {
                        rtn = 1;
                        compareIndex = firstData.length();
                    } else {
                        compareIndex = firstData.length();
                    }
                } else {
                    if (firstData.length() < secondData.length()) {
                        rtn = -1;
                        compareIndex = firstData.length();
                    } else if (firstData.length() > secondData.length()) {
                        rtn = 1;
                        compareIndex = secondData.length();
                    } else {
                        compareIndex = firstData.length();
                    }
                }
 
                for (int i = 0; i < compareIndex; i++) {
                    firstValue = Integer.valueOf(firstData.charAt(i));
                    secondValue = Integer.valueOf(secondData.charAt(i));
 
                    if ("DESC".equals(sortInfo)) {
                        if (firstValue > secondValue) {
                            rtn = -1;
                            break;
                        } else if (firstValue < secondValue) {
                            rtn = 1;
                            break;
                        }
                    } else {
                        if (firstValue < secondValue) {
                            rtn = -1;
                            break;
                        } else if (firstValue > secondValue) {
                            rtn = 1;
                            break;
                        }
                    }
                }
 
                return rtn;
            }
        });
    }

}
