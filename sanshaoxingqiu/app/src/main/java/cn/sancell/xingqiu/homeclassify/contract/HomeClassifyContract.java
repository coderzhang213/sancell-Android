package cn.sancell.xingqiu.homeclassify.contract;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifyFirstDataBean;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifySecondDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifyContract {
    public interface HomeClassifyView extends BaseView {
        void toast(String text);
        void netWorkError();
        void getClassifySuccess(List<HomeClassifyFirstDataBean.ClassifyFirstListBean> classifyFirstListBeans);
        void getSecondThirdClassifySuccess(List<HomeClassifySecondDataBean.ClassifySecondBean> classifySecondBeans);
    }

    public static class HomeClassifyPresenter extends BasePresenter<HomeClassifyContract.HomeClassifyView> {
        /**
         * 一级分类
         */
        public void GetClassify(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getClassifyData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomeClassifyFirstDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeClassifyFirstDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getClassifySuccess(t.getRetData().getDataList());
                                }
                            }else {
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
         * 二三级分类
         */
        public void GetSecondThirdClassify(String parentId,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("parentId",parentId);
            RetrofitUtil.getInstance().initRetrofit().getSecondThirdClassifyData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomeClassifySecondDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeClassifySecondDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getSecondThirdClassifySuccess(t.getRetData().getDataList());
                                }
                            }else {
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
