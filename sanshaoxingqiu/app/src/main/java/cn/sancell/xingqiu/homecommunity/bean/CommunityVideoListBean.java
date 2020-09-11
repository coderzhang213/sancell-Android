package cn.sancell.xingqiu.homecommunity.bean;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

public class CommunityVideoListBean extends ListBaseBean implements Serializable {
    private List<VideoBean> dataList;

    public List<VideoBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<VideoBean> dataList) {
        this.dataList = dataList;
    }

    public static class VideoBean implements Serializable {
        private String id;
        private String title;
        private String commVideoId;
        private int type;
        private String icon;
        private String intro;
        private int showFormat;
        private String videoUrl;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCommVideoId() {
            return commVideoId;
        }

        public void setCommVideoId(String commVideoId) {
            this.commVideoId = commVideoId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getShowFormat() {
            return showFormat;
        }

        public void setShowFormat(int showFormat) {
            this.showFormat = showFormat;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }
}
