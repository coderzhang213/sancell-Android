package cn.sancell.xingqiu.interfaces;

import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

public interface OnPinOpListener {

    void onJoin(ProductInfoDataBean.PinGroupInfo.GroupRecommend item);

    void onPinEnded();
}
