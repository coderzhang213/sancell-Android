package cn.sancell.xingqiu.homeuser;

import android.Manifest;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import com.hujiang.permissiondispatcher.NeedPermission;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class UserSettingAboutUsContactUsActivity extends BaseToobarActivity implements View.OnClickListener {
    @BindView(R.id.rl_contact_phone)
    RelativeLayout rl_contact_phone;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_setting_about_us_contact_us;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("联系我们");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rl_contact_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_contact_phone:
                showCallPhone();
                break;
        }
    }

    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(UserSettingAboutUsContactUsActivity.this, "", "");
    }

}
