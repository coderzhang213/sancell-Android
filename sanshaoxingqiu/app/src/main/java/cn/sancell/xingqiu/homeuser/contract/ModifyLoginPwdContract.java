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
 * Created by ai11 on 2019/7/22.
 */

public interface ModifyLoginPwdContract {
    interface ModifyLoginPwdView extends BaseView {
        void toast(String text);

        void modifySuccess();
    }

    class ModifyLoginPwdPresenter extends BasePresenter<ModifyLoginPwdContract.ModifyLoginPwdView> {
        /**
         * 修改登录密码
         *
         * @param oldPassword
         * @param newPassword
         */
        public void ModifyPwd(String oldPassword, String newPassword, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("oldPassword", RSAUtils.encryptByPublic(oldPassword));
            map.put("newPassword", RSAUtils.encryptByPublic(newPassword));
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(newPassword + oldPassword + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().modifyLoginPwd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifySuccess();
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
