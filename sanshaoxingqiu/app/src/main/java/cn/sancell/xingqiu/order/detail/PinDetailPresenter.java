package cn.sancell.xingqiu.order.detail;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.order.OrderModel;
import cn.sancell.xingqiu.order.entity.req.PinDetailReq;
import cn.sancell.xingqiu.order.entity.req.PinInviteReq;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;
import cn.sancell.xingqiu.order.entity.res.PinInviteRes;

public class PinDetailPresenter extends BasePresenter<PinDetailView> {

    private Context mContext;

    public PinDetailPresenter(Context context){
        mContext = context;
    }

    public void getPinOrderInfo(PinDetailReq req){

        OrderModel.getInstance().getPinDetailInfo(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<PinDetailRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<PinDetailRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getPInDetailInfoSuccess(t.getRetData());
                            }else{
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getError(e.toString());
                        }
                    }
                });
    }

//    public void getInviteInfo(PinInviteReq req){
//        OrderModel.getInstance().pinInviteUser(req).compose(RxUtils.transformerWithLoading(getView()))
//                .subscribe(new BaseObserver<PinInviteRes>(mContext) {
//                    @Override
//                    protected void onSuccess(BaseEntry<PinInviteRes> t) throws Exception {
//                        if (getView() != null){
//                            if (t.getRetCode() == 0){
//                                getView().getInviteInfoSuccess(t.getRetData());
//                            }else{
//                                getView().getError(t.getRetMsg());
//                            }
//                        }
//                    }
//
//                    @Override
//                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
//                        if (getView() != null){
//                            getView().getError(e.toString());
//                        }
//                    }
//                });
//    }
}
