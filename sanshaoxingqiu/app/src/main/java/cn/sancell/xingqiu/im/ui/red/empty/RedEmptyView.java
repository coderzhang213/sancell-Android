package cn.sancell.xingqiu.im.ui.red.empty;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;

public interface RedEmptyView extends BaseView {

    void checkRpSuccess(CheckRpRes res);

    void checkRpError(String str);

    void getRpDetailSuccess(RpDetailRes res);

    void getRpDetailError(String str);

    void grabRpSuccess(RpGrabRes res);

    void grabRpError(String str);

}
