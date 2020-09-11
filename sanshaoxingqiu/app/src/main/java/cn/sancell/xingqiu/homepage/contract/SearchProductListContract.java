package cn.sancell.xingqiu.homepage.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/24.
 */

public class SearchProductListContract {
    public interface SearchProductListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void addHistroySuccess();

        void getSearchListSuccess(HomePageLikeListDataBean.LikeListDataBean likeListDataBean, int page);

        void getLikeListSuccess(HomePageLikeListDataBean dataLike, int page);

    }

    public static class SearchProductListPresenter extends BasePresenter<SearchProductListContract.SearchProductListView> {

        /**
         * 搜索商品列表
         */
        public void GetSearchProductList(int page, String keyWord, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("keyWord", keyWord);
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(reqTime + skey));
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
                                        getView().getSearchListSuccess(t.getRetData(), page);
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
                                if (isNetWorkError && page == 1) {
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
         * 猜你喜欢
         */
        public void GetLikeList(int page, final Context context) {
            if(true){//这个接口不在调用
                return;
            }

            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", page + "");
            map.put("pageSize", "10");
            RetrofitUtil.getInstance().initRetrofit().getLikeListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomePageLikeListDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomePageLikeListDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getLikeListSuccess(t.getRetData(), page);
                                }
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError && page == 1) {
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
         * 添加历史搜索
         */
        public void AddHistory(String name, final Context context, String moudleId) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("name", name != null ? name : "");
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(reqTime + skey));
            map.put("moudleId", moudleId);
            RetrofitUtil.getInstance().initRetrofit().addHistroy(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().addHistroySuccess();
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                getView().addHistroySuccess();
                            }
                        }
                    });
        }


    }
}
