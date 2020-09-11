package cn.sancell.xingqiu.homeclassify.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeclassify.bean.PublishEvaluateContentBean;
import cn.sancell.xingqiu.homeclassify.bean.UpLoadPicListBean;
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/7/16.
 */

public class ProductPublishEvaluateContract {
    public interface PublishEvaluateView extends BaseView {
        void toast(String text);

        void publishContentSuccess(String id);

        void getuploadPhotoInfoSuccess(List<UpLoadPhotoInfoBean> upLoadPhotoInfoBeans);
    }

    public static class PublishEvaluatePresenter extends BasePresenter<ProductPublishEvaluateContract.PublishEvaluateView> {
        /**
         * 上传评价文本信息
         *
         * @param orderId
         * @param orderDetailId
         * @param evaluationCore
         * @param evaluationStr
         */
        public void publishEvaluateContent(String orderId, String orderDetailId, int evaluationCore, String evaluationStr, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("orderId", orderId);
            map.put("orderDetailId", orderDetailId);
            map.put("evaluationCore", evaluationCore + "");
            map.put("evaluationStr", evaluationStr);
            map.put("hashToken", RSAUtils.encryptByPublic(evaluationCore + RSAUtils.md5(evaluationStr) + orderDetailId + orderId + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().publishEvaluateContent(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<PublishEvaluateContentBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<PublishEvaluateContentBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().publishContentSuccess(t.getRetData().getEvaluationId() + "");
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
         * 获取上传图像的信息
         */
        public void GetUpLoadPhotoInfo(String objId, String rawPicArr,final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("objId", objId);
            map.put("rawPicArr", rawPicArr);
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(objId+RSAUtils.md5(rawPicArr) + reqTime + skey));
            RetrofitUtil.getInstance().initRetrofit().getUpLoadPicsInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UpLoadPicListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UpLoadPicListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getuploadPhotoInfoSuccess(t.getRetData().getDataList());
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
