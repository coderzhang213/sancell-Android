package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberVipGiftBean {
    private String bannerCoverPic;
    private List<VipGiftBean> giftData;


    public String getBannerCoverPic() {
        return bannerCoverPic;
    }

    public void setBannerCoverPic(String bannerCoverPic) {
        this.bannerCoverPic = bannerCoverPic;
    }

    public List<VipGiftBean> getGiftData() {
        return giftData;
    }

    public void setGiftData(List<VipGiftBean> giftData) {
        this.giftData = giftData;
    }

    public class VipGiftBean {
        private int memberLevelId;
        private String memberLevelStr;
        private String coverPic;
        private GiftListBean memberVipData;

        private RelatedVipRelatedData relatedVipRelatedData;

        public RelatedVipRelatedData getRelatedVipRelatedData() {
            return relatedVipRelatedData;
        }

        public void setRelatedVipRelatedData(RelatedVipRelatedData relatedVipRelatedData) {
            this.relatedVipRelatedData = relatedVipRelatedData;
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

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public GiftListBean getMemberVipData() {
            return memberVipData;
        }

        public void setMemberVipData(GiftListBean memberVipData) {
            this.memberVipData = memberVipData;
        }

        public class GiftListBean extends ListBaseBean {
            private List<LikeBean> dataList;

            public List<LikeBean> getDataList() {
                return dataList;
            }

            public void setDataList(List<LikeBean> dataList) {
                this.dataList = dataList;
            }
        }

        public class RelatedVipRelatedData {
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
    }


}
