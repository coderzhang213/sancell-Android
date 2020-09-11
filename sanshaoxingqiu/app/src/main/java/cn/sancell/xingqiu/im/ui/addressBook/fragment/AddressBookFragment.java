package cn.sancell.xingqiu.im.ui.addressBook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.api.NimUIKit;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyFragment;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.im.ImEmptyView;
import cn.sancell.xingqiu.im.entity.req.AddressReq;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.im.ui.addressBook.AddressBookAdapter;
import cn.sancell.xingqiu.im.ui.createTeam.CreateTeamActivity;
import cn.sancell.xingqiu.im.ui.findTeam.FindTeamActivity;

/**
 * @author Alan_Xiong
 * @desc: 通讯录fragment
 * @time 2019-11-14 10:57
 */
public class AddressBookFragment extends BaseLazyMVPFragment<AddressFgmPresenter> implements AddressFgmView,ImEmptyView.onBtnClickListener{

    @BindView(R.id.rv_address)
    RecyclerView rv_address;
    @BindView(R.id.im_empty)
    ImEmptyView im_empty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int team_type; //群组类型
    private int mPageSize = 10;
    private int mCurrentPage = 1;
    private int dataCount = 0;
    private AddressBookAdapter mAdapter;
    private List<AddressRes.TeamData> mDatas = new ArrayList<>();

    public static AddressBookFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(UiHelper.TEAM_TYPE, type);
        AddressBookFragment fragment = new AddressBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initVariables(@NonNull Bundle bundle) {
        super.initVariables(bundle);
        team_type = bundle.getInt(UiHelper.TEAM_TYPE);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_im_address_book, container, false);
    }

    @Override
    protected void initData() {
        rv_address.setLayoutManager(new LinearLayoutManager(getContext()));
        im_empty.setListener(this);
        getTeamReq();
        initFresh();
    }

    public void initFresh(){
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            initConfig();
            getTeamReq();
            refreshLayout.finishRefresh();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getTeamReq();
        });
    }


    /**
     * 还原分页
     */
    private void initConfig(){
        mCurrentPage = 1;
        dataCount = 0;
    }
    /**
     * 发起群组请求
     */
    private void getTeamReq(){
        AddressReq req = new AddressReq();
        req.page = mCurrentPage+"";
        req.pageSize = mPageSize+"";
        if (team_type == 0){
            req.type = "1";
        }else{
            req.type = "2";
        }
        mPresenter.getMyTeam(req);
    }


    private void showEmpty(boolean show){
        if (show){
            rv_address.setVisibility(View.GONE);
            im_empty.setVisibility(View.VISIBLE);
            im_empty.setEmptyDesc("暂无群组");
            im_empty.setBtnStr(team_type == UiHelper.TEAM_TYPE_CREATE ? getString(R.string.im_create_team_at_once):getString(R.string.im_find_new_team));

        }else{
            rv_address.setVisibility(View.VISIBLE);
            im_empty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public void onBtnClick() {
        //空按钮
        if (team_type == 0){
            CreateTeamActivity.start(getContext());
        }else{
            FindTeamActivity.start(getContext());
        }
    }

    @Override
    protected AddressFgmPresenter createPresenter() {
        return new AddressFgmPresenter(getContext());
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    public void getTeamSuccess(AddressRes res) {

        if (res != null){
            dataCount = res.dataCount;
            if (res.dataList != null && res.dataList.size() > 0){
                if (mCurrentPage != 1){
                    mDatas.addAll(res.dataList);
                    refreshLayout.finishLoadMore();
                }else{
                    mDatas.clear();
                    mDatas.addAll(res.dataList);
                }
                mCurrentPage++;
                showEmpty(false);
                setAdapter();
            }else{
                if (mCurrentPage == 1){
                    showEmpty(true);
                }else{
                    refreshLayout.finishLoadMore();
                    refreshLayout.setEnableLoadMore(false);
                }
            }
        }else{
            if (mCurrentPage == 1){
                showEmpty(true);
            }else{
                refreshLayout.finishLoadMore();
                refreshLayout.setEnableLoadMore(true);
            }
        }

    }

    public void setAdapter(){
        if (mAdapter == null){
            mAdapter = new AddressBookAdapter(mDatas);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                SessionHelper.rpTeamId = mDatas.get(position).tid;
                NimUIKit.startTeamSession(getActivity(), mDatas.get(position).tid);
            });
            rv_address.setAdapter(mAdapter);

        }else{
            mAdapter.setNewData(mDatas);
        }
        if (mAdapter.getData().size() < dataCount){
            refreshLayout.setEnableLoadMore(true);
        }else{
            refreshLayout.setEnableLoadMore(false);
        }
    }

    @Override
    public void getTeamError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }
}
