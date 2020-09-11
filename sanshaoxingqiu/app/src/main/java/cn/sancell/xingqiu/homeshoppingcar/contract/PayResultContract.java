package cn.sancell.xingqiu.homeshoppingcar.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.order.OrderModel;
import cn.sancell.xingqiu.order.entity.req.PinOrderPackageReq;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.usermember.model.UserModel;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/2.
 */

public class PayResultContract {
    public interface PayResultView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getOrderInfoSuccess(OderInfoDataBean oderInfoDataBean);

        void confirmSuccess();

        void getUserMemberSuccess(UserMemberRes res);

    }

    public static class PayResultPresenter extends BasePresenter<PayResultContract.PayResultView> {

        /**
         * 获取订单相关信息
         */
        public void GetOrderInfo(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("orderId", orderId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getOrderInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OderInfoDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OderInfoDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getOrderInfoSuccess(t.getRetData());
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().netWorkError();
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 确认支付信息
         */
        public void ConfirmPayResult(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("clientId", "3");
            map.put("orderId", orderId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().checkPayInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().confirmSuccess();
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().netWorkError();
                                } else {
                                    getView().confirmSuccess();
                                }
                            }
                        }
                    });
        }

        public void getPayUserMember(Context context) {
            UserModel.getInstance().getUserMember().compose(RxUtils.transformerWithLoading(getView()))
                    .safeSubscribe(new BaseObserver<UserMemberRes>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserMemberRes> t) throws Exception {
                            if (getView() != null) {
                                if (t.getRetCode() == 0) {
                                    getView().getUserMemberSuccess(t.getRetData());
                                } else {
                                    getView().toast(t.getRetMsg());
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().toast(e.toString());
                            }
                        }
                    });
        }


    }
}
