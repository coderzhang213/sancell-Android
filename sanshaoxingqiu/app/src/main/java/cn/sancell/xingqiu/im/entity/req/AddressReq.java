package cn.sancell.xingqiu.im.entity.req;

import cn.sancell.xingqiu.base.base.BaseReq;

/**
  * @author Alan_Xiong
  *
  * @desc: 我的群组
  * @time 2019-11-20 22:51
  */
public class AddressReq extends BaseReq {

    public String page;

    public String pageSize;

    public String type;//has:创建的群； join:加入的群
}
