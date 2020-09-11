package cn.sancell.xingqiu.base.base;


import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;

/**
  * @author Alan_Xiong
  *
  * @desc: 请求参数基类
  * @time 2019-10-22 11:03
  */
public class BaseReq{

    public String reqTime;

    public String skey;

    public BaseReq(){
        reqTime = StringUtils.getCurrentTime();
        skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
    }

}
