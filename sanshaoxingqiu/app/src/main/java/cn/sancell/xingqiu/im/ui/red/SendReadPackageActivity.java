package cn.sancell.xingqiu.im.ui.red;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.homeshoppingcar.ProductCreateOrderActivity;
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckPhoneActivity;
import cn.sancell.xingqiu.im.entity.req.SendRpReq;
import cn.sancell.xingqiu.im.entity.res.PayPassRes;
import cn.sancell.xingqiu.im.entity.res.SendRpRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.util.BigDecimalUtils;
import cn.sancell.xingqiu.util.MoneyValueFilter;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;

/**
 * @author Alan_Xiong
 * @desc: 发送红包页面
 * @time 2019-11-17 14:27
 */
public class SendReadPackageActivity extends BaseMVPActivity<SendReadPresenter> implements SendRedView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_rp_count)
    AppCompatEditText et_rp_count;
    @BindView(R.id.tv_team_num)
    AppCompatTextView tv_team_num;
    @BindView(R.id.et_rp_money)
    AppCompatEditText et_rp_money;
    @BindView(R.id.tv_rp_money)
    AppCompatTextView tv_rp_money;
    @BindView(R.id.tv_change_type)
    AppCompatTextView tv_change_type;
    @BindView(R.id.et_rp_desc)
    AppCompatEditText et_rp_desc;
    @BindView(R.id.tv_rmb)
    AppCompatTextView tv_rmb;
    @BindView(R.id.btn_send_rp)
    AppCompatButton btn_send_rp;
    @BindView(R.id.tv_head_tip)
    AppCompatTextView tv_head_tip;
    @BindView(R.id.tv_current_type)
    AppCompatTextView tv_current_type;

    //红包类型
    private int mRpType = 2; //凭手气2 普通1
    private SendRpReq mReq;
    private int teamNum = 0;//群人数
    private String mBalance;

    public static void start(Activity context, int requestCode) {
        Intent intent = new Intent(context, SendReadPackageActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected SendReadPresenter createPresenter() {
        return new SendReadPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_send_red;
    }

    @Override
    protected void initial() {
        //  StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        //获取群人数
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_actionbar_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        NIMClient.getService(TeamService.class).queryTeam(SessionHelper.rpTeamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                teamNum = team.getMemberCount();
                tv_team_num.setText(String.format(getResources().getString(R.string.team_total_num), teamNum));
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
        initListener();
        mPresenter.getRpYue();

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    public void initListener() {
        //设置金额输入过滤
        et_rp_money.setFilters(new InputFilter[]{new MoneyValueFilter()});
        btn_send_rp.setOnClickListener(this);
        tv_change_type.setOnClickListener(this);
        et_rp_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNormalMoney();
                checkAndTip();
            }
        });
        et_rp_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    tv_rmb.setText(getResources().getString(R.string.money_zero));
                } else {
                    if (mRpType == UiHelper.PIN_RP_TYPE) {
                        //拼手气为总金额
                        tv_rmb.setText(BigDecimalUtils.add(s.toString(), "0", 2));
                    } else {
                        checkNormalMoney();
                    }

                }
                checkAndTip();
            }
        });
    }

    //普通红包 计算总金额
    public void checkNormalMoney() {
        if (TextUtils.isEmpty(et_rp_money.getText().toString()) || TextUtils.isEmpty(et_rp_count.getText().toString())) {
            tv_rmb.setText(getResources().getString(R.string.money_zero));
        } else {
            if (mRpType == UiHelper.PIN_RP_TYPE) {
                tv_rmb.setText(BigDecimalUtils.add(et_rp_money.getText().toString(), "0", 2));

            } else {
                tv_rmb.setText(BigDecimalUtils.mul(et_rp_count.getText().toString(), et_rp_money.getText().toString(), 2));
            }
        }
    }

    /**
     * 切换红包类型 重算金额等数据
     *
     * @param single 是否是单个红包
     */
    public void checkTypeChangeMoney(boolean single) {
        if (TextUtils.isEmpty(et_rp_money.getText().toString()) || TextUtils.isEmpty(et_rp_count.getText().toString())) {
            tv_rmb.setText(getResources().getString(R.string.money_zero));
        } else {
            if (single) {
                et_rp_money.setText(BigDecimalUtils.div(et_rp_money.getText().toString(), et_rp_count.getText().toString(), 2));
                tv_rmb.setText(BigDecimalUtils.mul(et_rp_money.getText().toString(), et_rp_count.getText().toString(), 2));
            } else {
                et_rp_money.setText(BigDecimalUtils.mul(et_rp_money.getText().toString(), et_rp_count.getText().toString(), 2));
                tv_rmb.setText(BigDecimalUtils.add(et_rp_money.getText().toString(), "0", 2));

            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_change_type) {
            //改变红包类型
            changeRpType();
        } else if (v.getId() == R.id.btn_send_rp) {
            sendRp();
        }

    }

    public void changeRpType() {
        if (mRpType == UiHelper.PIN_RP_TYPE) {
            //切换普通
            tv_change_type.setText("改为拼手气红包");
            tv_current_type.setText("当前为普通红包，");
            tv_rp_money.setText("单个金额");
            tv_rp_money.setCompoundDrawables(null, null, null, null);
            mRpType = UiHelper.NOR_RP_TYPE;
            checkTypeChangeMoney(true);
        } else {
            tv_change_type.setText("改为普通红包");
            tv_current_type.setText("当前为拼手气红包，");
            tv_rp_money.setText("总金额");
            Drawable nav_right = getResources().getDrawable(R.mipmap.icon_rp_pin);
            nav_right.setBounds(0, 0, nav_right.getMinimumWidth(), nav_right.getMinimumHeight());
            tv_rp_money.setCompoundDrawables(null, null, nav_right, null);
            mRpType = UiHelper.PIN_RP_TYPE;
            checkTypeChangeMoney(false);
        }
    }

    public void sendRp() {
        if (TextUtils.isEmpty(et_rp_count.getText().toString()) || Double.parseDouble(et_rp_count.getText().toString()) <= 0) {
            SCApp.getInstance().showSystemCenterToast("请输入红包个数");
            return;
        }
        if (TextUtils.isEmpty(et_rp_money.getText().toString()) || Double.parseDouble(et_rp_money.getText().toString()) <= 0) {
            SCApp.getInstance().showSystemCenterToast("请输入红包金额");
            return;
        }
        mPresenter.checkPass(new BaseReq());
    }

    @Override
    public void sendRpSuccess(SendRpRes res, SendRpReq req) {
        //拿到红包id - 发送消息
        Intent intent = new Intent();
        intent.putExtra(IntentKey.rp_id, res.redId);
        intent.putExtra(IntentKey.rp_content, req.showInfo);
        intent.putExtra(IntentKey.rp_title, "我的红包");
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void sendRpError(String str) {
        if (dialog_check_pay_pwd != null && dialog_check_pay_pwd.isShowing()) {
            dialog_ed_pay_pwd.setText("");
        }
        SCApp.getInstance().showSystemCenterToast(str);
    }

    @Override
    public void checkPassSuccess(PayPassRes res) {
        if (res != null) {
            if (res.check == 1) {
                //输入密码支付
                showCheckPayPwdDialog();
            } else {
                //跳转密码设置
                ModifyPayPwdCheckPhoneActivity.start(this);
            }
        }
    }

    @Override
    public void checkPassError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void getYueSuccess(YueRes res) {
        mBalance = res.balance;
    }

    @Override
    public void getYueError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }


    public void checkAndTip() {
        //数量判断
        if (!TextUtils.isEmpty(et_rp_count.getText().toString())) {
            int sendCount = Integer.parseInt(et_rp_count.getText().toString());
//            if ( sendCount > 100) {
//                tv_head_tip.setVisibility(View.VISIBLE);
//                tv_head_tip.setText("红包数量不能超过100");
//                btn_send_rp.setEnabled(false);
//                return;
//            }
            if (sendCount > teamNum) {
                tv_head_tip.setVisibility(View.VISIBLE);
                tv_head_tip.setText("红包数量不能超过群组人数");
                btn_send_rp.setEnabled(false);
                return;
            }
        }

        //金额判断
        if (!TextUtils.isEmpty(et_rp_money.getText().toString())) {
            int count = 0;
            if (!TextUtils.isEmpty(et_rp_count.getText())) {
                count = Integer.parseInt(et_rp_count.getText().toString());
            }
            if (count > 0) {
                tv_head_tip.setVisibility(View.VISIBLE);
                String avg = "0";
                if (mRpType == UiHelper.PIN_RP_TYPE) {
                    String total = BigDecimalUtils.mul(et_rp_money.getText().toString(), "100", 0);
                    avg = BigDecimalUtils.div(total, count + "", 0);
                } else {
                    avg = BigDecimalUtils.mul(et_rp_money.getText().toString(), "100", 0);
                }

                if (Integer.parseInt(avg) < 1) {
                    tv_head_tip.setVisibility(View.VISIBLE);
                    tv_head_tip.setText("平均红包金额不能低于0.01");
                    btn_send_rp.setEnabled(false);
                    return;
                }
            }
            int maxMoney = 200;
            if (mRpType == UiHelper.PIN_RP_TYPE) {//拼手气最大红包金额
                maxMoney = 500;
            } else {
                maxMoney = 200;
            }
            if (Integer.parseInt(BigDecimalUtils.mul(et_rp_money.getText().toString(), "100", 0)) > maxMoney * 100) {
                tv_head_tip.setVisibility(View.VISIBLE);
                tv_head_tip.setText("红包金额不能高于" + maxMoney);
                btn_send_rp.setEnabled(false);
                return;
            }


            if (!"null".equals(tv_rmb.getText().toString())) {

                if (BigDecimalUtils.compare(tv_rmb.getText().toString(), mBalance + "")) {
                    tv_head_tip.setVisibility(View.VISIBLE);
                    tv_head_tip.setText("当前余额不足");
                    btn_send_rp.setEnabled(false);
                    return;
                }
            }

        }
        btn_send_rp.setEnabled(true);
        tv_head_tip.setVisibility(View.INVISIBLE);

    }

    public void buildReq() {
        //发起请求 - 请求成功后发送消息
        mReq = new SendRpReq();
        mReq.redNum = et_rp_count.getText().toString();
        if (TextUtils.isEmpty(et_rp_desc.getText())) {
            mReq.showInfo = et_rp_desc.getHint().toString();
        } else {
            mReq.showInfo = et_rp_desc.getText().toString();
        }
        mReq.redType = mRpType + "";
        mReq.source = "1";
        if (mRpType == UiHelper.NOR_RP_TYPE) {
            //单个红包金额
            mReq.singleMoney = Double.parseDouble(et_rp_money.getText().toString()) * 100 + "";
            mReq.sumMoney = BigDecimalUtils.mul(mReq.singleMoney, mReq.redNum, 0);
        } else {
            mReq.sumMoney = Double.parseDouble(tv_rmb.getText().toString()) * 100 + "";
            mReq.singleMoney = "0";
        }
    }

    /**
     * 输入支付密码对话框
     */
    private Dialog dialog_check_pay_pwd;
    private VerificationCodeEditText dialog_ed_pay_pwd;
    private TextView tv_error_tip;

    public void showCheckPayPwdDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_create_order_check_pay_pwd,
                null);
        dialog_check_pay_pwd = new Dialog(this, R.style.dialogPayPwdWindowStyle);
        dialog_check_pay_pwd.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog_check_pay_pwd.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom_pay_pwd);
        // 设置点击外围解散
        dialog_check_pay_pwd.setCanceledOnTouchOutside(true);
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        dialog_ed_pay_pwd = view.findViewById(R.id.ed_pay_pwd);
        dialog_ed_pay_pwd.setFocusable(true);
        dialog_ed_pay_pwd.requestFocus();
        tv_error_tip = view.findViewById(R.id.tv_error_tip);
        iv_dialog_close.setOnClickListener(view1 -> dialog_check_pay_pwd.dismiss());
        dialog_ed_pay_pwd.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tv_error_tip.setText("");
                }
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                buildReq();
                mReq.accountPassword = RSAUtils.encryptByPublic(s.toString());
                mPresenter.sendRp(mReq);
            }
        });

        dialog_check_pay_pwd.show();
    }
}
