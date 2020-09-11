package cn.sancell.xingqiu.base.base;

import java.io.Serializable;

public class BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean success = true;
    public String code;
    public String msg;
    public int status;// 0 成功
    public String traceId;
    //	public String timestamp;
    public String data;

}
