package cn.sancell.xingqiu.homeuser.bean;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/19.
 */

public class AddressListDataBean extends ListBaseBean {
    private List<AddressItemBean> dataList;

    public List<AddressItemBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<AddressItemBean> dataList) {
        this.dataList = dataList;
    }

    public static class AddressItemBean implements Serializable {
        private int id;
        private int userId;
        private String consignee;
        private String mobile;
        private String address;
        private int isDefault;//        是否是默认 1：是 2：否
        private String markName;
        private String provinceName, cityName, areasName, streetName;
        private String codeString;
        private int provinceId, cityId, areasId, streetId;
        public String refundStatus; //1.无 2.审核中 3.审核成功 4.审核失败
        public String url;//订单状态url
        public int lastChoose;//上一次选择的 地址 0：不是，1：是的
        public boolean isDelivery;//是否可以配送

        //local
        public boolean isCheck;  //是否点击

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getMarkName() {
            return markName;
        }

        public void setMarkName(String markName) {
            this.markName = markName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreasName() {
            return areasName;
        }

        public void setAreasName(String areasName) {
            this.areasName = areasName;
        }

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public String getCodeString() {
            return codeString;
        }

        public void setCodeString(String codeString) {
            this.codeString = codeString;
        }

        public int getStreetId() {
            return streetId;
        }

        public void setStreetId(int streetId) {
            this.streetId = streetId;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public int getAreasId() {
            return areasId;
        }

        public void setAreasId(int areasId) {
            this.areasId = areasId;
        }

        public int getTownId() {
            return streetId;
        }

        public void setTownId(int townId) {
            this.streetId = townId;
        }

    }
}
