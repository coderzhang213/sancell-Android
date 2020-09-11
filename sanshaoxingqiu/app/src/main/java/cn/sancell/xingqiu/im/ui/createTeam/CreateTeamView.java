package cn.sancell.xingqiu.im.ui.createTeam;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.res.TeamCreateRes;

public interface CreateTeamView extends BaseView {

    void teamCreateSuccess(TeamCreateRes res);

    void teamCreateError(String error);
}
