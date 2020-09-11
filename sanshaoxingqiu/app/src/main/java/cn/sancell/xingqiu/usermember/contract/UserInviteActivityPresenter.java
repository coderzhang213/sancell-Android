package cn.sancell.xingqiu.usermember.contract;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.usermember.bean.InviterBean;
import cn.sancell.xingqiu.usermember.model.UserModel;
import cn.sancell.xingqiu.usermember.req.UserInviteReq;

/**
 * @author Alan_Xiong
 * @desc: 邀请人p层
 * @time 2019-10-22 12:56
 */
public class UserInviteActivityPresenter extends BasePresenter<UserInviteActivityView> {
    private Context mContext;

    public UserInviteActivityPresenter(Context context) {
        mContext = context;
    }

    public void getUserInviteInfo(UserInviteReq req) {
        UserModel.getInstance().getInviteInfo(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<InviterBean>(mContext) {

                    @Override
                    protected void onSuccess(BaseEntry<InviterBean> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().getInviteSuccess(t.getRetData());
                                }
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().showLoading(false);
                            getView().toast(e.getMessage());
                        }
                    }
                });
    }

    public void getBindInviteInfo(UserInviteReq req){
        UserModel.getInstance().getBindInviteInfo(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<EmptyBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry t) throws Exception {
                        if (getView() != null){

                            if (t.getRetCode() == 0) {
                                if (t.getRetData() != null) {
                                    getView().bindInviteSuccess();
                                }
                            } else {
                                getView().toast(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().toast(e.getMessage());
                        }
                    }
                });
    }

}
