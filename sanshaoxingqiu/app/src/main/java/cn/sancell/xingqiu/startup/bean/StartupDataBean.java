package cn.sancell.xingqiu.startup.bean;

import java.util.List;

import cn.sancell.xingqiu.homepage.bean.NavigationInfoBean;
import cn.sancell.xingqiu.login.bean.UserBean;

/**
 * Created by ai11 on 2019/5/8.
 */

public class StartupDataBean<T> {
    private boolean isLogin;
    private UserBean user;
    private String skey;
    private String mobileRule;
    private String passwordRule;
    private String addressVersion;
    private int seckillStatus;
    private AgreementUrlInfo agreementUrlInfo;
    private List<MemberAgreementUrlData> memberAgreementUrlData;

    private String defaultSearchName;
    private NavigationInfoBean navigationInfo;

    //医院信息
    public HospitalInfo institutionsInfo;

    public static class HospitalInfo {
        public String title;
        public String address;
        public String phone;
        public String link;
    }


    public List<MemberAgreementUrlData> getMemberAgreementUrlData() {
        return memberAgreementUrlData;
    }

    public void setMemberAgreementUrlData(List<MemberAgreementUrlData> memberAgreementUrlData) {
        this.memberAgreementUrlData = memberAgreementUrlData;
    }

    public NavigationInfoBean getNavigationInfo() {
        return navigationInfo;
    }

    public void setNavigationInfo(NavigationInfoBean navigationInfo) {
        this.navigationInfo = navigationInfo;
    }

    public class AgreementUrlInfo {
        private String memberAgreementUrl;
        private String userAgreementUrl;
        private String privacyAgreementUrl;
        private String integralRightsUrl;


        public String getMemberAgreementUrl() {
            return memberAgreementUrl;
        }

        public void setMemberAgreementUrl(String memberAgreementUrl) {
            this.memberAgreementUrl = memberAgreementUrl;
        }

        public String getUserAgreementUrl() {
            return userAgreementUrl;
        }

        public void setUserAgreementUrl(String userAgreementUrl) {
            this.userAgreementUrl = userAgreementUrl;
        }

        public String getPrivacyAgreementUrl() {
            return privacyAgreementUrl;
        }

        public void setPrivacyAgreementUrl(String privacyAgreementUrl) {
            this.privacyAgreementUrl = privacyAgreementUrl;
        }

        public String getIntegralRightsUrl() {
            return integralRightsUrl;
        }

        public void setIntegralRightsUrl(String integralRightsUrl) {
            this.integralRightsUrl = integralRightsUrl;
        }
    }

    public class MemberAgreementUrlData {
        private int memberLevelId;
        private String memberVipAgreementUrl;

        public int getMemberLevelId() {
            return memberLevelId;
        }

        public void setMemberLevelId(int memberLevelId) {
            this.memberLevelId = memberLevelId;
        }

        public String getMemberVipAgreementUrl() {
            return memberVipAgreementUrl;
        }

        public void setMemberVipAgreementUrl(String memberVipAgreementUrl) {
            this.memberVipAgreementUrl = memberVipAgreementUrl;
        }
    }

    public AgreementUrlInfo getAgreementUrlInfo() {
        return agreementUrlInfo;
    }

    public void setAgreementUrlInfo(AgreementUrlInfo agreementUrlInfo) {
        this.agreementUrlInfo = agreementUrlInfo;
    }

    public String getAddressVersion() {
        return addressVersion;
    }

    public void setAddressVersion(String addressVersion) {
        this.addressVersion = addressVersion;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    /*public UserDataBean getUser() {
        return user;
    }

    public void setUser(UserDataBean user) {
        this.user = user;
    }*/

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getMobileRule() {
        return mobileRule;
    }

    public void setMobileRule(String mobileRule) {
        this.mobileRule = mobileRule;
    }

    public String getPasswordRule() {
        return passwordRule;
    }

    public void setPasswordRule(String passwordRule) {
        this.passwordRule = passwordRule;
    }

    public String getKeyWord() {
        return defaultSearchName;
    }

    public void setKeyWord(String keyWord) {
        this.defaultSearchName = keyWord;
    }

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }
}
