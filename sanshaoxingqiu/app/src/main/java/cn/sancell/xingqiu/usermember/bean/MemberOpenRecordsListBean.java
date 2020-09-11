package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/13.
 */

public class MemberOpenRecordsListBean extends ListBaseBean {
    private List<MemberOpenRecordsBean> dataList;

    public List<MemberOpenRecordsBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MemberOpenRecordsBean> dataList) {
        this.dataList = dataList;
    }

    public class MemberOpenRecordsBean {
        private String id;
        private String orderId;
        private String memberLevelStr;
        private String payEndStr;
        private int goodsLevel;
        private String userIsMemberVipImageUrl;
        private String memberVipStatusStr;
        private String buyMemberVipSuccessfulImageUrl;
        private String fontColor;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMemberLevelStr() {
            return memberLevelStr;
        }

        public void setMemberLevelStr(String memberLevelStr) {
            this.memberLevelStr = memberLevelStr;
        }

        public String getPayEndStr() {
            return payEndStr;
        }

        public void setPayEndStr(String payEndStr) {
            this.payEndStr = payEndStr;
        }

        public int getGoodsLevel() {
            return goodsLevel;
        }

        public void setGoodsLevel(int goodsLevel) {
            this.goodsLevel = goodsLevel;
        }

        public String getUserIsMemberVipImageUrl() {
            return userIsMemberVipImageUrl;
        }

        public void setUserIsMemberVipImageUrl(String userIsMemberVipImageUrl) {
            this.userIsMemberVipImageUrl = userIsMemberVipImageUrl;
        }


        public String getMemberVipStatusStr() {
            return memberVipStatusStr;
        }

        public void setMemberVipStatusStr(String memberVipStatusStr) {
            this.memberVipStatusStr = memberVipStatusStr;
        }

        public String getBuyMemberVipSuccessfulImageUrl() {
            return buyMemberVipSuccessfulImageUrl;
        }

        public void setBuyMemberVipSuccessfulImageUrl(String buyMemberVipSuccessfulImageUrl) {
            this.buyMemberVipSuccessfulImageUrl = buyMemberVipSuccessfulImageUrl;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }
    }
}
