package cn.sancell.xingqiu.usermember.bean;

/**
 * Created by ai11 on 2019/9/23.
 */

public class MemberVipGiftCardBean {

    public MemberVipGiftCardBean() {

    }

    private int memberLevel;
    private int cardLevel;
    private int memberLevelStrResourceId;
    private int memberLevelTextColorId;
    private int picResourceId;
    private int statusBgResourceId;

    private String memberLevelName;

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getMemberLevelTextColorId() {
        return memberLevelTextColorId;
    }

    public void setMemberLevelTextColorId(int memberLevelTextColorId) {
        this.memberLevelTextColorId = memberLevelTextColorId;
    }

    public int getPicResourceId() {
        return picResourceId;
    }

    public void setPicResourceId(int picResourceId) {
        this.picResourceId = picResourceId;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public int getMemberLevelStrResourceId() {
        return memberLevelStrResourceId;
    }

    public void setMemberLevelStrResourceId(int memberLevelStrResourceId) {
        this.memberLevelStrResourceId = memberLevelStrResourceId;
    }

    public int getStatusBgResourceId() {
        return statusBgResourceId;
    }

    public void setStatusBgResourceId(int statusBgResourceId) {
        this.statusBgResourceId = statusBgResourceId;
    }
}
