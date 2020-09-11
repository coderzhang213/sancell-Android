package cn.sancell.xingqiu.im.ui.red.empty;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;
import cn.sancell.xingqiu.im.ui.ImModel;

public class RedEmptyPresenter extends BasePresenter<RedEmptyView> {

    private Context mContext;

    public RedEmptyPresenter(Context context){
        mContext = context;
    }

    //抢红包
    public void rpGrab(RpGrabReq req){
        ImModel.getInstance().rpGrab(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<RpGrabRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<RpGrabRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().grabRpSuccess(t.getRetData());
                            }else{
                                getView().grabRpError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().grabRpError(e.toString());
                        }
                    }
                });
    }


    //检查红包有效性
    public void checkRp(RpGrabReq req){
        ImModel.getInstance().checkRp(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<CheckRpRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<CheckRpRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().checkRpSuccess(t.getRetData());
                            }else{
                                getView().checkRpError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().checkRpError(e.toString());
                        }
                    }
                });
    }


}
