package cn.sancell.xingqiu.im.entity.res;

import java.util.List;

public class VideoListRes {

    public int dataCount;

    public List<VideoBean> dataList;

    public static class VideoBean {

        public int dataCount;

        public String title;

        public int type;
        public String icon;
        public String intro;
        public int showFormat;
        public String videoUrl;

    }
}
