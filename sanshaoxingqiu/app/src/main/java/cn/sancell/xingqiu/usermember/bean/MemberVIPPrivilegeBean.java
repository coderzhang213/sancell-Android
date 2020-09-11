package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/9/25.
 */

public class MemberVIPPrivilegeBean {

    private VipInterestsIndex vipInterestsIndex;


    private VipPrivilegeListBean vipInterestsIntroduceData;

    public VipInterestsIndex getVipInterestsIndex() {
        return vipInterestsIndex;
    }

    public void setVipInterestsIndex(VipInterestsIndex vipInterestsIndex) {
        this.vipInterestsIndex = vipInterestsIndex;
    }

    public class VipInterestsIndex {
        private String coverPic;
        private int picWidth;
        private int picHeight;


        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public int getPicWidth() {
            return picWidth;
        }

        public void setPicWidth(int picWidth) {
            this.picWidth = picWidth;
        }

        public int getPicHeight() {
            return picHeight;
        }

        public void setPicHeight(int picHeight) {
            this.picHeight = picHeight;
        }
    }

    public VipPrivilegeListBean getVipPrivilegeListBean() {
        return vipInterestsIntroduceData;
    }

    public void setVipPrivilegeListBean(VipPrivilegeListBean vipPrivilegeListBean) {
        this.vipInterestsIntroduceData = vipPrivilegeListBean;
    }

    public class VipPrivilegeListBean extends ListBaseBean {
        private List<SinglePrivilegeBean> dataList;

        public List<SinglePrivilegeBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<SinglePrivilegeBean> dataList) {
            this.dataList = dataList;
        }

        public class SinglePrivilegeBean {
            private String coverPic;

            public String getCoverPic() {
                return coverPic;
            }

            public void setCoverPic(String coverPic) {
                this.coverPic = coverPic;
            }
        }
    }
}
