package cn.sancell.xingqiu.goods;

import android.content.Context;

import cn.sancell.xingqiu.base.mvp.BasePresenter;

/**
  * @author Alan_Xiong
  *
  * @desc:商品详情presenter
  * @time 2019-12-25 09:59
  */
public class GoodsDetailPresenter extends BasePresenter<GoodsDetailView> {

    private Context mContext;

    public GoodsDetailPresenter(Context context){
        mContext = context;
    }

}
