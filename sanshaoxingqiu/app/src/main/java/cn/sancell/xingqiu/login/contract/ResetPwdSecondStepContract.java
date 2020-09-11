package cn.sancell.xingqiu.login.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/25.
 */

public interface ResetPwdSecondStepContract {
    interface ResetPwdSecondView extends BaseView {
        void toast(String text);

        void resetPwdSuccess();
    }

    class ResetPwdSecondPresenter extends BasePresenter<ResetPwdSecondStepContract.ResetPwdSecondView> {
        /**
         * 重置密码
         *
         * @param mobile
         * @param smsCode
         */
        public void ResetPwdFirst(String mobile, String smsCode, String password, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("mobile", mobile);
            map.put("smsCode", smsCode);
            map.put("password", password);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("reqTime", reqTime);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("password", RSAUtils.encryptByPublic(password));
            RetrofitUtil.getInstance().initRetrofit().resetPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                getView().resetPwdSuccess();
                            } else {
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
