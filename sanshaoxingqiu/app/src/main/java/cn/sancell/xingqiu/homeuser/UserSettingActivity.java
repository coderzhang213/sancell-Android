package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.AppManager;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.contract.SettingContract;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.login.CodeLoginActivity;
import cn.sancell.xingqiu.util.DataCleanManager;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class UserSettingActivity extends BaseMVPToobarActivity<SettingContract.SettingPresenter>
        implements SettingContract.SettingView, View.OnClickListener {

    @BindView(R.id.rl_safe_center)
    RelativeLayout rl_safe_center;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rl_clear_cache;
    @BindView(R.id.tv_cache_size)
    TextView tv_cache_size;
    @BindView(R.id.rl_about_us)
    RelativeLayout rl_about_us;
    @BindView(R.id.tv_out_login)
    TextView tv_out_login;


    @Override
    protected SettingContract.SettingPresenter createPresenter() {
        return new SettingContract.SettingPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.setting_title);
        AppManager.getInstance().addActivity(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rl_safe_center.setOnClickListener(this);
        rl_clear_cache.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        tv_out_login.setOnClickListener(this);
        try {
            if (!DataCleanManager.getTotalCacheSize(UserSettingActivity.this).equals("0.0B")) {
                tv_cache_size.setText(DataCleanManager.getTotalCacheSize(UserSettingActivity.this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void outLoginSuccess() {
        ScClient.loginOut();
        UMShareAPI.get(UserSettingActivity.this).deleteOauth(UserSettingActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            startActivity(new Intent(UserSettingActivity.this, CodeLoginActivity.class));
            AppManager.getInstance().finishAllActivity();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            startActivity(new Intent(UserSettingActivity.this, CodeLoginActivity.class));
            AppManager.getInstance().finishAllActivity();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            startActivity(new Intent(UserSettingActivity.this, CodeLoginActivity.class));
            AppManager.getInstance().finishAllActivity();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_safe_center:
                startActivity(new Intent(UserSettingActivity.this, UserSafeCenterActivity.class));
                break;
            case R.id.rl_clear_cache:
                try {
                    if (DataCleanManager.getTotalCacheSize(UserSettingActivity.this).equals("0.0B")) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (loading_view != null) {
                    loading_view.show();
                }
                DataCleanManager.clearAllCache(UserSettingActivity.this);
                tv_cache_size.setText("");
                if (loading_view != null) {
                    loading_view.dismiss();
                }
                break;
            case R.id.rl_about_us:
                startActivity(new Intent(UserSettingActivity.this, UserSettingAboutUsActivity.class));
                break;
            case R.id.tv_out_login:
                mPresenter.OutLogin(UserSettingActivity.this);
                break;
        }
    }
}
