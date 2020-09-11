package cn.sancell.xingqiu.homeshoppingcar.contract;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/6/13.
 */

public class HomeShoppingCarContract {
    public interface HomeShoppingCarView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getShoppingCarListSuccess(List<HomeShoppingCarDataBean.ShopingCarProductBean> dataList, int invalid_nostock_num,boolean isCheckData);

        void deleteShoppingCarSuccess();

        void batchDeleteShoppingCarSuccess();

        void numAddShoppingCarSuccess(int pos,int goodsNum);

        void numDecreaseShoppingCarSuccess(int pos,int goodsNum);

        void modifyShoppingCarSelectStatusSuccess(int pos);

        void modifyAllShoppingCarSelectStatusSuccess();

        void clearUnderStockSuccess();

        void clearInvalidSuccess();

        void getLikeListSuccess(HomePageLikeListDataBean dataLike, int page);

    }

    public static class HomeShoppingCarPresenter extends BasePresenter<HomeShoppingCarContract.HomeShoppingCarView> {
        /**
         * 购物车列表
         */
        public void GetShoppingCarList(final Context context,boolean isCheckData) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getShoppingCarListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<HomeShoppingCarDataBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<HomeShoppingCarDataBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    List<HomeShoppingCarDataBean.ShopingCarProductBean> data = new ArrayList<>();
                                    if (t.getRetData().getNormalGoodsData().getDataCount() > 0) {
                                        for (HomeShoppingCarDataBean.ShopingCarProductBean temp :
                                                t.getRetData().getNormalGoodsData().getDataList()) {
                                            temp.setType(1);
                                            data.add(temp);
                                        }
                                    }
                                    if (t.getRetData().getUnderStockGoodsData().getDataCount() > 0) {
                                        for (HomeShoppingCarDataBean.ShopingCarProductBean temp :
                                                t.getRetData().getUnderStockGoodsData().getDataList()) {
                                            temp.setType(2);
                                            data.add(temp);
                                        }
                                    }
                                    if (t.getRetData().getFailureGoodsData().getDataCount() > 0) {
                                        for (HomeShoppingCarDataBean.ShopingCarProductBean temp :
                                                t.getRetData().getFailureGoodsData().getDataList()) {
                                            temp.setType(3);
                                            data.add(temp);
                                        }
                                    }
                                    getView().getShoppingCarListSuccess(data, t.getRetData().getUnderStockGoodsData().getDataCount() + t.getRetData().getFailureGoodsData().getDataCount(),isCheckData);
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
         * 单个删除购物车
         *
         * @param carDetailId
         */
        public void DeleteShoppingCar(String carDetailId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("carDetailId", carDetailId);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().deleteShoppingCar(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().deleteShoppingCarSuccess();
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
         * 批量删除购物车
         *
         * @param carDetailString
         */
        public void BatchDeleteShoppingCar(String carDetailString, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            String carDetailToken = RSAUtils.md5(carDetailString);
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("carDetailString", carDetailString);
            map.put("carDetailToken", carDetailToken);
            map.put("hashToken", RSAUtils.encryptByPublic(carDetailToken + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().batchDeleteShoppingCar(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().batchDeleteShoppingCarSuccess();
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
         * 增加购物车数量
         *
         * @param carDetailId
         */
        public void NumAddShoppingCar(String carDetailId, int goodsNum, int pos, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("carDetailId", carDetailId);
            map.put("actType", "1");
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("goodsNum", goodsNum + "");
            RetrofitUtil.getInstance().initRetrofit().modifyShoppingCarNum(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().numAddShoppingCarSuccess(pos,goodsNum);
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
         * 减少购物车数量
         *
         * @param carDetailId
         */
        public void NumDecreaseShoppingCar(String carDetailId,int goodsNum,  int pos,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("carDetailId", carDetailId);
            map.put("actType", "2");
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            map.put("goodsNum", goodsNum + "");
            RetrofitUtil.getInstance().initRetrofit().modifyShoppingCarNum(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);

                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().numDecreaseShoppingCarSuccess(pos,goodsNum);
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
         * 修改购物车选中状态
         *
         * @param carDetailId
         * @param isSelected
         */
        public void ModifyShoppingCarSelectStatus(String carDetailId, int isSelected, int pos, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("carDetailId", carDetailId);
            map.put("isSelected", isSelected + "");
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().modifyShoppingCarSelectStatus(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifyShoppingCarSelectStatusSuccess(pos);
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
         * 全选修改购物车选中状态
         *
         * @param isSelected
         */
        public void ModifyAllShoppingCarSelectStatus(int isSelected, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("isSelected", isSelected + "");
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().modifyAllShoppingCarSelectStatus(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().modifyAllShoppingCarSelectStatusSuccess();
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
         * 清空库存不足的商品
         */
        public void ClearUnderStock(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().clearUnderStock(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().clearUnderStockSuccess();
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
         * 清空失效商品
         */
        public void ClearInvalid(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().clearInvalid(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            getView().toast(t.getRetMsg());
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().clearInvalidSuccess();
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


    }
}
