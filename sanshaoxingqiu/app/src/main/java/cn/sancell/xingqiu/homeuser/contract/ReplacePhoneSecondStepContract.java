package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.ReplacePhoneBean;
import cn.sancell.xingqiu.login.bean.BindPhoneDataBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/18.
 */

public interface ReplacePhoneSecondStepContract {
    interface ReplacePhoneSecondView extends BaseView {
        void toast(String text);

        void getCodeSuccess();

        void replaceSuccess(String skey);
    }

    class ReplacePhoneSecondPresenter extends BasePresenter<ReplacePhoneSecondStepContract.ReplacePhoneSecondView> {
        /**
         * 更换手机号
         *
         * @param mobile
         * @param smsCode
         */
        public void ReplacePhone(String mobile, String smsCode, String oldMobile, String oldSmsCode, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String imei = AppUtils.getDeviceId(context);
            String reqTime = StringUtils.getCurrentTime();
            String skey = RSAUtils.md5(imei);
            map.put("newMobile", mobile);
            map.put("newSmsCode", smsCode);
            map.put("oldMobile", oldMobile);
            map.put("oldSmsCode", oldSmsCode);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("reqTime", reqTime);
            if (!StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""))) {
                skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            }
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().replacePhone(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ReplacePhoneBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<ReplacePhoneBean> t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().replaceSuccess(t.getRetData().getSkey());
                                }
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
         * 获取短信验证码
         *
         * @param mobile
         */
        public void getCode(String mobile, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("mobile", mobile);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("reqCacheType", "10001");
            map.put("reqTime", reqTime);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getCode(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getCodeSuccess();
                                }
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


    }
}
