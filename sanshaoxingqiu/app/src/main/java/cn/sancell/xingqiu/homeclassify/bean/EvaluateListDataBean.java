package cn.sancell.xingqiu.homeclassify.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/7/17.
 */

public class EvaluateListDataBean extends ListBaseBean {
    private List<EvaluateBean> dataList;

    public List<EvaluateBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<EvaluateBean> dataList) {
        this.dataList = dataList;
    }

    public static class EvaluateBean {
        private int id;
        private int goodsId;
        private String publishTime;
        private int score;
        private String content;
        private List<PicArr> detailPicArr;
        private User user;

        public EvaluateBean() {

        }

        public class PicArr {
            private String coverPic;

            public String getCoverPic() {
                return coverPic;
            }

            public void setCoverPic(String coverPic) {
                this.coverPic = coverPic;
            }
        }

        public class User {
            private String gravatar;
            private String nickName;
            private int realMemberLevel;
            private String nickNameFormatting;

            public String getNickNameFormatting() {
                return nickNameFormatting;
            }

            public void setNickNameFormatting(String nickNameFormatting) {
                this.nickNameFormatting = nickNameFormatting;
            }

            public String getGravatar() {
                return gravatar;
            }

            public void setGravatar(String gravatar) {
                this.gravatar = gravatar;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getRealMemberLevel() {
                return realMemberLevel;
            }

            public void setRealMemberLevel(int realMemberLevel) {
                this.realMemberLevel = realMemberLevel;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<PicArr> getDetailPicArr() {
            return detailPicArr;
        }

        public void setDetailPicArr(List<PicArr> detailPicArr) {
            this.detailPicArr = detailPicArr;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
