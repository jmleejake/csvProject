package jp.prj.araku.tanpin.vo;
import java.util.Date;

public class TanpinSubVO {
    private String seq_id;
    private String parent_seq_id;
    private String befor_trans;
    private String after_trans;
    private String jan_cd;
    private int prd_cnt;
    private Date reqister_date;
    private Date update_date;

    // デフォルトコンストラクタ
    public TanpinSubVO() {}

    // 全てのフィールドをパラメータに持つコンストラクタ
    public TanpinSubVO(String seq_id, String parent_seq_id, String befor_trans, String after_trans, String jan_cd, int prd_cnt, Date reqister_date, Date update_date) {
        this.seq_id = seq_id;
        this.parent_seq_id = parent_seq_id;
        this.befor_trans = befor_trans;
        this.after_trans = after_trans;
        this.jan_cd = jan_cd;
        this.prd_cnt = prd_cnt;
        this.reqister_date = reqister_date;
        this.update_date = update_date;
    }

    // ゲッターとセッター
    public String getSeq_id() {
        return seq_id;
    }

    public void setSeq_id(String seq_id) {
        this.seq_id = seq_id;
    }

    public String getParent_seq_id() {
        return parent_seq_id;
    }

    public void setParent_seq_id(String parent_seq_id) {
        this.parent_seq_id = parent_seq_id;
    }

    public String getBefor_trans() {
        return befor_trans;
    }

    public void setBefor_trans(String befor_trans) {
        this.befor_trans = befor_trans;
    }

    public String getAfter_trans() {
        return after_trans;
    }

    public void setAfter_trans(String after_trans) {
        this.after_trans = after_trans;
    }

    public String getJan_cd() {
        return jan_cd;
    }

    public void setJan_cd(String jan_cd) {
        this.jan_cd = jan_cd;
    }

    public int getPrd_cnt() {
        return prd_cnt;
    }

    public void setPrd_cnt(int prd_cnt) {
        this.prd_cnt = prd_cnt;
    }

    public Date getReqister_date() {
        return reqister_date;
    }

    public void setReqister_date(Date reqister_date) {
        this.reqister_date = reqister_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    // toStringメソッド
    @Override
    public String toString() {
        return "TanpinSubVO{" +
                "seq_id='" + seq_id + '\'' +
                ", parent_seq_id='" + parent_seq_id + '\'' +
                ", befor_trans='" + befor_trans + '\'' +
                ", after_trans='" + after_trans + '\'' +
                ", jan_cd='" + jan_cd + '\'' +
                ", prd_cnt=" + prd_cnt +
                ", reqister_date=" + reqister_date +
                ", update_date=" + update_date +
                '}';
    }
}
