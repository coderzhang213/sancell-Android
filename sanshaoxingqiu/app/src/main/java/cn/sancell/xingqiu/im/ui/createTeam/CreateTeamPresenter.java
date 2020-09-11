package cn.sancell.xingqiu.im.ui.createTeam;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.TeamCreateReq;
import cn.sancell.xingqiu.im.entity.res.TeamCreateRes;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc: 创建群组
  * @time 2019-11-13 18:58
  */
public class CreateTeamPresenter extends BasePresenter<CreateTeamView> {
    private Context mContext;

    public CreateTeamPresenter(Context context) {
        mContext = context;
    }

    public void createTeam(TeamCreateReq req){

        ImModel.getInstance().createTeam(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<TeamCreateRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<TeamCreateRes> t) {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().teamCreateSuccess(t.getRetData());
                            }else{
                                getView().teamCreateError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {
                        if (getView() != null){
                            getView().teamCreateError(e.toString());
                        }
                    }
                });
    }
}
