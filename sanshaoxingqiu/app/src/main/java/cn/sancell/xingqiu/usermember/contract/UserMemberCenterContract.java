package cn.sancell.xingqiu.usermember.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.usermember.bean.MemberPrivilegeListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/3.
 */

public class UserMemberCenterContract {
    public interface MemberCenterView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getUserInfoSuccess();

        void getMemberPrivilegeSuccess(List<MemberPrivilegeListBean.MemberPrivilegeBean> dataList);

        void getMemberLevelListSuccess(List<MemberLevelListBean.MemberLevelBean> data_level);

    }

    public static class MemberCenterPresenter extends BasePresenter<UserMemberCenterContract.MemberCenterView> {

        /**
         * 用户信息
         */
        public void GetUserInfo(final Context context) {
            getView().showLoading(true);
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
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    PreferencesUtils.put(Constants.Key.KEY_USERINFO, t.getRetData());
                                    getView().getUserInfoSuccess();
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
        public void GetMemberPrivilege(String memberLevel,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("memberLevel",memberLevel);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getMemberPrivilege(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberPrivilegeListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberPrivilegeListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getMemberPrivilegeSuccess(t.getRetData().getDataList());
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
         * 会员等级信息
         */
        public void GetMemberLevelInfo(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getMemberLevelList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberLevelListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberLevelListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode()==0) {
                                if (t.getRetData()!=null){
                                    getView().getMemberLevelListSuccess(t.getRetData().getDataList());
                                    PreferencesUtils.put(Constants.Key.KEY_MEMBERLEVEL, t.getRetData());
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
