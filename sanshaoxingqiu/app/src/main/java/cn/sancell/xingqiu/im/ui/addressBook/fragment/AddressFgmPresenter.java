package cn.sancell.xingqiu.im.ui.addressBook.fragment;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.AddressReq;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2019-11-20 23:05
  */
public class AddressFgmPresenter extends BasePresenter<AddressFgmView> {

    private Context mContext;

    public AddressFgmPresenter(Context context){
        mContext = context;
    }

    public void getMyTeam(AddressReq req){
        ImModel.getInstance().getMyTeam(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<AddressRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<AddressRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getTeamSuccess(t.getRetData());
                            }else{
                                getView().getTeamError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getTeamError(e.toString());
                        }
                    }
                });
    }
}
