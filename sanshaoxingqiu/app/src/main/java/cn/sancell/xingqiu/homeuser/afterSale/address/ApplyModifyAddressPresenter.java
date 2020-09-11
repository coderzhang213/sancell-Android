package cn.sancell.xingqiu.homeuser.afterSale.address;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressRegInfo;
import cn.sancell.xingqiu.homeuser.bean.req.EditAddressReq;
import cn.sancell.xingqiu.usermember.model.UserModel;

/**
 * @author Alan_Xiong
 * @desc: 修改地址
 * @time 2019-11-28 15:15
 */
public class ApplyModifyAddressPresenter extends BasePresenter<ApplyModifyAddressView> {

    private Context mContext;

    public ApplyModifyAddressPresenter(Context context) {
        mContext = context;
    }

    /**
     * @param goodsId  商品ID
     * @param parcelId 包裹ID
     */
    public void getAddressList(String goodsId, String parcelId) {

        UserModel.getInstance().getAddressReggList(goodsId, parcelId).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<AddressRegInfo>(mContext) {

                    @Override
                    protected void onSuccess(BaseEntry<AddressRegInfo> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().getAddressListSuccess(t.getRetData());
                            } else {
                                getView().getAddressListError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getAddressListError(e.toString());
                        }
                    }
                });

    }

    public void modifyAddress(EditAddressReq req) {
        UserModel.getInstance().modifyAddress(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<EmptyBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<EmptyBean> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                getView().getModifySuccess();
                            } else {
                                getView().getModifyError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getModifyError(e.toString());
                        }
                    }
                });
    }
}
