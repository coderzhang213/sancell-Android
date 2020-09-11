package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/6.
 */

public interface ModifyPayPwdNewPwdContract {
    interface ModifyPayPwdNewPwdView extends BaseView {
        void toast(String text);

        void modifyNewPwdSuccess();
        void modifyNewPwdFail(String errorTip);
    }

    class ModifyPayPwdNewPwdPresenter extends BasePresenter<ModifyPayPwdNewPwdContract.ModifyPayPwdNewPwdView> {
        /**
         * 修改支付密码
         *
         * @param tradePassword
         * @param newTradePassword
         */
        public void ModifyPayPwd(String tradePassword, String newTradePassword, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String imei = AppUtils.getDeviceId(context);
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("tradePassword", tradePassword);
            map.put("newTradePassword", newTradePassword);
            map.put("reqTime", reqTime);
            map.put("imei", imei);
            map.put("clientId", "3");
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("tradePassword", RSAUtils.encryptByPublic(tradePassword));
            map.put("newTradePassword", RSAUtils.encryptByPublic(newTradePassword));
            RetrofitUtil.getInstance().initRetrofit().modifyPayPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifyNewPwdSuccess();
                                }
                            } else {
                                //getView().toast(t.getRetMsg());
                                getView().modifyNewPwdFail(t.getRetMsg());
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
