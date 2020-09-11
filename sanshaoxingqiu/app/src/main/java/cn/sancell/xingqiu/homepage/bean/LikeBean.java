package cn.sancell.xingqiu.homepage.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/10/22.
 */

public class LikeBean {
    private int id;
    private String title;
    private String desc;
    private String skuId;
    private String coverPic;
    private String coverPicThumb;
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    private String specification;
    private Long userRealPriceE2;
    private Long sellingPriceE2;
    public Long marketPriceE2;
    private int minPriceE2;
    private int memberLevelOnePriceE2;
    private int memberLevelTwoPriceE2;
    private TagInfo tagInfo;
    private int goodsLevel;
    private int goodsFlag;
    private int seckillStockNum;

    public int getSeckillStockNum() {
        return seckillStockNum;
    }

    public void setSeckillStockNum(int seckillStockNum) {
        this.seckillStockNum = seckillStockNum;
    }

    private SeckillInfo seckillInfo;

    public SeckillInfo getSeckillInfo() {
        return seckillInfo;
    }

    public void setSeckillInfo(SeckillInfo seckillInfo) {
        this.seckillInfo = seckillInfo;
    }

    public class SeckillInfo implements Serializable {
        private String id;
        private String goodsId;
        private String seckillRoundId;
        private int skillPriceE2;
        private int stocksNum;
        private int seckillGoodsNum;
        private int seckillStatus;
        private String seckillStatusStr;
        private long gapTime;
        private String stocksPercentage;
        private int seckillGoodsStatus;
        private String waitBeginTimeStr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getSeckillRoundId() {
            return seckillRoundId;
        }

        public void setSeckillRoundId(String seckillRoundId) {
            this.seckillRoundId = seckillRoundId;
        }

        public int getSkillPriceE2() {
            return skillPriceE2;
        }

        public void setSkillPriceE2(int skillPriceE2) {
            this.skillPriceE2 = skillPriceE2;
        }

        public int getStocksNum() {
            return stocksNum;
        }

        public void setStocksNum(int stocksNum) {
            this.stocksNum = stocksNum;
        }

        public int getSeckillGoodsNum() {
            return seckillGoodsNum;
        }

        public void setSeckillGoodsNum(int seckillGoodsNum) {
            this.seckillGoodsNum = seckillGoodsNum;
        }

        public int getSeckillStatus() {
            return seckillStatus;
        }

        public void setSeckillStatus(int seckillStatus) {
            this.seckillStatus = seckillStatus;
        }

        public String getSeckillStatusStr() {
            return seckillStatusStr;
        }

        public void setSeckillStatusStr(String seckillStatusStr) {
            this.seckillStatusStr = seckillStatusStr;
        }

        public long getGapTime() {
            return gapTime;
        }

        public void setGapTime(long gapTime) {
            this.gapTime = gapTime;
        }

        public String getStocksPercentage() {
            return stocksPercentage;
        }

        public void setStocksPercentage(String stocksPercentage) {
            this.stocksPercentage = stocksPercentage;
        }

        public int getSeckillGoodsStatus() {
            return seckillGoodsStatus;
        }

        public void setSeckillGoodsStatus(int seckillGoodsStatus) {
            this.seckillGoodsStatus = seckillGoodsStatus;
        }

        public String getWaitBeginTimeStr() {
            return waitBeginTimeStr;
        }

        public void setWaitBeginTimeStr(String waitBeginTimeStr) {
            this.waitBeginTimeStr = waitBeginTimeStr;
        }
    }


    public int getGoodsLevel() {
        return goodsLevel;
    }

    public void setGoodsLevel(int goodsLevel) {
        this.goodsLevel = goodsLevel;
    }

    public int getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(int goodsFlag) {
        this.goodsFlag = goodsFlag;
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


    public TagInfo getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(TagInfo tagInfo) {
        this.tagInfo = tagInfo;
    }

    public class TagInfo extends ListBaseBean {
        private List<TagBean> dataList;

        public List<TagBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<TagBean> dataList) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getCoverPicThumb() {
        return coverPicThumb;
    }

    public void setCoverPicThumb(String coverPicThumb) {
        this.coverPicThumb = coverPicThumb;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Long getUserRealPriceE2() {
        return userRealPriceE2;
    }

    public void setUserRealPriceE2(Long userRealPriceE2) {
        this.userRealPriceE2 = userRealPriceE2;
    }

    public Long getSellingPriceE2() {
        return sellingPriceE2;
    }

    public void setSellingPriceE2(Long sellingPriceE2) {
        this.sellingPriceE2 = sellingPriceE2;
    }

    public Long getMarketPriceE2() {
        return marketPriceE2;
    }

    public void setMarketPriceE2(Long marketPriceE2) {
        this.marketPriceE2 = marketPriceE2;
    }
}
