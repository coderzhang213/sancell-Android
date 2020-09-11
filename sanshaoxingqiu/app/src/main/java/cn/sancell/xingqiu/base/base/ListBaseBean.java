package cn.sancell.xingqiu.base.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huyingying on 2019/6/8.
 */

public class ListBaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int dataCount;

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }
}
