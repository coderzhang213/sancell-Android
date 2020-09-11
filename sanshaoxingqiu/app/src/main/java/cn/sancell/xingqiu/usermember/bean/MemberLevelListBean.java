package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberLevelListBean extends ListBaseBean {
    private List<MemberLevelBean> dataList;

    public List<MemberLevelBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MemberLevelBean> dataList) {
        this.dataList = dataList;
    }

    public class MemberLevelBean{
        private int memberLevelId;
        private String memberLevelStr;
        private String buyMemberVipSuccessfulImageUrl;
        private String userIsMemberVipImageUrl;
        private String userBuyImageUrl;
        private String fontColor;
        private boolean isselect;
        private String logoCoverPic;

        public String getLogoCoverPic() {
            return logoCoverPic;
        }

        public void setLogoCoverPic(String logoCoverPic) {
            this.logoCoverPic = logoCoverPic;
        }

        public boolean isIsselect() {
            return isselect;
        }

        public void setIsselect(boolean isselect) {
            this.isselect = isselect;
        }

        public int getMemberLevelId() {
            return memberLevelId;
        }

        public void setMemberLevelId(int memberLevelId) {
            this.memberLevelId = memberLevelId;
        }

        public String getMemberLevelStr() {
            return memberLevelStr;
        }

        public void setMemberLevelStr(String memberLevelStr) {
            this.memberLevelStr = memberLevelStr;
        }

        public String getBuyMemberVipSuccessfulImageUrl() {
            return buyMemberVipSuccessfulImageUrl;
        }

        public void setBuyMemberVipSuccessfulImageUrl(String buyMemberVipSuccessfulImageUrl) {
            this.buyMemberVipSuccessfulImageUrl = buyMemberVipSuccessfulImageUrl;
        }

        public String getUserIsMemberVipImageUrl() {
            return userIsMemberVipImageUrl;
        }

        public void setUserIsMemberVipImageUrl(String userIsMemberVipImageUrl) {
            this.userIsMemberVipImageUrl = userIsMemberVipImageUrl;
        }

        public String getUserBuyImageUrl() {
            return userBuyImageUrl;
        }

        public void setUserBuyImageUrl(String userBuyImageUrl) {
            this.userBuyImageUrl = userBuyImageUrl;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }
    }
}
