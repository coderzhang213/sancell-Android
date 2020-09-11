package cn.sancell.xingqiu.usermember;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class MemberPrivilegeInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    @BindView(R.id.wb_content)
    WebView wb_content;

    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_privilege_info);
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(MemberPrivilegeInfoActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 440 / 375));
        btn_back.setOnClickListener(this);
        url = getIntent().getStringExtra(Constants.Key.KEY_1);
        WebSettings ws = wb_content.getSettings();
        ws.setAllowFileAccess(false);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setBuiltInZoomControls(false);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptEnabled(true);
        wb_content.setBackgroundColor(0);
        wb_content.getBackground().mutate().setAlpha(0);
        ws.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wb_content.loadUrl(url);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
