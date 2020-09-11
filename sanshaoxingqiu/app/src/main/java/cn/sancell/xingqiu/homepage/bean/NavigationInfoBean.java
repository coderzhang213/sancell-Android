package cn.sancell.xingqiu.homepage.bean;

/**
 * Created by ai11 on 2019/7/26.
 */

public class NavigationInfoBean  {
    private PicObjInfo picObjInfo;
    private String webUrl;
    private int isShow;
    private int objId;
    private int objType;
    private int dataType;

    public PicObjInfo getPicObjInfo() {
        return picObjInfo;
    }

    public void setPicObjInfo(PicObjInfo picObjInfo) {
        this.picObjInfo = picObjInfo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
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

    public class PicObjInfo {
        private String coverPic;
        private int picWidth;
        private int picHeight;

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public int getPicWidth() {
            return picWidth;
        }

        public void setPicWidth(int picWidth) {
            this.picWidth = picWidth;
        }

        public int getPicHeight() {
            return picHeight;
        }

        public void setPicHeight(int picHeight) {
            this.picHeight = picHeight;
        }
    }
}
