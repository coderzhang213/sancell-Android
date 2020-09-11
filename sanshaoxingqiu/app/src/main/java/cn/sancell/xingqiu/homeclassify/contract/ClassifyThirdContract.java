package cn.sancell.xingqiu.homeclassify.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.ClassifyScreeningDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ClassifyThirdContract  {
    public interface ClassifyThirdView extends BaseView {
        void toast(String text);
        void netWorkError();
        void getClassifyScreeningListSuccess(List<ClassifyScreeningDataBean.ScreeningItemBean> data);
    }

    public static class ClassifyThirdPresenter extends BasePresenter<ClassifyThirdView> {
        /**
         * 三级分类中筛选中商品特色分类
         */
        public void GetClassifyScreeningList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getThirdClassifyScreeningData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ClassifyScreeningDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<ClassifyScreeningDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode()==0) {
                                if (t.getRetData() != null) {
                                    getView().getClassifyScreeningListSuccess(t.getRetData().getDataList());

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
