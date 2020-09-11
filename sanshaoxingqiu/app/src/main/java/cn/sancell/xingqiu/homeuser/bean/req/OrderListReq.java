package cn.sancell.xingqiu.homeuser.bean.req;

import cn.sancell.xingqiu.base.base.BaseReq;

public class OrderListReq extends BaseReq {

    public String page;

    public String pageSize;

    public String orderStatus; //发货状态 1：待支付2：待发货 4：已发货 5.待评价

    public String goodsFlag; //商品类型 1：普通商品 2：礼包商品
}
