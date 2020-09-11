package cn.sancell.xingqiu.order.entity.req;

import cn.sancell.xingqiu.base.base.BaseReq;

public class SCPayReq extends BaseReq {

    public String orderNo;

    public String clientId = "3"; //客户端Id 1：pc 2：ios 3:安卓

}
