package cn.sancell.xingqiu.live.nim;

import java.io.Serializable;

/**
 * Created by zhukkun on 12/20/16.
 */
public class PublishParam implements Serializable {
    public static final String EXTRA_PARAMS = "extra_params";
    public static final String HD = "HD";//高清
    public static final String SD = "SD";//标清
    public static final String LD = "LD";// 流畅
    public String pushUrl = null;
    public String definition = HD; // HD: 高清   SD: 标清    LD: 流畅
    public boolean useFilter = false; //是否开启滤镜
    public boolean faceBeauty = false; //是否开启美颜
    public boolean openVideo = true;
    public boolean openAudio = true;

}
