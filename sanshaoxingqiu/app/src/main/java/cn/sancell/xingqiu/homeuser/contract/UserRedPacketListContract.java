package cn.sancell.xingqiu.homeuser.contract;

import android.content.Context;

import java.util.HashMap;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/23.
 */

public class UserRedPacketListContract {
    public interface UserRedPacketListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getUserRedPacketListSuccess(UserRedPacketListBean userRedPacketListBean, int page);
    }

    public static class UserRedPacketListPresenter extends BasePresenter<UserRedPacketListContract.UserRedPacketListView> {

        /**
         * 红包收支明细
         */
        public void GetUserRedPacketList(int page, final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            map.put("reqTime", reqTime);
            map.put("skey", skey);
            map.put("pointToAccountStatus", "1");
            map.put("hashToken", RSAUtils.encryptByPublic("1" + reqTime + skey));
            map.put("page", page + "");
            map.put("pageSize", "10");
            RetrofitUtil.getInstance().initRetrofit().getUserRedPacketListData(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserRedPacketListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<UserRedPacketListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    if (t.getRetData() != null) {
                                        getView().getUserRedPacketListSuccess(t.getRetData(), page);
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
