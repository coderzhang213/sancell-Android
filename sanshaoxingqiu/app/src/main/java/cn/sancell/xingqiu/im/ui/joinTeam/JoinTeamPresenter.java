package cn.sancell.xingqiu.im.ui.joinTeam;

import android.content.Context;

import cn.sancell.xingqiu.base.mvp.BasePresenter;

public class JoinTeamPresenter extends BasePresenter<JoinTeamView> {

    private Context mContext;

    public JoinTeamPresenter(Context context){
        mContext = context;
    }
}
