package cn.sancell.xingqiu.goods.fragment.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.AddressInfo;
import cn.sancell.xingqiu.bean.PurchaseInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.dialog.PinRuleDialog;
import cn.sancell.xingqiu.dialog.PurchaseSpecificationDialog;
import cn.sancell.xingqiu.dialog.SelectAddressDialog;
import cn.sancell.xingqiu.goods.GoodsDetailManager;
import cn.sancell.xingqiu.goods.entity.req.GoodsInfoReq;
import cn.sancell.xingqiu.goods.fragment.listener.OnGoodsInfoListener;
import cn.sancell.xingqiu.goods.widget.FansGainView;
import cn.sancell.xingqiu.goods.widget.VgGoodsInfo;
import cn.sancell.xingqiu.goods.widget.VgPinAct;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoPicsListAdapter;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homepage.adapter.BannerImageAdapter;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.interfaces.OnPinOpListener;
import cn.sancell.xingqiu.order.entity.PinIntentBean;
import cn.sancell.xingqiu.order.pin.PinOrderActivity;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.viewGroup.manager.GoodsAreaManger;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

/**
 * @author Alan_Xiong
 * @desc: 商品信息
 * @time 2019-12-26 11:13
 */
public class GoodsInfoFragment extends BaseLazyMVPFragment<GoodsInfoPresenter> implements GoodsInfoView, View.OnClickListener {

    Banner mBanner;
    AppCompatTextView tv_banner_indicator;

    VgGoodsInfo mVgGoodsInfo;

    VgPinAct mVgPinAct;

    RecyclerView rv_goods_img;

    SmartRefreshLayout common_fresh;
    NestedScrollView nest_scroll;

    //配送红包
    RelativeLayout rl_deliver;

    RelativeLayout rl_server;

    AppCompatTextView tv_server_desc;

    AppCompatTextView tv_deliver_desc;

    AppCompatTextView tv_cant_deliver;

    AppCompatImageView tv_hospital_phone;

    RelativeLayout rl_hospital;

    AppCompatTextView tv_hospital_name;
    AppCompatTextView tv_hospital_adr;

    FansGainView vg_gain;

    private String mGoodsId;
    private List<String> mBannerList;
    private OnGoodsInfoListener mListener;
    private ProductInfoDataBean mData;
    private int maxScroll = ScreenUtil.dip2px(120);
    /**
     * 地址选择弹框
     */
    private SelectAddressDialog mSelectAddressDialog;
    private List<AddressListDataBean.AddressItemBean> mAddressLists;

    private String mProvinceId; //配送区域id
    private String mAddressId; //地址id
    private ProductInfoPicsListAdapter mPicAdapter;
    private String mRoomId; //直播房间
    private StartupDataBean.HospitalInfo hospitalData;

    private List<PurchaseInfo> mServerList = new ArrayList<>();


    public static GoodsInfoFragment newInstance(String goodsId, String roomId) {

        Bundle args = new Bundle();
        args.putString(IntentKey.KEY_GOODID, goodsId);
        args.putString(IntentKey.ROOM_ID, roomId);
        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnGoodsInfoListener(OnGoodsInfoListener listener) {
        mListener = listener;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_goods_info, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        mBanner = view.findViewById(R.id.banner);
        tv_banner_indicator = view.findViewById(R.id.tv_banner_indicator);

        mVgGoodsInfo = view.findViewById(R.id.bg_goods_info);
        mVgPinAct = view.findViewById(R.id.vg_pin);
        rv_goods_img = view.findViewById(R.id.rv_goods_img);
        tv_banner_indicator = view.findViewById(R.id.tv_banner_indicator);
        common_fresh = view.findViewById(R.id.common_fresh);
        nest_scroll = view.findViewById(R.id.nest_scroll);
        rl_deliver = view.findViewById(R.id.rl_deliver);
        rl_server = view.findViewById(R.id.rl_server);
        tv_server_desc = view.findViewById(R.id.tv_server_desc);
        tv_cant_deliver = view.findViewById(R.id.tv_cant_deliver);
        tv_hospital_phone = view.findViewById(R.id.tv_hospital_phone);
        tv_banner_indicator = view.findViewById(R.id.tv_banner_indicator);
        rl_hospital = view.findViewById(R.id.rl_hospital);
        tv_hospital_name = view.findViewById(R.id.tv_hospital_name);
        tv_hospital_adr = view.findViewById(R.id.tv_hospital_adr);
        vg_gain = view.findViewById(R.id.vg_gain);

    }

    //获取商品信息
    public void getGoodsInfo() {
        GoodsInfoReq req = new GoodsInfoReq();
        req.objId = mGoodsId;
        req.liveBatchId = mRoomId == null ? "" : mRoomId;
        mPresenter.getGoodsDetail(req);
    }

    @Override
    protected void initData() {
        mGoodsId = getArguments().getString(IntentKey.KEY_GOODID);
        mRoomId = getArguments().getString(IntentKey.ROOM_ID);
        rv_goods_img.setLayoutManager(new LinearLayoutManager(getContext()));
        getGoodsInfo();
        getHospitalInfo();
        initFresh();
        initScroll();
        initListener();
    }

    private void initListener() {
        rl_deliver.setOnClickListener(this);
        rl_server.setOnClickListener(this);
        tv_hospital_phone.setOnClickListener(this);
        rl_hospital.setOnClickListener(this);
    }

    private void getHospitalInfo() {
        hospitalData = PreferencesUtils.readObject(Constants.Key.HOSPITAL_INFO, StartupDataBean.HospitalInfo.class);
        if (hospitalData != null) {
            tv_hospital_adr.setText(hospitalData.address);
            tv_hospital_name.setText(hospitalData.title);
        }
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    protected GoodsInfoPresenter createPresenter() {
        return new GoodsInfoPresenter(getContext());
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    private void initFresh() {
        common_fresh.setEnableLoadMore(false);
        common_fresh.setRefreshHeader(new SancellClassicsHeader(getContext()));
        common_fresh.setOnRefreshListener(refreshLayout -> {
            getGoodsInfo();
            common_fresh.finishRefresh();
        });
    }

    private void initScroll() {

        nest_scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (maxScroll >= scrollY) {

                float scale = (float) scrollY / (float) maxScroll;
                //    Log.e("onScrollChange","scale="+scale+",maxScroll="+maxScroll+",scrolly="+scrollY);
                if (mListener != null) {
                    mListener.onScrollChange(scale);
                }
            } else {
                if (mListener != null) {
                    mListener.onScrollChange(1);
                }

            }
        });

    }

    @Override
    public void getGoodsInfoSuccess(ProductInfoDataBean res) {
        mData = res;
        feedBackData();
        initBanner();
        mVgGoodsInfo.setGoodsInfo(mData);
        //拼团
//        if (res.hasGroupon == 1) {
//            mVgPinAct.setVisibility(View.VISIBLE);
//            mVgPinAct.setVisibility(View.VISIBLE);
//            mVgPinAct.setData(res.grouponInfo, new OnPinOpListener() {
//                @Override
//                public void onJoin(ProductInfoDataBean.PinGroupInfo.GroupRecommend item) {
//                    PinIntentBean bean = new PinIntentBean();
//                    bean.pinId = item.grouponOrderId;
//                    bean.buyNum = mData.getMinBuyNum();
//                    bean.total_price = mData.grouponInfo.grouponPriceE2;
//                    bean.supportArea = mData.getGoodsRegion().getProvinceIdList();
//                    bean.goodsId = mData.getId();
//                    bean.good_pic = mData.getCoverPic();
//                    bean.good_title = mData.getTitle();
//                    bean.good_spec = mData.getSpecification();
//                    bean.gruopId = item.grouponOrderId;
//                    bean.mMaxRpCanUse = mData.astrictPointE2;
//                    bean.liveBatchId = mRoomId;
//                    PinOrderActivity.start(getContext(), bean);
//                }
//
//                @Override
//                public void onPinEnded() {
////                        getGoodsInfo();
//                }
//            });
//            mVgPinAct.setRuleListener(() -> {
//                PinRuleDialog dialog = new PinRuleDialog(getActivity());
//                dialog.setDatas(res.grouponInfo.grouponFinishHour);
//                dialog.show();
//            });
//
//
//        } else {
//            mVgPinAct.setVisibility(View.GONE);
//        }

        if (res.getGoodsFlag() == 6){
            vg_gain.setVisibility(View.VISIBLE);
            vg_gain.setData(res);
        }else{
            vg_gain.setVisibility(View.GONE);
        }

        //   mPresenter.getAddressList(getContext());
        if (mPicAdapter == null) {
            mPicAdapter = new ProductInfoPicsListAdapter(res.getDetailPicArr(), getContext());
            rv_goods_img.setAdapter(mPicAdapter);
        } else {
            mPicAdapter.setNewData(res.getDetailPicArr());
        }
        mServerList = GoodsDetailManager.getInstance().getServerList(res);
        tv_server_desc.setText(GoodsDetailManager.getInstance().getTitle(mServerList));
    }


    /**
     * 反馈给activity
     */
    private void feedBackData() {
        if (mListener != null) {
            if (mData.getGoodsDetailShare() != null && mData.getGoodsDetailShare().getIsShow() == 1) {
                mListener.showShareBtn(true);
            } else {
                mListener.showShareBtn(false);
            }
            mListener.getGoodsInfo(mData);
        }

    }

    private void initBanner() {
        mBannerList = new ArrayList<>(mData.getBannerPicArr());
        mBanner.setAdapter(new BannerImageAdapter(getContext(), mData.getBannerPicArr()))
                .setIndicator(new CircleIndicator(getContext()))
                .setBannerRound(8f)
                .addOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        tv_banner_indicator.setText(String.format(getContext().getResources().getString(R.string.banner_index), position + 1, mBannerList.size()));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                })
                .start();

        tv_banner_indicator.setText(String.format(getContext().getResources().getString(R.string.banner_index), 1, mBannerList.size()));
    }


    @Override
    public void getInfoError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);

    }

    @Override
    public void getAddressListSuccess(AddressListDataBean res) {
        mAddressLists = res.getDataList();

        if (res.getDataList() == null || res.getDataList().size() <= 0) {
            //无地址
            setDeliverStatus(0);
        } else {
            if (TextUtils.isEmpty(mAddressId)) {
                bindAddressView(res.getDataList().get(0));
            } else {
                bindAddressView(GoodsAreaManger.getInstance().getGoodsAreaItemById(res, mAddressId));
            }
        }

    }

    private void bindAddressView(AddressListDataBean.AddressItemBean item) {
        mProvinceId = item.getProvinceId() + "";
        tv_deliver_desc.setText(item.getCodeString());
        if (mListener != null) {
            mListener.updateAddress(item);
        }
        //检查是否在配送区域
        if (GoodsAreaManger.getInstance().getGoodsInArea(mData.getGoodsRegion(), mProvinceId)) {
            setDeliverStatus(2);
        } else {
            setDeliverStatus(1);
        }

    }

    @Override
    public void getAddressListError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_purchase_rp:
//                DialogUtil.showRedPacket(getActivity());
//                break;
            case R.id.rl_deliver:
                if (mSelectAddressDialog == null) {
                    mSelectAddressDialog = new SelectAddressDialog(getActivity(), null);
                    mSelectAddressDialog.setOnAddressSelectLinsener(new SelectAddressDialog.OnAddressSelectLinsener() {
                        @Override
                        public void onAddressSelectListener(AddressListDataBean.AddressItemBean mAddressItemBean) {
                            bindAddressView(mAddressItemBean);
                        }

                        @Override
                        public void addAddressSuccess(AddressInfo info) {
                            mAddressId = info.addressId + "";
                            mPresenter.getAddressList(getContext());
                        }
                    });
                }
                mSelectAddressDialog.setAdapter(mAddressLists);
                mSelectAddressDialog.show();
                break;
            case R.id.rl_server:
                //服务弹出窗口
                PurchaseSpecificationDialog mPurchaseSpecificationDialog = new PurchaseSpecificationDialog(getActivity());
                mPurchaseSpecificationDialog.setDataLists(mServerList);
                mPurchaseSpecificationDialog.show();

                break;
            case R.id.tv_hospital_phone:
                if (mListener != null) {
                    mListener.callHospital(hospitalData != null ? hospitalData.phone : "");
                }
                break;
            case R.id.rl_hospital:
                UrlInfoActivity.start(getContext(), hospitalData != null ? hospitalData.link : "", "机构介绍");

                break;
            default:
                break;
        }
    }

    private void setDeliverStatus(int status) {
        if (status == 0) {
            //无地址
            tv_deliver_desc.setVisibility(View.GONE);
            tv_cant_deliver.setVisibility(View.GONE);
            tv_cant_deliver.setText("暂无收获地址");
            if (mListener != null) {
                mListener.showCartTip("暂无收货地址");
            }

        } else if (status == 1) {
            //超区
            tv_deliver_desc.setVisibility(View.VISIBLE);
            tv_cant_deliver.setVisibility(View.VISIBLE);
            tv_cant_deliver.setText("当前地址无法配送");
            if (mListener != null) {
                mListener.showCartTip("抱歉，当前地址不支持配送该商品");
            }

        } else {
            //可配送
            tv_deliver_desc.setVisibility(View.VISIBLE);
            tv_cant_deliver.setVisibility(View.GONE);
            if (mListener != null) {
                mListener.showCartTip("");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBanner != null) {
            mBanner.stop();
        }
    }

    @Override
    public void onDestroyView() {

        if (mVgPinAct != null) {
            mVgPinAct.cancelTimer();
        }
        super.onDestroyView();
    }
}
