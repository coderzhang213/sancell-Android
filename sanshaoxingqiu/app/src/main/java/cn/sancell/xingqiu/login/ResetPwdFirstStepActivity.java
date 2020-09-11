package cn.sancell.xingqiu.login;

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
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.interfaces.CodeTextWatcher;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.login.contract.ResetPwdFirstStepContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.TimeCount;

public class ResetPwdFirstStepActivity extends BaseMVPActivity<ResetPwdFirstStepContract.ResetPwdFirstPresenter>
        implements ResetPwdFirstStepContract.ResetPwdFirstView, View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
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
    @BindView(R.id.btn_next)
    TextView btn_next;
    private TimeCount timeCount;

    private int requestCode_SecondStep = 100;

    @Override
    protected ResetPwdFirstStepContract.ResetPwdFirstPresenter createPresenter() {
        return new ResetPwdFirstStepContract.ResetPwdFirstPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_pwd_first_step;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        timeCount = new TimeCount(this, 60000, 1000, btn_getcode);// 构造CountDownTimer对象
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_back.getLayoutParams();
            lp.topMargin = statusHeight;
            iv_back.setLayoutParams(lp);
        }
        ed_login_mobile.requestFocus();
        btn_getcode.setClickable(false);
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_code, btn_getcode, iv_clear, btn_next, ResetPwdFirstStepActivity.this));
        ed_code.addTextChangedListener(new CodeTextWatcher(ed_login_mobile, ed_code, btn_next, iv_clear_code, ResetPwdFirstStepActivity.this));
        btn_getcode.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_getcode.setClickable(false);
        btn_next.setEnabled(false);
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
        Intent intent=new Intent(ResetPwdFirstStepActivity.this, ResetPwdSecondStepActivity.class);
        intent.putExtra(Constants.Key.KEY_1,ed_login_mobile.getText().toString().replaceAll(" ",""));
        intent.putExtra(Constants.Key.KEY_2,ed_code.getText().toString());
        startActivityForResult(intent,requestCode_SecondStep);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_getcode:
                if(!StringUtils.isMobile(ed_login_mobile.getText().toString().replaceAll(" ",""))){
                    SCApp.getInstance().showSystemCenterToast(R.string.phone_format_error_tip);
                    return;
                }
                ed_code.setText("");
                mPresenter.getCode(ed_login_mobile.getText().toString().replaceAll(" ", ""), ResetPwdFirstStepActivity.this);
                break;
            case R.id.btn_next:
                mPresenter.ResetPwdFirst(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_code.getText().toString(), ResetPwdFirstStepActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_SecondStep) {
                this.finish();
            }
        }
    }
}
