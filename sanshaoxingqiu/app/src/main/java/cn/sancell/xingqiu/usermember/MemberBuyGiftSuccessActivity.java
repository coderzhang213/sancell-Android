package cn.sancell.xingqiu.usermember;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.OrderNewPackInfoActivity;
import cn.sancell.xingqiu.homeuser.OrderPackInfoActivity;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.InviterBean;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.usermember.contract.MemberBuyGiftSuccessContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.CusScrollview;

/**
 * 礼包购买成功界面
 */
public class MemberBuyGiftSuccessActivity extends BaseMVPActivity<MemberBuyGiftSuccessContract.MemberBuyGiftSuccessPresenter>
        implements MemberBuyGiftSuccessContract.MemberBuyGiftSuccessView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_back_black)
    ImageView btn_back_black;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_black)
    TextView tv_title_black;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.view_white)
    View view_white;

    @BindView(R.id.mScrollView)
    CusScrollview mScrollView;
    @BindView(R.id.sdv_memberlevel_card)
    ImageView sdv_memberlevel_card;
    @BindView(R.id.tv_member_level_name)
    TextView tv_member_level_name;
    @BindView(R.id.rl_inviter_id)
    RelativeLayout rl_inviter_id;
    @BindView(R.id.rl_edit_id)
    RelativeLayout rl_edit_id;
    @BindView(R.id.ed_id)
    EditText ed_id;
    @BindView(R.id.btn_sure)
    TextView btn_sure;
    @BindView(R.id.rl_inviter_info)
    RelativeLayout rl_inviter_info;
    @BindView(R.id.riv_inviter_photo)
    SimpleDraweeView riv_inviter_photo;
    @BindView(R.id.tv_inviter_name)
    TextView tv_inviter_name;
    @BindView(R.id.tv_inviter_id)
    TextView tv_inviter_id;

    @BindView(R.id.btn_parcelId)
    TextView btn_parcelId;

    UserBean userBean;
    int upMemberlevel;


    @Override
    protected MemberBuyGiftSuccessContract.MemberBuyGiftSuccessPresenter createPresenter() {
        return new MemberBuyGiftSuccessContract.MemberBuyGiftSuccessPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_buy_gift_success;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(MemberBuyGiftSuccessActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        final int scrllow_h = ScreenUtils.dip2px(MemberBuyGiftSuccessActivity.this, 45);
        mScrollView.setScrollViewListenner(new CusScrollview.ScrollViewListenner() {
            @Override
            public void onScrollChanged(CusScrollview view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    rl_top.setBackgroundColor(Color.argb( 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                    //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (scrollY > 0 && scrollY <= scrllow_h) {
                    float scale = (float) scrollY / scrllow_h;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    rl_top.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    tv_title.setTextColor(Color.argb((255 - (int) alpha), 255, 255, 255));
                    tv_title_black.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                    btn_back_black.setImageAlpha((int) alpha);
                    btn_back.setImageAlpha((255 - (int) alpha));
                } else {
                    rl_top.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }

            }
        });
        btn_back.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_parcelId.setOnClickListener(this);
        btn_parcelId.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_parcelId.getPaint().setAntiAlias(true);//抗锯齿
        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 440 / 375));

        //mPresenter.GetUserInfo(MemberBuyGiftSuccessActivity.this);
        mNetworkErrorLayout.setVisibility(View.GONE);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        upMemberlevel = Integer.parseInt(PreferencesUtils.getString(Constants.Key.KEY_orderMemberLevel, "0"));
        if (upMemberlevel > 1) {
            rl_inviter_id.setVisibility(View.VISIBLE);
            view_white.setVisibility(View.VISIBLE);
            if (userBean.getInviteFromUid() != 0) {
                mPresenter.GetInviterInfo(MemberBuyGiftSuccessActivity.this, userBean.getInviteFromUid() + "");
            }
        } else {
            rl_inviter_id.setVisibility(View.GONE);
            view_white.setVisibility(View.GONE);
        }
        switch (upMemberlevel) {
            case 2:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success1);
                tv_member_level_name.setText("恭喜您成为三少医美" + "银猩VIP");
                tv_member_level_name.setTextColor(getResources().getColor(R.color.color_text1));
                break;
            case 3:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success2);
                tv_member_level_name.setText("恭喜您成为三少医美" + "金猩VIP");
                tv_member_level_name.setTextColor(getResources().getColor(R.color.color_text1));
                break;
            case 4:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success3);
                tv_member_level_name.setText("恭喜您成为三少医美" + "红猩VIP");
                tv_member_level_name.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
        }
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (upMemberlevel > 1 && rl_inviter_info.getVisibility() == View.GONE) { //未填写邀请人id
                    DialogUtil.showOperateDialog(MemberBuyGiftSuccessActivity.this, "跳过填写，后续将无法再次添加 确认返回吗？", "", "取消", "确认", new DialogUtil.ClickSureAction() {
                        @Override
                        public void sureAction(int postion) {
                            MemberBuyGiftSuccessActivity.this.finish();
                            //startActivity(new Intent(MemberBuyGiftSuccessActivity.this, UserMemberCenterActivity.class));
                        }
                    });
                } else {
                    MemberBuyGiftSuccessActivity.this.finish();
                    //startActivity(new Intent(MemberBuyGiftSuccessActivity.this, UserMemberCenterActivity.class));
                }
                break;
            case R.id.btn_sure:
                if (StringUtils.isTextEmpty(ed_id.getText().toString())) {
                    return;
                }
                mPresenter.GetInviterInfo(MemberBuyGiftSuccessActivity.this, ed_id.getText().toString());
                break;
            case R.id.btn_parcelId:
                if (!StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_orderId, "")) && !StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_orderParceId, ""))) {
                    Intent intent = new Intent(MemberBuyGiftSuccessActivity.this, OrderNewPackInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, PreferencesUtils.getString(Constants.Key.KEY_orderParceId, ""));
                    intent.putExtra(Constants.Key.KEY_2, PreferencesUtils.getString(Constants.Key.KEY_orderId, ""));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.GetInviterInfo(MemberBuyGiftSuccessActivity.this, userBean.getInviteFromUid() + "");
            }
        });
    }

    @Override
    public void getUserInfoSuccess() {
        /*mNetworkErrorLayout.setVisibility(View.GONE);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (userBean.getRealMemberLevel() > 2) {
            rl_inviter_id.setVisibility(View.VISIBLE);
            view_white.setVisibility(View.VISIBLE);
            if (userBean.getInviteFromUid() != 0) {
                mPresenter.GetInviterInfo(MemberBuyGiftSuccessActivity.this, userBean.getInviteFromUid() + "");
            }
        } else {
            rl_inviter_id.setVisibility(View.GONE);
            view_white.setVisibility(View.GONE);
        }
        switch (userBean.getRealMemberLevel()) {
            case 2:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success1);
                break;
            case 3:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success2);
                break;
            case 4:
                sdv_memberlevel_card.setImageResource(R.mipmap.icon_member_level_success3);
                break;
        }
        MemberLevelListBean memberLevelListBean = PreferencesUtils.readObject(Constants.Key.KEY_MEMBERLEVEL, MemberLevelListBean.class);
        if (memberLevelListBean != null) {
            List<MemberLevelListBean.MemberLevelBean> data_level = memberLevelListBean.getDataList();
            if (data_level != null) {
                for (MemberLevelListBean.MemberLevelBean temp : data_level) {
                    if (temp.getMemberLevelId() == userBean.getRealMemberLevel()) {
                        //sdv_memberlevel_card.setImageURI(Uri.parse(temp.getBuyMemberVipSuccessfulImageUrl()));
                        tv_member_level_name.setText("恭喜您成为三少医美" + temp.getMemberLevelStr());
                        tv_member_level_name.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                        break;
                    }
                }
            }
        }*/
    }

    InviterBean inviterBean;

    @Override
    public void getInviterInfoSuccess(InviterBean inviterBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        this.inviterBean = inviterBean;
        if (userBean != null && userBean.getInviteFromUid() != 0) {
            rl_edit_id.setVisibility(View.GONE);
            rl_inviter_info.setVisibility(View.VISIBLE);
            riv_inviter_photo.setImageURI(Uri.parse(inviterBean.getGravatar()));
            tv_inviter_name.setText(inviterBean.getNickName());
            tv_inviter_id.setText("ID：" + userBean.getInviteFromUid());
        } else {
            showOperateDialog();
        }
    }

    @Override
    public void bindInviterSuccess() {
        dialog_sure_inviter.dismiss();
        rl_edit_id.setVisibility(View.GONE);
        rl_inviter_info.setVisibility(View.VISIBLE);
        riv_inviter_photo.setImageURI(Uri.parse(inviterBean.getGravatar()));
        tv_inviter_name.setText(inviterBean.getNickName());
        tv_inviter_id.setText("ID：" + ed_id.getText().toString());
    }

    Dialog dialog_sure_inviter;

    public void showOperateDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_sure_inviter,
                null);
        dialog_sure_inviter = new Dialog(MemberBuyGiftSuccessActivity.this, R.style.transparentFrameWindowStyle);
        dialog_sure_inviter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_sure_inviter.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(MemberBuyGiftSuccessActivity.this) * 295 / 375,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_sure_inviter.setCanceledOnTouchOutside(false);
        TextView dialog_tv_inviter_name = view.findViewById(R.id.dialog_tv_inviter_name);
        SimpleDraweeView dialog_riv_inviter_photo = view.findViewById(R.id.dialog_riv_inviter_photo);
        dialog_tv_inviter_name.setText(inviterBean.getNickName());
        dialog_riv_inviter_photo.setImageURI(Uri.parse(inviterBean.getGravatar()));
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(view1 -> {
            dialog_sure_inviter.dismiss();
        });
        tv_sure.setOnClickListener(view12 -> {
            mPresenter.BindInviterId(MemberBuyGiftSuccessActivity.this, ed_id.getText().toString());
        });
        dialog_sure_inviter.show();
    }


    @Override
    /**
     * 返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (upMemberlevel > 1 && rl_inviter_info.getVisibility() == View.GONE) { //未填写邀请人id
                DialogUtil.showOperateDialog(MemberBuyGiftSuccessActivity.this, "跳过填写，后续将无法再次添加 确认返回吗？", "", "取消", "确认", new DialogUtil.ClickSureAction() {
                    @Override
                    public void sureAction(int postion) {
                        MemberBuyGiftSuccessActivity.this.finish();
                        //startActivity(new Intent(MemberBuyGiftSuccessActivity.this, UserMemberCenterActivity.class));
                    }
                });

            } else {
                MemberBuyGiftSuccessActivity.this.finish();
                //startActivity(new Intent(MemberBuyGiftSuccessActivity.this, UserMemberCenterActivity.class));
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startActivity(new Intent(MemberBuyGiftSuccessActivity.this, UserMemberCenterActivity.class));
    }
}
