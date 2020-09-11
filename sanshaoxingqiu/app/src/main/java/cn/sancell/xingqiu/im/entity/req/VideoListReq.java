package cn.sancell.xingqiu.im.entity.req;

import cn.sancell.xingqiu.base.base.BaseReq;

public class VideoListReq extends BaseReq {

    public String page;
    public String pageSize;
    public String type; //1：上传的视频；2：直播回放）
}
