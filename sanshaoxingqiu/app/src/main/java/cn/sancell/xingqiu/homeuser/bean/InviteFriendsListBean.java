package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

public class InviteFriendsListBean {
    private InviteFromInfo inviteFromInfo;

    private int inviteCount;
    private String remark;
    private String isBindUser;//是否需要显示绑定按钮 0：不显示，1：显示

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsBindUser() {
        return isBindUser;
    }

    public void setIsBindUser(String isBindUser) {
        this.isBindUser = isBindUser;
    }

    private List<MyInviteInfo> myInviteInfo;

    public List<MyInviteInfo> getMyInviteInfo() {
        return myInviteInfo;
    }

    public void setMyInviteInfo(List<MyInviteInfo> myInviteInfo) {
        this.myInviteInfo = myInviteInfo;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public InviteFromInfo getInviteFromInfo() {
        return inviteFromInfo;
    }

    public void setInviteFromInfo(InviteFromInfo inviteFromInfo) {
        this.inviteFromInfo = inviteFromInfo;
    }

    public class InviteFromInfo {
        private String userId;
        private String nickName;
        private String mobile;
        private String gravatar;
        private int memberLevel;
        private String realMemberLevelStr;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGravatar() {
            return gravatar;
        }

        public void setGravatar(String gravatar) {
            this.gravatar = gravatar;
        }

        public int getMemberLevel() {
            return memberLevel;
        }

        public void setMemberLevel(int memberLevel) {
            this.memberLevel = memberLevel;
        }

        public String getRealMemberLevelStr() {
            return realMemberLevelStr;
        }

        public void setRealMemberLevelStr(String realMemberLevelStr) {
            this.realMemberLevelStr = realMemberLevelStr;
        }
    }

    public class MyInviteInfo {
        private String userId;
        private String nickName;
        private String mobile;
        private String gravatar;
        private int memberLevel;
        private String realMemberLevelStr;
        private String inviteTime;
        private int shareingTotal;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGravatar() {
            return gravatar;
        }

        public void setGravatar(String gravatar) {
            this.gravatar = gravatar;
        }

        public int getMemberLevel() {
            return memberLevel;
        }

        public void setMemberLevel(int memberLevel) {
            this.memberLevel = memberLevel;
        }

        public String getRealMemberLevelStr() {
            return realMemberLevelStr;
        }

        public void setRealMemberLevelStr(String realMemberLevelStr) {
            this.realMemberLevelStr = realMemberLevelStr;
        }

        public String getInviteTime() {
            return inviteTime;
        }

        public void setInviteTime(String inviteTime) {
            this.inviteTime = inviteTime;
        }

        public int getShareingTotal() {
            return shareingTotal;
        }

        public void setShareingTotal(int shareingTotal) {
            this.shareingTotal = shareingTotal;
        }
    }


}
