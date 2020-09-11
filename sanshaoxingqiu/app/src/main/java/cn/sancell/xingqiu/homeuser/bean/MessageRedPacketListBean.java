package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/23.
 */

public class MessageRedPacketListBean extends ListBaseBean {
    private List<MessageRedPacketBean> dataList;

    public List<MessageRedPacketBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MessageRedPacketBean> dataList) {
        this.dataList = dataList;
    }

    public class MessageRedPacketBean {
        private int id;
        private int pointType;
        private String noticeTitle;
        private String noticeDesc;
        private String publicTimeStr;
        private int readStatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPointType() {
            return pointType;
        }

        public void setPointType(int pointType) {
            this.pointType = pointType;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getNoticeDesc() {
            return noticeDesc;
        }

        public void setNoticeDesc(String noticeDesc) {
            this.noticeDesc = noticeDesc;
        }

        public String getPublicTimeStr() {
            return publicTimeStr;
        }

        public void setPublicTimeStr(String publicTimeStr) {
            this.publicTimeStr = publicTimeStr;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }
    }
}
