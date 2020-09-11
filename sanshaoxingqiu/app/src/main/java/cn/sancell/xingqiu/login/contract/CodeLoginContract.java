package cn.sancell.xingqiu.login.contract;


import android.content.Context;

import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.ui.ImModel;
import cn.sancell.xingqiu.login.bean.UserLoginDataBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.SPManager;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public interface CodeLoginContract {
    interface LoginView extends BaseView {
        void toast(String text);

        void getCodeSuccess();

        void codeLoginSuccess(LoginInfo info);

        void bingInviteId();

        void noBindPhone();

        void getImLogin(ImAccountRes res);
    }

    class LoginPresenter extends BasePresenter<LoginView> {
        /**
         * 验证码登录
         *
         * @param mobile
         * @param smsCode
         */
        public void UserCodeLogin(String mobile, String smsCode, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String imei = AppUtils.getDeviceId(context);
            String reqTime = StringUtils.getCurrentTime();
            map.put("mobile", mobile);
            map.put("smsCode", smsCode);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, RSAUtils.md5(imei)));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().userCodeLogin(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserLoginDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserLoginDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {

                                    SPManager.getInstance().putLoginAccount(t.getRetData());
                                    if (t.getRetData().getNewUser() == 2) {  //新注册用户
                                        getView().bingInviteId();
                                    } else {    //已注册用户
                                        getView().codeLoginSuccess(new LoginInfo(t.getRetData().yunxin_accid,t.getRetData().yunxin_token));
                                    }
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
            map.put("reqCacheType", "10003");
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
                                getView().getCodeSuccess();
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
         * 微信登录
         *
         * @param openUnionId
         */
        public void UserWeiXinLogin(String openUnionId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String imei = AppUtils.getDeviceId(context);
            String reqTime = StringUtils.getCurrentTime();
            map.put("openUnionId", openUnionId);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, RSAUtils.md5(imei)));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().userWeiXinLogin(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserLoginDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserLoginDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData().isBandingMobile == 1) {  //绑定过手机
                                        SPManager.getInstance().putLoginAccount(t.getRetData());
                                        getView().codeLoginSuccess(new LoginInfo(t.getRetData().yunxin_accid,t.getRetData().yunxin_token));
                                    } else {
                                        getView().noBindPhone();
                                    }
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
         * im 如果登陆没注册，需要调用注册接口进行注册
         *
         * @param context
         */
        public void loginIm(Context context) {
            ImModel.getInstance().loginIm(new BaseReq()).compose(RxUtils.transform(getView()))
                    .subscribe(new BaseObserver<ImAccountRes>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<ImAccountRes> t) throws Exception {
                            if (getView() != null) {
                                if (t.getRetCode() == 0) {
                                    SPManager.getInstance().putImAccount(t.getRetData());
                                    getView().getImLogin(t.getRetData());
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
