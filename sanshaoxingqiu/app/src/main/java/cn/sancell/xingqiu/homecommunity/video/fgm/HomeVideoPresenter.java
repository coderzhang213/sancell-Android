package cn.sancell.xingqiu.homecommunity.video.fgm;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.im.entity.req.VideoListReq;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2019-11-27 15:25
  */
public class HomeVideoPresenter extends BasePresenter<HomeVideoView> {

    private Context mContext;

    public HomeVideoPresenter(Context context){
        mContext = context;
    }

    public void getVideoList(VideoListReq req){

        ImModel.getInstance().getVideoList(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<CommunityVideoListBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<CommunityVideoListBean> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getVideoListSuccess(t.getRetData());
                            }else{
                                getView().getVideoListError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getVideoListError(e.toString());
                        }
                    }
                });
    }
}
