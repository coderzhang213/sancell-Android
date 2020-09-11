package cn.sancell.xingqiu.homecommunity.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cn.sancell.xingqiu.homecommunity.video.fgm.VideoPlayItemFragment;

/**
 * Created by zj on 2019/12/20.
 */
public class VideoPlayItemAdapter extends FragmentStatePagerAdapter {
    private int countSize = 0;

    public VideoPlayItemAdapter(@NonNull FragmentManager fm, int countSize) {
        super(fm);
        this.countSize = countSize;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        VideoPlayItemFragment mVideoPlayItemFragment = new VideoPlayItemFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt("postion", position + 1);
        mVideoPlayItemFragment.setArguments(mBundle);
        return mVideoPlayItemFragment;
    }

    @Override
    public int getCount() {
        return countSize;
    }
}
