package cn.sancell.xingqiu.bean;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.HomeClassifyFragment;
import cn.sancell.xingqiu.homecommunity.HomeCommunityFragment;
import cn.sancell.xingqiu.homepage.HomeFragment;
import cn.sancell.xingqiu.homeshoppingcar.HomeShoppingCarFragment;
import cn.sancell.xingqiu.homeuser.HomeUserFragment;

/**
 * Created by huyingying on 2019/5/8.
 * 底部切换资源
 */

public class TabDb {
    public static final String TAB_HOME = "tab_home";
    public static final String TAB_CLASS = "tab_class";
    public static final String TAB_GROUP = "tab_group";
    public static final String TAB_CART = "tab_cart";
    public static final String TAB_MY = "tab_my";

    /***
     * 获得底部所有项
     */
    public static String[] getTabsTxt() {
        String[] tabs = {"首页", "分类", "社群", "购物车", "我的"};
        return tabs;
    }

    /***
     * 获得所有碎片
     */
    public static Class[] getFramgent() {
        Class[] cls = {HomeFragment.class, HomeClassifyFragment.class, HomeCommunityFragment.class, HomeShoppingCarFragment.class, HomeUserFragment.class};
        return cls;
    }

    /***
     * 获得所有点击前的图片
     */
    public static int[] getTabsImg() {
        int[] img = {R.mipmap.icon_main_tab1, R.mipmap.icon_main_tab2, R.mipmap.icon_main_tab5, R.mipmap.icon_main_tab3, R.mipmap.icon_main_tab4,};
        return img;
    }

    /***
     * 获得所有点击后的图片
     */
    public static int[] getTabsImgLight() {
        int[] img = {R.mipmap.icon_main_tab1_yes, R.mipmap.icon_main_tab2_yes, R.mipmap.icon_main_tab5_yes, R.mipmap.icon_main_tab3_yes, R.mipmap.icon_main_tab4_yes,};
        return img;
    }
}
