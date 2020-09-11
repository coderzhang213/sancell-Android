package cn.sancell.xingqiu.im.ui.red.RpDetail;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;

public interface RpDetailView extends BaseView {

    void getDetailSuccess(RpDetailRes res);

    void getDetailError(String error);

    void getYueSuccess(YueRes res);

    void getYueError(String error);
}
