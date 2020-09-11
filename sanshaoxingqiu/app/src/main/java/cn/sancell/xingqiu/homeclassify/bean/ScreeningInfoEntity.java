package cn.sancell.xingqiu.homeclassify.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ScreeningInfoEntity implements Parcelable {
    private int first_id;//一级分类id
    private int second_id;//二级分类id
    private String third_classify_name;  //三级分类名称
    private String third_classify_id;  //三级分类id
    private String sort_general;  //综合类型
    private String sort_sales;  //销量

    public String getThird_classify_name() {
        return third_classify_name;
    }

    public void setThird_classify_name(String third_classify_name) {
        this.third_classify_name = third_classify_name;
    }

    public int getFirst_id() {
        return first_id;
    }

    public void setFirst_id(int first_id) {
        this.first_id = first_id;
    }

    public int getSecond_id() {
        return second_id;
    }

    public void setSecond_id(int second_id) {
        this.second_id = second_id;
    }

    private String sort_price;//价格排序
    private String screening_active_ids; //活动筛选
    private String screening_mark_ids;//商品标签
    private String edit_lowest_price;// 最低价
    private String edit_highest_price;//最高价

    public String getThird_classify_id() {
        return third_classify_id;
    }

    public void setThird_classify_id(String third_classify_id) {
        this.third_classify_id = third_classify_id;
    }

    public String getSort_general() {
        return sort_general;
    }

    public void setSort_general(String sort_general) {
        this.sort_general = sort_general;
    }

    public String getSort_sales() {
        return sort_sales;
    }

    public void setSort_sales(String sort_sales) {
        this.sort_sales = sort_sales;
    }

    public String getSort_price() {
        return sort_price;
    }

    public void setSort_price(String sort_price) {
        this.sort_price = sort_price;
    }

    public String getScreening_active_ids() {
        return screening_active_ids;
    }

    public void setScreening_active_ids(String screening_active_ids) {
        this.screening_active_ids = screening_active_ids;
    }

    public String getScreening_mark_ids() {
        return screening_mark_ids;
    }

    public void setScreening_mark_ids(String screening_mark_ids) {
        this.screening_mark_ids = screening_mark_ids;
    }

    public String getEdit_lowest_price() {
        return edit_lowest_price;
    }

    public void setEdit_lowest_price(String edit_lowest_price) {
        this.edit_lowest_price = edit_lowest_price;
    }

    public String getEdit_highest_price() {
        return edit_highest_price;
    }

    public void setEdit_highest_price(String edit_highest_price) {
        this.edit_highest_price = edit_highest_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.third_classify_id);
        dest.writeString(this.sort_general);
        dest.writeString(this.sort_sales);
        dest.writeString(this.sort_price);
        dest.writeString(this.screening_active_ids);
        dest.writeString(this.screening_mark_ids);
        dest.writeString(this.edit_lowest_price);
        dest.writeString(this.edit_highest_price);
    }

    public ScreeningInfoEntity() {
    }

    public ScreeningInfoEntity(String third_classify_name, String third_classify_id, String sort_general, String sort_sales, String sort_price,
                               String screening_active, String screening_mark,
                               String edit_lowest_price, String edit_highest_price) {
        this.third_classify_name = third_classify_name;
        this.third_classify_id = third_classify_id;
        this.sort_general = sort_general;
        this.sort_sales = sort_sales;
        this.sort_price = sort_price;
        this.screening_active_ids = screening_active;
        this.screening_mark_ids = screening_mark;
        this.edit_lowest_price = edit_lowest_price;
        this.edit_highest_price = edit_highest_price;
    }

    protected ScreeningInfoEntity(Parcel in) {
        this.third_classify_name = in.readString();
        this.third_classify_id = in.readString();
        this.sort_general = in.readString();
        this.sort_price = in.readString();
        this.sort_sales = in.readString();
        this.screening_active_ids = in.readString();
        this.screening_mark_ids = in.readString();
        this.edit_highest_price = in.readString();
        this.edit_lowest_price = in.readString();

    }

    public static final Parcelable.Creator<ScreeningInfoEntity> CREATOR = new Parcelable.Creator<ScreeningInfoEntity>() {
        @Override
        public ScreeningInfoEntity createFromParcel(Parcel source) {
            return new ScreeningInfoEntity(source);
        }

        @Override
        public ScreeningInfoEntity[] newArray(int size) {
            return new ScreeningInfoEntity[size];
        }
    };
}
