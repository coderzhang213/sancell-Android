package cn.sancell.xingqiu.constant.network;

import java.lang.reflect.Method;

/**
 * Created by zj on 2019/12/16.
 */
public class NetWorkStateReceiverMethod {
    /**
     * 网络改变执行的方法
     */
    Method method;
    /**
     * 网络改变执行的方法所属的类
     */
    Object object;
    /**
     * 监听的网络改变类型
     */
    NetWorkState[] netWorkState = {NetWorkState.GPRS, NetWorkState.WIFI, NetWorkState.NONE};

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public NetWorkState[] getNetWorkState() {
        return netWorkState;
    }

    public void setNetWorkState(NetWorkState[] netWorkState) {
        this.netWorkState = netWorkState;
    }
}
