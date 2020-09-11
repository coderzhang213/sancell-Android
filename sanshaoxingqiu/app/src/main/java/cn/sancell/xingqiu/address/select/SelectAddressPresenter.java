package cn.sancell.xingqiu.address.select;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.usermember.model.UserModel;

/**
  * @author Alan_Xiong
  *
  * @desc: 选择地址presenter
  * @time 2019-12-12 17:15
  */
public class SelectAddressPresenter extends BasePresenter<SelectAddressActivityView> {

    private Context mContext;

    public SelectAddressPresenter(Context context){
        mContext = context;
    }

    public void getAddressList(){
        UserModel.getInstance().getAddressList(new BaseReq()).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<AddressListDataBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<AddressListDataBean> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getAddressListSuccess(t.getRetData());
                            }else{
                                getView().getAddressError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getAddressError(e.toString());
                        }
                    }
                });
    }
}
