package cn.sancell.xingqiu.homepage.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.base.BaseLazyFragment;
import cn.sancell.xingqiu.homeclassify.ClassifyThirdFragment;
import cn.sancell.xingqiu.homeclassify.bean.ScreeningInfoEntity;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ClassifyPagerAdapter extends FragmentStatePagerAdapter {
    private List<ClassifyThirdFragment> fragmentList = new ArrayList<>();

    public ClassifyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(List<ScreeningInfoEntity> list) {
        fragmentList.clear();
        for (ScreeningInfoEntity info : list) {
            fragmentList.add(ClassifyThirdFragment.newInstance(info));
        }
    }

    public void refreshAllFragment(List<ScreeningInfoEntity> list) {
        for (ScreeningInfoEntity info : list) {
            for (ClassifyThirdFragment fragment : fragmentList) {
                //最好使用唯一标示来判定是否刷了正确的Fragment 比如id
                String pageTitle = fragment.getTitle();
                if (pageTitle != null && pageTitle.equals(info.getThird_classify_name())) {
                    fragment.refreshData(info);
                }
            }
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList != null && position < fragmentList.size()) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (getItem(position) instanceof BaseLazyFragment) {
            return ((BaseLazyFragment) getItem(position)).getTitle();
        }
        return super.getPageTitle(position);
    }
}
