package cn.sancell.xingqiu.bean;

import java.util.List;

public class ProfileRes {

    public int dataCount;

    public long sumMoney;

    public List<ProfileBean> dataList;

    public static class ProfileBean {

        public String id;

        public String liveId;

        public String liveBatchId;

        public String userId;

        public long amountOfMoneyE2;

        public String sUserId;

        public long createdAt;

        public long updatedAt;

        public String userGravatar;

        public int type;

        public String str;
    }
}



