package cn.sancell.xingqiu.login.bean;


import cn.sancell.xingqiu.util.StringUtils;

/**
 * @author liumingran
 * @ClassName: UserDataBean
 * @Description: 用户登录信息
 * @date 2018/4/23 上午9:57
 */
public class UserBean {
    private int userId;
    private String mobile;
    private String nickName;
    private String gravatar;
    private String genderStr;
    private int memberLevel;
    private String email;
    private String emailIsVerified;
    private String status;
    private int realMemberLevel;
    private String createAt;
    private String updateAt;
    private UserInfo userInfo;
    private String vipPeriodOfValidity;
    private int addPasswordStatus;
    private int addTransactionPasswdStatus;
    private String realMemberLevelStr;
    private long pointE2;
    private int freezePointE2;
    public int pointRollE2;
    private int isOldUser;
    private int inviteFromUid;
    private int economyMoneyE2;
    public int couponCount; //代金券数量
    private String isBindUser;//0 不需要显示 1 需要显示 是否显示绑定邀请人
    private Long vipEndTime = 0L;//VIP结束时间

    public Long getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(Long vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getIsBindUser() {
        return isBindUser;
    }

    public void setIsBindUser(String isBindUser) {
        this.isBindUser = isBindUser;
    }

    private MemberVipOrderParcelInfo memberVipOrderParcelInfo;

    public MemberVipOrderParcelInfo getMemberVipOrderParcelInfo() {
        return memberVipOrderParcelInfo;
    }

    public void setMemberVipOrderParcelInfo(MemberVipOrderParcelInfo memberVipOrderParcelInfo) {
        this.memberVipOrderParcelInfo = memberVipOrderParcelInfo;
    }

    public String getRealMemberLevelStr() {
        return realMemberLevelStr;
    }

    public void setRealMemberLevelStr(String realMemberLevelStr) {
        this.realMemberLevelStr = realMemberLevelStr;
    }

    public String getVipPeriodOfValidity() {
        return vipPeriodOfValidity;
    }

    public void setVipPeriodOfValidity(String vipPeriodOfValidity) {
        this.vipPeriodOfValidity = vipPeriodOfValidity;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public class UserInfo {
        private String birthday;
        private int gender;
        private String genderStr;


        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getGenderStr() {
            return genderStr;
        }

        public void setGenderStr(String genderStr) {
            this.genderStr = genderStr;
        }
    }

    public class MemberVipOrderParcelInfo {
        private String orderId;
        private String parcelId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getParcelId() {
            return parcelId;
        }

        public void setParcelId(String parcelId) {
            this.parcelId = parcelId;
        }
    }

    private MActivityOneDollarData mActivityOneDollarData;

    public MActivityOneDollarData getmActivityOneDollarData() {
        return mActivityOneDollarData;
    }

    public void setmActivityOneDollarData(MActivityOneDollarData mActivityOneDollarData) {
        this.mActivityOneDollarData = mActivityOneDollarData;
    }

    public class MActivityOneDollarData {
        private String title;
        private String desc;
        private String link;
        private String picTitle;
        private String logoUrl;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPicTitle() {
            return picTitle;
        }

        public void setPicTitle(String picTitle) {
            this.picTitle = picTitle;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }

    private MActivityData mActivityData;

    public MActivityData getmActivityData() {
        return mActivityData;
    }

    public void setmActivityData(MActivityData mActivityData) {
        this.mActivityData = mActivityData;
    }

    private MActivityData mActivityInviteData;

    public MActivityData getmActivityInviteData() {
        return mActivityInviteData;
    }

    public void setmActivityInviteData(MActivityData mActivityInviteData) {
        this.mActivityInviteData = mActivityInviteData;
    }

    public class MActivityData {
        private String title;
        private String desc;
        private String link;
        private String picTitle;
        private String codeUrl;
        private int isShow;

        public String getCodeUrl() {
            return codeUrl;
        }

        public void setCodeUrl(String codeUrl) {
            this.codeUrl = codeUrl;
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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPicTitle() {
            return picTitle;
        }

        public void setPicTitle(String picTitle) {
            this.picTitle = picTitle;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailIsVerified() {
        return emailIsVerified;
    }

    public void setEmailIsVerified(String emailIsVerified) {
        this.emailIsVerified = emailIsVerified;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRealMemberLevel() {
        return realMemberLevel;
    }

    public void setRealMemberLevel(int realMemberLevel) {
        this.realMemberLevel = realMemberLevel;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }


    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public int getAddPasswordStatus() {
        return addPasswordStatus;
    }

    public void setAddPasswordStatus(int addPasswordStatus) {
        this.addPasswordStatus = addPasswordStatus;
    }

    public int getAddPayPasswordStatus() {
        return addTransactionPasswdStatus;
    }

    public void setAddPayPasswordStatus(int addPayPasswordStatus) {
        this.addTransactionPasswdStatus = addPayPasswordStatus;
    }

    public long getPointE2() {
        return pointE2;
    }

    public void setPointE2(int pointE2) {
        this.pointE2 = pointE2;
    }

    public int getFreezePointE2() {
        return freezePointE2;
    }

    public void setFreezePointE2(int freezePointE2) {
        this.freezePointE2 = freezePointE2;
    }

    public int getIsOldUser() {
        return isOldUser;
    }

    public void setIsOldUser(int isOldUser) {
        this.isOldUser = isOldUser;
    }

    public int getInviteFromUid() {
        return inviteFromUid;
    }

    public void setInviteFromUid(int inviteFromUid) {
        this.inviteFromUid = inviteFromUid;
    }

    public int getEconomyMoneyE2() {
        return economyMoneyE2;
    }

    public void setEconomyMoneyE2(int economyMoneyE2) {
        this.economyMoneyE2 = economyMoneyE2;
    }
}
