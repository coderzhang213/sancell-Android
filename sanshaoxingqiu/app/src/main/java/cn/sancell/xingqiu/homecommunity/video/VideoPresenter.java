package cn.sancell.xingqiu.homecommunity.video;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Text;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.im.entity.req.AddCartReq;
import cn.sancell.xingqiu.im.entity.req.VideoDetailReq;
import cn.sancell.xingqiu.im.entity.req.VideoRelationReq;
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.im.ui.ImModel;

/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2019-11-21 20:57
  */
public class VideoPresenter extends BasePresenter<VideoView> {

    private Context mContext;

    public VideoPresenter(Context context) {
        this.mContext = context;
    }


    public void getReCommendTeam(VideoRelationReq relationReq) {
        ImModel.getInstance().getVideoRelation(relationReq).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<VideoRelationRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<VideoRelationRes> t) throws Exception {
                        if (getView() != null) {
                            if (t.getRetCode() == 0) {
                                if (TextUtils.equals(relationReq.type, "a")) { //商品
                                    getView().getVideoGoodsSuccess(t.getRetData());
                                } else {
                                    getView().getVideoTeamSuccess(t.getRetData());
                                }
                            } else {
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    public void addCart(AddCartReq req){
        ImModel.getInstance().addCart(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().addCartSuccess();
                            }else{
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    public void getVideoDetail(VideoDetailReq req){
        ImModel.getInstance().loadVideoDetail(req).compose(RxUtils.transform(getView()))
                .subscribe(new BaseObserver<VideoDetailRes>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<VideoDetailRes> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getVideoDetailSuccess(t.getRetData());
                            }else{
                                getView().getError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getError(e.toString());
                        }
                    }
                });
    }

}
