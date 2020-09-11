package cn.sancell.xingqiu.homeuser.afterSale.address;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressRegInfo;

public interface ApplyModifyAddressView extends BaseView {

    void getAddressListSuccess(AddressRegInfo res);

    void getAddressListError(String error);

    void getModifySuccess();

    void getModifyError(String error);
}
