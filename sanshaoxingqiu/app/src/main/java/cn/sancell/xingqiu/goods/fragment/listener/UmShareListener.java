package cn.sancell.xingqiu.goods.fragment.listener;

import android.os.Handler;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.sancell.xingqiu.SCApp;

public class UmShareListener implements UMShareListener {
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享成功"), 1000);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享失败"), 1000);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
