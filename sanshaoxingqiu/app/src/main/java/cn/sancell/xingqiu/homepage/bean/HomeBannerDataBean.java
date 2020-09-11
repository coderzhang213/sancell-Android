package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/26.
 */

public class HomeBannerDataBean extends ListBaseBean {
    private List<BannerBean> dataList;

    public List<BannerBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<BannerBean> dataList) {
        this.dataList = dataList;
    }

    public class BannerBean {
        private String id;
        private int objId;
        private int objType;
        private int dataType;
        private int bannerTypeId;
        private String title;
        private String desc;
        private String viewLink;
        private String coverPic;
        private int isNeedLoginData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getObjId() {
            return objId;
        }

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public int getObjType() {
            return objType;
        }

        public void setObjType(int objType) {
            this.objType = objType;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public int getBannerTypeId() {
            return bannerTypeId;
        }

        public void setBannerTypeId(int bannerTypeId) {
            this.bannerTypeId = bannerTypeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getViewLink() {
            return viewLink;
        }

        public void setViewLink(String viewLink) {
            this.viewLink = viewLink;
        }


        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public int getIsNeedLoginData() {
            return isNeedLoginData;
        }

        public void setIsNeedLoginData(int isNeedLoginData) {
            this.isNeedLoginData = isNeedLoginData;
        }
    }
}
