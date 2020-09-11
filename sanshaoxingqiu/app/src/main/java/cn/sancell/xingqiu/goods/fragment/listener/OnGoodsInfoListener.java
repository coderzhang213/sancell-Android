package cn.sancell.xingqiu.goods.fragment.listener;

import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

public interface OnGoodsInfoListener {

    void onScrollChange(float alpha); //滑动渐变

    void showShareBtn(boolean canShare); //是否可分享

    void getGoodsInfo(ProductInfoDataBean data);

    void showCartTip(String str); //购物车提示

    void updateAddress(AddressListDataBean.AddressItemBean item); //更新地址

    void callHospital(String phone);


}
