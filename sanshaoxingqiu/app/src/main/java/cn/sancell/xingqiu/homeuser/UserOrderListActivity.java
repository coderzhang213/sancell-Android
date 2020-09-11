package cn.sancell.xingqiu.homeuser;

import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.adapter.FragmentsViewPagerAdapter;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class UserOrderListActivity extends BaseToobarActivity implements View.OnClickListener {
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_nopay)
    TextView tv_nopay;
    @BindView(R.id.tv_undelivered)
    TextView tv_undelivered;
    @BindView(R.id.tv_delivered)
    TextView tv_delivered;
    @BindView(R.id.tv_finished)
    TextView tv_finished;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private FragmentsViewPagerAdapter listPagerAdapter;
    private List<BaseLazyMVPFragment> fragments = new ArrayList<>();

    private int initPos;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_order_list;
    }


    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.order_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        initPos=getIntent().getIntExtra(Constants.Key.KEY_1,0);
        tv_all.setOnClickListener(this);
//        tv_nopay.setOnClickListener(this);
//        tv_delivered.setOnClickListener(this);
        tv_undelivered.setOnClickListener(this);
        tv_finished.setOnClickListener(this);

        fragments.add(new UserOrderAllListFragment());
       // fragments.add(new UserOrderNoPayListFragment());
        fragments.add(new UserOrderUndeliveredListFragment());
//        fragments.add(new UserOrderDeliveredListFragment());
        fragments.add(new UserOrderFinishedListFragment());
        listPagerAdapter = new FragmentsViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(listPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_all.setTextColor(getResources().getColor(R.color.color_text1));
                        tv_nopay.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_undelivered.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_delivered.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_finished.setTextColor(getResources().getColor(R.color.color_text3));
                        break;
//                    case 1:
//                        tv_all.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_nopay.setTextColor(getResources().getColor(R.color.color_text1));
//                        tv_undelivered.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_delivered.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_finished.setTextColor(getResources().getColor(R.color.color_text3));
//                        break;
                    case 1:
                        tv_all.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_nopay.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_undelivered.setTextColor(getResources().getColor(R.color.color_text1));
                        tv_delivered.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_finished.setTextColor(getResources().getColor(R.color.color_text3));
                        break;
//                    case 3:
//                        tv_all.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_nopay.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_undelivered.setTextColor(getResources().getColor(R.color.color_text3));
//                        tv_delivered.setTextColor(getResources().getColor(R.color.color_text1));
//                        tv_finished.setTextColor(getResources().getColor(R.color.color_text3));
//                        break;
                    case 2:
                        tv_all.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_nopay.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_undelivered.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_delivered.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_finished.setTextColor(getResources().getColor(R.color.color_text1));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(initPos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                viewPager.setCurrentItem(0);
                break;
//            case R.id.tv_nopay:
//                viewPager.setCurrentItem(1);
//                break;
            case R.id.tv_undelivered:
                viewPager.setCurrentItem(1);
                break;
//            case R.id.tv_delivered:
//                viewPager.setCurrentItem(3);
//                break;
            case R.id.tv_finished:
                viewPager.setCurrentItem(2);
                break;

        }
    }
}
