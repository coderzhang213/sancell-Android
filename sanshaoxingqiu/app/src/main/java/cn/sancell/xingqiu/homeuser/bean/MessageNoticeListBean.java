package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/23.
 */

public class MessageNoticeListBean extends ListBaseBean {
    private List<MessageNoticeBean> dataList;

    public List<MessageNoticeBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MessageNoticeBean> dataList) {
        this.dataList = dataList;
    }

    public class MessageNoticeBean {
        private String title;
        private String content;
        private String url;
        private int readStatus;
        private int userId;
        private String publicTimeStr;
        private int isNeedLoginData;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPublicTimeStr() {
            return publicTimeStr;
        }

        public void setPublicTimeStr(String publicTimeStr) {
            this.publicTimeStr = publicTimeStr;
        }

        public int getIsNeedLoginData() {
            return isNeedLoginData;
        }

        public void setIsNeedLoginData(int isNeedLoginData) {
            this.isNeedLoginData = isNeedLoginData;
        }
    }
}
