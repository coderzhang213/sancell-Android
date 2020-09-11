package cn.sancell.xingqiu.im.ui;

import com.umeng.socialize.media.Base;

import java.util.HashMap;

import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.bean.CheckTeamReq;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.homecommunity.entity.LiveListReq;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveListRes;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.im.entity.req.AddCartReq;
import cn.sancell.xingqiu.im.entity.req.AddListReq;
import cn.sancell.xingqiu.im.entity.req.AddressReq;
import cn.sancell.xingqiu.im.entity.req.JoinGroupReq;
import cn.sancell.xingqiu.im.entity.req.LiveRoomReq;
import cn.sancell.xingqiu.im.entity.req.RpDetailReq;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.req.SendRpReq;
import cn.sancell.xingqiu.im.entity.req.TeamCreateReq;
import cn.sancell.xingqiu.im.entity.req.TeamNameReq;
import cn.sancell.xingqiu.im.entity.req.VideoDetailReq;
import cn.sancell.xingqiu.im.entity.req.VideoListReq;
import cn.sancell.xingqiu.im.entity.req.VideoRelationReq;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.entity.res.PayPassRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;
import cn.sancell.xingqiu.im.entity.res.SendRpRes;
import cn.sancell.xingqiu.im.entity.res.TeamCreateRes;
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes;
import cn.sancell.xingqiu.im.entity.res.VideoListRes;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.util.RetrofitUtil;
import io.reactivex.Observable;
import retrofit2.http.POST;

/**
  * @author Alan_Xiong
  *
  * @desc: im 通信model
  * @time 2019-11-13 17:07
  */
public class ImModel {

    public static class ImHolder{
        private static ImModel INSTANCE = new ImModel();
    }

    public static ImModel getInstance() {
        return ImHolder.INSTANCE;
    }

    //社群相关
    public Observable<BaseEntry<TeamCreateRes>> createTeam(TeamCreateReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().createTeam(map);
    }

    public Observable<BaseEntry<AddressRes>> getMyTeam(AddressReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getMyTeam(map);
    }


    //红包相关
    public Observable<BaseEntry<SendRpRes>> sendRp(SendRpReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().sendRp(map);

    }

    public Observable<BaseEntry<PayPassRes>> checkPayPass(BaseReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().checkPayPass(map);
    }

    public Observable<BaseEntry<RpGrabRes>> rpGrab(RpGrabReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().rpGrab(map);
    }

    public Observable<BaseEntry<RpDetailRes>> getRpDetails(RpDetailReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getRpDetail(map);
    }

    public Observable<BaseEntry<CheckRpRes>> checkRp(RpGrabReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().checkRp(map);
    }

    public Observable<BaseEntry<YueRes>> getRpYue(BaseReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getRpYue(map);
    }


    //视频关联商品
    public Observable<BaseEntry<VideoRelationRes>> getVideoRelation(VideoRelationReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getVideoRelation(map);
    }

    //加车
    public Observable<BaseEntry> addCart(AddCartReq req){
        HashMap<String,String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().addCart(map);
    }

    public Observable<BaseEntry<EmptyBean>> joinTeam(JoinGroupReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().joinTeam(map);
    }

    public Observable<BaseEntry<VideoDetailRes>> loadVideoDetail(VideoDetailReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getVidoDetail(map);
    }

    public Observable<BaseEntry<HomeBannerDataBean>> getAddList(AddListReq req){
        HashMap<String,String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getHomeBannerData(map);
    }

    public Observable<BaseEntry<ImAccountRes>> loginIm(BaseReq req){
        HashMap<String,String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().loginIm(map);
    }

    public Observable<BaseEntry<LiveData>> getLiveList(LiveListReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getLiveList(map);
    }

    public Observable<BaseEntry<CommunityVideoListBean>> getVideoList(VideoListReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getVideoList(map);
    }
    public Observable<BaseEntry<String >> getLiveRoomInfo(LiveRoomReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().getLiveRoomInfo(map);
    }

    public Observable<BaseEntry<EmptyBean>> saveTeamName(TeamNameReq req){
        HashMap<String,String> map = ConvertUtils.convertToMapPartRsa(req);
        return RetrofitUtil.getInstance().initRetrofit().saveTeamName(map);
    }

    public Observable<BaseEntry<EmptyBean>> checkInOtherGroup(CheckTeamReq req){
        HashMap<String,String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().checkInOtherTeam(map);
    }
}
