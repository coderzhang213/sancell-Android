package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/10/22.
 */

public class OneYuanPurchaseBean {
    private UserInfo userInfo;

    public class UserInfo {
        private int times;

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }
    }

    private List<OneYuanPurchaseProductBean> goodsInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<OneYuanPurchaseProductBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OneYuanPurchaseProductBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }


    public class OneYuanPurchaseProductBean{
        private int id;
        private String goodsId;
        private int onePriceE2;
        private int sellStockNumber;
        private String title;
        private String coverPicThumb;
        private String appInfo;
        private int marketPriceE2;

        public int getMarketPriceE2() {
            return marketPriceE2;
        }

        public void setMarketPriceE2(int marketPriceE2) {
            this.marketPriceE2 = marketPriceE2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getOnePriceE2() {
            return onePriceE2;
        }

        public void setOnePriceE2(int onePriceE2) {
            this.onePriceE2 = onePriceE2;
        }

        public int getSellStockNumber() {
            return sellStockNumber;
        }

        public void setSellStockNumber(int sellStockNumber) {
            this.sellStockNumber = sellStockNumber;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCoverPicThumb() {
            return coverPicThumb;
        }

        public void setCoverPicThumb(String coverPicThumb) {
            this.coverPicThumb = coverPicThumb;
        }

        public String getAppInfo() {
            return appInfo;
        }

        public void setAppInfo(String appInfo) {
            this.appInfo = appInfo;
        }
    }
}
