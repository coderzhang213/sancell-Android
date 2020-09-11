package cn.sancell.xingqiu.homecommunity.live;

import android.content.Context;
import android.icu.text.AlphabeticIndex;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.homecommunity.entity.LiveListReq;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveListRes;
import cn.sancell.xingqiu.im.entity.req.LiveRoomReq;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2019-11-27 14:05
  */
public class HomeLivePresenter extends BasePresenter<HomeLiveView> {

    private Context mContext;

    public HomeLivePresenter(Context context){
        mContext = context;
    }


    public void getLiveList(LiveListReq req){
        ImModel.getInstance().getLiveList(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<LiveData>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<LiveData> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getLiveListSuccess(t.getRetData());
                            }else{
                                getView().getLiveListError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getLiveListError(e.toString());
                        }
                    }
                });
    }

    /**
     * 获取房间信息
     * @param liveId
     */
    public void getLiveRommInfo(LiveRoomReq liveId){

    }

}
