package cn.sancell.xingqiu.homecommunity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.netease.nimlib.sdk.RequestCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.fragment.BaseNotDataFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.homecommunity.adapter.CommunityRecommGroupListAdapter;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean;
import cn.sancell.xingqiu.homecommunity.contract.HomeCommunityContract;
import cn.sancell.xingqiu.homecommunity.live.HomeLiveFragment;
import cn.sancell.xingqiu.homecommunity.video.fgm.HomeVideoFgm;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.im.activity.ChatGroupActivity;
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm;
import cn.sancell.xingqiu.im.entity.TabEntity;
import cn.sancell.xingqiu.im.entity.req.AddListReq;
import cn.sancell.xingqiu.im.ui.addressBook.AddressBookActivity;
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter;
import cn.sancell.xingqiu.im.ui.findTeam.FindTeamActivity;
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener;
import cn.sancell.xingqiu.im.ui.recent.RecentMessageActivity;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.util.BannerJumpUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

public class HomeCommunityFragment extends BaseNotDataFragment<HomeCommunityContract.HomeCommunityPresenter>
        implements HomeCommunityContract.HomeCommunityView, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_group_message)
    LinearLayout ll_group_message;
    @BindView(R.id.ll_create_group)
    LinearLayout ll_create_group;
    @BindView(R.id.ll_lookfor_group)
    LinearLayout ll_lookfor_group;
    @BindView(R.id.ll_address_book)
    LinearLayout ll_address_book;
    /**
     * 推荐群组
     */
    @BindView(R.id.rcv_recomm_group_list)
    RecyclerView rcv_recomm_group_list;
    private CommunityRecommGroupListAdapter communityRecommGroupListAdapter;
    private List<RecommendGroupListBean.RecommGroupBean> data_recomm_group_list = new ArrayList<>();
    @BindView(R.id.slide_indicator_point)
    SeekBar slide_indicator_point;
    @BindView(R.id.btn_recomm_more)
    View btn_recomm_more;
    @BindView(R.id.iv_top)
    ImageView iv_top;

    @BindView(R.id.vp_video)
    ViewPager vp_video;
    @BindView(R.id.tb_video)
    TabLayout tb_video;
    private List<Fragment> mFragments = new ArrayList<>();
    ;
    private List<CommunityVideoListBean.VideoBean> data_video_list = new ArrayList<>();
    private List<HomeBannerDataBean.BannerBean> mBannerData = new ArrayList<>();

    private NormalPageAdapter mNormalPageAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_community;
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        initial();
    }

    @Override
    protected void onReloadData() {
        mPresenter.GetRecommGroupListList(getActivity());
    }

    @Override
    public boolean isLoadNotDat() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetRecommGroupListList(getActivity());
    }

    public void initial() {
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(getActivity()));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.GetRecommGroupListList(getActivity());
            for (Fragment mFragment : mFragments) {
                if (mFragment instanceof OnRefLinsener) {
                    ((OnRefLinsener) mFragment).onRefLinsener();
                }

            }

        });
        refreshLayout.setEnableLoadMore(false);

        ll_group_message.setOnClickListener(this);
        ll_create_group.setOnClickListener(this);
        ll_lookfor_group.setOnClickListener(this);
        ll_address_book.setOnClickListener(this);
        btn_recomm_more.setOnClickListener(this);
        iv_top.setOnClickListener(this);
        //群组推荐
        rcv_recomm_group_list.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        communityRecommGroupListAdapter = new CommunityRecommGroupListAdapter(data_recomm_group_list);
        communityRecommGroupListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_apply_join:   //申请加入

                    ScClient.applyJoinWithVer(getContext(), data_recomm_group_list.get(position).getTid(), PreferencesUtils.getString(Constants.Key.key_im_accid, ""), new TeamApplyListener() {
                        @Override
                        public void onSuccess(Object o) {
                            data_recomm_group_list.get(position).setInGroup(1);
                            communityRecommGroupListAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailed(int i) {

                        }

                        @Override
                        public void onException(Throwable throwable) {

                        }

                        @Override
                        public void showInputDialog() {
                            ApplyJoinTeamInputDialogFgm dialogFgm = ApplyJoinTeamInputDialogFgm.newInstance();
                            dialogFgm.setOnApplyListener(str -> {
                                ScClient.applyJoinTeam(getContext(), data_recomm_group_list.get(position).getTid(), null
                                        , PreferencesUtils.getString(Constants.Key.key_im_accid, ""), new RequestCallback() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                data_recomm_group_list.get(position).setInGroup(1);
                                                communityRecommGroupListAdapter.notifyItemChanged(position);
                                            }

                                            @Override
                                            public void onFailed(int i) {

                                            }

                                            @Override
                                            public void onException(Throwable throwable) {

                                            }
                                        });
                            });
                            dialogFgm.show(getFragmentManager(), "apply");
                        }
                    });

                    break;
                case R.id.iv_join_group_chat:  //参与群聊
                    ScClient.enterTeamChat(getActivity(), data_recomm_group_list.get(position).getTid());
                    break;

            }
        });
        rcv_recomm_group_list.setAdapter(communityRecommGroupListAdapter);
        slide_indicator_point.setPadding(0, 0, 0, 0);
        slide_indicator_point.setThumbOffset(0);

        rcv_recomm_group_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("onScrolled", "dx = " + dx + "dy = " + dy);
                //显示区域的高度。
                int extent = rcv_recomm_group_list.computeHorizontalScrollExtent();
                //整体的高度，注意是整体，包括在显示区域之外的。
                int range = rcv_recomm_group_list.computeHorizontalScrollRange();
                //已经向下滚动的距离，为0时表示已处于顶部。
                int offset = rcv_recomm_group_list.computeHorizontalScrollOffset();
                /*//此处获取seekbar的getThumb，就是可以滑动的小的滚动游标
                GradientDrawable gradientDrawable =(GradientDrawable) seekBar.getThumb();
                //根据列表的个数，动态设置游标的大小，设置游标的时候，progress进度的颜色设置为和seekbar的颜色设置的一样的，所以就不显示进度了。
                gradientDrawable.setSize(extent/(strings.size()/2),5);*/
                slide_indicator_point.setMax(range - extent);
                if (dx == 0) {
                    slide_indicator_point.setProgress(0);
                } else {
                    slide_indicator_point.setProgress(offset);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

        });

        // checkLiveList();
        AddListReq req = new AddListReq();
        req.bannerAdType = "2";
        mPresenter.getAdList(getContext(), req);
    }

    /**
     * 先调用直播列表的接口，如果有数据显示，没有就不展示
     */
//    private void checkLiveList() {
//        LiveListReq req = new LiveListReq();
//        req.page = 1 + "";
//        req.pageSize = 1 + "";
//        HashMap<String, String> map = ConvertUtils.convertToMapPartRsa(req);
//        RetrofitUtil.getInstance().initRetrofit().getLiveList(map).
//                subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<LiveData>(getActivity()) {
//
//            @Override
//            protected void onSuccess(BaseEntry<LiveData> t) throws Exception {
//                if (t == null || t.getRetData() == null || t.getRetData().getDataList() == null || t.getRetData().getDataList().size() <= 0) {
//                    initTabLayout(false);
//                } else {
//                    initTabLayout(true);
//                }
//            }
//
//            @Override
//            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
//                initTabLayout(false);
//            }
//        });
//
//    }

    /**
     * 用来判断直播列表是否为空
     *
     * @param isAdd
     */
    public void initTabLayout(boolean isAdd) {
        List<TabEntity> tabs = new ArrayList<>();
        mFragments.clear();
        if (mNormalPageAdapter != null) {//如果是重新刷新，先清除原有fragment
            List<Fragment> fragments = mNormalPageAdapter.getmFragments();
            if (fragments != null && fragments.size() > 0) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                for (Fragment mFragment : fragments) {
                    fragmentTransaction.remove(mFragment);
                }
            }
        }
        if (isAdd) {
            tabs.add(new TabEntity(getResources().getString(R.string.home_live), UiHelper.TAB_HOME_LIVE));
        }

        tabs.add(new TabEntity(getResources().getString(R.string.home_video), UiHelper.TAB_HOME_VIDEO));

        if (isAdd) {
            tb_video.addTab(tb_video.newTab());
            mFragments.add(HomeLiveFragment.newInstance());
        }
        tb_video.addTab(tb_video.newTab());
        mFragments.add(HomeVideoFgm.newInstance());
        mNormalPageAdapter = new NormalPageAdapter(getChildFragmentManager(), mFragments, tabs);
        vp_video.setAdapter(mNormalPageAdapter);
        vp_video.setOffscreenPageLimit(2);
        vp_video.setCurrentItem(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_group_message:     //群组消息跳转
                RecentMessageActivity.start(getContext());
                break;
            case R.id.ll_create_group:      //创建群组跳转
                startActivity(new Intent(getContext(), ChatGroupActivity.class));
                //CreateTeamActivity.start(getContext());
                break;
            case R.id.ll_lookfor_group:     //查找群组跳转
                FindTeamActivity.start(getContext());
                break;
            case R.id.ll_address_book:      //通讯录跳转
                AddressBookActivity.start(getContext());
                break;
            case R.id.btn_recomm_more:      //群组推荐更多点击跳转
                startActivity(new Intent(getActivity(), RecommendGroupListActivity.class));
                break;
            case R.id.iv_top:
                BannerJumpUtils.bannerJump(getContext(), mBannerData, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected HomeCommunityContract.HomeCommunityPresenter createPresenter() {
        return new HomeCommunityContract.HomeCommunityPresenter();
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
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
        refreshLayout.finishRefresh();
    }

    @Override
    public void netWorkError() {
        showNewWorkError();
        refreshLayout.finishRefresh();
    }

    @Override
    public void getRecommGroupListSuccess(List<RecommendGroupListBean.RecommGroupBean> recommGroupBeanList) {
        goneNewWorkError();
        refreshLayout.finishRefresh();
        data_recomm_group_list.clear();
        data_recomm_group_list.addAll(recommGroupBeanList);
        communityRecommGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecommGroupGetLiveListData(int sum) {
        if (sum > 0) {//直播列表有数据
            if (mFragments.size() == 0 || mFragments.size() == 1) {//刷新的时候本来就会去刷新两个tab的数据，这里做一点优化
                initTabLayout(true);
            }

        } else {
            if (mFragments.size() == 0 || mFragments.size() == 2) {//刷新的时候本来就会去刷新两个tab的数据，这里做一点优化
                initTabLayout(false);
            }
        }
    }


    @Override
    public void getVideoListSuccess(CommunityVideoListBean videoBeanList, int page) {
        refreshLayout.finishLoadMore();

        if (page == 1) {
            goneNewWorkError();
            data_video_list.clear();
            data_video_list.addAll(videoBeanList.getDataList());
//            communityVideoListAdapter.notifyDataSetChanged();
//            communityVideoListAdapter.correctCurrentPage();
        } else {
            data_video_list.addAll(videoBeanList.getDataList());
//            communityVideoListAdapter.notifyDataSetChanged();
//            communityVideoListAdapter.correctCurrentPage();
        }
        if (data_video_list.size() == videoBeanList.getDataCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void getAdList(HomeBannerDataBean data) {
        if (data != null && data.getDataList() != null && data.getDataList().size() > 0) {
            mBannerData = data.getDataList();
            Glide.with(this).load(data.getDataList().get(0).getCoverPic()).into(iv_top);
        }
    }


    public interface OnRefLinsener {
        void onRefLinsener();
    }
}
