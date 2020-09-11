package cn.sancell.xingqiu.homeclassify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.bean.ScreeningInfoEntity;
import cn.sancell.xingqiu.homeclassify.contract.ClassifyThirdFragmentContract;
import cn.sancell.xingqiu.homepage.adapter.HomepageLikeListAdapter;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;
import cn.sancell.xingqiu.widget.SpaceItemDecoration;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ClassifyThirdFragment extends BaseLazyMVPFragment<ClassifyThirdFragmentContract.ClassifyThirdFragmentPresenter>
        implements ClassifyThirdFragmentContract.ClassifyThirdListView {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    HomepageLikeListAdapter homepageLikeListAdapter;
    private List<LikeBean> data_likeList = new ArrayList<>();

    private static final String ARG_INFO_ENTITY = "arg_info_entity";
    ScreeningInfoEntity screening_info;

    public ClassifyThirdFragment() {
    }

    public static ClassifyThirdFragment newInstance(ScreeningInfoEntity infoEntity) {
        ClassifyThirdFragment fragment = new ClassifyThirdFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_INFO_ENTITY, infoEntity);
        fragment.setArguments(args);
        if (infoEntity != null) {
            fragment.setTitle(infoEntity.getThird_classify_name());
        }
        return fragment;
    }

    @Override
    public void initVariables(@NonNull Bundle bundle) {
        screening_info = bundle.getParcelable(ARG_INFO_ENTITY);
    }


    public void refreshData(ScreeningInfoEntity infoEntity) {
        if (infoEntity != null) {
            screening_info = infoEntity;
            //如果被回收的Fragment会重新从Bundle里获取数据,所以也要更新一下
            Bundle args = getArguments();
            if (args != null) {
                args.putParcelable(ARG_INFO_ENTITY, screening_info);
            }
            if (isFragmentVisible()) {
                initData();
            } else {
                setForceLoad(true);
            }
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
                if (homepageLikeListAdapter != null) {
                    homepageLikeListAdapter.resetCurrentPage();
                }
                mPresenter.GetClassifyThirdList(1, screening_info, getActivity());
            }
        });
    }

    @Override
    public void getClassifyThirdList(HomePageLikeListDataBean.LikeListDataBean dataLike, int page) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_likeList.clear();
            if (dataLike != null && dataLike.getDataList() != null && dataLike.getDataList().size() > 0) {
                data_likeList.addAll(dataLike.getDataList());
            }
            homepageLikeListAdapter.notifyDataSetChanged();
            homepageLikeListAdapter.correctCurrentPage();
        } else {
            if (dataLike != null && dataLike.getDataList() != null && dataLike.getDataList().size() > 0) {
                data_likeList.addAll(dataLike.getDataList());
                homepageLikeListAdapter.notifyDataSetChanged();
                homepageLikeListAdapter.correctCurrentPage();
            } else {
                refreshLayout.setNoMoreData(true);
            }
        }
        if (data_likeList.size() == dataLike.getDataCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    protected ClassifyThirdFragmentContract.ClassifyThirdFragmentPresenter createPresenter() {
        return new ClassifyThirdFragmentContract.ClassifyThirdFragmentPresenter();
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_classify_third, null);
        ButterKnife.bind(this, view);
        rcv_list.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcv_list.addItemDecoration(new SpaceItemDecoration(2, ScreenUtils.dip2px(getActivity(), 2), ScreenUtils.dip2px(getActivity(), 12), ScreenUtils.dip2px(getActivity(), 5), ScreenUtils.dip2px(getActivity(), 12)));
        homepageLikeListAdapter = new HomepageLikeListAdapter(R.layout.item_homepage_like_list, data_likeList);
        homepageLikeListAdapter.openLoadAnimation();
        rcv_list.setAdapter(homepageLikeListAdapter);
        homepageLikeListAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            switch (view1.getId()) {
                case R.id.rl_item:
//                    Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
//                    intent.putExtra(Constants.Key.KEY_1, data_likeList.get(position).getId() + "");
//                    startActivity(intent);
                    GoodsDetailActivity.start(getContext(),data_likeList.get(position).getId());
                    break;
            }
        });
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(getActivity()));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (homepageLikeListAdapter != null) {
                homepageLikeListAdapter.resetCurrentPage();
            }
            mPresenter.GetClassifyThirdList(1, screening_info, getActivity());
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (homepageLikeListAdapter != null) {
                mPresenter.GetClassifyThirdList(homepageLikeListAdapter.getNextPage(), screening_info, getActivity());
            } else {
                mPresenter.GetClassifyThirdList(1, screening_info, getActivity());
            }
        });
        return view;
    }

    /**
     * 网络请求数据
     */
    @Override
    protected void initData() {
        if (homepageLikeListAdapter != null) {
            homepageLikeListAdapter.resetCurrentPage();
        }
        mPresenter.GetClassifyThirdList(1, screening_info, getActivity());
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

}
