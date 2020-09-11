package cn.sancell.xingqiu.im.ui.findTeam;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.ui.joinTeam.JoinTeamActivity;
import cn.sancell.xingqiu.im.widget.ClearableEditTextWithIcon;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
  * @author Alan_Xiong
  *
  * @desc: 查找群信息
  * @time 2019-11-14 23:56
  */
public class FindTeamActivity extends BaseMVPToobarActivity<FindTeamPresenter> implements FindTeamView {

    @BindView(R.id.et_team_search)
    ClearableEditTextWithIcon et_team_search;
    @BindView(R.id.btn_search)
    AppCompatButton btn_search;


    public static void start(Context context) {
        Intent intent = new Intent(context, FindTeamActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected FindTeamPresenter createPresenter() {
        return new FindTeamPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_find_team;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("搜索");
        btn_search.setOnClickListener(v -> queryTeamById());
        btn_search.setEnabled(false);
        et_team_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    btn_search.setEnabled(true);
                }else{
                    btn_search.setEnabled(false);
                }

            }
        });
    }


    @Override
    protected BaseView getView() {
        return this;
    }

    /**
     * 根据id查询群组信息
     */
    private void queryTeamById() {
        NIMClient.getService(TeamService.class).searchTeam(et_team_search.getText().toString()).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                updateTeamInfo(team);
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_ENOTEXIST) {
                    SCApp.getInstance().showSystemCenterToast(getResources().getString(R.string.team_number_not_exist));
                } else {
                    SCApp.getInstance().showSystemCenterToast(getResources().getString(R.string.error_network));
                }
            }

            @Override
            public void onException(Throwable exception) {
                SCApp.getInstance().showSystemCenterToast(getResources().getString(R.string.error_network));
            }
        });
    }
    /**
     * 搜索群组成功的回调
     *
     * @param team 群
     */
    private void updateTeamInfo(Team team) {
        if (team.getId().equals(et_team_search.getText().toString())) {
            JoinTeamActivity.start(this, team.getId());
        }

    }
}
