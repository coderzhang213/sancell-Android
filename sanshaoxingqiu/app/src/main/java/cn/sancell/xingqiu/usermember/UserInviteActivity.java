package cn.sancell.xingqiu.usermember;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.dialog.UserInviteSureDialog;
import cn.sancell.xingqiu.dialog.UserInviteTipDialog;
import cn.sancell.xingqiu.dialog.listener.OnInviteSureListener;
import cn.sancell.xingqiu.dialog.listener.OnInviteTipListener;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.InviterBean;
import cn.sancell.xingqiu.usermember.contract.UserInviteActivityPresenter;
import cn.sancell.xingqiu.usermember.contract.UserInviteActivityView;
import cn.sancell.xingqiu.usermember.req.UserInviteReq;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 邀请人activity
 * @time 2019-10-22 12:57
 */
public class UserInviteActivity extends BaseMVPActivity<UserInviteActivityPresenter> implements UserInviteActivityView {

    @BindView(R.id.et_inviter_id)
    AppCompatEditText et_id;
    @BindView(R.id.tv_save)
    AppCompatTextView tv_save;
    @BindView(R.id.tv_jump)
    AppCompatTextView tv_jump;

    private InviterBean mData;
    private UserInviteSureDialog mSureDialog;


    public static Intent getIntent(Context context) {
        return new Intent(context, UserInviteActivity.class);
    }

    @Override
    protected UserInviteActivityPresenter createPresenter() {
        return new UserInviteActivityPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.actvity_user_invite;
    }

    @Override
    protected void initial() {
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_jump.getLayoutParams();
            lp.topMargin = statusHeight;
            tv_jump.setLayoutParams(lp);
        }
        tv_save.setEnabled(false);
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    tv_save.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_save.setEnabled(true);
                } else {
                    tv_save.setTextColor(getResources().getColor(R.color.colorWhite_tran66));
                    tv_save.setEnabled(false);
                }
            }
        });

        tv_save.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_id.getText().toString())) {
                Toast.makeText(getApplicationContext(), getString(R.string.user_bind_invite_input_id), Toast.LENGTH_SHORT).show();
                return;
            }
            getInviteInfo(et_id.getText().toString(), false);
        });
        tv_jump.setOnClickListener(v -> showBindTipDialog("", getString(R.string.user_bind_invite_go_on_bind)));
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String msg) {
        SCApp.getInstance().showSystemCenterToast(msg);
    }

    @Override
    public void getInviteSuccess(InviterBean inviterBean) {
        if (inviterBean != null) {
            mData = inviterBean;
            showSureBindDialog();
        } else {
            SCApp.getInstance().showSystemCenterToast("数据异常");
        }
    }

    @Override
    public void bindInviteSuccess() {
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        userBean.setInviteFromUid(Integer.valueOf(et_id.getText().toString()));
        PreferencesUtils.put(Constants.Key.KEY_USERINFO, userBean);
        if (mSureDialog != null && mSureDialog.isShowing()) {
            mSureDialog.dismiss();
        }
        setResult(RESULT_OK);
//        Intent intent = new Intent();
//        intent.setClass(UserInviteActivity.this, MainActivity.class);
//        startActivity(intent);
        HomeTabsActivity.Companion.start(this, 0);
        finish();
    }

    /**
     * 确认绑定
     */
    public void showSureBindDialog() {
        if (mData != null) {
            //if (mSureDialog == null) {

            mSureDialog = UserInviteSureDialog.newInstance(mData.getNickName(), mData.getGravatar());
            mSureDialog.setListener(new OnInviteSureListener() {
                @Override
                public void onSure() {
                    getInviteInfo(et_id.getText().toString(), true);
                }

                @Override
                public void onBack() {
                    mSureDialog.dismiss();
                }
            });
            //}
            mSureDialog.show(getSupportFragmentManager(), "sure");
        }
    }

    /**
     * 其他绑定提示
     *
     * @param msg
     */
    public void showBindTipDialog(String msg, String btnStr) {
        UserInviteTipDialog tipDialog = UserInviteTipDialog.newInstance(msg, btnStr);
        tipDialog.setTipListener(new OnInviteTipListener() {
            @Override
            public void onBind() {
                tipDialog.dismiss();
            }

            @Override
            public void onUnBind() {
                HomeTabsActivity.Companion.start(UserInviteActivity.this, 0);
//                Intent intent = new Intent();
//                intent.setClass(UserInviteActivity.this, MainActivity.class);
//                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });
        tipDialog.show(getSupportFragmentManager(), "tip");
    }


    /**
     * 绑定请求
     *
     * @param id   输入id
     * @param bind 是否绑定
     */
    public void getInviteInfo(String id, boolean bind) {
        UserInviteReq req = new UserInviteReq();
        req.inviteUserId = id;
        if (bind) {
            mPresenter.getBindInviteInfo(req);
        } else {
            mPresenter.getUserInviteInfo(req);
        }
    }

}
