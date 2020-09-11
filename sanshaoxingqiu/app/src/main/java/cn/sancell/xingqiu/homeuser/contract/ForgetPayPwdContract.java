package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/5.
 */

public interface ForgetPayPwdContract {
    interface ForgetPayPwdView extends BaseView {
        void toast(String text);

        void modifySuccess();
        void modifyFail(String errorTip);
    }

    class ForgetPayPwdPresenter extends BasePresenter<ForgetPayPwdContract.ForgetPayPwdView> {
        /**
         * 创建支付密码
         *
         * @param tradePassword
         * @param smsCode
         */
        public void CreatePayPwd(String tradePassword, String smsCode, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String imei = AppUtils.getDeviceId(context);
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("tradePassword", tradePassword);
            map.put("smsCode", smsCode);
            map.put("reqTime", reqTime);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("tradePassword", RSAUtils.encryptByPublic(tradePassword));
            RetrofitUtil.getInstance().initRetrofit().createPayPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifySuccess();
                                }
                            } else {
                                getView().modifyFail(t.getRetMsg());
                                //getView().toast(t.getRetMsg());
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
         * 忘记找回支付密码
         *
         * @param tradePassword
         * @param smsCode
         */
        public void ForgetFindPayPwd(String tradePassword, String smsCode, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String imei = AppUtils.getDeviceId(context);
            String mobile = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getMobile();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("tradePassword", tradePassword);
            map.put("smsCode", smsCode);
            map.put("reqTime", reqTime);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("skey", skey);
            map.put("mobile", mobile);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("tradePassword", RSAUtils.encryptByPublic(tradePassword));
            RetrofitUtil.getInstance().initRetrofit().findPayPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifySuccess();
                                }
                            } else {
                                getView().modifyFail(t.getRetMsg());
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


    }
}
