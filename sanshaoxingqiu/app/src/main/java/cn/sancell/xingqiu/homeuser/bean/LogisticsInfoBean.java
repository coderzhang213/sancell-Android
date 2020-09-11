package cn.sancell.xingqiu.homeuser.bean;

import com.umeng.socialize.media.Base;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;

/**
 * Created by ai11 on 2019/7/4.
 */

public class LogisticsInfoBean {
    private String nu;
    private String com;
    private String orderNumber;
    private List<InfoBean> data;

    public String getOrderNum() {
        return orderNumber;
    }

    public void setOrderNum(String orderNum) {
        this.orderNumber = orderNum;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public List<InfoBean> getData() {
        return data;
    }

    public void setData(List<InfoBean> data) {
        this.data = data;
    }

    public class InfoBean {
        private String time;
        private String context;
        private String status;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
