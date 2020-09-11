package cn.sancell.xingqiu.im.ui.joinTeam;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm;
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.util.EToast2;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 加入群组
 * @time 2019-11-14 20:17
 */
public class JoinTeamActivity extends BaseMVPToobarActivity<JoinTeamPresenter> implements JoinTeamView {

    @BindView(R.id.iv_head_image)
    AppCompatImageView iv_head_image;
    @BindView(R.id.tv_team_name)
    AppCompatTextView tv_team_name;
    @BindView(R.id.team_type)
    AppCompatTextView team_type;
    @BindView(R.id.apply_join)
    AppCompatButton apply_join;
    @BindView(R.id.member_count)
    AppCompatTextView member_count;
    @BindView(R.id.tv_team_num)
    AppCompatTextView tv_team_num;

    private String mTeamId;
    private Team mTeamInfo;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent(context, JoinTeamActivity.class);
        intent.putExtra(UiHelper.TEAM_ID, teamId);
        context.startActivity(intent);
    }

    @Override
    protected JoinTeamPresenter createPresenter() {
        return new JoinTeamPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_join_team;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle(getResources().getString(R.string.search_join_team));
        mTeamId = getIntent().getStringExtra(UiHelper.TEAM_ID);
        fetchTeamById();
        apply_join.setOnClickListener(v -> {
            joinTeam();
        });

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    /**
     * 获取群信息
     */
    public void fetchTeamById() {
        NIMClient.getService(TeamService.class).queryTeam(mTeamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team param) {
                if (param == null) {
                    EToast2.makeText(JoinTeamActivity.this, R.string.team_not_exist, EToast2.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                //绑定群数据
                mTeamInfo = param;
                apply_join.setEnabled(true);
                tv_team_name.setText(param.getName());
                member_count.setText(String.format(getResources().getString(R.string.team_member_count), param.getMemberCount()));
                tv_team_num.setText(param.getId());
                Glide.with(JoinTeamActivity.this).load(param.getIcon()).into(iv_head_image);
                if (param.isMyTeam()) {
                    apply_join.setText(getResources().getString(R.string.team_already_join));
                } else {
                    if (param.getVerifyType() == VerifyTypeEnum.Private) {
                        apply_join.setText(getResources().getString(R.string.team_can_not_join));
                    } else {
                        apply_join.setText(getResources().getString(R.string.team_apply_to_join));
                    }
                }

            }

            @Override
            public void onFailed(int code) {
                EToast2.makeText(JoinTeamActivity.this, "群组查询异常", EToast2.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                EToast2.makeText(JoinTeamActivity.this, "网络异常", EToast2.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加入群聊
     */
    public void joinTeam() {
        if (mTeamInfo != null) {
            if (mTeamInfo.isMyTeam()) {
                ScClient.enterTeamChat(this, mTeamInfo.getId());
            } else {

                ScClient.applyJoinWithVer(this, mTeamId, PreferencesUtils.getString(Constants.Key.key_im_accid, ""), new TeamApplyListener() {
                    @Override
                    public void showInputDialog() {
                        ApplyJoinTeamInputDialogFgm dialogFgm = ApplyJoinTeamInputDialogFgm.newInstance();
                        dialogFgm.setOnApplyListener(str -> sendReq(str));
                        dialogFgm.show(getSupportFragmentManager(), "apply");
                    }

                    @Override
                    public void onSuccess(Object o) {
                        sendReq("");
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });


//                if (mTeamInfo.getVerifyType() == VerifyTypeEnum.Private) {
//                    SCApp.getInstance().showSystemCenterToast("无法加入该群");
//                } else if (mTeamInfo.getVerifyType() == VerifyTypeEnum.Apply) {
//                    //弹出申请
//                    ApplyJoinTeamInputDialogFgm dialogFgm = ApplyJoinTeamInputDialogFgm.newInstance();
//                    dialogFgm.setOnApplyListener(str -> sendReq(str));
//                    dialogFgm.show(getSupportFragmentManager(), "apply");
//                } else {
//                    sendReq("");
//                }
            }

        }
    }

    /**
     * 发送请求
     *
     * @param str
     */
    public void sendReq(String str) {


        ScClient.applyJoinTeam(this, mTeamInfo.getId(), str, PreferencesUtils.getString(Constants.Key.key_im_accid, ""), null);


//        NIMClient.getService(TeamService.class).applyJoinTeam(mTeamInfo.getId(), str).setCallback(new RequestCallback<Team>() {
//            @Override
//            public void onSuccess(Team team) {
//                apply_join.setEnabled(false);
//                SCApp.getInstance().showSystemCenterToast(getString(R.string.team_join_success, team.getName()));
//                //进入群聊
//                ScClient.joinGroup(JoinTeamActivity.this,mTeamInfo.getId(), PreferencesUtils.getString(Constants.Key.key_im_accid,""),null);
//                ScClient.enterTeamChat(JoinTeamActivity.this, team.getId());
//            }
//
//            @Override
//            public void onFailed(int code) {
//                //仅仅是申请成功
//                if (code == ResponseCode.RES_TEAM_APPLY_SUCCESS) {
//                    apply_join.setEnabled(false);
//                    SCApp.getInstance().showSystemCenterToast(R.string.team_apply_to_join_send_success);
//
//                } else if (code == ResponseCode.RES_TEAM_ALREADY_IN) {
//                    apply_join.setEnabled(false);
//                    SCApp.getInstance().showSystemCenterToast(R.string.has_exist_in_team);
//                } else if (code == ResponseCode.RES_TEAM_LIMIT) {
//                    apply_join.setEnabled(false);
//                    SCApp.getInstance().showSystemCenterToast(R.string.team_num_limit);
//                } else {
//                    SCApp.getInstance().showSystemCenterToast("加入群聊失败");
//                }
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//                SCApp.getInstance().showSystemCenterToast(getString(R.string.error_network));
//            }
//        });
    }

}
