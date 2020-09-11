package cn.sancell.xingqiu.homeclassify.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.ScreeningInfoEntity;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ClassifyThirdFragmentContract {
    public interface ClassifyThirdListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getClassifyThirdList(HomePageLikeListDataBean.LikeListDataBean dataLike, int page);
    }

    public static class ClassifyThirdFragmentPresenter extends BasePresenter<ClassifyThirdFragmentContract.ClassifyThirdListView> {
        /**
         * 三级分类中商品列表
         *
         * @param screeningInfoEntity
         */
        public void GetClassifyThirdList(int page, ScreeningInfoEntity screeningInfoEntity, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("goodsTypeOne", screeningInfoEntity.getFirst_id() + "");
            map.put("goodsTypeTwo", screeningInfoEntity.getSecond_id() + "");
            map.put("goodsTypeThree", screeningInfoEntity.getThird_classify_id());
            if (!StringUtils.isTextEmpty(screeningInfoEntity.getEdit_highest_price())) {
                map.put("maxPrice", screeningInfoEntity.getEdit_highest_price());
            }
            if (!StringUtils.isTextEmpty(screeningInfoEntity.getEdit_lowest_price())) {
                map.put("minPrice", screeningInfoEntity.getEdit_lowest_price());
            }
            if (!screeningInfoEntity.getSort_general().equals("-1")) {
                map.put("orderByType", screeningInfoEntity.getSort_general());
            }
            if (!screeningInfoEntity.getSort_price().equals("-1")) {
                map.put("orderByType", screeningInfoEntity.getSort_price());
            }
            if (!screeningInfoEntity.getSort_sales().equals("-1")) {
                map.put("orderByType", screeningInfoEntity.getSort_sales());
            }
            if(!StringUtils.isTextEmpty(screeningInfoEntity.getScreening_mark_ids())){
                map.put("tagId", screeningInfoEntity.getScreening_mark_ids());
            }
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
                                    getView().getClassifyThirdList(t.getRetData(), page);
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
                                    if (page == 1) {
                                        getView().netWorkError();
                                    } else {
                                        getView().toast(context.getResources().getString(R.string.error_network));
                                    }
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

    }
}
