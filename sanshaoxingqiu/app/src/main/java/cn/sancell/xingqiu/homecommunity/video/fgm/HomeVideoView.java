package cn.sancell.xingqiu.homecommunity.video.fgm;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.im.entity.res.VideoListRes;

public interface HomeVideoView extends BaseView {

    void getVideoListSuccess(CommunityVideoListBean res);

    void getVideoListError(String error);
}
