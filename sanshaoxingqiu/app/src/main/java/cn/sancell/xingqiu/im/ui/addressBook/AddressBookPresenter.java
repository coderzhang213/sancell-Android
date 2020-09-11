package cn.sancell.xingqiu.im.ui.addressBook;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.AddressReq;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc: im通讯录
  * @time 2019-11-13 22:22
  */
public class AddressBookPresenter extends BasePresenter<AddressBookView> {

    private Context mContext;

    public AddressBookPresenter(Context context){
        mContext = context;
    }


}
