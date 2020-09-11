package cn.sancell.xingqiu.order.entity.res;

import java.util.List;

public class PinDetailRes {

    public int grouponNum;

    public String grouponOrderStatus;

    public GoodsInfo goods;

    public static class GoodsInfo {
        public String title;

        public String coverPic;

        public int goodsId;

        public long grouponPriceE2;
    }

    public int lastUserNum;
    public List<GrouponUsersInfo> grouponUsersInfo;
    public long grouponOwnerBonus;
    public String currentUserIsOwner;
    public int grouponSuccessUser;
    public int payStatus;
    public long finishTime;
    public long currentTime;
    public String buyerOrderId;
    public String grouponFinishHour;
    public int stockStatus; //库存状态（1有库存，2拼团库存售罄；3安全库存售罄）
    public int currentUserInGroupon;
    public int isActivityGoods;
    public String parcelId;
    public int currentUserStatus;
    public InviteBean grouponInviteData;

    public static class GrouponUsersInfo {
        public String nickName;
        public String gravatar;
        public String isGrouponOwner;
    }


}
