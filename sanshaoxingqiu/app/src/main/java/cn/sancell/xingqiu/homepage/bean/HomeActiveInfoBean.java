package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/9/10.
 */

public class HomeActiveInfoBean {
    private HeadlinesInfo headlinesInfo;
    private CapsuleInfo capsuleInfo;
    private GetNavInfo navInfo;


    public HeadlinesInfo getHeadlinesInfo() {
        return headlinesInfo;
    }

    public void setHeadlinesInfo(HeadlinesInfo headlinesInfo) {
        this.headlinesInfo = headlinesInfo;
    }

    public CapsuleInfo getCapsuleInfo() {
        return capsuleInfo;
    }

    public void setCapsuleInfo(CapsuleInfo capsuleInfo) {
        this.capsuleInfo = capsuleInfo;
    }

    public GetNavInfo getGetNavInfo() {
        return navInfo;
    }

    public void setGetNavInfo(GetNavInfo getNavInfo) {
        this.navInfo = getNavInfo;
    }

    public class GetNavInfo extends ListBaseBean {
        private List<GetNavBean> dataList;

        public List<GetNavBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<GetNavBean> dataList) {
            this.dataList = dataList;
        }

        public class GetNavBean {
            private String webUrl;

            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getWebUrl() {
                return webUrl;
            }

            public void setWebUrl(String webUrl) {
                this.webUrl = webUrl;
            }
        }
    }


    public class CapsuleInfo {
        private String coverPic;

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }
    }

    public class HeadlinesInfo extends ListBaseBean {
        private List<HeadlinesBean> dataList;

        public List<HeadlinesBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<HeadlinesBean> dataList) {
            this.dataList = dataList;
        }

        public class HeadlinesBean {
            private int id;
            private String title;
            private String webUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getWebUrl() {
                return webUrl;
            }

            public void setWebUrl(String webUrl) {
                this.webUrl = webUrl;
            }
        }
    }
}
