package cn.sancell.xingqiu.live.interfacep;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

public interface OnActivityUiLinsenr {
    void showInputPanel();
    void onStartLivingFinished();
    void onLiveDisconnect();
    void normalFinishLive();
    void onMemberOperate(ChatRoomMember member);
    void onStartLivePlay();
    //是否精简模式
    void onShowStreamline(boolean isShow);
    void hideSendMsg();
    //简洁模式
    void isShowStreamline(boolean isShow);
    void inputMsg();
}
