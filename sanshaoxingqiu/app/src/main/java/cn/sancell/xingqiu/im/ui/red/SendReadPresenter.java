package cn.sancell.xingqiu.im.ui.red;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.req.SendRpReq;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.PayPassRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;
import cn.sancell.xingqiu.im.entity.res.SendRpRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.im.ui.ImModel;
import io.reactivex.Observer;
import io.reactivex.internal.observers.BlockingBaseObserver;

/**
 * @author Alan_Xiong
 * @desc: 发送红包
 * @time 2019-11-17 14:29
 */
public class SendReadPresenter extends BasePresenter<SendRedView> {

    private Context mContext;

    public SendReadPresenter(Context context) {
        mContext = context;
    }

    //发红包
    public void sendRp(SendRpReq req) {
        ImModel.getInstance().sendRp(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<SendRpRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<SendRpRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){

                                getView().sendRpSuccess(t.getRetData(),req);
                            }else{
                                getView().sendRpError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().sendRpError(e.toString());
                        }
                    }
                });

    }

    //检查支付密码
    public void checkPass(BaseReq req){
        ImModel.getInstance().checkPayPass(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<PayPassRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<PayPassRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().checkPassSuccess(t.getRetData());
                            }else{
                                getView().checkPassError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().checkPassError(e.toString());
                        }
                    }
                });
    }

    public void getRpYue(){
        ImModel.getInstance().getRpYue(new BaseReq()).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<YueRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<YueRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getYueSuccess(t.getRetData());
                            }else{
                                getView().getYueError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getYueError(e.toString());
                        }
                    }
                });
    }


}
