package cn.sancell.xingqiu.viewGroup.entity;

import java.util.List;

import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;

/**
  * @author Alan_Xiong
  *
  * @desc: 商品配送区域支持
  * @time 2019-12-13 15:47
  */
public class AreaSupportModel {

    public int totalCount;//总共的商品数量

    public List<HomeShoppingCarDataBean.ShopingCarProductBean> vailList; //可配送

    public List<HomeShoppingCarDataBean.ShopingCarProductBean> inVailList; //不可配送
}
