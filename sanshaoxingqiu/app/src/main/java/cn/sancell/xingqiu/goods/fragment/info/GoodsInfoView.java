package cn.sancell.xingqiu.goods.fragment.info;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

public interface GoodsInfoView extends BaseView {

    void getGoodsInfoSuccess(ProductInfoDataBean res);

    void getInfoError(String error);

    void getAddressListSuccess(AddressListDataBean res);

    void getAddressListError(String error);
}

