package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListNoPayListDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.bean.req.OrderListReq;
import cn.sancell.xingqiu.usermember.model.UserModel;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/20.
 */

public class UserOrderListContract {
    public interface UserOrderListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason, int pos);

        void getOrderAllListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount);

        void getOrderNoPayListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount);

        void getOrderUndeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount);

        void getOrderDeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount);

        void getOrderFinishedListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount);

        void cancelOrderSuccess();

        void confirmReceiptSuccess();

        void deleteOrderSuccess();

        void batchAddSuccess();
    }

    public static class OrderListFragmentPresenter extends BasePresenter<UserOrderListContract.UserOrderListView> {
        /**
         * 全部订单列表
         *
         * @param page
         */
        public void GetAllOrderList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("orderState", "0");
            map.put("payState", "0");
            map.put("confirmState", "0");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("0" + "0" + "0" + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getOrderAllList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderAllListDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    List<OrderAllListNoPayListDataBean> data = getResetData(t.getRetData().getDataList());
                                    getView().getOrderAllListSuccess(data, page, t.getRetData().getDataCount(), t.getRetData().getDataList().size());
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

        public void getOrderListType(Context context, OrderListReq req) {
            UserModel.getInstance().getOrderList(req).compose(RxUtils.transformerWithLoading(getView()))
                    .subscribe(new BaseObserver<OrderAllListDataBean.OrderAllBean.OrderPackListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean> t) throws Exception {
                            if (getView() != null) {
                                if (t.getRetCode() == 0) {
                                    if (req.orderStatus.equals("2")) {
                                        getView().getOrderUndeliveredListSuccess(getResetOtherUnDeliveredData(t.getRetData().getDataList()), Integer.parseInt(req.page), t.getRetData().getDataCount());
                                    } else if (req.orderStatus.equals("4")) {
                                        getView().getOrderDeliveredListSuccess(getResetOtherFinishedData(t.getRetData().getDataList()), Integer.parseInt(req.page), t.getRetData().getDataCount());

                                    } else if (req.orderStatus.equals("5")) {
                                        getView().getOrderFinishedListSuccess(getResetOtherFinishedData(t.getRetData().getDataList()), Integer.parseInt(req.page), t.getRetData().getDataCount());

                                    }
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


        /**
         * 待付款订单列表
         */
        public void GetOrderNoPayList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("orderState", "1");
            map.put("payState", "1");
            map.put("confirmState", "2");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("2" + "1" + "1" + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getOrderNoPayList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderAllListDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    List<OrderAllListNoPayListDataBean> data = getResetData(t.getRetData().getDataList());
                                    getView().getOrderNoPayListSuccess(data, page, t.getRetData().getDataCount(), t.getRetData().getDataList().size());
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


        /**
         * 待发货订单列表
         *
         * @param page
         */
        public void GetOrderUndeliveredList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("deliveryState", "1");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("1" + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getOrderUndeliveredList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderAllListDataBean.OrderAllBean.OrderPackListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderUndeliveredListSuccess(getResetOtherUnDeliveredData(t.getRetData().getDataList()), page, t.getRetData().getDataCount());
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


        /**
         * 已发货订单列表
         *
         * @param page
         */
        public void GetOrderDeliveredList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("deliveryState", "2");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("2" + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getOrderDeliveredList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderAllListDataBean.OrderAllBean.OrderPackListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderDeliveredListSuccess(getResetOtherDeliveredData(t.getRetData().getDataList(), Constants.OrderShowStatus.KEY_delivered), page, t.getRetData().getDataCount());
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

        /**
         * 完成订单列表
         *
         * @param page
         */
        public void GetOrderFinishedList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("deliveryState", "3");
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic("3" + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getOrderFinishedList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderAllListDataBean.OrderAllBean.OrderPackListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOrderFinishedListSuccess(getResetOtherFinishedData(t.getRetData().getDataList()), page, t.getRetData().getDataCount());

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
         * 确认收货
         *
         * @param parcelId
         * @param orderId
         */
        public void ConfirmReceipt(String orderId, String parcelId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("parcelId", parcelId);
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().confirmReceipt(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().confirmReceiptSuccess();
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
         * 获取取消订单原因
         */
        public void GetCancelReasonInfo(final Context context, int pos) {
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
                                    getView().getOrderReasonSuccess(t.getRetData().getDataList(), pos);
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
         * 全部订单和未支付订单数据处理
         *
         * @param data_orderlist
         * @return
         */
        public List<OrderAllListNoPayListDataBean> getResetData
        (List<OrderAllListDataBean.OrderAllBean> data_orderlist) {
            List<OrderAllListNoPayListDataBean> data = new ArrayList<>();
            if (data_orderlist == null) {
                return data;
            }
            for (OrderAllListDataBean.OrderAllBean order : data_orderlist
            ) {
                if (order.getPayState() == 1) {  //未支付
                    OrderAllListNoPayListDataBean orderAllListNoPayListDataBean = new OrderAllListNoPayListDataBean();
                    orderAllListNoPayListDataBean.setOrderId(order.getId());
                    orderAllListNoPayListDataBean.setPayState(order.getPayState());
                    orderAllListNoPayListDataBean.setOrderNumber(order.getOrderNumber());
                    orderAllListNoPayListDataBean.setOrderState(order.getOrderState());
                    orderAllListNoPayListDataBean.setConfirmState(order.getConfirmState());
                    orderAllListNoPayListDataBean.setPayAmtE2(order.getTotalMoneyAmtE2());
                    orderAllListNoPayListDataBean.setGoodsFlag(order.getGoodsFlag());
                    orderAllListNoPayListDataBean.setOrderPayEndTime(order.getOrderPayEndTime());
                    orderAllListNoPayListDataBean.setPackName("");
                    orderAllListNoPayListDataBean.setShowState(StringUtils.getShowStatus(order.getOrderState(), order.getPayState(), order.getConfirmState(), order.getDeliveryState()));
                    List<String> data_productPics = new ArrayList<>();
                    List<String> data_productIds = new ArrayList<>();
                    int productNum = 0;
                    List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean> data_packlist = order.getParcelInfo().getDataList();
                    for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean pack : data_packlist
                    ) {
                        List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean> data_productlist = pack.getOrderDetail().getDataList();
                        orderAllListNoPayListDataBean.setSingleProductName(data_productlist.get(0).getGoodsName());
                        orderAllListNoPayListDataBean.setSingleProductSpecs(data_productlist.get(0).getGoodsSpecs());
                        orderAllListNoPayListDataBean.setSingleProductNum(data_productlist.get(0).getGoodsNum());
                        orderAllListNoPayListDataBean.setWarehouseId(pack.getWarehouseId());
                        for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean product : data_productlist
                        ) {
                            data_productIds.add(product.getGoodsId() + "");
                            data_productPics.add(product.getGoodsCoverPic());
                            productNum += product.getGoodsNum();
                        }
                    }
                    orderAllListNoPayListDataBean.setShowProdutPics(data_productPics);
                    orderAllListNoPayListDataBean.setProductIds(data_productIds);
                    orderAllListNoPayListDataBean.setProductNum(productNum);
                    data.add(orderAllListNoPayListDataBean);
                } else {
                    List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean> data_packlist = order.getParcelInfo().getDataList();
                    //int pos = 1;
                    for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean pack : data_packlist
                    ) {
                        OrderAllListNoPayListDataBean orderAllListNoPayListDataBean = new OrderAllListNoPayListDataBean();
                        orderAllListNoPayListDataBean.setOrderId(order.getId());
                        orderAllListNoPayListDataBean.setPayState(order.getPayState());
                        orderAllListNoPayListDataBean.setOrderNumber(order.getOrderNumber());
                        orderAllListNoPayListDataBean.setOrderState(order.getOrderState());
                        orderAllListNoPayListDataBean.setConfirmState(order.getConfirmState());
                        orderAllListNoPayListDataBean.setPackId(pack.getId());
                        orderAllListNoPayListDataBean.setDeliveryState(pack.getDeliveryState());
                        orderAllListNoPayListDataBean.setShowState(StringUtils.getShowStatus(pack.getOrderState(), pack.getPayState(), pack.getConfirmState(), pack.getDeliveryState()));
                        orderAllListNoPayListDataBean.setPayAmtE2(pack.getTotalMoneyAmtE2());
                        orderAllListNoPayListDataBean.setWarehouseId(pack.getWarehouseId());
                        if (order.getParcelInfo().getDataCount() >= 1) {
                            orderAllListNoPayListDataBean.setPackName(pack.getParcelName());
                            /*orderAllListNoPayListDataBean.setPackName("包裹" + pos);
                            pos++;*/
                        } else {
                            orderAllListNoPayListDataBean.setPackName("");
                        }
                        List<String> data_productPics = new ArrayList<>();
                        List<String> data_productIds = new ArrayList<>();
                        int productNum = 0;
                        List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean> data_productlist = pack.getOrderDetail().getDataList();
                        orderAllListNoPayListDataBean.setSingleProductName(data_productlist.get(0).getGoodsName());
                        orderAllListNoPayListDataBean.setSingleProductSpecs(data_productlist.get(0).getGoodsSpecs());
                        orderAllListNoPayListDataBean.setSingleProductNum(data_productlist.get(0).getGoodsNum());
                        for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean product : data_productlist
                        ) {
                            data_productPics.add(product.getGoodsCoverPic());
                            data_productIds.add(product.getGoodsId() + "");
                            productNum += product.getGoodsNum();
                        }
                        orderAllListNoPayListDataBean.setShowProdutPics(data_productPics);
                        orderAllListNoPayListDataBean.setProductIds(data_productIds);
                        orderAllListNoPayListDataBean.setProductNum(productNum);
                        data.add(orderAllListNoPayListDataBean);
                    }
                }
            }
            return data;
        }

        /**
         * 待发货订单数据处理
         *
         * @param data_packlist
         * @return
         */
        public List<OrderAllListNoPayListDataBean> getResetOtherUnDeliveredData
        (List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean> data_packlist) {
            List<OrderAllListNoPayListDataBean> data = new ArrayList<>();
            if (data_packlist == null) {
                return data;
            }
            for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean pack : data_packlist
            ) {
                OrderAllListNoPayListDataBean orderAllListNoPayListDataBean = new OrderAllListNoPayListDataBean();
                orderAllListNoPayListDataBean.setOrderId(pack.getOrderId());
                orderAllListNoPayListDataBean.setPayState(pack.getPayState());
                orderAllListNoPayListDataBean.setOrderNumber(pack.getParcelNumber());
                orderAllListNoPayListDataBean.setOrderState(pack.getOrderState());
                orderAllListNoPayListDataBean.setConfirmState(pack.getConfirmState());
                orderAllListNoPayListDataBean.setPackId(pack.getId());
                orderAllListNoPayListDataBean.setDeliveryState(pack.getDeliveryState());
                orderAllListNoPayListDataBean.setShowState(StringUtils.getShowStatus(pack.getOrderState(), pack.getPayState(), pack.getConfirmState(), pack.getDeliveryState()));
                orderAllListNoPayListDataBean.setPayAmtE2(pack.getTotalMoneyAmtE2());
                orderAllListNoPayListDataBean.setPackName(pack.getParcelName());
                orderAllListNoPayListDataBean.setWarehouseId(pack.getWarehouseId());
                orderAllListNoPayListDataBean.isEvaluation = pack.getIsEvaluation();
                List<String> data_productPics = new ArrayList<>();
                List<String> data_productIds = new ArrayList<>();
                int productNum = 0;
                List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean> data_productlist = pack.getOrderDetail().getDataList();
                orderAllListNoPayListDataBean.setSingleProductName(data_productlist.get(0).getGoodsName());
                orderAllListNoPayListDataBean.setSingleProductSpecs(data_productlist.get(0).getGoodsSpecs());
                orderAllListNoPayListDataBean.setSingleProductNum(data_productlist.get(0).getGoodsNum());
                for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean product : data_productlist
                ) {
                    data_productPics.add(product.getGoodsCoverPic());
                    data_productIds.add(product.getGoodsId() + "");
                    productNum += product.getGoodsNum();
                }
                orderAllListNoPayListDataBean.setShowProdutPics(data_productPics);
                orderAllListNoPayListDataBean.setProductIds(data_productIds);
                orderAllListNoPayListDataBean.setProductNum(productNum);
                data.add(orderAllListNoPayListDataBean);
            }
            return data;
        }

        /**
         * 已发货订单数据处理
         *
         * @param data_packlist
         * @param showStatus
         * @return
         */
        public List<OrderAllListNoPayListDataBean> getResetOtherDeliveredData
        (List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean> data_packlist,
         int showStatus) {
            List<OrderAllListNoPayListDataBean> data = new ArrayList<>();
            if (data_packlist == null) {
                return data;
            }
            for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean pack : data_packlist
            ) {
                OrderAllListNoPayListDataBean orderAllListNoPayListDataBean = new OrderAllListNoPayListDataBean();
                orderAllListNoPayListDataBean.setOrderId(pack.getOrderId());
                orderAllListNoPayListDataBean.setOrderNumber(pack.getOrderNumber());
                orderAllListNoPayListDataBean.setPayState(pack.getPayState());
                orderAllListNoPayListDataBean.setOrderState(pack.getOrderState());
                orderAllListNoPayListDataBean.setConfirmState(pack.getConfirmState());
                orderAllListNoPayListDataBean.setPackId(pack.getId());
                orderAllListNoPayListDataBean.setDeliveryState(pack.getDeliveryState());
                orderAllListNoPayListDataBean.setShowState(showStatus);
                orderAllListNoPayListDataBean.setPayAmtE2(pack.getTotalMoneyAmtE2());
                orderAllListNoPayListDataBean.setPackName(pack.getParcelName());
                orderAllListNoPayListDataBean.setWarehouseId(pack.getWarehouseId());
                orderAllListNoPayListDataBean.isEvaluation = pack.getIsEvaluation();
                List<String> data_productPics = new ArrayList<>();
                List<String> data_productIds = new ArrayList<>();
                int productNum = 0;
                List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean> data_productlist = pack.getOrderDetail().getDataList();
                orderAllListNoPayListDataBean.setSingleProductName(data_productlist.get(0).getGoodsName());
                orderAllListNoPayListDataBean.setSingleProductSpecs(data_productlist.get(0).getGoodsSpecs());
                orderAllListNoPayListDataBean.setSingleProductNum(data_productlist.get(0).getGoodsNum());
                for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean product : data_productlist
                ) {
                    data_productPics.add(product.getGoodsCoverPic());
                    data_productIds.add(product.getGoodsId() + "");
                    productNum += product.getGoodsNum();

                }
                orderAllListNoPayListDataBean.setShowProdutPics(data_productPics);
                orderAllListNoPayListDataBean.setProductIds(data_productIds);
                orderAllListNoPayListDataBean.setProductNum(productNum);
                data.add(orderAllListNoPayListDataBean);
            }
            return data;
        }

        /**
         * 已完成订单数据处理
         *
         * @param data_packlist
         * @return
         */
        public List<OrderAllListNoPayListDataBean> getResetOtherFinishedData
        (List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean> data_packlist) {
            List<OrderAllListNoPayListDataBean> data = new ArrayList<>();
            if (data_packlist == null) {
                return data;
            }
            for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean pack : data_packlist
            ) {
                OrderAllListNoPayListDataBean orderAllListNoPayListDataBean = new OrderAllListNoPayListDataBean();
                orderAllListNoPayListDataBean.setOrderId(pack.getOrderId());
                orderAllListNoPayListDataBean.setOrderNumber(pack.getOrderNumber());
                orderAllListNoPayListDataBean.setPayState(pack.getPayState());
                orderAllListNoPayListDataBean.setOrderState(pack.getOrderState());
                orderAllListNoPayListDataBean.setConfirmState(pack.getConfirmState());
                orderAllListNoPayListDataBean.setPackId(pack.getId());
                orderAllListNoPayListDataBean.setDeliveryState(pack.getDeliveryState());
                orderAllListNoPayListDataBean.setShowState(StringUtils.getShowStatus(pack.getOrderState(), pack.getPayState(), pack.getConfirmState(), pack.getDeliveryState()));
                orderAllListNoPayListDataBean.setPayAmtE2(pack.getTotalMoneyAmtE2());
                orderAllListNoPayListDataBean.setPackName(pack.getParcelName());
                orderAllListNoPayListDataBean.setWarehouseId(pack.getWarehouseId());
                orderAllListNoPayListDataBean.isEvaluation = pack.getIsEvaluation();
                List<String> data_productPics = new ArrayList<>();
                List<String> data_productIds = new ArrayList<>();
                int productNum = 0;
                List<OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean> data_productlist = pack.getOrderDetail().getDataList();
                orderAllListNoPayListDataBean.setSingleProductName(data_productlist.get(0).getGoodsName());
                orderAllListNoPayListDataBean.setSingleProductSpecs(data_productlist.get(0).getGoodsSpecs());
                orderAllListNoPayListDataBean.setSingleProductNum(data_productlist.get(0).getGoodsNum());
                for (OrderAllListDataBean.OrderAllBean.OrderPackListBean.OrderPackBean.PackProductListBean.ProductBean product : data_productlist
                ) {
                    data_productPics.add(product.getGoodsCoverPic());
                    data_productIds.add(product.getGoodsId() + "");
                    productNum += product.getGoodsNum();

                }
                orderAllListNoPayListDataBean.setShowProdutPics(data_productPics);
                orderAllListNoPayListDataBean.setProductIds(data_productIds);
                orderAllListNoPayListDataBean.setProductNum(productNum);
                data.add(orderAllListNoPayListDataBean);
            }
            return data;
        }


    }
}
