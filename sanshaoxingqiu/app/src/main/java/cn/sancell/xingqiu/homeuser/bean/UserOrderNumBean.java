package cn.sancell.xingqiu.homeuser.bean;

/**
 * Created by ai11 on 2019/8/2.
 */

public class UserOrderNumBean {
    private int waitPayOrderCount;
    private int waitDeliveryCount;
    private int hadDeliveryCount;
    private int waitEvaluationCount;
    public int finishCount;

    public int getWaitPayOrderCount() {
        return waitPayOrderCount;
    }

    public void setWaitPayOrderCount(int waitPayOrderCount) {
        this.waitPayOrderCount = waitPayOrderCount;
    }

    public int getWaitDeliveryCount() {
        return waitDeliveryCount;
    }

    public void setWaitDeliveryCount(int waitDeliveryCount) {
        this.waitDeliveryCount = waitDeliveryCount;
    }

    public int getHadDeliveryCount() {
        return hadDeliveryCount;
    }

    public void setHadDeliveryCount(int hadDeliveryCount) {
        this.hadDeliveryCount = hadDeliveryCount;
    }

    public int getWaitEvaluationCount() {
        return waitEvaluationCount;
    }

    public void setWaitEvaluationCount(int waitEvaluationCount) {
        this.waitEvaluationCount = waitEvaluationCount;
    }
}
