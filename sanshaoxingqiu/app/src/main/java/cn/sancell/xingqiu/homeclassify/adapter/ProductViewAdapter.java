package cn.sancell.xingqiu.homeclassify.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by ai11 on 2019/6/16.
 */

public class ProductViewAdapter extends PagerAdapter {
    private List<View> viewList;//数据源

    public ProductViewAdapter(List<View> viewList) {

        this.viewList = viewList;
    }

    //数据源的数目
    public int getCount() {

        return viewList.size();
    }


    //view是否由对象产生，官方写arg0==arg1即可
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;

    }


    //销毁一个页卡(即ViewPager的一个item)
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }


    //对应页卡添加上数据
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(viewList.get(position));//千万别忘记添加到container
        return viewList.get(position);
    }
}
