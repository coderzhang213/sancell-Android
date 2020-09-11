package cn.sancell.xingqiu.homecommunity.live;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveListRes;

public interface HomeLiveView extends BaseView {

    void getLiveListSuccess(LiveData res);

    void getLiveListError(String error);
}
