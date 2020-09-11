package cn.sancell.xingqiu.homepage.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.HomeActiveInfoBean;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageHotSellDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageRecommDataBean;
import cn.sancell.xingqiu.homepage.bean.HomepageSeckillInfoBean;
import cn.sancell.xingqiu.homeuser.HomeModel;
import cn.sancell.xingqiu.homeuser.bean.res.ActivityConfigRes;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/6.
 */

public class HomePageContract {
    public interface HomePageView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getRecommListSuccess(HomePageRecommDataBean dataRecomm);

        void getHotSellListSuccess(HomePageHotSellDataBean dataHotSell);

        void getBannerListSuccess(List<HomeBannerDataBean.BannerBean> dataList);

        void getActiveInfoSuccess(HomeActiveInfoBean homeActiveInfoBean);

        void getSeckillInfoSuccess(HomepageSeckillInfoBean.SeckillRoundInfo seckillRoundInfo);

        void getLikeListSuccess(HomePageLikeListDataBean dataLike, int page);

        void getActivityConfigSuccess(ActivityConfigRes res);
    }

    public static class HomePagePresenter extends BasePresenter<HomePageContract.HomePageView> {

        /**
         * banner
         */
        public void GetBannerList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("bannerAdType", "1");
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getHomeBannerData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomeBannerDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeBannerDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getBannerListSuccess(t.getRetData().getDataList());
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
         * 精品推荐
         */
        public void GetRecommList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getHomeRecommData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomePageRecommDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomePageRecommDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getRecommListSuccess(t.getRetData());
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
         * 热销排行
         */
        public void GetHotSellList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getHomeHotSellData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomePageHotSellDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomePageHotSellDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getHotSellListSuccess(t.getRetData());
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
         * 首页相关活动（金刚区，秒杀，头条）
         */
        public void GetActiveInfo(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getHomeActiveInfoData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomeActiveInfoBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeActiveInfoBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getActiveInfoSuccess(t.getRetData());
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
         * 首页相关活动（秒杀）
         */
        public void GetSeckillInfo(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getHomeSeckillInfoData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomepageSeckillInfoBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomepageSeckillInfoBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getSeckillInfoSuccess(t.getRetData().getSeckillRoundInfo());
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
         * 猜你喜欢
         */
        public void GetLikeList(int page, final Context context) {
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
         * 活动配置
         * @param context
         */
        public void getActivityConfig(Context context){
            HomeModel.getInstance().getActivityConfig(new BaseReq()).compose(RxUtils.transform(getView()))
            .subscribe(new BaseObserver<ActivityConfigRes>(context) {
                @Override
                protected void onSuccess(BaseEntry<ActivityConfigRes> t) throws Exception {
                    if (getView() != null){
                        if (t.getRetCode() == 0){
                            getView().getActivityConfigSuccess(t.getRetData());
                        }else{
                            getView().toast(t.getRetMsg());
                        }
                    }
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                    if (getView() != null){
                        getView().toast(e.toString());
                    }
                }
            });
        }


    }
}
