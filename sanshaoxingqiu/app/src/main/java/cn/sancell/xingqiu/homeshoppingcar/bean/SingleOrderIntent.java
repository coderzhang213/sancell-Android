package cn.sancell.xingqiu.homeshoppingcar.bean;

import java.io.Serializable;
import java.util.List;

/**
  * @author Alan_Xiong
  *
  * @desc: 单个商品跳转传参model
  * @time 2019-12-16 11:27
  */
public class SingleOrderIntent implements Serializable {

    public String good_id;
    public String good_title;
    public String good_spec;
    public String good_pic;
    public int total_price;
    public boolean isOneYuan;
    public int mActivityId;

}
