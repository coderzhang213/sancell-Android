package cn.sancell.xingqiu.usermember.contract;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.usermember.bean.MemberOpenRecordsListBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ai11 on 2019/8/13.
 */

public class MemberOpenRecordsListContract {
    public interface MemberOpenRecordsListView extends BaseView {
        void toast(String text);

        void netWorkError();

        void getOpenRecordsListSuccess(List<MemberOpenRecordsListBean.MemberOpenRecordsBean> dataList);
    }

    public static class MemberOpenRecordsListPresenter extends BasePresenter<MemberOpenRecordsListContract.MemberOpenRecordsListView> {

        /**
         * 会员开通记录
         */
        public void GetMemberRecordsList(final Context context) {
            getView().showLoading(true);
            HashMap<String, String> map = new HashMap<>();
            String reqTime = StringUtils.getCurrentTime();
            map.put("reqTime", reqTime);
            map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
            map.put("hashToken", RSAUtils.encryptByPublic(map));
            RetrofitUtil.getInstance().initRetrofit().getMemberRecordsList(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<MemberOpenRecordsListBean>(context) {
                        @Override
                        protected void onSuccess(BaseEntry<MemberOpenRecordsListBean> t) throws Exception {
                            getView().showLoading(false);
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getOpenRecordsListSuccess(t.getRetData().getDataList());
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
