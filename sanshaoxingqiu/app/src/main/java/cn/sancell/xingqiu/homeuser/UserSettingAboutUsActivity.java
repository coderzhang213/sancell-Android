package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.constant.VipManager;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class UserSettingAboutUsActivity extends BaseToobarActivity implements View.OnClickListener {
    @BindView(R.id.rl_user_agreement)
    RelativeLayout rl_user_agreement;
    @BindView(R.id.rl_privacy_agreement)
    RelativeLayout rl_privacy_agreement;
    @BindView(R.id.rl_red_packet_agreement)
    RelativeLayout rl_red_packet_agreement;
    @BindView(R.id.rl_member_agreement)
    RelativeLayout rl_member_agreement;
    @BindView(R.id.rl_contact_us)
    RelativeLayout rl_contact_us;
    @BindView(R.id.tv_version)
    TextView tv_version;

    private UserBean userBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_setting_about_us;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.about_us);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        rl_user_agreement.setOnClickListener(this);
        rl_privacy_agreement.setOnClickListener(this);
        rl_contact_us.setOnClickListener(this);
        rl_red_packet_agreement.setOnClickListener(this);
        rl_member_agreement.setOnClickListener(this);
        if (VipManager.Companion.isVipCheck()) {
            rl_member_agreement.setVisibility(View.VISIBLE);
        } else {
            rl_member_agreement.setVisibility(View.GONE);
        }
        tv_version.setText("Version " + AppUtils.getVersionName(this));
    }

    String agreement = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_agreement:
                UrlInfoActivity.start(UserSettingAboutUsActivity.this, UrlConstants.INSTANCE.getUSER_PROTOCOL(), getResources().getString(R.string.user_agreement));
                break;
            case R.id.rl_privacy_agreement:
                UrlInfoActivity.start(UserSettingAboutUsActivity.this, UrlConstants.INSTANCE.getUSER_YS_PRO(), getResources().getString(R.string.privacy_agreement));

                break;
            case R.id.rl_red_packet_agreement:
                UrlInfoActivity.start(UserSettingAboutUsActivity.this, UrlConstants.INSTANCE.getREAD_GZ(), getResources().getString(R.string.red_packet_agreement));

                break;
            case R.id.rl_member_agreement:
                UrlInfoActivity.start(UserSettingAboutUsActivity.this, UrlConstants.INSTANCE.getVIP_YX(), "会员协议");

                break;
            case R.id.rl_contact_us:
                startActivity(new Intent(UserSettingAboutUsActivity.this, UserSettingAboutUsContactUsActivity.class));
                break;
        }
    }
}
