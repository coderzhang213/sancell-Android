package com.lcw.library.imagepicker.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lcw.library.imagepicker.utils.StatusBarUtil;

/**
 * BaseActivity基类
 * Create by: chenWei.li
 * Date: 2018/10/9
 * Time: 下午11:34
 * Email: lichenwei.me@foxmail.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mView == null) {
            mView = View.inflate(this, bindLayout(), null);
        }
        setContentView(mView);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initConfig();
        initView();
        initListener();
        getData();
    }


    protected abstract int bindLayout();

    protected void initConfig() {
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void getData();


}
