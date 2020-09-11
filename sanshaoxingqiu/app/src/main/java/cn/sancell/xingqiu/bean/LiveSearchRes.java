package cn.sancell.xingqiu.bean;

import java.util.List;

public class LiveSearchRes {

    public LiverUserBean tvUserList;

    public LiveParFollowInfo lives;

    public static class LiverUserBean {

        public int dataCount;

        public List<LiverBean> dataList;
    }

}
