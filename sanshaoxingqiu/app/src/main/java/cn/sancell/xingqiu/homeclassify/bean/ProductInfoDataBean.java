package cn.sancell.xingqiu.homeclassify.bean;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.ProducAddressInfo;
import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.homepage.bean.OneYuanPurchaseBean;

/**
 * Created by ai11 on 2019/6/16.
 */

public class ProductInfoDataBean extends BaseBean {
    private String title;
    private int id;
    private int userRealPriceE2;
    private int sellingPriceE2;
    private int memberLevelOnePriceE2;
    private int memberLevelTwoPriceE2;
    private int memberLevelThreePriceE2;
    private int minPriceE2;
    private int marketPriceE2;
    private int lv0ProfitSharingE2;
    private int lv1ProfitSharingE2;
    private int lv2ProfitSharingE2;
    private int lv3ProfitSharingE2;
    private int lv0RecommProfitSharingE2;
    private int lv1RecommProfitSharingE2;
    private int lv2RecommProfitSharingE2;
    private int lv3RecommProfitSharingE2;
    private int refundStatus;
    private int refundDays;
    private String desc;
    private String specification;
    private int car_num;
    private String coverPic;
    private int stockNumber;
    private int minBuyNum;
    private String evaluationPercentageStr;

    private TagInfoListData tagInfo;
    private List<String> bannerPicArr;

    private int goodsFlag;
    private int goodsLevel;

    private int seckillGoodsStatus;
    private String seckillGoodsStatusStr;

    private int seckillStockNum;

    public ActivityConfigBean activityConfig;

    private ProducAddressInfo goodsRegion;

    public ProducAddressInfo getGoodsRegion() {
        return goodsRegion;
    }

    public void setGoodsRegion(ProducAddressInfo goodsRegion) {
        this.goodsRegion = goodsRegion;
    }

    //团购
    public PinGroupInfo grouponInfo;

    public int hasGroupon; //1有团；0无团

    public long astrictPointE2; //红包限制使用金额

    public int canUseCouponCount;
    public int productType;
    public String lv1FansSharingE2;
    public String lv2FansSharingE2;
    public String lv3FansSharingE2;


    public static class PinGroupInfo implements Serializable {

        public int grouponBuyPageStatus;

        public long groupOrderId;

        public int buyerNum;

        public int grouponPriceE2;

        public long grouponEndTime;

        public String grouponFinishHour;

        public List<GroupRecommend> grouponOrders;

        public int grouponSumStorck;


        public static class GroupRecommend {

            public String nickName;

            public String gravatar;

            public String lastUserNum;

            public long currentTime;

            public long finishTime;

            public int grouponOrderId;

        }

    }


    public class TagInfoListData extends ListBaseBean {
        private List<TagInfoBean> dataList;

        public List<TagInfoBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<TagInfoBean> dataList) {
            this.dataList = dataList;
        }

        public class TagInfoBean {
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

    private ServerInfoListData serverInfo;

    public class ServerInfoListData extends ListBaseBean {
        private List<ServerInfo> dataList;

        public List<ServerInfo> getDataList() {
            return dataList;
        }

        public void setDataList(List<ServerInfo> dataList) {
            this.dataList = dataList;
        }

        public class ServerInfo {
            private int id;
            private String serverName;
            private String serverDesc;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getServerName() {
                return serverName;
            }

            public void setServerName(String serverName) {
                this.serverName = serverName;
            }

            public String getServerDesc() {
                return serverDesc;
            }

            public void setServerDesc(String serverDesc) {
                this.serverDesc = serverDesc;
            }
        }
    }

    private List<PricesData> pricesData;

    public class PricesData {
        private String pic_mark;
        private String price_name;
        private int price;

    }


    private AttrInfoListData attrInfo;

    public class AttrInfoListData implements Serializable {
        private List<AttrInfo> dataList;

        public List<AttrInfo> getDataList() {
            return dataList;
        }

        public void setDataList(List<AttrInfo> dataList) {
            this.dataList = dataList;
        }

        public class AttrInfo {
            private int id;
            private String attrName;
            private String attrValue;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAttrName() {
                return attrName;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

            public String getAttrValue() {
                return attrValue;
            }

            public void setAttrValue(String attrValue) {
                this.attrValue = attrValue;
            }
        }
    }

    private GoodsDetailShare goodsDetailShare;

    public GoodsDetailShare getGoodsDetailShare() {
        return goodsDetailShare;
    }

    public void setGoodsDetailShare(GoodsDetailShare goodsDetailShare) {
        this.goodsDetailShare = goodsDetailShare;
    }

    public class GoodsDetailShare {
        private String title;
        private String desc;
        private String link;
        private String logoUrl;
        private int isShow;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }


        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }


    private List<InfoPics> detailPicArr;

    public class InfoPics {
        private String coverPic;
        private int picWidth;
        private int picHeight;


        public String getPic_url() {
            return coverPic;
        }

        public void setPic_url(String pic_url) {
            this.coverPic = pic_url;
        }

        public int getPic_width() {
            return picWidth;
        }

        public void setPic_width(int pic_width) {
            this.picWidth = pic_width;
        }

        public int getPic_hight() {
            return picHeight;
        }

        public void setPic_hight(int pic_hight) {
            this.picHeight = pic_hight;
        }
    }

    private OneYuanPurchaseBean.OneYuanPurchaseProductBean oneShop;

    public OneYuanPurchaseBean.OneYuanPurchaseProductBean getOneShop() {
        return oneShop;
    }

    public void setOneShop(OneYuanPurchaseBean.OneYuanPurchaseProductBean oneShop) {
        this.oneShop = oneShop;
    }

    private LikeBean.SeckillInfo seckillInfo;

    public LikeBean.SeckillInfo getSeckillInfo() {
        return seckillInfo;
    }

    public void setSeckillInfo(LikeBean.SeckillInfo seckillInfo) {
        this.seckillInfo = seckillInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getBannerPicArr() {
        return bannerPicArr;
    }

    public void setBannerPicArr(List<String> bannerPicArr) {
        this.bannerPicArr = bannerPicArr;
    }

    public List<InfoPics> getDetailPicArr() {
        return detailPicArr;
    }

    public void setDetailPicArr(List<InfoPics> detailPicArr) {
        this.detailPicArr = detailPicArr;
    }

    public int getUserRealPriceE2() {
        return userRealPriceE2;
    }

    public void setUserRealPriceE2(int userRealPriceE2) {
        this.userRealPriceE2 = userRealPriceE2;
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


    public int getCar_num() {
        return car_num;
    }

    public void setCar_num(int car_num) {
        this.car_num = car_num;
    }


    public TagInfoListData getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(TagInfoListData tagInfo) {
        this.tagInfo = tagInfo;
    }

    public ServerInfoListData getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfoListData serverInfo) {
        this.serverInfo = serverInfo;
    }

    public AttrInfoListData getAttrInfo() {
        return attrInfo;
    }

    public void setAttrInfo(AttrInfoListData attrInfo) {
        this.attrInfo = attrInfo;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public int getSellingPriceE2() {
        return sellingPriceE2;
    }

    public void setSellingPriceE2(int sellingPriceE2) {
        this.sellingPriceE2 = sellingPriceE2;
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

    public int getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(int minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getEvaluationPercentageStr() {
        return evaluationPercentageStr;
    }

    public void setEvaluationPercentageStr(String evaluationPercentageStr) {
        this.evaluationPercentageStr = evaluationPercentageStr;
    }

    public int getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(int goodsFlag) {
        this.goodsFlag = goodsFlag;
    }

    public int getGoodsLevel() {
        return goodsLevel;
    }

    public void setGoodsLevel(int goodsLevel) {
        this.goodsLevel = goodsLevel;
    }

    public int getMinPriceE2() {
        return minPriceE2;
    }

    public void setMinPriceE2(int minPriceE2) {
        this.minPriceE2 = minPriceE2;
    }

    public int getMarketPriceE2() {
        return marketPriceE2;
    }

    public void setMarketPriceE2(int marketPriceE2) {
        this.marketPriceE2 = marketPriceE2;
    }

    public int getMemberLevelThreePriceE2() {
        return memberLevelThreePriceE2;
    }

    public void setMemberLevelThreePriceE2(int memberLevelThreePriceE2) {
        this.memberLevelThreePriceE2 = memberLevelThreePriceE2;
    }

    public int getLv2ProfitSharingE2() {
        return lv2ProfitSharingE2;
    }

    public void setLv2ProfitSharingE2(int lv2ProfitSharingE2) {
        this.lv2ProfitSharingE2 = lv2ProfitSharingE2;
    }

    public int getLv3ProfitSharingE2() {
        return lv3ProfitSharingE2;
    }

    public void setLv3ProfitSharingE2(int lv3ProfitSharingE2) {
        this.lv3ProfitSharingE2 = lv3ProfitSharingE2;
    }

    public int getLv2RecommProfitSharingE2() {
        return lv2RecommProfitSharingE2;
    }

    public void setLv2RecommProfitSharingE2(int lv2RecommProfitSharingE2) {
        this.lv2RecommProfitSharingE2 = lv2RecommProfitSharingE2;
    }

    public int getLv3RecommProfitSharingE2() {
        return lv3RecommProfitSharingE2;
    }

    public void setLv3RecommProfitSharingE2(int lv3RecommProfitSharingE2) {
        this.lv3RecommProfitSharingE2 = lv3RecommProfitSharingE2;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getRefundDays() {
        return refundDays;
    }

    public void setRefundDays(int refundDays) {
        this.refundDays = refundDays;
    }

    public int getSeckillGoodsStatus() {
        return seckillGoodsStatus;
    }

    public void setSeckillGoodsStatus(int seckillGoodsStatus) {
        this.seckillGoodsStatus = seckillGoodsStatus;
    }

    public String getSeckillGoodsStatusStr() {
        return seckillGoodsStatusStr;
    }

    public void setSeckillGoodsStatusStr(String seckillGoodsStatusStr) {
        this.seckillGoodsStatusStr = seckillGoodsStatusStr;
    }

    public int getSeckillStockNum() {
        return seckillStockNum;
    }

    public void setSeckillStockNum(int seckillStockNum) {
        this.seckillStockNum = seckillStockNum;
    }


    public int getLv0ProfitSharingE2() {
        return lv0ProfitSharingE2;
    }

    public void setLv0ProfitSharingE2(int lv0ProfitSharingE2) {
        this.lv0ProfitSharingE2 = lv0ProfitSharingE2;
    }

    public int getLv1ProfitSharingE2() {
        return lv1ProfitSharingE2;
    }

    public void setLv1ProfitSharingE2(int lv1ProfitSharingE2) {
        this.lv1ProfitSharingE2 = lv1ProfitSharingE2;
    }

    public int getLv0RecommProfitSharingE2() {
        return lv0RecommProfitSharingE2;
    }

    public void setLv0RecommProfitSharingE2(int lv0RecommProfitSharingE2) {
        this.lv0RecommProfitSharingE2 = lv0RecommProfitSharingE2;
    }

    public int getLv1RecommProfitSharingE2() {
        return lv1RecommProfitSharingE2;
    }

    public void setLv1RecommProfitSharingE2(int lv1RecommProfitSharingE2) {
        this.lv1RecommProfitSharingE2 = lv1RecommProfitSharingE2;
    }

    public static class ActivityConfigBean {

        public int activityId;

        public long startTime;
        public long endTime;
        public String startTimeStr;
        public String endTimeStr;
        public int sellStockNumber;
        public int salesVolume;
        public int userRealPriceE2;
        public int status;
        public String statusStr;
        public int goodsStatus;
    }


}
