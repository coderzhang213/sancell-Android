package cn.sancell.xingqiu.login.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.login.bean.BindPhoneDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/25.
 */

public interface BindPhoneContract {
    interface BindPhoneView extends BaseView {
        void toast(String text);

        void bingInviteId();
        void getCodeSuccess();

        void bindSuccess();
    }

    class BindPhonePresenter extends BasePresenter<BindPhoneContract.BindPhoneView> {
        /**
         * 绑定手机号
         *
         * @param mobile
         * @param smsCode
         */
        public void BindPhone(String mobile, String smsCode,String openUnionId, String nickname,String gravatar,String gender,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String imei = AppUtils.getDeviceId(context);
            String reqTime = StringUtils.getCurrentTime();
            String skey=RSAUtils.md5(imei);
            map.put("mobile", mobile);
            map.put("smsCode", smsCode);
            map.put("openUnionId", openUnionId);
            map.put("nickname", nickname);
            map.put("tvUserGravatar", gravatar);
            map.put("gender", gender);
            map.put("clientId", "3");
            map.put("imei", imei);
            map.put("reqTime", reqTime);
            if(!StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""))){
                skey= PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            }
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("3"+gender+openUnionId+reqTime+skey+smsCode));
            RetrofitUtil.getInstance().initRetrofit().bindPhone(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<BindPhoneDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<BindPhoneDataBean> t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if(t.getRetCode()==0) {
                                if (t.getRetData() != null) {
                                    PreferencesUtils.put(Constants.Key.KEY_USERINFO, t.getRetData().getUser());
                                    PreferencesUtils.put(Constants.Key.KEY_SKEY, t.getRetData().getSkey());
                                    if (t.getRetData().getNewUser() == 2) {  //新注册用户
                                        getView().bingInviteId();
                                    } else {    //已注册用户
                                        getView().bindSuccess();
                                    }

                                }
                            }else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if(isNetWorkError){
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                }else {
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
                            }else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if(isNetWorkError){
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                }else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }


    }
}
