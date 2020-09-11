package cn.sancell.xingqiu.service;

import cn.sancell.xingqiu.order.entity.res.OrderRes;

public class PayDataService {

    public static OrderRes.AddressInfoBean mAddress; //缓存订单的地址信息

    public static long totalPrice; //总价
}
