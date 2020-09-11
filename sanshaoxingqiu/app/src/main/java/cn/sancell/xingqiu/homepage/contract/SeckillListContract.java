package cn.sancell.xingqiu.homepage.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.SearchKeyListBean;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionListBean;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionProductListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/9/8.
 */

public class SeckillListContract {
    public interface SeckillListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getSeckillRemindSuccess();

        void getSeckillSessionListSuccess(List<SeckillSessionListBean.SeckillSessionBean> dataList);

        void getSessionProductListSuccess(SeckillSessionProductListBean seckillSessionProductListBean, int page);

    }

    public static class SeckillListPresenter extends BasePresenter<SeckillListContract.SeckillListView> {

        /**
         * 场次列表
         */
        public void GetSeckillSessionList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getSeckillSessionList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<SeckillSessionListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<SeckillSessionListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getSeckillSessionListSuccess(t.getRetData().getDataList());
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
         * 单个场次商品列表
         */
        public void GetSessionProductList(int page, String seckillRoundId, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("seckillRoundId", seckillRoundId);
            map.put("hashToken", RSAUtils.encryptByPublic(reqTime + seckillRoundId + skey));
            map.put("page", page + "");
            map.put("pageSize", "10");
            RetrofitUtil.getInstance().initRetrofit().getSeckillSessionProductList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<SeckillSessionProductListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<SeckillSessionProductListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getSessionProductListSuccess(t.getRetData(), page);
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
         * 提醒
         */
        public void GetSeckillRemind(String seckillRoundId,String seckillRoundRelGoodsId,String goodsId,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("seckillRoundId", seckillRoundId);
            map.put("seckillRoundRelGoodsId", seckillRoundRelGoodsId);
            map.put("goodsId", goodsId);
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getSeckillRemind(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(context) {
                        @Override
                        protected void onSuccess(BaseEntry t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getSeckillRemindSuccess();
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

    }
}
