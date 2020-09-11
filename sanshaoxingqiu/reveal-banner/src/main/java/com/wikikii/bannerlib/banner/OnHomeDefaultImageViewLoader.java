package com.wikikii.bannerlib.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wikikii.bannerlib.R;
import com.wikikii.bannerlib.banner.listener.OnLoadImageViewListener;


public abstract class OnHomeDefaultImageViewLoader implements OnLoadImageViewListener {

    @Override
    public View createImageView(Context context, boolean isScaleAnimation) {

        return LayoutInflater.from(context).inflate(R.layout.item_home_banner, null, false);
    }
}
