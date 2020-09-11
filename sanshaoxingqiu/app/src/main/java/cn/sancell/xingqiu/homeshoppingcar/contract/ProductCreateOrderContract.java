package cn.sancell.xingqiu.homeshoppingcar.contract;

import android.content.Context;

import com.netease.nim.uikit.common.util.string.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.req.GoodsAreaReq;
import cn.sancell.xingqiu.homeshoppingcar.bean.res.GoodAreaRes;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.contract.SettingContract;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.model.UserModel;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/29.
 */

public class ProductCreateOrderContract {
    public interface CreateOrderView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getDefaultOrderPreSuccess(CreateOrderDefaultPreBean createOrderDefaultPreBean);

        void createOrderSuccess(String orderId, String orderParcelId);

        void createOrderFail(String errorTip);

        void getGoodsAreaSuccess(List<String> res);

        void getGoodsSupportAreaError(String error);

    }

    public static class CreateOrderPresenter extends BasePresenter<ProductCreateOrderContract.CreateOrderView> {

        /**
         * 获取订单生成预确定信息（默认地址和红包）
         **/
        public void GetDefualtOrderPre(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getDefualtOrderPre(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<CreateOrderDefaultPreBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<CreateOrderDefaultPreBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().getDefaultOrderPreSuccess(t.getRetData());
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
         * 生成订单
         */
        public void CreateOrder(String addressId, String carIdString,
                                String pointE2, String payPassword,
                                String isNeedInvoice, String invoiceLookedUp, String companyName, String companyIdentifyNumber,
                                String mobile, String email, JSONObject remark, final Context context) {
            getView().showLoading(true);
            HashMap<String, Object> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("addressId", addressId);
            map.put("carIdString", carIdString);
            map.put("clientId", "3");
            map.put("remark", remark);
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(addressId + RSAUtils.md5(carIdString) + "3" + reqTime + skey));
            if (!StringUtils.isTextEmpty(pointE2)) {
                map.put("pointE2", pointE2);
            }
            if (!StringUtils.isTextEmpty(payPassword)) {
                map.put("payPassword", RSAUtils.encryptByPublic(payPassword));
            }
            map.put("isNeedInvoice", isNeedInvoice);
            if (!StringUtils.isTextEmpty(companyName)) {
                map.put("companyName", companyName);
            }
            if (!StringUtils.isTextEmpty(invoiceLookedUp)) {
                map.put("invoiceLookedUp", invoiceLookedUp);
            }
            if (!StringUtils.isTextEmpty(companyIdentifyNumber)) {
                map.put("companyIdentifyNumber", companyIdentifyNumber);
            }
            if (!StringUtils.isTextEmpty(mobile)) {
                map.put("mobile", mobile);
            }
            if (!StringUtils.isTextEmpty(email)) {
                map.put("email", email);
            }
            RetrofitUtil.getInstance().initRetrofit().createOrder(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<CreateOrderBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<CreateOrderBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().createOrderSuccess(t.getRetData().getOrderId(), "");
                            } else {
                                if (!StringUtils.isTextEmpty(payPassword)) {
                                    getView().createOrderFail(t.getRetMsg());
                                } else {
                                    getView().toast(t.getRetMsg());
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
         * 单个商品生成订单
         */
        public void SingleCreateOrder(String addressId, String goodsId,
                                      String pointE2, String payPassword,
                                      String isNeedInvoice, String invoiceLookedUp, String companyName, String companyIdentifyNumber,
                                      String mobile, String email, String remark, String oneShop, final Context context, int actvityId) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("addressId", addressId);
            map.put("goodsId", goodsId);
            map.put("clientId", "3");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("remark", remark);
            map.put("hashToken", RSAUtils.encryptByPublic(addressId + "3" + goodsId + reqTime + skey));
            if (!StringUtils.isTextEmpty(pointE2)) {
                map.put("pointE2", pointE2);
            }
            if (!StringUtils.isTextEmpty(payPassword)) {
                map.put("payPassword", RSAUtils.encryptByPublic(payPassword));
            }
            map.put("isNeedInvoice", isNeedInvoice);
            if (!StringUtils.isTextEmpty(companyName)) {
                map.put("companyName", companyName);
            }
            if (!StringUtils.isTextEmpty(invoiceLookedUp)) {
                map.put("invoiceLookedUp", invoiceLookedUp);
            }
            if (!StringUtils.isTextEmpty(companyIdentifyNumber)) {
                map.put("companyIdentifyNumber", companyIdentifyNumber);
            }
            if (!StringUtils.isTextEmpty(mobile)) {
                map.put("mobile", mobile);
            }
            if (!StringUtils.isTextEmpty(email)) {
                map.put("email", email);
            }
            if (!StringUtils.isTextEmpty(oneShop)) {
                map.put("oneShop", oneShop);
            }
            if (actvityId > 0) {
                map.put("activityId", actvityId + "");
            }
            RetrofitUtil.getInstance().initRetrofit().createSingleProductOrder(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<CreateOrderBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<CreateOrderBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().createOrderSuccess(t.getRetData().getOrderId(), "");
                            } else {
                                if (!StringUtils.isTextEmpty(payPassword)) {
                                    getView().createOrderFail(t.getRetMsg());
                                } else {
                                    getView().toast(t.getRetMsg());
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
         * 会员购买礼包生成订单
         */
        public void CreateMemberOrder(String addressId, String goodsId, String pointE2, String remark, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("addressId", addressId);
            map.put("goodsId", goodsId);
            map.put("clientId", "3");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(addressId + "3" + goodsId + reqTime + skey));
            map.put("remark", remark);
            if (!StringUtils.isTextEmpty(pointE2)) {
                map.put("pointE2", pointE2);
            }
            RetrofitUtil.getInstance().initRetrofit().createMemberGiftOrder(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<CreateOrderBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<CreateOrderBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                getView().createOrderSuccess(t.getRetData().getOrderId(), t.getRetData().getOrderParcelId());
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
         * 商品支持配送区域
         * @param context
         * @param req
         */
        public void getGoodsArea(Context context,GoodsAreaReq req){
            UserModel.getInstance().getGoodsArea(req).compose(RxUtils.transformerWithLoading(getView()))
                    .subscribe(new BaseObserver<GoodAreaRes>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<GoodAreaRes> t) throws Exception {
                            if (getView() != null){
                                if (t.getRetCode() == 0){
                                    getView().getGoodsAreaSuccess(t.getRetData().goodsRegion);
                                }else{
                                    getView().toast(t.getRetMsg());
                                }
                            }
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (getView() != null){
                                getView().getGoodsSupportAreaError(e.toString());
                            }
                        }
                    });
        }

    }
}
