package cn.sancell.xingqiu.bean;

import android.graphics.Bitmap;

import com.umeng.socialize.media.UMImage;

import java.io.Serializable;

public class LiveShareBean implements Serializable {


    public UMImage image;

    public String pageUrl;

    public String shareTitle;

    public String shareDesc;

    public Bitmap bitmap;

    public String path; //小程序路径


    public LiveShareBean(String pageUrl, String shareTitle, String shareDesc) {
        this.pageUrl = pageUrl;
        this.shareTitle = shareTitle;
        this.shareDesc = shareDesc;
    }

    public LiveShareBean(UMImage image, String pageUrl, String shareTitle, String shareDesc) {
        this.image = image;
        this.pageUrl = pageUrl;
        this.shareTitle = shareTitle;
        this.shareDesc = shareDesc;
    }
}
