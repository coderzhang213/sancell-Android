package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class UserSafeCenterActivity extends BaseToobarActivity implements View.OnClickListener {

    @BindView(R.id.rl_replace_phone)
    RelativeLayout rl_replace_phone;
    @BindView(R.id.rl_modify_login_pwd)
    RelativeLayout rl_modify_login_pwd;
    @BindView(R.id.tv_login_pwd)
    TextView tv_login_pwd;
    @BindView(R.id.rl_modify_pay_pwd)
    RelativeLayout rl_modify_pay_pwd;
    @BindView(R.id.tv_pay_pwd)
    TextView tv_pay_pwd;


    @Override
    public void onResume() {
        super.onResume();
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (userBean != null && userBean.getAddPayPasswordStatus() == 1) {  //设置过支付密码
            tv_pay_pwd.setText(R.string.pay_pwd);
        } else {
            tv_pay_pwd.setText(R.string.set_pay_pwd);
        }
        if (userBean != null && userBean.getAddPasswordStatus() == 1) {  //设置过登录密码
            tv_login_pwd.setText(R.string.modify_login_pwd);
        } else {
            tv_login_pwd.setText(R.string.set_login_pwd);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_safe_center;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.safe_center_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rl_replace_phone.setOnClickListener(this);
        rl_modify_login_pwd.setOnClickListener(this);
        rl_modify_pay_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_replace_phone:
                DialogUtil.showOperateDialog(UserSafeCenterActivity.this, getResources().getString(R.string.is_replace_phone), "", getResources().getString(R.string.dialog_replace_phone_no), getResources().getString(R.string.dialog_replace_phone_yes), clickSureAction);
                break;
            case R.id.rl_modify_login_pwd:
                startActivity(new Intent(UserSafeCenterActivity.this, ModifyLoginPwdActivity.class));
                break;
            case R.id.rl_modify_pay_pwd:
                if (PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getAddPayPasswordStatus() == 1) {  //设置过支付密码
                    DialogUtil.showModifyPayPwd(UserSafeCenterActivity.this);
                } else {
                    Intent intent = new Intent(UserSafeCenterActivity.this, ModifyPayPwdCheckPhoneActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    DialogUtil.ClickSureAction clickSureAction = postion -> startActivity(new Intent(UserSafeCenterActivity.this, ReplacePhoneFirstStepActivity.class));
}
