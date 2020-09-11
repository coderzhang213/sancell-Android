package cn.sancell.xingqiu.homepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.fragment.ImageDetailFragment;
import cn.sancell.xingqiu.photoview.HackyViewPager;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    /**
     * 上个界面传来的图片数组
     */
    private List<String> data_altas;
    /**
     * 上个界面传来的显示的初始图片位置
     */
    private int init_postion;
    private HackyViewPager mPager;

    private ImageView iv_close;

    ImagePagerAdapter mAdapter;

    int currentItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_detail_pager);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        StatusBarUtil.setStatusBarDarkTheme(this, true);

        init_postion = getIntent().getIntExtra(Constants.Key.KEY_2, 0);
        data_altas = getIntent().getStringArrayListExtra(Constants.Key.KEY_1);
        iv_close =  findViewById(R.id.btn_back);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_close.getLayoutParams();
            lp.topMargin = statusHeight;
            iv_close.setLayoutParams(lp);
        }
        iv_close.setOnClickListener(view -> finish());
        mPager =  findViewById(R.id.pager);
        //initViewPager();
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), data_altas);
        mPager.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            init_postion = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(init_postion);
    }

    private void initViewPager() {
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (data_altas.size() - 1) && startX - endX > 0 && startX - endX >= (width / 6)) {
                            ImagePagerActivity.this.finish();
                        }
                        break;
                }
                return false;
            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
