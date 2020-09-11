package cn.sancell.xingqiu.homecommunity.video;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;

public interface VideoView extends BaseView {

    void getVideoGoodsSuccess(VideoRelationRes datas);

    void getVideoTeamSuccess(VideoRelationRes datas);

    void getError(String error);

    void addCartSuccess();

    void getVideoDetailSuccess(VideoDetailRes res);
}
