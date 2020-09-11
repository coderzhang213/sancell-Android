package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/9/8.
 */

public class SeckillSessionProductListBean extends ListBaseBean {

    private List<SeckillProductBean> dataList;

    public List<SeckillProductBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SeckillProductBean> dataList) {
        this.dataList = dataList;
    }

    public class SeckillProductBean {
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
        private ProductInfo goodsInfo;


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

        public ProductInfo getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(ProductInfo goodsInfo) {
            this.goodsInfo = goodsInfo;
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

        public class ProductInfo {
            private String title;
            private String coverPic;
            private String coverPicThumb;
            private int sellingPriceE2;
            public int marketPriceE2;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public int getSellingPriceE2() {
                return sellingPriceE2;
            }

            public void setSellingPriceE2(int sellingPriceE2) {
                this.sellingPriceE2 = sellingPriceE2;
            }
        }
    }
}
