package cn.sancell.xingqiu.im.ui.addressBook.fragment;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.res.AddressRes;

/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2019-11-20 23:05
  */
public interface AddressFgmView extends BaseView {

    void getTeamSuccess(AddressRes res);

    void getTeamError(String error);
}
