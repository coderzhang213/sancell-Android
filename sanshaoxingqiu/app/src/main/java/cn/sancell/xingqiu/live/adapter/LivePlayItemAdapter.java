package cn.sancell.xingqiu.live.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.bean.RecomLiveInfo;
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener;
import cn.sancell.xingqiu.ktenum.LivePlayType;
import cn.sancell.xingqiu.live.play.LivePlayBaseFragment;


/**
 * Created by zj on 2019/12/20.
 */
public class LivePlayItemAdapter extends FragmentStatePagerAdapter {
    private List<RecomLiveInfo> mList;
    private OnAddRecommendLinsener mOnAddRecommendLinsener;
    private List<Fragment> fragments;
    private String mType;

    public LivePlayItemAdapter(@NonNull FragmentManager fm, String mType, List<RecomLiveInfo> mList, OnAddRecommendLinsener mOnAddRecommendLinsener) {
        super(fm);
        this.mList = mList;
        this.mType = mType;
        this.mOnAddRecommendLinsener = mOnAddRecommendLinsener;
        this.fragments = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = LivePlayBaseFragment.Companion.getInstet(mType, mList.get(position).getRecId(), position, mOnAddRecommendLinsener);
        fragments.add(mFragment);
        return mFragment;
    }

    /**
     * 获取当前viewPager显示的fragment
     *
     * @return
     */
    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setmList(List<RecomLiveInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
