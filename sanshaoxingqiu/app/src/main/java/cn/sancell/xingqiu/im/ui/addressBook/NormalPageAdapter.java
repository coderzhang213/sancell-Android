package cn.sancell.xingqiu.im.ui.addressBook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.sancell.xingqiu.im.entity.TabEntity;

/**
 * @author Alan_Xiong
 * @desc: pager adapter
 * @time 2019-11-14 13:42
 */
public class NormalPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    private List<TabEntity> tabs;

    public NormalPageAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<TabEntity> tabs) {
        super(fm);
        mFragments = fragments;
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    public List<Fragment> getmFragments() {
        return this.mFragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs != null ? tabs.get(position).tab_name : "";
    }

    /**
     * 修改标题
     * @param position
     * @param title
     */
    public void setPageTitle(int position, String title) {
        if (position >= 0 && position < tabs.size()) {

            tabs.get(position).tab_name = title;
            notifyDataSetChanged();
        }
    }
}
