package cn.sancell.xingqiu.live.bean;

/**
 * Created by zj on 2019/11/28.
 * 直播间商品信息
 */
public class CommentItenmInfo {
    private String title;
    private String marketPriceE2;//市场价*100
    private String sellingPriceE2;//售价*100
    private String coverPicThumb;//
    private String minPriceE2;//
    private String userRealPriceE2;
    private String goodsId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarketPriceE2() {
        return marketPriceE2;
    }

    public void setMarketPriceE2(String marketPriceE2) {
        this.marketPriceE2 = marketPriceE2;
    }

    public String getSellingPriceE2() {
        return sellingPriceE2;
    }

    public void setSellingPriceE2(String sellingPriceE2) {
        this.sellingPriceE2 = sellingPriceE2;
    }

    public String getCoverPicThumb() {
        return coverPicThumb;
    }

    public void setCoverPicThumb(String coverPicThumb) {
        this.coverPicThumb = coverPicThumb;
    }

    public String getMinPriceE2() {
        return minPriceE2;
    }

    public void setMinPriceE2(String minPriceE2) {
        this.minPriceE2 = minPriceE2;
    }

    public String getUserRealPriceE2() {
        return userRealPriceE2;
    }

    public void setUserRealPriceE2(String userRealPriceE2) {
        this.userRealPriceE2 = userRealPriceE2;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
