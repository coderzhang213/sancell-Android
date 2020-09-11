package cn.sancell.xingqiu.homeuser.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.sancell.xingqiu.base.BaseLazyMVPFragment;

/**
 * @Title: ListViewFragment
 * @Package com.gxz.stickynavlayout.fragments
 * @Description: 我的订单fragment适配器
 */
public class FragmentsViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<? extends BaseLazyMVPFragment> fragments;

    public FragmentsViewPagerAdapter(FragmentManager fm,
                                     List<? extends BaseLazyMVPFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments != null && position < fragments.size() ? fragments.get(position).getTitle() : "";
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        return super.instantiateItem(container, position);
    }
}
