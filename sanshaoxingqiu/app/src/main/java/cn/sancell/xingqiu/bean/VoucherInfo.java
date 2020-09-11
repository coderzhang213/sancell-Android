package cn.sancell.xingqiu.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class VoucherInfo implements MultiItemEntity, Serializable {

    public String id;
    public String couponCode;
    public String name;
    public String desc;
    public String getNumTimes;
    public int getUserType;
    public String useLimitTimes;
    public int useType;
    public int sendType;
    public int usedTimeType;
    public String receiveLimitUseDay;
    public long fixedUseBeginTime;
    public long fixedUseEndTime;
    public int type;
    public long faceValueE2;
    public long limitMinUseMoneyE2;
    public long discountE2;
    public int status;
    public String sendTime;
    public String createdAt;
    public String updatedAt;
    public String couponReceiveId;
    public int showUseStatus; //1已使用；3已过期

    //local
    public int voucherType;
    public boolean show;

    @Override
    public int getItemType() {
        return voucherType;
    }
}
