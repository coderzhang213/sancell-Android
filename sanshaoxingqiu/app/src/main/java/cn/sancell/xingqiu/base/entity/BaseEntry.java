package cn.sancell.xingqiu.base.entity;


/**
 * @author: huyingying.
 * @date: 2018/7/25
 * @description:
 */

public class BaseEntry<T> {

    private int retCode;
    private String retMsg;
    private T retData;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getRetData() {
        return retData;
    }

    public void setRetData(T retData) {
        this.retData = retData;
    }
}
