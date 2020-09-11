package cn.sancell.xingqiu.im.ui.red;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.req.SendRpReq;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.PayPassRes;
import cn.sancell.xingqiu.im.entity.res.SendRpRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;

public interface SendRedView extends BaseView {

    void sendRpSuccess(SendRpRes res, SendRpReq req);

    void sendRpError(String str);

    void checkPassSuccess(PayPassRes res);

    void checkPassError(String error);

    void getYueSuccess(YueRes res);

    void getYueError(String error);
}
