package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

/**
 * Created by zj on 2019/12/16.
 */
public class AddressRegInfo {
    private List<AddressListDataBean.AddressItemBean> inRange;
    private List<AddressListDataBean.AddressItemBean> notInRange;

    public List<AddressListDataBean.AddressItemBean> getInRange() {
        return inRange;
    }

    public void setInRange(List<AddressListDataBean.AddressItemBean> inRange) {
        this.inRange = inRange;
    }

    public List<AddressListDataBean.AddressItemBean> getNotInRange() {
        return notInRange;
    }

    public void setNotInRange(List<AddressListDataBean.AddressItemBean> notInRange) {
        this.notInRange = notInRange;
    }
}
