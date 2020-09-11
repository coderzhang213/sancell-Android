package cn.sancell.xingqiu.homecommunity.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
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

public class RecommendGroupListContract {
    public interface RecommendGroupListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getRecommGroupListSuccess(RecommendGroupListBean recommGroupBeanList, int page);

        void getAdList(HomeBannerDataBean data);

    }

    public static class RecommendGroupListPresenter extends BasePresenter<RecommendGroupListContract.RecommendGroupListView> {

        /**
         * 推荐群组列表
         */
        public void GetRecommGroupListList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("page", page + "");
            map.put("pageSize", "10");
            RetrofitUtil.getInstance().initRetrofit().getRecommGroupListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<RecommendGroupListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<RecommendGroupListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getRecommGroupListSuccess(t.getRetData(),page);
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

        public void getAdList(Context context,AddListReq req){
            ImModel.getInstance().getAddList(req).compose(RxUtils.transform(getView()))
                    .subscribe(new BaseObserver<HomeBannerDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeBannerDataBean> t) throws Exception {
                            if (getView() != null){
                                if (t.getRetCode() == 0){
                                    getView().getAdList(t.getRetData());
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
