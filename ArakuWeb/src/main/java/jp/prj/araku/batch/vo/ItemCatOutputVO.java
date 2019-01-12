package jp.prj.araku.batch.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class ItemCatOutputVO extends ArakuVO {
	
	/**コントロールカラム*/
	@CsvBindByPosition(position=0)
	private String ctrl_col;
	/**商品管理番号（商品URL）*/
	@CsvBindByPosition(position=1)
	private String item_url;
	/**商品名*/
	@CsvBindByPosition(position=2)
	private String item_name;
	/**優先度*/
	@CsvBindByPosition(position=3)
	private String priority;
	/**1ページ複数形式*/
	@CsvBindByPosition(position=4)
	private String page1_multi_format;
	/**表示先カテゴリ*/
	@CsvBindByPosition(position=5)
	private String destination_category;
	
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
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPage1_multi_format() {
		return page1_multi_format;
	}
	public void setPage1_multi_format(String page1_multi_format) {
		this.page1_multi_format = page1_multi_format;
	}
	public String getDestination_category() {
		return destination_category;
	}
	public void setDestination_category(String destination_category) {
		this.destination_category = destination_category;
	}

}
