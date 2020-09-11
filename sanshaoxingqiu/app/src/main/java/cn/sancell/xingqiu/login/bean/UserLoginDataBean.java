package cn.sancell.xingqiu.login.bean;

import cn.sancell.xingqiu.base.base.BaseBean;

/**
 * Created by ai11 on 2019/6/25.
 */

public class UserLoginDataBean extends BaseBean {
    private String skey;
    private UserBean user;
    private int newUser;
    public String yunxin_accid;
    public String yunxin_token;
    public String yunxin_name;
    public int isBandingMobile; //微信登陆是否绑定手机号


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
