package jp.prj.araku.batch.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class ItemOutputVO extends ArakuVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	
	private String start_date;
	private String end_date;
	
	/**コントロールカラム*/
	@CsvBindByPosition(position=0)
	private String ctrl_col;
	/**商品管理番号（商品URL）*/
	@CsvBindByPosition(position=1)
	private String item_url;
	/**商品番号*/
	@CsvBindByPosition(position=2)
	private String item_num;
	/**全商品ディレクトリID*/
	@CsvBindByPosition(position=3)
	private String directory_id;
	/**タグID*/
	@CsvBindByPosition(position=4)
	private String tag_id;
	/**PC用キャッチコピー*/
	@CsvBindByPosition(position=5)
	private String pc_catch_cp;
	/**モバイル用キャッチコピー*/
	@CsvBindByPosition(position=6)
	private String mobile_catch_cp;
	/**商品名*/
	@CsvBindByPosition(position=7)
	private String item_name;
	/**販売価格*/
	@CsvBindByPosition(position=8)
	private String sell_price;
	/**表示価格*/
	@CsvBindByPosition(position=9)
	private String display_price;
	/**消費税*/
	@CsvBindByPosition(position=10)
	private String consume_tax;
	/**送料*/
	@CsvBindByPosition(position=11)
	private String shipping_cost;
	/**個別送料*/
	@CsvBindByPosition(position=12)
	private String individual_shipping_cost;
	/**送料区分1*/
	@CsvBindByPosition(position=13)
	private String shipping_category1;
	/**送料区分2*/
	@CsvBindByPosition(position=14)
	private String shipping_category2;
	/**代引料*/
	@CsvBindByPosition(position=15)
	private String commission_fee;
	/**倉庫指定*/
	@CsvBindByPosition(position=16)
	private String select_warehouse;
	/**商品情報レイアウト*/
	@CsvBindByPosition(position=17)
	private String item_info_layout;
	/**注文ボタン*/
	@CsvBindByPosition(position=18)
	private String order_btn;
	/**資料請求ボタン*/
	@CsvBindByPosition(position=19)
	private String material_req_btn;
	/**商品問い合わせボタン*/
	@CsvBindByPosition(position=20)
	private String item_inquiry_btn;
	/**再入荷お知らせボタン*/
	@CsvBindByPosition(position=21)
	private String notice_btn;
	/**のし対応*/
	@CsvBindByPosition(position=22)
	private String nosi_resp;
	/**PC用商品説明文*/
	@CsvBindByPosition(position=23)
	private String desc_pc_item;
	/**スマートフォン用商品説明文*/
	@CsvBindByPosition(position=24)
	private String desc_phone_item;
	/**PC用販売説明文*/
	@CsvBindByPosition(position=25)
	private String desc_pc_sales;
	/**商品画像URL*/
	@CsvBindByPosition(position=26)
	private String item_image_url;
	/**商品画像名（ALT）*/
	@CsvBindByPosition(position=27)
	private String item_image_name;
	/**動画*/
	@CsvBindByPosition(position=28)
	private String video_name;
	/**販売期間指定*/
	@CsvBindByPosition(position=29)
	private String select_sales_period;
	/**注文受付数*/
	@CsvBindByPosition(position=30)
	private String order_num;
	/**在庫タイプ*/
	@CsvBindByPosition(position=31)
	private String inven_type;
	/**在庫数*/
	@CsvBindByPosition(position=32)
	private String inven_num;
	/**在庫数表示*/
	@CsvBindByPosition(position=33)
	private String inven_num_view;
	/**項目選択肢別在庫用横軸項目名*/
	@CsvBindByPosition(position=34)
	private String item_horizon_name;
	/**項目選択肢別在庫用縦軸項目名*/
	@CsvBindByPosition(position=35)
	private String item_vertical_name;
	/**項目選択肢別在庫用残り表示閾値*/
	@CsvBindByPosition(position=36)
	private String item_inven_view;
	/**サーチ非表示*/
	@CsvBindByPosition(position=37)
	private String search_type;
	/**闇市パスワード*/
	@CsvBindByPosition(position=38)
	private String pw_black_market;
	/**カタログID*/
	@CsvBindByPosition(position=39)
	private String catalog_id;
	/**在庫戻しフラグ*/
	@CsvBindByPosition(position=40)
	private String back_stock_flag;
	/**在庫切れ時の注文受付*/
	@CsvBindByPosition(position=41)
	private String out_stock_order;
	/**在庫あり時納期管理番号*/
	@CsvBindByPosition(position=42)
	private String in_stock_delivery_num;
	/**在庫切れ時納期管理番号*/
	@CsvBindByPosition(position=43)
	private String out_stock_delivery_num;
	/**予約商品発売日*/
	@CsvBindByPosition(position=44)
	private String reserved_item_date;
	/**ポイント変倍率*/
	@CsvBindByPosition(position=45)
	private String percent_point_var;
	/**ポイント変倍率適用期間*/
	@CsvBindByPosition(position=46)
	private String period_point_apply;
	/**ヘッダー・フッター・レフトナビ*/
	@CsvBindByPosition(position=47)
	private String header_footer_leftnavi;
	/**表示項目の並び順*/
	@CsvBindByPosition(position=48)
	private String order_display_item;
	/**共通説明文（小）*/
	@CsvBindByPosition(position=49)
	private String desc_common_small;
	/**目玉商品*/
	@CsvBindByPosition(position=50)
	private String item_eye_catch;
	/**共通説明文（大）*/
	@CsvBindByPosition(position=51)
	private String desc_common_big;
	/**レビュー本文表示*/
	@CsvBindByPosition(position=52)
	private String display_review;
	/**あす楽配送管理番号*/
	@CsvBindByPosition(position=53)
	private String manage_num_tomorrow_delivery;
	/**海外配送管理番号*/
	@CsvBindByPosition(position=54)
	private String manage_num_oversea_delivery;
	/**サイズ表リンク*/
	@CsvBindByPosition(position=55)
	private String link_size_tbl;
	/**二重価格文言管理番号*/
	@CsvBindByPosition(position=56)
	private String manage_num_dbl_price;
	/**カタログIDなしの理由*/
	@CsvBindByPosition(position=57)
	private String reason_no_catalog_id;
	/**配送方法セット管理番号*/
	@CsvBindByPosition(position=58)
	private String manage_num_shipping_method;
	/**白背景画像URL*/
	@CsvBindByPosition(position=59)
	private String white_bg_image_url;
	/**メーカー提供情報表示*/
	@CsvBindByPosition(position=60)
	private String display_maker_info;
	
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getRegister_date() {
		return register_date;
	}
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	public String getCtrl_col() {
		return ctrl_col;
	}
	public void setCtrl_col(String ctrl_col) {
		this.ctrl_col = ctrl_col;
	}
	public String getItem_url() {
		return item_url;
	}
	public void setItem_url(String item_url) {
		this.item_url = item_url;
	}
	public String getItem_num() {
		return item_num;
	}
	public void setItem_num(String item_num) {
		this.item_num = item_num;
	}
	public String getDirectory_id() {
		return directory_id;
	}
	public void setDirectory_id(String directory_id) {
		this.directory_id = directory_id;
	}
	public String getTag_id() {
		return tag_id;
	}
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
	public String getPc_catch_cp() {
		return pc_catch_cp;
	}
	public void setPc_catch_cp(String pc_catch_cp) {
		this.pc_catch_cp = pc_catch_cp;
	}
	public String getMobile_catch_cp() {
		return mobile_catch_cp;
	}
	public void setMobile_catch_cp(String mobile_catch_cp) {
		this.mobile_catch_cp = mobile_catch_cp;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}
	public String getDisplay_price() {
		return display_price;
	}
	public void setDisplay_price(String display_price) {
		this.display_price = display_price;
	}
	public String getConsume_tax() {
		return consume_tax;
	}
	public void setConsume_tax(String consume_tax) {
		this.consume_tax = consume_tax;
	}
	public String getShipping_cost() {
		return shipping_cost;
	}
	public void setShipping_cost(String shipping_cost) {
		this.shipping_cost = shipping_cost;
	}
	public String getIndividual_shipping_cost() {
		return individual_shipping_cost;
	}
	public void setIndividual_shipping_cost(String individual_shipping_cost) {
		this.individual_shipping_cost = individual_shipping_cost;
	}
	public String getShipping_category1() {
		return shipping_category1;
	}
	public void setShipping_category1(String shipping_category1) {
		this.shipping_category1 = shipping_category1;
	}
	public String getShipping_category2() {
		return shipping_category2;
	}
	public void setShipping_category2(String shipping_category2) {
		this.shipping_category2 = shipping_category2;
	}
	public String getCommission_fee() {
		return commission_fee;
	}
	public void setCommission_fee(String commission_fee) {
		this.commission_fee = commission_fee;
	}
	public String getSelect_warehouse() {
		return select_warehouse;
	}
	public void setSelect_warehouse(String select_warehouse) {
		this.select_warehouse = select_warehouse;
	}
	public String getItem_info_layout() {
		return item_info_layout;
	}
	public void setItem_info_layout(String item_info_layout) {
		this.item_info_layout = item_info_layout;
	}
	public String getOrder_btn() {
		return order_btn;
	}
	public void setOrder_btn(String order_btn) {
		this.order_btn = order_btn;
	}
	public String getMaterial_req_btn() {
		return material_req_btn;
	}
	public void setMaterial_req_btn(String material_req_btn) {
		this.material_req_btn = material_req_btn;
	}
	public String getItem_inquiry_btn() {
		return item_inquiry_btn;
	}
	public void setItem_inquiry_btn(String item_inquiry_btn) {
		this.item_inquiry_btn = item_inquiry_btn;
	}
	public String getNotice_btn() {
		return notice_btn;
	}
	public void setNotice_btn(String notice_btn) {
		this.notice_btn = notice_btn;
	}
	public String getNosi_resp() {
		return nosi_resp;
	}
	public void setNosi_resp(String nosi_resp) {
		this.nosi_resp = nosi_resp;
	}
	public String getDesc_pc_item() {
		return desc_pc_item;
	}
	public void setDesc_pc_item(String desc_pc_item) {
		this.desc_pc_item = desc_pc_item;
	}
	public String getDesc_phone_item() {
		return desc_phone_item;
	}
	public void setDesc_phone_item(String desc_phone_item) {
		this.desc_phone_item = desc_phone_item;
	}
	public String getDesc_pc_sales() {
		return desc_pc_sales;
	}
	public void setDesc_pc_sales(String desc_pc_sales) {
		this.desc_pc_sales = desc_pc_sales;
	}
	public String getItem_image_url() {
		return item_image_url;
	}
	public void setItem_image_url(String item_image_url) {
		this.item_image_url = item_image_url;
	}
	public String getItem_image_name() {
		return item_image_name;
	}
	public void setItem_image_name(String item_image_name) {
		this.item_image_name = item_image_name;
	}
	public String getVideo_name() {
		return video_name;
	}
	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}
	public String getSelect_sales_period() {
		return select_sales_period;
	}
	public void setSelect_sales_period(String select_sales_period) {
		this.select_sales_period = select_sales_period;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getInven_type() {
		return inven_type;
	}
	public void setInven_type(String inven_type) {
		this.inven_type = inven_type;
	}
	public String getInven_num() {
		return inven_num;
	}
	public void setInven_num(String inven_num) {
		this.inven_num = inven_num;
	}
	public String getInven_num_view() {
		return inven_num_view;
	}
	public void setInven_num_view(String inven_num_view) {
		this.inven_num_view = inven_num_view;
	}
	public String getItem_horizon_name() {
		return item_horizon_name;
	}
	public void setItem_horizon_name(String item_horizon_name) {
		this.item_horizon_name = item_horizon_name;
	}
	public String getItem_vertical_name() {
		return item_vertical_name;
	}
	public void setItem_vertical_name(String item_vertical_name) {
		this.item_vertical_name = item_vertical_name;
	}
	public String getItem_inven_view() {
		return item_inven_view;
	}
	public void setItem_inven_view(String item_inven_view) {
		this.item_inven_view = item_inven_view;
	}
	public String getSearch_type() {
		return search_type;
	}
	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}
	public String getPw_black_market() {
		return pw_black_market;
	}
	public void setPw_black_market(String pw_black_market) {
		this.pw_black_market = pw_black_market;
	}
	public String getCatalog_id() {
		return catalog_id;
	}
	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}
	public String getBack_stock_flag() {
		return back_stock_flag;
	}
	public void setBack_stock_flag(String back_stock_flag) {
		this.back_stock_flag = back_stock_flag;
	}
	public String getOut_stock_order() {
		return out_stock_order;
	}
	public void setOut_stock_order(String out_stock_order) {
		this.out_stock_order = out_stock_order;
	}
	public String getIn_stock_delivery_num() {
		return in_stock_delivery_num;
	}
	public void setIn_stock_delivery_num(String in_stock_delivery_num) {
		this.in_stock_delivery_num = in_stock_delivery_num;
	}
	public String getOut_stock_delivery_num() {
		return out_stock_delivery_num;
	}
	public void setOut_stock_delivery_num(String out_stock_delivery_num) {
		this.out_stock_delivery_num = out_stock_delivery_num;
	}
	public String getReserved_item_date() {
		return reserved_item_date;
	}
	public void setReserved_item_date(String reserved_item_date) {
		this.reserved_item_date = reserved_item_date;
	}
	public String getPercent_point_var() {
		return percent_point_var;
	}
	public void setPercent_point_var(String percent_point_var) {
		this.percent_point_var = percent_point_var;
	}
	public String getPeriod_point_apply() {
		return period_point_apply;
	}
	public void setPeriod_point_apply(String period_point_apply) {
		this.period_point_apply = period_point_apply;
	}
	public String getHeader_footer_leftnavi() {
		return header_footer_leftnavi;
	}
	public void setHeader_footer_leftnavi(String header_footer_leftnavi) {
		this.header_footer_leftnavi = header_footer_leftnavi;
	}
	public String getOrder_display_item() {
		return order_display_item;
	}
	public void setOrder_display_item(String order_display_item) {
		this.order_display_item = order_display_item;
	}
	public String getDesc_common_small() {
		return desc_common_small;
	}
	public void setDesc_common_small(String desc_common_small) {
		this.desc_common_small = desc_common_small;
	}
	public String getItem_eye_catch() {
		return item_eye_catch;
	}
	public void setItem_eye_catch(String item_eye_catch) {
		this.item_eye_catch = item_eye_catch;
	}
	public String getDesc_common_big() {
		return desc_common_big;
	}
	public void setDesc_common_big(String desc_common_big) {
		this.desc_common_big = desc_common_big;
	}
	public String getDisplay_review() {
		return display_review;
	}
	public void setDisplay_review(String display_review) {
		this.display_review = display_review;
	}
	public String getManage_num_tomorrow_delivery() {
		return manage_num_tomorrow_delivery;
	}
	public void setManage_num_tomorrow_delivery(String manage_num_tomorrow_delivery) {
		this.manage_num_tomorrow_delivery = manage_num_tomorrow_delivery;
	}
	public String getManage_num_oversea_delivery() {
		return manage_num_oversea_delivery;
	}
	public void setManage_num_oversea_delivery(String manage_num_oversea_delivery) {
		this.manage_num_oversea_delivery = manage_num_oversea_delivery;
	}
	public String getLink_size_tbl() {
		return link_size_tbl;
	}
	public void setLink_size_tbl(String link_size_tbl) {
		this.link_size_tbl = link_size_tbl;
	}
	public String getManage_num_dbl_price() {
		return manage_num_dbl_price;
	}
	public void setManage_num_dbl_price(String manage_num_dbl_price) {
		this.manage_num_dbl_price = manage_num_dbl_price;
	}
	public String getReason_no_catalog_id() {
		return reason_no_catalog_id;
	}
	public void setReason_no_catalog_id(String reason_no_catalog_id) {
		this.reason_no_catalog_id = reason_no_catalog_id;
	}
	public String getManage_num_shipping_method() {
		return manage_num_shipping_method;
	}
	public void setManage_num_shipping_method(String manage_num_shipping_method) {
		this.manage_num_shipping_method = manage_num_shipping_method;
	}
	public String getWhite_bg_image_url() {
		return white_bg_image_url;
	}
	public void setWhite_bg_image_url(String white_bg_image_url) {
		this.white_bg_image_url = white_bg_image_url;
	}
	public String getDisplay_maker_info() {
		return display_maker_info;
	}
	public void setDisplay_maker_info(String display_maker_info) {
		this.display_maker_info = display_maker_info;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemOutputVO [");
		if (seq_id != null) {
			builder.append("seq_id=");
			builder.append(seq_id);
			builder.append(", ");
		}
		if (register_date != null) {
			builder.append("register_date=");
			builder.append(register_date);
			builder.append(", ");
		}
		if (start_date != null) {
			builder.append("start_date=");
			builder.append(start_date);
			builder.append(", ");
		}
		if (end_date != null) {
			builder.append("end_date=");
			builder.append(end_date);
			builder.append(", ");
		}
		if (ctrl_col != null) {
			builder.append("ctrl_col=");
			builder.append(ctrl_col);
			builder.append(", ");
		}
		if (item_url != null) {
			builder.append("item_url=");
			builder.append(item_url);
			builder.append(", ");
		}
		if (item_num != null) {
			builder.append("item_num=");
			builder.append(item_num);
			builder.append(", ");
		}
		if (directory_id != null) {
			builder.append("directory_id=");
			builder.append(directory_id);
			builder.append(", ");
		}
		if (tag_id != null) {
			builder.append("tag_id=");
			builder.append(tag_id);
			builder.append(", ");
		}
		if (pc_catch_cp != null) {
			builder.append("pc_catch_cp=");
			builder.append(pc_catch_cp);
			builder.append(", ");
		}
		if (mobile_catch_cp != null) {
			builder.append("mobile_catch_cp=");
			builder.append(mobile_catch_cp);
			builder.append(", ");
		}
		if (item_name != null) {
			builder.append("item_name=");
			builder.append(item_name);
			builder.append(", ");
		}
		if (sell_price != null) {
			builder.append("sell_price=");
			builder.append(sell_price);
			builder.append(", ");
		}
		if (display_price != null) {
			builder.append("display_price=");
			builder.append(display_price);
			builder.append(", ");
		}
		if (consume_tax != null) {
			builder.append("consume_tax=");
			builder.append(consume_tax);
			builder.append(", ");
		}
		if (shipping_cost != null) {
			builder.append("shipping_cost=");
			builder.append(shipping_cost);
			builder.append(", ");
		}
		if (individual_shipping_cost != null) {
			builder.append("individual_shipping_cost=");
			builder.append(individual_shipping_cost);
			builder.append(", ");
		}
		if (shipping_category1 != null) {
			builder.append("shipping_category1=");
			builder.append(shipping_category1);
			builder.append(", ");
		}
		if (shipping_category2 != null) {
			builder.append("shipping_category2=");
			builder.append(shipping_category2);
			builder.append(", ");
		}
		if (commission_fee != null) {
			builder.append("commission_fee=");
			builder.append(commission_fee);
			builder.append(", ");
		}
		if (select_warehouse != null) {
			builder.append("select_warehouse=");
			builder.append(select_warehouse);
			builder.append(", ");
		}
		if (item_info_layout != null) {
			builder.append("item_info_layout=");
			builder.append(item_info_layout);
			builder.append(", ");
		}
		if (order_btn != null) {
			builder.append("order_btn=");
			builder.append(order_btn);
			builder.append(", ");
		}
		if (material_req_btn != null) {
			builder.append("material_req_btn=");
			builder.append(material_req_btn);
			builder.append(", ");
		}
		if (item_inquiry_btn != null) {
			builder.append("item_inquiry_btn=");
			builder.append(item_inquiry_btn);
			builder.append(", ");
		}
		if (notice_btn != null) {
			builder.append("notice_btn=");
			builder.append(notice_btn);
			builder.append(", ");
		}
		if (nosi_resp != null) {
			builder.append("nosi_resp=");
			builder.append(nosi_resp);
			builder.append(", ");
		}
		if (desc_pc_item != null) {
			builder.append("desc_pc_item=");
			builder.append(desc_pc_item);
			builder.append(", ");
		}
		if (desc_phone_item != null) {
			builder.append("desc_phone_item=");
			builder.append(desc_phone_item);
			builder.append(", ");
		}
		if (desc_pc_sales != null) {
			builder.append("desc_pc_sales=");
			builder.append(desc_pc_sales);
			builder.append(", ");
		}
		if (item_image_url != null) {
			builder.append("item_image_url=");
			builder.append(item_image_url);
			builder.append(", ");
		}
		if (item_image_name != null) {
			builder.append("item_image_name=");
			builder.append(item_image_name);
			builder.append(", ");
		}
		if (video_name != null) {
			builder.append("video_name=");
			builder.append(video_name);
			builder.append(", ");
		}
		if (select_sales_period != null) {
			builder.append("select_sales_period=");
			builder.append(select_sales_period);
			builder.append(", ");
		}
		if (order_num != null) {
			builder.append("order_num=");
			builder.append(order_num);
			builder.append(", ");
		}
		if (inven_type != null) {
			builder.append("inven_type=");
			builder.append(inven_type);
			builder.append(", ");
		}
		if (inven_num != null) {
			builder.append("inven_num=");
			builder.append(inven_num);
			builder.append(", ");
		}
		if (inven_num_view != null) {
			builder.append("inven_num_view=");
			builder.append(inven_num_view);
			builder.append(", ");
		}
		if (item_horizon_name != null) {
			builder.append("item_horizon_name=");
			builder.append(item_horizon_name);
			builder.append(", ");
		}
		if (item_vertical_name != null) {
			builder.append("item_vertical_name=");
			builder.append(item_vertical_name);
			builder.append(", ");
		}
		if (item_inven_view != null) {
			builder.append("item_inven_view=");
			builder.append(item_inven_view);
			builder.append(", ");
		}
		if (search_type != null) {
			builder.append("search_type=");
			builder.append(search_type);
			builder.append(", ");
		}
		if (pw_black_market != null) {
			builder.append("pw_black_market=");
			builder.append(pw_black_market);
			builder.append(", ");
		}
		if (catalog_id != null) {
			builder.append("catalog_id=");
			builder.append(catalog_id);
			builder.append(", ");
		}
		if (back_stock_flag != null) {
			builder.append("back_stock_flag=");
			builder.append(back_stock_flag);
			builder.append(", ");
		}
		if (out_stock_order != null) {
			builder.append("out_stock_order=");
			builder.append(out_stock_order);
			builder.append(", ");
		}
		if (in_stock_delivery_num != null) {
			builder.append("in_stock_delivery_num=");
			builder.append(in_stock_delivery_num);
			builder.append(", ");
		}
		if (out_stock_delivery_num != null) {
			builder.append("out_stock_delivery_num=");
			builder.append(out_stock_delivery_num);
			builder.append(", ");
		}
		if (reserved_item_date != null) {
			builder.append("reserved_item_date=");
			builder.append(reserved_item_date);
			builder.append(", ");
		}
		if (percent_point_var != null) {
			builder.append("percent_point_var=");
			builder.append(percent_point_var);
			builder.append(", ");
		}
		if (period_point_apply != null) {
			builder.append("period_point_apply=");
			builder.append(period_point_apply);
			builder.append(", ");
		}
		if (header_footer_leftnavi != null) {
			builder.append("header_footer_leftnavi=");
			builder.append(header_footer_leftnavi);
			builder.append(", ");
		}
		if (order_display_item != null) {
			builder.append("order_display_item=");
			builder.append(order_display_item);
			builder.append(", ");
		}
		if (desc_common_small != null) {
			builder.append("desc_common_small=");
			builder.append(desc_common_small);
			builder.append(", ");
		}
		if (item_eye_catch != null) {
			builder.append("item_eye_catch=");
			builder.append(item_eye_catch);
			builder.append(", ");
		}
		if (desc_common_big != null) {
			builder.append("desc_common_big=");
			builder.append(desc_common_big);
			builder.append(", ");
		}
		if (display_review != null) {
			builder.append("display_review=");
			builder.append(display_review);
			builder.append(", ");
		}
		if (manage_num_tomorrow_delivery != null) {
			builder.append("manage_num_tomorrow_delivery=");
			builder.append(manage_num_tomorrow_delivery);
			builder.append(", ");
		}
		if (manage_num_oversea_delivery != null) {
			builder.append("manage_num_oversea_delivery=");
			builder.append(manage_num_oversea_delivery);
			builder.append(", ");
		}
		if (link_size_tbl != null) {
			builder.append("link_size_tbl=");
			builder.append(link_size_tbl);
			builder.append(", ");
		}
		if (manage_num_dbl_price != null) {
			builder.append("manage_num_dbl_price=");
			builder.append(manage_num_dbl_price);
			builder.append(", ");
		}
		if (reason_no_catalog_id != null) {
			builder.append("reason_no_catalog_id=");
			builder.append(reason_no_catalog_id);
			builder.append(", ");
		}
		if (manage_num_shipping_method != null) {
			builder.append("manage_num_shipping_method=");
			builder.append(manage_num_shipping_method);
			builder.append(", ");
		}
		if (white_bg_image_url != null) {
			builder.append("white_bg_image_url=");
			builder.append(white_bg_image_url);
			builder.append(", ");
		}
		if (display_maker_info != null) {
			builder.append("display_maker_info=");
			builder.append(display_maker_info);
			builder.append(", ");
		}
		if (getSeq_id() != null) {
			builder.append("getSeq_id()=");
			builder.append(getSeq_id());
			builder.append(", ");
		}
		if (getRegister_date() != null) {
			builder.append("getRegister_date()=");
			builder.append(getRegister_date());
			builder.append(", ");
		}
		if (getCtrl_col() != null) {
			builder.append("getCtrl_col()=");
			builder.append(getCtrl_col());
			builder.append(", ");
		}
		if (getItem_url() != null) {
			builder.append("getItem_url()=");
			builder.append(getItem_url());
			builder.append(", ");
		}
		if (getItem_num() != null) {
			builder.append("getItem_num()=");
			builder.append(getItem_num());
			builder.append(", ");
		}
		if (getDirectory_id() != null) {
			builder.append("getDirectory_id()=");
			builder.append(getDirectory_id());
			builder.append(", ");
		}
		if (getTag_id() != null) {
			builder.append("getTag_id()=");
			builder.append(getTag_id());
			builder.append(", ");
		}
		if (getPc_catch_cp() != null) {
			builder.append("getPc_catch_cp()=");
			builder.append(getPc_catch_cp());
			builder.append(", ");
		}
		if (getMobile_catch_cp() != null) {
			builder.append("getMobile_catch_cp()=");
			builder.append(getMobile_catch_cp());
			builder.append(", ");
		}
		if (getItem_name() != null) {
			builder.append("getItem_name()=");
			builder.append(getItem_name());
			builder.append(", ");
		}
		if (getSell_price() != null) {
			builder.append("getSell_price()=");
			builder.append(getSell_price());
			builder.append(", ");
		}
		if (getDisplay_price() != null) {
			builder.append("getDisplay_price()=");
			builder.append(getDisplay_price());
			builder.append(", ");
		}
		if (getConsume_tax() != null) {
			builder.append("getConsume_tax()=");
			builder.append(getConsume_tax());
			builder.append(", ");
		}
		if (getShipping_cost() != null) {
			builder.append("getShipping_cost()=");
			builder.append(getShipping_cost());
			builder.append(", ");
		}
		if (getIndividual_shipping_cost() != null) {
			builder.append("getIndividual_shipping_cost()=");
			builder.append(getIndividual_shipping_cost());
			builder.append(", ");
		}
		if (getShipping_category1() != null) {
			builder.append("getShipping_category1()=");
			builder.append(getShipping_category1());
			builder.append(", ");
		}
		if (getShipping_category2() != null) {
			builder.append("getShipping_category2()=");
			builder.append(getShipping_category2());
			builder.append(", ");
		}
		if (getCommission_fee() != null) {
			builder.append("getCommission_fee()=");
			builder.append(getCommission_fee());
			builder.append(", ");
		}
		if (getSelect_warehouse() != null) {
			builder.append("getSelect_warehouse()=");
			builder.append(getSelect_warehouse());
			builder.append(", ");
		}
		if (getItem_info_layout() != null) {
			builder.append("getItem_info_layout()=");
			builder.append(getItem_info_layout());
			builder.append(", ");
		}
		if (getOrder_btn() != null) {
			builder.append("getOrder_btn()=");
			builder.append(getOrder_btn());
			builder.append(", ");
		}
		if (getMaterial_req_btn() != null) {
			builder.append("getMaterial_req_btn()=");
			builder.append(getMaterial_req_btn());
			builder.append(", ");
		}
		if (getItem_inquiry_btn() != null) {
			builder.append("getItem_inquiry_btn()=");
			builder.append(getItem_inquiry_btn());
			builder.append(", ");
		}
		if (getNotice_btn() != null) {
			builder.append("getNotice_btn()=");
			builder.append(getNotice_btn());
			builder.append(", ");
		}
		if (getNosi_resp() != null) {
			builder.append("getNosi_resp()=");
			builder.append(getNosi_resp());
			builder.append(", ");
		}
		if (getDesc_pc_item() != null) {
			builder.append("getDesc_pc_item()=");
			builder.append(getDesc_pc_item());
			builder.append(", ");
		}
		if (getDesc_phone_item() != null) {
			builder.append("getDesc_phone_item()=");
			builder.append(getDesc_phone_item());
			builder.append(", ");
		}
		if (getDesc_pc_sales() != null) {
			builder.append("getDesc_pc_sales()=");
			builder.append(getDesc_pc_sales());
			builder.append(", ");
		}
		if (getItem_image_url() != null) {
			builder.append("getItem_image_url()=");
			builder.append(getItem_image_url());
			builder.append(", ");
		}
		if (getItem_image_name() != null) {
			builder.append("getItem_image_name()=");
			builder.append(getItem_image_name());
			builder.append(", ");
		}
		if (getVideo_name() != null) {
			builder.append("getVideo_name()=");
			builder.append(getVideo_name());
			builder.append(", ");
		}
		if (getSelect_sales_period() != null) {
			builder.append("getSelect_sales_period()=");
			builder.append(getSelect_sales_period());
			builder.append(", ");
		}
		if (getOrder_num() != null) {
			builder.append("getOrder_num()=");
			builder.append(getOrder_num());
			builder.append(", ");
		}
		if (getInven_type() != null) {
			builder.append("getInven_type()=");
			builder.append(getInven_type());
			builder.append(", ");
		}
		if (getInven_num() != null) {
			builder.append("getInven_num()=");
			builder.append(getInven_num());
			builder.append(", ");
		}
		if (getInven_num_view() != null) {
			builder.append("getInven_num_view()=");
			builder.append(getInven_num_view());
			builder.append(", ");
		}
		if (getItem_horizon_name() != null) {
			builder.append("getItem_horizon_name()=");
			builder.append(getItem_horizon_name());
			builder.append(", ");
		}
		if (getItem_vertical_name() != null) {
			builder.append("getItem_vertical_name()=");
			builder.append(getItem_vertical_name());
			builder.append(", ");
		}
		if (getItem_inven_view() != null) {
			builder.append("getItem_inven_view()=");
			builder.append(getItem_inven_view());
			builder.append(", ");
		}
		if (getSearch_type() != null) {
			builder.append("getSearch_type()=");
			builder.append(getSearch_type());
			builder.append(", ");
		}
		if (getPw_black_market() != null) {
			builder.append("getPw_black_market()=");
			builder.append(getPw_black_market());
			builder.append(", ");
		}
		if (getCatalog_id() != null) {
			builder.append("getCatalog_id()=");
			builder.append(getCatalog_id());
			builder.append(", ");
		}
		if (getBack_stock_flag() != null) {
			builder.append("getBack_stock_flag()=");
			builder.append(getBack_stock_flag());
			builder.append(", ");
		}
		if (getOut_stock_order() != null) {
			builder.append("getOut_stock_order()=");
			builder.append(getOut_stock_order());
			builder.append(", ");
		}
		if (getIn_stock_delivery_num() != null) {
			builder.append("getIn_stock_delivery_num()=");
			builder.append(getIn_stock_delivery_num());
			builder.append(", ");
		}
		if (getOut_stock_delivery_num() != null) {
			builder.append("getOut_stock_delivery_num()=");
			builder.append(getOut_stock_delivery_num());
			builder.append(", ");
		}
		if (getReserved_item_date() != null) {
			builder.append("getReserved_item_date()=");
			builder.append(getReserved_item_date());
			builder.append(", ");
		}
		if (getPercent_point_var() != null) {
			builder.append("getPercent_point_var()=");
			builder.append(getPercent_point_var());
			builder.append(", ");
		}
		if (getPeriod_point_apply() != null) {
			builder.append("getPeriod_point_apply()=");
			builder.append(getPeriod_point_apply());
			builder.append(", ");
		}
		if (getHeader_footer_leftnavi() != null) {
			builder.append("getHeader_footer_leftnavi()=");
			builder.append(getHeader_footer_leftnavi());
			builder.append(", ");
		}
		if (getOrder_display_item() != null) {
			builder.append("getOrder_display_item()=");
			builder.append(getOrder_display_item());
			builder.append(", ");
		}
		if (getDesc_common_small() != null) {
			builder.append("getDesc_common_small()=");
			builder.append(getDesc_common_small());
			builder.append(", ");
		}
		if (getItem_eye_catch() != null) {
			builder.append("getItem_eye_catch()=");
			builder.append(getItem_eye_catch());
			builder.append(", ");
		}
		if (getDesc_common_big() != null) {
			builder.append("getDesc_common_big()=");
			builder.append(getDesc_common_big());
			builder.append(", ");
		}
		if (getDisplay_review() != null) {
			builder.append("getDisplay_review()=");
			builder.append(getDisplay_review());
			builder.append(", ");
		}
		if (getManage_num_tomorrow_delivery() != null) {
			builder.append("getManage_num_tomorrow_delivery()=");
			builder.append(getManage_num_tomorrow_delivery());
			builder.append(", ");
		}
		if (getManage_num_oversea_delivery() != null) {
			builder.append("getManage_num_oversea_delivery()=");
			builder.append(getManage_num_oversea_delivery());
			builder.append(", ");
		}
		if (getLink_size_tbl() != null) {
			builder.append("getLink_size_tbl()=");
			builder.append(getLink_size_tbl());
			builder.append(", ");
		}
		if (getManage_num_dbl_price() != null) {
			builder.append("getManage_num_dbl_price()=");
			builder.append(getManage_num_dbl_price());
			builder.append(", ");
		}
		if (getReason_no_catalog_id() != null) {
			builder.append("getReason_no_catalog_id()=");
			builder.append(getReason_no_catalog_id());
			builder.append(", ");
		}
		if (getManage_num_shipping_method() != null) {
			builder.append("getManage_num_shipping_method()=");
			builder.append(getManage_num_shipping_method());
			builder.append(", ");
		}
		if (getWhite_bg_image_url() != null) {
			builder.append("getWhite_bg_image_url()=");
			builder.append(getWhite_bg_image_url());
			builder.append(", ");
		}
		if (getDisplay_maker_info() != null) {
			builder.append("getDisplay_maker_info()=");
			builder.append(getDisplay_maker_info());
			builder.append(", ");
		}
		if (getStart_date() != null) {
			builder.append("getStart_date()=");
			builder.append(getStart_date());
			builder.append(", ");
		}
		if (getEnd_date() != null) {
			builder.append("getEnd_date()=");
			builder.append(getEnd_date());
			builder.append(", ");
		}
		if (getClass() != null) {
			builder.append("getClass()=");
			builder.append(getClass());
			builder.append(", ");
		}
		builder.append("hashCode()=");
		builder.append(hashCode());
		builder.append(", ");
		if (super.toString() != null) {
			builder.append("toString()=");
			builder.append(super.toString());
		}
		builder.append("]");
		return builder.toString();
	}

}
