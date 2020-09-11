package cn.sancell.xingqiu.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/6/17.
 */

public abstract class BaseToobarActivity extends BaseActivity {
    View view;
    /**
     * 头部布局
     */
    protected Toolbar toolbar;
    /**
     * 返回按钮和功能按钮
     */
    protected ImageView btn_back;
    protected ImageView btn_fun;
    protected TextView tv_fun;
    /**
     * 头部展示的标题
     */
    protected TextView tv_title;
    protected LinearLayout layout_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.layout_act_activity_toolbar, null);
        setContentView(getLayoutResId());
        initial();
        AppManager.getInstance().addActivity(this );

    }
    protected abstract int getLayoutResId();

    protected abstract void initial();

    /**
     * 初始化 得到标题中的控件 并设定后退按钮事件
     *
     * @param title 标题展示文字
     */
    protected void initActivityTitle(String title) {
        initActivityTitle(title, true, 0, null, 0);
    }


    /**
     * 初始化 得到标题中的控件 并设定后退按钮事件
     *
     * @param titleResId 标题展示文字的对应索引
     */
    protected void initActivityTitle(int titleResId) {
        initActivityTitle(getString(titleResId));
    }

    /**
     * 初始化 得到标题中的控件 并设定后退,导航按钮事件
     *
     * @param title      标题展示文字
     * @param isShowBack 是否显示回退按钮
     */
    protected void initActivityTitle(String title, boolean isShowBack, int type) {
        initActivityTitle(title, isShowBack, 0, null, type);
    }

    /**
     * 初始化 得到标题中的控件 并设定后退按钮事件
     *
     * @param titleResId 标题展示文字的对应索引
     * @param isShowBack 是否显示回退按钮
     */
    protected void initActivityTitle(int titleResId, boolean isShowBack,
                                     int type) {
        initActivityTitle(getString(titleResId), isShowBack, type);
    }

    /**
     * 初始化 得到标题中的控件 并设定功能按钮事件
     *
     * @param title              标题展示文字
     * @param resid              功能按钮的展示图片的对应索引
     * @param funOnClickListener 功能按钮事件
     */
    protected void initActivityTitle(String title, int resid,
                                     View.OnClickListener funOnClickListener) {

        initActivityTitle(title, true, resid, funOnClickListener, 0);

    }

    /**
     * 初始化 得到标题中的控件 并设定功能按钮事件
     *
     * @param titleResId         标题展示文字的对应索引
     * @param resid              功能按钮的展示图片的对应索引
     * @param funOnClickListener 功能按钮事件
     */
    protected void initActivityTitle(int titleResId, int resid,
                                     View.OnClickListener funOnClickListener) {
        initActivityTitle(getString(titleResId), resid, funOnClickListener);
    }

    /**
     * 初始化 得到标题中的控件 并设定功能按钮事件 和返回按钮
     *
     * @param title              标题展示文字
     * @param isShowBack         是否显示回退按钮
     * @param resid              功能按钮的展示图片的对应索引
     * @param funOnClickListener 功能按钮事件
     */
    @SuppressLint("RestrictedApi")
    protected void initActivityTitle(String title, boolean isShowBack,
                                     int resid, View.OnClickListener funOnClickListener, int type) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tv_fun=(TextView)view.findViewById(R.id.tv_fun);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        btn_fun = (ImageView) view.findViewById(R.id.btn_fun);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        /*
		 * 回退的展示设定
		 */
        if (null != btn_back) {
            if (type == 0) {
                btn_back.setOnClickListener(clickListener);
            }
            btn_back.setVisibility(isShowBack ? View.VISIBLE : View.INVISIBLE);
        }
		/*
		 * 标题的展示设定
		 */
        if (null != tv_title) {
            if (!StringUtils.isTextEmpty(title)) {
                tv_title.setText(title);
            }
        }

		/*
		 * 功能按钮的展示的图片和调用函数的设定
		 */
        if (null != btn_fun) {
            if (resid > 0 && null != funOnClickListener) {
                btn_fun.setVisibility(View.VISIBLE);
                btn_fun.setBackgroundResource(resid);
                btn_fun.setOnClickListener(funOnClickListener);
            } else {
                btn_fun.setVisibility(View.INVISIBLE);
                btn_fun.setOnClickListener(null);
            }
        }
    }

    /**
     * 初始化 得到标题中的控件 并设定功能按钮事件 和返回按钮
     *
     * @param title                  标题展示文字
     * @param isShowBack             是否显示回退按钮
     * @param resid                  功能按钮的展示图片的对应索引
     * @param funOnClickListener     功能按钮事件
     * @param type(0:正常返回,1：自定义返回事件) 返回按钮事件类型
     * @param backOnClick            自定义返回按钮事件
     */
    @SuppressLint("RestrictedApi")
    protected void initActivityTitle(String title, boolean isShowBack,
                                     int resid, View.OnClickListener funOnClickListener, int type, View.OnClickListener backOnClick) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tv_fun=(TextView)view.findViewById(R.id.tv_fun);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        btn_fun = (ImageView) view.findViewById(R.id.btn_fun);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        /*
		 * 回退的展示设定
		 */
        if (null != btn_back) {
            if (type == 0) {
                btn_back.setOnClickListener(clickListener);
            } else if (type == 1) {
                btn_back.setOnClickListener(backOnClick);
            }
            btn_back.setVisibility(isShowBack ? View.VISIBLE : View.INVISIBLE);
        }
		/*
		 * 标题的展示设定
		 */
        if (null != tv_title) {
            if (null != title) {
                tv_title.setText(title);
            }
        }
		/*
		 * 功能按钮的展示的图片和调用函数的设定
		 */
        if (null != btn_fun) {
            if (resid > 0 && null != funOnClickListener) {
                btn_fun.setVisibility(View.VISIBLE);
                btn_fun.setBackgroundResource(resid);
                btn_fun.setOnClickListener(funOnClickListener);
            } else {
                btn_fun.setVisibility(View.INVISIBLE);
                btn_fun.setOnClickListener(null);
            }
        }
    }


    /**
     * 初始化 得到标题中的控件 并设定功能按钮事件 和返回按钮
     *
     * @param titleResId         标题展示文字的对应索引
     * @param isShowBack         是否显示回退按钮
     * @param resid              功能按钮的展示图片的对应索引
     * @param funOnClickListener 功能按钮事件
     */
    protected void initActivityTitle(int titleResId, boolean isShowBack,
                                     int resid, View.OnClickListener funOnClickListener) {
        initActivityTitle(getString(titleResId), isShowBack, resid,
                funOnClickListener, 0);
    }

    // 设定后退按钮事件
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            BaseToobarActivity.this.finish();
        }
    };
    // 设定后退按钮事件


    /**
     * 重新父类的设置主view的方法
     */
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        setActivityView(view, null);
    };

    /**
     * 重新父类的设置主view的方法
     */
    @Override
    public void setContentView(View view) {
        setActivityView(view, null);
    }

    /**
     * 重新父类的设置主view的方法
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        setActivityView(view, params);
    }

    /**
     * 设置带有标题的activity
     *
     * @param v
     *            内容
     */
    private void setActivityView(View v, ViewGroup.LayoutParams params) {
        layout_content = (LinearLayout) view.findViewById(R.id.center);
        if (params == null) {
            layout_content.addView(v, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            layout_content.addView(v, params);
        }
        super.setContentView(view);
    }
}
