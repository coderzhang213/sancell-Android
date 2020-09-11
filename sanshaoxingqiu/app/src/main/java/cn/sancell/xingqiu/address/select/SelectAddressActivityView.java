package cn.sancell.xingqiu.address.select;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

public interface SelectAddressActivityView extends BaseView {

    void getAddressListSuccess(AddressListDataBean res);

    void getAddressError(String error);
}
