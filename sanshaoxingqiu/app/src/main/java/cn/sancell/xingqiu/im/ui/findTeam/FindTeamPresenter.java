package cn.sancell.xingqiu.im.ui.findTeam;

import android.content.Context;

import cn.sancell.xingqiu.base.mvp.BasePresenter;

public class FindTeamPresenter extends BasePresenter<FindTeamView> {

    private Context mContext;

    public FindTeamPresenter(Context context){
        mContext = context;
    }
}
