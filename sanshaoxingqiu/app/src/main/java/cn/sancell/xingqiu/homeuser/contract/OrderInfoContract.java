package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OrderInfoContract {
    public interface OrderInfoView extends BaseView {
        void toast(String text);

        void netWorkError();

        void cancelOrderSuccess();

        void deleteOrderSuccess();

        void batchAddSuccess();

        void openInvoiceSuccess();

        void getOrderInfoSuccess(OderInfoDataBean oderInfoDataBean);

        void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason);
    }

    public static class OrderInfoPresenter extends BasePresenter<OrderInfoContract.OrderInfoView> {
        /**
         * 订单详情
         *
         * @param orderId
         */
        public void GetOrderInfo(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("orderId", orderId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getOrderInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OderInfoDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OderInfoDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderInfoSuccess(t.getRetData());
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
         * 获取取消订单原因
         */
        public void GetCancelReasonInfo(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getOrderCancelReason(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderCancelReasonListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderCancelReasonListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderReasonSuccess(t.getRetData().getDataList());
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
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 取消订单
         *
         * @param reasonId
         * @param orderId
         */
        public void CancelOrder(String orderId, String reasonId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("reasonId", reasonId);
            map.put("clientId", "3");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().cancelOrder(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().cancelOrderSuccess();
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 删除订单
         *
         * @param orderId
         */
        public void DeleteOrder(String orderId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("clientId", "3");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().deleteOrder(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().deleteOrderSuccess();
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 再次购买
         *
         * @param goodsIdString
         */
        public void BatchAddCar(String goodsIdString, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String carDetailToken = RSAUtils.md5(goodsIdString);
            map.put("goodsIdString", goodsIdString);
            map.put("carDetailToken", carDetailToken);
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(carDetailToken + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().batchAddCar(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().toast(context.getResources().getString(R.string.again_buy_success));
                                    getView().batchAddSuccess();
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
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }

        /**
         * 补开发票
         */
        public void InvoiceOpen(String orderId, String invoiceLookedUp, String companyName, String companyIdentifyNumber,
                                String mobile, String email, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("orderId", orderId);
            map.put("invoiceLookedUp", invoiceLookedUp);
            map.put("hashToken", RSAUtils.encryptByPublic(invoiceLookedUp + mobile + orderId + reqTime + skey));
            if (!StringUtils.isTextEmpty(companyName)) {
                map.put("companyName", companyName);
            }
            if (!StringUtils.isTextEmpty(companyIdentifyNumber)) {
                map.put("companyIdentifyNumber", companyIdentifyNumber);
            }
            map.put("mobile", mobile);
            if (!StringUtils.isTextEmpty(email)) {
                map.put("email", email);
            }
            RetrofitUtil.getInstance().initRetrofit().invoiceOpen(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().openInvoiceSuccess();
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null) {
                                getView().showLoading(false);
                                if (isNetWorkError) {
                                    getView().toast(context.getResources().getString(R.string.error_network));
                                } else {
                                    getView().toast(e.getMessage());
                                }
                            }
                        }
                    });
        }


    }
}
