package cn.sancell.xingqiu.live.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.bean.LivePlayInfo;
import cn.sancell.xingqiu.bean.StartAudienceInfo;
import cn.sancell.xingqiu.dialog.ImChatMsgInputDialog;
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener;
import cn.sancell.xingqiu.interfaces.OnChatLoginLisener;
import cn.sancell.xingqiu.live.activity.InputActivity;
import cn.sancell.xingqiu.live.base.RoomBaseFragment;
import cn.sancell.xingqiu.live.bean.InputConfig;
import cn.sancell.xingqiu.live.constant.GiftType;
import cn.sancell.xingqiu.live.constant.LiveConstant;
import cn.sancell.xingqiu.live.help.ClickLikeHelp;
import cn.sancell.xingqiu.live.help.GifHelp;
import cn.sancell.xingqiu.live.help.ImLoginHelp;
import cn.sancell.xingqiu.live.help.MarginsUtils;
import cn.sancell.xingqiu.live.interfacep.OnActivityUiLinsenr;
import cn.sancell.xingqiu.live.interfacep.OnPlayLinsenr;
import cn.sancell.xingqiu.live.interfacep.PLAY_STATE;
import cn.sancell.xingqiu.live.nim.NimContract;
import cn.sancell.xingqiu.live.nim.NimController;
import cn.sancell.xingqiu.live.nim.PublishParam;
import cn.sancell.xingqiu.live.tengxun.LivePlayerFragment;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.live.widget.extension.CouponBin;
import cn.sancell.xingqiu.live.widget.extension.GiftAttachment;
import cn.sancell.xingqiu.live.widget.extension.LikeAttachment;
import cn.sancell.xingqiu.live.widget.extension.LiveCountBin;
import cn.sancell.xingqiu.live.widget.extension.LiveLikeBin;
import cn.sancell.xingqiu.live.widget.extension.RedEnvelopeInfo;
import cn.sancell.xingqiu.util.BackPressedUtils;
import cn.sancell.xingqiu.util.SensitiveWordsUtils;
import cn.sancell.xingqiu.viewmodel.LiveViewModel;

/**
 * Created by zhukkun on 1/5/17.
 */
@SuppressLint("Registered")
public class LiveRoomFragment extends RoomBaseFragment implements NimContract.Ui, OnActivityUiLinsenr, OnPlayLinsenr, OnBackPressedLinsener {

    //各大板块容器
    private TengXunCameraPusherFragment captureFragment; //主播CaptureFragment
    private LivePlayerFragment audienceFragment; //观众   VideoPlayFragment  LivePlayerFragment
    private FrameLayout roomInfoLayout;
    private LiveRoomInfoFragment liveRoomInfoFragment; //房间信息

    private RecyclerView rl_gift_list;//礼物列表

    //聊天室相关
    private FrameLayout chatLayout;
    private ChatRoomMessageFragment chatRoomFragment;
    private NimController mNimController;

    //人员操作相关
    private ChatRoomMember current_operate_member; //当前正在操作的人员
    private ImageView iv_avatar;
    private TextView tv_nick_name;
    private Button btn_kick;
    private Button btn_mute;

    //直播结束回调布局
    private LinearLayout ll_live_finish;
    private TextView tv_finish_operate;
    private TextView tv_finish_tip;
    private Button btn_finish_back;

    //直播参数
    private boolean isAudience = true; //默认为观众
    private String roomId;
    private boolean isLivePlay = true;//是直播还是回放
    private String yxid;
    private OnPlayLinsenr mOnPlayLinsenr;
    //消息输入按钮
    private TextView tv_input_mssage;
    private boolean isShowInputMsg = false;
    //用来区分是从哪过来的，
    private String mType;
    private GifHelp mGifHelp;
    private ImChatMsgInputDialog mImChatMsgInputDialog;
    private LottieAnimationView iv_gift_icon;
    //点赞辅助类
    private ClickLikeHelp mClickLikeHelp;
    private LiveViewModel mLiveViewModel;
    //直播间ID(我们自己平台的)
    private String batchId;
    private LivePlayInfo mEmpLivePlayInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNimController = new NimController(mActivity, this);
        isLivePlay = getArguments().getBoolean(LiveConstant.IS_LIVE_PLAY, true);
        isAudience = getArguments().getBoolean(LiveConstant.IS_AUDIENCE, true);
        mType = getArguments().getString(LiveConstant.LIVE_TYPE);
        loginIm();

        BackPressedUtils.INSTANCE.bindOnBack(getActivity(), this);
    }

    private void loginIm() {//观众并且不是回放就直接初始化
        if (isAudience && isLivePlay) {//观众并且不是回放就直接初始化
            mNimController.onHandleIntent(getArguments());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLiveViewModel = ViewModelProviders.of(this).get(LiveViewModel.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_live_room;
    }

    @Override
    protected void handleIntent(Bundle intent) {
        Bundle arguments = getArguments();
        if (arguments != null) {

            roomId = arguments.getString(NimController.EXTRA_ROOM_ID);
            yxid = intent.getString(LiveConstant.YXID);
        }

    }

    /**
     * 静态方法 启动主播
     */
    public static LiveRoomFragment startLive(String roomId, PublishParam param, String batchId) {
        LiveRoomFragment mFragment = new LiveRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LiveConstant.IS_AUDIENCE, false);
        bundle.putString(NimController.EXTRA_ROOM_ID, roomId);
        bundle.putString(LiveConstant.YXID, batchId);
        bundle.putBoolean(LiveConstant.IS_LIVE_PLAY, true);
        bundle.putSerializable(PublishParam.EXTRA_PARAMS, param);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    /**
     * 静态方法 启动观众
     *
     * @param nStartAudienceInfo
     * @return
     */
    public static LiveRoomFragment startAudience(StartAudienceInfo nStartAudienceInfo) {
        LiveRoomFragment mFragment = new LiveRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LiveConstant.IS_AUDIENCE, true);
        bundle.putBoolean(LiveConstant.IS_START, nStartAudienceInfo.isStart());
        bundle.putString(NimController.EXTRA_ROOM_ID, nStartAudienceInfo.getRoomId());
        bundle.putBoolean(LiveConstant.IS_LIVE, true); //观众默认为直播, 另一个种模式为点播.
        bundle.putBoolean(LiveConstant.IS_SOFT_DECODE, nStartAudienceInfo.isSoftDecode());
        bundle.putString(LiveConstant.EXTRA_URL, nStartAudienceInfo.getUrl());
        bundle.putString(LiveConstant.YXID, nStartAudienceInfo.getBatchId());
        bundle.putInt(LiveConstant.LIVE_SUM, nStartAudienceInfo.getLiveSum());
        bundle.putBoolean(LiveConstant.IS_LIVE_PLAY, nStartAudienceInfo.isLivePaly());
        bundle.putInt(LiveConstant.LIVE_SHOW_POSTION, nStartAudienceInfo.getShowPostion());
        bundle.putString(LiveConstant.LIVE_TYPE, nStartAudienceInfo.getMType());
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void initView() {


        loadFragment(isAudience);
        iv_gift_icon = (LottieAnimationView) findViewById(R.id.iv_gift_icon);
        mClickLikeHelp = new ClickLikeHelp(getContext(), iv_gift_icon);
        mClickLikeHelp.setPlayAinJsonName("data.json");
        rl_gift_list = findView(R.id.rl_gift_list);
        //打算动画
        if (mGifHelp == null) {
            mGifHelp = new GifHelp(getContext(), rl_gift_list);
            mGifHelp.initAdapter();
        }
        chatLayout = findView(R.id.layout_chat_room);
        roomInfoLayout = findView(R.id.layout_room_info);
        tv_input_mssage = findView(R.id.tv_input_mssage);

        if (isAudience) {
            tv_input_mssage.setText("加入聊天…");
            MarginsUtils.INSTANCE.setMargins(tv_input_mssage, ScreenUtil.dip2px(12f),
                    ScreenUtil.dip2px(0f), ScreenUtil.dip2px(270f),
                    ScreenUtil.dip2px(20f));
        } else {
            tv_input_mssage.setText("和粉丝聊两句吧…");
        }
        tv_input_mssage.setOnClickListener(v -> {
            showInputPanel();
        });
        initMemberOperate();
        initFinishLiveLayout();
        if (isAudience) {
            //观众 直接显示聊天列表与底部控制栏
            onStartLivingFinished();
        }

    }

    /**
     * 根据是否为观众,加载不同的Fragment
     *
     * @param isAudience 是否为观众
     */
    private void loadFragment(boolean isAudience) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        batchId = getArguments().getString(LiveConstant.YXID);
        roomId = getArguments().getString(NimController.EXTRA_ROOM_ID);

        int postion = getArguments().getInt(LiveConstant.LIVE_SHOW_POSTION);
        if (isAudience) {//客户端
            audienceFragment = LivePlayerFragment.getInstance(this, roomId,
                    batchId, getArguments().getString(LiveConstant.EXTRA_URL),
                    getArguments().getBoolean(LiveConstant.IS_START),
                    isLivePlay, postion, mType);
            if (audienceFragment instanceof OnPlayLinsenr) {
                mOnPlayLinsenr = audienceFragment;
            }
            transaction.replace(R.id.layout_main_content, audienceFragment);
        } else {//主播端
            captureFragment = new TengXunCameraPusherFragment(this, roomId, batchId, postion);
            captureFragment.setArguments(getArguments());
            transaction.replace(R.id.layout_main_content, captureFragment);
        }

        liveRoomInfoFragment = LiveRoomInfoFragment.getInstance(isAudience);
        liveRoomInfoFragment.setArguments(getArguments());

        transaction.replace(R.id.layout_room_info, liveRoomInfoFragment);
        transaction.commit();
    }

    @Override
    public void finish() {

    }

    /**
     * 成功登入聊天室的回调
     *
     * @param roomId
     */
    @Override
    public void onEnterChatRoomSuc(final String roomId) {
        if (!isAdded()) {
            return;
        }
        Log.i("keey", "直播间登录成功");
        if (isLivePlay) {
            tv_input_mssage.setVisibility(View.VISIBLE);
        }

        chatRoomFragment = (ChatRoomMessageFragment) getChildFragmentManager().findFragmentById(R.id.chat_room_fragment);
        if (chatRoomFragment != null) {
            initChatRoomFragment();
        } else {
            // 如果Fragment还未Create完成，延迟初始化
            onEnterChatRoomSuc(roomId);
        }
    }

    /**
     * 设置礼物
     */
    private void showGift(RedEnvelopeInfo mRedEnvelopeInfo) {

        mGifHelp.addGiftInfo(mRedEnvelopeInfo);
    }

    /**
     * 初始化聊天室Fragment
     */
    private void initChatRoomFragment() {
        chatRoomFragment.init(roomId);
        chatRoomFragment.setMsgExtraDelegate(new ChatRoomMsgListPanel.ExtraDelegate() {

            @Override
            public void onReceivedCustomAttachment(ChatRoomMessage msg) {
                if (msg.getAttachment() instanceof CouponBin) {//优惠券
                    if (isAudience) {
                        audienceFragment.setShowCoupon();
                    } else {
                        captureFragment.setShowCoupon();
                    }

                } else if (msg.getAttachment() instanceof LiveLikeBin) {//直播点赞
                    if (isAudience && msg.getFromAccount().equals(LiveCache.getAccount())) {//自己的消息自己不响应
                        return;
                    }
                    LiveLikeBin mInfo = (LiveLikeBin) msg.getAttachment();
                    //播放点赞动画
                    //  mClickLikeHelp.clickLike();
                    onUpLikeSum(mInfo.likeNumber);
                } else if (msg.getAttachment() instanceof LiveCountBin) {//直播在线人数
                    LiveCountBin mInfo = (LiveCountBin) msg.getAttachment();
                    if (mEmpLivePlayInfo == null) {
                        if (isAudience) {
                            mEmpLivePlayInfo = audienceFragment.getLiveInfoData();
                        } else {
                            mEmpLivePlayInfo = captureFragment.getLiveInfoData();
                        }
                    }

                    if (mEmpLivePlayInfo != null && !TextUtils.isEmpty(mEmpLivePlayInfo.getPushFlag())) {//如果是值班详情pushFlag不为空，才去判断自定义消息里面
                        if (!TextUtils.isEmpty(mInfo.pushFlag)) {
                            onUpLiveWahtSum(mInfo.onlineUserCount);
                        }
                    } else {
                        onUpLiveWahtSum(mInfo.onlineUserCount);
                    }

                } else if (msg.getAttachment() instanceof RedEnvelopeInfo) {//红包
                    RedEnvelopeInfo mInfo = (RedEnvelopeInfo) msg.getAttachment();
                    showGift(mInfo);
                } else if (msg.getAttachment() instanceof LikeAttachment) {
                } else if (msg.getAttachment() instanceof GiftAttachment) {
                    // 收到礼物消息
                    GiftType type = ((GiftAttachment) msg.getAttachment()).getGiftType();
                } else if (msg.getAttachment() instanceof ChatRoomNotificationAttachment) {
                    liveRoomInfoFragment.onReceivedNotification((ChatRoomNotificationAttachment) msg.getAttachment());
                }
            }

            @Override
            public void onMessageClick(IMMessage imMessage) {
                if (imMessage.getMsgType() == MsgTypeEnum.text) {
                    onMemberOperate(getCurrentClickMember(imMessage.getFromAccount()));
                }
            }
        });
    }

    /**
     * 初始化直播结束布局
     */
    private void initFinishLiveLayout() {
        ll_live_finish = findView(R.id.ll_live_finish);
        tv_finish_operate = findView(R.id.tv_operate_name);
        tv_finish_tip = findView(R.id.tv_finish_tip);
        btn_finish_back = findView(R.id.btn_finish_back);
        btn_finish_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_live_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //空方法,拦截点击事件
            }
        });

    }


    /**
     * 初始化人员操作布局
     */
    private void initMemberOperate() {
        iv_avatar = findView(R.id.iv_avatar);
        tv_nick_name = findView(R.id.tv_nick_name);
        btn_kick = findView(R.id.btn_kick);
        btn_mute = findView(R.id.btn_mute);


        btn_kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(null);
                builder.setMessage("确认将此人踢出房间?");
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mNimController.kickMember(current_operate_member);
                                dismissMemberOperateLayout();
                            }
                        });
                builder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(null);
                builder.setMessage("确认将此人在该直播间" + (current_operate_member.isMuted() ? "解禁?" : " 禁言?"));
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mNimController.muteMember(current_operate_member);
                                dismissMemberOperateLayout();
                            }
                        });
                builder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });
    }


    /**
     * 正常结束直播
     */
    @Override
    public void normalFinishLive() {
        //主播发送离开房间请求
//        if (!isAudience) {
//        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BackPressedUtils.INSTANCE.unBindOnBack(getActivity(), this);

        if (mGifHelp != null) {
            mGifHelp.onDestroy();
        }
        if (mClickLikeHelp != null) {
            mClickLikeHelp.onDestroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNimController.onDestroy();
        mNimController.logoutChatRoom();
        if (captureFragment != null) {
            captureFragment.destroyController();
        }

    }

    /**
     * 根据账号获取聊天室成员对象
     *
     * @param fromAccount
     * @return
     */
    private ChatRoomMember getCurrentClickMember(String fromAccount) {
        return liveRoomInfoFragment.getMember(fromAccount);
    }

    /**
     * 刷新房间信息
     *
     * @param roomInfo
     */
    @Override
    public void refreshRoomInfo(ChatRoomInfo roomInfo) {
        liveRoomInfoFragment.refreshRoomInfo(roomInfo);
        //设置观看人数
        // onUpLiveWahtSum(roomInfo.getOnlineUserCount());
        tv_finish_operate.setText(roomInfo.getCreator());
    }

    /**
     * 刷新人员列表
     *
     * @param result
     */
    @Override
    public void refreshRoomMember(List<ChatRoomMember> result) {
        if (result == null) return;
        liveRoomInfoFragment.updateMember(result);
        //    onUpLiveWahtSum(result.size());
    }

    /**
     * 聊天室结束回调
     *
     * @param reason 结束原因
     */
    @Override
    public void onChatRoomFinished(String reason) {
        //ll_live_finish.setVisibility(View.VISIBLE);
        Log.i("keey", "聊天室结束回调:" + reason);
        mLiveViewModel.checkLiveRoomStatus(batchId).observe(this, it -> {
                    if (it.getBatchStatus() == 2) {////2 直播中 3已结束 1预告 4 时间段内未直播 5.急停
                        //在从新登陆聊天室
                        loginIm();
                    } else {
                        liveEnd();
                    }
                }
        );

    }

    /**
     * 直播结束
     */
    private void liveEnd() {

        //隐藏聊天输入框
        tv_input_mssage.setVisibility(View.GONE);
        if (mOnPlayLinsenr != null) {
            mOnPlayLinsenr.onLiveEndPlay();
        }
        if (!isAudience) {//如果是主播端
            captureFragment.onLiveEndPlay();
        }

        if (isAudience && audienceFragment != null) {
            // audienceFragment.stopWatching();
        }
    }

    /**
     * 点击人员时的回调
     *
     * @param member
     */
    @Override
    public void onMemberOperate(ChatRoomMember member) {
        //主播显示人员操作面板
        if (member != null && !isAudience) {
            current_operate_member = member;


            tv_nick_name.setText(member.getNick());
            if (member.isMuted()) {
                btn_mute.setText("解禁");
            } else {
                btn_mute.setText("禁言");
            }
            if (isAudience) {
                btn_mute.setEnabled(false);
                btn_kick.setEnabled(false);
            }
        }
    }

    @Override
    public void onStartLivePlay() {
        if (!isAudience) {
            mNimController.onHandleIntent(getArguments());
        }

    }

    @Override
    public void onShowStreamline(boolean isShow) {
        if (isLivePlay) {//如果是直播才有效果
            if (isShow) {//精简模式
                chatLayout.setVisibility(View.GONE);
                tv_input_mssage.setVisibility(View.GONE);
            } else {
                chatLayout.setVisibility(View.VISIBLE);
                tv_input_mssage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideSendMsg() {
        if (tv_input_mssage != null) {
            tv_input_mssage.setVisibility(View.GONE);
        }
    }

    @Override
    public void isShowStreamline(boolean isShow) {
        if (tv_input_mssage == null) {
            return;
        }
        if (isShow) {
            tv_input_mssage.setVisibility(View.VISIBLE);
        } else {
            tv_input_mssage.setVisibility(View.GONE);
        }

    }

    @Override
    public void inputMsg() {
        showInputPanel();
    }

    /**
     * 隐藏人员操作布局
     */
    @Override
    public void dismissMemberOperateLayout() {
    }

    /**
     * 直播开始完成的回调
     */
    @Override
    public void onStartLivingFinished() {
        if (isLivePlay) {
            chatLayout.setVisibility(View.VISIBLE);
            tv_input_mssage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 直播断开时的回调
     */
    @Override
    public void onLiveDisconnect() {
        chatLayout.setVisibility(View.GONE);
        roomInfoLayout.setVisibility(View.GONE);
    }

    /**
     * 显示聊天输入布局 展开键盘
     */
    @Override
    public void showInputPanel() {
        setIsInputSendMsg(true);
        startInputActivity();
    }

    /**
     * 设置是否进入了
     *
     * @param isShowInputMsg
     */
    public void setIsInputSendMsg(boolean isShowInputMsg) {
        this.isShowInputMsg = isShowInputMsg;
    }

    /**
     * ***************************** 部分机型键盘弹出会造成布局挤压的解决方案 ***********************************
     */
    private InputConfig inputConfig = new InputConfig(false, false, false);
    private String cacheInputString = "";

    private void startInputActivity() {
        //showGift(new RedEnvelopeInfo());
        if (mImChatMsgInputDialog == null) {
            mImChatMsgInputDialog = new ImChatMsgInputDialog(getActivity());
            mImChatMsgInputDialog.setOnSendChatMsgLinsener(text -> {
                if (chatRoomFragment == null) {
                    Log.i("keey", "chatRoomFragment为空");
                    ImLoginHelp.INSTANCE.imLogin(new OnChatLoginLisener() {
                        @Override
                        public void onLoginSucess() {
                            //在从新登陆聊天室
                            loginIm();
                        }

                        @Override
                        public void onLoginError(@NotNull String error) {
                            ToastHelper.showToast("请重新登陆在试试");

                        }
                    });
                    return;
                }
                //这里清除缓存，去拿拿最新的数据
                LiveCache.setUserInfo(null);
                String name = LiveCache.getUserInfo().getName();
                if (SensitiveWordsUtils.contains(name)) {
                    ToastHelper.showToast("你的名字不合法，请修改名称后在发送消息");
                    return;
                }

                String msg = SensitiveWordsUtils.replaceSensitiveWord(text, '*');
                // String msg = SensitiveWordsUtil.INSTANCE.sensitiveWords(text);
                chatRoomFragment.onTextMessageSendButtonPressed(msg);
            });
        }
        if (mImChatMsgInputDialog.isShowing()) {
            mImChatMsgInputDialog.dismiss();
        }

        mImChatMsgInputDialog.show();
//        InputActivity.startActivityForResult(this, cacheInputString,
//                inputConfig, text -> {
//                    if (chatRoomFragment == null) {
//                        Log.i("keey", "chatRoomFragment为空");
//                        return;
//                    }
//                    String msg = SensitiveWordsUtils.replaceSensitiveWord(text, '*');
//                    // String msg = SensitiveWordsUtil.INSTANCE.sensitiveWords(text);
//                    chatRoomFragment.onTextMessageSendButtonPressed(msg);
//                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InputActivity.REQ_CODE) {
            setIsInputSendMsg(false);
            if (resultCode == Activity.RESULT_OK) {
                // 设置EditText显示的内容
                cacheInputString = data.getStringExtra(InputActivity.EXTRA_TEXT);
            }
        }

    }

    @Override
    public void showTextToast(String text) {
        showToast(text);
    }

    @Override
    public void onBaseStart() {
        Log.i("keey", "onBaseStart");
        if (mOnPlayLinsenr != null) {
            mOnPlayLinsenr.onBaseStart();
        }
    }

    @Override
    public void onBaseStop() {
        Log.i("keey", "onBaseStop");
        if (mOnPlayLinsenr != null) {
            mOnPlayLinsenr.onBaseStop();
        }
    }

    @NotNull
    @Override
    public PLAY_STATE onBasePlayStates() {
        if (mOnPlayLinsenr != null) {
            return mOnPlayLinsenr.onBasePlayStates();
        }
        return PLAY_STATE.PLAYT;
    }

    @Override
    public boolean onBackPressedLinsener() {
        if (isAudience) {
            audienceFragment.onPagerCloce();
        } else {
            captureFragment.onPagerCloce();
        }
        return true;
    }


    @Override
    public void onLiveEndPlay() {

    }

    /**
     * 点赞人数
     *
     * @param sum
     */
    private void onUpLikeSum(int sum) {
        if (!isLivePlay) {
            return;
        }
        if (isAudience) {
            audienceFragment.onUpLikeSum(sum);
        } else {
            captureFragment.onUpLikeSum(sum);
        }
    }

    /**
     * 设置观看人数
     *
     * @param sum
     */
    private void onUpLiveWahtSum(int sum) {
        if (!isLivePlay) {
            return;
        }
        if (isAudience) {
            audienceFragment.onUpLiveWahtSum(sum);
        } else {
            captureFragment.onUpLiveWahtSum(sum);
        }
    }

    @Override
    public void onSetPlayStatus(boolean isPlay) {
        if (isAudience) {
            if (audienceFragment != null) {
                audienceFragment.onSetViewPlayStatus(isPlay);

            }
        } else {
            if (captureFragment != null) {
                captureFragment.onSetViewPlayStatus(isPlay);

            }
        }
    }
}
