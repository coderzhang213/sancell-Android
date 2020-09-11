package cn.sancell.xingqiu.base.entity;

/**
 * Created by huyingying
 */
public abstract class BaseEntity {

    /**
     * 重新登录的状态码
     */
    protected static final int NEED_RE_LOGIN_STATUS_CODE = 10008;

    public abstract boolean isSuccess();

    /**
     * 当前的账号有可能会被踢下来，因此服务器会返回一个状态码10008代表账号别在别处登录了
     * @return
     */
    public abstract boolean isNeedReLogin();

}
