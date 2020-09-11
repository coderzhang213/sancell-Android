package cn.sancell.xingqiu.order.detail;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;
import cn.sancell.xingqiu.order.entity.res.PinInviteRes;

public interface PinDetailView extends BaseView {

    void getPInDetailInfoSuccess(PinDetailRes res);

    void getError(String error);

}
