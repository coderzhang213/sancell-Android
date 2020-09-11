package cn.sancell.xingqiu.homeuser;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import cn.sancell.xingqiu.homeuser.contract.ReplacePhoneSecondStepContract;
import cn.sancell.xingqiu.interfaces.CodeTextWatcher;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.TimeCount;

public class ReplacePhoneSecondStepActivity extends BaseMVPToobarActivity<ReplacePhoneSecondStepContract.ReplacePhoneSecondPresenter>
        implements ReplacePhoneSecondStepContract.ReplacePhoneSecondView, View.OnClickListener {
    @BindView(R.id.ed_login_mobile)
    EditText ed_login_mobile;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.iv_clear_code)
    ImageView iv_clear_code;
    @BindView(R.id.btn_getcode)
    TextView btn_getcode;
    @BindView(R.id.btn_sure_replace)
    TextView btn_sure_replace;

    private String oldMobile, oldSmsCode;

    private TimeCount timeCount;

    @Override
    protected ReplacePhoneSecondStepContract.ReplacePhoneSecondPresenter createPresenter() {
        return new ReplacePhoneSecondStepContract.ReplacePhoneSecondPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_replace_phone_second_step;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        timeCount = new TimeCount(this, 60000, 1000, btn_getcode);// 构造CountDownTimer对象
        initActivityTitle(R.string.replace_phone);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        oldMobile = getIntent().getStringExtra(Constants.Key.KEY_1);
        oldSmsCode = getIntent().getStringExtra(Constants.Key.KEY_2);
        ed_login_mobile.requestFocus();
        ed_login_mobile.setHint(R.string.input_new_phone_tip);
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_code, btn_getcode, iv_clear, btn_sure_replace, ReplacePhoneSecondStepActivity.this));
        ed_code.addTextChangedListener(new CodeTextWatcher(ed_login_mobile, ed_code, btn_sure_replace, iv_clear_code, ReplacePhoneSecondStepActivity.this));
        btn_getcode.setOnClickListener(this);
        btn_sure_replace.setOnClickListener(this);
        btn_getcode.setClickable(false);
        btn_sure_replace.setEnabled(false);

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getcode:
                if (!StringUtils.isMobile(ed_login_mobile.getText().toString().replaceAll(" ", ""))) {
                    SCApp.getInstance().showSystemCenterToast(R.string.phone_format_error_tip);
                    return;
                }
                ed_code.setText("");
                mPresenter.getCode(ed_login_mobile.getText().toString().replaceAll(" ", ""), ReplacePhoneSecondStepActivity.this);
                break;
            case R.id.btn_sure_replace:
                mPresenter.ReplacePhone(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_code.getText().toString(), oldMobile, oldSmsCode, ReplacePhoneSecondStepActivity.this);
                break;
        }
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void getCodeSuccess() {
        ed_code.requestFocus();
        btn_getcode.setClickable(false);
        timeCount.start();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_code, InputMethodManager.SHOW_IMPLICIT);
        SCApp.getInstance().showSystemCenterToast(R.string.send_success);
    }

    @Override
    public void replaceSuccess(String skey) {
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        userBean.setMobile(ed_login_mobile.getText().toString().replaceAll(" ", ""));
        PreferencesUtils.put(Constants.Key.KEY_SKEY,skey);
        PreferencesUtils.put(Constants.Key.KEY_USERINFO, userBean);
        setResult(RESULT_OK);
        finish();
    }
}
