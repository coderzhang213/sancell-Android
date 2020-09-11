package cn.sancell.xingqiu.im.entity.res;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.util.BigDecimalUtils;
import retrofit2.http.PUT;

public class VideoRelationRes implements Serializable {

    public int dataCount;
    public String type = "";//用来保存回调
    public boolean isLastGet;//是否第一次加载默认数据
    public List<InfoList> dataList;

    public static class InfoList implements Serializable {

        //商品
        public String title;

        public int marketPriceE2;
        public int newMarketPriceE2;
        public int sellingPriceE2;

        public String coverPicThumb;

        public int userRealPriceE2;

        public int goodsId;
        public String titleAlias;

        //群组
        public String id;

        public String tid;

        public String groupName;

        public String icon;

        public String intro;

        public int minPriceE2; //非会员

        public int inGroup; // 1在 0不再
        public int liveVolume;//成交量
        public int goodsBrowse;//浏览次数
        public int stock;//剩余商品

        public InfoList(int goodsBrowse, int liveVolume, String titleAlias, String title, int marketPriceE2, int sellingPriceE2, String id, String tid, String groupName, String intro, int inGroup) {
            this.title = title;
            this.marketPriceE2 = marketPriceE2;
            this.id = id;
            this.tid = tid;
            this.groupName = groupName;
            this.intro = intro;
            this.inGroup = inGroup;
            this.titleAlias = titleAlias;
            this.sellingPriceE2 = sellingPriceE2;
            this.liveVolume = liveVolume;
            this.goodsBrowse = goodsBrowse;
        }

        //会员
        public String getRelPrice() {
            return BigDecimalUtils.div(userRealPriceE2 + "", "100", 2);
        }

        //非会员
        public String getMinPrice() {
            return new StringBuffer().append(BigDecimalUtils.div(minPriceE2 + "", "100", 2)).append("起").toString();
        }

        public String getSellingPriceE2() {
            return BigDecimalUtils.div(sellingPriceE2 + "", "100", 2);
        }

        public String getNewMarketPriceE2() {
            return BigDecimalUtils.div(marketPriceE2 + "", "100", 2);
        }
    }
}
