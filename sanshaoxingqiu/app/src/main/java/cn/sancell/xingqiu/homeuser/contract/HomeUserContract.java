package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import com.netease.nim.uikit.common.util.string.StringUtil;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.LiveBaseStatusInfo;
import cn.sancell.xingqiu.bean.VerifyResultRes;
import cn.sancell.xingqiu.bean.VoucherCenterRes;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.UserOrderNumBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/5/14.
 */

public class HomeUserContract {
    public interface HomeUserView extends BaseView {
        void toast(String text);

        void getUserInfoSuccess();

        void getOrderNumSuccess(UserOrderNumBean userOrderNumBean);

        void getVerifyResult(VerifyResultRes res);

        void getLiveCheck(LiveBaseStatusInfo res);

    }

    public static class GetUserInfoPresenter extends BasePresenter<HomeUserContract.HomeUserView> {
        /**
         * 用户信息
         */
        public void GetUserInfo(final Context context) {
            //getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getUserInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserBean> t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    PreferencesUtils.put(Constants.Key.KEY_USERINFO, t.getRetData());
                                    getView().getUserInfoSuccess();
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().toast(e.getMessage());
                            }
                        }
                    });
        }

        /**
         * 用户不同订单数量
         */
        public void GetUserOrderNum(final Context context) {
            //getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getUserOrderNum(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserOrderNumBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserOrderNumBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderNumSuccess(t.getRetData());
                                }
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().toast(e.getMessage());
                            }
                        }
                    });
        }


        //直播认证状态
        public void checkVerifyStatus(Context context) {
            HashMap<String, String> maps = ConvertUtils.getRequest();
            RetrofitUtil.getInstance().initRetrofit().getLiveVerifyResult(maps)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<VerifyResultRes>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<VerifyResultRes> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getVerifyResult(t.getRetData());

                            } else {
                                getView().toast(t.getRetMsg());
                            }

                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().toast(e.getMessage());
                            }
                        }
                    });

        }

        //直播状态检查
        public void checkLiveStatus(Context context) {
            HashMap<String, String> maps = ConvertUtils.getRequest();
            RetrofitUtil.getInstance().initRetrofit().checkLive(maps)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<LiveBaseStatusInfo>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<LiveBaseStatusInfo> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getLiveCheck(t.getRetData());

                            } else {
                                getView().toast(t.getRetMsg());
                            }

                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().toast(e.getMessage());
                            }
                        }
                    });

        }

    }
}

