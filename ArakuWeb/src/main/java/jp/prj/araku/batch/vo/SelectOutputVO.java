package jp.prj.araku.batch.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class SelectOutputVO extends ArakuVO {
	
	/**項目選択肢用コントロールカラム*/
	@CsvBindByPosition(position=0)
	private String item_ctrl_col;
	/**商品管理番号（商品URL）*/
	@CsvBindByPosition(position=1)
	private String item_url;
	/**選択肢タイプ*/
	@CsvBindByPosition(position=2)
	private String selection_type;
	/**Select/Checkbox用項目名*/
	@CsvBindByPosition(position=3)
	private String sel_chk_item_name;
	/**Select/Checkbox用選択肢*/
	@CsvBindByPosition(position=4)
	private String sel_chk_options;
	/**項目選択肢別在庫用横軸選択肢*/
	@CsvBindByPosition(position=5)
	private String item_horizon_axis;
	/**項目選択肢別在庫用横軸選択肢子番号*/
	@CsvBindByPosition(position=6)
	private String item_horizon_choice_num;
	/**項目選択肢別在庫用縦軸選択肢*/
	@CsvBindByPosition(position=7)
	private String item_vertical_axis;
	/**項目選択肢別在庫用縦軸選択肢子番号*/
	@CsvBindByPosition(position=8)
	private String item_vertical_choice_num;
	/**項目選択肢別在庫用取り寄せ可能表示*/
	@CsvBindByPosition(position=9)
	private String item_orderable_display;
	/**項目選択肢別在庫用在庫数*/
	@CsvBindByPosition(position=10)
	private String item_inventory;
	/**在庫戻しフラグ*/
	@CsvBindByPosition(position=11)
	private String stock_flag;
	/**在庫切れ時の注文受付*/
	@CsvBindByPosition(position=12)
	private String out_stock_order;
	/**在庫あり時納期管理番号*/
	@CsvBindByPosition(position=13)
	private String in_stock_delivery_num;
	/**在庫切れ時納期管理番号*/
	@CsvBindByPosition(position=14)
	private String out_stock_delivery_num;
	/**タグID*/
	@CsvBindByPosition(position=15)
	private String tag_id;
	/**画像URL*/
	@CsvBindByPosition(position=16)
	private String image_url;
	
	public String getItem_ctrl_col() {
		return item_ctrl_col;
	}
	public void setItem_ctrl_col(String item_ctrl_col) {
		this.item_ctrl_col = item_ctrl_col;
	}
	public String getItem_url() {
		return item_url;
	}
	public void setItem_url(String item_url) {
		this.item_url = item_url;
	}
	public String getSelection_type() {
		return selection_type;
	}
	public void setSelection_type(String selection_type) {
		this.selection_type = selection_type;
	}
	public String getSel_chk_item_name() {
		return sel_chk_item_name;
	}
	public void setSel_chk_item_name(String sel_chk_item_name) {
		this.sel_chk_item_name = sel_chk_item_name;
	}
	public String getSel_chk_options() {
		return sel_chk_options;
	}
	public void setSel_chk_options(String sel_chk_options) {
		this.sel_chk_options = sel_chk_options;
	}
	public String getItem_horizon_axis() {
		return item_horizon_axis;
	}
	public void setItem_horizon_axis(String item_horizon_axis) {
		this.item_horizon_axis = item_horizon_axis;
	}
	public String getItem_horizon_choice_num() {
		return item_horizon_choice_num;
	}
	public void setItem_horizon_choice_num(String item_horizon_choice_num) {
		this.item_horizon_choice_num = item_horizon_choice_num;
	}
	public String getItem_vertical_axis() {
		return item_vertical_axis;
	}
	public void setItem_vertical_axis(String item_vertical_axis) {
		this.item_vertical_axis = item_vertical_axis;
	}
	public String getItem_vertical_choice_num() {
		return item_vertical_choice_num;
	}
	public void setItem_vertical_choice_num(String item_vertical_choice_num) {
		this.item_vertical_choice_num = item_vertical_choice_num;
	}
	public String getItem_orderable_display() {
		return item_orderable_display;
	}
	public void setItem_orderable_display(String item_orderable_display) {
		this.item_orderable_display = item_orderable_display;
	}
	public String getItem_inventory() {
		return item_inventory;
	}
	public void setItem_inventory(String item_inventory) {
		this.item_inventory = item_inventory;
	}
	public String getStock_flag() {
		return stock_flag;
	}
	public void setStock_flag(String stock_flag) {
		this.stock_flag = stock_flag;
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
	public String getTag_id() {
		return tag_id;
	}
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

}
