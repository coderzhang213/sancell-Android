package cn.sancell.xingqiu.im.ui.recent;

import android.content.Context;

import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;

public class RecentMsgPresenter extends BasePresenter<BaseView> {

    private Context mContext;

    public RecentMsgPresenter(Context context){
        this.mContext = context;
    }
}
