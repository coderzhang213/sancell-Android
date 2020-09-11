package cn.sancell.xingqiu.order.pin;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.bean.OrderVoucherRes;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.req.GoodsAreaReq;
import cn.sancell.xingqiu.homeshoppingcar.bean.res.GoodAreaRes;
import cn.sancell.xingqiu.order.OrderModel;
import cn.sancell.xingqiu.order.entity.req.GoodVoucherReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderNewReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderReq;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.usermember.model.UserModel;

/**
 * @author Alan_Xiong
 * @desc: 拼团
 * @time 2020-01-02 10:35
 */
public class PinOrderPresenter extends BasePresenter<PinOrderView> {

    public Context mContext;

    public PinOrderPresenter(Context context) {
        mContext = context;
    }

    public void getDefaultAddress() {
        OrderModel.getInstance().getDefaultAddress(new BaseReq()).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<CreateOrderDefaultPreBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<CreateOrderDefaultPreBean> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().getDefaultAddressSuccess(t.getRetData());
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    /**
     * 商品支持配送区域
     *
     * @param context
     * @param req
     */
    public void getGoodsArea(Context context, GoodsAreaReq req) {
        UserModel.getInstance().getGoodsArea(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<GoodAreaRes>(context) {
                    @Override
                    protected void onSuccess(BaseEntry<GoodAreaRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().getGoodsAreaSuccess(t.getRetData().goodsRegion);
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    /**
     * 创建订单
     *
     * @param req
     */
    public void createPinOrder(PinOrderReq req) {
        OrderModel.getInstance().createPinOrder(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<OrderRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<OrderRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().createPinOrderSuccess(t.getRetData());
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    public void getVoucherInfo(GoodVoucherReq req) {
        OrderModel.getInstance().getVoucherInfo(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<OrderVoucherRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<OrderVoucherRes> t) throws Exception {
                        if (getView() != null) {
                            getView().getVoucherInfoSuccess(t.getRetData(),req.onlyCount);
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getVoucherInfoError(e.toString());
                        }
                    }

                });
    }

    public void createNewPinOrder(PinOrderNewReq req) {
        OrderModel.getInstance().createPinNewOrder(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<OrderRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<OrderRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().createPinOrderSuccess(t.getRetData());
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    public void getUserMember(){
        UserModel.getInstance().getUserMember().compose(RxUtils.transformerWithLoading(getView()))
                .safeSubscribe(new BaseObserver<UserMemberRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<UserMemberRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().getUserMemberSuccess(t.getRetData());
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

}
