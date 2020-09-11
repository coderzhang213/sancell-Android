package cn.sancell.xingqiu.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.interfaces.CodeTextWatcher;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.login.contract.BindPhoneContract;
import cn.sancell.xingqiu.usermember.UserInviteActivity;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.TimeCount;

public class BindPhoneActivity extends BaseMVPActivity<BindPhoneContract.BindPhonePresenter>
        implements BindPhoneContract.BindPhoneView, View.OnClickListener {

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
    @BindView(R.id.btn_sure_bind)
    TextView btn_sure_bind;

    private TimeCount timeCount;

    private String openUnionId, nickname, gravatar, gender;

    private int requestCode_BindInviteId = 102;


    @Override
    protected BindPhoneContract.BindPhonePresenter createPresenter() {
        return new BindPhoneContract.BindPhonePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind_phone;
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
        openUnionId = getIntent().getStringExtra(Constants.Key.KEY_1);
        nickname = getIntent().getStringExtra(Constants.Key.KEY_2);
        gravatar = getIntent().getStringExtra(Constants.Key.KEY_3);
        gender = getIntent().getStringExtra(Constants.Key.KEY_4);
        ed_login_mobile.requestFocus();
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_code, btn_getcode, iv_clear, btn_sure_bind, BindPhoneActivity.this));
        ed_code.addTextChangedListener(new CodeTextWatcher(ed_login_mobile, ed_code, btn_sure_bind, iv_clear_code, BindPhoneActivity.this));
        btn_getcode.setOnClickListener(this);
        btn_sure_bind.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_getcode.setClickable(false);
        btn_sure_bind.setEnabled(false);
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
    public void bingInviteId() {
        startActivityForResult(UserInviteActivity.getIntent(this), requestCode_BindInviteId);
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
    public void bindSuccess() {
        setResult(RESULT_OK);
       // startActivity(new Intent(BindPhoneActivity.this, MainActivity.class));
        HomeTabsActivity.Companion.start(this,0);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                DialogUtil.showOperateDialog(BindPhoneActivity.this, "联合注册尚未完成，是否继续注册", "", "退出", "继续", clickCancelAction);
                break;
            case R.id.btn_getcode:
                if (!StringUtils.isMobile(ed_login_mobile.getText().toString().replaceAll(" ", ""))) {
                    SCApp.getInstance().showSystemCenterToast(R.string.phone_format_error_tip);
                    return;
                }
                ed_code.setText("");
                mPresenter.getCode(ed_login_mobile.getText().toString().replaceAll(" ", ""), BindPhoneActivity.this);
                break;
            case R.id.btn_sure_bind:
                mPresenter.BindPhone(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_code.getText().toString(),
                        openUnionId, nickname, gravatar, gender, BindPhoneActivity.this);
                break;
        }
    }

    DialogUtil.ClickCancelAction clickCancelAction = new DialogUtil.ClickCancelAction() {
        @Override
        public void cancelAction() {
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogUtil.showOperateDialog(BindPhoneActivity.this, "联合注册尚未完成，是否继续注册", "", "退出", "继续", clickCancelAction);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
           if (requestCode == requestCode_BindInviteId) {
                this.finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        super.onDestroy();
    }
}
