package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

/**
 * Created by ai11 on 2019/9/25.
 */

public class MemberPrivilegeListBean {
    private List<MemberPrivilegeBean> dataList;

    public List<MemberPrivilegeBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MemberPrivilegeBean> dataList) {
        this.dataList = dataList;
    }

    public class MemberPrivilegeBean {
        private String coverPic;
        private CoverPicInstructionsData coverPicInstructionsData;

        public class CoverPicInstructionsData {
            private int picWidth;
            private int picHeight;
            private String coverPic;

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

            public String getCoverPic() {
                return coverPic;
            }

            public void setCoverPic(String coverPic) {
                this.coverPic = coverPic;
            }
        }

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public CoverPicInstructionsData getCoverPicInstructionsData() {
            return coverPicInstructionsData;
        }

        public void setCoverPicInstructionsData(CoverPicInstructionsData coverPicInstructionsData) {
            this.coverPicInstructionsData = coverPicInstructionsData;
        }
    }

}
