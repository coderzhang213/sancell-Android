package cn.sancell.xingqiu.usermember;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.adapter.MemberVIPPrivilegeListAdapter;
import cn.sancell.xingqiu.usermember.adapter.MemberVipGiftCardListAdapter;
import cn.sancell.xingqiu.usermember.adapter.MemberVipGiftProductListAdapter;
import cn.sancell.xingqiu.usermember.adapter.MemberVipGiftSeckillProductListAdapter;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.usermember.bean.MemberVIPPrivilegeBean;
import cn.sancell.xingqiu.usermember.bean.MemberVipGiftBean;
import cn.sancell.xingqiu.usermember.bean.MemberVipGiftCardBean;
import cn.sancell.xingqiu.usermember.contract.MemberVIPGiftBuyContract;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.RecyclerViewPageChangeListenerHelper;
import cn.sancell.xingqiu.widget.SpaceItemDecorationBuyMember;

public class MemberVipGiftBuyActivity extends BaseMVPActivity<MemberVIPGiftBuyContract.MemberVIPGiftBuyPresenter>
        implements MemberVIPGiftBuyContract.MemberVIPGiftBuyView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    @BindView(R.id.rcv_card_list)
    RecyclerView rcv_card_list;
    MemberVipGiftCardListAdapter memberVipGiftCardListAdapter;
    private List<MemberVipGiftCardBean> data_cards = new ArrayList<>();

    @BindView(R.id.sdv_privilege)
    SimpleDraweeView sdv_privilege;

    /**
     * 超值购秒杀
     */
    @BindView(R.id.rl_seckill_gift)
    RelativeLayout rl_seckill_gift;
    @BindView(R.id.view_seckill_gift_more)
    View view_seckill_gift_more;
    @BindView(R.id.rcv_seckill_gift)
    RecyclerView rcv_seckill_gift;
    private List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data_gift_seckill = new ArrayList<>();
    MemberVipGiftSeckillProductListAdapter memberVipGiftSeckillProductListAdapter;


    /**
     * 会员礼包
     */
    @BindView(R.id.rl_gift)
    RelativeLayout rl_gift;
    @BindView(R.id.view_gift_more)
    View view_gift_more;
    @BindView(R.id.rcv_gift)
    RecyclerView rcv_gift;
    private List<LikeBean> data_gift = new ArrayList<>();
    MemberVipGiftProductListAdapter memberVipGiftProductListAdapter_normal;

    /**
     * 会员权益列表
     */
    @BindView(R.id.rcv_privilege)
    RecyclerView rcv_privilege;
    private List<MemberVIPPrivilegeBean.VipPrivilegeListBean.SinglePrivilegeBean> data_privilege = new ArrayList<>();
    private MemberVIPPrivilegeListAdapter memberVIPPrivilegeListAdapter;
    private int currentLevel = 2;


    private UserBean userBean;
    private int[] cardResourceId = new int[]{R.mipmap.member_level_card1, R.mipmap.member_level_card2, R.mipmap.member_level_card3};
    private int[] levelNameId = new int[]{R.mipmap.icon_member_center_name1, R.mipmap.icon_member_center_name2, R.mipmap.icon_member_center_name3};
    private int[] cardTextColorId = new int[]{R.color.color_text1, R.color.color_text1, R.color.colorWhite};
    private int[] cardStatusBgId=new int[]{R.drawable.round_color_text1_stroke1_12,R.drawable.round_color_text1_stroke1_12,R.drawable.round_color_white_stroke1_12};
    private int[] cardLevel = new int[]{2, 3, 4};


    @Override
    protected MemberVIPGiftBuyContract.MemberVIPGiftBuyPresenter createPresenter() {
        return new MemberVIPGiftBuyContract.MemberVIPGiftBuyPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_vip_gift_buy;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(MemberVipGiftBuyActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        btn_back.setOnClickListener(this);
        if(userBean.getRealMemberLevel()>1) {
            currentLevel = userBean.getRealMemberLevel();
        }
        for (int i = 0; i < cardResourceId.length; i++) {
            MemberVipGiftCardBean memberVipGiftCardBean = new MemberVipGiftCardBean();
            memberVipGiftCardBean.setCardLevel(cardLevel[i]);
            memberVipGiftCardBean.setMemberLevel(userBean.getRealMemberLevel());
            memberVipGiftCardBean.setPicResourceId(cardResourceId[i]);
            memberVipGiftCardBean.setMemberLevelStrResourceId(levelNameId[i]);
            memberVipGiftCardBean.setMemberLevelTextColorId(cardTextColorId[i]);
            memberVipGiftCardBean.setStatusBgResourceId(cardStatusBgId[i]);
            if (userBean.getRealMemberLevel() == 1) {
                memberVipGiftCardBean.setMemberLevelName("成为VIP");
            } else if (userBean.getRealMemberLevel() == 2) {
                memberVipGiftCardBean.setMemberLevelName("您已经是银猩VIP");
            } else if (userBean.getRealMemberLevel() == 3) {
                memberVipGiftCardBean.setMemberLevelName("您已经是金猩VIP");
            } else if (userBean.getRealMemberLevel() == 4) {
                memberVipGiftCardBean.setMemberLevelName("您已经是红猩VIP");
            }
            data_cards.add(memberVipGiftCardBean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_card_list.setLayoutManager(layoutManager);
        rcv_card_list.setHasFixedSize(true);
        rcv_card_list.setNestedScrollingEnabled(false);
        rcv_card_list.addItemDecoration(new SpaceItemDecorationBuyMember(ScreenUtils.dip2px(this, 6), ScreenUtils.dip2px(this, 30)));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcv_card_list);
        rcv_card_list.addOnScrollListener(new RecyclerViewPageChangeListenerHelper(snapHelper, new RecyclerViewPageChangeListenerHelper.OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                currentLevel = cardLevel[position];
                mPresenter.GetMemberLimitTimeGiftList(cardLevel[position] + "", MemberVipGiftBuyActivity.this);
                mPresenter.GetMemberPrivilegeList(cardLevel[position] + "", MemberVipGiftBuyActivity.this);
                if(memberVipGiftBean!=null&&memberVipGiftBean.getGiftData()!=null&&memberVipGiftBean.getGiftData().size()>0) {
                    data_gift.clear();
                    data_gift.addAll(memberVipGiftBean.getGiftData().get(position).getMemberVipData().getDataList());
                    memberVipGiftProductListAdapter_normal.notifyDataSetChanged();
                }
            }
        }));
        memberVipGiftCardListAdapter = new MemberVipGiftCardListAdapter(this, data_cards, userBean);
        rcv_card_list.setAdapter(memberVipGiftCardListAdapter);
        if(userBean.getRealMemberLevel()>1) {
            layoutManager.scrollToPositionWithOffset(currentLevel - 2,0);
            layoutManager.setStackFromEnd(true);
        }
        memberVipGiftCardListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.rl_item:
                    Intent intent = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftListActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, cardLevel[i] + "");
                    intent.putExtra(Constants.Key.KEY_2, false);
                    startActivity(intent);
                    break;
            }
        });

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_seckill_gift.setLayoutManager(layoutManager1);
        rcv_seckill_gift.setHasFixedSize(true);
        rcv_seckill_gift.setNestedScrollingEnabled(false);
        memberVipGiftSeckillProductListAdapter = new MemberVipGiftSeckillProductListAdapter(this, data_gift_seckill);
        memberVipGiftSeckillProductListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.rl_item:
                    Intent intent = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, data_gift_seckill.get(i).getGoodsInfo().getId() + "");
                    startActivity(intent);
                    break;
            }
        });
        rcv_seckill_gift.setAdapter(memberVipGiftSeckillProductListAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_gift.setLayoutManager(layoutManager2);
        rcv_gift.setHasFixedSize(true);
        rcv_gift.setNestedScrollingEnabled(false);
        memberVipGiftProductListAdapter_normal = new MemberVipGiftProductListAdapter(this, data_gift);
        memberVipGiftProductListAdapter_normal.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.rl_item:
                    Intent intent = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, data_gift.get(i).getId() + "");
                    startActivity(intent);
                    break;
            }
        });
        rcv_gift.setAdapter(memberVipGiftProductListAdapter_normal);

        rcv_privilege.setLayoutManager(new LinearLayoutManager(this));
        rcv_privilege.setHasFixedSize(true);
        rcv_privilege.setNestedScrollingEnabled(false);
        memberVIPPrivilegeListAdapter = new MemberVIPPrivilegeListAdapter(data_privilege);
        rcv_privilege.setAdapter(memberVIPPrivilegeListAdapter);

        view_seckill_gift_more.setOnClickListener(this);
        view_gift_more.setOnClickListener(this);
        sdv_privilege.setOnClickListener(this);

        mPresenter.GetVipGiftList(this);
        mPresenter.GetMemberLimitTimeGiftList(currentLevel + "", this);
        mPresenter.GetMemberPrivilegeList(currentLevel + "", this);

    }

    @Override
    protected BaseView getView() {
        return this;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.view_seckill_gift_more:
                Intent intent = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftListActivity.class);
                intent.putExtra(Constants.Key.KEY_1, currentLevel + "");
                intent.putExtra(Constants.Key.KEY_2, true);
                startActivity(intent);
                break;
            case R.id.view_gift_more:
                Intent intent1 = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftListActivity.class);
                intent1.putExtra(Constants.Key.KEY_1, currentLevel + "");
                intent1.putExtra(Constants.Key.KEY_2, false);
                startActivity(intent1);
                break;
            case R.id.sdv_privilege:
                Intent intent2 = new Intent(MemberVipGiftBuyActivity.this, MemberPrivilegeInfoActivity.class);
                switch (currentLevel) {
                    case 2:
                        intent2.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Silver_Privilege);
                        break;
                    case 3:
                        intent2.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Golden_Privilege);
                        break;
                    case 4:
                        intent2.putExtra(Constants.Key.KEY_1, Constants.Url.URL_Red_Privilege);
                        break;
                }
                startActivity(intent2);
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
        tv_refresh.setOnClickListener(view -> mPresenter.GetVipGiftList(MemberVipGiftBuyActivity.this));
    }

    MemberVipGiftBean memberVipGiftBean;

    @Override
    public void getVipGiftListSuccess(MemberVipGiftBean memberVipGiftBean) {
        this.memberVipGiftBean = memberVipGiftBean;
        if (memberVipGiftBean != null && memberVipGiftBean.getGiftData() != null && memberVipGiftBean.getGiftData().size() > 0) {
            data_gift.clear();
            data_gift.addAll(memberVipGiftBean.getGiftData().get(currentLevel-2).getMemberVipData().getDataList());
            memberVipGiftProductListAdapter_normal.notifyDataSetChanged();
        } else {
            data_gift.clear();
            memberVipGiftProductListAdapter_normal.notifyDataSetChanged();
        }
    }

    @Override
    public void getVipPrivilegeListSuccess(MemberVIPPrivilegeBean memberVIPPrivilegeBean) {
        sdv_privilege.setImageURI(Uri.parse(memberVIPPrivilegeBean.getVipInterestsIndex().getCoverPic()));
        sdv_privilege.setAspectRatio(memberVIPPrivilegeBean.getVipInterestsIndex().getPicWidth() / (float) memberVIPPrivilegeBean.getVipInterestsIndex().getPicHeight());
        data_privilege.clear();
        if (memberVIPPrivilegeBean.getVipPrivilegeListBean().getDataList() != null) {
            data_privilege.addAll(memberVIPPrivilegeBean.getVipPrivilegeListBean().getDataList());
        }
        memberVIPPrivilegeListAdapter.notifyDataSetChanged();

    }

    @Override
    public void getLimitTimeGiftListSuccess(List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data_limitList) {
        if (data_limitList != null && data_limitList.size() > 0) {
            rl_seckill_gift.setVisibility(View.VISIBLE);
            data_gift_seckill.clear();
            Log.i("token",data_limitList.toString());
            for (MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean memberLimitTimeGiftBean : data_limitList
                    ) {
                data_gift_seckill.add(memberLimitTimeGiftBean);
            }
            memberVipGiftSeckillProductListAdapter.notifyDataSetChanged();
        } else {
            rl_seckill_gift.setVisibility(View.GONE);
        }
    }

    /*MemberVipGiftCardListAdapter.ItemClick itemClick = new MemberVipGiftCardListAdapter.ItemClick() {
        @Override
        public void itemClick(String id) {
            Intent intent = new Intent(MemberVipGiftBuyActivity.this, MemberVipGiftInfoActivity.class);
            intent.putExtra(Constants.Key.KEY_1, id);
            startActivity(intent);
        }
    };*/

    public void showDetailDialog(int position) {
        View view = getLayoutInflater().inflate(R.layout.dialog_member_level_detailinfo,
                null);
        final Dialog dialog_message = new Dialog(MemberVipGiftBuyActivity.this, R.style.transparentFrameWindowStyle);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.dip2px(MemberVipGiftBuyActivity.this, 343),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);

        ImageView iv_close = view.findViewById(R.id.iv_close);
        SimpleDraweeView iv_detail = view.findViewById(R.id.iv_detailinfo);
        int w = ScreenUtils.dip2px(MemberVipGiftBuyActivity.this, 343) - ScreenUtils.dip2px(MemberVipGiftBuyActivity.this, 48);

        if (memberVipGiftBean.getGiftData().get(position).getRelatedVipRelatedData() != null && memberVipGiftBean.getGiftData().get(position).getRelatedVipRelatedData().getPicWidth() != 0) {
            int h = w * memberVipGiftBean.getGiftData().get(position).getRelatedVipRelatedData().getPicHeight() / memberVipGiftBean.getGiftData().get(position).getRelatedVipRelatedData().getPicWidth();
            iv_detail.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            iv_detail.setImageURI(Uri.parse(memberVipGiftBean.getGiftData().get(position).getRelatedVipRelatedData().getCoverPic()));
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_message.dismiss();
            }
        });
        dialog_message.show();
    }
}
