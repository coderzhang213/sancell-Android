package cn.sancell.xingqiu.order.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.dialog.PinRuleDialog;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.goods.fragment.listener.UmShareListener;
import cn.sancell.xingqiu.homeuser.OrderNewPackInfoActivity;
import cn.sancell.xingqiu.homeuser.OrderPackInfoActivity;
import cn.sancell.xingqiu.interfaces.OnTimeBackListener;
import cn.sancell.xingqiu.order.PinImageOverlyView;
import cn.sancell.xingqiu.order.entity.req.PinDetailReq;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;
import cn.sancell.xingqiu.order.orderInfo.PinOrderPackInfoActivity;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.FontUtils;
import cn.sancell.xingqiu.util.PriceUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.CommonCountTimer;

/**
 * @author Alan_Xiong
 * @desc: 拼团详情页
 * @time 2020-01-02 20:18
 */
public class PinDetailActivity extends BaseMVPToobarActivity<PinDetailPresenter> implements PinDetailView, View.OnClickListener {

    @BindView(R.id.iv_goods)
    AppCompatImageView iv_goods;
    @BindView(R.id.tv_goods_name)
    AppCompatTextView tv_goods_name;
    @BindView(R.id.tv_pin_num)
    AppCompatTextView tv_pin_num;
    @BindView(R.id.tv_pin_price)
    AppCompatTextView tv_pin_price;
    @BindView(R.id.iv_pin_states)
    AppCompatImageView iv_pin_states;
    @BindView(R.id.rl_order_detail)
    RelativeLayout rl_order_detail;

    //状态进行中
    @BindView(R.id.rl_pin_ing)
    RelativeLayout rl_pin_ing;
    @BindView(R.id.tv_pin_end)
    AppCompatTextView tv_pin_end;
    @BindView(R.id.tv_pin_hour)
    AppCompatTextView tv_pin_hour;
    @BindView(R.id.tv_pin_minute)
    AppCompatTextView tv_pin_minute;
    @BindView(R.id.tv_pin_second)
    AppCompatTextView tv_pin_second;

    @BindView(R.id.tv_num_need)
    AppCompatTextView tv_num_need;

    @BindView(R.id.ll_pin_over)
    LinearLayout ll_pin_over;
    @BindView(R.id.tv_pin_states)
    AppCompatTextView tv_pin_states;
    @BindView(R.id.tv_pin_states_desc)
    AppCompatTextView tv_pin_states_desc;

    @BindView(R.id.vg_img)
    PinImageOverlyView vg_img;
    @BindView(R.id.btn_invite)
    AppCompatTextView btn_invite;

    //规则
    @BindView(R.id.rl_server)
    RelativeLayout rl_server;
    @BindView(R.id.tv_rule_desc)
    AppCompatTextView tv_rule_desc;

    //红包
    @BindView(R.id.rl_rp)
    RelativeLayout rl_rp;
    @BindView(R.id.tv_success_rp)
    AppCompatTextView tv_success_rp;

    @BindView(R.id.ll_pin_cancel)
    LinearLayout ll_pin_cancel;
    @BindView(R.id.ll_pin_info)
    LinearLayout ll_pin_info;

    public String mOrderId;
    private CommonCountTimer mTimer;
    PinDetailRes mDatas;

    public static void start(Context context, String orderId) {
        Intent intent = new Intent(context, PinDetailActivity.class);
        intent.putExtra(IntentKey.PIN_DETAIL_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected PinDetailPresenter createPresenter() {
        return new PinDetailPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activtity_pin_detail;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("直拼详情");
        mOrderId = getIntent().getStringExtra(IntentKey.PIN_DETAIL_ORDER_ID);

        getPinInfo();
        initListener();
    }

    //获取拼团详情信息
    public void getPinInfo() {
        PinDetailReq req = new PinDetailReq();
        req.grouponOrderId = mOrderId;
        mPresenter.getPinOrderInfo(req);
    }

    public void initListener() {
        rl_order_detail.setOnClickListener(this);
        btn_invite.setOnClickListener(this);
        rl_server.setOnClickListener(this);
        iv_goods.setOnClickListener(this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods:
                if (mDatas != null) {
                    GoodsDetailActivity.start(this, mDatas.goods.goodsId);
                }

                break;
            case R.id.rl_order_detail:

                if (mDatas.currentUserStatus == 2) {
                    //已完成
                    OrderNewPackInfoActivity.start(this, mDatas.parcelId, mDatas.buyerOrderId);
                } else {
                    PinOrderPackInfoActivity.start(this, mDatas.buyerOrderId);
                }

                break;
            case R.id.btn_invite:
                if (0 == (int) btn_invite.getTag()) {
                    //tag = 1 立即参加
                    SCApp.getInstance().showSystemCenterToast("未开发");

                } else if (1 == (int) btn_invite.getTag()) {
                    //分享
                    if (mDatas.grouponInviteData != null) {
                        DialogUtil.getShareDialog(this, new UMImage(this, mDatas.grouponInviteData.logoUrl)
                                , mDatas.grouponInviteData.link, mDatas.grouponInviteData.title, mDatas.grouponInviteData.desc, new UmShareListener());
                    }
                } else {
                    //继续参加拼团
                    GoodsDetailActivity.start(this, mDatas.goods.goodsId);
                }

                break;
            case R.id.rl_server:
                PinRuleDialog dialog = new PinRuleDialog(this);
                dialog.setDatas(mDatas.grouponFinishHour);
                dialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPInDetailInfoSuccess(PinDetailRes res) {
        mDatas = res;

        iv_pin_states.setVisibility(View.VISIBLE);
        ll_pin_info.setVisibility(View.VISIBLE);
        ll_pin_cancel.setVisibility(View.GONE);

        tv_pin_num.setText(String.format(getString(R.string.pin_number), res.grouponNum));
        //商品信息
        if (res.goods != null) {
            tv_goods_name.setText(res.goods.title);
            Glide.with(this).load(res.goods.coverPic).into(iv_goods);
            tv_pin_price.setText(PriceUtils.getInstance().getMainPrice(this, (int) res.goods.grouponPriceE2, 14));
        }

        if (res.currentUserStatus == 1) {

            iv_pin_states.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_on_the_way));
            //显示邀请好友
            btn_invite.setVisibility(View.VISIBLE);

            btn_invite.setEnabled(true);
            if (res.currentUserInGroupon == 1) {
                btn_invite.setText(R.string.invite_firend_join);
                btn_invite.setTag(1);
            } else {
                btn_invite.setTag(0);
                btn_invite.setText(R.string.pin_join_in);
            }
            //倒计时
            rl_pin_ing.setVisibility(View.VISIBLE);
            ll_pin_over.setVisibility(View.GONE);

            tv_num_need.setText(FontUtils.getInstance().changeTextColor(getResources().getColor(R.color.color_theme)
                    , String.format(getString(R.string.pin_need_num_join), res.lastUserNum), String.format(getString(R.string.user_num), res.lastUserNum)));
            long diff = res.finishTime - res.currentTime;
            mTimer = new CommonCountTimer(diff * 1000, 1000, new OnTimeBackListener() {
                @Override
                public void onTick(String hour, String minute, String second) {
                    tv_pin_hour.setText(hour);
                    tv_pin_minute.setText(minute);
                    tv_pin_second.setText(second);
                }

                @Override
                public void onFinish() {
                    //重新获取数据
                    mTimer.cancel();
                    getPinInfo();
                }
            });
            mTimer.start();
            rl_rp.setVisibility(View.GONE);

        } else if (res.currentUserStatus == 2) {
            //拼团成功
            iv_pin_states.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_success));

            rl_pin_ing.setVisibility(View.GONE);
            ll_pin_over.setVisibility(View.VISIBLE);
            if (res.currentUserInGroupon == 1) {
                btn_invite.setVisibility(View.GONE);
                tv_pin_states_desc.setVisibility(View.VISIBLE);
                tv_pin_states.setText(R.string.congratulation_pin_success);
                tv_pin_states_desc.setText(R.string.please_wait_delivery);

                //是否是团长，团长红包
                if (res.currentUserIsOwner.equals("1")) {
                    //是
                    rl_rp.setVisibility(View.VISIBLE);
                    String price = PriceUtils.getInstance().getPriceWithSyp(res.grouponOwnerBonus);
                    tv_success_rp.setText(FontUtils.getInstance().changeTextColor(getResources().getColor(R.color.color_theme)
                            , String.format(getResources().getString(R.string.pin_rp_to_leader), price), price));

                } else {
                    rl_rp.setVisibility(View.GONE);
                }
            }
        } else if (res.currentUserStatus == 3) {
            //拼团失败

            btn_invite.setVisibility(View.VISIBLE);
            rl_pin_ing.setVisibility(View.GONE);
            ll_pin_over.setVisibility(View.VISIBLE);
            rl_rp.setVisibility(View.GONE);
            iv_pin_states.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_failed));
            tv_pin_states.setText(getString(R.string.pin_failed));
            if (res.currentUserInGroupon == 1) {
                checkPayStates(res.payStatus);
            } else {
                tv_pin_states_desc.setVisibility(View.GONE);
            }

            btn_invite.setTag(3);

            if (res.isActivityGoods == 1) {

                if (res.stockStatus == 2) {
                    //无库存
                    btn_invite.setText(R.string.pin_goods_sold_out);
                    btn_invite.setEnabled(false);
                } else {
                    btn_invite.setText(R.string.join_recommend_team);
                    btn_invite.setEnabled(true);

                }

            } else {
                btn_invite.setText(R.string.pin_over);
                btn_invite.setEnabled(false);
            }

        } else {
            //取消状态
            iv_pin_states.setVisibility(View.GONE);
            ll_pin_info.setVisibility(View.GONE);
            ll_pin_cancel.setVisibility(View.VISIBLE);
            rl_rp.setVisibility(View.GONE);
        }

        //状态
        tv_rule_desc.setText(String.format(getString(R.string.pin_rule_desc), res.grouponNum, res.grouponFinishHour));
        vg_img.setData(res.grouponNum, res.grouponSuccessUser, res.grouponUsersInfo);
    }


    public void checkPayStates(int payStatus) {
        tv_pin_states_desc.setVisibility(View.VISIBLE);
        if (payStatus == 4) {
            //退款中
            tv_pin_states_desc.setText(R.string.pin_wait_money_back);
        } else if (payStatus == 5) {
            //已退款
            tv_pin_states_desc.setText(R.string.pin_money_already_back);
        }
    }

    @Override
    public void getError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
