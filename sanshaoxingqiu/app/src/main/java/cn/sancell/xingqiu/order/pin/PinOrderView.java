package cn.sancell.xingqiu.order.pin;

import java.util.List;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.OrderVoucherRes;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;

public interface PinOrderView extends BaseView {

    void getDefaultAddressSuccess(CreateOrderDefaultPreBean res);

    void getError(String error);

    void getGoodsAreaSuccess(List<String> res);

    void createPinOrderSuccess(OrderRes order);

    void getVoucherInfoSuccess(OrderVoucherRes res,String type);

    void getVoucherInfoError(String msg);

    void getUserMemberSuccess(UserMemberRes res);
}
