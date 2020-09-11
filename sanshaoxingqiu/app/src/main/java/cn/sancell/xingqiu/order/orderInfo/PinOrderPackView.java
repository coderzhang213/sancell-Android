package cn.sancell.xingqiu.order.orderInfo;

import java.util.List;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.order.entity.res.PinOrderPackRes;

public interface PinOrderPackView extends BaseView {

     void getPackInfoSuccess(PinOrderPackRes res);

     void getError(String error);

     void pinCancelOrderSuccess();
}
