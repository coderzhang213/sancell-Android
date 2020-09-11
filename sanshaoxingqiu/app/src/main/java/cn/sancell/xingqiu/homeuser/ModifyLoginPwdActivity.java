package cn.sancell.xingqiu.homeuser;

import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.contract.ModifyLoginPwdContract;
import cn.sancell.xingqiu.interfaces.ModifyPwdTextWatcher;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class ModifyLoginPwdActivity extends BaseMVPToobarActivity<ModifyLoginPwdContract.ModifyLoginPwdPresenter>
        implements ModifyLoginPwdContract.ModifyLoginPwdView, View.OnClickListener {

    @BindView(R.id.ed_old_pwd)
    EditText ed_old_pwd;
    @BindView(R.id.iv_clear_oldpwd)
    ImageView iv_clear_oldpwd;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.ed_new_pwd)
    EditText ed_new_pwd;
    @BindView(R.id.iv_clear_newpwd)
    ImageView iv_clear_newpwd;
    @BindView(R.id.ed_new_pwd_again)
    EditText ed_new_pwd_again;
    @BindView(R.id.iv_clear_newpwd_again)
    ImageView iv_clear_newpwd_again;
    @BindView(R.id.btn_sure)
    TextView btn_sure;

    UserBean userBean;

    @Override
    protected ModifyLoginPwdContract.ModifyLoginPwdPresenter createPresenter() {
        return new ModifyLoginPwdContract.ModifyLoginPwdPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_modify_login_pwd;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (userBean.getAddPasswordStatus() == 1) { //有登录密码
            initActivityTitle(R.string.modify_login_pwd);
        } else {
            initActivityTitle(R.string.set_login_pwd);
        }
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        ed_old_pwd.addTextChangedListener(new ModifyPwdTextWatcher(ed_old_pwd, ed_new_pwd, ed_new_pwd_again, btn_sure, iv_clear_oldpwd, ModifyLoginPwdActivity.this));
        ed_new_pwd.addTextChangedListener(new ModifyPwdTextWatcher(ed_new_pwd, ed_old_pwd, ed_new_pwd_again, btn_sure, iv_clear_newpwd, ModifyLoginPwdActivity.this));
        ed_new_pwd_again.addTextChangedListener(new ModifyPwdTextWatcher(ed_new_pwd_again, ed_old_pwd, ed_new_pwd, btn_sure, iv_clear_newpwd_again, ModifyLoginPwdActivity.this));
        ed_old_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ed_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ed_new_pwd_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        btn_sure.setOnClickListener(this);
        if (userBean.getAddPasswordStatus() == 1) { //有登录密码
            //ed_old_pwd.setEnabled(true);
            ed_old_pwd.setVisibility(View.VISIBLE);
            iv_clear_oldpwd.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
        } else {
            /*ed_old_pwd.setEnabled(false);
            ed_old_pwd.setText("未设置过登录密码");
            iv_clear_oldpwd.setVisibility(View.GONE);*/
            ed_old_pwd.setText("未设置过登录密码");
            ed_old_pwd.setVisibility(View.GONE);
            iv_clear_oldpwd.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if (!ed_new_pwd.getText().toString().equals(ed_new_pwd_again.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast(R.string.two_pwd_differ_tip);
                    return;
                }
                /*if (!StringUtils.isPwd(ed_new_pwd.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast("密码格式不对");
                    return;
                }*/
                if (userBean.getAddPasswordStatus() == 1) {
                    mPresenter.ModifyPwd(ed_old_pwd.getText().toString(), ed_new_pwd.getText().toString(), ModifyLoginPwdActivity.this);
                } else {
                    mPresenter.ModifyPwd("0", ed_new_pwd.getText().toString(), ModifyLoginPwdActivity.this);
                }
                break;
        }
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void modifySuccess() {
        userBean.setAddPasswordStatus(1);
        PreferencesUtils.put(Constants.Key.KEY_USERINFO, userBean);
        finish();
    }
}
