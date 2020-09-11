package cn.sancell.xingqiu.order.orderInfo;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.order.OrderModel;
import cn.sancell.xingqiu.order.entity.req.PinCancelReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderPackageReq;
import cn.sancell.xingqiu.order.entity.res.PinOrderPackRes;

public class PinOrderPackPresenter extends BasePresenter<PinOrderPackView> {

    public Context mContext;

    public PinOrderPackPresenter(Context context){
        mContext = context;
    }

    /**
     * 包裹详情
     * @param req
     */
    public void getPinOrderInfo(PinOrderPackageReq req) {
        OrderModel.getInstance().getPinOrderDetail(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<PinOrderPackRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<PinOrderPackRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getPackInfoSuccess(t.getRetData());
                                }
                            } else {
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

    public void pinCancelOrder(PinCancelReq req){
        OrderModel.getInstance().pinOrderCancel(req).compose(RxUtils.transformerWithLoading(getView()))
        .subscribe(new BaseObserver<EmptyBean>(mContext) {
            @Override
            protected void onSuccess(BaseEntry<EmptyBean> t) throws Exception {
                if (getView() != null){
                    if (t.getRetCode() == 0){
                        getView().pinCancelOrderSuccess();
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


}
