package cn.sancell.xingqiu.homeshoppingcar.bean;

import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

public class CreateOrderDefaultPreBean  {
    private AddressListDataBean.AddressItemBean addressInfo;
    private long pointE2;

    public AddressListDataBean.AddressItemBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressListDataBean.AddressItemBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    public long getPointE2() {
        return pointE2;
    }

    public void setPointE2(int pointE2) {
        this.pointE2 = pointE2;
    }
}
