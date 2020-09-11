package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeshoppingcar.bean.AlipayPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.WeiXinPayInfoBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/5.
 */

public class MemberPaymentContract {
    public interface MemberPaymentView extends BaseView {
        void toast(String text);

        void confirmSuccess();

        void getWeiXinInfoSuccess(WeiXinPayInfoBean weiXinPayInfoBean);

        void getAlipyInfoSuccess(AlipayPayInfoBean alipayPayInfoBean);

    }

    public static class MemberPaymentPresenter extends BasePresenter<MemberPaymentContract.MemberPaymentView> {

        /**
         * 获取微信支付信息
         */
        public void GetWeiXinInfo(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("clientId", "3");
            map.put("orderId", orderId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getWeiXinPayInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<WeiXinPayInfoBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<WeiXinPayInfoBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getWeiXinInfoSuccess(t.getRetData());
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 获取支付宝支付信息
         */
        public void GetAlipayInfo(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("clientId", "3");
            map.put("orderId", orderId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getAlipayPayInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<AlipayPayInfoBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<AlipayPayInfoBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getAlipyInfoSuccess(t.getRetData());
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
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
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().confirmSuccess();
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().confirmSuccess();
                               /* if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }*/
                            }
                        }
                    });
        }

    }
}
