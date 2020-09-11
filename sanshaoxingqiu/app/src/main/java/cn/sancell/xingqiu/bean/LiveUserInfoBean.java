package cn.sancell.xingqiu.bean;

public class LiveUserInfoBean {

    public int isTvUser; //是否是主播1是 0不是
    public String userIntro;
    public int fansCount;
    public int followCount;
    public int likeCount;
    public String backgroundImg;
    public int hasLiveing;
    public int isFollow;
    public String tvUserName;
    public String tvUserGravatar;
    public int isVip;
    public int isUnReadPrivateMsg;
    public String tvUserNumber;
    public LiveInfo liveInfo;

    public static class LiveInfo{
        public String roomId;

        public String pullUrl;

        public String batchId;
    }
}
