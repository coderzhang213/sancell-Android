package cn.sancell.xingqiu.usermember.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.usermember.bean.MemberVIPPrivilegeBean;
import cn.sancell.xingqiu.usermember.bean.MemberVipGiftBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberVIPGiftBuyContract {
    public interface MemberVIPGiftBuyView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getVipGiftListSuccess(MemberVipGiftBean memberVipGiftBean);
        void getVipPrivilegeListSuccess(MemberVIPPrivilegeBean memberVIPPrivilegeBean);

        void getLimitTimeGiftListSuccess(List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data_limitList);
    }

    public static class MemberVIPGiftBuyPresenter extends BasePresenter<MemberVIPGiftBuyContract.MemberVIPGiftBuyView> {

        /**
         * 会员vip礼包列表
         */
        public void GetVipGiftList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", "1");
            map.put("pageSize", "5");
            RetrofitUtil.getInstance().initRetrofit().getMemberVipGiftList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberVipGiftBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberVipGiftBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getVipGiftListSuccess(t.getRetData());
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
                                    getView().netWorkError();
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 限时（礼包列表）
         */
        public void GetMemberLimitTimeGiftList(String memberLevel, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("memberLevel", memberLevel);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", "1");
            map.put("pageSize", "5");


            RetrofitUtil.getInstance().initRetrofit().getLimitTimeGiftListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberLimitTimeGiftListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberLimitTimeGiftListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData() != null) {
                                        getView().getLimitTimeGiftListSuccess(t.getRetData().getDataList());
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
                                    getView().netWorkError();
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 会员权益
         */
        public void GetMemberPrivilegeList(String memberLevel, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("memberLevel", memberLevel);
            map.put("hashToken", RSAUtils.encryptByPublic(map));

            RetrofitUtil.getInstance().initRetrofit().getPrivilegeListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberVIPPrivilegeBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberVIPPrivilegeBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData() != null) {
                                        getView().getVipPrivilegeListSuccess(t.getRetData());
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
