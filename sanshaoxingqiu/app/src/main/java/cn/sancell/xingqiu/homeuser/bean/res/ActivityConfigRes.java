package cn.sancell.xingqiu.homeuser.bean.res;

public class ActivityConfigRes {


    public ConfigBean activityConfig;


    public static class ConfigBean {
        //活动配置
        public int id;
        public String title;
        public long startTime;
        public long endTime;
        public String startTimeStr;
        public String endTimeStr;
        public String url; //活动地址
        public String realUrl; //活动完整地址
        public int status; //1：进行中 ，2：未开始，3：已结束
        public int isEntry;
        public int userId;
        public int statusRel;
        public String createdAt;
        public String updatedAt;
        public String statusStr;
        public String bannerPic;
        public String background;//背景地址
    }

}
