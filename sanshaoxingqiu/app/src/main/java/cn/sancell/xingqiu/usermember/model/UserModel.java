package cn.sancell.xingqiu.usermember.model;

import java.util.HashMap;

import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.goods.entity.req.GoodsCommentReq;
import cn.sancell.xingqiu.goods.entity.req.GoodsInfoReq;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.req.GoodsAreaReq;
import cn.sancell.xingqiu.homeshoppingcar.bean.res.GoodAreaRes;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressRegInfo;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListDataBean;
import cn.sancell.xingqiu.homeuser.bean.req.EditAddressReq;
import cn.sancell.xingqiu.homeuser.bean.req.OrderListReq;
import cn.sancell.xingqiu.homeuser.bean.res.OrderListRes;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.InviterBean;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.usermember.req.UserInviteReq;
import cn.sancell.xingqiu.util.RetrofitUtil;
import io.reactivex.Observable;

/**
 * @author Alan_Xiong
 * @desc: 用户数据 model
 * @time 2019-10-22 11:37
 */
public class UserModel {

    public static class UserHolder {
        private static final UserModel MODEL = new UserModel();
    }

    public static UserModel getInstance() {
        return UserHolder.MODEL;
    }

    /**
     * 获取邀请人信息
     *
     * @param req 邀请人req
     * @return
     */
    public Observable<BaseEntry<InviterBean>> getInviteInfo(UserInviteReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getInviterInfo(map);
    }

    public Observable<BaseEntry<UserBean>> getUserInfo(BaseReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getUserInfo(map);
    }

    public Observable<BaseEntry<EmptyBean>> getBindInviteInfo(UserInviteReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        map.put("bind", "1");
        return RetrofitUtil.getInstance().initRetrofit().bindnViterId(map);
    }

    public Observable<BaseEntry<AddressListDataBean>> getAddressList(BaseReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getAddressList(map);
    }

    public Observable<BaseEntry<EmptyBean>> modifyAddress(EditAddressReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getModifyAddress(map);
    }

    public Observable<BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean>> getOrderList(OrderListReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req,"orderStatus");
        return RetrofitUtil.getInstance().initRetrofit().getOrderList(map);
    }


    public Observable<BaseEntry<GoodAreaRes>> getGoodsArea(GoodsAreaReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getGoodsArea(map);
    }

    /**
     * 更新用户ID
     *
     * @param maAddressId
     * @return
     */
    public Observable<BaseEntry<EmptyBean>> upChooseAddress(int maAddressId) {
        HashMap<String, String> map = ConvertUtils.getRequest();
        map.put("addressId", maAddressId + "");
        return RetrofitUtil.getInstance().initRetrofit().chooseAddress(map);
    }

    /**
     * 获取配送地址
     *
     * @param goodsId
     * @param parcelId
     * @return
     */
    public Observable<BaseEntry<AddressRegInfo>> getAddressReggList(String goodsId, String parcelId) {
        HashMap<String, String> map = ConvertUtils.getRequest();
        map.put("goodsId", goodsId);
        map.put("parcelId", parcelId);
        return RetrofitUtil.getInstance().initRetrofit().getAddressReggList(map);
    }

    /**
     * 商品详情
     *
     * @param req
     * @return
     */
    public Observable<BaseEntry<ProductInfoDataBean>> getGoodsDetail(GoodsInfoReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req,"objId");
        return RetrofitUtil.getInstance().initRetrofit().getGoodsDetail(map);
    }

    /**
     * 获取商品评论数据
     * @param req
     * @return
     */
    public Observable<BaseEntry<EvaluateListDataBean>> getGoodsComments(GoodsCommentReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req, "goodsId");
        return RetrofitUtil.getInstance().initRetrofit().getEvaluateListData(map);
    }

    /**
     * 用户会员
     * @return
     */
    public Observable<BaseEntry<UserMemberRes>> getUserMember(){
        HashMap<String, String> map = ConvertUtils.getRequest();
        return RetrofitUtil.getInstance().initRetrofit().getUserMember(map);
    }
}
