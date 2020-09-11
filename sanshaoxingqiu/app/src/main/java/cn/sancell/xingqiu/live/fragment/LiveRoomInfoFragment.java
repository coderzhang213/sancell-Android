package cn.sancell.xingqiu.live.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;

import java.util.HashMap;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.im.entity.LiveCountInfo;
import cn.sancell.xingqiu.live.adapter.MemberAdapter;
import cn.sancell.xingqiu.live.base.BaseLiveFragment;
import cn.sancell.xingqiu.live.constant.LiveConstant;
import cn.sancell.xingqiu.live.interfacep.OnActivityUiLinsenr;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.live.utils.TitlemarginUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.RxTimerUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhukkun on 1/9/17.
 */
public class LiveRoomInfoFragment extends BaseLiveFragment {

    public static final String EXTRA_IS_AUDIENCE = "is_audence";

    public static LiveRoomInfoFragment getInstance(boolean isAudience) {
        LiveRoomInfoFragment fragment = new LiveRoomInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_AUDIENCE, isAudience);
        fragment.setArguments(bundle);
        return fragment;
    }

    boolean isAudience;
    TextView tvOnlineCount;
    TextView tvRoomName;
    TextView tvMasterName;

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    int onlineCount;
    private int myLiveSum = 0;
    private String roomId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            Bundle arguments = parentFragment.getArguments();
            if (arguments != null) {
                isAudience = arguments.getBoolean(EXTRA_IS_AUDIENCE, true);
            } else {
                isAudience = true;
            }

        } else {
            isAudience = true;
        }

        return inflater.inflate(isAudience ? R.layout.layout_live_audience_room_info : R.layout.layout_live_captrue_room_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            roomId = arguments.getString("yxId");
        }
        initView();

        //每隔10秒去更新在线人数


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        tvOnlineCount = findView(R.id.online_count_text);
        tvRoomName = findView(R.id.room_name);
        recyclerView = findView(R.id.rv_room_member);
        findViewById(R.id.player_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {//获取直播人数
            myLiveSum = arguments.getInt(LiveConstant.LIVE_SUM, 0);
        }
        //全屏所以需要设置距离顶部的高度
        RelativeLayout rl_close_par = (RelativeLayout) findViewById(R.id.rl_close_par);
        TitlemarginUtils.setTitleTop(getContext(), rl_close_par);
        initRecycleView();
        if (isAudience) {
            tvMasterName = findView(R.id.master_name);
        }
    }

    private void initRecycleView() {
        memberAdapter = new MemberAdapter(getContext());
        recyclerView.setAdapter(memberAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        memberAdapter.setOnItemClickListener(new MemberAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ChatRoomMember member) {
                FragmentActivity activity = getActivity();
                if (activity instanceof OnActivityUiLinsenr) {
                    ((OnActivityUiLinsenr) activity).onMemberOperate(member);
                }

            }
        });
    }


    public void updateMember(List<ChatRoomMember> members) {
        memberAdapter.updateMember(members);
        onlineCount = members.size();
        //tvOnlineCount.setText(myLiveSum + "人观看");
    }

    public void refreshRoomInfo(ChatRoomInfo roomInfo) {
        onlineCount = roomInfo.getOnlineUserCount();
        //tvOnlineCount.setText(myLiveSum + "人观看");
        tvRoomName.setText(roomInfo.getRoomId());
        if (isAudience) {
            tvMasterName.setText(roomInfo.getCreator());
        }
    }

    public void onReceivedNotification(ChatRoomNotificationAttachment attachment) {

        ChatRoomMember chatRoomMember = new ChatRoomMember();
        chatRoomMember.setAccount(attachment.getTargets().get(0));
        chatRoomMember.setNick(attachment.getTargetNicks().get(0));

        if (!isAudience && chatRoomMember.getAccount().equals(LiveCache.getAccount())) {
            //主播的通知(主播进入房间,主播离开房间)不做处理,
            return;
        }

        switch (attachment.getType()) {
            case ChatRoomMemberIn:
                if (memberAdapter.addMember(chatRoomMember)) {
                    //  tvOnlineCount.setText(++myLiveSum + "人观看");
                }
                break;
            case ChatRoomMemberExit:
            case ChatRoomMemberKicked:
                memberAdapter.removeMember(chatRoomMember);
                // tvOnlineCount.setText(--myLiveSum + "人观看");
                break;
            case ChatRoomMemberMuteAdd:
                chatRoomMember.setMuted(true);
                memberAdapter.updateSingleMember(chatRoomMember);
                break;
            case ChatRoomMemberMuteRemove:
                chatRoomMember.setMuted(false);
                memberAdapter.updateSingleMember(chatRoomMember);
                break;
        }
    }

    public void updateMemberState(ChatRoomMember current_operate_member) {
        memberAdapter.updateSingleMember(current_operate_member);
    }

    public ChatRoomMember getMember(String fromAccount) {
        return memberAdapter.getMember(fromAccount);
    }
}
