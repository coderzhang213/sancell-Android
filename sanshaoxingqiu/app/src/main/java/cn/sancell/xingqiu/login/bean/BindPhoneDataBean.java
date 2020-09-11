package cn.sancell.xingqiu.login.bean;

import cn.sancell.xingqiu.base.base.BaseBean;

/**
 * Created by ai11 on 2019/6/25.
 */

public class BindPhoneDataBean extends BaseBean {
    private int isBandingMobile;
    private String skey;
    private UserBean user;
    private int newUser;


    public int getIsBandingMobile() {
        return isBandingMobile;
    }

    public void setIsBandingMobile(int isBandingMobile) {
        this.isBandingMobile = isBandingMobile;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getNewUser() {
        return newUser;
    }

    public void setNewUser(int newUser) {
        this.newUser = newUser;
    }
}
