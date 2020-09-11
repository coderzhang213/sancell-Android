package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.MessageNoticeListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/23.
 */

public class MessageNoticeListContract  {
    public interface MessageNoticeListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getMessageNoticeListSuccess(MessageNoticeListBean messageNoticeListBean, int page);
    }

    public static class MessageNoticeListPresenter extends BasePresenter<MessageNoticeListContract.MessageNoticeListView> {

        /**
         * 消息通知列表
         */
        public void GetMessageNoticeList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("hashToken", RSAUtils.encryptByPublic(reqTime + skey));
            map.put("page", page + "");
            map.put("pageSize", "10");
            RetrofitUtil.getInstance().initRetrofit().getMessageNoticeListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MessageNoticeListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MessageNoticeListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData() != null) {
                                        getView().getMessageNoticeListSuccess(t.getRetData(), page);
                                    }
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
