package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.contract.ModifyPayPwdCheckOldPwdContract;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;

public class ModifyPayPwdCheckOldPwdActivity extends BaseMVPToobarActivity<ModifyPayPwdCheckOldPwdContract.ModifyPayPwdCheckOldPwdPresenter>
        implements ModifyPayPwdCheckOldPwdContract.ModifyPayPwdCheckOldPwdView {
    @BindView(R.id.ed_pay_pwd)
    VerificationCodeEditText ed_pay_pwd;
    @BindView(R.id.tv_error_tip)
    TextView tv_error_tip;
    private String old_pay_pwd;

    @Override
    protected ModifyPayPwdCheckOldPwdContract.ModifyPayPwdCheckOldPwdPresenter createPresenter() {
        return new ModifyPayPwdCheckOldPwdContract.ModifyPayPwdCheckOldPwdPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_modify_pay_pwd_check_old_pwd;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.modify_pay_pwd);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        btn_back.setOnClickListener(view -> {
            ScreenUtils.hideKeyboard(ed_pay_pwd);
            finish();
        });
        ed_pay_pwd.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tv_error_tip.setText("");
                }
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                old_pay_pwd = s.toString();
                mPresenter.CheckOldPayPwd(old_pay_pwd, ModifyPayPwdCheckOldPwdActivity.this);
            }
        });
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
    public void checkOldPwdSuccess() {
        Intent intent = new Intent(this, ModifyPayPwdNewPwdActivity.class);
        intent.putExtra(Constants.Key.KEY_1, old_pay_pwd);
        startActivity(intent);
        finish();
    }

    @Override
    public void checkOldPwdFail(String errorTip) {
        tv_error_tip.setText(errorTip);
        old_pay_pwd = "";
        ed_pay_pwd.setText("");
        ed_pay_pwd.requestFocus();
    }

}
