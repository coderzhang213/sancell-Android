package cn.sancell.xingqiu.homeuser;

import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.contract.ModifyPayPwdNewPwdContract;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;

public class ModifyPayPwdNewPwdActivity extends BaseMVPToobarActivity<ModifyPayPwdNewPwdContract.ModifyPayPwdNewPwdPresenter>
        implements ModifyPayPwdNewPwdContract.ModifyPayPwdNewPwdView {
    @BindView(R.id.ed_pay_pwd)
    VerificationCodeEditText ed_pay_pwd;
    @BindView(R.id.ed_pay_pwd_again)
    VerificationCodeEditText ed_pay_pwd_again;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_error_tip)
    TextView tv_error_tip;
    private String new_pwd, new_pwd_again;
    private String old_pay_pwd;

    @Override
    protected ModifyPayPwdNewPwdContract.ModifyPayPwdNewPwdPresenter createPresenter() {
        return new ModifyPayPwdNewPwdContract.ModifyPayPwdNewPwdPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_pay_pwd;
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
        old_pay_pwd = getIntent().getStringExtra(Constants.Key.KEY_1);
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
                new_pwd = s.toString();
                tv_tip.setText(R.string.again_input_pay_pwd_tip);
                ed_pay_pwd.setVisibility(View.GONE);
                ed_pay_pwd_again.setVisibility(View.VISIBLE);
                ed_pay_pwd_again.setText("");
                ed_pay_pwd_again.requestFocus();
            }
        });

        ed_pay_pwd_again.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tv_error_tip.setText("");
                }
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                new_pwd_again = s.toString();
                if (!new_pwd_again.equals(new_pwd)) {
                    tv_error_tip.setText(R.string.two_pwd_differ_tip);
                    tv_tip.setText(R.string.input_pay_pwd_format_tip);
                    new_pwd = "";
                    new_pwd_again = "";
                    ed_pay_pwd.setVisibility(View.VISIBLE);
                    ed_pay_pwd.setText("");
                    ed_pay_pwd_again.setVisibility(View.GONE);
                    ed_pay_pwd_again.setText("");
                    ed_pay_pwd.requestFocus();
                    return;
                }
                mPresenter.ModifyPayPwd(old_pay_pwd, new_pwd_again, ModifyPayPwdNewPwdActivity.this);
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
    public void modifyNewPwdSuccess() {
        SCApp.getInstance().showSystemCenterToast(R.string.modify_pay_pwd_success);
        ScreenUtils.hideKeyboard(ed_pay_pwd_again);
        finish();
    }

    @Override
    public void modifyNewPwdFail(String errorTip) {
        tv_tip.setText(R.string.input_pay_pwd_format_tip);
        new_pwd = "";
        new_pwd_again = "";
        ed_pay_pwd.setVisibility(View.VISIBLE);
        ed_pay_pwd.setText("");
        ed_pay_pwd_again.setText("");
        ed_pay_pwd_again.setVisibility(View.GONE);
        ed_pay_pwd.requestFocus();
        tv_error_tip.setText(errorTip);
    }
}
