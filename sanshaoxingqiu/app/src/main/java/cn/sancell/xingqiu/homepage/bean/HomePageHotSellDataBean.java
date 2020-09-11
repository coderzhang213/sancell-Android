package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/26.
 */

public class HomePageHotSellDataBean extends BaseBean {
    private String moduleName;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public HotSaleData getHotSaleData() {
        return hotSaleData;
    }

    public void setHotSaleData(HotSaleData hotSaleData) {
        this.hotSaleData = hotSaleData;
    }

    private HotSaleData hotSaleData;

    public class HotSaleData extends ListBaseBean {
        private List<HotSellBean> dataList;

        public List<HotSellBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<HotSellBean> dataList) {
            this.dataList = dataList;
        }

        public class HotSellBean {
            private String id;
            private String title;
            private String desc;
            private String specification;
            private int userRealPriceE2;
            private int sellingPriceE2;
            public int marketPriceE2;
            private int minPriceE2;
            private int memberLevelOnePriceE2;
            private int memberLevelTwoPriceE2;
            private String coverPic;
            private String coverPicThumb;

            private TagInfo tagInfo;

            public String getCoverPicThumb() {
                return coverPicThumb;
            }

            public void setCoverPicThumb(String coverPicThumb) {
                this.coverPicThumb = coverPicThumb;
            }

            public int getMinPriceE2() {
                return minPriceE2;
            }

            public void setMinPriceE2(int minPriceE2) {
                this.minPriceE2 = minPriceE2;
            }

            public int getMemberLevelOnePriceE2() {
                return memberLevelOnePriceE2;
            }

            public void setMemberLevelOnePriceE2(int memberLevelOnePriceE2) {
                this.memberLevelOnePriceE2 = memberLevelOnePriceE2;
            }

            public int getMemberLevelTwoPriceE2() {
                return memberLevelTwoPriceE2;
            }

            public void setMemberLevelTwoPriceE2(int memberLevelTwoPriceE2) {
                this.memberLevelTwoPriceE2 = memberLevelTwoPriceE2;
            }


            public int getSellingPriceE2() {
                return sellingPriceE2;
            }

            public void setSellingPriceE2(int sellingPriceE2) {
                this.sellingPriceE2 = sellingPriceE2;
            }

            public TagInfo getTagInfo() {
                return tagInfo;
            }

            public void setTagInfo(TagInfo tagInfo) {
                this.tagInfo = tagInfo;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getSpecification() {
                return specification;
            }

            public void setSpecification(String specification) {
                this.specification = specification;
            }

            public int getUserRealPriceE2() {
                return userRealPriceE2;
            }

            public void setUserRealPriceE2(int userRealPriceE2) {
                this.userRealPriceE2 = userRealPriceE2;
            }

            public String getCoverPic() {
                return coverPic;
            }

            public void setCoverPic(String coverPic) {
                this.coverPic = coverPic;
            }

            public class TagInfo extends ListBaseBean {
                private List<TagBean> dataList;

                public List<TagBean> getDataList() {
                    return dataList;
                }

                public void setDataList(List<TagInfo.TagBean> dataList) {
                    this.dataList = dataList;
                }

                public class TagBean {
                    private int id;
                    private String tagName;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getTagName() {
                        return tagName;
                    }

                    public void setTagName(String tagName) {
                        this.tagName = tagName;
                    }
                }
            }
        }
    }
}
