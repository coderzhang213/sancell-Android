package cn.sancell.xingqiu.order;


import com.umeng.socialize.media.Base;

import java.util.HashMap;

import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.bean.OrderVoucherRes;
import cn.sancell.xingqiu.homeshoppingcar.bean.AlipayPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.WeiXinPayInfoBean;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.order.entity.req.GoodVoucherReq;
import cn.sancell.xingqiu.order.entity.req.PinCancelReq;
import cn.sancell.xingqiu.order.entity.req.PinDetailReq;
import cn.sancell.xingqiu.order.entity.req.PinInviteReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderNewReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderPackageReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderReq;
import cn.sancell.xingqiu.order.entity.req.SCPayReq;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;
import cn.sancell.xingqiu.order.entity.res.PinInviteRes;
import cn.sancell.xingqiu.order.entity.res.PinOrderPackRes;
import cn.sancell.xingqiu.util.RetrofitUtil;
import io.reactivex.Observable;

/**
  * @author Alan_Xiong
  *
  * @desc: 订单model
  * @time 2020-01-02 10:36
  */
public class OrderModel {

    private static final class Holder{
        private static final OrderModel instance = new OrderModel();
    }

    public static OrderModel getInstance() {
        return Holder.instance;
    }

    public Observable<BaseEntry<CreateOrderDefaultPreBean>> getDefaultAddress(BaseReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getDefualtOrderPre(map);
    }

    public Observable<BaseEntry<OrderRes>> createPinOrder(PinOrderReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().createPinOrder(map);
    }

    public Observable<BaseEntry<OrderRes>> createPinNewOrder(PinOrderNewReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().createPinNewOrder(map);
    }

    public Observable<BaseEntry<WeiXinPayInfoBean>> getPinWxPayInfo(SCPayReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getPinWeChatPayInfo(map);
    }

    public Observable<BaseEntry<AlipayPayInfoBean>> getPinAliPayInfo(SCPayReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getPinAliPayInfo(map);
    }

    //包裹详情
    public Observable<BaseEntry<PinOrderPackRes>> getPinOrderDetail(PinOrderPackageReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getPinOrderDetail(map);
    }

    public Observable<BaseEntry<PinDetailRes>> getPinDetailInfo(PinDetailReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getPinDetailInfo(map);
    }

    public Observable<BaseEntry<EmptyBean>> pinOrderCancel(PinCancelReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().pinOrderCancel(map);
    }

    public Observable<BaseEntry<PinInviteRes>> pinInviteUser(PinInviteReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().pinInviteUser(map);
    }

    public Observable<BaseEntry<OrderVoucherRes>> getVoucherInfo(GoodVoucherReq req){
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getOrderVoucher(map);
    }



}
