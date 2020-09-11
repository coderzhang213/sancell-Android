package cn.sancell.xingqiu.login;

import android.os.Build;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cn.sancell.xingqiu.interfaces.PwdTextWatcher;
import cn.sancell.xingqiu.login.contract.ResetPwdSecondStepContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class ResetPwdSecondStepActivity extends BaseMVPActivity<ResetPwdSecondStepContract.ResetPwdSecondPresenter>
        implements ResetPwdSecondStepContract.ResetPwdSecondView {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.cb_password)
    CheckBox cb_password;
    @BindView(R.id.iv_clear_pwd)
    ImageView iv_clear_pwd;
    @BindView(R.id.ed_pwd_again)
    EditText ed_pwd_again;
    @BindView(R.id.cb_password_again)
    CheckBox cb_password_again;
    @BindView(R.id.iv_clear_pwd_again)
    ImageView iv_clear_pwd_again;
    @BindView(R.id.btn_sure)
    TextView btn_sure;

    private String smsCode;
    private String mobile;

    @Override
    protected ResetPwdSecondStepContract.ResetPwdSecondPresenter createPresenter() {
        return new ResetPwdSecondStepContract.ResetPwdSecondPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_pwd_second_step;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_back.getLayoutParams();
            lp.topMargin = statusHeight;
            iv_back.setLayoutParams(lp);
        }
        mobile = getIntent().getStringExtra(Constants.Key.KEY_1);
        smsCode = getIntent().getStringExtra(Constants.Key.KEY_2);
        cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //选中显示密码
                    ed_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //隐藏密码
                    ed_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        cb_password.setChecked(false);
        cb_password_again.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //选中显示密码
                    ed_pwd_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //隐藏密码
                    ed_pwd_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //把光标设置在文字结尾
                ed_pwd_again.setSelection(ed_pwd_again.getText().length());
                /*if (b) {
                    ed_pwd_again.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ed_pwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }*/
            }
        });
        cb_password_again.setChecked(false);
        ed_pwd.addTextChangedListener(new PwdTextWatcher(ed_pwd_again, ed_pwd, btn_sure, cb_password, iv_clear_pwd, ResetPwdSecondStepActivity.this));
        ed_pwd_again.addTextChangedListener(new PwdTextWatcher(ed_pwd, ed_pwd_again, btn_sure, cb_password_again, iv_clear_pwd_again, ResetPwdSecondStepActivity.this));
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_pwd.getText().toString().equals(ed_pwd_again.getText().toString())){
                    SCApp.getInstance().showSystemCenterToast(R.string.two_pwd_differ_tip);
                    return;
                }
                /*if (!StringUtils.isPwd(ed_pwd.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast("密码格式不对");
                    return;
                }*/
                mPresenter.ResetPwdFirst(mobile, smsCode, ed_pwd.getText().toString(), ResetPwdSecondStepActivity.this);
            }
        });
        btn_sure.setEnabled(false);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    public void resetPwdSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
