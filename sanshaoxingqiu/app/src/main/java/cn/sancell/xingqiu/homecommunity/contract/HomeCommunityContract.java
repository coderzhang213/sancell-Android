package cn.sancell.xingqiu.homecommunity.contract;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.im.entity.req.AddListReq;
import cn.sancell.xingqiu.im.ui.ImModel;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeCommunityContract {
    public interface HomeCommunityView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getRecommGroupListSuccess(List<RecommendGroupListBean.RecommGroupBean> recommGroupBeanList);

        //因为群组列表返回了，是否有直播数据，
        void onRecommGroupGetLiveListData(int sum);

        void getVideoListSuccess(CommunityVideoListBean videoBeanList, int page);

        void getAdList(HomeBannerDataBean res);

    }

    public static class HomeCommunityPresenter extends BasePresenter<HomeCommunityContract.HomeCommunityView> {

        /**
         * 推荐群组
         */
        public void GetRecommGroupListList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getCommunityRecommGroupListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<RecommendGroupListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<RecommendGroupListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {

                                if (t.getRetData() != null) {
                                    getView().getRecommGroupListSuccess(t.getRetData().getDataList());
                                    getView().onRecommGroupGetLiveListData(t.getRetData().getLiveCount());
                                } else {
                                    getView().onRecommGroupGetLiveListData(0);

                                }
                            } else {
                                getView().onRecommGroupGetLiveListData(0);
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().onRecommGroupGetLiveListData(0);
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().netWorkError();
                                    getView().toast("暂无网络");
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }


        /**
         * 视频列表
         */
        public void GetVideoList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("type", "1");
            RetrofitUtil.getInstance().initRetrofit().getCommunityVideoListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<CommunityVideoListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<CommunityVideoListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getVideoListSuccess(t.getRetData(), page);
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
         * 广告位
         *
         * @param context
         * @param req
         */
        public void getAdList(Context context, AddListReq req) {
            ImModel.getInstance().getAddList(req).compose(RxUtils.transform(getView()))
                    .subscribe(new BaseObserver<HomeBannerDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeBannerDataBean> t) throws Exception {
                            if (getView() != null) {
                                if (t.getRetCode() == 0) {
                                    getView().getAdList(t.getRetData());
                                } else {
                                    getView().toast(t.getRetMsg());
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().toast(e.toString());
                            }
                        }
                    });
        }


    }
}
