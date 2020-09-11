package cn.sancell.xingqiu.order.entity;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

public class PinIntentBean implements Serializable {

    public int goodsId;
    public String good_title;
    public String good_spec;
    public String good_pic;
    public int total_price;
    public long pinId;
    public List<String> supportArea;
    public int buyNum; //规格
    public AddressListDataBean.AddressItemBean mAddress; //配送的地址
    public int gruopId; // 参团1 / 开团0
    public String liveBatchId;//直播id
    public long mMaxRpCanUse;//最大红包使用限额
    public int goodsFlag; //5是成本商品

}
