package cn.sancell.xingqiu.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.KeyBean;
import cn.sancell.xingqiu.homepage.bean.SearchKeyListBean;
import cn.sancell.xingqiu.homepage.contract.SearchContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.LabelsView;

/**
 * 搜索界面（历史搜索和热门搜索）
 */
public class SearchActivity extends BaseMVPActivity<SearchContract.SearchPresenter>
        implements SearchContract.SearchView, View.OnClickListener {

    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.ed_keyword)
    EditText ed_keyword;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.iv_delete_keyword)
    ImageView iv_delete_keyword;

    @BindView(R.id.rl_histroy)
    RelativeLayout rl_histroy;
    @BindView(R.id.iv_delete_histroy)
    ImageView iv_delete_histroy;
    @BindView(R.id.labels_histroy)
    LabelsView labels_histroy;
    @BindView(R.id.tv_more_histroy)
    TextView tv_more_histroy;
    private List<KeyBean> data_histroy = new ArrayList<>();

    @BindView(R.id.ll_hot)
    LinearLayout ll_hot;
    @BindView(R.id.labels_hot)
    LabelsView labels_hot;
    private List<KeyBean> data_hot = new ArrayList<>();

    private String keyWord;
    //默认是1
    private String moudleId = "1";

    @Override
    protected SearchContract.SearchPresenter createPresenter() {
        return new SearchContract.SearchPresenter();
    }

    public static void startIntent(Context context, String moudleId) {
        Intent intent = new Intent(context, SearchActivity.class);
        if (!TextUtils.isEmpty(moudleId)) {
            intent.putExtra("moudleId", moudleId);
        } else {
            intent.putExtra("moudleId", "1");

        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        moudleId = getIntent().getStringExtra("moudleId");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_top.getLayoutParams();
            lp.topMargin = statusHeight;
            rl_top.setLayoutParams(lp);
        }
        btn_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_delete_histroy.setOnClickListener(this);

        ed_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sourceStr = editable.toString();
                if (sourceStr.length() > 0) {
                    keyWord = sourceStr;
                    iv_delete_keyword.setVisibility(View.VISIBLE);
                    iv_delete_keyword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ed_keyword.setText("");
                        }
                    });

                } else {
                    keyWord = PreferencesUtils.getString(Constants.Key.KEY_searchKeyWord, "");
                    iv_delete_keyword.setVisibility(View.GONE);
                }
            }
        });
        ed_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    ScreenUtils.hideKeyboard(ed_keyword);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    SearchProductListActivity.startIntent(SearchActivity.this, keyWord, moudleId);
                    return true;
                }

                return false;
            }
        });
        //标签的点击监听
        labels_histroy.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                if (data instanceof KeyBean){
                    SearchProductListActivity.startIntent(SearchActivity.this, ((KeyBean) data).getName(), moudleId);
                }

            }
        });
        tv_more_histroy.setOnClickListener(this);
        //标签的点击监听
        labels_hot.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                if (data instanceof KeyBean){
                    SearchProductListActivity.startIntent(SearchActivity.this, ((KeyBean) data).getName(), moudleId);
                }

            }
        });
        mPresenter.GetHotSearchList(this, moudleId);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetHistoryList(this, moudleId);
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
            case R.id.tv_search:
                ScreenUtils.hideKeyboard(ed_keyword);
                SearchProductListActivity.startIntent(SearchActivity.this, keyWord, moudleId);

                break;
            case R.id.iv_delete_histroy:
                DialogUtil.showOperateDialog(SearchActivity.this, "是否要删除历史搜索记录", "", "否", "是", new DialogUtil.ClickSureAction() {
                    @Override
                    public void sureAction(int postion) {
                        mPresenter.DeleteHistory(SearchActivity.this, moudleId);
                    }
                });

                break;
            case R.id.tv_more_histroy:
                labels_histroy.setMaxLines(0);
                tv_more_histroy.setVisibility(View.GONE);
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
                mPresenter.GetHistoryList(SearchActivity.this, moudleId);
            }
        });
    }

    @Override
    public void deleteHistroySuccess() {
        mPresenter.GetHistoryList(SearchActivity.this, moudleId);
    }

    @Override
    public void getHotSearchSuccess(List<KeyBean> dataHot) {
        if (dataHot != null && dataHot.size() > 0) {
            data_hot.clear();
            data_hot.addAll(dataHot);
            ll_hot.setVisibility(View.VISIBLE);
            labels_hot.setLabels(dataHot, new LabelsView.LabelTextProvider<KeyBean>() {
                @Override
                public CharSequence getLabelText(TextView label, int position, KeyBean data) {
                    return data.getName();
                }
            }, R.mipmap.icon_hot_mark);
        } else {
            ll_hot.setVisibility(View.GONE);
        }
    }

    @Override
    public void getHistroySuccess(List<KeyBean> dataHistroy) {
        if (dataHistroy != null && dataHistroy.size() > 0) {
            data_histroy.clear();
            data_histroy.addAll(dataHistroy);
            rl_histroy.setVisibility(View.VISIBLE);
            labels_histroy.setLabels(dataHistroy, new LabelsView.LabelTextProvider<KeyBean>() {
                @Override
                public CharSequence getLabelText(TextView label, int position,KeyBean data) {
                    return data.getName();
                }
            });
            ViewTreeObserver vto = labels_histroy.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final int version = Build.VERSION.SDK_INT;
                    /*
                     * 移除监听器，避免重复调用
                     */
                    // 判断sdk版本，removeGlobalOnLayoutListener在API 16以后不再使用
                    if (version >= 16) {
                        vto.removeOnGlobalLayoutListener(this);
                    } else {
                        vto.removeGlobalOnLayoutListener(this);
                    }
                    if (labels_histroy.getLineCounts() > 3) {
                        tv_more_histroy.setVisibility(View.VISIBLE);
                        labels_histroy.setMaxLines(3);
                    } else {
                        labels_histroy.setMaxLines(0);
                        tv_more_histroy.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            rl_histroy.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDefautlNmae(String serNmae) {
        keyWord = serNmae;
        ed_keyword.setHint(keyWord);
    }

}
