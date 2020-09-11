package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.LogisticsInfoBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/4.
 */

public class LogisticsInfoContract {
    public interface LogisticsInfoView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getLogisticsInfoSuccess(LogisticsInfoBean logisticsInfoBean);
    }

    public static class LogisticsInfoPresenter extends BasePresenter<LogisticsInfoContract.LogisticsInfoView> {
        /**
         * 物流信息
         */
        public void GetLogisticsInfo(String orderId, String parcelId, String courierNumber,String courierTag, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("orderId", orderId);
            map.put("parcelId", parcelId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("courierNumber",courierNumber);
            map.put("courierTag",courierTag);
            map.put("hashToken", RSAUtils.encryptByPublic(map));

            RetrofitUtil.getInstance().initRetrofit().getLogisticsInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<LogisticsInfoBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<LogisticsInfoBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getLogisticsInfoSuccess(t.getRetData());
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
