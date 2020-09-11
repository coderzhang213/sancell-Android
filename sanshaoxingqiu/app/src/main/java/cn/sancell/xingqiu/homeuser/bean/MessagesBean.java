package cn.sancell.xingqiu.homeuser.bean;

/**
 * Created by ai11 on 2019/8/22.
 */

public class MessagesBean {
    private MessageRedPacketListBean.MessageRedPacketBean userPointNoticeInfo;
    private MessageNoticeListBean.MessageNoticeBean noticeInfo;
    private MessageTransactionLogisticsListBean.TransactionLogisticsBean logisticsNoticeInfo;

    public MessageRedPacketListBean.MessageRedPacketBean getUserPointNoticeInfo() {
        return userPointNoticeInfo;
    }

    public void setUserPointNoticeInfo(MessageRedPacketListBean.MessageRedPacketBean userPointNoticeInfo) {
        this.userPointNoticeInfo = userPointNoticeInfo;
    }

    public MessageNoticeListBean.MessageNoticeBean getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(MessageNoticeListBean.MessageNoticeBean noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public MessageTransactionLogisticsListBean.TransactionLogisticsBean getLogisticsNoticeInfo() {
        return logisticsNoticeInfo;
    }

    public void setLogisticsNoticeInfo(MessageTransactionLogisticsListBean.TransactionLogisticsBean logisticsNoticeInfo) {
        this.logisticsNoticeInfo = logisticsNoticeInfo;
    }
}
