package cn.sancell.xingqiu.startup.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/5/8.
 */

public interface StartupContract {
    interface StartView extends BaseView {
        void toast(String text);

        void netWorkError();

        void alreadyLogin();

        void notLogin();
    }

    class StartupPresenter extends BasePresenter<StartupContract.StartView> {
        /**
         * 初始化接口
         */
        public void StartUp(Context context) {
            //getView().showLoading(true);
            String reqTime = StringUtils.getCurrentTime();
            String imei = AppUtils.getDeviceId(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("clientId", "3");
            map.put("version", AppUtils.getVersionName(context) + "");
            map.put("imei", imei);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, RSAUtils.md5(imei)));
            map.put("hashToken", RSAUtils.encryptByPublic(map));

            RetrofitUtil.getInstance().initRetrofit().startUp(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<StartupDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<StartupDataBean> t) throws Exception {
                            //getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData().isLogin()) {  //已登录
                                        PreferencesUtils.put(Constants.Key.KEY_USERINFO, t.getRetData().getUser());
                                        PreferencesUtils.put(Constants.Key.KEY_SKEY, t.getRetData().getSkey());
                                        getView().alreadyLogin();
                                    } else {
                                        getView().notLogin();
                                    }
                                    PreferencesUtils.put(Constants.Key.KEY_NAVIGATIONINFO, t.getRetData().getNavigationInfo());
                                    PreferencesUtils.put(Constants.Key.KEY_passwordRule, t.getRetData().getPasswordRule());
                                    PreferencesUtils.put(Constants.Key.KEY_mobileRule, t.getRetData().getMobileRule());
                                    PreferencesUtils.put(Constants.Key.KEY_memberAgreementUrlData, t.getRetData().getMemberAgreementUrlData());
                                    PreferencesUtils.put(Constants.Key.KEY_memberAgreementUrl, t.getRetData().getAgreementUrlInfo().getMemberAgreementUrl());
                                    PreferencesUtils.put(Constants.Key.KEY_userAgreementUrl, t.getRetData().getAgreementUrlInfo().getUserAgreementUrl());
                                    PreferencesUtils.put(Constants.Key.KEY_privacyAgreementUrl, t.getRetData().getAgreementUrlInfo().getPrivacyAgreementUrl());
                                    PreferencesUtils.put(Constants.Key.KEY_integralRightsUrl, t.getRetData().getAgreementUrlInfo().getIntegralRightsUrl());
                                    PreferencesUtils.put(Constants.Key.KEY_searchKeyWord, t.getRetData().getKeyWord());
                                    PreferencesUtils.put(Constants.Key.KEY_seckillStatus, t.getRetData().getSeckillStatus());
                                    PreferencesUtils.put(Constants.Key.HOSPITAL_INFO, t.getRetData().institutionsInfo);

                                }
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
    }
}
