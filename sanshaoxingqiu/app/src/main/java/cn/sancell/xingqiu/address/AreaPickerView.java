package cn.sancell.xingqiu.address;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
 * Created by ZhouZi on 2018/9/29.
 * time:9:45
 * ----------Dragon be here!----------/
 * 　　　┏┓　　 ┏┓
 * 　　┏┛┻━━━┛┻┓━━━
 * 　　┃　　　　　 ┃
 * 　　┃　　　━　  ┃
 * 　　┃　┳┛　┗┳
 * 　　┃　　　　　 ┃
 * 　　┃　　　┻　  ┃
 * 　　┃　　　　   ┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛━━━━━
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━神兽出没━━━━━━━━━━━━━━
 */
public class AreaPickerView extends Dialog {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView iv_close;

    private AreaPickerViewCallback areaPickerViewCallback;
    /**
     * View的集合
     */
    private List<View> views;
    /**
     * tab的集合
     */
    private List<String> strings;
    /**
     * 省
     */
    private List<AddressBean> addressBeans;
    /**
     * 市
     */
    private List<AddressBean.CityBean> cityBeans;
    /**
     * 区
     */
    private List<AddressBean.CityBean.AreaBean> areaBeans;
    /**
     * 镇
     */
    private List<AddressBean.CityBean.AreaBean.TownBean> townBeans;
    private Context context;

    private ViewPagerAdapter viewPagerAdapter;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private AreaAdapter areaAdapter;
    private TownAdapter townAdapter;
    /**
     * 选中的区域下标 默认-1
     */
    private int provinceSelected = -1;
    private int citySelected = -1;
    private int areaSelected = -1;
    private int townSelected = -1;
    /**
     * 历史选中的区域下标 默认-1
     */
    private int oldProvinceSelected = -1;
    private int oldCitySelected = -1;
    private int oldAreaSelected = -1;
    private int oldTownSelected = -1;
    private RecyclerView areaRecyclerView;
    private RecyclerView cityRecyclerView;
    private RecyclerView townRecyclerView;
    /**
     * 街道选择时有暂不选择选项
     */
    /*private LinearLayout ll_noselect_item;
    private TextView tv_noselect_item;
    private ImageView iv_tv_noselect_item_mark;*/
    private boolean isCreate;
    private int[] initValue;


    public AreaPickerView(@NonNull Context context, int themeResId, List<AddressBean> addressBeans, int... value) {
        super(context, themeResId);
        this.addressBeans = addressBeans;
        this.context = context;
        this.initValue = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address_pickerview);
        Window window = this.getWindow();

        isCreate = true;

        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        //setOnDismissListener(onDismissListener);


        tabLayout = findViewById(R.id.tablayout);
        iv_close = findViewById(R.id.iv_dialog_close);
        viewPager = findViewById(R.id.view_pager);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        View provinceView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_address_select, null, false);
        View cityView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_address_select, null, false);
        View areaView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_address_select, null, false);
        View townView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_address_select, null, false);
        final RecyclerView provinceRecyclerView = provinceView.findViewById(R.id.recyclerview);
        cityRecyclerView = cityView.findViewById(R.id.recyclerview);
        areaRecyclerView = areaView.findViewById(R.id.recyclerview);
        townRecyclerView = townView.findViewById(R.id.recyclerview);
        /*ll_noselect_item = townView.findViewById(R.id.ll_noselect_item);
        tv_noselect_item = townView.findViewById(R.id.tv_noselect_item);
        iv_tv_noselect_item_mark = townView.findViewById(R.id.iv_tv_noselect_item_mark);*/
        views = new ArrayList<>();
        views.add(provinceView);
        views.add(cityView);
        views.add(areaView);
        views.add(townView);
        InitCount(initValue);
        /**
         * 配置adapter
         */
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabWidth(tabLayout, 0);
        /**
         * 这句话设置了过后，假如又3个tab 删除第三个 刷新过后 第二个划第三个会有弹性
         * viewPager.setOffscreenPageLimit(2);
         */

        provinceAdapter = new ProvinceAdapter(R.layout.item_dialog_address_select, addressBeans);
        provinceRecyclerView.setAdapter(provinceAdapter);
        LinearLayoutManager provinceManager = new LinearLayoutManager(context);
        provinceRecyclerView.setLayoutManager(provinceManager);
        provinceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("AreaPickerView", oldProvinceSelected + "~~~" + oldCitySelected + "~~~" + oldAreaSelected);
                cityBeans.clear();
                areaBeans.clear();
                townBeans.clear();
                addressBeans.get(position).setStatus(true);
                provinceSelected = position;
                if (oldProvinceSelected != -1 && oldProvinceSelected != provinceSelected) {
                    addressBeans.get(oldProvinceSelected).setStatus(false);
                    Log.e("AreaPickerView", "清空");
                }
                if (position != oldProvinceSelected) {
                    if (oldCitySelected != -1) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).setStatus(false);
                    }
                    if (oldAreaSelected != -1) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).setStatus(false);
                    }
                    if (oldTownSelected != -1) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).getChildren().get(oldTownSelected).setStatus(false);
                    }
                    oldCitySelected = -1;
                    oldAreaSelected = -1;
                    oldTownSelected = -1;
                }
                cityBeans.addAll(addressBeans.get(position).getChildren());
                provinceAdapter.notifyDataSetChanged();
                cityAdapter.notifyDataSetChanged();
                areaAdapter.notifyDataSetChanged();
                townAdapter.notifyDataSetChanged();
                strings.set(0, addressBeans.get(position).getLabel());
                if (strings.size() == 1) {
                    strings.add("请选择");
                } else if (strings.size() > 1) {
                    if (position != oldProvinceSelected) {
                        strings.set(1, "请选择");
                        if (strings.size() == 3) {
                            strings.remove(2);
                        } else if (strings.size() == 4) {
                            strings.remove(3);
                            strings.remove(2);
                        }
                    }
                }
                tabLayout.setupWithViewPager(viewPager);
                setTabWidth(tabLayout, 0);
                viewPagerAdapter.notifyDataSetChanged();
                tabLayout.getTabAt(1).select();
                oldProvinceSelected = provinceSelected;
            }
        });

        cityBeans = new ArrayList<>();
        cityAdapter = new CityAdapter(R.layout.item_dialog_address_select, cityBeans);
        LinearLayoutManager cityListManager = new LinearLayoutManager(context);
        cityRecyclerView.setLayoutManager(cityListManager);
        cityRecyclerView.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                areaBeans.clear();
                townBeans.clear();
                cityBeans.get(position).setStatus(true);
                citySelected = position;
                if (oldCitySelected != -1 && oldCitySelected != citySelected) {
                    addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).setStatus(false);
                }
                if (position != oldCitySelected) {
                    if (oldAreaSelected != -1 && cityBeans.get(position).getChildren() != null) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).setStatus(false);
                    }
                    if (oldTownSelected != -1 && cityBeans.get(position).getChildren() != null) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).getChildren().get(oldTownSelected).setStatus(false);
                    }
                    oldAreaSelected = -1;
                    oldTownSelected = -1;
                }
                oldCitySelected = citySelected;
                if (cityBeans.get(position).getChildren() != null) {
                    areaBeans.addAll(cityBeans.get(position).getChildren());
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    strings.set(1, cityBeans.get(position).getLabel());
                    if (strings.size() == 2) {
                        strings.add("请选择");
                    } else if (strings.size() == 3) {
                        strings.set(2, "请选择");
                    } else if (strings.size() == 4) {
                        strings.remove(3);
                    }
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(2).select();
                } else {
                    oldAreaSelected = -1;
                    oldTownSelected = -1;
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    townAdapter.notifyDataSetChanged();
                    strings.set(1, cityBeans.get(position).getLabel());
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    dismiss();
                    areaPickerViewCallback.callback(provinceSelected, citySelected);
                }
            }
        });

        areaBeans = new ArrayList<>();
        areaAdapter = new AreaAdapter(R.layout.item_dialog_address_select, areaBeans);
        LinearLayoutManager areaListManager = new LinearLayoutManager(context);
        areaRecyclerView.setLayoutManager(areaListManager);
        areaRecyclerView.setAdapter(areaAdapter);
        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                townBeans.clear();
                areaBeans.get(position).setStatus(true);
                areaSelected = position;
                if (oldAreaSelected != -1 && oldAreaSelected != areaSelected) {
                    addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).setStatus(false);
                }
                if (position != oldAreaSelected) {
                    if (oldTownSelected != -1 && areaBeans.get(position).getChildren() != null) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).getChildren().get(oldTownSelected).setStatus(false);
                    }
                    oldTownSelected = -1;
                }
                oldAreaSelected = areaSelected;
                if (areaBeans.get(position).getChildren() != null) {
                    townBeans.addAll(areaBeans.get(position).getChildren());
                    areaAdapter.notifyDataSetChanged();
                    townAdapter.notifyDataSetChanged();
                    strings.set(2, areaBeans.get(position).getLabel());
                    if (strings.size() == 3) {
                        strings.add("请选择");
                    } else if (strings.size() == 4) {
                        strings.set(3, "请选择");
                    }
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(3).select();
                } else {
                    oldTownSelected = -1;
                    areaAdapter.notifyDataSetChanged();
                    townAdapter.notifyDataSetChanged();
                    strings.set(2, areaBeans.get(position).getLabel());
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    dismiss();
                    areaPickerViewCallback.callback(provinceSelected, citySelected, areaSelected);
                }

            }
        });
        townBeans = new ArrayList<>();
        townAdapter = new TownAdapter(R.layout.item_dialog_address_select, townBeans);
        LinearLayoutManager townListManager = new LinearLayoutManager(context);
        townRecyclerView.setLayoutManager(townListManager);
        townRecyclerView.setAdapter(townAdapter);
        townAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                strings.set(3, townBeans.get(position).getLabel());
                tabLayout.setupWithViewPager(viewPager);
                setTabWidth(tabLayout, 0);
                viewPagerAdapter.notifyDataSetChanged();
                townBeans.get(position).setStatus(true);
                townSelected = position;
                if (oldTownSelected != -1 && oldTownSelected != position) {
                    townBeans.get(oldTownSelected).setStatus(false);
                }
                oldTownSelected = townSelected;
                townAdapter.notifyDataSetChanged();
                /*tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_text1));
                iv_tv_noselect_item_mark.setVisibility(View.GONE);*/
                dismiss();
                areaPickerViewCallback.callback(provinceSelected, citySelected, areaSelected, townSelected);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        provinceRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
                        break;
                    case 1:
                        cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
                        break;
                    case 2:
                        areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
                        break;
                    case 3:
                        townRecyclerView.scrollToPosition(oldTownSelected == -1 ? 0 : oldTownSelected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        /*ll_noselect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_theme));
                iv_tv_noselect_item_mark.setVisibility(View.VISIBLE);
                strings.set(3, "暂不选择");
                tabLayout.setupWithViewPager(viewPager);
                setTabWidth(tabLayout, 0);
                viewPagerAdapter.notifyDataSetChanged();
                if (oldTownSelected != -1) {
                    townBeans.get(oldTownSelected).setStatus(false);
                }
                townSelected = -1;
                oldTownSelected = -1;
                townAdapter.notifyDataSetChanged();
                dismiss();
                areaPickerViewCallback.callback(provinceSelected, citySelected, areaSelected, townSelected);
            }
        });*/
        setSelect(initValue);
    }


    /*OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            if (provinceSelected != -1 && citySelected != -1 && areaSelected != -1 && townSelected == -1) {
                areaPickerViewCallback.callback(provinceSelected, citySelected, areaSelected);
            }
        }
    };*/


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return strings.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            Log.e("AreaPickView", "------------instantiateItem");
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
            Log.e("AreaPickView", "------------destroyItem");
        }

    }

    public interface AreaPickerViewCallback {
        void callback(int... value);
    }

    public void setAreaPickerViewCallback(AreaPickerViewCallback areaPickerViewCallback) {
        this.areaPickerViewCallback = areaPickerViewCallback;
    }

    public void InitCount(int... value) {
        strings = new ArrayList<>();
        if (value == null) {
            strings.add("请选择");
        } else {
            if (value.length == 4) {
                strings.add(addressBeans.get(value[0]).getLabel());
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                /*if (value[3] == -1) {
                    strings.add("暂无选择");
                    tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_theme));
                    iv_tv_noselect_item_mark.setVisibility(View.VISIBLE);
                } else {*/
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).getLabel());
                //}
            }
            if (value.length == 3) {
                strings.add(addressBeans.get(value[0]).getLabel());
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
            }
            if (value.length == 2) {
                strings.add(addressBeans.get(value[0]).getLabel());
                strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
            }

        }
    }

    public void reset() {
        tabLayout.setupWithViewPager(viewPager);
        setTabWidth(tabLayout, 0);
    }

    public void setSelect(int... value) {
        if (value == null) {
            if (isCreate) {
                tabLayout.setupWithViewPager(viewPager);
                setTabWidth(tabLayout, 0);
                viewPagerAdapter.notifyDataSetChanged();
                tabLayout.getTabAt(0).select();
                if (provinceSelected != -1)
                    addressBeans.get(provinceSelected).setStatus(false);
                if (citySelected != -1)
                    addressBeans.get(provinceSelected).getChildren().get(citySelected).setStatus(false);
                if (areaSelected != -1) {
                    addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).setStatus(false);
                }
                if (townSelected != -1) {
                    addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).getChildren().get(townSelected).setStatus(false);
                }
                cityBeans.clear();
                areaBeans.clear();
                townBeans.clear();
                provinceAdapter.notifyDataSetChanged();
                cityAdapter.notifyDataSetChanged();
                areaAdapter.notifyDataSetChanged();
                townAdapter.notifyDataSetChanged();
            }
            return;
        } else {
            if (isCreate) {
                if (value.length == 4) {
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(value.length - 1).select();
                    if (provinceSelected != -1)
                        addressBeans.get(provinceSelected).setStatus(false);
                    if (citySelected != -1)
                        addressBeans.get(provinceSelected).getChildren().get(citySelected).setStatus(false);
                    if (areaSelected != -1)
                        addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).setStatus(false);
                    if (townSelected != -1)
                        addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).getChildren().get(townSelected).setStatus(false);
                    /*if (townSelected != -1) {
                        tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_text1));
                        iv_tv_noselect_item_mark.setVisibility(View.GONE);
                        addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).getChildren().get(townSelected).setStatus(false);
                    } else {
                        tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_theme));
                        iv_tv_noselect_item_mark.setVisibility(View.VISIBLE);
                    }*/
                    provinceSelected = value[0];
                    citySelected = value[1];
                    areaSelected = value[2];
                    townSelected = value[3];
                    addressBeans.get(value[0]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).setStatus(true);
                    /*if (townSelected != -1) {
                        tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_text1));
                        iv_tv_noselect_item_mark.setVisibility(View.GONE);
                        addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).setStatus(true);
                    } else {
                        tv_noselect_item.setTextColor(context.getResources().getColor(R.color.color_theme));
                        iv_tv_noselect_item_mark.setVisibility(View.VISIBLE);
                    }*/
                    cityBeans.clear();
                    cityBeans.addAll(addressBeans.get(value[0]).getChildren());
                    areaBeans.clear();
                    areaBeans.addAll(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren());
                    townBeans.clear();
                    townBeans.addAll(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren());
                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    townAdapter.notifyDataSetChanged();
                    oldProvinceSelected = value[0];
                    oldCitySelected = value[1];
                    oldAreaSelected = value[2];
                    oldTownSelected = value[3];
                    townRecyclerView.scrollToPosition(oldTownSelected == -1 ? 0 : oldTownSelected);
                }

                if (value.length == 3) {
                    strings.add(addressBeans.get(value[0]).getLabel());
                    strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
                    strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(value.length - 1).select();
                    if (provinceSelected != -1)
                        addressBeans.get(provinceSelected).setStatus(false);
                    if (citySelected != -1)
                        addressBeans.get(provinceSelected).getChildren().get(citySelected).setStatus(false);
                    addressBeans.get(value[0]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).setStatus(true);
                    cityBeans.clear();
                    cityBeans.addAll(addressBeans.get(value[0]).getChildren());
                    areaBeans.clear();
                    areaBeans.addAll(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren());
                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    oldProvinceSelected = value[0];
                    oldCitySelected = value[1];
                    oldAreaSelected = value[2];
                    areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
                }

                if (value.length == 2) {
                    strings.add(addressBeans.get(value[0]).getLabel());
                    strings.add(addressBeans.get(value[0]).getChildren().get(value[1]).getLabel());
                    tabLayout.setupWithViewPager(viewPager);
                    setTabWidth(tabLayout, 0);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(value.length - 1).select();
                    addressBeans.get(provinceSelected).setStatus(false);
                    addressBeans.get(provinceSelected).getChildren().get(citySelected).setStatus(false);
                    addressBeans.get(value[0]).setStatus(true);
                    addressBeans.get(value[0]).getChildren().get(value[1]).setStatus(true);
                    cityBeans.clear();
                    cityBeans.addAll(addressBeans.get(value[0]).getChildren());
                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.notifyDataSetChanged();
                    oldProvinceSelected = value[0];
                    oldCitySelected = value[1];
                    oldAreaSelected = -1;
                    cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
                }
            }
        }

    }

    public void setTabWidth(final TabLayout tabLayout, int padding) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);


                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = ScreenUtils.dip2px(context, 12);
                        params.rightMargin = ScreenUtils.dip2px(context, 12);
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
