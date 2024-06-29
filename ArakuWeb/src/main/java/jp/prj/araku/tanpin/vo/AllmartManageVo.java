package jp.prj.araku.tanpin.vo;

import java.util.ArrayList;
import java.util.List;

import jp.prj.araku.util.ArakuVO;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class AllmartManageVo extends ArakuVO{

    /** 商品ID */
	@CsvBindByPosition(position=0)
    private String product_id;
    /** 部門ID */
	@CsvBindByPosition(position=1)
    private String department_id;
    /** グループコード */
	@CsvBindByPosition(position=2)
    private String group_code;
    /** 商品コード */
	@CsvBindByPosition(position=3)
    private String product_code;
    /** 商品名 */
	@CsvBindByPosition(position=4)
    private String product_name;
    /** 商品単価 */
	@CsvBindByPosition(position=5)
    private String unit_price;
    /** 税区分 */
	@CsvBindByPosition(position=6)
    private String tax_category;
    
	/**データ登録日*/
	private String insert_date;
	/**データ修正日*/
	private String update_date;
	/** ログイン情報*/
	private String login_id;
	
	private String start_date;
	private String end_date;
	private String prd_nm;
	private String jan_cd;
	
	private String select_type;
	private String search_type;
	private ArrayList<String> product_id_list;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getTax_category() {
        return tax_category;
    }

    public void setTax_category(String tax_category) {
        this.tax_category = tax_category;
    }
}
