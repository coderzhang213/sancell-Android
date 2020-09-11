package cn.sancell.xingqiu.im.ui.red.RpDetail;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.RpDetailReq;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.im.ui.ImModel;

public class RpDetailPresenter extends BasePresenter<RpDetailView> {

    public Context mContext;
    RpDetailPresenter(Context context){
        mContext = context;
    }


    public void getDetail(RpDetailReq req){
        ImModel.getInstance().getRpDetails(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<RpDetailRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<RpDetailRes> t) throws Exception {
                        if(getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getDetailSuccess(t.getRetData());
                            }else{
                                getView().getDetailError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getDetailError(e.toString());
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
