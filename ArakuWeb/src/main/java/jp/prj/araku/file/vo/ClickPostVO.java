package jp.prj.araku.file.vo;

import com.opencsv.bean.CsvBindByPosition;

import jp.prj.araku.util.ArakuVO;

public class ClickPostVO extends ArakuVO {
	/**お届け先郵便番号*/
	@CsvBindByPosition(position=0)
	private String post_no;
	/**お届け先氏名*/
	@CsvBindByPosition(position=1)
	private String delivery_name;
	/**お届け先敬称*/
	@CsvBindByPosition(position=2)
	private String delivery_name_title;
	/**お届け先住所1行目*/
	@CsvBindByPosition(position=3)
	private String delivery_add1;
	/**お届け先住所2行目*/
	@CsvBindByPosition(position=4)
	private String delivery_add2;
	/**お届け先住所3行目*/
	@CsvBindByPosition(position=5)
	private String delivery_add3;
	/**お届け先住所4行目*/
	@CsvBindByPosition(position=6)
	private String delivery_add4;
	/**内容品*/
	@CsvBindByPosition(position=7)
	private String delivery_contents;
	
	public String getPost_no() {
		return post_no;
	}
	public void setPost_no(String post_no) {
		this.post_no = post_no;
	}
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	public String getDelivery_name_title() {
		return delivery_name_title;
	}
	public void setDelivery_name_title(String delivery_name_title) {
		this.delivery_name_title = delivery_name_title;
	}
	public String getDelivery_add1() {
		return delivery_add1;
	}
	public void setDelivery_add1(String delivery_add1) {
		this.delivery_add1 = delivery_add1;
	}
	public String getDelivery_add2() {
		return delivery_add2;
	}
	public void setDelivery_add2(String delivery_add2) {
		this.delivery_add2 = delivery_add2;
	}
	public String getDelivery_add3() {
		return delivery_add3;
	}
	public void setDelivery_add3(String delivery_add3) {
		this.delivery_add3 = delivery_add3;
	}
	public String getDelivery_add4() {
		return delivery_add4;
	}
	public void setDelivery_add4(String delivery_add4) {
		this.delivery_add4 = delivery_add4;
	}
	public String getDelivery_contents() {
		return delivery_contents;
	}
	public void setDelivery_contents(String delivery_contents) {
		this.delivery_contents = delivery_contents;
	}
}
