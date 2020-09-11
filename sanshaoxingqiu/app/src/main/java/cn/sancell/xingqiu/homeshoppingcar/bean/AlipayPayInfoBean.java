package cn.sancell.xingqiu.homeshoppingcar.bean;

import com.umeng.socialize.media.Base;

import cn.sancell.xingqiu.base.base.BaseBean;

/**
 * Created by ai11 on 2019/6/30.
 */

public class AlipayPayInfoBean extends BaseBean {
    private String alipaySign;
    private String alipaySignToken;

    public String getAlipaySign() {
        return alipaySign;
    }

    public void setAlipaySign(String alipaySign) {
        this.alipaySign = alipaySign;
    }

    public String getAlipaySignToken() {
        return alipaySignToken;
    }

    public void setAlipaySignToken(String alipaySignToken) {
        this.alipaySignToken = alipaySignToken;
    }
}
