package cn.sancell.xingqiu.homeclassify;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.fragment.BaseNotDataFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.adapter.HomeClassifyFirstAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.HomeClassifySecondAdapter;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifyFirstDataBean;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifySecondDataBean;
import cn.sancell.xingqiu.homeclassify.contract.HomeClassifyContract;
import cn.sancell.xingqiu.homepage.SearchActivity;
import cn.sancell.xingqiu.live.activity.LiveSearchActivity;
import cn.sancell.xingqiu.live.activity.UserFocusActivity;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifyFragment extends BaseNotDataFragment<HomeClassifyContract.HomeClassifyPresenter>
        implements HomeClassifyContract.HomeClassifyView, OnRecyclerViewListener.OnItemClickListener {
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.tv_default_keyword)
    TextView tv_default_keyword;

    @BindView(R.id.rcv_first_classify)
    RecyclerView rcv_first_classify;
    private List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data_first_classify;
    private HomeClassifyFirstAdapter homeClassifyFirstAdapter;
    @BindView(R.id.rcv_second_classify)
    RecyclerView rcv_second_classify;
    private HomeClassifySecondAdapter homeClassifySecondAdapter;
    private List<RecyclerViewData> data_second_classify;

    View view;

    private int current_first_postion;
    //模块ID  这个分类有多处用，如果这个不为空，需要去判断选中哪个
    private String module = "";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homeclassify;
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void initData() {
        module = getActivity().getIntent().getStringExtra("module");
        mPresenter.GetClassify(getActivity());
    }

    @Override
    protected void initView() {
        int statusHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll_search.getLayoutParams();
            lp.topMargin = statusHeight;
            ll_search.setLayoutParams(lp);
        }
        initial();

    }

    @Override
    protected void onReloadData() {
        initData();
    }

    @Override
    public boolean isLoadNotDat() {
        return true;
    }

    public void initial() {
        tv_default_keyword.setText(PreferencesUtils.getString(Constants.Key.KEY_searchKeyWord, ""));
        rcv_first_classify.setLayoutManager(new LinearLayoutManager(getActivity()));
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return homeClassifySecondAdapter.getItemViewType(position) == BaseViewHolder.VIEW_TYPE_CHILD ? 1 : manager.getSpanCount();
            }
        });
        rcv_second_classify.setLayoutManager(manager);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.startIntent(getActivity(), module);
//                LiveSearchActivity.Companion.start(getContext());

            }
        });
    }

    @Override
    protected HomeClassifyContract.HomeClassifyPresenter createPresenter() {
        return new HomeClassifyContract.HomeClassifyPresenter();
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
    }

    @Override
    public void netWorkError() {
        showNewWorkError();
    }

    @Override
    public void getClassifySuccess(List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data_first_classify1) {
        goneNewWorkError();
        data_first_classify = data_first_classify1;
        //判断需要显示那一条
        setSelecTab(data_first_classify1);
        homeClassifyFirstAdapter = new HomeClassifyFirstAdapter(getActivity(), data_first_classify);
        rcv_first_classify.setAdapter(homeClassifyFirstAdapter);
        homeClassifyFirstAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < data_first_classify.size(); i++) {
                    if (position == i) {
                        data_first_classify.get(i).setSelect(true);
                    } else {
                        data_first_classify.get(i).setSelect(false);
                    }
                }
                homeClassifyFirstAdapter.notifyDataSetChanged();
                refreshSecondData(position);
            }
        });

    }

    /**
     * 判断当前显示哪一条
     */
    private void setSelecTab(List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data_first_classify1) {
        int mSelctIndex = 0;
        if (!TextUtils.isEmpty(module) && data_first_classify1 != null && data_first_classify1.size() > 0) {
            for (int i = 0; i < data_first_classify1.size(); i++) {
                if (module.equals(data_first_classify1.get(i).getModuleId())) {
                    mSelctIndex = i;
                    break;
                }
            }
        }
        data_first_classify.get(mSelctIndex).setSelect(true);
        refreshSecondData(mSelctIndex);
    }

    @Override
    public void getSecondThirdClassifySuccess(List<HomeClassifySecondDataBean.ClassifySecondBean> classifySecondBeans) {
        data_second_classify = null;
        data_second_classify = new ArrayList<>();
        homeClassifySecondAdapter = null;
        for (HomeClassifySecondDataBean.ClassifySecondBean second : classifySecondBeans
        ) {
            data_second_classify.add(new RecyclerViewData(second, second.getSonTypeInfo().getDataList(), true));
        }
        homeClassifySecondAdapter = new HomeClassifySecondAdapter(getActivity(), data_second_classify, data_first_classify.get(current_first_postion).getCoverPic());
        homeClassifySecondAdapter.setOnItemClickListener(this);
        rcv_second_classify.setAdapter(homeClassifySecondAdapter);
        homeClassifySecondAdapter.notifyDataSetChanged();
    }

    public void refreshSecondData(int postion) {
        current_first_postion = postion;
        mPresenter.GetSecondThirdClassify(data_first_classify.get(postion).getId() + "", getActivity());
    }


    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        /*view.findViewById(R.id.sdv_group_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        List<HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean> list = data_second_classify.get(groupPosition).getGroupItem().getChildDatas();
        Intent intent = new Intent(getActivity(), ClassifyThirdActivity.class);
        intent.putExtra(Constants.Key.KEY_1, ((HomeClassifySecondDataBean.ClassifySecondBean) data_second_classify.get(groupPosition).getGroupData()).getName());
        //把list强制类型转换成Serializable类型
        intent.putExtra(Constants.Key.KEY_2, (Serializable) list);
        intent.putExtra(Constants.Key.KEY_3, childPosition + 1);
        intent.putExtra(Constants.Key.KEY_4, data_first_classify.get(current_first_postion).getId());
        intent.putExtra(Constants.Key.KEY_5, ((HomeClassifySecondDataBean.ClassifySecondBean) data_second_classify.get(groupPosition).getGroupData()).getId());
        startActivity(intent);
    }
}
