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
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/15.
 */

public class MemberVipGiftListContract {
    public interface MemberVipGiftListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getLimitTimeGiftListSuccess(int page,MemberLimitTimeGiftListBean memberLimitTimeGiftListBean);

        void getMemberVipGiftListSuccess(int page, HomePageLikeListDataBean.LikeListDataBean listDataBean);
    }

    public static class MemberVipGiftListPresenter extends BasePresenter<MemberVipGiftListContract.MemberVipGiftListView> {


        /**
         * 全部商品列表（礼包列表）
         */
        public void GetMemberVipGiftList(int page, String goodsGiftLevel, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(reqTime + skey));
            map.put("goodsGiftLevel", goodsGiftLevel);
            map.put("goodsFlag", "2");
            map.put("page", page + "");
            map.put("pageSize", "10");

            RetrofitUtil.getInstance().initRetrofit().getThirdClassifyListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomePageLikeListDataBean.LikeListDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomePageLikeListDataBean.LikeListDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData() != null) {
                                        getView().getMemberVipGiftListSuccess(page, t.getRetData());
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
                                    if (isNetWorkError) {
                                        getView().toast(context.getResources().getString(R.string.error_network));
                                    } else {
                                        getView().toast(e.getMessage());
                                    }
                                }
                            }
                        }
                    });
        }

        /**
         * 限时（礼包列表）
         */
        public void GetMemberLimitTimeGiftList(int page,String memberLevel, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("memberLevel", memberLevel);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", page + "");
            map.put("pageSize", "10");

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
                                        getView().getLimitTimeGiftListSuccess(page,t.getRetData());
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
