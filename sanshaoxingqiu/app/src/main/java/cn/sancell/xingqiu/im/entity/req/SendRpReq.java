package cn.sancell.xingqiu.im.entity.req;

import cn.sancell.xingqiu.base.base.BaseReq;

/**
  * @author Alan_Xiong
  *
  * @desc: 发送红包
  * @time 2019-11-18 10:02
  */
public class SendRpReq extends BaseReq {

    public String redNum;

    public String singleMoney;

    public String sumMoney;

    public String source;

    public String showInfo;

    public String redType;

    public String accountPassword;//支付密码

}
