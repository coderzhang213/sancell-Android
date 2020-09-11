package cn.sancell.xingqiu.homeuser;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.fragment.BaseNotDataFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.IncomeParInfo;
import cn.sancell.xingqiu.bean.LiveBaseStatusInfo;
import cn.sancell.xingqiu.bean.UserLevelInfo;
import cn.sancell.xingqiu.bean.VerifyResultRes;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.dialog.ComfirmRpDialog;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.activity.MyFightOderActivity;
import cn.sancell.xingqiu.homeuser.activity.MyGoodFriendListActivity;
import cn.sancell.xingqiu.homeuser.activity.VoucherActivity;
import cn.sancell.xingqiu.homeuser.bean.UserOrderNumBean;
import cn.sancell.xingqiu.homeuser.contract.HomeUserContract;
import cn.sancell.xingqiu.homeuser.income.UserincomeActivity;
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener;
import cn.sancell.xingqiu.live.activity.AnchorHomeActivity;
import cn.sancell.xingqiu.live.activity.LiveIdentifyActivity;
import cn.sancell.xingqiu.live.activity.LiveOtherInfoActivity;
import cn.sancell.xingqiu.live.activity.LiveSettingActivity;
import cn.sancell.xingqiu.live.nim.PublishParam;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.BackPressedUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.PriceUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.viewmodel.IncomeViewModel;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by ai11 on 2019/5/14.
 */

public class HomeUserFragment extends BaseNotDataFragment<HomeUserContract.GetUserInfoPresenter>
        implements HomeUserContract.HomeUserView, View.OnClickListener, OnBackPressedLinsener {
    /*@BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;*/

    @BindView(R.id.tv_user_title)
    TextView tv_user_title;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.iv_contact)
    ImageView iv_contact;
    @BindView(R.id.rl_edit_userinfo)
    RelativeLayout rl_edit_userinfo;
    @BindView(R.id.sdv_user_photo)
    SimpleDraweeView sdv_user_photo;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.iv_member_mark)
    ImageView iv_member_mark;
    @BindView(R.id.iv_no_member_mark)
    TextView iv_no_member_mark;
    @BindView(R.id.tv_member_id)
    TextView tv_member_id;
    @BindView(R.id.tv_copy)
    TextView tv_copy;
    @BindView(R.id.tv_red_voucher)
    AppCompatTextView tv_red_voucher;

    @BindView(R.id.ll_red_packet)
    LinearLayout ll_red_packet;
    @BindView(R.id.tv_red_packet)
    RelativeSizeTextView tv_red_packet;
    @BindView(R.id.ll_red_packet_settled)
    LinearLayout ll_red_packet_settled;
    @BindView(R.id.tv_red_packet_settled)
    RelativeSizeTextView tv_red_packet_settled;
    @BindView(R.id.ll_red_packet_voucher)
    LinearLayout ll_red_packet_voucher;

    @BindView(R.id.iv_member_open)
    View iv_member_open;

    @BindView(R.id.tv_allorder)
    TextView tv_allorder;
    @BindView(R.id.rl_order_nopay)
    RelativeLayout rl_order_nopay;
    @BindView(R.id.tv_nopay_num)
    TextView tv_nopay_num;
    @BindView(R.id.rl_order_undelivered)
    RelativeLayout rl_order_undelivered;
    @BindView(R.id.tv_undelivered_num)
    TextView tv_undelivered_num;
    @BindView(R.id.rl_order_delivered)
    RelativeLayout rl_order_delivered;
    @BindView(R.id.tv_delivered_num)
    TextView tv_delivered_num;
    @BindView(R.id.rl_order_evaluated)
    RelativeLayout rl_order_evaluated;
    @BindView(R.id.tv_evaluated_num)
    TextView tv_evaluated_num;
    @BindView(R.id.rl_my_friends)
    RelativeLayout rl_my_friends;
    @BindView(R.id.rl_member_gift)
    RelativeLayout rl_member_gift;
    @BindView(R.id.rl_address)
    RelativeLayout rl_address;
    @BindView(R.id.rl_feed)
    RelativeLayout rl_feed;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.tv_active_title)
    TextView tv_active_title;
    @BindView(R.id.rl_invite_active)
    RelativeLayout rl_invite_active;
    @BindView(R.id.iv_invite_friend)
    ImageView iv_invite_friend;
    @BindView(R.id.rl_my_card)
    RelativeLayout rl_my_card;
    @BindView(R.id.rl_my_zp)
    RelativeLayout rl_my_zp;
    @BindView(R.id.iv_start_live)
    AppCompatImageView iv_start_live;
    @BindView(R.id.btn_back)
    AppCompatImageView btn_back;
    @BindView(R.id.tv_rp_voucher)
    RelativeSizeTextView tv_rp_voucher;
    @BindView(R.id.ll_rp_voucher)
    LinearLayout ll_rp_voucher;
    @BindView(R.id.ll_mod)
    View ll_mod;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.ll_vip_end_time)
    View ll_vip_end_time;
    @BindView(R.id.vip_end_time)
    TextView vip_end_time;
    UserBean userBean;
    private boolean mCurrPagerShow = true;
    private StartupDataBean.HospitalInfo hospitalData;
    private IncomeViewModel nIncomeViewModel;

    public static HomeUserFragment newInstance(boolean isActivity) {

        Bundle args = new Bundle();
        args.putBoolean(UiHelper.FRAGMENT_IS_ACTIVITY, isActivity);
        HomeUserFragment fragment = new HomeUserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homeuser_new;
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void initData() {
        boolean isActivity = getArguments().getBoolean(UiHelper.FRAGMENT_IS_ACTIVITY, true);
        if (isActivity) {
            btn_back.setVisibility(View.VISIBLE);
        } else {
            btn_back.setVisibility(View.GONE);
        }
        getHospitalInfo();
        if (nIncomeViewModel == null) {
            nIncomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);
        }


        nIncomeViewModel.useriNCOME("0").observe(this, new Observer<IncomeParInfo>() {
            @Override
            public void onChanged(IncomeParInfo incomeParInfo) {
                //设置累计收益
                tv_money.setText(PriceUtils.getInstance().getPrice(incomeParInfo.getSumIncome()));
            }
        });

    }

    @Override
    protected void initView() {
        BackPressedUtils.INSTANCE.bindOnBack(getActivity(), this);

//        int statusHeight = StatusBarUtil.getStatusBarHeight(getActivity());
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_user_title.getLayoutParams();
//            lp.topMargin = statusHeight;
//            tv_user_title.setLayoutParams(lp);
//            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) iv_contact.getLayoutParams();
//            lp1.topMargin = statusHeight;
//            iv_contact.setLayoutParams(lp1);
//            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) iv_message.getLayoutParams();
//            lp2.topMargin = statusHeight;
//            iv_message.setLayoutParams(lp2);
//
//        }

        iv_contact.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        rl_edit_userinfo.setOnClickListener(this);
        tv_allorder.setOnClickListener(this);
        rl_order_nopay.setOnClickListener(this);
        rl_order_undelivered.setOnClickListener(this);
        rl_order_delivered.setOnClickListener(this);
        rl_order_evaluated.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_feed.setOnClickListener(this);
        rl_my_friends.setOnClickListener(this);
        rl_member_gift.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        ll_red_packet.setOnClickListener(this);
        ll_red_packet_settled.setOnClickListener(this);
        iv_invite_friend.setOnClickListener(this);
        rl_invite_active.setOnClickListener(this);
        rl_my_card.setOnClickListener(this);
        rl_my_zp.setOnClickListener(this);
        iv_contact.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        rl_edit_userinfo.setOnClickListener(this);
        iv_member_open.setOnClickListener(this);
        tv_allorder.setOnClickListener(this);
        rl_order_nopay.setOnClickListener(this);
        rl_order_undelivered.setOnClickListener(this);
        rl_order_delivered.setOnClickListener(this);
        rl_order_evaluated.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_feed.setOnClickListener(this);
        rl_my_friends.setOnClickListener(this);
        rl_member_gift.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        ll_red_packet.setOnClickListener(this);
        ll_red_packet_settled.setOnClickListener(this);
        ll_red_packet_voucher.setOnClickListener(this);
        iv_invite_friend.setOnClickListener(this);
        rl_invite_active.setOnClickListener(this);
        iv_start_live.setOnClickListener(this);
        btn_back.setOnClickListener(v -> getActivity().finish());
        ll_rp_voucher.setOnClickListener(this);
        ll_mod.setOnClickListener(this);
    }

    private void getHospitalInfo() {
        hospitalData = PreferencesUtils.readObject(Constants.Key.HOSPITAL_INFO, StartupDataBean.HospitalInfo.class);
    }


    @Override
    protected void onReloadData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BackPressedUtils.INSTANCE.unBindOnBack(getActivity(), this);

    }

    @Override
    public boolean isLoadNotDat() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetUserInfo(getActivity());
        mPresenter.GetUserOrderNum(getActivity());
    }

    @Override
    protected HomeUserContract.GetUserInfoPresenter createPresenter() {
        return new HomeUserContract.GetUserInfoPresenter();
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    public Object initAnalyticsScreenName() {
        return null;
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void toast(String text) {

    }

    @Override
    public void getUserInfoSuccess() {

        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);

        if (userBean.getRealMemberLevel() > 1) {
            iv_no_member_mark.setVisibility(View.VISIBLE);
            iv_no_member_mark.setText(userBean.getRealMemberLevelStr());
            if (userBean.getVipEndTime() > 0) {
                ll_vip_end_time.setVisibility(View.VISIBLE);
                vip_end_time.setText(TimeUtil.getDateFromYYYYMMDD(userBean.getVipEndTime() * 1000) + " 到期");
            }
        } else {
            iv_no_member_mark.setVisibility(View.GONE);
            ll_vip_end_time.setVisibility(View.GONE);
        }

        tv_member_id.setText("ID:" + userBean.getUserId());
        tv_red_packet.setTagText(StringUtils.getAllPrice(userBean.getPointE2()));
        tv_red_packet_settled.setTagText(StringUtils.getAllPrice(userBean.getFreezePointE2()));
        //红包
        tv_rp_voucher.setTagText(StringUtils.getAllPrice(userBean.pointRollE2));
        //代金券
        tv_red_voucher.setText(String.valueOf(userBean.couponCount));
        tv_nickname.setText(userBean.getNickname());
        sdv_user_photo.setImageURI(Uri.parse(userBean.getGravatar()));
        if (userBean.getmActivityInviteData() != null && userBean.getmActivityInviteData().getIsShow() == 1) {
            //  iv_invite_friend.setVisibility(View.VISIBLE);
        } else {
            iv_invite_friend.setVisibility(View.GONE);
        }

        if (userBean != null && userBean.getMemberVipOrderParcelInfo() != null && !StringUtils.isTextEmpty(userBean.getMemberVipOrderParcelInfo().getOrderId())) {
            // rl_member_gift.setVisibility(View.VISIBLE);
        } else {
            rl_member_gift.setVisibility(View.GONE);
        }
        //mPresenter.GetMemberLevelInfo(getActivity());
    }


    @Override
    public void getOrderNumSuccess(UserOrderNumBean userOrderNumBean) {
        if (userOrderNumBean.getWaitPayOrderCount() > 0) {
            tv_nopay_num.setVisibility(View.VISIBLE);
            if (userOrderNumBean.getWaitPayOrderCount() > 99) {
                tv_nopay_num.setText("99");
            } else {
                tv_nopay_num.setText(userOrderNumBean.getWaitPayOrderCount() + "");
            }
        } else {
            tv_nopay_num.setVisibility(View.GONE);
        }
        if (userOrderNumBean.getWaitDeliveryCount() > 0) {
            tv_undelivered_num.setVisibility(View.VISIBLE);
            if (userOrderNumBean.getWaitDeliveryCount() > 99) {
                tv_undelivered_num.setText("99");
            } else {
                tv_undelivered_num.setText(userOrderNumBean.getWaitDeliveryCount() + "");
            }
        } else {
            tv_undelivered_num.setVisibility(View.GONE);
        }

        if (userOrderNumBean.finishCount > 0) {
            tv_delivered_num.setVisibility(View.VISIBLE);
            if (userOrderNumBean.finishCount > 99) {
                tv_delivered_num.setText("99");
            } else {
                tv_delivered_num.setText(userOrderNumBean.finishCount + "");
            }
        } else {
            tv_delivered_num.setVisibility(View.GONE);
        }
        if (userOrderNumBean.getWaitEvaluationCount() > 0) {
            tv_evaluated_num.setVisibility(View.VISIBLE);
            if (userOrderNumBean.getWaitEvaluationCount() > 99) {
                tv_evaluated_num.setText("99");
            } else {
                tv_evaluated_num.setText(userOrderNumBean.getWaitEvaluationCount() + "");
            }
        } else {
            tv_evaluated_num.setVisibility(View.GONE);
        }
    }

    @Override
    public void getVerifyResult(VerifyResultRes res) {
        if (res.status == 2) {
            //检查直播状态
            mPresenter.checkLiveStatus(getContext());
        } else {
            LiveIdentifyActivity.Companion.start(getContext());
        }
    }

    @Override
    public void getLiveCheck(LiveBaseStatusInfo res) {
        if (res.getHas().equals("1")) {
            //去直播
            PublishParam publishParam = new PublishParam();
            publishParam.pushUrl = res.getLiveInfo().getPushUrl();
            publishParam.definition = PublishParam.HD;
            AnchorHomeActivity.Companion.startLive(getContext(), res.getLiveInfo().getRoomId(), res.getLiveInfo().getBatchId(), publishParam, false);
        } else {
            //直播设置
            LiveSettingActivity.Companion.startIntent(getContext(), false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_contact:
                showCallPhone();
                break;
            case R.id.iv_member_open:
                LiveOtherInfoActivity.Companion.start(getContext(), AppUtils.getUserId());
                //  UrlInfoActivity.startVip(getContext(), UrlConstants.INSTANCE.getVIP_INFO() + PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""), "猩球会员");
                break;
            case R.id.rl_edit_userinfo:
                ActivityOptionsCompat optionsCompat1 = ActivityOptionsCompat.makeScaleUpAnimation(rl_edit_userinfo, 0, 0, 0, 0);
                startActivity(new Intent(getActivity(), UserInfoActivity.class), optionsCompat1.toBundle());
                break;
            case R.id.tv_allorder:
                ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeScaleUpAnimation(tv_allorder, 0, 0, 0, 0);
                startActivity(new Intent(getActivity(), UserOrderListActivity.class), optionsCompat2.toBundle());
                break;
            case R.id.rl_order_nopay:
                ActivityOptionsCompat optionsCompat3 = ActivityOptionsCompat.makeScaleUpAnimation(rl_order_nopay, 0, 0, 0, 0);
                Intent intent1 = new Intent(getActivity(), UserOrderListActivity.class);
                intent1.putExtra(Constants.Key.KEY_1, 1);
                startActivity(intent1, optionsCompat3.toBundle());
                break;
            case R.id.rl_order_undelivered:
                ActivityOptionsCompat optionsCompat4 = ActivityOptionsCompat.makeScaleUpAnimation(rl_order_undelivered, 0, 0, 0, 0);

                Intent intent2 = new Intent(getActivity(), UserOrderListActivity.class);
                intent2.putExtra(Constants.Key.KEY_1, 1);
                startActivity(intent2, optionsCompat4.toBundle());
                break;
            case R.id.rl_order_delivered:
                ActivityOptionsCompat optionsCompat5 = ActivityOptionsCompat.makeScaleUpAnimation(rl_order_delivered, 0, 0, 0, 0);

                Intent intent3 = new Intent(getActivity(), UserOrderListActivity.class);
                intent3.putExtra(Constants.Key.KEY_1, 2);
                startActivity(intent3, optionsCompat5.toBundle());
                break;
            case R.id.rl_order_evaluated:
                ActivityOptionsCompat optionsCompat6 = ActivityOptionsCompat.makeScaleUpAnimation(rl_order_evaluated, 0, 0, 0, 0);

                Intent intent4 = new Intent(getActivity(), EvaluatedProductListActivity.class);
                startActivity(intent4, optionsCompat6.toBundle());
                break;
            case R.id.rl_address:
                ActivityOptionsCompat optionsCompat7 = ActivityOptionsCompat.makeScaleUpAnimation(rl_address, 0, 0, 0, 0);

                startActivity(new Intent(getActivity(), UserAddressListActivity.class), optionsCompat7.toBundle());
                break;
            case R.id.rl_setting:
                ActivityOptionsCompat optionsCompat8 = ActivityOptionsCompat.makeScaleUpAnimation(rl_setting, 0, 0, 0, 0);
                // startActivity(UserInviteActivity.getIntent(getActivity()));
//                Intent intentUri = new Intent();
//                intentUri.setAction(Intent.ACTION_VIEW);
//                Uri uri = Uri.parse("ssxq://blog.csdn.net/setting?goodsId=102");
//                intentUri.setData(uri);
//                startActivity(intentUri);
                startActivity(new Intent(getActivity(), UserSettingActivity.class), optionsCompat8.toBundle());
                break;
            case R.id.rl_feed:
                ActivityOptionsCompat optionsCompat9 = ActivityOptionsCompat.makeScaleUpAnimation(rl_feed, 0, 0, 0, 0);

                startActivity(new Intent(getActivity(), UserFeedActivity.class), optionsCompat9.toBundle());
                break;
            case R.id.rl_member_gift:
                if (userBean != null) {
                    Intent intent = new Intent(getActivity(), OrderNewPackInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, userBean.getMemberVipOrderParcelInfo().getParcelId());
                    intent.putExtra(Constants.Key.KEY_2, userBean.getMemberVipOrderParcelInfo().getOrderId());
                    startActivity(intent);
                }
                break;
            case R.id.tv_copy:
                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_member_id.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast(R.string.copy_success);
                break;
            case R.id.ll_red_packet:  //我的红包
                if (userBean != null) {
                    Intent intent = new Intent(getActivity(), UserRedPacketListActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, userBean.getPointE2());
                    startActivity(intent);
                }
                break;
            case R.id.ll_red_packet_settled:  //待结算红包
                if (userBean != null) {
                    Intent intent11 = new Intent(getActivity(), UserRedPacketSettledListActivity.class);
                    intent11.putExtra(Constants.Key.KEY_1, userBean.getFreezePointE2());
                    startActivity(intent11);
                }
                break;
            case R.id.ll_red_packet_voucher:
                VoucherActivity.Companion.start(getContext(), 0);
                break;
            case R.id.rl_invite_active:  //活动邀请
                Intent intent = new Intent(getActivity(), UrlInfoActivity.class);
                intent.putExtra(Constants.Key.KEY_1, userBean.getmActivityData().getLink());
                intent.putExtra(Constants.Key.KEY_2, "");
                startActivity(intent);
                break;
            case R.id.iv_invite_friend:  //邀请好友
                startActivity(new Intent(getActivity(), InviteFriendActivity.class));
                //  startActivity(new Intent(getActivity(), VipCencerActivity.class));
                break;
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), UserMessagesActivity.class));
                break;
            case R.id.rl_my_friends:  //我的好友
                //startActivity(new Intent(getActivity(), UserFriendsActivity.class));
                startActivity(new Intent(getActivity(), MyGoodFriendListActivity.class));
                break;
            case R.id.rl_my_zp://我的直拼
                startActivity(new Intent(getActivity(), MyFightOderActivity.class));

                break;
            case R.id.rl_my_card:

                String testUrl = "http://testm1.sanshaoxingqiu.cn/sancell-shop-app-hybrid/shopCard/index.html?token=";
                String preUrl = "http://m3.sanshaoxingqiu.cn/sancell-shop-app-hybrid/shopCard/index.html?token=";
                String rel = "https://m.sanshaoxingqiu.cn/sancell-shop-app-hybrid/shopCard/index.html?token=";
                String token = RSAUtils.encryptByPublic("reqTime=" + StringUtils.getCurrentTime() + "&" + "skey=" + PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
                UrlInfoActivity.start(getContext(), rel + token, "购物卡激活", true, getResources().getColor(R.color.bg_card_top), false);
                break;
            case R.id.iv_start_live:
                showLoading(true);
                mPresenter.checkVerifyStatus(getContext());
                break;
            case R.id.ll_rp_voucher:
                ComfirmRpDialog dialog = new ComfirmRpDialog(getContext());
                if (hospitalData != null) {
                    if (userBean.pointRollE2 > 0) {
                        dialog.setTitleMsg("请前往" + hospitalData.title + "消费联系电话：" + hospitalData.phone);
                        dialog.setListener(new ComfirmRpDialog.OnPhoneCallListener() {
                            @Override
                            public void onCallListener() {
                                callPhone(hospitalData != null ? hospitalData.phone : "");
                            }
                        });
                        dialog.show();
                    } else {
                        SCApp.getInstance().showSystemCenterToast("您没有红包抵用券");
                    }
                } else {
                    SCApp.getInstance().showSystemCenterToast("机构信息获取失败");
                }
                break;
            case R.id.ll_mod://收益详情
                startActivity(new Intent(getContext(), UserincomeActivity.class));
                break;
            default:
                break;

        }
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            SCApp.getInstance().showSystemCenterToast("请您打开拨号权限");
            return;
        }
        startActivity(intent);
    }

    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(getActivity(), "", "");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurrPagerShow = !hidden;
    }

    @Override
    public boolean onBackPressedLinsener() {
        if (mCurrPagerShow) {
            getActivity().finish();
        }

        return mCurrPagerShow;
    }
}
