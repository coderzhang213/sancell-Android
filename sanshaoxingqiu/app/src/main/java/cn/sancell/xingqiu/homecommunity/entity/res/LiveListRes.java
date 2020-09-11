package cn.sancell.xingqiu.homecommunity.entity.res;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class LiveListRes implements MultiItemEntity {

    public String id;

    public String accid;

    public String status;

    public String icon;

    public String liveStatus;

    public String liveName;

    public String liveIntro;
    public String pullUrl;
    public String roomId;
    public int onlineUser;//观看人数
    public List<RelationGoods> relationGoodsList;
    public String batchId;//直播批次Id
    public int itemType = 0;
    public String replayUrl;//回放地址

    @Override
    public int getItemType() {
        return itemType;
    }


    public static class RelationGoods {

        public String title;

        public String marketPriceE2;

        public String sellingPriceE2;


        public String coverPicThumb;
    }

}
