package cn.sancell.xingqiu.goods.fragment.info;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.goods.entity.req.GoodsInfoReq;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.usermember.model.UserModel;

public class GoodsInfoPresenter extends BasePresenter<GoodsInfoView> {

    private Context mContext;

    public GoodsInfoPresenter(Context context){
        mContext = context;
    }


    public void getGoodsDetail(GoodsInfoReq req){
        UserModel.getInstance().getGoodsDetail(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<ProductInfoDataBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<ProductInfoDataBean> t) throws Exception {
                        if (t.getRetCode() == 0){
                            getView().getGoodsInfoSuccess(t.getRetData());
                        }else{
                            getView().getInfoError(t.getRetMsg());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getInfoError(e.toString());
                        }
                    }
                });
    }

    /**
     * 获取地址列表
     *
     * @param mContext
     */
    public void getAddressList(Context mContext) {
        UserModel.getInstance().getAddressList(new BaseReq()).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<AddressListDataBean>(mContext) {

                    @Override
                    protected void onSuccess(BaseEntry<AddressListDataBean> t) throws Exception {
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
}
