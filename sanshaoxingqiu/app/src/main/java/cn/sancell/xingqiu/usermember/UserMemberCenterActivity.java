package cn.sancell.xingqiu.usermember;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.OrderNewPackInfoActivity;
import cn.sancell.xingqiu.homeuser.OrderPackInfoActivity;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.adapter.MemberPrivilegeListAdapter;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.usermember.bean.MemberPrivilegeListBean;
import cn.sancell.xingqiu.usermember.contract.UserMemberCenterContract;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.CusScrollview;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

public class UserMemberCenterActivity extends BaseMVPActivity<UserMemberCenterContract.MemberCenterPresenter>
        implements UserMemberCenterContract.MemberCenterView, View.OnClickListener {
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

    @BindView(R.id.mScrollView)
    CusScrollview mScrollView;
    @BindView(R.id.iv_memberlevel_card)
    ImageView iv_memberlevel_card;
    @BindView(R.id.iv_member_level_name)
    ImageView iv_member_level_name;
    @BindView(R.id.sdv_member_photo)
    SimpleDraweeView sdv_member_photo;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_member_id_tip)
    TextView tv_member_id_tip;
    @BindView(R.id.tv_member_id)
    TextView tv_member_id;
    @BindView(R.id.iv_copy)
    ImageView iv_copy;
    @BindView(R.id.tv_member_valid_time)
    TextView tv_member_valid_time;
    @BindView(R.id.iv_privilege_mark)
    ImageView iv_privilege_mark;

    @BindView(R.id.rcv_privilege)
    RecyclerView rcv_privilege;
    private MemberPrivilegeListAdapter memberPrivilegeListAdapter;


    @BindView(R.id.tv_privilege_detail)
    TextView tv_privilege_detail;
    @BindView(R.id.tv_member_agreement)
    TextView tv_member_agreement;
    @BindView(R.id.ll_gift_info)
    LinearLayout ll_gift_info;
    @BindView(R.id.tv_member_open_record)
    TextView tv_member_open_record;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.tv_member_pack)
    TextView tv_member_pack;

    @BindView(R.id.rl_save_price)
    RelativeLayout rl_save_price;
    @BindView(R.id.tv_save_price)
    RelativeSizeTextView tv_save_price;


    private int[] card_bgs = new int[]{R.mipmap.icon_member_center_card0, R.mipmap.icon_member_center_card1,
            R.mipmap.icon_member_center_card2, R.mipmap.icon_member_center_card3};
    private int[] card_names = new int[]{R.mipmap.icon_member_center_name0, R.mipmap.icon_member_center_name1,
            R.mipmap.icon_member_center_name2, R.mipmap.icon_member_center_name3};
    private int[] card_colors = new int[]{R.color.colorWhite, R.color.color_text1,
            R.color.color_text1, R.color.colorWhite};


    @Override
    protected UserMemberCenterContract.MemberCenterPresenter createPresenter() {
        return new UserMemberCenterContract.MemberCenterPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_member_center;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetUserInfo(this);
    }


    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(UserMemberCenterActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        final int scrllow_h = ScreenUtils.dip2px(UserMemberCenterActivity.this, 45);
        mScrollView.setScrollViewListenner((view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY <= 0) {
                rl_top.setBackgroundColor(Color.argb(0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
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
                rl_top.setBackgroundColor(Color.argb(255, 255, 255, 255));
                //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

        });
        btn_back.setOnClickListener(this);
        btn_back_black.setOnClickListener(this);
        iv_copy.setOnClickListener(this);
        tv_privilege_detail.setOnClickListener(this);
        tv_member_open_record.setOnClickListener(this);
        tv_member_pack.setOnClickListener(this);
        tv_member_agreement.setOnClickListener(this);
        rl_save_price.setOnClickListener(this);
        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 440 / 375));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_privilege.setLayoutManager(layoutManager);

        tv_member_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_member_agreement.getPaint().setAntiAlias(true);//抗锯齿

        if (userBean != null && userBean.getMemberVipOrderParcelInfo() != null && !StringUtils.isTextEmpty(userBean.getMemberVipOrderParcelInfo().getOrderId())) {
            tv_member_pack.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
        } else {
            tv_member_pack.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
        }
    }


    @Override
    protected BaseView getView() {
        return this;
    }

    String agreement = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
            case R.id.btn_back_black:
                finish();
                startActivity(new Intent(UserMemberCenterActivity.this, HomeTabsActivity.class));
                break;
            case R.id.iv_copy: //复制id
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_member_id.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast(R.string.copy_success);
                break;
            case R.id.tv_privilege_detail:  //权益详情
                privilegeInfo();
                break;
            case R.id.tv_member_open_record:  //开通记录
                startActivity(new Intent(UserMemberCenterActivity.this, MemberOpenRecordsListActivity.class));
                break;
            /*case R.id.tv_member_detail:
                if (view_pager.getCurrentItem() == 0) {
                    showDetailDialog(R.mipmap.icon_member_level1_detailinfo);
                } else {
                    showDetailDialog(R.mipmap.icon_member_level2_detailinfo);
                }
                break;*/
            case R.id.tv_member_pack:
                if (userBean != null) {
                    Intent intent1 = new Intent(UserMemberCenterActivity.this, OrderNewPackInfoActivity.class);
                    intent1.putExtra(Constants.Key.KEY_2, userBean.getMemberVipOrderParcelInfo().getOrderId());
                    intent1.putExtra(Constants.Key.KEY_1, userBean.getMemberVipOrderParcelInfo().getParcelId());
                    startActivity(intent1);
                }
                break;
            case R.id.tv_member_agreement://会员协议
                if (StringUtils.isTextEmpty(agreement)) {
                    switch (userBean.getRealMemberLevel()){
                        case 1:
                            agreement="https://mapi.sanshaoxingqiu.cn/m/event/member-vip-one-agreement";
                            break;
                        case 2:
                            agreement="https://mapi.sanshaoxingqiu.cn/m/event/member-vip-two-agreement";
                            break;
                        case 3:
                            agreement="https://mapi.sanshaoxingqiu.cn/m/event/member-vip-three-agreement";
                            break;
                        case 4:
                            agreement="https://mapi.sanshaoxingqiu.cn/m/event/member-vip-four-agreement";
                            break;
                    }
                    /*List<StartupDataBean.MemberAgreementUrlData> memberAgreementUrlData = PreferencesUtils.readMemberAgreementListObject();
                    if (memberAgreementUrlData != null) {
                        for (StartupDataBean.MemberAgreementUrlData temp : memberAgreementUrlData
                                ) {
                            if (temp.getMemberLevelId() == userBean.getRealMemberLevel()) {
                                agreement = temp.getMemberVipAgreementUrl();
                                break;
                            }
                        }
                    }*/
                }
                if (StringUtils.isTextEmpty(agreement)) {
                    return;
                }
                Intent intent2 = new Intent(UserMemberCenterActivity.this, UrlInfoActivity.class);
                intent2.putExtra(Constants.Key.KEY_1, agreement);
                intent2.putExtra(Constants.Key.KEY_2, "");
                startActivity(intent2);
                break;
            case R.id.rl_save_price:  //节省总额
                if (userBean != null) {
                    Intent intent1 = new Intent(UserMemberCenterActivity.this, MemberSavePriceListActivity.class);
                    intent1.putExtra(Constants.Key.KEY_1, userBean.getEconomyMoneyE2());
                    startActivity(intent1);
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
        tv_refresh.setOnClickListener(view -> {
            mPresenter.GetUserInfo(UserMemberCenterActivity.this);
        });
    }

    /**
     * 会员权益详情跳转
     */
    public void privilegeInfo(){
        Intent intent = new Intent(UserMemberCenterActivity.this, MemberPrivilegeInfoActivity.class);
        switch (userBean.getRealMemberLevel()) {
            case 1:
                intent.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Normal_Privilege);
                break;
            case 2:
                intent.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Silver_Privilege);
                break;
            case 3:
                intent.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Golden_Privilege);
                break;
            case 4:
                intent.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Red_Privilege);
                break;
        }
        startActivity(intent);
    }

    UserBean userBean;

    @Override
    public void getUserInfoSuccess() {
        mNetworkErrorLayout.setVisibility(View.GONE);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        tv_nickname.setText(userBean.getNickname());
        sdv_member_photo.setImageURI(Uri.parse(userBean.getGravatar()));
        tv_member_id.setText(userBean.getUserId() + "");
        tv_member_valid_time.setText(userBean.getVipPeriodOfValidity());
        tv_save_price.setTagText(StringUtils.getAllPrice(userBean.getEconomyMoneyE2()));
        if (userBean != null) {
            if (userBean.getRealMemberLevel() > 1) {
                ll_gift_info.setVisibility(View.VISIBLE);
                tv_member_valid_time.setVisibility(View.VISIBLE);
                iv_privilege_mark.setImageResource(R.mipmap.icon_member_privilege_title);
            } else {
                ll_gift_info.setVisibility(View.GONE);
                tv_member_valid_time.setVisibility(View.GONE);
                iv_privilege_mark.setImageResource(R.mipmap.icon_normal_member_privilege_title);
            }
            iv_memberlevel_card.setImageResource(card_bgs[userBean.getRealMemberLevel() - 1]);
            iv_member_level_name.setImageResource(card_names[userBean.getRealMemberLevel() - 1]);
            tv_nickname.setTextColor(getResources().getColor(card_colors[userBean.getRealMemberLevel() - 1]));
            tv_member_id_tip.setTextColor(getResources().getColor(card_colors[userBean.getRealMemberLevel() - 1]));
            tv_member_id.setTextColor(getResources().getColor(card_colors[userBean.getRealMemberLevel() - 1]));
            tv_member_valid_time.setTextColor(getResources().getColor(card_colors[userBean.getRealMemberLevel() - 1]));
        }
        //mPresenter.GetMemberLevelInfo(this);
        mPresenter.GetMemberPrivilege(userBean.getRealMemberLevel() + "", this);
    }

    @Override
    public void getMemberPrivilegeSuccess(List<MemberPrivilegeListBean.MemberPrivilegeBean> dataList) {
        memberPrivilegeListAdapter = new MemberPrivilegeListAdapter(dataList);
        rcv_privilege.setAdapter(memberPrivilegeListAdapter);
        memberPrivilegeListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.sdv_pic:
                    privilegeInfo();
                    break;
            }
        });
    }

    @Override
    public void getMemberLevelListSuccess(List<MemberLevelListBean.MemberLevelBean> data_level) {
        /*if (data_level != null) {
            for (MemberLevelListBean.MemberLevelBean temp : data_level) {
                if (temp.getMemberLevelId() == userBean.getMemberLevel()) {
                    sdv_memberlevel_card.setImageURI(Uri.parse(temp.getUserIsMemberVipImageUrl()));
                    tv_member_level_name.setText(temp.getMemberLevelStr());
                    tv_nickname.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                    tv_member_level_name.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                    tv_member_id_tip.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                    tv_member_id.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                    tv_member_valid_time.setTextColor(android.graphics.Color.parseColor(temp.getFontColor()));
                    break;
                }
            }
        }*/
    }

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            startActivity(new Intent(UserMemberCenterActivity.this, HomeTabsActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
