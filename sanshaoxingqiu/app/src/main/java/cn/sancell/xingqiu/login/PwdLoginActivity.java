package cn.sancell.xingqiu.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.sys.ImCache;
import cn.sancell.xingqiu.im.sys.LogoutHelper;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.interfaces.PwdTextWatcher;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.login.contract.PwdLoginContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class PwdLoginActivity extends BaseMVPActivity<PwdLoginContract.PwdLoginPresenter>
        implements PwdLoginContract.PwdLoginView, View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ed_login_mobile)
    EditText ed_login_mobile;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.cb_password)
    CheckBox cb_password;
    @BindView(R.id.iv_clear_pwd)
    ImageView iv_clear_pwd;
    @BindView(R.id.btn_code_login)
    TextView btn_code_login;
    @BindView(R.id.btn_reset_pwd)
    TextView btn_reset_pwd;
    @BindView(R.id.btn_login)
    TextView btn_login;
    @BindView(R.id.btn_weixin_login)
    ImageView btn_weixin_login;
    @BindView(R.id.btn_protocol)
    TextView btn_protocol;
    @BindView(R.id.btn_privacy)
    TextView btn_privacy;

    private int requestCode_BindPhone = 101;

    /**
     * 第三方登录用户信息
     */
    private Map<String, String> loginData = new HashMap<>();

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void pwdLoginSuccess(LoginInfo info) {
        loginIm(info);
    }

    public void startMain() {
        Intent intent1 = new Intent();
        intent1.putExtra(Constants.Key.KEY_1, true);
        setResult(RESULT_OK, intent1);
        HomeTabsActivity.Companion.start(this, 0);
        PwdLoginActivity.this.finish();
    }

    @Override
    public void noBindPhone() {
        Intent intent = new Intent(PwdLoginActivity.this, BindPhoneActivity.class);
        intent.putExtra(Constants.Key.KEY_1, loginData.get("openUnionId"));
        intent.putExtra(Constants.Key.KEY_2, loginData.get("nickname"));
        intent.putExtra(Constants.Key.KEY_3, loginData.get("tvUserGravatar"));
        intent.putExtra(Constants.Key.KEY_4, loginData.get("gender"));
        startActivityForResult(intent, requestCode_BindPhone);
    }

    @Override
    public void getImLogin(ImAccountRes res) {
        LoginInfo info = new LoginInfo(res.yunxin_accid, res.yunxin_token);
        loginIm(info);
    }


    @Override
    protected PwdLoginContract.PwdLoginPresenter createPresenter() {
        return new PwdLoginContract.PwdLoginPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pwd_login;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
            lp.topMargin = statusHeight;
            tv_title.setLayoutParams(lp);
        }
        if (!StringUtils.isTextEmpty(getIntent().getStringExtra(Constants.Key.KEY_1))) {
            ed_login_mobile.setText(getIntent().getStringExtra(Constants.Key.KEY_1));
            ed_login_mobile.setSelection(ed_login_mobile.getText().length());
        }
        btn_privacy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_privacy.getPaint().setAntiAlias(true);//抗锯齿
        btn_protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_protocol.getPaint().setAntiAlias(true);//抗锯齿
        cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //选中显示密码
                    ed_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    ed_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //把光标设置在文字结尾
                ed_pwd.setSelection(ed_pwd.getText().length());
            }
        });
        cb_password.setChecked(false);
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_pwd, iv_clear, btn_login, PwdLoginActivity.this));
        ed_pwd.addTextChangedListener(new PwdTextWatcher(ed_login_mobile, ed_pwd, btn_login, cb_password, iv_clear_pwd, PwdLoginActivity.this));
        btn_code_login.setOnClickListener(this);
        btn_reset_pwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_weixin_login.setOnClickListener(this);
        btn_privacy.setOnClickListener(this);
        btn_protocol.setOnClickListener(this);
        btn_login.setEnabled(false);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    /**
     * 云信登陆，未收到服务器账号信息时进行注册
     *
     * @param info
     */
    public void loginIm(LoginInfo info) {
        if (TextUtils.isEmpty(info.getToken()) || TextUtils.isEmpty(info.getAccount())) {
            mPresenter.loginIm(this);
        } else {
            NimUIKit.login(info, new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo info) {
                    ImCache.setAccount(info.getAccount());
                    LiveCache.setAccount(info.getAccount());
                    startMain();
                }

                @Override
                public void onFailed(int code) {
                    LogoutHelper.logout();
                    if (code == 302 || code == 404) {
                        SCApp.getInstance().showSystemCenterToast("云信登陆失败");
                    } else {
                        SCApp.getInstance().showSystemCenterToast("登录失败: " + code);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.e("im_login_error", "error");
                }
            });
        }
    }


    private UMShareAPI mShareAPI = null;

    /**
     * 微信登陆
     */
    public void WXLogin() {
        mShareAPI = UMShareAPI.get(this);
        mShareAPI.getPlatformInfo(PwdLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                if (platform == SHARE_MEDIA.WEIXIN) {
                    getWXInfo(data);
                    mPresenter.UserWeiXinLogin(loginData.get("openUnionId"), PwdLoginActivity.this);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登录失败error" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };

    /**
     * 获取微信登录信息
     */
    public void getWXInfo(Map<String, String> map) {
        loginData.put("openUnionId", map.get("uid"));
        loginData.put("openIdType", "1");
        if (StringUtils.isTextEmpty(map.get("name"))) {
            loginData.put("nickname", "");
        } else {
            loginData.put("nickname", map.get("name"));
        }
        if (map.get("gender") != null) {
            if (map.get("gender").equals("女")) {
                loginData.put("gender", "2");
            } else if (map.get("gender").equals("男")) {
                loginData.put("gender", "1");
            } else {
                loginData.put("gender", "3");
            }
        } else {
            loginData.put("gender", "3");
        }
        loginData.put("tvUserGravatar", map.get("iconurl"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code_login:  //验证码登录
                Intent intent1 = new Intent();
                intent1.putExtra(Constants.Key.KEY_1, false);
                intent1.putExtra(Constants.Key.KEY_2, ed_login_mobile.getText().toString());
                setResult(RESULT_OK, intent1);
                finish();
                break;
            case R.id.btn_reset_pwd:  //重置密码
                startActivity(new Intent(PwdLoginActivity.this, ResetPwdFirstStepActivity.class));
                break;
            case R.id.btn_login:
                if (!StringUtils.isMobile(ed_login_mobile.getText().toString().replaceAll(" ", ""))) {
                    SCApp.getInstance().showSystemCenterToast(R.string.phone_format_error_tip);
                    return;
                }
                mPresenter.UserPwdLogin(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_pwd.getText().toString(), PwdLoginActivity.this);
                break;
            case R.id.btn_weixin_login:  //微信登录
                WXLogin();
                break;
            case R.id.btn_protocol:
                UrlInfoActivity.start(PwdLoginActivity.this, UrlConstants.INSTANCE.getUSER_PROTOCOL(), getResources().getString(R.string.user_agreement));

                break;
            case R.id.btn_privacy:
                UrlInfoActivity.start(PwdLoginActivity.this, UrlConstants.INSTANCE.getUSER_YS_PRO(), getResources().getString(R.string.privacy_agreement));

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_BindPhone) {
                Intent intent1 = new Intent();
                intent1.putExtra(Constants.Key.KEY_1, true);
                setResult(RESULT_OK, intent1);
                this.finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent();
            intent1.putExtra(Constants.Key.KEY_1, false);
            intent1.putExtra(Constants.Key.KEY_2, ed_login_mobile.getText().toString());
            setResult(RESULT_OK, intent1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
