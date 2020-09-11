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
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/5.
 */

public interface ModifyPayPwdCheckOldPwdContract {
    interface ModifyPayPwdCheckOldPwdView extends BaseView {
        void toast(String text);

        void checkOldPwdSuccess();

        void checkOldPwdFail(String errorTip);
    }

    class ModifyPayPwdCheckOldPwdPresenter extends BasePresenter<ModifyPayPwdCheckOldPwdContract.ModifyPayPwdCheckOldPwdView> {
        /**
         * 验证旧支付密码
         *
         * @param oldTradePassword
         */
        public void CheckOldPayPwd(String oldTradePassword, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String mobile = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getMobile();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("oldTradePassword", RSAUtils.encryptByPublic(oldTradePassword));
            map.put("mobile", mobile);
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(mobile + oldTradePassword + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().checkOldPayPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().checkOldPwdSuccess();
                                }
                            } else {
                                getView().checkOldPwdFail(t.getRetMsg());
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
