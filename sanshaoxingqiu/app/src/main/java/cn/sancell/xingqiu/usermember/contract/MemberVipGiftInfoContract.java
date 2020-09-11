package cn.sancell.xingqiu.usermember.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeclassify.contract.ProductInfoContract;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberVipGiftInfoContract {
    public interface MemberVipGiftInfoView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getGiftInfoSuccess(ProductInfoDataBean productInfoDataBean);

    }

    public static class MemberVipGiftInfoPresenter extends BasePresenter<MemberVipGiftInfoContract.MemberVipGiftInfoView> {
        /**
         * 礼包详情
         *
         * @param objId
         */
        public void GetGiftInfo(String objId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("objId", objId);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getProductInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ProductInfoDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<ProductInfoDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getGiftInfoSuccess(t.getRetData());
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
