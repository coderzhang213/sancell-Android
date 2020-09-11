package cn.sancell.xingqiu.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.ClientType;
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
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.sys.ImCache;
import cn.sancell.xingqiu.im.sys.LogoutHelper;
import cn.sancell.xingqiu.interfaces.CodeTextWatcher;
import cn.sancell.xingqiu.interfaces.NumberTextWatcher;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.login.contract.CodeLoginContract;
import cn.sancell.xingqiu.usermember.UserInviteActivity;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.TimeCount;

/**
 * @ClassName: CodeLoginActivity
 * @Description:验证码登录
 */
public class CodeLoginActivity extends BaseMVPActivity<CodeLoginContract.LoginPresenter>
        implements CodeLoginContract.LoginView, View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
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
    @BindView(R.id.btn_pwd_login)
    TextView btn_pwd_login;
    @BindView(R.id.btn_login)
    TextView btn_login;
    @BindView(R.id.btn_weixin_login)
    ImageView btn_weixin_login;
    @BindView(R.id.btn_protocol)
    TextView btn_protocol;
    @BindView(R.id.btn_privacy)
    TextView btn_privacy;

    private TimeCount timeCount;

    private int requestCode_PwdLogin = 100;
    private int requestCode_BindPhone = 101;
    private int requestCode_BindInviteId = 102;

    /**
     * 第三方登录用户信息
     */
    private Map<String, String> loginData = new HashMap<>();


    @Override
    protected CodeLoginContract.LoginPresenter createPresenter() {
        return new CodeLoginContract.LoginPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        timeCount = new TimeCount(this, 60000, 1000, btn_getcode);// 构造CountDownTimer对象
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
            lp.topMargin = statusHeight;
            tv_title.setLayoutParams(lp);
        }
        btn_privacy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_privacy.getPaint().setAntiAlias(true);//抗锯齿
        btn_protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_protocol.getPaint().setAntiAlias(true);//抗锯齿
        ed_login_mobile.requestFocus();
        ed_login_mobile.addTextChangedListener(new NumberTextWatcher(ed_login_mobile, ed_code, btn_getcode, iv_clear, btn_login, CodeLoginActivity.this));
        ed_code.addTextChangedListener(new CodeTextWatcher(ed_login_mobile, ed_code, btn_login, iv_clear_code, CodeLoginActivity.this));
        btn_pwd_login.setOnClickListener(this);
        btn_weixin_login.setOnClickListener(this);
        btn_protocol.setOnClickListener(this);
        btn_privacy.setOnClickListener(this);
        btn_getcode.setOnClickListener(this);
        iv_clear_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_getcode.setClickable(false);
        btn_login.setEnabled(false);
        btn_login.setClickable(false);

        checkImLogin();
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
        timeCount.start();
        btn_getcode.setClickable(false);
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_code, InputMethodManager.SHOW_IMPLICIT);
        SCApp.getInstance().showSystemCenterToast(R.string.send_success);
    }

    @Override
    public void codeLoginSuccess(LoginInfo info) {
        loginIm(info);

    }

    private void start() {
        HomeTabsActivity.Companion.start(this, 0);
        CodeLoginActivity.this.finish();
    }

    /**
     * 云信登陆
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
                    start();
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

                }
            });
        }
    }

    @Override
    public void bingInviteId() {
        startActivityForResult(UserInviteActivity.getIntent(this), requestCode_BindInviteId);
    }

    @Override
    public void noBindPhone() {
        Intent intent = new Intent(CodeLoginActivity.this, BindPhoneActivity.class);
        intent.putExtra(Constants.Key.KEY_1, loginData.get("openUnionId"));
        intent.putExtra(Constants.Key.KEY_2, loginData.get("nickname"));
        intent.putExtra(Constants.Key.KEY_3, loginData.get("tvUserGravatar"));
        intent.putExtra(Constants.Key.KEY_4, loginData.get("gender"));
        startActivityForResult(intent, requestCode_BindPhone);
    }

    @Override
    public void getImLogin(ImAccountRes res) {
        LoginInfo info = new LoginInfo(res.yunxin_accid,res.yunxin_token);
        loginIm(info);
    }

    private UMShareAPI mShareAPI = null;

    /**
     * 微信登陆
     */
    public void WXLogin() {
        mShareAPI = UMShareAPI.get(this);
        mShareAPI.getPlatformInfo(CodeLoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
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
                    mPresenter.UserWeiXinLogin(loginData.get("openUnionId"), CodeLoginActivity.this);
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
            case R.id.btn_pwd_login:  //密码登录
                Intent intent1 = new Intent(CodeLoginActivity.this, PwdLoginActivity.class);
                intent1.putExtra(Constants.Key.KEY_1, ed_login_mobile.getText().toString());
                startActivityForResult(intent1, requestCode_PwdLogin);
                break;
            case R.id.btn_getcode:
                Log.e(TAG, "OnClick2:" + view);
                if (!StringUtils.isMobile(ed_login_mobile.getText().toString().replaceAll(" ", ""))) {
                    SCApp.getInstance().showSystemCenterToast(R.string.phone_format_error_tip);
                    return;
                }
                ed_code.setText("");
                mPresenter.getCode(ed_login_mobile.getText().toString().replaceAll(" ", ""), CodeLoginActivity.this);
                break;
            case R.id.btn_login:
                Log.e(TAG, "OnClick2:" + view);
                mPresenter.UserCodeLogin(ed_login_mobile.getText().toString().replaceAll(" ", ""), ed_code.getText().toString(), CodeLoginActivity.this);
                break;
            case R.id.btn_weixin_login:
                WXLogin();
                break;
            case R.id.btn_protocol:  //用户协议
                UrlInfoActivity.start(CodeLoginActivity.this, UrlConstants.INSTANCE.getUSER_PROTOCOL(), getResources().getString(R.string.user_agreement));

                break;
            case R.id.btn_privacy:  //隐私协议
                UrlInfoActivity.start(CodeLoginActivity.this, UrlConstants.INSTANCE.getUSER_YS_PRO(), getResources().getString(R.string.privacy_agreement));

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_PwdLogin) {
                if (data.getBooleanExtra(Constants.Key.KEY_1, false)) {
                    this.finish();
                } else {
                    String phone = data.getStringExtra(Constants.Key.KEY_2);
                    if (!StringUtils.isTextEmpty(phone)) {
                        ed_login_mobile.setText(phone);
                        ed_login_mobile.setSelection(ed_login_mobile.getText().length());
                        if (phone.length() == 13 && btn_getcode != null && !btn_getcode.getText().toString().contains("s")) {
                            btn_getcode.setTextColor(getResources().getColor(R.color.color_text1));
                            btn_getcode.setClickable(true);
                        } else {
                            btn_getcode.setTextColor(getResources().getColor(R.color.color_text4));
                            btn_getcode.setClickable(false);
                        }
                    }
                }
            } else if (requestCode == requestCode_BindPhone || requestCode == requestCode_BindInviteId) {
                this.finish();
            }
        }
    }

    public void checkImLogin() {
        if (!getIntent().getBooleanExtra(IntentKey.KICK_OUT, false)) {
            return;
        }
        int type = NIMClient.getService(AuthService.class).getKickedClientType();
        String client;
        switch (type) {
            case ClientType.Web:
                client = "网页端";
                break;
            case ClientType.Windows:
            case ClientType.MAC:
                client = "电脑端";
                break;
            case ClientType.REST:
                client = "服务端";
                break;
            default:
                client = "移动端";
                break;
        }
        EasyAlertDialogHelper.showOneButtonDiolag(this,
                getString(R.string.kickout_notify),
                String.format(getString(R.string.kickout_content),
                        client), getString(R.string.ok),
                true, null);
    }

    @Override
    protected void onDestroy() {
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        super.onDestroy();
    }

    private boolean mIsExit;

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                SCApp.getInstance().finishAllActivities();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
