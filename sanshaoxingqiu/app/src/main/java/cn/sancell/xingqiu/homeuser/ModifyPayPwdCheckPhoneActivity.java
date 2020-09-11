package cn.sancell.xingqiu.homeuser;

import android.content.Context;
import android.content.Intent;
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
import cn.sancell.xingqiu.homeuser.contract.ModifyPayPwdCheckPhoneContract;
import cn.sancell.xingqiu.interfaces.CodeTextWatcher;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.TimeCount;

public class ModifyPayPwdCheckPhoneActivity extends BaseMVPToobarActivity<ModifyPayPwdCheckPhoneContract.ModifyPayPwdCheckPhonePresenter>
        implements ModifyPayPwdCheckPhoneContract.ModifyPayPwdCheckPhoneView, View.OnClickListener {
    @BindView(R.id.ed_login_mobile)
    EditText ed_login_mobile;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.iv_clear_code)
    ImageView iv_clear_code;
    @BindView(R.id.btn_getcode)
    TextView btn_getcode;
    @BindView(R.id.btn_next)
    TextView btn_next;
    private TimeCount timeCount;
    private int requestCode_SecondStep = 100;

    public static void start(Context context){
        Intent intent = new Intent(context,ModifyPayPwdCheckPhoneActivity.class);
        context.startActivity(intent);
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
                mPresenter.getCode(ed_login_mobile.getText().toString().replaceAll(" ", ""), ModifyPayPwdCheckPhoneActivity.this);
                break;
            case R.id.btn_next:
                mPresenter.CheckPhone(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_code.getText().toString(), ModifyPayPwdCheckPhoneActivity.this);
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
    public void checkSuccess() {
        Intent intent = new Intent(ModifyPayPwdCheckPhoneActivity.this, ForgetPayPwdActivity.class);
        intent.putExtra(Constants.Key.KEY_1, ed_code.getText().toString());
        startActivityForResult(intent, requestCode_SecondStep);
        finish();
    }

    @Override
    protected ModifyPayPwdCheckPhoneContract.ModifyPayPwdCheckPhonePresenter createPresenter() {
        return new ModifyPayPwdCheckPhoneContract.ModifyPayPwdCheckPhonePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_replace_phone_first_step;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("验证身份");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        timeCount = new TimeCount(this, 60000, 1000, btn_getcode);// 构造CountDownTimer对象
        btn_next.setText("设置新密码");
        ed_login_mobile.setEnabled(false);
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        btn_getcode.setClickable(false);
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_code, btn_getcode, null, btn_next, ModifyPayPwdCheckPhoneActivity.this));
        ed_code.addTextChangedListener(new CodeTextWatcher(ed_login_mobile, ed_code, btn_next, iv_clear_code, ModifyPayPwdCheckPhoneActivity.this));
        btn_getcode.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_getcode.setClickable(false);
        btn_next.setEnabled(false);
        ed_login_mobile.setText(userBean.getMobile());
    }

    @Override
    protected BaseView getView() {
        return this;
    }
}
