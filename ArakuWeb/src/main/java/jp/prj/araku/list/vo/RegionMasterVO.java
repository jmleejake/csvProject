package jp.prj.araku.list.vo;

public class RegionMasterVO {
	/**区分ID*/
	private String seq_id;
	/**データ登録日*/
	private String register_date;
	/**地域名称*/
	private String parent_region_name;
	/**名称*/
	private String region_name;
	/**名称(英語)*/
	private String region_name_en;
	/**配送会社*/
	private String delivery_company;
	
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
	public String getParent_region_name() {
		return parent_region_name;
	}
	public void setParent_region_name(String parent_region_name) {
		this.parent_region_name = parent_region_name;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getRegion_name_en() {
		return region_name_en;
	}
	public void setRegion_name_en(String region_name_en) {
		this.region_name_en = region_name_en;
	}
	public String getDelivery_company() {
		return delivery_company;
	}
	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}
}
