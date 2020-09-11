package cn.sancell.xingqiu.homecommunity.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

public class RecommendGroupListBean extends ListBaseBean {
    //是否有直播数据
    private int liveCount;

    public int getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(int liveCount) {
        this.liveCount = liveCount;
    }

    private List<RecommGroupBean> dataList;

    public List<RecommGroupBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<RecommGroupBean> dataList) {
        this.dataList = dataList;
    }

    public static class RecommGroupBean implements MultiItemEntity {
        private String groupName;
        private String announcement;
        private String intro;
        private String icon;
        private String groupId;
        private String tid;
        private int inGroup;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public int getInGroup() {
            return inGroup;
        }

        public void setInGroup(int inGroup) {
            this.inGroup = inGroup;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }

}
