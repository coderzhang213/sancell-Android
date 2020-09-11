package cn.sancell.xingqiu.homeshoppingcar.bean;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

/**
 * Created by ai11 on 2019/7/2.
 */

public class CreateOrderBean extends BaseBean {
    private String orderId;
    private String orderParcelId;
    private AddressListDataBean.AddressItemBean addressInfo;

    public String getOrderParcelId() {
        return orderParcelId;
    }

    public void setOrderParcelId(String orderParcelId) {
        this.orderParcelId = orderParcelId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public AddressListDataBean.AddressItemBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressListDataBean.AddressItemBean addressInfo) {
        this.addressInfo = addressInfo;
    }
}
