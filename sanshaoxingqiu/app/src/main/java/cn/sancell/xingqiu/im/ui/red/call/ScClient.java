package cn.sancell.xingqiu.im.ui.red.call;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.net.Authenticator;
import java.util.Random;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.bean.CheckTeamReq;
import cn.sancell.xingqiu.im.dialog.listener.CheckRpListener;
import cn.sancell.xingqiu.im.entity.req.JoinGroupReq;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;
import cn.sancell.xingqiu.im.sys.LogoutHelper;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.im.ui.ImModel;
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener;
import cn.sancell.xingqiu.im.ui.red.RpDetail.RpDetailActivity;
import cn.sancell.xingqiu.live.nim.NimContract;

/**
 * @author Alan_Xiong
 * @desc: 网易/红包的服务
 * @time 2019-11-19 09:43
 */
public class ScClient {

    /**
     * 检查红包有效性
     *
     * @param rpId
     * @param activity
     */
    static void checkRp(String rpId, Activity activity, CheckRpListener listener) {

        RpGrabReq req = new RpGrabReq();
        req.redId = rpId;

        ImModel.getInstance().checkRp(req).compose(RxUtils.transform()).subscribe(new BaseObserver<CheckRpRes>(activity) {
            @Override
            protected void onSuccess(BaseEntry<CheckRpRes> t) {
                CheckRpRes res = t.getRetData();
                if (res.isGet == 0) {
                    //未抢
                    // grabRp(rpId, activity, listener);
                    if (listener != null) {
                        listener.rpIsGrape(false);
                    }

                } else {
                    //抢了 - 红包详情
                    if (listener != null) {
                        listener.rpIsGrape(true);
                    }
                    RpDetailActivity.start(activity, rpId);
                }

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                if (listener != null) {
                    listener.rpIsGrape(false);
                }
                SCApp.getInstance().showSystemCenterToast(e.toString());
            }
        });

    }


    /**
     * 抢红包
     *
     * @param rpId
     * @param activity
     */
    public static void grabRp(String rpId, Activity activity, CheckRpListener listener) {
        RpGrabReq req = new RpGrabReq();
        req.redId = rpId;
        new Handler().postDelayed(() -> ImModel.getInstance().rpGrab(req).compose(RxUtils.transform()).subscribe(new BaseObserver<RpGrabRes>(activity) {
            @Override
            protected void onSuccess(BaseEntry<RpGrabRes> t) throws Exception {
                if (listener != null) {

                    if (t.getRetCode() == 0) {
                        //成功
                        listener.rpIsGrape(true);
                        RpDetailActivity.start(activity, rpId);
                    } else {
                        listener.rpIsGrape(false);
                        SCApp.getInstance().showSystemCenterToast(t.getRetMsg());
                    }
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                if (listener != null) {
                    listener.rpIsGrape(false);
                }
                SCApp.getInstance().showSystemCenterToast(e.toString());
            }
        }),timeRandom());
    }


    /**
     * own server interface 申请入群
     *
     * @param context
     * @param teamId
     */
    private static void joinGroup(Context context, String teamId, String accid, RequestCallback mCallBack) {
        JoinGroupReq req = new JoinGroupReq();
        req.tid = teamId;
        req.accid = accid;
        ImModel.getInstance().joinTeam(req).compose(RxUtils.transform())
                .subscribe(new BaseObserver<EmptyBean>(context) {
                    @Override
                    protected void onSuccess(BaseEntry<EmptyBean> t) throws Exception {
                        if (t.getRetCode() == 0) {
                            if (mCallBack != null) {
                                mCallBack.onSuccess(null);
                            }

                        } else {
                            if (mCallBack != null) {
                                mCallBack.onFailed(t.getRetCode());
                            }
                            SCApp.getInstance().showSystemCenterToast(t.getRetMsg());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        SCApp.getInstance().showSystemCenterToast(e.toString());
                        if (mCallBack != null) {
                            mCallBack.onFailed(500);
                        }

                    }
                });
    }

    /**
     * 查询群组状态，在申请加群
     *
     * @param context
     * @param teamId
     * @param accId
     */
    public static void applyJoinWithVer(Context context, String teamId, String accId, TeamApplyListener listener) {
        NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                if (team.getVerifyType() == VerifyTypeEnum.Private) {
                    SCApp.getInstance().showSystemCenterToast("无法加入该群");
                } else if (team.getVerifyType() == VerifyTypeEnum.Apply) {

                    //检查是否加过其他群组，特定的群组限制
                    checkInOtherTeam(context, teamId,listener);

                } else {
                    applyJoinTeam(context, teamId, null, accId, listener);
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }


    /**
     * 同意加入群聊
     *
     * @param context
     * @param teamId
     * @param accId
     * @param callback
     */
    public static void passApply(Context context, String teamId, String accId, RequestCallback callback) {

        NIMClient.getService(TeamService.class).passApply(teamId, accId).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                joinGroup(context, teamId, accId, callback);
                enterTeamChat(context, teamId);
            }

            @Override
            public void onFailed(int i) {
                if (i == ResponseCode.RES_TEAM_ALREADY_IN) {
                    SCApp.getInstance().showSystemCenterToast(R.string.has_exist_in_team);
                } else {
                    SCApp.getInstance().showSystemCenterToast("加入群聊失败");
                }
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }


    /**
     * 申请加入群聊
     *
     * @param context
     * @param teamId
     */
    public static void applyJoinTeam(Context context, String teamId, String desc, String accId, RequestCallback mCallBack) {

        NIMClient.getService(TeamService.class).applyJoinTeam(teamId, desc).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                //加入成功
                SCApp.getInstance().showSystemCenterToast(context.getResources().getString(R.string.team_join_success, team.getName()));
                joinGroup(context, teamId, accId, mCallBack);
                enterTeamChat(context, teamId);
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_APPLY_SUCCESS) {
                    SCApp.getInstance().showSystemCenterToast(R.string.team_apply_to_join_send_success);
                } else if (code == ResponseCode.RES_TEAM_ALREADY_IN) {
                    SCApp.getInstance().showSystemCenterToast(R.string.has_exist_in_team);
                } else if (code == ResponseCode.RES_TEAM_LIMIT) {
                    SCApp.getInstance().showSystemCenterToast(R.string.team_num_limit);
                } else if (code == ResponseCode.RES_TEAM_ECOUNT_LIMIT) {
                    SCApp.getInstance().showSystemCenterToast("群人数已达上限");
                } else {
                    SCApp.getInstance().showSystemCenterToast("加入群聊失败");
                }
            }

            @Override
            public void onException(Throwable throwable) {
                SCApp.getInstance().showSystemCenterToast(context.getString(R.string.error_network));
            }
        });
    }

    /**
     * 进入群聊
     *
     * @param context
     * @param teamId
     */
    public static void enterTeamChat(Context context, String teamId) {
        SessionHelper.startTeamSession(context, teamId);
    }

    /**
     * 退出登陆
     */
    public static void loginOut() {
        NIMClient.getService(AuthService.class).logout();
        LogoutHelper.logout();
    }

    private static void checkInOtherTeam(Context context, String teamId,TeamApplyListener listener) {
        CheckTeamReq req = new CheckTeamReq();
        req.tid = teamId;
        ImModel.getInstance().checkInOtherGroup(req).compose(RxUtils.transform()).subscribe(new BaseObserver<EmptyBean>(context) {
            @Override
            protected void onSuccess(BaseEntry<EmptyBean> t) throws Exception {
                if (t.getRetCode() == 0) {
                    if (listener != null) {
                        listener.showInputDialog();
                    }
                } else {
                    SCApp.getInstance().showSystemCenterToast("已经加入其他群，当前群限制无法加入");
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                SCApp.getInstance().showSystemCenterToast(e.toString());
            }
        });
    }

    //最大的毫秒
    private static int timeRandom() {
        int max = 100;
        int min = 10;
        Random random = new Random();

        return random.nextInt(max) % (max - min + 1) + min;
    }


}
